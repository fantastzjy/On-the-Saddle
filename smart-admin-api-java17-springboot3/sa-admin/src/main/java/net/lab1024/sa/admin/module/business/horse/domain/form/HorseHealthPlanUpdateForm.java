package net.lab1024.sa.admin.module.business.horse.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 马匹健康计划更新表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseHealthPlanUpdateForm {

    @Schema(description = "计划ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划ID不能为空")
    private Long id;

    @Schema(description = "马匹ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "马匹ID不能为空")
    private Long horseId;

    @Schema(description = "计划类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "计划类型不能为空")
    private String planType;

    @Schema(description = "计划名称")
    private String planName;

    @Schema(description = "周期天数")
    private Integer cycleDays;

    @Schema(description = "上次执行日期")
    private LocalDateTime lastDate;

    @Schema(description = "下次执行日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "下次执行日期不能为空")
    private LocalDateTime nextDate;

    @Schema(description = "提前提醒天数")
    private Integer reminderDays;

    @Schema(description = "计划配置JSON格式")
    private String planConfig;
}