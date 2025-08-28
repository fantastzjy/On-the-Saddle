package net.lab1024.sa.admin.module.business.product.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 产品类型枚举
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@AllArgsConstructor
@Getter
public enum ProductTypeEnum implements BaseEnum {

    /**
     * 1 课程
     */
    COURSE(1, "课程"),

    /**
     * 2 课时包
     */
    PACKAGE(2, "课时包"),

    /**
     * 3 活动
     */
    ACTIVITY(3, "活动"),

    ;

    private final Integer value;

    private final String desc;
}