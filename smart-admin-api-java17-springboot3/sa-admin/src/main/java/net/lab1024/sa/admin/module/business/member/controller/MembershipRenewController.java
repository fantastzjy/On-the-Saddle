package net.lab1024.sa.admin.module.business.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.business.member.domain.entity.MembershipRenewHistoryEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.MembershipRenewForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.MembershipRenewHistoryVO;
import net.lab1024.sa.admin.module.business.member.service.MembershipRenewService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 会籍续费管理控制器
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Tag(name = "会籍续费管理")
@RestController
@RequestMapping("/club/member/membership")
public class MembershipRenewController {

    @Resource
    private MembershipRenewService membershipRenewService;

    @Operation(summary = "会员续费")
    @PostMapping("/renew")
    public ResponseDTO<String> renewMembership(@RequestBody @Valid MembershipRenewForm renewForm) {
        return membershipRenewService.renewMembership(renewForm);
    }

    @Operation(summary = "查询会员续费历史")
    @GetMapping("/history/{memberId}")
    public ResponseDTO<List<MembershipRenewHistoryVO>> getMembershipHistory(@PathVariable Long memberId) {
        return membershipRenewService.getMembershipHistory(memberId);
    }

    @Operation(summary = "获取会员最近一次续费记录")
    @GetMapping("/lastRecord/{memberId}")
    public ResponseDTO<MembershipRenewHistoryEntity> getLastRenewRecord(@PathVariable Long memberId) {
        return membershipRenewService.getLastRenewRecord(memberId);
    }

    @Operation(summary = "检查会员是否需要续费提醒")
    @GetMapping("/checkReminder/{memberId}")
    public ResponseDTO<Boolean> checkRenewReminder(@PathVariable Long memberId,
                                                 @RequestParam(defaultValue = "30") Integer reminderDays) {
        return membershipRenewService.checkRenewReminder(memberId, reminderDays);
    }

    @Operation(summary = "获取会员到期状态")
    @GetMapping("/status/{memberId}")
    public ResponseDTO<String> getMembershipStatus(@PathVariable Long memberId) {
        return membershipRenewService.getMembershipStatus(memberId);
    }

    @Operation(summary = "批量更新会员到期状态")
    @PostMapping("/batchUpdateStatus")
    public ResponseDTO<String> batchUpdateMembershipStatus() {
        return membershipRenewService.batchUpdateMembershipStatus();
    }
}