package net.lab1024.sa.admin.module.business.horse.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 健康计划类型选项VO
 *
 * @Author: 1024创新实验室-主任：卓大
 * @Date: 2024-01-15
 * @Wechat: zhuda1024
 * @Email: lab1024@163.com
 * @Copyright: 1024创新实验室 （ https://1024lab.net ），Since 2012
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthPlanTypeOptionVO {

    @Schema(description = "选项值")
    private String value;

    @Schema(description = "选项标签")
    private String label;
}