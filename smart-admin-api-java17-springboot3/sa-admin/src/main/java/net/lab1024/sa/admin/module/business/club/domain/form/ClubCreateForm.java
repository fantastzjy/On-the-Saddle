package net.lab1024.sa.admin.module.business.club.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

/**
 * 俱乐部创建表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ClubCreateForm {

    @Schema(description = "俱乐部名称")
    @NotBlank(message = "俱乐部名称不能为空")
    @Length(max = 100, message = "俱乐部名称最多100字符")
    private String clubName;

    @Schema(description = "俱乐部编码")
    @Length(max = 50, message = "俱乐部编码最多50字符")
    private String clubCode;

    @Schema(description = "LOGO地址")
    @Length(max = 500, message = "LOGO地址最多500字符")
    private String logoUrl;

    @Schema(description = "轮播图片地址列表JSON格式")
    private String carouselImages;

    @Schema(description = "PC端首页图片地址")
    @Length(max = 500, message = "PC端首页图片地址最多500字符")
    private String pcBannerUrl;

    @Schema(description = "营业开始时间")
    private LocalTime businessStartTime;

    @Schema(description = "营业结束时间")
    private LocalTime businessEndTime;

    @Schema(description = "详细地址")
    @Length(max = 200, message = "详细地址最多200字符")
    private String address;

    @Schema(description = "电话")
    @Length(max = 20, message = "电话最多20字符")
    @Pattern(regexp = "^[0-9-()\\s]*$", message = "电话格式不正确")
    private String phone;

    @Schema(description = "俱乐部详情")
    private String description;

    @Schema(description = "荣誉信息")
    private String honorInfo;

    @Schema(description = "约课需知")
    private String bookingNotice;

    @Schema(description = "省份")
    @Length(max = 50, message = "省份最多50字符")
    private String province;

    @Schema(description = "城市")
    @Length(max = 50, message = "城市最多50字符")
    private String city;

    @Schema(description = "区县")
    @Length(max = 50, message = "区县最多50字符")
    private String district;

    @Schema(description = "营业执照图片地址")
    @Length(max = 500, message = "营业执照图片地址最多500字符")
    private String businessLicenseUrl;

    @Schema(description = "联系人")
    @Length(max = 50, message = "联系人最多50字符")
    private String contactPerson;

    @Schema(description = "联系电话")
    @Length(max = 20, message = "联系电话最多20字符")
    @Pattern(regexp = "^[0-9-()\\s]*$", message = "联系电话格式不正确")
    private String contactPhone;

    @Schema(description = "邮箱")
    @Length(max = 100, message = "邮箱最多100字符")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "邮箱格式不正确")
    private String email;
}
