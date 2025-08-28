package net.lab1024.sa.admin.module.business.horse.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 马匹健康记录新增表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseHealthRecordCreateForm {

    @Schema(description = "马匹ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "马匹ID不能为空")
    private Long horseId;

    @Schema(description = "关联计划ID")
    private Long planId;

    @Schema(description = "记录日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "记录日期不能为空")
    private LocalDateTime recordDate;

    @Schema(description = "执行人ID")
    private Long executorId;

    @Schema(description = "记录内容")
    private String content;

    @Schema(description = "图片地址JSON格式")
    private String imgUrl;

    @Schema(description = "下次执行日期")
    private LocalDateTime nextDate;

    @Schema(description = "记录扩展数据JSON格式")
    private String recordData;

    @Schema(description = "实际费用")
    private java.math.BigDecimal actualCost;

    @Schema(description = "预算费用") 
    private java.math.BigDecimal budgetCost;
}