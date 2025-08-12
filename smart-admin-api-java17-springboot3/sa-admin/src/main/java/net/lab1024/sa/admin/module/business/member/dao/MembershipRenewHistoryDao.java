package net.lab1024.sa.admin.module.business.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.member.domain.entity.MembershipRenewHistoryEntity;
import net.lab1024.sa.admin.module.business.member.domain.vo.MembershipRenewHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会籍续费历史DAO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface MembershipRenewHistoryDao extends BaseMapper<MembershipRenewHistoryEntity> {

    /**
     * 根据会员ID查询续费历史
     */
    List<MembershipRenewHistoryVO> getMembershipHistoryByMemberId(@Param("memberId") Long memberId);

    /**
     * 查询会员最近一次续费记录
     */
    MembershipRenewHistoryEntity getLastRenewRecord(@Param("memberId") Long memberId);
}