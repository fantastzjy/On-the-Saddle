package net.lab1024.sa.admin.module.business.schedule.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.schedule.service.ResourceScheduleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单系统定时任务
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Component
@Slf4j
public class OrderScheduledTasks {
    
    @Resource
    private ResourceScheduleService resourceScheduleService;
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private BookingDao bookingDao;
    
    /**
     * 清理过期的待支付订单占用
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = 300000)
    @Transactional(rollbackFor = Exception.class)
    public void cleanExpiredPaymentOccupancy() {
        try {
            log.debug("开始清理过期的待支付订单占用");
            
            // 1. 清理资源占用表中的过期记录
            resourceScheduleService.cleanExpiredPaymentOccupancy();
            
            // 2. 查询过期的待支付订单
            LambdaQueryWrapper<OrderEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderEntity::getOrderStatus, 1) // 待支付
                   .lt(OrderEntity::getPaymentExpireTime, LocalDateTime.now())
                   .isNotNull(OrderEntity::getPaymentExpireTime);
            
            List<OrderEntity> expiredOrders = orderDao.selectList(wrapper);
            
            if (!expiredOrders.isEmpty()) {
                // 3. 批量更新过期订单状态
                for (OrderEntity order : expiredOrders) {
                    LambdaUpdateWrapper<OrderEntity> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.set(OrderEntity::getOrderStatus, 4) // 已取消
                               .set(OrderEntity::getAutoCancelFlag, 1) // 自动取消
                               .set(OrderEntity::getUpdateTime, LocalDateTime.now())
                               .eq(OrderEntity::getOrderId, order.getOrderId());
                    
                    orderDao.update(null, updateWrapper);
                    log.info("自动取消过期订单：{}", order.getOrderNo());
                }
                
                log.info("批量处理过期订单完成，共处理{}个订单", expiredOrders.size());
            }
            
        } catch (Exception e) {
            log.error("清理过期订单失败", e);
        }
    }
    
    /**
     * 处理过期预约
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void handleExpiredBookings() {
        try {
            log.info("开始处理过期预约");
            
            LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();
            
            // 1. 查询昨天及之前的未上课预约
            LambdaQueryWrapper<BookingEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BookingEntity::getBookingStatus, 1) // 已预约
                   .lt(BookingEntity::getStartTime, yesterday);
            
            List<BookingEntity> expiredBookings = bookingDao.selectList(wrapper);
            
            if (!expiredBookings.isEmpty()) {
                for (BookingEntity booking : expiredBookings) {
                    // 2. 更新预约状态为已过期
                    LambdaUpdateWrapper<BookingEntity> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.set(BookingEntity::getBookingStatus, 4) // 已过期
                               .set(BookingEntity::getUpdateTime, LocalDateTime.now())
                               .eq(BookingEntity::getBookingId, booking.getBookingId());
                    
                    bookingDao.update(null, updateWrapper);
                    
                    // 3. TODO: 触发自动退款流程
                    processAutoRefund(booking);
                    
                    log.info("处理过期预约：预约ID={}, 开始时间={}", booking.getBookingId(), booking.getStartTime());
                }
                
                log.info("批量处理过期预约完成，共处理{}个预约", expiredBookings.size());
            } else {
                log.info("未发现过期预约");
            }
            
        } catch (Exception e) {
            log.error("处理过期预约失败", e);
        }
    }
    
    /**
     * 清理无效数据
     * 每天凌晨3点执行
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void cleanInvalidData() {
        try {
            log.info("开始清理无效数据");
            
            // 清理30天前的过期资源占用记录（状态5且已过期）
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            resourceScheduleService.cleanExpiredPaymentOccupancy();
            
            log.info("清理无效数据完成");
            
        } catch (Exception e) {
            log.error("清理无效数据失败", e);
        }
    }
    
    /**
     * 数据一致性检查
     * 每天凌晨4点执行
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void dataConsistencyCheck() {
        try {
            log.info("开始执行数据一致性检查");
            
            // 1. 检查待支付订单不应该有预约记录
            LambdaQueryWrapper<OrderEntity> pendingOrderWrapper = new LambdaQueryWrapper<>();
            pendingOrderWrapper.eq(OrderEntity::getOrderStatus, 1); // 待支付
            List<OrderEntity> pendingOrders = orderDao.selectList(pendingOrderWrapper);
            
            for (OrderEntity order : pendingOrders) {
                LambdaQueryWrapper<BookingEntity> bookingWrapper = new LambdaQueryWrapper<>();
                bookingWrapper.eq(BookingEntity::getOrderId, order.getOrderId());
                Long bookingCount = bookingDao.selectCount(bookingWrapper);
                
                if (bookingCount > 0) {
                    log.warn("数据一致性问题：待支付订单{}存在{}个预约记录", order.getOrderNo(), bookingCount);
                }
            }
            
            // 2. 检查已支付订单应该有预约记录
            LambdaQueryWrapper<OrderEntity> paidOrderWrapper = new LambdaQueryWrapper<>();
            paidOrderWrapper.ge(OrderEntity::getOrderStatus, 2) // 已支付及以上
                           .le(OrderEntity::getOrderStatus, 3); // 已核销及以下
            List<OrderEntity> paidOrders = orderDao.selectList(paidOrderWrapper);
            
            for (OrderEntity order : paidOrders) {
                LambdaQueryWrapper<BookingEntity> bookingWrapper = new LambdaQueryWrapper<>();
                bookingWrapper.eq(BookingEntity::getOrderId, order.getOrderId());
                Long bookingCount = bookingDao.selectCount(bookingWrapper);
                
                if (bookingCount == 0) {
                    log.warn("数据一致性问题：已支付订单{}没有预约记录", order.getOrderNo());
                }
            }
            
            log.info("数据一致性检查完成");
            
        } catch (Exception e) {
            log.error("数据一致性检查失败", e);
        }
    }
    
    /**
     * 处理自动退款
     * TODO: 实现具体的退款逻辑
     */
    private void processAutoRefund(BookingEntity booking) {
        try {
            // 这里应该调用退款接口
            log.info("触发自动退款流程：预约ID={}, 会员ID={}", booking.getBookingId(), booking.getMemberId());
            
            // TODO: 
            // 1. 查询原始订单信息
            // 2. 计算退款金额
            // 3. 调用支付平台退款接口
            // 4. 更新订单状态为已退款
            // 5. 发送退款通知
            
        } catch (Exception e) {
            log.error("处理自动退款失败，预约ID：{}", booking.getBookingId(), e);
        }
    }
}