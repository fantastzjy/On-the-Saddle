package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信用户信息VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "微信用户信息")
public class WechatUserInfoVO {

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户头像")
    private String avatarUrl;

    @Schema(description = "用户性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer gender;

    @Schema(description = "用户所在城市")
    private String city;

    @Schema(description = "用户所在省份")
    private String province;

    @Schema(description = "用户所在国家")
    private String country;

    @Schema(description = "用户语言")
    private String language;
}