package net.lab1024.sa.admin.module.business.payment.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 微信支付创建订单请求表单
 *
 * @author 1024创新实验室：开云
 * @since 2024-12-30
 */
@Data
@Schema(description = "微信支付创建订单请求表单")
public class WechatPayCreateForm {

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "订单号")
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "商品描述")
    @NotBlank(message = "商品描述不能为空")
    private String description;

    @Schema(description = "支付金额（元）")
    @NotNull(message = "支付金额不能为空")
    private BigDecimal amount;

    @Schema(description = "用户openid")
    @NotBlank(message = "用户openid不能为空")
    private String openid;

    @Schema(description = "用户IP地址")
    private String userIp = "127.0.0.1";

    @Schema(description = "订单过期时间（分钟，默认30分钟）")
    private Integer expireMinutes = 30;

    @Schema(description = "附加数据（可选）")
    private String attach;
}
