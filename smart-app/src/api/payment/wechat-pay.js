import { request } from '@/lib/smart-request.js';

/**
 * 创建微信支付订单
 */
export const createWechatPayment = (data) => {
  return request({
    url: '/api/payment/wechat/create',
    data,
    method: 'POST',
  });
};

/**
 * 查询支付订单状态
 */
export const queryPaymentStatus = (paymentNo) => {
  return request({
    url: `/api/payment/wechat/query/${paymentNo}`,
    method: 'GET',
  });
};

/**
 * 申请退款
 */
export const applyRefund = (data) => {
  return request({
    url: '/api/payment/wechat/refund',
    data,
    method: 'POST',
  });
};