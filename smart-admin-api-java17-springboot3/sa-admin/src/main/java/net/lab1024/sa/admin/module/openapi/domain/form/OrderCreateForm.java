package net.lab1024.sa.admin.module.openapi.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.admin.module.business.member.domain.vo.BookingTimeVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单创建表单
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "订单创建表单")
public class OrderCreateForm {

    @Schema(description = "俱乐部编码", required = true)
    @NotBlank(message = "俱乐部编码不能为空")
    private String clubCode;

    @Schema(description = "教练编号", required = true)
    @NotBlank(message = "教练编号不能为空")
    private String coachNo;

    @Schema(description = "课程编号", required = true)
    @NotBlank(message = "课程编号不能为空")
    private String courseCode;

    @Schema(description = "预约时间信息列表", required = true)
    @NotEmpty(message = "预约时间不能为空")
    private List<BookingTimeVO> times;

    @Schema(description = "教练费", required = true)
    @NotNull(message = "教练费不能为空")
    private BigDecimal coachFee;

    @Schema(description = "基础费", required = true)
    @NotNull(message = "基础费不能为空")
    private BigDecimal baseFee;

    @Schema(description = "总计", required = true)
    @NotNull(message = "总计不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "被服务会员ID")
    private Long selectedMemberId;

    @Schema(description = "被服务会员编号")
    private String selectedMemberNo;

    @Schema(description = "被服务会员姓名")
    private String selectedMemberName;

    @Schema(description = "被服务会员手机号")
    private String selectedMemberPhone;

    @Schema(description = "是否为监护人购买：false-否 true-是")
    private Boolean isGuardianPurchase;

    @Schema(description = "订单来源：1-手动下单 2-语音约课")
    private Integer source;
}
