package net.lab1024.sa.admin.module.business.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付流水表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_payment_flow")
public class PaymentFlowEntity {

    @TableId(type = IdType.AUTO)
    private Long flowId;

    @DataTracerFieldLabel("支付记录ID")
    private Long paymentId;

    @DataTracerFieldLabel("流水类型")
    private Integer flowType;

    @DataTracerFieldLabel("流水金额")
    private BigDecimal flowAmount;

    @DataTracerFieldLabel("流水描述")
    private String flowDesc;

    @DataTracerFieldLabel("操作人ID")
    private Long operatorId;

    @DataTracerFieldLabel("操作人类型")
    private Integer operatorType;

    @DataTracerFieldLabel("流水扩展数据")
    private String flowData;

    private LocalDateTime createTime;
}