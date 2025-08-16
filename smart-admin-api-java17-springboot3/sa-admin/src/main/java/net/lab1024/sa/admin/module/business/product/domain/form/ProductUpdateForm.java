package net.lab1024.sa.admin.module.business.product.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 商品更新表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "商品更新表单")
public class ProductUpdateForm {

    @Schema(description = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

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

    @Schema(description = "商品状态: 1-上架 2-下架")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "动态配置数据(JSON格式)")
    private Map<String, Object> configData;
}
