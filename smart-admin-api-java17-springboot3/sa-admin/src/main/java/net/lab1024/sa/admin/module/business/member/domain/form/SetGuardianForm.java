package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

/**
 * 设置监护人表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "设置监护人表单")
public class SetGuardianForm {

    @Schema(description = "家庭组ID")
    @DataTracerFieldLabel("家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Long familyGroupId;

    @Schema(description = "会员ID")
    @DataTracerFieldLabel("会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Schema(description = "是否设为监护人，1-是，0-否")
    @DataTracerFieldLabel("是否监护人")
    @NotNull(message = "监护人标识不能为空")
    private Integer isGuardian;
}
