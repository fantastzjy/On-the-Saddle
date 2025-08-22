package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 课程级别信息VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "课程级别信息")
public class CourseLevelInfoVO {

    @Schema(description = "级别编码")
    private String levelCode;

    @Schema(description = "级别名称")
    private String levelName;

    @Schema(description = "级别描述")
    private String description;
}