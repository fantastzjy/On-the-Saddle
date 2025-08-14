package net.lab1024.sa.admin.module.business.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.dao.FamilyGroupDao;
import net.lab1024.sa.admin.module.business.member.dao.FamilyMemberRelationDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.FamilyGroupEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupQueryForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupUpdateForm;
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
    private DataTracerService dataTracerService;

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
}