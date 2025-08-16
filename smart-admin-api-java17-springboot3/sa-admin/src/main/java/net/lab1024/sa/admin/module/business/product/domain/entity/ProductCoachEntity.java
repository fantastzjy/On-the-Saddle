package net.lab1024.sa.admin.module.business.product.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 商品教练关联表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_product_coach")
public class ProductCoachEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("商品ID")
    private Long productId;

    @DataTracerFieldLabel("教练ID")
    private Long coachId;

    @DataTracerFieldLabel("是否默认教练")
    private Integer isDefault;

    private String createBy;

    private LocalDateTime createTime;
}