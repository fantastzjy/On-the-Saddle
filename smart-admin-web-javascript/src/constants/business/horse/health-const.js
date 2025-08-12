/*
 * 马匹健康管理相关常量
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2024-01-15
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

// 健康计划类型枚举（基于数据库定义）
export const HEALTH_PLAN_TYPE_ENUM = {
  SHOEING: { value: 'shoeing', desc: '钉蹄', color: 'orange' },
  DEWORMING: { value: 'deworming', desc: '驱虫', color: 'green' },
  DENTAL: { value: 'dental', desc: '搓牙', color: 'purple' },
  VACCINE: { value: 'vaccine', desc: '疫苗', color: 'blue' },
  MEDICATION: { value: 'medication', desc: '养护', color: 'red' }
};

// 获取所有类型列表
export function getHealthPlanTypeList() {
  return Object.values(HEALTH_PLAN_TYPE_ENUM);
}

// 根据值获取描述
export function getPlanTypeDesc(value) {
  if (!value) return '';
  const type = Object.values(HEALTH_PLAN_TYPE_ENUM).find(t => t.value === value);
  return type ? type.desc : value;
}

// 根据值获取颜色
export function getPlanTypeColor(value) {
  if (!value) return 'default';
  const type = Object.values(HEALTH_PLAN_TYPE_ENUM).find(t => t.value === value);
  return type ? type.color : 'default';
}

// 获取状态描述和颜色
export function getPlanStatusInfo(value) {
  // 支持数字和字符串两种格式
  let status;
  
  if (typeof value === 'number') {
    // 数字格式
    status = Object.values(HEALTH_PLAN_STATUS_ENUM).find(s => s.value === value);
  } else if (typeof value === 'string') {
    // 字符串格式
    const statusMap = {
      'normal': HEALTH_PLAN_STATUS_ENUM.NORMAL,
      'reminder': HEALTH_PLAN_STATUS_ENUM.REMINDER, 
      'urgent': HEALTH_PLAN_STATUS_ENUM.URGENT,
      'overdue': HEALTH_PLAN_STATUS_ENUM.OVERDUE
    };
    status = statusMap[value] || Object.values(HEALTH_PLAN_STATUS_ENUM).find(s => s.desc === value);
  }
  
  return status || { desc: value, color: 'default' };
}

// 健康计划状态枚举
export const HEALTH_PLAN_STATUS_ENUM = {
  NORMAL: { value: 1, desc: '正常', color: 'green' },
  REMINDER: { value: 2, desc: '提醒', color: 'orange' },
  URGENT: { value: 3, desc: '紧急', color: 'red' },
  OVERDUE: { value: 4, desc: '超期', color: 'red' }
};

// 根据剩余天数计算状态
export function calculatePlanStatus(remainingDays, reminderDays = 7) {
  if (remainingDays < 0) {
    return HEALTH_PLAN_STATUS_ENUM.OVERDUE;
  } else if (remainingDays === 0) {
    return HEALTH_PLAN_STATUS_ENUM.URGENT;
  } else if (remainingDays <= reminderDays) {
    return HEALTH_PLAN_STATUS_ENUM.REMINDER;
  } else {
    return HEALTH_PLAN_STATUS_ENUM.NORMAL;
  }
}

export default {
  HEALTH_PLAN_TYPE_ENUM,
  HEALTH_PLAN_STATUS_ENUM,
  getHealthPlanTypeList,
  getPlanTypeDesc,
  getPlanTypeColor,
  getPlanStatusInfo,
  calculatePlanStatus,
};