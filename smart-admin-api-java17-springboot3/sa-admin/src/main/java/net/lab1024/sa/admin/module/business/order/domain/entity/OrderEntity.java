package net.lab1024.sa.admin.module.business.order.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_order")
public class OrderEntity {

    @TableId(type = IdType.AUTO)
    private Long orderId;

    @DataTracerFieldLabel("订单号")
    private String orderNo;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("订单类型")
    private Integer orderType;

    @DataTracerFieldLabel("订单状态")
    private Integer orderStatus;

    @DataTracerFieldLabel("订单总金额")
    private BigDecimal totalAmount;

    @DataTracerFieldLabel("已支付金额")
    private BigDecimal paidAmount;

    @DataTracerFieldLabel("支付方式")
    private String paymentMethod;

    @DataTracerFieldLabel("支付时间")
    private LocalDateTime paymentTime;

    @DataTracerFieldLabel("订单备注")
    private String remark;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean isValid;

    private Boolean isDelete;
}