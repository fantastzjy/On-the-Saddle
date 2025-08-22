package net.lab1024.sa.admin.module.business.member.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.miniapp")
public class WechatMiniappConfig {

    /**
     * 小程序AppID
     */
    private String appId;

    /**
     * 小程序AppSecret
     */
    private String appSecret;

    /**
     * 微信服务器域名
     */
    private String serverDomain = "https://api.weixin.qq.com";

    /**
     * 登录凭证校验接口
     */
    private String jscode2sessionUrl = "/sns/jscode2session";

    /**
     * 获取用户信息接口
     */
    private String getUserInfoUrl = "/sns/userinfo";

    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectTimeout = 5000;

    /**
     * 读取超时时间（毫秒）
     */
    private Integer readTimeout = 10000;
}