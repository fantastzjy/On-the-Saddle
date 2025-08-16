/*
 * 课表管理API接口
 * 
 * @Author: 1024创新实验室
 * @Date: 2024-08-16
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const scheduleApi = {
  // ==================== 智能排课 ====================

  // 自动生成课表
  autoGenerateSchedule: (param) => {
    return postRequest('/api/admin/schedule/auto/generate', param);
  },

  // 批量安排课程
  batchSchedule: (orderItems) => {
    return postRequest('/api/admin/schedule/batch', orderItems);
  },

  // 推荐最佳时段
  recommendTimeSlots: (param) => {
    return postRequest('/api/admin/schedule/recommend', param);
  },

  // ==================== 冲突检测 ====================

  // 检测时间冲突
  checkTimeConflict: (param) => {
    return postRequest('/api/admin/schedule/conflict/check', param);
  },

  // 获取冲突详情
  getConflictDetail: (param) => {
    return postRequest('/api/admin/schedule/conflict/detail', param);
  },

  // ==================== 课表查询 ====================

  // 分页查询课表
  queryScheduleList: (param) => {
    return postRequest('/api/admin/schedule/query', param);
  },

  // 根据日期范围查询课表
  getScheduleByDateRange: (param) => {
    return postRequest('/api/admin/schedule/dateRange', param);
  },

  // 获取今日课表
  getTodaySchedule: (clubId) => {
    return getRequest(`/api/admin/schedule/today/${clubId}`);
  },

  // 根据教练查询课表
  getScheduleByCoach: (param) => {
    return postRequest('/api/admin/schedule/coach', param);
  },

  // 根据会员查询课表
  getScheduleByMember: (param) => {
    return postRequest('/api/admin/schedule/member', param);
  },

  // ==================== 课表操作 ====================

  // 手动创建课表
  createSchedule: (param) => {
    return postRequest('/api/admin/schedule/create', param);
  },

  // 更新课表信息
  updateSchedule: (param) => {
    return postRequest('/api/admin/schedule/update', param);
  },

  // 删除课表
  deleteSchedule: (scheduleId) => {
    return getRequest(`/api/admin/schedule/delete/${scheduleId}`);
  },

  // 批量删除课表
  batchDeleteSchedule: (scheduleIds) => {
    return postRequest('/api/admin/schedule/batchDelete', scheduleIds);
  },

  // 拖拽调整课表时间
  dragUpdateSchedule: (param) => {
    return postRequest('/api/admin/schedule/drag/update', param);
  },

  // ==================== 课表状态管理 ====================

  // 确认课表
  confirmSchedule: (scheduleId) => {
    return postRequest('/api/admin/schedule/confirm', { scheduleId });
  },

  // 取消课表
  cancelSchedule: (scheduleId, reason) => {
    return postRequest('/api/admin/schedule/cancel', { scheduleId, reason });
  },

  // 批量确认课表
  batchConfirmSchedule: (scheduleIds) => {
    return postRequest('/api/admin/schedule/batchConfirm', scheduleIds);
  },

  // ==================== 资源可用性管理 ====================

  // 获取教练可用时间
  getCoachAvailability: (param) => {
    return postRequest('/api/admin/schedule/coach/availability', param);
  },

  // 设置教练可用时间
  setCoachAvailability: (param) => {
    return postRequest('/api/admin/schedule/coach/availability/set', param);
  },

  // 获取马匹可用时间
  getHorseAvailability: (param) => {
    return postRequest('/api/admin/schedule/horse/availability', param);
  },

  // 设置马匹可用时间
  setHorseAvailability: (param) => {
    return postRequest('/api/admin/schedule/horse/availability/set', param);
  },

  // ==================== 时间段模板管理 ====================

  // 获取时间段模板列表
  getTimeSlotTemplates: (clubId) => {
    return getRequest(`/api/admin/schedule/timeSlot/templates/${clubId}`);
  },

  // 创建时间段模板
  createTimeSlotTemplate: (param) => {
    return postRequest('/api/admin/schedule/timeSlot/template/create', param);
  },

  // 更新时间段模板
  updateTimeSlotTemplate: (param) => {
    return postRequest('/api/admin/schedule/timeSlot/template/update', param);
  },

  // 删除时间段模板
  deleteTimeSlotTemplate: (templateId) => {
    return getRequest(`/api/admin/schedule/timeSlot/template/delete/${templateId}`);
  },

  // ==================== 课表统计 ====================

  // 获取课表统计信息
  getScheduleStatistics: (param) => {
    return postRequest('/api/admin/schedule/statistics', param);
  },

  // 获取教练工作量统计
  getCoachWorkloadStatistics: (param) => {
    return postRequest('/api/admin/schedule/statistics/workload', param);
  },

  // 获取资源利用率统计
  getResourceUtilizationStatistics: (param) => {
    return postRequest('/api/admin/schedule/statistics/utilization', param);
  }
};

export const bookingApi = {
  // ==================== 预约管理 ====================

  // 分页查询预约列表
  queryBookingList: (param) => {
    return postRequest('/api/admin/booking/query', param);
  },

  // 根据ID查询预约详情
  getBookingDetail: (bookingId) => {
    return getRequest(`/api/admin/booking/detail/${bookingId}`);
  },

  // 创建预约
  createBooking: (param) => {
    return postRequest('/api/admin/booking/create', param);
  },

  // 更新预约信息
  updateBooking: (param) => {
    return postRequest('/api/admin/booking/update', param);
  },

  // 确认预约
  confirmBooking: (bookingId) => {
    return postRequest('/api/admin/booking/confirm', { bookingId });
  },

  // 取消预约
  cancelBooking: (bookingId, reason) => {
    return postRequest('/api/admin/booking/cancel', { bookingId, reason });
  },

  // ==================== 前台核销 ====================

  // 签到
  checkin: (param) => {
    return postRequest('/api/admin/booking/checkin', param);
  },

  // 签退
  checkout: (param) => {
    return postRequest('/api/admin/booking/checkout', param);
  },

  // 获取核销记录
  getCheckinRecords: (param) => {
    return postRequest('/api/admin/booking/checkin/records', param);
  },

  // ==================== 预约统计 ====================

  // 获取预约统计
  getBookingStatistics: (param) => {
    return postRequest('/api/admin/booking/statistics', param);
  },

  // 获取会员预约历史
  getMemberBookingHistory: (memberId, param) => {
    return postRequest(`/api/admin/booking/member/${memberId}/history`, param);
  }
};