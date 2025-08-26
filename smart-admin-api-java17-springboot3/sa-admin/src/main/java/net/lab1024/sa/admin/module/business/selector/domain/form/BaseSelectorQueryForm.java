package net.lab1024.sa.admin.module.business.selector.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 基础选择器查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Data
public abstract class BaseSelectorQueryForm {

    @Schema(description = "关键字搜索")
    private String keywords;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "分页大小")
    @Min(1)
    @Max(100)
    private Integer pageSize = 50;

    @Schema(description = "页码")
    @Min(1)
    private Integer pageNum = 1;
}