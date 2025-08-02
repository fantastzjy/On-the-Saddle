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
      <a-descriptions :column="1" bordered size="small">
        <a-descriptions-item label="计划名称">{{ planInfo.planName }}</a-descriptions-item>
        <a-descriptions-item label="计划类型">{{ planInfo.planType }}</a-descriptions-item>
        <a-descriptions-item label="计划内容">{{ planInfo.planContent }}</a-descriptions-item>
      </a-descriptions>
    </div>

    <a-form :model="formState.form" :rules="formRules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" style="margin-top: 20px;">
      <a-form-item label="执行人" name="executorId">
        <a-select v-model:value="formState.form.executorId" placeholder="请选择执行人" showSearch>
          <a-select-option v-for="executor in executorList" :key="executor.employeeId" :value="executor.employeeId">
            {{ executor.actualName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="执行内容" name="content">
        <a-textarea v-model:value="formState.form.content" placeholder="请输入执行详细内容" :rows="3" />
      </a-form-item>

      <a-form-item label="图片" name="imgUrl">
        <a-input v-model:value="formState.form.imgUrl" placeholder="请输入图片URL" />
      </a-form-item>

      <a-form-item label="记录数据" name="recordData">
        <a-textarea v-model:value="formState.form.recordData" placeholder="请输入相关数据（如体温、体重等）" :rows="2" />
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
import { smartSentry } from '/@/lib/smart-sentry';

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
  planName: '',
  planType: '',
  planContent: '',
  cycleDays: 0,
});

const formRules = {
  content: [{ required: true, message: '请输入执行内容' }],
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

    const { content, imgUrl, recordData } = formState.form;
    await horseHealthRecordApi.createFromPlan(planInfo.id, content, imgUrl, recordData);
    
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
}

:deep(.ant-descriptions-item-label) {
  width: 80px;
  background-color: #fafafa;
}
</style>