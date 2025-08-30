package net.lab1024.sa.admin.module.business.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付配置类（APIv3）
 * 
 * @author 1024创新实验室：开云
 * @since 2024-12-30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.pay")
public class WechatPayConfig {

    /**
     * 商户号（必填）
     * 微信支付分配的商户号
     */
    private String mchId;

    /**
     * 商户APIv3密钥（必填）
     * 32位字符串，用于APIv3接口的加密解密
     */
    private String apiV3Key;

    /**
     * 商户私钥文件路径（必填）
     * 用于请求签名的商户私钥证书文件路径
     */
    private String privateKeyPath;

    /**
     * 商户证书序列号（必填）
     * 商户API证书的证书序列号
     */
    private String merchantSerialNumber;

    /**
     * 微信支付证书文件路径（可选）
     * 微信支付平台证书文件路径，可通过API自动下载更新
     */
    private String wechatPayCertPath;

    /**
     * 支付回调通知URL（必填）
     * 用于接收微信支付异步通知的回调地址
     */
    private String notifyUrl;

    /**
     * 退款回调通知URL（可选）
     * 用于接收微信退款异步通知的回调地址
     */
    private String refundNotifyUrl;

    /**
     * 请求超时时间，单位毫秒（可选，默认30000）
     */
    private Integer requestTimeout = 30000;

    /**
     * 支付环境：mock（模拟）/ sandbox（沙箱）/ production（生产）
     * 默认为sandbox，生产环境需要配置为production，Mock测试使用mock
     */
    private String environment = "sandbox";

    /**
     * Mock模式配置
     */
    private MockConfig mock = new MockConfig();

    /**
     * Mock配置类
     */
    @Data
    public static class MockConfig {
        /**
         * 模拟网络延迟（秒）
         */
        private Integer delaySeconds = 2;

        /**
         * 模拟成功率（百分比，0-100）
         */
        private Integer successRate = 90;

        /**
         * 是否自动触发支付回调
         */
        private Boolean autoCallback = true;

        /**
         * 回调延迟时间（秒）
         */
        private Integer callbackDelay = 5;
    }

    /**
     * 获取微信支付APIv3基础URL
     */
    public String getBaseUrl() {
        return "sandbox".equals(environment) 
            ? "https://api.mch.weixin.qq.com" 
            : "https://api.mch.weixin.qq.com";
    }

    /**
     * 是否为沙箱环境
     */
    public boolean isSandbox() {
        return "sandbox".equals(environment);
    }

    /**
     * 是否启用Mock模式
     */
    public boolean isMockMode() {
        return "mock".equals(environment);
    }

    /**
     * 是否为生产环境
     */
    public boolean isProduction() {
        return "production".equals(environment);
    }
}