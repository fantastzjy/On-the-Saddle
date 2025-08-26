package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.openapi.domain.form.OrderDetailQueryForm;
import net.lab1024.sa.admin.module.openapi.domain.form.OrderQueryForm;
import net.lab1024.sa.admin.module.openapi.domain.vo.OrderDetailVO;
import net.lab1024.sa.admin.module.openapi.domain.vo.OrderListVO;
import net.lab1024.sa.admin.module.openapi.service.AppOrderService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序订单管理控制器
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@RestController
@RequestMapping("/app/order")
@Tag(name = "订单", description = "小程序订单列表和详情查询")
public class AppOrderController {

    @Resource
    private AppOrderService appOrderService;

    /**
     * 获取订单列表
     */
    @PostMapping("/list")
    @NoNeedLogin
    @Operation(summary = "获取订单列表", description = "支持按状态查询：1-课程预约(预留), 2-待完成, 3-已完成, 4-退款/售后")
    public ResponseDTO<PageResult<OrderListVO>> getOrderList(@RequestBody @Valid OrderQueryForm form) {
        return appOrderService.getOrderList(form);
    }

    /**
     * 获取订单详情
     */
    @PostMapping("/detail")
    @Operation(summary = "获取订单详情", description = "根据订单号查询订单详细信息，包括预约记录等")
    public ResponseDTO<OrderDetailVO> getOrderDetail(@RequestBody @Valid OrderDetailQueryForm form) {
        return appOrderService.getOrderDetail(form);
    }
}
