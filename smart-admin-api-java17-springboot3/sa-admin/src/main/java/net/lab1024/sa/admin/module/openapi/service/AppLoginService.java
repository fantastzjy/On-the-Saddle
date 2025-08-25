package net.lab1024.sa.admin.module.openapi.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.coach.constant.CoachAppConst;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.member.constant.MemberAppConst;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.service.WechatMiniappService;
import net.lab1024.sa.admin.module.openapi.domain.form.MemberAppLoginForm;
import net.lab1024.sa.admin.module.openapi.domain.vo.AppLoginVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.*;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictDataVO;
import net.lab1024.sa.base.module.support.dict.service.DictService;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 小程序统一登录服务
 * 支持会员(usr)和教练(cc)登录
 */
@Slf4j
@Service
public class AppLoginService {

	@Resource
	private MemberDao memberDao;

	@Resource
	private CoachDao coachDao;

	@Resource
	private ClubDao clubDao;

	@Resource
	private DictService dictService;

	@Resource
	private WechatMiniappService wechatMiniappService;

	/**
	 * 统一微信小程序登录
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<AppLoginVO> wxLogin(MemberAppLoginForm form) {
		try {
			// 1. 检查微信配置
			if (!wechatMiniappService.isConfigValid()) {
				return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "微信小程序配置未完成");
			}

			// 2. 调用微信接口获取session信息
			ResponseDTO<WechatJscode2SessionVO> sessionResponse = wechatMiniappService.jscode2session(form.getCode());
			if (!sessionResponse.getOk()) {
				return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, sessionResponse.getMsg());
			}

			WechatJscode2SessionVO sessionInfo = sessionResponse.getData();
			String unionId = sessionInfo.getUnionId();
			String openId = sessionInfo.getOpenId();

			// 3. 解密用户信息（如果提供了加密数据）
			WechatUserInfoVO userInfo = null;
			if (StrUtil.isNotBlank(form.getEncryptedData()) && StrUtil.isNotBlank(form.getIv())) {
				ResponseDTO<WechatUserInfoVO> userInfoResponse = wechatMiniappService.decryptUserInfo(
						form.getEncryptedData(), form.getIv(), sessionInfo.getSessionKey());
				if (userInfoResponse.getOk()) {
					userInfo = userInfoResponse.getData();
				}
			}

			// 4. 根据角色类型处理登录
			String role = form.getRole();
			if ("usr".equals(role)) {
				return handleMemberLogin(unionId, openId, userInfo, form.getClubId());
			} else if ("cc".equals(role)) {
				return handleCoachLogin(unionId, openId, userInfo, form.getClubId());
			} else {
				return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "不支持的登录角色类型");
			}

		} catch (Exception e) {
			log.error("微信小程序登录失败", e);
			return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "登录失败，请重试");
		}
	}

	/**
	 * 处理会员登录
	 */
	private ResponseDTO<AppLoginVO> handleMemberLogin(String unionId, String openId, WechatUserInfoVO userInfo, Long clubId) {
		// 查询会员信息（优先使用unionId，其次使用openId）
		MemberEntity member = null;

		// if (StrUtil.isNotBlank(unionId)) {
		// 	member = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>()
		// 			.eq(MemberEntity::getUnionId, unionId)
		// 			.eq(MemberEntity::getIsDelete, 0)
		// 			.eq(MemberEntity::getIsValid, 1));
		// }

		if (member == null && StrUtil.isNotBlank(openId)) {
			member = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>()
					.eq(MemberEntity::getOpenId, openId)
					.eq(MemberEntity::getIsDelete, 0)
					.eq(MemberEntity::getIsValid, 1));
		}

		if (member == null) {
			return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "会员不存在，请先注册");
		}

		// 检查会员状态
		if (member.getDisabledFlag() == 1) {
			return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "账号已被禁用");
		}

		if (member.getRegistrationStatus() == 0) {
			return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "账号未激活");
		}

		// 更新会员信息
		MemberEntity updateEntity = new MemberEntity();
		updateEntity.setMemberId(member.getMemberId());
		updateEntity.setLastLoginTime(LocalDateTime.now());
		updateEntity.setUpdateTime(LocalDateTime.now());

		// 更新微信信息（如果缺失）
		if (StrUtil.isBlank(member.getUnionId()) && StrUtil.isNotBlank(unionId)) {
			updateEntity.setUnionId(unionId);
		}
		if (StrUtil.isBlank(member.getOpenId()) && StrUtil.isNotBlank(openId)) {
			updateEntity.setOpenId(openId);
		}

		// 更新用户头像（如果微信提供了新的）
		if (userInfo != null && StrUtil.isNotBlank(userInfo.getAvatarUrl())) {
			updateEntity.setAvatarUrl(userInfo.getAvatarUrl());
		}

		memberDao.updateById(updateEntity);

		// 生成会员token
		String loginId = "member_" + member.getMemberId();
		StpUtil.login(loginId);
		String token = MemberAppConst.MEMBER_TOKEN_PREFIX + StpUtil.getTokenValue();

		// 构建统一返回结果
		AppLoginVO loginVO = buildMemberLoginVO(member, token);

		log.info("会员微信登录成功，memberId: {}, unionId: {}, loginVO：{}",
				member.getMemberId(), unionId, JSON.toJSONString(loginVO));

		return ResponseDTO.ok(loginVO);
	}

	/**
	 * 处理教练登录
	 */
	private ResponseDTO<AppLoginVO> handleCoachLogin(String unionId, String openId, WechatUserInfoVO userInfo, Long clubId) {
		// 查询教练信息（优先使用unionId，其次使用openId）
		CoachEntity coach = null;

		// if (StrUtil.isNotBlank(unionId)) {
		// 	coach = coachDao.selectOne(new LambdaQueryWrapper<CoachEntity>()
		// 			.eq(CoachEntity::getUnionId, unionId)
		// 			.eq(CoachEntity::getIsDelete, 0)
		// 			.eq(CoachEntity::getIsValid, 1));
		// }

		if (coach == null && StrUtil.isNotBlank(openId)) {
			coach = coachDao.selectOne(new LambdaQueryWrapper<CoachEntity>()
					.eq(CoachEntity::getOpenId, openId)
					.eq(CoachEntity::getIsDelete, 0)
					.eq(CoachEntity::getIsValid, 1));
		}

		if (coach == null) {
			return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "教练不存在，请联系管理员");
		}

		// 检查教练状态
		if (coach.getDisabledFlag() != null && coach.getDisabledFlag() == 1) {
			return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "账号已被禁用");
		}

		// 更新教练信息
		CoachEntity updateEntity = new CoachEntity();
		updateEntity.setCoachId(coach.getCoachId());
		updateEntity.setLastLoginTime(LocalDateTime.now());
		updateEntity.setUpdateTime(LocalDateTime.now());

		// 更新微信信息（如果缺失）
		if (StrUtil.isBlank(coach.getUnionId()) && StrUtil.isNotBlank(unionId)) {
			updateEntity.setUnionId(unionId);
		}
		if (StrUtil.isBlank(coach.getOpenId()) && StrUtil.isNotBlank(openId)) {
			updateEntity.setOpenId(openId);
		}

		// 更新用户头像（如果微信提供了新的）
		if (userInfo != null && StrUtil.isNotBlank(userInfo.getAvatarUrl())) {
			updateEntity.setAvatarUrl(userInfo.getAvatarUrl());
		}

		coachDao.updateById(updateEntity);

		// 生成教练token
		String loginId = "coach_" + coach.getCoachId();
		StpUtil.login(loginId);
		String token = CoachAppConst.COACH_TOKEN_PREFIX + StpUtil.getTokenValue();

		// 构建统一返回结果
		AppLoginVO loginVO = buildCoachLoginVO(coach, token);

		log.info("教练微信登录成功，coachId: {}, unionId: {}, loginVO：{}",
				coach.getCoachId(), unionId, JSON.toJSONString(loginVO));

		return ResponseDTO.ok(loginVO);
	}

	/**
	 * 构建会员登录响应
	 */
	private AppLoginVO buildMemberLoginVO(MemberEntity member, String token) {
		AppLoginVO loginVO = new AppLoginVO();
		loginVO.setToken(token);
		loginVO.setRole("usr");
		return loginVO;
	}

	/**
	 * 构建教练登录响应
	 */
	private AppLoginVO buildCoachLoginVO(CoachEntity coach, String token) {
		AppLoginVO loginVO = new AppLoginVO();
		loginVO.setToken(token);
		loginVO.setRole("cc");
		return loginVO;
	}

	/**
	 * 检查用户注册状态
	 */
	public ResponseDTO<Boolean> checkRegistration(String unionId, String role) {
		if (StrUtil.isBlank(unionId)) {
			return ResponseDTO.ok(false);
		}

		if ("usr".equals(role)) {
			MemberEntity member = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>()
					.eq(MemberEntity::getUnionId, unionId)
					.eq(MemberEntity::getIsDelete, 0)
					.eq(MemberEntity::getIsValid, 1));
			return ResponseDTO.ok(member != null && member.getRegistrationStatus() == 1);
		} else if ("cc".equals(role)) {
			CoachEntity coach = coachDao.selectOne(new LambdaQueryWrapper<CoachEntity>()
					.eq(CoachEntity::getUnionId, unionId)
					.eq(CoachEntity::getIsDelete, 0)
					.eq(CoachEntity::getIsValid, 1));
			return ResponseDTO.ok(coach != null);
		} else {
			return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "不支持的角色类型");
		}
	}
}
