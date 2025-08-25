package net.lab1024.sa.admin.module.business.coach.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教练实体
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_coach")
public class CoachEntity {

    @TableId(type = IdType.AUTO)
    private Long coachId;

    @DataTracerFieldLabel("俱乐部ID")
    private Long clubId;

    @DataTracerFieldLabel("关联用户ID")
    private Long userId;

    @DataTracerFieldLabel("教练真实姓名")
    private String actualName;

    @DataTracerFieldLabel("联系电话")
    private String phone;

    @DataTracerFieldLabel("邮箱地址")
    private String email;

    @DataTracerFieldLabel("性别")
    private Integer gender;

    @DataTracerFieldLabel("生日")
    private LocalDate birthDate;

    @DataTracerFieldLabel("身份证号码")
    private String idCard;

    @DataTracerFieldLabel("所属部门ID")
    private Long departmentId;

    @DataTracerFieldLabel("教练编号")
    private String coachNo;

    @DataTracerFieldLabel("头像照片地址")
    private String avatarUrl;

    @DataTracerFieldLabel("身份证正面照片地址")
    private String idCardFrontImg;

    @DataTracerFieldLabel("身份证反面照片地址")
    private String idCardBackImg;

    @DataTracerFieldLabel("入行时间")
    private LocalDateTime entryDate;

    @DataTracerFieldLabel("专长领域")
    private String specialties;

    @DataTracerFieldLabel("个人介绍")
    private String introduction;

    @DataTracerFieldLabel("骑手证号码")
    private String riderCertNo;

    @DataTracerFieldLabel("星级教练证号码")
    private String coachCertNo;

    // 新增教练证书扫平化字段（4个类别）
    @DataTracerFieldLabel("教练星级")
    private Integer coachStarLevel;
    @DataTracerFieldLabel("教练星级证书图片")
    private String coachStarCertImages;
    
    @DataTracerFieldLabel("教练场地障碍星级")
    private Integer coachShowJumpingLevel;
    @DataTracerFieldLabel("教练场地障碍证书图片")
    private String coachShowJumpingImages;
    
    @DataTracerFieldLabel("教练盛装舞步星级")
    private Integer coachDressageLevel;
    @DataTracerFieldLabel("教练盛装舞步证书图片")
    private String coachDressageImages;
    
    @DataTracerFieldLabel("教练三项赛星级")
    private Integer coachEventingLevel;
    @DataTracerFieldLabel("教练三项赛证书图片")
    private String coachEventingImages;

    // 新增骑手证书扫平化字段（3个类别）
    @DataTracerFieldLabel("骑手场地障碍等级")
    private Integer riderShowJumpingLevel;
    @DataTracerFieldLabel("骑手场地障碍证书图片")
    private String riderShowJumpingImages;
    
    @DataTracerFieldLabel("骑手盛装舞步等级")
    private Integer riderDressageLevel;
    @DataTracerFieldLabel("骑手盛装舞步证书图片")
    private String riderDressageImages;
    
    @DataTracerFieldLabel("骑手三项赛等级")
    private Integer riderEventingLevel;
    @DataTracerFieldLabel("骑手三项赛证书图片")
    private String riderEventingImages;

    @DataTracerFieldLabel("微信unionId")
    private String unionId;

    @DataTracerFieldLabel("微信openId")
    private String openId;

    @DataTracerFieldLabel("最后登录时间")
    private LocalDateTime lastLoginTime;

    @DataTracerFieldLabel("是否禁用")
    private Integer disabledFlag;

    @DataTracerFieldLabel("教练费")
    private java.math.BigDecimal coachFee;

    @DataTracerFieldLabel("排序")
    private Integer sortOrder;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @DataTracerFieldLabel("是否有效")
    private Integer isValid;

    @DataTracerFieldLabel("是否删除")
    private Integer isDelete;
}