package net.lab1024.sa.admin.module.business.payment.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付回调表单
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "支付回调表单")
public class PaymentCallbackForm {

    @Schema(description = "订单号", required = true)
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "支付金额", required = true)
    @NotNull(message = "支付金额不能为空")
    private BigDecimal paymentAmount;

    @Schema(description = "支付方式", required = true)
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    @Schema(description = "支付流水号")
    private String paymentTransactionId;

    @Schema(description = "支付平台订单号")
    private String platformOrderNo;
}
