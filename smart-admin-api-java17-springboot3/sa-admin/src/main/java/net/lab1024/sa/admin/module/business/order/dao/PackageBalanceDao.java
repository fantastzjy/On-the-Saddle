package net.lab1024.sa.admin.module.business.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.order.domain.entity.PackageBalanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课时包余额表 Dao
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface PackageBalanceDao extends BaseMapper<PackageBalanceEntity> {

    /**
     * 根据订单ID查询课时包余额
     */
    PackageBalanceEntity selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据会员ID查询有效的课时包余额列表
     */
    List<PackageBalanceEntity> selectValidByMemberId(@Param("memberId") Long memberId);

    /**
     * 更新课时包余额
     */
    int updateBalance(@Param("id") Long id, @Param("usedCount") Integer usedCount, 
                     @Param("remainingCount") Integer remainingCount);

    /**
     * 根据订单ID列表查询课时包余额
     */
    List<PackageBalanceEntity> selectByOrderIds(@Param("orderIds") List<Long> orderIds);
}