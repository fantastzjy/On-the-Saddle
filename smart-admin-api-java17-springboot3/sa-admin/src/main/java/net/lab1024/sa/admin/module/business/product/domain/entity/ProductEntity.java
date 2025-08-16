package net.lab1024.sa.admin.module.business.product.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 商品基础表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_product")
public class ProductEntity {

    @TableId(type = IdType.AUTO)
    private Long productId;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("商品名称")
    private String productName;

    @DataTracerFieldLabel("商品编码")
    private String productCode;

    @DataTracerFieldLabel("商品类型")
    private Integer productType;

    @DataTracerFieldLabel("商品子类型")
    private String subType;

    @DataTracerFieldLabel("商品描述")
    private String description;

    @DataTracerFieldLabel("商品图片地址列表")
    private String images;

    @DataTracerFieldLabel("商品状态")
    private Integer status;

    @DataTracerFieldLabel("排序")
    private Integer sortOrder;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean isValid;

    private Boolean isDelete;
}