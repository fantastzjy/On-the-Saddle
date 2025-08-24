package net.lab1024.sa.admin.module.business.coach.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * 教练证书信息VO
 * 
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "教练证书信息")
public class CoachCertificateVO {
    
    @Schema(description = "证书编号")
    private String certNo;
    
    @Schema(description = "证书类别：1-教练星级，2-场地障碍，3-盛装舞步，4-三项赛")
    private Integer category;
    
    @Schema(description = "证书类别描述")
    private String categoryText;
    
    @Schema(description = "星级：1-一星，2-二星，3-三星，4-四星，5-五星（所有教练证书都有星级）")
    private Integer starLevel;
    
    @Schema(description = "星级描述")
    private String starLevelText;
    
    @Schema(description = "证书图片列表")
    private List<String> images;
}