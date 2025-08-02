package net.lab1024.sa.admin.module.business.horse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseListVO;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 马匹管理DAO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface HorseDao extends BaseMapper<HorseEntity> {

    /**
     * 分页查询马匹列表
     */
    List<HorseListVO> queryByPage(Page<?> page, @Param("query") HorseQueryForm queryForm);

    /**
     * 查询马匹详情
     */
    HorseVO getDetail(@Param("horseId") Long horseId);

    /**
     * 查询马匹列表
     */
    List<HorseListVO> queryList(@Param("clubId") Long clubId, @Param("horseType") Integer horseType);

    /**
     * 根据马匹编号查询马匹
     */
    HorseEntity selectByHorseCode(@Param("horseCode") String horseCode, @Param("clubId") Long clubId);

    /**
     * 根据芯片号查询马匹
     */
    HorseEntity selectByChipNo(@Param("chipNo") String chipNo);

    /**
     * 根据马匹编号查询马匹（排除指定ID）
     */
    HorseEntity selectByHorseCodeExcludeId(@Param("horseCode") String horseCode, @Param("clubId") Long clubId, @Param("excludeHorseId") Long excludeHorseId);

    /**
     * 根据芯片号查询马匹（排除指定ID）
     */
    HorseEntity selectByChipNoExcludeId(@Param("chipNo") String chipNo, @Param("excludeHorseId") Long excludeHorseId);
}