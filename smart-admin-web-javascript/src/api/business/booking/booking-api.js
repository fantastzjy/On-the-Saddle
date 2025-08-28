import { getRequest, postRequest } from '/@/lib/axios'

export const bookingApi = {

  /**
   * 分页查询预约列表
   */
  getBookingList: (data) => {
    return postRequest('/api/admin/booking/query', data)
  },

  /**
   * 获取简化预约列表 - 用于列表视图
   */
  getSimpleBookingList: (data) => {
    return postRequest('/api/admin/booking/simple-query', data)
  },

  /**
   * 获取预约详情
   */
  getBookingDetail: (bookingId) => {
    return getRequest(`/api/admin/booking/detail/${bookingId}`)
  },

  /**
   * 创建预约
   */
  addBooking: (data) => {
    return postRequest('/api/admin/booking/create', data)
  },

  /**
   * 更新预约
   */
  updateBooking: (data) => {
    return postRequest('/api/admin/booking/update', data)
  },

  /**
   * 确认预约
   */
  confirmBooking: (bookingId) => {
    return postRequest(`/api/admin/booking/confirm/${bookingId}`)
  },

  /**
   * 核销预约
   */
  checkinBooking: (bookingId) => {
    return postRequest(`/api/admin/booking/checkin/${bookingId}`)
  },

  /**
   * 完成预约（别名方法，实际调用核销接口）
   */
  completeBooking: (bookingId) => {
    return postRequest(`/api/admin/booking/checkin/${bookingId}`)
  },

  /**
   * 取消预约
   */
  cancelBooking: (bookingId, reason) => {
    const data = reason ? { reason } : {}
    return postRequest(`/api/admin/booking/cancel/${bookingId}`, data)
  },

  /**
   * 批量确认预约
   */
  batchConfirmBookings: (bookingIds) => {
    return postRequest('/api/admin/booking/batchConfirm', { bookingIds })
  },

  /**
   * 批量取消预约
   */
  batchCancelBookings: (bookingIds) => {
    return postRequest('/api/admin/booking/batchCancel', { bookingIds })
  },

  /**
   * 获取会员预约历史
   */
  getMemberBookingHistory: (memberId) => {
    return getRequest(`/api/admin/booking/member/${memberId}/history`)
  },

  /**
   * 获取预约统计
   */
  getBookingStatistics: (params) => {
    return getRequest('/api/admin/booking/statistics', params)
  },

  /**
   * 预约时间冲突检测
   */
  checkTimeConflict: (data) => {
    return postRequest('/api/admin/booking/checkConflict', data)
  },

  /**
   * 检查冲突（别名方法）
   */
  checkConflict: (data) => {
    return postRequest('/api/admin/booking/checkConflict', data)
  },

  /**
   * 获取可用时间段
   */
  getAvailableTimeSlots: (params) => {
    return getRequest('/api/admin/booking/availableTimeSlots', params)
  },

  /**
   * 预约改期
   */
  rescheduleBooking: (data) => {
    return postRequest('/api/admin/booking/reschedule', data)
  },

  /**
   * 改期冲突检测
   */
  checkRescheduleConflict: (data) => {
    return postRequest('/api/admin/booking/checkRescheduleConflict', data)
  },

  // ==================== 内联编辑接口 ====================

  /**
   * 内联编辑：切换预约教练
   */
  updateBookingCoach: (bookingId, coachId) => {
    return postRequest('/api/admin/booking/inline-edit/coach', null, {
      bookingId,
      coachId
    })
  },

  /**
   * 内联编辑：切换预约马匹
   */
  updateBookingHorse: (bookingId, horseId) => {
    return postRequest('/api/admin/booking/inline-edit/horse', null, {
      bookingId,
      horseId
    })
  },

  /**
   * 内联编辑：切换预约时间
   */
  updateBookingTime: (bookingId, newStartTime, newEndTime) => {
    return postRequest('/api/admin/booking/inline-edit/time', null, {
      bookingId,
      newStartTime,
      newEndTime
    })
  },

  /**
   * 内联编辑：切换预约状态
   */
  updateBookingStatus: (bookingId, status) => {
    return postRequest('/api/admin/booking/inline-edit/status', null, {
      bookingId,
      status
    })
  }
}
