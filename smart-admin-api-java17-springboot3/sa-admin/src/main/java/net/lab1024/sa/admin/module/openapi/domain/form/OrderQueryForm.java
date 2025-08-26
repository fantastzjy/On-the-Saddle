package net.lab1024.sa.admin.module.openapi.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 订单查询表单
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "订单查询表单")
public class OrderQueryForm {

    @Schema(description = "订单状态: pending-待完成, completed-已完成, afterSale-退款/售后")
    private String status;

    @Schema(description = "页码，默认1")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Schema(description = "每页数量，默认10")
    @Min(value = 1, message = "每页数量必须大于0")
    @Max(value = 50, message = "每页数量不能超过50")
    private Integer pageSize = 10;
}
