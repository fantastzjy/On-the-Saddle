package net.lab1024.sa.admin.module.business.member.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CourseListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubTypeVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.UnavailableTimeSlotVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.OrderCreateVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.ActivityListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MyHorseListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CareStatisticsVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MedicalInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.form.OrderCreateForm;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.dao.ProductCourseDao;
import net.lab1024.sa.admin.module.business.product.dao.ProductActivityDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductCourseEntity;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductActivityEntity;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.horse.dao.HorseHealthPlanDao;
import net.lab1024.sa.admin.module.business.horse.dao.HorseHealthRecordDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseEntity;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthPlanEntity;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthRecordEntity;
import net.lab1024.sa.admin.module.business.horse.constant.HealthPlanTypeEnum;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.sa.admin.util.MemberRequestUtil;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 小程序首页服务
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class HomeService {

    @Resource
    private ClubDao clubDao;

    @Resource
    private CoachDao coachDao;

    @Resource
    private ProductDao productDao;

    @Resource
    private ProductCourseDao productCourseDao;

    @Resource
    private ProductActivityDao productActivityDao;

    @Resource
    private HorseDao horseDao;

    @Resource
    private HorseHealthPlanDao horseHealthPlanDao;

    @Resource
    private HorseHealthRecordDao horseHealthRecordDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private EmployeeDao employeeDao;

    /**
     * 获取俱乐部详细信息
     */
    public ResponseDTO<ClubInfoVO> getClubInfo(String clubCode) {
        if (StrUtil.isBlank(clubCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
        }

        ClubEntity club = clubDao.selectByClubCode(clubCode);
        if (club == null || club.getIsDelete() || !club.getIsValid()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部信息不存在");
        }

        ClubInfoVO clubInfo = SmartBeanUtil.copy(club, ClubInfoVO.class);

        clubInfo.setClubType(1);

        // 处理轮播图 - 将JSON字符串解析为List
        if (StrUtil.isNotBlank(club.getCarouselImages())) {
            try {
                JSONArray carouselArray = JSONUtil.parseArray(club.getCarouselImages());
                List<String> carouselList = carouselArray.toList(String.class);
                clubInfo.setCarouselImages(carouselList);
            } catch (Exception e) {
                log.warn("解析轮播图JSON失败: {}", club.getCarouselImages(), e);
                clubInfo.setCarouselImages(new ArrayList<>());
            }
        } else {
            clubInfo.setCarouselImages(new ArrayList<>());
        }

        // 处理营业时间 - 合并为一个字段
        if (club.getBusinessStartTime() != null && club.getBusinessEndTime() != null) {
            String businessHours = club.getBusinessStartTime() + " - " + club.getBusinessEndTime();
            clubInfo.setBusinessHours(businessHours);
        } else {
            clubInfo.setBusinessHours("营业时间待定");
        }

        return ResponseDTO.ok(clubInfo);
    }

    /**
     * 获取俱乐部类型信息
     */
    public ResponseDTO<ClubTypeVO> getClubType(String clubCode) {
        if (StrUtil.isBlank(clubCode)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
        }

        ClubEntity club = clubDao.selectByClubCode(clubCode);
        if (club == null || club.getIsDelete() || !club.getIsValid()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部信息不存在");
        }

        ClubTypeVO clubTypeVO = new ClubTypeVO();
        clubTypeVO.setClubType(1); // 固定设置为1
        clubTypeVO.setClubName(club.getClubName());

        return ResponseDTO.ok(clubTypeVO);
    }

    /**
     * 获取教练列表
     */
    public ResponseDTO<List<CoachListVO>> getCoachList(String clubCode) {
        try {
            if (StrUtil.isBlank(clubCode)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
            }

            // 验证俱乐部是否存在且有效
            ClubEntity club = clubDao.selectByClubCode(clubCode);
            if (club == null || club.getIsDelete() || !club.getIsValid()) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部不存在");
            }

            // 查询该俱乐部的所有有效教练
            List<CoachEntity> coaches = coachDao.selectList(
                new LambdaQueryWrapper<CoachEntity>()
                    .eq(CoachEntity::getClubId, club.getClubId())
                    .eq(CoachEntity::getIsValid, 1)
                    .eq(CoachEntity::getIsDelete, 0)
                    .orderBy(true, true, CoachEntity::getSortOrder)
            );

            // 转换为VO对象
            List<CoachListVO> result = new ArrayList<>();
            for (CoachEntity coach : coaches) {
                CoachListVO vo = buildCoachListVO(coach);
                result.add(vo);
            }

            return ResponseDTO.ok(result);
        } catch (Exception e) {
            log.error("获取教练列表失败", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "获取教练列表失败");
        }
    }

    /**
     * 构建教练列表VO对象
     */
    private CoachListVO buildCoachListVO(CoachEntity coach) {
        CoachListVO vo = new CoachListVO();

        // 基础信息
        vo.setCoachNo(coach.getCoachNo());
        vo.setActualName(coach.getActualName());
        vo.setGender(coach.getGender());

        // 头像处理
        vo.setAvatarUrl(StrUtil.isNotBlank(coach.getAvatarUrl()) ?
            coach.getAvatarUrl() : "");//TODO 加墨人头像

        // 课时费处理
        vo.setCoachFee(coach.getCoachFee() != null ? coach.getCoachFee() : BigDecimal.ZERO);

        // 教练介绍处理
        vo.setIntroduction(StrUtil.isNotBlank(coach.getIntroduction()) ?
            coach.getIntroduction() : "暂无介绍");

        // 从业时长计算
        vo.setWorkingYears(calculateWorkingYears(coach.getEntryDate()));

        // 骑手等级标签生成
        vo.setRiderLevelTags(buildAllLevelTags(coach));

        // 生成模拟的不可用时间
        vo.setUnavailableTimeSlots(generateMockUnavailableTimeSlots(coach.getCoachId()));

        return vo;
    }

    /**
     * 计算从业时长
     */
    private Integer calculateWorkingYears(LocalDateTime entryDate) {
        if (entryDate == null) return 0;

        long years = ChronoUnit.YEARS.between(entryDate.toLocalDate(), LocalDate.now());
        return (int) Math.max(0, years);
    }

    /**
     * 构建所有等级标签列表（骑手等级 + 教练等级）
     */
    private List<String> buildAllLevelTags(CoachEntity coach) {
        List<String> tags = new ArrayList<>();

        // 添加骑手等级标签
        if (StrUtil.isNotBlank(coach.getRiderLevelShowJumping())) {
            tags.add(coach.getRiderLevelShowJumping() + "场地障碍");
        }

        if (StrUtil.isNotBlank(coach.getRiderLevelDressage())) {
            tags.add(coach.getRiderLevelDressage() + "盛装舞步");
        }

        if (StrUtil.isNotBlank(coach.getRiderLevelEventing())) {
            tags.add(coach.getRiderLevelEventing() + "三项赛");
        }

        // 添加教练等级标签
        if (StrUtil.isNotBlank(coach.getCoachLevel())) {
            tags.add(coach.getCoachLevel() + "教练证");
        }

        return tags;
    }

    /**
     * 模拟生成教练不可用时间（未来7天）
     */
    private List<UnavailableTimeSlotVO> generateMockUnavailableTimeSlots(Long coachId) {
        List<UnavailableTimeSlotVO> unavailableSlots = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 基于教练ID生成固定的"随机"不可用时间
        Random random = new Random(coachId); // 使用coachId作为种子，确保同一教练返回相同结果

        for (int day = 0; day < 7; day++) {
            LocalDate date = today.plusDays(day);
            String dateStr = date.toString();

            // 每天随机生成2-5个不可用时间段
            int slotsCount = 2 + random.nextInt(4); // 2-5个时间段
            Set<Integer> usedHours = new HashSet<>();
            List<String> timeSlots = new ArrayList<>();

            for (int i = 0; i < slotsCount; i++) {
                int hour = 9 + random.nextInt(9); // 9:00-17:00之间
                if (!usedHours.contains(hour)) {
                    usedHours.add(hour);
                    String timeSlot = String.format("%02d:00-%02d:00", hour, hour + 1);
                    timeSlots.add(timeSlot);
                }
            }

            // 对时间段排序
            Collections.sort(timeSlots);

            // 创建当天的不可用时间VO
            UnavailableTimeSlotVO daySlot = new UnavailableTimeSlotVO();
            daySlot.setDate(dateStr);
            daySlot.setTimeSlots(timeSlots);
            unavailableSlots.add(daySlot);
        }

        return unavailableSlots;
    }

    /**
     * 获取课程列表
     */
    public ResponseDTO<List<CourseListVO>> getCourseList(String clubCode) {
        try {
            if (StrUtil.isBlank(clubCode)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
            }

            // 验证俱乐部是否存在且有效
            ClubEntity club = clubDao.selectByClubCode(clubCode);
            if (club == null || club.getIsDelete() || !club.getIsValid()) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部不存在");
            }

            // 查询该俱乐部的商品，过滤课程类型为"课程"(productType=1)，子类型为"单人课"
            List<ProductEntity> products = productDao.selectList(
                new LambdaQueryWrapper<ProductEntity>()
                    .eq(ProductEntity::getClubId, club.getClubId())
                    .eq(ProductEntity::getProductType, 1) // 1代表课程
                    .eq(ProductEntity::getSubType, "single_class")
                    .eq(ProductEntity::getIsValid, true)
                    .eq(ProductEntity::getIsDelete, false)
                    .orderBy(true, true, ProductEntity::getProductId)
            );

            // 转换为VO对象
            List<CourseListVO> result = new ArrayList<>();
            for (ProductEntity product : products) {
                // 查询对应的课程配置信息
                ProductCourseEntity courseConfig = productCourseDao.selectOne(
                    new LambdaQueryWrapper<ProductCourseEntity>()
                        .eq(ProductCourseEntity::getProductId, product.getProductId())
                );

                CourseListVO vo = new CourseListVO();
                vo.setCourseCode(product.getProductCode());
                vo.setCourseName(product.getProductName());

                if (courseConfig != null) {
                    vo.setBasePrice(courseConfig.getBasePrice() != null ? courseConfig.getBasePrice() : BigDecimal.ZERO);
                } else {
                    vo.setBasePrice(BigDecimal.ZERO);
                }

                result.add(vo);
            }

            return ResponseDTO.ok(result);
        } catch (Exception e) {
            log.error("获取课程列表失败", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "获取课程列表失败");
        }
    }

    /**
     * 创建订单
     */
    public ResponseDTO<OrderCreateVO> createOrder(OrderCreateForm form) {
        try {
            // TODO: 数据校验（预留位置）
            // 1. 验证俱乐部编码是否存在
            // 2. 验证教练编号是否存在
            // 3. 验证课程编号是否存在
            // 4. 验证时间段是否可用
            // 5. 验证金额计算是否正确

            // 生成订单号（格式：yyyyMMddHHmmss + 6位随机数）
            String orderNo = generateOrderNo();

            // 暂不保存到数据库，只模拟返回
            // 构建响应
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expireTime = now.plusMinutes(30);

            OrderCreateVO vo = new OrderCreateVO();
            vo.setOrderNo(orderNo);
            vo.setStatus(1);
            vo.setCreateTime(now);
            vo.setExpireTime(expireTime);
            vo.setPaymentCountdown(1800L); // 30分钟 = 1800秒

            return ResponseDTO.ok(vo);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "创建订单失败");
        }
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomNum = String.format("%06d", new Random().nextInt(1000000));
        return timestamp + randomNum;
    }

    /**
     * 获取活动列表
     */
    public ResponseDTO<List<ActivityListVO>> getActivityList(String clubCode) {
        try {
            if (StrUtil.isBlank(clubCode)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部编码不能为空");
            }

            // 验证俱乐部是否存在且有效
            ClubEntity club = clubDao.selectByClubCode(clubCode);
            if (club == null || club.getIsDelete() || !club.getIsValid()) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部不存在");
            }

            // 查询该俱乐部的活动商品，过滤活动类型（productType=3，假设3代表活动）
            List<ProductEntity> products = productDao.selectList(
                new LambdaQueryWrapper<ProductEntity>()
                    .eq(ProductEntity::getClubId, club.getClubId())
                    .eq(ProductEntity::getProductType, 3) // 3代表活动类型
                    .eq(ProductEntity::getIsValid, true)
                    .eq(ProductEntity::getIsDelete, false)
                    .orderBy(true, true, ProductEntity::getProductId)
            );

            // 转换为VO对象
            List<ActivityListVO> result = new ArrayList<>();
            for (ProductEntity product : products) {
                // 查询对应的活动配置信息
                ProductActivityEntity activityConfig = productActivityDao.selectOne(
                    new LambdaQueryWrapper<ProductActivityEntity>()
                        .eq(ProductActivityEntity::getProductId, product.getProductId())
                );

                if (activityConfig != null) {
                    ActivityListVO vo = new ActivityListVO();
                    vo.setActivityName(activityConfig.getActivityName());
                    vo.setActivityDetail(activityConfig.getActivityDetails());
                    vo.setStartTime(activityConfig.getStartTime());
                    vo.setEndTime(activityConfig.getEndTime());
                    vo.setLocation(activityConfig.getActivityLocation());
                    vo.setPrice(activityConfig.getPrice() != null ? activityConfig.getPrice() : BigDecimal.ZERO);
                    vo.setRefundRule(StrUtil.isNotBlank(activityConfig.getRefundRule()) ?
                        activityConfig.getRefundRule() : "退款规则待定");

                    // 解析详情图片JSON为List<String>
                    if (StrUtil.isNotBlank(activityConfig.getDetailImages())) {
                        try {
                            JSONArray imageArray = JSONUtil.parseArray(activityConfig.getDetailImages());
                            List<String> imageList = imageArray.toList(String.class);
                            vo.setDetailImages(imageList);
                        } catch (Exception e) {
                            log.warn("解析活动详情图片JSON失败: {}", activityConfig.getDetailImages(), e);
                            vo.setDetailImages(new ArrayList<>());
                        }
                    } else {
                        vo.setDetailImages(new ArrayList<>());
                    }

                    result.add(vo);
                }
            }

            return ResponseDTO.ok(result);
        } catch (Exception e) {
            log.error("获取活动列表失败", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "获取活动列表失败");
        }
    }

    /**
     * 获取我的马匹列表
     */
    public ResponseDTO<List<MyHorseListVO>> getMyHorseList() {
        try {
            // 获取当前登录会员ID
            Long currentMemberId = MemberRequestUtil.getRequestMemberId();

            // 验证登录状态
            if (currentMemberId == null) {
                return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID, "请先登录");
            }

            // 查询该会员的马主马列表（不限制俱乐部）
            List<HorseEntity> horses = horseDao.selectList(
                new LambdaQueryWrapper<HorseEntity>()
                    .eq(HorseEntity::getHorseType, 2) // 2-马主马
                    .eq(HorseEntity::getOwnerId, currentMemberId)
                    .eq(HorseEntity::getIsValid, 1)
                    .eq(HorseEntity::getIsDelete, 0)
                    .orderBy(true, true, HorseEntity::getCreateTime)
            );

            // 收集所有需要查询的员工ID（责任教练和责任马工）
            Set<Long> employeeIds = new HashSet<>();
            for (HorseEntity horse : horses) {
                if (horse.getResponsibleCoachId() != null) {
                    employeeIds.add(horse.getResponsibleCoachId());
                }
                if (horse.getResponsibleGroomId() != null) {
                    employeeIds.add(horse.getResponsibleGroomId());
                }
            }

            // 批量查询员工信息
            Map<Long, String> employeeNameMap = new HashMap<>();
            if (!employeeIds.isEmpty()) {
                List<EmployeeVO> employees = employeeDao.getEmployeeByIds(employeeIds);
                for (EmployeeVO employee : employees) {
                    employeeNameMap.put(employee.getEmployeeId(), employee.getActualName());
                }
            }

            // 转换为VO对象
            List<MyHorseListVO> result = new ArrayList<>();
            for (HorseEntity horse : horses) {
                MyHorseListVO vo = buildMyHorseVO(horse, employeeNameMap);
                result.add(vo);
            }

            return ResponseDTO.ok(result);
        } catch (Exception e) {
            log.error("获取我的马匹列表失败", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "获取我的马匹列表失败");
        }
    }

    /**
     * 构建我的马匹VO对象
     */
    private MyHorseListVO buildMyHorseVO(HorseEntity horse, Map<Long, String> employeeNameMap) {
        MyHorseListVO vo = new MyHorseListVO();

        // 基础信息
        vo.setHorseName(horse.getHorseName());
        vo.setChipNo(horse.getChipNo());

        // 生日格式化（yyyy/MM/dd）
        if (horse.getBirthDate() != null) {
            vo.setBirthDate(horse.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        } else {
            vo.setBirthDate("");
        }

        // 责任教练
        String responsibleCoachName = "";
        if (horse.getResponsibleCoachId() != null) {
            responsibleCoachName = employeeNameMap.getOrDefault(horse.getResponsibleCoachId(), "未知教练");
        }
        vo.setResponsibleCoach(responsibleCoachName);

        // 责任马工
        String responsibleGroomName = "";
        if (horse.getResponsibleGroomId() != null) {
            responsibleGroomName = employeeNameMap.getOrDefault(horse.getResponsibleGroomId(), "未知马工");
        }
        vo.setResponsibleGroom(responsibleGroomName);

        // 寄养时间段格式化
        vo.setBoardingPeriod(formatBoardingPeriod(horse.getBoardingStartDate(), horse.getBoardingEndDate()));

        // 构建养护统计列表
        vo.setCareStatistics(buildCareStatistics(horse.getHorseId()));

        // 构建医疗信息列表
        vo.setMedicalInfo(buildMedicalInfo(horse.getHorseId()));

        return vo;
    }

    /**
     * 格式化寄养时间段
     */
    private String formatBoardingPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");

        if (startDate != null && endDate != null) {
            String startStr = startDate.format(formatter);
            String endStr = endDate.format(formatter);
            return startStr + "~" + endStr;
        } else if (startDate != null) {
            return startDate.format(formatter) + "~";
        } else {
            return "~" + endDate.format(formatter);
        }
    }

    /**
     * 构建养护统计列表
     */
    private List<CareStatisticsVO> buildCareStatistics(Long horseId) {
        List<CareStatisticsVO> careStatistics = new ArrayList<>();

        // 查询该马匹的健康记录，按计划类型分组统计
        List<HorseHealthRecordEntity> records = horseHealthRecordDao.selectList(
            new LambdaQueryWrapper<HorseHealthRecordEntity>()
                .eq(HorseHealthRecordEntity::getHorseId, horseId)
                .eq(HorseHealthRecordEntity::getIsValid, 1)
                .eq(HorseHealthRecordEntity::getIsDelete, 0)
        );

        // 按计划类型分组统计
        Map<String, Long> statisticsMap = records.stream()
            .collect(Collectors.groupingBy(HorseHealthRecordEntity::getPlanType, Collectors.counting()));

        // 转换为VO
        for (Map.Entry<String, Long> entry : statisticsMap.entrySet()) {
            String planType = entry.getKey();
            Long count = entry.getValue();

            CareStatisticsVO statistics = new CareStatisticsVO();
            statistics.setPlanTypeName(HealthPlanTypeEnum.getDescByValue(planType));
            statistics.setCompletedCount(count.intValue());
            statistics.setDescription(statistics.getPlanTypeName() + "已完成" + count + "次");

            careStatistics.add(statistics);
        }

        return careStatistics;
    }

    /**
     * 构建医疗信息列表
     */
    private List<MedicalInfoVO> buildMedicalInfo(Long horseId) {
        List<MedicalInfoVO> medicalInfo = new ArrayList<>();

        // 查询该马匹的健康计划
        List<HorseHealthPlanEntity> plans = horseHealthPlanDao.selectList(
            new LambdaQueryWrapper<HorseHealthPlanEntity>()
                .eq(HorseHealthPlanEntity::getHorseId, horseId)
                .eq(HorseHealthPlanEntity::getIsValid, 1)
                .eq(HorseHealthPlanEntity::getIsDelete, 0)
                .isNotNull(HorseHealthPlanEntity::getNextDate)
                .orderBy(true, true, HorseHealthPlanEntity::getNextDate)
        );

        // 转换为VO
        for (HorseHealthPlanEntity plan : plans) {
            MedicalInfoVO medical = new MedicalInfoVO();
            medical.setPlanTypeName(HealthPlanTypeEnum.getDescByValue(plan.getPlanType()));
            medical.setNextExecuteTime(plan.getNextDate());

            medicalInfo.add(medical);
        }

        return medicalInfo;
    }
}
