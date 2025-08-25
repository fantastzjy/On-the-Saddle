/*
 * 报告管理相关API
 *
 * @Author: 1024创新实验室
 * @Date: 2024-12-07
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

import { postRequest, getRequest, request } from '/@/lib/axios';

export const reportApi = {
  // 生成报告
  generateReport: (data) => {
    return postRequest('/api/admin/report/generate', data);
  },
  
  // 导出PDF报告
  exportToPdf: (data) => {
    console.log('reportApi.exportToPdf - 请求数据:', data);
    
    const requestConfig = {
      method: 'post',
      url: '/api/admin/report/export/pdf',
      data: data,
      responseType: 'blob',
      timeout: 60000 // 60秒超时
    };
    
    console.log('reportApi.exportToPdf - 请求配置:', requestConfig);
    
    return request(requestConfig).then(response => {
      console.log('reportApi.exportToPdf - 原始响应:', response);
      console.log('reportApi.exportToPdf - 响应类型:', typeof response);
      console.log('reportApi.exportToPdf - 响应instanceof:', {
        isArrayBuffer: response instanceof ArrayBuffer,
        isBlob: response instanceof Blob,
        isString: typeof response === 'string',
        isObject: response instanceof Object,
        constructor: response?.constructor?.name
      });
      return response;
    }).catch(error => {
      console.error('reportApi.exportToPdf - 请求失败:', error);
      throw error;
    });
  },
  
  // 导出Excel报告
  exportToExcel: (data) => {
    console.log('reportApi.exportToExcel - 请求数据:', data);
    
    const requestConfig = {
      method: 'post',
      url: '/api/admin/report/export/excel',
      data: data,
      responseType: 'blob',
      timeout: 60000 // 60秒超时
    };
    
    console.log('reportApi.exportToExcel - 请求配置:', requestConfig);
    
    return request(requestConfig).then(response => {
      console.log('reportApi.exportToExcel - 原始响应:', response);
      console.log('reportApi.exportToExcel - 响应类型:', typeof response);
      console.log('reportApi.exportToExcel - 响应instanceof:', {
        isArrayBuffer: response instanceof ArrayBuffer,
        isBlob: response instanceof Blob,
        isString: typeof response === 'string',
        isObject: response instanceof Object,
        constructor: response?.constructor?.name
      });
      return response;
    }).catch(error => {
      console.error('reportApi.exportToExcel - 请求失败:', error);
      throw error;
    });
  },
  
  // 获取支持的报告类型
  getSupportedReportTypes: () => {
    return getRequest('/api/admin/report/types');
  }
};