package net.lab1024.sa.admin.module.business.payment.service;

import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.AmountReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.config.WechatMiniappConfig;
import net.lab1024.sa.admin.module.business.payment.config.WechatPayConfig;
import net.lab1024.sa.admin.module.business.payment.dao.PaymentRecordDao;
import net.lab1024.sa.admin.module.business.payment.domain.entity.PaymentRecordEntity;
import net.lab1024.sa.admin.module.business.payment.domain.form.WechatPayCreateForm;
import net.lab1024.sa.admin.module.business.payment.domain.vo.WechatPayCreateVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 微信支付集成服务（APIv3）
 *
 * @Author 1024创新实验室：开云
 * @Date 2024-12-30
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class WechatPaymentService {

    @Resource
    private PaymentRecordDao paymentRecordDao;

    @Resource
    private WechatPayConfig wechatPayConfig;

    @Resource
    private WechatMiniappConfig wechatMiniappConfig;

    @Resource
    private WechatPayCertificateService certificateService;

    @Resource
    private MockWechatPaymentService mockWechatPaymentService;

    /**
     * 创建微信支付订单（小程序支付）
     *
     * @param form 支付创建请求
     * @return 支付预下单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<WechatPayCreateVO> createPayment(WechatPayCreateForm form) {

        // 检查是否启用Mock模式
        if (wechatPayConfig.isMockMode()) {
            log.info("🎭 [支付模式] 当前为Mock模式，使用模拟支付，订单号：{}", form.getOrderNo());
            return mockWechatPaymentService.createMockPayment(form);
        }

        // 真实微信支付模式
        log.info("💳 [支付模式] 当前为{}模式，使用真实微信支付，订单号：{}",
                wechatPayConfig.getEnvironment(), form.getOrderNo());

        try {
            // 1. 生成支付单号
            String paymentNo = generatePaymentNo();
            log.info("开始创建微信支付订单，订单号：{}, 支付单号：{}", form.getOrderNo(), paymentNo);

            // 2. 创建支付记录
            PaymentRecordEntity paymentRecord = createPaymentRecord(form, paymentNo);
            paymentRecordDao.insert(paymentRecord);

            // 3. 调用微信支付API创建预支付订单
            PrepayResponse prepayResponse = createWechatPrepayOrder(form, paymentNo);

            // 4. 更新预支付ID
            paymentRecord.setPrepayId(prepayResponse.getPrepayId());
            paymentRecordDao.updateById(paymentRecord);

            // 5. 生成小程序支付参数
            WechatPayCreateVO result = buildMiniappPayParams(prepayResponse.getPrepayId(), paymentNo);

            log.info("创建微信支付订单成功，支付单号：{}, prepay_id：{}", paymentNo, prepayResponse.getPrepayId());
            return ResponseDTO.ok(result);

        } catch (ServiceException e) {
            log.error("微信支付API调用失败，错误码：{}, 错误信息：{}", e.getErrorCode(), e.getErrorMessage(), e);
            return ResponseDTO.userErrorParam("微信支付创建失败：" + e.getErrorMessage());
        } catch (Exception e) {
            log.error("创建微信支付订单失败", e);
            return ResponseDTO.userErrorParam("创建支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 创建支付记录
     */
    private PaymentRecordEntity createPaymentRecord(WechatPayCreateForm form, String paymentNo) {
        PaymentRecordEntity paymentRecord = new PaymentRecordEntity();
        paymentRecord.setOrderId(form.getOrderId());
        paymentRecord.setPaymentNo(paymentNo);
        paymentRecord.setPaymentMethod("wechat");
        paymentRecord.setPaymentType(1); // 付款
        paymentRecord.setPaymentAmount(form.getAmount());
        paymentRecord.setPaymentStatus(1); // 待支付
        paymentRecord.setOpenid(form.getOpenid());
        paymentRecord.setExpireTime(LocalDateTime.now().plusMinutes(form.getExpireMinutes()));
        paymentRecord.setCreateBy("system");
        paymentRecord.setCreateTime(LocalDateTime.now());
        paymentRecord.setUpdateBy("system");
        paymentRecord.setUpdateTime(LocalDateTime.now());
        return paymentRecord;
    }

    /**
     * 调用微信支付统一下单接口
     */
    private PrepayResponse createWechatPrepayOrder(WechatPayCreateForm form, String paymentNo) {
        JsapiService service = certificateService.getJsapiService();

        PrepayRequest request = new PrepayRequest();
        // 必填字段
        request.setAppid(wechatMiniappConfig.getAppId());
        request.setMchid(wechatPayConfig.getMchId());
        request.setDescription(form.getDescription());
        request.setOutTradeNo(paymentNo);
        request.setNotifyUrl(wechatPayConfig.getNotifyUrl());

        // 订单金额（分）
        Amount amount = new Amount();
        amount.setTotal(convertYuanToFen(form.getAmount()));
        amount.setCurrency("CNY");
        request.setAmount(amount);

        // 支付者信息
        Payer payer = new Payer();
        payer.setOpenid(form.getOpenid());
        request.setPayer(payer);

        // 场景信息
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp(form.getUserIp());
        request.setSceneInfo(sceneInfo);

        // 订单优惠标记
        if (StringUtils.isNotBlank(form.getAttach())) {
            request.setAttach(form.getAttach());
        }

        // 订单失效时间
        String expireTime = LocalDateTime.now()
                .plusMinutes(form.getExpireMinutes())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+08:00'"));
        request.setTimeExpire(expireTime);

        log.info("发起微信支付统一下单请求：商户订单号={}, 金额={}分, openid={}",
                paymentNo, amount.getTotal(), form.getOpenid());

        return service.prepay(request);
    }

    /**
     * 构建小程序支付参数
     */
    private WechatPayCreateVO buildMiniappPayParams(String prepayId, String paymentNo) {
        WechatPayCreateVO result = new WechatPayCreateVO();
        result.setPaymentNo(paymentNo);
        result.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        result.setNonceStr(generateNonceStr());
        result.setPackageValue("prepay_id=" + prepayId);
        result.setSignType("RSA");
        result.setOrderStatus(1); // 待支付
        result.setPaymentStatus(1); // 待支付

        // 生成签名（由微信SDK自动处理）
        // 小程序端调用wx.requestPayment时会自动验证这些参数
        result.setPaySign(""); // 实际使用中由前端调用wx.requestPayment时SDK自动生成

        return result;
    }

    /**
     * 查询支付订单状态
     */
    public ResponseDTO<Object> queryPaymentOrder(String paymentNo) {

        // Mock模式处理
        if (wechatPayConfig.isMockMode()) {
            log.info("🎭 [支付查询] Mock模式查询支付状态，支付单号：{}", paymentNo);
            ResponseDTO<String> mockResult = mockWechatPaymentService.queryMockPayment(paymentNo);
            if (mockResult.getData() != null) {
                // 返回Mock查询结果
                return ResponseDTO.ok("Mock支付状态: " + mockResult.getData());
            } else {
                return ResponseDTO.userErrorParam("查询支付状态失败");
            }
        }

        // 真实微信支付查询
        try {
            JsapiService service = certificateService.getJsapiService();

            QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
            request.setMchid(wechatPayConfig.getMchId());
            request.setOutTradeNo(paymentNo);

            Object response = service.queryOrderByOutTradeNo(request);

            log.info("查询微信支付订单状态成功，支付单号：{}", paymentNo);

            return ResponseDTO.ok(response);

        } catch (ServiceException e) {
            log.error("查询微信支付订单失败，错误码：{}, 错误信息：{}", e.getErrorCode(), e.getErrorMessage(), e);
            return ResponseDTO.userErrorParam("查询支付状态失败：" + e.getErrorMessage());
        } catch (Exception e) {
            log.error("查询支付订单状态失败", e);
            return ResponseDTO.userErrorParam("查询支付状态失败：" + e.getMessage());
        }
    }

    /**
     * 申请退款
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> applyRefund(Long paymentId, BigDecimal refundAmount, String refundReason) {

        // Mock模式处理
        if (wechatPayConfig.isMockMode()) {
            log.info("🎭 [退款申请] Mock模式申请退款，支付ID：{}", paymentId);
            return mockWechatPaymentService.applyMockRefund(paymentId, refundAmount, refundReason);
        }

        // 真实微信退款
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

            // 调用微信退款API
            String refundResult = processWechatRefund(paymentRecord, refundAmount, refundReason);

            if (StringUtils.isNotBlank(refundResult)) {
                paymentRecord.setRefundAmount(refundAmount);
                paymentRecord.setRefundReason(refundReason);
                paymentRecord.setRefundStatus(1); // 退款处理中
                paymentRecord.setRefundTime(LocalDateTime.now());
                paymentRecordDao.updateById(paymentRecord);

                log.info("申请退款成功，支付ID：{}, 退款金额：{}", paymentId, refundAmount);
                return ResponseDTO.ok("退款申请成功");
            } else {
                return ResponseDTO.userErrorParam("退款申请失败");
            }

        } catch (ServiceException e) {
            log.error("微信退款API调用失败，错误码：{}, 错误信息：{}", e.getErrorCode(), e.getErrorMessage(), e);
            return ResponseDTO.userErrorParam("退款申请失败：" + e.getErrorMessage());
        } catch (Exception e) {
            log.error("申请退款失败", e);
            return ResponseDTO.userErrorParam("申请退款失败：" + e.getMessage());
        }
    }

    /**
     * 处理微信退款
     */
    private String processWechatRefund(PaymentRecordEntity paymentRecord, BigDecimal refundAmount, String refundReason) {
        RefundService refundService = certificateService.getRefundService();

        CreateRequest request = new CreateRequest();
        request.setOutTradeNo(paymentRecord.getPaymentNo());
        request.setOutRefundNo(generateRefundNo());
        request.setReason(refundReason);
        request.setNotifyUrl(wechatPayConfig.getRefundNotifyUrl());

        // 退款金额
        AmountReq amountReq = new AmountReq();
        amountReq.setRefund(convertYuanToFen(refundAmount).longValue());
        amountReq.setTotal(convertYuanToFen(paymentRecord.getPaymentAmount()).longValue());
        amountReq.setCurrency("CNY");
        request.setAmount(amountReq);

        Refund refund = refundService.create(request);

        log.info("微信退款请求成功，退款单号：{}, 状态：{}",
                refund.getOutRefundNo(), refund.getStatus());

        return refund.getRefundId();
    }

    /**
     * 元转分
     */
    private Integer convertYuanToFen(BigDecimal yuan) {
        return yuan.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * 生成支付单号
     */
    private String generatePaymentNo() {
        return "PAY" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    /**
     * 生成退款单号
     */
    private String generateRefundNo() {
        return "REFUND" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    /**
     * 生成随机字符串
     */
    private String generateNonceStr() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
