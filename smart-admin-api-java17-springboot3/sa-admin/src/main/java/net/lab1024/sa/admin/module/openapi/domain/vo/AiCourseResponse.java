package net.lab1024.sa.admin.module.openapi.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI约课响应VO
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "AI约课响应")
public class AiCourseResponse {
    
    @Schema(description = "识别的文本")
    private String recognizedText;
    
    @Schema(description = "教练名称")
    private String coachName;
    
    @Schema(description = "课程类型")
    private String courseType;
    
    @Schema(description = "约课时间")
    private LocalDateTime appointmentTime;
    
    @Schema(description = "用户角色")
    private String userRole;
    
    @Schema(description = "参数是否完整")
    private Boolean parametersComplete;
    
    @Schema(description = "缺失的参数列表")
    private List<String> missingParameters;
    
    @Schema(description = "前端导航指令")
    private String navigationInstruction;
    
    @Schema(description = "跳转页面路径")
    private String targetPage;
    
    @Schema(description = "跳转页面参数")
    private String pageParams;
    
    @Schema(description = "AI回复内容")
    private String aiResponse;
    
    @Schema(description = "处理状态")
    private String status;
    
    @Schema(description = "错误信息")
    private String errorMessage;
    
    // === 小程序适配字段 ===
    
    @Schema(description = "提取的教练编号（小程序需要）")
    private String extractedCoach;
    
    @Schema(description = "提取的课程编号（小程序需要）")
    private String extractedCourse;
    
    @Schema(description = "提取的时间段列表（小程序需要）")
    private List<Map<String, Object>> extractedTimes;
}
