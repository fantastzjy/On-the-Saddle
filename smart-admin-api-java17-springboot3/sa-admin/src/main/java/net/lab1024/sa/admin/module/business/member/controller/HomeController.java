package net.lab1024.sa.admin.module.business.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.member.domain.form.HomeQueryForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CourseListVO;
import net.lab1024.sa.admin.module.business.member.service.HomeService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序首页控制器
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@RestController
@RequestMapping("/app/home")
@Tag(name = "首页", description = "小程序首页业务信息查询接口")
public class HomeController {

    @Resource
    private HomeService homeService;

    /**
     * 获取俱乐部详细信息
     */
    @PostMapping("/club/info")
    @NoNeedLogin
    @Operation(summary = "获取俱乐部详细信息")
    public ResponseDTO<ClubInfoVO> getClubInfo(@RequestBody @Valid HomeQueryForm form) {
        return homeService.getClubInfo(form.getClubCode());
    }

    /**
     * 获取教练列表
     */
    @PostMapping("/coach/list")
    @NoNeedLogin
    @Operation(summary = "获取教练列表")
    public ResponseDTO<List<CoachListVO>> getCoachList(@RequestBody @Valid HomeQueryForm form) {
        return homeService.getCoachList(form.getClubCode());
    }

    /**
     * 获取课程列表
     */
    @PostMapping("/course/list")
    @NoNeedLogin
    @Operation(summary = "获取课程列表")
    public ResponseDTO<List<CourseListVO>> getCourseList(@RequestBody @Valid HomeQueryForm form) {
        return homeService.getCourseList(form.getClubCode());
    }
}
