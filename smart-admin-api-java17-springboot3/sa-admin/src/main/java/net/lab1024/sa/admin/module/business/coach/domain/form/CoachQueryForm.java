package net.lab1024.sa.admin.module.business.coach.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 教练查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class CoachQueryForm extends PageParam {

    @Schema(description = "关键字(教练编号)")
    private String keywords;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "关联用户ID")
    private Long userId;

    @Schema(description = "教练等级")
    private String coachLevel;

    @Schema(description = "场地障碍等级")
    private String riderLevelShowJumping;

    @Schema(description = "盛装舞步等级")
    private String riderLevelDressage;

    @Schema(description = "三项赛等级")
    private String riderLevelEventing;

    @Schema(description = "创建开始日期")
    private String startDate;

    @Schema(description = "创建结束日期")
    private String endDate;

    @Schema(description = "是否删除")
    private Integer isDelete;

    @Schema(description = "是否有效")
    private Integer isValid;
}