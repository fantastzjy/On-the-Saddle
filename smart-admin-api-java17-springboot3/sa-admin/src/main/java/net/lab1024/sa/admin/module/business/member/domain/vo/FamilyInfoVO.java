package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 家庭信息VO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyInfoVO {

    @Schema(description = "家庭组ID")
    private Long familyGroupId;

    @Schema(description = "家庭名称")
    private String familyName;

    @Schema(description = "家庭描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "家庭成员列表")
    private List<FamilyMemberVO> memberList;

    /**
     * 家庭成员VO
     */
    @Data
    public static class FamilyMemberVO {
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
        private LocalDate joinDate;

        @Schema(description = "备注")
        private String remark;
    }
}