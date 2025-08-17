package net.lab1024.sa.admin.module.business.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细数据访问层
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

    /**
     * 根据订单ID查询订单明细
     */
    List<OrderItemEntity> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据商品ID查询订单明细
     */
    List<OrderItemEntity> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据教练ID查询订单明细
     */
    List<OrderItemEntity> selectByCoachId(@Param("coachId") Long coachId);

    /**
     * 根据订单ID删除订单明细
     */
    int deleteByOrderId(@Param("orderId") Long orderId);

    /**
     * 批量插入订单明细
     */
    int batchInsert(@Param("orderItems") List<OrderItemEntity> orderItems);
}