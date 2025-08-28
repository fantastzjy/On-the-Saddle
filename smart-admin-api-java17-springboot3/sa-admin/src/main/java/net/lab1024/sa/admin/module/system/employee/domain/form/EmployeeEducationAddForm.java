package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 员工学历信息 添加表单
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeEducationAddForm {

    @Schema(description = "员工ID")
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;

    @Schema(description = "学历层次")
    @NotBlank(message = "学历层次不能为空")
    @Length(max = 20, message = "学历层次长度不能超过20")
    private String educationLevel;

    @Schema(description = "学校名称")
    @NotBlank(message = "学校名称不能为空")
    @Length(max = 100, message = "学校名称长度不能超过100")
    private String schoolName;

    @Schema(description = "专业")
    @Length(max = 100, message = "专业长度不能超过100")
    private String major;

    @Schema(description = "入学时间")
    @NotNull(message = "入学时间不能为空")
    private LocalDate startDate;

    @Schema(description = "毕业时间")
    private LocalDate endDate;

    @Schema(description = "是否毕业")
    private Boolean isGraduated;

    @Schema(description = "学位证书附件URL")
    private String degreeCertificateUrl;

    @Schema(description = "排序字段")
    private Integer sortOrder;

    @Schema(description = "备注")
    @Length(max = 500, message = "备注长度不能超过500")
    private String remark;
}
