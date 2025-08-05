package net.lab1024.sa.admin.module.system.employee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工工作经历 实体表
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_employee_work_experience")
public class EmployeeWorkExperienceEntity {

    @TableId(type = IdType.AUTO)
    private Long workExperienceId;

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 职位
     */
    private String position;

    /**
     * 部门
     */
    private String department;

    /**
     * 工作描述
     */
    private String jobDescription;

    /**
     * 入职时间
     */
    private LocalDate startDate;

    /**
     * 离职时间（为空表示当前工作）
     */
    private LocalDate endDate;

    /**
     * 薪资范围
     */
    private String salaryRange;

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