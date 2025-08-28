package net.lab1024.sa.admin.module.business.activity.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.activity.domain.form.ActivityQueryForm;
import net.lab1024.sa.admin.module.business.activity.domain.vo.ActivityDetailVO;
import net.lab1024.sa.admin.module.business.activity.domain.vo.ActivityListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 活动管理DAO - 联合查询两张表
 *
 * @Author 1024创新实验室
 * @Date 2025-08-28
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface ActivityDao {

    /**
     * 分页查询活动列表（联合查询 m_product + m_product_activity）
     */
    IPage<ActivityListVO> queryActivityList(Page<ActivityListVO> page, @Param("queryForm") ActivityQueryForm queryForm);

    /**
     * 查询活动详情（联合查询 m_product + m_product_activity）
     */
    ActivityDetailVO queryActivityDetail(@Param("productId") Long productId);
}