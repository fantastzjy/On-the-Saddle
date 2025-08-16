/*
 * 商品管理API接口
 * 
 * @Author: 1024创新实验室
 * @Date: 2024-08-16
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const productApi = {
  // ==================== 商品基础管理 ====================

  // 分页查询商品列表
  queryProductList: (param) => {
    return postRequest('/api/admin/product/query', param);
  },

  // 根据ID查询商品详情
  getProductDetail: (productId) => {
    return getRequest(`/api/admin/product/detail/${productId}`);
  },

  // 新增商品
  addProduct: (param) => {
    return postRequest('/api/admin/product/add', param);
  },

  // 更新商品
  updateProduct: (param) => {
    return postRequest('/api/admin/product/update', param);
  },

  // 删除商品
  deleteProduct: (productId) => {
    return getRequest(`/api/admin/product/delete/${productId}`);
  },

  // 批量删除商品
  batchDeleteProduct: (productIds) => {
    return postRequest('/api/admin/product/batchDelete', productIds);
  },

  // 更新商品状态（上架/下架）
  updateProductStatus: (productId, status) => {
    return postRequest('/api/admin/product/updateStatus', { productId, status });
  },

  // 批量更新商品状态
  batchUpdateProductStatus: (productIds, status) => {
    return postRequest('/api/admin/product/batchUpdateStatus', { productIds, status });
  },

  // ==================== 动态表单配置 ====================

  // 根据商品类型获取表单配置
  getFormConfig: (productType) => {
    return getRequest(`/api/admin/product/form/config/${productType}`);
  },

  // 验证表单数据
  validateFormData: (productType, formData) => {
    return postRequest('/api/admin/product/form/validate', { productType, formData });
  },

  // ==================== 价格计算 ====================

  // 计算商品价格
  calculatePrice: (param) => {
    return postRequest('/api/admin/product/price/calculate', param);
  },

  // 批量价格计算
  calculateBatchPrice: (param) => {
    return postRequest('/api/admin/product/price/calculateBatch', param);
  },

  // 获取价格预估
  getPriceEstimate: (productId) => {
    return getRequest(`/api/admin/product/price/estimate/${productId}`);
  },

  // ==================== 商品配置查询 ====================

  // 获取商品类型选项
  getProductTypeOptions: () => {
    return getRequest('/api/admin/product/options/types');
  },

  // 获取上架商品列表（用于选择器）
  getOnlineProductList: (clubId) => {
    return getRequest(`/api/admin/product/online/${clubId}`);
  },

  // 根据商品类型查询商品
  getProductsByType: (productType, clubId) => {
    return getRequest(`/api/admin/product/type/${productType}/${clubId}`);
  },

  // ==================== 商品关联管理 ====================

  // 获取商品关联的教练列表
  getProductCoaches: (productId) => {
    return getRequest(`/api/admin/product/coaches/${productId}`);
  },

  // 更新商品教练关联
  updateProductCoaches: (productId, coachIds) => {
    return postRequest('/api/admin/product/coaches/update', { productId, coachIds });
  },

  // ==================== 数据统计 ====================

  // 获取商品统计信息
  getProductStatistics: (clubId) => {
    return getRequest(`/api/admin/product/statistics/${clubId}`);
  },

  // 获取商品销售统计
  getProductSalesStatistics: (param) => {
    return postRequest('/api/admin/product/statistics/sales', param);
  }
};