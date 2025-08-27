package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 家庭成员列表VO
 *
 * @Author Claude Code
 * @Date 2025-08-27
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyMembersVO {
    
    @Schema(description = "家庭组信息")
    private FamilyGroupInfo familyGroup;
    
    @Schema(description = "家庭成员列表")
    private List<FamilyMemberInfo> members;
    
    @Data
    public static class FamilyGroupInfo {
        @Schema(description = "家庭名称")
        private String familyName;
        
        @Schema(description = "俱乐部名称")
        private String clubName;
        
        @Schema(description = "成员总数")
        private Integer memberCount;
    }
    
    @Data
    public static class FamilyMemberInfo {
        @Schema(description = "会员编号")
        private String memberNo;
        
        @Schema(description = "会员姓名")
        private String actualName;
        
        @Schema(description = "手机号")
        private String phone;
        
        @Schema(description = "是否为监护人")
        private Boolean isGuardian;
        
        @Schema(description = "加入日期")
        private String joinDate;
        
        @Schema(description = "年龄")
        private Integer age;
        
        @Schema(description = "性别")
        private Integer gender;
        
        @Schema(description = "性别描述")
        private String genderDesc;
    }
}