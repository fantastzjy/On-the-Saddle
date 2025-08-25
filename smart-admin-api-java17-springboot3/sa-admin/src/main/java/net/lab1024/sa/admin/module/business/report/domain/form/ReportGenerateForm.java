package net.lab1024.sa.admin.module.business.report.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 报告生成表单
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "报告生成表单")
public class ReportGenerateForm {

    @Schema(description = "报告类型")
    @NotNull(message = "报告类型不能为空")
    private Integer reportType;

    @Schema(description = "报告参数")
    private Map<String, Object> params;

    @Schema(description = "模板ID")
    private String templateId;
}