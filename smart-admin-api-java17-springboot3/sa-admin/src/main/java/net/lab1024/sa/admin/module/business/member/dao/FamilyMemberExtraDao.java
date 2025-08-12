package net.lab1024.sa.admin.module.business.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyMemberExtraEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 家庭成员扩展信息DAO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface FamilyMemberExtraDao extends BaseMapper<FamilyMemberExtraEntity> {

    /**
     * 根据会员ID查询扩展信息
     */
    FamilyMemberExtraEntity selectByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据会员ID删除扩展信息
     */
    int deleteByMemberId(@Param("memberId") Long memberId);
}