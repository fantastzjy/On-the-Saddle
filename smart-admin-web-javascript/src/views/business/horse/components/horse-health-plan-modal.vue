<template>
  <a-modal
    :title="formState.isCreate ? '新建健康计划' : '编辑健康计划'"
    :open="formState.visible"
    @ok="onSubmit"
    @cancel="onClose"
    :confirmLoading="formState.loading"
    width="600px"
    :destroyOnClose="true"
  >
    <a-form :model="formState.form" :rules="formRules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="计划类型" name="planType">
        <a-select v-model:value="formState.form.planType" placeholder="请选择计划类型">
          <a-select-option v-for="type in planTypeOptions" :key="type.value" :value="type.value">
            {{ type.desc }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="周期设置" name="cycleDays">
        <a-input-number
          v-model:value="formState.form.cycleDays"
          placeholder="执行周期天数，0表示不重复"
          :min="0"
          :max="365"
          style="width: 100%"
          addonAfter="天"
        />
      </a-form-item>

      <a-form-item label="提醒设置" name="reminderDays">
        <a-input-number
          v-model:value="formState.form.reminderDays"
          placeholder="提前提醒天数，0表示不提醒"
          :min="0"
          :max="30"
          style="width: 100%"
          addonAfter="天"
        />
      </a-form-item>

      <a-form-item label="下次执行日期" name="nextDate">
        <a-date-picker v-model:value="formState.form.nextDate" placeholder="请选择下次执行日期" style="width: 100%" />
      </a-form-item>

      <a-form-item label="计划内容" name="planContent">
        <a-textarea v-model:value="formState.form.planContent" placeholder="请输入计划详细内容" :rows="4" />
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="formState.form.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { horseHealthPlanApi } from '/@/api/business/horse/horse-api';
import { HEALTH_PLAN_TYPE_ENUM } from '/@/constants/business/horse/health-const';
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

// 计划类型选项
const planTypeOptions = Object.values(HEALTH_PLAN_TYPE_ENUM);

const formState = reactive({
  visible: false,
  loading: false,
  isCreate: true,
  form: {
    id: undefined,
    horseId: undefined,
    planType: '',
    cycleDays: 0,
    reminderDays: 0,
    nextDate: undefined,
    planContent: '',
    remark: '',
  },
});

const formRules = {
  planType: [{ required: true, message: '请选择计划类型' }],
  cycleDays: [{ required: true, message: '请设置执行周期' }],
  reminderDays: [{ required: true, message: '请设置提醒天数' }],
  nextDate: [{ required: true, message: '请选择下次执行日期' }],
};

function showModal(isCreate, rowData = {}) {
  formState.visible = true;
  formState.isCreate = isCreate;
  
  if (isCreate) {
    resetForm();
    formState.form.horseId = props.horseId;
  } else {
    Object.assign(formState.form, {
      ...rowData,
      nextDate: rowData.nextDate ? dayjs(rowData.nextDate) : undefined,
    });
  }
}

function resetForm() {
  Object.assign(formState.form, {
    id: undefined,
    horseId: props.horseId,
    planType: '',
    cycleDays: 0,
    reminderDays: 0,
    nextDate: undefined,
    planContent: '',
    remark: '',
  });
}

async function onSubmit() {
  try {
    await formRef.value.validate();
    formState.loading = true;

    const params = { ...formState.form };
    if (params.nextDate) {
      params.nextDate = params.nextDate.format('YYYY-MM-DD HH:mm:ss');
    }

    if (formState.isCreate) {
      await horseHealthPlanApi.create(params);
      message.success('创建成功');
    } else {
      await horseHealthPlanApi.update(params);
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

defineExpose({
  showModal,
});
</script>