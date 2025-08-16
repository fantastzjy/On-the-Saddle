package net.lab1024.sa.admin.module.business.product.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态表单配置服务
 * 根据商品类型自动生成不同的表单配置
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class DynamicFormConfigService {

    /**
     * 根据商品类型获取表单配置
     * 
     * @param productType 商品类型 1-课程 2-课时包 3-活动
     * @return 表单配置信息
     */
    public ResponseDTO<Map<String, Object>> getFormConfig(Integer productType) {
        try {
            Map<String, Object> formConfig = new HashMap<>();
            
            switch (productType) {
                case 1: // 课程
                    formConfig = getCourseFormConfig();
                    break;
                case 2: // 课时包
                    formConfig = getPackageFormConfig();
                    break;
                case 3: // 活动
                    formConfig = getActivityFormConfig();
                    break;
                default:
                    return ResponseDTO.userErrorParam("不支持的商品类型");
            }
            
            log.info("获取商品类型 {} 的表单配置成功", productType);
            return ResponseDTO.ok(formConfig);
            
        } catch (Exception e) {
            log.error("获取表单配置失败", e);
            return ResponseDTO.userErrorParam("获取表单配置失败");
        }
    }

    /**
     * 验证表单数据
     * 
     * @param productType 商品类型
     * @param formData 表单数据
     * @return 验证结果
     */
    public ResponseDTO<String> validateFormData(Integer productType, Map<String, Object> formData) {
        try {
            switch (productType) {
                case 1: // 课程
                    return validateCourseData(formData);
                case 2: // 课时包
                    return validatePackageData(formData);
                case 3: // 活动
                    return validateActivityData(formData);
                default:
                    return ResponseDTO.userErrorParam("不支持的商品类型");
            }
        } catch (Exception e) {
            log.error("验证表单数据失败", e);
            return ResponseDTO.userErrorParam("验证表单数据失败");
        }
    }

    /**
     * 获取课程表单配置
     */
    private Map<String, Object> getCourseFormConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("title", "课程配置");
        config.put("type", "course");
        
        List<Map<String, Object>> fields = new ArrayList<>();
        
        // 课程分类
        fields.add(createSelectField("classType", "课程分类", true, 
            List.of(
                Map.of("value", 1, "label", "单人课"),
                Map.of("value", 2, "label", "多人课")
            )));
        
        // 时长配置
        fields.add(createNumberField("durationMinutes", "时长（分钟）", true, 30, 300, 60));
        fields.add(createNumberField("durationPeriods", "时长（鞍时）", true, 0.5, 5.0, 1.0));
        
        // 人数配置
        fields.add(createNumberField("maxStudents", "最大人数", true, 1, 10, 1));
        
        // 费用配置
        fields.add(createNumberField("coachFee", "教练费", true, 0, 9999, 200));
        fields.add(createNumberField("horseFee", "马匹费用", true, 0, 9999, 100));
        
        // 多人课价格配置（条件显示）
        fields.add(createTextareaField("multiPriceConfig", "多人课价格配置", false, 
            "当课程分类为多人课时，配置不同人数的价格"));
        
        config.put("fields", fields);
        config.put("rules", getCourseValidationRules());
        
        return config;
    }

    /**
     * 获取课时包表单配置
     */
    private Map<String, Object> getPackageFormConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("title", "课时包配置");
        config.put("type", "package");
        
        List<Map<String, Object>> fields = new ArrayList<>();
        
        // 课包详情
        fields.add(createTextareaField("details", "课包详情", true, "请详细描述课时包内容"));
        
        // 价格配置
        fields.add(createNumberField("price", "课包单价", true, 0, 99999, 1000));
        
        // 课时配置
        fields.add(createNumberField("totalSessions", "总课时数", true, 1, 500, 10));
        
        // 有效期配置
        fields.add(createNumberField("validityDays", "有效期（天）", true, 1, 3650, 365));
        
        // 库存配置
        fields.add(createNumberField("stockQuantity", "库存数量", true, 0, 9999, 100));
        
        config.put("fields", fields);
        config.put("rules", getPackageValidationRules());
        
        return config;
    }

    /**
     * 获取活动表单配置
     */
    private Map<String, Object> getActivityFormConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("title", "活动配置");
        config.put("type", "activity");
        
        List<Map<String, Object>> fields = new ArrayList<>();
        
        // 活动名称
        fields.add(createTextField("activityName", "活动名称", true, "最多5个字"));
        
        // 活动详情
        fields.add(createTextareaField("activityDetails", "活动详情", true, "请详细介绍活动内容"));
        
        // 时间配置
        fields.add(createDatetimeField("startTime", "活动开始时间", true));
        fields.add(createDatetimeField("endTime", "活动结束时间", true));
        
        // 地点配置
        fields.add(createTextField("activityLocation", "活动地点", true, "请输入活动举办地点"));
        
        // 价格配置
        fields.add(createNumberField("price", "活动单价", true, 0, 99999, 200));
        
        // 人数配置
        fields.add(createNumberField("maxParticipants", "最大参与人数", true, 1, 1000, 20));
        
        // 退款规则
        fields.add(createTextareaField("refundRule", "退款规则", true, "请明确退款政策"));
        
        // 详情图片
        fields.add(createUploadField("detailImages", "详情图片", false, "最多上传9张图片"));
        
        config.put("fields", fields);
        config.put("rules", getActivityValidationRules());
        
        return config;
    }

    /**
     * 创建文本输入字段
     */
    private Map<String, Object> createTextField(String key, String label, boolean required, String placeholder) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "input");
        field.put("required", required);
        field.put("placeholder", placeholder);
        return field;
    }

    /**
     * 创建数字输入字段
     */
    private Map<String, Object> createNumberField(String key, String label, boolean required, 
                                                 Object min, Object max, Object defaultValue) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "number");
        field.put("required", required);
        field.put("min", min);
        field.put("max", max);
        field.put("defaultValue", defaultValue);
        return field;
    }

    /**
     * 创建选择字段
     */
    private Map<String, Object> createSelectField(String key, String label, boolean required, 
                                                 List<Map<String, Object>> options) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "select");
        field.put("required", required);
        field.put("options", options);
        return field;
    }

    /**
     * 创建文本域字段
     */
    private Map<String, Object> createTextareaField(String key, String label, boolean required, String placeholder) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "textarea");
        field.put("required", required);
        field.put("placeholder", placeholder);
        return field;
    }

    /**
     * 创建日期时间字段
     */
    private Map<String, Object> createDatetimeField(String key, String label, boolean required) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "datetime");
        field.put("required", required);
        return field;
    }

    /**
     * 创建文件上传字段
     */
    private Map<String, Object> createUploadField(String key, String label, boolean required, String tip) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "upload");
        field.put("required", required);
        field.put("tip", tip);
        return field;
    }

    /**
     * 获取课程验证规则
     */
    private Map<String, Object> getCourseValidationRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("durationMinutes", List.of("required", "number", "min:30", "max:300"));
        rules.put("durationPeriods", List.of("required", "number", "min:0.5", "max:5"));
        rules.put("maxStudents", List.of("required", "number", "min:1", "max:10"));
        rules.put("coachFee", List.of("required", "number", "min:0"));
        rules.put("horseFee", List.of("required", "number", "min:0"));
        return rules;
    }

    /**
     * 获取课时包验证规则
     */
    private Map<String, Object> getPackageValidationRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("details", List.of("required", "string", "max:1000"));
        rules.put("price", List.of("required", "number", "min:0"));
        rules.put("totalSessions", List.of("required", "number", "min:1", "max:500"));
        rules.put("validityDays", List.of("required", "number", "min:1", "max:3650"));
        rules.put("stockQuantity", List.of("required", "number", "min:0"));
        return rules;
    }

    /**
     * 获取活动验证规则
     */
    private Map<String, Object> getActivityValidationRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("activityName", List.of("required", "string", "max:5"));
        rules.put("activityDetails", List.of("required", "string"));
        rules.put("startTime", List.of("required", "date"));
        rules.put("endTime", List.of("required", "date", "after:startTime"));
        rules.put("activityLocation", List.of("required", "string", "max:200"));
        rules.put("price", List.of("required", "number", "min:0"));
        rules.put("maxParticipants", List.of("required", "number", "min:1", "max:1000"));
        rules.put("refundRule", List.of("required", "string"));
        return rules;
    }

    /**
     * 验证课程数据
     */
    private ResponseDTO<String> validateCourseData(Map<String, Object> formData) {
        // 验证必填字段
        String[] requiredFields = {"classType", "durationMinutes", "durationPeriods", "maxStudents", "coachFee", "horseFee"};
        for (String field : requiredFields) {
            if (!formData.containsKey(field) || formData.get(field) == null) {
                return ResponseDTO.userErrorParam(field + " 是必填字段");
            }
        }
        
        // 验证数值范围
        Integer durationMinutes = (Integer) formData.get("durationMinutes");
        if (durationMinutes < 30 || durationMinutes > 300) {
            return ResponseDTO.userErrorParam("时长必须在30-300分钟之间");
        }
        
        return ResponseDTO.ok("验证通过");
    }

    /**
     * 验证课时包数据
     */
    private ResponseDTO<String> validatePackageData(Map<String, Object> formData) {
        // 验证必填字段
        String[] requiredFields = {"details", "price", "totalSessions", "validityDays", "stockQuantity"};
        for (String field : requiredFields) {
            if (!formData.containsKey(field) || formData.get(field) == null) {
                return ResponseDTO.userErrorParam(field + " 是必填字段");
            }
        }
        
        // 验证总课时数
        Integer totalSessions = (Integer) formData.get("totalSessions");
        if (totalSessions < 1 || totalSessions > 500) {
            return ResponseDTO.userErrorParam("总课时数必须在1-500之间");
        }
        
        return ResponseDTO.ok("验证通过");
    }

    /**
     * 验证活动数据
     */
    private ResponseDTO<String> validateActivityData(Map<String, Object> formData) {
        // 验证必填字段
        String[] requiredFields = {"activityName", "activityDetails", "startTime", "endTime", 
                                  "activityLocation", "price", "maxParticipants", "refundRule"};
        for (String field : requiredFields) {
            if (!formData.containsKey(field) || formData.get(field) == null) {
                return ResponseDTO.userErrorParam(field + " 是必填字段");
            }
        }
        
        // 验证活动名称长度
        String activityName = (String) formData.get("activityName");
        if (activityName.length() > 5) {
            return ResponseDTO.userErrorParam("活动名称最多5个字");
        }
        
        return ResponseDTO.ok("验证通过");
    }
}