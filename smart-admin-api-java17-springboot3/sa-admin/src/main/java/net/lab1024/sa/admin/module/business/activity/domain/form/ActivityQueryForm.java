package net.lab1024.sa.admin.module.business.activity.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 活动查询表单
 *
 * @Author 1024创新实验室
 * @Date 2025-08-28
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "活动查询表单")
public class ActivityQueryForm extends PageParam {

    @Schema(description = "关键字搜索（活动名称）")
    private String keywords;

    @Schema(description = "创建开始时间")
    private String startTime;

    @Schema(description = "创建结束时间")
    private String endTime;

    @Schema(description = "俱乐部ID")
    private Long clubId;
}