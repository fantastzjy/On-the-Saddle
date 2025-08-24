package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程表综合数据VO (订单+预约+课程)
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "课程表综合数据VO")
public class ScheduleCombinedVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品类型")
    private Integer productType;

    @Schema(description = "商品类型名称")
    private String productTypeName;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "订单状态名称")
    private String orderStatusName;

    @Schema(description = "购买会员ID")
    private Long buyerMemberId;

    @Schema(description = "购买会员姓名")
    private String buyerMemberName;

    @Schema(description = "数量/总课时数")
    private Integer quantity;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "默认教练ID")
    private Long defaultCoachId;

    @Schema(description = "默认教练姓名")
    private String defaultCoachName;

    @Schema(description = "期望时间列表")
    private String preferredTimes;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    // 课时包相关信息
    @Schema(description = "总课时数")
    private Integer totalCount;

    @Schema(description = "已使用课时数")
    private Integer usedCount;

    @Schema(description = "剩余课时数")
    private Integer remainingCount;

    @Schema(description = "课时包状态")
    private Integer packageStatus;

    @Schema(description = "过期时间")
    private LocalDateTime expireDate;

    // 预约记录列表
    @Schema(description = "预约记录列表")
    private List<BookingRecordVO> bookings;
}