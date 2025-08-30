package net.lab1024.sa.admin.module.business.product.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 更新商品教练关联表单
 *
 * @Author Claude Code  
 * @Date 2025-08-29
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "更新商品教练关联表单")
public class UpdateProductCoachesForm {

    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Schema(description = "教练ID列表")
    private List<Long> coachIds;

    @Schema(description = "操作人")
    private String operator;
}