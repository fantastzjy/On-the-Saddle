package net.lab1024.sa.admin.module.business.payment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.payment.domain.entity.PaymentRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付记录数据访问层
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface PaymentRecordDao extends BaseMapper<PaymentRecordEntity> {

    /**
     * 根据支付单号查询
     */
    PaymentRecordEntity selectByPaymentNo(@Param("paymentNo") String paymentNo);

    /**
     * 根据第三方交易号查询
     */
    PaymentRecordEntity selectByTradeNo(@Param("tradeNo") String tradeNo);

    /**
     * 根据订单ID查询支付记录
     */
    List<PaymentRecordEntity> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 更新支付状态
     */
    int updatePaymentStatus(@Param("paymentId") Long paymentId, @Param("paymentStatus") Integer paymentStatus);

    /**
     * 更新退款信息
     */
    int updateRefundInfo(@Param("paymentId") Long paymentId, 
                        @Param("refundAmount") java.math.BigDecimal refundAmount,
                        @Param("refundReason") String refundReason,
                        @Param("refundStatus") Integer refundStatus);

    /**
     * 查询待处理的支付记录
     */
    List<PaymentRecordEntity> selectPendingPayments();

    /**
     * 查询超时的支付记录
     */
    List<PaymentRecordEntity> selectExpiredPayments();
}