package net.lab1024.sa.admin.module.business.report.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 报告表格VO
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "报告表格")
public class ReportTableVO {

    @Schema(description = "表格ID")
    private String tableId;

    @Schema(description = "表格标题")
    private String title;

    @Schema(description = "表格列定义")
    private List<Map<String, Object>> columns;

    @Schema(description = "表格数据")
    private List<Map<String, Object>> data;
}