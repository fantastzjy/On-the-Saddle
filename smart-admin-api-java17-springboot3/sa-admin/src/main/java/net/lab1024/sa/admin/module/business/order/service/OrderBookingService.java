package net.lab1024.sa.admin.module.business.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderItemEntity;
import net.lab1024.sa.admin.module.business.schedule.service.ScheduleService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单预约关联服务
 * 处理订单和预约之间的业务关联逻辑
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class OrderBookingService {

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 根据订单明细创建预约
     * 实现用户选择教练和时间后自动生成待确认预约的核心逻辑
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> createBookingsFromOrder(Long orderId, List<OrderItemEntity> orderItems) {
        try {
            List<BookingEntity> bookingEntities = new ArrayList<>();

            for (OrderItemEntity orderItem : orderItems) {
                // 根据商品类型和数量创建对应的预约
                for (int i = 0; i < orderItem.getQuantity(); i++) {
                    BookingEntity bookingEntity = new BookingEntity();
                    
                    // 基础信息
                    bookingEntity.setOrderId(orderId);
                    bookingEntity.setOrderItemId(orderItem.getId());
                    bookingEntity.setProductId(orderItem.getProductId());
                    bookingEntity.setCoachId(orderItem.getCoachId());
                    
                    // 时间安排（基于期望时间和商品配置计算）
                    LocalDateTime startTime = calculateBookingStartTime(orderItem, i);
                    LocalDateTime endTime = calculateBookingEndTime(orderItem, startTime);
                    bookingEntity.setStartTime(startTime);
                    bookingEntity.setEndTime(endTime);
                    
                    // 预约状态：1-待确认（等待教练确认）
                    bookingEntity.setBookingStatus(1);
                    
                    // 费用信息（可以后续通过教练和马匹配置调整）
                    bookingEntity.setActualCoachFee(orderItem.getUnitPrice());
                    
                    // 创建信息
                    bookingEntity.setCreateTime(LocalDateTime.now());
                    
                    bookingEntities.add(bookingEntity);
                }
            }

            // 批量插入预约记录
            for (BookingEntity booking : bookingEntities) {
                bookingDao.insert(booking);
            }

            log.info("根据订单{}创建预约成功，共{}个预约", orderId, bookingEntities.size());
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("根据订单{}创建预约失败", orderId, e);
            return ResponseDTO.userErrorParam("创建预约失败：" + e.getMessage());
        }
    }

    /**
     * 确认订单的所有预约
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> confirmBookingsByOrderId(Long orderId) {
        try {
            List<BookingEntity> bookings = bookingDao.selectList(
                new LambdaQueryWrapper<BookingEntity>()
                    .eq(BookingEntity::getOrderId, orderId)
            );
            
            for (BookingEntity booking : bookings) {
                if (booking.getBookingStatus() == 1) { // 待确认状态
                    booking.setBookingStatus(2); // 已确认
                    booking.setUpdateTime(LocalDateTime.now());
                    bookingDao.updateById(booking);
                    
                    // 预约确认时自动创建课表
                    scheduleService.createScheduleFromBooking(booking);
                }
            }

            log.info("确认订单{}的所有预约成功", orderId);
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("确认订单{}的预约失败", orderId, e);
            return ResponseDTO.userErrorParam("确认预约失败");
        }
    }

    /**
     * 完成订单的所有预约
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> completeBookingsByOrderId(Long orderId) {
        try {
            List<BookingEntity> bookings = bookingDao.selectList(
                new LambdaQueryWrapper<BookingEntity>()
                    .eq(BookingEntity::getOrderId, orderId)
            );
            
            for (BookingEntity booking : bookings) {
                if (booking.getBookingStatus() < 4) { // 未完成状态
                    booking.setBookingStatus(4); // 已完成
                    booking.setCompletionTime(LocalDateTime.now());
                    booking.setUpdateTime(LocalDateTime.now());
                    bookingDao.updateById(booking);
                    
                    // 同步更新课表状态
                    scheduleService.updateScheduleStatusByBooking(booking.getBookingId(), 4);
                }
            }

            log.info("完成订单{}的所有预约成功", orderId);
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("完成订单{}的预约失败", orderId, e);
            return ResponseDTO.userErrorParam("完成预约失败");
        }
    }

    /**
     * 取消订单的所有预约
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> cancelBookingsByOrderId(Long orderId, String cancelReason) {
        try {
            List<BookingEntity> bookings = bookingDao.selectList(
                new LambdaQueryWrapper<BookingEntity>()
                    .eq(BookingEntity::getOrderId, orderId)
            );
            
            for (BookingEntity booking : bookings) {
                if (booking.getBookingStatus() < 4) { // 未完成状态才能取消
                    booking.setBookingStatus(5); // 已取消
                    booking.setCancelReason(cancelReason);
                    booking.setUpdateTime(LocalDateTime.now());
                    bookingDao.updateById(booking);
                    
                    // 同步更新课表状态或删除课表
                    scheduleService.updateScheduleStatusByBooking(booking.getBookingId(), 5);
                }
            }

            log.info("取消订单{}的所有预约成功", orderId);
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("取消订单{}的预约失败", orderId, e);
            return ResponseDTO.userErrorParam("取消预约失败");
        }
    }

    // ========================================
    // 私有辅助方法
    // ========================================

    /**
     * 计算预约开始时间
     * 基于期望时间和序号计算具体的预约时间
     */
    private LocalDateTime calculateBookingStartTime(OrderItemEntity orderItem, int index) {
        LocalDateTime baseTime = orderItem.getPreferredTime();
        
        // 如果是多节课，可以按天递增或按教练的可用时间安排
        // 这里简化处理：多节课按天递增
        if (index > 0) {
            baseTime = baseTime.plusDays(index);
        }
        
        return baseTime;
    }

    /**
     * 计算预约结束时间
     * 根据商品配置确定课程时长
     */
    private LocalDateTime calculateBookingEndTime(OrderItemEntity orderItem, LocalDateTime startTime) {
        // 默认课程时长60分钟，可以从商品配置中读取
        int duration = 60; // 分钟
        
        // 如果商品配置中有时长信息，从中解析
        if (orderItem.getItemConfig() != null && orderItem.getItemConfig().contains("duration")) {
            try {
                // 简化处理，实际应该用JSON解析
                // duration = parseConfigDuration(orderItem.getItemConfig());
            } catch (Exception e) {
                log.warn("解析课程时长失败，使用默认60分钟");
            }
        }
        
        return startTime.plusMinutes(duration);
    }
}