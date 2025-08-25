import { postRequest } from '/@/lib/axios';

/**
 * 教练排课资源视图API
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-25  
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

export const coachScheduleApi = {

  /**
   * 获取教练日视图数据
   */
  getDayView: (params) => {
    return postRequest('/api/coach-schedule/day-view', params);
  },

  /**
   * 获取教练周视图数据  
   */
  getWeekView: (params) => {
    return postRequest('/api/coach-schedule/week-view', params);
  },

  /**
   * 获取教练月视图数据
   */
  getMonthView: (params) => {
    return postRequest('/api/coach-schedule/month-view', params);
  }

};