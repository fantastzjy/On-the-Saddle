package net.lab1024.sa.admin.module.business.member.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 家庭成员关系实体
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
@TableName("m_family_member_relation")
public class FamilyMemberRelationEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("家庭组ID")
    private Long familyGroupId;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("是否监护人")
    private Integer isGuardian;

    @DataTracerFieldLabel("加入家庭日期")
    private LocalDateTime joinDate;

    @DataTracerFieldLabel("备注")
    private String remark;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @DataTracerFieldLabel("是否有效")
    private Integer isValid;

    @DataTracerFieldLabel("是否删除")
    private Integer isDelete;
}