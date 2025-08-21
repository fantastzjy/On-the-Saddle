package net.lab1024.sa.admin.module.business.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.dao.BookingDao;
import net.lab1024.sa.admin.module.business.booking.domain.entity.BookingEntity;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.order.dao.OrderDao;
import net.lab1024.sa.admin.module.business.order.dao.OrderItemDao;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderEntity;
import net.lab1024.sa.admin.module.business.order.domain.entity.OrderItemEntity;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderAddForm;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderQueryForm;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderStatusUpdateForm;
import net.lab1024.sa.admin.module.business.order.domain.vo.OrderDetailVO;
import net.lab1024.sa.admin.module.business.order.domain.vo.OrderListVO;
import net.lab1024.sa.admin.module.business.order.domain.enums.PaymentMethodEnum;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单管理服务
 * 实现第四阶段订单预约管理的核心业务逻辑
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private OrderBookingService orderBookingService;

    @Autowired
    private MemberDao memberDao;

    // ========================================
    // 订单核心业务逻辑
    // ========================================

    /**
     * 创建订单
     * 核心流程：创建订单 -> 创建订单明细 -> 自动生成预约（待教练确认）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Long> createOrder(OrderAddForm addForm) {
        try {
            // 1. 创建订单主表
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderNo(generateOrderNo());
            orderEntity.setClubId(addForm.getClubId());
            orderEntity.setMemberId(addForm.getMemberId());
            orderEntity.setOrderType(addForm.getOrderType());
            orderEntity.setOrderStatus(1); // 1-待支付
            orderEntity.setPaymentMethod(addForm.getPaymentMethod());
            orderEntity.setRemark(addForm.getRemark());
            orderEntity.setCreateTime(LocalDateTime.now());
            orderEntity.setIsValid(true);
            orderEntity.setIsDelete(false);

            // 计算订单总金额
            BigDecimal totalAmount = addForm.getOrderItems().stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            orderEntity.setTotalAmount(totalAmount);
            orderEntity.setPaidAmount(BigDecimal.ZERO);

            orderDao.insert(orderEntity);
            log.info("创建订单成功，订单ID：{}, 订单号：{}", orderEntity.getOrderId(), orderEntity.getOrderNo());

            // 2. 创建订单明细
            List<OrderItemEntity> orderItems = new ArrayList<>();
            for (OrderAddForm.OrderItemForm itemForm : addForm.getOrderItems()) {
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                orderItemEntity.setOrderId(orderEntity.getOrderId());
                orderItemEntity.setProductId(itemForm.getProductId());
                orderItemEntity.setProductName(itemForm.getProductName());
                orderItemEntity.setProductType(itemForm.getProductType());
                orderItemEntity.setQuantity(itemForm.getQuantity());
                orderItemEntity.setUnitPrice(itemForm.getUnitPrice());
                orderItemEntity.setTotalPrice(itemForm.getUnitPrice().multiply(BigDecimal.valueOf(itemForm.getQuantity())));
                orderItemEntity.setCoachId(itemForm.getCoachId());
                orderItemEntity.setPreferredTime(itemForm.getPreferredTime());
                orderItemEntity.setItemConfig(itemForm.getItemConfig());
                orderItemEntity.setCreateTime(LocalDateTime.now());

                orderItems.add(orderItemEntity);
            }

            // 批量插入订单明细
            for (OrderItemEntity item : orderItems) {
                orderItemDao.insert(item);
            }
            log.info("创建订单明细成功，共{}条", orderItems.size());

            // 3. 自动生成预约（待教练确认）
            ResponseDTO<Void> bookingResult = orderBookingService.createBookingsFromOrder(
                    orderEntity.getOrderId(), orderItems);
            if (!bookingResult.getOk()) {
                log.error("自动生成预约失败：{}", bookingResult.getMsg());
                throw new RuntimeException("自动生成预约失败：" + bookingResult.getMsg());
            }

            log.info("订单创建完成，订单ID：{}", orderEntity.getOrderId());
            return ResponseDTO.ok(orderEntity.getOrderId());

        } catch (Exception e) {
            log.error("创建订单失败", e);
            return ResponseDTO.userErrorParam("创建订单失败：" + e.getMessage());
        }
    }

    /**
     * 查询订单列表
     */
    public ResponseDTO<PageResult<OrderListVO>> queryOrderList(OrderQueryForm queryForm) {
        try {
            Page<OrderEntity> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
            LambdaQueryWrapper<OrderEntity> queryWrapper = buildOrderQueryWrapper(queryForm);

            IPage<OrderEntity> pageResult = orderDao.selectPage(page, queryWrapper);

            // 转换为VO
            Page<OrderEntity> resultPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
            PageResult<OrderListVO> result = SmartPageUtil.convert2PageResult(resultPage, pageResult.getRecords(), OrderListVO.class);

            // 补充额外信息
            enhanceOrderListData(result.getList());

            log.info("查询订单列表成功，共{}条记录", result.getTotal());
            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("查询订单列表失败", e);
            return ResponseDTO.userErrorParam("查询订单列表失败");
        }
    }

    /**
     * 获取订单详情
     */
    public ResponseDTO<OrderDetailVO> getOrderDetail(Long orderId) {
        try {
            OrderEntity orderEntity = orderDao.selectById(orderId);
            if (orderEntity == null || orderEntity.getIsDelete()) {
                return ResponseDTO.userErrorParam("订单不存在");
            }

            OrderDetailVO orderDetailVO = SmartBeanUtil.copy(orderEntity, OrderDetailVO.class);

            // 补充会员信息
            if (orderEntity.getMemberId() != null) {
                MemberEntity member = memberDao.selectById(orderEntity.getMemberId());
                if (member != null) {
                    orderDetailVO.setMemberName(member.getActualName());
                    orderDetailVO.setMemberPhone(member.getPhone());
                }
            }

            // 补充订单明细
            List<OrderItemEntity> orderItems = orderItemDao.selectList(
                new LambdaQueryWrapper<OrderItemEntity>()
                    .eq(OrderItemEntity::getOrderId, orderId)
            );
            List<OrderDetailVO.OrderItemVO> orderItemVOs = SmartBeanUtil.copyList(orderItems, OrderDetailVO.OrderItemVO.class);
            orderDetailVO.setOrderItems(orderItemVOs);

            // 补充预约信息
            List<BookingEntity> bookings = bookingDao.selectList(
                new LambdaQueryWrapper<BookingEntity>()
                    .eq(BookingEntity::getOrderId, orderId)
            );
            List<OrderDetailVO.BookingInfoVO> bookingVOs = SmartBeanUtil.copyList(bookings, OrderDetailVO.BookingInfoVO.class);
            orderDetailVO.setBookings(bookingVOs);

            // 补充状态名称等
            enhanceOrderDetailData(orderDetailVO);

            log.info("获取订单详情成功，订单ID：{}", orderId);
            return ResponseDTO.ok(orderDetailVO);

        } catch (Exception e) {
            log.error("获取订单详情失败，订单ID：{}", orderId, e);
            return ResponseDTO.userErrorParam("获取订单详情失败");
        }
    }

    /**
     * 更新订单状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> updateOrderStatus(OrderStatusUpdateForm updateForm) {
        try {
            OrderEntity orderEntity = orderDao.selectById(updateForm.getOrderId());
            if (orderEntity == null || orderEntity.getIsDelete()) {
                return ResponseDTO.userErrorParam("订单不存在");
            }

            // 状态流转校验
            if (!isValidStatusTransition(orderEntity.getOrderStatus(), updateForm.getOrderStatus())) {
                return ResponseDTO.userErrorParam("订单状态流转不合法");
            }

            // 更新订单状态
            orderEntity.setOrderStatus(updateForm.getOrderStatus());
            orderEntity.setUpdateTime(LocalDateTime.now());

            // 根据状态设置对应时间
            switch (updateForm.getOrderStatus()) {
                case 2: // 已支付
                    orderEntity.setPaymentTime(LocalDateTime.now());
                    orderEntity.setPaidAmount(orderEntity.getTotalAmount());
                    log.info("订单支付成功，订单ID：{}，将自动确认预约", updateForm.getOrderId());
                    break;
            }

            if (SmartStringUtil.isNotBlank(updateForm.getRemark())) {
                orderEntity.setRemark(updateForm.getRemark());
            }

            orderDao.updateById(orderEntity);

            // 根据订单状态变更，触发相应的预约状态变更
            try {
                handleOrderStatusChange(orderEntity, updateForm.getOrderStatus());
            } catch (Exception e) {
                log.error("处理订单状态变更时发生异常，订单ID：{}，状态：{}", updateForm.getOrderId(), updateForm.getOrderStatus(), e);
                // 如果是支付状态但预约确认失败，需要记录异常但不回滚订单状态
                if (updateForm.getOrderStatus() == 2) {
                    log.warn("订单支付成功但预约确认失败，订单ID：{}，需要人工处理", updateForm.getOrderId());
                    // 可以在这里添加告警或重试机制
                } else {
                    throw e; // 其他状态变更失败则抛出异常，触发事务回滚
                }
            }

            log.info("更新订单状态成功，订单ID：{}，新状态：{}", updateForm.getOrderId(), updateForm.getOrderStatus());
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("更新订单状态失败，订单ID：{}", updateForm.getOrderId(), e);
            return ResponseDTO.userErrorParam("更新订单状态失败");
        }
    }

    /**
     * 取消订单
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Void> cancelOrder(Long orderId, String cancelReason) {
        try {
            OrderEntity orderEntity = orderDao.selectById(orderId);
            if (orderEntity == null || orderEntity.getIsDelete()) {
                return ResponseDTO.userErrorParam("订单不存在");
            }

            if (orderEntity.getOrderStatus() >= 3) {
                return ResponseDTO.userErrorParam("订单已核销，无法取消");
            }

            // 更新订单状态为已取消
            orderEntity.setOrderStatus(4);
            orderEntity.setUpdateTime(LocalDateTime.now());
            if (SmartStringUtil.isNotBlank(cancelReason)) {
                orderEntity.setRemark(orderEntity.getRemark() + " [取消原因：" + cancelReason + "]");
            }

            orderDao.updateById(orderEntity);

            // 取消所有关联的预约
            orderBookingService.cancelBookingsByOrderId(orderId, cancelReason);

            log.info("取消订单成功，订单ID：{}", orderId);
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("取消订单失败，订单ID：{}", orderId, e);
            return ResponseDTO.userErrorParam("取消订单失败");
        }
    }

    // ========================================
    // 私有辅助方法
    // ========================================

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                String.format("%03d", (int) (Math.random() * 1000));
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<OrderEntity> buildOrderQueryWrapper(OrderQueryForm queryForm) {
        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(OrderEntity::getIsDelete, false);

        if (SmartStringUtil.isNotBlank(queryForm.getOrderNo())) {
            queryWrapper.like(OrderEntity::getOrderNo, queryForm.getOrderNo());
        }
        if (queryForm.getClubId() != null) {
            queryWrapper.eq(OrderEntity::getClubId, queryForm.getClubId());
        }
        if (queryForm.getMemberId() != null) {
            queryWrapper.eq(OrderEntity::getMemberId, queryForm.getMemberId());
        }
        if (queryForm.getOrderType() != null) {
            queryWrapper.eq(OrderEntity::getOrderType, queryForm.getOrderType());
        }
        if (queryForm.getOrderStatus() != null) {
            queryWrapper.eq(OrderEntity::getOrderStatus, queryForm.getOrderStatus());
        }
        if (queryForm.getCreateTimeStart() != null) {
            queryWrapper.ge(OrderEntity::getCreateTime, queryForm.getCreateTimeStart());
        }
        if (queryForm.getCreateTimeEnd() != null) {
            queryWrapper.le(OrderEntity::getCreateTime, queryForm.getCreateTimeEnd());
        }
        if (queryForm.getPaymentTimeStart() != null) {
            queryWrapper.ge(OrderEntity::getPaymentTime, queryForm.getPaymentTimeStart());
        }
        if (queryForm.getPaymentTimeEnd() != null) {
            queryWrapper.le(OrderEntity::getPaymentTime, queryForm.getPaymentTimeEnd());
        }

        queryWrapper.orderByDesc(OrderEntity::getCreateTime);
        return queryWrapper;
    }

    /**
     * 补充订单列表数据
     */
    private void enhanceOrderListData(List<OrderListVO> orderList) {
        if (orderList.isEmpty()) {
            return;
        }

        List<Long> orderIds = orderList.stream()
                .map(OrderListVO::getOrderId)
                .collect(Collectors.toList());

        // 统计订单明细数量
        Map<Long, Integer> itemCountMap = getOrderItemCounts(orderIds);

        // 统计预约数量
        Map<Long, Integer> bookingCountMap = getBookingCounts(orderIds);
        Map<Long, Integer> pendingBookingCountMap = getPendingBookingCounts(orderIds);

        for (OrderListVO orderVO : orderList) {
            // 设置状态名称
            orderVO.setOrderTypeName(getOrderTypeName(orderVO.getOrderType()));
            orderVO.setOrderStatusName(getOrderStatusName(orderVO.getOrderStatus()));

            // 设置统计数据
            orderVO.setItemCount(itemCountMap.getOrDefault(orderVO.getOrderId(), 0));
            orderVO.setBookingCount(bookingCountMap.getOrDefault(orderVO.getOrderId(), 0));
            orderVO.setPendingBookingCount(pendingBookingCountMap.getOrDefault(orderVO.getOrderId(), 0));
        }
    }

    /**
     * 补充订单详情数据
     */
    private void enhanceOrderDetailData(OrderDetailVO orderDetail) {
        orderDetail.setOrderTypeName(getOrderTypeName(orderDetail.getOrderType()));
        orderDetail.setOrderStatusName(getOrderStatusName(orderDetail.getOrderStatus()));
        
        // 补充支付方式名称
        orderDetail.setPaymentMethodName(PaymentMethodEnum.getNameByCode(orderDetail.getPaymentMethod()));

        // 补充订单明细中的类型名称
        if (orderDetail.getOrderItems() != null) {
            for (OrderDetailVO.OrderItemVO item : orderDetail.getOrderItems()) {
                item.setProductTypeName(getProductTypeName(item.getProductType()));
            }
        }

        // 补充预约信息中的状态名称
        if (orderDetail.getBookings() != null) {
            for (OrderDetailVO.BookingInfoVO booking : orderDetail.getBookings()) {
                booking.setBookingStatusName(getBookingStatusName(booking.getBookingStatus()));
            }
        }
    }

    /**
     * 验证订单状态流转是否合法
     */
    private boolean isValidStatusTransition(Integer currentStatus, Integer newStatus) {
        // 订单状态：1-待支付 2-已支付 3-已核销 4-已取消 5-已退款
        if (currentStatus.equals(newStatus)) {
            return true;
        }

        switch (currentStatus) {
            case 1: // 待支付
                return newStatus == 2 || newStatus == 4; // 可以支付或取消
            case 2: // 已支付
                return newStatus == 3 || newStatus == 4 || newStatus == 5; // 可以核销、取消或退款
            case 3: // 已核销
            case 4: // 已取消
            case 5: // 已退款
                return false; // 终态，不能再变更
            default:
                return false;
        }
    }

    /**
     * 处理订单状态变更引起的预约状态变更
     */
    private void handleOrderStatusChange(OrderEntity orderEntity, Integer newStatus) {
        switch (newStatus) {
            case 2: // 已支付 -> 自动确认所有预约并生成课表
                orderBookingService.confirmBookingsByOrderId(orderEntity.getOrderId());
                break;
            case 3: // 已核销 -> 完成所有预约
                orderBookingService.completeBookingsByOrderId(orderEntity.getOrderId());
                break;
            case 4: // 已取消 -> 取消所有预约
                orderBookingService.cancelBookingsByOrderId(orderEntity.getOrderId(), "订单已取消");
                break;
            case 5: // 已退款 -> 取消所有预约
                orderBookingService.cancelBookingsByOrderId(orderEntity.getOrderId(), "订单已退款");
                break;
        }
    }

    // ========================================
    // 数据查询辅助方法
    // ========================================

    private Map<Long, Integer> getOrderItemCounts(List<Long> orderIds) {
        // 实现订单明细数量统计逻辑
        return new HashMap<>();
    }

    private Map<Long, Integer> getBookingCounts(List<Long> orderIds) {
        // 实现预约数量统计逻辑
        return new HashMap<>();
    }

    private Map<Long, Integer> getPendingBookingCounts(List<Long> orderIds) {
        // 实现待确认预约数量统计逻辑
        return new HashMap<>();
    }

    private String getOrderTypeName(Integer orderType) {
        switch (orderType) {
            case 1: return "课程订单";
            case 2: return "课时包订单";
            case 3: return "活动订单";
            default: return "未知类型";
        }
    }

    private String getOrderStatusName(Integer orderStatus) {
        switch (orderStatus) {
            case 1: return "待支付";
            case 2: return "已支付";
            case 3: return "已核销";
            case 4: return "已取消";
            case 5: return "已退款";
            default: return "未知状态";
        }
    }

    private String getProductTypeName(Integer productType) {
        switch (productType) {
            case 1: return "课程";
            case 2: return "课时包";
            case 3: return "活动";
            default: return "未知类型";
        }
    }

    private String getBookingStatusName(Integer bookingStatus) {
        switch (bookingStatus) {
            case 1: return "待确认";
            case 2: return "已确认";
            case 3: return "进行中";
            case 4: return "已完成";
            case 5: return "已取消";
            case 6: return "未到场";
            default: return "未知状态";
        }
    }
}