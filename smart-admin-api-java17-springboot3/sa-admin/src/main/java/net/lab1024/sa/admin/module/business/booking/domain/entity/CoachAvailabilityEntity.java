package net.lab1024.sa.admin.module.business.booking.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 教练可用时间表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_coach_availability")
public class CoachAvailabilityEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("教练ID")
    private Long coachId;

    @DataTracerFieldLabel("可用日期")
    private LocalDate availableDate;

    @DataTracerFieldLabel("开始时间")
    private LocalTime startTime;

    @DataTracerFieldLabel("结束时间")
    private LocalTime endTime;

    @DataTracerFieldLabel("状态")
    private Integer status;

    @DataTracerFieldLabel("备注")
    private String remark;

    private LocalDateTime createTime;
}