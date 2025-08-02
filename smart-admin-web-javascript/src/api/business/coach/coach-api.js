/*
 * 教练API
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2024-01-08
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const coachApi = {
  // 分页查询教练
  pageQuery: (param) => {
    return postRequest('/club/coach/page/query', param);
  },

  // 查询教练详情
  detail: (coachId) => {
    return getRequest(`/club/coach/get/${coachId}`);
  },

  // 新建教练
  create: (param) => {
    return postRequest('/club/coach/create', param);
  },

  // 更新教练
  update: (param) => {
    return postRequest('/club/coach/update', param);
  },

  // 删除教练
  delete: (coachId) => {
    return getRequest(`/club/coach/delete/${coachId}`);
  },

  // 教练列表查询
  queryList: (isActive, clubId) => {
    let query = '';
    const params = [];
    if (isActive !== undefined && isActive !== null) {
      params.push(`isActive=${isActive}`);
    }
    if (clubId !== undefined && clubId !== null) {
      params.push(`clubId=${clubId}`);
    }
    if (params.length > 0) {
      query = `?${params.join('&')}`;
    }
    return getRequest(`/club/coach/query/list${query}`);
  },
};