package net.lab1024.sa.admin.module.business.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductCoachEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品教练关联 DAO
 *
 * @Author Claude Code
 * @Date 2025-08-29
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface ProductCoachDao extends BaseMapper<ProductCoachEntity> {
    
    /**
     * 根据产品ID查询关联教练
     */
    List<ProductCoachEntity> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据教练ID查询关联课程
     */
    List<ProductCoachEntity> selectByCoachId(@Param("coachId") Long coachId);
    
    /**
     * 批量插入教练产品关联
     */
    int insertBatch(@Param("list") List<ProductCoachEntity> entities);
    
    /**
     * 删除产品的所有教练关联
     */
    int deleteByProductId(@Param("productId") Long productId);
    
    /**
     * 根据教练ID获取关联的课程信息（连表查询，供小程序使用）
     */
    List<Map<String, Object>> selectCoachCourseList(@Param("coachId") Long coachId, @Param("clubId") Long clubId);
    
    /**
     * 获取产品关联的教练详细信息（连表查询）
     */
    List<Map<String, Object>> selectProductCoachDetails(@Param("productId") Long productId);
}