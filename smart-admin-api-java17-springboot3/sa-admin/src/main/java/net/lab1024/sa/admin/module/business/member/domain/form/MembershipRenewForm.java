package net.lab1024.sa.admin.module.business.member.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 会籍续费表单
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class MembershipRenewForm {

    @Schema(description = "会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Schema(description = "续费月数")
    @NotNull(message = "续费月数不能为空")
    @Min(value = 1, message = "续费月数不能少于1个月")
    @Max(value = 60, message = "续费月数不能超过60个月")
    private Integer renewMonths;

    @Schema(description = "续费金额")
    @NotNull(message = "续费金额不能为空")
    @DecimalMin(value = "0", message = "续费金额不能小于0")
    private BigDecimal renewAmount;

    @Schema(description = "原到期时间")
    private LocalDate oldExpireDate;

    @Schema(description = "新到期时间")
    @NotNull(message = "新到期时间不能为空")
    private LocalDate newExpireDate;

    @Schema(description = "支付方式")
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}