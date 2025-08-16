package net.lab1024.sa.admin.module.business.booking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.booking.domain.entity.CoachAvailabilityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 教练可用时间数据访问层
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface CoachAvailabilityDao extends BaseMapper<CoachAvailabilityEntity> {

    /**
     * 根据教练ID和日期查询可用时间
     */
    List<CoachAvailabilityEntity> selectByCoachAndDate(@Param("coachId") Long coachId, 
                                                       @Param("availableDate") LocalDate availableDate);

    /**
     * 根据俱乐部ID和日期范围查询教练可用时间
     */
    List<CoachAvailabilityEntity> selectByClubAndDateRange(@Param("clubId") Long clubId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);

    /**
     * 批量更新教练可用状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}