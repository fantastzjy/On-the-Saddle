import { USER_TOKEN } from '@/constants/local-storage-key-const';

/**
 * 生成请求头
 * @param {Object} data - 请求参数
 * @returns {Object} 请求头
 */
export function generateRequestHeader(requestHeader) {
  // 获取token
  const token = uni.getStorageSync(USER_TOKEN);
  if (token) {
    // console.log("当前请求有token", token);
    requestHeader.Authorization = 'Bearer ' + token;
  }
  return requestHeader;
}

export function customResponseBody(response) {
  if (!isJsonData(response.data)) {
    response.data = JSON.parse(response.data);
  }

  //判断当前是否是401状态 如果是则需要重新登录
  if (response.data.code === 401) {
    console.log('重新登录');
    uni.clearStorageSync('openId');
    uni.clearStorageSync('userInfo');
    uni.clearStorageSync(USER_TOKEN);

    uni.showModal({
      title: '消息提醒',
      content: '请求超时，长时间未操作！登录出错请刷新重试',
      showCancel: false,
      complete() {
        uni.reLaunch({
          url: '/pages/home/index',
        });
      },
    });
    return null;
  }
  return response.data;
}

/**
 * 判断是否为JSON数据
 * @param {Object} jsonData - 待判断的数据
 * @returns {Boolean} 是否为JSON数据
 */
function isJsonData(jsonData) {
  if (jsonData && Object.prototype.toString.call(jsonData) === '[object Object]') {
    return true;
  } else {
    return false;
  }
}
