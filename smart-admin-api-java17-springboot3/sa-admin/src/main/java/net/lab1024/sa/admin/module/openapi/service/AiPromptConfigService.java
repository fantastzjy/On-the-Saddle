package net.lab1024.sa.admin.module.openapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI提示词配置服务
 * 负责读取和管理ai-prompt-config-v2.yml配置文件
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Service
@Slf4j
public class AiPromptConfigService {
    
    private Map<String, Object> configData;
    private final Map<String, String> configCache = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void loadConfig() {
        try {
            log.info("🔧 [配置加载] 开始加载AI提示词配置文件");
            ClassPathResource resource = new ClassPathResource("ai-prompt-config-v2.yml");
            
            if (!resource.exists()) {
                log.error("❌ [配置加载] 配置文件不存在：ai-prompt-config-v2.yml");
                throw new RuntimeException("AI配置文件不存在");
            }
            
            try (InputStream inputStream = resource.getInputStream()) {
                Yaml yaml = new Yaml();
                configData = yaml.load(inputStream);
                log.info("✅ [配置加载] AI配置文件加载成功");
                log.debug("🔧 [配置加载] 配置内容预览：{}", configData.keySet());
            }
            
        } catch (Exception e) {
            log.error("❌ [配置加载] 加载AI配置文件失败", e);
            throw new RuntimeException("加载AI配置文件失败", e);
        }
    }
    
    /**
     * 获取系统配置
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSystemConfig() {
        if (configData == null) {
            log.warn("⚠️ [配置获取] 配置数据为空，重新加载");
            loadConfig();
        }
        
        Map<String, Object> aiPrompt = (Map<String, Object>) configData.get("ai-prompt");
        if (aiPrompt == null) {
            log.error("❌ [配置获取] 未找到ai-prompt配置节");
            return new ConcurrentHashMap<>();
        }
        
        return (Map<String, Object>) aiPrompt.get("system");
    }
    
    /**
     * 获取系统提示词
     */
    public String getSystemPrompt() {
        String cacheKey = "system-prompt";
        String cached = configCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> aiPrompt = (Map<String, Object>) configData.get("ai-prompt");
            String template = (String) aiPrompt.get("system-prompt");
            
            if (template != null) {
                configCache.put(cacheKey, template);
                log.debug("🔧 [配置获取] 获取系统提示词成功，长度：{}", template.length());
                return template;
            }
            
        } catch (Exception e) {
            log.error("❌ [配置获取] 获取系统提示词失败", e);
        }
        
        return getDefaultSystemPrompt();
    }
    
    /**
     * 获取用户提示词模板
     */
    public String getUserPromptTemplate() {
        String cacheKey = "user-prompt-template";
        String cached = configCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> aiPrompt = (Map<String, Object>) configData.get("ai-prompt");
            String template = (String) aiPrompt.get("user-prompt-template");
            
            if (template != null) {
                configCache.put(cacheKey, template);
                log.debug("🔧 [配置获取] 获取用户提示词模板成功，长度：{}", template.length());
                return template;
            }
            
        } catch (Exception e) {
            log.error("❌ [配置获取] 获取用户提示词模板失败", e);
        }
        
        return getDefaultUserPromptTemplate();
    }
    
    /**
     * 获取角色特定策略
     */
    @SuppressWarnings("unchecked")
    public String getRoleStrategy(String userRole) {
        try {
            Map<String, Object> aiPrompt = (Map<String, Object>) configData.get("ai-prompt");
            Map<String, Object> roleStrategies = (Map<String, Object>) aiPrompt.get("role-strategies");
            
            if (roleStrategies != null && roleStrategies.containsKey(userRole)) {
                Map<String, Object> roleConfig = (Map<String, Object>) roleStrategies.get(userRole);
                String strategy = (String) roleConfig.get("strategy");
                log.debug("🔧 [配置获取] 获取角色策略成功：{}", userRole);
                return strategy;
            }
            
        } catch (Exception e) {
            log.error("❌ [配置获取] 获取角色策略失败：{}", userRole, e);
        }
        
        log.warn("⚠️ [配置获取] 未找到角色策略，使用默认策略：{}", userRole);
        return "";
    }
    
    /**
     * 获取响应模板格式
     */
    @SuppressWarnings("unchecked")
    public String getResponseTemplate() {
        String cacheKey = "response-template";
        String cached = configCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        try {
            Map<String, Object> aiPrompt = (Map<String, Object>) configData.get("ai-prompt");
            Map<String, Object> responseTemplate = (Map<String, Object>) aiPrompt.get("response-template");
            
            if (responseTemplate != null) {
                String format = (String) responseTemplate.get("format");
                if (format != null) {
                    configCache.put(cacheKey, format);
                    log.debug("🔧 [配置获取] 获取响应模板成功");
                    return format;
                }
            }
            
        } catch (Exception e) {
            log.error("❌ [配置获取] 获取响应模板失败", e);
        }
        
        return getDefaultResponseTemplate();
    }
    
    /**
     * 获取完整性规则
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCompletenessRules() {
        try {
            Map<String, Object> aiPrompt = (Map<String, Object>) configData.get("ai-prompt");
            return (Map<String, Object>) aiPrompt.get("completeness-rules");
        } catch (Exception e) {
            log.error("❌ [配置获取] 获取完整性规则失败", e);
            return new ConcurrentHashMap<>();
        }
    }
    
    /**
     * 获取系统模型配置
     */
    public String getModel() {
        Map<String, Object> systemConfig = getSystemConfig();
        return (String) systemConfig.getOrDefault("model", "Qwen/QwQ-32B");
    }
    
    /**
     * 获取最大令牌数
     */
    public Integer getMaxTokens() {
        Map<String, Object> systemConfig = getSystemConfig();
        return (Integer) systemConfig.getOrDefault("max-tokens", 200);
    }
    
    /**
     * 获取温度参数
     */
    public Double getTemperature() {
        Map<String, Object> systemConfig = getSystemConfig();
        Object temp = systemConfig.get("temperature");
        if (temp instanceof Number) {
            return ((Number) temp).doubleValue();
        }
        return 0.03;
    }
    
    /**
     * 获取超时时间
     */
    public Integer getTimeoutSeconds() {
        Map<String, Object> systemConfig = getSystemConfig();
        return (Integer) systemConfig.getOrDefault("timeout-seconds", 10);
    }
    
    /**
     * 默认系统提示词 - 极简版
     */
    private String getDefaultSystemPrompt() {
        return """
            你是马术俱乐部的AI约课助手。你的唯一任务是从用户语音中提取约课参数。
            
            **重要原则：**
            1. 只提取用户明确说出的信息，禁止猜测或编造
            2. 严格按照JSON格式返回
            3. 如果无法确定参数，返回null
            4. 不做任何推荐或匹配
            
            **提取参数：**
            - coachName: 教练姓名（如：张三、李四、王五）
            - courseType: 课程类型（如：基础课程、进阶课程、专业课程）
            - appointmentTime: 约课时间（格式：yyyy-MM-dd HH:mm:ss）
            
            **时间解析规则：**
            - 明天 = 当前日期+1天
            - 后天 = 当前日期+2天  
            - 上午 = 10:00，下午 = 14:00，晚上 = 19:00
            - 具体时间按用户说的时间（如：3点 = 15:00）
            
            **严格要求：**
            - 必须返回有效的JSON格式
            - 只包含这3个字段
            - 不添加任何其他字段
            - 不返回解释文字
            """;
    }
    
    /**
     * 默认用户提示词模板
     */
    private String getDefaultUserPromptTemplate() {
        return """
            **候选数据：** {候选数据}
            **用户语音：** {语音文本}
            **今日：** {今日日期}
            
            请严格从候选数据中匹配，返回JSON格式结果。
            """;
    }
    
    /**
     * 默认响应模板
     */
    private String getDefaultResponseTemplate() {
        return """
            ```json
            {
              "coachName": "从候选教练中匹配的教练姓名",
              "extractedCoach": "对应的教练编号",
              "courseType": "从候选课程中匹配的课程名称", 
              "extractedCourse": "对应的课程编号",
              "appointmentTime": "解析的具体时间，格式：yyyy-MM-dd HH:mm:ss",
              "extractedTimes": [
                {
                  "date": "MM-dd格式",
                  "range": "HH:mm-HH:mm格式", 
                  "day": "星期几",
                  "id": "时间段ID"
                }
              ],
              "userRole": "用户角色",
              "parametersComplete": true/false,
              "missingParameters": ["缺失参数列表"],
              "aiResponse": "友好的中文回复",
              "targetPage": "跳转页面路径",
              "pageParams": "页面参数",
              "navigationInstruction": "导航指令",
              "status": "success"
            }
            ```
            """;
    }
}