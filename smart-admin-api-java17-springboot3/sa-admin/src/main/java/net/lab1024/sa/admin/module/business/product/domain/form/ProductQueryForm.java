package net.lab1024.sa.admin.module.business.product.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 商品查询表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "商品查询表单")
public class ProductQueryForm extends PageParam {

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品编码")
    private String productCode;

    @Schema(description = "商品类型: 1-课程 2-课时包 3-活动")
    private Integer productType;

    @Schema(description = "商品状态: 1-上架 2-下架 3-售罄")
    private Integer status;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "创建开始时间")
    private String createTimeBegin;

    @Schema(description = "创建结束时间")
    private String createTimeEnd;

    @Schema(description = "搜索关键词")
    private String keyword;
}