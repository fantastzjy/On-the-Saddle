package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 教练信息查询表单
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "教练信息查询表单")
public class CoachInfoQueryForm {

    @Schema(description = "教练编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "教练编号不能为空")
    private String coachNo;
}