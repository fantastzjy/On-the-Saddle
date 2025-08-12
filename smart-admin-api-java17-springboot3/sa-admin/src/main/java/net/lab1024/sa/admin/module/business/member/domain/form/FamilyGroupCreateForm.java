package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 家庭组创建表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyGroupCreateForm {

    @Schema(description = "家庭名称")
    @NotBlank(message = "家庭名称不能为空")
    @Size(max = 50, message = "家庭名称长度不能超过50个字符")
    private String familyName;

    @Schema(description = "俱乐部ID")
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;

    @Schema(description = "主要联系人会员ID")
    @NotNull(message = "主要联系人不能为空")
    private Long memberId;

    @Schema(description = "家庭描述")
    @Size(max = 200, message = "家庭描述长度不能超过200个字符")
    private String description;
}