<!--
  * 俱乐部表单弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-01-08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-modal
    :title="form.clubId ? '编辑俱乐部' : '新建俱乐部'"
    v-model:open="visible"
    :width="800"
    @ok="onSubmit"
    @cancel="onCancel"
    :confirmLoading="confirmLoading"
    destroyOnClose
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 19 }"
    >
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="俱乐部名称" name="clubName">
            <a-input v-model:value="form.clubName" placeholder="请输入俱乐部名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="俱乐部编码" name="clubCode">
            <a-input v-model:value="form.clubCode" placeholder="请输入俱乐部编码" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="LOGO地址" name="logoUrl">
            <a-input v-model:value="form.logoUrl" placeholder="请输入LOGO地址" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="置顶图片" name="bannerUrl">
            <a-input v-model:value="form.bannerUrl" placeholder="请输入置顶图片地址" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="营业开始时间" name="businessStartTime">
            <a-time-picker v-model:value="form.businessStartTime" placeholder="选择营业开始时间" format="HH:mm" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="营业结束时间" name="businessEndTime">
            <a-time-picker v-model:value="form.businessEndTime" placeholder="选择营业结束时间" format="HH:mm" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="详细地址" name="address">
        <a-input v-model:value="form.address" placeholder="请输入详细地址" />
      </a-form-item>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="电话" name="phone">
            <a-input v-model:value="form.phone" placeholder="请输入电话" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="联系电话" name="contactPhone">
            <a-input v-model:value="form.contactPhone" placeholder="请输入联系电话" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-item label="省份" name="province">
            <a-input v-model:value="form.province" placeholder="请输入省份" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="城市" name="city">
            <a-input v-model:value="form.city" placeholder="请输入城市" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="区县" name="district">
            <a-input v-model:value="form.district" placeholder="请输入区县" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="联系人" name="contactPerson">
            <a-input v-model:value="form.contactPerson" placeholder="请输入联系人" />
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
          <a-form-item label="法人代表" name="legalPerson">
            <a-input v-model:value="form.legalPerson" placeholder="请输入法人代表" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="到期时间" name="expireDate">
            <a-date-picker 
              v-model:value="form.expireDate" 
              placeholder="选择到期时间" 
              style="width: 100%"
              show-time
              format="YYYY-MM-DD HH:mm:ss"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="纬度" name="latitude">
            <a-input-number v-model:value="form.latitude" placeholder="请输入纬度" :precision="6" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="经度" name="longitude">
            <a-input-number v-model:value="form.longitude" placeholder="请输入经度" :precision="6" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="营业执照" name="businessLicenseUrl">
        <a-input v-model:value="form.businessLicenseUrl" placeholder="请输入营业执照图片地址" />
      </a-form-item>

      <a-form-item label="俱乐部详情" name="description">
        <a-textarea v-model:value="form.description" placeholder="请输入俱乐部详情" :rows="3" />
      </a-form-item>

      <a-form-item label="荣誉信息" name="honorInfo">
        <a-textarea v-model:value="form.honorInfo" placeholder="请输入荣誉信息" :rows="3" />
      </a-form-item>

      <a-form-item label="约课需知" name="bookingNotice">
        <a-textarea v-model:value="form.bookingNotice" placeholder="请输入约课需知" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';
import { clubApi } from '/@/api/business/club/club-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const emits = defineEmits(['refresh']);

const visible = ref(false);
const confirmLoading = ref(false);
const formRef = ref();

const formDefault = {
  clubId: null,
  clubName: '',
  clubCode: '',
  logoUrl: '',
  bannerUrl: '',
  pcBannerUrl: '',
  businessStartTime: null,
  businessEndTime: null,
  address: '',
  phone: '',
  description: '',
  honorInfo: '',
  bookingNotice: '',
  latitude: null,
  longitude: null,
  province: '',
  city: '',
  district: '',
  businessLicenseUrl: '',
  legalPerson: '',
  contactPerson: '',
  contactPhone: '',
  email: '',
  expireDate: null,
};

const form = reactive({ ...formDefault });

const rules = {
  clubName: [{ required: true, message: '请输入俱乐部名称', trigger: 'blur' }],
  phone: [
    {
      pattern: /^[0-9-()\\s]*$/,
      message: '电话格式不正确',
      trigger: 'blur',
    },
  ],
  contactPhone: [
    {
      pattern: /^[0-9-()\\s]*$/,
      message: '联系电话格式不正确',
      trigger: 'blur',
    },
  ],
  email: [
    {
      pattern: /^[A-Za-z0-9+_.-]+@(.+)$/,
      message: '邮箱格式不正确',
      trigger: 'blur',
    },
  ],
};

function show(clubId) {
  visible.value = true;
  if (clubId) {
    getDetail(clubId);
  } else {
    Object.assign(form, formDefault);
  }
}

async function getDetail(clubId) {
  try {
    let res = await clubApi.detail(clubId);
    let data = res.data;
    Object.assign(form, data);
    
    // 处理时间格式
    if (data.businessStartTime) {
      form.businessStartTime = dayjs(data.businessStartTime, 'HH:mm');
    }
    if (data.businessEndTime) {
      form.businessEndTime = dayjs(data.businessEndTime, 'HH:mm');
    }
    if (data.expireDate) {
      form.expireDate = dayjs(data.expireDate);
    }
  } catch (e) {
    smartSentry.captureError(e);
  }
}

async function onSubmit() {
  try {
    await formRef.value.validateFields();
    confirmLoading.value = true;
    
    let params = { ...form };
    
    // 处理时间格式
    if (params.businessStartTime) {
      params.businessStartTime = params.businessStartTime.format('HH:mm:ss');
    }
    if (params.businessEndTime) {
      params.businessEndTime = params.businessEndTime.format('HH:mm:ss');
    }
    if (params.expireDate) {
      params.expireDate = params.expireDate.format('YYYY-MM-DD HH:mm:ss');
    }

    if (params.clubId) {
      await clubApi.update(params);
      message.success('更新成功');
    } else {
      await clubApi.create(params);
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

function onCancel() {
  visible.value = false;
  Object.assign(form, formDefault);
}

defineExpose({
  show,
});
</script>