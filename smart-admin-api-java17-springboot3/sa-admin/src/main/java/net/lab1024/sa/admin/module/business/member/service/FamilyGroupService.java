package net.lab1024.sa.admin.module.business.member.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.dao.FamilyGroupDao;
import net.lab1024.sa.admin.module.business.member.dao.FamilyMemberRelationDao;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyGroupEntity;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyMemberRelationEntity;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupCreateForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupSearchForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyMemberAddForm;
import net.lab1024.sa.admin.module.business.member.domain.form.JoinFamilyGroupForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyGroupSearchVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 家庭组管理服务
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class FamilyGroupService {

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
    public ResponseDTO<String> createFamilyGroup(FamilyGroupCreateForm createForm) {
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

        return ResponseDTO.ok();
    }

    /**
     * 添加家庭成员
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> addFamilyMember(FamilyMemberAddForm addForm) {
        if (addForm.getFamilyGroupId() == null || addForm.getMemberId() == null) {
            return ResponseDTO.userErrorParam("家庭组ID和会员ID不能为空");
        }

        // 检查家庭组是否存在
        FamilyGroupEntity familyGroup = familyGroupDao.selectById(addForm.getFamilyGroupId());
        if (familyGroup == null || familyGroup.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("家庭组不存在");
        }

        // 检查会员是否存在
        MemberEntity member = memberDao.selectById(addForm.getMemberId());
        if (member == null || member.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        // 检查会员是否已在家庭组中
        FamilyMemberRelationEntity existingRelation = familyMemberRelationDao.selectByFamilyAndMember(
            addForm.getFamilyGroupId(), addForm.getMemberId());
        if (existingRelation != null) {
            return ResponseDTO.userErrorParam("该会员已在此家庭组中");
        }

        // 检查会员是否属于其他家庭组
        Long existingFamilyGroupId = familyMemberRelationDao.getFamilyGroupIdByMemberId(addForm.getMemberId());
        if (existingFamilyGroupId != null) {
            return ResponseDTO.userErrorParam("该会员已属于其他家庭组");
        }

        // 如果要设置为监护人，检查是否已有监护人
        if (addForm.getIsGuardian() != null && addForm.getIsGuardian() == 1) {
            int guardianCount = familyMemberRelationDao.countGuardiansByFamilyGroupId(addForm.getFamilyGroupId());
            if (guardianCount > 0) {
                return ResponseDTO.userErrorParam("该家庭组已有监护人，每个家庭组只能有一个监护人");
            }
        }

        // 添加家庭成员关系
        FamilyMemberRelationEntity relation = SmartBeanUtil.copy(addForm, FamilyMemberRelationEntity.class);
        relation.setCreateTime(LocalDateTime.now());
        relation.setIsValid(1);
        relation.setIsDelete(0);
        
        if (relation.getIsGuardian() == null) {
            relation.setIsGuardian(0);
        }
        if (relation.getJoinDate() == null) {
            relation.setJoinDate(LocalDate.now());
        }

        familyMemberRelationDao.insert(relation);

        // 记录数据变更日志
        dataTracerService.insert(relation.getId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP);

        return ResponseDTO.ok();
    }

    /**
     * 移除家庭成员
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> removeFamilyMember(Long familyGroupId, Long memberId) {
        if (familyGroupId == null || memberId == null) {
            return ResponseDTO.userErrorParam("家庭组ID和会员ID不能为空");
        }

        // 检查关系是否存在
        FamilyMemberRelationEntity relation = familyMemberRelationDao.selectByFamilyAndMember(familyGroupId, memberId);
        if (relation == null) {
            return ResponseDTO.userErrorParam("该会员不在此家庭组中");
        }

        // 检查是否为监护人
        if (relation.getIsGuardian() == 1) {
            return ResponseDTO.userErrorParam("监护人不能被移除，请先转移监护权或删除家庭组");
        }

        // 软删除关系
        int result = familyMemberRelationDao.removeFamilyMember(familyGroupId, memberId);
        if (result > 0) {
            // 记录数据变更日志
            dataTracerService.delete(relation.getId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP);
            return ResponseDTO.ok();
        }

        return ResponseDTO.userErrorParam("移除家庭成员失败");
    }

    /**
     * 转移监护权
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> transferGuardianship(Long familyGroupId, Long oldGuardianId, Long newGuardianId) {
        if (familyGroupId == null || oldGuardianId == null || newGuardianId == null) {
            return ResponseDTO.userErrorParam("参数不能为空");
        }

        if (oldGuardianId.equals(newGuardianId)) {
            return ResponseDTO.userErrorParam("新旧监护人不能为同一人");
        }

        // 检查旧监护人
        FamilyMemberRelationEntity oldGuardian = familyMemberRelationDao.selectByFamilyAndMember(familyGroupId, oldGuardianId);
        if (oldGuardian == null || oldGuardian.getIsGuardian() != 1) {
            return ResponseDTO.userErrorParam("原监护人不存在或权限错误");
        }

        // 检查新监护人
        FamilyMemberRelationEntity newGuardian = familyMemberRelationDao.selectByFamilyAndMember(familyGroupId, newGuardianId);
        if (newGuardian == null) {
            return ResponseDTO.userErrorParam("新监护人不在此家庭组中");
        }

        // 移除旧监护人权限
        familyMemberRelationDao.setGuardian(familyGroupId, oldGuardianId, 0);
        
        // 设置新监护人权限
        familyMemberRelationDao.setGuardian(familyGroupId, newGuardianId, 1);

        // 记录数据变更日志
        FamilyMemberRelationEntity oldGuardianCopy = SmartBeanUtil.copy(oldGuardian, FamilyMemberRelationEntity.class);
        oldGuardianCopy.setIsGuardian(0);
        dataTracerService.update(oldGuardian.getId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP, oldGuardian, oldGuardianCopy);
        
        FamilyMemberRelationEntity newGuardianCopy = SmartBeanUtil.copy(newGuardian, FamilyMemberRelationEntity.class);
        newGuardianCopy.setIsGuardian(1);
        dataTracerService.update(newGuardian.getId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP, newGuardian, newGuardianCopy);

        return ResponseDTO.ok();
    }

    /**
     * 查询家庭成员信息
     */
    public ResponseDTO<FamilyInfoVO> getFamilyInfo(Long familyGroupId) {
        log.info("开始查询家庭信息，familyGroupId: {}", familyGroupId);
        
        if (familyGroupId == null) {
            log.warn("家庭组ID为空");
            return ResponseDTO.userErrorParam("家庭组ID不能为空");
        }

        // 查询家庭组基本信息
        FamilyGroupEntity familyGroup = familyGroupDao.selectById(familyGroupId);
        log.info("查询到家庭组基本信息: {}", familyGroup);
        
        if (familyGroup == null || familyGroup.getIsDelete() == 1) {
            log.warn("家庭组不存在或已删除，familyGroup: {}", familyGroup);
            return ResponseDTO.userErrorParam("家庭组不存在");
        }

        // 查询家庭成员列表
        List<FamilyInfoVO.FamilyMemberVO> memberList = familyMemberRelationDao.getMembersByFamilyGroupId(familyGroupId);
        log.info("查询到家庭成员列表，数量: {}, 详情: {}", memberList.size(), memberList);

        // 查找主要联系人姓名
        String mainContactName = null;
        if (familyGroup.getMainContactId() != null) {
            for (FamilyInfoVO.FamilyMemberVO member : memberList) {
                if (member.getMemberId().equals(familyGroup.getMainContactId())) {
                    mainContactName = member.getActualName();
                    break;
                }
            }
        }
        log.info("主要联系人姓名: {}", mainContactName);

        // 构建返回对象
        FamilyInfoVO familyInfo = new FamilyInfoVO();
        familyInfo.setFamilyGroupId(familyGroup.getFamilyGroupId());
        familyInfo.setFamilyName(familyGroup.getFamilyName());
        familyInfo.setMainContactName(mainContactName);
        familyInfo.setDescription(familyGroup.getDescription());
        familyInfo.setCreateTime(familyGroup.getCreateTime());
        familyInfo.setMemberList(memberList);

        log.info("构建完成的家庭信息: {}", familyInfo);
        return ResponseDTO.ok(familyInfo);
    }

    /**
     * 根据会员ID查询家庭信息
     */
    public ResponseDTO<FamilyInfoVO> getFamilyInfoByMemberId(Long memberId) {
        log.info("开始根据会员ID查询家庭信息，memberId: {}", memberId);
        
        if (memberId == null) {
            log.warn("会员ID为空");
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        Long familyGroupId = familyMemberRelationDao.getFamilyGroupIdByMemberId(memberId);
        log.info("根据会员ID查询到的家庭组ID: {}", familyGroupId);
        
        if (familyGroupId == null) {
            log.info("该会员未加入任何家庭组，memberId: {}", memberId);
            return ResponseDTO.userErrorParam("该会员未加入任何家庭组");
        }

        return getFamilyInfo(familyGroupId);
    }

    /**
     * 删除家庭组
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> deleteFamilyGroup(Long familyGroupId) {
        if (familyGroupId == null) {
            return ResponseDTO.userErrorParam("家庭组ID不能为空");
        }

        FamilyGroupEntity familyGroup = familyGroupDao.selectById(familyGroupId);
        if (familyGroup == null || familyGroup.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("家庭组不存在");
        }

        // 软删除家庭组
        familyGroup.setIsDelete(1);
        familyGroup.setUpdateTime(LocalDateTime.now());
        familyGroupDao.updateById(familyGroup);

        // 删除所有家庭成员关系
        familyMemberRelationDao.deleteByFamilyGroupId(familyGroupId);

        // 记录数据变更日志
        dataTracerService.delete(familyGroupId, DataTracerTypeEnum.CLUB_FAMILY_GROUP);

        return ResponseDTO.ok();
    }

    /**
     * 搜索家庭组
     */
    public ResponseDTO<List<FamilyGroupSearchVO>> searchFamilyGroups(FamilyGroupSearchForm searchForm) {
        log.info("开始搜索家庭组，参数: {}", searchForm);
        
        if (searchForm.getClubId() == null) {
            log.warn("俱乐部ID为空");
            return ResponseDTO.userErrorParam("俱乐部ID不能为空");
        }
        
        if (searchForm.getKeyword() == null || searchForm.getKeyword().trim().isEmpty()) {
            log.warn("搜索关键字为空");
            return ResponseDTO.userErrorParam("搜索关键字不能为空");
        }

        List<FamilyGroupSearchVO> results = familyGroupDao.searchFamilyGroups(
            searchForm.getSearchType(), 
            searchForm.getKeyword().trim(), 
            searchForm.getClubId()
        );
        
        log.info("搜索到{}个家庭组", results.size());
        return ResponseDTO.ok(results);
    }

    /**
     * 现有会员加入家庭组
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> joinFamilyGroup(JoinFamilyGroupForm joinForm) {
        log.info("现有会员加入家庭组，参数: {}", joinForm);
        
        if (joinForm.getFamilyGroupId() == null || joinForm.getMemberId() == null) {
            log.warn("家庭组ID和会员ID不能为空");
            return ResponseDTO.userErrorParam("家庭组ID和会员ID不能为空");
        }

        // 检查家庭组是否存在
        FamilyGroupEntity familyGroup = familyGroupDao.selectById(joinForm.getFamilyGroupId());
        log.info("查询到家庭组信息: {}", familyGroup);
        
        if (familyGroup == null || familyGroup.getIsDelete() == 1) {
            log.warn("家庭组不存在或已删除");
            return ResponseDTO.userErrorParam("家庭组不存在");
        }

        // 检查会员是否存在
        MemberEntity member = memberDao.selectById(joinForm.getMemberId());
        log.info("查询到会员信息: {}", member);
        
        if (member == null || member.getIsDelete() == 1) {
            log.warn("会员不存在或已删除");
            return ResponseDTO.userErrorParam("会员不存在");
        }

        // 检查会员是否已在此家庭组中
        FamilyMemberRelationEntity existingRelation = familyMemberRelationDao.selectByFamilyAndMember(
            joinForm.getFamilyGroupId(), joinForm.getMemberId());
        if (existingRelation != null) {
            log.warn("该会员已在此家庭组中");
            return ResponseDTO.userErrorParam("该会员已在此家庭组中");
        }

        // 检查会员是否属于其他家庭组
        Long existingFamilyGroupId = familyMemberRelationDao.getFamilyGroupIdByMemberId(joinForm.getMemberId());
        if (existingFamilyGroupId != null) {
            log.warn("该会员已属于其他家庭组，existingFamilyGroupId: {}", existingFamilyGroupId);
            return ResponseDTO.userErrorParam("该会员已属于其他家庭组");
        }

        // 如果要设置为监护人，检查是否已有监护人
        if (joinForm.getIsGuardian() != null && joinForm.getIsGuardian() == 1) {
            int guardianCount = familyMemberRelationDao.countGuardiansByFamilyGroupId(joinForm.getFamilyGroupId());
            if (guardianCount > 0) {
                log.warn("该家庭组已有监护人");
                return ResponseDTO.userErrorParam("该家庭组已有监护人，每个家庭组只能有一个监护人");
            }
        }

        // 添加家庭成员关系
        FamilyMemberRelationEntity relation = new FamilyMemberRelationEntity();
        relation.setFamilyGroupId(joinForm.getFamilyGroupId());
        relation.setMemberId(joinForm.getMemberId());
        relation.setIsGuardian(joinForm.getIsGuardian() == null ? 0 : joinForm.getIsGuardian());
        relation.setJoinDate(joinForm.getJoinDate() == null ? LocalDate.now() : joinForm.getJoinDate());
        relation.setRemark(joinForm.getRemark());
        relation.setCreateTime(LocalDateTime.now());
        relation.setIsValid(1);
        relation.setIsDelete(0);

        log.info("准备插入的家庭成员关系数据: {}", relation);
        familyMemberRelationDao.insert(relation);
        log.info("家庭成员关系插入成功，ID: {}", relation.getId());

        // 记录数据变更日志
        dataTracerService.insert(relation.getId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP);
        log.info("会员加入家庭组完成，relationId: {}", relation.getId());

        return ResponseDTO.ok();
    }
}