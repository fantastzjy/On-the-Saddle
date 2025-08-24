package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

/**
 * 时间段VO
 *
 * @Author Claude Code  
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "时间段信息")
public class TimeSlotVO {
    
    @Schema(description = "开始时间")
    private LocalTime startTime;
    
    @Schema(description = "结束时间")  
    private LocalTime endTime;
}