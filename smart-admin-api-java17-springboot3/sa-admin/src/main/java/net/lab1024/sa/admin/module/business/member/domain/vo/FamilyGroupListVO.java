package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家庭组列表VO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyGroupListVO {

    @Schema(description = "家庭组ID")
    private Long familyGroupId;

    @Schema(description = "家庭名称")
    private String familyName;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "主要联系人ID")
    private Long mainContactId;

    @Schema(description = "主要联系人姓名")
    private String mainContactName;

    @Schema(description = "主要联系人手机号")
    private String mainContactPhone;

    @Schema(description = "成员数量")
    private Integer memberCount;

    @Schema(description = "家庭描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "状态: 1-有效 0-无效")
    private Integer isValid;

    @Schema(description = "是否删除: 1-已删除 0-未删除")
    private Integer isDelete;
}