package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动列表信息VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "活动列表信息")
public class ActivityListVO {

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "活动详情")
    private String activityDetail;

    @Schema(description = "活动开始时间")
    private LocalDateTime startTime;

    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;

    @Schema(description = "活动地点")
    private String location;

    @Schema(description = "活动单价")
    private BigDecimal price;

    @Schema(description = "退款规则")
    private String refundRule;

    @Schema(description = "详情图片列表")
    private List<String> detailImages;
}