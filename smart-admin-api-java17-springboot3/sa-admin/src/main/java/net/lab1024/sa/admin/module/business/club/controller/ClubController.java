package net.lab1024.sa.admin.module.business.club.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubCreateForm;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubQueryForm;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubUpdateForm;
import net.lab1024.sa.admin.module.business.club.domain.vo.ClubListVO;
import net.lab1024.sa.admin.module.business.club.domain.vo.ClubVO;
import net.lab1024.sa.admin.module.business.club.service.ClubService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 俱乐部管理控制器
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@Tag(name = AdminSwaggerTagConst.Business.MCLUB_CLUB)
@OperateLog
public class ClubController {

    @Resource
    private ClubService clubService;

    @Operation(summary = "分页查询俱乐部")
    @PostMapping("/mclub/club/page/query")
    @SaCheckPermission("mclub:club:query")
    public ResponseDTO<PageResult<ClubListVO>> queryByPage(@RequestBody @Valid ClubQueryForm queryForm) {
        return clubService.queryByPage(queryForm);
    }

    @Operation(summary = "查询俱乐部详情")
    @GetMapping("/mclub/club/get/{clubId}")
    @SaCheckPermission("mclub:club:detail")
    public ResponseDTO<ClubVO> getDetail(@PathVariable Long clubId) {
        return clubService.getDetail(clubId);
    }

    @Operation(summary = "新建俱乐部")
    @PostMapping("/mclub/club/create")
    @SaCheckPermission("mclub:club:add")
    public ResponseDTO<String> create(@RequestBody @Valid ClubCreateForm createForm) {
        return clubService.create(createForm);
    }

    @Operation(summary = "更新俱乐部")
    @PostMapping("/mclub/club/update")
    @SaCheckPermission("mclub:club:update")
    public ResponseDTO<String> update(@RequestBody @Valid ClubUpdateForm updateForm) {
        return clubService.update(updateForm);
    }

    @Operation(summary = "删除俱乐部")
    @GetMapping("/mclub/club/delete/{clubId}")
    @SaCheckPermission("mclub:club:delete")
    public ResponseDTO<String> delete(@PathVariable Long clubId) {
        return clubService.delete(clubId);
    }

    @Operation(summary = "俱乐部列表查询")
    @GetMapping("/mclub/club/query/list")
    @SaCheckPermission("mclub:club:query")
    public ResponseDTO<List<ClubListVO>> queryList(@RequestParam(required = false) Boolean isValid) {
        return clubService.queryList(isValid);
    }
}
