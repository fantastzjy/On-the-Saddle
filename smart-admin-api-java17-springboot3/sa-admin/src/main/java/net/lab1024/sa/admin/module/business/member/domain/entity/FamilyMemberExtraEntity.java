package net.lab1024.sa.admin.module.business.member.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 未注册成员扩展信息实体
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
@TableName("m_family_member_extra")
public class FamilyMemberExtraEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("关联会员ID")
    private Long memberId;

    @DataTracerFieldLabel("监护人电话")
    private String guardianPhone;

    @DataTracerFieldLabel("监护人姓名")
    private String guardianName;

    @DataTracerFieldLabel("与监护人关系")
    private String guardianRelation;

    @DataTracerFieldLabel("默认教练ID")
    private Long defaultCoachId;

    @DataTracerFieldLabel("默认课程级别")
    private String defaultCourseLevel;

    @DataTracerFieldLabel("其他扩展信息JSON格式")
    private String extraData;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @DataTracerFieldLabel("是否有效")
    private Integer isValid;

    @DataTracerFieldLabel("是否删除")
    private Integer isDelete;
}