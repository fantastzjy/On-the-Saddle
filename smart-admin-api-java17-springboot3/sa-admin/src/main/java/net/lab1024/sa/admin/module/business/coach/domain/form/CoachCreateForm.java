package net.lab1024.sa.admin.module.business.coach.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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

    @Schema(description = "星级教练证号码")
    @Length(max = 100, message = "星级教练证号码最多100字符")
    private String coachCertNo;

    @Schema(description = "骑手证号码")
    @Length(max = 100, message = "骑手证号码最多100字符")
    private String riderCertNo;

    // 教练证书扫平化字段（4个类别）
    @Schema(description = "教练星级：0-无证书，1-5星")
    private Integer coachStarLevel;
    @Schema(description = "教练星级证书图片JSON")
    private String coachStarCertImages;

    @Schema(description = "教练场地障碍星级：0-无证书，1-5星")
    private Integer coachShowJumpingLevel;
    @Schema(description = "教练场地障碍证书图片JSON")
    private String coachShowJumpingImages;

    @Schema(description = "教练盛装舞步星级：0-无证书，1-5星")
    private Integer coachDressageLevel;
    @Schema(description = "教练盛装舞步证书图片JSON")
    private String coachDressageImages;

    @Schema(description = "教练三项赛星级：0-无证书，1-5星")
    private Integer coachEventingLevel;
    @Schema(description = "教练三项赛证书图片JSON")
    private String coachEventingImages;

    // 骑手证书扫平化字段（3个类别）
    @Schema(description = "骑手场地障碍等级：0-无证书，1-10级")
    private Integer riderShowJumpingLevel;
    @Schema(description = "骑手场地障碍证书图片JSON")
    private String riderShowJumpingImages;

    @Schema(description = "骑手盛装舞步等级：0-无证书，1-10级")
    private Integer riderDressageLevel;
    @Schema(description = "骑手盛装舞步证书图片JSON")
    private String riderDressageImages;

    @Schema(description = "骑手三项赛等级：0-无证书，1-10级")
    private Integer riderEventingLevel;
    @Schema(description = "骑手三项赛证书图片JSON")
    private String riderEventingImages;

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
}
