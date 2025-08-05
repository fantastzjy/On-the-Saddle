package net.lab1024.sa.admin.module.business.horse.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 马匹实体类
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_horse")
public class HorseEntity {

    @DataTracerFieldLabel("马匹ID")
    @TableId(type = IdType.AUTO)
    private Long horseId;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("马名")
    private String horseName;

    @DataTracerFieldLabel("马匹编号")
    private String horseCode;

    @DataTracerFieldLabel("品种")
    private String breed;

    @DataTracerFieldLabel("性别")
    private Integer gender;

    @DataTracerFieldLabel("毛色")
    private String color;

    @DataTracerFieldLabel("身高")
    private Integer height;

    @DataTracerFieldLabel("体重")
    private Integer weight;

    @DataTracerFieldLabel("出生日期")
    private LocalDateTime birthDate;

    @DataTracerFieldLabel("芯片号")
    private String chipNo;

    @DataTracerFieldLabel("护照号")
    private String passportNo;

    @DataTracerFieldLabel("血统证书图片地址")
    private String pedigreeCertUrl;

    @DataTracerFieldLabel("马匹类型")
    private Integer horseType;

    @DataTracerFieldLabel("马主ID")
    private Long ownerId;

    @DataTracerFieldLabel("责任教练ID")
    private Long responsibleCoachId;

    @DataTracerFieldLabel("责任马工ID")
    private Long responsibleGroomId;

    @DataTracerFieldLabel("寄养开始日期")
    private LocalDateTime boardingStartDate;

    @DataTracerFieldLabel("寄养结束日期")
    private LocalDateTime boardingEndDate;

    @DataTracerFieldLabel("健康状态")
    private Integer healthStatus;

    @DataTracerFieldLabel("工作状态")
    private Integer workStatus;

    @DataTracerFieldLabel("马匹扩展数据")
    private String horseData;

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
