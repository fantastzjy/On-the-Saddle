package net.lab1024.sa.admin.module.business.club.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubCreateForm;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubQueryForm;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubUpdateForm;
import net.lab1024.sa.admin.module.business.club.domain.vo.ClubListVO;
import net.lab1024.sa.admin.module.business.club.domain.vo.ClubVO;
import net.lab1024.sa.admin.module.business.club.service.ClubService;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ResourceScheduleCreateForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ResourceScheduleQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ResourceScheduleUpdateForm;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ResourceScheduleVO;
import net.lab1024.sa.admin.module.business.schedule.service.ResourceScheduleService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = AdminSwaggerTagConst.Business.CLUB_CLUB)
@OperateLog
public class ClubController {

    @Resource
    private ClubService clubService;

    @Resource
    private ResourceScheduleService resourceScheduleService;

    @Operation(summary = "分页查询俱乐部")
    @PostMapping("/club/club/page/query")
    @SaCheckPermission("club:club:query")
    public ResponseDTO<PageResult<ClubListVO>> queryByPage(@RequestBody @Valid ClubQueryForm queryForm) {
        return clubService.queryByPage(queryForm);
    }

    @Operation(summary = "查询俱乐部详情")
    @GetMapping("/club/club/get/{clubId}")
    @SaCheckPermission("club:club:detail")
    public ResponseDTO<ClubVO> getDetail(@PathVariable Long clubId) {
        return clubService.getDetail(clubId);
    }

    @Operation(summary = "新建俱乐部")
    @PostMapping("/club/club/create")
    @SaCheckPermission("club:club:add")
    public ResponseDTO<String> create(@RequestBody @Valid ClubCreateForm createForm) {
        return clubService.create(createForm);
    }

    @Operation(summary = "更新俱乐部")
    @PostMapping("/club/club/update")
    @SaCheckPermission("club:club:update")
    public ResponseDTO<String> update(@RequestBody @Valid ClubUpdateForm updateForm) {
        return clubService.update(updateForm);
    }

    @Operation(summary = "删除俱乐部")
    @GetMapping("/club/club/delete/{clubId}")
    @SaCheckPermission("club:club:delete")
    public ResponseDTO<String> delete(@PathVariable Long clubId) {
        return clubService.delete(clubId);
    }

    @Operation(summary = "俱乐部列表查询")
    @GetMapping("/club/club/query/list")
    @SaCheckPermission("club:club:query")
    public ResponseDTO<List<ClubListVO>> queryList(@RequestParam(required = false) Boolean isValid) {
        return clubService.queryList(isValid);
    }

    // ==================== 俱乐部时间管理接口 ====================

    @Operation(summary = "查询俱乐部时间安排")
    @PostMapping("/club/club/schedule/query/{clubId}")
    @SaCheckPermission("club:schedule:query")
    public ResponseDTO<List<ResourceScheduleVO>> queryClubSchedule(@PathVariable Long clubId,
                                                                   @RequestBody @Valid ResourceScheduleQueryForm queryForm) {
        return resourceScheduleService.queryByResource(1, clubId, queryForm);
    }

    @Operation(summary = "创建俱乐部时间安排")
    @PostMapping("/club/club/schedule/create")
    @SaCheckPermission("club:schedule:manage")
    public ResponseDTO<String> createClubSchedule(@RequestBody @Valid ResourceScheduleCreateForm createForm) {
        createForm.setResourceType(1); // 设置为俱乐部类型
        return resourceScheduleService.create(createForm);
    }

    @Operation(summary = "更新俱乐部时间安排")
    @PostMapping("/club/club/schedule/update")
    @SaCheckPermission("club:schedule:manage")
    public ResponseDTO<String> updateClubSchedule(@RequestBody @Valid ResourceScheduleUpdateForm updateForm) {
        return resourceScheduleService.update(updateForm);
    }

    @Operation(summary = "删除俱乐部时间安排")
    @GetMapping("/club/club/schedule/delete/{id}")
    @SaCheckPermission("club:schedule:manage")
    public ResponseDTO<String> deleteClubSchedule(@PathVariable Long id) {
        return resourceScheduleService.delete(id);
    }
}
