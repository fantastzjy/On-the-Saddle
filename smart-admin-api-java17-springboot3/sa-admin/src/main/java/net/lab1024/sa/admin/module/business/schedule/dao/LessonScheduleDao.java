package net.lab1024.sa.admin.module.business.schedule.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.LessonScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

}