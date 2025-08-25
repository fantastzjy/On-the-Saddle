package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 教练月视图数据VO
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-25
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
@Data
@Schema(description = "教练月视图数据VO")
public class CoachMonthViewVO {

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "月份")
    private Integer month;

    @Schema(description = "日历天数据")
    private List<MonthDayVO> calendarDays;

    @Schema(description = "教练统计")
    private List<CoachMonthStatsVO> coachStats;

    @Data
    @Schema(description = "月日历天VO")
    public static class MonthDayVO {
        
        @Schema(description = "日期")
        private LocalDate date;
        
        @Schema(description = "日")
        private Integer dayOfMonth;
        
        @Schema(description = "是否其他月")
        private Boolean isOtherMonth;
        
        @Schema(description = "是否今天")
        private Boolean isToday;
        
        @Schema(description = "总课程数")
        private Integer totalLessons;
    }

    @Data
    @Schema(description = "教练月统计VO")
    public static class CoachMonthStatsVO {
        
        @Schema(description = "教练ID")
        private Long coachId;
        
        @Schema(description = "教练名称")
        private String coachName;
        
        @Schema(description = "总课程数")
        private Integer totalLessons;
        
        @Schema(description = "占比百分比")
        private BigDecimal percentage;
        
        @Schema(description = "每日统计")
        private List<DailyStatVO> dailyStats;
    }

    @Data
    @Schema(description = "日统计VO")
    public static class DailyStatVO {
        
        @Schema(description = "日期")
        private LocalDate date;
        
        @Schema(description = "课程数")
        private Integer lessonCount;
    }
}