package net.lab1024.sa.admin.module.business.schedule.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 资源时间安排查询表单
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "资源时间安排查询表单")
public class ResourceScheduleQueryForm {

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "状态：1-请假 2-占用 3-维护 4-其他")
    private Integer status;
}