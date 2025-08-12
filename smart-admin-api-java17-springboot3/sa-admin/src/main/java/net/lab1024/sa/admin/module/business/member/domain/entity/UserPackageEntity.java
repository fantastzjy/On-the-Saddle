package net.lab1024.sa.admin.module.business.member.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 用户课时包实体
 *
 * @Author Claude Code
 * @Date 2025-01-11
 * @Copyright 马术俱乐部管理系统
 */
@Data
@TableName("m_user_package")
public class UserPackageEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("购买订单ID")
    private Long packageOrderId;

    @DataTracerFieldLabel("课时包商品ID")
    private Long packageId;

    @DataTracerFieldLabel("课时包名称")
    private String packageName;

    @DataTracerFieldLabel("总课时数")
    private Integer totalLessons;

    @DataTracerFieldLabel("已用课时数")
    private Integer usedLessons;

    @DataTracerFieldLabel("剩余课时数")
    private Integer remainingLessons;

    @DataTracerFieldLabel("购买日期时间")
    private LocalDateTime purchaseDate;

    @DataTracerFieldLabel("到期日期时间")
    private LocalDateTime expireDate;

    @DataTracerFieldLabel("适用课程ID列表JSON格式")
    private String applicableCourses;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @DataTracerFieldLabel("是否有效")
    private Integer isValid;

    @DataTracerFieldLabel("是否删除")
    private Integer isDelete;
}