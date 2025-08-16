package net.lab1024.sa.admin.module.business.product.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品新增表单 - 严格按照数据库表结构设计
 * 对应主表 m_product 和扩展表 m_product_course/m_product_package/m_product_activity
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "商品新增表单")
public class ProductAddForm {

    // ========================================
    // 主表字段 m_product
    // ========================================
    
    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String productName;

    @Schema(description = "商品编码")
    private String productCode;

    @Schema(description = "商品类型: 1-课程 2-课时包 3-活动")
    @NotNull(message = "商品类型不能为空")
    private Integer productType;

    @Schema(description = "商品子类型")
    private String subType;

    @Schema(description = "商品描述")
    @NotBlank(message = "商品描述不能为空")
    private String description;

    @Schema(description = "商品图片地址列表(JSON格式)")
    private String images;

    @Schema(description = "商品状态: 1-上架 2-下架 3-售罄")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;

    // ========================================
    // 课程商品字段 m_product_course (productType=1时有效)
    // ========================================
    
    @Schema(description = "课程分类: 1-单人课 2-多人课")
    private Integer classType;

    @Schema(description = "时长（分钟）")
    private Integer durationMinutes;

    @Schema(description = "时长（鞍时）")
    private BigDecimal durationPeriods;

    @Schema(description = "最大人数")
    private Integer maxStudents;

    @Schema(description = "教练费")
    private BigDecimal coachFee;

    @Schema(description = "马匹费用")
    private BigDecimal horseFee;

    @Schema(description = "多人课价格配置JSON（仅多人课）")
    private String multiPriceConfig;

    // ========================================
    // 课时包商品字段 m_product_package (productType=2时有效)
    // ========================================
    
    @Schema(description = "课包详情")
    private String details;

    @Schema(description = "课包单价")
    private BigDecimal price;

    @Schema(description = "总课时数")
    private Integer totalSessions;

    @Schema(description = "有效期（天）")
    private Integer validityDays;

    @Schema(description = "库存数量")
    private Integer stockQuantity;

    // ========================================
    // 活动商品字段 m_product_activity (productType=3时有效)
    // ========================================
    
    @Schema(description = "活动名称（最多5个字）")
    private String activityName;

    @Schema(description = "活动详情")
    private String activityDetails;

    @Schema(description = "活动开始时间")
    private String startTime;

    @Schema(description = "活动结束时间")  
    private String endTime;

    @Schema(description = "活动地点")
    private String activityLocation;

    @Schema(description = "活动单价")
    private BigDecimal activityPrice;

    @Schema(description = "最大参与人数")
    private Integer maxParticipants;

    @Schema(description = "退款规则")
    private String refundRule;

    @Schema(description = "详情图片地址列表JSON格式（最多9张）")
    private String detailImages;

    // ========================================
    // 动态配置字段（兼容旧版本）
    // ========================================
    
    @Schema(description = "动态配置数据(JSON格式) - 兼容字段")
    private String dynamicConfig;
}
