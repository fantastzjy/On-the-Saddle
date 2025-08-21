package net.lab1024.sa.admin.module.business.booking.domain.form;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约改期表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-21
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class BookingRescheduleForm {
    
    @NotNull(message = "预约ID不能为空")
    private Long bookingId;
    
    @NotNull(message = "新开始时间不能为空")
    @Future(message = "新开始时间必须是未来时间")
    private LocalDateTime newStartTime;
    
    @NotNull(message = "新结束时间不能为空")
    private LocalDateTime newEndTime;
    
    private String rescheduleReason; // 改期原因
    
    /**
     * 验证时间范围是否合理
     */
    @AssertTrue(message = "结束时间必须晚于开始时间")
    public boolean isValidTimeRange() {
        return newEndTime != null && newStartTime != null && 
               newEndTime.isAfter(newStartTime);
    }
}