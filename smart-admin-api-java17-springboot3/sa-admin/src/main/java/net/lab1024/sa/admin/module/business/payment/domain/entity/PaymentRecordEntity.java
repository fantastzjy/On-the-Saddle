package net.lab1024.sa.admin.module.business.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_payment_record")
public class PaymentRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long paymentId;

    @DataTracerFieldLabel("关联订单ID")
    private Long orderId;

    @DataTracerFieldLabel("支付单号")
    private String paymentNo;

    @DataTracerFieldLabel("第三方交易号")
    private String tradeNo;

    @DataTracerFieldLabel("支付方式")
    private String paymentMethod;

    @DataTracerFieldLabel("支付类型")
    private Integer paymentType;

    @DataTracerFieldLabel("支付金额")
    private BigDecimal paymentAmount;

    @DataTracerFieldLabel("支付状态")
    private Integer paymentStatus;

    @DataTracerFieldLabel("微信预支付ID")
    private String prepayId;

    @DataTracerFieldLabel("用户openid")
    private String openid;

    @DataTracerFieldLabel("支付完成时间")
    private LocalDateTime paymentTime;

    @DataTracerFieldLabel("通知回调时间")
    private LocalDateTime notifyTime;

    @DataTracerFieldLabel("支付回调原始数据")
    private String callbackData;

    @DataTracerFieldLabel("退款原因")
    private String refundReason;

    @DataTracerFieldLabel("退款金额")
    private BigDecimal refundAmount;

    @DataTracerFieldLabel("退款时间")
    private LocalDateTime refundTime;

    @DataTracerFieldLabel("退款状态")
    private Integer refundStatus;

    @DataTracerFieldLabel("支付过期时间")
    private LocalDateTime expireTime;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}