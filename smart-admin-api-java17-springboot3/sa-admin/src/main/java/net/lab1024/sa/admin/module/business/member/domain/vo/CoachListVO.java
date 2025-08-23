package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 教练列表信息VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "教练列表信息")
public class CoachListVO {

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "教练姓名")
    private String actualName;

    @Schema(description = "性别：1-男 2-女")
    private Integer gender;

    @Schema(description = "骑手等级标签列表")
    private List<String> riderLevelTags;

    @Schema(description = "课时费")
    private BigDecimal coachFee;

    @Schema(description = "从业时长（年）")
    private Integer workingYears;

    @Schema(description = "教练介绍")
    private String introduction;

    @Schema(description = "不可用时间列表")
    private List<UnavailableTimeSlotVO> unavailableTimeSlots;
}
