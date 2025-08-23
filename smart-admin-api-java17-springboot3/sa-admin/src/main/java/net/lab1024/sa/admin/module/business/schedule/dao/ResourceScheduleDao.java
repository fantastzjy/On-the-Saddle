package net.lab1024.sa.admin.module.business.schedule.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.ResourceScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 资源时间安排DAO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface ResourceScheduleDao extends BaseMapper<ResourceScheduleEntity> {

    /**
     * 根据资源查询时间安排
     */
    List<ResourceScheduleEntity> selectByResource(@Param("resourceType") Integer resourceType,
                                                  @Param("resourceId") Long resourceId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    /**
     * 检查时间冲突
     */
    Integer countConflictTime(@Param("resourceType") Integer resourceType,
                              @Param("resourceId") Long resourceId,
                              @Param("scheduleDate") LocalDate scheduleDate,
                              @Param("startTime") LocalTime startTime,
                              @Param("endTime") LocalTime endTime,
                              @Param("excludeId") Long excludeId);

    /**
     * 根据俱乐部ID删除所有相关记录
     */
    void deleteByClubId(@Param("clubId") Long clubId);

    /**
     * 根据资源删除记录
     */
    void deleteByResource(@Param("resourceType") Integer resourceType,
                          @Param("resourceId") Long resourceId);
}