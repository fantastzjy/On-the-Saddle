package net.lab1024.sa.admin.module.system.employee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工工作经历 VO
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeWorkExperienceVO {

    @Schema(description = "工作经历ID")
    private Long workExperienceId;

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "职位")
    private String position;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "工作描述")
    private String jobDescription;

    @Schema(description = "入职时间")
    private LocalDate startDate;

    @Schema(description = "离职时间")
    private LocalDate endDate;

    @Schema(description = "薪资范围")
    private String salaryRange;

    @Schema(description = "排序字段")
    private Integer sortOrder;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}