package net.lab1024.sa.admin.module.business.order.constant;

/**
 * 订单子类型枚举
 *
 * @Author Claude Code
 * @Date 2025-08-27
 * @Copyright 马术俱乐部管理系统
 */
public enum OrderSubTypeEnum {

    // 课程订单子类型（复用现有CourseSubTypeEnum）
    SINGLE_CLASS("single_class", "单人课", 1),
    MULTI_CLASS("multi_class", "多人课", 1),
    
    // 补差费订单子类型
    EXTRA_COACH_FEE("extra_coach_fee", "教练费补差", 4),
    EXTRA_LESSON_FEE("extra_lesson_fee", "课时费补差", 4),
    EXTRA_HORSE_FEE("extra_horse_fee", "马匹费补差", 4),
    
    // 饲养费订单子类型
    BOARDING_FEE("boarding_fee", "饲养费", 5),
    
    // 健康服务订单子类型（自动生成）
    HEALTH_SHOEING("health_shoeing", "钉蹄服务", 6),
    HEALTH_DEWORMING("health_deworming", "驱虫服务", 6),
    HEALTH_DENTAL("health_dental", "搓牙服务", 6),
    HEALTH_VACCINE("health_vaccine", "疫苗服务", 6),
    HEALTH_MEDICATION("health_medication", "养护服务", 6);
    
    private final String code;
    private final String desc;
    private final Integer parentOrderType; // 所属订单类型

    OrderSubTypeEnum(String code, String desc, Integer parentOrderType) {
        this.code = code;
        this.desc = desc;
        this.parentOrderType = parentOrderType;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getParentOrderType() {
        return parentOrderType;
    }

    public static String getDescByCode(String code) {
        if (code == null) {
            return "";
        }
        for (OrderSubTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type.desc;
            }
        }
        return "";
    }

    public static OrderSubTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (OrderSubTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据父类型获取子类型列表
     */
    public static OrderSubTypeEnum[] getByParentType(Integer parentOrderType) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.parentOrderType.equals(parentOrderType))
                .toArray(OrderSubTypeEnum[]::new);
    }
}