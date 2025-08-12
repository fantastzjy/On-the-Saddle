package net.lab1024.sa.admin.module.business.horse.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

import java.util.Arrays;

/**
 * 健康计划类型枚举
 *
 * @Author: 1024创新实验室-主任：卓大
 * @Date: 2024-01-15
 * @Wechat: zhuda1024
 * @Email: lab1024@163.com
 * @Copyright: 1024创新实验室 （ https://1024lab.net ），Since 2012
 */
@Getter
@AllArgsConstructor
public enum HealthPlanTypeEnum implements BaseEnum {

    /**
     * 钉蹄
     */
    SHOEING("shoeing", "钉蹄"),

    /**
     * 驱虫
     */
    DEWORMING("deworming", "驱虫"),

    /**
     * 搓牙
     */
    DENTAL("dental", "搓牙"),

    /**
     * 疫苗
     */
    VACCINE("vaccine", "疫苗"),

    /**
     * 养护
     */
    MEDICATION("medication", "养护");

    private final String value;
    private final String desc;

    /**
     * 根据值获取描述
     *
     * @param value 枚举值
     * @return 描述
     */
    public static String getDescByValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .map(HealthPlanTypeEnum::getDesc)
                .orElse(value);
    }

    /**
     * 检查值是否有效
     *
     * @param value 枚举值
     * @return 是否有效
     */
    public static boolean isValid(String value) {
        return Arrays.stream(values())
                .anyMatch(e -> e.getValue().equals(value));
    }

    /**
     * 获取所有枚举值
     *
     * @return 枚举值数组
     */
    public static String[] getAllValues() {
        return Arrays.stream(values())
                .map(HealthPlanTypeEnum::getValue)
                .toArray(String[]::new);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
