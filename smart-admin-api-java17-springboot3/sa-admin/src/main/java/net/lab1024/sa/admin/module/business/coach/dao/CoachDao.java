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
    CoachEntity selectByCoachNo(@Param("coachNo") String coachNo);

    /**
     * 根据俱乐部和用户ID查询
     */
    CoachEntity selectByClubAndUser(@Param("clubId") Long clubId, @Param("userId") Long userId);

    /**
     * 查询教练详情
     */
    CoachVO getDetail(@Param("coachId") Long coachId);

    /**
     * 教练列表查询
     */
    List<CoachListVO> queryList(@Param("isValid") Integer isValid, @Param("clubId") Long clubId);
    
    // === AI服务专用查询方法 ===
    
    /**
     * 查询前20个教练（AI候选数据用）
     */
    List<CoachEntity> selectTop20Coaches(@Param("clubId") Long clubId);
    
    /**
     * 根据教练编号和俱乐部查询教练（重载方法）
     */
    CoachEntity selectByCoachNoAndClub(@Param("coachNo") String coachNo, @Param("clubId") Long clubId);
    
    /**
     * 根据教练姓名查询
     */
    CoachEntity selectByName(@Param("coachName") String coachName, @Param("clubId") Long clubId);
    
    /**
     * 根据教练姓名模糊查询
     */
    CoachEntity selectByFuzzyName(@Param("coachName") String coachName, @Param("clubId") Long clubId);
    
    /**
     * 根据姓氏前缀匹配教练（支持单字姓氏匹配）
     */
    CoachEntity selectByNamePrefix(@Param("namePrefix") String namePrefix, @Param("clubId") Long clubId);
    
    /**
     * 查询默认教练（第一个有效教练）
     */
    CoachEntity selectDefaultCoach(@Param("clubId") Long clubId);
    
    /**
     * 根据俱乐部ID查询教练（限制数量）
     */
    List<CoachEntity> selectByClubIdLimit(@Param("clubId") Long clubId, @Param("limit") int limit);
}