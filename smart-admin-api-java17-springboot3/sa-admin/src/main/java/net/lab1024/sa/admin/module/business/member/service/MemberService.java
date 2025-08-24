package net.lab1024.sa.admin.module.business.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.dao.FamilyMemberExtraDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberCreateForm;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberQueryForm;
import net.lab1024.sa.admin.module.openapi.domain.form.MemberUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberDetailVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 会员管理服务
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class MemberService {

    @Resource
    private MemberDao memberDao;

    @Resource
    private FamilyMemberExtraDao familyMemberExtraDao;

    @Resource
    private DataTracerService dataTracerService;


    /**
     * 分页查询会员
     */
    public ResponseDTO<PageResult<MemberVO>> queryByPage(MemberQueryForm queryForm) {
        queryForm.setIsDelete(0);
        queryForm.setIsValid(1);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MemberVO> memberList = memberDao.queryPage(page, queryForm);
        PageResult<MemberVO> pageResult = SmartPageUtil.convert2PageResult(page, memberList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询会员详情
     */
    public ResponseDTO<MemberDetailVO> getDetail(Long memberId) {
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        MemberDetailVO memberDetail = memberDao.getMemberDetail(memberId);
        if (memberDetail == null) {
            return ResponseDTO.userErrorParam("会员不存在");
        }
        return ResponseDTO.ok(memberDetail);
    }

    /**
     * 新建会员
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> create(MemberCreateForm createForm) {
        // 校验手机号是否重复
        if (StringUtils.isNotBlank(createForm.getPhone())) {
            int phoneCount = memberDao.checkPhoneExists(createForm.getPhone(), null);
            if (phoneCount > 0) {
                return ResponseDTO.userErrorParam("该手机号已存在");
            }
        }

        // 生成会员编号
        String memberNo = generateMemberNo();
        createForm.setMemberNo(memberNo);

        // 处理未激活用户
        if (createForm.getRegistrationStatus() == null || createForm.getRegistrationStatus() == 0) {
            createForm.setRegistrationStatus(0);
            createForm.setDisabledFlag(1); // 未激活用户默认禁用
        }

        // 转换实体并保存
        MemberEntity memberEntity = SmartBeanUtil.copy(createForm, MemberEntity.class);
        memberEntity.setCreateTime(LocalDateTime.now());
        memberEntity.setIsValid(1);
        memberEntity.setIsDelete(0);

        memberDao.insert(memberEntity);

        // 记录数据变更日志
        dataTracerService.insert(memberEntity.getMemberId(), DataTracerTypeEnum.CLUB_MEMBER);

        return ResponseDTO.ok();
    }

    /**
     * 更新会员
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> update(MemberUpdateForm updateForm) {
        if (updateForm.getMemberId() == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        // 检查会员是否存在
        MemberEntity existMember = memberDao.selectById(updateForm.getMemberId());
        if (existMember == null || existMember.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        // 校验手机号是否重复
        if (StringUtils.isNotBlank(updateForm.getPhone())) {
            int phoneCount = memberDao.checkPhoneExists(updateForm.getPhone(), updateForm.getMemberId());
            if (phoneCount > 0) {
                return ResponseDTO.userErrorParam("该手机号已存在");
            }
        }

        // 获取更新前的数据用于审计日志
        MemberEntity oldMember = memberDao.selectById(updateForm.getMemberId());

        // 转换实体并更新
        MemberEntity memberEntity = SmartBeanUtil.copy(updateForm, MemberEntity.class);
        memberEntity.setUpdateTime(LocalDateTime.now());

        memberDao.updateById(memberEntity);

        // 记录数据变更日志
        dataTracerService.update(updateForm.getMemberId(), DataTracerTypeEnum.CLUB_MEMBER, oldMember, memberEntity);

        return ResponseDTO.ok();
    }

    /**
     * 删除会员
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> delete(Long memberId) {
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        MemberEntity memberEntity = memberDao.selectById(memberId);
        if (memberEntity == null || memberEntity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        // 软删除
        memberEntity.setIsDelete(1);
        memberEntity.setUpdateTime(LocalDateTime.now());
        memberDao.updateById(memberEntity);

        // 删除扩展信息
        familyMemberExtraDao.deleteByMemberId(memberId);

        // 记录数据变更日志
        dataTracerService.delete(memberId, DataTracerTypeEnum.CLUB_MEMBER);

        return ResponseDTO.ok();
    }

    /**
     * 批量删除会员
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> batchDelete(List<Long> memberIdList) {
        if (memberIdList == null || memberIdList.isEmpty()) {
            return ResponseDTO.userErrorParam("请选择要删除的会员");
        }

        for (Long memberId : memberIdList) {
            this.delete(memberId);
        }

        return ResponseDTO.ok();
    }

    /**
     * 更新会员状态
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> updateStatus(Long memberId, Integer disabledFlag) {
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }
        if (disabledFlag == null) {
            return ResponseDTO.userErrorParam("状态不能为空");
        }

        MemberEntity memberEntity = memberDao.selectById(memberId);
        if (memberEntity == null || memberEntity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        MemberEntity oldMemberEntity = SmartBeanUtil.copy(memberEntity, MemberEntity.class);
        oldMemberEntity.setDisabledFlag(memberEntity.getDisabledFlag());

        int result = memberDao.updateMemberStatus(memberId, disabledFlag);
        if (result > 0) {
            // 获取更新后的数据用于审计日志
            MemberEntity updatedMemberEntity = SmartBeanUtil.copy(memberEntity, MemberEntity.class);
            updatedMemberEntity.setDisabledFlag(disabledFlag);

            // 记录数据变更日志
            dataTracerService.update(memberId, DataTracerTypeEnum.CLUB_MEMBER, oldMemberEntity, updatedMemberEntity);
            return ResponseDTO.ok();
        }

        return ResponseDTO.userErrorParam("更新状态失败");
    }


    /**
     * 检查手机号是否存在
     */
    public ResponseDTO<Boolean> checkPhoneExists(String phone, Long excludeId) {
        if (StringUtils.isBlank(phone)) {
            return ResponseDTO.ok(false);
        }

        int count = memberDao.checkPhoneExists(phone, excludeId);
        return ResponseDTO.ok(count > 0);
    }


    /**
     * 生成会员编号
     */
    public ResponseDTO<String> generateMemberNoApi() {
        String memberNo = this.generateMemberNo();
        return ResponseDTO.ok(memberNo);
    }

    /**
     * 根据俱乐部查询会员列表
     */
    public ResponseDTO<List<MemberVO>> queryListByClub(Long clubId) {
        if (clubId == null) {
            return ResponseDTO.userErrorParam("俱乐部ID不能为空");
        }

        List<MemberVO> memberList = memberDao.queryListByClub(clubId, 1);
        return ResponseDTO.ok(memberList);
    }

    // ==================== 私有方法 ====================

    /**
     * 生成会员编号
     */
    private String generateMemberNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%04d", ThreadLocalRandom.current().nextInt(1000, 9999));
        String memberNo = "M" + dateStr + randomStr;

        // 检查是否重复
        int count = memberDao.checkMemberNoExists(memberNo);
        if (count > 0) {
            // 如果重复，递归生成新的编号
            return generateMemberNo();
        }

        return memberNo;
    }

}
