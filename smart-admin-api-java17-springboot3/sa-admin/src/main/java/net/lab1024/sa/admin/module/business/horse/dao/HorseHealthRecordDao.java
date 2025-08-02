package net.lab1024.sa.admin.module.business.horse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthRecordEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthRecordQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseHealthRecordListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 马匹健康记录DAO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface HorseHealthRecordDao extends BaseMapper<HorseHealthRecordEntity> {

    /**
     * 分页查询健康记录列表
     */
    List<HorseHealthRecordListVO> queryByPage(Page<?> page, @Param("query") HorseHealthRecordQueryForm queryForm);

    /**
     * 查询马匹的健康记录列表（时间线）
     */
    List<HorseHealthRecordListVO> queryByHorseId(@Param("horseId") Long horseId);

    /**
     * 查询计划的健康记录列表
     */
    List<HorseHealthRecordListVO> queryByPlanId(@Param("planId") Long planId);
}