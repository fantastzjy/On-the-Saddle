package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 会籍状态VO
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "会籍状态信息")
public class MembershipStatusVO {

    @Schema(description = "是否为会籍会员：1-是 0-否")
    private Integer isMembership;

    @Schema(description = "会籍状态：1-正常 2-即将到期 3-已过期")
    private Integer membershipStatus;

    @Schema(description = "会籍有效期")
    private LocalDate membershipExpireDate;

    @Schema(description = "距离到期天数")
    private Long daysToExpire;

    @Schema(description = "会籍状态描述")
    private String statusDescription;
}