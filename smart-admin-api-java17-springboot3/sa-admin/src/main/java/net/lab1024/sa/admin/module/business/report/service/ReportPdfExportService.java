package net.lab1024.sa.admin.module.business.report.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportDataVO;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportSectionVO;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportTableVO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * PDF导出服务
 * 使用HTML转PDF的方式实现，避免引入额外的PDF库依赖
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class ReportPdfExportService {

    /**
     * 导出HTML格式的报告（可在浏览器中打印为PDF）
     */
    public void exportToHtmlPdf(ReportDataVO reportData, String fileName, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            // 对文件名进行URL编码以支持中文
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName + ".html");

            // 生成HTML内容
            String htmlContent = generateHtmlReport(reportData);

            // 写入响应
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }

            log.info("导出HTML报告成功，文件名: {}", fileName);

        } catch (IOException e) {
            log.error("导出HTML报告失败", e);
            throw new RuntimeException("导出报告失败", e);
        }
    }

    /**
     * 生成HTML报告内容
     */
    private String generateHtmlReport(ReportDataVO reportData) {
        StringBuilder html = new StringBuilder();

        // HTML文档开始
        html.append("<!DOCTYPE html>");
        html.append("<html lang='zh-CN'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>").append(reportData.getReportTitle()).append("</title>");
        html.append(generateCssStyles());
        html.append("</head>");
        html.append("<body>");

        // 报告头部
        html.append(generateReportHeader(reportData));

        // 报告摘要
        if (reportData.getSummary() != null && !reportData.getSummary().isEmpty()) {
            html.append(generateSummarySection(reportData.getSummary()));
        }

        // 报告章节
        if (reportData.getSections() != null) {
            for (ReportSectionVO section : reportData.getSections()) {
                html.append(generateSection(section));
            }
        }

        // 报告尾部
        html.append(generateReportFooter(reportData));

        // HTML文档结束
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * 生成CSS样式
     */
    private String generateCssStyles() {
        return """
            <style>
                @media print {
                    .no-print { display: none !important; }
                    body { margin: 0; }
                }
                
                body {
                    font-family: 'Microsoft YaHei', Arial, sans-serif;
                    line-height: 1.6;
                    color: #333;
                    max-width: 1200px;
                    margin: 0 auto;
                    padding: 20px;
                }
                
                .report-header {
                    text-align: center;
                    border-bottom: 3px solid #4CAF50;
                    margin-bottom: 30px;
                    padding-bottom: 20px;
                }
                
                .report-title {
                    font-size: 28px;
                    font-weight: bold;
                    color: #2E7D32;
                    margin: 0;
                }
                
                .report-subtitle {
                    font-size: 18px;
                    color: #666;
                    margin: 10px 0;
                }
                
                .report-meta {
                    font-size: 14px;
                    color: #999;
                    margin-top: 10px;
                }
                
                .summary-section {
                    background: #f8f9fa;
                    border: 1px solid #e9ecef;
                    border-radius: 8px;
                    padding: 20px;
                    margin-bottom: 30px;
                }
                
                .summary-title {
                    font-size: 20px;
                    font-weight: bold;
                    color: #495057;
                    margin-bottom: 15px;
                }
                
                .summary-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                    gap: 15px;
                }
                
                .summary-item {
                    background: white;
                    border: 1px solid #dee2e6;
                    border-radius: 6px;
                    padding: 15px;
                    text-align: center;
                }
                
                .summary-label {
                    font-size: 14px;
                    color: #6c757d;
                    margin-bottom: 5px;
                }
                
                .summary-value {
                    font-size: 24px;
                    font-weight: bold;
                    color: #28a745;
                }
                
                .section {
                    margin-bottom: 40px;
                    break-inside: avoid;
                }
                
                .section-title {
                    font-size: 22px;
                    font-weight: bold;
                    color: #343a40;
                    border-bottom: 2px solid #28a745;
                    padding-bottom: 10px;
                    margin-bottom: 20px;
                }
                
                .section-content {
                    margin-bottom: 20px;
                    line-height: 1.8;
                }
                
                .report-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 20px;
                    background: white;
                    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                }
                
                .report-table th,
                .report-table td {
                    border: 1px solid #dee2e6;
                    padding: 12px;
                    text-align: center;
                }
                
                .report-table th {
                    background: #f8f9fa;
                    font-weight: bold;
                    color: #495057;
                }
                
                .report-table tbody tr:nth-child(even) {
                    background: #f8f9fa;
                }
                
                .report-table tbody tr:hover {
                    background: #e3f2fd;
                }
                
                .report-footer {
                    margin-top: 40px;
                    padding-top: 20px;
                    border-top: 1px solid #dee2e6;
                    text-align: center;
                    color: #6c757d;
                    font-size: 12px;
                }
                
                .print-button {
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    background: #007bff;
                    color: white;
                    border: none;
                    border-radius: 6px;
                    padding: 12px 24px;
                    cursor: pointer;
                    font-size: 14px;
                    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                }
                
                .print-button:hover {
                    background: #0056b3;
                }
            </style>
            """;
    }

    /**
     * 生成报告头部
     */
    private String generateReportHeader(ReportDataVO reportData) {
        StringBuilder header = new StringBuilder();
        header.append("<div class='report-header'>");
        header.append("<h1 class='report-title'>").append(reportData.getReportTitle()).append("</h1>");

        if (reportData.getReportSubtitle() != null) {
            header.append("<p class='report-subtitle'>").append(reportData.getReportSubtitle()).append("</p>");
        }

        if (reportData.getGenerateTime() != null) {
            header.append("<p class='report-meta'>生成时间：")
                  .append(reportData.getGenerateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                  .append("</p>");
        }

        header.append("</div>");

        // 添加打印按钮
        header.append("<button class='print-button no-print' onclick='window.print()'>🖨️ 打印/导出PDF</button>");

        return header.toString();
    }

    /**
     * 生成摘要章节
     */
    private String generateSummarySection(Map<String, Object> summary) {
        StringBuilder summaryHtml = new StringBuilder();
        summaryHtml.append("<div class='summary-section'>");
        summaryHtml.append("<h2 class='summary-title'>📊 报告摘要</h2>");
        summaryHtml.append("<div class='summary-grid'>");

        for (Map.Entry<String, Object> entry : summary.entrySet()) {
            summaryHtml.append("<div class='summary-item'>");
            summaryHtml.append("<div class='summary-label'>").append(entry.getKey()).append("</div>");
            summaryHtml.append("<div class='summary-value'>").append(entry.getValue()).append("</div>");
            summaryHtml.append("</div>");
        }

        summaryHtml.append("</div>");
        summaryHtml.append("</div>");
        return summaryHtml.toString();
    }

    /**
     * 生成章节内容
     */
    private String generateSection(ReportSectionVO section) {
        StringBuilder sectionHtml = new StringBuilder();
        sectionHtml.append("<div class='section'>");
        sectionHtml.append("<h2 class='section-title'>").append(section.getTitle()).append("</h2>");

        if (section.getContent() != null) {
            sectionHtml.append("<div class='section-content'>").append(section.getContent()).append("</div>");
        }

        // 渲染表格
        if (section.getTables() != null) {
            for (ReportTableVO table : section.getTables()) {
                sectionHtml.append(generateTable(table));
            }
        }

        sectionHtml.append("</div>");
        return sectionHtml.toString();
    }

    /**
     * 生成表格
     */
    private String generateTable(ReportTableVO table) {
        StringBuilder tableHtml = new StringBuilder();

        if (table.getTitle() != null) {
            tableHtml.append("<h3>").append(table.getTitle()).append("</h3>");
        }

        tableHtml.append("<table class='report-table'>");

        // 表头
        if (table.getColumns() != null && !table.getColumns().isEmpty()) {
            tableHtml.append("<thead><tr>");
            for (Map<String, Object> column : table.getColumns()) {
                String title = (String) column.get("title");
                tableHtml.append("<th>").append(title != null ? title : "").append("</th>");
            }
            tableHtml.append("</tr></thead>");
        }

        // 表格数据
        if (table.getData() != null && !table.getData().isEmpty()) {
            tableHtml.append("<tbody>");
            for (Map<String, Object> row : table.getData()) {
                tableHtml.append("<tr>");
                if (table.getColumns() != null) {
                    for (Map<String, Object> column : table.getColumns()) {
                        String dataIndex = (String) column.get("dataIndex");
                        Object value = row.get(dataIndex);
                        tableHtml.append("<td>").append(value != null ? value.toString() : "-").append("</td>");
                    }
                }
                tableHtml.append("</tr>");
            }
            tableHtml.append("</tbody>");
        }

        tableHtml.append("</table>");
        return tableHtml.toString();
    }

    /**
     * 生成报告尾部
     */
    private String generateReportFooter(ReportDataVO reportData) {
        StringBuilder footer = new StringBuilder();
        footer.append("<div class='report-footer'>");
        footer.append("<p>本报告由智能马术管理系统自动生成</p>");
        footer.append("<p>报告ID: ").append(reportData.getReportId()).append("</p>");
        footer.append("<p>© 2024 SmartAdmin 1024创新实验室</p>");
        footer.append("</div>");
        return footer.toString();
    }
}
