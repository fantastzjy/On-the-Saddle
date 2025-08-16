package net.lab1024.sa.admin.module.business.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品数据访问层
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

    /**
     * 查询上架商品列表
     */
    List<ProductEntity> selectOnlineProducts(@Param("clubId") Long clubId);

    /**
     * 批量更新商品状态
     */
    int batchUpdateStatus(@Param("productIds") List<Long> productIds, @Param("status") Integer status);
}