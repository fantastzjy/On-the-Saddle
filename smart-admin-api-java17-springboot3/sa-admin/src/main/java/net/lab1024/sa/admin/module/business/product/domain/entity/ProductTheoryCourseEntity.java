package net.lab1024.sa.admin.module.business.product.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理论课商品表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_product_theory_course")
public class ProductTheoryCourseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("商品ID")
    private Long productId;

    @DataTracerFieldLabel("鞍时")
    private BigDecimal durationPeriods;

    @DataTracerFieldLabel("基础单价")
    private BigDecimal basePrice;

    @DataTracerFieldLabel("最大人数")
    private Integer maxStudents;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}