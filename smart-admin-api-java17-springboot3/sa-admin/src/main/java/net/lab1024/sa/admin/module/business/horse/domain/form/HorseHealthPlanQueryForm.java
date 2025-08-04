package net.lab1024.sa.admin.module.business.horse.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 马匹健康计划查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseHealthPlanQueryForm extends PageParam {

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "计划类型")
    private String planType;

    @Schema(description = "下次执行开始日期")
    private LocalDateTime nextDateStart;

    @Schema(description = "下次执行结束日期")
    private LocalDateTime nextDateEnd;

    @Schema(description = "是否需要提醒(根据提醒天数计算)")
    private Boolean needReminder;

    @Schema(description = "是否有效")
    private Integer isValid = 1;

    @Schema(description = "是否删除")
    private Integer isDelete = 0;
}