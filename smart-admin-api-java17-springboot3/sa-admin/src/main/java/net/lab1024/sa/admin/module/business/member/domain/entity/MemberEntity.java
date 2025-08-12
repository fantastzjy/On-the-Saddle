package net.lab1024.sa.admin.module.business.member.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员实体
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
@TableName("m_member")
public class MemberEntity {

    @TableId(type = IdType.AUTO)
    private Long memberId;

    @DataTracerFieldLabel("会员编号")
    private String memberNo;

    @DataTracerFieldLabel("微信unionId")
    private String unionId;

    @DataTracerFieldLabel("微信openId")
    private String openId;


    @DataTracerFieldLabel("真实姓名")
    private String actualName;

    @DataTracerFieldLabel("手机号")
    private String phone;

    @DataTracerFieldLabel("邮箱")
    private String email;

    @DataTracerFieldLabel("头像地址")
    private String avatarUrl;

    @DataTracerFieldLabel("性别")
    private Integer gender;

    @DataTracerFieldLabel("出生日期")
    private LocalDate birthDate;

    @DataTracerFieldLabel("所属俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("是否为会籍会员")
    private Integer isMembership;

    @DataTracerFieldLabel("会籍状态")
    private Integer membershipStatus;

    @DataTracerFieldLabel("会籍有效期")
    private LocalDate membershipExpireDate;

    @DataTracerFieldLabel("身份证号")
    private String idCardNo;

    @DataTracerFieldLabel("骑手证号")
    private String riderCertNo;

    @DataTracerFieldLabel("注册状态")
    private Integer registrationStatus;

    @DataTracerFieldLabel("是否由监护人创建")
    private Integer createdByGuardian;

    @DataTracerFieldLabel("是否禁用")
    private Integer disabledFlag;

    @DataTracerFieldLabel("扩展数据JSON格式")
    private String profileData;

    @DataTracerFieldLabel("默认教练ID")
    private Long defaultCoachId;

    @DataTracerFieldLabel("默认课程级别")
    private String defaultCourseLevel;

    @DataTracerFieldLabel("最后登录时间")
    private LocalDateTime lastLoginTime;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @DataTracerFieldLabel("是否有效")
    private Integer isValid;

    @DataTracerFieldLabel("是否删除")
    private Integer isDelete;
}
