package net.lab1024.sa.admin.module.business.payment.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信支付创建订单响应VO
 * 
 * @author 1024创新实验室：开云
 * @since 2024-12-30
 */
@Data
@Schema(description = "微信支付创建订单响应")
public class WechatPayCreateVO {

    @Schema(description = "支付单号")
    private String paymentNo;

    @Schema(description = "时间戳")
    private String timeStamp;

    @Schema(description = "随机字符串")
    private String nonceStr;

    @Schema(description = "统一下单接口返回的prepay_id参数值")
    private String packageValue;

    @Schema(description = "签名类型，默认为RSA")
    private String signType = "RSA";

    @Schema(description = "签名")
    private String paySign;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "支付状态")
    private Integer paymentStatus;
}