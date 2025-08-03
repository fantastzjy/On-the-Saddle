<template>
  <div class="horse-health-record">
    <div class="sa-table-header">
      <div class="sa-table-header-left">
        <h4>健康记录</h4>
      </div>
      <div class="sa-table-header-right">
        <a-button v-privilege="'club:horse:health:record:add'" type="primary" @click="showRecordModal(true)">
          <template #icon>
            <PlusOutlined />
          </template>
          新建记录
        </a-button>
      </div>
    </div>

    <a-row :gutter="16">
      <!-- 左侧统计面板 -->
      <a-col :span="8">
        <div class="health-stats-panel">
          <!-- 记录统计 -->
          <a-card title="记录统计" size="small" style="margin-bottom: 16px;">
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-number">{{ recordList.length }}</div>
                <div class="stat-label">总记录数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ getRecordCountByType('疫苗') }}</div>
                <div class="stat-label">疫苗记录</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ getRecordCountByType('体检') }}</div>
                <div class="stat-label">体检记录</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ getRecordCountByType('治疗') }}</div>
                <div class="stat-label">治疗记录</div>
              </div>
            </div>
          </a-card>

          <!-- 记录类型分布 -->
          <a-card title="记录类型" size="small" style="margin-bottom: 16px;">
            <div class="type-list">
              <div 
                v-for="type in recordTypes" 
                :key="type.name"
                class="type-item"
                :class="{ active: filterForm.recordType === type.name }"
                @click="filterByType(type.name)"
              >
                <a-tag :color="type.color">{{ type.name }}</a-tag>
                <span class="type-count">{{ getRecordCountByType(type.name) }}</span>
              </div>
              <div 
                class="type-item"
                :class="{ active: !filterForm.recordType }"
                @click="filterByType(null)"
              >
                <a-tag color="default">全部</a-tag>
                <span class="type-count">{{ recordList.length }}</span>
              </div>
            </div>
          </a-card>

          <!-- 快捷操作 -->
          <a-card title="快捷操作" size="small">
            <div class="quick-actions">
              <a-button 
                v-for="action in quickActions" 
                :key="action.type"
                block 
                style="margin-bottom: 8px;"
                @click="quickRecord(action.type)"
              >
                <template #icon>
                  <component :is="action.icon" />
                </template>
                {{ action.name }}
              </a-button>
            </div>
          </a-card>
        </div>
      </a-col>

      <!-- 右侧记录列表 -->
      <a-col :span="16">
        <div class="health-records-panel">
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
          <a-timeline mode="left" class="health-record-timeline">
            <a-timeline-item v-for="record in filteredRecordList" :key="record.id" :color="getTimelineColor(record.recordType)">
              <template #label>
                <div class="timeline-date">
                  {{ dayjs(record.recordDate).format('MM-DD') }}
                  <div class="timeline-time">{{ dayjs(record.recordDate).format('HH:mm') }}</div>
                </div>
              </template>
              <div class="timeline-content">
                <div class="timeline-header">
                  <a-tag :color="getTypeColor(record.recordType)">{{ record.recordType }}</a-tag>
                  <span class="timeline-title">{{ record.planName || '临时记录' }}</span>
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
                  <div v-if="record.imgUrl" class="timeline-image">
                    <a-image :src="record.imgUrl" :width="100" />
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

          <div v-if="filteredRecordList.length === 0 && !recordLoading" class="empty-state">
            <a-empty description="暂无健康记录" />
          </div>
        </div>
      </a-col>
    </a-row>

    <HorseHealthRecordModal ref="recordModalRef" :horse-id="horseId" @reload="loadRecordList" />
    <QuickRecordModal ref="quickRecordModalRef" :horse-id="horseId" @reload="loadRecordList" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { 
  PlusOutlined, 
  MedicineBoxOutlined, 
  HeartOutlined, 
  ToolOutlined,
  BugOutlined,
  ExperimentOutlined
} from '@ant-design/icons-vue';
import { horseHealthRecordApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';
import HorseHealthRecordModal from './horse-health-record-modal.vue';
import QuickRecordModal from './quick-record-modal.vue';
import dayjs from 'dayjs';

const props = defineProps({
  horseId: {
    type: [String, Number],
    required: true,
  },
});

const recordList = ref([]);
const recordLoading = ref(false);
const recordModalRef = ref();
const quickRecordModalRef = ref();
const executorList = ref([]);

const filterForm = reactive({
  recordType: undefined,
  dateRange: undefined,
  executorId: undefined,
});

// 记录类型配置
const recordTypes = [
  { name: '疫苗', color: 'blue' },
  { name: '驱虫', color: 'green' },
  { name: '修蹄', color: 'orange' },
  { name: '体检', color: 'purple' },
  { name: '治疗', color: 'red' },
  { name: '其他', color: 'default' },
];

// 快捷操作配置
const quickActions = [
  { type: '体检', name: '快速体检', icon: 'HeartOutlined' },
  { type: '疫苗', name: '疫苗接种', icon: 'MedicineBoxOutlined' },
  { type: '驱虫', name: '驱虫处理', icon: 'BugOutlined' },
  { type: '修蹄', name: '修蹄护理', icon: 'ToolOutlined' },
  { type: '治疗', name: '治疗记录', icon: 'ExperimentOutlined' },
];

// 过滤后的记录列表
const filteredRecordList = computed(() => {
  let filtered = [...recordList.value];
  
  // 按记录类型过滤
  if (filterForm.recordType) {
    filtered = filtered.filter(record => record.recordType === filterForm.recordType);
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
  
  return filtered;
});

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

function showRecordModal(isCreate, record = {}) {
  recordModalRef.value.showModal(isCreate, record);
}

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

function getTimelineColor(recordType) {
  const colorMap = {
    '疫苗': 'blue',
    '驱虫': 'green',
    '修蹄': 'orange',
    '体检': 'purple',
    '治疗': 'red',
    '其他': 'gray',
  };
  return colorMap[recordType] || 'gray';
}

function getTypeColor(recordType) {
  const colorMap = {
    '疫苗': 'blue',
    '驱虫': 'green',
    '修蹄': 'orange',
    '体检': 'purple',
    '治疗': 'red',
    '其他': 'default',
  };
  return colorMap[recordType] || 'default';
}

// 获取指定类型的记录数量
function getRecordCountByType(type) {
  return recordList.value.filter(record => record.recordType === type).length;
}

// 按类型筛选
function filterByType(type) {
  filterForm.recordType = type;
}

// 应用筛选
function applyFilters() {
  // 筛选逻辑已在计算属性中处理
}

// 重置筛选
function resetFilters() {
  filterForm.recordType = undefined;
  filterForm.dateRange = undefined;
  filterForm.executorId = undefined;
}

// 快速记录
function quickRecord(type) {
  if (quickRecordModalRef.value) {
    quickRecordModalRef.value.showModal(type);
  } else {
    // 如果没有快速记录组件，使用普通记录模态框
    showRecordModal(true, { recordType: type });
  }
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

async function loadExecutorList() {
  try {
    const res = await employeeApi.queryAll();
    executorList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  }
}

onMounted(() => {
  loadRecordList();
  loadExecutorList();
});

defineExpose({
  loadRecordList,
});
</script>

<style scoped>
.horse-health-record {
  padding: 16px 0;
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

/* 左侧统计面板 */
.health-stats-panel {
  height: 100%;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #8c8c8c;
}

/* 记录类型列表 */
.type-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.type-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.type-item:hover {
  background-color: #f5f5f5;
}

.type-item.active {
  background-color: #e6f7ff;
  border: 1px solid #91d5ff;
}

.type-count {
  font-size: 12px;
  color: #8c8c8c;
  font-weight: 500;
}

/* 快捷操作 */
.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

/* 右侧记录面板 */
.health-records-panel {
  height: 100%;
}

.sa-filter-bar {
  margin-bottom: 24px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
}

.health-record-timeline {
  margin-top: 20px;
  max-height: 600px;
  overflow-y: auto;
}

.timeline-date {
  font-size: 14px;
  font-weight: 500;
  text-align: right;
  color: #262626;
  min-width: 60px;
}

.timeline-time {
  font-size: 12px;
  color: #8c8c8c;
  margin-top: 2px;
}

.timeline-content {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  padding: 16px;
  margin-left: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.2s;
}

.timeline-content:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.timeline-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.timeline-title {
  font-weight: 500;
  flex: 1;
  color: #262626;
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

.empty-state {
  text-align: center;
  padding: 40px 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: 1fr;
    gap: 8px;
  }
  
  .stat-item {
    padding: 8px;
  }
  
  .stat-number {
    font-size: 20px;
  }
}

/* 滚动条样式 */
.health-record-timeline::-webkit-scrollbar {
  width: 4px;
}

.health-record-timeline::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.health-record-timeline::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.health-record-timeline::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>