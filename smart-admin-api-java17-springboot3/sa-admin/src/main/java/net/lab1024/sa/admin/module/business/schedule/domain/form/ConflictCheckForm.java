package net.lab1024.sa.admin.module.business.schedule.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 冲突检测表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "冲突检测表单")
public class ConflictCheckForm {

    @Schema(description = "课表ID（更新时传入，新增时不传）")
    private Long scheduleId;

    @Schema(description = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "教练ID")
    @NotNull(message = "教练ID不能为空")
    private Long coachId;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "会员ID")
    private Long memberId;
}
