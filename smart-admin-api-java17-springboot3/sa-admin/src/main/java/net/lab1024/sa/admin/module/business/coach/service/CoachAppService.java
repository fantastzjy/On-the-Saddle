package net.lab1024.sa.admin.module.business.coach.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.RequestCoach;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachAppInfoVO;
import net.lab1024.sa.admin.module.business.coach.constant.CoachCertificateConstant;
import net.lab1024.sa.base.module.support.dict.service.DictService;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 教练小程序业务服务
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class CoachAppService {

	@Resource
	private CoachDao coachDao;

	@Resource
	private ClubDao clubDao;

	@Resource
	private DictService dictService;

	/**
	 * 根据登录ID获取教练信息（用于拦截器）
	 */
	public RequestCoach getLoginCoach(String loginId, HttpServletRequest request) {
		if (StrUtil.isBlank(loginId)) {
			return null;
		}

		try {
			// loginId格式: coach_教练ID
			if (!loginId.startsWith("coach_")) {
				return null;
			}

			Long coachId = Long.valueOf(loginId.substring(6));
			CoachEntity coach = coachDao.selectById(coachId);

			if (coach == null || coach.getIsDelete() == 1 || coach.getIsValid() != 1) {
				return null;
			}

			// 构建RequestCoach对象
			RequestCoach requestCoach = new RequestCoach();
			requestCoach.setCoachId(coach.getCoachId());
			requestCoach.setCoachNo(coach.getCoachNo());
			requestCoach.setActualName(coach.getActualName());
			requestCoach.setAvatarUrl(coach.getAvatarUrl());
			requestCoach.setGender(coach.getGender());
			requestCoach.setPhone(coach.getPhone());
			requestCoach.setClubId(coach.getClubId());
			// 使用新的综合等级显示方法
			requestCoach.setCoachLevel(getDisplayCoachLevel(coach));//TODO 逻辑待更新 不是最高的
			requestCoach.setSpecialties(coach.getSpecialties());
			requestCoach.setCoachCertNo(coach.getCoachCertNo());
			requestCoach.setRiderCertNo(coach.getRiderCertNo());

			// 设置扁平化证书字段
			requestCoach.setCoachStarLevel(coach.getCoachStarLevel());
			requestCoach.setCoachShowJumpingLevel(coach.getCoachShowJumpingLevel());
			requestCoach.setCoachDressageLevel(coach.getCoachDressageLevel());
			requestCoach.setCoachEventingLevel(coach.getCoachEventingLevel());
			requestCoach.setRiderShowJumpingLevel(coach.getRiderShowJumpingLevel());
			requestCoach.setRiderDressageLevel(coach.getRiderDressageLevel());
			requestCoach.setRiderEventingLevel(coach.getRiderEventingLevel());

			requestCoach.setUnionId(coach.getUnionId());
			requestCoach.setOpenId(coach.getOpenId());
			requestCoach.setIsValid(coach.getIsValid());
			requestCoach.setIsDelete(coach.getIsDelete());
			requestCoach.setDisabledFlag(coach.getDisabledFlag());

			// 设置请求信息
			requestCoach.setIp(SmartRequestUtil.getIp(request));
			requestCoach.setUserAgent(request.getHeader("User-Agent"));

			return requestCoach;

		} catch (Exception e) {
			log.error("获取教练登录信息失败, loginId: {}", loginId, e);
			return null;
		}
	}

	/**
	 * 获取教练个人信息
	 */
	public ResponseDTO<CoachAppInfoVO> getCoachInfo() {
		RequestCoach requestCoach = (RequestCoach) SmartRequestUtil.getRequestUser();
		if (requestCoach == null) {
			return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
		}

		CoachEntity coach = coachDao.selectById(requestCoach.getCoachId());
		if (coach == null) {
			return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "教练信息不存在");
		}
		CoachAppInfoVO infoVO = new CoachAppInfoVO();
		SmartBeanUtil.copyProperties(coach, infoVO);

		// 获取俱乐部名称
		if (coach.getClubId() != null) {
			ClubEntity club = clubDao.selectById(coach.getClubId());
			if (club != null) {
				infoVO.setClubName(club.getClubName());
			}
		}

		return ResponseDTO.ok(infoVO);
	}

	/**
	 * 获取教练综合等级显示文本
	 * 优先级：教练星级 > 专业证书最高等级
	 */
	private String getDisplayCoachLevel(CoachEntity coach) {
		// 如果有教练星级证书，优先显示
		if (coach.getCoachStarLevel() != null && coach.getCoachStarLevel() > 0) {
			return CoachCertificateConstant.getCoachStarLevelText(coach.getCoachStarLevel()) + "教练";
		}

		// 否则显示专业证书最高等级
		int maxLevel = 0;
		String maxType = "";

		if (coach.getCoachShowJumpingLevel() != null && coach.getCoachShowJumpingLevel() > maxLevel) {
			maxLevel = coach.getCoachShowJumpingLevel();
			maxType = "场地障碍";
		}

		if (coach.getCoachDressageLevel() != null && coach.getCoachDressageLevel() > maxLevel) {
			maxLevel = coach.getCoachDressageLevel();
			maxType = "盛装舞步";
		}

		if (coach.getCoachEventingLevel() != null && coach.getCoachEventingLevel() > maxLevel) {
			maxLevel = coach.getCoachEventingLevel();
			maxType = "三项赛";
		}

		if (maxLevel > 0) {
			return CoachCertificateConstant.getCoachStarLevelText(maxLevel) + maxType + "教练";
		}

		return "教练";
	}
}
