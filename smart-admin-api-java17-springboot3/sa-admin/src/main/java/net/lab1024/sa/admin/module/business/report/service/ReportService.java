package net.lab1024.sa.admin.module.business.report.service;

import net.lab1024.sa.admin.module.business.report.domain.form.ReportExportForm;
import net.lab1024.sa.admin.module.business.report.domain.form.ReportGenerateForm;
import net.lab1024.sa.admin.module.business.report.domain.vo.ReportDataVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 通用报告服务接口
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
public interface ReportService {

    /**
     * 生成报告数据
     *
     * @param form 报告生成表单
     * @return 报告数据
     */
    ResponseDTO<ReportDataVO> generateReport(ReportGenerateForm form);

    /**
     * 导出PDF报告
     *
     * @param form 导出表单
     * @param response HTTP响应
     */
    void exportToPdf(ReportExportForm form, HttpServletResponse response);

    /**
     * 导出Excel报告
     *
     * @param form 导出表单
     * @param response HTTP响应
     */
    void exportToExcel(ReportExportForm form, HttpServletResponse response);

    /**
     * 获取支持的报告类型
     *
     * @return 报告类型
     */
    Integer getSupportedReportType();
}