package net.lab1024.sa.admin.module.business.booking.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.dao.CoachAvailabilityDao;
import net.lab1024.sa.admin.module.business.booking.dao.HorseAvailabilityDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.booking.domain.entity.CoachAvailabilityEntity;
import net.lab1024.sa.admin.module.business.booking.domain.entity.HorseAvailabilityEntity;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderItemEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能排课算法服务
 * 自动生成课表，检测时间冲突，推荐最佳时段
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class SmartSchedulingService {

    @Resource
    private BookingDao bookingDao;

    @Resource
    private CoachAvailabilityDao coachAvailabilityDao;

    @Resource
    private HorseAvailabilityDao horseAvailabilityDao;

    /**
     * 智能生成课表
     * 根据订单明细自动安排课程时间、教练和马匹
     * 
     * @param orderItem 订单明细
     * @param preferredTime 期望上课时间（可选）
     * @return 生成的预约信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<List<Map<String, Object>>> autoGenerateSchedule(OrderItemEntity orderItem, LocalDateTime preferredTime) {
        try {
            List<Map<String, Object>> scheduleResults = new ArrayList<>();
            
            // 根据商品类型处理不同的排课逻辑
            switch (orderItem.getProductType()) {
                case 1: // 课程
                    scheduleResults = scheduleCourse(orderItem, preferredTime);
                    break;
                case 2: // 课时包
                    scheduleResults = schedulePackage(orderItem, preferredTime);
                    break;
                case 3: // 活动
                    scheduleResults = scheduleActivity(orderItem, preferredTime);
                    break;
                default:
                    return ResponseDTO.userErrorParam("不支持的商品类型");
            }
            
            log.info("智能排课完成，订单明细ID：{}, 生成 {} 个时段", orderItem.getId(), scheduleResults.size());
            return ResponseDTO.ok(scheduleResults);
            
        } catch (Exception e) {
            log.error("智能排课失败", e);
            return ResponseDTO.userErrorParam("智能排课失败：" + e.getMessage());
        }
    }

    /**
     * 检测时间冲突
     * 
     * @param coachId 教练ID
     * @param horseId 马匹ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 冲突检测结果
     */
    public ResponseDTO<Map<String, Object>> checkTimeConflict(Long coachId, Long horseId, 
                                                             LocalDateTime startTime, LocalDateTime endTime) {
        try {
            List<BookingEntity> conflicts = bookingDao.checkTimeConflict(coachId, horseId, startTime, endTime);
            
            Map<String, Object> result = new HashMap<>();
            result.put("hasConflict", !conflicts.isEmpty());
            result.put("conflictCount", conflicts.size());
            result.put("conflicts", conflicts.stream().map(this::convertToConflictInfo).collect(Collectors.toList()));
            
            return ResponseDTO.ok(result);
            
        } catch (Exception e) {
            log.error("检测时间冲突失败", e);
            return ResponseDTO.userErrorParam("检测时间冲突失败");
        }
    }

    /**
     * 推荐最佳时段
     * 
     * @param clubId 俱乐部ID
     * @param coachId 教练ID（可选）
     * @param duration 课程时长（分钟）
     * @param preferredDate 期望日期
     * @return 推荐时段列表
     */
    public ResponseDTO<List<Map<String, Object>>> recommendTimeSlots(Long clubId, Long coachId, 
                                                                    Integer duration, LocalDate preferredDate) {
        try {
            List<Map<String, Object>> recommendations = new ArrayList<>();
            
            // 获取营业时间段（简化版，实际应从配置表获取）
            List<TimeSlot> businessHours = getBusinessHours();
            
            // 获取指定日期的教练可用时间
            List<CoachAvailabilityEntity> coachAvailability = getCoachAvailability(clubId, coachId, preferredDate);
            
            // 获取指定日期的马匹可用时间
            List<HorseAvailabilityEntity> horseAvailability = getHorseAvailability(clubId, preferredDate);
            
            // 获取已有预约
            List<BookingEntity> existingBookings = getExistingBookings(clubId, preferredDate);
            
            // 计算可用时段
            for (TimeSlot slot : businessHours) {
                List<Map<String, Object>> availableSlots = calculateAvailableSlots(
                    slot, duration, coachAvailability, horseAvailability, existingBookings, preferredDate);
                recommendations.addAll(availableSlots);
            }
            
            // 按推荐度排序
            recommendations.sort((a, b) -> {
                Integer scoreA = (Integer) a.getOrDefault("score", 0);
                Integer scoreB = (Integer) b.getOrDefault("score", 0);
                return scoreB.compareTo(scoreA);
            });
            
            log.info("推荐时段计算完成，共 {} 个推荐时段", recommendations.size());
            return ResponseDTO.ok(recommendations.subList(0, Math.min(10, recommendations.size()))); // 返回前10个
            
        } catch (Exception e) {
            log.error("推荐时段计算失败", e);
            return ResponseDTO.userErrorParam("推荐时段计算失败");
        }
    }

    /**
     * 批量安排课程
     * 
     * @param orderItems 订单明细列表
     * @return 批量排课结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Map<String, Object>> batchSchedule(List<OrderItemEntity> orderItems) {
        try {
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();
            
            for (OrderItemEntity orderItem : orderItems) {
                try {
                    ResponseDTO<List<Map<String, Object>>> scheduleResult = 
                        autoGenerateSchedule(orderItem, orderItem.getPreferredTime());
                    
                    if (scheduleResult.getOk()) {
                        successCount++;
                    } else {
                        failCount++;
                        errors.add("订单明细 " + orderItem.getId() + ": " + scheduleResult.getMsg());
                    }
                } catch (Exception e) {
                    failCount++;
                    errors.add("订单明细 " + orderItem.getId() + ": " + e.getMessage());
                }
            }
            
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            
            log.info("批量排课完成，成功 {} 个，失败 {} 个", successCount, failCount);
            return ResponseDTO.ok(result);
            
        } catch (Exception e) {
            log.error("批量排课失败", e);
            return ResponseDTO.userErrorParam("批量排课失败");
        }
    }

    /**
     * 安排单次课程
     */
    private List<Map<String, Object>> scheduleCourse(OrderItemEntity orderItem, LocalDateTime preferredTime) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        // 根据商品配置获取教练和马匹
        Long coachId = orderItem.getCoachId();
        if (coachId == null || coachId == 0) {
            // 自动分配教练
            coachId = autoAssignCoach(orderItem.getProductId(), preferredTime);
        }
        
        // 自动分配马匹
        Long horseId = autoAssignHorse(preferredTime);
        
        // 计算课程时长
        Integer duration = getCourseConfig(orderItem.getProductId(), "duration", 60);
        
        // 确定上课时间
        LocalDateTime startTime = preferredTime != null ? preferredTime : findBestTimeSlot(coachId, horseId, duration);
        LocalDateTime endTime = startTime.plusMinutes(duration);
        
        // 创建预约记录
        BookingEntity booking = createBooking(orderItem, coachId, horseId, startTime, endTime);
        
        Map<String, Object> result = new HashMap<>();
        result.put("bookingId", booking.getBookingId());
        result.put("coachId", coachId);
        result.put("horseId", horseId);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("status", "scheduled");
        
        results.add(result);
        return results;
    }

    /**
     * 安排课时包
     */
    private List<Map<String, Object>> schedulePackage(OrderItemEntity orderItem, LocalDateTime preferredTime) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        // 课时包不立即安排具体时间，创建预约记录供后续预约
        BookingEntity booking = createBooking(orderItem, orderItem.getCoachId(), 0L, null, null);
        booking.setBookingStatus(1); // 待确认状态
        
        Map<String, Object> result = new HashMap<>();
        result.put("bookingId", booking.getBookingId());
        result.put("status", "pending");
        result.put("message", "课时包已创建，请联系前台预约具体时间");
        
        results.add(result);
        return results;
    }

    /**
     * 安排活动
     */
    private List<Map<String, Object>> scheduleActivity(OrderItemEntity orderItem, LocalDateTime preferredTime) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        // 活动时间通常是固定的，从商品配置中获取
        Map<String, Object> activityConfig = getActivityConfig(orderItem.getProductId());
        LocalDateTime activityStartTime = (LocalDateTime) activityConfig.get("startTime");
        LocalDateTime activityEndTime = (LocalDateTime) activityConfig.get("endTime");
        
        // 创建预约记录
        BookingEntity booking = createBooking(orderItem, 0L, 0L, activityStartTime, activityEndTime);
        
        Map<String, Object> result = new HashMap<>();
        result.put("bookingId", booking.getBookingId());
        result.put("startTime", activityStartTime);
        result.put("endTime", activityEndTime);
        result.put("status", "scheduled");
        result.put("type", "activity");
        
        results.add(result);
        return results;
    }

    /**
     * 创建预约记录
     */
    private BookingEntity createBooking(OrderItemEntity orderItem, Long coachId, Long horseId, 
                                       LocalDateTime startTime, LocalDateTime endTime) {
        BookingEntity booking = new BookingEntity();
        booking.setOrderId(orderItem.getOrderId());
        booking.setOrderItemId(orderItem.getId());
        booking.setClubId(1L); // 从订单中获取
        booking.setMemberId(1L); // 从订单中获取
        booking.setProductId(orderItem.getProductId());
        booking.setCoachId(coachId);
        booking.setHorseId(horseId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setBookingStatus(startTime != null ? 2 : 1); // 有具体时间则已确认，否则待确认
        booking.setCreateBy("system");
        booking.setCreateTime(LocalDateTime.now());
        booking.setUpdateBy("system");
        booking.setUpdateTime(LocalDateTime.now());
        
        bookingDao.insert(booking);
        return booking;
    }

    /**
     * 自动分配教练
     */
    private Long autoAssignCoach(Long productId, LocalDateTime preferredTime) {
        // 简化实现：返回默认教练ID
        // 实际应该根据商品配置、教练可用时间、教练能力等因素智能分配
        return 1L;
    }

    /**
     * 自动分配马匹
     */
    private Long autoAssignHorse(LocalDateTime preferredTime) {
        // 简化实现：返回默认马匹ID
        // 实际应该根据马匹可用时间、马匹状态等因素智能分配
        return 1L;
    }

    /**
     * 寻找最佳时段
     */
    private LocalDateTime findBestTimeSlot(Long coachId, Long horseId, Integer duration) {
        // 简化实现：返回明天上午10点
        // 实际应该根据可用性算法计算最佳时段
        return LocalDate.now().plusDays(1).atTime(10, 0);
    }

    /**
     * 获取课程配置
     */
    private Integer getCourseConfig(Long productId, String configKey, Integer defaultValue) {
        // 简化实现：返回默认值
        // 实际应该从数据库中获取商品配置
        return defaultValue;
    }

    /**
     * 获取活动配置
     */
    private Map<String, Object> getActivityConfig(Long productId) {
        // 简化实现：返回模拟数据
        // 实际应该从数据库中获取活动配置
        Map<String, Object> config = new HashMap<>();
        config.put("startTime", LocalDate.now().plusDays(7).atTime(14, 0));
        config.put("endTime", LocalDate.now().plusDays(7).atTime(16, 0));
        return config;
    }

    /**
     * 转换冲突信息
     */
    private Map<String, Object> convertToConflictInfo(BookingEntity booking) {
        Map<String, Object> info = new HashMap<>();
        info.put("bookingId", booking.getBookingId());
        info.put("startTime", booking.getStartTime());
        info.put("endTime", booking.getEndTime());
        info.put("coachId", booking.getCoachId());
        info.put("horseId", booking.getHorseId());
        info.put("status", booking.getBookingStatus());
        return info;
    }

    /**
     * 获取营业时间段
     */
    private List<TimeSlot> getBusinessHours() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0))); // 上午时段
        timeSlots.add(new TimeSlot(LocalTime.of(14, 0), LocalTime.of(18, 0))); // 下午时段
        timeSlots.add(new TimeSlot(LocalTime.of(19, 0), LocalTime.of(21, 0))); // 晚上时段
        return timeSlots;
    }

    /**
     * 获取教练可用时间
     */
    private List<CoachAvailabilityEntity> getCoachAvailability(Long clubId, Long coachId, LocalDate date) {
        // TODO: 实现具体的查询逻辑
        return new ArrayList<>();
    }

    /**
     * 获取马匹可用时间
     */
    private List<HorseAvailabilityEntity> getHorseAvailability(Long clubId, LocalDate date) {
        // TODO: 实现具体的查询逻辑
        return new ArrayList<>();
    }

    /**
     * 获取已有预约
     */
    private List<BookingEntity> getExistingBookings(Long clubId, LocalDate date) {
        // TODO: 实现具体的查询逻辑
        return new ArrayList<>();
    }

    /**
     * 计算可用时段
     */
    private List<Map<String, Object>> calculateAvailableSlots(TimeSlot slot, Integer duration,
                                                             List<CoachAvailabilityEntity> coachAvailability,
                                                             List<HorseAvailabilityEntity> horseAvailability,
                                                             List<BookingEntity> existingBookings,
                                                             LocalDate date) {
        List<Map<String, Object>> availableSlots = new ArrayList<>();
        
        // 简化实现：每小时一个时段
        LocalTime current = slot.startTime;
        while (current.plusMinutes(duration).isBefore(slot.endTime) || current.plusMinutes(duration).equals(slot.endTime)) {
            Map<String, Object> slotInfo = new HashMap<>();
            slotInfo.put("startTime", date.atTime(current));
            slotInfo.put("endTime", date.atTime(current.plusMinutes(duration)));
            slotInfo.put("score", calculateSlotScore(current, date.getDayOfWeek()));
            slotInfo.put("available", true);
            
            availableSlots.add(slotInfo);
            current = current.plusHours(1);
        }
        
        return availableSlots;
    }

    /**
     * 计算时段评分
     */
    private Integer calculateSlotScore(LocalTime time, DayOfWeek dayOfWeek) {
        int score = 50; // 基础分
        
        // 时间段加分
        if (time.isAfter(LocalTime.of(9, 0)) && time.isBefore(LocalTime.of(11, 0))) {
            score += 20; // 上午黄金时段
        } else if (time.isAfter(LocalTime.of(14, 0)) && time.isBefore(LocalTime.of(16, 0))) {
            score += 15; // 下午优质时段
        }
        
        // 工作日加分
        if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            score += 10;
        }
        
        return score;
    }

    /**
     * 时间段内部类
     */
    private static class TimeSlot {
        LocalTime startTime;
        LocalTime endTime;
        
        public TimeSlot(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}