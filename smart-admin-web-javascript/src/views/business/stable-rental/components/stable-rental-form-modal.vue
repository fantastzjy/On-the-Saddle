<template>
  <div>
    <a-modal
      v-model:open="visible"
      :title="form.rentalId ? '编辑马房租赁' : '新增马房租赁'"
      :width="800"
      :maskClosable="false"
      @ok="onSubmit"
      @cancel="onCancel"
      :confirmLoading="confirmLoading"
    >
      <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="出租人" name="lessorId">
              <EmployeeSelector
                v-model:value="form.lessorId"
                :lazy="false"
                :auto-load="true"
                placeholder="请选择出租人"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="租赁人" name="lesseeId">
              <CoachSelector
                v-model:value="form.lesseeId"
                :lazy="false"
                :auto-load="true"
                placeholder="请选择租赁人"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="租赁俱乐部" name="targetClubId">
              <ClubSelector
                v-model:value="form.targetClubId"
                :lazy="false"
                :auto-load="true"
                placeholder="请选择租赁俱乐部"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="租赁金额" name="rentalAmount">
              <a-input-number
                v-model:value="form.rentalAmount"
                placeholder="请输入租赁金额"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="租赁开始时间" name="rentalStartTime">
              <a-date-picker
                v-model:value="form.rentalStartTime"
                format="YYYY-MM-DD"
                placeholder="请选择租赁开始日期"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="租赁结束时间" name="rentalEndTime">
              <a-date-picker
                v-model:value="form.rentalEndTime"
                format="YYYY-MM-DD"
                placeholder="请选择租赁结束日期"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12" v-if="form.rentalId">
            <a-form-item label="租赁状态" name="rentalStatus">
              <a-select v-model:value="form.rentalStatus" placeholder="请选择租赁状态">
                <a-select-option :value="1">生效中</a-select-option>
                <a-select-option :value="2">已过期</a-select-option>
                <a-select-option :value="3">已取消</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" name="remark" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
              <a-textarea
                v-model:value="form.remark"
                placeholder="请输入备注"
                :rows="3"
                :maxlength="500"
                show-count
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { stableRentalApi } from '/@/api/business/stable-rental-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { message } from 'ant-design-vue';
import { EmployeeSelector, CoachSelector, ClubSelector } from '/@/components/business/selector/index.js';
import dayjs from 'dayjs';

const emits = defineEmits(['refresh']);

// 弹窗状态
const visible = ref(false);
const confirmLoading = ref(false);

// 表单引用
const formRef = ref();

// 表单数据
const form = reactive({
  rentalId: null,
  lessorId: null,
  lesseeId: null,
  targetClubId: null,
  rentalStartTime: null,
  rentalEndTime: null,
  rentalAmount: null,
  rentalStatus: 1,
  remark: '',
});

// 表单验证规则
const rules = {
  lessorId: [{ required: true, message: '请选择出租人' }],
  lesseeId: [{ required: true, message: '请选择租赁人' }],
  targetClubId: [{ required: true, message: '请选择租赁俱乐部' }],
  rentalAmount: [{ required: true, message: '请输入租赁金额' }],
  rentalStartTime: [{ required: true, message: '请选择租赁开始时间' }],
  rentalEndTime: [
    { required: true, message: '请选择租赁结束时间' },
    {
      validator: (_, value) => {
        if (value && form.rentalStartTime && dayjs(value).isBefore(form.rentalStartTime)) {
          return Promise.reject(new Error('租赁结束时间不能早于开始时间'));
        }
        return Promise.resolve();
      },
    },
  ],
};

// 显示弹窗
function showModal(record) {
  visible.value = true;
  
  if (record) {
    // 编辑模式
    Object.assign(form, {
      rentalId: record.rentalId,
      lessorId: record.lessorId,
      lesseeId: record.lesseeId,
      targetClubId: record.targetClubId,
      // 只取日期部分，忽略时分秒
      rentalStartTime: record.rentalStartTime ? dayjs(record.rentalStartTime) : null,
      rentalEndTime: record.rentalEndTime ? dayjs(record.rentalEndTime) : null,
      rentalAmount: record.rentalAmount,
      rentalStatus: record.rentalStatus,
      remark: record.remark || '',
    });
  } else {
    // 新增模式
    resetForm();
  }
}

// 重置表单
function resetForm() {
  Object.assign(form, {
    rentalId: null,
    lessorId: null,
    lesseeId: null,
    targetClubId: null,
    rentalStartTime: null,
    rentalEndTime: null,
    rentalAmount: null,
    rentalStatus: 1,
    remark: '',
  });
}

// 提交表单
async function onSubmit() {
  try {
    await formRef.value.validateFields();
    confirmLoading.value = true;

    const params = {
      ...form,
      // 开始时间设置为当天的 00:00:00
      rentalStartTime: form.rentalStartTime ? dayjs(form.rentalStartTime).startOf('day').format('YYYY-MM-DD HH:mm:ss') : null,
      // 结束时间设置为当天的 23:59:59
      rentalEndTime: form.rentalEndTime ? dayjs(form.rentalEndTime).endOf('day').format('YYYY-MM-DD HH:mm:ss') : null,
    };

    if (form.rentalId) {
      await stableRentalApi.update(params);
      message.success('更新成功');
    } else {
      await stableRentalApi.create(params);
      message.success('创建成功');
    }

    visible.value = false;
    emits('refresh');
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    confirmLoading.value = false;
  }
}

// 取消
function onCancel() {
  visible.value = false;
  resetForm();
  formRef.value?.resetFields();
}

// 暴露方法
defineExpose({
  showModal,
});
</script>