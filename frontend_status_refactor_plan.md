# 前端状态枚举重构方案

## 📋 项目概述

基于新的订单系统状态设计，对前端课程表页面综合视图中的状态枚举进行系统性重构，确保前后端状态定义完全一致。

## 🎯 总体策略

以新设计的状态定义为准，系统性重构所有相关的前端枚举、组件逻辑和业务处理。

## 📊 状态设计对比

### 新状态设计标准
- **订单状态**：1-待支付、2-已支付、3-已核销、4-已取消、5-已退款
- **预约状态**：1-已预约、2-已核销、3-已取消、4-已过期
- **课程状态**：1-待上课、2-已上课、3-已取消

### 现有实现问题
- 预约状态：5个状态 vs 新设计4个状态，含义完全不对应
- 课程状态：4个状态 vs 新设计3个状态，概念存在差异
- 订单状态：缺少统一枚举定义，逻辑分散

## 🔧 具体更新方案

### 1. 状态枚举常量重构

**文件：** `/src/constants/business/schedule/schedule-const.js`

#### 1.1 新增订单状态枚举
```javascript
export const ORDER_STATUS_ENUM = {
  PENDING_PAYMENT: { value: 1, desc: '待支付', color: 'orange' },
  PAID: { value: 2, desc: '已支付', color: 'green' },
  VERIFIED: { value: 3, desc: '已核销', color: 'blue' },
  CANCELLED: { value: 4, desc: '已取消', color: 'red' },
  REFUNDED: { value: 5, desc: '已退款', color: 'purple' }
}
```

#### 1.2 重构预约状态枚举
```javascript
// 旧版本（5个状态）→ 新版本（4个状态）
export const BOOKING_STATUS_ENUM = {
  BOOKED: { value: 1, desc: '已预约', color: 'green' },
  VERIFIED: { value: 2, desc: '已核销', color: 'blue' },
  CANCELLED: { value: 3, desc: '已取消', color: 'red' },
  EXPIRED: { value: 4, desc: '已过期', color: 'gray' }
}
```

#### 1.3 简化课程状态枚举
```javascript
// 旧版本（4个状态）→ 新版本（3个状态）
export const LESSON_STATUS_ENUM = {
  PENDING: { value: 1, desc: '待上课', color: 'orange' },
  COMPLETED: { value: 2, desc: '已上课', color: 'green' },
  CANCELLED: { value: 3, desc: '已取消', color: 'red' }
}
```

### 2. 组件更新计划

#### 2.1 ScheduleManage.vue（主页面）
- 更新导入的枚举常量
- 修改状态筛选逻辑
- 调整状态展示标签

#### 2.2 OrderCard.vue（订单卡片）
- 替换分散的状态处理函数为统一枚举
- 更新订单状态显示逻辑
- 修改状态颜色映射

#### 2.3 BookingRecordList.vue（预约记录列表）
- 重构预约状态显示
- 调整操作按钮的显示条件
- 更新状态变更权限逻辑

#### 2.4 PackageBookingCreateModal.vue（预约创建弹窗）
- 同步新的状态枚举
- 确保创建后状态一致性

### 3. 业务逻辑调整

#### 3.1 操作权限重新定义
```javascript
// 基于新状态的操作权限逻辑
const canConfirm = (record) => record.bookingStatus === 1; // 已预约 → 可核销
const canReschedule = (record) => record.bookingStatus === 1; // 仅已预约可改期
const canCancel = (record) => [1, 2].includes(record.bookingStatus); // 已预约、已核销可取消
```

#### 3.2 状态流转逻辑
- **订单流转**：待支付 → 已支付 → 已核销 → 已取消/已退款
- **预约流转**：已预约 → 已核销 → 已取消/已过期
- **课程流转**：待上课 → 已上课 → 已取消

### 4. 数据兼容性处理

#### 4.1 预约状态映射转换
```javascript
const mapOldBookingStatus = (oldStatus) => {
  const mapping = {
    1: 1, // 待确认 → 已预约
    2: 1, // 已确认 → 已预约  
    3: 1, // 进行中 → 已预约
    4: 2, // 已完成 → 已核销
    5: 3  // 已取消 → 已取消
  }
  return mapping[oldStatus] || oldStatus;
}
```

#### 4.2 课程状态映射转换
```javascript
const mapOldLessonStatus = (oldStatus) => {
  const mapping = {
    1: 1, // 待上课 → 待上课
    2: 1, // 进行中 → 待上课
    3: 2, // 已完成 → 已上课
    4: 3  // 已取消 → 已取消
  }
  return mapping[oldStatus] || oldStatus;
}
```

### 5. UI展示优化

#### 5.1 状态标签样式统一
- 使用 Ant Design 的 `Tag` 组件
- 统一颜色方案和样式
- 添加状态图标

#### 5.2 状态筛选器更新
- 更新筛选选项
- 调整筛选逻辑
- 保持筛选状态的持久化

## 🔄 实施步骤

1. **第一步**：更新状态枚举常量定义
2. **第二步**：重构核心组件（OrderCard、BookingRecordList）
3. **第三步**：更新主页面的状态处理逻辑
4. **第四步**：添加数据兼容性处理
5. **第五步**：测试各种状态流转场景
6. **第六步**：验证操作权限和业务逻辑

## ⚠️ 潜在风险点

1. **数据不一致**：现有数据库中的状态值可能与新枚举不匹配
2. **业务中断**：状态变更可能影响正在进行的业务流程
3. **用户体验**：状态名称变更可能造成用户困惑
4. **API兼容性**：前端状态变更需要与后端API保持同步

## 🎯 预期效果

- 状态定义与后端新设计完全一致
- 简化的状态流转逻辑更清晰
- 统一的枚举管理便于维护
- 改善的用户界面体验

## 📝 测试验证清单

### 功能测试
- [ ] 订单状态显示正确
- [ ] 预约状态显示正确
- [ ] 课程状态显示正确
- [ ] 状态筛选功能正常
- [ ] 操作按钮权限正确

### 兼容性测试
- [ ] 旧数据状态映射正确
- [ ] 状态变更流程正常
- [ ] API接口调用正确

### UI测试
- [ ] 状态标签样式统一
- [ ] 颜色方案一致性
- [ ] 响应式布局正常

---

**创建时间：** 2025-08-23  
**版本：** v1.0  
**负责人：** Claude Code