/*
 * 俱乐部管理API
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright https://1024lab.net
 */

import { postRequest, getRequest } from '/@/lib/axios';

export const clubApi = {

  // 新建俱乐部
  create: (param) => {
    return postRequest('/mclub/club/create', param);
  },

  // 删除俱乐部
  delete: (clubId) => {
    return getRequest(`/mclub/club/delete/${clubId}`);
  },

  // 查询俱乐部详情
  detail: (clubId) => {
    return getRequest(`/mclub/club/get/${clubId}`);
  },

  // 分页查询俱乐部
  pageQuery: (param) => {
    return postRequest('/mclub/club/page/query', param);
  },

  // 更新俱乐部
  update: (param) => {
    return postRequest('/mclub/club/update', param);
  },

  // 俱乐部列表查询
  queryList: (isValid) => {
    let query = isValid !== undefined ? `?isValid=${isValid}` : '';
    return getRequest(`/mclub/club/query/list${query}`);
  }

};