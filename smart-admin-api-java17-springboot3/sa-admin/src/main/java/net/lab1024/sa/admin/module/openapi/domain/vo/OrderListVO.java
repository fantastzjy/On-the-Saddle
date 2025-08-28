package net.lab1024.sa.admin.module.openapi.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单列表响应VO
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "订单列表响应")
public class OrderListVO {

    // 基础订单信息
    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "状态文本")
    private String statusText;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    // 马场信息
    @Schema(description = "马场名称")
    private String clubName;

    // 教练信息
    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "教练姓名")
    private String coachName;

    @Schema(description = "教练头像")
    private String coachAvatarUrl;

    @Schema(description = "教练性别：0-女 1-男")
    private Integer coachGender;

    // 课程信息
    @Schema(description = "课程名称")
    private String productName;

    @Schema(description = "课程编号")
    private String productCode;

    @Schema(description = "产品类型：1-课程 2-课时包 3-活动")
    private Integer productType;

    @Schema(description = "订单类型：1-课程 2-课时包 3-活动")
    private Integer orderType;

    @Schema(description = "时间段列表")
    private List<TimeSlotInfo> timeSlots;

    @Schema(description = "上课地点")
    private String location;

    // 付款信息
    @Schema(description = "教练费")
    private BigDecimal coachFee;

    @Schema(description = "马匹费")
    private BigDecimal horseFee;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    // 课时包特殊字段
    @Schema(description = "剩余课时数（仅课时包显示）")
    private Integer remainingCount;


    @Schema(description = "退款数量（非课时包类型）")
    private Integer refundQuantity;

    @Data
    @Schema(description = "时间段信息")
    public static class TimeSlotInfo {
        @Schema(description = "日期")
        private String date;

        @Schema(description = "时间段")
        private String timeRange;
    }
}