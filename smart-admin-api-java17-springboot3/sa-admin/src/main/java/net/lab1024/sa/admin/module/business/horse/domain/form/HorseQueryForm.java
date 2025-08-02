package net.lab1024.sa.admin.module.business.horse.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 马匹查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseQueryForm extends PageParam {

    @Schema(description = "关键字(马名/编号/芯片号)")
    private String keywords;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "品种")
    private String breed;

    @Schema(description = "性别：1-公 2-母 3-骟")
    private Integer gender;

    @Schema(description = "马匹类型：1-俱乐部马 2-马主马 3-教练马")
    private Integer horseType;

    @Schema(description = "马主ID")
    private Long ownerId;

    @Schema(description = "责任教练ID")
    private Long responsibleCoachId;

    @Schema(description = "健康状态：1-健康 2-观察 3-治疗")
    private Integer healthStatus;

    @Schema(description = "工作状态：1-可用 2-休息 3-治疗")
    private Integer workStatus;

    @Schema(description = "开始日期")
    private LocalDateTime startDate;

    @Schema(description = "结束日期")
    private LocalDateTime endDate;

    @Schema(description = "是否有效")
    private Integer isValid = 1;

    @Schema(description = "是否删除")
    private Integer isDelete = 0;
}