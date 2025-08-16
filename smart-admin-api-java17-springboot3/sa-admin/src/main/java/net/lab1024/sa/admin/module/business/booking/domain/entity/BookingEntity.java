package net.lab1024.sa.admin.module.business.booking.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预约约课表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_booking")
public class BookingEntity {

    @TableId(type = IdType.AUTO)
    private Long bookingId;

    @DataTracerFieldLabel("订单ID")
    private Long orderId;

    @DataTracerFieldLabel("订单明细ID")
    private Long orderItemId;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("商品ID")
    private Long productId;

    @DataTracerFieldLabel("教练ID")
    private Long coachId;

    @DataTracerFieldLabel("马匹ID")
    private Long horseId;

    @DataTracerFieldLabel("预约开始时间")
    private LocalDateTime startTime;

    @DataTracerFieldLabel("预约结束时间")
    private LocalDateTime endTime;

    @DataTracerFieldLabel("预约状态")
    private Integer bookingStatus;

    @DataTracerFieldLabel("实际教练费")
    private BigDecimal actualCoachFee;

    @DataTracerFieldLabel("实际马匹费")
    private BigDecimal actualHorseFee;

    @DataTracerFieldLabel("到店时间")
    private LocalDateTime arrivalTime;

    @DataTracerFieldLabel("完成时间")
    private LocalDateTime completionTime;

    @DataTracerFieldLabel("取消原因")
    private String cancelReason;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}