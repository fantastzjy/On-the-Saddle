package net.lab1024.sa.base.enums;

/**
 * 马匹类型枚举
 *
 * @Author Claude Code
 * @Date 2025-08-27
 * @Copyright 马术俱乐部管理系统
 */
public enum HorseTypeEnum {

    CLUB_HORSE(1, "俱乐部马"),
    RACING_STABLE_HORSE(2, "竞技马房马"),
    OWNER_HORSE(3, "马主马");

    private final Integer code;
    private final String desc;

    HorseTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
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
    public static HorseTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (HorseTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
