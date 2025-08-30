package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.member.domain.form.CoachInfoQueryForm;
import net.lab1024.sa.admin.module.openapi.domain.form.OrderCreateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CoachSimpleProfileVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CourseListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubTypeVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.OrderCreateVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.ActivityListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MyHorseListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.FamilyMembersVO;
import net.lab1024.sa.admin.module.openapi.service.HomeService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import cn.hutool.core.util.StrUtil;
import net.lab1024.sa.base.common.code.UserErrorCode;

import java.util.List;
import java.util.Map;

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
public class AppHomeController {

    @Resource
    private HomeService homeService;

    /**
     * 获取俱乐部类型信息
     */
    @PostMapping("/club/type")
    @NoNeedLogin
    @Operation(summary = "获取俱乐部类型")
    public ResponseDTO<ClubTypeVO> getClubType(@RequestBody Map<String, Object> emptyBody,
                                               HttpServletRequest request) {
        String clubCode = request.getHeader("X-Club-Code");
        if (StrUtil.isBlank(clubCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
        }
        return homeService.getClubType(clubCode);
    }

    /**
     * 获取俱乐部详细信息
     */
    @PostMapping("/club/info")
    @NoNeedLogin
    @Operation(summary = "获取俱乐部信息")
    public ResponseDTO<ClubInfoVO> getClubInfo(@RequestBody Map<String, Object> emptyBody,
                                              HttpServletRequest request) {
        String clubCode = request.getHeader("X-Club-Code");
        if (StrUtil.isBlank(clubCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
        }
        return homeService.getClubInfo(clubCode);
    }

    /**
     * 获取教练列表
     */
    @PostMapping("/coach/list")
    @NoNeedLogin
    @Operation(summary = "获取教练列表")
    public ResponseDTO<List<CoachListVO>> getCoachList(@RequestBody Map<String, Object> emptyBody,
                                                      HttpServletRequest request) {
        String clubCode = request.getHeader("X-Club-Code");
        if (StrUtil.isBlank(clubCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
        }
        return homeService.getCoachList(clubCode);
    }

    /**
     * 获取课程列表
     */
    @PostMapping("/course/list")
    @NoNeedLogin
    @Operation(summary = "获取课程列表")
    public ResponseDTO<List<CourseListVO>> getCourseList(@RequestBody Map<String, Object> requestBody,
                                                        HttpServletRequest request) {
        String clubCode = request.getHeader("X-Club-Code");
        if (StrUtil.isBlank(clubCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
        }
        
        // 获取教练编号参数
        String coachNo = null;
        if (requestBody != null && requestBody.containsKey("coachNo")) {
            coachNo = requestBody.get("coachNo").toString();
        }
        
        return homeService.getCourseList(clubCode, coachNo);
    }

    /**
     * 创建订单
     */
    @PostMapping("/order/create")
    @NoNeedLogin
    @Operation(summary = "创建订单")
    public ResponseDTO<OrderCreateVO> createOrder(@RequestBody @Valid OrderCreateForm form) {
        return homeService.createOrder(form);
    }

    /**
     * 获取活动列表
     */
    @PostMapping("/activity/list")
    @NoNeedLogin
    @Operation(summary = "获取活动列表")
    public ResponseDTO<List<ActivityListVO>> getActivityList(@RequestBody Map<String, Object> emptyBody,
                                                            HttpServletRequest request) {
        String clubCode = request.getHeader("X-Club-Code");
        if (StrUtil.isBlank(clubCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
        }
        return homeService.getActivityList(clubCode);
    }

    /**
     * 获取我的马匹列表
     */
    @PostMapping("/horse/my/list")
    @Operation(summary = "获取我的马匹列表")
    public ResponseDTO<List<MyHorseListVO>> getMyHorseList() {
        return homeService.getMyHorseList();
    }

    /**
     * 获取教练个人简要信息
     */
    @PostMapping("/coach/info")
    @NoNeedLogin
    @Operation(summary = "获取教练个人简要信息")
    public ResponseDTO<CoachSimpleProfileVO> getCoachInfo(@RequestBody @Valid CoachInfoQueryForm form) {
        return homeService.getCoachProfile(form.getCoachNo());
    }

    /**
     * 根据教练获取可授课程列表（新增功能）
     */
    @PostMapping("/coach/courses")
    @NoNeedLogin
    @Operation(summary = "获取教练可授课程")
    public ResponseDTO<List<Map<String, Object>>> getCoachCourses(@RequestBody Map<String, Object> request) {
        Long coachId = Long.valueOf(request.get("coachId").toString());
        Long clubId = Long.valueOf(request.get("clubId").toString());
        return homeService.getCoachCourses(coachId, clubId);
    }

    /**
     * 获取家庭成员列表
     */
    @PostMapping("/family/members")
    @Operation(summary = "获取家庭成员列表")
    public ResponseDTO<FamilyMembersVO> getFamilyMembers(@RequestBody Map<String, Object> emptyBody) {
        return homeService.getFamilyMembers();
    }
}
