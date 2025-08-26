package net.lab1024.sa.admin.module.business.selector.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 马匹选择器VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
public class HorseSelectorVO {

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马名")
    private String horseName;

    @Schema(description = "芯片号")
    private String chipNo;

    @Schema(description = "格式化显示标签")
    private String displayLabel;
}