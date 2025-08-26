package net.lab1024.sa.admin.module.business.selector.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.selector.domain.form.*;
import net.lab1024.sa.admin.module.business.selector.domain.vo.*;
import net.lab1024.sa.admin.module.business.selector.service.SelectorService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 选择器控制器
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@RestController
@RequestMapping("/api/selector")
@Tag(name = "选择器接口")
@Slf4j
public class SelectorController {

    @Autowired
    private SelectorService selectorService;

    // 固定俱乐部ID，后续可以从用户上下文获取
    private static final Long DEFAULT_CLUB_ID = 1L;

    @GetMapping("/horse")
    @Operation(summary = "马匹选择器")
    public ResponseDTO<List<HorseSelectorVO>> queryHorseSelector(@Valid HorseSelectorQueryForm form) {
        form.setClubId(DEFAULT_CLUB_ID);
        List<HorseSelectorVO> result = selectorService.queryHorseSelector(form);
        return ResponseDTO.ok(result);
    }

    @GetMapping("/member")
    @Operation(summary = "会员选择器")
    public ResponseDTO<List<MemberSelectorVO>> queryMemberSelector(@Valid MemberSelectorQueryForm form) {
        form.setClubId(DEFAULT_CLUB_ID);
        List<MemberSelectorVO> result = selectorService.queryMemberSelector(form);
        return ResponseDTO.ok(result);
    }

    @GetMapping("/coach")
    @Operation(summary = "教练选择器")
    public ResponseDTO<List<CoachSelectorVO>> queryCoachSelector(@Valid CoachSelectorQueryForm form) {
        form.setClubId(DEFAULT_CLUB_ID);
        List<CoachSelectorVO> result = selectorService.queryCoachSelector(form);
        return ResponseDTO.ok(result);
    }

    @GetMapping("/employee")
    @Operation(summary = "员工选择器")
    public ResponseDTO<List<EmployeeSelectorVO>> queryEmployeeSelector(@Valid EmployeeSelectorQueryForm form) {
        form.setClubId(DEFAULT_CLUB_ID);
        List<EmployeeSelectorVO> result = selectorService.queryEmployeeSelector(form);
        return ResponseDTO.ok(result);
    }

    @GetMapping("/club")
    @Operation(summary = "俱乐部选择器")
    public ResponseDTO<List<ClubSelectorVO>> queryClubSelector(@Valid ClubSelectorQueryForm form) {
        List<ClubSelectorVO> result = selectorService.queryClubSelector(form);
        return ResponseDTO.ok(result);
    }

    @GetMapping("/course")
    @Operation(summary = "课程选择器")
    public ResponseDTO<List<CourseSelectorVO>> queryCourseSelector(@Valid CourseSelectorQueryForm form) {
        form.setClubId(DEFAULT_CLUB_ID);
        List<CourseSelectorVO> result = selectorService.queryCourseSelector(form);
        return ResponseDTO.ok(result);
    }
}