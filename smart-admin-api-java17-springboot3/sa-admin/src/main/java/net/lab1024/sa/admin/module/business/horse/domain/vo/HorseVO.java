package net.lab1024.sa.admin.module.business.horse.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 马匹详情VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseVO {

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "马名")
    private String horseName;

    @Schema(description = "马匹编号")
    private String horseCode;

    @Schema(description = "血统")
    private String breed;

    @Schema(description = "性别：1-公 2-母 3-骟")
    private Integer gender;

    @Schema(description = "毛色")
    private String color;

    @Schema(description = "身高(cm)")
    private Integer height;

    @Schema(description = "体重(kg)")
    private Integer weight;

    @Schema(description = "出生日期")
    private LocalDateTime birthDate;

    @Schema(description = "芯片号")
    private String chipNo;

    @Schema(description = "护照号")
    private String passportNo;

    @Schema(description = "血统证书图片地址")
    private String pedigreeCertUrl;

    @Schema(description = "马匹类型：1-俱乐部马 2-竞技马房马 3-马主马")
    private Integer horseType;

    @Schema(description = "马主ID")
    private Long ownerId;

    @Schema(description = "马主姓名")
    private String ownerName;

    @Schema(description = "责任教练ID")
    private Long responsibleCoachId;

    @Schema(description = "责任教练姓名")
    private String responsibleCoachName;

    @Schema(description = "责任马工ID")
    private Long responsibleGroomId;

    @Schema(description = "责任马工姓名")
    private String responsibleGroomName;

    @Schema(description = "寄养开始日期")
    private LocalDateTime boardingStartDate;

    @Schema(description = "寄养结束日期")
    private LocalDateTime boardingEndDate;

    @Schema(description = "寄养费(元)")
    private BigDecimal boardingFee;

    @Schema(description = "健康状态：1-健康 2-观察 3-治疗")
    private Integer healthStatus;

    @Schema(description = "工作状态：1-可用 2-休息 3-治疗")
    private Integer workStatus;

    @Schema(description = "马匹扩展数据")
    private String horseData;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private String updateBy;
}
