package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 医疗信息VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "医疗信息")
public class MedicalInfoVO {

    @Schema(description = "计划类型名称", example = "疫苗")
    private String planTypeName;

    @Schema(description = "下次执行时间")
    private LocalDateTime nextExecuteTime;
}