package net.lab1024.sa.admin.module.business.stablerental.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 租赁状态枚举
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@AllArgsConstructor
@Getter
public enum RentalStatusEnum {

    /**
     * 生效中
     */
    ACTIVE(1, "生效中"),

    /**
     * 已过期
     */
    EXPIRED(2, "已过期"),

    /**
     * 已取消
     */
    CANCELLED(3, "已取消");

    private final Integer value;

    private final String desc;

    public static RentalStatusEnum getByValue(Integer value) {
        for (RentalStatusEnum statusEnum : RentalStatusEnum.values()) {
            if (statusEnum.value.equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}