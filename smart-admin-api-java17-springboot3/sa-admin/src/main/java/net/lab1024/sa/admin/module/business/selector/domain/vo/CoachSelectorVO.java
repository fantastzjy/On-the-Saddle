package net.lab1024.sa.admin.module.business.selector.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 教练选择器VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
public class CoachSelectorVO {

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "等级编码")
    private String level;

    @Schema(description = "等级名称")
    private String levelText;

    @Schema(description = "格式化显示标签")
    private String displayLabel;
}
