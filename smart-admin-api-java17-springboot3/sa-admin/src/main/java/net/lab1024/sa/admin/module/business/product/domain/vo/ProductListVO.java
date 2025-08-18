package net.lab1024.sa.admin.module.business.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

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




    @Schema(description = "排序")
    private Integer sortOrder;

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
}