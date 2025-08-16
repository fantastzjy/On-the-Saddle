package net.lab1024.sa.admin.module.business.product.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程商品表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_product_course")
public class ProductCourseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("商品ID")
    private Long productId;

    @DataTracerFieldLabel("课程分类")
    private Integer classType;

    @DataTracerFieldLabel("时长（分钟）")
    private Integer durationMinutes;

    @DataTracerFieldLabel("时长（鞍时）")
    private BigDecimal durationPeriods;

    @DataTracerFieldLabel("最大人数")
    private Integer maxStudents;

    @DataTracerFieldLabel("教练费")
    private BigDecimal coachFee;

    @DataTracerFieldLabel("马匹费用")
    private BigDecimal horseFee;

    @DataTracerFieldLabel("基础单价")
    private BigDecimal basePrice;

    @DataTracerFieldLabel("多人课价格配置")
    private String multiPriceConfig;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}