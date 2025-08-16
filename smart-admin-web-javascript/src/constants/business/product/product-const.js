/*
 * 商品管理相关常量
 * 
 * @Author: 1024创新实验室
 * @Date: 2024-08-16
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

// ==================== 商品类型 ====================
export const PRODUCT_TYPE_ENUM = {
  COURSE: {
    value: 1,
    desc: '课程'
  },
  PACKAGE: {
    value: 2,
    desc: '课时包'
  },
  ACTIVITY: {
    value: 3,
    desc: '活动'
  }
};

// ==================== 商品状态 ====================
export const PRODUCT_STATUS_ENUM = {
  ONLINE: {
    value: 1,
    desc: '上架',
    color: 'green'
  },
  OFFLINE: {
    value: 2,
    desc: '下架',
    color: 'red'
  },
  SOLD_OUT: {
    value: 3,
    desc: '售罄',
    color: 'orange'
  }
};

// ==================== 课程类型 ====================
export const COURSE_CLASS_TYPE_ENUM = {
  SINGLE: {
    value: 1,
    desc: '单人课'
  },
  MULTI: {
    value: 2,
    desc: '多人课'
  }
};

// ==================== 课时包状态 ====================
export const PACKAGE_STATUS_ENUM = {
  NORMAL: {
    value: 1,
    desc: '正常',
    color: 'green'
  },
  EXPIRED: {
    value: 2,
    desc: '已过期',
    color: 'red'
  },
  USED_UP: {
    value: 3,
    desc: '已用完',
    color: 'orange'
  }
};

// ==================== 表单字段类型 ====================
export const FORM_FIELD_TYPE_ENUM = {
  INPUT: 'input',
  NUMBER: 'number',
  SELECT: 'select',
  TEXTAREA: 'textarea',
  DATETIME: 'datetime',
  UPLOAD: 'upload',
  SWITCH: 'switch',
  CHECKBOX: 'checkbox',
  RADIO: 'radio'
};

// ==================== 价格计算类型 ====================
export const PRICE_CALCULATION_TYPE_ENUM = {
  BASIC: {
    value: 1,
    desc: '基础定价'
  },
  TIERED: {
    value: 2,
    desc: '阶梯定价'
  },
  DYNAMIC: {
    value: 3,
    desc: '动态定价'
  }
};

// ==================== 会员等级 ====================
export const MEMBER_LEVEL_ENUM = {
  BRONZE: {
    value: 1,
    desc: '铜牌会员',
    discount: 0.05
  },
  SILVER: {
    value: 2,
    desc: '银牌会员',
    discount: 0.08
  },
  GOLD: {
    value: 3,
    desc: '金牌会员',
    discount: 0.12
  },
  DIAMOND: {
    value: 4,
    desc: '钻石会员',
    discount: 0.15
  }
};

// ==================== 默认配置 ====================
export const PRODUCT_DEFAULT_CONFIG = {
  // 商品图片配置
  IMAGE: {
    MAX_COUNT: 5,           // 最大图片数量
    MAX_SIZE: 5,            // 最大文件大小(MB)
    ACCEPTED_TYPES: ['jpg', 'jpeg', 'png', 'gif']
  },
  
  // 活动详情图片配置
  ACTIVITY_DETAIL_IMAGE: {
    MAX_COUNT: 9,           // 最大图片数量
    MAX_SIZE: 10,           // 最大文件大小(MB)
    ACCEPTED_TYPES: ['jpg', 'jpeg', 'png', 'gif']
  },
  
  // 课程配置
  COURSE: {
    MIN_DURATION: 30,       // 最小时长(分钟)
    MAX_DURATION: 300,      // 最大时长(分钟)
    MIN_PERIODS: 0.5,       // 最小鞍时
    MAX_PERIODS: 5.0,       // 最大鞍时
    MAX_STUDENTS: 10        // 最大学生数
  },
  
  // 课时包配置
  PACKAGE: {
    MIN_SESSIONS: 1,        // 最小课时数
    MAX_SESSIONS: 500,      // 最大课时数
    MIN_VALIDITY: 1,        // 最小有效期(天)
    MAX_VALIDITY: 3650      // 最大有效期(天)
  },
  
  // 活动配置
  ACTIVITY: {
    NAME_MAX_LENGTH: 5,     // 活动名称最大长度
    MAX_PARTICIPANTS: 1000  // 最大参与人数
  }
};

// ==================== 表格列配置 ====================
export const PRODUCT_TABLE_COLUMNS = [
  {
    title: '商品编码',
    dataIndex: 'productCode',
    width: 120,
    ellipsis: true
  },
  {
    title: '商品名称',
    dataIndex: 'productName',
    width: 200,
    ellipsis: true
  },
  {
    title: '商品类型',
    dataIndex: 'productType',
    width: 100
  },
  {
    title: '商品图片',
    dataIndex: 'images',
    width: 80,
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    align: 'center'
  },
  {
    title: '排序',
    dataIndex: 'sortOrder',
    width: 80,
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
export const PRODUCT_FORM_RULES = {
  productName: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { max: 100, message: '商品名称不能超过100个字符', trigger: 'blur' }
  ],
  productCode: [
    { max: 50, message: '商品编码不能超过50个字符', trigger: 'blur' }
  ],
  productType: [
    { required: true, message: '请选择商品类型', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' },
    { max: 1000, message: '商品描述不能超过1000个字符', trigger: 'blur' }
  ],
  sortOrder: [
    { type: 'number', min: 0, max: 9999, message: '排序值应在0-9999之间', trigger: 'blur' }
  ]
};

// ==================== 搜索表单配置 ====================
export const PRODUCT_SEARCH_FORM = {
  keywords: '',
  productType: null,
  status: null,
  createTimeRange: []
};

// ==================== 文件上传配置 ====================
export const UPLOAD_CONFIG = {
  action: '/api/file/upload',
  accept: '.jpg,.jpeg,.png,.gif',
  maxSize: 5, // MB
  maxCount: 5,
  headers: {
    'Authorization': localStorage.getItem('token')
  }
};