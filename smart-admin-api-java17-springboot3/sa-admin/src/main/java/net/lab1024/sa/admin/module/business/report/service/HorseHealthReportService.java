package net.lab1024.sa.admin.module.business.report.service;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseListVO;
import net.lab1024.sa.admin.module.business.report.constant.ReportTypeEnum;
import net.lab1024.sa.admin.module.business.report.domain.form.ReportExportForm;
import net.lab1024.sa.admin.module.business.report.domain.form.ReportGenerateForm;
import net.lab1024.sa.admin.module.business.report.domain.vo.*;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 马匹健康报告服务
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class HorseHealthReportService implements ReportService {

	@Autowired
	private HorseDao horseDao;

	@Autowired
	private ReportPdfExportService reportPdfExportService;

	@Override
	public ResponseDTO<ReportDataVO> generateReport(ReportGenerateForm form) {
		try {
			// 解析参数
			HorseHealthReportParam param = parseReportParam(form);

			// 生成报告数据
			ReportDataVO reportData = buildHorseInfoReport(param);

			log.info("生成马匹健康报告成功，参数: {}", JSON.toJSONString(param));
			return ResponseDTO.ok(reportData);

		} catch (Exception e) {
			log.error("生成马匹健康报告失败", e);
			return ResponseDTO.userErrorParam("生成报告失败: " + e.getMessage());
		}
	}

	@Override
	public void exportToPdf(ReportExportForm form, HttpServletResponse response) {
		try {
			// 生成报告数据
			ReportGenerateForm generateForm = new ReportGenerateForm();
			generateForm.setReportType(form.getReportType());
			generateForm.setParams(form.getParams());

			ResponseDTO<ReportDataVO> reportResponse = generateReport(generateForm);
			if (!reportResponse.getOk()) {
				throw new RuntimeException("生成报告数据失败: " + reportResponse.getMsg());
			}

			// 生成文件名
			String fileName = form.getFileName();
			if (fileName == null || fileName.trim().isEmpty()) {
				fileName = "马匹健康报告_" + System.currentTimeMillis();
			}

			// 导出PDF（实际是HTML格式）
			reportPdfExportService.exportToHtmlPdf(reportResponse.getData(), fileName, response);

		} catch (Exception e) {
			log.error("导出PDF报告失败", e);
			throw new RuntimeException("导出PDF报告失败", e);
		}
	}

	@Override
	public void exportToExcel(ReportExportForm form, HttpServletResponse response) {
		throw new UnsupportedOperationException("Excel导出功能已移除，请使用PDF导出");
	}

	@Override
	public Integer getSupportedReportType() {
		return ReportTypeEnum.HORSE_HEALTH_MONTHLY.getValue();
	}

	/**
	 * 解析报告参数
	 */
	private HorseHealthReportParam parseReportParam(ReportGenerateForm form) {
		HorseHealthReportParam param = new HorseHealthReportParam();

		Map<String, Object> params = form.getParams();
		if (params != null) {
			param.setYear((Integer) params.get("year"));
			param.setMonth((Integer) params.get("month"));
			param.setClubId(Long.valueOf(params.get("clubId").toString()));

			// 优先使用前端传递的具体时间范围
			if (params.get("startDate") != null && params.get("endDate") != null) {
				String startDateStr = params.get("startDate").toString();
				String endDateStr = params.get("endDate").toString();
				param.setStartDate(LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				param.setEndDate(LocalDateTime.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			} else if (param.getYear() != null && param.getMonth() != null) {
				// 兜底：如果没有具体时间范围，使用年月计算
				param.setStartDate(LocalDateTime.of(param.getYear(), param.getMonth(), 1, 0, 0, 0));
				param.setEndDate(param.getStartDate().plusMonths(1).minusSeconds(1));
			}
		}

		return param;
	}

	/**
	 * 构建马匹信息报告
	 */
	private ReportDataVO buildHorseInfoReport(HorseHealthReportParam param) {
		ReportDataVO report = new ReportDataVO();
		report.setReportId(generateReportId(param));
		report.setReportTitle("马匹健康报告");
		// 显示具体的时间范围
		String startDateStr = param.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String endDateStr = param.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		report.setReportSubtitle(param.getYear() + "年" + param.getMonth() + "月 (" + startDateStr + " 至 " + endDateStr + ")");
		report.setGenerateTime(LocalDateTime.now());

		// 构建报告章节
		List<ReportSectionVO> sections = new ArrayList<>();
		sections.add(buildHorseInfoSection(param));     // 马匹信息章节
		report.setSections(sections);

		return report;
	}

	/**
	 * 生成报告ID
	 */
	private String generateReportId(HorseHealthReportParam param) {
		return String.format("HORSE_INFO_%d_%02d_%d_%d",
				param.getYear(), param.getMonth(), param.getClubId(), System.currentTimeMillis());
	}

	/**
	 * 构建马匹信息章节
	 */
	private ReportSectionVO buildHorseInfoSection(HorseHealthReportParam param) {
		ReportSectionVO section = new ReportSectionVO();
		section.setSectionId("horseInfo");
		section.setTitle(""); // 移除章节标题
		section.setContent(""); // 移除描述文字

		// 创建马匹信息表格
		ReportTableVO table = new ReportTableVO();
		table.setTableId("horseInfoTable");
		table.setTitle(""); // 移除表格标题

		// 定义表格列 - 只显示指定的9个字段
		List<Map<String, Object>> columns = Arrays.asList(
				Map.of("title", "马主", "dataIndex", "ownerName", "align", "center"),
				Map.of("title", "马匹名字", "dataIndex", "horseName", "align", "center"),
				Map.of("title", "血统", "dataIndex", "breed", "align", "center"),
				Map.of("title", "芯片号", "dataIndex", "chipNo", "align", "center"),
				Map.of("title", "马匹年龄", "dataIndex", "horseAge", "align", "center"),
				Map.of("title", "责任教练", "dataIndex", "responsibleCoachName", "align", "center"),
				Map.of("title", "责任马工", "dataIndex", "responsibleGroomName", "align", "center"),
				Map.of("title", "饲养费金额", "dataIndex", "boardingFeeFormatted", "align", "center"),
				Map.of("title", "饲养费有效期", "dataIndex", "boardingEndDateFormatted", "align", "center")
		);
		table.setColumns(columns);

		// 获取马匹数据
		// TODO: 当前使用update_time过滤，后续可能需要调整为业务相关的时间字段
		List<HorseListVO> horseList = horseDao.queryHorseInfoForReport(1L, param.getStartDate(), param.getEndDate()); // 默认俱乐部ID = 1
		List<Map<String, Object>> tableData = horseList.stream().map(horse -> {
			Map<String, Object> row = new HashMap<>();

			// 马主：根据马匹类型判断
			String ownerName = "";
			if (horse.getHorseType() == 1) {
				ownerName = horse.getClubName(); // 俱乐部马
			} else if (horse.getHorseType() == 2) {
				ownerName = horse.getOwnerName() != null ? horse.getOwnerName() : "未设置"; // 马主马
			} else if (horse.getHorseType() == 3) {
				ownerName = horse.getResponsibleCoachName() != null ? horse.getResponsibleCoachName() : "未设置"; // 教练马
			}
			row.put("ownerName", ownerName);

			row.put("horseName", horse.getHorseName() != null ? horse.getHorseName() : "-");
			row.put("breed", horse.getBreed() != null ? horse.getBreed() : "-");
			row.put("chipNo", horse.getChipNo() != null ? horse.getChipNo() : "-");

			// 计算马匹年龄
			String horseAge = "-";
			if (horse.getBirthDate() != null) {
				LocalDate birthDate = horse.getBirthDate().toLocalDate();
				LocalDate now = LocalDate.now();
				int age = Period.between(birthDate, now).getYears();
				horseAge = age + "岁";
			}
			row.put("horseAge", horseAge);

			row.put("responsibleCoachName", horse.getResponsibleCoachName() != null ? horse.getResponsibleCoachName() : "-");
			row.put("responsibleGroomName", horse.getResponsibleGroomName() != null ? horse.getResponsibleGroomName() : "-");

			// 饲养费金额（只对马主马显示）
			String boardingFeeFormatted = "-";
			if (horse.getHorseType() == 2 && horse.getBoardingFee() != null) {
				boardingFeeFormatted = "¥" + horse.getBoardingFee().setScale(2, RoundingMode.HALF_UP).toString() + "/月";
			}
			row.put("boardingFeeFormatted", boardingFeeFormatted);

			// 饲养费有效期（只对马主马显示）
			String boardingEndDateFormatted = "-";
			if (horse.getHorseType() == 2 && horse.getBoardingEndDate() != null) {
				boardingEndDateFormatted = horse.getBoardingEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
			row.put("boardingEndDateFormatted", boardingEndDateFormatted);

			return row;
		}).collect(Collectors.toList());

		table.setData(tableData);
		section.setTables(Collections.singletonList(table));

		return section;
	}
}
