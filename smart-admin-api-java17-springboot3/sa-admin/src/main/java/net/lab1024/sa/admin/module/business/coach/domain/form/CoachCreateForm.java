package net.lab1024.sa.admin.module.business.coach.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 教练创建表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class CoachCreateForm {

    @Schema(description = "教练姓名")
    @NotBlank(message = "教练姓名不能为空")
    @Length(max = 50, message = "教练姓名最多50字符")
    private String coachName;

    @Schema(description = "教练编号")
    @Length(max = 50, message = "教练编号最多50字符")
    private String coachCode;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "手机号码")
    @Length(max = 20, message = "手机号码最多20字符")
    @Pattern(regexp = "^[0-9-()\\s]*$", message = "手机号码格式不正确")
    private String phone;

    @Schema(description = "邮箱")
    @Length(max = 100, message = "邮箱最多100字符")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "邮箱格式不正确")
    private String email;

    @Schema(description = "身份证号")
    @Length(max = 18, message = "身份证号最多18字符")
    private String idCard;

    @Schema(description = "头像地址")
    @Length(max = 500, message = "头像地址最多500字符")
    private String avatarUrl;

    @Schema(description = "专业等级")
    @Length(max = 50, message = "专业等级最多50字符")
    private String professionalLevel;

    @Schema(description = "专业特长")
    @Length(max = 200, message = "专业特长最多200字符")
    private String speciality;

    @Schema(description = "从业年限")
    private Integer yearsExperience;

    @Schema(description = "资质证书")
    @Length(max = 500, message = "资质证书最多500字符")
    private String certification;

    @Schema(description = "所属俱乐部ID")
    private Long clubId;

    @Schema(description = "薪资")
    private BigDecimal salary;

    @Schema(description = "入职日期")
    private LocalDate entryDate;

    @Schema(description = "联系地址")
    @Length(max = 200, message = "联系地址最多200字符")
    private String address;

    @Schema(description = "紧急联系人")
    @Length(max = 50, message = "紧急联系人最多50字符")
    private String emergencyContact;

    @Schema(description = "紧急联系电话")
    @Length(max = 20, message = "紧急联系电话最多20字符")
    @Pattern(regexp = "^[0-9-()\\s]*$", message = "紧急联系电话格式不正确")
    private String emergencyPhone;

    @Schema(description = "个人简介")
    private String description;

    @Schema(description = "获奖经历")
    private String achievements;

    @Schema(description = "教学风格")
    private String teachingStyle;
}