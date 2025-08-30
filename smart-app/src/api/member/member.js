import request from '@/lib/request';
import { postRequest } from '@/lib/smart-request';

// 会员信息相关API
export const memberApi = {
  // 获取会员详细信息
  getUserInfo: (data) => {
    console.log('memberApi.getUserInfo被调用，参数:', data);
    console.log('使用的接口路径: /app/member/info/info');
    return postRequest('/app/member/info/info', data);
  },

  // 更新会员个人信息  
  updateInfo: (data) => {
    return postRequest('/app/member/info/update', data);
  },

  // 获取家庭组信息
  getFamilyInfo: (data) => {
    return postRequest('/app/member/info/family/info', data);
  },

  // 获取会籍状态
  getMembership: (data) => {
    return request({
      url: '/app/member/info/membership/status',
      data,
      method: 'GET',
    });
  },
};

// 兼容旧的API调用方式
export const undateInfo = memberApi.updateInfo;
export const getMembership = memberApi.getMembership;
export const getMemberInfo = memberApi.getUserInfo;
export const getFamilyInfo = memberApi.getFamilyInfo;

//检查是否注册
export const getRegistration = (data) => {
  return request({
    url: '/app/member/info/check-registration/{unionId}',
    data,
    method: 'GET',
  });
};
