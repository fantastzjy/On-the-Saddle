<!--
  * 预约列表页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="booking-list">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" v-privilege="'business:booking:query'">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input 
            style="width: 300px" 
            v-model:value="queryForm.keywords" 
            placeholder="预约单号/会员姓名/教练姓名" 
          />
        </a-form-item>

        <a-form-item label="预约状态" class="smart-query-form-item">
          <a-select
            style="width: 120px"
            v-model:value="queryForm.bookingStatus"
            placeholder="请选择"
            allowClear
          >
            <a-select-option 
              v-for="item in Object.values(BOOKING_STATUS_ENUM)" 
              :key="item.value" 
              :value="item.value"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="预约日期" class="smart-query-form-item">
          <a-range-picker
            v-model:value="queryForm.dateRange"
            style="width: 240px"
            :placeholder="['开始日期', '结束日期']"
            format="YYYY-MM-DD"
          />
        </a-form-item>

        <a-form-item label="教练" class="smart-query-form-item">
          <a-select
            style="width: 150px"
            v-model:value="queryForm.coachId"
            placeholder="请选择教练"
            allowClear
            showSearch
            :filterOption="filterOption"
          >
            <a-select-option 
              v-for="coach in coachList" 
              :key="coach.coachId" 
              :value="coach.coachId"
            >
              {{ coach.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item class="smart-query-form-item">
          <a-button type="primary" @click="queryData">
            <SearchOutlined />
            查询
          </a-button>
          <a-button @click="resetQuery" style="margin-left: 10px;">
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <!-- 操作栏 -->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button 
          type="primary" 
          size="small" 
          @click="addBooking"
          v-privilege="'business:booking:add'"
        >
          <PlusOutlined />
          新增预约
        </a-button>
        <a-button 
          @click="batchConfirm" 
          size="small" 
          :disabled="!selectedRowKeyList.length"
          v-privilege="'business:booking:confirm'"
        >
          <CheckOutlined />
          批量确认
        </a-button>
        <a-button 
          @click="batchCancel" 
          size="small" 
          :disabled="!selectedRowKeyList.length"
          v-privilege="'business:booking:cancel'"
        >
          <CloseOutlined />
          批量取消
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.SCHEDULE.BOOKING" :refresh="queryData" />
      </div>
    </a-row>

    <!-- 数据表格 -->
    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="bookingId"
      :loading="tableLoading"
      :pagination="false"
      :scroll="{ x: 1500 }"
      :row-selection="rowSelection"
    >
      <template #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'bookingNo'">
          <a @click="showBookingDetail(record)" v-privilege="'business:booking:detail'">
            {{ text }}
          </a>
        </template>

        <template v-else-if="column.dataIndex === 'bookingStatus'">
          <a-space direction="vertical" size="small">
            <a-tag :color="getStatusColor(text)">
              {{ getStatusName(text) }}
            </a-tag>
            <a-tag v-if="record.arrivalTime" color="success" size="small">
              已核销 {{ formatTime(record.arrivalTime) }}
            </a-tag>
          </a-space>
        </template>

        <template v-else-if="column.dataIndex === 'bookingDate'">
          {{ formatDate(text) }}
        </template>

        <template v-else-if="column.dataIndex === 'bookingTime'">
          {{ record.startTime }} - {{ record.endTime }}
        </template>

        <template v-else-if="column.dataIndex === 'memberInfo'">
          <div>
            <div>{{ record.memberName }}</div>
            <div style="color: #999; font-size: 12px;">{{ record.memberPhone }}</div>
          </div>
        </template>

        <template v-else-if="column.dataIndex === 'coachInfo'">
          <div>
            <div>{{ record.coachName }}</div>
            <div style="color: #999; font-size: 12px;">{{ record.coachSpecialties }}</div>
          </div>
        </template>

        <template v-else-if="column.dataIndex === 'action'">
          <a-space>
            <a @click="showBookingDetail(record)" v-privilege="'business:booking:detail'">详情</a>
            
            <!-- 已确认且未核销：显示核销和改期 -->
            <template v-if="record.bookingStatus === 2 && !record.arrivalTime">
              <a 
                @click="checkinBooking(record)" 
                v-privilege="'business:booking:checkin'"
                style="color: #52c41a;"
              >
                核销
              </a>
              <a 
                @click="showRescheduleModal(record)" 
                v-privilege="'business:booking:reschedule'"
                style="color: #1890ff;"
              >
                改期
              </a>
            </template>
            
            <!-- 已确认但未核销：可以取消 -->
            <a 
              @click="cancelBooking(record)" 
              v-if="record.bookingStatus === 2 && !record.arrivalTime"
              v-privilege="'business:booking:cancel'"
              style="color: #ff4d4f;"
            >
              取消
            </a>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 分页 -->
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
        @change="queryData"
        @showSizeChange="queryData"
        :showTotal="(total) => `共${total}条`"
      />
    </div>

    <!-- 改期弹窗 -->
    <BookingRescheduleModal
      v-model:visible="rescheduleModalVisible"
      :booking-info="currentBookingInfo"
      @success="onRescheduleSuccess"
    />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import {
  SearchOutlined,
  PlusOutlined,
  CheckOutlined,
  CloseOutlined
} from '@ant-design/icons-vue';
import { bookingApi } from '/@/api/business/booking/booking-api';
import { coachApi } from '/@/api/business/coach/coach-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
import { BOOKING_STATUS_ENUM } from '/@/constants/business/schedule/schedule-const';
import TableOperator from '/@/components/support/table-operator/index.vue';
import BookingRescheduleModal from './components/BookingRescheduleModal.vue';
import dayjs from 'dayjs';

const router = useRouter();

// 表格数据
const columns = ref([
  {
    title: '预约单号',
    dataIndex: 'bookingNo',
    width: 140,
    fixed: 'left'
  },
  {
    title: '预约状态',
    dataIndex: 'bookingStatus',
    width: 100
  },
  {
    title: '预约日期',
    dataIndex: 'bookingDate',
    width: 120
  },
  {
    title: '预约时间',
    dataIndex: 'bookingTime',
    width: 140
  },
  {
    title: '会员信息',
    dataIndex: 'memberInfo',
    width: 140
  },
  {
    title: '教练信息',
    dataIndex: 'coachInfo',
    width: 140
  },
  {
    title: '马匹',
    dataIndex: 'horseName',
    width: 100
  },
  // {
  //   title: '课程类型',
  //   dataIndex: 'lessonTypeName',
  //   width: 100
  // },
  {
    title: '费用',
    dataIndex: 'totalFee',
    width: 80
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
    width: 200
  }
]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

// 查询表单
const queryForm = reactive({
  keywords: '',
  bookingStatus: null,
  dateRange: null,
  coachId: null,
  pageNum: 1,
  pageSize: 10
});

// 选择行
const selectedRowKeyList = ref([]);
const rowSelection = {
  onChange: (selectedRowKeys) => {
    selectedRowKeyList.value = selectedRowKeys;
  }
};

// 其他数据
const coachList = ref([]);

// 改期弹窗相关
const rescheduleModalVisible = ref(false);
const currentBookingInfo = ref(null);

// 生命周期
onMounted(() => {
  queryData();
  loadCoaches();
});

// 方法
const queryData = async () => {
  try {
    tableLoading.value = true;
    
    let params = { ...queryForm };
    
    // 处理日期范围
    if (queryForm.dateRange && queryForm.dateRange.length === 2) {
      params.startDate = dayjs(queryForm.dateRange[0]).format('YYYY-MM-DD');
      params.endDate = dayjs(queryForm.dateRange[1]).format('YYYY-MM-DD');
    }
    delete params.dateRange;

    const res = await bookingApi.getBookingList(params);
    if (res.ok) {
      tableData.value = res.data.list || [];
      total.value = res.data.total || 0;
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
    bookingStatus: null,
    dateRange: null,
    coachId: null,
    pageNum: 1,
    pageSize: 10
  });
  queryData();
};

const loadCoaches = async () => {
  try {
    const res = await coachApi.getCoachList({ pageNum: 1, pageSize: 100 });
    if (res.ok) {
      coachList.value = res.data.list || [];
    }
  } catch (error) {
    console.error('加载教练列表失败', error);
  }
};

const addBooking = () => {
  router.push('/booking/add');
};

const showBookingDetail = (record) => {
  router.push(`/booking/detail/${record.bookingId}`);
};

const editBooking = (record) => {
  router.push(`/booking/edit/${record.bookingId}`);
};

const confirmBooking = async (record) => {
  try {
    const res = await bookingApi.confirmBooking(record.bookingId);
    if (res.ok) {
      message.success('确认成功');
      queryData();
    } else {
      message.error(res.msg || '确认失败');
    }
  } catch (error) {
    message.error('确认失败');
    smartSentry.captureError(error);
  }
};

const cancelBooking = (record) => {
  Modal.confirm({
    title: '确认取消',
    content: `确定要取消预约"${record.bookingNo}"吗？`,
    okText: '确认取消',
    cancelText: '取消',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await bookingApi.cancelBooking(record.bookingId);
        if (res.ok) {
          message.success('取消成功');
          queryData();
        } else {
          message.error(res.msg || '取消失败');
        }
      } catch (error) {
        message.error('取消失败');
        smartSentry.captureError(error);
      }
    }
  });
};

const checkinBooking = async (record) => {
  try {
    const res = await bookingApi.checkinBooking(record.bookingId);
    if (res.ok) {
      message.success('核销成功');
      queryData();
    } else {
      message.error(res.msg || '核销失败');
    }
  } catch (error) {
    message.error('核销失败');
    smartSentry.captureError(error);
  }
};

const batchConfirm = () => {
  if (selectedRowKeyList.value.length === 0) {
    message.warning('请选择要确认的预约');
    return;
  }

  Modal.confirm({
    title: '批量确认',
    content: `确定要确认选中的${selectedRowKeyList.value.length}个预约吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        const res = await bookingApi.batchConfirmBookings(selectedRowKeyList.value);
        if (res.ok) {
          message.success('批量确认成功');
          selectedRowKeyList.value = [];
          queryData();
        } else {
          message.error(res.msg || '批量确认失败');
        }
      } catch (error) {
        message.error('批量确认失败');
        smartSentry.captureError(error);
      }
    }
  });
};

const batchCancel = () => {
  if (selectedRowKeyList.value.length === 0) {
    message.warning('请选择要取消的预约');
    return;
  }

  Modal.confirm({
    title: '批量取消',
    content: `确定要取消选中的${selectedRowKeyList.value.length}个预约吗？`,
    okText: '确认取消',
    cancelText: '取消',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await bookingApi.batchCancelBookings(selectedRowKeyList.value);
        if (res.ok) {
          message.success('批量取消成功');
          selectedRowKeyList.value = [];
          queryData();
        } else {
          message.error(res.msg || '批量取消失败');
        }
      } catch (error) {
        message.error('批量取消失败');
        smartSentry.captureError(error);
      }
    }
  });
};

const getStatusColor = (status) => {
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

const getStatusName = (status) => {
  const statusItem = Object.values(BOOKING_STATUS_ENUM).find(item => item.value === status);
  return statusItem ? statusItem.desc : '未知';
};

const formatDate = (date) => {
  if (!date) return '-';
  return dayjs(date).format('MM-DD dddd');
};

const filterOption = (input, option) => {
  return option.children[0].children.toLowerCase().includes(input.toLowerCase());
};

// 改期相关方法
const showRescheduleModal = (record) => {
  console.log('BookingList - record data:', record);
  console.log('BookingList - startTime:', record.startTime, typeof record.startTime);
  console.log('BookingList - endTime:', record.endTime, typeof record.endTime);
  currentBookingInfo.value = record;
  rescheduleModalVisible.value = true;
};

const onRescheduleSuccess = () => {
  rescheduleModalVisible.value = false;
  currentBookingInfo.value = null;
  queryData(); // 刷新数据
};

const formatTime = (dateTime) => {
  if (!dateTime) return '';
  return dayjs(dateTime).format('HH:mm');
};
</script>

<style scoped>
.booking-list {
  margin: 10px;
}

:deep(.ant-table-tbody > tr > td) {
  padding: 8px;
}
</style>