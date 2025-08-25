<!--
 * 简化预约列表组件 - 用于列表视图显示
 *
 * @Author: Claude Code
 * @Date: 2024-08-25
 * @Copyright: 马术俱乐部管理系统
-->
<template>
  <div class="simple-booking-list">
    <a-table
      :dataSource="bookingList"
      :columns="columns"
      :pagination="paginationConfig"
      :loading="loading"
      size="small"
      rowKey="bookingId"
      bordered
      :scroll="{ x: 'max-content' }"
    >
      <template #bodyCell="{ column, record, text }">
        <!-- 价格信息格式化 -->
        <template v-if="column.dataIndex === 'priceInfo'">
          <div class="price-info">
            <div class="price-row">教练费: ¥{{ formatPrice(record.coachFee) }}</div>
            <div class="price-row">马匹费: ¥{{ formatPrice(record.horseFee) }}</div>
            <div class="price-row total">总费用: ¥{{ formatPrice(record.totalFee) }}</div>
          </div>
        </template>

        <!-- 预约时间格式化 -->
        <template v-if="column.dataIndex === 'startTime'">
          {{ formatBookingDate(text) }}
        </template>

        <!-- 预约状态标签 -->
        <template v-if="column.dataIndex === 'bookingStatus'">
          <a-tag :color="getBookingStatusInfo(record.bookingStatus).color">
            {{ getBookingStatusInfo(record.bookingStatus).desc }}
          </a-tag>
        </template>

        <!-- 更新人格式化 -->
        <template v-if="column.dataIndex === 'updateBy'">
          {{ formatUpdateBy(text) }}
        </template>

        <!-- 更新时间格式化 -->
        <template v-if="column.dataIndex === 'updateTime'">
          {{ formatUpdateTime(text) }}
        </template>

        <!-- 操作列 -->
        <template v-if="column.dataIndex === 'action'">
          <a-space>
            <a-button 
              type="primary" 
              size="small"
              @click="handleCheckin(record)"
              :disabled="record.bookingStatus !== 2"
            >
              核销
            </a-button>
            <a-button 
              danger
              size="small"
              @click="handleCancel(record)"
              :disabled="record.bookingStatus === 5"
            >
              取消
            </a-button>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import dayjs from 'dayjs';
import { getBookingStatusInfo } from '/@/constants/business/schedule/schedule-const.js';

// Props定义
const props = defineProps({
  bookingList: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  total: {
    type: Number,
    default: 0
  },
  pageNum: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number,
    default: 10
  }
});

// Emits定义
const emit = defineEmits(['query', 'page-change']);

// 表格列定义 - 课程、会员、教练、马匹、价格、预约时间、预约状态、更新人、更新时间
const columns = [
  {
    title: '课程',
    dataIndex: 'courseName',
    ellipsis: true,
    width: 120,
    align: 'center'
  },
  {
    title: '会员',
    dataIndex: 'memberName',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '教练',
    dataIndex: 'coachName',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '马匹',
    dataIndex: 'horseName',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '价格',
    dataIndex: 'priceInfo',
    width: 150,
    align: 'center'
  },
  {
    title: '预约时间',
    dataIndex: 'startTime',
    width: 120,
    sorter: true,
    align: 'center'
  },
  {
    title: '预约状态',
    dataIndex: 'bookingStatus',
    width: 100,
    align: 'center'
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 120,
    align: 'center'
  },
  {
    title: '更新人',
    dataIndex: 'updateBy',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 120,
    align: 'center',
    fixed: 'right'
  }
];

// 分页配置
const paginationConfig = computed(() => ({
  current: props.pageNum,
  pageSize: props.pageSize,
  total: props.total,
  showSizeChanger: true,
  showQuickJumper: true,
  showLessItems: true,
  pageSizeOptions: ['10', '20', '50', '100'],
  showTotal: (total) => `共${total}条`,
  onChange: (page, size) => {
    emit('page-change', { pageNum: page, pageSize: size });
  },
  onShowSizeChange: (current, size) => {
    emit('page-change', { pageNum: 1, pageSize: size });
  }
}));

// 工具函数
const formatBookingDate = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('MM-DD HH:mm') : '-';
};

const formatUpdateTime = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('MM-DD HH:mm') : '-';
};

const formatUpdateBy = (updateBy) => {
  return updateBy === 'system' ? '' : (updateBy || '');
};

const formatPrice = (price) => {
  return price ? Number(price).toFixed(2) : '0.00';
};

// 操作按钮处理函数
const handleCheckin = (record) => {
  console.log('核销预约:', record);
  // TODO: 实现核销逻辑
};

const handleCancel = (record) => {
  console.log('取消预约:', record);
  // TODO: 实现取消逻辑
};
</script>

<style lang="less" scoped>
.simple-booking-list {
  // 自适应表格样式
  :deep(.ant-table) {
    .ant-table-tbody > tr > td {
      padding: 8px 12px;
    }
    
    .ant-table-thead > tr > th {
      padding: 12px;
      background-color: #fafafa;
      font-weight: 500;
    }
    
    // 表格自适应
    .ant-table-container {
      width: 100%;
    }
  }
  
  // 价格信息样式
  .price-info {
    .price-row {
      font-size: 12px;
      line-height: 1.4;
      margin-bottom: 2px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &.total {
        font-weight: 600;
        color: #1890ff;
      }
    }
  }
}
</style>