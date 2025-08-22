package net.lab1024.sa.admin.module.business.member.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信小程序登录响应VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "微信小程序登录响应")
public class WechatJscode2SessionVO {

    @Schema(description = "用户唯一标识")
    @JsonProperty("openid")
    private String openId;

    @Schema(description = "会话密钥")
    @JsonProperty("session_key")
    private String sessionKey;

    @Schema(description = "用户在开放平台的唯一标识符")
    @JsonProperty("unionid")
    private String unionId;

    @Schema(description = "错误码")
    @JsonProperty("errcode")
    private Integer errCode;

    @Schema(description = "错误信息")
    @JsonProperty("errmsg")
    private String errMsg;

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return errCode == null || errCode == 0;
    }
}