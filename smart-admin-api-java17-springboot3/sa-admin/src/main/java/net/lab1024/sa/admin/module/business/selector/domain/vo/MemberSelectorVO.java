package net.lab1024.sa.admin.module.business.selector.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员选择器VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
public class MemberSelectorVO {

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "脱敏手机号")
    private String maskedPhone;

    @Schema(description = "格式化显示标签")
    private String displayLabel;
}