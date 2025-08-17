package net.lab1024.sa.admin.module.business.schedule.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课表时间更新表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "课表时间更新表单")
public class ScheduleTimeUpdateForm {

    @Schema(description = "课表ID")
    @NotNull(message = "课表ID不能为空")
    private Long scheduleId;

    @Schema(description = "新的开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "新的结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
}
