package net.lab1024.sa.admin.module.business.member.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.RequestMember;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.member.domain.form.MemberAppUpdateForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.*;
import net.lab1024.sa.admin.util.MemberRequestUtil;
import net.lab1024.sa.base.module.support.dict.service.DictService;
import net.lab1024.sa.base.module.support.file.constant.FileFolderTypeEnum;
import net.lab1024.sa.base.module.support.file.dao.FileDao;
import net.lab1024.sa.base.module.support.file.domain.entity.FileEntity;
import net.lab1024.sa.base.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.sa.base.module.support.file.service.FileService;
import net.lab1024.sa.base.module.support.file.service.IFileStorageService;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 会员小程序业务服务
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class MemberAppService {

	@Resource
	private MemberDao memberDao;

	@Resource
	private ClubDao clubDao;

	@Resource
	private CoachDao coachDao;

	@Resource
	private DictService dictService;

	@Resource
	private FamilyGroupService familyGroupService;

	@Resource
	private FileService fileService;

	@Resource
	private IFileStorageService fileStorageService;

	@Resource
	private FileDao fileDao;

	@Resource
	private WechatMiniappService wechatMiniappService;

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

		// 查询并设置俱乐部编号和名称
		if (member.getClubId() != null) {
			try {
				var club = clubDao.selectById(member.getClubId());
				if (club != null) {
					infoVO.setClubName(club.getClubName());
					infoVO.setClubCode(club.getClubCode());
				}
			} catch (Exception e) {
				log.warn("查询俱乐部信息失败: clubId={}", member.getClubId(), e);
			}
		}

		// 查询并设置默认教练编号和姓名
		if (member.getDefaultCoachId() != null) {
			try {
				CoachEntity coach = coachDao.selectById(member.getDefaultCoachId());
				if (coach != null) {
					infoVO.setDefaultCoachNo(coach.getCoachNo());
					infoVO.setDefaultCoachName(coach.getActualName());
				}
			} catch (Exception e) {
				log.warn("查询教练信息失败: coachId={}", member.getDefaultCoachId(), e);
			}
		}

		// 查询并设置课程级别名称
		if (StrUtil.isNotBlank(member.getDefaultCourseLevel())) {
			try {
				var courseLevelDict = dictService.getDictData("COURSE_LEVEL", member.getDefaultCourseLevel());
				if (courseLevelDict != null) {
					infoVO.setDefaultCourseLevelName(courseLevelDict.getDataLabel());
				}
			} catch (Exception e) {
				log.warn("查询课程级别字典失败: courseLevel={}", member.getDefaultCourseLevel(), e);
			}
		}

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
	 * 上传并更新头像
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<String> uploadAvatar(MultipartFile file) {
		Long memberId = MemberRequestUtil.getRequestMemberId();
		if (memberId == null) {
			return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
		}

		// 检查会员是否存在
		MemberEntity member = memberDao.selectById(memberId);
		if (member == null) {
			return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
		}

		// 保存旧头像URL用于清理
		String oldAvatarUrl = member.getAvatarUrl();
		String oldFileKey = extractFileKeyFromUrl(oldAvatarUrl);

		try {
			// 创建RequestMember对象用于文件上传
			RequestMember requestMember = new RequestMember();
			requestMember.setMemberId(memberId);
			requestMember.setActualName(member.getActualName());

			// 调用文件服务上传头像
			ResponseDTO<FileUploadVO> uploadResponse = fileService.fileUpload(file, FileFolderTypeEnum.COMMON.getValue(), requestMember);
			if (!uploadResponse.getOk()) {
				return ResponseDTO.userErrorParam(uploadResponse.getMsg());
			}

			FileUploadVO uploadVO = uploadResponse.getData();
			String newAvatarUrl = uploadVO.getFileUrl();

			// 更新会员头像URL
			MemberEntity updateEntity = new MemberEntity();
			updateEntity.setMemberId(memberId);
			updateEntity.setAvatarUrl(newAvatarUrl);
			updateEntity.setUpdateTime(LocalDateTime.now());

			memberDao.updateById(updateEntity);

			// 清理旧头像文件（如果存在）
			if (StringUtils.isNotBlank(oldFileKey)) {
				try {
					// 删除物理文件
					fileStorageService.delete(oldFileKey);

					// 删除文件记录
					deleteFileRecord(oldFileKey);

					log.info("成功清理旧头像文件, memberId: {}, oldFileKey: {}", memberId, oldFileKey);
				} catch (Exception e) {
					// 清理失败不影响头像更新成功
					log.warn("清理旧头像文件失败, memberId: {}, oldFileKey: {}", memberId, oldFileKey, e);
				}
			}

			log.info("会员头像上传成功, memberId: {}, avatarUrl: {}", memberId, newAvatarUrl);
			return ResponseDTO.ok(newAvatarUrl);

		} catch (Exception e) {
			log.error("会员头像上传失败, memberId: {}", memberId, e);
			return ResponseDTO.userErrorParam("头像上传失败，请重试");
		}
	}

	/**
	 * 从avatarUrl中提取fileKey的工具方法
	 */
	private String extractFileKeyFromUrl(String avatarUrl) {
		if (StringUtils.isBlank(avatarUrl)) {
			return null;
		}

		String uploadMapping = "/upload/";
		int uploadIndex = avatarUrl.indexOf(uploadMapping);
		if (uploadIndex != -1) {
			return avatarUrl.substring(uploadIndex + uploadMapping.length());
		}

		return null;
	}

	/**
	 * 删除文件记录的方法
	 */
	private void deleteFileRecord(String fileKey) {
		LambdaQueryWrapper<FileEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(FileEntity::getFileKey, fileKey);

		FileEntity fileEntity = fileDao.selectOne(queryWrapper);
		if (fileEntity != null) {
			fileDao.deleteById(fileEntity.getFileId());
		}
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
