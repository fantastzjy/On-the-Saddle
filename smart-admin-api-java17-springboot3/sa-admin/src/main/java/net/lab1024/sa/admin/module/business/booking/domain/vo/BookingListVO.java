package net.lab1024.sa.admin.module.business.booking.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预约列表VO
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "预约列表VO")
public class BookingListVO {

    @Schema(description = "预约ID")
    private Long bookingId;

    @Schema(description = "预约单号")
    private String bookingNo;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

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

    @Schema(description = "教练专长")
    private String coachSpecialties;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马匹名称")
    private String horseName;

    @Schema(description = "预约日期")
    private LocalDateTime bookingDate;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "预约状态")
    private Integer bookingStatus;

    @Schema(description = "预约状态名称")
    private String bookingStatusName;

    @Schema(description = "课程类型名称")
    private String lessonTypeName;

    @Schema(description = "总费用")
    private BigDecimal totalFee;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}