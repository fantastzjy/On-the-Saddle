package net.lab1024.sa.admin.module.business.booking.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 时间段模板表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_time_slot_template")
public class TimeSlotTemplateEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("时段名称")
    private String slotName;

    @DataTracerFieldLabel("开始时间")
    private LocalTime startTime;

    @DataTracerFieldLabel("结束时间")
    private LocalTime endTime;

    @DataTracerFieldLabel("适用星期")
    private String weekDays;

    @DataTracerFieldLabel("是否可用")
    private Integer isAvailable;

    @DataTracerFieldLabel("排序")
    private Integer sortOrder;

    private LocalDateTime createTime;

    private Boolean isDelete;
}