package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家庭组搜索结果VO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyGroupSearchVO {

    @Schema(description = "家庭组ID")
    private Long familyGroupId;

    @Schema(description = "家庭名称")
    private String familyName;

    @Schema(description = "主要联系人姓名")
    private String mainContactName;

    @Schema(description = "家庭描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "成员数量")
    private Integer memberCount;
}