package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会籍续费历史VO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class MembershipRenewHistoryVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "续费月数")
    private Integer renewMonths;

    @Schema(description = "续费金额")
    private BigDecimal renewAmount;

    @Schema(description = "原到期时间")
    private LocalDate oldExpireDate;

    @Schema(description = "新到期时间")
    private LocalDate newExpireDate;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "续费时间")
    private LocalDateTime renewDate;

    @Schema(description = "创建人")
    private String createBy;
}