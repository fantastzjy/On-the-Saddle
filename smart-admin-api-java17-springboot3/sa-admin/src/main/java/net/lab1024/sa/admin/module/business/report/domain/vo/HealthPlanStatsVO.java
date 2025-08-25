package net.lab1024.sa.admin.module.business.report.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 健康计划统计VO
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "健康计划统计")
public class HealthPlanStatsVO {

    @Schema(description = "计划类型")
    private String planType;

    @Schema(description = "计划类型名称")
    private String planTypeName;

    @Schema(description = "计划总数")
    private Integer totalPlans;

    @Schema(description = "已执行数量")
    private Integer executedCount;

    @Schema(description = "逾期数量")
    private Integer overdueCount;

    @Schema(description = "执行率")
    private Double executionRate;

    @Schema(description = "及时率")
    private Double timelyRate;
}