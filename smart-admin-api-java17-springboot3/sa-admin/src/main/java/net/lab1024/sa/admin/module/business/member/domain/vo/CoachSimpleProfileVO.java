package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 教练简要个人信息VO
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "教练简要个人信息")
public class CoachSimpleProfileVO {
    
    @Schema(description = "头像地址")
    private String avatarUrl;
    
    @Schema(description = "真实姓名") 
    private String actualName;
    
    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer gender;
}