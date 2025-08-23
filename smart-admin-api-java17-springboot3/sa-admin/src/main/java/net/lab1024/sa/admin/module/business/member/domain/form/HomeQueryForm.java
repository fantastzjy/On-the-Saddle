package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 首页查询表单
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "首页查询表单")
public class HomeQueryForm {

    @Schema(description = "俱乐部编码", required = true, example = "DEMO_CLUB_001")
    @NotNull(message = "俱乐部编码不能为空")
    private String clubCode;
}
