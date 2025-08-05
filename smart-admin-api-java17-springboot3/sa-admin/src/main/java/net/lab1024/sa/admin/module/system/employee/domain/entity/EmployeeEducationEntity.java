package net.lab1024.sa.admin.module.system.employee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工学历信息 实体表
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_employee_education")
public class EmployeeEducationEntity {

    @TableId(type = IdType.AUTO)
    private Long educationId;

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 学历层次
     */
    private String educationLevel;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 专业
     */
    private String major;

    /**
     * 入学时间
     */
    private LocalDate startDate;

    /**
     * 毕业时间
     */
    private LocalDate endDate;

    /**
     * 是否毕业：0 未毕业，1 已毕业
     */
    private Boolean isGraduated;

    /**
     * 学位证书附件URL
     */
    private String degreeCertificateUrl;

    /**
     * 排序字段
     */
    private Integer sortOrder;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除：0 否，1 是
     */
    private Boolean deletedFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}