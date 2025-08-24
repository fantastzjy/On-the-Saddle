package net.lab1024.sa.admin.module.business.schedule.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseEntity;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.order.dao.PackageBalanceDao;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.order.domain.entity.PackageBalanceEntity;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.dao.ProductCourseDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductCourseEntity;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.admin.module.business.schedule.dao.LessonScheduleDao;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.LessonScheduleEntity;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ConflictCheckForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleCombinedQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleTimeUpdateForm;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ConflictCheckVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ScheduleDetailVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ScheduleListVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ScheduleCombinedVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.BookingRecordVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课表管理服务
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private LessonScheduleDao lessonScheduleDao;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private CoachDao coachDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private HorseDao horseDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductCourseDao productCourseDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PackageBalanceDao packageBalanceDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 查询课表列表
     */
    public ResponseDTO<PageResult<ScheduleListVO>> queryScheduleList(ScheduleQueryForm queryForm) {
        try {
            Page<LessonScheduleEntity> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
            LambdaQueryWrapper<LessonScheduleEntity> queryWrapper = buildQueryWrapper(queryForm);

            IPage<LessonScheduleEntity> pageResult = lessonScheduleDao.selectPage(page, queryWrapper);

            // 转换为VO
            List<ScheduleListVO> scheduleList = SmartBeanUtil.copyList(pageResult.getRecords(), ScheduleListVO.class);

            // 补充关联数据
            enhanceScheduleData(scheduleList);

            PageResult<ScheduleListVO> result = SmartPageUtil.convert2PageResult(page, scheduleList);
            result.setTotal(pageResult.getTotal());

            log.info("查询课表列表成功，共{}条记录", result.getTotal());
            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("查询课表列表失败", e);
            return ResponseDTO.userErrorParam("查询课表列表失败");
        }
    }

    /**
     * 获取教练列表
     */
    public ResponseDTO<List<CoachVO>> getCoachList() {
        try {
            LambdaQueryWrapper<CoachEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CoachEntity::getIsValid, 1)
                       .eq(CoachEntity::getIsDelete, 0)
                       .orderByAsc(CoachEntity::getSortOrder);

            List<CoachEntity> coachEntities = coachDao.selectList(queryWrapper);
            List<CoachVO> coachList = SmartBeanUtil.copyList(coachEntities, CoachVO.class);

            log.info("获取教练列表成功，共{}名教练", coachList.size());
            return ResponseDTO.ok(coachList);

        } catch (Exception e) {
            log.error("获取教练列表失败", e);
            return ResponseDTO.userErrorParam("获取教练列表失败");
        }
    }

    /**
     * 获取课表详情
     */
    public ResponseDTO<ScheduleDetailVO> getScheduleDetail(Long scheduleId) {
        try {
            LessonScheduleEntity schedule = lessonScheduleDao.selectById(scheduleId);
            if (schedule == null) {
                return ResponseDTO.userErrorParam("课表不存在");
            }

            ScheduleDetailVO scheduleVO = SmartBeanUtil.copy(schedule, ScheduleDetailVO.class);

            // 补充详细关联数据
            enhanceScheduleDetailData(scheduleVO);

            log.info("获取课表详情成功，课表ID：{}", scheduleId);
            return ResponseDTO.ok(scheduleVO);

        } catch (Exception e) {
            log.error("获取课表详情失败，课表ID：{}", scheduleId, e);
            return ResponseDTO.userErrorParam("获取课表详情失败");
        }
    }

    /**
     * 根据预约创建课表记录
     * 在预约确认时自动调用
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> createScheduleFromBooking(BookingEntity booking) {
        try {
            LessonScheduleEntity schedule = new LessonScheduleEntity();

            // 生成课单号
            schedule.setScheduleNo(generateScheduleNo());
            schedule.setBookingId(booking.getBookingId());
            schedule.setClubId(booking.getClubId());
            schedule.setMemberId(booking.getMemberId());
            schedule.setCoachId(booking.getCoachId());
            schedule.setHorseId(booking.getHorseId());

            // 时间设置
            schedule.setLessonDate(booking.getStartTime().toLocalDate());
            schedule.setStartTime(booking.getStartTime());
            schedule.setEndTime(booking.getEndTime());

            // 状态设置：根据预约状态映射课表状态
            schedule.setLessonStatus(mapBookingStatusToLessonStatus(booking.getBookingStatus()));

            schedule.setNotificationSent(0);
            schedule.setReminderSent(0);
            schedule.setCreateBy("system");
            schedule.setCreateTime(LocalDateTime.now());
            schedule.setUpdateBy("system");
            schedule.setUpdateTime(LocalDateTime.now());

            lessonScheduleDao.insert(schedule);

            log.info("根据预约{}创建课表成功，课单号：{}", booking.getBookingId(), schedule.getScheduleNo());
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("根据预约{}创建课表失败", booking.getBookingId(), e);
            return ResponseDTO.userErrorParam("创建课表失败：" + e.getMessage());
        }
    }

    /**
     * 根据预约状态更新课表状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> updateScheduleStatusByBooking(Long bookingId, Integer bookingStatus) {
        try {
            LambdaQueryWrapper<LessonScheduleEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LessonScheduleEntity::getBookingId, bookingId);

            LessonScheduleEntity schedule = lessonScheduleDao.selectOne(queryWrapper);
            if (schedule != null) {
                Integer newLessonStatus = mapBookingStatusToLessonStatus(bookingStatus);
                schedule.setLessonStatus(newLessonStatus);
                schedule.setUpdateTime(LocalDateTime.now());

                lessonScheduleDao.updateById(schedule);
                log.info("更新课表状态成功，预约ID：{}，新状态：{}", bookingId, newLessonStatus);
            }

            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("更新课表状态失败，预约ID：{}", bookingId, e);
            return ResponseDTO.userErrorParam("更新课表状态失败");
        }
    }

    /**
     * 根据预约ID删除课表
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> deleteScheduleByBooking(Long bookingId) {
        try {
            LambdaQueryWrapper<LessonScheduleEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LessonScheduleEntity::getBookingId, bookingId);

            lessonScheduleDao.delete(queryWrapper);
            log.info("删除课表成功，预约ID：{}", bookingId);
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("删除课表失败，预约ID：{}", bookingId, e);
            return ResponseDTO.userErrorParam("删除课表失败");
        }
    }

    /**
     * 批量更新课表时间
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> batchUpdateScheduleTime(ValidateList<ScheduleTimeUpdateForm> updateForms) {
        try {
            if (updateForms == null || updateForms.isEmpty()) {
                return ResponseDTO.userErrorParam("更新数据不能为空");
            }

            for (ScheduleTimeUpdateForm form : updateForms) {
                LessonScheduleEntity schedule = lessonScheduleDao.selectById(form.getScheduleId());
                if (schedule == null) {
                    log.warn("课表不存在，ID：{}", form.getScheduleId());
                    continue;
                }

                // 更新时间
                schedule.setStartTime(form.getStartTime());
                schedule.setEndTime(form.getEndTime());
                schedule.setLessonDate(form.getStartTime().toLocalDate());
                schedule.setUpdateTime(LocalDateTime.now());

                lessonScheduleDao.updateById(schedule);
            }

            log.info("批量更新课表时间成功，共{}条记录", updateForms.size());
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("批量更新课表时间失败", e);
            return ResponseDTO.userErrorParam("批量更新课表时间失败：" + e.getMessage());
        }
    }

    /**
     * 检查时间冲突
     */
    public ResponseDTO<ConflictCheckVO> checkConflict(ConflictCheckForm form) {
        try {
            ConflictCheckVO result = new ConflictCheckVO();
            result.setHasConflict(false);
            result.setConflicts(new ArrayList<>());
            result.setSuggestions(new ArrayList<>());

            // 检查教练冲突
            List<ConflictCheckVO.ConflictDetailVO> coachConflicts = checkCoachConflict(form);
            if (!coachConflicts.isEmpty()) {
                result.setHasConflict(true);
                result.setConflictType("COACH");
                result.getConflicts().addAll(coachConflicts);
                result.getSuggestions().add("建议更换教练或调整时间");
            }

            // 检查马匹冲突（如果指定了马匹）
            if (form.getHorseId() != null && form.getHorseId() > 0) {
                List<ConflictCheckVO.ConflictDetailVO> horseConflicts = checkHorseConflict(form);
                if (!horseConflicts.isEmpty()) {
                    result.setHasConflict(true);
                    if (result.getConflictType() == null) {
                        result.setConflictType("HORSE");
                    } else {
                        result.setConflictType("MULTIPLE");
                    }
                    result.getConflicts().addAll(horseConflicts);
                    result.getSuggestions().add("建议更换马匹或调整时间");
                }
            }

            // 检查会员冲突（如果指定了会员）
            if (form.getMemberId() != null && form.getMemberId() > 0) {
                List<ConflictCheckVO.ConflictDetailVO> memberConflicts = checkMemberConflict(form);
                if (!memberConflicts.isEmpty()) {
                    result.setHasConflict(true);
                    if (result.getConflictType() == null) {
                        result.setConflictType("MEMBER");
                    } else {
                        result.setConflictType("MULTIPLE");
                    }
                    result.getConflicts().addAll(memberConflicts);
                    result.getSuggestions().add("该会员在此时间段已有其他课程安排");
                }
            }

            log.info("冲突检测完成，是否有冲突：{}", result.getHasConflict());
            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("冲突检测失败", e);
            return ResponseDTO.userErrorParam("冲突检测失败：" + e.getMessage());
        }
    }

    // ========================================
    // 私有辅助方法
    // ========================================

    /**
     * 生成课单号
     */
    private String generateScheduleNo() {
        return "SCH" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                String.format("%03d", (int) (Math.random() * 1000));
    }

    /**
     * 预约状态映射到课表状态
     * 预约状态: 1-待确认 2-已确认 3-进行中 4-已完成 5-已取消
     * 课表状态: 1-待上课 2-进行中 3-已完成 4-已取消
     */
    private Integer mapBookingStatusToLessonStatus(Integer bookingStatus) {
        switch (bookingStatus) {
            case 1: // 待确认
            case 2: // 已确认
                return 1; // 待上课
            case 3: // 进行中
                return 2; // 进行中
            case 4: // 已完成
                return 3; // 已完成
            case 5: // 已取消
                return 4; // 已取消
            default:
                return 1; // 默认待上课
        }
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<LessonScheduleEntity> buildQueryWrapper(ScheduleQueryForm queryForm) {
        LambdaQueryWrapper<LessonScheduleEntity> queryWrapper = new LambdaQueryWrapper<>();

        if (SmartStringUtil.isNotBlank(queryForm.getKeywords())) {
            queryWrapper.like(LessonScheduleEntity::getScheduleNo, queryForm.getKeywords());
        }
        if (queryForm.getLessonStatus() != null) {
            queryWrapper.eq(LessonScheduleEntity::getLessonStatus, queryForm.getLessonStatus());
        }
        if (queryForm.getCoachId() != null) {
            queryWrapper.eq(LessonScheduleEntity::getCoachId, queryForm.getCoachId());
        }
        if (queryForm.getClubId() != null) {
            queryWrapper.eq(LessonScheduleEntity::getClubId, queryForm.getClubId());
        }
        if (queryForm.getTimeRangeStart() != null) {
            queryWrapper.ge(LessonScheduleEntity::getStartTime, queryForm.getTimeRangeStart());
        }
        if (queryForm.getTimeRangeEnd() != null) {
            queryWrapper.le(LessonScheduleEntity::getStartTime, queryForm.getTimeRangeEnd());
        }
        if (SmartStringUtil.isNotBlank(queryForm.getLessonDateStart())) {
            queryWrapper.ge(LessonScheduleEntity::getLessonDate, LocalDate.parse(queryForm.getLessonDateStart()));
        }
        if (SmartStringUtil.isNotBlank(queryForm.getLessonDateEnd())) {
            queryWrapper.le(LessonScheduleEntity::getLessonDate, LocalDate.parse(queryForm.getLessonDateEnd()));
        }

        queryWrapper.orderByDesc(LessonScheduleEntity::getStartTime);
        return queryWrapper;
    }

    /**
     * 补充课表关联数据
     */
    private void enhanceScheduleData(List<ScheduleListVO> scheduleList) {
        if (scheduleList.isEmpty()) {
            return;
        }

        // 收集ID
        List<Long> memberIds = scheduleList.stream().map(ScheduleListVO::getMemberId).distinct().collect(Collectors.toList());
        List<Long> coachIds = scheduleList.stream().map(ScheduleListVO::getCoachId).distinct().collect(Collectors.toList());
        List<Long> horseIds = scheduleList.stream().map(ScheduleListVO::getHorseId).filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        List<Long> bookingIds = scheduleList.stream().map(ScheduleListVO::getBookingId).filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());

        // 批量查询关联数据
        Map<Long, MemberEntity> memberMap = memberDao.selectBatchIds(memberIds).stream()
                .collect(Collectors.toMap(MemberEntity::getMemberId, m -> m));
        Map<Long, CoachEntity> coachMap = coachDao.selectBatchIds(coachIds).stream()
                .collect(Collectors.toMap(CoachEntity::getCoachId, c -> c));
        Map<Long, HorseEntity> horseMap = horseIds.isEmpty() ? Map.of() :
                horseDao.selectBatchIds(horseIds).stream()
                        .collect(Collectors.toMap(HorseEntity::getHorseId, h -> h));

        // 查询预约信息以获取商品ID
        Map<Long, BookingEntity> bookingMap = bookingIds.isEmpty() ? Map.of() :
                bookingDao.selectBatchIds(bookingIds).stream()
                        .collect(Collectors.toMap(BookingEntity::getBookingId, b -> b));

        // 收集商品ID并查询商品信息
        List<Long> productIds = bookingMap.values().stream()
                .map(BookingEntity::getProductId)
                .filter(productId -> productId != null && productId > 0)
                .distinct().collect(Collectors.toList());
        Map<Long, ProductEntity> productMap = productIds.isEmpty() ? Map.of() :
                productDao.selectBatchIds(productIds).stream()
                        .collect(Collectors.toMap(ProductEntity::getProductId, p -> p));

        // 获取教练关联的员工ID并查询员工信息
        List<Long> employeeIds = coachMap.values().stream()
                .map(CoachEntity::getUserId)
                .filter(userId -> userId != null && userId > 0)
                .distinct().collect(Collectors.toList());
        Map<Long, EmployeeEntity> employeeMap = employeeIds.isEmpty() ? Map.of() :
                employeeDao.selectBatchIds(employeeIds).stream()
                        .collect(Collectors.toMap(EmployeeEntity::getEmployeeId, e -> e));

        // 补充数据
        for (ScheduleListVO schedule : scheduleList) {
            // 会员信息
            MemberEntity member = memberMap.get(schedule.getMemberId());
            if (member != null) {
                schedule.setMemberName(member.getActualName());
                schedule.setMemberPhone(member.getPhone());
            }

            // 教练信息
            CoachEntity coach = coachMap.get(schedule.getCoachId());
            if (coach != null) {
                // 获取教练真实姓名
                if (coach.getUserId() != null && coach.getUserId() > 0) {
                    log.debug("列表查询：教练{}的员工信息，userId: {}", coach.getCoachNo(), coach.getUserId());
                    EmployeeEntity employee = employeeMap.get(coach.getUserId());
                    if (employee != null && SmartStringUtil.isNotBlank(employee.getActualName())) {
                        log.debug("列表查询：找到员工信息: {}", employee.getActualName());
                        schedule.setCoachName(employee.getActualName());
                    } else {
                        log.warn("列表查询：教练{}关联的员工不存在或姓名为空，userId: {}", coach.getCoachNo(), coach.getUserId());
                        schedule.setCoachName("教练" + coach.getCoachNo()); // 如果没有关联员工，使用教练编号
                    }
                } else {
                    log.warn("列表查询：教练{}没有关联员工，userId: {}", coach.getCoachNo(), coach.getUserId());
                    schedule.setCoachName("教练" + coach.getCoachNo()); // 如果没有关联员工，使用教练编号
                }
            }

            // 马匹信息
            if (schedule.getHorseId() != null && schedule.getHorseId() > 0) {
                HorseEntity horse = horseMap.get(schedule.getHorseId());
                if (horse != null) {
                    schedule.setHorseName(horse.getHorseName());
                }
            }

            // 商品信息
            if (schedule.getBookingId() != null && schedule.getBookingId() > 0) {
                BookingEntity booking = bookingMap.get(schedule.getBookingId());
                if (booking != null && booking.getProductId() != null && booking.getProductId() > 0) {
                    ProductEntity product = productMap.get(booking.getProductId());
                    if (product != null) {
                        schedule.setProductName(product.getProductName());

                        // 组合类型和子类型信息
                        String mainType = getProductTypeNameFromEntity(product);
                        String fullType = mainType;
                        if (SmartStringUtil.isNotBlank(product.getSubType())) {
                            fullType = mainType + "-" + product.getSubType();
                        }
                        schedule.setProductType(product.getProductType());
                    }
                }
            }

            // 状态名称
            schedule.setLessonStatusName(getLessonStatusName(schedule.getLessonStatus()));

            // 计算课程时长
            if (schedule.getStartTime() != null && schedule.getEndTime() != null) {
                long minutes = java.time.Duration.between(schedule.getStartTime(), schedule.getEndTime()).toMinutes();
                schedule.setDuration((int) minutes);
            }
        }
    }

    /**
     * 补充课表详情关联数据
     */
    private void enhanceScheduleDetailData(ScheduleDetailVO schedule) {
        // 会员信息
        if (schedule.getMemberId() != null) {
            MemberEntity member = memberDao.selectById(schedule.getMemberId());
            if (member != null) {
                schedule.setMemberName(member.getActualName());
                schedule.setMemberPhone(member.getPhone());
                schedule.setMemberGender(member.getGender() != null ? (member.getGender() == 1 ? "男" : "女") : null);
                schedule.setMemberLevel(member.getDefaultCourseLevel());

                // 获取默认教练姓名
                if (member.getDefaultCoachId() != null && member.getDefaultCoachId() > 0) {
                    CoachEntity defaultCoach = coachDao.selectById(member.getDefaultCoachId());
                    if (defaultCoach != null && defaultCoach.getUserId() != null && defaultCoach.getUserId() > 0) {
                        EmployeeEntity defaultCoachEmployee = employeeDao.selectById(defaultCoach.getUserId());
                        if (defaultCoachEmployee != null && SmartStringUtil.isNotBlank(defaultCoachEmployee.getActualName())) {
                            schedule.setDefaultCoachName(defaultCoachEmployee.getActualName());
                        } else {
                            schedule.setDefaultCoachName("教练" + defaultCoach.getCoachNo());
                        }
                    }
                }

                // 解析JSON格式的会员扩展信息
                if (SmartStringUtil.isNotBlank(member.getProfileData())) {
                    try {
                        JsonNode profileJson = objectMapper.readTree(member.getProfileData());
                        schedule.setMemberHeight(profileJson.path("height").asText(null));
                        schedule.setMemberWeight(profileJson.path("weight").asText(null));
                        schedule.setRidingExperience(profileJson.path("ridingExperience").asText(null));

                        // 构建可读的备注信息
                        StringBuilder remarkBuilder = new StringBuilder();
                        if (profileJson.has("level")) {
                            remarkBuilder.append("骑行水平: ").append(profileJson.get("level").asText()).append("; ");
                        }
                        if (profileJson.has("interests") && profileJson.get("interests").isArray()) {
                            remarkBuilder.append("兴趣爱好: ");
                            for (JsonNode interest : profileJson.get("interests")) {
                                remarkBuilder.append(interest.asText()).append(" ");
                            }
                            remarkBuilder.append("; ");
                        }
                        schedule.setMemberRemark(remarkBuilder.toString());
                    } catch (Exception e) {
                        log.warn("解析会员扩展信息失败: {}", e.getMessage());
                        schedule.setMemberRemark(member.getProfileData());
                    }
                } else {
                    schedule.setMemberRemark("");
                }
            }
        }

        // 教练信息
        if (schedule.getCoachId() != null) {
            CoachEntity coach = coachDao.selectById(schedule.getCoachId());
            if (coach != null) {
                schedule.setCoachNo(coach.getCoachNo());
                schedule.setCoachAvatar(coach.getAvatarUrl());
                schedule.setCoachLevel(coach.getCoachLevel());
                schedule.setCoachSpecialties(coach.getSpecialties());
                schedule.setCoachIntroduction(coach.getIntroduction());

                // 获取教练真实姓名 - 修复映射问题
                if (coach.getUserId() != null && coach.getUserId() > 0) {
                    log.debug("查询教练{}的员工信息，userId: {}", coach.getCoachNo(), coach.getUserId());
                    EmployeeEntity employee = employeeDao.selectById(coach.getUserId());
                    if (employee != null && SmartStringUtil.isNotBlank(employee.getActualName())) {
                        log.debug("找到员工信息: {}", employee.getActualName());
                        schedule.setCoachName(employee.getActualName());
                        schedule.setCoachPhone(employee.getPhone());
                    } else {
                        log.warn("教练{}关联的员工不存在或姓名为空，userId: {}", coach.getCoachNo(), coach.getUserId());
                        // 如果没有找到员工信息，使用教练编号作为备选
                        schedule.setCoachName("教练" + coach.getCoachNo());
                    }
                } else {
                    log.warn("教练{}没有关联员工，userId: {}", coach.getCoachNo(), coach.getUserId());
                    schedule.setCoachName("教练" + coach.getCoachNo());
                }
            }
        }

        // 马匹信息
        if (schedule.getHorseId() != null && schedule.getHorseId() > 0) {
            HorseEntity horse = horseDao.selectById(schedule.getHorseId());
            if (horse != null) {
                schedule.setHorseName(horse.getHorseName());
                schedule.setHorseNo(horse.getHorseCode());
                schedule.setHorseBreed(horse.getBreed());
                schedule.setHorseGender(horse.getGender() != null ? (horse.getGender() == 1 ? "雄性" : "雌性") : null);
                schedule.setHorseAge(horse.getBirthDate() != null ?
                    java.time.Period.between(horse.getBirthDate().toLocalDate(), java.time.LocalDate.now()).getYears() : null);
                schedule.setHorseColor(horse.getColor());
                // 修复马匹尺寸显示 - 避免0值显示
                schedule.setHorseHeight(horse.getHeight() != null && horse.getHeight() > 0 ?
                    horse.getHeight().toString() + "cm" : "未录入");
                schedule.setHorseWeight(horse.getWeight() != null && horse.getWeight() > 0 ?
                    horse.getWeight().toString() + "kg" : "未录入");
                schedule.setHorseHealthStatus(getHorseHealthStatusName(horse.getHealthStatus()));
                // 注意：Horse实体中没有character字段，这可能在horseData的JSON中
            }
        }

        // 预约和商品信息
        if (schedule.getBookingId() != null && schedule.getBookingId() > 0) {
            BookingEntity booking = bookingDao.selectById(schedule.getBookingId());
            if (booking != null) {
                schedule.setBookingStatus(booking.getBookingStatus());
                schedule.setBookingStatusName(getBookingStatusName(booking.getBookingStatus()));
                schedule.setActualCoachFee(booking.getActualCoachFee());
                schedule.setActualHorseFee(booking.getActualHorseFee());
                schedule.setArrivalTime(booking.getArrivalTime());
                schedule.setCompletionTime(booking.getCompletionTime());
                schedule.setCancelReason(booking.getCancelReason());

                // 商品信息
                if (booking.getProductId() != null && booking.getProductId() > 0) {
                    ProductEntity product = productDao.selectById(booking.getProductId());
                    if (product != null) {
                        log.warn("商品信息 - ID: {}, 名称: {}, 原始类型: {}, 子类型: {}",
                            product.getProductId(), product.getProductName(),
                            product.getProductType(), product.getSubType());

                        schedule.setProductId(product.getProductId());
                        schedule.setProductName(product.getProductName());

                        // 获取完整的商品类型信息（包含子类型）
                        String fullType = getProductTypeNameFromEntity(product);
                        log.warn("商品类型转换 - 原始: {}, 最终类型: {}",
                            product.getProductType(), fullType);
                        schedule.setProductType(fullType);

                        // 注意：Product实体中没有price、lessonType、difficultyLevel字段
                        // 这些可能需要从产品配置表或扩展表中获取
                    }
                }
            }
        }

        // 状态名称
        schedule.setLessonStatusName(getLessonStatusName(schedule.getLessonStatus()));

        // 计算课程时长
        if (schedule.getStartTime() != null && schedule.getEndTime() != null) {
            long minutes = java.time.Duration.between(schedule.getStartTime(), schedule.getEndTime()).toMinutes();
            schedule.setDuration((int) minutes);
        }
    }

    /**
     * 获取预约状态名称
     */
    private String getBookingStatusName(Integer bookingStatus) {
        if (bookingStatus == null) return "未知状态";
        switch (bookingStatus) {
            case 1: return "待确认";
            case 2: return "已确认";
            case 3: return "进行中";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知状态";
        }
    }

    /**
     * 获取马匹健康状态名称
     */
    private String getHorseHealthStatusName(Integer healthStatus) {
        if (healthStatus == null) return "未知";
        switch (healthStatus) {
            case 1: return "健康";
            case 2: return "轻微不适";
            case 3: return "需要休息";
            case 4: return "康复中";
            case 5: return "停止工作";
            default: return "未知";
        }
    }

    /**
     * 获取商品类型名称
     */
    private String getProductTypeName(Integer productType) {
        if (productType == null) return "未知类型";
        switch (productType) {
            case 1: return "课程";
            case 2: return "课时包";
            case 3: return "活动";
            default: return "未知类型";
        }
    }

    /**
     * 从ProductEntity获取商品类型名称（支持字符串和数字类型）
     */
    private String getProductTypeNameFromEntity(ProductEntity product) {
        if (product.getProductType() == null) {
            return "未知类型";
        }

        // 获取主类型名称 - 安全处理类型转换
        String mainType;
        Object productTypeObj = product.getProductType();

        if (productTypeObj instanceof Integer) {
            // 如果是整数类型，使用标准映射
            mainType = getProductTypeName((Integer) productTypeObj);
        } else {
            // 如果是字符串或其他类型，进行字符串标准化
            String typeStr = String.valueOf(productTypeObj);
            mainType = normalizeProductTypeName(typeStr);
        }

        // 如果是课程类型，尝试获取子类型（单人课/多人课）
        if ("课程".equals(mainType) || mainType.contains("课程")) {
            String subType = getCourseSubType(product.getProductId());
            if (SmartStringUtil.isNotBlank(subType)) {
                return mainType + "-" + subType;
            }
        }

        return mainType;
    }

    /**
     * 获取课程子类型（单人课/多人课）
     */
    private String getCourseSubType(Long productId) {
        try {
            LambdaQueryWrapper<ProductCourseEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductCourseEntity::getProductId, productId);
            ProductCourseEntity courseEntity = productCourseDao.selectOne(wrapper);

            if (courseEntity != null && courseEntity.getClassType() != null) {
                switch (courseEntity.getClassType()) {
                    case 1:
                        return "单人课";
                    case 2:
                        return "多人课";
                    default:
                        return null;
                }
            }
        } catch (Exception e) {
            log.warn("获取课程子类型失败，productId: {}", productId, e);
        }
        return null;
    }

    /**
     * 标准化商品类型名称（处理数据库中的遗留字符串数据）
     */
    private String normalizeProductTypeName(String typeStr) {
        if (SmartStringUtil.isBlank(typeStr)) {
            return "未知类型";
        }

        String lowerType = typeStr.toLowerCase().trim();

        // 映射遗留的字符串类型到标准类型
        if (lowerType.contains("体验") || lowerType.contains("私教") || lowerType.contains("单人") || lowerType.contains("课程")) {
            return "课程";
        }
        if (lowerType.contains("课时包") || lowerType.contains("package")) {
            return "课时包";
        }
        if (lowerType.contains("活动") || lowerType.contains("activity")) {
            return "活动";
        }
        if (lowerType.contains("多人") || lowerType.contains("团体")) {
            return "课程";
        }

        // 如果已经是标准名称，直接返回
        if (lowerType.equals("课程") || lowerType.equals("课时包") || lowerType.equals("活动")) {
            return typeStr;
        }

        return typeStr; // 保持原值
    }

    /**
     * 检查教练时间冲突
     */
    private List<ConflictCheckVO.ConflictDetailVO> checkCoachConflict(ConflictCheckForm form) {
        List<ConflictCheckVO.ConflictDetailVO> conflicts = new ArrayList<>();

        LambdaQueryWrapper<LessonScheduleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LessonScheduleEntity::getCoachId, form.getCoachId())
                   .in(LessonScheduleEntity::getLessonStatus, 1, 2) // 待上课、进行中状态
                   .and(wrapper -> wrapper
                       .le(LessonScheduleEntity::getStartTime, form.getEndTime())
                       .ge(LessonScheduleEntity::getEndTime, form.getStartTime())
                   );

        // 如果是更新操作，排除当前课表
        if (form.getScheduleId() != null) {
            queryWrapper.ne(LessonScheduleEntity::getScheduleId, form.getScheduleId());
        }

        List<LessonScheduleEntity> conflictSchedules = lessonScheduleDao.selectList(queryWrapper);

        for (LessonScheduleEntity schedule : conflictSchedules) {
            ConflictCheckVO.ConflictDetailVO conflict = new ConflictCheckVO.ConflictDetailVO();
            conflict.setConflictScheduleId(schedule.getScheduleId());
            conflict.setConflictScheduleNo(schedule.getScheduleNo());
            conflict.setResourceType("COACH");
            conflict.setResourceName("教练时间冲突");
            conflict.setConflictTimeRange(formatTimeRange(schedule.getStartTime(), schedule.getEndTime()));
            conflict.setDescription("教练在该时间段已有其他课程安排");

            // 获取会员信息
            if (schedule.getMemberId() != null) {
                MemberEntity member = memberDao.selectById(schedule.getMemberId());
                if (member != null) {
                    conflict.setMemberName(member.getActualName());
                }
            }

            conflicts.add(conflict);
        }

        return conflicts;
    }

    /**
     * 检查马匹时间冲突
     */
    private List<ConflictCheckVO.ConflictDetailVO> checkHorseConflict(ConflictCheckForm form) {
        List<ConflictCheckVO.ConflictDetailVO> conflicts = new ArrayList<>();

        LambdaQueryWrapper<LessonScheduleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LessonScheduleEntity::getHorseId, form.getHorseId())
                   .in(LessonScheduleEntity::getLessonStatus, 1, 2) // 待上课、进行中状态
                   .and(wrapper -> wrapper
                       .le(LessonScheduleEntity::getStartTime, form.getEndTime())
                       .ge(LessonScheduleEntity::getEndTime, form.getStartTime())
                   );

        // 如果是更新操作，排除当前课表
        if (form.getScheduleId() != null) {
            queryWrapper.ne(LessonScheduleEntity::getScheduleId, form.getScheduleId());
        }

        List<LessonScheduleEntity> conflictSchedules = lessonScheduleDao.selectList(queryWrapper);

        for (LessonScheduleEntity schedule : conflictSchedules) {
            ConflictCheckVO.ConflictDetailVO conflict = new ConflictCheckVO.ConflictDetailVO();
            conflict.setConflictScheduleId(schedule.getScheduleId());
            conflict.setConflictScheduleNo(schedule.getScheduleNo());
            conflict.setResourceType("HORSE");
            conflict.setResourceName("马匹时间冲突");
            conflict.setConflictTimeRange(formatTimeRange(schedule.getStartTime(), schedule.getEndTime()));
            conflict.setDescription("马匹在该时间段已被其他课程占用");

            // 获取会员信息
            if (schedule.getMemberId() != null) {
                MemberEntity member = memberDao.selectById(schedule.getMemberId());
                if (member != null) {
                    conflict.setMemberName(member.getActualName());
                }
            }

            conflicts.add(conflict);
        }

        return conflicts;
    }

    /**
     * 检查会员时间冲突
     */
    private List<ConflictCheckVO.ConflictDetailVO> checkMemberConflict(ConflictCheckForm form) {
        List<ConflictCheckVO.ConflictDetailVO> conflicts = new ArrayList<>();

        LambdaQueryWrapper<LessonScheduleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LessonScheduleEntity::getMemberId, form.getMemberId())
                   .in(LessonScheduleEntity::getLessonStatus, 1, 2) // 待上课、进行中状态
                   .and(wrapper -> wrapper
                       .le(LessonScheduleEntity::getStartTime, form.getEndTime())
                       .ge(LessonScheduleEntity::getEndTime, form.getStartTime())
                   );

        // 如果是更新操作，排除当前课表
        if (form.getScheduleId() != null) {
            queryWrapper.ne(LessonScheduleEntity::getScheduleId, form.getScheduleId());
        }

        List<LessonScheduleEntity> conflictSchedules = lessonScheduleDao.selectList(queryWrapper);

        for (LessonScheduleEntity schedule : conflictSchedules) {
            ConflictCheckVO.ConflictDetailVO conflict = new ConflictCheckVO.ConflictDetailVO();
            conflict.setConflictScheduleId(schedule.getScheduleId());
            conflict.setConflictScheduleNo(schedule.getScheduleNo());
            conflict.setResourceType("MEMBER");
            conflict.setResourceName("会员时间冲突");
            conflict.setConflictTimeRange(formatTimeRange(schedule.getStartTime(), schedule.getEndTime()));
            conflict.setDescription("会员在该时间段已有其他课程安排");

            // 获取会员信息
            if (schedule.getMemberId() != null) {
                MemberEntity member = memberDao.selectById(schedule.getMemberId());
                if (member != null) {
                    conflict.setMemberName(member.getActualName());
                }
            }

            conflicts.add(conflict);
        }

        return conflicts;
    }

    /**
     * 格式化时间范围
     */
    private String formatTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return "-";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        return startTime.format(formatter) + " - " + endTime.format(formatter);
    }

    /**
     * 课程表综合查询 (订单+预约+课程)
     */
    public ResponseDTO<PageResult<ScheduleCombinedVO>> queryCombinedSchedule(ScheduleCombinedQueryForm queryForm) {
        try {
            // 1. 查询订单数据
            Page<OrderEntity> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
            LambdaQueryWrapper<OrderEntity> queryWrapper = buildCombinedQueryWrapper(queryForm);

            IPage<OrderEntity> pageResult = orderDao.selectPage(page, queryWrapper);
            List<OrderEntity> orders = pageResult.getRecords();

            if (orders.isEmpty()) {
                PageResult<ScheduleCombinedVO> result = new PageResult<>();
                result.setList(new ArrayList<>());
                result.setTotal(pageResult.getTotal());
                result.setPageNum(pageResult.getCurrent());
                result.setPageSize(pageResult.getSize());
                result.setPages(pageResult.getPages());
                result.setEmptyFlag(true);
                return ResponseDTO.ok(result);
            }

            // 2. 聚合数据
            List<ScheduleCombinedVO> combinedList = buildCombinedData(orders);

            PageResult<ScheduleCombinedVO> result = new PageResult<>();
            result.setList(combinedList);
            result.setTotal(pageResult.getTotal());
            result.setPageNum(pageResult.getCurrent());
            result.setPageSize(pageResult.getSize());
            result.setPages(pageResult.getPages());
            result.setEmptyFlag(combinedList.isEmpty());
            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("查询课程表综合数据失败", e);
            return ResponseDTO.userErrorParam("查询失败");
        }
    }

    /**
     * 构建综合查询条件
     */
    private LambdaQueryWrapper<OrderEntity> buildCombinedQueryWrapper(ScheduleCombinedQueryForm queryForm) {
        LambdaQueryWrapper<OrderEntity> wrapper = new LambdaQueryWrapper<>();

        // 基础条件
        if (queryForm.getClubId() != null) {
            wrapper.eq(OrderEntity::getClubId, queryForm.getClubId());
        }

        // 商品类型
        if (queryForm.getProductType() != null) {
            wrapper.eq(OrderEntity::getProductType, queryForm.getProductType());
        }

        // 订单状态
        if (queryForm.getOrderStatus() != null) {
            wrapper.eq(OrderEntity::getOrderStatus, queryForm.getOrderStatus());
        }

        // 购买会员
        if (queryForm.getBuyerMemberId() != null) {
            wrapper.eq(OrderEntity::getMemberId, queryForm.getBuyerMemberId());
        }

        // 关键词搜索
        if (SmartStringUtil.isNotBlank(queryForm.getKeywords())) {
            wrapper.and(w -> w.like(OrderEntity::getOrderNo, queryForm.getKeywords())
                           .or().like(OrderEntity::getProductName, queryForm.getKeywords()));
        }

        // 时间范围
        if (queryForm.getCreateTimeStart() != null) {
            wrapper.ge(OrderEntity::getCreateTime, queryForm.getCreateTimeStart());
        }
        if (queryForm.getCreateTimeEnd() != null) {
            wrapper.le(OrderEntity::getCreateTime, queryForm.getCreateTimeEnd());
        }

        // 默认排序
        wrapper.orderByDesc(OrderEntity::getCreateTime);

        return wrapper;
    }

    /**
     * 构建综合数据
     */
    private List<ScheduleCombinedVO> buildCombinedData(List<OrderEntity> orders) {
        List<ScheduleCombinedVO> result = new ArrayList<>();

        // 获取订单IDs
        List<Long> orderIds = orders.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());

        // 批量查询关联数据
        List<BookingEntity> allBookings = bookingDao.selectByOrderIds(orderIds);
        List<PackageBalanceEntity> allBalances = packageBalanceDao.selectByOrderIds(orderIds);
        List<LessonScheduleEntity> allSchedules = getAllSchedulesByBookings(allBookings);

        // 构建映射关系
        Map<Long, List<BookingEntity>> bookingsByOrderId = allBookings.stream()
            .collect(Collectors.groupingBy(BookingEntity::getOrderId));
        Map<Long, PackageBalanceEntity> balancesByOrderId = allBalances.stream()
            .collect(Collectors.toMap(PackageBalanceEntity::getOrderId, balance -> balance));
        Map<Long, LessonScheduleEntity> schedulesByBookingId = allSchedules.stream()
            .collect(Collectors.toMap(LessonScheduleEntity::getBookingId, schedule -> schedule));

        // 批量获取关联的基础数据
        Map<Long, MemberEntity> memberMap = getMemberMap(orders, allBookings);
        Map<Long, CoachEntity> coachMap = getCoachMap(orders, allBookings);
        Map<Long, HorseEntity> horseMap = getHorseMap(allBookings);

        // 构建结果数据
        for (OrderEntity order : orders) {
            ScheduleCombinedVO vo = new ScheduleCombinedVO();

            // 订单基础信息
            SmartBeanUtil.copyProperties(order, vo);
            vo.setOrderStatusName(getOrderStatusName(order.getOrderStatus()));
            vo.setProductTypeName(getProductTypeName(order.getProductType()));

            // 购买会员信息
            MemberEntity buyer = memberMap.get(order.getMemberId());
            if (buyer != null) {
                vo.setBuyerMemberName(buyer.getActualName());
            }

            // 默认教练信息
            if (order.getCoachId() != null && order.getCoachId() > 0) {
                CoachEntity coach = coachMap.get(order.getCoachId());
                if (coach != null) {
                    vo.setDefaultCoachName(coach.getActualName());
                }
            }

            // 课时包余额信息
            PackageBalanceEntity balance = balancesByOrderId.get(order.getOrderId());
            if (balance != null) {
                vo.setTotalCount(balance.getTotalCount());
                vo.setUsedCount(balance.getUsedCount());
                vo.setRemainingCount(balance.getRemainingCount());
                vo.setPackageStatus(balance.getStatus());
                vo.setExpireDate(balance.getExpireDate());
            }

            // 预约记录列表
            List<BookingEntity> bookings = bookingsByOrderId.get(order.getOrderId());
            if (bookings != null && !bookings.isEmpty()) {
                List<BookingRecordVO> bookingRecords = buildBookingRecords(bookings, schedulesByBookingId,
                    memberMap, coachMap, horseMap);
                vo.setBookings(bookingRecords);
            } else {
                vo.setBookings(new ArrayList<>());
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 构建预约记录列表
     */
    private List<BookingRecordVO> buildBookingRecords(List<BookingEntity> bookings,
            Map<Long, LessonScheduleEntity> schedulesByBookingId,
            Map<Long, MemberEntity> memberMap,
            Map<Long, CoachEntity> coachMap,
            Map<Long, HorseEntity> horseMap) {

        List<BookingRecordVO> records = new ArrayList<>();

        for (BookingEntity booking : bookings) {
            BookingRecordVO record = new BookingRecordVO();
            SmartBeanUtil.copyProperties(booking, record);

            // 消费会员信息
            Long consumerMemberId = booking.getConsumerMemberId() != null ?
                booking.getConsumerMemberId() : booking.getMemberId();
            MemberEntity consumer = memberMap.get(consumerMemberId);
            if (consumer != null) {
                record.setConsumerMemberName(consumer.getActualName());
            }

            // 教练信息
            CoachEntity coach = coachMap.get(booking.getCoachId());
            if (coach != null) {
                record.setCoachName(coach.getActualName());
            }

            // 马匹信息
            HorseEntity horse = horseMap.get(booking.getHorseId());
            if (horse != null) {
                record.setHorseName(horse.getHorseName());
            }

            // 状态名称
            record.setBookingStatusName(getBookingStatusName(booking.getBookingStatus()));

            // 课程信息
            LessonScheduleEntity schedule = schedulesByBookingId.get(booking.getBookingId());
            if (schedule != null) {
                record.setScheduleId(schedule.getScheduleId());
                record.setScheduleNo(schedule.getScheduleNo());
                record.setLessonStatus(schedule.getLessonStatus());
                record.setLessonStatusName(getLessonStatusName(schedule.getLessonStatus()));
            }

            records.add(record);
        }

        // 按预约时间倒序排列
        records.sort((a, b) -> {
            if (a.getStartTime() == null) return 1;
            if (b.getStartTime() == null) return -1;
            return b.getStartTime().compareTo(a.getStartTime());
        });

        return records;
    }

    /**
     * 获取所有相关的课程表记录
     */
    private List<LessonScheduleEntity> getAllSchedulesByBookings(List<BookingEntity> bookings) {
        if (bookings.isEmpty()) return new ArrayList<>();

        List<Long> bookingIds = bookings.stream()
            .map(BookingEntity::getBookingId)
            .collect(Collectors.toList());

        LambdaQueryWrapper<LessonScheduleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LessonScheduleEntity::getBookingId, bookingIds);

        return lessonScheduleDao.selectList(wrapper);
    }

    /**
     * 批量获取会员映射
     */
    private Map<Long, MemberEntity> getMemberMap(List<OrderEntity> orders, List<BookingEntity> bookings) {
        List<Long> memberIds = new ArrayList<>();

        // 购买会员
        orders.stream().map(OrderEntity::getMemberId).forEach(memberIds::add);

        // 消费会员
        bookings.stream()
            .map(booking -> booking.getConsumerMemberId() != null ?
                booking.getConsumerMemberId() : booking.getMemberId())
            .forEach(memberIds::add);

        List<Long> uniqueMemberIds = memberIds.stream().distinct().collect(Collectors.toList());

        if (uniqueMemberIds.isEmpty()) return new java.util.HashMap<>();

        List<MemberEntity> members = memberDao.selectBatchIds(uniqueMemberIds);
        return members.stream().collect(Collectors.toMap(MemberEntity::getMemberId, member -> member));
    }

    /**
     * 批量获取教练映射
     */
    private Map<Long, CoachEntity> getCoachMap(List<OrderEntity> orders, List<BookingEntity> bookings) {
        List<Long> coachIds = new ArrayList<>();

        // 默认教练
        orders.stream()
            .filter(order -> order.getCoachId() != null && order.getCoachId() > 0)
            .map(OrderEntity::getCoachId)
            .forEach(coachIds::add);

        // 预约教练
        bookings.stream()
            .filter(booking -> booking.getCoachId() != null && booking.getCoachId() > 0)
            .map(BookingEntity::getCoachId)
            .forEach(coachIds::add);

        List<Long> uniqueCoachIds = coachIds.stream().distinct().collect(Collectors.toList());

        if (uniqueCoachIds.isEmpty()) return new java.util.HashMap<>();

        List<CoachEntity> coaches = coachDao.selectBatchIds(uniqueCoachIds);
        return coaches.stream().collect(Collectors.toMap(CoachEntity::getCoachId, coach -> coach));
    }

    /**
     * 批量获取马匹映射
     */
    private Map<Long, HorseEntity> getHorseMap(List<BookingEntity> bookings) {
        List<Long> horseIds = bookings.stream()
            .filter(booking -> booking.getHorseId() != null && booking.getHorseId() > 0)
            .map(BookingEntity::getHorseId)
            .distinct()
            .collect(Collectors.toList());

        if (horseIds.isEmpty()) return new java.util.HashMap<>();

        List<HorseEntity> horses = horseDao.selectBatchIds(horseIds);
        return horses.stream().collect(Collectors.toMap(HorseEntity::getHorseId, horse -> horse));
    }

    /**
     * 获取订单状态名称
     */
    private String getOrderStatusName(Integer orderStatus) {
        if (orderStatus == null) return "未知状态";
        switch (orderStatus) {
            case 1: return "待支付";
            case 2: return "已支付";
            case 3: return "已核销";
            case 4: return "已取消";
            case 5: return "已退款";
            default: return "未知状态";
        }
    }

    /**
     * 获取课程状态名称
     */
    private String getLessonStatusName(Integer lessonStatus) {
        if (lessonStatus == null) return "未知状态";
        switch (lessonStatus) {
            case 1: return "待上课";
            case 2: return "进行中";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知状态";
        }
    }
}
