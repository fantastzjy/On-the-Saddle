package net.lab1024.sa.admin.module.business.schedule.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 课表查询表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "课表查询表单")
public class ScheduleQueryForm extends PageParam {

    @Schema(description = "关键字(课单号/会员姓名/教练姓名)")
    private String keywords;

    @Schema(description = "课单状态")
    private Integer lessonStatus;

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "开始时间范围-开始")
    private LocalDateTime timeRangeStart;

    @Schema(description = "开始时间范围-结束")
    private LocalDateTime timeRangeEnd;

    @Schema(description = "上课日期范围-开始")
    private String lessonDateStart;

    @Schema(description = "上课日期范围-结束")
    private String lessonDateEnd;
}
