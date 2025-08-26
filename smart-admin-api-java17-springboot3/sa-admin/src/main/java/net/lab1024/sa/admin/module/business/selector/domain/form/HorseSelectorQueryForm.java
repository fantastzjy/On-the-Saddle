package net.lab1024.sa.admin.module.business.selector.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 马匹选择器查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HorseSelectorQueryForm extends BaseSelectorQueryForm {

    @Schema(description = "马匹类型：1-俱乐部马 2-马主马 3-教练马")
    private Integer horseType;

    @Schema(description = "健康状态：1-健康 2-观察 3-治疗")
    private Integer healthStatus;

    @Schema(description = "工作状态：1-可用 2-休息 3-治疗")
    private Integer workStatus;
}