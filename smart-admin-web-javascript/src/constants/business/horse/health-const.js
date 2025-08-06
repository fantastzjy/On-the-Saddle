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
  MEDICATION: { value: 'medication', desc: '用药', color: 'red' }
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
  calculatePlanStatus,
};