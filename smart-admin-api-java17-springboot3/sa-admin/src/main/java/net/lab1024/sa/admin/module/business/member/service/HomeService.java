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
import net.lab1024.sa.admin.module.business.member.domain.vo.UnavailableTimeSlotVO;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.dao.ProductCourseDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductCourseEntity;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    /**
     * 获取俱乐部详细信息
     */
    public ResponseDTO<ClubInfoVO> getClubInfo(Long clubId) {
        if (clubId == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部ID不能为空");
        }

        ClubEntity club = clubDao.selectById(clubId);
        if (club == null || club.getIsDelete() || !club.getIsValid()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部信息不存在");
        }

        ClubInfoVO clubInfo = SmartBeanUtil.copy(club, ClubInfoVO.class);

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
     * 获取教练列表
     */
    public ResponseDTO<List<CoachListVO>> getCoachList(Long clubId) {
        try {
            if (clubId == null) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部ID不能为空");
            }

            // 验证俱乐部是否存在且有效
            ClubEntity club = clubDao.selectById(clubId);
            if (club == null || club.getIsDelete() || !club.getIsValid()) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部不存在");
            }

            // 查询该俱乐部的所有有效教练
            List<CoachEntity> coaches = coachDao.selectList(
                new LambdaQueryWrapper<CoachEntity>()
                    .eq(CoachEntity::getClubId, clubId)
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
    public ResponseDTO<List<CourseListVO>> getCourseList(Long clubId) {
        try {
            if (clubId == null) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "俱乐部ID不能为空");
            }

            // 验证俱乐部是否存在且有效
            ClubEntity club = clubDao.selectById(clubId);
            if (club == null || club.getIsDelete() || !club.getIsValid()) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部不存在");
            }

            // 查询该俱乐部的商品，过滤课程类型为"课程"(productType=1)，子类型为"单人课"
            List<ProductEntity> products = productDao.selectList(
                new LambdaQueryWrapper<ProductEntity>()
                    .eq(ProductEntity::getClubId, clubId)
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
}
