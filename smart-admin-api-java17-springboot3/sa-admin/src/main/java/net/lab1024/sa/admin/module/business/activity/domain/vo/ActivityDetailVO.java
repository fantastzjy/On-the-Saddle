package net.lab1024.sa.admin.module.business.activity.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动详情VO
 *
 * @Author 1024创新实验室
 * @Date 2025-08-28
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "活动详情VO")
public class ActivityDetailVO {

    // ========================================
    // m_product 字段
    // ========================================

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品编码")
    private String productCode;

    // ========================================
    // m_product_activity 字段
    // ========================================

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "活动详情")
    private String activityDetails;

    @Schema(description = "活动开始时间")
    private LocalDateTime startTime;

    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;

    @Schema(description = "活动地点")
    private String activityLocation;

    @Schema(description = "活动价格")
    private BigDecimal price;

    @Schema(description = "最大参与人数")
    private Integer maxParticipants;

    @Schema(description = "活动规则")
    private String activityRule;

    @Schema(description = "详情图片JSON")
    private String detailImages;

    // ========================================
    // 审计字段
    // ========================================

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}