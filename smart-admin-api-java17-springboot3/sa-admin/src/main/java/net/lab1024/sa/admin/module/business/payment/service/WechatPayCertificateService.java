package net.lab1024.sa.admin.module.business.payment.service;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.refund.RefundService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.payment.config.WechatPayConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 微信支付证书管理服务
 *
 * @author 1024创新实验室：开云
 * @since 2024-12-30
 */
@Slf4j
@Service
public class WechatPayCertificateService {

    @Resource
    private WechatPayConfig wechatPayConfig;

    private volatile RSAAutoCertificateConfig config;
    private volatile JsapiService jsapiService;
    private volatile RefundService refundService;
    private volatile NotificationConfig notificationConfig;

    /**
     * 获取微信支付配置（懒加载）
     */
    public RSAAutoCertificateConfig getWechatPayConfig() {
        if (config == null) {
            synchronized (this) {
                if (config == null) {
                    config = buildWechatPayConfig();
                    log.info("微信支付证书配置初始化完成");
                }
            }
        }
        return config;
    }

    /**
     * 获取小程序支付服务（懒加载）
     */
    public JsapiService getJsapiService() {
        if (jsapiService == null) {
            synchronized (this) {
                if (jsapiService == null) {
                    jsapiService = new JsapiService.Builder()
                            .config(getWechatPayConfig())
                            .build();
                    log.info("微信小程序支付服务初始化完成");
                }
            }
        }
        return jsapiService;
    }

    /**
     * 获取退款服务（懒加载）
     */
    public RefundService getRefundService() {
        if (refundService == null) {
            synchronized (this) {
                if (refundService == null) {
                    refundService = new RefundService.Builder()
                            .config(getWechatPayConfig())
                            .build();
                    log.info("微信支付退款服务初始化完成");
                }
            }
        }
        return refundService;
    }

    /**
     * 获取通知配置（懒加载）
     */
    public NotificationConfig getNotificationConfig() {
        if (notificationConfig == null) {
            synchronized (this) {
                if (notificationConfig == null) {
                    notificationConfig = new RSAAutoCertificateConfig.Builder()
                            .merchantId(wechatPayConfig.getMchId())
                            .privateKeyFromPath(wechatPayConfig.getPrivateKeyPath())
                            .merchantSerialNumber(wechatPayConfig.getMerchantSerialNumber())
                            .apiV3Key(wechatPayConfig.getApiV3Key())
                            .build();
                    log.info("微信支付通知配置初始化完成");
                }
            }
        }
        return notificationConfig;
    }

    /**
     * 构建微信支付配置
     */
    private RSAAutoCertificateConfig buildWechatPayConfig() {
        try {
            // 验证私钥文件是否存在
            validatePrivateKeyFile();

            return new RSAAutoCertificateConfig.Builder()
                    .merchantId(wechatPayConfig.getMchId())
                    .privateKeyFromPath(wechatPayConfig.getPrivateKeyPath())
                    .merchantSerialNumber(wechatPayConfig.getMerchantSerialNumber())
                    .apiV3Key(wechatPayConfig.getApiV3Key())
                    .build();

        } catch (Exception e) {
            log.error("微信支付证书配置构建失败", e);
            throw new RuntimeException("微信支付证书配置构建失败: " + e.getMessage(), e);
        }
    }

    /**
     * 验证私钥文件
     */
    private void validatePrivateKeyFile() {
        String privateKeyPath = wechatPayConfig.getPrivateKeyPath();

        // 处理classpath路径
        if (privateKeyPath.startsWith("classpath:")) {
            String resourcePath = privateKeyPath.substring(10);
            if (getClass().getClassLoader().getResource(resourcePath) == null) {
                throw new RuntimeException("微信支付私钥文件不存在: " + privateKeyPath);
            }
        } else {
            // 处理绝对路径
            if (!Files.exists(Paths.get(privateKeyPath))) {
                throw new RuntimeException("微信支付私钥文件不存在: " + privateKeyPath);
            }
        }

        log.info("微信支付私钥文件验证通过: {}", privateKeyPath);
    }

    /**
     * 重新加载证书配置（用于证书更新）
     */
    public void reloadCertificate() {
        synchronized (this) {
            config = null;
            jsapiService = null;
            refundService = null;
            notificationConfig = null;
            log.info("微信支付证书配置已重新加载");
        }
    }
}
