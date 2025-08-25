package net.lab1024.sa.admin.module.business.report.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 报告导出表单
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "报告导出表单")
public class ReportExportForm {

    @Schema(description = "报告类型")
    @NotNull(message = "报告类型不能为空")
    private Integer reportType;

    @Schema(description = "导出格式：pdf/excel")
    @NotBlank(message = "导出格式不能为空")
    private String exportFormat;

    @Schema(description = "报告参数")
    private Map<String, Object> params;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "文件名")
    private String fileName;
}