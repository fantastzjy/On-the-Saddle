package net.lab1024.sa.admin.module.business.activity.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 活动更新表单
 * 严格按照数据库字段：m_product + m_product_activity
 *
 * @Author 1024创新实验室
 * @Date 2025-08-28
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "活动更新表单")
public class ActivityUpdateForm {

    @Schema(description = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    // ========================================
    // m_product 字段 (商品名称将自动更新为活动名称)
    // ========================================

    // ========================================
    // m_product_activity 字段
    // ========================================

    @Schema(description = "活动名称（最多5个字）")
    @NotBlank(message = "活动名称不能为空")
    private String activityName;

    @Schema(description = "活动详情全文介绍")
    @NotBlank(message = "活动详情不能为空")
    private String activityDetails;

    @Schema(description = "活动开始时间")
    @NotBlank(message = "活动开始时间不能为空")
    private String startTime;

    @Schema(description = "活动结束时间")
    @NotBlank(message = "活动结束时间不能为空")
    private String endTime;

    @Schema(description = "活动地点")
    @NotBlank(message = "活动地点不能为空")
    private String activityLocation;

    @Schema(description = "活动单价")
    @NotNull(message = "活动价格不能为空")
    private BigDecimal price;

    @Schema(description = "最大参与人数")
    @NotNull(message = "最大参与人数不能为空")
    private Integer maxParticipants;

    @Schema(description = "活动规则")
    @NotBlank(message = "活动规则不能为空")
    private String activityRule;

    @Schema(description = "详情图片地址列表JSON格式（最多9张）")
    private String detailImages;
}