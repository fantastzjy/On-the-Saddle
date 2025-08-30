package net.lab1024.sa.admin.module.business.selector.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教练选择器查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CoachSelectorQueryForm extends BaseSelectorQueryForm {
    
    @Schema(description = "是否显示教练费")
    private Boolean showCoachFee;
    
}