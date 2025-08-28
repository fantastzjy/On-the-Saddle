package net.lab1024.sa.admin.module.business.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberQueryForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberDetailVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员DAO
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    /**
     * 分页查询会员列表
     */
    List<MemberVO> queryPage(Page<?> page, @Param("queryForm") MemberQueryForm queryForm);

    /**
     * 根据会员编号查询
     */
    MemberEntity selectByMemberNo(@Param("memberNo") String memberNo);


    /**
     * 根据手机号查询
     */
    MemberEntity selectByPhone(@Param("phone") String phone);

    /**
     * 查询会员详情
     */
    MemberDetailVO getMemberDetail(@Param("memberId") Long memberId);

    /**
     * 检查手机号是否存在
     */
    int checkPhoneExists(@Param("phone") String phone, @Param("excludeId") Long excludeId);


    /**
     * 检查会员编号是否存在
     */
    int checkMemberNoExists(@Param("memberNo") String memberNo);

    /**
     * 根据俱乐部ID查询会员列表
     */
    List<MemberVO> queryListByClub(@Param("clubId") Long clubId, @Param("isValid") Integer isValid);

    /**
     * 更新会员状态
     */
    int updateMemberStatus(@Param("memberId") Long memberId, @Param("disabledFlag") Integer disabledFlag, @Param("updateBy") String updateBy);


    /**
     * 更新最后登录时间
     */
    int updateLastLoginTime(@Param("memberId") Long memberId);

    /**
     * 查询所有有效会员
     */
    List<MemberEntity> queryAllValidMembers();

    /**
     * 根据会员ID查找其监护人
     */
    MemberVO getGuardianByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据监护人ID查找其所有被监护人
     */
    List<MemberVO> getGuardedMembersByGuardianId(@Param("guardianId") Long guardianId);
}
