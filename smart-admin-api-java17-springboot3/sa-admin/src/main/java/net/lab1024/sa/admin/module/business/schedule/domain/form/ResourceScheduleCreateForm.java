package net.lab1024.sa.admin.module.business.schedule.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 资源时间安排创建表单
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "资源时间安排创建表单")
public class ResourceScheduleCreateForm {

    @Schema(description = "俱乐部ID")
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;

    @Schema(description = "资源类型：1-俱乐部 2-教练 3-马匹")
    @NotNull(message = "资源类型不能为空")
    private Integer resourceType;

    @Schema(description = "资源ID")
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @Schema(description = "日期")
    @NotNull(message = "日期不能为空")
    private LocalDate scheduleDate;

    @Schema(description = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalTime endTime;

    @Schema(description = "状态：1-请假 2-占用 3-维护 4-其他")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "标题/事由")
    private String title;

    @Schema(description = "详细描述")
    private String description;
}