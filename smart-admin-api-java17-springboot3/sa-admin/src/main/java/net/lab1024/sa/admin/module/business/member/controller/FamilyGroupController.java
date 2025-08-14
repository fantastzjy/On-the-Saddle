package net.lab1024.sa.admin.module.business.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupCreateForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupSearchForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyMemberAddForm;
import net.lab1024.sa.admin.module.business.member.domain.form.JoinFamilyGroupForm;
import net.lab1024.sa.admin.module.business.member.domain.form.SetGuardianForm;
import net.lab1024.sa.admin.module.business.member.domain.form.RemoveFamilyMemberForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyGroupSearchVO;
import net.lab1024.sa.admin.module.business.member.service.FamilyGroupService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 家庭组管理控制器
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Tag(name = "家庭组管理")
@RestController
@RequestMapping("/club/family")
public class FamilyGroupController {

    @Resource
    private FamilyGroupService familyGroupService;

    @Operation(summary = "创建家庭组")
    @PostMapping("/create")
    public ResponseDTO<String> createFamilyGroup(@RequestBody @Valid FamilyGroupCreateForm createForm) {
        return familyGroupService.createFamilyGroup(createForm);
    }

    @Operation(summary = "搜索家庭组")
    @PostMapping("/search")
    public ResponseDTO<List<FamilyGroupSearchVO>> searchFamilyGroups(@RequestBody @Valid FamilyGroupSearchForm searchForm) {
        return familyGroupService.searchFamilyGroups(searchForm);
    }

    @Operation(summary = "添加家庭成员")
    @PostMapping("/addMember")
    public ResponseDTO<String> addFamilyMember(@RequestBody @Valid FamilyMemberAddForm addForm) {
        return familyGroupService.addFamilyMember(addForm);
    }

    @Operation(summary = "现有会员加入家庭组")
    @PostMapping("/joinMember")
    public ResponseDTO<String> joinFamilyGroup(@RequestBody @Valid JoinFamilyGroupForm joinForm) {
        return familyGroupService.joinFamilyGroup(joinForm);
    }

    @Operation(summary = "移除家庭成员")
    @DeleteMapping("/{familyGroupId}/member/{memberId}")
    public ResponseDTO<String> removeFamilyMember(@PathVariable Long familyGroupId, @PathVariable Long memberId) {
        return familyGroupService.removeFamilyMember(familyGroupId, memberId);
    }

    @Operation(summary = "移除家庭成员")
    @PostMapping("/remove-member")
    public ResponseDTO<String> removeFamilyMemberPost(@RequestBody @Valid RemoveFamilyMemberForm removeForm) {
        return familyGroupService.removeFamilyMember(removeForm.getFamilyGroupId(), removeForm.getMemberId());
    }

    @Operation(summary = "设置监护人")
    @PostMapping("/set-guardian")
    public ResponseDTO<String> setGuardian(@RequestBody @Valid SetGuardianForm setGuardianForm) {
        return familyGroupService.setGuardian(setGuardianForm.getFamilyGroupId(), 
                                             setGuardianForm.getMemberId(), 
                                             setGuardianForm.getIsGuardian());
    }

    @Operation(summary = "转移监护权")
    @PostMapping("/transferGuardianship")
    public ResponseDTO<String> transferGuardianship(@RequestParam Long familyGroupId,
                                                  @RequestParam Long oldGuardianId,
                                                  @RequestParam Long newGuardianId) {
        return familyGroupService.transferGuardianship(familyGroupId, oldGuardianId, newGuardianId);
    }

    @Operation(summary = "查询家庭信息")
    @GetMapping("/{familyGroupId}")
    public ResponseDTO<FamilyInfoVO> getFamilyInfo(@PathVariable Long familyGroupId) {
        return familyGroupService.getFamilyInfo(familyGroupId);
    }

    @Operation(summary = "根据会员ID查询家庭信息")
    @GetMapping("/byMember/{memberId}")
    public ResponseDTO<FamilyInfoVO> getFamilyInfoByMemberId(@PathVariable Long memberId) {
        return familyGroupService.getFamilyInfoByMemberId(memberId);
    }

    @Operation(summary = "删除家庭组")
    @DeleteMapping("/{familyGroupId}")
    public ResponseDTO<String> deleteFamilyGroup(@PathVariable Long familyGroupId) {
        return familyGroupService.deleteFamilyGroup(familyGroupId);
    }
}