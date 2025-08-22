package net.lab1024.sa.admin.module.business.member.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.constant.MemberAppConst;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.RequestMember;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberAppLoginForm;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberAppUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.*;
import net.lab1024.sa.admin.util.MemberRequestUtil;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 会员小程序业务服务
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class AppLoginService {

	@Resource
	private MemberDao memberDao;

	@Resource
	private FamilyGroupService familyGroupService;

	@Resource
	private WechatMiniappService wechatMiniappService;

	/**
	 * 微信小程序登录
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<MemberAppLoginVO> wxLogin(MemberAppLoginForm form) {
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

			// 4. 查询会员信息（优先使用unionId，其次使用openId）
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

			if (member == null) {
				return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "会员不存在，请先注册");
			}

			// 5. 检查会员状态
			if (member.getDisabledFlag() == 1) {
				return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "账号已被禁用");
			}

			if (member.getRegistrationStatus() == 0) {
				return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "账号未激活");
			}

			// 6. 判断是否首次登录
			boolean firstLogin = member.getLastLoginTime() == null;

			// 7. 更新会员信息
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

			// 8. 生成会员token
			String loginId = "member_" + member.getMemberId();
			StpUtil.login(loginId);
			String token = MemberAppConst.MEMBER_TOKEN_PREFIX + StpUtil.getTokenValue();

			// 9. 构建返回结果
			MemberAppLoginVO loginVO = SmartBeanUtil.copy(member, MemberAppLoginVO.class);
			loginVO.setToken(token);
			// loginVO.setFirstLogin(firstLogin);

			// 如果有更新的头像，返回最新的
			if (userInfo != null && StrUtil.isNotBlank(userInfo.getAvatarUrl())) {
				loginVO.setAvatarUrl(userInfo.getAvatarUrl());
			}

			log.info("会员微信登录成功，memberId: {}, unionId: {}, firstLogin: {} loginVO：{}",
					member.getMemberId(), unionId, firstLogin, JSON.toJSONString(loginVO));

			return ResponseDTO.ok(loginVO);

		} catch (Exception e) {
			log.error("会员微信登录失败", e);
			return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "登录失败，请重试");
		}
	}

	/**
	 * 获取登录的会员信息（供拦截器使用）
	 */
	public RequestMember getLoginMember(String loginId, HttpServletRequest request) {
		try {
			if (StrUtil.isBlank(loginId) || !loginId.startsWith("member_")) {
				return null;
			}

			String memberIdStr = loginId.substring("member_".length());
			Long memberId = Long.valueOf(memberIdStr);

			MemberEntity member = memberDao.selectById(memberId);
			if (member == null || member.getIsDelete() == 1 || member.getIsValid() == 0) {
				return null;
			}

			// 转换为RequestMember
			RequestMember requestMember = SmartBeanUtil.copy(member, RequestMember.class);
			requestMember.setMemberId(member.getMemberId());
			requestMember.setMemberNo(member.getMemberNo());
			requestMember.setActualName(member.getActualName());
			requestMember.setAvatar(member.getAvatarUrl());
			requestMember.setPhone(member.getPhone());
			requestMember.setEmail(member.getEmail());
			requestMember.setClubId(member.getClubId());
			requestMember.setIsMembership(member.getIsMembership());
			requestMember.setMembershipStatus(member.getMembershipStatus());
			requestMember.setRegistrationStatus(member.getRegistrationStatus());
			requestMember.setDisabledFlag(member.getDisabledFlag());

			// 设置请求信息
			requestMember.setIp(SmartRequestUtil.getIp(request));
			requestMember.setUserAgent(SmartRequestUtil.getUserAgent(request));

			return requestMember;

		} catch (Exception e) {
			log.error("获取登录会员信息失败, loginId: {}", loginId, e);
			return null;
		}
	}

	/**
	 * 获取会员个人信息
	 */
	public ResponseDTO<MemberAppInfoVO> getMemberInfo() {
		Long memberId = MemberRequestUtil.getRequestMemberId();
		if (memberId == null) {
			return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
		}

		MemberEntity member = memberDao.selectById(memberId);
		if (member == null) {
			return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
		}

		MemberAppInfoVO infoVO = SmartBeanUtil.copy(member, MemberAppInfoVO.class);
		return ResponseDTO.ok(infoVO);
	}

	/**
	 * 更新会员个人信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<String> updateMemberInfo(MemberAppUpdateForm form) {
		Long memberId = MemberRequestUtil.getRequestMemberId();
		if (memberId == null) {
			return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
		}

		// 检查会员是否存在
		MemberEntity member = memberDao.selectById(memberId);
		if (member == null) {
			return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
		}

		// 更新会员信息
		MemberEntity updateEntity = new MemberEntity();
		updateEntity.setMemberId(memberId);
		updateEntity.setActualName(form.getActualName());
		updateEntity.setPhone(form.getPhone());
		updateEntity.setEmail(form.getEmail());
		updateEntity.setAvatarUrl(form.getAvatarUrl());
		updateEntity.setGender(form.getGender());
		updateEntity.setBirthDate(form.getBirthDate());
		updateEntity.setIdCardNo(form.getIdCardNo());
		updateEntity.setRiderCertNo(form.getRiderCertNo());
		updateEntity.setDefaultCourseLevel(form.getDefaultCourseLevel());
		updateEntity.setProfileData(form.getProfileData());
		updateEntity.setUpdateTime(LocalDateTime.now());

		memberDao.updateById(updateEntity);

		return ResponseDTO.ok();
	}

	/**
	 * 获取会籍状态
	 */
	public ResponseDTO<MembershipStatusVO> getMembershipStatus() {
		Long memberId = MemberRequestUtil.getRequestMemberId();
		if (memberId == null) {
			return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
		}

		MemberEntity member = memberDao.selectById(memberId);
		if (member == null) {
			return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
		}

		MembershipStatusVO statusVO = new MembershipStatusVO();
		statusVO.setIsMembership(member.getIsMembership());
		statusVO.setMembershipStatus(member.getMembershipStatus());
		statusVO.setMembershipExpireDate(member.getMembershipExpireDate());

		// 计算距离到期天数
		if (member.getMembershipExpireDate() != null) {
			long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), member.getMembershipExpireDate());
			statusVO.setDaysToExpire(daysToExpire);
		}

		// 设置状态描述
		String statusDescription = "未知状态";
		if (member.getMembershipStatus() != null) {
			switch (member.getMembershipStatus()) {
				case 1:
					statusDescription = "正常";
					break;
				case 2:
					statusDescription = "即将到期";
					break;
				case 3:
					statusDescription = "已过期";
					break;
			}
		}
		statusVO.setStatusDescription(statusDescription);

		return ResponseDTO.ok(statusVO);
	}

	/**
	 * 获取家庭组信息
	 */
	public ResponseDTO<FamilyInfoVO> getFamilyInfo() {
		Long memberId = MemberRequestUtil.getRequestMemberId();
		if (memberId == null) {
			return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
		}

		// 调用现有的家庭组服务获取信息
		return familyGroupService.getFamilyInfoByMemberId(memberId);
	}

	/**
	 * 检查用户注册状态
	 */
	public ResponseDTO<Boolean> checkRegistration(String unionId) {
		if (StrUtil.isBlank(unionId)) {
			return ResponseDTO.ok(false);
		}

		MemberEntity member = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>()
				.eq(MemberEntity::getUnionId, unionId)
				.eq(MemberEntity::getIsDelete, 0)
				.eq(MemberEntity::getIsValid, 1));

		return ResponseDTO.ok(member != null && member.getRegistrationStatus() == 1);
	}
}
