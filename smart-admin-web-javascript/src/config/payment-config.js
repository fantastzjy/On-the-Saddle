/**
 * 支付方式配置
 * 
 * @Author 1024创新实验室
 * @Date 2024-08-21
 * @Copyright 马术俱乐部管理系统
 */

// 支付方式代码常量
export const PAYMENT_METHODS = {
  WECHAT: 'wechat',
  ALIPAY: 'alipay', 
  CASH: 'cash',
  PACKAGE: 'package'
};

// 支付方式配置
export const PAYMENT_METHOD_CONFIG = {
  [PAYMENT_METHODS.WECHAT]: {
    name: '微信支付',
    icon: 'wechat',
    color: '#1AAD19',
    bgColor: '#f0f9ff'
  },
  [PAYMENT_METHODS.ALIPAY]: {
    name: '支付宝',
    icon: 'alipay',
    color: '#1677FF',
    bgColor: '#f0f9ff'
  },
  [PAYMENT_METHODS.CASH]: {
    name: '现金支付',
    icon: 'money-collect',
    color: '#52C41A',
    bgColor: '#f6ffed'
  },
  [PAYMENT_METHODS.PACKAGE]: {
    name: '课时包支付',
    icon: 'gift',
    color: '#722ED1',
    bgColor: '#f9f0ff'
  }
};

/**
 * 根据支付方式代码获取配置信息
 * @param {string} code 支付方式代码
 * @returns {Object} 支付方式配置
 */
export const getPaymentMethodConfig = (code) => {
  return PAYMENT_METHOD_CONFIG[code] || {
    name: '未知支付方式',
    icon: 'question-circle',
    color: '#8c8c8c',
    bgColor: '#fafafa'
  };
};

/**
 * 根据支付方式代码获取名称
 * @param {string} code 支付方式代码
 * @returns {string} 支付方式名称
 */
export const getPaymentMethodName = (code) => {
  return getPaymentMethodConfig(code).name;
};

/**
 * 获取所有支付方式列表（用于下拉选择）
 * @returns {Array} 支付方式列表
 */
export const getPaymentMethodOptions = () => {
  return Object.entries(PAYMENT_METHOD_CONFIG).map(([code, config]) => ({
    value: code,
    label: config.name,
    icon: config.icon,
    color: config.color
  }));
};

/**
 * 验证支付方式代码是否有效
 * @param {string} code 支付方式代码
 * @returns {boolean} 是否有效
 */
export const isValidPaymentMethod = (code) => {
  return Object.prototype.hasOwnProperty.call(PAYMENT_METHOD_CONFIG, code);
};