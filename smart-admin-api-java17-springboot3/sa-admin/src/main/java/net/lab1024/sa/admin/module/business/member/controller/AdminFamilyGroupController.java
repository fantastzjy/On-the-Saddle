package net.lab1024.sa.admin.module.business.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupQueryForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyGroupCreateForm;
import net.lab1024.sa.admin.module.business.member.domain.form.FamilyMemberAddForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyGroupListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyGroupDetailVO;
import net.lab1024.sa.admin.module.business.member.service.AdminFamilyGroupService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 家庭组后台管理控制器
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Tag(name = "家庭组后台管理")
@RestController
@RequestMapping("/admin/family-group")
public class AdminFamilyGroupController {

    @Resource
    private AdminFamilyGroupService adminFamilyGroupService;

    @Operation(summary = "创建家庭组")
    @PostMapping("/create")
    public ResponseDTO<String> create(@RequestBody @Valid FamilyGroupCreateForm createForm) {
        return adminFamilyGroupService.create(createForm);
    }

    @Operation(summary = "分页查询家庭组列表")
    @PostMapping("/page/query")
    public ResponseDTO<PageResult<FamilyGroupListVO>> pageQuery(@RequestBody @Valid FamilyGroupQueryForm queryForm) {
        return adminFamilyGroupService.pageQuery(queryForm);
    }

    @Operation(summary = "获取家庭组详情")
    @GetMapping("/{familyGroupId}")
    public ResponseDTO<FamilyGroupDetailVO> getDetail(@PathVariable Long familyGroupId) {
        return adminFamilyGroupService.getDetail(familyGroupId);
    }

    @Operation(summary = "更新家庭组信息")
    @PostMapping("/update")
    public ResponseDTO<String> update(@RequestBody @Valid FamilyGroupUpdateForm updateForm) {
        return adminFamilyGroupService.update(updateForm);
    }

    @Operation(summary = "批量删除家庭组")
    @PostMapping("/batch/delete")
    public ResponseDTO<String> batchDelete(@RequestBody @Valid ValidateList<Long> familyGroupIds) {
        return adminFamilyGroupService.batchDelete(familyGroupIds);
    }

    @Operation(summary = "批量恢复家庭组")
    @PostMapping("/batch/restore")
    public ResponseDTO<String> batchRestore(@RequestBody @Valid ValidateList<Long> familyGroupIds) {
        return adminFamilyGroupService.batchRestore(familyGroupIds);
    }

    @Operation(summary = "根据会员ID查询家庭信息")
    @GetMapping("/member/{memberId}")
    public ResponseDTO<FamilyGroupDetailVO> getMemberFamily(@PathVariable Long memberId) {
        return adminFamilyGroupService.getMemberFamily(memberId);
    }

    @Operation(summary = "创建新会员并加入家庭组")
    @PostMapping("/add-family-member")
    public ResponseDTO<String> addFamilyMember(@RequestBody @Valid FamilyMemberAddForm addForm) {
        return adminFamilyGroupService.addFamilyMember(addForm);
    }
}