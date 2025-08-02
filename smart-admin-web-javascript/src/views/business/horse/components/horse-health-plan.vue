<template>
  <div class="horse-health-plan">
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
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'planType'">
          <a-tag color="blue">{{ text }}</a-tag>
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
        <template v-else-if="column.dataIndex === 'action'">
          <div class="sa-table-action">
            <a-button v-privilege="'club:horse:health:record:add'" type="link" @click="quickCreateRecord(record)">
              快速执行
            </a-button>
            <a-button v-privilege="'club:horse:health:plan:update'" type="link" @click="showPlanModal(false, record)">
              编辑
            </a-button>
            <a-button v-privilege="'club:horse:health:plan:delete'" type="link" danger @click="deletePlan(record)">
              删除
            </a-button>
          </div>
        </template>
      </template>
    </a-table>

    <HorseHealthPlanModal ref="planModalRef" :horse-id="horseId" @reload="loadPlanList" />
    <QuickRecordModal ref="quickRecordModalRef" @reload="loadPlanList" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { horseHealthPlanApi } from '/@/api/business/horse/horse-api';
import { smartSentry } from '/@/lib/smart-sentry';
import HorseHealthPlanModal from './horse-health-plan-modal.vue';
import QuickRecordModal from './quick-record-modal.vue';
import dayjs from 'dayjs';

const props = defineProps({
  horseId: {
    type: [String, Number],
    required: true,
  },
});

const planList = ref([]);
const planLoading = ref(false);
const planModalRef = ref();
const quickRecordModalRef = ref();

const planColumns = [
  {
    title: '计划名称',
    dataIndex: 'planName',
    width: 150,
  },
  {
    title: '计划类型',
    dataIndex: 'planType',
    width: 120,
  },
  {
    title: '周期',
    dataIndex: 'cycleDays',
    width: 100,
  },
  {
    title: '提醒设置',
    dataIndex: 'reminderDays',
    width: 100,
  },
  {
    title: '下次执行',
    dataIndex: 'nextDate',
    width: 120,
  },
  {
    title: '状态',
    dataIndex: 'reminderStatus',
    width: 100,
  },
  {
    title: '计划内容',
    dataIndex: 'planContent',
    ellipsis: true,
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 180,
  },
];

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

function showPlanModal(isCreate, record = {}) {
  planModalRef.value.showModal(isCreate, record);
}

function quickCreateRecord(plan) {
  quickRecordModalRef.value.showModal(plan);
}

function deletePlan(record) {
  Modal.confirm({
    title: '提示',
    content: `确定要删除计划"${record.planName}"吗？`,
    okText: '删除',
    okType: 'danger',
    onOk: async () => {
      try {
        await horseHealthPlanApi.delete(record.id);
        message.success('删除成功');
        loadPlanList();
      } catch (error) {
        smartSentry.captureError(error);
      }
    },
  });
}

onMounted(() => {
  loadPlanList();
});

defineExpose({
  loadPlanList,
});
</script>

<style scoped>
.horse-health-plan {
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
</style>