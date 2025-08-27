/*
 * @Description: 表格id
 * @Author: zhuoda
 * @Date: 2022-08-21
 * @LastEditTime: 2022-08-21
 * @LastEditors: zhuoda
 */

//system系统功能表格初始化id
let systemInitTableId = 10000;

//support支撑功能表格初始化id
let supportInitTableId = 20000;

//业务表格初始化id
let businessOAInitTableId = 30000;

let businessERPInitTableId = 40000;

let businessClubInitTableId = 50000;

export const TABLE_ID_CONST = {
  /**
   * 业务
   */
  BUSINESS: {
    OA: {
      NOTICE: businessOAInitTableId + 1, //通知公告
      ENTERPRISE: businessOAInitTableId + 2, //企业信息
      ENTERPRISE_EMPLOYEE: businessOAInitTableId + 3, //企业员工
      ENTERPRISE_BANK: businessOAInitTableId + 4, //企业银行
      ENTERPRISE_INVOICE: businessOAInitTableId + 5, //企业发票
    },
    ERP: {
      GOODS: businessERPInitTableId + 1, //商品管理
    },
    CLUB: {
      CLUB: businessClubInitTableId + 1, //俱乐部管理
      COACH: businessClubInitTableId + 2, //教练管理
      HORSE: businessClubInitTableId + 3, //马匹管理
      HORSE_HEALTH_PLAN: businessClubInitTableId + 4, //马匹健康计划
      HORSE_HEALTH_RECORD: businessClubInitTableId + 5, //马匹健康记录
      MEMBER: businessClubInitTableId + 6, //会员管理
    },
    PRODUCT: {
      PRODUCT: businessClubInitTableId + 7, //商品管理
    },
    SCHEDULE: {
      SCHEDULE: businessClubInitTableId + 8, //课表管理
      BOOKING: businessClubInitTableId + 9, //预约管理
    },
    STABLE_RENTAL: {
      STABLE_RENTAL: businessClubInitTableId + 10, //马房租赁
    },
  },

  /**
   * 系统
   */
  SYSTEM: {
    EMPLOYEE: systemInitTableId + 1, //员工
    MENU: systemInitTableId + 2, //菜单
    POSITION: systemInitTableId + 3, //职位
  },
  /**
   * 支撑
   */
  SUPPORT: {
    CONFIG: supportInitTableId + 1, //参数配置
    DICT: supportInitTableId + 2, //字典
    SERIAL_NUMBER: supportInitTableId + 3, //单号
    OPERATE_LOG: supportInitTableId + 4, //请求监控
    HEART_BEAT: supportInitTableId + 5, //心跳
    LOGIN_LOG: supportInitTableId + 6, //登录日志
    RELOAD: supportInitTableId + 7, //reload
    HELP_DOC: supportInitTableId + 8, //帮助文档
    JOB: supportInitTableId + 9, //Job
    JOB_LOG: supportInitTableId + 10, //JobLog
    MAIL: supportInitTableId + 11,
  },
};
