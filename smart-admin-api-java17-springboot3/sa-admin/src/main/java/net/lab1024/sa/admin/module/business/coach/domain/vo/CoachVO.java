package net.lab1024.sa.admin.module.business.coach.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教练详情VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class CoachVO {

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "关联用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "头像照片地址")
    private String avatarUrl;

    @Schema(description = "生日")
    private LocalDate birthDate;

    @Schema(description = "身份证号码")
    private String idCard;

    @Schema(description = "身份证正面照片地址")
    private String idCardFrontImg;

    @Schema(description = "身份证反面照片地址")
    private String idCardBackImg;

    @Schema(description = "入行时间")
    private LocalDateTime entryDate;

    @Schema(description = "专长领域")
    private String specialties;

    @Schema(description = "个人介绍")
    private String introduction;

    @Schema(description = "骑手证号码")
    private String riderCertNo;

    @Schema(description = "星级教练证号码")
    private String coachCertNo;

    // 教练证书扫平化字段（4个类别）
    @Schema(description = "教练星级")
    private Integer coachStarLevel;
    @Schema(description = "教练星级证书图片JSON")
    private String coachStarCertImages;

    @Schema(description = "教练场地障碍星级")
    private Integer coachShowJumpingLevel;
    @Schema(description = "教练场地障碍证书图片JSON")
    private String coachShowJumpingImages;

    @Schema(description = "教练盛装舞步星级")
    private Integer coachDressageLevel;
    @Schema(description = "教练盛装舞步证书图片JSON")
    private String coachDressageImages;

    @Schema(description = "教练三项赛星级")
    private Integer coachEventingLevel;
    @Schema(description = "教练三项赛证书图片JSON")
    private String coachEventingImages;

    // 骑手证书扫平化字段（3个类别）
    @Schema(description = "骑手场地障碍等级")
    private Integer riderShowJumpingLevel;
    @Schema(description = "骑手场地障碍证书图片JSON")
    private String riderShowJumpingImages;

    @Schema(description = "骑手盛装舞步等级")
    private Integer riderDressageLevel;
    @Schema(description = "骑手盛装舞步证书图片JSON")
    private String riderDressageImages;

    @Schema(description = "骑手三项赛等级")
    private Integer riderEventingLevel;
    @Schema(description = "骑手三项赛证书图片JSON")
    private String riderEventingImages;

    @Schema(description = "教练费(元/鞍时)")
    private BigDecimal coachFee;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "是否有效")
    private Integer isValid;

    @Schema(description = "是否删除")
    private Integer isDelete;
}
