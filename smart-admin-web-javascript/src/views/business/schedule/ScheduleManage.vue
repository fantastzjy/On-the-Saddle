<!--
  * 课表管理页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="schedule-manage">
    <!-- 全局加载指示器 -->
    <a-spin :spinning="isLoading" tip="处理中...">
      <!-- 查询表单 -->
    <a-form class="smart-query-form" v-privilege="'business:schedule:query'">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input 
            style="width: 200px" 
            v-model:value="queryForm.keywords" 
            placeholder="课单号/会员姓名/教练姓名" 
          />
        </a-form-item>

        <a-form-item label="课单状态" class="smart-query-form-item">
          <a-select
            style="width: 120px"
            v-model:value="queryForm.lessonStatus"
            placeholder="请选择"
            allowClear
          >
            <a-select-option 
              v-for="item in Object.values(LESSON_STATUS_ENUM)" 
              :key="item.value" 
              :value="item.value"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="教练" class="smart-query-form-item">
          <a-select
            style="width: 150px"
            v-model:value="queryForm.coachId"
            placeholder="请选择教练"
            allowClear
            showSearch
            :loading="loadingStates.coachList"
            :filterOption="filterOption"
          >
            <a-select-option 
              v-for="coach in coachList" 
              :key="coach.coachId" 
              :value="coach.coachId"
            >
              {{ coach.coachName }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="上课时间" class="smart-query-form-item">
          <a-range-picker 
            style="width: 280px" 
            v-model:value="queryForm.dateRange" 
            :ranges="defaultTimeRanges"
            format="YYYY-MM-DD"
            valueFormat="YYYY-MM-DD"
          />
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch" :loading="loadingStates.query">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery" :disabled="loadingStates.query">
              <template #icon>
                <ReloadOutlined />
              </template>
              清空
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <!-- 操作栏和视图切换 -->
    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button-group>
            <a-button 
              :type="viewType === 'list' ? 'primary' : 'default'"
              @click="switchView('list')"
            >
              <template #icon>
                <UnorderedListOutlined />
              </template>
              列表视图
            </a-button>
            <a-button 
              :type="viewType === 'combined' ? 'primary' : 'default'"
              @click="switchView('combined')"
            >
              <template #icon>
                <AppstoreOutlined />
              </template>
              综合视图
            </a-button>
            <a-button 
              :type="viewType === 'calendar' ? 'primary' : 'default'"
              @click="switchView('calendar')"
            >
              <template #icon>
                <CalendarOutlined />
              </template>
              日历视图
            </a-button>
            <a-button 
              :type="viewType === 'coach' ? 'primary' : 'default'"
              @click="switchView('coach')"
            >
              <template #icon>
                <TeamOutlined />
              </template>
              教练视图
            </a-button>
          </a-button-group>

          <a-button 
            @click="addSchedule()" 
            v-privilege="'business:schedule:add'" 
            type="primary"
            class="ml-16"
          >
            <template #icon>
              <PlusOutlined />
            </template>
            新增排课
          </a-button>

          <a-button 
            @click="showDragSchedule()" 
            v-privilege="'business:schedule:drag'" 
            type="dashed"
          >
            <template #icon>
              <DragOutlined />
            </template>
            拖拽排课
          </a-button>
        </div>

      </a-row>

      <!-- 列表视图 -->
      <div v-show="viewType === 'list'">
        <SimpleBookingList
          :booking-list="listData"
          :loading="listLoading"
          :total="listTotal"
          :page-num="listQueryForm.pageNum"
          :page-size="listQueryForm.pageSize"
          @query="loadBookingList"
          @page-change="handleListPageChange"
        />
      </div>

      <!-- 综合视图 -->
      <div v-show="viewType === 'combined'">
        <div class="combined-view-container">
          <a-spin :spinning="combinedLoading">
            <div v-if="combinedData.length > 0" class="order-list">
              <OrderCard
                v-for="order in combinedData"
                :key="order.orderId"
                :order-data="order"
                @add-booking="handleAddBooking"
                @view-detail="handleViewOrderDetail"
                @booking-action="handleBookingAction"
              />
            </div>
            <a-empty v-else description="暂无数据" />
          </a-spin>
        </div>

        <div class="smart-query-table-page" v-if="viewType === 'combined'">
          <a-pagination
            showSizeChanger
            showQuickJumper
            show-less-items
            :pageSizeOptions="PAGE_SIZE_OPTIONS"
            :defaultPageSize="combinedQueryForm.pageSize"
            v-model:current="combinedQueryForm.pageNum"
            v-model:pageSize="combinedQueryForm.pageSize"
            :total="combinedTotal"
            @change="loadCombinedData"
            @showSizeChange="loadCombinedData"
            :show-total="(total) => `共${total}条`"
          />
        </div>
      </div>

      <!-- 日历视图 -->
      <div v-show="viewType === 'calendar'">
        <CalendarView
          :loading="tableLoading"
          :schedules="tableData"
          :view-type="calendarViewType"
          @view-change="onCalendarViewChange"
          @date-change="onCalendarDateChange"
          @schedule-click="detail"
          @schedule-drag="onScheduleDrag"
          @time-slot-click="onTimeSlotClick"
        />
      </div>

      <!-- 教练视图 -->
      <div v-show="viewType === 'coach'">
        <div class="coach-view-controls">
          <a-space>
            <a-radio-group v-model:value="coachViewType" @change="onCoachViewChange">
              <a-radio-button value="day">日视图</a-radio-button>
              <a-radio-button value="week">周视图</a-radio-button>
              <a-radio-button value="month">月视图</a-radio-button>
            </a-radio-group>
            
            <a-date-picker 
              v-model:value="coachQueryDate" 
              @change="onCoachDateChange"
              :format="getDateFormat()"
              :picker="getDatePicker()"
            />
            
            <a-button @click="loadCoachViewData" :loading="coachViewLoading">
              <template #icon>
                <ReloadOutlined />
              </template>
              刷新
            </a-button>
          </a-space>
        </div>

        <!-- 日视图 -->
        <CoachDayView
          v-if="coachViewType === 'day'"
          :loading="coachViewLoading"
          :coach-data="coachDayData.coaches || []"
          :query-date="coachDayData.queryDate"
          @schedule-click="detail"
          @slot-click="onCoachSlotClick"
          @create-schedule="onCreateScheduleFromSlot"
        />

        <!-- 周视图 -->
        <CoachWeekView
          v-else-if="coachViewType === 'week'"
          :loading="coachViewLoading"
          :coach-data="coachWeekData.coaches || []"
          :week-start-date="coachWeekData.weekStartDate"
          :week-end-date="coachWeekData.weekEndDate"
          @slot-click="onCoachWeekSlotClick"
        />

        <!-- 月视图 -->
        <CoachMonthView
          v-else-if="coachViewType === 'month'"
          :loading="coachViewLoading"
          :year="coachMonthData.year"
          :month="coachMonthData.month"
          :calendar-days="coachMonthData.calendarDays || []"
          :coach-stats="coachMonthData.coachStats || []"
          @day-click="onCoachMonthDayClick"
        />
      </div>
    </a-card>

    <!-- 拖拽排课模态框 -->
    <DragScheduleModal
      v-model:visible="dragScheduleVisible"
      @ok="onDragScheduleOk"
    />

    <!-- 冲突检测模态框 -->
    <ConflictDetectorModal
      v-model:visible="conflictDetectorVisible"
      :schedule="currentSchedule"
      @ok="onConflictDetectorOk"
    />

    <!-- 课时包预约创建模态框 -->
    <PackageBookingCreateModal
      v-model:visible="packageBookingModalVisible"
      :package-data="currentPackageData"
      @success="onPackageBookingSuccess"
    />
    </a-spin>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { 
  SearchOutlined, 
  ReloadOutlined, 
  PlusOutlined,
  UnorderedListOutlined,
  CalendarOutlined,
  AppstoreOutlined,
  DragOutlined,
  TeamOutlined
} from '@ant-design/icons-vue';
import { useRouter } from 'vue-router';
import { scheduleApi } from '/@/api/business/schedule/schedule-api';
import { bookingApi } from '/@/api/business/booking/booking-api';
import { coachScheduleApi } from '/@/api/business/schedule/coach-schedule-api';
import { 
  LESSON_STATUS_ENUM,
  BOOKING_STATUS_ENUM,
  ORDER_STATUS_ENUM,
  SCHEDULE_SEARCH_FORM,
  SCHEDULE_VIEW_TYPE_ENUM,
  getOrderStatusInfo,
  getBookingStatusInfo,
  getLessonStatusInfo
} from '/@/constants/business/schedule/schedule-const';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import SimpleBookingList from './components/SimpleBookingList.vue';
import CalendarView from './components/CalendarView.vue';
import DragScheduleModal from './components/DragScheduleModal.vue';
import ConflictDetectorModal from './components/ConflictDetectorModal.vue';
import OrderCard from './components/OrderCard.vue';
import PackageBookingCreateModal from './components/PackageBookingCreateModal.vue';
import CoachDayView from './components/CoachDayView.vue';
import CoachWeekView from './components/CoachWeekView.vue';
import CoachMonthView from './components/CoachMonthView.vue';
import dayjs from 'dayjs';

const router = useRouter();

// ======================== 时间范围配置 ========================
const defaultTimeRanges = {
  今天: [dayjs().startOf('day'), dayjs().endOf('day')],
  昨天: [dayjs().subtract(1, 'day').startOf('day'), dayjs().subtract(1, 'day').endOf('day')],
  本周: [dayjs().startOf('week'), dayjs().endOf('week')],
  本月: [dayjs().startOf('month'), dayjs().endOf('month')],
  上月: [dayjs().subtract(1, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month')]
};

// ======================== 响应式数据 ========================
const queryForm = reactive({ ...SCHEDULE_SEARCH_FORM, pageNum: 1, pageSize: 10 });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const coachList = ref([]);

// 缓存相关
const dataCache = ref(new Map()); // 数据缓存
const cacheExpiry = ref(new Map()); // 缓存过期时间
const CACHE_DURATION = 5 * 60 * 1000; // 5分钟缓存
const coachListLoading = ref(false);

// 视图相关
const viewType = ref('list'); // list | combined | calendar | coach
const calendarViewType = ref(SCHEDULE_VIEW_TYPE_ENUM.WEEK.value);

// 教练视图相关
const coachViewType = ref('day'); // day | week | month
const coachQueryDate = ref(dayjs());
const coachViewLoading = ref(false);
const coachDayData = ref({});
const coachWeekData = ref({});
const coachMonthData = ref({});

// 列表视图相关
const listData = ref([]);
const listLoading = ref(false);
const listTotal = ref(0);
const listQueryForm = reactive({
  keywords: '',
  coachId: null,
  dateRange: [],
  pageNum: 1,
  pageSize: 10
});

// 综合视图相关
const combinedData = ref([]);
const combinedLoading = ref(false);
const combinedTotal = ref(0);
const combinedQueryForm = reactive({
  keywords: '',
  productType: null,
  orderStatus: null,
  buyerMemberId: null,
  consumerMemberId: null, 
  coachId: null,
  pageNum: 1,
  pageSize: 10
});

// 模态框相关
const dragScheduleVisible = ref(false);
const conflictDetectorVisible = ref(false);
const packageBookingModalVisible = ref(false);
const currentSchedule = ref(null);
const currentPackageData = ref(null);

// 加载状态优化
const loadingStates = reactive({
  query: false,
  coachList: false,
  delete: false,
  cancel: false,
  drag: false
});

// 计算属性 - 是否有任何加载状态
const isLoading = computed(() => {
  return Object.values(loadingStates).some(loading => loading);
});

// ======================== 初始化 ========================
onMounted(() => {
  loadCoachList();
  // 根据默认视图类型加载相应数据
  if (viewType.value === 'list') {
    loadBookingList();
  } else if (viewType.value === 'combined') {
    loadCombinedData();
  } else {
    ajaxQuery();
  }
});

// ======================== 缓存工具函数 ========================
function getCacheKey(params) {
  return JSON.stringify(params);
}

function isCacheValid(key) {
  const expiry = cacheExpiry.value.get(key);
  return expiry && Date.now() < expiry;
}

function setCache(key, data) {
  dataCache.value.set(key, data);
  cacheExpiry.value.set(key, Date.now() + CACHE_DURATION);
}

function getCache(key) {
  if (isCacheValid(key)) {
    return dataCache.value.get(key);
  }
  // 清理过期缓存
  dataCache.value.delete(key);
  cacheExpiry.value.delete(key);
  return null;
}

function clearCache() {
  dataCache.value.clear();
  cacheExpiry.value.clear();
}

// ======================== 查询相关 ========================
function onSearch() {
  if (viewType.value === 'list') {
    // 列表视图查询
    listQueryForm.pageNum = 1;
    listQueryForm.keywords = queryForm.keywords;
    listQueryForm.coachId = queryForm.coachId;
    listQueryForm.dateRange = queryForm.dateRange;
    loadBookingList();
  } else if (viewType.value === 'combined') {
    combinedQueryForm.pageNum = 1;
    // 将查询表单的条件同步到综合视图查询
    combinedQueryForm.keywords = queryForm.keywords;
    combinedQueryForm.coachId = queryForm.coachId;
    loadCombinedData();
  } else {
    // 日历视图也需要查询数据
    queryForm.pageNum = 1;
    clearCache();
    ajaxQuery();
  }
}

function resetQuery() {
  Object.assign(queryForm, { ...SCHEDULE_SEARCH_FORM, pageNum: 1, pageSize: queryForm.pageSize });
  if (viewType.value === 'list') {
    // 重置列表视图查询
    Object.assign(listQueryForm, {
      keywords: '',
      coachId: null,
      dateRange: [],
      pageNum: 1,
      pageSize: 10
    });
    loadBookingList();
  } else if (viewType.value === 'combined') {
    // 重置综合视图查询
    Object.assign(combinedQueryForm, {
      keywords: '',
      productType: null,
      orderStatus: null,
      buyerMemberId: null,
      consumerMemberId: null, 
      coachId: null,
      pageNum: 1,
      pageSize: 10
    });
    loadCombinedData();
  } else {
    // 重置日历视图查询
    clearCache();
    ajaxQuery();
  }
}

async function ajaxQuery(useCache = true) {
  try {
    loadingStates.query = true;
    tableLoading.value = true;
    
    const params = {
      ...queryForm,
      startDate: queryForm.dateRange?.[0],
      endDate: queryForm.dateRange?.[1],
      dateRange: undefined
    };
    
    // 尝试从缓存获取数据
    const cacheKey = getCacheKey(params);
    if (useCache) {
      const cachedData = getCache(cacheKey);
      if (cachedData) {
        tableData.value = cachedData.list || [];
        total.value = cachedData.total || 0;
        loadingStates.query = false;
        tableLoading.value = false;
        return;
      }
    }
    
    const response = await scheduleApi.queryScheduleList(params);
    if (response.ok) {
      const data = response.data;
      tableData.value = data.list || [];
      total.value = data.total || 0;
      
      // 缓存数据
      setCache(cacheKey, data);
    } else {
      message.error(response.msg || '查询失败');
    }
  } catch (error) {
    message.error('查询课表列表失败');
    console.error('查询课表列表失败:', error);
  } finally {
    loadingStates.query = false;
    tableLoading.value = false;
  }
}

async function loadCoachList() {
  try {
    loadingStates.coachList = true;
    coachListLoading.value = true;
    
    // 检查教练列表缓存
    const cacheKey = 'coachList';
    const cachedCoaches = getCache(cacheKey);
    if (cachedCoaches) {
      coachList.value = cachedCoaches;
      loadingStates.coachList = false;
      coachListLoading.value = false;
      return;
    }
    
    const response = await scheduleApi.getCoachList();
    if (response.ok) {
      coachList.value = response.data || [];
      // 缓存教练列表
      setCache(cacheKey, response.data || []);
    }
  } catch (error) {
    console.error('加载教练列表失败:', error);
    message.error('加载教练列表失败');
  } finally {
    loadingStates.coachList = false;
    coachListLoading.value = false;
  }
}

// ======================== 视图切换 ========================
function switchView(type) {
  viewType.value = type;
  if (type === 'list') {
    // 列表视图需要查询预约数据
    loadBookingList();
  } else if (type === 'calendar') {
    // 日历视图需要重新查询当月数据
    ajaxQuery();
  } else if (type === 'combined') {
    // 综合视图需要查询订单数据
    loadCombinedData();
  } else if (type === 'coach') {
    // 教练视图需要查询教练数据
    loadCoachViewData();
  }
}

function onCalendarViewChange(newViewType) {
  calendarViewType.value = newViewType;
}

function onCalendarDateChange(date) {
  // 根据日历选择的日期更新查询参数
  queryForm.dateRange = [date, date];
  ajaxQuery();
}

function onTimeSlotClick(timeSlot) {
  // 点击时间段创建新课程
  router.push(`/schedule/add?date=${timeSlot.date}&time=${timeSlot.time}`);
}

// ======================== 页面操作 ========================
function addSchedule() {
  router.push('/schedule/add');
}

function edit(record) {
  router.push(`/schedule/edit/${record.scheduleId}`);
}

function detail(scheduleId) {
  router.push(`/schedule/detail/${scheduleId}`);
}

async function remove(scheduleId) {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除这个课单吗？删除后不可恢复。',
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        loadingStates.delete = true;
        const response = await scheduleApi.deleteSchedule(scheduleId);
        if (response.ok) {
          message.success('删除成功');
          // 清除缓存并重新查询
          clearCache();
          ajaxQuery(false);
        } else {
          message.error(response.msg || '删除失败');
        }
      } catch (error) {
        message.error('删除课单失败');
        console.error('删除课单失败:', error);
      } finally {
        loadingStates.delete = false;
      }
    }
  });
}

async function cancelLesson(record) {
  Modal.confirm({
    title: '确认取消',
    content: `确定要取消课单 ${record.scheduleNo} 吗？`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        loadingStates.cancel = true;
        const response = await scheduleApi.cancelLesson(record.scheduleId);
        if (response.ok) {
          message.success('取消成功');
          // 清除缓存并重新查询
          clearCache();
          ajaxQuery(false);
        } else {
          message.error(response.msg || '取消失败');
        }
      } catch (error) {
        message.error('取消课程失败');
        console.error('取消课程失败:', error);
      } finally {
        loadingStates.cancel = false;
      }
    }
  });
}

// ======================== 拖拽排课 ========================
function showDragSchedule() {
  dragScheduleVisible.value = true;
}

function onDragScheduleOk() {
  dragScheduleVisible.value = false;
  // 清除缓存并重新查询
  clearCache();
  ajaxQuery(false);
}

function onScheduleDrag(dragData) {
  // 处理日历视图中的拖拽事件
  handleScheduleDrag(dragData);
}

async function handleScheduleDrag(dragData) {
  try {
    loadingStates.drag = true;
    const response = await scheduleApi.updateScheduleTime({
      scheduleId: dragData.scheduleId,
      startTime: dragData.newStartTime,
      endTime: dragData.newEndTime
    });
    
    if (response.ok) {
      message.success('课程时间更新成功');
      // 清除缓存并重新查询
      clearCache();
      ajaxQuery(false);
    } else {
      message.error(response.msg || '更新失败');
    }
  } catch (error) {
    message.error('更新课程时间失败');
    console.error('更新课程时间失败:', error);
  } finally {
    loadingStates.drag = false;
  }
}

// ======================== 冲突检测 ========================
function checkConflict(record) {
  currentSchedule.value = record;
  conflictDetectorVisible.value = true;
}

function onConflictDetectorOk() {
  conflictDetectorVisible.value = false;
  currentSchedule.value = null;
}

// ======================== 辅助方法 ========================
// 使用新的统一状态信息获取函数，移除旧的分散函数

function formatDateTime(dateTime) {
  if (!dateTime) return '-';
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function filterOption(input, option) {
  return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
}

// ======================== 综合视图相关方法 ========================
async function loadCombinedData() {
  try {
    combinedLoading.value = true;
    
    const params = {
      ...combinedQueryForm,
      // 将原有查询条件同步到综合视图查询
      keywords: queryForm.keywords || combinedQueryForm.keywords,
      coachId: queryForm.coachId || combinedQueryForm.coachId,
      createTimeStart: queryForm.dateRange?.[0],
      createTimeEnd: queryForm.dateRange?.[1]
    };
    
    const response = await scheduleApi.queryCombinedSchedule(params);
    
    if (response.code === 0 && response.data) {
      combinedData.value = response.data.list || [];
      combinedTotal.value = response.data.total || 0;
    } else {
      message.error(response.mesg || '查询失败');
      combinedData.value = [];
      combinedTotal.value = 0;
    }
  } catch (error) {
    console.error('加载综合数据失败:', error);
    message.error('加载数据失败');
    combinedData.value = [];
    combinedTotal.value = 0;
  } finally {
    combinedLoading.value = false;
  }
}

function handleAddBooking(orderData) {
  currentPackageData.value = orderData;
  packageBookingModalVisible.value = true;
}

function handleViewOrderDetail(orderData) {
  // 跳转到订单详情页面
  router.push(`/business/order/detail/${orderData.orderId}`);
}

function handleBookingAction(action, booking, orderData) {
  switch (action) {
    case 'view':
      handleViewBookingDetail(booking);
      break;
    case 'confirm':
      handleConfirmBooking(booking);
      break;
    case 'reschedule':
      handleRescheduleBooking(booking);
      break;
    case 'cancel':
      handleCancelBooking(booking);
      break;
    default:
      console.warn('未知操作:', action);
  }
}

function handleViewBookingDetail(booking) {
  // 跳转到预约详情页面
  router.push(`/business/booking/detail/${booking.bookingId}`);
}

async function handleConfirmBooking(booking) {
  try {
    Modal.confirm({
      title: '确认预约',
      content: '确定要确认这个预约吗？',
      okText: '确认',
      cancelText: '取消',
      onOk: async () => {
        const response = await bookingApi.confirmBooking(booking.bookingId);
        if (response.code === 0) {
          message.success('预约确认成功');
          loadCombinedData(); // 刷新数据
        } else {
          message.error(response.mesg || '确认失败');
        }
      }
    });
  } catch (error) {
    console.error('确认预约失败:', error);
    message.error('操作失败');
  }
}

function handleRescheduleBooking(booking) {
  // 实现预约改期逻辑
  message.info('预约改期功能开发中');
}

async function handleCancelBooking(booking) {
  try {
    Modal.confirm({
      title: '取消预约',
      content: '确定要取消这个预约吗？取消后无法恢复。',
      okText: '确认取消',
      cancelText: '保留',
      okType: 'danger',
      onOk: async () => {
        const response = await bookingApi.cancelBooking(booking.bookingId, '用户取消');
        if (response.code === 0) {
          message.success('预约取消成功');
          loadCombinedData(); // 刷新数据
        } else {
          message.error(response.mesg || '取消失败');
        }
      }
    });
  } catch (error) {
    console.error('取消预约失败:', error);
    message.error('操作失败');
  }
}

function onPackageBookingSuccess() {
  message.success('预约创建成功');
  loadCombinedData(); // 刷新综合视图数据
}

// ======================== 列表视图相关方法 ========================
async function loadBookingList() {
  try {
    listLoading.value = true;
    
    const params = {
      ...listQueryForm,
      startDate: listQueryForm.dateRange?.[0],
      endDate: listQueryForm.dateRange?.[1],
      dateRange: undefined
    };
    
    const response = await bookingApi.getSimpleBookingList(params);
    
    if (response.ok && response.data) {
      listData.value = response.data.list || [];
      listTotal.value = response.data.total || 0;
    } else {
      console.error('加载简化预约列表失败:', response.msg || response.mesg);
      message.error(response.msg || response.mesg || '加载数据失败');
      listData.value = [];
      listTotal.value = 0;
    }
  } catch (error) {
    console.error('加载预约列表失败:', error);
    message.error('加载数据失败');
    listData.value = [];
    listTotal.value = 0;
  } finally {
    listLoading.value = false;
  }
}

function handleListPageChange({ pageNum, pageSize }) {
  listQueryForm.pageNum = pageNum;
  listQueryForm.pageSize = pageSize;
  loadBookingList();
}

// ======================== 教练视图相关方法 ========================
async function loadCoachViewData() {
  try {
    coachViewLoading.value = true;
    
    const params = {
      queryDate: coachQueryDate.value.format('YYYY-MM-DD'),
      clubId: 1, // TODO: 从用户信息或选择器中获取clubId
      coachIds: queryForm.coachId ? [queryForm.coachId] : null,
      keywords: queryForm.keywords
    };

    if (coachViewType.value === 'day') {
      const response = await coachScheduleApi.getDayView(params);
      if (response.ok && response.data) {
        coachDayData.value = response.data;
      } else {
        message.error(response.msg || '加载教练日视图数据失败');
      }
    } else if (coachViewType.value === 'week') {
      const response = await coachScheduleApi.getWeekView(params);
      if (response.ok && response.data) {
        coachWeekData.value = response.data;
      } else {
        message.error(response.msg || '加载教练周视图数据失败');
      }
    } else if (coachViewType.value === 'month') {
      const response = await coachScheduleApi.getMonthView(params);
      if (response.ok && response.data) {
        coachMonthData.value = response.data;
      } else {
        message.error(response.msg || '加载教练月视图数据失败');
      }
    }
  } catch (error) {
    console.error('加载教练视图数据失败:', error);
    message.error('加载数据失败');
  } finally {
    coachViewLoading.value = false;
  }
}

function onCoachViewChange() {
  loadCoachViewData();
}

function onCoachDateChange() {
  loadCoachViewData();
}

function getDateFormat() {
  switch (coachViewType.value) {
    case 'day':
      return 'YYYY-MM-DD';
    case 'week':
      return 'YYYY-MM-DD';
    case 'month':
      return 'YYYY-MM';
    default:
      return 'YYYY-MM-DD';
  }
}

function getDatePicker() {
  switch (coachViewType.value) {
    case 'month':
      return 'month';
    default:
      return undefined;
  }
}

function onCoachSlotClick({ coachId, slot }) {
  // 点击教练日视图时段
  console.log('点击教练时段:', coachId, slot);
}

function onCreateScheduleFromSlot({ coachId, hourSlot }) {
  // 从教练视图创建排课
  const dateTime = `${coachQueryDate.value.format('YYYY-MM-DD')} ${hourSlot}:00`;
  router.push({
    path: '/schedule/add',
    query: {
      coachId: coachId,
      startTime: dateTime
    }
  });
}

function onCoachWeekSlotClick({ coachId, date, period }) {
  // 点击教练周视图时段
  coachQueryDate.value = dayjs(date);
  coachViewType.value = 'day';
  loadCoachViewData();
}

function onCoachMonthDayClick(day) {
  // 点击教练月视图日期
  coachQueryDate.value = dayjs(day.date);
  coachViewType.value = 'day';
  loadCoachViewData();
}
</script>

<style scoped>
.schedule-manage {
  padding: 0;
}

.combined-view-container {
  padding: 16px 0;
}

.coach-view-controls {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 16px;
}

.order-list {
  display: flex;
  flex-direction: column;
}

.ml-16 {
  margin-left: 16px;
}

.smart-table-operate {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.smart-table-operate .ant-btn {
  padding: 0;
  height: auto;
  line-height: 1.2;
}
</style>