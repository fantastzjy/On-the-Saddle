package net.lab1024.sa.admin.module.business.payment.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.payment.config.WechatPayConfig;
import net.lab1024.sa.admin.module.business.payment.dao.PaymentRecordDao;
import net.lab1024.sa.admin.module.business.payment.domain.entity.PaymentRecordEntity;
import net.lab1024.sa.admin.module.business.payment.domain.form.WechatPayCreateForm;
import net.lab1024.sa.admin.module.business.payment.domain.vo.WechatPayCreateVO;
import net.lab1024.sa.admin.module.business.payment.util.MockDataGenerator;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Mock微信支付服务
 * 
 * @author 1024创新实验室：开云
 * @since 2024-12-30
 */
@Slf4j
@Service
public class MockWechatPaymentService {

    @Resource
    private WechatPayConfig wechatPayConfig;

    @Resource
    private PaymentRecordDao paymentRecordDao;

    @Resource
    private WechatPayCallbackService wechatPayCallbackService;

    /**
     * 创建Mock支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<WechatPayCreateVO> createMockPayment(WechatPayCreateForm form) {
        try {
            log.info("🎭 [Mock支付] 开始创建模拟支付订单，订单号：{}, 金额：{}元", 
                    form.getOrderNo(), form.getAmount());

            // 模拟网络延迟
            MockDataGenerator.simulateNetworkDelay(wechatPayConfig.getMock().getDelaySeconds());

            // 1. 生成Mock支付单号
            String paymentNo = generateMockPaymentNo();

            // 2. 创建支付记录
            PaymentRecordEntity paymentRecord = createMockPaymentRecord(form, paymentNo);
            paymentRecordDao.insert(paymentRecord);

            // 3. 生成Mock支付参数
            WechatPayCreateVO result = MockDataGenerator.generateMockPayParams(paymentNo);

            log.info("🎭 [Mock支付] Mock支付订单创建成功，支付单号：{}", paymentNo);

            // 4. 如果启用自动回调，则异步触发回调
            if (Boolean.TRUE.equals(wechatPayConfig.getMock().getAutoCallback())) {
                triggerMockCallbackAsync(paymentNo, wechatPayConfig.getMock().getCallbackDelay());
            }

            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("🎭 [Mock支付] 创建Mock支付订单失败", e);
            return ResponseDTO.userErrorParam("Mock支付创建失败：" + e.getMessage());
        }
    }

    /**
     * 查询Mock支付状态
     */
    public ResponseDTO<String> queryMockPayment(String paymentNo) {
        try {
            log.info("🎭 [Mock支付] 查询Mock支付状态，支付单号：{}", paymentNo);

            // 模拟网络延迟
            MockDataGenerator.simulateNetworkDelay(1);

            PaymentRecordEntity paymentRecord = paymentRecordDao.selectByPaymentNo(paymentNo);
            if (paymentRecord == null) {
                return ResponseDTO.userErrorParam("支付记录不存在");
            }

            String statusText = getPaymentStatusText(paymentRecord.getPaymentStatus());
            log.info("🎭 [Mock支付] 支付状态查询成功，状态：{}", statusText);

            return ResponseDTO.ok(statusText);

        } catch (Exception e) {
            log.error("🎭 [Mock支付] 查询Mock支付状态失败", e);
            return ResponseDTO.userErrorParam("查询支付状态失败：" + e.getMessage());
        }
    }

    /**
     * Mock退款申请
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> applyMockRefund(Long paymentId, BigDecimal refundAmount, String refundReason) {
        try {
            log.info("🎭 [Mock支付] 申请Mock退款，支付ID：{}, 退款金额：{}", paymentId, refundAmount);

            // 模拟网络延迟
            MockDataGenerator.simulateNetworkDelay(wechatPayConfig.getMock().getDelaySeconds());

            PaymentRecordEntity paymentRecord = paymentRecordDao.selectById(paymentId);
            if (paymentRecord == null) {
                return ResponseDTO.userErrorParam("支付记录不存在");
            }

            if (paymentRecord.getPaymentStatus() != 3) {
                return ResponseDTO.userErrorParam("支付状态异常，无法退款");
            }

            // 模拟退款成功（根据成功率）
            boolean refundSuccess = MockDataGenerator.simulatePaymentResult(
                    wechatPayConfig.getMock().getSuccessRate());

            if (refundSuccess) {
                // 更新退款状态
                paymentRecord.setRefundAmount(refundAmount);
                paymentRecord.setRefundReason(refundReason);
                paymentRecord.setRefundStatus(2); // 退款成功
                paymentRecord.setRefundTime(LocalDateTime.now());
                paymentRecordDao.updateById(paymentRecord);

                log.info("🎭 [Mock支付] Mock退款申请成功");
                return ResponseDTO.ok("Mock退款申请成功");
            } else {
                log.warn("🎭 [Mock支付] Mock退款申请失败（模拟失败）");
                return ResponseDTO.userErrorParam("Mock退款申请失败");
            }

        } catch (Exception e) {
            log.error("🎭 [Mock支付] Mock退款申请异常", e);
            return ResponseDTO.userErrorParam("Mock退款申请失败：" + e.getMessage());
        }
    }

    /**
     * 手动触发Mock支付回调
     */
    public ResponseDTO<String> triggerMockCallback(String paymentNo, boolean success) {
        try {
            log.info("🎭 [Mock回调] 手动触发Mock支付回调，支付单号：{}, 结果：{}", 
                    paymentNo, success ? "成功" : "失败");

            return wechatPayCallbackService.handleMockPaymentCallback(paymentNo, success);

        } catch (Exception e) {
            log.error("🎭 [Mock回调] 触发Mock回调异常", e);
            return ResponseDTO.userErrorParam("触发Mock回调失败：" + e.getMessage());
        }
    }

    /**
     * 异步触发Mock支付回调
     */
    @Async
    public void triggerMockCallbackAsync(String paymentNo, int delaySeconds) {
        try {
            log.info("🎭 [Mock回调] 将在{}秒后自动触发Mock支付回调，支付单号：{}", 
                    delaySeconds, paymentNo);

            // 延迟指定时间
            Thread.sleep(delaySeconds * 1000L);

            // 模拟支付结果（根据成功率）
            boolean paymentSuccess = MockDataGenerator.simulatePaymentResult(
                    wechatPayConfig.getMock().getSuccessRate());

            // 触发回调
            triggerMockCallback(paymentNo, paymentSuccess);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("🎭 [Mock回调] Mock回调任务被中断");
        } catch (Exception e) {
            log.error("🎭 [Mock回调] 自动触发Mock回调异常", e);
        }
    }

    /**
     * 创建Mock支付记录
     */
    private PaymentRecordEntity createMockPaymentRecord(WechatPayCreateForm form, String paymentNo) {
        PaymentRecordEntity paymentRecord = new PaymentRecordEntity();
        paymentRecord.setOrderId(form.getOrderId());
        paymentRecord.setPaymentNo(paymentNo);
        paymentRecord.setPaymentMethod("wechat");
        paymentRecord.setPaymentType(1); // 付款
        paymentRecord.setPaymentAmount(form.getAmount());
        paymentRecord.setPaymentStatus(1); // 待支付
        paymentRecord.setOpenid(form.getOpenid());
        paymentRecord.setExpireTime(LocalDateTime.now().plusMinutes(form.getExpireMinutes()));
        paymentRecord.setPrepayId("MOCK_PREPAY_" + System.currentTimeMillis());
        paymentRecord.setCreateBy("mock_system");
        paymentRecord.setCreateTime(LocalDateTime.now());
        paymentRecord.setUpdateBy("mock_system");
        paymentRecord.setUpdateTime(LocalDateTime.now());
        return paymentRecord;
    }

    /**
     * 生成Mock支付单号
     */
    private String generateMockPaymentNo() {
        return "MOCK_PAY_" + System.currentTimeMillis() + String.format("%04d", 
                (int)(Math.random() * 10000));
    }

    /**
     * 获取支付状态文本
     */
    private String getPaymentStatusText(Integer status) {
        return switch (status) {
            case 1 -> "待支付";
            case 2 -> "支付中";
            case 3 -> "支付成功";
            case 4 -> "支付失败";
            case 5 -> "已取消";
            default -> "未知状态";
        };
    }
}