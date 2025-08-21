package net.lab1024.sa.admin.module.business.booking.domain.vo;

import lombok.Data;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 冲突检测结果
 *
 * @Author 1024创新实验室
 * @Date 2024-08-21
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ConflictCheckResult {
    
    private boolean hasConflict = false;
    private List<ConflictDetail> conflicts = new ArrayList<>();
    private String conflictMessage;
    
    /**
     * 添加冲突信息
     */
    public void addConflict(String resourceType, List<BookingEntity> conflictBookings) {
        if (!conflictBookings.isEmpty()) {
            this.hasConflict = true;
            conflicts.add(new ConflictDetail(resourceType, conflictBookings));
            updateConflictMessage();
        }
    }
    
    /**
     * 更新冲突消息
     */
    private void updateConflictMessage() {
        this.conflictMessage = conflicts.stream()
            .map(c -> c.getResourceType() + "时间冲突")
            .collect(Collectors.joining("，"));
    }
    
    /**
     * 冲突详情内部类
     */
    @Data
    public static class ConflictDetail {
        private String resourceType; // 资源类型：教练/马匹
        private List<BookingEntity> conflictBookings; // 冲突的预约列表
        
        public ConflictDetail(String resourceType, List<BookingEntity> conflictBookings) {
            this.resourceType = resourceType;
            this.conflictBookings = conflictBookings;
        }
    }
}