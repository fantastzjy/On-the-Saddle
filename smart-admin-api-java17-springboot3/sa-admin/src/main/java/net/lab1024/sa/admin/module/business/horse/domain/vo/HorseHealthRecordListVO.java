package net.lab1024.sa.admin.module.business.horse.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 马匹健康记录列表VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseHealthRecordListVO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马名")
    private String horseName;

    @Schema(description = "马匹编号")
    private String horseCode;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "关联计划ID")
    private Long planId;

    @Schema(description = "关联计划名称")
    private String planName;

    @Schema(description = "记录类型")
    private String recordType;

    @Schema(description = "记录日期")
    private LocalDateTime recordDate;

    @Schema(description = "执行人ID")
    private Long executorId;

    @Schema(description = "执行人姓名")
    private String executorName;

    @Schema(description = "记录内容")
    private String content;

    @Schema(description = "图片地址")
    private String imgUrl;

    @Schema(description = "下次执行日期")
    private LocalDateTime nextDate;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createBy;
}