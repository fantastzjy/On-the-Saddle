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
     * @param productType 商品类型 1-课程 2-课时包 3-活动 4-体验课 5-理论课
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
                case 4: // 体验课
                    formConfig = getExperienceFormConfig();
                    break;
                case 5: // 理论课
                    formConfig = getTheoryCourseFormConfig();
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
     * 根据商品类型和子类型获取详细表单配置
     * 
     * @param productType 商品类型 1-课程 2-课时包 3-活动 4-体验课
     * @param classType 课程分类 1-单人课 2-多人课 (仅课程类型有效)
     * @return 详细表单配置信息
     */
    public ResponseDTO<Map<String, Object>> getDetailedFormConfig(Integer productType, Integer classType) {
        try {
            Map<String, Object> formConfig = new HashMap<>();
            
            if (productType == 1 && classType != null) {
                // 课程类型，需要根据classType进一步细分
                formConfig = getCourseDetailedFormConfig(classType);
            } else {
                // 其他类型或未指定classType，返回基础配置
                return getFormConfig(productType);
            }
            
            log.info("获取商品类型 {} 课程分类 {} 的详细表单配置成功", productType, classType);
            return ResponseDTO.ok(formConfig);
            
        } catch (Exception e) {
            log.error("获取详细表单配置失败", e);
            return ResponseDTO.userErrorParam("获取详细表单配置失败");
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
                case 4: // 体验课
                    return validateExperienceData(formData);
                default:
                    return ResponseDTO.userErrorParam("不支持的商品类型");
            }
        } catch (Exception e) {
            log.error("验证表单数据失败", e);
            return ResponseDTO.userErrorParam("验证表单数据失败");
        }
    }

    /**
     * 获取课程表单配置（基础版，包含课程分类选择）
     */
    private Map<String, Object> getCourseFormConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("title", "课程配置");
        config.put("type", "course");
        
        List<Map<String, Object>> fields = new ArrayList<>();
        
        // 课程分类（必选项，用于切换详细配置）
        fields.add(createSelectField("classType", "课程分类", true, 
            List.of(
                Map.of("value", 1, "label", "单人课"),
                Map.of("value", 2, "label", "多人课")
            )));
        
        config.put("fields", fields);
        config.put("rules", new HashMap<>());
        config.put("needsDetailedConfig", true); // 标记需要根据classType获取详细配置
        
        return config;
    }

    /**
     * 根据课程分类获取详细表单配置
     * 
     * @param classType 课程分类 1-单人课 2-多人课
     * @return 详细表单配置
     */
    private Map<String, Object> getCourseDetailedFormConfig(Integer classType) {
        Map<String, Object> config = new HashMap<>();
        config.put("title", classType == 1 ? "单人课配置" : "多人课配置");
        config.put("type", "course");
        config.put("classType", classType);
        
        List<Map<String, Object>> fields = new ArrayList<>();
        
        // 课程分类（已选定，显示但不可更改）
        fields.add(createSelectField("classType", "课程分类", true, 
            List.of(
                Map.of("value", 1, "label", "单人课"),
                Map.of("value", 2, "label", "多人课")
            )));
        
        // 时长配置
        fields.add(createNumberField("durationPeriods", "鞍时数", true, 0.5, 5.0, 1.0));
        
        if (classType == 1) {
            // 单人课配置 - 严格按照数据库字段 m_product_course
            fields.add(createNumberField("maxStudents", "最大人数", true, 1, 1, 1)); // 固定为1人
            fields.add(createNumberField("coachFee", "教练费", true, 0, 9999, 200));
            fields.add(createNumberField("horseFee", "马匹费用", true, 0, 9999, 100));
            // 注意：base_price是计算字段，由数据库自动计算 = coach_fee + horse_fee，前端不需要输入
                
        } else {
            // 多人课配置 - 严格按照数据库字段 m_product_course
            fields.add(createNumberField("maxStudents", "最大人数", true, 2, 5, 2)); // 人数范围2-5，初始值为2
            fields.add(createNumberField("coachFee", "教练费", true, 0, 9999, 300));
            fields.add(createNumberField("horseFee", "马匹费用", true, 0, 9999, 80));
            
            // 多人课价格配置 - 使用专门的价格配置组件
            fields.add(createMultiPriceConfigField("multiPriceConfig", "多人课价格配置", false));
        }
        
        config.put("fields", fields);
        config.put("rules", getCourseDetailedValidationRules(classType));
        
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
        
        // 活动规则
        fields.add(createTextareaField("activityRule", "活动规则", true, "请明确活动政策"));
        
        // 详情图片
        fields.add(createActivityDetailImagesField("detailImages", "详情图片", false, "最多上传9张图片"));
        
        config.put("fields", fields);
        config.put("rules", getActivityValidationRules());
        
        return config;
    }

    /**
     * 获取体验课表单配置
     */
    private Map<String, Object> getExperienceFormConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("title", "体验课配置");
        config.put("type", "experience");
        
        List<Map<String, Object>> fields = new ArrayList<>();
        
        // 价格配置（基础单价）
        fields.add(createNumberField("price", "基础单价", true, 0, 9999, 200));
        
        // 时长配置（只显示分钟）
        fields.add(createNumberField("durationMinutes", "课程时长（分钟）", true, 30, 300, 60));
        
        // 人数配置
        fields.add(createNumberField("maxStudents", "最大人数", true, 1, 10, 1));
        
        config.put("fields", fields);
        config.put("rules", getExperienceValidationRules());
        
        return config;
    }

    /**
     * 获取理论课表单配置
     */
    private Map<String, Object> getTheoryCourseFormConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("title", "理论课配置");
        config.put("type", "theoryCourse");
        
        List<Map<String, Object>> fields = new ArrayList<>();
        
        // 鞍时配置
        fields.add(createNumberField("theoryCourse_durationPeriods", "鞍时", true, 0.5, 10.0, 1.0));
        
        // 基础单价配置
        fields.add(createNumberField("theoryCourse_basePrice", "基础单价", true, 1, 9999, 100));
        
        // 人数配置
        fields.add(createNumberField("theoryCourse_maxStudents", "最大人数", true, 1, 100, 10));
        
        config.put("fields", fields);
        config.put("rules", getTheoryCourseValidationRules());
        
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
     * 创建多人课价格配置字段
     */
    private Map<String, Object> createMultiPriceConfigField(String key, String label, boolean required) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "multi-price-config");
        field.put("required", required);
        field.put("help", "根据参与人数设置不同的价格，人数越多通常价格越优惠");
        field.put("placeholder", "系统将自动生成价格配置表单");
        return field;
    }

    /**
     * 创建开关字段
     */
    private Map<String, Object> createSwitchField(String key, String label, boolean required, boolean defaultValue) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "switch");
        field.put("required", required);
        field.put("defaultValue", defaultValue);
        return field;
    }

    /**
     * 创建多选框字段
     */
    private Map<String, Object> createCheckboxField(String key, String label, boolean required, 
                                                   List<Map<String, Object>> options) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "checkbox");
        field.put("required", required);
        field.put("options", options);
        return field;
    }

    /**
     * 创建活动详情图片上传字段
     */
    private Map<String, Object> createActivityDetailImagesField(String key, String label, boolean required, String tip) {
        Map<String, Object> field = new HashMap<>();
        field.put("key", key);
        field.put("label", label);
        field.put("type", "activity-detail-images");
        field.put("required", required);
        field.put("tip", tip);
        field.put("maxCount", 9);
        field.put("maxSize", 2);
        field.put("acceptTypes", ".jpg,.jpeg,.png,.gif");
        field.put("showTips", true);
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
     * 获取课程详细验证规则
     * 严格按照数据库表 m_product_course 字段
     * 
     * @param classType 课程分类 1-单人课 2-多人课
     * @return 验证规则
     */
    private Map<String, Object> getCourseDetailedValidationRules(Integer classType) {
        Map<String, Object> rules = new HashMap<>();
        
        // 数据库字段验证规则
        rules.put("classType", List.of("required", "number"));
        rules.put("durationPeriods", List.of("required", "number", "min:0.5", "max:5"));
        rules.put("maxStudents", List.of("required", "number", "min:1", "max:10"));
        rules.put("coachFee", List.of("required", "number", "min:0"));
        rules.put("horseFee", List.of("required", "number", "min:0"));
        
        if (classType == 2) {
            // 多人课价格配置（可选）
            rules.put("multiPriceConfig", List.of("string"));
        }
        
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
        rules.put("activityRule", List.of("required", "string"));
        return rules;
    }

    /**
     * 获取体验课验证规则
     */
    private Map<String, Object> getExperienceValidationRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("price", List.of("required", "number", "min:0"));
        rules.put("durationMinutes", List.of("required", "number", "min:30", "max:300"));
        rules.put("maxStudents", List.of("required", "number", "min:1", "max:10"));
        return rules;
    }

    /**
     * 获取理论课验证规则
     */
    private Map<String, Object> getTheoryCourseValidationRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("theoryCourse_durationPeriods", List.of("required", "number", "min:0.5", "max:10"));
        rules.put("theoryCourse_basePrice", List.of("required", "number", "min:1", "max:9999"));
        rules.put("theoryCourse_maxStudents", List.of("required", "number", "min:1", "max:100"));
        return rules;
    }

    /**
     * 验证课程数据 - 严格按照数据库表 m_product_course 字段
     */
    private ResponseDTO<String> validateCourseData(Map<String, Object> formData) {
        // 验证必填字段 - 对应数据库字段
        String[] requiredFields = {"classType", "durationPeriods", "maxStudents", "coachFee", "horseFee"};
        for (String field : requiredFields) {
            if (!formData.containsKey(field) || formData.get(field) == null) {
                return ResponseDTO.userErrorParam(field + " 是必填字段");
            }
        }
        
        // 验证鞍时数范围
        Double durationPeriods = (Double) formData.get("durationPeriods");
        if (durationPeriods < 0.5 || durationPeriods > 5.0) {
            return ResponseDTO.userErrorParam("鞍时数必须在0.5-5.0之间");
        }
        
        // 多人课价格配置为可选项，如果有则验证格式
        Integer classType = (Integer) formData.get("classType");
        if (classType == 2 && formData.containsKey("multiPriceConfig") && formData.get("multiPriceConfig") != null) {
            String multiPriceConfig = (String) formData.get("multiPriceConfig");
            if (multiPriceConfig.trim().isEmpty()) {
                return ResponseDTO.userErrorParam("多人课价格配置不能为空字符串");
            }
            // 可以添加JSON格式验证
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
                                  "activityLocation", "price", "maxParticipants", "activityRule"};
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

    /**
     * 验证体验课数据
     */
    private ResponseDTO<String> validateExperienceData(Map<String, Object> formData) {
        // 验证必填字段
        String[] requiredFields = {"price", "durationMinutes", "maxStudents"};
        for (String field : requiredFields) {
            if (!formData.containsKey(field) || formData.get(field) == null) {
                return ResponseDTO.userErrorParam(field + " 是必填字段");
            }
        }
        
        // 验证时长
        Integer durationMinutes = (Integer) formData.get("durationMinutes");
        if (durationMinutes < 30 || durationMinutes > 300) {
            return ResponseDTO.userErrorParam("时长必须在30-300分钟之间");
        }
        
        return ResponseDTO.ok("验证通过");
    }
}