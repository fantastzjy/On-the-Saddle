package net.lab1024.sa.admin.module.business.schedule.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.LessonScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 课单表Dao
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface LessonScheduleDao extends BaseMapper<LessonScheduleEntity> {

    /**
     * 根据预约ID查询课单
     */
    LessonScheduleEntity selectByBookingId(@Param("bookingId") Long bookingId);

    /**
     * 查询教练日视图数据
     */
    List<LessonScheduleEntity> selectCoachDaySchedules(@Param("lessonDate") LocalDate lessonDate, 
                                                       @Param("coachIds") List<Long> coachIds, 
                                                       @Param("clubId") Long clubId);

    /**
     * 查询教练周视图数据
     */
    List<LessonScheduleEntity> selectCoachWeekSchedules(@Param("startDate") LocalDate startDate, 
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("coachIds") List<Long> coachIds, 
                                                        @Param("clubId") Long clubId);

    /**
     * 查询教练月视图数据
     */
    List<LessonScheduleEntity> selectCoachMonthSchedules(@Param("startDate") LocalDate startDate, 
                                                         @Param("endDate") LocalDate endDate,
                                                         @Param("coachIds") List<Long> coachIds, 
                                                         @Param("clubId") Long clubId);

}