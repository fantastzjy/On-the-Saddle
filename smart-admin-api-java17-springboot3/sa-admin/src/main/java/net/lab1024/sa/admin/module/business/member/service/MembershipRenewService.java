package net.lab1024.sa.admin.module.business.member.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.dao.MembershipRenewHistoryDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.entity.MembershipRenewHistoryEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.MembershipRenewForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.MembershipRenewHistoryVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会籍续费管理服务
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class MembershipRenewService {

    @Resource
    private MemberDao memberDao;

    @Resource
    private MembershipRenewHistoryDao membershipRenewHistoryDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 会员续费
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> renewMembership(MembershipRenewForm renewForm) {
        if (renewForm.getMemberId() == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }
        if (renewForm.getRenewMonths() == null || renewForm.getRenewMonths() <= 0) {
            return ResponseDTO.userErrorParam("续费月数必须大于0");
        }
        if (renewForm.getRenewAmount() == null || renewForm.getRenewAmount().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseDTO.userErrorParam("续费金额不能为负数");
        }

        // 检查会员是否存在
        MemberEntity member = memberDao.selectById(renewForm.getMemberId());
        if (member == null || member.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        // 获取会员当前到期日期
        LocalDate currentExpireDate = member.getMembershipExpireDate();
        LocalDate baseDate = currentExpireDate != null && currentExpireDate.isAfter(LocalDate.now())
            ? currentExpireDate
            : LocalDate.now();

        // 计算新的到期日期
        LocalDate newExpireDate = baseDate.plusMonths(renewForm.getRenewMonths());

        // 创建续费历史记录
        MembershipRenewHistoryEntity renewHistory = SmartBeanUtil.copy(renewForm, MembershipRenewHistoryEntity.class);
        renewHistory.setOldExpireDate(currentExpireDate);
        renewHistory.setNewExpireDate(newExpireDate);
        renewHistory.setRenewDate(LocalDate.now());
        renewHistory.setCreateTime(LocalDateTime.now());
        renewHistory.setIsValid(1);
        renewHistory.setIsDelete(0);

        membershipRenewHistoryDao.insert(renewHistory);

        // 更新会员到期日期
        member.setMembershipExpireDate(newExpireDate);
        member.setUpdateTime(LocalDateTime.now());

        // 如果会员之前已过期或即将过期，更新会员状态为正常
        if (member.getMembershipStatus() == null || member.getMembershipStatus() != 1) {
            member.setMembershipStatus(1); // 正常
        }

        memberDao.updateById(member);

        // 记录数据变更日志
        MemberEntity oldMember = SmartBeanUtil.copy(member, MemberEntity.class);
        MemberEntity newMember = SmartBeanUtil.copy(member, MemberEntity.class);
        newMember.setMembershipExpireDate(newExpireDate);
        newMember.setMembershipStatus(member.getMembershipStatus());
        
        dataTracerService.insert(renewHistory.getId(), DataTracerTypeEnum.CLUB_MEMBERSHIP_RENEW);
        dataTracerService.update(renewForm.getMemberId(), DataTracerTypeEnum.CLUB_MEMBER, oldMember, newMember);

        return ResponseDTO.ok();
    }

    /**
     * 查询会员续费历史
     */
    public ResponseDTO<List<MembershipRenewHistoryVO>> getMembershipHistory(Long memberId) {
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        // 检查会员是否存在
        MemberEntity member = memberDao.selectById(memberId);
        if (member == null || member.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        List<MembershipRenewHistoryVO> historyList = membershipRenewHistoryDao.getMembershipHistoryByMemberId(memberId);
        return ResponseDTO.ok(historyList);
    }

    /**
     * 获取会员最近一次续费记录
     */
    public ResponseDTO<MembershipRenewHistoryEntity> getLastRenewRecord(Long memberId) {
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        MembershipRenewHistoryEntity lastRecord = membershipRenewHistoryDao.getLastRenewRecord(memberId);
        return ResponseDTO.ok(lastRecord);
    }

    /**
     * 检查会员是否需要续费提醒
     */
    public ResponseDTO<Boolean> checkRenewReminder(Long memberId, Integer reminderDays) {
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        MemberEntity member = memberDao.selectById(memberId);
        if (member == null || member.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        if (member.getMembershipExpireDate() == null) {
            return ResponseDTO.ok(true); // 没有到期日期，需要续费
        }

        // 默认提前30天提醒
        if (reminderDays == null) {
            reminderDays = 30;
        }

        LocalDate reminderDate = LocalDate.now().plusDays(reminderDays);
        boolean needReminder = member.getMembershipExpireDate().isBefore(reminderDate) || member.getMembershipExpireDate().equals(reminderDate);

        return ResponseDTO.ok(needReminder);
    }

    /**
     * 获取会员到期状态
     */
    public ResponseDTO<String> getMembershipStatus(Long memberId) {
        if (memberId == null) {
            return ResponseDTO.userErrorParam("会员ID不能为空");
        }

        MemberEntity member = memberDao.selectById(memberId);
        if (member == null || member.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("会员不存在");
        }

        if (member.getMembershipExpireDate() == null) {
            return ResponseDTO.ok("未设置到期日期");
        }

        LocalDate today = LocalDate.now();
        if (member.getMembershipExpireDate().isBefore(today)) {
            return ResponseDTO.ok("已过期");
        } else if (member.getMembershipExpireDate().equals(today)) {
            return ResponseDTO.ok("今日到期");
        } else {
            long daysLeft = today.until(member.getMembershipExpireDate()).getDays();
            if (daysLeft <= 30) {
                return ResponseDTO.ok("即将到期(" + daysLeft + "天)");
            } else {
                return ResponseDTO.ok("正常");
            }
        }
    }

    /**
     * 批量检查会员到期状态并更新
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> batchUpdateMembershipStatus() {
        // 查询所有有效会员
        List<MemberEntity> members = memberDao.queryAllValidMembers();

        int updatedCount = 0;
        LocalDate today = LocalDate.now();

        for (MemberEntity member : members) {
            if (member.getMembershipExpireDate() == null) {
                continue;
            }

            Integer newStatus;
            if (member.getMembershipExpireDate().isBefore(today)) {
                newStatus = 3; // 已过期
            } else if (member.getMembershipExpireDate().equals(today) ||
                      today.until(member.getMembershipExpireDate()).getDays() <= 30) {
                newStatus = 2; // 即将到期
            } else {
                newStatus = 1; // 正常
            }

            // 只有状态发生变化时才更新
            if (!newStatus.equals(member.getMembershipStatus())) {
                member.setMembershipStatus(newStatus);
                member.setUpdateTime(LocalDateTime.now());
                memberDao.updateById(member);
                updatedCount++;

                // 记录数据变更日志
                MemberEntity oldMember = SmartBeanUtil.copy(member, MemberEntity.class);
                oldMember.setMembershipStatus(member.getMembershipStatus()); // 原状态
                MemberEntity newMember = SmartBeanUtil.copy(member, MemberEntity.class);
                newMember.setMembershipStatus(newStatus); // 新状态
                
                dataTracerService.update(member.getMemberId(), DataTracerTypeEnum.CLUB_MEMBER, oldMember, newMember);
            }
        }

        return ResponseDTO.ok("成功更新" + updatedCount + "个会员的到期状态");
    }
}
