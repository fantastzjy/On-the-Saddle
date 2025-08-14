package net.lab1024.sa.admin.module.business.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyGroupEntity;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyGroupSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 家庭组DAO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface FamilyGroupDao extends BaseMapper<FamilyGroupEntity> {

    /**
     * 根据会员ID获取家庭信息
     */
    FamilyInfoVO getFamilyInfoByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据俱乐部ID和家庭名称查询
     */
    FamilyGroupEntity selectByClubAndName(@Param("clubId") Long clubId, @Param("familyName") String familyName);

    /**
     * 更新主要联系人
     */
    int updateMainContact(@Param("familyGroupId") Long familyGroupId, @Param("mainContactId") Long mainContactId);

    /**
     * 搜索家庭组
     */
    List<FamilyGroupSearchVO> searchFamilyGroups(@Param("searchType") String searchType, 
                                                @Param("keyword") String keyword, 
                                                @Param("clubId") Long clubId);
}