package net.lab1024.sa.admin.module.business.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.dao.FamilyGroupDao;
import net.lab1024.sa.admin.module.business.member.dao.FamilyMemberRelationDao;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyGroupEntity;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyMemberRelationEntity;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupQueryForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupCreateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyGroupListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyGroupDetailVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 家庭组后台管理服务
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class AdminFamilyGroupService {

    @Resource
    private FamilyGroupDao familyGroupDao;

    @Resource
    private FamilyMemberRelationDao familyMemberRelationDao;

    @Resource
    private MemberDao memberDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 创建家庭组
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> create(FamilyGroupCreateForm createForm) {
        log.info("开始创建家庭组，参数: {}", createForm);
        
        if (createForm.getGuardianMemberId() == null) {
            log.warn("监护人会员ID为空");
            return ResponseDTO.userErrorParam("监护人会员ID不能为空");
        }

        // 检查监护人是否存在
        MemberEntity guardianMember = memberDao.selectById(createForm.getGuardianMemberId());
        log.info("查询到监护人会员信息: {}", guardianMember);
        
        if (guardianMember == null || guardianMember.getIsDelete() == 1) {
            log.warn("监护人会员不存在或已删除，guardianMember: {}", guardianMember);
            return ResponseDTO.userErrorParam("监护人会员不存在");
        }

        // 检查监护人是否已经属于其他家庭组
        Long existingFamilyGroupId = familyMemberRelationDao.getFamilyGroupIdByMemberId(createForm.getGuardianMemberId());
        log.info("监护人已存在的家庭组ID: {}", existingFamilyGroupId);
        
        if (existingFamilyGroupId != null) {
            log.warn("该会员已属于其他家庭组，existingFamilyGroupId: {}", existingFamilyGroupId);
            return ResponseDTO.userErrorParam("该会员已属于其他家庭组");
        }

        // 检查同俱乐部下是否已有同名家庭组
        FamilyGroupEntity existingGroup = familyGroupDao.selectByClubAndName(createForm.getClubId(), createForm.getFamilyName());
        if (existingGroup != null) {
            log.warn("该俱乐部下已存在同名家庭组: {}", existingGroup.getFamilyName());
            return ResponseDTO.userErrorParam("该俱乐部下已存在同名家庭组");
        }

        // 创建家庭组
        FamilyGroupEntity familyGroup = new FamilyGroupEntity();
        familyGroup.setFamilyName(createForm.getFamilyName());
        familyGroup.setClubId(createForm.getClubId());
        familyGroup.setMainContactId(createForm.getGuardianMemberId());
        familyGroup.setDescription(createForm.getDescription());
        familyGroup.setCreateTime(LocalDateTime.now());
        familyGroup.setIsValid(1);
        familyGroup.setIsDelete(0);
        
        log.info("准备插入的家庭组数据: {}", familyGroup);
        familyGroupDao.insert(familyGroup);
        log.info("家庭组插入成功，生成的ID: {}", familyGroup.getFamilyGroupId());

        // 添加监护人关系
        FamilyMemberRelationEntity guardianRelation = new FamilyMemberRelationEntity();
        guardianRelation.setFamilyGroupId(familyGroup.getFamilyGroupId());
        guardianRelation.setMemberId(createForm.getGuardianMemberId());
        guardianRelation.setIsGuardian(1);
        guardianRelation.setJoinDate(LocalDate.now());
        guardianRelation.setRemark("监护人");
        guardianRelation.setCreateTime(LocalDateTime.now());
        guardianRelation.setIsValid(1);
        guardianRelation.setIsDelete(0);
        
        log.info("准备插入的监护人关系数据: {}", guardianRelation);
        familyMemberRelationDao.insert(guardianRelation);
        log.info("监护人关系插入成功，ID: {}", guardianRelation.getId());

        // 记录数据变更日志
        dataTracerService.insert(familyGroup.getFamilyGroupId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP);
        log.info("家庭组创建完成，familyGroupId: {}", familyGroup.getFamilyGroupId());

        return ResponseDTO.ok("家庭组创建成功");
    }

    /**
     * 分页查询家庭组列表
     */
    public ResponseDTO<PageResult<FamilyGroupListVO>> pageQuery(FamilyGroupQueryForm queryForm) {
        log.info("开始分页查询家庭组列表，参数: {}", queryForm);
        
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FamilyGroupListVO> list = familyGroupDao.queryFamilyGroupList(page, queryForm);
        PageResult<FamilyGroupListVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        
        log.info("查询到{}条家庭组记录", pageResult.getTotal());
        
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 获取家庭组详情
     */
    public ResponseDTO<FamilyGroupDetailVO> getDetail(Long familyGroupId) {
        log.info("开始获取家庭组详情，familyGroupId: {}", familyGroupId);
        
        if (familyGroupId == null) {
            return ResponseDTO.userErrorParam("家庭组ID不能为空");
        }

        // 获取家庭组基本信息
        FamilyGroupDetailVO detail = familyGroupDao.getFamilyGroupDetail(familyGroupId);
        if (detail == null) {
            return ResponseDTO.userErrorParam("家庭组不存在");
        }

        // 获取家庭成员详情列表
        List<FamilyGroupDetailVO.FamilyMemberDetailVO> members = familyGroupDao.getFamilyMemberDetails(familyGroupId);
        detail.setMembers(members);
        detail.setMemberCount(members.size());

        log.info("获取家庭组详情成功，家庭组: {}, 成员数量: {}", detail.getFamilyName(), members.size());
        return ResponseDTO.ok(detail);
    }

    /**
     * 更新家庭组信息
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> update(FamilyGroupUpdateForm updateForm) {
        log.info("开始更新家庭组信息，参数: {}", updateForm);
        
        if (updateForm.getFamilyGroupId() == null) {
            return ResponseDTO.userErrorParam("家庭组ID不能为空");
        }

        // 检查家庭组是否存在
        FamilyGroupEntity existingGroup = familyGroupDao.selectById(updateForm.getFamilyGroupId());
        if (existingGroup == null || existingGroup.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("家庭组不存在");
        }

        // 更新家庭组信息
        FamilyGroupEntity updateEntity = new FamilyGroupEntity();
        updateEntity.setFamilyGroupId(updateForm.getFamilyGroupId());
        updateEntity.setFamilyName(updateForm.getFamilyName());
        updateEntity.setMainContactId(updateForm.getMainContactId());
        updateEntity.setDescription(updateForm.getDescription());
        updateEntity.setUpdateTime(LocalDateTime.now());

        int result = familyGroupDao.updateById(updateEntity);
        if (result > 0) {
            // 记录数据变更日志
            dataTracerService.update(updateForm.getFamilyGroupId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP, existingGroup, updateEntity);
            log.info("更新家庭组信息成功，familyGroupId: {}", updateForm.getFamilyGroupId());
            return ResponseDTO.ok();
        }

        return ResponseDTO.userErrorParam("更新家庭组信息失败");
    }

    /**
     * 批量删除家庭组
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> batchDelete(List<Long> familyGroupIds) {
        log.info("开始批量删除家庭组，IDs: {}", familyGroupIds);
        
        if (familyGroupIds == null || familyGroupIds.isEmpty()) {
            return ResponseDTO.userErrorParam("家庭组ID列表不能为空");
        }

        int result = familyGroupDao.batchDelete(familyGroupIds);
        if (result > 0) {
            // 记录数据变更日志
            for (Long familyGroupId : familyGroupIds) {
                dataTracerService.delete(familyGroupId, DataTracerTypeEnum.CLUB_FAMILY_GROUP);
            }
            log.info("批量删除家庭组成功，删除数量: {}", result);
            return ResponseDTO.ok("批量删除成功，共删除" + result + "个家庭组");
        }

        return ResponseDTO.userErrorParam("批量删除家庭组失败");
    }

    /**
     * 批量恢复家庭组
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> batchRestore(List<Long> familyGroupIds) {
        log.info("开始批量恢复家庭组，IDs: {}", familyGroupIds);
        
        if (familyGroupIds == null || familyGroupIds.isEmpty()) {
            return ResponseDTO.userErrorParam("家庭组ID列表不能为空");
        }

        int result = familyGroupDao.batchRestore(familyGroupIds);
        if (result > 0) {
            // 记录数据变更日志
            for (Long familyGroupId : familyGroupIds) {
                dataTracerService.insert(familyGroupId, DataTracerTypeEnum.CLUB_FAMILY_GROUP);
            }
            log.info("批量恢复家庭组成功，恢复数量: {}", result);
            return ResponseDTO.ok("批量恢复成功，共恢复" + result + "个家庭组");
        }

        return ResponseDTO.userErrorParam("批量恢复家庭组失败");
    }

    /**
     * 根据会员ID查询家庭信息
     */
    public ResponseDTO<FamilyGroupDetailVO> getMemberFamily(Long memberId) {
        log.info("根据会员ID查询家庭信息，memberId: {}", memberId);
        
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        // 查询会员所在的家庭组
        FamilyGroupDetailVO familyDetail = familyGroupDao.getFamilyByMemberId(memberId);
        
        if (familyDetail != null) {
            log.info("查询到家庭信息: {}", familyDetail);
            return ResponseDTO.ok(familyDetail);
        } else {
            log.info("该会员未加入任何家庭组，memberId: {}", memberId);
            return ResponseDTO.userErrorParam("该会员未加入任何家庭组");
        }
    }
}