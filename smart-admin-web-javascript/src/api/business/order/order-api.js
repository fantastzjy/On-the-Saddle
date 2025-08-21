import { getRequest, postRequest } from '/@/lib/axios'

export const orderApi = {

  /**
   * 创建订单
   */
  createOrder: (data) => {
    return postRequest('/api/admin/order/create', data)
  },

  /**
   * 订单列表查询
   */
  queryOrderList: (data) => {
    return postRequest('/api/admin/order/query', data)
  },

  /**
   * 获取订单详情
   */
  getOrderDetail: (orderId) => {
    return getRequest(`/api/admin/order/detail/${orderId}`)
  },

  /**
   * 更新订单状态
   */
  updateOrderStatus: (data) => {
    return postRequest('/api/admin/order/updateStatus', data)
  },

  /**
   * 取消订单
   */
  cancelOrder: (orderId, cancelReason) => {
    const params = cancelReason ? { cancelReason } : {};
    return postRequest(`/api/admin/order/cancel/${orderId}`, null, params)
  },

  /**
   * 确认支付
   */
  confirmPayment: (orderId) => {
    return postRequest(`/api/admin/order/confirmPayment/${orderId}`)
  },

  /**
   * 确认订单
   */
  confirmOrder: (orderId) => {
    return postRequest(`/api/admin/order/confirm/${orderId}`)
  },

  /**
   * 完成订单
   */
  completeOrder: (orderId) => {
    return postRequest(`/api/admin/order/complete/${orderId}`)
  },

  /**
   * 核销预约
   */
  completeBooking: (bookingId) => {
    return postRequest(`/api/admin/order/booking/complete/${bookingId}`)
  },

  /**
   * 订单统计
   */
  getOrderStatistics: (clubId) => {
    return getRequest('/api/admin/order/statistics', { clubId })
  },

  /**
   * 今日订单
   */
  getTodayOrders: (clubId) => {
    return getRequest('/api/admin/order/today', { clubId })
  },

  /**
   * 待处理订单
   */
  getPendingOrders: (clubId) => {
    return getRequest('/api/admin/order/pending', { clubId })
  }
}