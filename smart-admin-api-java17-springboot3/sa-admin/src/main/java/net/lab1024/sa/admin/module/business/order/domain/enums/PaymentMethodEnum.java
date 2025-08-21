package net.lab1024.sa.admin.module.business.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式枚举
 *
 * @Author 1024创新实验室
 * @Date 2024-08-21
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Getter
@AllArgsConstructor
public enum PaymentMethodEnum {

    /**
     * 微信支付
     */
    WECHAT("wechat", "微信支付"),

    /**
     * 支付宝
     */
    ALIPAY("alipay", "支付宝"),

    /**
     * 现金支付
     */
    CASH("cash", "现金支付"),

    /**
     * 课时包支付
     */
    PACKAGE("package", "课时包支付");

    /**
     * 支付方式代码
     */
    private final String code;

    /**
     * 支付方式名称
     */
    private final String name;

    /**
     * 根据代码获取支付方式枚举
     *
     * @param code 支付方式代码
     * @return 支付方式枚举
     */
    public static PaymentMethodEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (PaymentMethodEnum method : values()) {
            if (method.getCode().equals(code)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 根据代码获取支付方式名称
     *
     * @param code 支付方式代码
     * @return 支付方式名称
     */
    public static String getNameByCode(String code) {
        PaymentMethodEnum method = getByCode(code);
        return method != null ? method.getName() : "未知支付方式";
    }

    /**
     * 验证支付方式代码是否有效
     *
     * @param code 支付方式代码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        return getByCode(code) != null;
    }
}