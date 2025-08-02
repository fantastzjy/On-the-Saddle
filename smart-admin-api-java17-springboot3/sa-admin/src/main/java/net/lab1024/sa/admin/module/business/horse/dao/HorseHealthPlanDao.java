package net.lab1024.sa.admin.module.business.horse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthPlanEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthPlanQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseHealthPlanListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 马匹健康计划DAO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface HorseHealthPlanDao extends BaseMapper<HorseHealthPlanEntity> {

    /**
     * 分页查询健康计划列表
     */
    List<HorseHealthPlanListVO> queryByPage(Page<?> page, @Param("query") HorseHealthPlanQueryForm queryForm);

    /**
     * 查询马匹的健康计划列表
     */
    List<HorseHealthPlanListVO> queryByHorseId(@Param("horseId") Long horseId);

    /**
     * 查询需要提醒的健康计划
     */
    List<HorseHealthPlanListVO> queryNeedReminder(@Param("reminderDate") LocalDateTime reminderDate);

    /**
     * 更新计划的下次执行日期
     */
    int updateNextDate(@Param("planId") Long planId, @Param("nextDate") LocalDateTime nextDate, @Param("lastDate") LocalDateTime lastDate);
}