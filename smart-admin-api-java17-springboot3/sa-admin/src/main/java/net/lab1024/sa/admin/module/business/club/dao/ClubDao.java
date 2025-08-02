package net.lab1024.sa.admin.module.business.club.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubQueryForm;
import net.lab1024.sa.admin.module.business.club.domain.vo.ClubListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 俱乐部数据访问层
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface ClubDao extends BaseMapper<ClubEntity> {

    /**
     * 分页查询俱乐部
     */
    List<ClubListVO> queryPage(Page page, @Param("queryForm") ClubQueryForm queryForm);

    /**
     * 根据俱乐部编码查询
     */
    ClubEntity selectByClubCode(@Param("clubCode") String clubCode);

    /**
     * 根据俱乐部名称查询
     */
    ClubEntity selectByClubName(@Param("clubName") String clubName);

    /**
     * 俱乐部列表查询
     */
    List<ClubListVO> queryList(@Param("isValid") Boolean isValid);
}
