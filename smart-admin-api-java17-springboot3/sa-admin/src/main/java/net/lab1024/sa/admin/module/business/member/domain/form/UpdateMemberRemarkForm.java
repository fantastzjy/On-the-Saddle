package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新家庭成员关系备注表单
 *
 * @Author Claude Code
 * @Date 2025-08-28
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class UpdateMemberRemarkForm {

    @Schema(description = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Long familyGroupId;

    @Schema(description = "会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Schema(description = "关系备注")
    @Size(max = 100, message = "关系备注长度不能超过100个字符")
    private String remark;
}