package net.lab1024.sa.admin.module.business.order.constant;

/**
 * 订单状态枚举
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
public enum OrderStatusEnum {

    PENDING_PAYMENT(1, "待支付"),
    PAID(2, "已支付"),
    COMPLETED(3, "已核销"),
    CANCELLED(4, "已取消"),
    REFUNDED(5, "已退款");

    private final Integer code;
    private final String desc;

    OrderStatusEnum(Integer code, String desc) {
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
        for (OrderStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status.desc;
            }
        }
        return "";
    }

    public static OrderStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}