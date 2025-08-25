package net.lab1024.sa.admin.module.business.report.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 报告章节VO
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "报告章节")
public class ReportSectionVO {

    @Schema(description = "章节ID")
    private String sectionId;

    @Schema(description = "章节标题")
    private String title;

    @Schema(description = "章节内容")
    private String content;

    @Schema(description = "章节表格")
    private List<ReportTableVO> tables;

    @Schema(description = "章节图表")
    private List<ReportChartVO> charts;
}