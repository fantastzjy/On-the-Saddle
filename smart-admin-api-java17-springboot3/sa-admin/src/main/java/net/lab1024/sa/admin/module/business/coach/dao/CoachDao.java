package net.lab1024.sa.admin.module.business.coach.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachQueryForm;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教练DAO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface CoachDao extends BaseMapper<CoachEntity> {

    /**
     * 分页查询教练
     */
    List<CoachListVO> queryPage(Page<?> page, @Param("queryForm") CoachQueryForm queryForm);

    /**
     * 根据教练编号查询
     */
    CoachEntity selectByCoachCode(@Param("coachCode") String coachCode);

    /**
     * 根据身份证号查询
     */
    CoachEntity selectByIdCard(@Param("idCard") String idCard);

    /**
     * 查询教练详情
     */
    CoachVO getDetail(@Param("coachId") Long coachId);

    /**
     * 教练列表查询
     */
    List<CoachListVO> queryList(@Param("isActive") Boolean isActive, @Param("clubId") Long clubId);
}