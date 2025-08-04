package net.lab1024.sa.admin.module.business.horse.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 马匹健康记录实体类
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_horse_health_record")
public class HorseHealthRecordEntity {

    @DataTracerFieldLabel("主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("马匹ID")
    private Long horseId;

    @DataTracerFieldLabel("关联计划ID")
    private Long planId;

    @DataTracerFieldLabel("计划类型")
    private String planType;

    @DataTracerFieldLabel("记录日期")
    private LocalDateTime recordDate;

    @DataTracerFieldLabel("执行人ID")
    private Long executorId;

    @DataTracerFieldLabel("记录内容")
    private String content;

    @DataTracerFieldLabel("图片地址")
    private String imgUrl;

    @DataTracerFieldLabel("下次执行日期")
    private LocalDateTime nextDate;

    @DataTracerFieldLabel("记录扩展数据")
    private String recordData;

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