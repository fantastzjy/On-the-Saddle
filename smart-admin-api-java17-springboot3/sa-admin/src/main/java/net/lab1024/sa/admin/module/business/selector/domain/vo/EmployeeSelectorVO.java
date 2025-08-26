package net.lab1024.sa.admin.module.business.selector.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 员工选择器VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
public class EmployeeSelectorVO {

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "格式化显示标签")
    private String displayLabel;
}