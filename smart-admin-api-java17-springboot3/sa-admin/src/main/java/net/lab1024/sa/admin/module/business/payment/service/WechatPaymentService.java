package net.lab1024.sa.admin.module.business.payment.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.payment.dao.PaymentRecordDao;
import net.lab1024.sa.admin.module.business.payment.domain.entity.PaymentRecordEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 微信支付集成服务
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class WechatPaymentService {

    @Resource
    private PaymentRecordDao paymentRecordDao;

    /**
     * 创建支付订单
     *
     * @param orderId 订单ID
     * @param amount 支付金额
     * @param openid 用户openid
     * @param description 商品描述
     * @return 支付预下单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Map<String, Object>> createPayment(Long orderId, BigDecimal amount, String openid, String description) {
        try {
            // 生成支付单号
            String paymentNo = generatePaymentNo();

            // 创建支付记录
            PaymentRecordEntity paymentRecord = new PaymentRecordEntity();
            paymentRecord.setOrderId(orderId);
            paymentRecord.setPaymentNo(paymentNo);
            paymentRecord.setPaymentMethod("wechat");
            paymentRecord.setPaymentType(1); // 付款
            paymentRecord.setPaymentAmount(amount);
            paymentRecord.setPaymentStatus(1); // 待支付
            paymentRecord.setOpenid(openid);
            paymentRecord.setExpireTime(LocalDateTime.now().plusMinutes(30)); // 30分钟过期
            paymentRecord.setCreateBy("system");
            paymentRecord.setCreateTime(LocalDateTime.now());
            paymentRecord.setUpdateBy("system");
            paymentRecord.setUpdateTime(LocalDateTime.now());

            paymentRecordDao.insert(paymentRecord);

            // TODO: 调用微信支付API创建预支付订单
            // 这里需要集成微信支付SDK
            Map<String, Object> prepayInfo = createWechatPrepayOrder(paymentNo, amount, openid, description);

            // 更新预支付ID
            if (prepayInfo.containsKey("prepay_id")) {
                paymentRecord.setPrepayId((String) prepayInfo.get("prepay_id"));
                paymentRecordDao.updateById(paymentRecord);
            }

            log.info("创建支付订单成功，支付单号：{}, 订单ID：{}, 金额：{}", paymentNo, orderId, amount);
            return ResponseDTO.ok(prepayInfo);

        } catch (Exception e) {
            log.error("创建支付订单失败", e);
            return ResponseDTO.userErrorParam("创建支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 处理支付回调
     *
     * @param callbackData 回调数据
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> handlePaymentCallback(String callbackData) {
        try {
            // TODO: 解析微信支付回调数据
            // 验证签名
            // 获取支付结果
            Map<String, Object> callbackMap = parseCallbackData(callbackData);

            String tradeNo = (String) callbackMap.get("transaction_id");
            String paymentNo = (String) callbackMap.get("out_trade_no");
            String resultCode = (String) callbackMap.get("trade_state");

            if (StringUtils.isBlank(paymentNo)) {
                return ResponseDTO.userErrorParam("支付单号为空");
            }

            PaymentRecordEntity paymentRecord = paymentRecordDao.selectByPaymentNo(paymentNo);
            if (paymentRecord == null) {
                return ResponseDTO.userErrorParam("支付记录不存在");
            }

            // 更新支付记录
            paymentRecord.setTradeNo(tradeNo);
            paymentRecord.setCallbackData(callbackData);
            paymentRecord.setNotifyTime(LocalDateTime.now());

            if ("SUCCESS".equals(resultCode)) {
                paymentRecord.setPaymentStatus(3); // 支付成功
                paymentRecord.setPaymentTime(LocalDateTime.now());
                log.info("支付成功，支付单号：{}", paymentNo);
            } else {
                paymentRecord.setPaymentStatus(4); // 支付失败
                log.warn("支付失败，支付单号：{}, 结果：{}", paymentNo, resultCode);
            }

            paymentRecordDao.updateById(paymentRecord);

            return ResponseDTO.ok("SUCCESS");

        } catch (Exception e) {
            log.error("处理支付回调失败", e);
            return ResponseDTO.userErrorParam("处理支付回调失败");
        }
    }

    /**
     * 申请退款
     *
     * @param paymentId 支付记录ID
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @return 退款结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> applyRefund(Long paymentId, BigDecimal refundAmount, String refundReason) {
        try {
            PaymentRecordEntity paymentRecord = paymentRecordDao.selectById(paymentId);
            if (paymentRecord == null) {
                return ResponseDTO.userErrorParam("支付记录不存在");
            }

            if (paymentRecord.getPaymentStatus() != 3) {
                return ResponseDTO.userErrorParam("支付状态异常，无法退款");
            }

            if (refundAmount.compareTo(paymentRecord.getPaymentAmount()) > 0) {
                return ResponseDTO.userErrorParam("退款金额不能大于支付金额");
            }

            // TODO: 调用微信退款API
            boolean refundSuccess = processWechatRefund(paymentRecord.getTradeNo(), refundAmount, refundReason);

            if (refundSuccess) {
                paymentRecord.setRefundAmount(refundAmount);
                paymentRecord.setRefundReason(refundReason);
                paymentRecord.setRefundStatus(2); // 退款成功
                paymentRecord.setRefundTime(LocalDateTime.now());
                paymentRecordDao.updateById(paymentRecord);

                log.info("申请退款成功，支付ID：{}, 退款金额：{}", paymentId, refundAmount);
                return ResponseDTO.ok("退款申请成功");
            } else {
                return ResponseDTO.userErrorParam("退款申请失败");
            }

        } catch (Exception e) {
            log.error("申请退款失败", e);
            return ResponseDTO.userErrorParam("申请退款失败：" + e.getMessage());
        }
    }

    /**
     * 查询支付状态
     *
     * @param paymentNo 支付单号
     * @return 支付状态
     */
    public ResponseDTO<Map<String, Object>> queryPaymentStatus(String paymentNo) {
        try {
            PaymentRecordEntity paymentRecord = paymentRecordDao.selectByPaymentNo(paymentNo);
            if (paymentRecord == null) {
                return ResponseDTO.userErrorParam("支付记录不存在");
            }

            Map<String, Object> result = Map.of(
                "paymentNo", paymentRecord.getPaymentNo(),
                "paymentStatus", paymentRecord.getPaymentStatus(),
                "paymentAmount", paymentRecord.getPaymentAmount(),
                "paymentTime", paymentRecord.getPaymentTime()
            );

            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("查询支付状态失败", e);
            return ResponseDTO.userErrorParam("查询支付状态失败");
        }
    }

    /**
     * 生成支付单号
     */
    private String generatePaymentNo() {
        return "PAY" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 创建微信预支付订单
     * TODO: 集成微信支付SDK
     */
    private Map<String, Object> createWechatPrepayOrder(String paymentNo, BigDecimal amount, String openid, String description) {
        // 模拟微信支付API调用
        return Map.of(
            "prepay_id", "wx123456789",
            "code_url", "weixin://wxpay/bizpayurl?pr=xxx",
            "timeStamp", String.valueOf(System.currentTimeMillis() / 1000),
            "nonceStr", UUID.randomUUID().toString().replace("-", ""),
            "package", "prepay_id=wx123456789",
            "signType", "RSA"
        );
    }

    /**
     * 解析支付回调数据
     * TODO: 实现微信支付回调数据解析
     */
    private Map<String, Object> parseCallbackData(String callbackData) {
        // 模拟解析微信支付回调
        return Map.of(
            "transaction_id", "4200001234567890",
            "out_trade_no", "PAY123456789",
            "trade_state", "SUCCESS"
        );
    }

    /**
     * 处理微信退款
     * TODO: 集成微信退款API
     */
    private boolean processWechatRefund(String tradeNo, BigDecimal refundAmount, String refundReason) {
        // 模拟微信退款API调用
        log.info("处理微信退款，交易号：{}, 退款金额：{}, 退款原因：{}", tradeNo, refundAmount, refundReason);
        return true;
    }
}
