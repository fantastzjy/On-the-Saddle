package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.admin.module.business.member.domain.vo.UnavailableTimeSlotVO;

import java.math.BigDecimal;

/**
 * 订单创建表单
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "订单创建表单")
public class OrderCreateForm {

    @Schema(description = "教练编号", required = true)
    @NotBlank(message = "教练编号不能为空")
    private String coachNo;

    @Schema(description = "课程编号", required = true)
    @NotBlank(message = "课程编号不能为空")
    private String courseCode;

    @Schema(description = "时间段信息", required = true)
    @NotNull(message = "时间段不能为空")
    private UnavailableTimeSlotVO timeSlot;

    @Schema(description = "教练费", required = true)
    @NotNull(message = "教练费不能为空")
    private BigDecimal coachFee;

    @Schema(description = "基础费", required = true)
    @NotNull(message = "基础费不能为空")
    private BigDecimal baseFee;

    @Schema(description = "总计", required = true)
    @NotNull(message = "总计不能为空")
    private BigDecimal totalAmount;
}