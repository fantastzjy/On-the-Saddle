<!--
 * 订单卡片组件 - 用于展示订单和预约的分层数据
 *
 * @Author: Claude Code
 * @Date: 2025-08-23
 * @Copyright: 马术俱乐部管理系统
-->
<template>
  <a-card 
    class="order-card" 
    size="small" 
    :hoverable="true"
    :style="{ marginBottom: '16px' }"
  >
    <!-- 订单头部信息 -->
    <template #title>
      <div class="order-header">
        <div class="order-basic-info">
          <a-tag :color="getOrderStatusInfo(orderData.orderStatus).color" class="mr-8">
            {{ getOrderStatusInfo(orderData.orderStatus).desc }}
          </a-tag>
          <span class="order-no">{{ orderData.orderNo }}</span>
          <a-tag :color="getProductTypeColor(orderData.productType)" class="ml-8">
            {{ orderData.productTypeName }}
          </a-tag>
        </div>
        <div class="order-actions">
          <a-button 
            v-if="orderData.productType === 2 && orderData.remainingCount > 0" 
            type="primary" 
            size="small"
            @click="handleAddBooking"
            :loading="addingBooking"
          >
            添加预约
          </a-button>
          <a-button type="link" size="small" @click="handleViewDetail">
            详情
          </a-button>
        </div>
      </div>
    </template>

    <!-- 订单详细信息 -->
    <div class="order-content">
      <a-row :gutter="24">
        <!-- 基本信息 -->
        <a-col :span="8">
          <div class="info-section">
            <h4>商品信息</h4>
            <p><strong>商品名称：</strong>{{ orderData.productName }}</p>
            <p><strong>购买会员：</strong>{{ orderData.buyerMemberName }}</p>
            <p><strong>数量/课时：</strong>{{ orderData.quantity }}</p>
            <p><strong>单价：</strong>¥{{ formatMoney(orderData.unitPrice) }}</p>
            <p><strong>总金额：</strong>¥{{ formatMoney(orderData.totalAmount) }}</p>
          </div>
        </a-col>

        <!-- 课时包余额信息（仅课时包显示） -->
        <a-col :span="8" v-if="orderData.productType === 2">
          <div class="info-section package-balance">
            <h4>课时包余额</h4>
            <div class="balance-info">
              <a-progress 
                :percent="getUsagePercent()" 
                :stroke-color="getProgressColor()"
                size="small"
              />
              <p class="balance-text">
                <span>总课时：{{ orderData.totalCount }}</span>
                <span class="ml-16">已用：{{ orderData.usedCount }}</span>
                <span class="ml-16">余额：{{ orderData.remainingCount }}</span>
              </p>
            </div>
            <p v-if="orderData.expireDate">
              <strong>过期时间：</strong>{{ formatDateTime(orderData.expireDate) }}
            </p>
          </div>
        </a-col>

        <!-- 时间信息 -->
        <a-col :span="8">
          <div class="info-section">
            <h4>时间信息</h4>
            <p><strong>创建时间：</strong>{{ formatDateTime(orderData.createTime) }}</p>
            <p v-if="orderData.paymentTime">
              <strong>支付时间：</strong>{{ formatDateTime(orderData.paymentTime) }}
            </p>
            <p v-if="orderData.defaultCoachName">
              <strong>默认教练：</strong>{{ orderData.defaultCoachName }}
            </p>
          </div>
        </a-col>
      </a-row>

      <!-- 预约记录列表 -->
      <div class="booking-section" v-if="orderData.bookings && orderData.bookings.length > 0">
        <div class="section-header">
          <h4>预约记录 ({{ orderData.bookings.length }})</h4>
          <a-button 
            type="link" 
            size="small"
            @click="toggleBookingList"
          >
            {{ showBookings ? '收起' : '展开' }}
            <UpOutlined v-if="showBookings" />
            <DownOutlined v-else />
          </a-button>
        </div>

        <div v-show="showBookings" class="booking-list">
          <BookingRecordList 
            :bookings="orderData.bookings"
            :order-data="orderData"
            @booking-action="handleBookingAction"
          />
        </div>
      </div>

      <!-- 无预约记录提示 -->
      <div v-else-if="orderData.productType === 2" class="no-booking">
        <a-empty 
          description="暂无预约记录，点击上方「添加预约」开始使用课时包"
          :image="Empty.PRESENTED_IMAGE_SIMPLE"
        />
      </div>
    </div>
  </a-card>
</template>

<script setup>
import { ref, computed } from 'vue';
import { Empty } from 'ant-design-vue';
import { UpOutlined, DownOutlined } from '@ant-design/icons-vue';
import BookingRecordList from './BookingRecordList.vue';
import dayjs from 'dayjs';
import { ORDER_STATUS_ENUM, getOrderStatusInfo } from '/@/constants/business/schedule/schedule-const.js';

// Props定义
const props = defineProps({
  orderData: {
    type: Object,
    required: true
  }
});

// Emits定义
const emit = defineEmits(['add-booking', 'view-detail', 'booking-action']);

// 响应式数据
const showBookings = ref(true);
const addingBooking = ref(false);

// 计算属性
const getUsagePercent = () => {
  if (!props.orderData.totalCount) return 0;
  return Math.round((props.orderData.usedCount / props.orderData.totalCount) * 100);
};

const getProgressColor = () => {
  const percent = getUsagePercent();
  if (percent < 30) return '#52c41a';
  if (percent < 70) return '#faad14';
  return '#f5222d';
};

// 方法
const toggleBookingList = () => {
  showBookings.value = !showBookings.value;
};

const handleAddBooking = () => {
  emit('add-booking', props.orderData);
};

const handleViewDetail = () => {
  emit('view-detail', props.orderData);
};

const handleBookingAction = (action, booking) => {
  emit('booking-action', action, booking, props.orderData);
};

// 工具函数
const formatMoney = (amount) => {
  return amount ? Number(amount).toFixed(2) : '0.00';
};

const formatDateTime = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('YYYY-MM-DD HH:mm') : '-';
};

const getProductTypeColor = (type) => {
  switch (type) {
    case 1: return 'blue';        // 单次课程
    case 2: return 'green';       // 课时包
    case 3: return 'purple';      // 活动
    default: return 'default';
  }
};
</script>

<style lang="less" scoped>
.order-card {
  border-radius: 8px;
  transition: all 0.3s ease;

  &:hover {
    border-color: #1890ff;
    box-shadow: 0 4px 12px rgba(24, 144, 255, 0.15);
  }

  .order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .order-basic-info {
      display: flex;
      align-items: center;

      .order-no {
        font-size: 16px;
        font-weight: 500;
        color: #1890ff;
      }
    }

    .order-actions {
      display: flex;
      gap: 8px;
    }
  }

  .order-content {
    .info-section {
      margin-bottom: 16px;

      h4 {
        margin-bottom: 8px;
        color: #333;
        font-size: 14px;
        font-weight: 500;
        border-bottom: 1px solid #f0f0f0;
        padding-bottom: 4px;
      }

      p {
        margin-bottom: 4px;
        font-size: 13px;
        line-height: 1.4;
        color: #666;

        strong {
          color: #333;
          margin-right: 8px;
        }
      }

      &.package-balance {
        .balance-info {
          .balance-text {
            display: flex;
            justify-content: space-between;
            margin-top: 8px;
            font-size: 12px;
          }
        }
      }
    }

    .booking-section {
      margin-top: 20px;
      border-top: 1px solid #f0f0f0;
      padding-top: 16px;

      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        h4 {
          margin: 0;
          font-size: 14px;
          font-weight: 500;
          color: #333;
        }
      }

      .booking-list {
        animation: fadeIn 0.3s ease-in-out;
      }
    }

    .no-booking {
      margin-top: 20px;
      text-align: center;
      padding: 20px;
      background-color: #fafafa;
      border-radius: 6px;
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.ml-8 {
  margin-left: 8px;
}

.ml-16 {
  margin-left: 16px;
}

.mr-8 {
  margin-right: 8px;
}
</style>