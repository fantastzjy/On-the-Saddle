package net.lab1024.sa.admin.module.business.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.payment.dao.PaymentRecordDao;
import net.lab1024.sa.admin.module.business.payment.domain.entity.PaymentRecordEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 微信支付回调处理服务
 * 
 * @author 1024创新实验室：开云
 * @since 2024-12-30
 */
@Slf4j
@Service
public class WechatPayCallbackService {

    @Resource
    private PaymentRecordDao paymentRecordDao;

    @Resource
    private WechatPayCertificateService certificateService;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 处理微信支付成功回调
     *
     * @param requestBody 请求体
     * @param wechatpaySignature 微信签名
     * @param wechatpayTimestamp 微信时间戳
     * @param wechatpayNonce 微信随机字符串
     * @param wechatpaySerial 微信证书序列号
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> handlePaymentNotify(String requestBody,
                                                   String wechatpaySignature,
                                                   String wechatpayTimestamp,
                                                   String wechatpayNonce,
                                                   String wechatpaySerial) {
        try {
            log.info("收到微信支付回调通知，开始验证签名");

            // 1. 验证签名并解析回调数据
            Transaction transaction = parseAndVerifyCallback(
                    requestBody, wechatpaySignature, wechatpayTimestamp, 
                    wechatpayNonce, wechatpaySerial);

            if (transaction == null) {
                log.error("微信支付回调签名验证失败");
                return ResponseDTO.userErrorParam("签名验证失败");
            }

            // 2. 处理支付成功逻辑
            String result = processPaymentSuccess(transaction, requestBody);

            if ("SUCCESS".equals(result)) {
                log.info("微信支付回调处理成功，商户订单号：{}", transaction.getOutTradeNo());
                return ResponseDTO.ok("SUCCESS");
            } else {
                log.error("微信支付回调处理失败，商户订单号：{}", transaction.getOutTradeNo());
                return ResponseDTO.userErrorParam("FAIL");
            }

        } catch (Exception e) {
            log.error("处理微信支付回调异常", e);
            return ResponseDTO.userErrorParam("FAIL");
        }
    }

    /**
     * 处理微信退款回调
     *
     * @param requestBody 请求体
     * @param wechatpaySignature 微信签名
     * @param wechatpayTimestamp 微信时间戳
     * @param wechatpayNonce 微信随机字符串
     * @param wechatpaySerial 微信证书序列号
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> handleRefundNotify(String requestBody,
                                                  String wechatpaySignature,
                                                  String wechatpayTimestamp,
                                                  String wechatpayNonce,
                                                  String wechatpaySerial) {
        try {
            log.info("收到微信退款回调通知，开始验证签名");

            // 1. 验证签名并解析退款回调数据
            JsonNode refundData = parseRefundCallback(
                    requestBody, wechatpaySignature, wechatpayTimestamp, 
                    wechatpayNonce, wechatpaySerial);

            if (refundData == null) {
                log.error("微信退款回调签名验证失败");
                return ResponseDTO.userErrorParam("签名验证失败");
            }

            // 2. 处理退款成功逻辑
            String result = processRefundSuccess(refundData, requestBody);

            if ("SUCCESS".equals(result)) {
                log.info("微信退款回调处理成功");
                return ResponseDTO.ok("SUCCESS");
            } else {
                log.error("微信退款回调处理失败");
                return ResponseDTO.userErrorParam("FAIL");
            }

        } catch (Exception e) {
            log.error("处理微信退款回调异常", e);
            return ResponseDTO.userErrorParam("FAIL");
        }
    }

    /**
     * 验证签名并解析支付回调数据
     */
    private Transaction parseAndVerifyCallback(String requestBody, String signature,
                                               String timestamp, String nonce, String serial) {
        try {
            // 构建请求参数
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serial)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(requestBody)
                    .build();

            // 使用微信支付SDK验证签名并解析数据
            NotificationParser parser = new NotificationParser(certificateService.getNotificationConfig());
            
            // 解析支付成功通知
            Transaction transaction = parser.parse(requestParam, Transaction.class);
            
            log.info("微信支付回调签名验证成功，商户订单号：{}, 微信订单号：{}, 交易状态：{}", 
                    transaction.getOutTradeNo(), transaction.getTransactionId(), transaction.getTradeState());
            
            return transaction;

        } catch (Exception e) {
            log.error("微信支付回调签名验证失败", e);
            return null;
        }
    }

    /**
     * 验证签名并解析退款回调数据
     */
    private JsonNode parseRefundCallback(String requestBody, String signature,
                                         String timestamp, String nonce, String serial) {
        try {
            // 构建请求参数
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serial)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(requestBody)
                    .build();

            // 使用微信支付SDK验证签名
            NotificationParser parser = new NotificationParser(certificateService.getNotificationConfig());
            
            // 解析退款通知（使用JsonNode因为退款通知结构更复杂）
            String decryptedData = parser.parse(requestParam, String.class);
            JsonNode refundData = objectMapper.readTree(decryptedData);
            
            log.info("微信退款回调签名验证成功");
            
            return refundData;

        } catch (Exception e) {
            log.error("微信退款回调签名验证失败", e);
            return null;
        }
    }

    /**
     * 处理支付成功业务逻辑
     */
    private String processPaymentSuccess(Transaction transaction, String originalData) {
        try {
            String paymentNo = transaction.getOutTradeNo();
            String tradeState = transaction.getTradeState().name();

            // 查询支付记录
            PaymentRecordEntity paymentRecord = paymentRecordDao.selectByPaymentNo(paymentNo);
            if (paymentRecord == null) {
                log.error("支付记录不存在，商户订单号：{}", paymentNo);
                return "FAIL";
            }

            // 检查是否已经处理过（防止重复通知）
            if (paymentRecord.getPaymentStatus() == 3) {
                log.info("订单已处理，避免重复处理，商户订单号：{}", paymentNo);
                return "SUCCESS";
            }

            // 验证金额
            BigDecimal notifyAmount = new BigDecimal(transaction.getAmount().getTotal())
                    .divide(new BigDecimal("100")); // 分转元
            if (notifyAmount.compareTo(paymentRecord.getPaymentAmount()) != 0) {
                log.error("支付金额不匹配，订单金额：{}, 通知金额：{}", 
                        paymentRecord.getPaymentAmount(), notifyAmount);
                return "FAIL";
            }

            // 更新支付记录
            paymentRecord.setTradeNo(transaction.getTransactionId());
            paymentRecord.setCallbackData(originalData);
            paymentRecord.setNotifyTime(LocalDateTime.now());

            if ("SUCCESS".equals(tradeState)) {
                paymentRecord.setPaymentStatus(3); // 支付成功
                paymentRecord.setPaymentTime(LocalDateTime.now());
                
                // 更新数据库
                paymentRecordDao.updateById(paymentRecord);
                
                // TODO: 触发业务逻辑（更新订单状态、创建预约等）
                triggerBusinessLogic(paymentRecord);
                
                log.info("支付成功处理完成，商户订单号：{}", paymentNo);
                return "SUCCESS";
                
            } else {
                paymentRecord.setPaymentStatus(4); // 支付失败
                paymentRecordDao.updateById(paymentRecord);
                
                log.warn("支付失败，商户订单号：{}, 交易状态：{}", paymentNo, tradeState);
                return "SUCCESS"; // 微信要求返回SUCCESS确认接收到通知
            }

        } catch (Exception e) {
            log.error("处理支付成功业务逻辑异常", e);
            return "FAIL";
        }
    }

    /**
     * 处理退款成功业务逻辑
     */
    private String processRefundSuccess(JsonNode refundData, String originalData) {
        try {
            String outTradeNo = refundData.get("out_trade_no").asText();
            String refundStatus = refundData.get("refund_status").asText();

            // 查询支付记录
            PaymentRecordEntity paymentRecord = paymentRecordDao.selectByPaymentNo(outTradeNo);
            if (paymentRecord == null) {
                log.error("支付记录不存在，商户订单号：{}", outTradeNo);
                return "FAIL";
            }

            // 检查是否已经处理过
            if (paymentRecord.getRefundStatus() == 2) {
                log.info("退款已处理，避免重复处理，商户订单号：{}", outTradeNo);
                return "SUCCESS";
            }

            // 更新退款状态
            if ("SUCCESS".equals(refundStatus)) {
                paymentRecord.setRefundStatus(2); // 退款成功
                paymentRecord.setRefundTime(LocalDateTime.now());
                
                // 更新数据库
                paymentRecordDao.updateById(paymentRecord);
                
                // TODO: 触发退款业务逻辑（取消预约、释放资源等）
                triggerRefundBusinessLogic(paymentRecord);
                
                log.info("退款成功处理完成，商户订单号：{}", outTradeNo);
                return "SUCCESS";
                
            } else {
                paymentRecord.setRefundStatus(3); // 退款失败
                paymentRecordDao.updateById(paymentRecord);
                
                log.warn("退款失败，商户订单号：{}, 退款状态：{}", outTradeNo, refundStatus);
                return "SUCCESS";
            }

        } catch (Exception e) {
            log.error("处理退款成功业务逻辑异常", e);
            return "FAIL";
        }
    }

    /**
     * 触发支付成功后的业务逻辑
     */
    private void triggerBusinessLogic(PaymentRecordEntity paymentRecord) {
        // TODO: 实现支付成功后的业务逻辑
        // 1. 更新订单状态为已支付
        // 2. 创建预约记录
        // 3. 发送支付成功通知
        // 4. 记录积分等
        
        log.info("触发支付成功业务逻辑，订单ID：{}", paymentRecord.getOrderId());
    }

    /**
     * 处理Mock支付回调
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> handleMockPaymentCallback(String paymentNo, boolean success) {
        try {
            log.info("🎭 [Mock回调] 处理Mock支付回调，支付单号：{}, 结果：{}", 
                    paymentNo, success ? "成功" : "失败");

            // 查询支付记录
            PaymentRecordEntity paymentRecord = paymentRecordDao.selectByPaymentNo(paymentNo);
            if (paymentRecord == null) {
                log.error("🎭 [Mock回调] 支付记录不存在，支付单号：{}", paymentNo);
                return ResponseDTO.userErrorParam("支付记录不存在");
            }

            // 检查是否已经处理过（防止重复处理）
            if (paymentRecord.getPaymentStatus() == 3) {
                log.info("🎭 [Mock回调] 订单已处理，避免重复处理，支付单号：{}", paymentNo);
                return ResponseDTO.ok("SUCCESS");
            }

            // 更新支付记录
            paymentRecord.setCallbackData("Mock payment callback - " + (success ? "SUCCESS" : "FAILED"));
            paymentRecord.setNotifyTime(LocalDateTime.now());

            if (success) {
                // 模拟支付成功
                paymentRecord.setPaymentStatus(3); // 支付成功
                paymentRecord.setPaymentTime(LocalDateTime.now());
                paymentRecord.setTradeNo("MOCK_TRADE_" + System.currentTimeMillis());
                
                // 更新数据库
                paymentRecordDao.updateById(paymentRecord);
                
                // 触发业务逻辑
                triggerBusinessLogic(paymentRecord);
                
                log.info("🎭 [Mock回调] Mock支付成功处理完成，支付单号：{}", paymentNo);
                return ResponseDTO.ok("SUCCESS");
                
            } else {
                // 模拟支付失败
                paymentRecord.setPaymentStatus(4); // 支付失败
                paymentRecordDao.updateById(paymentRecord);
                
                log.warn("🎭 [Mock回调] Mock支付失败，支付单号：{}", paymentNo);
                return ResponseDTO.ok("SUCCESS"); // 微信要求返回SUCCESS确认接收到通知
            }

        } catch (Exception e) {
            log.error("🎭 [Mock回调] 处理Mock支付回调异常", e);
            return ResponseDTO.userErrorParam("FAIL");
        }
    }

    /**
     * 触发退款成功后的业务逻辑
     */
    private void triggerRefundBusinessLogic(PaymentRecordEntity paymentRecord) {
        // TODO: 实现退款成功后的业务逻辑
        // 1. 取消预约
        // 2. 释放资源
        // 3. 发送退款成功通知
        // 4. 扣减积分等
        
        log.info("触发退款成功业务逻辑，订单ID：{}", paymentRecord.getOrderId());
    }
}