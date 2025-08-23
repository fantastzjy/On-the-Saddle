package net.lab1024.sa.admin.module.business.coach.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachCreateForm;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachQueryForm;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachUpdateForm;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachVO;
import net.lab1024.sa.admin.module.business.coach.service.CoachService;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ResourceScheduleCreateForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ResourceScheduleQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ResourceScheduleUpdateForm;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ResourceScheduleVO;
import net.lab1024.sa.admin.module.business.schedule.service.ResourceScheduleService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 教练管理控制器
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@Tag(name = AdminSwaggerTagConst.Business.CLUB_COACH)
@OperateLog
public class CoachController {

    @Resource
    private CoachService coachService;

    @Resource
    private ResourceScheduleService resourceScheduleService;

    @Operation(summary = "分页查询教练")
    @PostMapping("/club/coach/page/query")
    @SaCheckPermission("club:coach:query")
    public ResponseDTO<PageResult<CoachListVO>> queryByPage(@RequestBody @Valid CoachQueryForm queryForm) {
        return coachService.queryByPage(queryForm);
    }

    @Operation(summary = "查询教练详情")
    @GetMapping("/club/coach/get/{coachId}")
    @SaCheckPermission("club:coach:detail")
    public ResponseDTO<CoachVO> getDetail(@PathVariable Long coachId) {
        return coachService.getDetail(coachId);
    }

    @Operation(summary = "新建教练")
    @PostMapping("/club/coach/create")
    @SaCheckPermission("club:coach:add")
    public ResponseDTO<String> create(@RequestBody @Valid CoachCreateForm createForm) {
        return coachService.create(createForm);
    }

    @Operation(summary = "更新教练")
    @PostMapping("/club/coach/update")
    @SaCheckPermission("club:coach:update")
    public ResponseDTO<String> update(@RequestBody @Valid CoachUpdateForm updateForm) {
        return coachService.update(updateForm);
    }

    @Operation(summary = "删除教练")
    @GetMapping("/club/coach/delete/{coachId}")
    @SaCheckPermission("club:coach:delete")
    public ResponseDTO<String> delete(@PathVariable Long coachId) {
        return coachService.delete(coachId);
    }

    @Operation(summary = "教练列表查询")
    @GetMapping("/club/coach/query/list")
    @SaCheckPermission("club:coach:query")
    public ResponseDTO<List<CoachListVO>> queryList(@RequestParam(required = false) Integer isValid,
                                                    @RequestParam(required = false) Long clubId) {
        return coachService.queryList(isValid, clubId);
    }

    // ==================== 教练时间管理接口 ====================

    @Operation(summary = "查询教练时间安排")
    @PostMapping("/club/coach/schedule/query/{coachId}")
    @SaCheckPermission("coach:schedule:query")
    public ResponseDTO<List<ResourceScheduleVO>> queryCoachSchedule(@PathVariable Long coachId,
                                                                    @RequestBody @Valid ResourceScheduleQueryForm queryForm) {
        return resourceScheduleService.queryByResource(2, coachId, queryForm);
    }

    @Operation(summary = "创建教练时间安排")
    @PostMapping("/club/coach/schedule/create")
    @SaCheckPermission("coach:schedule:manage")
    public ResponseDTO<String> createCoachSchedule(@RequestBody @Valid ResourceScheduleCreateForm createForm) {
        createForm.setResourceType(2); // 设置为教练类型
        return resourceScheduleService.create(createForm);
    }

    @Operation(summary = "更新教练时间安排")
    @PostMapping("/club/coach/schedule/update")
    @SaCheckPermission("coach:schedule:manage")
    public ResponseDTO<String> updateCoachSchedule(@RequestBody @Valid ResourceScheduleUpdateForm updateForm) {
        return resourceScheduleService.update(updateForm);
    }

    @Operation(summary = "删除教练时间安排")
    @GetMapping("/club/coach/schedule/delete/{id}")
    @SaCheckPermission("coach:schedule:manage")
    public ResponseDTO<String> deleteCoachSchedule(@PathVariable Long id) {
        return resourceScheduleService.delete(id);
    }
}