package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 员工工作经历 添加表单
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeWorkExperienceAddForm {

    @Schema(description = "员工ID")
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;

    @Schema(description = "公司名称")
    @NotBlank(message = "公司名称不能为空")
    @Length(max = 200, message = "公司名称长度不能超过200")
    private String companyName;

    @Schema(description = "职位")
    @NotBlank(message = "职位不能为空")
    @Length(max = 100, message = "职位长度不能超过100")
    private String position;

    @Schema(description = "部门")
    @Length(max = 100, message = "部门长度不能超过100")
    private String department;

    @Schema(description = "工作描述")
    private String jobDescription;

    @Schema(description = "入职时间")
    @NotNull(message = "入职时间不能为空")
    private LocalDate startDate;

    @Schema(description = "离职时间")
    private LocalDate endDate;

    @Schema(description = "薪资范围")
    @Length(max = 50, message = "薪资范围长度不能超过50")
    private String salaryRange;

    @Schema(description = "排序字段")
    private Integer sortOrder;

    @Schema(description = "备注")
    @Length(max = 500, message = "备注长度不能超过500")
    private String remark;
}