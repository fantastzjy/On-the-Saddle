/**
 * 预设选项配置常量
 * 定义各种预设选择器的选项数据
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-23
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

// 专长领域预设选项
export const SPECIALTY_PRESET_OPTIONS = [
  { value: '基础入门', label: '基础入门' },
  { value: '马场马术', label: '马场马术' },
  { value: '中级进阶', label: '中级进阶' },
  { value: '比赛进阶', label: '比赛进阶' },
  { value: '国级赛事', label: '国级赛事' }
];

// 预设选项映射表
export const PRESET_OPTIONS_MAP = {
  // 专长领域
  specialties: SPECIALTY_PRESET_OPTIONS,
};

// 预设类型枚举
export const PRESET_TYPE_ENUM = {
  SPECIALTIES: {
    value: 'specialties',
    desc: '专长领域',
    options: SPECIALTY_PRESET_OPTIONS
  }
};

// 导出所有预设选项（便于扩展）
export default {
  SPECIALTY_PRESET_OPTIONS,
  PRESET_OPTIONS_MAP,
  PRESET_TYPE_ENUM
};