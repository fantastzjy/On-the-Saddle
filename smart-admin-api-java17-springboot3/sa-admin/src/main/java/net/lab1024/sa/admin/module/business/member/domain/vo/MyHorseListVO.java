package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 我的马匹列表VO
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Schema(description = "我的马匹列表信息")
public class MyHorseListVO {

    @Schema(description = "马匹名字")
    private String horseName;

    @Schema(description = "生日", example = "2020/03/15")
    private String birthDate;

    @Schema(description = "养护统计列表")
    private List<CareStatisticsVO> careStatistics;

    @Schema(description = "芯片号")
    private String chipNo;

    @Schema(description = "责任教练")
    private String responsibleCoach;

    @Schema(description = "责任马工")
    private String responsibleGroom;

    @Schema(description = "寄养时间", example = "2025年08月~2025年09月")
    private String boardingPeriod;

    @Schema(description = "医疗信息列表")
    private List<MedicalInfoVO> medicalInfo;
}