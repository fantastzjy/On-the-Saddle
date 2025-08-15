package net.lab1024.sa.admin.module.business.member.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.dao.FamilyGroupDao;
import net.lab1024.sa.admin.module.business.member.dao.FamilyMemberRelationDao;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyGroupEntity;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyMemberRelationEntity;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.JoinFamilyGroupForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyInfoVO;
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
     * 设置监护人
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> setGuardian(Long familyGroupId, Long memberId, Integer isGuardian) {
        log.info("开始设置监护人，familyGroupId: {}, memberId: {}, isGuardian: {}", familyGroupId, memberId, isGuardian);
        
        if (familyGroupId == null || memberId == null || isGuardian == null) {
            return ResponseDTO.userErrorParam("参数不能为空");
        }

        // 检查家庭组是否存在
        FamilyGroupEntity familyGroup = familyGroupDao.selectById(familyGroupId);
        if (familyGroup == null || familyGroup.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("家庭组不存在");
        }

        // 检查成员是否在该家庭组中
        FamilyMemberRelationEntity relation = familyMemberRelationDao.selectByFamilyAndMember(familyGroupId, memberId);
        if (relation == null) {
            return ResponseDTO.userErrorParam("该成员不在此家庭组中");
        }

        if (isGuardian == 1) {
            // 检查该成员是否已经是监护人
            if (relation.getIsGuardian() == 1) {
                log.info("会员{}已经是家庭组{}的监护人，无需重复设置", memberId, familyGroupId);
                return ResponseDTO.ok("该成员已是监护人");
            }
            
            // 设为监护人：先清除当前监护人，再设置新监护人
            familyMemberRelationDao.clearGuardianByFamilyGroup(familyGroupId);
            familyMemberRelationDao.setGuardian(familyGroupId, memberId, 1);
            
            // 更新家庭组主联系人
            familyGroupDao.updateMainContact(familyGroupId, memberId);
            
            log.info("成功设置会员{}为家庭组{}的监护人", memberId, familyGroupId);
            return ResponseDTO.ok("监护人设置成功");
        } else {
            // 检查该成员是否已经不是监护人
            if (relation.getIsGuardian() == 0) {
                log.info("会员{}已经不是家庭组{}的监护人，无需重复取消", memberId, familyGroupId);
                return ResponseDTO.ok("该成员已不是监护人");
            }
            
            // 取消监护人身份
            familyMemberRelationDao.setGuardian(familyGroupId, memberId, 0);
            log.info("成功取消会员{}在家庭组{}的监护人身份", memberId, familyGroupId);
            return ResponseDTO.ok("取消监护人成功");
        }
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

        // 检查会员是否已在此家庭组中（包括已删除的记录）
        FamilyMemberRelationEntity existingRelation = familyMemberRelationDao.selectByFamilyAndMemberIncludeDeleted(
            joinForm.getFamilyGroupId(), joinForm.getMemberId());
        
        if (existingRelation != null) {
            if (existingRelation.getIsDelete() == 0) {
                // 已存在且未删除，返回提示
                log.warn("该会员已在此家庭组中，existingRelation: {}", existingRelation);
                return ResponseDTO.userErrorParam("该会员已在此家庭组中");
            } else {
                // 已存在但已删除，恢复该记录
                log.info("发现已删除的关系记录，准备恢复，existingRelation: {}", existingRelation);
                
                // 更新为活跃状态
                existingRelation.setIsGuardian(joinForm.getIsGuardian() == null ? 0 : joinForm.getIsGuardian());
                existingRelation.setJoinDate(joinForm.getJoinDate() == null ? LocalDate.now() : joinForm.getJoinDate());
                existingRelation.setRemark(joinForm.getRemark());
                existingRelation.setUpdateTime(LocalDateTime.now());
                existingRelation.setIsValid(1);
                existingRelation.setIsDelete(0);
                
                // 如果要设置为监护人，检查是否已有监护人
                if (existingRelation.getIsGuardian() == 1) {
                    int guardianCount = familyMemberRelationDao.countGuardiansByFamilyGroupId(joinForm.getFamilyGroupId());
                    if (guardianCount > 0) {
                        log.warn("该家庭组已有监护人");
                        return ResponseDTO.userErrorParam("该家庭组已有监护人，每个家庭组只能有一个监护人");
                    }
                }
                
                log.info("准备更新的家庭成员关系数据: {}", existingRelation);
                familyMemberRelationDao.updateById(existingRelation);
                log.info("家庭成员关系更新成功，ID: {}", existingRelation.getId());
                
                // 记录数据变更日志
                dataTracerService.update(existingRelation.getId(), DataTracerTypeEnum.CLUB_FAMILY_GROUP, existingRelation, existingRelation);
                log.info("会员重新加入家庭组完成，relationId: {}", existingRelation.getId());
                
                return ResponseDTO.ok("会员重新加入家庭组成功");
            }
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

        return ResponseDTO.ok("会员加入家庭组成功");
    }
}