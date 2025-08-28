package net.lab1024.sa.admin.module.business.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ConflictCheckForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleCombinedQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleTimeUpdateForm;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.*;
import net.lab1024.sa.admin.module.business.schedule.service.ScheduleService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课表管理控制器
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Tag(name = "课表管理")
@RestController
@RequestMapping("/api/admin/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Operation(summary = "课表列表查询")
    @PostMapping("/query")
    public ResponseDTO<PageResult<ScheduleListVO>> queryScheduleList(@RequestBody @Valid ScheduleQueryForm queryForm) {
        return scheduleService.queryScheduleList(queryForm);
    }

    @Operation(summary = "课程表综合查询", description = "查询订单+预约+课程的综合数据，支持分层展示")
    @PostMapping("/combined/query")
    public ResponseDTO<PageResult<ScheduleCombinedVO>> queryCombinedSchedule(@RequestBody @Valid ScheduleCombinedQueryForm queryForm) {
        return scheduleService.queryCombinedSchedule(queryForm);
    }

    @Operation(summary = "获取教练列表")
    @GetMapping("/coaches")
    public ResponseDTO<List<CoachVO>> getCoachList() {
        return scheduleService.getCoachList();
    }

    @Operation(summary = "课表详情")
    @GetMapping("/detail/{scheduleId}")
    public ResponseDTO<ScheduleDetailVO> getScheduleDetail(@PathVariable Long scheduleId) {
        return scheduleService.getScheduleDetail(scheduleId);
    }

    @Operation(summary = "批量更新课表时间")
    @PostMapping("/batchUpdateTime")
    public ResponseDTO<Void> batchUpdateScheduleTime(@RequestBody @Valid ValidateList<ScheduleTimeUpdateForm> updateForms) {
        return scheduleService.batchUpdateScheduleTime(updateForms);
    }

    @Operation(summary = "检查时间冲突")
    @PostMapping("/checkConflict")
    public ResponseDTO<ConflictCheckVO> checkConflict(@RequestBody @Valid ConflictCheckForm conflictForm) {
        return scheduleService.checkConflict(conflictForm);
    }

    @Operation(summary = "获取推荐时间段")
    @PostMapping("/recommendedTimeSlots")
    public ResponseDTO<List<Object>> getRecommendedTimeSlots(@RequestBody Object request) {
        // TODO: 实现推荐时间段
        return ResponseDTO.ok(List.of());
    }

    @Operation(summary = "获取资源状态")
    @PostMapping("/resourceStatus")
    public ResponseDTO<Object> getResourceStatus(@RequestBody Object request) {
        // TODO: 实现资源状态查询
        return ResponseDTO.ok(new Object());
    }

    @Operation(summary = "解决冲突")
    @PostMapping("/resolveConflict")
    public ResponseDTO<Void> resolveConflict(@RequestBody Object conflictData) {
        // TODO: 实现冲突解决
        return ResponseDTO.userErrorParam("冲突解决功能正在开发中");
    }

    @Operation(summary = "更新课表时间")
    @PostMapping("/updateTime")
    public ResponseDTO<Void> updateScheduleTime(@RequestBody Object timeData) {
        // TODO: 实现课表时间更新
        return ResponseDTO.userErrorParam("时间更新功能正在开发中");
    }

    @Operation(summary = "开始上课")
    @PostMapping("/start/{scheduleId}")
    public ResponseDTO<Void> startLesson(@PathVariable Long scheduleId) {
        // TODO: 实现开始上课
        return ResponseDTO.userErrorParam("开始上课功能正在开发中");
    }

    @Operation(summary = "结束上课")
    @PostMapping("/finish/{scheduleId}")
    public ResponseDTO<Void> finishLesson(@PathVariable Long scheduleId) {
        // TODO: 实现结束上课
        return ResponseDTO.userErrorParam("结束上课功能正在开发中");
    }

    @Operation(summary = "取消课程")
    @PostMapping("/cancel/{scheduleId}")
    public ResponseDTO<Void> cancelLesson(@PathVariable Long scheduleId) {
        // TODO: 实现取消课程
        return ResponseDTO.userErrorParam("取消课程功能正在开发中");
    }

    @Operation(summary = "删除课表")
    @DeleteMapping("/{scheduleId}")
    public ResponseDTO<Void> deleteSchedule(@PathVariable Long scheduleId) {
        // TODO: 实现删除课表
        return ResponseDTO.userErrorParam("删除课表功能正在开发中");
    }
}
