package net.lab1024.sa.admin.module.openapi.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.coach.constant.CoachCertificateConstant;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CoachSimpleProfileVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CourseListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubTypeVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.UnavailableTimeSlotVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.OrderCreateVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.ActivityListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MyHorseListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CareStatisticsVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MedicalInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.BookingTimeVO;
import net.lab1024.sa.admin.module.openapi.domain.form.OrderCreateForm;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.schedule.service.ResourceScheduleService;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Resource
    private ResourceScheduleService resourceScheduleService;

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

        // 专长领域处理：字符串转列表
        vo.setSpecialtiesList(parseSpecialtiesString(coach.getSpecialties()));

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

        // 添加教练等级标签
        if (coach.getCoachStarLevel() != null && coach.getCoachStarLevel() > 0) {
            String levelText = CoachCertificateConstant.getCoachStarLevelText(coach.getCoachStarLevel());
            tags.add(levelText + "教练证");
        }

        // 添加骑手等级标签
        if (coach.getRiderShowJumpingLevel() != null && coach.getRiderShowJumpingLevel() > 0) {
            String levelText = CoachCertificateConstant.getRiderLevelText(coach.getRiderShowJumpingLevel());
            tags.add(levelText + "场地障碍");
        }

        if (coach.getRiderDressageLevel() != null && coach.getRiderDressageLevel() > 0) {
            String levelText = CoachCertificateConstant.getRiderLevelText(coach.getRiderDressageLevel());
            tags.add(levelText + "盛装舞步");
        }

        if (coach.getRiderEventingLevel() != null && coach.getRiderEventingLevel() > 0) {
            String levelText = CoachCertificateConstant.getRiderLevelText(coach.getRiderEventingLevel());
            tags.add(levelText + "三项赛");
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
     * 解析专长领域字符串为列表
     * @param specialties 专长领域字符串（逗号分隔）
     * @return 专长领域列表
     */
    private List<String> parseSpecialtiesString(String specialties) {
        if (StrUtil.isBlank(specialties)) {
            return new ArrayList<>();
        }

        return Arrays.stream(specialties.split(","))
            .map(String::trim)
            .filter(StrUtil::isNotBlank)
            .collect(Collectors.toList());
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
     * 检查活动人数是否已满
     */
    private ResponseDTO<Boolean> checkActivityCapacity(Long productId) {
        try {
            // 1. 查询活动配置的最大人数
            ProductActivityEntity activity = productActivityDao.selectOne(
                new LambdaQueryWrapper<ProductActivityEntity>()
                    .eq(ProductActivityEntity::getProductId, productId)
            );

            if (activity == null || activity.getMaxParticipants() == null) {
                return ResponseDTO.userErrorParam("活动配置不存在或未设置最大参与人数");
            }

            // 2. 统计已参与人数（待支付+已支付+已完成状态）
            List<Integer> statusList = Arrays.asList(1, 2, 3); // 待支付、已支付、已完成
            int participantCount = orderDao.countActivityParticipants(productId, statusList);

            // 3. 判断是否已满
            if (participantCount >= activity.getMaxParticipants()) {
                return ResponseDTO.userErrorParam("活动人数已满，当前参与人数：" + participantCount + "，最大人数：" + activity.getMaxParticipants());
            }

            return ResponseDTO.ok(true);
        } catch (Exception e) {
            log.error("检查活动人数失败", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "检查活动人数失败");
        }
    }

    /**
     * 创建订单 - 完整实现
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<OrderCreateVO> createOrder(OrderCreateForm form) {
        try {
            // 1. 数据校验
            ResponseDTO<ValidationResult> validationResult = validateOrderCreateForm(form);
            if (!validationResult.getOk()) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, validationResult.getMsg());
            }

            ValidationResult validation = validationResult.getData();
            ClubEntity club = validation.getClub();
            CoachEntity coach = validation.getCoach();
            ProductEntity product = validation.getProduct();

            // 2. 检查时间段可用性（仅限课程类型）
            if (product.getProductType() == 1) {
                for (BookingTimeVO timeInfo : form.getTimes()) {
                    LocalDate date = LocalDate.parse(timeInfo.getDate());

                    for (String timeSlot : timeInfo.getTimeSlots()) {
                        String[] times = timeSlot.split("-");
                        LocalTime startTime = LocalTime.parse(times[0]);
                        LocalTime endTime = LocalTime.parse(times[1]);

                        // 检查教练可用性
                        ResponseDTO<Boolean> coachAvailable = resourceScheduleService
                                .checkResourceAvailability(club.getClubId(), 2, coach.getCoachId(),
                                                         date, startTime, endTime);

                        if (!coachAvailable.getOk() || !Boolean.TRUE.equals(coachAvailable.getData())) {
                            return ResponseDTO.userErrorParam(
                                    String.format("教练%s在%s %s时间段不可用",
                                                coach.getActualName(), date, timeSlot));
                        }
                    }
                }
            }

            // 3. 如果是活动类型，校验人数
            if (product.getProductType() == 3) {
                ResponseDTO<Boolean> capacityCheck = checkActivityCapacity(product.getProductId());
                if (!capacityCheck.getOk()) {
                    return ResponseDTO.error(UserErrorCode.PARAM_ERROR, capacityCheck.getMsg());
                }
            }

            // 4. 生成订单号
            String orderNo = generateOrderNo();

            // 4. 创建订单记录
            OrderEntity order = buildOrderEntity(form, validation, orderNo);
            order.setOrderStatus(1); // 待支付
            order.setPaymentExpireTime(LocalDateTime.now().plusMinutes(10)); // 10分钟支付期限
            orderDao.insert(order);

            // 5. 占用教练时间段（仅限课程类型）
            if (product.getProductType() == 1) {
                LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
                for (BookingTimeVO timeInfo : form.getTimes()) {
                    LocalDate date = LocalDate.parse(timeInfo.getDate());

                    for (String timeSlot : timeInfo.getTimeSlots()) {
                        String[] times = timeSlot.split("-");
                        LocalTime startTime = LocalTime.parse(times[0]);
                        LocalTime endTime = LocalTime.parse(times[1]);

                        ResponseDTO<Void> occupyResult = resourceScheduleService.occupyResourceTimeSlot(
                                orderNo, club.getClubId(), 2, coach.getCoachId(),
                                date, startTime, endTime, expireTime);

                        if (!occupyResult.getOk()) {
                            // 如果占用失败，需要回滚之前的操作
                            throw new RuntimeException("占用教练时间段失败：" + occupyResult.getMsg());
                        }
                    }
                }
            }

            // 6. 构建响应
            OrderCreateVO vo = buildOrderCreateVO(order);

            log.info("订单创建成功，订单号：{}", orderNo);
            return ResponseDTO.ok(vo);

        } catch (Exception e) {
            log.error("创建订单失败", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 订单数据校验
     */
    private ResponseDTO<ValidationResult> validateOrderCreateForm(OrderCreateForm form) {
        ValidationResult result = new ValidationResult();

        // 1. 校验俱乐部
        ClubEntity club = clubDao.selectByClubCode(form.getClubCode());
        if (club == null || club.getIsDelete() || !club.getIsValid()) {
            return ResponseDTO.userErrorParam("俱乐部不存在");
        }
        result.setClub(club);

        // 2. 校验教练
        CoachEntity coach = coachDao.selectByCoachNo(form.getCoachNo());
        if (coach == null || Boolean.TRUE.equals(coach.getIsDelete()) || !Boolean.TRUE.equals(coach.getIsValid())) {
            return ResponseDTO.userErrorParam("教练不存在");
        }
        if (!coach.getClubId().equals(club.getClubId())) {
            return ResponseDTO.userErrorParam("教练不属于该俱乐部");
        }
        result.setCoach(coach);

        // 3. 校验课程
        ProductEntity product = productDao.selectByProductCode(club.getClubId(), form.getCourseCode());
        if (product == null || product.getIsDelete() || !product.getIsValid()) {
            return ResponseDTO.userErrorParam("课程不存在");
        }
        if (!product.getClubId().equals(club.getClubId())) {
            return ResponseDTO.userErrorParam("课程不属于该俱乐部");
        }
        result.setProduct(product);

        // 4. 校验时间格式
        for (BookingTimeVO timeInfo : form.getTimes()) {
            try {
                LocalDate.parse(timeInfo.getDate());
                for (String timeSlot : timeInfo.getTimeSlots()) {
                    if (!timeSlot.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
                        return ResponseDTO.userErrorParam("时间格式错误：" + timeSlot);
                    }
                    // 验证时间段的逻辑性
                    String[] times = timeSlot.split("-");
                    LocalTime start = LocalTime.parse(times[0]);
                    LocalTime end = LocalTime.parse(times[1]);
                    if (!start.isBefore(end)) {
                        return ResponseDTO.userErrorParam("开始时间必须早于结束时间：" + timeSlot);
                    }
                }
            } catch (Exception e) {
                return ResponseDTO.userErrorParam("时间格式错误：" + timeInfo.getDate());
            }
        }

        // 5. 校验金额计算
        ResponseDTO<Void> amountValidation = validateAmountCalculation(form);
        if (!amountValidation.getOk()) {
            return ResponseDTO.userErrorParam(amountValidation.getMsg());
        }

        return ResponseDTO.ok(result);
    }

    /**
     * 增强的金额校验方法
     */
    private ResponseDTO<Void> validateAmountCalculation(OrderCreateForm form) {
        try {
            // 1. 计算总时间段数
            int totalTimeSlots = calculateTotalTimeSlots(form.getTimes());

            if (totalTimeSlots <= 0) {
                return ResponseDTO.userErrorParam("时间段数量不能为0");
            }

            // 2. 计算预期总价：(教练费 + 基础费) × 时间段个数
            BigDecimal unitPrice = form.getCoachFee().add(form.getBaseFee());
            BigDecimal expectedTotal = unitPrice.multiply(BigDecimal.valueOf(totalTimeSlots));

            // 3. 校验金额（使用BigDecimal的精确比较）
            if (expectedTotal.compareTo(form.getTotalAmount()) != 0) {
                return ResponseDTO.userErrorParam(
                        String.format("金额计算错误，预期总价：%.2f 元，实际总价：%.2f 元，时间段数：%d，单价：%.2f 元",
                                expectedTotal, form.getTotalAmount(), totalTimeSlots, unitPrice));
            }

            // 4. 额外的时间段合理性校验
            ResponseDTO<Void> timeSlotsValidation = validateTimeSlots(form.getTimes());
            if (!timeSlotsValidation.getOk()) {
                return timeSlotsValidation;
            }

            return ResponseDTO.ok();
        } catch (Exception e) {
            log.error("金额校验异常", e);
            return ResponseDTO.userErrorParam("金额校验失败：" + e.getMessage());
        }
    }

    /**
     * 计算总时间段数量
     */
    private int calculateTotalTimeSlots(List<BookingTimeVO> times) {
        if (times == null || times.isEmpty()) {
            return 0;
        }

        return times.stream()
                .filter(Objects::nonNull)
                .mapToInt(timeInfo -> {
                    List<String> timeSlots = timeInfo.getTimeSlots();
                    return timeSlots != null ? timeSlots.size() : 0;
                })
                .sum();
    }

    /**
     * 校验时间段合理性
     */
    private ResponseDTO<Void> validateTimeSlots(List<BookingTimeVO> times) {
        try {
            for (BookingTimeVO timeInfo : times) {
                LocalDate date = LocalDate.parse(timeInfo.getDate());

                // 检查日期不能是过去的日期
                if (date.isBefore(LocalDate.now())) {
                    return ResponseDTO.userErrorParam("不能预订过去的日期：" + timeInfo.getDate());
                }

                // 检查时间段格式和逻辑
                for (String timeSlot : timeInfo.getTimeSlots()) {
                    if (!timeSlot.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
                        return ResponseDTO.userErrorParam("时间格式错误：" + timeSlot + "，正确格式应为：HH:mm-HH:mm");
                    }

                    String[] times_array = timeSlot.split("-");
                    LocalTime startTime = LocalTime.parse(times_array[0]);
                    LocalTime endTime = LocalTime.parse(times_array[1]);

                    if (!startTime.isBefore(endTime)) {
                        return ResponseDTO.userErrorParam("开始时间必须早于结束时间：" + timeSlot);
                    }

                    // 检查时间段长度是否合理（比如不能超过4小时）
                    long minutes = Duration.between(startTime, endTime).toMinutes();
                    if (minutes > 240) { // 4小时 = 240分钟
                        return ResponseDTO.userErrorParam("单个时间段不能超过4小时：" + timeSlot);
                    }
                    if (minutes < 30) { // 最少30分钟
                        return ResponseDTO.userErrorParam("单个时间段不能少于30分钟：" + timeSlot);
                    }
                }

                // 检查同一天内时间段是否重叠
                ResponseDTO<Void> overlapCheck = checkTimeSlotOverlap(timeInfo.getTimeSlots());
                if (!overlapCheck.getOk()) {
                    return ResponseDTO.userErrorParam("日期 " + timeInfo.getDate() + " " + overlapCheck.getMsg());
                }
            }

            return ResponseDTO.ok();
        } catch (Exception e) {
            log.error("时间段校验异常", e);
            return ResponseDTO.userErrorParam("时间段校验失败：" + e.getMessage());
        }
    }

    /**
     * 检查时间段重叠
     */
    private ResponseDTO<Void> checkTimeSlotOverlap(List<String> timeSlots) {
        if (timeSlots == null || timeSlots.size() <= 1) {
            return ResponseDTO.ok();
        }

        // 转换为时间对象并排序
        List<LocalTime[]> timeRanges = new ArrayList<>();
        for (String timeSlot : timeSlots) {
            String[] times = timeSlot.split("-");
            LocalTime start = LocalTime.parse(times[0]);
            LocalTime end = LocalTime.parse(times[1]);
            timeRanges.add(new LocalTime[]{start, end});
        }

        // 按开始时间排序
        timeRanges.sort(Comparator.comparing(range -> range[0]));

        // 检查重叠
        for (int i = 0; i < timeRanges.size() - 1; i++) {
            LocalTime currentEnd = timeRanges.get(i)[1];
            LocalTime nextStart = timeRanges.get(i + 1)[0];

            if (currentEnd.isAfter(nextStart)) {
                return ResponseDTO.userErrorParam(
                        String.format("存在时间段重叠：%s-%s 与 %s-%s",
                                timeRanges.get(i)[0], currentEnd, nextStart, timeRanges.get(i + 1)[1]));
            }
        }

        return ResponseDTO.ok();
    }

    /**
     * 构建订单实体
     */
    private OrderEntity buildOrderEntity(OrderCreateForm form, ValidationResult validation, String orderNo) {
        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNo);
        order.setClubId(validation.getClub().getClubId());
        order.setMemberId(getCurrentMemberId());
        order.setOrderType(1); // 课程订单
        order.setOrderStatus(1); // 待支付
        order.setTotalAmount(form.getTotalAmount());
        order.setPaidAmount(BigDecimal.ZERO);
        order.setPaymentMethod("");
        order.setRemark(""); // OrderCreateForm中没有remark字段，设为空字符串

        // 设置商品信息（合并后的字段）
        order.setProductId(validation.getProduct().getProductId());
        order.setProductName(validation.getProduct().getProductName());
        order.setProductType(validation.getProduct().getProductType());
        order.setQuantity(calculateTotalQuantity(form.getTimes()));
        order.setUnitPrice(calculateUnitPrice(form.getTotalAmount(), order.getQuantity()));
        order.setCoachId(validation.getCoach().getCoachId());
        order.setPreferredTimes(JSONUtil.toJsonStr(form.getTimes()));

        order.setCreateBy("member");
        order.setCreateTime(LocalDateTime.now());
        return order;
    }

    /**
     * 计算总课时数
     */
    private Integer calculateTotalQuantity(List<BookingTimeVO> times) {
        return times.stream()
                   .mapToInt(timeInfo -> timeInfo.getTimeSlots().size())
                   .sum();
    }

    /**
     * 计算单价
     */
    private BigDecimal calculateUnitPrice(BigDecimal totalAmount, Integer quantity) {
        if (quantity == null || quantity == 0) {
            return BigDecimal.ZERO;
        }
        return totalAmount.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP);
    }

    /**
     * 构建订单创建响应
     */
    private OrderCreateVO buildOrderCreateVO(OrderEntity order) {
        OrderCreateVO vo = new OrderCreateVO();
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getOrderStatus());
        vo.setCreateTime(order.getCreateTime());
        vo.setExpireTime(order.getPaymentExpireTime());

        // 计算支付倒计时
        if (order.getPaymentExpireTime() != null) {
            long countdown = Duration.between(LocalDateTime.now(), order.getPaymentExpireTime()).getSeconds();
            vo.setPaymentCountdown(Math.max(0, countdown));
        } else {
            vo.setPaymentCountdown(0L);
        }

        return vo;
    }

    /**
     * 获取当前会员ID（从登录信息中获取）
     */
    private Long getCurrentMemberId() {
        try {
            return MemberRequestUtil.getRequestMemberId();
        } catch (Exception e) {
            log.warn("获取当前会员ID失败，使用默认值", e);
            return 1L; // 临时默认值
        }
    }

    /**
     * 校验结果内部类
     */
    private static class ValidationResult {
        private ClubEntity club;
        private CoachEntity coach;
        private ProductEntity product;

        public ClubEntity getClub() { return club; }
        public void setClub(ClubEntity club) { this.club = club; }

        public CoachEntity getCoach() { return coach; }
        public void setCoach(CoachEntity coach) { this.coach = coach; }

        public ProductEntity getProduct() { return product; }
        public void setProduct(ProductEntity product) { this.product = product; }
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
        vo.setBreed(horse.getBreed());
        vo.setColor(horse.getColor());
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

    /**
     * 获取教练个人简要信息
     */
    public ResponseDTO<CoachSimpleProfileVO> getCoachProfile(String coachNo) {
        if (StrUtil.isBlank(coachNo)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "教练编号不能为空");
        }

        // 根据教练编号查询教练信息
        CoachEntity coach = coachDao.selectByCoachNo(coachNo);
        if (coach == null || Boolean.TRUE.equals(coach.getIsDelete()) || !Boolean.TRUE.equals(coach.getIsValid())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "教练信息不存在");
        }

        // 构建返回结果
        CoachSimpleProfileVO profileVO = new CoachSimpleProfileVO();
        profileVO.setAvatarUrl(coach.getAvatarUrl());
        profileVO.setActualName(coach.getActualName());
        profileVO.setGender(coach.getGender());

        return ResponseDTO.ok(profileVO);
    }
}
