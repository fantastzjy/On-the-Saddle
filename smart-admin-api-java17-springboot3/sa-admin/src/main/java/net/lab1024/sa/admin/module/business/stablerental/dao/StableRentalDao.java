package net.lab1024.sa.admin.module.business.stablerental.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.stablerental.domain.entity.StableRentalEntity;
import net.lab1024.sa.admin.module.business.stablerental.domain.form.StableRentalQueryForm;
import net.lab1024.sa.admin.module.business.stablerental.domain.vo.StableRentalDetailVO;
import net.lab1024.sa.admin.module.business.stablerental.domain.vo.StableRentalListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 马房租赁Dao
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface StableRentalDao extends BaseMapper<StableRentalEntity> {

    /**
     * 分页查询马房租赁列表
     */
    Page<StableRentalListVO> queryPage(Page page, @Param("queryForm") StableRentalQueryForm queryForm);

    /**
     * 根据ID查询马房租赁详情
     */
    StableRentalDetailVO getDetailById(@Param("rentalId") Long rentalId);

    /**
     * 根据租赁单号查询
     */
    StableRentalEntity getByRentalNo(@Param("rentalNo") String rentalNo);
}