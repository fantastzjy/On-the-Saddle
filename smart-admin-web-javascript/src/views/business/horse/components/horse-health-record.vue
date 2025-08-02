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

    <div class="sa-filter-bar">
      <a-row :gutter="16">
        <a-col :span="6">
          <a-select v-model:value="filterForm.recordType" placeholder="记录类型" allowClear @change="loadRecordList">
            <a-select-option value="疫苗">疫苗</a-select-option>
            <a-select-option value="驱虫">驱虫</a-select-option>
            <a-select-option value="修蹄">修蹄</a-select-option>
            <a-select-option value="体检">体检</a-select-option>
            <a-select-option value="治疗">治疗</a-select-option>
            <a-select-option value="其他">其他</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="8">
          <a-range-picker v-model:value="filterForm.dateRange" @change="loadRecordList" />
        </a-col>
        <a-col :span="6">
          <a-select v-model:value="filterForm.executorId" placeholder="执行人" allowClear @change="loadRecordList" showSearch>
            <a-select-option v-for="executor in executorList" :key="executor.employeeId" :value="executor.employeeId">
              {{ executor.actualName }}
            </a-select-option>
          </a-select>
        </a-col>
      </a-row>
    </div>

    <a-timeline mode="left" class="health-record-timeline">
      <a-timeline-item v-for="record in recordList" :key="record.id" :color="getTimelineColor(record.recordType)">
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
              <pre>{{ record.recordData }}</pre>
            </div>
          </div>
          <div class="timeline-footer">
            <span class="timeline-executor">执行人：{{ record.executorName || '-' }}</span>
            <span v-if="record.nextDate" class="timeline-next">下次：{{ dayjs(record.nextDate).format('YYYY-MM-DD') }}</span>
          </div>
        </div>
      </a-timeline-item>
    </a-timeline>

    <div v-if="recordList.length === 0 && !recordLoading" class="empty-state">
      <a-empty description="暂无健康记录" />
    </div>

    <HorseHealthRecordModal ref="recordModalRef" :horse-id="horseId" @reload="loadRecordList" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { horseHealthRecordApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';
import HorseHealthRecordModal from './horse-health-record-modal.vue';
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
const executorList = ref([]);

const filterForm = reactive({
  recordType: undefined,
  dateRange: undefined,
  executorId: undefined,
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

.sa-filter-bar {
  margin-bottom: 24px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
}

.health-record-timeline {
  margin-top: 20px;
}

.timeline-date {
  font-size: 14px;
  font-weight: 500;
  text-align: right;
  color: #262626;
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
}

.timeline-actions {
  display: flex;
  gap: 4px;
}

.timeline-body {
  margin-bottom: 12px;
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
  padding: 8px;
  border-radius: 4px;
  margin: 8px 0;
}

.timeline-data pre {
  margin: 0;
  font-size: 12px;
  color: #595959;
}

.timeline-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #8c8c8c;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
}
</style>