package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 搜索家庭组表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyGroupSearchForm {

    @Schema(description = "搜索类型: familyName-家庭名称, memberName-成员姓名, memberPhone-成员手机号")
    @NotBlank(message = "搜索类型不能为空")
    private String searchType;

    @Schema(description = "搜索关键字")
    @NotBlank(message = "搜索关键字不能为空")
    private String keyword;

    @Schema(description = "俱乐部ID")
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;
}
