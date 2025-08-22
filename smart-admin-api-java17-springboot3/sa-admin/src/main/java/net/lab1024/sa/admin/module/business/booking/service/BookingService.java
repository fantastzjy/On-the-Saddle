package net.lab1024.sa.admin.module.business.booking.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.booking.domain.form.BookingQueryForm;
import net.lab1024.sa.admin.module.business.booking.domain.form.BookingRescheduleForm;
import net.lab1024.sa.admin.module.business.booking.domain.vo.BookingDetailVO;
import net.lab1024.sa.admin.module.business.booking.domain.vo.BookingListVO;
import net.lab1024.sa.admin.module.business.booking.domain.vo.ConflictCheckResult;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseEntity;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.business.schedule.dao.LessonScheduleDao;
import net.lab1024.sa.admin.module.business.schedule.domain.entity.LessonScheduleEntity;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.base.module.support.operatelog.OperateLogService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 预约管理服务
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class BookingService {

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private CoachDao coachDao;

	@Autowired
	private HorseDao horseDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private LessonScheduleDao scheduleDao;

	@Autowired
	private OperateLogService operateLogService;

	/**
	 * 分页查询预约列表
	 */
	public ResponseDTO<PageResult<BookingListVO>> queryBookingList(BookingQueryForm queryForm) {
		try {
			Page<BookingEntity> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
			LambdaQueryWrapper<BookingEntity> queryWrapper = buildBookingQueryWrapper(queryForm);

			IPage<BookingEntity> pageResult = bookingDao.selectPage(page, queryWrapper);

			// 手动转换为VO，确保所有字段正确映射
			List<BookingListVO> bookingVOList = new ArrayList<>();
			for (BookingEntity entity : pageResult.getRecords()) {
				BookingListVO vo = convertToBookingListVO(entity);
				bookingVOList.add(vo);
			}

			// 构建分页结果
			PageResult<BookingListVO> result = new PageResult<>();
			result.setList(bookingVOList);
			result.setTotal(pageResult.getTotal());
			result.setPageNum(pageResult.getCurrent());
			result.setPageSize(pageResult.getSize());

			// 补充额外信息
			enhanceBookingListData(result.getList());

			log.info("查询预约列表成功，共{}条记录", result.getTotal());
			return ResponseDTO.ok(result);

		} catch (Exception e) {
			log.error("查询预约列表失败", e);
			return ResponseDTO.userErrorParam("查询预约列表失败");
		}
	}

	/**
	 * 将BookingEntity转换为BookingListVO
	 */
	private BookingListVO convertToBookingListVO(BookingEntity entity) {
		BookingListVO vo = new BookingListVO();
		vo.setBookingId(entity.getBookingId());
		vo.setOrderId(entity.getOrderId());
		vo.setMemberId(entity.getMemberId());
		vo.setCoachId(entity.getCoachId());
		vo.setHorseId(entity.getHorseId());
		vo.setBookingStatus(entity.getBookingStatus());
		vo.setCreateTime(entity.getCreateTime());

		// 设置预约日期为开始时间
		if (entity.getStartTime() != null) {
			vo.setBookingDate(entity.getStartTime());
		} else {
			vo.setBookingDate(entity.getCreateTime());
		}

		// 设置总费用
		BigDecimal totalFee = BigDecimal.ZERO;
		if (entity.getActualCoachFee() != null) {
			totalFee = totalFee.add(entity.getActualCoachFee());
		}
		if (entity.getActualHorseFee() != null) {
			totalFee = totalFee.add(entity.getActualHorseFee());
		}
		vo.setTotalFee(totalFee);

		return vo;
	}

	/**
	 * 获取预约详情
	 */
	public ResponseDTO<BookingDetailVO> getBookingDetail(Long bookingId) {
		try {
			BookingEntity bookingEntity = bookingDao.selectById(bookingId);
			if (bookingEntity == null) {
				return ResponseDTO.userErrorParam("预约不存在");
			}

			BookingDetailVO bookingDetailVO = SmartBeanUtil.copy(bookingEntity, BookingDetailVO.class);

			// 补充订单信息
			if (bookingEntity.getOrderId() != null) {
				OrderEntity order = orderDao.selectById(bookingEntity.getOrderId());
				if (order != null) {
					bookingDetailVO.setOrderNo(order.getOrderNo());
				}
			}

			// 补充会员信息（与ScheduleDetailVO保持一致的字段）
			if (bookingEntity.getMemberId() != null) {
				MemberEntity member = memberDao.selectById(bookingEntity.getMemberId());
				if (member != null) {
					bookingDetailVO.setMemberName(member.getActualName());
					bookingDetailVO.setMemberPhone(member.getPhone());
					bookingDetailVO.setMemberGender(getMemberGenderName(member.getGender()));
					// 使用真实的课程级别
					bookingDetailVO.setMemberLevel(member.getDefaultCourseLevel());

					// 从扩展数据中解析身高、体重、骑乘经验
					if (SmartStringUtil.isNotBlank(member.getProfileData())) {
						try {
							ObjectMapper objectMapper = new ObjectMapper();
							JsonNode profileJson = objectMapper.readTree(member.getProfileData());
							bookingDetailVO.setMemberHeight(profileJson.path("height").asText("未设置"));
							bookingDetailVO.setMemberWeight(profileJson.path("weight").asText("未设置"));
							bookingDetailVO.setRidingExperience(profileJson.path("ridingExperience").asText("无"));
						} catch (Exception e) {
							log.warn("解析会员扩展数据失败，会员ID：{}", member.getMemberId(), e);
							bookingDetailVO.setMemberHeight("未设置");
							bookingDetailVO.setMemberWeight("未设置");
							bookingDetailVO.setRidingExperience("无");
						}
					} else {
						bookingDetailVO.setMemberHeight("未设置");
						bookingDetailVO.setMemberWeight("未设置");
						bookingDetailVO.setRidingExperience("无");
					}
					bookingDetailVO.setMemberRemark("无备注");

					// 默认教练姓名需要从会员表的默认教练ID查询
					if (member.getDefaultCoachId() != null) {
						try {
							CoachEntity defaultCoach = coachDao.selectById(member.getDefaultCoachId());
							if (defaultCoach != null && defaultCoach.getUserId() != null) {
								EmployeeEntity defaultCoachEmployee = employeeDao.selectById(defaultCoach.getUserId());
								if (defaultCoachEmployee != null) {
									bookingDetailVO.setDefaultCoachName(defaultCoachEmployee.getActualName());
								}
							}
						} catch (Exception e) {
							log.warn("获取默认教练信息失败，教练ID：{}", member.getDefaultCoachId());
							bookingDetailVO.setDefaultCoachName("未设置");
						}
					} else {
						bookingDetailVO.setDefaultCoachName("未设置");
					}
				}
			}

			// 补充教练信息（与ScheduleDetailVO保持一致的字段）
			if (bookingEntity.getCoachId() != null && bookingEntity.getCoachId() > 0) {
				try {
					CoachEntity coach = coachDao.selectById(bookingEntity.getCoachId());
					if (coach != null) {
						// 从教练关联的用户信息获取姓名和电话
						if (coach.getUserId() != null) {
							EmployeeEntity employee = employeeDao.selectById(coach.getUserId());
							if (employee != null) {
								bookingDetailVO.setCoachName(employee.getActualName());
								bookingDetailVO.setCoachPhone(employee.getPhone());
							}
						}
						bookingDetailVO.setCoachNo(coach.getCoachNo());
						bookingDetailVO.setCoachAvatar(coach.getAvatarUrl());
						bookingDetailVO.setCoachLevel(coach.getCoachLevel());
						bookingDetailVO.setCoachSpecialties(coach.getSpecialties());
						bookingDetailVO.setCoachIntroduction(coach.getIntroduction());
					}
				} catch (Exception e) {
					log.warn("获取教练信息失败，教练ID：{}", bookingEntity.getCoachId());
				}
			}

			// 补充马匹信息（与ScheduleDetailVO保持一致的字段）
			if (bookingEntity.getHorseId() != null && bookingEntity.getHorseId() > 0) {
				try {
					HorseEntity horse = horseDao.selectById(bookingEntity.getHorseId());
					if (horse != null) {
						bookingDetailVO.setHorseName(horse.getHorseName());
						bookingDetailVO.setHorseNo(horse.getHorseCode());
						bookingDetailVO.setHorseBreed(horse.getBreed());
						bookingDetailVO.setHorseGender(getHorseGenderName(horse.getGender()));
						bookingDetailVO.setHorseColor(horse.getColor());
						bookingDetailVO.setHorseHeight(horse.getHeight() != null ? horse.getHeight() + "cm" : "未设置");
						bookingDetailVO.setHorseWeight(horse.getWeight() != null ? horse.getWeight() + "kg" : "未设置");
						bookingDetailVO.setHorseHealthStatus(getHealthStatusName(horse.getHealthStatus()));
						// 使用准确的年龄计算方式（参考ScheduleService）
						if (horse.getBirthDate() != null) {
							int age = java.time.Period.between(
								horse.getBirthDate().toLocalDate(),
								java.time.LocalDate.now()
							).getYears();
							bookingDetailVO.setHorseAge(age);
						}
						// 马匹性格从扩展数据中获取，暂设默认值
						bookingDetailVO.setHorseCharacter("温和");
					}
				} catch (Exception e) {
					log.warn("获取马匹信息失败，马匹ID：{}", bookingEntity.getHorseId());
				}
			}

			// 补充商品信息（替换模拟数据为真实查询）
			if (bookingEntity.getProductId() != null) {
				try {
					ProductEntity product = productDao.selectById(bookingEntity.getProductId());
					if (product != null) {
						bookingDetailVO.setProductName(product.getProductName());
						bookingDetailVO.setProductType(getProductTypeName(product.getProductType()));
						// 根据产品类型确定课程类型
						bookingDetailVO.setLessonType(getLessonTypeName(product.getProductType()));
						bookingDetailVO.setDifficultyLevel("初级"); // 默认难度，后续可从产品表扩展
						// 商品价格需要从相关表查询，暂时使用默认值
						bookingDetailVO.setProductPrice(BigDecimal.valueOf(300.00));
					}
				} catch (Exception e) {
					log.warn("获取商品信息失败，商品ID：{}", bookingEntity.getProductId(), e);
					// 设置默认值
					bookingDetailVO.setProductName("马术体验课");
					bookingDetailVO.setProductType("体验课程");
					bookingDetailVO.setLessonType("体验课");
					bookingDetailVO.setDifficultyLevel("初级");
					bookingDetailVO.setProductPrice(BigDecimal.valueOf(300.00));
				}
			}

			// 计算衍生字段
			// 计算总费用
			BigDecimal totalFee = BigDecimal.ZERO;
			if (bookingEntity.getActualCoachFee() != null) {
				totalFee = totalFee.add(bookingEntity.getActualCoachFee());
			}
			if (bookingEntity.getActualHorseFee() != null) {
				totalFee = totalFee.add(bookingEntity.getActualHorseFee());
			}
			bookingDetailVO.setTotalFee(totalFee);

			// 计算课程时长
			if (bookingEntity.getStartTime() != null && bookingEntity.getEndTime() != null) {
				long minutes = java.time.Duration.between(bookingEntity.getStartTime(), bookingEntity.getEndTime()).toMinutes();
				bookingDetailVO.setDuration((int) minutes);
			}

			// 设置预约日期
			if (bookingEntity.getStartTime() != null) {
				bookingDetailVO.setBookingDate(bookingEntity.getStartTime().toLocalDate());
			}

			// 补充状态名称
			bookingDetailVO.setBookingStatusName(getBookingStatusName(bookingDetailVO.getBookingStatus()));

			// 设置预约特有信息的默认值
			bookingDetailVO.setContactName(bookingDetailVO.getMemberName());
			bookingDetailVO.setContactPhone(bookingDetailVO.getMemberPhone());
			// bookingDetailVO.setPaymentMethod("会员卡扣费");
			bookingDetailVO.setDeposit(BigDecimal.ZERO);

			log.info("获取预约详情成功，预约ID：{}", bookingId);
			return ResponseDTO.ok(bookingDetailVO);

		} catch (Exception e) {
			log.error("获取预约详情失败，预约ID：{}", bookingId, e);
			return ResponseDTO.userErrorParam("获取预约详情失败");
		}
	}

	/**
	 * 确认预约
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<Void> confirmBooking(Long bookingId) {
		try {
			BookingEntity booking = bookingDao.selectById(bookingId);
			if (booking == null) {
				return ResponseDTO.userErrorParam("预约不存在");
			}

			if (booking.getBookingStatus() != 1) {
				return ResponseDTO.userErrorParam("只有待确认的预约才能确认");
			}

			booking.setBookingStatus(2); // 已确认
			booking.setUpdateTime(LocalDateTime.now());
			bookingDao.updateById(booking);

			log.info("确认预约成功，预约ID：{}", bookingId);
			return ResponseDTO.ok();

		} catch (Exception e) {
			log.error("确认预约失败，预约ID：{}", bookingId, e);
			return ResponseDTO.userErrorParam("确认预约失败");
		}
	}

	/**
	 * 核销预约
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<Void> checkinBooking(Long bookingId) {
		try {
			BookingEntity booking = bookingDao.selectById(bookingId);
			if (booking == null) {
				return ResponseDTO.userErrorParam("预约不存在");
			}

			if (booking.getBookingStatus() != 2) {
				return ResponseDTO.userErrorParam("只有已确认的预约才能核销");
			}

			// 只记录核销时间，不改变状态
			booking.setArrivalTime(LocalDateTime.now());
			booking.setUpdateTime(LocalDateTime.now());
			bookingDao.updateById(booking);

			log.info("核销预约成功，预约ID：{}", bookingId);
			return ResponseDTO.ok();

		} catch (Exception e) {
			log.error("核销预约失败，预约ID：{}", bookingId, e);
			return ResponseDTO.userErrorParam("核销预约失败");
		}
	}

	/**
	 * 取消预约
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<Void> cancelBooking(Long bookingId, String reason) {
		try {
			BookingEntity booking = bookingDao.selectById(bookingId);
			if (booking == null) {
				return ResponseDTO.userErrorParam("预约不存在");
			}

			if (booking.getBookingStatus() >= 4) {
				return ResponseDTO.userErrorParam("已完成或已取消的预约无法取消");
			}

			booking.setBookingStatus(5); // 已取消
			booking.setCancelReason(reason);
			booking.setUpdateTime(LocalDateTime.now());
			bookingDao.updateById(booking);

			log.info("取消预约成功，预约ID：{}", bookingId);
			return ResponseDTO.ok();

		} catch (Exception e) {
			log.error("取消预约失败，预约ID：{}", bookingId, e);
			return ResponseDTO.userErrorParam("取消预约失败");
		}
	}

	/**
	 * 批量确认预约
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<Void> batchConfirmBookings(List<Long> bookingIds) {
		try {
			for (Long bookingId : bookingIds) {
				confirmBooking(bookingId);
			}
			log.info("批量确认预约成功，共{}个", bookingIds.size());
			return ResponseDTO.ok();
		} catch (Exception e) {
			log.error("批量确认预约失败", e);
			return ResponseDTO.userErrorParam("批量确认预约失败");
		}
	}

	/**
	 * 批量取消预约
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<Void> batchCancelBookings(List<Long> bookingIds) {
		try {
			for (Long bookingId : bookingIds) {
				cancelBooking(bookingId, "批量取消");
			}
			log.info("批量取消预约成功，共{}个", bookingIds.size());
			return ResponseDTO.ok();
		} catch (Exception e) {
			log.error("批量取消预约失败", e);
			return ResponseDTO.userErrorParam("批量取消预约失败");
		}
	}

	// ========================================
	// 私有辅助方法
	// ========================================

	private LambdaQueryWrapper<BookingEntity> buildBookingQueryWrapper(BookingQueryForm queryForm) {
		LambdaQueryWrapper<BookingEntity> queryWrapper = new LambdaQueryWrapper<>();

		if (SmartStringUtil.isNotBlank(queryForm.getKeywords())) {
			// 这里需要联表查询，暂时简化处理
		}
		if (queryForm.getBookingStatus() != null) {
			queryWrapper.eq(BookingEntity::getBookingStatus, queryForm.getBookingStatus());
		}
		if (queryForm.getCoachId() != null) {
			queryWrapper.eq(BookingEntity::getCoachId, queryForm.getCoachId());
		}
		if (queryForm.getMemberId() != null) {
			queryWrapper.eq(BookingEntity::getMemberId, queryForm.getMemberId());
		}
		if (queryForm.getClubId() != null) {
			queryWrapper.eq(BookingEntity::getClubId, queryForm.getClubId());
		}
		if (queryForm.getStartDate() != null) {
			queryWrapper.ge(BookingEntity::getStartTime, queryForm.getStartDate().atStartOfDay());
		}
		if (queryForm.getEndDate() != null) {
			queryWrapper.le(BookingEntity::getStartTime, queryForm.getEndDate().atTime(23, 59, 59));
		}

		queryWrapper.orderByDesc(BookingEntity::getCreateTime);
		return queryWrapper;
	}

	private void enhanceBookingListData(List<BookingListVO> bookingList) {
		for (BookingListVO booking : bookingList) {
			// 设置状态名称
			booking.setBookingStatusName(getBookingStatusName(booking.getBookingStatus()));

			// 生成预约单号（如果没有的话）
			if (SmartStringUtil.isBlank(booking.getBookingNo())) {
				booking.setBookingNo(generateBookingNo(booking.getBookingId()));
			}

			// 获取会员信息
			if (booking.getMemberId() != null) {
				try {
					MemberEntity member = memberDao.selectById(booking.getMemberId());
					if (member != null) {
						booking.setMemberName(member.getActualName());
						booking.setMemberPhone(member.getPhone());
					}
				} catch (Exception e) {
					log.warn("获取会员信息失败，会员ID：{}", booking.getMemberId());
				}
			}

			// 获取订单信息
			if (booking.getOrderId() != null) {
				try {
					OrderEntity order = orderDao.selectById(booking.getOrderId());
					if (order != null) {
						booking.setOrderNo(order.getOrderNo());
					}
				} catch (Exception e) {
					log.warn("获取订单信息失败，订单ID：{}", booking.getOrderId());
				}
			}

			// 格式化时间
			if (booking.getBookingDate() != null) {
				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

				booking.setStartTime(booking.getBookingDate().format(timeFormatter));
				booking.setEndTime(booking.getBookingDate().plusHours(1).format(timeFormatter));
			}

			// 设置默认值
			if (booking.getTotalFee() == null || booking.getTotalFee().compareTo(BigDecimal.ZERO) == 0) {
				booking.setTotalFee(BigDecimal.valueOf(100.00)); // 默认费用
			}

			// 设置课程类型名称
			if (SmartStringUtil.isBlank(booking.getLessonTypeName())) {
				booking.setLessonTypeName("体验课"); // 默认课程类型
			}

			// 设置教练相关信息（暂时使用默认值，后续可以从教练表查询）
			if (SmartStringUtil.isBlank(booking.getCoachName())) {
				booking.setCoachName("待分配");
			}
		}
	}

	/**
	 * 生成预约单号
	 */
	private String generateBookingNo(Long bookingId) {
		if (bookingId == null) {
			return "BK" + System.currentTimeMillis();
		}
		return "BK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
				String.format("%06d", bookingId);
	}

	private String getBookingStatusName(Integer bookingStatus) {
		if (bookingStatus == null) return "未知";
		switch (bookingStatus) {
			case 1:
				return "待确认";
			case 2:
				return "已确认";
			case 3:
				return "进行中";
			case 4:
				return "已完成";
			case 5:
				return "已取消";
			case 6:
				return "未到场";
			default:
				return "未知状态";
		}
	}

	/**
	 * 会员性别转换（人类性别）
	 */
	private String getMemberGenderName(Integer gender) {
		if (gender == null) return "未知";
		return gender == 1 ? "男" : "女";
	}

	/**
	 * 马匹性别转换（动物性别）
	 */
	private String getHorseGenderName(Integer gender) {
		if (gender == null) return "未知";   // TODO
		switch (gender) {
			case 1:
				return "雄性";
			case 2:
				return "雌性";
			case 3:
				return "去势雄性";
			default:
				return "未知";
		}
	}

	private String getHealthStatusName(Integer healthStatus) {
		if (healthStatus == null) return "未知";
		switch (healthStatus) {
			case 1:
				return "健康";
			case 2:
				return "轻微不适";
			case 3:
				return "需要观察";
			case 4:
				return "治疗中";
			case 5:
				return "康复中";
			default:
				return "未知";
		}
	}

	/**
	 * 商品类型转换
	 */
	private String getProductTypeName(Integer productType) {
		if (productType == null) return "未知类型";
		switch (productType) {
			case 1:
				return "马术课程";
			case 2:
				return "马匹训练";
			case 3:
				return "体验活动";
			default:
				return "未知类型";
		}
	}

	/**
	 * 课程类型转换
	 */
	private String getLessonTypeName(Integer productType) {
		if (productType == null) return "未知";
		switch (productType) {
			case 1:
				return "私教课";
			case 2:
				return "小组课";
			case 3:
				return "体验课";
			default:
				return "未知";
		}
	}

	/**
	 * 预约改期
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO<Void> rescheduleBooking(Long bookingId, BookingRescheduleForm rescheduleForm) {
		try {
			// 1. 校验预约状态
			BookingEntity booking = bookingDao.selectById(bookingId);
			if (booking == null) {
				return ResponseDTO.userErrorParam("预约不存在");
			}

			if (booking.getBookingStatus() != 2) {
				return ResponseDTO.userErrorParam("只有已确认的预约才能改期");
			}

			// 2. 校验新时间的可用性（教练、马匹冲突检测）
			ConflictCheckResult conflictResult = checkTimeConflict(
				booking.getCoachId(),
				booking.getHorseId(),
				rescheduleForm.getNewStartTime(),
				rescheduleForm.getNewEndTime(),
				bookingId // 排除当前预约
			);

			if (conflictResult.isHasConflict()) {
				return ResponseDTO.userErrorParam("新时间段存在冲突：" + conflictResult.getConflictMessage());
			}

			// 3. 记录原时间用于日志
			LocalDateTime originalStartTime = booking.getStartTime();

			// 4. 更新预约时间
			booking.setStartTime(rescheduleForm.getNewStartTime());
			booking.setEndTime(rescheduleForm.getNewEndTime());
			booking.setUpdateTime(LocalDateTime.now());
			bookingDao.updateById(booking);

			// 5. 同步更新课表时间
			LessonScheduleEntity schedule = scheduleDao.selectByBookingId(bookingId);
			if (schedule != null) {
				schedule.setStartTime(rescheduleForm.getNewStartTime());
				schedule.setEndTime(rescheduleForm.getNewEndTime());
				schedule.setLessonDate(rescheduleForm.getNewStartTime().toLocalDate());
				scheduleDao.updateById(schedule);
			}

			// 6. 记录改期日志
			log.info("预约改期成功，预约ID：{}，原时间：{}，新时间：{}",
				bookingId, originalStartTime, rescheduleForm.getNewStartTime());

			return ResponseDTO.ok();

		} catch (Exception e) {
			log.error("预约改期失败，预约ID：{}", bookingId, e);
			return ResponseDTO.userErrorParam("预约改期失败");
		}
	}

	/**
	 * 时间冲突检测
	 */
	public ConflictCheckResult checkTimeConflict(Long coachId, Long horseId,
	                                           LocalDateTime startTime, LocalDateTime endTime,
	                                           Long excludeBookingId) {
		ConflictCheckResult result = new ConflictCheckResult();

		// 检查教练时间冲突
		List<BookingEntity> coachConflicts = bookingDao.findCoachTimeConflicts(
			coachId, startTime, endTime, excludeBookingId);
		if (!coachConflicts.isEmpty()) {
			result.addConflict("教练", coachConflicts);
		}

		// 检查马匹时间冲突
		List<BookingEntity> horseConflicts = bookingDao.findHorseTimeConflicts(
			horseId, startTime, endTime, excludeBookingId);
		if (!horseConflicts.isEmpty()) {
			result.addConflict("马匹", horseConflicts);
		}

		return result;
	}
}
