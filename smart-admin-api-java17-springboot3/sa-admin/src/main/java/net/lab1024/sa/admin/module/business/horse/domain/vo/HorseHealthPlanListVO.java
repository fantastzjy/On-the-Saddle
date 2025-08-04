package net.lab1024.sa.admin.module.business.horse.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 马匹健康计划列表VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseHealthPlanListVO {

    @Schema(description = "计划ID")
    private Long id;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马名")
    private String horseName;

    @Schema(description = "马匹编号")
    private String horseCode;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "计划类型")
    private String planType;

    @Schema(description = "周期天数")
    private Integer cycleDays;

    @Schema(description = "上次执行日期")
    private LocalDateTime lastDate;

    @Schema(description = "下次执行日期")
    private LocalDateTime nextDate;

    @Schema(description = "提前提醒天数")
    private Integer reminderDays;

    @Schema(description = "剩余天数")
    private Long remainingDays;

    @Schema(description = "提醒状态：1-正常 2-提醒 3-紧急 4-超期")
    private Integer reminderStatus;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createBy;
}