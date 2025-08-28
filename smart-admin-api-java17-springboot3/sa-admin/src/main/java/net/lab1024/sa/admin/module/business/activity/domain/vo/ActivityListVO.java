package net.lab1024.sa.admin.module.business.activity.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动列表VO
 *
 * @Author 1024创新实验室
 * @Date 2025-08-28
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "活动列表VO")
public class ActivityListVO {

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "活动名称")
    private String activityName;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}