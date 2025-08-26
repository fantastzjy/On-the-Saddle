package net.lab1024.sa.admin.module.openapi.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情响应VO
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "订单详情响应")
public class OrderDetailVO {

    // 基础订单信息
    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "状态文本")
    private String statusText;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "订单类型文本")
    private String orderTypeText;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    @Schema(description = "订单备注")
    private String remark;

    // 马场信息
    @Schema(description = "马场详细信息")
    private ClubDetailInfo clubInfo;

    // 教练信息
    @Schema(description = "教练详细信息")
    private CoachDetailInfo coachInfo;

    // 课程信息
    @Schema(description = "课程详细信息")
    private ProductDetailInfo productInfo;

    // 付款信息
    @Schema(description = "付款详细信息")
    private PaymentDetailInfo paymentInfo;

    // 课时包余额信息（仅课时包订单）
    @Schema(description = "课时包余额信息")
    private PackageBalanceInfo packageBalance;

    // 预约信息列表
    @Schema(description = "预约记录列表")
    private List<BookingRecordInfo> bookingRecords;

    @Data
    @Schema(description = "马场详细信息")
    public static class ClubDetailInfo {
        @Schema(description = "马场名称")
        private String clubName;

        @Schema(description = "马场地址")
        private String address;

        @Schema(description = "联系电话")
        private String phone;
    }

    @Data
    @Schema(description = "教练详细信息")
    public static class CoachDetailInfo {
        @Schema(description = "教练编号")
        private String coachNo;

        @Schema(description = "教练姓名")
        private String actualName;

        @Schema(description = "教练头像")
        private String avatarUrl;

        @Schema(description = "教练性别：0-女 1-男")
        private Integer gender;

        @Schema(description = "教练费(元/鞍时)")
        private BigDecimal coachFee;

        @Schema(description = "专长领域")
        private List<String> specialties;
    }

    @Data
    @Schema(description = "产品详细信息")
    public static class ProductDetailInfo {
        @Schema(description = "产品名称")
        private String productName;

        @Schema(description = "产品编号")
        private String productCode;

        @Schema(description = "产品类型")
        private Integer productType;

        @Schema(description = "产品类型文本")
        private String productTypeText;

        @Schema(description = "时间段列表")
        private List<OrderListVO.TimeSlotInfo> timeSlots;

        @Schema(description = "上课地点")
        private String location;

        @Schema(description = "课程时长（分钟）")
        private Integer durationMinutes;

        @Schema(description = "课程时长（鞍时）")
        private BigDecimal durationPeriods;
    }

    @Data
    @Schema(description = "付款详细信息")
    public static class PaymentDetailInfo {
        @Schema(description = "教练费")
        private BigDecimal coachFee;

        @Schema(description = "马匹费")
        private BigDecimal horseFee;

        @Schema(description = "数量/课时数")
        private Integer quantity;

        @Schema(description = "单价")
        private BigDecimal unitPrice;

        @Schema(description = "订单总金额")
        private BigDecimal totalAmount;

        @Schema(description = "已支付金额")
        private BigDecimal paidAmount;

        @Schema(description = "支付方式")
        private String paymentMethod;
    }

    @Data
    @Schema(description = "课时包余额信息")
    public static class PackageBalanceInfo {
        @Schema(description = "总课时数")
        private Integer totalCount;

        @Schema(description = "已使用课时数")
        private Integer usedCount;

        @Schema(description = "剩余课时数")
        private Integer remainingCount;

        @Schema(description = "过期时间")
        private LocalDateTime expireDate;

        @Schema(description = "余额状态：1-有效 2-已用完 3-已过期")
        private Integer status;
    }

    @Data
    @Schema(description = "预约记录信息")
    public static class BookingRecordInfo {
        @Schema(description = "预约ID")
        private Long bookingId;

        @Schema(description = "预约状态：1-已预约 2-已核销 3-已取消 4-已过期")
        private Integer bookingStatus;

        @Schema(description = "预约状态文本")
        private String statusText;

        @Schema(description = "预约时间")
        private LocalDateTime startTime;

        @Schema(description = "结束时间")
        private LocalDateTime endTime;

        @Schema(description = "实际教练费")
        private BigDecimal actualCoachFee;

        @Schema(description = "实际马匹费")
        private BigDecimal actualHorseFee;

        @Schema(description = "消费课时数")
        private Integer packageConsumeCount;

        @Schema(description = "到店时间")
        private LocalDateTime arrivalTime;

        @Schema(description = "完成时间")
        private LocalDateTime completionTime;
    }
}