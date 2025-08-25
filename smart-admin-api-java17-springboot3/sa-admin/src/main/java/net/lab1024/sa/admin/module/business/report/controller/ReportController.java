package net.lab1024.sa.admin.module.business.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.report.domain.form.ReportExportForm;
import net.lab1024.sa.admin.module.business.report.domain.form.ReportGenerateForm;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportDataVO;
import net.lab1024.sa.admin.module.business.report.service.ReportManagerService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;

/**
 * 报告管理控制器
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/report")
@Tag(name = "报告管理")
public class ReportController {

    @Autowired
    private ReportManagerService reportManagerService;

    @Operation(summary = "生成报告")
    @PostMapping("/generate")
    public ResponseDTO<ReportDataVO> generateReport(@RequestBody @Valid ReportGenerateForm form) {
        return reportManagerService.generateReport(form);
    }

    @Operation(summary = "导出PDF报告")
    @PostMapping("/export/pdf")
    public void exportToPdf(@RequestBody @Valid ReportExportForm form,
                           HttpServletResponse response) {
        form.setExportFormat("pdf");
        reportManagerService.exportToPdf(form, response);
    }

    @Operation(summary = "导出Excel报告")
    @PostMapping("/export/excel")
    public void exportToExcel(@RequestBody @Valid ReportExportForm form,
                             HttpServletResponse response) {
        form.setExportFormat("excel");
        reportManagerService.exportToExcel(form, response);
    }

    @Operation(summary = "获取支持的报告类型")
    @GetMapping("/types")
    public ResponseDTO<Map<String, Object>> getSupportedReportTypes() {
        return reportManagerService.getSupportedReportTypes();
    }
}