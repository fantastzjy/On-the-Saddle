package net.lab1024.sa.admin.module.business.schedule.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间安排状态枚举
 *
 * @Author Claude Code
 * @Date 2025-08-23
 * @Copyright 马术俱乐部管理系统
 */
@Getter
@AllArgsConstructor
public enum ScheduleStatusEnum {

    LEAVE(1, "请假"),
    OCCUPIED(2, "占用"),
    MAINTENANCE(3, "维护"),
    OTHER(4, "其他");

    private final Integer value;
    private final String desc;

    public static String getDescByValue(Integer value) {
        if (value == null) {
            return "";
        }
        for (ScheduleStatusEnum statusEnum : values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.getDesc();
            }
        }
        return "";
    }

    public static ScheduleStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ScheduleStatusEnum statusEnum : values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}