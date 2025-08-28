package net.lab1024.sa.admin.module.business.coach.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 骑手证书信息VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "骑手证书信息")
public class RiderCertificateVO {

    @Schema(description = "类别：1-场地障碍，2-盛装舞步，3-三项赛")
    private Integer category;

    @Schema(description = "类别描述")
    private String categoryText;

    @Schema(description = "证书图片列表")
    private List<String> images;
}
