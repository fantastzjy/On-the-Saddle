package net.lab1024.sa.admin.module.business.booking.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课单表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_lesson_schedule")
public class LessonScheduleEntity {

    @TableId(type = IdType.AUTO)
    private Long scheduleId;

    @DataTracerFieldLabel("预约ID")
    private Long bookingId;

    @DataTracerFieldLabel("课单号")
    private String scheduleNo;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("教练ID")
    private Long coachId;

    @DataTracerFieldLabel("马匹ID")
    private Long horseId;

    @DataTracerFieldLabel("上课日期")
    private LocalDate lessonDate;

    @DataTracerFieldLabel("开始时间")
    private LocalDateTime startTime;

    @DataTracerFieldLabel("结束时间")
    private LocalDateTime endTime;

    @DataTracerFieldLabel("课单状态")
    private Integer lessonStatus;

    @DataTracerFieldLabel("是否已发送通知")
    private Integer notificationSent;

    @DataTracerFieldLabel("是否已发送提醒")
    private Integer reminderSent;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}