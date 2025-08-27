import { postRequest, getRequest, deleteRequest } from '/@/lib/axios';

const API_PREFIX = '/business/stable-rental';

// 马房租赁API
export const stableRentalApi = {
  // 分页查询马房租赁列表
  queryPage: (params) => {
    return postRequest(`${API_PREFIX}/page`, params);
  },

  // 根据ID查询马房租赁详情
  getDetailById: (rentalId) => {
    return getRequest(`${API_PREFIX}/${rentalId}`);
  },

  // 创建马房租赁记录
  create: (params) => {
    return postRequest(`${API_PREFIX}`, params);
  },

  // 更新马房租赁记录
  update: (params) => {
    return postRequest(`${API_PREFIX}/update`, params);
  },

  // 删除马房租赁记录
  delete: (rentalId) => {
    return deleteRequest(`${API_PREFIX}/${rentalId}`);
  },

  // 批量删除马房租赁记录
  batchDelete: (rentalIdList) => {
    return postRequest(`${API_PREFIX}/batch/delete`, rentalIdList);
  },

  // 更新租赁状态
  updateStatus: (rentalId, status) => {
    return postRequest(`${API_PREFIX}/${rentalId}/status/${status}`);
  }
};