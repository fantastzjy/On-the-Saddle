<!--
 * 预约记录列表组件 - 展示订单下的预约记录
 *
 * @Author: Claude Code
 * @Date: 2025-08-23
 * @Copyright: 马术俱乐部管理系统
-->
<template>
  <div class="booking-record-list">
    <a-table
      :dataSource="bookings"
      :columns="columns"
      :pagination="false"
      :scroll="{ x: 800 }"
      size="small"
      rowKey="bookingId"
      bordered
    >
      <template #bodyCell="{ column, record, text }">
        <!-- 消费会员 -->
        <template v-if="column.dataIndex === 'consumerMemberName'">
          <span>{{ text }}</span>
          <a-tag 
            v-if="record.consumerMemberId !== orderData.buyerMemberId" 
            color="orange" 
            size="small"
            class="ml-8"
          >
            代消费
          </a-tag>
        </template>

        <!-- 预约时间 -->
        <template v-if="column.dataIndex === 'timeRange'">
          <div class="time-range">
            <span class="start-time">{{ formatStartTime(record.startTime) }}</span>
            <span class="time-separator">~</span>
            <span class="end-time">{{ formatEndTime(record.startTime, record.endTime) }}</span>
          </div>
        </template>

        <!-- 教练信息 -->
        <template v-if="column.dataIndex === 'coachName'">
          <span>{{ text || '-' }}</span>
        </template>

        <!-- 马匹信息 -->
        <template v-if="column.dataIndex === 'horseName'">
          <span>{{ text || '-' }}</span>
        </template>

        <!-- 预约状态 -->
        <template v-if="column.dataIndex === 'bookingStatus'">
          <a-tag :color="getBookingStatusInfo(record.bookingStatus).color">
            {{ getBookingStatusInfo(record.bookingStatus).desc }}
          </a-tag>
        </template>

        <!-- 课程状态 -->
        <template v-if="column.dataIndex === 'lessonStatus'">
          <a-tag 
            v-if="record.lessonStatus" 
            :color="getLessonStatusInfo(record.lessonStatus).color"
          >
            {{ getLessonStatusInfo(record.lessonStatus).desc }}
          </a-tag>
          <span v-else>-</span>
        </template>

        <!-- 费用信息 -->
        <template v-if="column.dataIndex === 'fees'">
          <div class="fee-info">
            <div v-if="record.actualCoachFee">
              教练: ¥{{ formatMoney(record.actualCoachFee) }}
            </div>
            <div v-if="record.actualHorseFee">
              马匹: ¥{{ formatMoney(record.actualHorseFee) }}
            </div>
            <div v-if="record.packageConsumeCount" class="consume-count">
              消耗: {{ record.packageConsumeCount }}课时
            </div>
          </div>
        </template>

        <!-- 操作按钮 -->
        <template v-if="column.dataIndex === 'action'">
          <div class="action-buttons">
            <a-button 
              type="link" 
              size="small" 
              @click="handleAction('view', record)"
            >
              详情
            </a-button>
            
            <a-button 
              v-if="canConfirm(record)" 
              type="link" 
              size="small" 
              @click="handleAction('confirm', record)"
            >
              确认
            </a-button>
            
            <a-button 
              v-if="canReschedule(record)" 
              type="link" 
              size="small" 
              @click="handleAction('reschedule', record)"
            >
              改期
            </a-button>
            
            <a-button 
              v-if="canCancel(record)" 
              type="link" 
              size="small" 
              danger
              @click="handleAction('cancel', record)"
            >
              取消
            </a-button>
          </div>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import dayjs from 'dayjs';
import { BOOKING_STATUS_ENUM, LESSON_STATUS_ENUM, getBookingStatusInfo, getLessonStatusInfo } from '/@/constants/business/schedule/schedule-const.js';

// Props定义
const props = defineProps({
  bookings: {
    type: Array,
    default: () => []
  },
  orderData: {
    type: Object,
    required: true
  }
});

// Emits定义
const emit = defineEmits(['booking-action']);

// 表格列定义
const columns = computed(() => [
  {
    title: '消费会员',
    dataIndex: 'consumerMemberName',
    width: 120,
    fixed: 'left'
  },
  {
    title: '预约时间',
    dataIndex: 'timeRange',
    width: 120
  },
  {
    title: '教练',
    dataIndex: 'coachName',
    width: 100
  },
  {
    title: '马匹',
    dataIndex: 'horseName',
    width: 100
  },
  {
    title: '预约状态',
    dataIndex: 'bookingStatus',
    width: 100
  },
  {
    title: '课程状态',
    dataIndex: 'lessonStatus',
    width: 100
  },
  {
    title: '费用信息',
    dataIndex: 'fees',
    width: 120
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 180,
    fixed: 'right'
  }
]);

// 方法
const handleAction = (action, record) => {
  emit('booking-action', action, record);
};

// 状态判断方法 - 基于新状态设计
const canConfirm = (record) => {
  return record.bookingStatus === 1; // 已预约 → 可核销
};

const canReschedule = (record) => {
  return record.bookingStatus === 1; // 仅已预约可改期
};

const canCancel = (record) => {
  return [1, 2].includes(record.bookingStatus); // 已预约、已核销可取消
};

// 工具函数
const formatDateTime = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('MM-DD HH:mm') : '-';
};

// 优化的时间格式化函数
const formatStartTime = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('MM-DD HH:mm') : '-';
};

const formatEndTime = (startTime, endTime) => {
  if (!endTime) return '-';
  if (!startTime) return dayjs(endTime).format('MM-DD HH:mm');
  
  const startDay = dayjs(startTime);
  const endDay = dayjs(endTime);
  
  // 如果是同一天，只显示结束时间的小时分钟
  if (startDay.isSame(endDay, 'day')) {
    return endDay.format('HH:mm');
  }
  // 如果跨天，显示完整的结束时间
  return endDay.format('MM-DD HH:mm');
};

const formatMoney = (amount) => {
  return amount ? Number(amount).toFixed(2) : '0.00';
};
</script>

<style lang="less" scoped>
.booking-record-list {
  .time-range {
    display: flex;
    flex-direction: row;
    align-items: center;
    font-size: 12px;
    line-height: 1.3;
    white-space: nowrap;
    gap: 2px;

    .start-time {
      color: #333;
    }

    .time-separator {
      color: #999;
      font-size: 11px;
      margin: 0 1px;
    }

    .end-time {
      color: #333;
    }
  }

  .fee-info {
    font-size: 12px;
    line-height: 1.3;
    
    .consume-count {
      color: #1890ff;
      font-weight: 500;
    }
  }

  .action-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
  }

  :deep(.ant-table) {
    .ant-table-tbody > tr > td {
      padding: 8px;
    }
    
    .ant-table-thead > tr > th {
      padding: 8px;
      background-color: #fafafa;
      font-size: 12px;
    }
  }
}

.ml-8 {
  margin-left: 8px;
}
</style>