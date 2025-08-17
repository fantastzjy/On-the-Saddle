package net.lab1024.sa.admin.module.business.order.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单创建表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "订单创建表单")
public class OrderAddForm {

    @Schema(description = "俱乐部ID")
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;

    @Schema(description = "会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Schema(description = "订单类型：1-课程订单 2-套餐订单 3-活动订单")
    @NotNull(message = "订单类型不能为空")
    private Integer orderType;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "订单明细列表")
    @NotEmpty(message = "订单明细不能为空")
    @Valid
    private List<OrderItemForm> orderItems;

    @Data
    @Schema(description = "订单明细表单")
    public static class OrderItemForm {

        @Schema(description = "商品ID")
        @NotNull(message = "商品ID不能为空")
        private Long productId;

        @Schema(description = "商品名称")
        @NotNull(message = "商品名称不能为空")
        private String productName;

        @Schema(description = "商品类型")
        @NotNull(message = "商品类型不能为空")
        private Integer productType;

        @Schema(description = "数量")
        @NotNull(message = "数量不能为空")
        private Integer quantity;

        @Schema(description = "单价")
        @NotNull(message = "单价不能为空")
        private BigDecimal unitPrice;

        @Schema(description = "关联教练ID")
        @NotNull(message = "教练ID不能为空")
        private Long coachId;

        @Schema(description = "期望上课时间")
        @NotNull(message = "期望上课时间不能为空")
        private LocalDateTime preferredTime;

        @Schema(description = "商品配置JSON")
        private String itemConfig;
    }
}
