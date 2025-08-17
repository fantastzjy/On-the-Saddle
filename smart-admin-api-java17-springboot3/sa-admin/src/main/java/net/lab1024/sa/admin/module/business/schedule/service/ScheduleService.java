package net.lab1024.sa.admin.module.business.schedule.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseEntity;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.business.schedule.dao.LessonScheduleDao;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.LessonScheduleEntity;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ConflictCheckForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleQueryForm;
import net.lab1024.sa.admin.module.business.schedule.domain.form.ScheduleTimeUpdateForm;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.CoachVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ConflictCheckVO;
import net.lab1024.sa.admin.module.business.schedule.domain.vo.ScheduleListVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private CoachDao coachDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private HorseDao horseDao;

    @Autowired
    private ProductDao productDao;

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
    public ResponseDTO<ScheduleListVO> getScheduleDetail(Long scheduleId) {
        try {
            LessonScheduleEntity schedule = lessonScheduleDao.selectById(scheduleId);
            if (schedule == null) {
                return ResponseDTO.userErrorParam("课表不存在");
            }

            ScheduleListVO scheduleVO = SmartBeanUtil.copy(schedule, ScheduleListVO.class);
            
            // 补充关联数据
            List<ScheduleListVO> scheduleList = List.of(scheduleVO);
            enhanceScheduleData(scheduleList);

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

        // 批量查询关联数据
        Map<Long, MemberEntity> memberMap = memberDao.selectBatchIds(memberIds).stream()
                .collect(Collectors.toMap(MemberEntity::getMemberId, m -> m));
        Map<Long, CoachEntity> coachMap = coachDao.selectBatchIds(coachIds).stream()
                .collect(Collectors.toMap(CoachEntity::getCoachId, c -> c));
        Map<Long, HorseEntity> horseMap = horseIds.isEmpty() ? Map.of() : 
                horseDao.selectBatchIds(horseIds).stream()
                        .collect(Collectors.toMap(HorseEntity::getHorseId, h -> h));

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
                schedule.setCoachName(coach.getCoachNo()); // 这里需要关联用户表获取真实姓名，暂用编号
            }

            // 马匹信息
            if (schedule.getHorseId() != null && schedule.getHorseId() > 0) {
                HorseEntity horse = horseMap.get(schedule.getHorseId());
                if (horse != null) {
                    schedule.setHorseName(horse.getHorseName());
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
     * 获取课表状态名称
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
}