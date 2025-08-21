package net.lab1024.sa.admin.module.business.booking.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约详情VO
 * 基于 ScheduleDetailVO 设计，保持数据结构一致性
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "预约详情VO")
public class BookingDetailVO {

    // ===== 预约基础信息 =====
    @Schema(description = "预约ID")
    private Long bookingId;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单明细ID")
    private Long orderItemId;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "预约日期")
    private LocalDate bookingDate;

    @Schema(description = "预约开始时间")
    private LocalDateTime startTime;

    @Schema(description = "预约结束时间")
    private LocalDateTime endTime;

    @Schema(description = "课程时长(分钟)")
    private Integer duration;

    @Schema(description = "预约状态")
    private Integer bookingStatus;

    @Schema(description = "预约状态名称")
    private String bookingStatusName;

    // ===== 会员信息（与ScheduleDetailVO完全一致） =====
    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员姓名")
    private String memberName;

    @Schema(description = "会员手机号")
    private String memberPhone;

    @Schema(description = "会员性别")
    private String memberGender;

    @Schema(description = "会员级别")
    private String memberLevel;

    @Schema(description = "默认教练姓名")
    private String defaultCoachName;

    @Schema(description = "会员身高")
    private String memberHeight;

    @Schema(description = "会员体重")
    private String memberWeight;

    @Schema(description = "骑行经验")
    private String ridingExperience;

    @Schema(description = "会员备注")
    private String memberRemark;

    // ===== 教练信息（与ScheduleDetailVO完全一致） =====
    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "教练姓名")
    private String coachName;

    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "教练头像")
    private String coachAvatar;

    @Schema(description = "教练等级")
    private String coachLevel;

    @Schema(description = "教练专长")
    private String coachSpecialties;

    @Schema(description = "教练介绍")
    private String coachIntroduction;

    @Schema(description = "教练手机号")
    private String coachPhone;

    // ===== 马匹信息（与ScheduleDetailVO完全一致） =====
    @Schema(description = "马匹ID")
    private Long horseId;

    @Schema(description = "马匹名称")
    private String horseName;

    @Schema(description = "马匹编号")
    private String horseNo;

    @Schema(description = "马匹品种")
    private String horseBreed;

    @Schema(description = "马匹性别")
    private String horseGender;

    @Schema(description = "马匹年龄")
    private Integer horseAge;

    @Schema(description = "马匹颜色")
    private String horseColor;

    @Schema(description = "马匹身高")
    private String horseHeight;

    @Schema(description = "马匹体重")
    private String horseWeight;

    @Schema(description = "马匹性格特点")
    private String horseCharacter;

    @Schema(description = "马匹健康状态")
    private String horseHealthStatus;

    // ===== 商品信息 =====
    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品类型")
    private String productType;

    @Schema(description = "商品价格")
    private BigDecimal productPrice;

    @Schema(description = "课程类型")
    private String lessonType;

    @Schema(description = "难度等级")
    private String difficultyLevel;

    // ===== 费用信息 =====
    @Schema(description = "实际教练费")
    private BigDecimal actualCoachFee;

    @Schema(description = "实际马匹费")
    private BigDecimal actualHorseFee;

    @Schema(description = "总费用")
    private BigDecimal totalFee;

    // ===== 时间信息 =====
    @Schema(description = "核销时间(到店时间)")
    private LocalDateTime arrivalTime;

    @Schema(description = "完成时间")
    private LocalDateTime completionTime;

    @Schema(description = "取消原因")
    private String cancelReason;

    // ===== 预约特有信息 =====
    @Schema(description = "预约备注")
    private String bookingNotes;

    @Schema(description = "特殊要求")
    private String specialRequests;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "付费方式")
    private String paymentMethod;

    @Schema(description = "预付款")
    private BigDecimal deposit;

    // ===== 系统信息 =====
    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}