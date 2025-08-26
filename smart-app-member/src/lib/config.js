// 配置服务器相关信息

//测试环境
// export const BaseURL = 'http://10.33.16.80:8099/'
//开发环境
// export const BaseURL = 'http://127.0.0.1:8099/'
export const BaseURL = 'http://192.168.255.6:61812';

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
