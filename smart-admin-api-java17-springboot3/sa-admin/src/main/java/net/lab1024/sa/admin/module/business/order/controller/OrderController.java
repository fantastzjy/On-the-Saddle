package net.lab1024.sa.admin.module.business.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderAddForm;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderQueryForm;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderStatusUpdateForm;
import net.lab1024.sa.admin.module.business.order.domain.vo.OrderDetailVO;
import net.lab1024.sa.admin.module.business.order.domain.vo.OrderListVO;
import net.lab1024.sa.admin.module.business.order.service.OrderBookingService;
import net.lab1024.sa.admin.module.business.order.service.OrderService;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理Controller
 * 第四阶段：订单和预约模块的核心接口
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/admin/order")
public class OrderController extends SupportBaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderBookingService orderBookingService;

    // ========================================
    // 订单基础CRUD操作
    // ========================================

    @Operation(summary = "创建订单", description = "用户选择教练和时间后创建订单，自动生成待确认预约")
    @PostMapping("/create")
    public ResponseDTO<Long> createOrder(@RequestBody @Valid OrderAddForm addForm) {
        return orderService.createOrder(addForm);
    }

    @Operation(summary = "订单列表查询", description = "支持多维度筛选的订单列表查询")
    @PostMapping("/query")
    public ResponseDTO<PageResult<OrderListVO>> queryOrderList(@RequestBody @Valid OrderQueryForm queryForm) {
        return orderService.queryOrderList(queryForm);
    }

    @Operation(summary = "订单详情", description = "获取订单详细信息，包括订单明细和预约信息")
    @GetMapping("/detail/{orderId}")
    public ResponseDTO<OrderDetailVO> getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @Operation(summary = "更新订单状态", description = "订单状态流转：待支付→已支付→已确认→已完成")
    @PostMapping("/updateStatus")
    public ResponseDTO<Void> updateOrderStatus(@RequestBody @Valid OrderStatusUpdateForm updateForm) {
        return orderService.updateOrderStatus(updateForm);
    }

    @Operation(summary = "取消订单", description = "取消订单并关联取消所有预约")
    @PostMapping("/cancel/{orderId}")
    public ResponseDTO<Void> cancelOrder(@PathVariable Long orderId, @RequestParam(required = false) String cancelReason) {
        return orderService.cancelOrder(orderId, cancelReason);
    }

    // ========================================
    // 订单状态操作
    // ========================================

    @Operation(summary = "确认支付", description = "将订单状态从待支付更新为已支付")
    @PostMapping("/confirmPayment/{orderId}")
    public ResponseDTO<Void> confirmPayment(@PathVariable Long orderId) {
        OrderStatusUpdateForm updateForm = new OrderStatusUpdateForm();
        updateForm.setOrderId(orderId);
        updateForm.setOrderStatus(2); // 已支付
        updateForm.setRemark("确认支付");
        return orderService.updateOrderStatus(updateForm);
    }

    @Operation(summary = "确认订单", description = "将订单状态从已支付更新为已确认，同时确认所有关联预约")
    @PostMapping("/confirm/{orderId}")
    public ResponseDTO<Void> confirmOrder(@PathVariable Long orderId) {
        OrderStatusUpdateForm updateForm = new OrderStatusUpdateForm();
        updateForm.setOrderId(orderId);
        updateForm.setOrderStatus(3); // 已确认
        updateForm.setRemark("订单确认");
        return orderService.updateOrderStatus(updateForm);
    }

    @Operation(summary = "完成订单", description = "将订单状态更新为已完成，同时完成所有关联预约")
    @PostMapping("/complete/{orderId}")
    public ResponseDTO<Void> completeOrder(@PathVariable Long orderId) {
        OrderStatusUpdateForm updateForm = new OrderStatusUpdateForm();
        updateForm.setOrderId(orderId);
        updateForm.setOrderStatus(4); // 已完成
        updateForm.setRemark("订单完成");
        return orderService.updateOrderStatus(updateForm);
    }

    // ========================================
    // 预约操作接口
    // ========================================

    @Operation(summary = "核销预约", description = "核销单个预约，从已确认状态直接跳转到已完成")
    @PostMapping("/booking/complete/{bookingId}")
    public ResponseDTO<Void> completeBooking(@PathVariable Long bookingId) {
        return orderBookingService.completeBooking(bookingId);
    }

    // ========================================
    // 统计查询接口
    // ========================================

    @Operation(summary = "订单统计", description = "获取订单各状态的统计数据")
    @GetMapping("/statistics")
    public ResponseDTO<Object> getOrderStatistics(@RequestParam(required = false) Long clubId) {
        // 这里可以实现订单统计逻辑
        return ResponseDTO.ok("统计功能待实现");
    }

    @Operation(summary = "今日订单", description = "获取今日创建的订单列表")
    @GetMapping("/today")
    public ResponseDTO<Object> getTodayOrders(@RequestParam(required = false) Long clubId) {
        // 这里可以实现今日订单查询逻辑
        return ResponseDTO.ok("今日订单功能待实现");
    }

    @Operation(summary = "待处理订单", description = "获取需要处理的订单（待支付、待确认等）")
    @GetMapping("/pending")
    public ResponseDTO<Object> getPendingOrders(@RequestParam(required = false) Long clubId) {
        // 这里可以实现待处理订单查询逻辑
        return ResponseDTO.ok("待处理订单功能待实现");
    }
}
