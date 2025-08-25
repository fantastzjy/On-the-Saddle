package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachAppInfoVO;
import net.lab1024.sa.admin.module.business.coach.service.CoachAppService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * 教练小程序API控制器
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@RestController
@RequestMapping("/app/coach")
@Tag(name = "教练", description = "小程序教练信息相关接口")
public class AppCoachController {

    @Resource
    private CoachAppService coachAppService;

    @PostMapping("/info")
    @Operation(summary = "获取教练个人信息")
    public ResponseDTO<CoachAppInfoVO> getCoachInfo(@RequestBody(required = false) Object emptyParam) {
        return coachAppService.getCoachInfo();
    }
}