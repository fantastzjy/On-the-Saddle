package net.lab1024.sa.admin.module.business.schedule.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.schedule.dao.LessonScheduleDao;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.LessonScheduleEntity;
import net.lab1024.sa.admin.module.business.schedule.domain.form.CoachScheduleQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachDayViewVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachMonthViewVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachWeekViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 教练排课资源视图服务
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-25
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
@Service
public class CoachScheduleService {

    @Autowired
    private LessonScheduleDao lessonScheduleDao;

    @Autowired
    private CoachDao coachDao;

    // 时间段配置
    private static final Map<String, String[]> TIME_PERIODS = new HashMap<>();
    static {
        TIME_PERIODS.put("morning", new String[]{"09:00", "10:00", "11:00", "12:00"});
        TIME_PERIODS.put("afternoon", new String[]{"13:00", "14:00", "15:00", "16:00", "17:00", "18:00"});
        TIME_PERIODS.put("evening", new String[]{"19:00", "20:00"});
    }

    // 完整时间点列表（包含13点）
    private static final String[] ALL_TIME_SLOTS = {
        "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
    };

    /**
     * 获取教练日视图数据
     */
    public CoachDayViewVO getDayViewData(CoachScheduleQueryForm queryForm, Long clubId) {
        LocalDate queryDate = queryForm.getQueryDate();
        List<Long> coachIds = queryForm.getCoachIds();

        // 获取教练列表
        List<CoachEntity> coaches = getCoachList(coachIds, clubId);
        if (CollectionUtils.isEmpty(coaches)) {
            return new CoachDayViewVO();
        }

        // 获取当日课程数据
        List<LessonScheduleEntity> schedules = getDaySchedules(queryDate,
            coaches.stream().map(CoachEntity::getCoachId).collect(Collectors.toList()), clubId);

        // 构建返回数据
        CoachDayViewVO result = new CoachDayViewVO();
        result.setQueryDate(queryDate);
        result.setCoaches(buildCoachDayData(coaches, schedules));

        return result;
    }

    /**
     * 获取教练周视图数据
     */
    public CoachWeekViewVO getWeekViewData(CoachScheduleQueryForm queryForm, Long clubId) {
        LocalDate queryDate = queryForm.getQueryDate();
        List<Long> coachIds = queryForm.getCoachIds();

        // 计算周开始和结束日期
        LocalDate weekStart = queryDate.minusDays(queryDate.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        // 获取教练列表
        List<CoachEntity> coaches = getCoachList(coachIds, clubId);
        if (CollectionUtils.isEmpty(coaches)) {
            return new CoachWeekViewVO();
        }

        // 获取本周课程数据
        List<LessonScheduleEntity> schedules = getWeekSchedules(weekStart, weekEnd,
            coaches.stream().map(CoachEntity::getCoachId).collect(Collectors.toList()), clubId);

        // 构建返回数据
        CoachWeekViewVO result = new CoachWeekViewVO();
        result.setWeekStartDate(weekStart);
        result.setWeekEndDate(weekEnd);
        result.setCoaches(buildCoachWeekData(coaches, schedules, weekStart));

        return result;
    }

    /**
     * 获取教练月视图数据
     */
    public CoachMonthViewVO getMonthViewData(CoachScheduleQueryForm queryForm, Long clubId) {
        LocalDate queryDate = queryForm.getQueryDate();
        List<Long> coachIds = queryForm.getCoachIds();

        // 计算月份范围
        LocalDate monthStart = queryDate.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        // 扩展到完整日历视图（包含其他月份的天）
        LocalDate calendarStart = monthStart.minusDays(monthStart.getDayOfWeek().getValue() - 1);
        LocalDate calendarEnd = monthEnd.plusDays(7 - monthEnd.getDayOfWeek().getValue());

        // 获取教练列表
        List<CoachEntity> coaches = getCoachList(coachIds, clubId);
        if (CollectionUtils.isEmpty(coaches)) {
            return new CoachMonthViewVO();
        }

        // 获取日历范围内的课程数据
        List<LessonScheduleEntity> schedules = getMonthSchedules(calendarStart, calendarEnd,
            coaches.stream().map(CoachEntity::getCoachId).collect(Collectors.toList()), clubId);

        // 构建返回数据
        CoachMonthViewVO result = new CoachMonthViewVO();
        result.setYear(queryDate.getYear());
        result.setMonth(queryDate.getMonthValue());
        result.setCalendarDays(buildCalendarDays(calendarStart, calendarEnd, monthStart, monthEnd, schedules));
        result.setCoachStats(buildCoachMonthStats(coaches, schedules, monthStart, monthEnd));

        return result;
    }

    /**
     * 获取教练列表
     */
    private List<CoachEntity> getCoachList(List<Long> coachIds, Long clubId) {
        QueryWrapper<CoachEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("club_id", clubId);
        if (!CollectionUtils.isEmpty(coachIds)) {
            wrapper.in("coach_id", coachIds);
        }
        wrapper.eq("is_delete", 0);
        wrapper.orderByAsc("actual_name");
        return coachDao.selectList(wrapper);
    }

    /**
     * 获取某日的课程数据
     */
    private List<LessonScheduleEntity> getDaySchedules(LocalDate date, List<Long> coachIds, Long clubId) {
        return lessonScheduleDao.selectCoachDaySchedules(date, coachIds, clubId);
    }

    /**
     * 获取某周的课程数据
     */
    private List<LessonScheduleEntity> getWeekSchedules(LocalDate startDate, LocalDate endDate, List<Long> coachIds, Long clubId) {
        return lessonScheduleDao.selectCoachWeekSchedules(startDate, endDate, coachIds, clubId);
    }

    /**
     * 获取某月的课程数据
     */
    private List<LessonScheduleEntity> getMonthSchedules(LocalDate startDate, LocalDate endDate, List<Long> coachIds, Long clubId) {
        return lessonScheduleDao.selectCoachMonthSchedules(startDate, endDate, coachIds, clubId);
    }

    /**
     * 构建教练日数据
     */
    private List<CoachDayViewVO.CoachDayDataVO> buildCoachDayData(List<CoachEntity> coaches, List<LessonScheduleEntity> schedules) {
        // 按教练分组课程
        Map<Long, List<LessonScheduleEntity>> coachScheduleMap = schedules.stream()
            .collect(Collectors.groupingBy(LessonScheduleEntity::getCoachId));

        return coaches.stream().map(coach -> {
            CoachDayViewVO.CoachDayDataVO coachData = new CoachDayViewVO.CoachDayDataVO();
            coachData.setCoachId(coach.getCoachId());
            coachData.setCoachName(coach.getActualName());
            // coachData.setCoachStatus("在线");// TODO???

            List<LessonScheduleEntity> coachSchedules = coachScheduleMap.getOrDefault(coach.getCoachId(), new ArrayList<>());

            // 按时间段分组
            coachData.setMorningSlots(buildTimeSlots(coachSchedules, "morning"));
            coachData.setAfternoonSlots(buildTimeSlots(coachSchedules, "afternoon"));
            coachData.setEveningSlots(buildTimeSlots(coachSchedules, "evening"));

            return coachData;
        }).collect(Collectors.toList());
    }

    /**
     * 构建时间段数据
     */
    private List<CoachDayViewVO.ScheduleSlotVO> buildTimeSlots(List<LessonScheduleEntity> schedules, String period) {
        String[] hourSlots = TIME_PERIODS.get(period);
        List<CoachDayViewVO.ScheduleSlotVO> slots = new ArrayList<>();

        for (String hourSlot : hourSlots) {
            CoachDayViewVO.ScheduleSlotVO slot = new CoachDayViewVO.ScheduleSlotVO();
            slot.setHourSlot(hourSlot);

            // 查找该时间段的课程
            Optional<LessonScheduleEntity> scheduleOpt = schedules.stream()
                .filter(s -> {
                    LocalTime startTime = s.getStartTime().toLocalTime();
                    LocalTime slotTime = LocalTime.parse(hourSlot);
                    return startTime.getHour() == slotTime.getHour();
                })
                .findFirst();

            if (scheduleOpt.isPresent()) {
                LessonScheduleEntity schedule = scheduleOpt.get();
                slot.setScheduleId(schedule.getScheduleId());
                slot.setMemberName(schedule.getMemberName());
                slot.setHorseName(schedule.getHorseName());
                slot.setCourseName(schedule.getProductName());
                slot.setLessonStatus(schedule.getLessonStatus());
                slot.setProductType(schedule.getProductType());
                slot.setStartTime(schedule.getStartTime());
                slot.setEndTime(schedule.getEndTime());
            }

            slots.add(slot);
        }

        return slots;
    }

    /**
     * 构建教练周数据
     */
    private List<CoachWeekViewVO.CoachWeekDataVO> buildCoachWeekData(List<CoachEntity> coaches, List<LessonScheduleEntity> schedules, LocalDate weekStart) {
        // 按教练分组课程
        Map<Long, List<LessonScheduleEntity>> coachScheduleMap = schedules.stream()
            .collect(Collectors.groupingBy(LessonScheduleEntity::getCoachId));

        return coaches.stream().map(coach -> {
            CoachWeekViewVO.CoachWeekDataVO coachData = new CoachWeekViewVO.CoachWeekDataVO();
            coachData.setCoachId(coach.getCoachId());
            coachData.setCoachName(coach.getActualName());

            List<LessonScheduleEntity> coachSchedules = coachScheduleMap.getOrDefault(coach.getCoachId(), new ArrayList<>());
            coachData.setDailyStats(buildDailyStats(coachSchedules, weekStart));

            return coachData;
        }).collect(Collectors.toList());
    }

    /**
     * 构建每日统计数据
     */
    private List<CoachWeekViewVO.DayPeriodCountVO> buildDailyStats(List<LessonScheduleEntity> schedules, LocalDate weekStart) {
        List<CoachWeekViewVO.DayPeriodCountVO> dailyStats = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            CoachWeekViewVO.DayPeriodCountVO dayStat = new CoachWeekViewVO.DayPeriodCountVO();
            dayStat.setDate(date);

            // 统计该日各时间段的课程数
            List<LessonScheduleEntity> daySchedules = schedules.stream()
                .filter(s -> s.getLessonDate().equals(date))
                .collect(Collectors.toList());

            int morningCount = (int) daySchedules.stream()
                .filter(s -> s.getStartTime().getHour() >= 9 && s.getStartTime().getHour() < 12)
                .count();

            int afternoonCount = (int) daySchedules.stream()
                .filter(s -> s.getStartTime().getHour() >= 14 && s.getStartTime().getHour() < 18)
                .count();

            int eveningCount = (int) daySchedules.stream()
                .filter(s -> s.getStartTime().getHour() >= 19 && s.getStartTime().getHour() < 22)
                .count();

            dayStat.setMorningCount(morningCount);
            dayStat.setAfternoonCount(afternoonCount);
            dayStat.setEveningCount(eveningCount);
            dayStat.setTotalCount(morningCount + afternoonCount + eveningCount);

            dailyStats.add(dayStat);
        }

        return dailyStats;
    }

    /**
     * 构建日历天数据
     */
    private List<CoachMonthViewVO.MonthDayVO> buildCalendarDays(LocalDate calendarStart, LocalDate calendarEnd,
                                                               LocalDate monthStart, LocalDate monthEnd,
                                                               List<LessonScheduleEntity> schedules) {
        List<CoachMonthViewVO.MonthDayVO> calendarDays = new ArrayList<>();

        // 按日期统计课程数
        Map<LocalDate, Long> dailyLessonCount = schedules.stream()
            .collect(Collectors.groupingBy(LessonScheduleEntity::getLessonDate, Collectors.counting()));

        LocalDate current = calendarStart;
        LocalDate today = LocalDate.now();

        while (!current.isAfter(calendarEnd)) {
            CoachMonthViewVO.MonthDayVO dayVO = new CoachMonthViewVO.MonthDayVO();
            dayVO.setDate(current);
            dayVO.setDayOfMonth(current.getDayOfMonth());
            dayVO.setIsOtherMonth(current.isBefore(monthStart) || current.isAfter(monthEnd));
            dayVO.setIsToday(current.equals(today));
            dayVO.setTotalLessons(dailyLessonCount.getOrDefault(current, 0L).intValue());

            calendarDays.add(dayVO);
            current = current.plusDays(1);
        }

        return calendarDays;
    }

    /**
     * 构建教练月统计数据
     */
    private List<CoachMonthViewVO.CoachMonthStatsVO> buildCoachMonthStats(List<CoachEntity> coaches, List<LessonScheduleEntity> schedules,
                                                                         LocalDate monthStart, LocalDate monthEnd) {
        // 过滤本月的课程
        List<LessonScheduleEntity> monthSchedules = schedules.stream()
            .filter(s -> !s.getLessonDate().isBefore(monthStart) && !s.getLessonDate().isAfter(monthEnd))
            .collect(Collectors.toList());

        // 按教练统计
        Map<Long, List<LessonScheduleEntity>> coachScheduleMap = monthSchedules.stream()
            .collect(Collectors.groupingBy(LessonScheduleEntity::getCoachId));

        int totalLessons = monthSchedules.size();

        return coaches.stream().map(coach -> {
            CoachMonthViewVO.CoachMonthStatsVO stats = new CoachMonthViewVO.CoachMonthStatsVO();
            stats.setCoachId(coach.getCoachId());
            stats.setCoachName(coach.getActualName());

            List<LessonScheduleEntity> coachSchedules = coachScheduleMap.getOrDefault(coach.getCoachId(), new ArrayList<>());
            stats.setTotalLessons(coachSchedules.size());

            // 计算占比
            if (totalLessons > 0) {
                BigDecimal percentage = BigDecimal.valueOf(coachSchedules.size())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalLessons), 2, RoundingMode.HALF_UP);
                stats.setPercentage(percentage);
            } else {
                stats.setPercentage(BigDecimal.ZERO);
            }

            // 构建每日统计
            stats.setDailyStats(buildCoachDailyStats(coachSchedules, monthStart, monthEnd));

            return stats;
        }).collect(Collectors.toList());
    }

    /**
     * 构建教练每日统计
     */
    private List<CoachMonthViewVO.DailyStatVO> buildCoachDailyStats(List<LessonScheduleEntity> schedules, LocalDate monthStart, LocalDate monthEnd) {
        Map<LocalDate, Long> dailyCount = schedules.stream()
            .collect(Collectors.groupingBy(LessonScheduleEntity::getLessonDate, Collectors.counting()));

        List<CoachMonthViewVO.DailyStatVO> dailyStats = new ArrayList<>();
        LocalDate current = monthStart;

        while (!current.isAfter(monthEnd)) {
            CoachMonthViewVO.DailyStatVO dayStat = new CoachMonthViewVO.DailyStatVO();
            dayStat.setDate(current);
            dayStat.setLessonCount(dailyCount.getOrDefault(current, 0L).intValue());

            dailyStats.add(dayStat);
            current = current.plusDays(1);
        }

        return dailyStats;
    }
}
