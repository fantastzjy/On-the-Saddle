package net.lab1024.sa.admin.module.openapi.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.order.constant.OrderStatusEnum;
import net.lab1024.sa.admin.module.business.order.constant.OrderTypeEnum;
import net.lab1024.sa.admin.module.business.order.constant.ProductTypeEnum;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.order.dao.PackageBalanceDao;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.order.domain.entity.PackageBalanceEntity;
import net.lab1024.sa.admin.module.business.product.dao.ProductCourseDao;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductCourseEntity;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.openapi.domain.form.OrderDetailQueryForm;
import net.lab1024.sa.admin.module.openapi.domain.form.OrderQueryForm;
import net.lab1024.sa.admin.module.openapi.domain.vo.OrderDetailVO;
import net.lab1024.sa.admin.module.openapi.domain.vo.OrderListVO;
import net.lab1024.sa.admin.util.MemberRequestUtil;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单服务
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class AppOrderService {

	@Resource
	private OrderDao orderDao;

	@Resource
	private ClubDao clubDao;

	@Resource
	private CoachDao coachDao;

	@Resource
	private PackageBalanceDao packageBalanceDao;

	@Resource
	private BookingDao bookingDao;

	@Resource
	private ProductCourseDao productCourseDao;

	@Resource
	private ProductDao productDao;

	/**
	 * 获取订单列表
	 */
	public ResponseDTO<PageResult<OrderListVO>> getOrderList(OrderQueryForm form) {
		try {
			// 1. 获取当前登录会员ID
			// Long memberId = MemberRequestUtil.getRequestMemberId();
			// if (memberId == null) {
			//     return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID, "请先登录");
			// }

			Long memberId = 11L;

			// 2. 构建查询条件
			List<Integer> statusList = getStatusByGroup(form.getStatus());

			// 3. 分页查询订单
			Page<OrderEntity> page = new Page<>(form.getPageNum(), form.getPageSize());
			LambdaQueryWrapper<OrderEntity> wrapper = new LambdaQueryWrapper<OrderEntity>()
					.eq(OrderEntity::getMemberId, memberId)
					.in(OrderEntity::getOrderStatus, statusList)
					.eq(OrderEntity::getIsDelete, 0)
					.orderByDesc(OrderEntity::getCreateTime);

			IPage<OrderEntity> orderPage = orderDao.selectPage(page, wrapper);

			// 4. 检查是否有数据
			List<OrderEntity> orders = orderPage.getRecords();
			if (CollUtil.isEmpty(orders)) {
				PageResult<OrderListVO> emptyResult = new PageResult<>();
				emptyResult.setList(new ArrayList<>());
				emptyResult.setTotal(0L);
				emptyResult.setPageNum((long) form.getPageNum());
				emptyResult.setPageSize((long) form.getPageSize());
				emptyResult.setPages(0L);
				emptyResult.setEmptyFlag(true);
				return ResponseDTO.ok(emptyResult);
			}

			// 5. 组装VO数据
			List<OrderListVO> voList = buildOrderListVOs(orders);

			PageResult<OrderListVO> pageResult = new PageResult<>();
			pageResult.setList(voList);
			pageResult.setTotal(orderPage.getTotal());
			pageResult.setPageNum((long) form.getPageNum());
			pageResult.setPageSize((long) form.getPageSize());
			pageResult.setPages((orderPage.getTotal() + (long) form.getPageSize() - 1) / (long) form.getPageSize());
			pageResult.setEmptyFlag(false);

			return ResponseDTO.ok(pageResult);

		} catch (Exception e) {
			log.error("查询订单列表失败", e);
			return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "查询订单列表失败");
		}
	}

	/**
	 * 获取订单详情
	 */
	public ResponseDTO<OrderDetailVO> getOrderDetail(OrderDetailQueryForm form) {
		try {
			// 1. 获取当前登录会员ID
			Long memberId = MemberRequestUtil.getRequestMemberId();
			if (memberId == null) {
				return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID, "请先登录");
			}

			// 2. 查询订单
			OrderEntity order = orderDao.selectOne(
					new LambdaQueryWrapper<OrderEntity>()
							.eq(OrderEntity::getOrderNo, form.getOrderNo())
							.eq(OrderEntity::getMemberId, memberId)
							.eq(OrderEntity::getIsDelete, 0)
			);

			if (order == null) {
				return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "订单不存在");
			}

			// 3. 构建详情VO
			OrderDetailVO detailVO = buildOrderDetailVO(order);

			return ResponseDTO.ok(detailVO);

		} catch (Exception e) {
			log.error("查询订单详情失败，订单号: {}", form.getOrderNo(), e);
			return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "查询订单详情失败");
		}
	}

	/**
	 * 根据状态分组获取状态列表
	 */
	private List<Integer> getStatusByGroup(String statusGroup) {
		if (StrUtil.isBlank(statusGroup)) {
			return Arrays.asList(1, 2, 3, 4, 5); // 全部状态
		}

		switch (statusGroup) {
			case "pending":   // 待完成
				return Arrays.asList(1, 2); // 待支付、已支付
			case "completed": // 已完成
				return Arrays.asList(3);     // 已核销
			case "after_sale": // 退款/售后
				return Arrays.asList(4, 5);  // 已取消、已退款
			default:
				return Arrays.asList(1, 2, 3, 4, 5); // 全部
		}
	}

	/**
	 * 构建订单列表VO
	 */
	private List<OrderListVO> buildOrderListVOs(List<OrderEntity> orders) {
		// 收集需要批量查询的ID
		Set<Long> clubIds = orders.stream().map(OrderEntity::getClubId).collect(Collectors.toSet());
		Set<Long> coachIds = orders.stream()
				.map(OrderEntity::getCoachId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Set<Long> productIds = orders.stream()
				.map(OrderEntity::getProductId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		List<Long> orderIds = orders.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());

		// 批量查询关联数据
		Map<Long, ClubEntity> clubMap = new HashMap<>();
		if (!clubIds.isEmpty()) {
			List<ClubEntity> clubs = clubDao.selectBatchIds(clubIds);
			clubMap = clubs.stream().collect(Collectors.toMap(ClubEntity::getClubId, club -> club));
		}

		Map<Long, CoachEntity> coachMap = new HashMap<>();
		if (!coachIds.isEmpty()) {
			List<CoachEntity> coaches = coachDao.selectBatchIds(coachIds);
			coachMap = coaches.stream().collect(Collectors.toMap(CoachEntity::getCoachId, coach -> coach));
		}

		Map<Long, ProductEntity> productMap = new HashMap<>();
		if (!productIds.isEmpty()) {
			List<ProductEntity> products = productDao.selectBatchIds(productIds);
			productMap = products.stream().collect(Collectors.toMap(ProductEntity::getProductId, product -> product));
		}

		List<PackageBalanceEntity> balanceList = packageBalanceDao.selectByOrderIds(orderIds);
		Map<Long, PackageBalanceEntity> balanceMap = balanceList.stream()
				.collect(Collectors.toMap(PackageBalanceEntity::getOrderId, balance -> balance));

		// 构建VO列表
		Map<Long, ClubEntity> finalClubMap = clubMap;
		Map<Long, CoachEntity> finalCoachMap = coachMap;
		Map<Long, ProductEntity> finalProductMap = productMap;
		return orders.stream().map(order -> {
			OrderListVO vo = SmartBeanUtil.copy(order, OrderListVO.class);

			// 设置马场信息
			ClubEntity club = finalClubMap.get(order.getClubId());
			if (club != null) {
				vo.setClubName(club.getClubName());
			}

			// 设置教练信息
			if (order.getCoachId() != null) {
				CoachEntity coach = finalCoachMap.get(order.getCoachId());
				if (coach != null) {
					vo.setCoachNo(coach.getCoachNo());
					vo.setCoachName(coach.getActualName());
					vo.setCoachAvatarUrl(coach.getAvatarUrl());
					vo.setCoachGender(coach.getGender());
				}
			}

			// 设置产品编号信息
			if (order.getProductId() != null) {
				ProductEntity product = finalProductMap.get(order.getProductId());
				if (product != null) {
					vo.setProductCode(product.getProductCode());
				}
			}

			// 解析时间段信息
			vo.setTimeSlots(parseTimeSlots(order.getPreferredTimes()));

			// 设置课时包剩余数量
			if (OrderTypeEnum.PACKAGE.getCode().equals(order.getOrderType())) { // 课时包订单
				PackageBalanceEntity balance = balanceMap.get(order.getOrderId());
				if (balance != null) {
					vo.setRemainingCount(balance.getRemainingCount());
				}
			} else {
				// 其他类型显示退款数量（暂时设为数量，后续可根据业务调整）
				vo.setRefundQuantity(order.getQuantity());
			}

			// 设置状态文本
			vo.setStatusText(OrderStatusEnum.getDescByCode(order.getOrderStatus()));

			// 设置地点信息（默认为俱乐部地址，后续可扩展）
			if (club != null) {
				vo.setLocation(club.getAddress());
			}

			// 设置费用信息
			setOrderFeeInfo(vo, order);

			return vo;
		}).collect(Collectors.toList());
	}

	/**
	 * 构建订单详情VO
	 */
	private OrderDetailVO buildOrderDetailVO(OrderEntity order) {
		OrderDetailVO detailVO = new OrderDetailVO();

		// 基础订单信息
		detailVO.setOrderNo(order.getOrderNo());
		detailVO.setOrderStatus(order.getOrderStatus());
		detailVO.setStatusText(OrderStatusEnum.getDescByCode(order.getOrderStatus()));
		detailVO.setOrderType(order.getOrderType());
		detailVO.setOrderTypeText(OrderTypeEnum.getDescByCode(order.getOrderType()));
		detailVO.setCreateTime(order.getCreateTime());
		detailVO.setPaymentTime(order.getPaymentTime());
		detailVO.setRemark(order.getRemark());

		// 马场信息
		ClubEntity club = clubDao.selectById(order.getClubId());
		if (club != null) {
			OrderDetailVO.ClubDetailInfo clubInfo = new OrderDetailVO.ClubDetailInfo();
			clubInfo.setClubName(club.getClubName());
			clubInfo.setAddress(club.getAddress());
			clubInfo.setPhone(club.getPhone());
			detailVO.setClubInfo(clubInfo);
		}

		// 教练信息
		if (order.getCoachId() != null) {
			CoachEntity coach = coachDao.selectById(order.getCoachId());
			if (coach != null) {
				OrderDetailVO.CoachDetailInfo coachInfo = new OrderDetailVO.CoachDetailInfo();
				coachInfo.setCoachNo(coach.getCoachNo());
				coachInfo.setActualName(coach.getActualName());
				coachInfo.setAvatarUrl(coach.getAvatarUrl());
				coachInfo.setGender(coach.getGender());
				coachInfo.setCoachFee(coach.getCoachFee());
				coachInfo.setSpecialties(parseSpecialties(coach.getSpecialties()));
				detailVO.setCoachInfo(coachInfo);
			}
		}

		// 产品信息
		OrderDetailVO.ProductDetailInfo productInfo = new OrderDetailVO.ProductDetailInfo();
		productInfo.setProductName(order.getProductName());
		productInfo.setProductType(order.getProductType());
		productInfo.setProductTypeText(ProductTypeEnum.getDescByCode(order.getProductType()));
		productInfo.setTimeSlots(parseTimeSlots(order.getPreferredTimes()));

		// 获取产品编号
		if (order.getProductId() != null) {
			ProductEntity product = productDao.selectById(order.getProductId());
			if (product != null) {
				productInfo.setProductCode(product.getProductCode());
			}
		}

		// 获取课程配置信息
		if (order.getProductId() != null && ProductTypeEnum.COURSE.getCode().equals(order.getProductType())) {
			ProductCourseEntity courseConfig = productCourseDao.selectOne(
					new LambdaQueryWrapper<ProductCourseEntity>()
							.eq(ProductCourseEntity::getProductId, order.getProductId())
			);
			if (courseConfig != null) {
				productInfo.setDurationMinutes(courseConfig.getDurationMinutes());
				productInfo.setDurationPeriods(courseConfig.getDurationPeriods());
			}
		}

		if (club != null) {
			productInfo.setLocation(club.getAddress());
		}
		detailVO.setProductInfo(productInfo);

		// 付款信息
		OrderDetailVO.PaymentDetailInfo paymentInfo = new OrderDetailVO.PaymentDetailInfo();
		paymentInfo.setQuantity(order.getQuantity());
		paymentInfo.setUnitPrice(order.getUnitPrice());
		paymentInfo.setTotalAmount(order.getTotalAmount());
		paymentInfo.setPaidAmount(order.getPaidAmount());
		paymentInfo.setPaymentMethod(order.getPaymentMethod());

		// 从课程配置获取费用信息
		setDetailPaymentFeeInfo(paymentInfo, order);
		detailVO.setPaymentInfo(paymentInfo);

		// 课时包余额信息（仅课时包订单）
		if (OrderTypeEnum.PACKAGE.getCode().equals(order.getOrderType())) {
			PackageBalanceEntity balance = packageBalanceDao.selectByOrderId(order.getOrderId());
			if (balance != null) {
				OrderDetailVO.PackageBalanceInfo balanceInfo = new OrderDetailVO.PackageBalanceInfo();
				balanceInfo.setTotalCount(balance.getTotalCount());
				balanceInfo.setUsedCount(balance.getUsedCount());
				balanceInfo.setRemainingCount(balance.getRemainingCount());
				balanceInfo.setExpireDate(balance.getExpireDate());
				balanceInfo.setStatus(balance.getStatus());
				detailVO.setPackageBalance(balanceInfo);
			}
		}

		// 预约记录列表
		List<BookingEntity> bookings = bookingDao.selectByOrderId(order.getOrderId());
		List<OrderDetailVO.BookingRecordInfo> bookingRecords = bookings.stream().map(booking -> {
			OrderDetailVO.BookingRecordInfo recordInfo = new OrderDetailVO.BookingRecordInfo();
			recordInfo.setBookingId(booking.getBookingId());
			recordInfo.setBookingStatus(booking.getBookingStatus());
			recordInfo.setStatusText(getBookingStatusText(booking.getBookingStatus()));
			recordInfo.setStartTime(booking.getStartTime());
			recordInfo.setEndTime(booking.getEndTime());
			recordInfo.setActualCoachFee(booking.getActualCoachFee());
			recordInfo.setActualHorseFee(booking.getActualHorseFee());
			recordInfo.setPackageConsumeCount(booking.getPackageConsumeCount());
			recordInfo.setArrivalTime(booking.getArrivalTime());
			recordInfo.setCompletionTime(booking.getCompletionTime());
			return recordInfo;
		}).collect(Collectors.toList());
		detailVO.setBookingRecords(bookingRecords);

		return detailVO;
	}

	/**
	 * 解析时间段信息
	 */
	private List<OrderListVO.TimeSlotInfo> parseTimeSlots(String preferredTimesJson) {
		if (StrUtil.isBlank(preferredTimesJson)) {
			return new ArrayList<>();
		}

		try {
			JSONArray timeArray = JSONUtil.parseArray(preferredTimesJson);
			List<OrderListVO.TimeSlotInfo> timeSlots = new ArrayList<>();

			for (Object item : timeArray) {
				JSONObject timeObj = (JSONObject) item;
				String date = timeObj.getStr("date");
				JSONArray slots = timeObj.getJSONArray("timeSlots");

				if (slots != null) {
					for (Object slot : slots) {
						OrderListVO.TimeSlotInfo timeSlot = new OrderListVO.TimeSlotInfo();
						timeSlot.setDate(date);
						timeSlot.setTimeRange(slot.toString());
						timeSlots.add(timeSlot);
					}
				}
			}

			return timeSlots;
		} catch (Exception e) {
			log.warn("解析时间段JSON失败: {}", preferredTimesJson, e);
			return new ArrayList<>();
		}
	}

	/**
	 * 解析专长领域
	 */
	private List<String> parseSpecialties(String specialties) {
		if (StrUtil.isBlank(specialties)) {
			return new ArrayList<>();
		}
		return Arrays.stream(specialties.split(","))
				.map(String::trim)
				.filter(StrUtil::isNotBlank)
				.collect(Collectors.toList());
	}

	/**
	 * 设置订单费用信息
	 */
	private void setOrderFeeInfo(OrderListVO vo, OrderEntity order) {
		if (order.getProductId() != null && ProductTypeEnum.COURSE.getCode().equals(order.getProductType())) {
			// 课程类型，从产品配置获取费用信息
			ProductCourseEntity courseConfig = productCourseDao.selectOne(
					new LambdaQueryWrapper<ProductCourseEntity>()
							.eq(ProductCourseEntity::getProductId, order.getProductId())
			);
			if (courseConfig != null) {
				vo.setCoachFee(courseConfig.getCoachFee());
				vo.setHorseFee(courseConfig.getHorseFee());
			}
		} else {
			// 其他类型，使用默认值
			vo.setCoachFee(BigDecimal.ZERO);
			vo.setHorseFee(BigDecimal.ZERO);
		}
	}

	/**
	 * 设置详情页付款费用信息
	 */
	private void setDetailPaymentFeeInfo(OrderDetailVO.PaymentDetailInfo paymentInfo, OrderEntity order) {
		if (order.getProductId() != null && ProductTypeEnum.COURSE.getCode().equals(order.getProductType())) {
			// 课程类型，从产品配置获取费用信息
			ProductCourseEntity courseConfig = productCourseDao.selectOne(
					new LambdaQueryWrapper<ProductCourseEntity>()
							.eq(ProductCourseEntity::getProductId, order.getProductId())
			);
			if (courseConfig != null) {
				paymentInfo.setCoachFee(courseConfig.getCoachFee());
				paymentInfo.setHorseFee(courseConfig.getHorseFee());
			}
		} else {
			// 其他类型，使用默认值
			paymentInfo.setCoachFee(BigDecimal.ZERO);
			paymentInfo.setHorseFee(BigDecimal.ZERO);
		}
	}

	/**
	 * 获取预约状态文本
	 */
	private String getBookingStatusText(Integer status) {
		if (status == null) {
			return "";
		}
		switch (status) {
			case 1:
				return "已预约";
			case 2:
				return "已核销";
			case 3:
				return "已取消";
			case 4:
				return "已过期";
			default:
				return "";
		}
	}
}
