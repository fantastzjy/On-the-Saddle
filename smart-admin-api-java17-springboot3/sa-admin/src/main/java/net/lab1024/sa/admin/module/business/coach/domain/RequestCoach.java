package net.lab1024.sa.admin.module.business.coach.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;

import java.io.Serializable;

/**
 * 请求教练登录信息
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
@Data
public class RequestCoach implements RequestUser, Serializable {

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "所属俱乐部ID")
    private Long clubId;

    @Schema(description = "教练等级显示")
    private String coachLevel;

    @Schema(description = "专长领域")
    private String specialties;

    @Schema(description = "教练证书编号")
    private String coachCertNo;

    // 扩展字段：扁平化证书信息
    @Schema(description = "教练星级")
    private Integer coachStarLevel;

    @Schema(description = "教练场地障碍星级")
    private Integer coachShowJumpingLevel;

    @Schema(description = "教练盛装舞步星级")
    private Integer coachDressageLevel;

    @Schema(description = "教练三项赛星级")
    private Integer coachEventingLevel;

    @Schema(description = "骑手场地障碍等级")
    private Integer riderShowJumpingLevel;

    @Schema(description = "骑手盛装舞步等级")
    private Integer riderDressageLevel;

    @Schema(description = "骑手三项赛等级")
    private Integer riderEventingLevel;

    @Schema(description = "骑手证号码")
    private String riderCertNo;

    @Schema(description = "微信unionId")
    private String unionId;

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "是否有效")
    private Integer isValid;

    @Schema(description = "是否删除")
    private Integer isDelete;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "请求IP")
    private String ip;

    @Schema(description = "请求user-agent")
    private String userAgent;

    @Override
    public Long getUserId() {
        return coachId;
    }

    @Override
    public String getUserName() {
        return actualName;
    }

    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.COACH;
    }
}
