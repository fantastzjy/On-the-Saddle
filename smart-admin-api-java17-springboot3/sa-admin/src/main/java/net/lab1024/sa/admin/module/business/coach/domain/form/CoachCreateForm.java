package net.lab1024.sa.admin.module.business.coach.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Schema(description = "俱乐部ID")
    @NotNull(message = "俱乐部ID不能为空")
    private Long clubId;

    @Schema(description = "教练真实姓名")
    @NotNull(message = "教练姓名不能为空")
    @Length(max = 30, message = "教练姓名最多30字符")
    private String actualName;

    @Schema(description = "联系电话")
    @Length(max = 15, message = "联系电话最多15字符")
    private String phone;

    @Schema(description = "邮箱地址")
    @Length(max = 100, message = "邮箱地址最多100字符")
    private String email;

    @Schema(description = "性别：0女1男")
    private Integer gender;

    @Schema(description = "生日")
    private LocalDate birthDate;

    @Schema(description = "身份证号码")
    @Length(max = 18, message = "身份证号码最多18字符")
    private String idCard;

    @Schema(description = "教练编号")
    @Length(max = 50, message = "教练编号最多50字符")
    private String coachNo;

    @Schema(description = "头像照片地址")
    @Length(max = 500, message = "头像照片地址最多500字符")
    private String avatarUrl;

    @Schema(description = "入行时间")
    private LocalDateTime entryDate;

    @Schema(description = "专长领域")
    @Length(max = 200, message = "专长领域最多200字符")
    private String specialties;

    @Schema(description = "个人介绍")
    private String introduction;

    @Schema(description = "骑手证号码")
    @Length(max = 100, message = "骑手证号码最多100字符")
    private String riderCertNo;

    @Schema(description = "场地障碍等级：初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级")
    @Length(max = 20, message = "场地障碍等级最多20字符")
    private String riderLevelShowJumping;

    @Schema(description = "盛装舞步等级：初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级")
    @Length(max = 20, message = "盛装舞步等级最多20字符")
    private String riderLevelDressage;

    @Schema(description = "三项赛等级：初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级")
    @Length(max = 20, message = "三项赛等级最多20字符")
    private String riderLevelEventing;

    @Schema(description = "骑手证书图片地址JSON格式")
    private String riderCertImgUrl;

    @Schema(description = "星级教练证号码")
    @Length(max = 100, message = "星级教练证号码最多100字符")
    private String coachCertNo;

    @Schema(description = "教练等级：一星,二星,三星,四星,五星")
    @Length(max = 20, message = "教练等级最多20字符")
    private String coachLevel;

    @Schema(description = "教练证书图片地址JSON格式")
    private String coachCertImgUrl;

    @Schema(description = "教练费(元/鞍时)")
    @DecimalMin(value = "0.00", message = "教练费不能小于0")
    private BigDecimal coachFee;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "身份证正面照片地址")
    @Length(max = 500, message = "身份证正面照片地址最多500字符")
    private String idCardFrontImg;

    @Schema(description = "身份证反面照片地址")
    @Length(max = 500, message = "身份证反面照片地址最多500字符")
    private String idCardBackImg;

    @Schema(description = "教练证书列表JSON格式")
    private String coachCertificates;

    @Schema(description = "骑手证书列表JSON格式")
    private String riderCertificates;
}