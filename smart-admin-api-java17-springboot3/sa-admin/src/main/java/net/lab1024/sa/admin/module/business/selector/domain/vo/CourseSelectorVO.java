package net.lab1024.sa.admin.module.business.selector.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 课程选择器VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
public class CourseSelectorVO {

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "课程类型编码")
    private String courseType;

    @Schema(description = "课程类型名称")
    private String courseTypeName;

    @Schema(description = "格式化显示标签")
    private String displayLabel;
}