package net.lab1024.sa.admin.module.openapi.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 语音请求表单
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "语音请求表单")
public class VoiceRequestForm {
    
    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long memberId;
    
    @Schema(description = "俱乐部ID", required = true)
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;
    
    @Schema(description = "语音文本内容", required = true)
    @NotNull(message = "语音文本内容不能为空")
    private String speechText;
}
