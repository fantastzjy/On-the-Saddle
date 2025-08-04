package net.lab1024.sa.admin.module.business.horse.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 马匹健康计划实体类
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_horse_health_plan")
public class HorseHealthPlanEntity {

    @DataTracerFieldLabel("主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("马匹ID")
    private Long horseId;

    @DataTracerFieldLabel("计划类型")
    private String planType;

    @DataTracerFieldLabel("周期天数")
    private Integer cycleDays;

    @DataTracerFieldLabel("上次执行日期")
    private LocalDateTime lastDate;

    @DataTracerFieldLabel("下次执行日期")
    private LocalDateTime nextDate;

    @DataTracerFieldLabel("提前提醒天数")
    private Integer reminderDays;

    @DataTracerFieldLabel("计划配置")
    private String planConfig;

    @DataTracerFieldLabel("创建人")
    private String createBy;

    @DataTracerFieldLabel("创建时间")
    private LocalDateTime createTime;

    @DataTracerFieldLabel("更新人")
    private String updateBy;

    @DataTracerFieldLabel("更新时间")
    private LocalDateTime updateTime;

    @DataTracerFieldLabel("是否有效")
    private Integer isValid;

    @DataTracerFieldLabel("是否删除")
    private Integer isDelete;
}