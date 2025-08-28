package net.lab1024.sa.admin.module.business.report.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportDataVO;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportSectionVO;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportTableVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Excel导出服务
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class ReportExcelExportService {

    /**
     * 导出Excel格式的报告
     */
    public void exportToExcel(ReportDataVO reportData, String fileName, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");

            // 对文件名进行URL编码以支持中文
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName + ".xlsx");

            // 创建工作簿
            try (Workbook workbook = new XSSFWorkbook()) {
                // 创建样式
                CellStyle titleStyle = createTitleStyle(workbook);
                CellStyle headerStyle = createHeaderStyle(workbook);
                CellStyle dataStyle = createDataStyle(workbook);

                // 创建报告摘要工作表
                createSummarySheet(workbook, reportData, titleStyle, headerStyle, dataStyle);

                // 创建详细数据工作表
                if (reportData.getSections() != null) {
                    for (ReportSectionVO section : reportData.getSections()) {
                        if (section.getTables() != null && !section.getTables().isEmpty()) {
                            createSectionSheet(workbook, section, titleStyle, headerStyle, dataStyle);
                        }
                    }
                }

                // 写入响应
                try (OutputStream outputStream = response.getOutputStream()) {
                    workbook.write(outputStream);
                    outputStream.flush();
                }

                log.info("导出Excel报告成功，文件名: {}", fileName);
            }

        } catch (IOException e) {
            log.error("导出Excel报告失败", e);
            throw new RuntimeException("导出报告失败", e);
        }
    }

    /**
     * 创建报告摘要工作表
     */
    private void createSummarySheet(Workbook workbook, ReportDataVO reportData,
                                   CellStyle titleStyle, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("报告摘要");

        int rowIndex = 0;

        // 创建标题行
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(reportData.getReportTitle());
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        // 创建副标题行
        if (reportData.getReportSubtitle() != null) {
            Row subtitleRow = sheet.createRow(rowIndex++);
            Cell subtitleCell = subtitleRow.createCell(0);
            subtitleCell.setCellValue(reportData.getReportSubtitle());
            subtitleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 3));
        }

        // 创建生成时间行
        if (reportData.getGenerateTime() != null) {
            Row timeRow = sheet.createRow(rowIndex++);
            Cell timeCell = timeRow.createCell(0);
            timeCell.setCellValue("生成时间：" + reportData.getGenerateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            timeCell.setCellStyle(dataStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 3));
        }

        // 空行
        rowIndex++;

        // 创建摘要数据
        if (reportData.getSummary() != null && !reportData.getSummary().isEmpty()) {
            Row headerRow = sheet.createRow(rowIndex++);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("指标项目");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("数值");
            headerCell2.setCellStyle(headerStyle);

            for (Map.Entry<String, Object> entry : reportData.getSummary().entrySet()) {
                Row dataRow = sheet.createRow(rowIndex++);

                Cell keyCell = dataRow.createCell(0);
                keyCell.setCellValue(entry.getKey());
                keyCell.setCellStyle(dataStyle);

                Cell valueCell = dataRow.createCell(1);
                valueCell.setCellValue(entry.getValue().toString());
                valueCell.setCellStyle(dataStyle);
            }
        }

        // 自动调整列宽
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * 创建章节工作表
     */
    private void createSectionSheet(Workbook workbook, ReportSectionVO section,
                                   CellStyle titleStyle, CellStyle headerStyle, CellStyle dataStyle) {
        String sheetName = section.getTitle().length() > 30 ?
            section.getTitle().substring(0, 30) : section.getTitle();
        Sheet sheet = workbook.createSheet(sheetName);

        int rowIndex = 0;

        // 创建章节标题
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(section.getTitle());
        titleCell.setCellStyle(titleStyle);

        // 空行
        rowIndex++;

        // 创建章节内容
        if (section.getContent() != null && !section.getContent().isEmpty()) {
            Row contentRow = sheet.createRow(rowIndex++);
            Cell contentCell = contentRow.createCell(0);
            // 移除HTML标签
            String cleanContent = section.getContent().replaceAll("<[^>]*>", "");
            contentCell.setCellValue(cleanContent);
            contentCell.setCellStyle(dataStyle);
            rowIndex++;
        }

        // 创建表格数据
        if (section.getTables() != null) {
            for (ReportTableVO table : section.getTables()) {
                rowIndex = createTableInSheet(sheet, table, rowIndex, headerStyle, dataStyle);
                rowIndex += 2; // 添加空行分隔
            }
        }

        // 自动调整列宽
        int maxColumns = getMaxColumnsCount(section);
        for (int i = 0; i < maxColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * 在工作表中创建表格
     */
    private int createTableInSheet(Sheet sheet, ReportTableVO table, int startRowIndex,
                                  CellStyle headerStyle, CellStyle dataStyle) {
        int rowIndex = startRowIndex;

        // 表格标题
        if (table.getTitle() != null) {
            Row tableHeaderRow = sheet.createRow(rowIndex++);
            Cell tableHeaderCell = tableHeaderRow.createCell(0);
            tableHeaderCell.setCellValue(table.getTitle());
            tableHeaderCell.setCellStyle(headerStyle);
            rowIndex++;
        }

        // 表格列头
        if (table.getColumns() != null && !table.getColumns().isEmpty()) {
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < table.getColumns().size(); i++) {
                Map<String, Object> column = table.getColumns().get(i);
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue((String) column.get("title"));
                headerCell.setCellStyle(headerStyle);
            }
        }

        // 表格数据
        if (table.getData() != null && !table.getData().isEmpty()) {
            for (Map<String, Object> row : table.getData()) {
                Row dataRow = sheet.createRow(rowIndex++);
                if (table.getColumns() != null) {
                    for (int i = 0; i < table.getColumns().size(); i++) {
                        Map<String, Object> column = table.getColumns().get(i);
                        String dataIndex = (String) column.get("dataIndex");
                        Object value = row.get(dataIndex);

                        Cell dataCell = dataRow.createCell(i);
                        dataCell.setCellValue(value != null ? value.toString() : "-");
                        dataCell.setCellStyle(dataStyle);
                    }
                }
            }
        }

        return rowIndex;
    }

    /**
     * 获取章节中表格的最大列数
     */
    private int getMaxColumnsCount(ReportSectionVO section) {
        int maxColumns = 4; // 默认最小列数
        if (section.getTables() != null) {
            for (ReportTableVO table : section.getTables()) {
                if (table.getColumns() != null) {
                    maxColumns = Math.max(maxColumns, table.getColumns().size());
                }
            }
        }
        return maxColumns;
    }

    /**
     * 创建标题样式
     */
    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 创建表头样式
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 设置背景色
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    /**
     * 创建数据样式
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }
}
