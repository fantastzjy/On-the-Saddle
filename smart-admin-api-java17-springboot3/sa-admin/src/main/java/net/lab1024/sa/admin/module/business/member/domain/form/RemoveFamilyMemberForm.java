package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 移除家庭成员表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "移除家庭成员表单")
public class RemoveFamilyMemberForm {

    @Schema(description = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Long familyGroupId;

    @Schema(description = "会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;
}