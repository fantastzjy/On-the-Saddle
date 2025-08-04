package net.lab1024.sa.admin.module.business.club.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 俱乐部列表视图对象
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ClubListVO {

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "俱乐部编码")
    private String clubCode;

    @Schema(description = "LOGO地址")
    private String logoUrl;

    @Schema(description = "轮播图片地址列表JSON格式")
    private String carouselImages;

    @Schema(description = "营业开始时间")
    private LocalTime businessStartTime;

    @Schema(description = "营业结束时间")
    private LocalTime businessEndTime;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "是否有效")
    private Boolean isValid;
}
