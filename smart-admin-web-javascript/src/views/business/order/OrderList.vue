<!--
  * 订单列表页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-form class="smart-query-form" v-privilege="'business:order:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
          style="width: 200px"
          v-model:value="queryForm.keywords"
          placeholder="订单号/会员姓名/联系电话"
        />
      </a-form-item>

      <a-form-item label="订单类型" class="smart-query-form-item">
        <a-select
          style="width: 150px"
          v-model:value="queryForm.orderType"
          placeholder="请选择"
          allowClear
        >
          <a-select-option value="1">课程订单</a-select-option>
          <a-select-option value="2">课时包订单</a-select-option>
          <a-select-option value="3">活动订单</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="订单状态" class="smart-query-form-item">
        <a-select
          style="width: 120px"
          v-model:value="queryForm.orderStatus"
          placeholder="请选择"
          allowClear
        >
          <a-select-option value="1">待支付</a-select-option>
          <a-select-option value="2">已支付</a-select-option>
          <a-select-option value="3">已确认</a-select-option>
          <a-select-option value="4">已完成</a-select-option>
          <a-select-option value="5">已取消</a-select-option>
          <a-select-option value="6">已退款</a-select-option>
        </a-select>
      </a-form-item>


      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-range-picker
          style="width: 240px"
          v-model:value="queryForm.createTimeRange"
          :ranges="defaultTimeRanges"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary" @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="createOrder()" v-privilege="'business:order:add'" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          创建订单
        </a-button>
        <a-button @click="refreshData()">
          <template #icon>
            <ReloadOutlined />
          </template>
          刷新
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="null" :refresh="refreshData" />
      </div>
    </a-row>

    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="orderId"
      :loading="tableLoading"
      :pagination="false"
      :scroll="{ x: 1500 }"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'orderNo'">
          <a-button type="link" @click="viewDetail(record)" size="small">
            {{ record.orderNo }}
          </a-button>
        </template>

        <template v-if="column.dataIndex === 'memberInfo'">
          <div>
            <div style="font-weight: 500;">{{ record.memberName }}</div>
            <div style="font-size: 12px; color: #666;">{{ record.memberPhone }}</div>
          </div>
        </template>

        <template v-if="column.dataIndex === 'orderType'">
          <a-tag :color="getOrderTypeColor(record.orderType)">
            {{ record.orderTypeName }}
          </a-tag>
        </template>

        <template v-if="column.dataIndex === 'orderStatus'">
          <a-tag :color="getOrderStatusColor(record.orderStatus)">
            {{ record.orderStatusName }}
          </a-tag>
        </template>

        <template v-if="column.dataIndex === 'totalAmount'">
          <span style="font-weight: 500; color: #ff4d4f;">
            ¥{{ record.totalAmount }}
          </span>
        </template>

        <template v-if="column.dataIndex === 'bookingInfo'">
          <div style="font-size: 12px;">
            <div>订单明细: {{ record.itemCount || 0 }}</div>
            <div>预约数量: {{ record.bookingCount || 0 }}</div>
            <div v-if="record.pendingBookingCount > 0" style="color: #faad14;">
              待确认: {{ record.pendingBookingCount }}
            </div>
          </div>
        </template>

        <template v-if="column.dataIndex === 'createTime'">
          {{ formatDateTime(record.createTime) }}
        </template>

        <template v-if="column.dataIndex === 'action'">
          <a-space>
            <a-button @click="viewDetail(record)" size="small" type="link">
              <template #icon>
                <EyeOutlined />
              </template>
              详情
            </a-button>

            <a-button
              v-if="record.orderStatus === 1"
              @click="confirmPayment(record)"
              size="small"
              type="link"
              v-privilege="'business:order:payment'"
            >
              <template #icon>
                <CheckOutlined />
              </template>
              确认支付
            </a-button>

            <a-button
              v-if="record.orderStatus === 2"
              @click="confirmOrder(record)"
              size="small"
              type="link"
              v-privilege="'business:order:confirm'"
            >
              <template #icon>
                <CheckCircleOutlined />
              </template>
              确认订单
            </a-button>

            <a-button
              v-if="record.orderStatus === 3"
              @click="completeOrder(record)"
              size="small"
              type="link"
              v-privilege="'business:order:complete'"
            >
              <template #icon>
                <CheckSquareOutlined />
              </template>
              完成订单
            </a-button>

            <a-dropdown v-if="record.orderStatus < 4 && record.orderStatus !== 6">
              <a-button size="small" type="link">
                更多
                <DownOutlined />
              </a-button>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="cancelOrder(record)" v-privilege="'business:order:cancel'">
                    <CloseOutlined />
                    取消订单
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a-space>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="onSearch"
        @showSizeChange="onSearch"
        :showTotal="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import {
  SearchOutlined,
  ReloadOutlined,
  PlusOutlined,
  EyeOutlined,
  CheckOutlined,
  CheckCircleOutlined,
  CheckSquareOutlined,
  CloseOutlined,
  DownOutlined
} from '@ant-design/icons-vue';
import { orderApi } from '/@/api/business/order/order-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import TableOperator from '/@/components/support/table-operator/index.vue';
import dayjs from 'dayjs';

const router = useRouter();

// 响应式数据
const queryForm = reactive({
  keywords: '',
  orderType: undefined,
  orderStatus: undefined,
  createTimeRange: [],
  pageNum: 1,
  pageSize: 10
});

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const selectedRowKeys = ref([]);

// 时间范围选择器配置
const defaultTimeRanges = {
  今天: [dayjs().startOf('day'), dayjs().endOf('day')],
  昨天: [dayjs().subtract(1, 'day').startOf('day'), dayjs().subtract(1, 'day').endOf('day')],
  本周: [dayjs().startOf('week'), dayjs().endOf('week')],
  本月: [dayjs().startOf('month'), dayjs().endOf('month')],
  上月: [dayjs().subtract(1, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month')]
};

// 表格列配置
const columns = ref([
  {
    title: '订单号',
    dataIndex: 'orderNo',
    width: 180,
    fixed: 'left'
  },
  {
    title: '会员信息',
    dataIndex: 'memberInfo',
    width: 150
  },
  {
    title: '订单类型',
    dataIndex: 'orderType',
    width: 100
  },
  {
    title: '订单状态',
    dataIndex: 'orderStatus',
    width: 100
  },
  {
    title: '订单金额',
    dataIndex: 'totalAmount',
    width: 120
  },
  {
    title: '预约信息',
    dataIndex: 'bookingInfo',
    width: 120
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 180
  }
]);

// 生命周期
onMounted(() => {
  onSearch();
});

// 方法
const onSearch = async () => {
  try {
    tableLoading.value = true;
    let params = { ...queryForm };

    // 处理时间范围
    if (queryForm.createTimeRange && queryForm.createTimeRange.length === 2) {
      params.createTimeStart = queryForm.createTimeRange[0].format('YYYY-MM-DD HH:mm:ss');
      params.createTimeEnd = queryForm.createTimeRange[1].format('YYYY-MM-DD HH:mm:ss');
    }
    delete params.createTimeRange;

    const res = await orderApi.queryOrderList(params);
    if (res.ok) {
      tableData.value = res.data.list;
      total.value = res.data.total;
    } else {
      message.error(res.msg || '查询失败');
    }
  } catch (error) {
    message.error('查询失败');
    smartSentry.captureError(error);
  } finally {
    tableLoading.value = false;
  }
};

const resetQuery = () => {
  Object.assign(queryForm, {
    keywords: '',
    orderType: undefined,
    orderStatus: undefined,
    createTimeRange: [],
    pageNum: 1,
    pageSize: 10
  });
  onSearch();
};

const refreshData = () => {
  onSearch();
};

const onSelectChange = (keys) => {
  selectedRowKeys.value = keys;
};

const createOrder = () => {
  router.push('/order/create');
};

const viewDetail = (record) => {
  router.push(`/order/detail/${record.orderId}`);
};

const confirmPayment = async (record) => {
  try {
    const res = await orderApi.confirmPayment(record.orderId);
    if (res.ok) {
      message.success('确认支付成功');
      onSearch();
    } else {
      message.error(res.msg || '确认支付失败');
    }
  } catch (error) {
    message.error('确认支付失败');
    smartSentry.captureError(error);
  }
};

const confirmOrder = async (record) => {
  try {
    const res = await orderApi.confirmOrder(record.orderId);
    if (res.ok) {
      message.success('确认订单成功');
      onSearch();
    } else {
      message.error(res.msg || '确认订单失败');
    }
  } catch (error) {
    message.error('确认订单失败');
    smartSentry.captureError(error);
  }
};

const completeOrder = async (record) => {
  try {
    const res = await orderApi.completeOrder(record.orderId);
    if (res.ok) {
      message.success('完成订单成功');
      onSearch();
    } else {
      message.error(res.msg || '完成订单失败');
    }
  } catch (error) {
    message.error('完成订单失败');
    smartSentry.captureError(error);
  }
};

const cancelOrder = (record) => {
  Modal.confirm({
    title: '确认取消',
    content: `确定要取消订单"${record.orderNo}"吗？取消后将同时取消所有关联的预约。`,
    okText: '确认取消',
    cancelText: '取消',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await orderApi.cancelOrder(record.orderId, '手动取消');
        if (res.ok) {
          message.success('取消订单成功');
          onSearch();
        } else {
          message.error(res.msg || '取消订单失败');
        }
      } catch (error) {
        message.error('取消订单失败');
        smartSentry.captureError(error);
      }
    }
  });
};

// 辅助方法
const getOrderTypeColor = (orderType) => {
  const colors = {
    1: 'blue',    // 课程订单
    2: 'green',   // 课时包订单
    3: 'orange'   // 活动订单
  };
  return colors[orderType] || 'default';
};

const getOrderStatusColor = (orderStatus) => {
  const colors = {
    1: 'default',    // 待支付
    2: 'processing', // 已支付
    3: 'warning',    // 已确认
    4: 'success',    // 已完成
    5: 'error',      // 已取消
    6: 'volcano'     // 已退款
  };
  return colors[orderStatus] || 'default';
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm');
};
</script>

<style scoped>
.smart-query-form {
  background: #fff;
  border-radius: 6px;
  margin-bottom: 16px;
  padding: 16px;
}

.smart-query-form-row {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: -16px;
}

.smart-query-form-item {
  margin-bottom: 16px;
  margin-right: 16px;
}

.smart-margin-left10 {
  margin-left: 10px;
}

.smart-table-btn-block {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.smart-table-operate-block {
  display: flex;
  gap: 8px;
}

.smart-query-table-page {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
