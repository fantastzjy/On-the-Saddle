package net.lab1024.sa.admin.module.business.horse.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 马匹新增表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseCreateForm {

    @Schema(description = "俱乐部ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;

    @Schema(description = "马名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "马名不能为空")
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

    @Schema(description = "马匹类型：1-俱乐部马 2-马主马 3-教练马", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "马匹类型不能为空")
    private Integer horseType;

    @Schema(description = "马主ID（马主马时必填）")
    private Long ownerId;

    @Schema(description = "责任教练ID")
    private Long responsibleCoachId;

    @Schema(description = "责任马工ID")
    private Long responsibleGroomId;

    @Schema(description = "寄养开始日期")
    private LocalDateTime boardingStartDate;

    @Schema(description = "寄养结束日期")
    private LocalDateTime boardingEndDate;

    @Schema(description = "健康状态：1-健康 2-观察 3-治疗")
    private Integer healthStatus = 1;

    @Schema(description = "工作状态：1-可用 2-休息 3-治疗")
    private Integer workStatus = 1;

    @Schema(description = "马匹扩展数据JSON格式")
    private String horseData;
}
