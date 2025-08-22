package net.lab1024.sa.admin.module.business.member.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.vo.ClubInfoVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.CourseLevelInfoVO;
import net.lab1024.sa.admin.util.MemberRequestUtil;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictDataVO;
import net.lab1024.sa.base.module.support.dict.service.DictService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
    private MemberDao memberDao;

    @Resource
    private CoachDao coachDao;

    /**
     * 获取俱乐部详细信息
     */
    public ResponseDTO<ClubInfoVO> getClubInfo() {
        // 获取当前登录会员的俱乐部ID
        Long memberId = MemberRequestUtil.getRequestMemberId();
        if (memberId == null) {
            return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
        }

        // 查询会员信息获取clubId
        MemberEntity member = memberDao.selectById(memberId);
        if (member == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "会员信息不存在");
        }

        if (member.getClubId() == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "会员未关联俱乐部");
        }

        ClubEntity club = clubDao.selectById(member.getClubId());
        if (club == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "俱乐部信息不存在");
        }

        ClubInfoVO clubInfo = SmartBeanUtil.copy(club, ClubInfoVO.class);
        return ResponseDTO.ok(clubInfo);
    }

    /**
     * 获取教练列表
     */
    public ResponseDTO<List<CoachListVO>> getCoachList() {
        try {
            // 1. 获取当前登录会员的俱乐部ID
            Long memberId = MemberRequestUtil.getRequestMemberId();
            if (memberId == null) {
                return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
            }

            MemberEntity member = memberDao.selectById(memberId);
            if (member == null || member.getClubId() == null) {
                return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "会员未关联俱乐部");
            }

            // 2. 查询该俱乐部的所有有效教练
            List<CoachEntity> coaches = coachDao.selectList(
                new LambdaQueryWrapper<CoachEntity>()
                    .eq(CoachEntity::getClubId, member.getClubId())
                    .eq(CoachEntity::getIsValid, 1)
                    .eq(CoachEntity::getIsDelete, 0)
                    .orderBy(true, true, CoachEntity::getSortOrder)
            );

            // 3. 转换为VO对象
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
        vo.setCoachId(coach.getCoachId());
        vo.setActualName(coach.getActualName());
        vo.setGender(coach.getGender());
        vo.setGenderText(getGenderText(coach.getGender()));

        // 头像处理
        vo.setAvatarUrl(StrUtil.isNotBlank(coach.getAvatarUrl()) ?
            coach.getAvatarUrl() : "/default-avatar.png");

        // 课时费处理
        vo.setCoachFee(coach.getCoachFee() != null ? coach.getCoachFee() : BigDecimal.ZERO);

        // 教练介绍处理
        vo.setIntroduction(StrUtil.isNotBlank(coach.getIntroduction()) ?
            coach.getIntroduction() : "暂无介绍");

        // 从业时长计算
        vo.setWorkingYears(calculateWorkingYears(coach.getEntryDate()));

        // 骑手等级标签生成
        vo.setRiderLevelTags(buildRiderLevelTags(coach));

        // 教练等级标签生成
        vo.setCoachLevelTag(buildCoachLevelTag(coach));

        return vo;
    }

    /**
     * 获取性别描述
     */
    private String getGenderText(Integer gender) {
        if (gender == null) return "未知";
        switch (gender) {
            case 1: return "男";
            case 2: return "女";
            default: return "未知";
        }
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
     * 构建骑手等级标签列表
     */
    private List<String> buildRiderLevelTags(CoachEntity coach) {
        List<String> tags = new ArrayList<>();

        // 场地障碍等级
        if (StrUtil.isNotBlank(coach.getRiderLevelShowJumping())) {
            tags.add(coach.getRiderLevelShowJumping() + "场地障碍");
        }

        // 盛装舞步等级
        if (StrUtil.isNotBlank(coach.getRiderLevelDressage())) {
            tags.add(coach.getRiderLevelDressage() + "盛装舞步");
        }

        // 三项赛等级
        if (StrUtil.isNotBlank(coach.getRiderLevelEventing())) {
            tags.add(coach.getRiderLevelEventing() + "三项赛");
        }

        return tags;
    }

    /**
     * 构建教练等级标签
     */
    private String buildCoachLevelTag(CoachEntity coach) {
        if (StrUtil.isNotBlank(coach.getCoachLevel())) {
            return coach.getCoachLevel() + "教练证";
        }
        return null;
    }
}
