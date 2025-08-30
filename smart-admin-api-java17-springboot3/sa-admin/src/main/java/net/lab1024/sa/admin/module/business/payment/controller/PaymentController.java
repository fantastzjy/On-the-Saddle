package net.lab1024.sa.admin.module.business.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.payment.domain.form.WechatPayCreateForm;
import net.lab1024.sa.admin.module.business.payment.domain.vo.WechatPayCreateVO;
import net.lab1024.sa.admin.module.business.payment.service.MockWechatPaymentService;
import net.lab1024.sa.admin.module.business.payment.service.WechatPayCallbackService;
import net.lab1024.sa.admin.module.business.payment.service.WechatPaymentService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 微信支付控制器
 *
 * @Author 1024创新实验室：开云
 * @Date 2024-12-30
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@RequestMapping("/api/payment/wechat")
@Tag(name = "微信支付", description = "微信支付相关接口")
public class PaymentController {

    @Resource
    private WechatPaymentService wechatPaymentService;

    @Resource
    private WechatPayCallbackService wechatPayCallbackService;

    @Resource
    private MockWechatPaymentService mockWechatPaymentService;

    /**
     * 创建微信支付订单
     */
    @PostMapping("/create")
    @Operation(summary = "创建微信支付订单", description = "创建小程序支付订单")
    public ResponseDTO<WechatPayCreateVO> createPayment(@RequestBody @Valid WechatPayCreateForm form) {
        return wechatPaymentService.createPayment(form);
    }

    /**
     * 查询支付订单状态
     */
    @GetMapping("/query/{paymentNo}")
    @Operation(summary = "查询支付订单状态", description = "根据支付单号查询订单状态")
    public ResponseDTO<?> queryPayment(@PathVariable String paymentNo) {
        return wechatPaymentService.queryPaymentOrder(paymentNo);
    }

    /**
     * 微信支付成功回调（APIv3）
     */
    @PostMapping("/notify")
    @NoNeedLogin
    @Operation(summary = "微信支付回调通知", description = "接收微信支付成功通知")
    public String paymentNotify(HttpServletRequest request) {
        try {
            // 获取请求体
            String requestBody = getRequestBody(request);

            // 获取微信签名相关头部
            String wechatpaySignature = request.getHeader("Wechatpay-Signature");
            String wechatpayTimestamp = request.getHeader("Wechatpay-Timestamp");
            String wechatpayNonce = request.getHeader("Wechatpay-Nonce");
            String wechatpaySerial = request.getHeader("Wechatpay-Serial");

            log.info("收到微信支付回调通知");

            // 处理回调
            ResponseDTO<String> result = wechatPayCallbackService.handlePaymentNotify(
                    requestBody, wechatpaySignature, wechatpayTimestamp,
                    wechatpayNonce, wechatpaySerial);

            return result.getData();

        } catch (Exception e) {
            log.error("处理微信支付回调异常", e);
            return "FAIL";
        }
    }

    /**
     * 微信退款成功回调（APIv3）
     */
    @PostMapping("/refund/notify")
    @NoNeedLogin
    @Operation(summary = "微信退款回调通知", description = "接收微信退款成功通知")
    public String refundNotify(HttpServletRequest request) {
        try {
            // 获取请求体
            String requestBody = getRequestBody(request);

            // 获取微信签名相关头部
            String wechatpaySignature = request.getHeader("Wechatpay-Signature");
            String wechatpayTimestamp = request.getHeader("Wechatpay-Timestamp");
            String wechatpayNonce = request.getHeader("Wechatpay-Nonce");
            String wechatpaySerial = request.getHeader("Wechatpay-Serial");

            log.info("收到微信退款回调通知");

            // 处理退款回调
            ResponseDTO<String> result = wechatPayCallbackService.handleRefundNotify(
                    requestBody, wechatpaySignature, wechatpayTimestamp,
                    wechatpayNonce, wechatpaySerial);

            return result.getData();

        } catch (Exception e) {
            log.error("处理微信退款回调异常", e);
            return "FAIL";
        }
    }

    /**
     * 申请退款
     */
    @PostMapping("/refund")
    @Operation(summary = "申请微信退款", description = "申请微信支付退款")
    public ResponseDTO<String> applyRefund(@RequestParam Long paymentId,
                                           @RequestParam String refundAmount,
                                           @RequestParam String refundReason) {
        try {
            return wechatPaymentService.applyRefund(paymentId,
                    new java.math.BigDecimal(refundAmount), refundReason);
        } catch (Exception e) {
            log.error("申请退款异常", e);
            return ResponseDTO.userErrorParam("申请退款失败：" + e.getMessage());
        }
    }

    /**
     * Mock支付回调触发接口（仅开发测试使用）
     */
    @PostMapping("/mock/callback")
    @Operation(summary = "触发Mock支付回调", description = "开发测试专用接口，手动触发Mock支付回调")
    public ResponseDTO<String> triggerMockCallback(@RequestParam String paymentNo,
                                                   @RequestParam(defaultValue = "true") Boolean success) {
        log.info("🎭 [Mock接口] 手动触发Mock支付回调，支付单号：{}, 结果：{}", paymentNo, success);
        return mockWechatPaymentService.triggerMockCallback(paymentNo, success);
    }

    /**
     * 获取Mock支付状态接口（仅开发测试使用）
     */
    @GetMapping("/mock/status/{paymentNo}")
    @Operation(summary = "查询Mock支付状态", description = "开发测试专用接口，查询Mock支付状态")
    public ResponseDTO<String> getMockPaymentStatus(@PathVariable String paymentNo) {
        log.info("🎭 [Mock接口] 查询Mock支付状态，支付单号：{}", paymentNo);
        return mockWechatPaymentService.queryMockPayment(paymentNo);
    }

    /**
     * 获取请求体内容
     */
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }
}
