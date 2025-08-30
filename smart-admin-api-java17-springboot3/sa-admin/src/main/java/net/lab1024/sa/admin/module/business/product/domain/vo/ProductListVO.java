package net.lab1024.sa.admin.module.business.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品列表VO
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "商品列表VO")
public class ProductListVO {

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品编码")
    private String productCode;

    @Schema(description = "商品类型: 1-课程 2-课时包 3-活动")
    private Integer productType;

    @Schema(description = "商品类型名称")
    private String productTypeName;

    @Schema(description = "商品子类型")
    private String subType;

    // ========== 课程详细信息 ==========
    @Schema(description = "课程分类：1-单人课，2-小组课")
    private Integer classType;

    @Schema(description = "课程分类名称")
    private String classTypeName;

    @Schema(description = "课时费")
    private BigDecimal sessionFee;

    @Schema(description = "鞍时数")
    private BigDecimal durationPeriods;

    @Schema(description = "最大人数")
    private Integer maxStudents;

    @Schema(description = "时长（分钟）- 用于体验课")
    private Integer durationMinutes;

    @Schema(description = "小组课多人价格配置JSON")
    private String multiPriceConfig;

    @Schema(description = "小组课多人价格展示文本")
    private String multiPriceDisplay;

    @Schema(description = "解析后的价格配置列表")
    private List<PriceConfigItem> priceConfigs;

    // ========== 课时包详细信息 ==========
    @Schema(description = "总课时数")
    private Integer totalSessions;

    @Schema(description = "有效期天数")
    private Integer validityDays;

    @Schema(description = "库存数量")
    private Integer stockQuantity;

    @Schema(description = "课时包价格")
    private BigDecimal packagePrice;

    // ========== 统一显示信息 ==========
    @Schema(description = "价格展示文本")
    private String priceDisplay;

    @Schema(description = "时长展示文本")
    private String durationDisplay;

    @Schema(description = "容量展示文本")
    private String capacityDisplay;

    @Schema(description = "价格信息")
    private String priceInfo;

    @Schema(description = "库存信息")
    private String stockInfo;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 价格配置项
     */
    @Data
    @Schema(description = "价格配置项")
    public static class PriceConfigItem {
        
        @Schema(description = "参与人数")
        private Integer participantCount;
        
        @Schema(description = "每人价格")
        private BigDecimal pricePerPerson;
        
        @Schema(description = "显示文本")
        private String displayText;
    }
}