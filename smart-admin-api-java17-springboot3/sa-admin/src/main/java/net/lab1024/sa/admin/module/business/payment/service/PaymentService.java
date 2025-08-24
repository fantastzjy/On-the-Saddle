package net.lab1024.sa.admin.module.business.payment.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.schedule.dao.LessonScheduleDao;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.LessonScheduleEntity;
import net.lab1024.sa.admin.module.business.member.domain.vo.BookingTimeVO;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.payment.domain.form.PaymentCallbackForm;
import net.lab1024.sa.admin.module.business.schedule.service.ResourceScheduleService;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 支付服务
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Service
@Slf4j
public class PaymentService {
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private BookingDao bookingDao;
    
    @Resource
    private LessonScheduleDao lessonScheduleDao;
    
    @Resource
    private ResourceScheduleService resourceScheduleService;
    
    /**
     * 处理支付成功
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> handlePaymentSuccess(PaymentCallbackForm form) {
        try {
            // 1. 查询订单
            OrderEntity order = orderDao.selectByOrderNo(form.getOrderNo());
            if (order == null) {
                return ResponseDTO.userErrorParam("订单不存在");
            }
            
            if (!Integer.valueOf(1).equals(order.getOrderStatus())) {
                return ResponseDTO.userErrorParam("订单状态异常，当前状态：" + order.getOrderStatus());
            }
            
            // 2. 更新订单状态
            order.setOrderStatus(2); // 已支付
            order.setPaidAmount(form.getPaymentAmount());
            order.setPaymentMethod(form.getPaymentMethod());
            order.setPaymentTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            orderDao.updateById(order);
            
            // 3. 确认资源预约
            ResponseDTO<Void> confirmResult = resourceScheduleService.confirmResourceBooking(order.getOrderNo());
            if (!confirmResult.getOk()) {
                log.warn("确认资源预约失败，但不影响支付成功处理：{}", confirmResult.getMsg());
            }
            
            // 4. 创建预约记录
            List<BookingEntity> bookings = createBookingsFromOrder(order);
            for (BookingEntity booking : bookings) {
                bookingDao.insert(booking);
                
                // 5. 创建课程记录
                LessonScheduleEntity lesson = createLessonFromBooking(booking);
                lessonScheduleDao.insert(lesson);
            }
            
            log.info("支付成功处理完成，订单号：{}，创建了{}个预约记录", order.getOrderNo(), bookings.size());
            return ResponseDTO.ok();
            
        } catch (Exception e) {
            log.error("处理支付成功失败，订单号：{}", form.getOrderNo(), e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "处理支付失败：" + e.getMessage());
        }
    }
    
    /**
     * 从订单创建预约记录
     */
    private List<BookingEntity> createBookingsFromOrder(OrderEntity order) {
        List<BookingEntity> bookings = new ArrayList<>();
        
        try {
            // 解析预约时间信息
            String preferredTimesJson = order.getPreferredTimes();
            List<BookingTimeVO> times = JSON.parseArray(preferredTimesJson, BookingTimeVO.class);
            
            for (BookingTimeVO timeInfo : times) {
                LocalDate date = LocalDate.parse(timeInfo.getDate());
                
                for (String timeSlot : timeInfo.getTimeSlots()) {
                    String[] timeParts = timeSlot.split("-");
                    LocalTime startTime = LocalTime.parse(timeParts[0]);
                    LocalTime endTime = LocalTime.parse(timeParts[1]);
                    
                    BookingEntity booking = new BookingEntity();
                    booking.setOrderId(order.getOrderId());
                    booking.setOrderItemId(0L); // 合并后无订单明细
                    booking.setClubId(order.getClubId());
                    booking.setMemberId(order.getMemberId());
                    booking.setConsumerMemberId(null); // 默认自己消费
                    booking.setProductId(order.getProductId());
                    booking.setCoachId(order.getCoachId());
                    booking.setHorseId(0L); // TODO: 从订单中获取马匹信息或默认分配
                    booking.setStartTime(LocalDateTime.of(date, startTime));
                    booking.setEndTime(LocalDateTime.of(date, endTime));
                    booking.setBookingStatus(1); // 已预约
                    booking.setPackageConsumeCount(1);
                    booking.setActualCoachFee(BigDecimal.ZERO); // TODO: 计算实际教练费
                    booking.setActualHorseFee(BigDecimal.ZERO); // TODO: 计算实际马匹费
                    booking.setCreateBy("system");
                    booking.setCreateTime(LocalDateTime.now());
                    
                    bookings.add(booking);
                }
            }
        } catch (Exception e) {
            log.error("解析订单预约时间失败，订单号：{}", order.getOrderNo(), e);
            throw new RuntimeException("解析订单预约时间失败：" + e.getMessage());
        }
        
        return bookings;
    }
    
    /**
     * 从预约创建课程记录
     */
    private LessonScheduleEntity createLessonFromBooking(BookingEntity booking) {
        LessonScheduleEntity lesson = new LessonScheduleEntity();
        lesson.setBookingId(booking.getBookingId());
        lesson.setScheduleNo(generateScheduleNo());
        lesson.setClubId(booking.getClubId());
        lesson.setMemberId(booking.getMemberId());
        lesson.setCoachId(booking.getCoachId());
        lesson.setHorseId(booking.getHorseId());
        lesson.setStartTime(booking.getStartTime());
        lesson.setEndTime(booking.getEndTime());
        lesson.setLessonStatus(1); // 待上课
        // 移除 duration 字段，改为前端计算
        lesson.setCreateBy("system");
        lesson.setCreateTime(LocalDateTime.now());
        
        return lesson;
    }
    
    /**
     * 生成课单号
     */
    private String generateScheduleNo() {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomNum = String.format("%04d", new Random().nextInt(10000));
        return "SCH" + timestamp + randomNum;
    }
}