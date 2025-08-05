package net.lab1024.sa.admin.module.system.employee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工学历信息 VO
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeEducationVO {

    @Schema(description = "学历ID")
    private Long educationId;

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "学历层次")
    private String educationLevel;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "入学时间")
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
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}