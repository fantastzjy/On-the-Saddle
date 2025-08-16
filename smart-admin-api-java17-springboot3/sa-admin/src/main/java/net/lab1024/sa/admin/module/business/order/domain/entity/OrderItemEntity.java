package net.lab1024.sa.admin.module.business.order.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_order_item")
public class OrderItemEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("订单ID")
    private Long orderId;

    @DataTracerFieldLabel("商品ID")
    private Long productId;

    @DataTracerFieldLabel("商品名称")
    private String productName;

    @DataTracerFieldLabel("商品类型")
    private Integer productType;

    @DataTracerFieldLabel("数量")
    private Integer quantity;

    @DataTracerFieldLabel("单价")
    private BigDecimal unitPrice;

    @DataTracerFieldLabel("小计")
    private BigDecimal totalPrice;

    @DataTracerFieldLabel("关联教练ID")
    private Long coachId;

    @DataTracerFieldLabel("期望上课时间")
    private LocalDateTime preferredTime;

    @DataTracerFieldLabel("商品配置")
    private String itemConfig;

    private LocalDateTime createTime;
}