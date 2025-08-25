package net.lab1024.sa.admin.module.openapi.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 小程序统一登录响应VO
 * 支持会员(usr)和教练(cc)登录
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "小程序统一登录响应")
public class AppLoginVO {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "角色类型：usr=会员, cc=教练")
    private String role;
}