package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 现有会员加入家庭组表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class JoinFamilyGroupForm {

    @Schema(description = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Long familyGroupId;

    @Schema(description = "会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Schema(description = "是否监护人: 1-是 0-否")
    private Integer isGuardian = 0;

    @Schema(description = "加入日期")
    private LocalDate joinDate;

    @Schema(description = "关系备注")
    @Size(max = 50, message = "关系备注长度不能超过50个字符")
    private String remark;
}
