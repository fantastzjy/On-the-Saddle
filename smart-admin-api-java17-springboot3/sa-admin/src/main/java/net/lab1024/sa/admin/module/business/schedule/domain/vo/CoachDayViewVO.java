package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 教练日视图数据VO
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-25
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
@Data
@Schema(description = "教练日视图数据VO")
public class CoachDayViewVO {

    @Schema(description = "查询日期")
    private LocalDate queryDate;

    @Schema(description = "教练列表")
    private List<CoachDayDataVO> coaches;

    @Data
    @Schema(description = "教练日数据VO")
    public static class CoachDayDataVO {
        
        @Schema(description = "教练ID")
        private Long coachId;
        
        @Schema(description = "教练名称")
        private String coachName;
        
        @Schema(description = "教练状态")
        private String coachStatus;
        
        @Schema(description = "上午课程")
        private List<ScheduleSlotVO> morningSlots;
        
        @Schema(description = "下午课程")
        private List<ScheduleSlotVO> afternoonSlots;
        
        @Schema(description = "晚上课程")
        private List<ScheduleSlotVO> eveningSlots;
    }

    @Data
    @Schema(description = "课程时段VO")
    public static class ScheduleSlotVO {
        
        @Schema(description = "小时时段")
        private String hourSlot;
        
        @Schema(description = "课单ID")
        private Long scheduleId;
        
        @Schema(description = "会员名称")
        private String memberName;
        
        @Schema(description = "马匹名称")
        private String horseName;
        
        @Schema(description = "课程名称")
        private String courseName;
        
        @Schema(description = "课单状态")
        private Integer lessonStatus;
        
        @Schema(description = "商品类型")
        private Integer productType;
        
        @Schema(description = "开始时间")
        private LocalDateTime startTime;
        
        @Schema(description = "结束时间")
        private LocalDateTime endTime;
    }
}