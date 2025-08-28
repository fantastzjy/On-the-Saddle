package net.lab1024.sa.admin.module.business.booking.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDate;
import java.util.List;

/**
 * 预约查询表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "预约查询表单")
public class BookingQueryForm extends PageParam {

    @Schema(description = "关键字（预约单号/会员姓名/教练姓名）")
    private String keywords;

    @Schema(description = "预约状态（支持多个状态查询）")
    private List<Integer> bookingStatus;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "俱乐部ID")
    private Long clubId;
}