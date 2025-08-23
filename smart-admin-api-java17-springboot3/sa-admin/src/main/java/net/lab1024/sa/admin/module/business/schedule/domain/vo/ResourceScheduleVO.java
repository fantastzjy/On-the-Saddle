package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 资源时间安排VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "资源时间安排信息")
public class ResourceScheduleVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "资源类型：1-俱乐部 2-教练 3-马匹")
    private Integer resourceType;

    @Schema(description = "资源类型名称")
    private String resourceTypeName;

    @Schema(description = "资源ID")
    private Long resourceId;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "日期")
    private LocalDate scheduleDate;

    @Schema(description = "开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    private LocalTime endTime;

    @Schema(description = "状态：1-请假 2-占用 3-维护 4-其他")
    private Integer status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "标题/事由")
    private String title;

    @Schema(description = "详细描述")
    private String description;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updatedBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}