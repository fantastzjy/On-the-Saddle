import { BaseURL } from './config';
import { generateRequestHeader, customResponseBody } from './requestUtil';

function getUserToken() {
  let token = uni.getStorageSync('token');
  if (token) {
    return token;
  }
  return '';
}

export default (request) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BaseURL + request.url,
      data: request.data || {},
      method: request.method || 'GET',
      timeout: 6000000,
      header: {
        Authorization: 'Bearer ' + getUserToken(),
        'Content-Type': 'application/json',
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
