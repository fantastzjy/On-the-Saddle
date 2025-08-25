package net.lab1024.sa.admin.module.business.report.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.report.constant.ReportTypeEnum;
import net.lab1024.sa.admin.module.business.report.domain.form.ReportExportForm;
import net.lab1024.sa.admin.module.business.report.domain.form.ReportGenerateForm;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportDataVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报告管理服务
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class ReportManagerService {

    private final Map<Integer, ReportService> reportServiceMap = new HashMap<>();

    @Autowired
    public ReportManagerService(List<ReportService> reportServices) {
        // 自动注册所有报告服务
        for (ReportService reportService : reportServices) {
            reportServiceMap.put(reportService.getSupportedReportType(), reportService);
        }
        log.info("注册报告服务完成，共{}个服务", reportServiceMap.size());
    }

    /**
     * 生成报告
     */
    public ResponseDTO<ReportDataVO> generateReport(ReportGenerateForm form) {
        ReportService reportService = getReportService(form.getReportType());
        if (reportService == null) {
            return ResponseDTO.userErrorParam("不支持的报告类型: " + form.getReportType());
        }

        try {
            return reportService.generateReport(form);
        } catch (Exception e) {
            log.error("生成报告失败，报告类型: {}", form.getReportType(), e);
            return ResponseDTO.userErrorParam("生成报告失败: " + e.getMessage());
        }
    }

    /**
     * 导出PDF报告
     */
    public void exportToPdf(ReportExportForm form, HttpServletResponse response) {
        ReportService reportService = getReportService(form.getReportType());
        if (reportService == null) {
            throw new RuntimeException("不支持的报告类型: " + form.getReportType());
        }

        try {
            reportService.exportToPdf(form, response);
        } catch (Exception e) {
            log.error("导出PDF报告失败，报告类型: {}", form.getReportType(), e);
            throw new RuntimeException("导出PDF报告失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导出Excel报告
     */
    public void exportToExcel(ReportExportForm form, HttpServletResponse response) {
        ReportService reportService = getReportService(form.getReportType());
        if (reportService == null) {
            throw new RuntimeException("不支持的报告类型: " + form.getReportType());
        }

        try {
            reportService.exportToExcel(form, response);
        } catch (Exception e) {
            log.error("导出Excel报告失败，报告类型: {}", form.getReportType(), e);
            throw new RuntimeException("导出Excel报告失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取支持的报告类型列表
     */
    public ResponseDTO<Map<String, Object>> getSupportedReportTypes() {
        Map<String, Object> result = new HashMap<>();
        for (ReportTypeEnum typeEnum : ReportTypeEnum.values()) {
            if (reportServiceMap.containsKey(typeEnum.getValue())) {
                result.put(typeEnum.getValue().toString(), typeEnum.getDesc());
            }
        }
        return ResponseDTO.ok(result);
    }

    private ReportService getReportService(Integer reportType) {
        return reportServiceMap.get(reportType);
    }
}