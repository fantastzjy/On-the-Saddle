package net.lab1024.sa.admin.module.business.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.booking.domain.form.BatchBookingForm;
import net.lab1024.sa.admin.module.business.booking.domain.form.BookingQueryForm;
import net.lab1024.sa.admin.module.business.booking.domain.form.BookingRescheduleForm;
import net.lab1024.sa.admin.module.business.booking.domain.form.PackageBookingCreateForm;
import net.lab1024.sa.admin.module.business.booking.domain.vo.BookingDetailVO;
import net.lab1024.sa.admin.module.business.booking.domain.vo.BookingListVO;
import net.lab1024.sa.admin.module.business.booking.domain.vo.BookingSimpleListVO;
import net.lab1024.sa.admin.module.business.booking.domain.vo.ConflictCheckResult;
import net.lab1024.sa.admin.module.business.booking.service.BookingService;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 预约管理Controller
 * 
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Tag(name = "预约管理")
@RestController
@RequestMapping("/api/admin/booking")
public class BookingController extends SupportBaseController {

    @Autowired
    private BookingService bookingService;

    // ========================================
    // 预约基础查询操作
    // ========================================

    @Operation(summary = "预约列表查询", description = "分页查询预约列表，支持多维度筛选")
    @PostMapping("/query")
    public ResponseDTO<PageResult<BookingListVO>> queryBookingList(@RequestBody @Valid BookingQueryForm queryForm) {
        return bookingService.queryBookingList(queryForm);
    }

    @Operation(summary = "预约简化列表查询", description = "分页查询预约简化列表，用于列表视图快速显示")
    @PostMapping("/simple-query")
    public ResponseDTO<PageResult<BookingSimpleListVO>> querySimpleBookingList(@RequestBody @Valid BookingQueryForm queryForm) {
        return bookingService.querySimpleBookingList(queryForm);
    }

    @Operation(summary = "预约详情", description = "获取预约详细信息")
    @GetMapping("/detail/{bookingId}")
    public ResponseDTO<BookingDetailVO> getBookingDetail(@PathVariable Long bookingId) {
        return bookingService.getBookingDetail(bookingId);
    }

    // ========================================
    // 预约状态操作
    // ========================================

    @Operation(summary = "确认预约", description = "将预约从待确认状态更新为已确认")
    @PostMapping("/confirm/{bookingId}")
    public ResponseDTO<Void> confirmBooking(@PathVariable Long bookingId) {
        return bookingService.confirmBooking(bookingId);
    }

    @Operation(summary = "核销预约", description = "核销预约，从已确认状态直接跳转到已完成")
    @PostMapping("/checkin/{bookingId}")
    public ResponseDTO<Void> checkinBooking(@PathVariable Long bookingId) {
        return bookingService.checkinBooking(bookingId);
    }

    @Operation(summary = "取消预约", description = "取消预约并记录取消原因")
    @PostMapping("/cancel/{bookingId}")
    public ResponseDTO<Void> cancelBooking(@PathVariable Long bookingId, @RequestParam(required = false) String reason) {
        return bookingService.cancelBooking(bookingId, reason);
    }

    @Operation(summary = "预约改期", description = "修改预约时间，自动检测时间冲突")
    @PostMapping("/reschedule")
    public ResponseDTO<Void> rescheduleBooking(@RequestBody @Valid BookingRescheduleForm rescheduleForm) {
        return bookingService.rescheduleBooking(rescheduleForm.getBookingId(), rescheduleForm);
    }

    // ========================================
    // 批量操作
    // ========================================

    @Operation(summary = "批量确认预约", description = "批量确认多个预约")
    @PostMapping("/batchConfirm")
    public ResponseDTO<Void> batchConfirmBookings(@RequestBody @Valid BatchBookingForm batchForm) {
        return bookingService.batchConfirmBookings(batchForm.getBookingIds());
    }

    @Operation(summary = "批量取消预约", description = "批量取消多个预约")
    @PostMapping("/batchCancel")
    public ResponseDTO<Void> batchCancelBookings(@RequestBody @Valid BatchBookingForm batchForm) {
        return bookingService.batchCancelBookings(batchForm.getBookingIds());
    }

    // ========================================
    // 查询辅助接口
    // ========================================

    @Operation(summary = "获取会员预约历史", description = "获取指定会员的预约历史记录")
    @GetMapping("/member/{memberId}/history")
    public ResponseDTO<Object> getMemberBookingHistory(@PathVariable Long memberId) {
        // 实现会员预约历史查询逻辑
        return ResponseDTO.ok("会员预约历史功能待实现");
    }

    @Operation(summary = "获取预约统计", description = "获取预约相关的统计数据")
    @GetMapping("/statistics")
    public ResponseDTO<Object> getBookingStatistics(@RequestParam(required = false) Long clubId) {
        // 实现预约统计逻辑
        return ResponseDTO.ok("预约统计功能待实现");
    }

    @Operation(summary = "预约时间冲突检测", description = "检测预约时间是否冲突")
    @PostMapping("/checkRescheduleConflict")
    public ResponseDTO<ConflictCheckResult> checkRescheduleConflict(@RequestBody @Valid BookingRescheduleForm rescheduleForm) {
        BookingDetailVO booking = bookingService.getBookingDetail(rescheduleForm.getBookingId()).getData();
        if (booking == null) {
            return ResponseDTO.userErrorParam("预约不存在");
        }
        
        ConflictCheckResult result = bookingService.checkTimeConflict(
            booking.getCoachId(), booking.getHorseId(),
            rescheduleForm.getNewStartTime(), rescheduleForm.getNewEndTime(),
            rescheduleForm.getBookingId()
        );
        
        return ResponseDTO.ok(result);
    }

    @Operation(summary = "获取可用时间段", description = "获取指定条件下的可用时间段")
    @GetMapping("/availableTimeSlots")
    public ResponseDTO<Object> getAvailableTimeSlots(@RequestParam(required = false) Long coachId,
                                                    @RequestParam(required = false) Long horseId) {
        // 实现可用时间段查询逻辑
        return ResponseDTO.ok("可用时间段功能待实现");
    }

    // ========================================
    // 预约管理操作（为未来扩展预留）
    // ========================================

    @Operation(summary = "创建预约", description = "手动创建预约（暂未实现）")
    @PostMapping("/create")
    public ResponseDTO<String> createBooking(@RequestBody Object data) {
        // 手动创建预约的逻辑
        return ResponseDTO.ok("手动创建预约功能待实现");
    }

    @Operation(summary = "创建课时包预约", description = "基于课时包创建新的预约记录，支持代他人消费")
    @PostMapping("/package/create")
    public ResponseDTO<Void> createPackageBooking(@RequestBody @Valid PackageBookingCreateForm createForm) {
        return bookingService.createPackageBooking(createForm);
    }

    @Operation(summary = "更新预约", description = "更新预约信息（暂未实现）")
    @PostMapping("/update")
    public ResponseDTO<String> updateBooking(@RequestBody Object data) {
        // 更新预约的逻辑
        return ResponseDTO.ok("更新预约功能待实现");
    }

    // ========================================
    // 内联编辑接口
    // ========================================

    @Operation(summary = "切换预约教练", description = "内联编辑：切换预约的教练")
    @PostMapping("/inline-edit/coach")
    public ResponseDTO<Void> updateBookingCoach(@RequestParam Long bookingId, @RequestParam Long coachId) {
        log.info("内联编辑教练 - 预约ID: {}, 新教练ID: {}", bookingId, coachId);
        // TODO: 实现教练切换逻辑
        // 1. 验证预约是否存在
        // 2. 验证教练是否存在和可用
        // 3. 检查时间冲突
        // 4. 更新预约记录
        return ResponseDTO.ok();
    }

    @Operation(summary = "切换预约马匹", description = "内联编辑：切换预约的马匹")
    @PostMapping("/inline-edit/horse")
    public ResponseDTO<Void> updateBookingHorse(@RequestParam Long bookingId, @RequestParam Long horseId) {
        log.info("内联编辑马匹 - 预约ID: {}, 新马匹ID: {}", bookingId, horseId);
        // TODO: 实现马匹切换逻辑
        // 1. 验证预约是否存在
        // 2. 验证马匹是否存在和可用
        // 3. 检查时间冲突
        // 4. 更新预约记录
        return ResponseDTO.ok();
    }

    @Operation(summary = "切换预约时间", description = "内联编辑：切换预约的时间")
    @PostMapping("/inline-edit/time")
    public ResponseDTO<Void> updateBookingTime(@RequestParam Long bookingId, 
                                               @RequestParam String newStartTime, 
                                               @RequestParam String newEndTime) {
        log.info("内联编辑时间 - 预约ID: {}, 新开始时间: {}, 新结束时间: {}", bookingId, newStartTime, newEndTime);
        // TODO: 实现时间切换逻辑
        // 1. 验证预约是否存在
        // 2. 验证时间格式
        // 3. 检查教练和马匹在新时间段的可用性
        // 4. 更新预约记录
        return ResponseDTO.ok();
    }

    @Operation(summary = "切换预约状态", description = "内联编辑：切换预约的状态")
    @PostMapping("/inline-edit/status")
    public ResponseDTO<Void> updateBookingStatus(@RequestParam Long bookingId, @RequestParam Integer status) {
        log.info("内联编辑状态 - 预约ID: {}, 新状态: {}", bookingId, status);
        // TODO: 实现状态切换逻辑
        // 1. 验证预约是否存在
        // 2. 验证状态转换是否合法
        // 3. 更新预约记录
        return ResponseDTO.ok();
    }
}