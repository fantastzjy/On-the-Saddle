package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 家庭组更新表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyGroupUpdateForm {

    @Schema(description = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Long familyGroupId;

    @Schema(description = "家庭名称")
    @Size(max = 50, message = "家庭名称长度不能超过50个字符")
    private String familyName;

    @Schema(description = "主要联系人ID")
    private Long mainContactId;

    @Schema(description = "家庭描述")
    @Size(max = 200, message = "家庭描述长度不能超过200个字符")
    private String description;
}