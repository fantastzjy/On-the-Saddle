package net.lab1024.sa.admin.module.openapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.openapi.domain.vo.AiCourseResponse;
import net.lab1024.sa.admin.module.openapi.domain.vo.UserBookingHabitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * 硅基流动AI服务 - 极简版
 * 只负责参数提取，不做任何匹配推荐
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Service
@Slf4j
public class SiliconflowAiService {
    
    @Value("${siliconflow.api.url}")
    private String apiUrl;
    
    @Value("${siliconflow.api.token}")
    private String token;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Autowired
    private AiPromptConfigService promptConfigService;
    
    public SiliconflowAiService() {
        this.webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
            .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 从用户语音中提取参数（不做任何推荐和匹配）
     */
    public AiCourseResponse extractParameters(String speechText, UserBookingHabitVO userHabit) {
        try {
            log.info("🤖 [AI参数提取] 开始提取参数，语音文本：{}", speechText);
            
            // 1. 构建AI请求（注入当前日期信息）
            String systemPrompt = buildSystemPromptWithDateContext(promptConfigService.getSystemPrompt());
            
            SiliconflowRequest request = new SiliconflowRequest();
            request.setModel(promptConfigService.getModel());
            request.setMessages(Arrays.asList(
                new Message("system", systemPrompt),
                new Message("user", speechText)
            ));
            request.setMaxTokens(promptConfigService.getMaxTokens());
            request.setTemperature(promptConfigService.getTemperature());
            request.setTopP(0.95);
            request.setStream(false);
            
            // 2. 调用AI服务
            log.info("🤖 [AI参数提取] 调用AI服务");
            SiliconflowResponse response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SiliconflowResponse.class)
                .timeout(java.time.Duration.ofSeconds(30))
                .block();
            
            // 3. 解析AI返回的参数
            return parseAiParameters(response, speechText, userHabit);
            
        } catch (Exception e) {
            log.error("🤖 [AI参数提取] 提取失败：{}", e.getMessage(), e);
            // 返回空参数，让业务层处理
            return createEmptyResponse(speechText, userHabit.getUserRole());
        }
    }
    
    /**
     * 解析AI返回的参数（仅提取，不验证）
     */
    private AiCourseResponse parseAiParameters(SiliconflowResponse response, String speechText, UserBookingHabitVO userHabit) {
        try {
            String content = response.getChoices().get(0).getMessage().getContent();
            log.info("🤖 [AI参数提取] AI返回内容：{}", content);
            
            // 提取JSON部分
            String jsonContent = extractJsonFromContent(content);
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            
            // 构建响应对象
            AiCourseResponse result = new AiCourseResponse();
            result.setRecognizedText(speechText);
            result.setUserRole(userHabit.getUserRole());
            result.setStatus("success");
            
            // 仅提取参数，不做任何验证
            result.setCoachName(getJsonValue(jsonNode, "coachName"));
            result.setCourseType(getJsonValue(jsonNode, "courseType"));
            
            String timeStr = getJsonValue(jsonNode, "appointmentTime");
            result.setAppointmentTime(parseDateTime(timeStr));
            
            log.info("✅ [AI参数提取] 参数提取完成 - 教练:{}, 课程:{}, 时间:{}", 
                result.getCoachName(), result.getCourseType(), result.getAppointmentTime());
            
            return result;
            
        } catch (Exception e) {
            log.error("❌ [AI参数提取] 解析失败：{}", e.getMessage(), e);
            return createEmptyResponse(speechText, userHabit.getUserRole());
        }
    }
    
    /**
     * 创建空参数响应
     */
    private AiCourseResponse createEmptyResponse(String speechText, String userRole) {
        AiCourseResponse response = new AiCourseResponse();
        response.setRecognizedText(speechText);
        response.setUserRole(userRole);
        response.setStatus("success");
        response.setCoachName(null);
        response.setCourseType(null);
        response.setAppointmentTime(null);
        
        log.info("🤖 [AI参数提取] 返回空参数响应");
        return response;
    }
    
    /**
     * 从AI返回的内容中提取JSON部分
     */
    private String extractJsonFromContent(String content) {
        int startIndex = content.indexOf('{');
        int endIndex = content.lastIndexOf('}');
        
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return content.substring(startIndex, endIndex + 1);
        }
        
        return content;
    }
    
    /**
     * 获取JSON字符串值
     */
    private String getJsonValue(JsonNode jsonNode, String fieldName) {
        JsonNode node = jsonNode.get(fieldName);
        return node != null && !node.isNull() ? node.asText() : null;
    }
    
    /**
     * 解析日期时间
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(dateTimeStr.trim(), formatter);
        } catch (DateTimeParseException e) {
            log.warn("⚠️ [AI参数提取] 时间解析失败：{}", dateTimeStr);
            return null;
        }
    }
    
    /**
     * 构建带日期上下文的系统提示
     */
    private String buildSystemPromptWithDateContext(String originalPrompt) {
        // 获取当前日期信息
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfterTomorrow = today.plusDays(2);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        
        // 构建日期上下文
        String dateContext = String.format("""
            **重要时间信息：**
            - 今天是：%s (%s)
            - 明天是：%s (%s)  
            - 后天是：%s (%s)
            
            **时间解析严格要求：**
            - "明天" 必须解析为：%s 13:00:00 （如果用户说下午1点）
            - "今天" 必须解析为：%s 13:00:00 （如果用户说下午1点）
            - 禁止使用2023等过时年份，必须使用当前年份%d
            - 时间格式严格按照：yyyy-MM-dd HH:mm:ss
            
            """, 
            today.format(displayFormatter), today.format(dateFormatter),
            tomorrow.format(displayFormatter), tomorrow.format(dateFormatter),
            dayAfterTomorrow.format(displayFormatter), dayAfterTomorrow.format(dateFormatter),
            tomorrow.format(dateFormatter),
            today.format(dateFormatter),
            today.getYear()
        );
        
        // 将日期上下文注入到原始提示中
        String enhancedPrompt = dateContext + "\n" + originalPrompt;
        
        log.info("🤖 [AI参数提取] 已注入日期上下文，今天：{}，明天：{}", 
            today.format(dateFormatter), tomorrow.format(dateFormatter));
        
        return enhancedPrompt;
    }
    
    // 内部类：硅基流动请求
    public static class SiliconflowRequest {
        private String model;
        private List<Message> messages;
        private Integer maxTokens;
        private Double temperature;
        private Double topP;
        private Boolean stream;
        
        // getters and setters
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public List<Message> getMessages() { return messages; }
        public void setMessages(List<Message> messages) { this.messages = messages; }
        public Integer getMaxTokens() { return maxTokens; }
        public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        public Double getTopP() { return topP; }
        public void setTopP(Double topP) { this.topP = topP; }
        public Boolean getStream() { return stream; }
        public void setStream(Boolean stream) { this.stream = stream; }
    }
    
    public static class Message {
        private String role;
        private String content;
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
        
        // getters and setters
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
    
    public static class SiliconflowResponse {
        private List<Choice> choices;
        
        // getters and setters
        public List<Choice> getChoices() { return choices; }
        public void setChoices(List<Choice> choices) { this.choices = choices; }
    }
    
    public static class Choice {
        private Message message;
        
        // getters and setters
        public Message getMessage() { return message; }
        public void setMessage(Message message) { this.message = message; }
    }
}