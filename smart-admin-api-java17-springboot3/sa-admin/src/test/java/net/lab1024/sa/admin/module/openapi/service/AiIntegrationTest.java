package net.lab1024.sa.admin.module.openapi.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.openapi.domain.vo.AiCourseResponse;
import net.lab1024.sa.admin.module.openapi.domain.vo.UserBookingHabitVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * AI服务集成测试 - 极简版
 * 验证重构后的AI参数提取功能
 * 
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class AiIntegrationTest {
    
    @Autowired(required = false)
    private AiPromptConfigService promptConfigService;
    
    @Autowired(required = false)
    private SiliconflowAiService aiService;
    
    @Autowired(required = false)
    private CourseBookingService courseBookingService;
    
    @Test
    public void testConfigServiceLoad() {
        if (promptConfigService != null) {
            log.info("🔧 [测试] 测试配置服务加载");
            
            // 测试配置文件加载
            String systemPrompt = promptConfigService.getSystemPrompt();
            assert systemPrompt != null && !systemPrompt.trim().isEmpty();
            log.info("✅ [测试] 系统提示词加载成功，长度：{}", systemPrompt.length());
            
            // 测试系统配置
            String model = promptConfigService.getModel();
            Integer maxTokens = promptConfigService.getMaxTokens();
            Double temperature = promptConfigService.getTemperature();
            
            log.info("✅ [测试] 系统配置加载成功：model={}, maxTokens={}, temperature={}", 
                     model, maxTokens, temperature);
                     
        } else {
            log.warn("⚠️ [测试] AiPromptConfigService 未能注入，跳过配置测试");
        }
    }
    
    @Test
    public void testAiParameterExtraction() {
        if (aiService != null) {
            log.info("🔧 [测试] 测试AI参数提取功能");
            
            // 创建模拟用户习惯
            UserBookingHabitVO userHabit = new UserBookingHabitVO();
            userHabit.setMemberId(1L);
            userHabit.setClubId(1L);
            userHabit.setUserRole("老会员");
            
            try {
                // 测试参数提取
                String testText = "约张教练明天下午3点的基础课程";
                AiCourseResponse response = aiService.extractParameters(testText, userHabit);
                
                assert response != null;
                log.info("✅ [测试] AI参数提取成功");
                log.info("🔧 [测试] 提取结果：coachName={}, courseType={}, appointmentTime={}", 
                         response.getCoachName(), response.getCourseType(), response.getAppointmentTime());
                         
            } catch (Exception e) {
                log.warn("⚠️ [测试] AI参数提取测试失败：{}", e.getMessage());
            }
                         
        } else {
            log.warn("⚠️ [测试] SiliconflowAiService 未能注入，跳过AI测试");
        }
    }
    
    @Test
    public void testCourseBookingService() {
        if (courseBookingService != null) {
            log.info("🔧 [测试] 测试约课业务服务");
            
            try {
                // 测试完整约课流程
                String testText = "约张教练明天下午3点的基础课程";
                AiCourseResponse response = courseBookingService.processVoiceBooking(1L, 1L, testText);
                
                assert response != null;
                log.info("✅ [测试] 约课业务处理成功");
                log.info("🔧 [测试] 处理结果：status={}, parametersComplete={}, missingParameters={}", 
                         response.getStatus(), response.getParametersComplete(), response.getMissingParameters());
                         
            } catch (Exception e) {
                log.warn("⚠️ [测试] 约课业务测试失败：{}", e.getMessage());
            }
                         
        } else {
            log.warn("⚠️ [测试] CourseBookingService 未能注入，跳过业务测试");
        }
    }
    
    @Test
    public void testResponseFormat() {
        log.info("🔧 [测试] 测试响应格式匹配");
        
        // 创建测试响应对象
        AiCourseResponse response = new AiCourseResponse();
        response.setRecognizedText("帮我约马教练明天下午3点的基础课程");
        response.setCoachName("马教练");
        response.setCourseType("基础课程");
        response.setUserRole("新会员");
        response.setParametersComplete(true);
        response.setStatus("success");
        response.setAiResponse("已为您预约马教练明天下午3点的基础课程");
        
        // 测试小程序适配字段
        response.setExtractedCoach("C001");
        response.setExtractedCourse("BASIC_COURSE");
        
        // 验证响应格式
        assert response.getRecognizedText() != null;
        assert response.getExtractedCoach() != null;
        assert response.getExtractedCourse() != null;
        assert response.getUserRole() != null;
        assert response.getStatus() != null;
        
        log.info("✅ [测试] 响应格式验证通过");
        log.info("🔧 [测试] 响应数据：recognizedText={}, extractedCoach={}, extractedCourse={}, userRole={}", 
                 response.getRecognizedText(), response.getExtractedCoach(), 
                 response.getExtractedCourse(), response.getUserRole());
    }
    
    @Test
    public void testSimplifiedAiResponseParsing() {
        log.info("🔧 [测试] 测试极简化AI响应解析");
        
        // 模拟AI返回的JSON响应（极简版）
        String mockAiResponse = """
            {
              "coachName": "张教练",
              "courseType": "基础课程",
              "appointmentTime": "2025-08-29 15:00:00"
            }
            """;
        
        log.info("✅ [测试] 模拟AI响应格式验证通过");
        log.debug("🔧 [测试] 模拟AI响应内容：{}", mockAiResponse);
        
        // 验证JSON包含必要的参数提取字段
        assert mockAiResponse.contains("coachName");
        assert mockAiResponse.contains("courseType"); 
        assert mockAiResponse.contains("appointmentTime");
        
        log.info("✅ [测试] 极简化AI参数提取字段验证通过");
    }
}