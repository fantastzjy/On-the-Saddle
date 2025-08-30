import request from '@/lib/request';

// 获取教练列表
export const getCoachList = (data) => {
  return request({
    url: '/app/home/coach/list',
    data,
    method: 'POST',
  });
};
// 获取教练详情
export const getByCoach = (data) => {
  return request({
    url: '/app/home/coach/info',
    data,
    method: 'POST',
  });
};
// 创建订单
export const addOrder = (data) => {
  return request({
    url: '/app/home/order/create',
    data,
    method: 'POST',
  });
};

//获取我的马匹列表
export const getMyHorseList = (data) => {
  return request({
    url: '/app/home/horse/my/list',
    data,
    method: 'POST',
  });
};

//获取课程列表
export const getCourseList = (data) => {
  return request({
    url: '/app/home/course/list',
    data,
    method: 'POST',
  });
};
//获取俱乐部类型
export const getClubType = (data) => {
  return request({
    url: '/app/home/club/type',
    data,
    method: 'POST',
  });
};
//获取俱乐部信息
export const getClubInfo = (data) => {
  return request({
    url: '/app/home/club/info',
    data,
    method: 'POST',
  });
};
//获取活动列表
export const getActivityList = (data) => {
  return request({
    url: '/app/home/activity/list',
    data,
    method: 'POST',
  });
};

//获取家庭成员列表
export const getFamilyMembers = (data) => {
  return request({
    url: '/app/home/family/members',
    data,
    method: 'POST',
  });
};
