package net.lab1024.sa.admin.module.business.order.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 订单查询表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "订单查询表单")
public class OrderQueryForm extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "订单状态")
    private Integer orderStatus;


    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @Schema(description = "支付时间开始")
    private LocalDateTime paymentTimeStart;

    @Schema(description = "支付时间结束")
    private LocalDateTime paymentTimeEnd;

    @Schema(description = "关键词（会员姓名、订单号等）")
    private String keywords;
}
