package net.lab1024.sa.admin.module.business.schedule.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课单表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_lesson_schedule")
public class LessonScheduleEntity {

    @TableId(type = IdType.AUTO)
    private Long scheduleId;

    /**
     * 预约ID
     */
    private Long bookingId;

    /**
     * 课单号
     */
    private String scheduleNo;

    /**
     * 俱乐部ID
     */
    private Long clubId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 教练ID
     */
    private Long coachId;

    /**
     * 马匹ID
     */
    private Long horseId;

    /**
     * 上课日期
     */
    private LocalDate lessonDate;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 课单状态: 1-待上课 2-进行中 3-已完成 4-已取消
     */
    private Integer lessonStatus;

    /**
     * 课程时长（分钟）- 通过计算得出，不存储在数据库
     */
    @TableField(exist = false)
    private Integer duration;

    /**
     * 是否已发送通知: 0-未发送 1-已发送
     */
    private Integer notificationSent;

    /**
     * 是否已发送提醒: 0-未发送 1-已发送
     */
    private Integer reminderSent;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}