package net.lab1024.sa.admin.module.openapi.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 小程序统一登录表单
 * 支持会员(usr)和教练(cc)登录
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "小程序统一登录表单")
public class MemberAppLoginForm {

    @Schema(description = "微信登录code")
    @NotBlank(message = "微信登录code不能为空")
    private String code;

    @Schema(description = "加密数据")
    private String encryptedData;

    @Schema(description = "加密算法的初始向量")
    private String iv;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "登录角色类型：usr=会员, cc=教练")
    @NotBlank(message = "登录角色不能为空")
    private String role;
}
