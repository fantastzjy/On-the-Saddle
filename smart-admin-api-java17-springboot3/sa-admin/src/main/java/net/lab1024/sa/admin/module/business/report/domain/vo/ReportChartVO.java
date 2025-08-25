package net.lab1024.sa.admin.module.business.report.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 报告图表VO
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "报告图表")
public class ReportChartVO {

    @Schema(description = "图表ID")
    private String chartId;

    @Schema(description = "图表标题")
    private String title;

    @Schema(description = "图表类型")
    private String chartType;

    @Schema(description = "图表配置")
    private Map<String, Object> config;

    @Schema(description = "图表数据")
    private Map<String, Object> data;
}