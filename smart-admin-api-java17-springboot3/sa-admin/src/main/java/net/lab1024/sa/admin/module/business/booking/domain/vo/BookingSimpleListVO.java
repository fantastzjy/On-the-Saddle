package net.lab1024.sa.admin.module.business.booking.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预约简化列表VO - 用于列表视图快速显示
 * 只包含必要字段：课程名、会员名、教练名、马匹名、价格信息、预约时间、预约状态、更新人、更新时间
 *
 * @Author Claude Code
 * @Date 2024-08-25
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "预约简化列表VO")
public class BookingSimpleListVO {

    @Schema(description = "预约ID")
    private Long bookingId;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "会员姓名")
    private String memberName;

    @Schema(description = "教练姓名")
    private String coachName;

    @Schema(description = "马匹名称")
    private String horseName;

    @Schema(description = "教练费")
    private BigDecimal coachFee;

    @Schema(description = "马匹费")
    private BigDecimal horseFee;

    @Schema(description = "总费用")
    private BigDecimal totalFee;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "预约状态")
    private Integer bookingStatus;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}