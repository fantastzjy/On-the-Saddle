package net.lab1024.sa.admin.module.business.coach.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * 统一证书信息VO（教练证书和骑手证书通用）
 * 
 * @Author Claude Code
 * @Date 2025-08-24
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "统一证书信息")
public class CertificateVO {
    
    @Schema(description = "证书类别：教练证书(1-场地障碍,2-盛装舞步,3-三项赛,4-教练星级)；骑手证书(1-场地障碍,2-盛装舞步,3-三项赛)")
    private Integer category;
    
    @Schema(description = "类别描述")
    private String categoryText;
    
    @Schema(description = "等级：教练证书统一用星级(1-5)；骑手证书用等级(1-10)")
    private Integer level;
    
    @Schema(description = "等级描述")
    private String levelText;
    
    @Schema(description = "证书图片列表")
    private List<String> images;
}