package net.lab1024.sa.admin.module.business.schedule.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 资源时间安排实体
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@TableName("m_resource_schedule")
public class ResourceScheduleEntity {

    @DataTracerFieldLabel("主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("资源类型")
    private Integer resourceType;

    @DataTracerFieldLabel("资源ID")
    private Long resourceId;

    @DataTracerFieldLabel("日期")
    private LocalDate scheduleDate;

    @DataTracerFieldLabel("开始时间")
    private LocalTime startTime;

    @DataTracerFieldLabel("结束时间")
    private LocalTime endTime;

    @DataTracerFieldLabel("状态")
    private Integer status;

    @DataTracerFieldLabel("标题")
    private String title;

    @DataTracerFieldLabel("详细描述")
    private String description;

    @DataTracerFieldLabel("关联订单号")
    private String orderNo;

    @DataTracerFieldLabel("过期时间")
    private LocalDateTime expireTime;

    @DataTracerFieldLabel("创建人")
    private String createdBy;

    @DataTracerFieldLabel("创建时间")
    private LocalDateTime createTime;

    @DataTracerFieldLabel("更新人")
    private String updatedBy;

    @DataTracerFieldLabel("更新时间")
    private LocalDateTime updateTime;
}