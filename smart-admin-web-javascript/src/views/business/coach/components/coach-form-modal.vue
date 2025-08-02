<!--
  * 教练新增/编辑弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-01-08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-modal
    :title="form.coachId ? '编辑教练' : '新建教练'"
    :open="visible"
    @cancel="onClose"
    @ok="onSubmit"
    :width="800"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="教练姓名" name="coachName">
            <a-input v-model:value="form.coachName" placeholder="请输入教练姓名" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="教练编号" name="coachCode">
            <a-input v-model:value="form.coachCode" placeholder="请输入教练编号" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="性别" name="gender">
            <a-select v-model:value="form.gender" placeholder="请选择性别">
              <a-select-option :value="1">男</a-select-option>
              <a-select-option :value="2">女</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="出生日期" name="birthDate">
            <a-date-picker v-model:value="form.birthDate" placeholder="请选择出生日期" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="手机号码" name="phone">
            <a-input v-model:value="form.phone" placeholder="请输入手机号码" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="邮箱" name="email">
            <a-input v-model:value="form.email" placeholder="请输入邮箱" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="专业等级" name="professionalLevel">
            <a-input v-model:value="form.professionalLevel" placeholder="请输入专业等级" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="从业年限" name="yearsExperience">
            <a-input-number v-model:value="form.yearsExperience" placeholder="请输入从业年限" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="所属俱乐部" name="clubId">
            <a-select v-model:value="form.clubId" placeholder="请选择俱乐部" allowClear>
              <a-select-option v-for="club in clubList" :key="club.clubId" :value="club.clubId">
                {{ club.clubName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="入职日期" name="entryDate">
            <a-date-picker v-model:value="form.entryDate" placeholder="请选择入职日期" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="薪资" name="salary">
            <a-input-number v-model:value="form.salary" placeholder="请输入薪资" :min="0" :precision="2" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="在职状态" name="isActive">
            <a-select v-model:value="form.isActive" placeholder="请选择状态">
              <a-select-option :value="true">在职</a-select-option>
              <a-select-option :value="false">离职</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="紧急联系人" name="emergencyContact">
            <a-input v-model:value="form.emergencyContact" placeholder="请输入紧急联系人" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="紧急联系电话" name="emergencyPhone">
            <a-input v-model:value="form.emergencyPhone" placeholder="请输入紧急联系电话" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="专业特长" name="speciality">
            <a-textarea v-model:value="form.speciality" placeholder="请输入专业特长" :rows="3" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="个人简介" name="bio">
            <a-textarea v-model:value="form.bio" placeholder="请输入个人简介" :rows="3" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="2" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
import { reactive, ref, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { coachApi } from '/@/api/business/coach/coach-api';
import { clubApi } from '/@/api/business/club/club-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const emit = defineEmits(['refresh']);

// ----------------------- 表单 -----------------------

const formRef = ref();
const visible = ref(false);
const confirmLoading = ref(false);
const clubList = ref([]);

const formState = {
  coachId: null,
  coachName: '',
  coachCode: '',
  gender: null,
  birthDate: null,
  phone: '',
  email: '',
  professionalLevel: '',
  speciality: '',
  yearsExperience: null,
  clubId: null,
  salary: null,
  entryDate: null,
  emergencyContact: '',
  emergencyPhone: '',
  isActive: true,
  bio: '',
  remark: '',
};

const form = reactive({ ...formState });

const rules = {
  coachName: [{ required: true, message: '请输入教练姓名', trigger: 'blur' }],
  coachCode: [{ required: true, message: '请输入教练编号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  professionalLevel: [{ required: true, message: '请输入专业等级', trigger: 'blur' }],
  clubId: [{ required: true, message: '请选择所属俱乐部', trigger: 'change' }],
};

// ----------------------- 显示/隐藏 -----------------------

async function show(coachId) {
  visible.value = true;
  resetForm();
  await loadClubList();
  
  if (coachId) {
    await loadCoachDetail(coachId);
  }
}

function onClose() {
  visible.value = false;
  resetForm();
}

function resetForm() {
  Object.assign(form, formState);
  nextTick(() => {
    formRef.value?.clearValidate();
  });
}

// ----------------------- 数据加载 -----------------------

async function loadClubList() {
  try {
    let res = await clubApi.queryList(true);
    clubList.value = res.data;
  } catch (e) {
    smartSentry.captureError(e);
  }
}

async function loadCoachDetail(coachId) {
  try {
    let res = await coachApi.detail(coachId);
    Object.assign(form, res.data);
    
    // 处理日期字段
    if (form.birthDate) {
      form.birthDate = dayjs(form.birthDate);
    }
    if (form.entryDate) {
      form.entryDate = dayjs(form.entryDate);
    }
  } catch (e) {
    smartSentry.captureError(e);
  }
}

// ----------------------- 提交 -----------------------

async function onSubmit() {
  try {
    await formRef.value.validateFields();
    confirmLoading.value = true;
    
    let formData = { ...form };
    
    // 处理日期字段
    if (formData.birthDate) {
      formData.birthDate = dayjs(formData.birthDate).format('YYYY-MM-DD');
    }
    if (formData.entryDate) {
      formData.entryDate = dayjs(formData.entryDate).format('YYYY-MM-DD');
    }
    
    if (form.coachId) {
      await coachApi.update(formData);
      message.success('更新成功');
    } else {
      await coachApi.create(formData);
      message.success('创建成功');
    }
    
    onClose();
    emit('refresh');
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    confirmLoading.value = false;
  }
}

// ----------------------- 暴露方法 -----------------------

defineExpose({
  show,
});
</script>