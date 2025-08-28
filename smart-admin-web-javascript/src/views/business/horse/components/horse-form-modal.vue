<template>
  <a-modal
    :title="formState.isCreate ? '新建马匹' : '编辑马匹'"
    :open="formState.visible"
    @ok="onSubmit"
    @cancel="onClose"
    :confirmLoading="formState.loading"
    width="800px"
    :destroyOnClose="true"
  >
    <a-form :model="formState.form" :rules="formRules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-row :gutter="20">
        <!-- <a-col :span="12">
          <a-form-item label="马匹编号" name="horseCode">
            <a-input v-model:value="formState.form.horseCode" placeholder="请输入马匹编号" />
          </a-form-item>
        </a-col> -->
        <a-col :span="12">
          <a-form-item label="马名" name="horseName">
            <a-input v-model:value="formState.form.horseName" placeholder="请输入马名" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="俱乐部" name="clubId">
            <ClubSelector
              v-model:value="formState.form.clubId"
              placeholder="请选择俱乐部"
              :auto-load="true"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="归属" name="horseType">
            <a-select v-model:value="formState.form.horseType" placeholder="请选择归属" @change="onHorseTypeChange">
              <a-select-option :value="1">俱乐部马</a-select-option>
              <a-select-option :value="2">竞技马房马</a-select-option>
              <a-select-option :value="3">马主马</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="芯片号" name="chipNo">
            <a-input v-model:value="formState.form.chipNo" placeholder="请输入芯片号" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="护照号" name="passportNo">
            <a-input v-model:value="formState.form.passportNo" placeholder="请输入护照号" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="血统" name="breed">
            <a-input v-model:value="formState.form.breed" placeholder="请输入血统" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="性别" name="gender">
            <a-select v-model:value="formState.form.gender" placeholder="请选择性别">
              <a-select-option :value="1">公马</a-select-option>
              <a-select-option :value="2">母马</a-select-option>
              <a-select-option :value="3">骟马</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="出生日期" name="birthDate">
            <a-date-picker v-model:value="formState.form.birthDate" placeholder="请选择出生日期" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="身高(cm)" name="height">
            <a-input-number
              v-model:value="formState.form.height"
              placeholder="请输入身高"
              :min="100"
              :max="200"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 第一行：责任教练 + 责任马工 -->
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="责任教练" name="responsibleCoachId">
            <CoachSelector
              v-model:value="formState.form.responsibleCoachId"
              placeholder="请选择责任教练"
              :allow-clear="true"
              :auto-load="true"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="责任马工" name="responsibleGroomId">
            <EmployeeSelector
              v-model:value="formState.form.responsibleGroomId"
              placeholder="请选择责任马工"
              :role-id="35"
              :allow-clear="true"
              :auto-load="true"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 第二行：所属教练 + 马主 -->
      <a-row :gutter="20">
        <a-col :span="12" v-if="formState.form.horseType === 2">
          <a-form-item label="所属教练" name="coachId">
            <CoachSelector
              v-model:value="formState.form.coachId"
              placeholder="请选择教练"
              :auto-load="true"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="formState.form.horseType === 3">
          <a-form-item label="马主" name="ownerId">
            <MemberSelector
              v-model:value="formState.form.ownerId"
              placeholder="请选择马主"
              :auto-load="true"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20" v-if="formState.form.horseType === 2 || formState.form.horseType === 3">
        <a-col :span="12">
          <a-form-item label="寄养开始时间" name="boardingStartDate">
            <a-date-picker
              v-model:value="formState.form.boardingStartDate"
              placeholder="请选择寄养开始时间"
              style="width: 100%"
              format="YYYY-MM-DD"
              @change="onBoardingStartDateChange"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="寄养结束时间" name="boardingEndDate">
            <a-date-picker
              v-model:value="formState.form.boardingEndDate"
              placeholder="请选择寄养结束时间"
              style="width: 100%"
              format="YYYY-MM-DD"
              @change="onBoardingEndDateChange"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="寄养费(元)" name="boardingFee">
            <a-input-number
              v-model:value="formState.form.boardingFee"
              placeholder="请输入寄养费"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="健康状态" name="healthStatus">
            <a-select v-model:value="formState.form.healthStatus" placeholder="请选择健康状态">
              <a-select-option :value="1">健康</a-select-option>
              <a-select-option :value="2">观察</a-select-option>
              <a-select-option :value="3">治疗</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="工作状态" name="workStatus">
            <a-select v-model:value="formState.form.workStatus" placeholder="请选择工作状态">
              <a-select-option :value="1">可用</a-select-option>
              <a-select-option :value="2">休息</a-select-option>
              <a-select-option :value="3">治疗</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label="备注" name="remark" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
            <a-textarea v-model:value="formState.form.remark" placeholder="请输入备注" :rows="3" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { horseApi } from '/@/api/business/horse/horse-api';
import {
  ClubSelector,
  MemberSelector,
  CoachSelector,
  EmployeeSelector
} from '/@/components/business/selector';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const emit = defineEmits(['reloadList']);

const formRef = ref();

const formState = reactive({
  visible: false,
  loading: false,
  isCreate: true,
  form: {
    horseId: undefined,
    horseCode: '',
    horseName: '',
    clubId: undefined,
    horseType: 1,
    chipNo: '',
    passportNo: '',
    breed: '',
    gender: 1,
    color: '',
    birthDate: undefined,
    height: undefined,
    weight: undefined,
    coachId: undefined,
    ownerId: undefined,
    responsibleCoachId: undefined,
    responsibleGroomId: undefined,
    boardingStartDate: undefined,
    boardingEndDate: undefined,
    boardingFee: undefined,
    healthStatus: 1,
    workStatus: 1,
    remark: '',
  },
});

const formRules = {
  // horseCode: [{ required: true, message: '请输入马匹编号' }],
  horseName: [{ required: true, message: '请输入马名' }],
  clubId: [{ required: true, message: '请选择俱乐部' }],
  horseType: [{ required: true, message: '请选择归属' }],
  chipNo: [{ required: true, message: '请输入芯片号' }],
  breed: [{ required: true, message: '请输入血统' }],
  gender: [{ required: true, message: '请选择性别' }],
  healthStatus: [{ required: true, message: '请选择健康状态' }],
  workStatus: [{ required: true, message: '请选择工作状态' }],
};

function showModal(isCreate, rowData = {}) {
  formState.visible = true;
  formState.isCreate = isCreate;

  if (isCreate) {
    resetForm();
  } else {
    Object.assign(formState.form, {
      ...rowData,
      birthDate: rowData.birthDate ? dayjs(rowData.birthDate) : undefined,
      boardingStartDate: rowData.boardingStartDate ? dayjs(rowData.boardingStartDate) : undefined,
      boardingEndDate: rowData.boardingEndDate ? dayjs(rowData.boardingEndDate) : undefined,
    });
  }
}

function resetForm() {
  Object.assign(formState.form, {
    horseId: undefined,
    horseCode: '',
    horseName: '',
    clubId: undefined,
    horseType: 1,
    chipNo: '',
    passportNo: '',
    breed: '',
    gender: 1,
    color: '',
    birthDate: undefined,
    height: undefined,
    weight: undefined,
    ownerId: undefined,
    coachId: undefined,
    responsibleCoachId: undefined,
    responsibleGroomId: undefined,
    boardingStartDate: undefined,
    boardingEndDate: undefined,
    boardingFee: undefined,
    healthStatus: 1,
    workStatus: 1,
    remark: '',
  });
}

function onHorseTypeChange() {
  formState.form.ownerId = undefined;
  formState.form.coachId = undefined;
  if (formState.form.horseType === 1) {
    formState.form.boardingStartDate = undefined;
    formState.form.boardingEndDate = undefined;
    formState.form.boardingFee = undefined;
  }
}

// 寄养开始时间改变时，设置为当天开始时间（00:00:00）
function onBoardingStartDateChange(date) {
  if (date) {
    formState.form.boardingStartDate = dayjs(date).startOf('day');
  }
}

// 寄养结束时间改变时，设置为当天结束时间（23:59:59）
function onBoardingEndDateChange(date) {
  if (date) {
    formState.form.boardingEndDate = dayjs(date).endOf('day');
  }
}

async function onSubmit() {
  try {
    await formRef.value.validate();
    formState.loading = true;

    const params = { ...formState.form };
    if (params.birthDate) {
      params.birthDate = params.birthDate.format('YYYY-MM-DD 00:00:00');
    }
    if (params.boardingStartDate) {
      params.boardingStartDate = params.boardingStartDate.format('YYYY-MM-DD HH:mm:ss');
    }
    if (params.boardingEndDate) {
      params.boardingEndDate = params.boardingEndDate.format('YYYY-MM-DD HH:mm:ss');
    }

    if (formState.isCreate) {
      await horseApi.create(params);
      message.success('创建成功');
    } else {
      await horseApi.update(params);
      message.success('更新成功');
    }

    onClose();
    emit('reloadList');
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
