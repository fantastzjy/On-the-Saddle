/**
 * 会员管理相关常量
 */

// 注册状态
export const REGISTRATION_STATUS = {
  UNACTIVATED: 0,
  ACTIVATED: 1
}

export const REGISTRATION_STATUS_TEXT = {
  [REGISTRATION_STATUS.UNACTIVATED]: '未激活',
  [REGISTRATION_STATUS.ACTIVATED]: '已注册'
}

export const REGISTRATION_STATUS_COLOR = {
  [REGISTRATION_STATUS.UNACTIVATED]: 'orange',
  [REGISTRATION_STATUS.ACTIVATED]: 'green'
}

// 会籍状态
export const MEMBERSHIP_STATUS = {
  NORMAL: 0,
  MEMBER: 1
}

export const MEMBERSHIP_STATUS_TEXT = {
  [MEMBERSHIP_STATUS.NORMAL]: '普通会员',
  [MEMBERSHIP_STATUS.MEMBER]: '会籍会员'
}

export const MEMBERSHIP_STATUS_COLOR = {
  [MEMBERSHIP_STATUS.NORMAL]: 'default',
  [MEMBERSHIP_STATUS.MEMBER]: 'gold'
}

// 性别
export const GENDER = {
  UNKNOWN: 0,
  MALE: 1,
  FEMALE: 2
}

export const GENDER_TEXT = {
  [GENDER.UNKNOWN]: '未知',
  [GENDER.MALE]: '男',
  [GENDER.FEMALE]: '女'
}

export const GENDER_OPTIONS = [
  { value: GENDER.MALE, label: '男' },
  { value: GENDER.FEMALE, label: '女' },
  { value: GENDER.UNKNOWN, label: '未知' }
]

// 创建方式
export const CREATED_BY = {
  SELF: 0,
  GUARDIAN: 1
}

export const CREATED_BY_TEXT = {
  [CREATED_BY.SELF]: '自主注册',
  [CREATED_BY.GUARDIAN]: '监护人创建'
}

export const CREATED_BY_COLOR = {
  [CREATED_BY.SELF]: 'blue',
  [CREATED_BY.GUARDIAN]: 'purple'
}

// 状态
export const DISABLED_FLAG = {
  ENABLED: 0,
  DISABLED: 1
}

export const DISABLED_FLAG_TEXT = {
  [DISABLED_FLAG.ENABLED]: '启用',
  [DISABLED_FLAG.DISABLED]: '禁用'
}

export const DISABLED_FLAG_COLOR = {
  [DISABLED_FLAG.ENABLED]: 'success',
  [DISABLED_FLAG.DISABLED]: 'error'
}

// 搜索类型选项
export const SEARCH_TYPE_OPTIONS = [
  { value: 'memberNo', label: '会员编号' },
  { value: 'actualName', label: '姓名' },
  { value: 'phone', label: '手机号' },
  { value: 'riderCertNo', label: '骑手证号' }
]

// 课程级别字典编码
export const COURSE_LEVEL_DICT_CODE = 'COURSE_LEVEL'

// 课程级别转换映射（用于兼容旧数据）
export const COURSE_LEVEL_CONVERT_MAP = {
  '初级': 'BEGINNER',
  '中级': 'INTERMEDIATE', 
  '高级': 'ADVANCED',
  '专业级': 'EXPERT',
  '专业': 'EXPERT'
}

// 课程级别反向转换映射（枚举编码 -> 中文显示）
export const COURSE_LEVEL_REVERSE_MAP = {
  'BEGINNER': '初级',
  'INTERMEDIATE': '中级',
  'ADVANCED': '高级',
  'EXPERT': '专业级'
}

// 表格列配置
export const TABLE_COLUMNS = [
  {
    title: 'Union ID',
    dataIndex: 'unionId',
    width: 150
  },
  {
    title: 'Open ID',
    dataIndex: 'openId',
    width: 150
  },
  {
    title: '会员姓名',
    dataIndex: 'actualName',
    width: 120
  },
  {
    title: '会员编号',
    dataIndex: 'memberNo',
    width: 120
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    width: 140,
    slots: { customRender: 'phone' }
  },
  {
    title: '性别',
    dataIndex: 'gender',
    width: 80,
    slots: { customRender: 'gender' }
  },
  {
    title: '年龄',
    dataIndex: 'age',
    width: 80,
    slots: { customRender: 'age' }
  },
  {
    title: '注册状态',
    dataIndex: 'registrationStatus',
    width: 100,
    slots: { customRender: 'registrationStatus' }
  },
  {
    title: '会籍状态',
    dataIndex: 'isMembership',
    width: 100,
    slots: { customRender: 'isMembership' }
  },
  {
    title: '会籍到期时间',
    dataIndex: 'membershipExpireDate',
    width: 120,
    slots: { customRender: 'membershipExpireDate' }
  },
  {
    title: '家庭组',
    dataIndex: 'familyName',
    width: 120
  },
  {
    title: '身份证号',
    dataIndex: 'idCardNo',
    width: 180,
    slots: { customRender: 'idCardNo' }
  },
  {
    title: '骑手证号',
    dataIndex: 'riderCertNo',
    width: 120
  },
  {
    title: '默认教练',
    dataIndex: 'defaultCoachName',
    width: 120,
    slots: { customRender: 'defaultCoach' }
  },
  {
    title: '课程级别',
    dataIndex: 'defaultCourseLevel',
    width: 100,
    slots: { customRender: 'defaultCourseLevel' }
  },
  {
    title: '创建方式',
    dataIndex: 'createdByGuardian',
    width: 100,
    slots: { customRender: 'createdByGuardian' }
  },
  {
    title: '状态',
    dataIndex: 'disabledFlag',
    width: 80,
    slots: { customRender: 'disabledFlag' }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150,
    slots: { customRender: 'createTime' }
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 200,
    fixed: 'right',
    slots: { customRender: 'action' }
  }
]