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
        <a-col :span="12">
          <a-form-item label="马匹编号" name="horseCode">
            <a-input v-model:value="formState.form.horseCode" placeholder="请输入马匹编号" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="马名" name="horseName">
            <a-input v-model:value="formState.form.horseName" placeholder="请输入马名" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="俱乐部" name="clubId">
            <a-select v-model:value="formState.form.clubId" placeholder="请选择俱乐部">
              <a-select-option v-for="club in clubList" :key="club.clubId" :value="club.clubId">
                {{ club.clubName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="马匹类型" name="horseType">
            <a-select v-model:value="formState.form.horseType" placeholder="请选择马匹类型" @change="onHorseTypeChange">
              <a-select-option :value="1">俱乐部马</a-select-option>
              <a-select-option :value="2">马主马</a-select-option>
              <a-select-option :value="3">教练马</a-select-option>
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
          <a-form-item label="品种" name="breed">
            <a-input v-model:value="formState.form.breed" placeholder="请输入品种" />
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
          <a-form-item label="毛色" name="color">
            <a-input v-model:value="formState.form.color" placeholder="请输入毛色" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="出生日期" name="birthDate">
            <a-date-picker v-model:value="formState.form.birthDate" placeholder="请选择出生日期" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
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
        <a-col :span="12">
          <a-form-item label="体重(kg)" name="weight">
            <a-input-number
              v-model:value="formState.form.weight"
              placeholder="请输入体重"
              :min="200"
              :max="800"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20" v-if="formState.form.horseType === 2">
        <a-col :span="12">
          <a-form-item label="马主" name="ownerId">
            <a-select v-model:value="formState.form.ownerId" placeholder="请选择马主" showSearch>
              <a-select-option v-for="owner in ownerList" :key="owner.employeeId" :value="owner.employeeId">
                {{ owner.actualName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20" v-if="formState.form.horseType === 3">
        <a-col :span="12">
          <a-form-item label="所属教练" name="coachId">
            <a-select v-model:value="formState.form.coachId" placeholder="请选择教练" showSearch>
              <a-select-option v-for="coach in coachList" :key="coach.coachId" :value="coach.coachId">
                {{ coach.userName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="责任教练" name="responsibleCoachId">
            <a-select v-model:value="formState.form.responsibleCoachId" placeholder="请选择责任教练" showSearch allowClear>
              <a-select-option v-for="coach in coachList" :key="coach.coachId" :value="coach.coachId">
                {{ coach.userName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
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

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="formState.form.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { horseApi } from '/@/api/business/horse/horse-api';
import { clubApi } from '/@/api/business/club/club-api';
import { coachApi } from '/@/api/business/coach/coach-api';
import { employeeApi } from '/@/api/system/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const emit = defineEmits(['reloadList']);

const formRef = ref();
const clubList = ref([]);
const coachList = ref([]);
const ownerList = ref([]);

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
    ownerId: undefined,
    responsibleCoachId: undefined,
    healthStatus: 1,
    workStatus: 1,
    remark: '',
  },
});

const formRules = {
  horseCode: [{ required: true, message: '请输入马匹编号' }],
  horseName: [{ required: true, message: '请输入马名' }],
  clubId: [{ required: true, message: '请选择俱乐部' }],
  horseType: [{ required: true, message: '请选择马匹类型' }],
  chipNo: [{ required: true, message: '请输入芯片号' }],
  breed: [{ required: true, message: '请输入品种' }],
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
    responsibleCoachId: undefined,
    healthStatus: 1,
    workStatus: 1,
    remark: '',
  });
}

function onHorseTypeChange() {
  formState.form.ownerId = undefined;
}

async function onSubmit() {
  try {
    await formRef.value.validate();
    formState.loading = true;

    const params = { ...formState.form };
    if (params.birthDate) {
      params.birthDate = params.birthDate.format('YYYY-MM-DD');
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

async function queryClubList() {
  try {
    const res = await clubApi.queryList();
    clubList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  }
}

async function queryCoachList() {
  try {
    const res = await coachApi.queryList();
    coachList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  }
}

async function queryOwnerList() {
  try {
    const res = await employeeApi.queryAll();
    ownerList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  }
}

onMounted(() => {
  queryClubList();
  queryCoachList();
  queryOwnerList();
});

defineExpose({
  showModal,
});
</script>