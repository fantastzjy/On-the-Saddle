package net.lab1024.sa.admin.module.business.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;

import java.io.Serializable;

/**
 * 请求会员登录信息
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class RequestMember implements RequestUser, Serializable {

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员编号")
    private String memberNo;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "所属俱乐部ID")
    private Long clubId;

    @Schema(description = "是否为会籍会员")
    private Integer isMembership;

    @Schema(description = "会籍状态")
    private Integer membershipStatus;

    @Schema(description = "注册状态")
    private Integer registrationStatus;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "请求IP")
    private String ip;

    @Schema(description = "请求user-agent")
    private String userAgent;

    @Override
    public Long getUserId() {
        return memberId;
    }

    @Override
    public String getUserName() {
        return actualName;
    }

    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.MEMBER;
    }
}