package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 会员查询表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class MemberQueryForm extends PageParam {

    @Schema(description = "关键字(会员编号/姓名/手机号/骑手证号)")
    private String keywords;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "会员编号")
    private String memberNo;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "注册状态: 0-未激活 1-已注册")
    private Integer registrationStatus;

    @Schema(description = "是否为会籍会员: 1-是 0-否")
    private Integer isMembership;

    @Schema(description = "创建方式: 0-自主注册 1-监护人创建")
    private Integer createdByGuardian;

    @Schema(description = "是否禁用: 0-启用 1-禁用")
    private Integer disabledFlag;

    @Schema(description = "性别: 0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "创建开始时间")
    private String createTimeBegin;

    @Schema(description = "创建结束时间")
    private String createTimeEnd;

    @Schema(description = "是否删除")
    private Integer isDelete;

    @Schema(description = "是否有效")
    private Integer isValid;
}