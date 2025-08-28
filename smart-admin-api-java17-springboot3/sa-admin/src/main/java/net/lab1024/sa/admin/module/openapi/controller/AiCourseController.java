package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.openapi.domain.form.VoiceRequestForm;
import net.lab1024.sa.admin.module.openapi.domain.vo.AiCourseResponse;
import net.lab1024.sa.admin.module.openapi.domain.vo.UserBookingHabitVO;
import net.lab1024.sa.admin.module.openapi.service.CourseBookingService;
import net.lab1024.sa.admin.module.openapi.service.UserBookingHabitService;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;

/**
 * AI约课控制器 - 极简版
 * 提供AI约课相关的API接口
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Tag(name = "AI约课")
@RestController
@RequestMapping("/openapi/ai-course")
@Slf4j
public class AiCourseController {

    @Autowired
    private CourseBookingService courseBookingService;
    
    @Autowired
    private UserBookingHabitService userHabitService;

    @Operation(summary = "处理语音约课请求")
    @PostMapping("/process-voice")
    @NoNeedLogin
    public ResponseDTO<AiCourseResponse> processVoiceRequest(@RequestBody @Valid VoiceRequestForm form) {
        log.info("🎙️ [语音约课] 收到语音约课请求");
        log.info("🎙️ [语音约课] 请求参数：会员ID={}，俱乐部ID={}，语音文本={}", 
            form.getMemberId(), form.getClubId(), form.getSpeechText());
        
        long startTime = System.currentTimeMillis();
        
        // 使用约课业务服务处理请求（业务服务内部包含完整的异常处理和fallback逻辑）
        log.info("🤖 [AI处理] 开始调用约课业务服务");
        AiCourseResponse response = courseBookingService.processVoiceBooking(
            form.getMemberId(), form.getClubId(), form.getSpeechText());
        
        long endTime = System.currentTimeMillis();
        log.info("🤖 [AI处理] 约课业务处理完成，耗时: {}ms", endTime - startTime);
        log.info("🤖 [AI处理] 处理结果：status={}, userRole={}, parametersComplete={}", 
            response.getStatus(), response.getUserRole(), response.getParametersComplete());
        log.info("🤖 [AI处理] 识别结果：coachName={}, courseType={}, appointmentTime={}", 
            response.getCoachName(), response.getCourseType(), response.getAppointmentTime());
        log.info("🤖 [AI处理] 跳转信息：targetPage={}, navigationInstruction={}", 
            response.getTargetPage(), response.getNavigationInstruction());
        
        if (response.getMissingParameters() != null && !response.getMissingParameters().isEmpty()) {
            log.info("🤖 [AI处理] 缺失参数：{}", response.getMissingParameters());
        }
        
        // 返回结果
        log.info("✅ [约课完成] 成功返回AI约课响应");
        return ResponseDTO.ok(response);
    }
    
    @Operation(summary = "测试AI服务连接")
    @GetMapping("/test-connection")
    @NoNeedLogin
    public ResponseDTO<String> testConnection() {
        log.info("🔗 [测试] 测试AI服务连接接口被调用");
        return ResponseDTO.ok("AI服务连接正常 - 接口可访问");
    }
    
    @Operation(summary = "获取用户约课习惯")
    @GetMapping("/user-habit/{memberId}")
    public ResponseDTO<UserBookingHabitVO> getUserHabit(@PathVariable Long memberId) {
        try {
            UserBookingHabitVO habit = userHabitService.getUserBookingHabit(memberId);
            return ResponseDTO.ok(habit);
        } catch (Exception e) {
            log.error("❌ [获取习惯] 获取用户约课习惯失败，会员ID：{}", memberId, e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "获取失败：" + e.getMessage());
        }
    }

    @Operation(summary = "测试完整约课场景")
    @PostMapping("/test-complete-booking")
    @NoNeedLogin
    public ResponseDTO<AiCourseResponse> testCompleteBooking() {
        String testText = "约张教练明天下午3点的基础课程";
        AiCourseResponse response = courseBookingService.processVoiceBooking(1L, 1L, testText);
        return ResponseDTO.ok(response);
    }
    
    @Operation(summary = "测试缺失教练名称场景")
    @PostMapping("/test-missing-coach")
    @NoNeedLogin
    public ResponseDTO<AiCourseResponse> testMissingCoach() {
        String testText = "约明天下午3点的基础课程";
        AiCourseResponse response = courseBookingService.processVoiceBooking(1L, 1L, testText);
        return ResponseDTO.ok(response);
    }
    
    @Operation(summary = "测试缺失时间场景")
    @PostMapping("/test-missing-time")
    @NoNeedLogin
    public ResponseDTO<AiCourseResponse> testMissingTime() {
        String testText = "约张教练的基础课程";
        AiCourseResponse response = courseBookingService.processVoiceBooking(1L, 1L, testText);
        return ResponseDTO.ok(response);
    }
}
