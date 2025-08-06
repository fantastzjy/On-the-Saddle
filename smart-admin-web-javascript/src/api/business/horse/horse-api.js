/*
 * 马匹管理API
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2024-01-08
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const horseApi = {
  // 分页查询马匹
  pageQuery: (param) => {
    return postRequest('/club/horse/page/query', param);
  },

  // 查询马匹详情
  detail: (horseId) => {
    return getRequest(`/club/horse/get/${horseId}`);
  },

  // 新建马匹
  create: (param) => {
    return postRequest('/club/horse/create', param);
  },

  // 更新马匹
  update: (param) => {
    return postRequest('/club/horse/update', param);
  },

  // 删除马匹
  delete: (horseId) => {
    return getRequest(`/club/horse/delete/${horseId}`);
  },

  // 马匹列表查询
  queryList: (clubId, horseType) => {
    let query = '';
    const params = [];
    if (clubId !== undefined && clubId !== null) {
      params.push(`clubId=${clubId}`);
    }
    if (horseType !== undefined && horseType !== null) {
      params.push(`horseType=${horseType}`);
    }
    if (params.length > 0) {
      query = `?${params.join('&')}`;
    }
    return getRequest(`/club/horse/query/list${query}`);
  },
};

export const horseHealthPlanApi = {
  // 分页查询健康计划
  pageQuery: (param) => {
    return postRequest('/club/horse/health/plan/page/query', param);
  },

  // 查询健康计划详情
  detail: (planId) => {
    return getRequest(`/club/horse/health/plan/get/${planId}`);
  },

  // 新建健康计划
  create: (param) => {
    return postRequest('/club/horse/health/plan/create', param);
  },

  // 更新健康计划
  update: (param) => {
    return postRequest('/club/horse/health/plan/update', param);
  },

  // 删除健康计划
  delete: (planId) => {
    return getRequest(`/club/horse/health/plan/delete/${planId}`);
  },

  // 查询马匹的健康计划列表
  queryByHorseId: (horseId) => {
    return getRequest(`/club/horse/health/plan/query/horse/${horseId}`);
  },

  // 查询需要提醒的健康计划
  queryReminder: (days) => {
    return getRequest(`/club/horse/health/plan/reminder/${days}`);
  },

  // 获取健康计划类型选项
  getPlanTypes: () => {
    return getRequest('/club/horse/health/plan/types');
  },
};

export const horseHealthRecordApi = {
  // 分页查询健康记录
  pageQuery: (param) => {
    return postRequest('/club/horse/health/record/page/query', param);
  },

  // 查询健康记录详情
  detail: (recordId) => {
    return getRequest(`/club/horse/health/record/get/${recordId}`);
  },

  // 新建健康记录
  create: (param) => {
    return postRequest('/club/horse/health/record/create', param);
  },

  // 更新健康记录
  update: (param) => {
    return postRequest('/club/horse/health/record/update', param);
  },

  // 删除健康记录
  delete: (recordId) => {
    return getRequest(`/club/horse/health/record/delete/${recordId}`);
  },

  // 查询马匹的健康记录列表
  queryByHorseId: (horseId) => {
    return getRequest(`/club/horse/health/record/query/horse/${horseId}`);
  },

  // 查询计划的健康记录列表
  queryByPlanId: (planId) => {
    return getRequest(`/club/horse/health/record/query/plan/${planId}`);
  },

  // 根据健康计划快速创建记录
  createFromPlan: (planId, content, imgUrl, recordData) => {
    let query = '';
    const params = [];
    if (content !== undefined && content !== null) {
      params.push(`content=${encodeURIComponent(content)}`);
    }
    if (imgUrl !== undefined && imgUrl !== null) {
      params.push(`imgUrl=${encodeURIComponent(imgUrl)}`);
    }
    if (recordData !== undefined && recordData !== null) {
      params.push(`recordData=${encodeURIComponent(recordData)}`);
    }
    if (params.length > 0) {
      query = `?${params.join('&')}`;
    }
    return postRequest(`/club/horse/health/record/create/from-plan/${planId}${query}`);
  },
};