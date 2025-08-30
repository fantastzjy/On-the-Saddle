package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 时间槽信息VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "时间槽信息")
public class TimeSlotInfo {
    
    @Schema(description = "时间槽ID")
    private Long timeSlotId;
    
    @Schema(description = "开始时间")
    private String startTime;
    
    @Schema(description = "结束时间")
    private String endTime;
    
    @Schema(description = "日期")
    private String date;
    
    @Schema(description = "是否可用")
    private Boolean available;
    
    @Schema(description = "已预约人数")
    private Integer bookedCount;
    
    @Schema(description = "最大人数")
    private Integer maxCount;
}
