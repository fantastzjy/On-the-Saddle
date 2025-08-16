package net.lab1024.sa.admin.module.business.booking.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 前台核销记录表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_checkin_record")
public class CheckinRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("预约ID")
    private Long bookingId;

    @DataTracerFieldLabel("课单ID")
    private Long scheduleId;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("签到时间")
    private LocalDateTime checkinTime;

    @DataTracerFieldLabel("签到确认人员")
    private String checkinStaff;

    @DataTracerFieldLabel("签退时间")
    private LocalDateTime checkoutTime;

    @DataTracerFieldLabel("签退确认人员")
    private String checkoutStaff;

    @DataTracerFieldLabel("实际上课时长")
    private Integer actualDuration;

    @DataTracerFieldLabel("备注")
    private String remark;

    private String createBy;

    private LocalDateTime createTime;
}