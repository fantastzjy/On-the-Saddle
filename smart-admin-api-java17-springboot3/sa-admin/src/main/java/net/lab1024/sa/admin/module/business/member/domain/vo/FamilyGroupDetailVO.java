package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 家庭组详情VO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyGroupDetailVO {

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

    @Schema(description = "家庭描述")
    private String description;

    @Schema(description = "成员数量")
    private Integer memberCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "状态: 1-有效 0-无效")
    private Integer isValid;

    @Schema(description = "是否删除: 1-已删除 0-未删除")
    private Integer isDelete;

    @Schema(description = "家庭成员列表")
    private List<FamilyMemberDetailVO> members;

    /**
     * 家庭成员详情VO
     */
    @Data
    public static class FamilyMemberDetailVO {
        @Schema(description = "关系ID")
        private Long relationId;

        @Schema(description = "会员ID")
        private Long memberId;

        @Schema(description = "会员编号")
        private String memberNo;

        @Schema(description = "真实姓名")
        private String actualName;

        @Schema(description = "头像地址")
        private String avatarUrl;

        @Schema(description = "手机号")
        private String phone;

        @Schema(description = "性别: 0-未知 1-男 2-女")
        private Integer gender;

        @Schema(description = "年龄")
        private Integer age;

        @Schema(description = "注册状态: 0-未激活 1-已注册")
        private Integer registrationStatus;

        @Schema(description = "是否监护人: 1-是 0-否")
        private Integer isGuardian;

        @Schema(description = "加入家庭日期")
        private LocalDateTime joinDate;

        @Schema(description = "关系备注")
        private String remark;

        @Schema(description = "加入时间")
        private LocalDateTime createTime;
    }
}