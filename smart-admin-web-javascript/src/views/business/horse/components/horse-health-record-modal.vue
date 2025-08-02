<template>
  <a-modal
    :title="formState.isCreate ? '新建健康记录' : '编辑健康记录'"
    :open="formState.visible"
    @ok="onSubmit"
    @cancel="onClose"
    :confirmLoading="formState.loading"
    width="600px"
    :destroyOnClose="true"
  >
    <a-form :model="formState.form" :rules="formRules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="关联计划" name="planId">
        <a-select v-model:value="formState.form.planId" placeholder="请选择关联计划" allowClear>
          <a-select-option v-for="plan in planList" :key="plan.id" :value="plan.id">
            {{ plan.planName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="记录类型" name="recordType">
        <a-select v-model:value="formState.form.recordType" placeholder="请选择记录类型">
          <a-select-option value="疫苗">疫苗</a-select-option>
          <a-select-option value="驱虫">驱虫</a-select-option>
          <a-select-option value="修蹄">修蹄</a-select-option>
          <a-select-option value="体检">体检</a-select-option>
          <a-select-option value="治疗">治疗</a-select-option>
          <a-select-option value="其他">其他</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="记录日期" name="recordDate">
        <a-date-picker 
          v-model:value="formState.form.recordDate" 
          placeholder="请选择记录日期" 
          style="width: 100%" 
          showTime
          format="YYYY-MM-DD HH:mm"
        />
      </a-form-item>

      <a-form-item label="执行人" name="executorId">
        <a-select v-model:value="formState.form.executorId" placeholder="请选择执行人" showSearch allowClear>
          <a-select-option v-for="executor in executorList" :key="executor.employeeId" :value="executor.employeeId">
            {{ executor.actualName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="记录内容" name="content">
        <a-textarea v-model:value="formState.form.content" placeholder="请输入记录详细内容" :rows="4" />
      </a-form-item>

      <a-form-item label="图片" name="imgUrl">
        <a-input v-model:value="formState.form.imgUrl" placeholder="请输入图片URL" />
      </a-form-item>

      <a-form-item label="记录数据" name="recordData">
        <a-textarea v-model:value="formState.form.recordData" placeholder="请输入相关数据（如体温、体重等）" :rows="3" />
      </a-form-item>

      <a-form-item label="下次执行日期" name="nextDate">
        <a-date-picker v-model:value="formState.form.nextDate" placeholder="请选择下次执行日期" style="width: 100%" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { horseHealthRecordApi, horseHealthPlanApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const props = defineProps({
  horseId: {
    type: [String, Number],
    required: true,
  },
});

const emit = defineEmits(['reload']);

const formRef = ref();
const planList = ref([]);
const executorList = ref([]);

const formState = reactive({
  visible: false,
  loading: false,
  isCreate: true,
  form: {
    id: undefined,
    horseId: undefined,
    planId: undefined,
    recordType: '',
    recordDate: undefined,
    executorId: undefined,
    content: '',
    imgUrl: '',
    recordData: '',
    nextDate: undefined,
  },
});

const formRules = {
  recordType: [{ required: true, message: '请选择记录类型' }],
  recordDate: [{ required: true, message: '请选择记录日期' }],
  content: [{ required: true, message: '请输入记录内容' }],
};

function showModal(isCreate, rowData = {}) {
  formState.visible = true;
  formState.isCreate = isCreate;
  
  if (isCreate) {
    resetForm();
    formState.form.horseId = props.horseId;
    formState.form.recordDate = dayjs();
  } else {
    Object.assign(formState.form, {
      ...rowData,
      recordDate: rowData.recordDate ? dayjs(rowData.recordDate) : undefined,
      nextDate: rowData.nextDate ? dayjs(rowData.nextDate) : undefined,
    });
  }
}

function resetForm() {
  Object.assign(formState.form, {
    id: undefined,
    horseId: props.horseId,
    planId: undefined,
    recordType: '',
    recordDate: dayjs(),
    executorId: undefined,
    content: '',
    imgUrl: '',
    recordData: '',
    nextDate: undefined,
  });
}

async function onSubmit() {
  try {
    await formRef.value.validate();
    formState.loading = true;

    const params = { ...formState.form };
    if (params.recordDate) {
      params.recordDate = params.recordDate.format('YYYY-MM-DD HH:mm:ss');
    }
    if (params.nextDate) {
      params.nextDate = params.nextDate.format('YYYY-MM-DD HH:mm:ss');
    }

    if (formState.isCreate) {
      await horseHealthRecordApi.create(params);
      message.success('创建成功');
    } else {
      await horseHealthRecordApi.update(params);
      message.success('更新成功');
    }

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

async function loadPlanList() {
  if (!props.horseId) return;
  
  try {
    const res = await horseHealthPlanApi.queryByHorseId(props.horseId);
    planList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
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
  loadPlanList();
  loadExecutorList();
});

defineExpose({
  showModal,
});
</script>