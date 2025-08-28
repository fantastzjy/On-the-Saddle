package net.lab1024.sa.admin.module.openapi.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户约课习惯VO
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "用户约课习惯")
public class UserBookingHabitVO {
    
    @Schema(description = "会员ID")
    private Long memberId;
    
    @Schema(description = "默认教练ID")
    private Long defaultCoachId;
    
    @Schema(description = "默认课程级别")
    private String defaultCourseLevel;
    
    @Schema(description = "用户角色")
    private String userRole; // 老会员、新会员、马主
    
    @Schema(description = "常用教练ID列表")
    private List<Long> frequentCoachIds;
    
    @Schema(description = "常用课程类型列表")
    private List<String> frequentCourseTypes;
    
    @Schema(description = "俱乐部ID")
    private Long clubId;
}
