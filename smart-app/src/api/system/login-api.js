/*
 *  登录
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:59:58
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import request from '@/lib/request';
import { getRequest, postRequest } from '@/lib/smart-request';

export const loginApi = {
  /**
   * 登录 @author 卓大
   */
  login: (param) => {
    return request({
      url: '/app/login/wxLogin',
      param,
      method: 'POST',
    });
  },

  /**
   * 退出登录 @author 卓大
   */
  logout: () => {
    return postRequest('/app/login/logout');
  },

  /**
   * 检查注册状态 @author Claude Code
   */
  checkRegistration: (unionId, role) => {
    return getRequest(`/app/login/checkRegistration?unionId=${unionId}&role=${role}`);
  },

  /**
   * 获取验证码 @author 卓大
   */
  getCaptcha: () => {
    return getRequest('/login/getCaptcha');
  },

  /**
   * 获取登录信息 @author Claude Code (修改为会员专用接口)
   */
  getLoginInfo: () => {
    return postRequest('/app/member/info/info', {});
  },
  /**
   * 获取双因子登录标识 @author 卓大
   */
  getTwoFactorLoginFlag: () => {
    return getRequest('/login/getTwoFactorLoginFlag');
  },
  /**
   * 获取邮箱登录验证码 @author 卓大
   */
  sendLoginEmailCode: (loginName) => {
    return getRequest(`/login/sendEmailCode/${loginName}`);
  },
};
