package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 教练不可用时间段VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "教练不可用时间段信息")
public class UnavailableTimeSlotVO {

    @Schema(description = "日期", example = "2025-08-23")
    private String date;

    @Schema(description = "时间段列表", example = "[\"09:00-10:00\", \"11:00-12:00\"]")
    private List<String> timeSlots;
}