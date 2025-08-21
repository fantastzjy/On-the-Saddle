package net.lab1024.sa.admin.module.business.order.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情VO
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "订单详情VO")
public class OrderDetailVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员姓名")
    private String memberName;

    @Schema(description = "会员电话")
    private String memberPhone;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "订单类型名称")
    private String orderTypeName;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "订单状态名称")
    private String orderStatusName;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "已支付金额")
    private BigDecimal paidAmount;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "支付方式名称")
    private String paymentMethodName;

    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "订单明细列表")
    private List<OrderItemVO> orderItems;

    @Schema(description = "预约信息列表")
    private List<BookingInfoVO> bookings;

    @Data
    @Schema(description = "订单明细VO")
    public static class OrderItemVO {

        @Schema(description = "明细ID")
        private Long id;

        @Schema(description = "商品ID")
        private Long productId;

        @Schema(description = "商品名称")
        private String productName;

        @Schema(description = "商品类型")
        private Integer productType;

        @Schema(description = "商品类型名称")
        private String productTypeName;

        @Schema(description = "数量")
        private Integer quantity;

        @Schema(description = "单价")
        private BigDecimal unitPrice;

        @Schema(description = "小计")
        private BigDecimal totalPrice;

        @Schema(description = "关联教练ID")
        private Long coachId;

        @Schema(description = "教练姓名")
        private String coachName;

        @Schema(description = "期望上课时间")
        private LocalDateTime preferredTime;

        @Schema(description = "商品配置")
        private String itemConfig;

        @Schema(description = "创建时间")
        private LocalDateTime createTime;
    }

    @Data
    @Schema(description = "预约信息VO")
    public static class BookingInfoVO {

        @Schema(description = "预约ID")
        private Long bookingId;

        @Schema(description = "订单明细ID")
        private Long orderItemId;

        @Schema(description = "教练ID")
        private Long coachId;

        @Schema(description = "教练姓名")
        private String coachName;

        @Schema(description = "马匹ID")
        private Long horseId;

        @Schema(description = "马匹名称")
        private String horseName;

        @Schema(description = "预约开始时间")
        private LocalDateTime startTime;

        @Schema(description = "预约结束时间")
        private LocalDateTime endTime;

        @Schema(description = "预约状态")
        private Integer bookingStatus;

        @Schema(description = "预约状态名称")
        private String bookingStatusName;

        @Schema(description = "实际教练费")
        private BigDecimal actualCoachFee;

        @Schema(description = "实际马匹费")
        private BigDecimal actualHorseFee;

        @Schema(description = "到店时间")
        private LocalDateTime arrivalTime;

        @Schema(description = "完成时间")
        private LocalDateTime completionTime;

        @Schema(description = "取消原因")
        private String cancelReason;

        @Schema(description = "创建时间")
        private LocalDateTime createTime;
    }
}