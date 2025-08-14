package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 家庭组查询表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyGroupQueryForm extends PageParam {

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "家庭名称")
    private String familyName;

    @Schema(description = "主要联系人姓名")
    private String mainContactName;

    @Schema(description = "主要联系人手机号")
    private String mainContactPhone;

    @Schema(description = "最小成员数量")
    private Integer minMemberCount;

    @Schema(description = "最大成员数量")
    private Integer maxMemberCount;

    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @Schema(description = "状态: 1-正常 0-已删除")
    private Integer status;
}