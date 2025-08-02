/*
 * 俱乐部API
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2024-01-08
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const clubApi = {
  // 分页查询俱乐部
  pageQuery: (param) => {
    return postRequest('/club/club/page/query', param);
  },

  // 查询俱乐部详情
  detail: (clubId) => {
    return getRequest(`/club/club/get/${clubId}`);
  },

  // 新建俱乐部
  create: (param) => {
    return postRequest('/club/club/create', param);
  },

  // 更新俱乐部
  update: (param) => {
    return postRequest('/club/club/update', param);
  },

  // 删除俱乐部
  delete: (clubId) => {
    return getRequest(`/club/club/delete/${clubId}`);
  },

  // 俱乐部列表查询
  queryList: (isValid) => {
    let query = '';
    if (isValid !== undefined && isValid !== null) {
      query = `?isValid=${isValid}`;
    }
    return getRequest(`/club/club/query/list${query}`);
  },
};