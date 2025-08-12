package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 会员新增表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class MemberCreateForm {

    @Schema(description = "会员编号")
    private String memberNo;

    @Schema(description = "微信unionId")
    private String unionId;

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "登录账号")
    @Size(min = 4, max = 50, message = "登录账号长度为4-50个字符")
    private String loginName;

    @Schema(description = "登录密码")
    private String loginPwd;

    @Schema(description = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String actualName;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "邮箱")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "性别: 0-未知 1-男 2-女")
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "所属俱乐部ID")
    @NotNull(message = "所属俱乐部不能为空")
    private Long clubId;

    @Schema(description = "是否为会籍会员: 1-是 0-否")
    private Integer isMembership;

    @Schema(description = "会籍有效期")
    private LocalDate membershipExpireDate;

    @Schema(description = "身份证号")
    @Pattern(regexp = "^$|^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$", 
            message = "身份证号格式不正确")
    private String idCardNo;

    @Schema(description = "骑手证号")
    private String riderCertNo;

    @Schema(description = "注册状态: 0-未激活 1-已注册")
    private Integer registrationStatus;

    @Schema(description = "是否由监护人创建: 1-是 0-否")
    private Integer createdByGuardian;

    @Schema(description = "是否禁用: 0-启用 1-禁用")
    private Integer disabledFlag;

    @Schema(description = "扩展数据JSON格式")
    private String profileData;
}