package net.lab1024.sa.admin.module.business.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyMemberRelationEntity;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 家庭成员关系DAO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface FamilyMemberRelationDao extends BaseMapper<FamilyMemberRelationEntity> {

    /**
     * 根据家庭组ID查询成员列表
     */
    List<FamilyInfoVO.FamilyMemberVO> getMembersByFamilyGroupId(@Param("familyGroupId") Long familyGroupId);

    /**
     * 根据家庭组和会员ID查询关系
     */
    FamilyMemberRelationEntity selectByFamilyAndMember(@Param("familyGroupId") Long familyGroupId, @Param("memberId") Long memberId);

    /**
     * 设置监护人
     */
    int setGuardian(@Param("familyGroupId") Long familyGroupId, @Param("memberId") Long memberId, @Param("isGuardian") Integer isGuardian);

    /**
     * 清除家庭组中所有成员的监护人身份
     */
    int clearGuardianByFamilyGroup(@Param("familyGroupId") Long familyGroupId);

    /**
     * 移除家庭成员（软删除）
     */
    int removeFamilyMember(@Param("familyGroupId") Long familyGroupId, @Param("memberId") Long memberId);

    /**
     * 查询家庭组中的监护人数量
     */
    int countGuardiansByFamilyGroupId(@Param("familyGroupId") Long familyGroupId);

    /**
     * 查询会员所属的家庭组ID
     */
    Long getFamilyGroupIdByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据家庭组ID删除所有成员关系
     */
    int deleteByFamilyGroupId(@Param("familyGroupId") Long familyGroupId);
}