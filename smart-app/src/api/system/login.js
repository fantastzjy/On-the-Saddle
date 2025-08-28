import request from '@/lib/request';

// 获取用户信息
export const getLoginUserInfo = (data) => {
  return request({
    url: '/app/login/wxLogin',
    data,
    method: 'POST',
  });
};

// 获取用户信息
export const getWechatUserScore = (data) => {
  return request({
    url: 'app/getWechatUserScore',
    data,
    method: 'POST',
  });
};

// 更新用户背景图片
export const updateWechatBgImg = (data) => {
  return request({
    url: 'app/updateWechatBgImg',
    data,
    method: 'POST',
  });
};
