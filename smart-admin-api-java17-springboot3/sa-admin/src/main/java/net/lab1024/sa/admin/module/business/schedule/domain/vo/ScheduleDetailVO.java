package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课表详情VO
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "课表详情VO")
public class ScheduleDetailVO {

    @Schema(description = "课表ID")
    private Long scheduleId;

    @Schema(description = "课单号")
    private String scheduleNo;

    @Schema(description = "预约ID")
    private Long bookingId;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "上课日期")
    private LocalDate lessonDate;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "课程时长(分钟)")
    private Integer duration;

    @Schema(description = "课表状态")
    private Integer lessonStatus;

    @Schema(description = "课表状态名称")
    private String lessonStatusName;

    // 会员信息
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

    @Schema(description = "会员身高")
    private String memberHeight;

    @Schema(description = "会员体重")
    private String memberWeight;

    @Schema(description = "骑行经验")
    private String ridingExperience;

    @Schema(description = "会员备注")
    private String memberRemark;

    // 教练信息
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

    // 马匹信息
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

    // 商品信息
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

    // 预约相关信息
    @Schema(description = "预约状态")
    private Integer bookingStatus;

    @Schema(description = "预约状态名称")
    private String bookingStatusName;

    @Schema(description = "实际教练费")
    private BigDecimal actualCoachFee;

    @Schema(description = "实际马匹费")
    private BigDecimal actualHorseFee;

    @Schema(description = "到店时间")
    private LocalDateTime arrivalTime;

    @Schema(description = "完成时间")
    private LocalDateTime completionTime;

    @Schema(description = "取消原因")
    private String cancelReason;

    // 系统信息
    @Schema(description = "通知是否已发送")
    private Integer notificationSent;

    @Schema(description = "提醒是否已发送")
    private Integer reminderSent;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
