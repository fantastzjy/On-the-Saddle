package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课表列表视图对象
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "课表列表视图对象")
public class ScheduleListVO {

    @Schema(description = "课单ID")
    private Long scheduleId;

    @Schema(description = "预约ID")
    private Long bookingId;

    @Schema(description = "课单号")
    private String scheduleNo;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员姓名")
    private String memberName;

    @Schema(description = "会员电话")
    private String memberPhone;

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "教练姓名")
    private String coachName;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马匹名称")
    private String horseName;

    @Schema(description = "上课日期")
    private LocalDate lessonDate;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "课单状态")
    private Integer lessonStatus;

    @Schema(description = "课单状态名称")
    private String lessonStatusName;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品类型")
    private Integer productType;

    @Schema(description = "课程时长(分钟)")
    private Integer duration;

    @Schema(description = "是否已发送通知")
    private Integer notificationSent;

    @Schema(description = "是否已发送提醒")
    private Integer reminderSent;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
