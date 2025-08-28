/**
 * 活动管理API
 *
 * @Author: 1024创新实验室
 * @Date: 2025-08-28
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

import { getRequest, postRequest } from '/@/lib/axios';

export const activityApi = {
  /**
   * 活动列表查询
   */
  queryActivityList: (params) => {
    return postRequest('/api/admin/business/activity/query', params);
  },

  /**
   * 活动详情查询
   */
  queryActivityDetail: (productId) => {
    return getRequest(`/api/admin/business/activity/detail/${productId}`);
  },

  /**
   * 活动新增
   */
  addActivity: (data) => {
    return postRequest('/api/admin/business/activity/add', data);
  },

  /**
   * 活动更新
   */
  updateActivity: (data) => {
    return postRequest('/api/admin/business/activity/update', data);
  },

  /**
   * 活动删除
   */
  deleteActivity: (productId) => {
    return postRequest(`/api/admin/business/activity/delete/${productId}`);
  }
};