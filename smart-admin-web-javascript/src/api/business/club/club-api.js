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
    return postRequest('/mclub/club/page/query', param);
  },

  // 查询俱乐部详情
  detail: (clubId) => {
    return getRequest(`/mclub/club/get/${clubId}`);
  },

  // 新建俱乐部
  create: (param) => {
    return postRequest('/mclub/club/create', param);
  },

  // 更新俱乐部
  update: (param) => {
    return postRequest('/mclub/club/update', param);
  },

  // 删除俱乐部
  delete: (clubId) => {
    return getRequest(`/mclub/club/delete/${clubId}`);
  },

  // 俱乐部列表查询
  queryList: (isValid) => {
    let query = '';
    if (isValid !== undefined && isValid !== null) {
      query = `?isValid=${isValid}`;
    }
    return getRequest(`/mclub/club/query/list${query}`);
  },
};