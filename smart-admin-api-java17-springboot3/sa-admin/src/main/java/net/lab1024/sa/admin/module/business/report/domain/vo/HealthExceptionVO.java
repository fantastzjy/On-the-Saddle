package net.lab1024.sa.admin.module.business.report.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 健康异常记录VO
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "健康异常记录")
public class HealthExceptionVO {

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马匹名称")
    private String horseName;

    @Schema(description = "马匹编号")
    private String horseNumber;

    @Schema(description = "计划类型")
    private String planType;

    @Schema(description = "计划类型名称")
    private String planTypeName;

    @Schema(description = "应执行时间")
    private LocalDateTime dueDatetime;

    @Schema(description = "实际执行时间")
    private LocalDateTime executionDatetime;

    @Schema(description = "逾期天数")
    private Integer overdueDays;

    @Schema(description = "异常类型：overdue-逾期, missing-缺失")
    private String exceptionType;

    @Schema(description = "备注")
    private String remark;
}