package net.lab1024.sa.admin.module.business.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberAppLoginForm;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberAppUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberAppInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberAppLoginVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MembershipStatusVO;
import net.lab1024.sa.admin.module.business.member.service.MemberAppService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * 会员小程序API控制器
 */
@RestController
@RequestMapping("/app/member/info")
@Tag(name = "会员", description = "小程序会员信息相关接口")
public class AppMemberController {

    @Resource
    private MemberAppService memberAppService;

    @GetMapping("/info")
    @Operation(summary = "获取会员个人信息")
    public ResponseDTO<MemberAppInfoVO> getMemberInfo() {
        return memberAppService.getMemberInfo();
    }

    @PostMapping("/update")
    @Operation(summary = "更新会员个人信息")
    public ResponseDTO<String> updateMemberInfo(@RequestBody @Valid MemberAppUpdateForm form) {
        return memberAppService.updateMemberInfo(form);
    }

    @GetMapping("/membership/status")
    @Operation(summary = "获取会籍状态")
    public ResponseDTO<MembershipStatusVO> getMembershipStatus() {
        return memberAppService.getMembershipStatus();
    }

    @GetMapping("/family/info")
    @Operation(summary = "获取家庭组信息")
    public ResponseDTO<FamilyInfoVO> getFamilyInfo() {
        return memberAppService.getFamilyInfo();
    }

    @NoNeedLogin
    @GetMapping("/check-registration/{unionId}")
    @Operation(summary = "检查用户注册状态")
    public ResponseDTO<Boolean> checkRegistration(@PathVariable String unionId) {
        return memberAppService.checkRegistration(unionId);
    }
}
