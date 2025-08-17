<!--
  * 课表管理页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="schedule-manage">
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

    <!-- 操作栏和视图切换 -->
    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button-group>
            <a-button 
              :type="viewType === 'table' ? 'primary' : 'default'"
              @click="switchView('table')"
            >
              <template #icon>
                <TableOutlined />
              </template>
              列表视图
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

        <div class="smart-table-setting-block" v-if="viewType === 'table'">
          <TableOperator 
            v-model="columns" 
            :tableId="TABLE_ID_CONST.BUSINESS.SCHEDULE.SCHEDULE" 
            :refresh="ajaxQuery" 
          />
        </div>
      </a-row>

      <!-- 表格视图 -->
      <div v-show="viewType === 'table'">
        <a-table
          :scroll="{ x: 1500 }"
          size="small"
          :dataSource="tableData"
          :columns="columns"
          rowKey="scheduleId"
          :pagination="false"
          :loading="tableLoading"
          :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          bordered
        >
          <template #bodyCell="{ column, record, text }">
            <template v-if="column.dataIndex === 'scheduleNo'">
              <a-button 
                type="link" 
                @click="detail(record.scheduleId)" 
                :disabled="!$privilege('business:schedule:detail')"
              >
                {{ record.scheduleNo }}
              </a-button>
            </template>

            <template v-if="column.dataIndex === 'startTime'">
              {{ formatDateTime(text) }}
            </template>

            <template v-if="column.dataIndex === 'duration'">
              {{ text }}分钟
            </template>

            <template v-if="column.dataIndex === 'lessonStatus'">
              <a-tag :color="getLessonStatusColor(text)">
                {{ getLessonStatusDesc(text) }}
              </a-tag>
            </template>

            <template v-if="column.dataIndex === 'action'">
              <div class="smart-table-operate">
                <a-button 
                  @click="detail(record.scheduleId)" 
                  v-privilege="'business:schedule:detail'" 
                  size="small" 
                  type="link"
                >
                  详情
                </a-button>
                <a-button 
                  @click="edit(record)" 
                  v-privilege="'business:schedule:update'" 
                  size="small" 
                  type="link"
                >
                  编辑
                </a-button>
                <a-button 
                  @click="checkConflict(record)" 
                  v-privilege="'business:schedule:conflict'" 
                  size="small" 
                  type="link"
                >
                  冲突检测
                </a-button>
                <a-button 
                  @click="cancelLesson(record)" 
                  v-privilege="'business:schedule:cancel'" 
                  size="small" 
                  type="link"
                  v-if="record.lessonStatus === LESSON_STATUS_ENUM.PENDING.value"
                  danger
                >
                  取消课程
                </a-button>
                <a-button 
                  @click="remove(record.scheduleId)" 
                  v-privilege="'business:schedule:delete'" 
                  size="small" 
                  type="link" 
                  danger
                >
                  删除
                </a-button>
              </div>
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
            @change="ajaxQuery"
            @showSizeChange="ajaxQuery"
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
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { 
  SearchOutlined, 
  ReloadOutlined, 
  PlusOutlined,
  TableOutlined,
  CalendarOutlined,
  DragOutlined
} from '@ant-design/icons-vue';
import { useRouter } from 'vue-router';
import { scheduleApi } from '/@/api/business/schedule/schedule-api';
import { 
  LESSON_STATUS_ENUM,
  SCHEDULE_TABLE_COLUMNS, 
  SCHEDULE_SEARCH_FORM,
  SCHEDULE_VIEW_TYPE_ENUM
} from '/@/constants/business/schedule/schedule-const';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
import TableOperator from '/@/components/support/table-operator/index.vue';
import CalendarView from './components/CalendarView.vue';
import DragScheduleModal from './components/DragScheduleModal.vue';
import ConflictDetectorModal from './components/ConflictDetectorModal.vue';
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
const columns = ref([...SCHEDULE_TABLE_COLUMNS]);
const selectedRowKeys = ref([]);
const coachList = ref([]);

// 视图相关
const viewType = ref('table'); // table | calendar
const calendarViewType = ref(SCHEDULE_VIEW_TYPE_ENUM.WEEK.value);

// 模态框相关
const dragScheduleVisible = ref(false);
const conflictDetectorVisible = ref(false);
const currentSchedule = ref(null);

// ======================== 初始化 ========================
onMounted(() => {
  loadCoachList();
  ajaxQuery();
});

// ======================== 查询相关 ========================
function onSearch() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

function resetQuery() {
  Object.assign(queryForm, { ...SCHEDULE_SEARCH_FORM, pageNum: 1, pageSize: queryForm.pageSize });
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    const params = {
      ...queryForm,
      startDate: queryForm.dateRange?.[0],
      endDate: queryForm.dateRange?.[1],
      dateRange: undefined
    };
    
    const response = await scheduleApi.queryScheduleList(params);
    if (response.ok) {
      tableData.value = response.data.list || [];
      total.value = response.data.total || 0;
    } else {
      message.error(response.msg || '查询失败');
    }
  } catch (error) {
    message.error('查询课表列表失败');
    console.error('查询课表列表失败:', error);
  } finally {
    tableLoading.value = false;
  }
}

async function loadCoachList() {
  try {
    const response = await scheduleApi.getCoachList();
    if (response.ok) {
      coachList.value = response.data || [];
    }
  } catch (error) {
    console.error('加载教练列表失败:', error);
  }
}

// ======================== 表格操作 ========================
function onSelectChange(newSelectedRowKeys) {
  selectedRowKeys.value = newSelectedRowKeys;
}

// ======================== 视图切换 ========================
function switchView(type) {
  viewType.value = type;
  if (type === 'calendar') {
    // 日历视图需要重新查询当月数据
    ajaxQuery();
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
        const response = await scheduleApi.deleteSchedule(scheduleId);
        if (response.ok) {
          message.success('删除成功');
          ajaxQuery();
        } else {
          message.error(response.msg || '删除失败');
        }
      } catch (error) {
        message.error('删除课单失败');
        console.error('删除课单失败:', error);
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
        const response = await scheduleApi.cancelLesson(record.scheduleId);
        if (response.ok) {
          message.success('取消成功');
          ajaxQuery();
        } else {
          message.error(response.msg || '取消失败');
        }
      } catch (error) {
        message.error('取消课程失败');
        console.error('取消课程失败:', error);
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
  ajaxQuery();
}

function onScheduleDrag(dragData) {
  // 处理日历视图中的拖拽事件
  handleScheduleDrag(dragData);
}

async function handleScheduleDrag(dragData) {
  try {
    const response = await scheduleApi.updateScheduleTime({
      scheduleId: dragData.scheduleId,
      startTime: dragData.newStartTime,
      endTime: dragData.newEndTime
    });
    
    if (response.ok) {
      message.success('课程时间更新成功');
      ajaxQuery();
    } else {
      message.error(response.msg || '更新失败');
    }
  } catch (error) {
    message.error('更新课程时间失败');
    console.error('更新课程时间失败:', error);
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
function getLessonStatusDesc(value) {
  return Object.values(LESSON_STATUS_ENUM).find(item => item.value === value)?.desc || '-';
}

function getLessonStatusColor(value) {
  return Object.values(LESSON_STATUS_ENUM).find(item => item.value === value)?.color || 'default';
}

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
</script>

<style scoped>
.schedule-manage {
  padding: 0;
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