package net.lab1024.sa.admin.module.business.member.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会籍续费历史实体
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
@TableName("m_membership_renew_history")
public class MembershipRenewHistoryEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("续费月数")
    private Integer renewMonths;

    @DataTracerFieldLabel("续费金额")
    private BigDecimal renewAmount;

    @DataTracerFieldLabel("原到期时间")
    private LocalDate oldExpireDate;

    @DataTracerFieldLabel("新到期时间")
    private LocalDate newExpireDate;

    @DataTracerFieldLabel("支付方式")
    private String paymentMethod;

    @DataTracerFieldLabel("备注")
    private String remark;

    @DataTracerFieldLabel("续费时间")
    private LocalDateTime renewDate;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @DataTracerFieldLabel("是否有效")
    private Integer isValid;

    @DataTracerFieldLabel("是否删除")
    private Integer isDelete;
}