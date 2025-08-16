package net.lab1024.sa.admin.module.business.product.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动商品表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_product_activity")
public class ProductActivityEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("商品ID")
    private Long productId;

    @DataTracerFieldLabel("活动名称")
    private String activityName;

    @DataTracerFieldLabel("活动详情")
    private String activityDetails;

    @DataTracerFieldLabel("活动开始时间")
    private LocalDateTime startTime;

    @DataTracerFieldLabel("活动结束时间")
    private LocalDateTime endTime;

    @DataTracerFieldLabel("活动地点")
    private String activityLocation;

    @DataTracerFieldLabel("活动单价")
    private BigDecimal price;

    @DataTracerFieldLabel("最大参与人数")
    private Integer maxParticipants;

    @DataTracerFieldLabel("退款规则")
    private String refundRule;

    @DataTracerFieldLabel("详情图片地址列表")
    private String detailImages;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}