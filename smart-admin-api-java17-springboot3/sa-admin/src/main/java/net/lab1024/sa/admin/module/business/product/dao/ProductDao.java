package net.lab1024.sa.admin.module.business.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductQueryForm;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductDetailVO;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品数据访问层
 * 扩展支持模块2.1的商品管理需求
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface ProductDao extends BaseMapper<ProductEntity> {

    /**
     * 根据俱乐部ID查询商品列表
     */
    List<ProductEntity> selectByClubId(@Param("clubId") Long clubId);

    /**
     * 根据商品类型查询商品列表
     */
    List<ProductEntity> selectByProductType(@Param("productType") Integer productType);

    /**
     * 根据商品编码查询
     */
    ProductEntity selectByProductCode(@Param("clubId") Long clubId, @Param("productCode") String productCode);



    // ========================================
    // 新增方法 - 模块2.1商品管理功能
    // ========================================

    /**
     * 分页查询商品列表（关联查询俱乐部信息）
     */
    Page<ProductListVO> queryProductList(Page<ProductEntity> page, @Param("queryForm") ProductQueryForm queryForm);

    /**
     * 分页查询商品列表（增强版 - 关联课程和课时包详情）
     */
    Page<ProductListVO> queryProductListWithDetails(Page<ProductEntity> page, @Param("queryForm") ProductQueryForm queryForm);

    /**
     * 查询商品详情（关联查询相关配置信息）
     */
    ProductDetailVO getProductDetail(@Param("productId") Long productId);

    /**
     * 统计商品数量
     */
    int countProductsByClub(@Param("clubId") Long clubId);

    /**
     * 查询热门商品
     */
    List<ProductListVO> getPopularProducts(@Param("clubId") Long clubId, @Param("limit") Integer limit);

    /**
     * 检查商品编码是否存在
     */
    int checkProductCodeExists(@Param("productCode") String productCode, @Param("clubId") Long clubId, @Param("excludeId") Long excludeId);
    
    // === AI服务专用查询方法 ===
    
    /**
     * 查询课程类产品（AI候选数据用）
     */
    List<ProductEntity> selectCourseProducts(@Param("clubId") Long clubId);
    
    /**
     * 根据课程编码查询产品
     */
    ProductEntity selectByCourseCode(@Param("courseCode") String courseCode, @Param("clubId") Long clubId);
    
    /**
     * 根据课程名称查询产品
     */
    ProductEntity selectByCourseName(@Param("courseName") String courseName, @Param("clubId") Long clubId);
    
    /**
     * 根据课程名称模糊查询产品
     */
    ProductEntity selectByFuzzyCourseName(@Param("courseName") String courseName, @Param("clubId") Long clubId);
}