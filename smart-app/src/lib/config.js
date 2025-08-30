// 配置服务器相关信息

//测试环境
// export const BaseURL = 'http://10.33.16.80:8099/'
//开发环境
// export const BaseURL = 'http://127.0.0.1:8099/'
export const BaseURL = 'http://127.0.0.1:61812';

//生产环境
// export const BaseURL = 'http://112.28.216.81:8099/'

// export const agentAddrURL = 'https://hg1215.vicp.fun/';

//上传附件地址
export const uploadFileURL = BaseURL + 'file/uploadFile';

export const requestHeader = {
  Authorization: 'Bearer hotline-business-app-request-token',
  appId: 'aee7ghu29965f493rty766a7526955ggt',
  appSecret: 'accece68ac5945699bf6525hjk2625g6',
};

// 俱乐部配置
export const CLUB_CONFIG = {
  // 当前俱乐部编码 - 根据部署环境配置
  CLUB_CODE: 'CLUB001', // 生产环境需要修改为实际的俱乐部编码
  
  // 也可以根据环境动态配置
  // CLUB_CODE: process.env.VUE_APP_CLUB_CODE || 'CLUB001'
};
