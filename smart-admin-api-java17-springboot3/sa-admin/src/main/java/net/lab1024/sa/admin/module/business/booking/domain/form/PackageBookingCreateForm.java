package net.lab1024.sa.admin.module.business.booking.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课时包预约创建表单
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "课时包预约创建表单")
public class PackageBookingCreateForm {

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "消费会员ID", required = true)
    @NotNull(message = "消费会员ID不能为空")
    private Long consumerMemberId;

    @Schema(description = "教练ID", required = true)
    @NotNull(message = "教练ID不能为空")
    private Long coachId;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "预约开始时间", required = true)
    @NotNull(message = "预约开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "预约结束时间", required = true)
    @NotNull(message = "预约结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "实际教练费")
    private BigDecimal actualCoachFee;

    @Schema(description = "实际马匹费")
    private BigDecimal actualHorseFee;

    @Schema(description = "消费课时数", required = true)
    @NotNull(message = "消费课时数不能为空")
    private Integer packageConsumeCount;

    @Schema(description = "备注")
    private String remark;
}