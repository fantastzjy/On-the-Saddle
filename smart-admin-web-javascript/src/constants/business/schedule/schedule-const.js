/*
 * 课表管理相关常量
 * 
 * @Author: 1024创新实验室
 * @Date: 2024-08-16
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

// ==================== 订单状态 ====================
export const ORDER_STATUS_ENUM = {
  PENDING_PAYMENT: {
    value: 1,
    desc: '待支付',
    color: 'orange'
  },
  PAID: {
    value: 2,
    desc: '已支付',
    color: 'green'
  },
  VERIFIED: {
    value: 3,
    desc: '已核销',
    color: 'blue'
  },
  CANCELLED: {
    value: 4,
    desc: '已取消',
    color: 'red'
  },
  REFUNDED: {
    value: 5,
    desc: '已退款',
    color: 'purple'
  }
};

// ==================== 预约状态 ====================
export const BOOKING_STATUS_ENUM = {
  BOOKED: {
    value: 1,
    desc: '已预约',
    color: 'green'
  },
  VERIFIED: {
    value: 2,
    desc: '已核销',
    color: 'blue'
  },
  CANCELLED: {
    value: 3,
    desc: '已取消',
    color: 'red'
  },
  EXPIRED: {
    value: 4,
    desc: '已过期',
    color: 'gray'
  }
};

// ==================== 课单状态 ====================
export const LESSON_STATUS_ENUM = {
  PENDING: {
    value: 1,
    desc: '待上课',
    color: 'orange'
  },
  COMPLETED: {
    value: 2,
    desc: '已上课',
    color: 'green'
  },
  CANCELLED: {
    value: 3,
    desc: '已取消',
    color: 'red'
  }
};

// ==================== 数据兼容性映射函数 ====================
/**
 * 预约状态兼容性映射（旧状态 → 新状态）
 * @param {number} oldStatus 旧状态值
 * @returns {number} 新状态值
 */
export const mapOldBookingStatus = (oldStatus) => {
  const mapping = {
    1: 1, // 待确认 → 已预约
    2: 1, // 已确认 → 已预约  
    3: 1, // 进行中 → 已预约
    4: 2, // 已完成 → 已核销
    5: 3  // 已取消 → 已取消
  };
  return mapping[oldStatus] || oldStatus;
};

/**
 * 课程状态兼容性映射（旧状态 → 新状态）
 * @param {number} oldStatus 旧状态值
 * @returns {number} 新状态值
 */
export const mapOldLessonStatus = (oldStatus) => {
  const mapping = {
    1: 1, // 待上课 → 待上课
    2: 1, // 进行中 → 待上课
    3: 2, // 已完成 → 已上课
    4: 3  // 已取消 → 已取消
  };
  return mapping[oldStatus] || oldStatus;
};

/**
 * 根据状态值获取订单状态信息
 * @param {number} status 状态值
 * @returns {object} 状态信息对象
 */
export const getOrderStatusInfo = (status) => {
  return Object.values(ORDER_STATUS_ENUM).find(item => item.value === status) || 
    { value: status, desc: '未知状态', color: 'default' };
};

/**
 * 根据状态值获取预约状态信息
 * @param {number} status 状态值
 * @returns {object} 状态信息对象
 */
export const getBookingStatusInfo = (status) => {
  const mappedStatus = mapOldBookingStatus(status);
  return Object.values(BOOKING_STATUS_ENUM).find(item => item.value === mappedStatus) || 
    { value: mappedStatus, desc: '未知状态', color: 'default' };
};

/**
 * 根据状态值获取课程状态信息
 * @param {number} status 状态值
 * @returns {object} 状态信息对象
 */
export const getLessonStatusInfo = (status) => {
  const mappedStatus = mapOldLessonStatus(status);
  return Object.values(LESSON_STATUS_ENUM).find(item => item.value === mappedStatus) || 
    { value: mappedStatus, desc: '未知状态', color: 'default' };
};

// ==================== 教练可用状态 ====================
export const COACH_AVAILABILITY_STATUS_ENUM = {
  AVAILABLE: {
    value: 1,
    desc: '可用',
    color: 'green'
  },
  LEAVE: {
    value: 2,
    desc: '请假',
    color: 'orange'
  },
  OCCUPIED: {
    value: 3,
    desc: '占用',
    color: 'red'
  }
};

// ==================== 马匹可用状态 ====================
export const HORSE_AVAILABILITY_STATUS_ENUM = {
  AVAILABLE: {
    value: 1,
    desc: '可用',
    color: 'green'
  },
  REST: {
    value: 2,
    desc: '休息',
    color: 'orange'
  },
  TREATMENT: {
    value: 3,
    desc: '治疗',
    color: 'red'
  },
  OCCUPIED: {
    value: 4,
    desc: '占用',
    color: 'blue'
  }
};

// ==================== 课表视图类型 ====================
export const SCHEDULE_VIEW_TYPE_ENUM = {
  DAY: {
    value: 'day',
    desc: '日视图',
    icon: 'CalendarOutlined'
  },
  WEEK: {
    value: 'week',
    desc: '周视图',
    icon: 'CalendarOutlined'
  },
  MONTH: {
    value: 'month',
    desc: '月视图',
    icon: 'CalendarOutlined'
  }
};

// ==================== 时间段配置 ====================
export const TIME_SLOT_CONFIG = {
  // 营业时间
  BUSINESS_HOURS: {
    START: '09:00',
    END: '21:00'
  },
  
  // 时间段间隔（分钟）
  INTERVAL: 30,
  
  // 预定义时间段
  PREDEFINED_SLOTS: [
    { label: '上午黄金时段', start: '09:00', end: '12:00', score: 90 },
    { label: '下午优质时段', start: '14:00', end: '18:00', score: 80 },
    { label: '晚间时段', start: '18:00', end: '21:00', score: 70 }
  ]
};

// ==================== 冲突类型 ====================
export const CONFLICT_TYPE_ENUM = {
  COACH_CONFLICT: {
    value: 'coach',
    desc: '教练时间冲突',
    color: 'red'
  },
  HORSE_CONFLICT: {
    value: 'horse',
    desc: '马匹时间冲突',
    color: 'orange'
  },
  RESOURCE_CONFLICT: {
    value: 'resource',
    desc: '资源不足',
    color: 'blue'
  },
  TIME_CONFLICT: {
    value: 'time',
    desc: '时间段冲突',
    color: 'purple'
  }
};

// ==================== 排课优先级 ====================
export const SCHEDULE_PRIORITY_ENUM = {
  HIGH: {
    value: 1,
    desc: '高优先级',
    color: 'red'
  },
  MEDIUM: {
    value: 2,
    desc: '中优先级',
    color: 'orange'
  },
  LOW: {
    value: 3,
    desc: '低优先级',
    color: 'green'
  }
};

// ==================== 课表操作类型 ====================
export const SCHEDULE_OPERATION_TYPE_ENUM = {
  CREATE: {
    value: 'create',
    desc: '创建',
    icon: 'PlusOutlined'
  },
  UPDATE: {
    value: 'update',
    desc: '修改',
    icon: 'EditOutlined'
  },
  DELETE: {
    value: 'delete',
    desc: '删除',
    icon: 'DeleteOutlined'
  },
  DRAG: {
    value: 'drag',
    desc: '拖拽',
    icon: 'DragOutlined'
  },
  BATCH: {
    value: 'batch',
    desc: '批量',
    icon: 'AppstoreOutlined'
  }
};

// ==================== 默认配置 ====================
export const SCHEDULE_DEFAULT_CONFIG = {
  // 课表颜色配置
  COLORS: {
    COURSE: '#1890ff',      // 课程
    PACKAGE: '#52c41a',     // 课时包
    ACTIVITY: '#722ed1',    // 活动
    CONFLICT: '#ff4d4f',    // 冲突
    PENDING: '#faad14'      // 待确认
  },
  
  // 拖拽配置
  DRAG: {
    SNAP_DURATION: 15,      // 拖拽吸附间隔(分钟)
    MIN_DURATION: 30,       // 最小课程时长(分钟)
    MAX_DURATION: 180       // 最大课程时长(分钟)
  },
  
  // 日历配置
  CALENDAR: {
    WEEK_START: 1,          // 周开始日期(1=周一)
    SHOW_WEEK_NUMBERS: true, // 显示周数
    HOUR_FORMAT: 24         // 24小时制
  },
  
  // 通知配置
  NOTIFICATION: {
    ADVANCE_HOURS: 2,       // 提前通知时间(小时)
    REMINDER_HOURS: 0.5     // 提醒时间(小时)
  }
};

// ==================== 表格列配置 ====================
export const SCHEDULE_TABLE_COLUMNS = [
  {
    title: '课单号',
    dataIndex: 'scheduleNo',
    width: 140,
    ellipsis: true,
    align: 'center'
  },
  {
    title: '会员姓名',
    dataIndex: 'memberName',
    width: 120,
    align: 'center'
  },
  {
    title: '教练姓名',
    dataIndex: 'coachName',
    width: 120,
    align: 'center'
  },
  {
    title: '马匹名称',
    dataIndex: 'horseName',
    width: 120,
    align: 'center'
  },
  {
    title: '上课时间',
    dataIndex: 'startTime',
    width: 180,
    sorter: true,
    align: 'center'
  },
  {
    title: '课程时长',
    dataIndex: 'duration',
    width: 100,
    align: 'center'
  },
  {
    title: '课单状态',
    dataIndex: 'lessonStatus',
    width: 100,
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 200,
    fixed: 'right',
    align: 'center'
  }
];

export const BOOKING_TABLE_COLUMNS = [
  {
    title: '预约编号',
    dataIndex: 'bookingId',
    width: 120
  },
  {
    title: '会员姓名',
    dataIndex: 'memberName',
    width: 120
  },
  {
    title: '商品名称',
    dataIndex: 'productName',
    width: 200,
    ellipsis: true
  },
  {
    title: '预约时间',
    dataIndex: 'startTime',
    width: 180,
    sorter: true
  },
  {
    title: '教练姓名',
    dataIndex: 'coachName',
    width: 120
  },
  {
    title: '预约状态',
    dataIndex: 'bookingStatus',
    width: 100,
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
    sorter: true
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 200,
    fixed: 'right'
  }
];

// ==================== 表单验证规则 ====================
export const SCHEDULE_FORM_RULES = {
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  coachId: [
    { required: true, message: '请选择教练', trigger: 'change' }
  ],
  memberId: [
    { required: true, message: '请选择会员', trigger: 'change' }
  ]
};

// ==================== 搜索表单配置 ====================
export const SCHEDULE_SEARCH_FORM = {
  keywords: '',
  lessonStatus: null,
  coachId: null,
  dateRange: [],
  timeRange: []
};

export const BOOKING_SEARCH_FORM = {
  keywords: '',
  bookingStatus: null,
  memberId: null,
  coachId: null,
  dateRange: []
};