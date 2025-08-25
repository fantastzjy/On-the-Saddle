package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberAppUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberAppInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MembershipStatusVO;
import net.lab1024.sa.admin.module.business.member.service.MemberAppService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 会员小程序API控制器
 */
@RestController
@RequestMapping("/app/member/info")
@Tag(name = "会员", description = "小程序会员信息相关接口")
public class AppMemberController {

    @Resource
    private MemberAppService memberAppService;

    @PostMapping("/info")
    @Operation(summary = "获取会员个人信息")
    public ResponseDTO<MemberAppInfoVO> getMemberInfo(@RequestBody(required = false) Object emptyParam) {
        return memberAppService.getMemberInfo();
    }

    @PostMapping("/update")
    @Operation(summary = "更新会员个人信息")
    public ResponseDTO<String> updateMemberInfo(@RequestBody @Valid MemberAppUpdateForm form) {
        return memberAppService.updateMemberInfo(form);
    }

    @PostMapping("/family/info")
    @Operation(summary = "获取家庭组信息")
    public ResponseDTO<FamilyInfoVO> getFamilyInfo(@RequestBody(required = false) Object emptyParam) {
        return memberAppService.getFamilyInfo();
    }

    @PostMapping("/avatar/upload")
    @Operation(summary = "上传并更新头像")
    public ResponseDTO<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return memberAppService.uploadAvatar(file);
    }

}
