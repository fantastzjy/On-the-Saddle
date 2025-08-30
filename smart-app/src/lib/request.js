import { BaseURL, CLUB_CONFIG } from './config';
import { generateRequestHeader, customResponseBody } from './requestUtil';
import { USER_TOKEN } from '@/constants/local-storage-key-const';

function getUserToken() {
  let token = uni.getStorageSync(USER_TOKEN);
  console.log('🔐 [Token调试] USER_TOKEN常量值:', USER_TOKEN);
  console.log('🔐 [Token调试] 从storage获取的token:', token);
  console.log('🔐 [Token调试] token类型:', typeof token);
  console.log('🔐 [Token调试] token长度:', token ? token.length : 0);
  
  if (token) {
    return token;
  }
  console.warn('🔐 [Token调试] ⚠️ 未获取到有效token，返回空字符串');
  return '';
}

export default (request) => {
  return new Promise((resolve, reject) => {
    const token = getUserToken();
    console.log('🔐 [Request调试] 准备发送请求到:', BaseURL + request.url);
    console.log('🔐 [Request调试] 使用的token:', token);
    console.log('🔐 [Request调试] Authorization header将为:', 'Bearer ' + token);
    
    uni.request({
      url: BaseURL + request.url,
      data: request.data || {},
      method: request.method || 'GET',
      timeout: 6000000,
      header: {
        Authorization: token ? 'Bearer ' + token : '',
        'Content-Type': 'application/json',
        'X-Club-Code': CLUB_CONFIG.CLUB_CODE,  // 自动添加俱乐部编码到请求头
      },
      success: (response) => {
        resolve(customResponseBody(response));
      },
      fail: (error) => {
        console.error(error);
        reject(error);
      },
    });
  });
};
