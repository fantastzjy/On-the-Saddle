package net.lab1024.sa.admin.module.business.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.payment.domain.form.PaymentCallbackForm;
import net.lab1024.sa.admin.module.business.payment.service.PaymentService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付回调控制器
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@RestController
@RequestMapping("/api/payment")
@Tag(name = "支付回调", description = "支付成功回调处理")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * 支付成功回调
     */
    @PostMapping("/callback/success")
    @NoNeedLogin
    @Operation(summary = "支付成功回调")
    public ResponseDTO<Void> paymentSuccess(@RequestBody @Valid PaymentCallbackForm form) {
        return paymentService.handlePaymentSuccess(form);
    }
}
