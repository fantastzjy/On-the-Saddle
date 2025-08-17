package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 冲突检测结果VO
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "冲突检测结果")
public class ConflictCheckVO {

    @Schema(description = "是否有冲突")
    private Boolean hasConflict;

    @Schema(description = "冲突类型：COACH-教练冲突, HORSE-马匹冲突, MEMBER-会员冲突")
    private String conflictType;

    @Schema(description = "冲突详情列表")
    private List<ConflictDetailVO> conflicts;

    @Schema(description = "建议解决方案")
    private List<String> suggestions;

    @Data
    @Schema(description = "冲突详情")
    public static class ConflictDetailVO {
        
        @Schema(description = "冲突课表ID")
        private Long conflictScheduleId;
        
        @Schema(description = "冲突课单号")
        private String conflictScheduleNo;
        
        @Schema(description = "冲突资源类型")
        private String resourceType;
        
        @Schema(description = "冲突资源名称")
        private String resourceName;
        
        @Schema(description = "冲突时间段")
        private String conflictTimeRange;
        
        @Schema(description = "冲突会员姓名")
        private String memberName;
        
        @Schema(description = "冲突详情描述")
        private String description;
    }
}