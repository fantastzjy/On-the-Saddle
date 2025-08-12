package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员详情VO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class MemberDetailVO {

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员编号")
    private String memberNo;

    @Schema(description = "微信unionId")
    private String unionId;

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "登录账号")
    private String loginName;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "性别: 0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "所属俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "是否为会籍会员: 1-是 0-否")
    private Integer isMembership;

    @Schema(description = "会籍有效期")
    private LocalDate membershipExpireDate;

    @Schema(description = "会籍剩余天数")
    private Long membershipRemainingDays;

    @Schema(description = "身份证号")
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

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}