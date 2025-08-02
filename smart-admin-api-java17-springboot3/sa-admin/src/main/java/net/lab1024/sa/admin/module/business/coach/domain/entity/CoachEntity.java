package net.lab1024.sa.admin.module.business.coach.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教练实体
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_coach")
public class CoachEntity {

    @TableId(type = IdType.AUTO)
    private Long coachId;

    @DataTracerFieldLabel("教练姓名")
    private String coachName;

    @DataTracerFieldLabel("教练编号")
    private String coachCode;

    @DataTracerFieldLabel("性别")
    private Integer gender;

    @DataTracerFieldLabel("出生日期")
    private LocalDate birthDate;

    @DataTracerFieldLabel("手机号码")
    private String phone;

    @DataTracerFieldLabel("邮箱")
    private String email;

    @DataTracerFieldLabel("身份证号")
    private String idCard;

    @DataTracerFieldLabel("头像地址")
    private String avatarUrl;

    @DataTracerFieldLabel("专业等级")
    private String professionalLevel;

    @DataTracerFieldLabel("专业特长")
    private String speciality;

    @DataTracerFieldLabel("从业年限")
    private Integer yearsExperience;

    @DataTracerFieldLabel("资质证书")
    private String certification;

    @DataTracerFieldLabel("所属俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("薪资")
    private BigDecimal salary;

    @DataTracerFieldLabel("入职日期")
    private LocalDate entryDate;

    @DataTracerFieldLabel("联系地址")
    private String address;

    @DataTracerFieldLabel("紧急联系人")
    private String emergencyContact;

    @DataTracerFieldLabel("紧急联系电话")
    private String emergencyPhone;

    @DataTracerFieldLabel("个人简介")
    private String description;

    @DataTracerFieldLabel("获奖经历")
    private String achievements;

    @DataTracerFieldLabel("教学风格")
    private String teachingStyle;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean isActive;

    private Boolean isDelete;
}