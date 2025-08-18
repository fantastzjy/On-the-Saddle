package net.lab1024.sa.admin.module.business.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 商品详情VO
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "商品详情VO")
public class ProductDetailVO {

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

    @Schema(description = "课程配置详情")
    private Map<String, Object> courseDetails;

    @Schema(description = "课时包配置详情")
    private Map<String, Object> packageDetails;

    @Schema(description = "活动配置详情")
    private Map<String, Object> activityDetails;

    @Schema(description = "价格详情")
    private Map<String, Object> priceDetails;

    @Schema(description = "库存详情")
    private Map<String, Object> stockDetails;

    @Schema(description = "可选教练列表")
    private String coachIds;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}