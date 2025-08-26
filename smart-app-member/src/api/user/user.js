import request from '@/lib/request';

// 获取用户信息
export const getUserInfo = (data) => {
  return request({
    url: '/app/member/info/info',
    data,
    method: 'POST',
  });
};

// 获取用户信息
export const updateUserInfo = (data) => {
  return request({
    url: '/app/member/info/update',
    data,
    method: 'POST',
  });
};

// 更新用户背景图片
export const uploadAvatar = (data) => {
  return request({
    url: '/app/member/info/avatar/upload',
    data,
    method: 'POST',
  });
};
