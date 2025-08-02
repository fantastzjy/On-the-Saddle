package net.lab1024.sa.admin.module.business.club.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 俱乐部详情视图对象
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ClubVO {

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "俱乐部编码")
    private String clubCode;

    @Schema(description = "LOGO地址")
    private String logoUrl;

    @Schema(description = "置顶图片地址")
    private String bannerUrl;

    @Schema(description = "PC端首页图片地址")
    private String pcBannerUrl;

    @Schema(description = "营业开始时间")
    private LocalTime businessStartTime;

    @Schema(description = "营业结束时间")
    private LocalTime businessEndTime;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "俱乐部详情")
    private String description;

    @Schema(description = "荣誉信息")
    private String honorInfo;

    @Schema(description = "约课需知")
    private String bookingNotice;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "营业执照图片地址")
    private String businessLicenseUrl;

    @Schema(description = "法人代表")
    private String legalPerson;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "到期日期时间")
    private LocalDateTime expireDate;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "是否有效")
    private Boolean isValid;
}
