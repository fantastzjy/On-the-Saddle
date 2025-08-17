package net.lab1024.sa.admin.module.business.order.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * 订单状态更新表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "订单状态更新表单")
public class OrderStatusUpdateForm {

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "订单状态：1-待支付 2-已支付 3-已确认 4-已完成 5-已取消")
    @NotNull(message = "订单状态不能为空")
    private Integer orderStatus;

    @Schema(description = "备注")
    private String remark;
}
