<!--
  * 订单详情页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="order-detail-container">
    <a-card :loading="loading" style="margin-bottom: 16px;">
      <template #title>
        <div style="display: flex; align-items: center;">
          <div>
            <ArrowLeftOutlined @click="goBack" style="margin-right: 8px; cursor: pointer;" />
            订单详情
          </div>
        </div>
      </template>

      <!-- 订单基础信息 -->
      <a-row :gutter="24">
        <a-col :span="12">
          <a-descriptions title="订单信息" :column="1" bordered>
            <a-descriptions-item label="订单号">
              {{ orderDetail.orderNo }}
            </a-descriptions-item>
            <a-descriptions-item label="订单状态">
              <a-tag :color="getOrderStatusColor(orderDetail.orderStatus)">
                {{ orderDetail.orderStatusName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="订单类型">
              <a-tag :color="getOrderTypeColor(orderDetail.orderType)">
                {{ orderDetail.orderTypeName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="订单金额">
              <span style="font-size: 18px; font-weight: bold; color: #ff4d4f;">
                ¥{{ orderDetail.totalAmount }}
              </span>
            </a-descriptions-item>
            <a-descriptions-item label="已支付金额">
              ¥{{ orderDetail.paidAmount || 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="支付方式">
              <a-tag v-if="orderDetail.paymentMethod" 
                     :color="getPaymentMethodConfig(orderDetail.paymentMethod).color">
                <template #icon>
                  <component :is="getPaymentMethodConfig(orderDetail.paymentMethod).icon + '-outlined'" />
                </template>
                {{ orderDetail.paymentMethodName || getPaymentMethodConfig(orderDetail.paymentMethod).name }}
              </a-tag>
              <span v-else>-</span>
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="12">
          <a-descriptions title="时间信息" :column="1" bordered>
            <a-descriptions-item label="创建时间">
              {{ formatDateTime(orderDetail.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="支付时间">
              {{ formatDateTime(orderDetail.paymentTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="更新时间">
              {{ formatDateTime(orderDetail.updateTime) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>

      <!-- 会员信息 -->
      <a-divider />
      <a-descriptions title="会员信息" :column="2" bordered>
        <a-descriptions-item label="会员姓名">
          <a-button type="link" @click="goToMemberDetail" :disabled="!orderDetail.memberId" style="padding: 0; height: auto;">
            {{ orderDetail.memberName }}
          </a-button>
        </a-descriptions-item>
        <a-descriptions-item label="联系电话">
          {{ orderDetail.memberPhone }}
        </a-descriptions-item>
      </a-descriptions>

      <!-- 订单备注 -->
      <a-divider />
      <div>
        <h4 style="margin-bottom: 16px;">
          <FileTextOutlined style="margin-right: 8px;" />
          订单备注
        </h4>
        <div style="
          background: #fafafa; 
          padding: 16px; 
          border-radius: 6px;
          min-height: 80px;
          white-space: pre-wrap;
        ">
          {{ orderDetail.remark || '暂无备注' }}
        </div>
      </div>
    </a-card>

    <!-- 订单明细 -->
    <a-card title="订单明细" style="margin-bottom: 16px;">
      <a-table
        :dataSource="orderDetail.orderItems || []"
        :columns="itemColumns"
        rowKey="id"
        size="small"
        :pagination="false"
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'productType'">
            <a-tag :color="getProductTypeColor(record.productType)">
              {{ record.productTypeName }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'unitPrice'">
            ¥{{ record.unitPrice }}
          </template>
          <template v-if="column.dataIndex === 'totalPrice'">
            <span style="font-weight: 500;">¥{{ record.totalPrice }}</span>
          </template>
          <template v-if="column.dataIndex === 'preferredTime'">
            {{ formatDateTime(record.preferredTime) }}
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 预约信息 -->
    <a-card title="预约信息">
      <a-table
        :dataSource="orderDetail.bookings || []"
        :columns="bookingColumns"
        rowKey="bookingId"
        size="small"
        :pagination="false"
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'bookingStatus'">
            <a-tag :color="getBookingStatusColor(record.bookingStatus)">
              {{ record.bookingStatusName }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'timeRange'">
            <div>
              <div>开始：{{ formatDateTime(record.startTime) }}</div>
              <div>结束：{{ formatDateTime(record.endTime) }}</div>
            </div>
          </template>
          <template v-if="column.dataIndex === 'fees'">
            <div style="font-size: 12px;">
              <div v-if="record.actualCoachFee">教练费：¥{{ record.actualCoachFee }}</div>
              <div v-if="record.actualHorseFee">马匹费：¥{{ record.actualHorseFee }}</div>
            </div>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a-button 
                v-if="record.bookingStatus === 2" 
                size="small" 
                type="primary"
                @click="completeBooking(record)"
                v-privilege="'business:booking:complete'"
              >
                核销
              </a-button>
              <a-button 
                size="small" 
                type="link"
                @click="viewBookingDetail(record)"
              >
                详情
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { 
  ArrowLeftOutlined, 
  FileTextOutlined
} from '@ant-design/icons-vue';
import { orderApi } from '/@/api/business/order/order-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { getPaymentMethodConfig } from '/@/config/payment-config';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();

// 响应式数据
const loading = ref(false);
const orderDetail = ref({});

// 订单明细表格列配置
const itemColumns = ref([
  {
    title: '商品名称',
    dataIndex: 'productName',
    width: 200
  },
  {
    title: '商品类型',
    dataIndex: 'productType',
    width: 100
  },
  {
    title: '数量',
    dataIndex: 'quantity',
    width: 80
  },
  {
    title: '单价',
    dataIndex: 'unitPrice',
    width: 100
  },
  {
    title: '小计',
    dataIndex: 'totalPrice',
    width: 100
  },
  {
    title: '教练',
    dataIndex: 'coachName',
    width: 100
  },
  {
    title: '期望时间',
    dataIndex: 'preferredTime',
    width: 160
  }
]);

// 预约信息表格列配置
const bookingColumns = ref([
  {
    title: '预约状态',
    dataIndex: 'bookingStatus',
    width: 100
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
    title: '上课时间',
    dataIndex: 'timeRange',
    width: 200
  },
  {
    title: '费用',
    dataIndex: 'fees',
    width: 120
  },
  {
    title: '到店时间',
    dataIndex: 'arrivalTime',
    width: 160
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 150
  }
]);

// 生命周期
onMounted(() => {
  loadOrderDetail();
});

// 方法
const loadOrderDetail = async () => {
  try {
    loading.value = true;
    const orderId = route.params.id;
    const res = await orderApi.getOrderDetail(orderId);
    if (res.ok) {
      orderDetail.value = res.data;
    } else {
      message.error(res.msg || '获取订单详情失败');
    }
  } catch (error) {
    message.error('获取订单详情失败');
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.back();
};

const goToMemberDetail = () => {
  if (orderDetail.value.memberId) {
    router.push({ name: 'MemberDetail', params: { memberId: orderDetail.value.memberId } });
  }
};

const confirmBooking = (booking) => {
  message.info('确认预约功能需要与预约管理模块集成');
};

const completeBooking = async (booking) => {
  try {
    const res = await orderApi.completeBooking(booking.bookingId);
    if (res.ok) {
      message.success('核销成功');
      loadOrderDetail(); // 重新加载订单详情
    } else {
      message.error('核销失败：' + res.msg);
    }
  } catch (error) {
    message.error('核销失败');
    smartSentry.captureError(error);
  }
};

const viewBookingDetail = (booking) => {
  router.push(`/booking/detail/${booking.bookingId}`);
};

// 辅助方法
const getOrderStatusColor = (status) => {
  const colors = {
    1: 'default',    // 待支付
    2: 'processing', // 已支付
    3: 'success',    // 已核销
    4: 'error',      // 已取消
    5: 'volcano'     // 已退款
  };
  return colors[status] || 'default';
};

const getOrderTypeColor = (type) => {
  const colors = {
    1: 'blue',    // 课程订单
    2: 'green',   // 课时包订单
    3: 'orange',  // 活动订单
    4: 'purple'   // 体验课订单
  };
  return colors[type] || 'default';
};

const getProductTypeColor = (type) => {
  const colors = {
    1: 'blue',    // 课程
    2: 'green',   // 课时包
    3: 'orange',  // 活动
    4: 'purple'   // 体验课
  };
  return colors[type] || 'default';
};

const getBookingStatusColor = (status) => {
  const colors = {
    1: 'default',    // 待确认
    2: 'processing', // 已确认
    3: 'warning',    // 进行中
    4: 'success',    // 已完成
    5: 'error',      // 已取消
    6: 'red'         // 未到场
  };
  return colors[status] || 'default';
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm:ss');
};
</script>

<style scoped>
.order-detail-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.ant-descriptions-title {
  font-weight: 600;
  font-size: 16px;
}

.ant-descriptions-item-label {
  font-weight: 500;
  background-color: #fafafa;
}

.ant-divider {
  margin: 24px 0;
}
</style>