package net.lab1024.sa.base.enums;

/**
 * 课程子类型枚举
 *
 * @Author Claude Code
 * @Date 2025-08-27
 * @Copyright 马术俱乐部管理系统
 */
public enum CourseSubTypeEnum {

    SINGLE_CLASS("single_class", "单人课"),
    MULTI_CLASS("multi_class", "多人课");

    private final String code;
    private final String desc;

    CourseSubTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 编码
     * @return 枚举值
     */
    public static CourseSubTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (CourseSubTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
