package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 养护统计VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "养护统计信息")
public class CareStatisticsVO {

    @Schema(description = "计划类型名称", example = "疫苗")
    private String planTypeName;

    @Schema(description = "已完成次数", example = "2")
    private Integer completedCount;

    @Schema(description = "描述信息", example = "疫苗已打2针")
    private String description;
}