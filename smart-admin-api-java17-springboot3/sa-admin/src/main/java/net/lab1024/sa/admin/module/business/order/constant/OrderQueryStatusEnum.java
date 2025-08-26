package net.lab1024.sa.admin.module.business.order.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 订单查询状态枚举
 *
 * @Author Claude Code
 * @Date 2025-08-26
 * @Copyright 马术俱乐部管理系统
 */
public enum OrderQueryStatusEnum {

    COURSE_BOOKING(1, "课程预约", Collections.emptyList()),     // 预留，暂不处理
    PENDING(2, "待完成", Arrays.asList(1, 2)),                  // 待支付、已支付
    COMPLETED(3, "已完成", Arrays.asList(3)),                   // 已核销
    AFTER_SALE(4, "退款/售后", Arrays.asList(4, 5));           // 已取消、已退款

    private final Integer code;
    private final String desc;
    private final List<Integer> orderStatusList;

    OrderQueryStatusEnum(Integer code, String desc, List<Integer> orderStatusList) {
        this.code = code;
        this.desc = desc;
        this.orderStatusList = orderStatusList;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public List<Integer> getOrderStatusList() {
        return orderStatusList;
    }

    public static OrderQueryStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderQueryStatusEnum statusEnum : values()) {
            if (statusEnum.code.equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        OrderQueryStatusEnum statusEnum = getByCode(code);
        return statusEnum != null ? statusEnum.desc : "";
    }
}