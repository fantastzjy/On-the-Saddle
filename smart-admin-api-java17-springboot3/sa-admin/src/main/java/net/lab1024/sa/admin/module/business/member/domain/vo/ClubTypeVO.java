package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 俱乐部类型信息VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "俱乐部类型信息")
public class ClubTypeVO {

    @Schema(description = "俱乐部类型 1-俱乐部 2-竞技马房")
    private Integer clubType;

    @Schema(description = "俱乐部名称")
    private String clubName;
}