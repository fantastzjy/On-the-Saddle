package net.lab1024.sa.admin.module.business.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductActivityEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 活动商品表DAO
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface ProductActivityDao extends BaseMapper<ProductActivityEntity> {
}