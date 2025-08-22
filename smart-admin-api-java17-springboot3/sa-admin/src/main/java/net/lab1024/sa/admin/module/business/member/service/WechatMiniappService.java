package net.lab1024.sa.admin.module.business.member.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.config.WechatMiniappConfig;
import net.lab1024.sa.admin.module.business.member.domain.vo.WechatJscode2SessionVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.WechatUserInfoVO;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序API服务
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
@Slf4j
@Service
public class WechatMiniappService {

    @Resource
    private WechatMiniappConfig wechatConfig;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 通过code获取session信息
     *
     * @param code 登录时获取的code
     * @return 微信响应信息
     */
    public ResponseDTO<WechatJscode2SessionVO> jscode2session(String code) {
        try {
            if (StrUtil.isBlank(code)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "code不能为空");
            }

            // 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("appid", wechatConfig.getAppId());
            params.put("secret", wechatConfig.getAppSecret());
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");

            // 构建请求URL
            String url = wechatConfig.getServerDomain() + wechatConfig.getJscode2sessionUrl();

            log.info("调用微信jscode2session接口，url: {}, params: {}", url, JSONUtil.toJsonStr(params));

            // 发起HTTP请求
             String responseStr = HttpUtil.get(url, params, wechatConfig.getConnectTimeout());

            log.info("微信jscode2session接口响应: {}", responseStr);

            if (StrUtil.isBlank(responseStr)) {
                return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "微信接口无响应");
            }

            // 解析响应
            WechatJscode2SessionVO response = objectMapper.readValue(responseStr, WechatJscode2SessionVO.class);

            if (!response.isSuccess()) {
                log.error("微信jscode2session接口调用失败，errcode: {}, errmsg: {}", response.getErrCode(), response.getErrMsg());
                return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "微信登录失败：" + response.getErrMsg());
            }

            return ResponseDTO.ok(response);

        } catch (Exception e) {
            log.error("调用微信jscode2session接口异常", e);
            return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "微信登录失败，请重试");
        }
    }

    /**
     * 解密微信用户信息
     *
     * @param encryptedData 加密数据
     * @param iv            初始向量
     * @param sessionKey    会话密钥
     * @return 用户信息
     */
    public ResponseDTO<WechatUserInfoVO> decryptUserInfo(String encryptedData, String iv, String sessionKey) {
        try {
            if (StrUtil.hasBlank(encryptedData, iv, sessionKey)) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "解密参数不能为空");
            }

            // Base64解码
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);

            // AES解密
            SecretKeySpec secretKeySpec = new SecretKeySpec(sessionKeyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedStr = new String(decryptedBytes, StandardCharsets.UTF_8);

            log.info("解密后的用户信息: {}", decryptedStr);

            // 解析JSON
            WechatUserInfoVO userInfo = objectMapper.readValue(decryptedStr, WechatUserInfoVO.class);

            return ResponseDTO.ok(userInfo);

        } catch (Exception e) {
            log.error("解密微信用户信息失败", e);
            return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR, "用户信息解密失败");
        }
    }

    /**
     * 验证数据完整性
     *
     * @param rawData    原始数据
     * @param sessionKey 会话密钥
     * @param signature  签名
     * @return 是否验证通过
     */
    public boolean validateSignature(String rawData, String sessionKey, String signature) {
        try {
            if (StrUtil.hasBlank(rawData, sessionKey, signature)) {
                return false;
            }

            String computedSignature = DigestUtil.sha1Hex(rawData + sessionKey);
            return signature.equals(computedSignature);

        } catch (Exception e) {
            log.error("验证微信数据签名失败", e);
            return false;
        }
    }

    /**
     * 检查配置是否完整
     */
    public boolean isConfigValid() {
        return StrUtil.isNotBlank(wechatConfig.getAppId())
               && StrUtil.isNotBlank(wechatConfig.getAppSecret());
    }

    /**
     * 获取错误码说明
     */
    public String getErrorMessage(Integer errCode) {
        if (errCode == null) {
            return "未知错误";
        }

        switch (errCode) {
            case -1:
                return "系统繁忙，此时请开发者稍候再试";
            case 40013:
                return "不合法的 AppID";
            case 40014:
                return "不合法的 access_token";
            case 40029:
                return "code 无效";
            case 45011:
                return "频率限制，每个用户每分钟100次";
            case 40226:
                return "高风险等级用户，小程序登录拦截";
            default:
                return "错误码：" + errCode;
        }
    }
}
