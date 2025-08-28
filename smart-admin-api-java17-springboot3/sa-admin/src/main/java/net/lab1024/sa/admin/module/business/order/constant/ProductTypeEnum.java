package net.lab1024.sa.admin.module.business.order.constant;

/**
 * 产品类型枚举
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
public enum ProductTypeEnum {

    COURSE(1, "课程"),
    PACKAGE(2, "课时包"),
    ACTIVITY(3, "活动");

    private final Integer code;
    private final String desc;

    ProductTypeEnum(Integer code, String desc) {
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
        for (ProductTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type.desc;
            }
        }
        return "";
    }

    public static ProductTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProductTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}