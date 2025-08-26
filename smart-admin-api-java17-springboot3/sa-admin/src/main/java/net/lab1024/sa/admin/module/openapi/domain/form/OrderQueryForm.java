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

    @Schema(description = "订单状态：1-课程预约, 2-待完成, 3-已完成, 4-退款/售后")
    @Min(value = 1, message = "状态值必须大于等于1")
    @Max(value = 4, message = "状态值必须小于等于4")
    private Integer status;

    @Schema(description = "页码，默认1")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Schema(description = "每页数量，默认10")
    @Min(value = 1, message = "每页数量必须大于0")
    @Max(value = 50, message = "每页数量不能超过50")
    private Integer pageSize = 10;
}
