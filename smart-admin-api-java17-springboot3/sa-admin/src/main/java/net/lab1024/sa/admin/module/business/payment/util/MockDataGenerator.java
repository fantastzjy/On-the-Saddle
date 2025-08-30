package net.lab1024.sa.admin.module.business.payment.util;

import net.lab1024.sa.admin.module.business.payment.domain.vo.WechatPayCreateVO;

import java.util.Random;

/**
 * Mock数据生成工具类
 *
 * @author 1024创新实验室：开云
 * @since 2024-12-30
 */
public class MockDataGenerator {

    private static final Random random = new Random();

    /**
     * 生成Mock支付参数
     */
    public static WechatPayCreateVO generateMockPayParams(String paymentNo) {
        WechatPayCreateVO result = new WechatPayCreateVO();
        result.setPaymentNo(paymentNo);
        result.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        result.setNonceStr("MOCK_NONCE_" + generateRandomString(8));
        result.setPackageValue("prepay_id=MOCK_PREPAY_" + System.currentTimeMillis());
        result.setSignType("RSA");
        result.setPaySign("MOCK_SIGN_" + generateRandomString(32));
        result.setOrderStatus(1); // 待支付
        result.setPaymentStatus(1); // 待支付
        return result;
    }

    /**
     * 模拟支付结果（根据成功率）
     */
    public static boolean simulatePaymentResult(int successRate) {
        return random.nextInt(100) < successRate;
    }

    /**
     * 生成Mock查询订单响应
     */
    public static Object generateMockQueryResponse(String paymentNo, boolean isPaid) {
        // 返回一个简单的Mock响应对象，避免依赖微信SDK类
        return "Mock查询结果: " + paymentNo + " 支付状态: " + (isPaid ? "成功" : "待支付");
    }

    /**
     * 生成随机字符串
     */
    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成Mock微信交易号
     */
    public static String generateMockTradeNo() {
        return "MOCK_TRADE_" + System.currentTimeMillis() + generateRandomString(4);
    }

    /**
     * 生成Mock退款单号
     */
    public static String generateMockRefundNo() {
        return "MOCK_REFUND_" + System.currentTimeMillis() + generateRandomString(4);
    }

    /**
     * 模拟网络延迟
     */
    public static void simulateNetworkDelay(int delaySeconds) {
        if (delaySeconds > 0) {
            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
