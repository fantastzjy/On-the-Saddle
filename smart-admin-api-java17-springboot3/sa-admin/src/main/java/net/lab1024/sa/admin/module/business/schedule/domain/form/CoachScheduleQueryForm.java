package net.lab1024.sa.admin.module.business.schedule.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 教练排课资源视图查询表单
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-25
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
@Data
@Schema(description = "教练排课资源视图查询表单")
public class CoachScheduleQueryForm {

    @Schema(description = "查询日期", example = "2024-08-25")
    @NotNull(message = "查询日期不能为空")
    private LocalDate queryDate;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "教练ID列表")
    private List<Long> coachIds;

    @Schema(description = "关键词搜索")
    private String keywords;
}
