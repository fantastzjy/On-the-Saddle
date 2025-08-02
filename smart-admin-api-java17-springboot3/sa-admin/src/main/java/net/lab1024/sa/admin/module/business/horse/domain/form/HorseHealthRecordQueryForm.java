package net.lab1024.sa.admin.module.business.horse.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 马匹健康记录查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HorseHealthRecordQueryForm extends PageParam {

    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "关联计划ID")
    private Long planId;

    @Schema(description = "记录类型")
    private String recordType;

    @Schema(description = "执行人ID")
    private Long executorId;

    @Schema(description = "记录开始日期")
    private LocalDateTime recordDateStart;

    @Schema(description = "记录结束日期")
    private LocalDateTime recordDateEnd;

    @Schema(description = "是否有效")
    private Integer isValid = 1;

    @Schema(description = "是否删除")
    private Integer isDelete = 0;
}