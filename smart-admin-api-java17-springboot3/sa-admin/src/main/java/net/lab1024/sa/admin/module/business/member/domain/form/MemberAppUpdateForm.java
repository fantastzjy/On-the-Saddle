package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 会员信息更新表单
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "会员信息更新表单")
public class MemberAppUpdateForm {

    @Schema(description = "会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Schema(description = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    private String actualName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "身份证号")
    private String idCardNo;

    @Schema(description = "骑手证号")
    private String riderCertNo;

    @Schema(description = "默认课程级别")
    private String defaultCourseLevel;

    @Schema(description = "扩展数据JSON格式")
    private String profileData;
}