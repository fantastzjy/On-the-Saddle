package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 教练周视图数据VO
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-25
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
@Data
@Schema(description = "教练周视图数据VO")
public class CoachWeekViewVO {

    @Schema(description = "周开始日期")
    private LocalDate weekStartDate;

    @Schema(description = "周结束日期")
    private LocalDate weekEndDate;

    @Schema(description = "教练列表")
    private List<CoachWeekDataVO> coaches;

    @Data
    @Schema(description = "教练周数据VO")
    public static class CoachWeekDataVO {
        
        @Schema(description = "教练ID")
        private Long coachId;
        
        @Schema(description = "教练名称")
        private String coachName;
        
        @Schema(description = "每日统计")
        private List<DayPeriodCountVO> dailyStats;
    }

    @Data
    @Schema(description = "日期时间段统计VO")
    public static class DayPeriodCountVO {
        
        @Schema(description = "日期")
        private LocalDate date;
        
        @Schema(description = "上午课程数")
        private Integer morningCount;
        
        @Schema(description = "下午课程数")
        private Integer afternoonCount;
        
        @Schema(description = "晚上课程数")
        private Integer eveningCount;
        
        @Schema(description = "当日总课程数")
        private Integer totalCount;
    }
}