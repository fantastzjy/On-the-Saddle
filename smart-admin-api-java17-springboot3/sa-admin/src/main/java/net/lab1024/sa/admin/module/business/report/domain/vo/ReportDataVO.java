package net.lab1024.sa.admin.module.business.report.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 通用报告数据VO
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "通用报告数据")
public class ReportDataVO {

    @Schema(description = "报告唯一标识")
    private String reportId;

    @Schema(description = "报告标题")
    private String reportTitle;

    @Schema(description = "报告副标题")
    private String reportSubtitle;

    @Schema(description = "生成时间")
    private LocalDateTime generateTime;

    @Schema(description = "报告摘要")
    private Map<String, Object> summary;

    @Schema(description = "报告章节")
    private List<ReportSectionVO> sections;

    @Schema(description = "图表数据")
    private Map<String, Object> chartData;

    @Schema(description = "附件列表")
    private List<String> attachments;
}