package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程列表信息VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "课程列表信息")
public class CourseListVO {

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "课程编码")
    private String courseCode;

    @Schema(description = "基础单价")
    private BigDecimal basePrice;
}
