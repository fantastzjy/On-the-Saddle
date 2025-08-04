package net.lab1024.sa.admin.module.business.club.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 俱乐部实体
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_club")
public class ClubEntity {

    @TableId(type = IdType.AUTO)
    private Long clubId;

    @DataTracerFieldLabel("俱乐部名称")
    private String clubName;

    @DataTracerFieldLabel("俱乐部编码")
    private String clubCode;

    @DataTracerFieldLabel("LOGO地址")
    private String logoUrl;

    @DataTracerFieldLabel("轮播图片地址列表")
    private String carouselImages;

    @DataTracerFieldLabel("PC端首页图片地址")
    private String pcBannerUrl;

    @DataTracerFieldLabel("营业开始时间")
    private LocalTime businessStartTime;

    @DataTracerFieldLabel("营业结束时间")
    private LocalTime businessEndTime;

    @DataTracerFieldLabel("详细地址")
    private String address;

    @DataTracerFieldLabel("电话")
    private String phone;

    @DataTracerFieldLabel("俱乐部详情")
    private String description;

    @DataTracerFieldLabel("荣誉信息")
    private String honorInfo;

    @DataTracerFieldLabel("约课需知")
    private String bookingNotice;

    @DataTracerFieldLabel("省份")
    private String province;

    @DataTracerFieldLabel("城市")
    private String city;

    @DataTracerFieldLabel("区县")
    private String district;

    @DataTracerFieldLabel("营业执照图片地址")
    private String businessLicenseUrl;

    @DataTracerFieldLabel("联系人")
    private String contactPerson;

    @DataTracerFieldLabel("联系电话")
    private String contactPhone;

    @DataTracerFieldLabel("邮箱")
    private String email;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean isValid;

    private Boolean isDelete;
}
