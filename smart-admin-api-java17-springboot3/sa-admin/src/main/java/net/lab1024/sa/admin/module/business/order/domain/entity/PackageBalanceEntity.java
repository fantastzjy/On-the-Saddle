package net.lab1024.sa.admin.module.business.order.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 课时包余额表实体
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@TableName("m_package_balance")
public class PackageBalanceEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("课时包订单ID")
    private Long orderId;

    @DataTracerFieldLabel("购买会员ID")
    private Long memberId;

    @DataTracerFieldLabel("总课时数")
    private Integer totalCount;

    @DataTracerFieldLabel("已使用课时数")
    private Integer usedCount;

    @DataTracerFieldLabel("剩余课时数")
    private Integer remainingCount;

    @DataTracerFieldLabel("过期时间")
    private LocalDateTime expireDate;

    @DataTracerFieldLabel("状态")
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}