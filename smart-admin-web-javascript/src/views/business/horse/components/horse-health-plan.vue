<template>
  <div class="horse-health-plan-container">
    <a-row :gutter="24">
      <!-- 左侧：健康计划 -->
      <a-col :span="12">
        <div class="health-plan-panel">
          <div class="sa-table-header">
            <div class="sa-table-header-left">
              <h4>健康计划</h4>
            </div>
            <div class="sa-table-header-right">
              <a-button v-privilege="'club:horse:health:plan:add'" type="primary" @click="showPlanModal(true)">
                <template #icon>
                  <PlusOutlined />
                </template>
                新建计划
              </a-button>
            </div>
          </div>

          <a-table
            size="small"
            :dataSource="planList"
            :columns="planColumns"
            rowKey="id"
            :loading="planLoading"
            :pagination="false"
            bordered
            :customRow="(record) => ({
              onClick: () => onPlanRowClick(record),
              style: {
                cursor: 'pointer',
                backgroundColor: selectedPlanId === record.id ? '#e6f7ff' : '',
                transition: 'background-color 0.2s'
              }
            })"
          >
            <template #bodyCell="{ text, record, column }">
              <template v-if="column.dataIndex === 'planType'">
                <a-tag :color="getPlanTypeColor(text)">{{ getPlanTypeDesc(text) }}</a-tag>
                <span v-if="selectedPlanId === record.id" style="margin-left: 8px;">
                  <CheckOutlined style="color: #1890ff;" />
                </span>
              </template>
              <template v-else-if="column.dataIndex === 'cycleDays'">
                {{ text > 0 ? `${text}天` : '不重复' }}
              </template>
              <template v-else-if="column.dataIndex === 'reminderDays'">
                {{ text > 0 ? `提前${text}天` : '不提醒' }}
              </template>
              <template v-else-if="column.dataIndex === 'nextDate'">
                {{ text ? dayjs(text).format('YYYY-MM-DD') : '-' }}
              </template>
              <template v-else-if="column.dataIndex === 'reminderStatus'">
                <a-tag v-if="text === 'overdue'" color="red">已逾期</a-tag>
                <a-tag v-else-if="text === 'reminder'" color="orange">需要提醒</a-tag>
                <a-tag v-else-if="text === 'normal'" color="green">正常</a-tag>
              </template>
              <template v-else-if="column.dataIndex === 'recordCount'">
                <div class="record-countdown">
                  <div class="countdown-text">
                    <span v-if="getCountdownDays(record) > 0" class="countdown-days">
                      还有{{ getCountdownDays(record) }}天
                    </span>
                    <span v-else-if="getCountdownDays(record) === 0" class="countdown-today">
                      今天执行
                    </span>
                    <span v-else class="countdown-overdue">
                      逾期{{ Math.abs(getCountdownDays(record)) }}天
                    </span>
                  </div>
                  <div class="execution-count">
                    (已执行{{ getPlanRecordCount(record.id) }}次)
                  </div>
                </div>
              </template>
              <template v-else-if="column.dataIndex === 'action'">
                <div class="smart-table-operate">
                  <a-button v-privilege="'club:horse:health:record:add'" type="link" @click.stop="quickCreateRecord(record)">
                    快速执行
                  </a-button>
                  <a-button v-privilege="'club:horse:health:plan:update'" type="link" @click.stop="showPlanModal(false, record)">
                    编辑
                  </a-button>
                  <a-button v-privilege="'club:horse:health:plan:delete'" type="link" danger @click.stop="deletePlan(record)">
                    删除
                  </a-button>
                </div>
              </template>
            </template>
          </a-table>
        </div>
      </a-col>

      <!-- 右侧：健康记录 -->
      <a-col :span="12">
        <div class="health-record-panel">
          <!-- 记录头部 -->
          <div class="record-header">
            <div class="record-stats">
              <a-row :gutter="16">
                <a-col :span="6">
                  <a-statistic title="当前显示" :value="filteredRecordList.length" suffix="条" />
                </a-col>
                <a-col :span="6">
                  <a-statistic title="总记录" :value="recordList.length" suffix="条" />
                </a-col>
                <a-col :span="6">
                  <a-statistic title="本月新增" :value="getMonthRecordCount()" suffix="条" />
                </a-col>
                <a-col :span="6">
                  <a-button v-privilege="'club:horse:health:record:add'" type="primary" @click="showRecordModal(true)">
                    <template #icon>
                      <PlusOutlined />
                    </template>
                    新建记录
                  </a-button>
                </a-col>
              </a-row>
            </div>
          </div>

          <!-- 快速筛选 -->
          <div class="quick-filter-bar">
            <a-space>
              <span>快速筛选：</span>
              <a-tag
                  :color="!filterForm.planType ? 'blue' : 'default'"
                  style="cursor: pointer;"
                  @click="filterByType(null)"
              >
                全部 ({{ recordList.length }})
              </a-tag>
              <a-tag
                v-for="type in recordTypes"
                :key="type.value"
                :color="filterForm.planType === type.value ? type.color : 'default'"
                style="cursor: pointer;"
                @click="filterByType(type.value)"
              >
                {{ type.desc }} ({{ getRecordCountByType(type.value) }})
              </a-tag>
            </a-space>
          </div>

          <!-- 筛选栏 -->
          <div class="sa-filter-bar">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-range-picker
                  v-model:value="filterForm.dateRange"
                  placeholder="选择日期范围"
                  @change="applyFilters"
                />
              </a-col>
              <a-col :span="8">
                <a-select
                  v-model:value="filterForm.executorId"
                  placeholder="执行人"
                  allowClear
                  @change="applyFilters"
                  showSearch
                >
                  <a-select-option v-for="executor in executorList" :key="executor.employeeId" :value="executor.employeeId">
                    {{ executor.actualName }}
                  </a-select-option>
                </a-select>
              </a-col>
              <a-col :span="8">
                <a-button @click="resetFilters">重置筛选</a-button>
              </a-col>
            </a-row>
          </div>

          <!-- 记录时间轴 -->
          <div class="record-timeline-container">
            <a-empty v-if="filteredRecordList.length === 0 && !recordLoading"
              :description="selectedPlanId ? '该计划暂无相关记录' : '暂无健康记录'" />

            <a-timeline v-else class="health-record-timeline">
              <a-timeline-item v-for="record in filteredRecordList" :key="record.id" :color="getPlanTypeColor(record.planType)">
                <div class="timeline-content">
                  <div class="timeline-header">
                    <div class="timeline-date-time">
                      <div class="timeline-date">{{ dayjs(record.recordDate).format('MM-DD') }}</div>
                      <div class="timeline-time">{{ dayjs(record.recordDate).format('HH:mm') }}</div>
                    </div>
                    <a-tag :color="getPlanTypeColor(record.planType)">{{ getPlanTypeDesc(record.planType) }}</a-tag>
                    <div class="timeline-actions">
                      <a-button v-privilege="'club:horse:health:record:update'" type="link" size="small" @click="showRecordModal(false, record)">
                        编辑
                      </a-button>
                      <a-button v-privilege="'club:horse:health:record:delete'" type="link" size="small" danger @click="deleteRecord(record)">
                        删除
                      </a-button>
                    </div>
                  </div>
                  <div class="timeline-body">
                    <p v-if="record.content">{{ record.content }}</p>
                    <div v-if="record.imgUrl" class="timeline-images">
                      <a-image-preview-group>
                        <a-image
                          v-for="(imageUrl, index) in getImageUrls(record.imgUrl)"
                          :key="index"
                          :src="imageUrl"
                          :width="100"
                          :height="100"
                          :style="{ margin: '4px', borderRadius: '6px', objectFit: 'cover' }"
                          :preview="{ mask: '点击放大' }"
                        />
                      </a-image-preview-group>
                    </div>
                    <div v-if="record.recordData" class="timeline-data">
                      <pre>{{ formatRecordData(record.recordData) }}</pre>
                    </div>
                  </div>
                  <div class="timeline-footer">
                    <span class="timeline-executor">执行人：{{ record.executorName || '-' }}</span>
                    <span v-if="record.nextDate" class="timeline-next">下次：{{ dayjs(record.nextDate).format('YYYY-MM-DD') }}</span>
                  </div>
                </div>
              </a-timeline-item>
            </a-timeline>
          </div>
        </div>
      </a-col>
    </a-row>

    <!-- 模态框 -->
    <HorseHealthPlanModal ref="planModalRef" :horse-id="horseId" @reload="refreshAll" />
    <HorseHealthRecordModal ref="recordModalRef" :horse-id="horseId" :selected-plan-id="selectedPlanId" @reload="refreshAll" />
    <QuickRecordModal ref="quickRecordModalRef" :horse-id="horseId" @reload="refreshAll" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import {
  PlusOutlined,
  CheckOutlined
} from '@ant-design/icons-vue';
import { horseHealthPlanApi, horseHealthRecordApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { HEALTH_PLAN_TYPE_ENUM, getPlanTypeDesc, getPlanTypeColor } from '/@/constants/business/horse/health-const';
import HorseHealthPlanModal from './horse-health-plan-modal.vue';
import HorseHealthRecordModal from './horse-health-record-modal.vue';
import QuickRecordModal from './quick-record-modal.vue';
import dayjs from 'dayjs';

const props = defineProps({
  horseId: {
    type: [String, Number],
    required: true,
  },
});

// 状态管理
const planList = ref([]);
const recordList = ref([]);
const planLoading = ref(false);
const recordLoading = ref(false);
const selectedPlanId = ref(null);
const executorList = ref([]);

// 模态框引用
const planModalRef = ref();
const recordModalRef = ref();
const quickRecordModalRef = ref();

// 筛选表单
const filterForm = reactive({
  planType: undefined,
  dateRange: undefined,
  executorId: undefined,
});

// 记录类型配置（基于常量）
const recordTypes = Object.values(HEALTH_PLAN_TYPE_ENUM);

// 计划表格列配置
const planColumns = [
  {
    title: '计划类型',
    dataIndex: 'planType',
    width: 120,
  },
  {
    title: '周期',
    dataIndex: 'cycleDays',
    width: 80,
  },
  {
    title: '提醒设置',
    dataIndex: 'reminderDays',
    width: 100,
  },
  {
    title: '下次执行',
    dataIndex: 'nextDate',
    width: 110,
  },
  {
    title: '状态',
    dataIndex: 'reminderStatus',
    width: 80,
  },
  {
    title: '执行情况',
    dataIndex: 'recordCount',
    width: 100,
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 180,
  },
];

// 过滤后的记录列表
const filteredRecordList = computed(() => {
  let filtered = [...recordList.value];

  // 根据选中的计划过滤
  if (selectedPlanId.value) {
    filtered = filtered.filter(record => record.planId === selectedPlanId.value);
  }

  // 按记录类型过滤
  if (filterForm.planType) {
    filtered = filtered.filter(record => record.planType === filterForm.planType);
  }

  // 按日期范围过滤
  if (filterForm.dateRange && filterForm.dateRange.length === 2) {
    const [startDate, endDate] = filterForm.dateRange;
    filtered = filtered.filter(record => {
      const recordDate = dayjs(record.recordDate);
      return recordDate.isAfter(startDate) && recordDate.isBefore(endDate.add(1, 'day'));
    });
  }

  // 按执行人过滤
  if (filterForm.executorId) {
    filtered = filtered.filter(record => record.executorId === filterForm.executorId);
  }

  // 按时间降序排列
  return filtered.sort((a, b) => dayjs(b.recordDate).valueOf() - dayjs(a.recordDate).valueOf());
});

// 加载健康计划列表
async function loadPlanList() {
  if (!props.horseId) return;

  try {
    planLoading.value = true;
    const res = await horseHealthPlanApi.queryByHorseId(props.horseId);
    planList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    planLoading.value = false;
  }
}

// 加载健康记录列表
async function loadRecordList() {
  if (!props.horseId) return;

  try {
    recordLoading.value = true;
    const res = await horseHealthRecordApi.queryByHorseId(props.horseId);
    recordList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    recordLoading.value = false;
  }
}

// 加载执行人列表
async function loadExecutorList() {
  try {
    const res = await employeeApi.queryAll();
    executorList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  }
}

// 统一刷新方法
const refreshAll = () => {
  loadPlanList();
  loadRecordList();
};

// 计划行点击处理
const onPlanRowClick = (plan) => {
  if (selectedPlanId.value === plan.id) {
    selectedPlanId.value = null; // 取消选择，显示全部
    filterForm.planType = undefined; // 清除类型筛选
  } else {
    selectedPlanId.value = plan.id; // 选择该计划
    filterForm.planType = plan.planType; // 同步设置类型筛选
  }
};

// 清除选择
const clearSelection = () => {
  selectedPlanId.value = null;
};

// 获取选中计划的名称
const getSelectedPlanName = () => {
  if (!selectedPlanId.value) return '';
  const plan = planList.value.find(p => p.id === selectedPlanId.value);
  return plan ? getPlanTypeDesc(plan.planType) : '';
};

// 解析图片URL数组
function getImageUrls(imgUrlString) {
  if (!imgUrlString) return [];
  return imgUrlString.split(',').filter(url => url.trim()).map(url => url.trim());
}

// 获取计划的记录数量
const getPlanRecordCount = (planId) => {
  return recordList.value.filter(r => r.planId === planId).length;
};

// 计算距离下次执行的天数
const getCountdownDays = (plan) => {
  if (!plan.nextDate) return null;
  const today = dayjs().startOf('day');
  const nextDate = dayjs(plan.nextDate).startOf('day');
  return nextDate.diff(today, 'day');
};

// 获取本月记录数量
const getMonthRecordCount = () => {
  const currentMonth = dayjs().format('YYYY-MM');
  return recordList.value.filter(record =>
    dayjs(record.recordDate).format('YYYY-MM') === currentMonth
  ).length;
};

// 显示计划模态框
function showPlanModal(isCreate, record = {}) {
  planModalRef.value.showModal(isCreate, record);
}

// 显示记录模态框
function showRecordModal(isCreate, record = {}) {
  recordModalRef.value.showModal(isCreate, record);
}

// 快速创建记录
function quickCreateRecord(plan) {
  if (quickRecordModalRef.value) {
    quickRecordModalRef.value.showModal(plan.planType, plan.id);
  } else {
    showRecordModal(true, {
      planType: plan.planType,
      planId: plan.id
    });
  }
}

// 删除计划
function deletePlan(record) {
  Modal.confirm({
    title: '提示',
    content: `确定要删除计划"${record.planType}"吗？`,
    okText: '删除',
    okType: 'danger',
    onOk: async () => {
      try {
        await horseHealthPlanApi.delete(record.id);
        message.success('删除成功');
        if (selectedPlanId.value === record.id) {
          selectedPlanId.value = null;
        }
        refreshAll();
      } catch (error) {
        smartSentry.captureError(error);
      }
    },
  });
}

// 删除记录
function deleteRecord(record) {
  Modal.confirm({
    title: '提示',
    content: `确定要删除该健康记录吗？`,
    okText: '删除',
    okType: 'danger',
    onOk: async () => {
      try {
        await horseHealthRecordApi.delete(record.id);
        message.success('删除成功');
        loadRecordList();
      } catch (error) {
        smartSentry.captureError(error);
      }
    },
  });
}

// 获取指定类型的记录数量
function getRecordCountByType(type) {
  return recordList.value.filter(record => record.planType === type).length;
}

// 按类型筛选
function filterByType(type) {
  filterForm.planType = type;
  selectedPlanId.value = null; // 清除计划选择，因为现在按类型筛选
}

// 应用筛选
function applyFilters() {
  // 筛选逻辑已在计算属性中处理
}

// 重置筛选
function resetFilters() {
  filterForm.planType = undefined;
  filterForm.dateRange = undefined;
  filterForm.executorId = undefined;
  selectedPlanId.value = null; // 同时清除计划选择
}

// 格式化记录数据
function formatRecordData(data) {
  if (!data) return '';
  try {
    const parsed = typeof data === 'string' ? JSON.parse(data) : data;
    return JSON.stringify(parsed, null, 2);
  } catch {
    return data;
  }
}

onMounted(() => {
  loadPlanList();
  loadRecordList();
  loadExecutorList();
});

defineExpose({
  loadPlanList,
  loadRecordList,
  refreshAll,
});
</script>

<style scoped>
.horse-health-plan-container {
  padding: 16px 0;
}

/* 左侧计划面板 */
.health-plan-panel {
  height: 100%;
}

.sa-table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.sa-table-header h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

/* 右侧记录面板 */
.health-record-panel {
  height: 100%;
}

.record-header {
  margin-bottom: 16px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
}

.quick-filter-bar {
  margin-bottom: 16px;
  padding: 12px 16px;
  background: #f0f2f5;
  border-radius: 6px;
  border-left: 3px solid #1890ff;
}

.sa-filter-bar {
  margin-bottom: 24px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
}

.record-timeline-container {
  max-height: 600px;
  overflow-y: auto;
}

.health-record-timeline {
  margin-top: 20px;
}

.timeline-date {
  font-size: 14px;
  font-weight: 500;
  color: #262626;
}

.timeline-time {
  font-size: 12px;
  color: #8c8c8c;
  margin-top: 2px;
}

.timeline-date-time {
  margin-right: 12px;
  text-align: center;
  min-width: 48px;
}

.timeline-content {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.2s;
}

.timeline-content:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.timeline-header {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-bottom: 12px;
}

.timeline-title {
  font-weight: 500;
  flex: 1;
  color: #262626;
  margin-left: 4px;
}

.timeline-actions {
  display: flex;
  gap: 4px;
}

.timeline-body {
  margin-bottom: 12px;
  line-height: 1.6;
}

.timeline-body p {
  margin-bottom: 8px;
  color: #595959;
}

.timeline-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin: 8px 0;
}

.timeline-image {
  margin: 8px 0;
}

.timeline-data {
  background: #f5f5f5;
  padding: 8px 12px;
  border-radius: 4px;
  margin: 8px 0;
  border-left: 3px solid #1890ff;
}

.timeline-data pre {
  margin: 0;
  font-size: 12px;
  color: #595959;
  white-space: pre-wrap;
  word-break: break-all;
}

.timeline-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #8c8c8c;
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
}

.timeline-executor {
  font-weight: 500;
}

.timeline-next {
  color: #1890ff;
  font-weight: 500;
}

/* 执行情况样式 */
.record-countdown {
  text-align: center;
  font-size: 12px;
}

.countdown-text {
  font-weight: 500;
  margin-bottom: 2px;
}

.countdown-days {
  color: #1890ff;
}

.countdown-today {
  color: #faad14;
  font-weight: 600;
}

.countdown-overdue {
  color: #ff4d4f;
  font-weight: 600;
}

.execution-count {
  color: #8c8c8c;
  font-size: 11px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .horse-health-plan-container .ant-col:first-child {
    margin-bottom: 24px;
  }
}

/* 滚动条样式 */
.record-timeline-container::-webkit-scrollbar {
  width: 4px;
}

.record-timeline-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.record-timeline-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.record-timeline-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
