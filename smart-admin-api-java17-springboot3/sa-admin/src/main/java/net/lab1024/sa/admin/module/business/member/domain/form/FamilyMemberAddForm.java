package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 家庭成员添加表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class FamilyMemberAddForm {

    @Schema(description = "家庭组ID")
    @NotNull(message = "家庭组ID不能为空")
    private Long familyGroupId;

    @Schema(description = "俱乐部ID")
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;

    @Schema(description = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String actualName;

    @Schema(description = "性别: 0-未知 1-男 2-女")
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @Schema(description = "出生日期")
    @NotNull(message = "出生日期不能为空")
    private LocalDate birthDate;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "身份证号")
    @Pattern(regexp = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$", 
            message = "身份证号格式不正确")
    private String idCardNo;

    @Schema(description = "骑手证号")
    private String riderCertNo;

    @Schema(description = "监护人姓名")
    private String guardianName;

    @Schema(description = "监护人电话")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "监护人电话格式不正确")
    private String guardianPhone;

    @Schema(description = "与监护人关系")
    private String guardianRelation;

    @Schema(description = "默认教练ID")
    private Long defaultCoachId;

    @Schema(description = "课程级别偏好")
    private String courseLevelPreference;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}