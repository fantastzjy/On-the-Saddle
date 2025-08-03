package net.lab1024.sa.admin.module.business.coach.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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

    @Schema(description = "入行时间")
    private LocalDateTime entryDate;

    @Schema(description = "专长领域")
    private String specialties;

    @Schema(description = "个人介绍")
    private String introduction;

    @Schema(description = "骑手证号码")
    private String riderCertNo;

    @Schema(description = "场地障碍等级")
    private String riderLevelShowJumping;

    @Schema(description = "盛装舞步等级")
    private String riderLevelDressage;

    @Schema(description = "三项赛等级")
    private String riderLevelEventing;

    @Schema(description = "骑手证书图片地址JSON格式")
    private String riderCertImgUrl;

    @Schema(description = "星级教练证号码")
    private String coachCertNo;

    @Schema(description = "教练等级")
    private String coachLevel;

    @Schema(description = "教练证书图片地址JSON格式")
    private String coachCertImgUrl;

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