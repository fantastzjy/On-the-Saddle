package net.lab1024.sa.admin.module.openapi.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.openapi.domain.vo.AiCourseResponse;
import net.lab1024.sa.admin.module.openapi.domain.vo.UserBookingHabitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 约课业务服务 - 极简版
 * 专注数据库查询和基础推荐，严格按照业务规则匹配
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Service
@Slf4j
public class CourseBookingService {

    // 课程名称映射表（AI识别 -> 数据库实际）- 扩展版
    private static final Map<String, List<String>> ENHANCED_COURSE_MAPPING = new ConcurrentHashMap<String, List<String>>() {{
        // 课程类型映射
        put("体验", Arrays.asList("体验课程", "体验课", "初学者课程", "试听课程"));
        put("体验课", Arrays.asList("体验课程", "体验课", "初学者课程", "试听课程"));
        put("体验课程", Arrays.asList("体验课程"));

        // 基础课程映射
        put("基础", Arrays.asList("基础课程", "基础课", "入门课程", "初级课程"));
        put("基础课", Arrays.asList("基础课程", "基础课", "入门课程", "初级课程"));
        put("基础课程", Arrays.asList("基础课程"));
        put("入门", Arrays.asList("基础课程", "入门课程", "初级课程"));
        put("初级", Arrays.asList("基础课程", "初级课程", "入门课程"));

        // 进阶课程映射
        put("进阶", Arrays.asList("进阶课程", "中级课程", "提高课程"));
        put("进阶课", Arrays.asList("进阶课程", "中级课程", "提高课程"));
        put("进阶课程", Arrays.asList("进阶课程"));
        put("中级", Arrays.asList("进阶课程", "中级课程", "提高课程"));
        put("提高", Arrays.asList("进阶课程", "提高课程", "中级课程"));

        // 专业课程映射
        put("专业", Arrays.asList("专业课程", "高级课程", "专业训练"));
        put("专业课", Arrays.asList("专业课程", "高级课程", "专业训练"));
        put("专业课程", Arrays.asList("专业课程"));
        put("高级", Arrays.asList("专业课程", "高级课程", "专业训练"));
        put("高级课", Arrays.asList("专业课程", "高级课程", "专业训练"));
        put("高级课程", Arrays.asList("专业课程", "高级课程"));
    }};

    // 营业时间常量
    private static final int BUSINESS_START_HOUR = 9;  // 9:00
    private static final int BUSINESS_END_HOUR = 21;   // 21:00

    @Autowired
    private SiliconflowAiService aiService;

    @Autowired
    private UserBookingHabitService userHabitService;

    @Autowired
    private CoachDao coachDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 处理语音约课请求（极简化）
     */
    public AiCourseResponse processVoiceBooking(Long memberId, Long clubId, String speechText) {
        log.info("📝 [约课处理] 开始处理约课请求：会员ID={}，俱乐部ID={}，语音文本={}", memberId, clubId, speechText);

        // 1. 获取用户约课习惯
        UserBookingHabitVO userHabit = userHabitService.getUserBookingHabit(memberId);
        if (userHabit == null) {
            log.warn("⚠️ [约课处理] 未找到用户约课习惯，会员ID：{}", memberId);
            return createErrorResponse(speechText, "未找到用户约课习惯信息");
        }

        // 🔧 关键修复：使用用户习惯中的俱乐部ID，覆盖前端传递的ID
        Long actualClubId = userHabit.getClubId();
        if (!actualClubId.equals(clubId)) {
            log.warn("⚠️ [约课处理] 俱乐部ID不匹配，前端传递：{}，用户实际：{}，使用实际俱乐部ID", clubId, actualClubId);
        }

        // 更新用户习惯中的俱乐部ID确保一致性
        userHabit.setClubId(actualClubId);

        // 2. AI提取参数（不做验证）
        AiCourseResponse aiResponse = aiService.extractParameters(speechText, userHabit);

        // 3. 数据库查询和基础推荐
        return processDataQuery(aiResponse, userHabit);
    }

    /**
     * 数据库查询和基础推荐
     */
    private AiCourseResponse processDataQuery(AiCourseResponse aiResponse, UserBookingHabitVO userHabit) {
        log.info("🔍 [数据查询] AI提取的参数 - 教练:{}, 课程:{}, 时间:{}",
            aiResponse.getCoachName(), aiResponse.getCourseType(), aiResponse.getAppointmentTime());

        List<String> missingParameters = new ArrayList<>();

        // 1. 教练查询和推荐 - 🔧 关键修复：强制使用用户实际所属俱乐部ID
        CoachEntity coach = null;
        if (aiResponse.getCoachName() != null) {
            // 使用用户实际所属的俱乐部ID进行教练匹配
            coach = queryCoachByName(aiResponse.getCoachName(), userHabit.getClubId());
            if (coach != null) {
                // 验证教练确实属于该俱乐部
                if (!coach.getClubId().equals(userHabit.getClubId())) {
                    log.warn("⚠️ [教练验证] 教练{}不属于用户俱乐部{}，查找失败", coach.getActualName(), userHabit.getClubId());
                    coach = null;
                } else {
                    aiResponse.setCoachName(coach.getActualName()); // 更新为数据库中的完整姓名
                    aiResponse.setExtractedCoach(coach.getCoachNo());
                    log.info("✅ [数据查询] 教练匹配成功：{} -> {} ({}) [俱乐部ID:{}]",
                            aiResponse.getCoachName(), coach.getActualName(), coach.getCoachNo(), coach.getClubId());
                }
            }
        }

        // 教练查不到时按角色推荐 - 🔧 关键修复：使用用户实际俱乐部ID
        if (coach == null) {
            coach = getDefaultCoachByRole(userHabit.getUserRole(), userHabit.getClubId());
            if (coach != null) {
                // 再次验证推荐的教练属于正确俱乐部
                if (coach.getClubId().equals(userHabit.getClubId())) {
                    aiResponse.setCoachName(coach.getActualName());
                    aiResponse.setExtractedCoach(coach.getCoachNo());
                    log.info("🎯 [角色推荐] 推荐教练：{} -> {} [用户俱乐部ID:{}]",
                            coach.getActualName(), coach.getCoachNo(), userHabit.getClubId());
                } else {
                    log.warn("⚠️ [角色推荐] 推荐教练{}不属于用户俱乐部{}，推荐失败",
                            coach.getActualName(), userHabit.getClubId());
                    coach = null;
                    missingParameters.add("约课教练");
                }
            } else {
                missingParameters.add("约课教练");
                log.info("❌ [数据查询] 用户俱乐部{}无可用教练，需用户选择", userHabit.getClubId());
            }
        }

        // 2. 课程查询和严格匹配 - 🔧 关键修复：强制使用用户实际所属俱乐部ID
        ProductEntity course = null;
        String validatedCourseType = null;

        if (aiResponse.getCourseType() != null) {
            // 使用用户实际所属的俱乐部ID进行课程匹配
            validatedCourseType = validateAndMapCourseType(aiResponse.getCourseType(), userHabit.getUserRole(), userHabit.getClubId());
            if (validatedCourseType != null) {
                course = queryCourseByName(validatedCourseType, userHabit.getClubId());
                if (course != null) {
                    // 验证课程确实属于该俱乐部
                    if (!course.getClubId().equals(userHabit.getClubId())) {
                        log.warn("⚠️ [课程验证] 课程{}不属于用户俱乐部{}，查找失败", course.getProductName(), userHabit.getClubId());
                        course = null;
                        validatedCourseType = null;
                    } else {
                        aiResponse.setCourseType(validatedCourseType);
                        aiResponse.setExtractedCourse(course.getProductCode());
                        log.info("✅ [数据查询] 课程匹配成功：{} -> {} -> {} [俱乐部ID:{}]",
                                aiResponse.getCourseType(), validatedCourseType, course.getProductCode(), course.getClubId());
                    }
                }
            }
        }

        // 课程查不到时按角色推荐 - 🔧 关键修复：使用用户实际俱乐部ID
        if (course == null) {
            course = getDefaultCourseByRole(userHabit.getUserRole(), userHabit.getClubId());
            if (course != null) {
                // 验证推荐课程属于正确俱乐部
                if (course.getClubId().equals(userHabit.getClubId())) {
                    aiResponse.setCourseType(course.getProductName());
                    aiResponse.setExtractedCourse(course.getProductCode());
                    log.info("🎯 [角色推荐] 推荐课程：{} -> {} [用户俱乐部ID:{}]",
                            course.getProductName(), course.getProductCode(), userHabit.getClubId());
                } else {
                    log.warn("⚠️ [角色推荐] 推荐课程{}不属于用户俱乐部{}，推荐失败",
                            course.getProductName(), userHabit.getClubId());
                    course = null;
                    missingParameters.add("课程类型");
                }
            } else {
                missingParameters.add("课程类型");
                log.info("❌ [数据查询] 用户俱乐部{}无可用课程，需用户选择", userHabit.getClubId());
            }
        }

        // 3. 时间验证和处理
        if (aiResponse.getAppointmentTime() != null) {
            LocalDateTime validatedTime = validateAppointmentTime(aiResponse.getAppointmentTime());
            if (validatedTime != null) {
                aiResponse.setAppointmentTime(validatedTime);
                aiResponse.setExtractedTimes(createTimeSlots(validatedTime));
                log.info("✅ [数据查询] 时间验证完成：{}", validatedTime);
            } else {
                missingParameters.add("约课时间");
                log.info("❌ [时间验证] 时间不在营业时间内，需重新选择");
            }
        } else {
            missingParameters.add("约课时间");
            log.info("❌ [数据查询] 缺少时间，需用户选择");
        }

        // 4. 新会员特殊处理
        if ("新会员".equals(userHabit.getUserRole()) &&
            validatedCourseType != null && isExperienceCourse(validatedCourseType)) {
            aiResponse.setAiResponse("请点击进入俱乐部，选择喜欢的教练进行课程体验");
            aiResponse.setTargetPage("/pages/club/coaches");
            aiResponse.setNavigationInstruction("选择教练进行体验课程");
            aiResponse.setParametersComplete(false);
            aiResponse.setMissingParameters(List.of("需要选择教练"));
            return aiResponse;
        }

        // 5. 设置完整性和导航（优化前后端交互）
        aiResponse.setMissingParameters(missingParameters);
        aiResponse.setParametersComplete(missingParameters.isEmpty());

        if (aiResponse.getParametersComplete()) {
            // 所有参数完整，直接跳转订单确认页面
            aiResponse.setAiResponse("约课信息已完整，正在为您跳转到订单确认页面");
            aiResponse.setTargetPage("/pages/booking/confirm");
            aiResponse.setNavigationInstruction("跳转订单确认页面");

            // 设置订单确认页面所需的参数
            Map<String, Object> pageParams = new HashMap<>();
            if (coach != null) {
                pageParams.put("coachId", coach.getCoachId()); // 订单必需的教练ID
                pageParams.put("coachName", coach.getActualName()); // 显示用的教练姓名
                pageParams.put("coachNo", coach.getCoachNo()); // 教练编号（备用）
                pageParams.put("coachFee", coach.getCoachFee()); // 教练费用
                pageParams.put("actualClubId", coach.getClubId()); // 🔧 新增：教练所属俱乐部ID
            }
            if (course != null) {
                pageParams.put("productId", course.getProductId()); // 订单必需的商品ID
                pageParams.put("courseName", course.getProductName()); // 显示用的课程名称
                pageParams.put("courseCode", course.getProductCode()); // 课程编号（备用）
                pageParams.put("productType", 1); // 商品类型：1-课程订单
                pageParams.put("quantity", 1); // 默认数量为1
                // 注意：价格将在订单创建时由订单服务计算
            }
            if (aiResponse.getAppointmentTime() != null) {
                pageParams.put("appointmentTime", aiResponse.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                pageParams.put("preferredTime", aiResponse.getAppointmentTime()); // 期望上课时间
            }

            // 添加其他订单必需参数
            pageParams.put("clubId", userHabit.getClubId()); // 使用实际俱乐部ID
            pageParams.put("actualClubId", userHabit.getClubId()); // 🔧 新增：实际俱乐部ID标识
            pageParams.put("memberId", userHabit.getMemberId());
            pageParams.put("orderType", 1); // 1-课程订单
            pageParams.put("paymentMethod", "WECHAT"); // 默认微信支付

            // 转换为JSON字符串传递给前端
            try {
                String pageParamsJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(pageParams);
                aiResponse.setPageParams(pageParamsJson);
                log.info("✅ [前后端交互] 参数完整，跳转订单确认页面，参数：{}", pageParamsJson);
            } catch (Exception e) {
                log.warn("⚠️ [前后端交互] 页面参数序列化失败：{}", e.getMessage());
                aiResponse.setPageParams("");
            }

        } else {
            // 参数不完整，根据缺失参数引导用户选择
            String missingText = String.join("、", missingParameters);
            aiResponse.setAiResponse("还需要补充：" + missingText + "，请选择");

            // 根据缺失的第一个参数决定跳转页面（优先级：教练 > 课程 > 时间）
            if (missingParameters.contains("约课教练")) {
                aiResponse.setTargetPage("/pages/coach/list");
                aiResponse.setNavigationInstruction("选择教练");
                log.info("🔍 [前后端交互] 缺少教练，跳转教练选择页面");
            } else if (missingParameters.contains("课程类型")) {
                aiResponse.setTargetPage("/pages/course/list");
                aiResponse.setNavigationInstruction("选择课程类型");
                log.info("🔍 [前后端交互] 缺少课程，跳转课程选择页面");
            } else if (missingParameters.contains("约课时间")) {
                aiResponse.setTargetPage("/pages/time/select");
                aiResponse.setNavigationInstruction("选择时间");
                log.info("🔍 [前后端交互] 缺少时间，跳转时间选择页面");
            } else {
                // 兜底处理
                aiResponse.setTargetPage("/pages/booking/index");
                aiResponse.setNavigationInstruction("重新约课");
                log.info("🔍 [前后端交互] 未知缺失参数，跳转约课首页");
            }
        }

        log.info("📊 [约课处理] 处理完成 - 完整性:{}, 缺失参数:{}",
            aiResponse.getParametersComplete(), aiResponse.getMissingParameters());

        return aiResponse;
    }

    /**
     * 根据教练名称查询教练（增强版匹配逻辑）
     */
    private CoachEntity queryCoachByName(String coachName, Long clubId) {
        try {
            log.info("🔍 [教练匹配] 开始匹配教练：{}，俱乐部ID：{}", coachName, clubId);

            // 清理教练名称：移除"教练"后缀
            String cleanName = coachName.replace("教练", "").trim();
            log.info("🔍 [教练匹配] 清理后的教练名：{}", cleanName);

            // 1. 精确全名匹配
            CoachEntity coach = coachDao.selectByName(cleanName, clubId);
            if (coach != null) {
                log.info("✅ [教练匹配] 精确匹配成功：{} -> {}", cleanName, coach.getActualName());
                return coach;
            }

            // 2. 姓氏前缀匹配（支持单字姓氏：马 -> 马二）
            if (cleanName.length() == 1) {
                coach = coachDao.selectByNamePrefix(cleanName, clubId);
                if (coach != null) {
                    log.info("✅ [教练匹配] 姓氏前缀匹配成功：{} -> {}", cleanName, coach.getActualName());
                    return coach;
                }
            }

            // 3. 模糊匹配
            coach = coachDao.selectByFuzzyName(cleanName, clubId);
            if (coach != null) {
                log.info("✅ [教练匹配] 模糊匹配成功：{} -> {}", cleanName, coach.getActualName());
                return coach;
            }

            log.warn("⚠️ [教练匹配] 所有匹配策略均失败：{}", coachName);
            return null;

        } catch (Exception e) {
            log.error("❌ [教练匹配] 教练查询失败：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据课程名称查询课程（增强版调试）
     */
    private ProductEntity queryCourseByName(String courseName, Long clubId) {
        try {
            log.info("🔍 [课程查询] 开始查询课程：{}，俱乐部ID：{}", courseName, clubId);

            // 精确匹配
            ProductEntity course = productDao.selectByCourseName(courseName, clubId);
            if (course != null) {
                log.info("✅ [课程查询] 精确匹配成功：{} -> {} (产品ID:{})", courseName, course.getProductName(), course.getProductId());
                return course;
            }

            log.info("🔍 [课程查询] 精确匹配失败，尝试模糊匹配：{}", courseName);

            // 模糊匹配
            course = productDao.selectByFuzzyCourseName(courseName, clubId);
            if (course != null) {
                log.info("✅ [课程查询] 模糊匹配成功：{} -> {} (产品ID:{})", courseName, course.getProductName(), course.getProductId());
                return course;
            }

            log.warn("⚠️ [课程查询] 所有查询策略均失败：{}", courseName);
            return null;

        } catch (Exception e) {
            log.error("❌ [课程查询] 查询失败：{}，错误：{}", courseName, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据用户角色获取默认教练（增强调试）
     */
    private CoachEntity getDefaultCoachByRole(String userRole, Long clubId) {
        try {
            log.info("🔍 [默认教练] 根据角色获取默认教练：{}，俱乐部ID：{}", userRole, clubId);

            switch (userRole) {
                case "新会员":
                case "老会员":
                    CoachEntity defaultCoach = coachDao.selectDefaultCoach(clubId);
                    if (defaultCoach != null) {
                        log.info("✅ [默认教练] 获取默认教练成功：{} (教练编号:{})", defaultCoach.getActualName(), defaultCoach.getCoachNo());
                    } else {
                        log.warn("⚠️ [默认教练] 未找到默认教练，俱乐部ID：{}", clubId);
                    }
                    return defaultCoach;

                case "马主":
                    // 马主推荐最高级别教练
                    List<CoachEntity> coaches = coachDao.selectTop20Coaches(clubId);
                    if (!coaches.isEmpty()) {
                        CoachEntity topCoach = coaches.get(0);
                        log.info("✅ [默认教练] 马主获取顶级教练成功：{} (教练编号:{})", topCoach.getActualName(), topCoach.getCoachNo());
                        return topCoach;
                    } else {
                        log.warn("⚠️ [默认教练] 未找到顶级教练，俱乐部ID：{}", clubId);
                        return null;
                    }

                default:
                    CoachEntity fallbackCoach = coachDao.selectDefaultCoach(clubId);
                    if (fallbackCoach != null) {
                        log.info("✅ [默认教练] 获取兜底教练成功：{} (教练编号:{})", fallbackCoach.getActualName(), fallbackCoach.getCoachNo());
                    } else {
                        log.warn("⚠️ [默认教练] 未找到兜底教练，俱乐部ID：{}", clubId);
                    }
                    return fallbackCoach;
            }
        } catch (Exception e) {
            log.error("❌ [默认教练] 获取默认教练失败：{}，角色：{}，俱乐部ID：{}", e.getMessage(), userRole, clubId, e);
            return null;
        }
    }

    /**
     * 严格验证和映射课程类型（扩展版智能匹配）
     */
    private String validateAndMapCourseType(String aiCourseType, String userRole, Long clubId) {
        if (aiCourseType == null || aiCourseType.trim().isEmpty()) {
            return null;
        }

        try {
            String cleanCourseType = aiCourseType.replace("课程", "").replace("课", "").trim();
            log.info("🔍 [课程匹配] 开始匹配课程：{}，清理后：{}，俱乐部ID：{}", aiCourseType, cleanCourseType, clubId);

            // 1. 直接精确匹配数据库课程名
            ProductEntity directMatch = productDao.selectByCourseName(aiCourseType, clubId);
            if (directMatch != null) {
                log.info("✅ [课程匹配] 直接匹配成功：{}", aiCourseType);
                return aiCourseType;
            }

            // 2. 使用扩展映射表进行智能匹配
            String mappedCourse = findCourseUsingEnhancedMapping(aiCourseType, cleanCourseType, clubId);
            if (mappedCourse != null) {
                return mappedCourse;
            }

            // 3. 尝试清理后的课程名直接匹配
            ProductEntity cleanMatch = productDao.selectByCourseName(cleanCourseType + "课程", clubId);
            if (cleanMatch != null) {
                log.info("✅ [课程匹配] 清理后匹配成功：{} -> {}", aiCourseType, cleanCourseType + "课程");
                return cleanCourseType + "课程";
            }

            // 4. 模糊匹配兜底
            ProductEntity fuzzyMatch = productDao.selectByFuzzyCourseName(aiCourseType, clubId);
            if (fuzzyMatch != null) {
                log.info("✅ [课程匹配] 模糊匹配成功：{} -> {}", aiCourseType, fuzzyMatch.getProductName());
                return fuzzyMatch.getProductName();
            }

            log.warn("⚠️ [课程匹配] 所有匹配策略均失败：{}", aiCourseType);
            return null;

        } catch (Exception e) {
            log.error("❌ [课程匹配] 验证失败：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 使用扩展映射表进行课程智能匹配
     */
    private String findCourseUsingEnhancedMapping(String originalCourse, String cleanCourse, Long clubId) {
        // 遍历扩展映射表
        for (Map.Entry<String, List<String>> entry : ENHANCED_COURSE_MAPPING.entrySet()) {
            String key = entry.getKey();
            List<String> candidates = entry.getValue();

            // 检查是否匹配映射键
            if (originalCourse.contains(key) || cleanCourse.equals(key)) {
                log.info("🔍 [课程匹配] 找到映射键：{} -> 候选项：{}", key, candidates);

                // 尝试每个候选课程名
                for (String candidate : candidates) {
                    try {
                        ProductEntity course = productDao.selectByCourseName(candidate, clubId);
                        if (course != null) {
                            log.info("✅ [课程匹配] 映射匹配成功：{} -> {} -> {}", originalCourse, key, candidate);
                            return candidate;
                        }
                    } catch (Exception e) {
                        log.debug("⚠️ [课程匹配] 候选课程查询失败：{}", candidate);
                    }
                }
            }
        }

        return null;
    }

    /**
     * 验证约课时间是否在营业时间内
     */
    private LocalDateTime validateAppointmentTime(LocalDateTime requestTime) {
        if (requestTime == null) {
            return null;
        }

        try {
            int hour = requestTime.getHour();

            // 检查是否在营业时间内（9:00-21:00）
            if (hour < BUSINESS_START_HOUR || hour >= BUSINESS_END_HOUR) {
                log.warn("⚠️ [时间验证] 约课时间超出营业时间：{}，营业时间：{}:00-{}:00",
                        requestTime, BUSINESS_START_HOUR, BUSINESS_END_HOUR);

                // 调整到营业时间内
                if (hour < BUSINESS_START_HOUR) {
                    LocalDateTime adjustedTime = requestTime.withHour(BUSINESS_START_HOUR).withMinute(0).withSecond(0);
                    log.info("🔧 [时间调整] 调整到营业开始时间：{}", adjustedTime);
                    return adjustedTime;
                } else {
                    LocalDateTime adjustedTime = requestTime.withHour(BUSINESS_END_HOUR - 1).withMinute(0).withSecond(0);
                    log.info("🔧 [时间调整] 调整到营业结束前：{}", adjustedTime);
                    return adjustedTime;
                }
            }

            log.info("✅ [时间验证] 时间在营业时间内：{}", requestTime);
            return requestTime;

        } catch (Exception e) {
            log.error("❌ [时间验证] 验证失败：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 判断是否为体验课程
     */
    private boolean isExperienceCourse(String courseType) {
        if (courseType == null) {
            return false;
        }
        return courseType.contains("体验") ||
               courseType.contains("初级") ||
               courseType.contains("入门") ||
               courseType.equals("基础课程");
    }

    /**
     * 根据用户角色获取默认课程
     */
    private ProductEntity getDefaultCourseByRole(String userRole, Long clubId) {
        try {
            switch (userRole) {
                case "新会员":
                    // 新会员推荐基础课程
                    ProductEntity basicCourse = productDao.selectByCourseName("基础课程", clubId);
                    if (basicCourse != null) {
                        log.info("🎯 [角色推荐] 新会员推荐基础课程");
                        return basicCourse;
                    }
                    break;

                case "老会员":
                    // 老会员推荐最多下单的课程，如果课时包有剩余优先使用
                    ProductEntity preferredCourse = getMostBookedCourseByMember(clubId);
                    if (preferredCourse != null) {
                        log.info("🎯 [角色推荐] 老会员推荐常用课程：{}", preferredCourse.getProductName());
                        return preferredCourse;
                    }
                    // 默认推荐基础课程
                    return productDao.selectByCourseName("基础课程", clubId);

                case "马主":
                    // 马主检查是否绑定马匹，如果有则推荐专业课程
                    if (hasOwnedHorses(clubId)) {
                        ProductEntity professionalCourse = productDao.selectByCourseName("专业课程", clubId);
                        if (professionalCourse != null) {
                            log.info("🎯 [角色推荐] 马主推荐专业课程");
                            return professionalCourse;
                        }
                    }
                    // 默认推荐基础课程
                    return productDao.selectByCourseName("基础课程", clubId);

                default:
                    // 默认推荐基础课程
                    return productDao.selectByCourseName("基础课程", clubId);
            }
        } catch (Exception e) {
            log.warn("⚠️ [角色推荐] 获取默认课程失败：{}", e.getMessage());
        }

        // 最终默认返回基础课程
        try {
            return productDao.selectByCourseName("基础课程", clubId);
        } catch (Exception e) {
            log.error("❌ [角色推荐] 获取基础课程失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取会员最常预订的课程（老会员逻辑）
     */
    private ProductEntity getMostBookedCourseByMember(Long clubId) {
        try {
            // 这里应该查询该会员的历史订单，找出最常预订的课程
            // 暂时返回基础课程作为默认
            return productDao.selectByCourseName("基础课程", clubId);
        } catch (Exception e) {
            log.warn("⚠️ [数据查询] 获取常用课程失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 检查马主是否拥有马匹（马主逻辑）
     */
    private boolean hasOwnedHorses(Long clubId) {
        try {
            // 这里应该查询马匹表，检查是否有绑定该会员的马匹
            // 暂时返回false
            return false;
        } catch (Exception e) {
            log.warn("⚠️ [数据查询] 检查马匹拥有情况失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建时间段数据
     */
    private List<Map<String, Object>> createTimeSlots(LocalDateTime dateTime) {
        List<Map<String, Object>> timeSlots = new ArrayList<>();
        Map<String, Object> slot = new HashMap<>();

        String date = dateTime.format(DateTimeFormatter.ofPattern("MM-dd"));
        String range = dateTime.format(DateTimeFormatter.ofPattern("HH:mm")) + "-" +
                      dateTime.plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm"));
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String day = weekDays[dateTime.getDayOfWeek().getValue() % 7];
        String id = dateTime.format(DateTimeFormatter.ofPattern("MMddHHmm"));

        slot.put("date", date);
        slot.put("range", range);
        slot.put("day", day);
        slot.put("id", id);

        timeSlots.add(slot);
        return timeSlots;
    }

    /**
     * 创建错误响应
     */
    private AiCourseResponse createErrorResponse(String speechText, String errorMessage) {
        AiCourseResponse response = new AiCourseResponse();
        response.setRecognizedText(speechText);
        response.setStatus("error");
        response.setErrorMessage(errorMessage);
        response.setParametersComplete(false);
        response.setMissingParameters(List.of("系统错误"));
        response.setNavigationInstruction("返回首页");
        response.setTargetPage("/pages/index/index");
        response.setPageParams("");
        return response;
    }
}
