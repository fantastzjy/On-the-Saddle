<template>
  <a-modal
    title="快速执行计划"
    :open="formState.visible"
    @ok="onSubmit"
    @cancel="onClose"
    :confirmLoading="formState.loading"
    width="500px"
    :destroyOnClose="true"
  >
    <div class="quick-record-info">
      <div class="plan-info-item">
        <span class="info-label">计划类型：</span>
        <span class="info-value">{{ getPlanTypeDesc(planInfo.planType) }}</span>
      </div>
      <div class="plan-info-item">
        <span class="info-label">周期设置：</span>
        <span class="info-value">{{ planInfo.cycleDays > 0 ? `${planInfo.cycleDays}天` : '不重复' }}</span>
      </div>
      <div class="plan-info-item" v-if="planInfo.nextDate">
        <span class="info-label">下次执行：</span>
        <span class="info-value">{{ planInfo.nextDate }}</span>
      </div>
    </div>

    <a-form :model="formState.form" :rules="formRules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" style="margin-top: 20px;">
      <a-form-item label="执行人" name="executorId">
        <a-select v-model:value="formState.form.executorId" placeholder="请选择执行人" showSearch allowClear>
          <a-select-option v-for="executor in executorList" :key="executor.employeeId" :value="executor.employeeId">
            {{ executor.actualName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="执行内容" name="content">
        <a-textarea v-model:value="formState.form.content" placeholder="请输入执行详细内容" :rows="3" />
      </a-form-item>

      <a-form-item label="记录数据" name="recordData">
        <a-textarea v-model:value="formState.form.recordData" placeholder="请输入相关数据（如体温、体重等）" :rows="2" />
      </a-form-item>

      <a-form-item label="图片" name="imgUrl">
        <a-input v-model:value="formState.form.imgUrl" placeholder="可选：图片URL地址" />
      </a-form-item>
    </a-form>

    <a-alert 
      v-if="planInfo.cycleDays && planInfo.cycleDays > 0"
      :message="`该计划为周期性计划，周期为${planInfo.cycleDays}天，执行后将自动计算下次执行时间`"
      type="info"
      showIcon
      style="margin-top: 16px;"
    />
  </a-modal>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { horseHealthRecordApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { getPlanTypeDesc } from '/@/constants/business/horse/health-const';
import { smartSentry } from '/@/lib/smart-sentry';

const props = defineProps({
  horseId: {
    type: [String, Number],
    required: true,
  },
});

const emit = defineEmits(['reload']);

const formRef = ref();
const executorList = ref([]);

const formState = reactive({
  visible: false,
  loading: false,
  form: {
    executorId: undefined,
    content: '',
    imgUrl: '',
    recordData: '',
  },
});

const planInfo = reactive({
  id: undefined,
  planType: '',
  cycleDays: 0,
  nextDate: '',
});

const formRules = {
  content: [{ required: true, message: '请输入执行内容' }],
  executorId: [{ required: true, message: '请选择执行人' }],
};

function showModal(plan) {
  formState.visible = true;
  Object.assign(planInfo, plan);
  resetForm();
}

function resetForm() {
  Object.assign(formState.form, {
    executorId: undefined,
    content: '',
    imgUrl: '',
    recordData: '',
  });
}

async function onSubmit() {
  try {
    await formRef.value.validate();
    formState.loading = true;

    const params = {
      horseId: props.horseId,
      planId: planInfo.id,
      planType: planInfo.planType,
      executorId: formState.form.executorId,
      recordDate: new Date().toISOString(),
      content: formState.form.content,
      imgUrl: formState.form.imgUrl || '',
      recordData: formState.form.recordData || '',
      nextDate: null // 快速执行时不设置下次日期
    };
    
    await horseHealthRecordApi.create(params);
    
    message.success('执行成功');
    onClose();
    emit('reload');
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    formState.loading = false;
  }
}

function onClose() {
  formState.visible = false;
  resetForm();
  formRef.value?.resetFields();
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
  loadExecutorList();
});

defineExpose({
  showModal,
});
</script>

<style scoped>
.quick-record-info {
  margin-bottom: 16px;
  padding: 12px 16px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
}

.plan-info-item {
  display: flex;
  margin-bottom: 8px;
}

.plan-info-item:last-child {
  margin-bottom: 0;
}

.info-label {
  width: 80px;
  text-align: right;
  margin-right: 12px;
  color: #666;
  font-weight: 500;
}

.info-value {
  flex: 1;
  color: #262626;
}

:deep(.ant-form-item-label) {
  text-align: left !important;
}

:deep(.ant-form-item-label > label) {
  text-align: left;
}
</style>