import request from '@/lib/request';

// 更新会员个人信息
export const undateInfo = (data) => {
  return request({
    url: '/app/member/info/update',
    data,
    method: 'POST',
  });
};

// 获取会籍状态
export const getMembership = (data) => {
  return request({
    url: '/app/member/info/membership/status',
    data,
    method: 'GET',
  });
};

//获取会员个人信息
export const getMemberInfo = (data) => {
  return request({
    url: '/app/member/info/info',
    data,
    method: 'GET',
  });
};

//获取家庭组信息
export const getFamilyInfo = (data) => {
  return request({
    url: '/app/member/info/family/info',
    data,
    method: 'GET',
  });
};
//检查是否注册

export const getRegistration = (data) => {
  return request({
    url: '/app/member/info/check-registration/{unionId}',
    data,
    method: 'GET',
  });
};
