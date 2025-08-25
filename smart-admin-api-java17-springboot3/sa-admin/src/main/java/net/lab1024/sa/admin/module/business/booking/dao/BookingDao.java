package net.lab1024.sa.admin.module.business.booking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.booking.domain.form.BookingQueryForm;
import net.lab1024.sa.admin.module.business.booking.domain.vo.BookingSimpleListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约数据访问层
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface BookingDao extends BaseMapper<BookingEntity> {

    /**
     * 根据订单ID查询预约
     */
    List<BookingEntity> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据会员ID查询预约列表
     */
    List<BookingEntity> selectByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据教练ID查询预约列表
     */
    List<BookingEntity> selectByCoachId(@Param("coachId") Long coachId, 
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 根据马匹ID查询预约列表
     */
    List<BookingEntity> selectByHorseId(@Param("horseId") Long horseId, 
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 检查时间段冲突
     */
    List<BookingEntity> checkTimeConflict(@Param("coachId") Long coachId,
                                         @Param("horseId") Long horseId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 更新预约状态
     */
    int updateBookingStatus(@Param("bookingId") Long bookingId, @Param("bookingStatus") Integer bookingStatus);

    /**
     * 查询今日预约
     */
    List<BookingEntity> selectTodayBookings(@Param("clubId") Long clubId);

    /**
     * 统计预约数量
     */
    int countBookingsByStatus(@Param("clubId") Long clubId, @Param("bookingStatus") Integer bookingStatus);

    /**
     * 查询教练时间冲突的预约
     */
    List<BookingEntity> findCoachTimeConflicts(@Param("coachId") Long coachId,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime,
                                             @Param("excludeBookingId") Long excludeBookingId);

    /**
     * 查询马匹时间冲突的预约  
     */
    List<BookingEntity> findHorseTimeConflicts(@Param("horseId") Long horseId,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime,
                                             @Param("excludeBookingId") Long excludeBookingId);

    /**
     * 根据订单ID列表批量查询预约
     */
    List<BookingEntity> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 简化查询预约列表 - 用于列表视图
     * 只返回必要字段：会员名、教练名、马匹名、预约时间、预约状态、更新人、更新时间
     */
    IPage<BookingSimpleListVO> selectSimpleBookingList(Page<BookingSimpleListVO> page, @Param("queryForm") BookingQueryForm queryForm);
}