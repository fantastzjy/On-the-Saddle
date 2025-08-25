package net.lab1024.sa.admin.module.business.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.schedule.domain.form.CoachScheduleQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachDayViewVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachMonthViewVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachWeekViewVO;
import net.lab1024.sa.admin.module.business.schedule.service.CoachScheduleService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 教练排课资源视图控制器
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-25
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
@RestController
@RequestMapping("/api/coach-schedule")
@Tag(name = "教练排课资源视图")
public class CoachScheduleController {

    @Autowired
    private CoachScheduleService coachScheduleService;

    @Operation(summary = "获取教练日视图数据")
    @PostMapping("/day-view")
    public ResponseDTO<CoachDayViewVO> getDayView(@RequestBody @Valid CoachScheduleQueryForm queryForm) {
        Long clubId = queryForm.getClubId();
        if (clubId == null) {
            clubId = 1L; // 默认俱乐部ID，实际项目中可根据需求调整
        }
        return ResponseDTO.ok(coachScheduleService.getDayViewData(queryForm, clubId));
    }

    @Operation(summary = "获取教练周视图数据")
    @PostMapping("/week-view")
    public ResponseDTO<CoachWeekViewVO> getWeekView(@RequestBody @Valid CoachScheduleQueryForm queryForm) {
        Long clubId = queryForm.getClubId();
        if (clubId == null) {
            clubId = 1L; // 默认俱乐部ID，实际项目中可根据需求调整
        }
        return ResponseDTO.ok(coachScheduleService.getWeekViewData(queryForm, clubId));
    }

    @Operation(summary = "获取教练月视图数据")
    @PostMapping("/month-view")
    public ResponseDTO<CoachMonthViewVO> getMonthView(@RequestBody @Valid CoachScheduleQueryForm queryForm) {
        Long clubId = queryForm.getClubId();
        if (clubId == null) {
            clubId = 1L; // 默认俱乐部ID，实际项目中可根据需求调整
        }
        return ResponseDTO.ok(coachScheduleService.getMonthViewData(queryForm, clubId));
    }
}
