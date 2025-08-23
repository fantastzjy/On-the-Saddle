package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 俱乐部信息VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "俱乐部信息")
public class ClubInfoVO {

    @Schema(description = "俱乐部类型 1-俱乐部 2-竞技马房")
    private Integer clubType;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "LOGO地址")
    private String logoUrl;

    @Schema(description = "轮播图片地址列表")
    private List<String> carouselImages;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "营业时间")
    private String businessHours;

    @Schema(description = "俱乐部详情")
    private String description;
}
