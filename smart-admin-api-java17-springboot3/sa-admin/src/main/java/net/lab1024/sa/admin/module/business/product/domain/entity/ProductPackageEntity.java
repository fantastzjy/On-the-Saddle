package net.lab1024.sa.admin.module.business.product.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课时包商品表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_product_package")
public class ProductPackageEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("商品ID")
    private Long productId;

    @DataTracerFieldLabel("课包详情")
    private String details;

    @DataTracerFieldLabel("课包单价")
    private BigDecimal price;

    @DataTracerFieldLabel("总课时数")
    private Integer totalSessions;

    @DataTracerFieldLabel("有效期（天）")
    private Integer validityDays;

    @DataTracerFieldLabel("库存数量")
    private Integer stockQuantity;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}