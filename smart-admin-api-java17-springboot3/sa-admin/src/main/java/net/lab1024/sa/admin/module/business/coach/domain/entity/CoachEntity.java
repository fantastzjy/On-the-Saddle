package net.lab1024.sa.admin.module.business.coach.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

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

    @DataTracerFieldLabel("教练编号")
    private String coachNo;

    @DataTracerFieldLabel("头像照片地址")
    private String avatarUrl;

    @DataTracerFieldLabel("入行时间")
    private LocalDateTime entryDate;

    @DataTracerFieldLabel("专长领域")
    private String specialties;

    @DataTracerFieldLabel("个人介绍")
    private String introduction;

    @DataTracerFieldLabel("骑手证号码")
    private String riderCertNo;

    @DataTracerFieldLabel("场地障碍等级")
    private String riderLevelShowJumping;

    @DataTracerFieldLabel("盛装舞步等级")
    private String riderLevelDressage;

    @DataTracerFieldLabel("三项赛等级")
    private String riderLevelEventing;

    @DataTracerFieldLabel("骑手证书图片地址JSON格式")
    private String riderCertImgUrl;

    @DataTracerFieldLabel("星级教练证号码")
    private String coachCertNo;

    @DataTracerFieldLabel("教练等级")
    private String coachLevel;

    @DataTracerFieldLabel("教练证书图片地址JSON格式")
    private String coachCertImgUrl;

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