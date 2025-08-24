package net.lab1024.sa.admin.module.business.schedule.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 课程表综合查询表单
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "课程表综合查询表单")
public class ScheduleCombinedQueryForm extends PageParam {

    @Schema(description = "关键词搜索(订单号/会员姓名/教练姓名/商品名称)")
    private String keywords;

    @Schema(description = "商品类型: 1-单次课程 2-课时包 3-多时段课程")
    private Integer productType;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "预约状态")
    private Integer bookingStatus;

    @Schema(description = "课程状态")
    private Integer lessonStatus;

    @Schema(description = "购买会员ID")
    private Long buyerMemberId;

    @Schema(description = "消费会员ID")
    private Long consumerMemberId;

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @Schema(description = "俱乐部ID")
    private Long clubId;
}