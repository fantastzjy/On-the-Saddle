package net.lab1024.sa.admin.module.business.order.constant;

/**
 * 订单类型枚举
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
public enum OrderTypeEnum {

    COURSE(1, "课程"),
    PACKAGE(2, "课时包"),
    ACTIVITY(3, "活动");

    private final Integer code;
    private final String desc;

    OrderTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "";
        }
        for (OrderTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type.desc;
            }
        }
        return "";
    }

    public static OrderTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}