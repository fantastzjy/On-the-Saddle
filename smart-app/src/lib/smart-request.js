/*
 *  ajax请求
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:46:03
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { USER_TOKEN } from '@/constants/local-storage-key-const';
import { DATA_TYPE_ENUM } from '@/constants/common-const';
import { decryptData, encryptData } from './encrypt';
import { useUserStore } from '@/store/modules/system/user';
import { BaseURL, CLUB_CONFIG } from './config';

// const baseUrl = import.meta.env.VITE_APP_API_URL;
const baseUrl = BaseURL;

function getUserToken() {
  let token = uni.getStorageSync(USER_TOKEN);
  console.log('🔐 [Smart-Request Token调试] USER_TOKEN常量值:', USER_TOKEN);
  console.log('🔐 [Smart-Request Token调试] 从storage获取的token:', token);
  console.log('🔐 [Smart-Request Token调试] token类型:', typeof token);
  console.log('🔐 [Smart-Request Token调试] token长度:', token ? token.length : 0);
  
  if (token) {
    return token;
  }
  console.warn('🔐 [Smart-Request Token调试] ⚠️ 未获取到有效token，返回空字符串');
  return '';
}

/**
 * 处理返回的消息
 */
function handleResponse(response, resolve, reject) {
  // 如果是加密数据
  if (response.data.dataType === DATA_TYPE_ENUM.ENCRYPT.value) {
    response.data.encryptData = response.data.data;
    let decryptStr = decryptData(response.data.data);
    if (decryptStr) {
      response.data.data = JSON.parse(decryptStr);
    }
  }

  const res = response.data;
  console.log('smart-request响应处理:', res);
  
  // 优化响应判断逻辑 - 使用双重验证
  // 支持 code=0(标准成功) 或 code=1(兼容) 且 ok=true
  const isSuccess = (res.code === 0 || res.code === 1) && res.ok === true;
  
  if (!isSuccess && res.code) {
    console.log('请求失败，code:', res.code, 'ok:', res.ok, 'msg:', res.msg);
    
    // `token` 过期或者账号已在别处登录
    if (res.code === 30007 || res.code === 30008 || res.code === 30012) {
      uni.showToast({
        title: res.msg,
        icon: 'none',
      });
      useUserStore().clearUserLoginInfo();
      uni.navigateTo({ url: '/pages/login/login' });
    }

    uni.showToast({
      title: res.msg,
      icon: 'none',
    });
    reject(response);
  } else {
    console.log('请求成功');
    resolve(res);
  }
}

/**
 * 通用请求封装
 */
export const request = function (url, method, data) {
  return new Promise((resolve, reject) => {
    const token = getUserToken();
    console.log('🔐 [Smart-Request调试] 准备发送请求到:', baseUrl + url);
    console.log('🔐 [Smart-Request调试] 使用的token:', token);
    console.log('🔐 [Smart-Request调试] Authorization header将为:', 'Bearer ' + token);
    
    uni.request({
      url: baseUrl + url, //拼接请求路径
      data: data,
      method: method,
      header: {
        Authorization: token ? 'Bearer ' + token : '',
        'Content-Type': 'application/json',
        'X-Club-Code': CLUB_CONFIG.CLUB_CODE,  // 自动添加俱乐部编码到请求头
      },
      success: (response) => {
        handleResponse(response, resolve, reject);
      },
      fail: (error) => {
        reject(error);
      },
    });
  });
};

/**
 * get请求
 */
export const getRequest = (url) => {
  return request(url, 'GET');
};

/**
 * post请求
 */
export const postRequest = (url, data) => {
  return request(url, 'POST', data);
};

// ================================= 加密 =================================

/**
 * 加密请求参数的post请求
 */
export const postEncryptRequest = (url, data) => {
  return request(url, 'POST', { encryptData: encryptData(data) });
};

// ================================= 文件 =================================

export const uploadRequest = function (filePath, folder) {
  return new Promise((resolve, reject) => {
    const token = getUserToken();
    console.log('🔐 [Upload请求调试] 使用的token:', token);
    console.log('🔐 [Upload请求调试] Authorization header将为:', 'Bearer ' + token);
    
    uni.uploadFile({
      url: baseUrl + '/support/file/upload',
      filePath,
      header: {
        Authorization: token ? 'Bearer ' + token : '',
        'X-Club-Code': CLUB_CONFIG.CLUB_CODE,  // 自动添加俱乐部编码到请求头
      },
      name: 'file',
      formData: {
        folder,
      },
      success: (response) => {
        response.data = JSON.parse(response.data.replace('\uFEFF', ''));
        handleResponse(response, resolve, reject);
      },
      fail: (error) => {
        reject(error);
      },
    });
  });
};
