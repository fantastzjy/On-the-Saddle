package net.lab1024.sa.admin.module.business.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单数据访问层
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 根据订单号查询
     */
    OrderEntity selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据会员ID查询订单列表
     */
    List<OrderEntity> selectByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据俱乐部ID查询订单列表
     */
    List<OrderEntity> selectByClubId(@Param("clubId") Long clubId);

    /**
     * 根据订单状态查询
     */
    List<OrderEntity> selectByOrderStatus(@Param("orderStatus") Integer orderStatus);

    /**
     * 更新订单状态
     */
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatus") Integer orderStatus);

    /**
     * 统计订单数量
     */
    int countOrdersByStatus(@Param("clubId") Long clubId, @Param("orderStatus") Integer orderStatus);
}