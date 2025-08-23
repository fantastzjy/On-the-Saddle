package net.lab1024.sa.admin.module.business.schedule.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型枚举
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    CLUB(1, "俱乐部"),
    COACH(2, "教练"),
    HORSE(3, "马匹");

    private final Integer value;
    private final String desc;

    public static String getDescByValue(Integer value) {
        if (value == null) {
            return "";
        }
        for (ResourceTypeEnum typeEnum : values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum.getDesc();
            }
        }
        return "";
    }

    public static ResourceTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ResourceTypeEnum typeEnum : values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}