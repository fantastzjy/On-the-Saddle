package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预约记录信息VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "预约记录信息VO")
public class BookingRecordVO {

    @Schema(description = "预约ID")
    private Long bookingId;

    @Schema(description = "消费会员ID")
    private Long consumerMemberId;

    @Schema(description = "消费会员姓名")
    private String consumerMemberName;

    @Schema(description = "预约开始时间")
    private LocalDateTime startTime;

    @Schema(description = "预约结束时间")
    private LocalDateTime endTime;

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "教练姓名")
    private String coachName;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马匹名称")
    private String horseName;

    @Schema(description = "预约状态")
    private Integer bookingStatus;

    @Schema(description = "预约状态名称")
    private String bookingStatusName;

    @Schema(description = "课程状态")
    private Integer lessonStatus;

    @Schema(description = "课程状态名称")
    private String lessonStatusName;

    @Schema(description = "实际教练费")
    private BigDecimal actualCoachFee;

    @Schema(description = "实际马匹费")
    private BigDecimal actualHorseFee;

    @Schema(description = "本次消费课时数")
    private Integer packageConsumeCount;

    @Schema(description = "课单ID")
    private Long scheduleId;

    @Schema(description = "课单号")
    private String scheduleNo;

    @Schema(description = "到店时间")
    private LocalDateTime arrivalTime;

    @Schema(description = "完成时间")
    private LocalDateTime completionTime;

    @Schema(description = "取消原因")
    private String cancelReason;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}