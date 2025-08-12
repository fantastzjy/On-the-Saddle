package net.lab1024.sa.admin.module.business.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberCreateForm;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberQueryForm;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberDetailVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberVO;
import net.lab1024.sa.admin.module.business.member.service.MemberService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 会员管理控制器
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Tag(name = "会员管理")
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @Operation(summary = "分页查询会员列表")
    @PostMapping("/queryByPage")
    public ResponseDTO<PageResult<MemberVO>> queryByPage(@RequestBody @Valid MemberQueryForm queryForm) {
        return memberService.queryByPage(queryForm);
    }

    @Operation(summary = "查询会员详情")
    @GetMapping("/{memberId}")
    public ResponseDTO<MemberDetailVO> getDetail(@PathVariable Long memberId) {
        return memberService.getDetail(memberId);
    }

    @Operation(summary = "新建会员")
    @PostMapping("/create")
    public ResponseDTO<String> create(@RequestBody @Valid MemberCreateForm createForm) {
        return memberService.create(createForm);
    }

    @Operation(summary = "更新会员")
    @PostMapping("/update")
    public ResponseDTO<String> update(@RequestBody @Valid MemberUpdateForm updateForm) {
        return memberService.update(updateForm);
    }

    @Operation(summary = "删除会员")
    @DeleteMapping("/{memberId}")
    public ResponseDTO<String> delete(@PathVariable Long memberId) {
        return memberService.delete(memberId);
    }

    @Operation(summary = "批量删除会员")
    @PostMapping("/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> memberIdList) {
        return memberService.batchDelete(memberIdList);
    }

    @Operation(summary = "更新会员状态")
    @PostMapping("/updateStatus/{memberId}/{disabledFlag}")
    public ResponseDTO<String> updateStatus(@PathVariable Long memberId, @PathVariable Integer disabledFlag) {
        return memberService.updateStatus(memberId, disabledFlag);
    }

    @Operation(summary = "重置会员密码")
    @PostMapping("/resetPassword/{memberId}")
    public ResponseDTO<String> resetPassword(@PathVariable Long memberId) {
        return memberService.resetPassword(memberId);
    }

    @Operation(summary = "检查手机号是否存在")
    @GetMapping("/checkPhone")
    public ResponseDTO<Boolean> checkPhoneExists(@RequestParam String phone, 
                                               @RequestParam(required = false) Long excludeId) {
        return memberService.checkPhoneExists(phone, excludeId);
    }

    @Operation(summary = "检查登录名是否存在")
    @GetMapping("/checkLoginName")
    public ResponseDTO<Boolean> checkLoginNameExists(@RequestParam String loginName, 
                                                   @RequestParam(required = false) Long excludeId) {
        return memberService.checkLoginNameExists(loginName, excludeId);
    }

    @Operation(summary = "生成会员编号")
    @GetMapping("/generateMemberNo")
    public ResponseDTO<String> generateMemberNo() {
        return memberService.generateMemberNoApi();
    }

    @Operation(summary = "根据俱乐部查询会员列表")
    @GetMapping("/queryByClub/{clubId}")
    public ResponseDTO<List<MemberVO>> queryListByClub(@PathVariable Long clubId) {
        return memberService.queryListByClub(clubId);
    }
}