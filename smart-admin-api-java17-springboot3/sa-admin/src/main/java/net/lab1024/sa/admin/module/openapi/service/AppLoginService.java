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

import java.time.LocalDate;
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

			// 2. 根据clubCode获取clubId
			Long clubId = resolveClubId(form);

			// 3. 调用微信接口获取session信息
			ResponseDTO<WechatJscode2SessionVO> sessionResponse = wechatMiniappService.jscode2session(form.getCode());
			if (!sessionResponse.getOk()) {
				return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, sessionResponse.getMsg());
			}

			WechatJscode2SessionVO sessionInfo = sessionResponse.getData();
			String unionId = sessionInfo.getUnionId();
			String openId = sessionInfo.getOpenId();

			// 4. 解密用户信息（如果提供了加密数据）
			WechatUserInfoVO userInfo = null;
			if (StrUtil.isNotBlank(form.getEncryptedData()) && StrUtil.isNotBlank(form.getIv())) {
				ResponseDTO<WechatUserInfoVO> userInfoResponse = wechatMiniappService.decryptUserInfo(
						form.getEncryptedData(), form.getIv(), sessionInfo.getSessionKey());
				if (userInfoResponse.getOk()) {
					userInfo = userInfoResponse.getData();
				}
			}

			// 5. 根据角色类型处理登录
			String role = form.getRole();
			if ("usr".equals(role)) {
				return handleMemberLogin(unionId, openId, userInfo, clubId);
			} else if ("cc".equals(role)) {
				return handleCoachLogin(unionId, openId, userInfo, clubId);
			} else {
				return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "不支持的登录角色类型");
			}

		} catch (Exception e) {
			log.error("微信小程序登录失败", e);
			return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "登录失败，请重试");
		}
	}

	/**
	 * 根据clubCode或clubId解析俱乐部ID
	 */
	private Long resolveClubId(MemberAppLoginForm form) {
		// 优先使用clubCode解析
		if (StrUtil.isNotBlank(form.getClubCode())) {
			ClubEntity club = clubDao.selectOne(new LambdaQueryWrapper<ClubEntity>()
				.eq(ClubEntity::getClubCode, form.getClubCode()));
				// .eq(ClubEntity::getIsDelete, false));  TODO

			if (club == null) {
				throw new RuntimeException("无效的俱乐部编码: " + form.getClubCode());
			}

			log.info("根据clubCode解析俱乐部，clubCode: {}, clubId: {}", form.getClubCode(), club.getClubId());
			return club.getClubId();
		}

		// 向后兼容：仍支持直接传clubId
		if (form.getClubId() != null) {
			ClubEntity club = clubDao.selectById(form.getClubId());
			if (club == null) {
				throw new RuntimeException("无效的俱乐部ID: " + form.getClubId());
			}
			return form.getClubId();
		}

		return null;
	}

	/**
	 * 处理会员登录
	 */
	private ResponseDTO<AppLoginVO> handleMemberLogin(String unionId, String openId, WechatUserInfoVO userInfo, Long clubId) {
		// 查询会员信息（优先使用unionId，其次使用openId）
		MemberEntity member = null;

		if (StrUtil.isNotBlank(unionId)) {
			member = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>()
					.eq(MemberEntity::getUnionId, unionId)
					.eq(MemberEntity::getIsDelete, 0)
					.eq(MemberEntity::getIsValid, 1));
		}

		if (member == null && StrUtil.isNotBlank(openId)) {
			member = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>()
					.eq(MemberEntity::getOpenId, openId)
					.eq(MemberEntity::getIsDelete, 0)
					.eq(MemberEntity::getIsValid, 1));
		}

		// 用户不存在时自动创建
		boolean isFirstLogin = false;
		if (member == null) {
			member = createMinimalMember(unionId, openId, userInfo, clubId);
			isFirstLogin = true;
		}

		// 检查会员状态
		if (member.getDisabledFlag() == 1) {
			return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "账号已被禁用");
		}

		if (member.getRegistrationStatus() == 0) {
			// 注册状态为0不阻止登录，只是标记为首次登录
			isFirstLogin = true;
		}

		// 更新会员登录信息
		updateMemberLoginInfo(member, unionId, openId, userInfo);

		// 生成会员token
		String loginId = "member_" + member.getMemberId();
		StpUtil.login(loginId);
		String token = MemberAppConst.MEMBER_TOKEN_PREFIX + StpUtil.getTokenValue();

		// 构建简化的登录响应
		AppLoginVO loginVO = buildSimpleLoginVO(token, "usr", isFirstLogin);

		log.info("会员微信登录成功，memberId: {}, unionId: {}, isFirstLogin: {}",
				member.getMemberId(), unionId, isFirstLogin);

		return ResponseDTO.ok(loginVO);
	}

	/**
	 * 处理教练登录
	 */
	private ResponseDTO<AppLoginVO> handleCoachLogin(String unionId, String openId, WechatUserInfoVO userInfo, Long clubId) {
		// 查询教练信息（优先使用unionId，其次使用openId）
		CoachEntity coach = null;

		if (StrUtil.isNotBlank(unionId)) {
			coach = coachDao.selectOne(new LambdaQueryWrapper<CoachEntity>()
					.eq(CoachEntity::getUnionId, unionId)
					.eq(CoachEntity::getIsDelete, 0)
					.eq(CoachEntity::getIsValid, 1));
		}

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

		// 构建简化的登录响应
		AppLoginVO loginVO = buildSimpleLoginVO(token, "cc", coach.getLastLoginTime() == null);

		log.info("教练微信登录成功，coachId: {}, unionId: {}",
				coach.getCoachId(), unionId);

		return ResponseDTO.ok(loginVO);
	}

	/**
	 * 创建最基本的会员记录
	 */
	private MemberEntity createMinimalMember(String unionId, String openId, WechatUserInfoVO userInfo, Long clubId) {
		MemberEntity newMember = new MemberEntity();

		// 必要字段
		newMember.setMemberNo(generateMemberNo()); // 生成会员编号 TODO
		newMember.setUnionId(unionId);
		newMember.setOpenId(openId);
		newMember.setClubId(clubId);

		// 从微信获取的信息（如果有）
		if (userInfo != null) {
			if (StrUtil.isNotBlank(userInfo.getNickName())) {
				newMember.setActualName(userInfo.getNickName());
			} else {
				newMember.setActualName("微信用户");
			}
			newMember.setAvatarUrl(userInfo.getAvatarUrl());
		} else {
			newMember.setActualName("微信用户");
		}

		// 状态字段
		newMember.setRegistrationStatus(0); // 0-未完善信息，1-已完善
		newMember.setIsValid(1);
		newMember.setIsDelete(0);
		newMember.setDisabledFlag(0);
		newMember.setCreateTime(LocalDateTime.now());
		newMember.setUpdateTime(LocalDateTime.now());

		memberDao.insert(newMember);
		log.info("自动创建会员记录成功，memberId: {}, memberNo: {}", newMember.getMemberId(), newMember.getMemberNo());
		return newMember;
	}

	/**
	 * 更新会员登录信息
	 */
	private void updateMemberLoginInfo(MemberEntity member, String unionId, String openId, WechatUserInfoVO userInfo) {
		MemberEntity updateEntity = new MemberEntity();
		updateEntity.setMemberId(member.getMemberId());
		updateEntity.setLastLoginTime(LocalDateTime.now());
		updateEntity.setUpdateTime(LocalDateTime.now());

		// 更新缺失的微信信息
		if (StrUtil.isBlank(member.getUnionId()) && StrUtil.isNotBlank(unionId)) {
			updateEntity.setUnionId(unionId);
		}
		if (StrUtil.isBlank(member.getOpenId()) && StrUtil.isNotBlank(openId)) {
			updateEntity.setOpenId(openId);
		}

		// 更新头像（如果微信提供了新的）
		if (userInfo != null && StrUtil.isNotBlank(userInfo.getAvatarUrl())) {
			updateEntity.setAvatarUrl(userInfo.getAvatarUrl());
		}

		memberDao.updateById(updateEntity);
	}

	/**
	 * 构建简化的登录响应
	 */
	private AppLoginVO buildSimpleLoginVO(String token, String role, boolean isFirstLogin) {
		AppLoginVO loginVO = new AppLoginVO();
		loginVO.setToken(token);
		loginVO.setRole(role);
		loginVO.setIsFirstLogin(isFirstLogin);
		return loginVO;
	}

	/**
	 * 生成会员编号
	 */
	private String generateMemberNo() {
		// 生成格式：M + 年月日 + 4位序号，如：M20250829001
		String dateStr = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

		// 查询当天已有的最大序号
		String prefix = "M" + dateStr;
		String maxMemberNo = memberDao.selectMaxMemberNoByPrefix(prefix);

		int sequence = 1;
		if (StrUtil.isNotBlank(maxMemberNo) && maxMemberNo.length() >= 12) {
			String sequenceStr = maxMemberNo.substring(9);
			try {
				sequence = Integer.parseInt(sequenceStr) + 1;
			} catch (NumberFormatException e) {
				sequence = 1;
			}
		}

		return String.format("%s%03d", prefix, sequence);
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
