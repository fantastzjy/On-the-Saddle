package net.lab1024.sa.admin.module.business.coach.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教练信息VO
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "教练信息")
public class CoachAppInfoVO {

    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "从业时间")
    private LocalDateTime entryDate;

    @Schema(description = "专长领域")
    private String specialties;

    @Schema(description = "个人介绍")
    private String introduction;

    @Schema(description = "教练证号码")
    private String coachCertNo;

    @Schema(description = "骑手证号码")
    private String riderCertNo;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    // 教练证书扁平化字段（4个类别）
    @Schema(description = "教练星级")
    private Integer coachStarLevel;

    @Schema(description = "教练星级证书图片")
    private String coachStarCertImages;

    @Schema(description = "教练场地障碍星级")
    private Integer coachShowJumpingLevel;

    @Schema(description = "教练场地障碍证书图片")
    private String coachShowJumpingImages;

    @Schema(description = "教练盛装舞步星级")
    private Integer coachDressageLevel;

    @Schema(description = "教练盛装舞步证书图片")
    private String coachDressageImages;

    @Schema(description = "教练三项赛星级")
    private Integer coachEventingLevel;

    @Schema(description = "教练三项赛证书图片")
    private String coachEventingImages;

    // 骑手证书扁平化字段（3个类别）
    @Schema(description = "骑手场地障碍等级")
    private Integer riderShowJumpingLevel;

    @Schema(description = "骑手场地障碍证书图片")
    private String riderShowJumpingImages;

    @Schema(description = "骑手盛装舞步等级")
    private Integer riderDressageLevel;

    @Schema(description = "骑手盛装舞步证书图片")
    private String riderDressageImages;

    @Schema(description = "骑手三项赛等级")
    private Integer riderEventingLevel;

    @Schema(description = "骑手三项赛证书图片")
    private String riderEventingImages;
}
