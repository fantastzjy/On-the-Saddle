package net.lab1024.sa.admin.module.business.report.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 报告类型枚举
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@AllArgsConstructor
@Getter
public enum ReportTypeEnum implements BaseEnum {

    /**
     * 1 月度马匹健康报告
     */
    HORSE_HEALTH_MONTHLY(1, "月度马匹健康报告"),

    /**
     * 2 课程统计报告
     */
    COURSE_STATISTICS(2, "课程统计报告"),

    /**
     * 3 会员分析报告
     */
    MEMBER_ANALYSIS(3, "会员分析报告"),

    /**
     * 4 财务汇总报告
     */
    FINANCIAL_SUMMARY(4, "财务汇总报告"),

    /**
     * 99 自定义报告
     */
    CUSTOM_REPORT(99, "自定义报告"),

    ;

    private final Integer value;

    private final String desc;
}