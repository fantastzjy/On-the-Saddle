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
    :width="1200"
    @ok="onSubmit"
    @cancel="onCancel"
    :confirmLoading="confirmLoading"
    destroyOnClose
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 20 }"
    >
      <!-- 基本信息 -->
      <a-divider orientation="left">
        <span style="font-weight: 600; color: #1890ff;">基本信息</span>
      </a-divider>
      
      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="俱乐部名称" name="clubName">
            <a-input v-model:value="form.clubName" placeholder="请输入俱乐部名称" />
          </a-form-item>
        </a-col>
        <!-- <a-col :span="12">
          <a-form-item label="俱乐部编码" name="clubCode">
            <a-input v-model:value="form.clubCode" placeholder="请输入俱乐部编码" />
          </a-form-item>
        </a-col> -->
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="营业时间" name="businessTime">
            <a-input-group compact>
              <a-time-picker 
                v-model:value="form.businessStartTime" 
                placeholder="开始时间" 
                format="HH:mm" 
                style="width: 50%" 
              />
              <a-time-picker 
                v-model:value="form.businessEndTime" 
                placeholder="结束时间" 
                format="HH:mm" 
                style="width: 50%" 
              />
            </a-input-group>
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 图片资料 -->
      <a-divider orientation="left">
        <span style="font-weight: 600; color: #1890ff;">图片资料</span>
      </a-divider>

      <a-row :gutter="12" style="align-items: flex-start;">
        <a-col :span="8">
          <a-form-item label="LOGO" name="logoUrl" style="margin-bottom: 0;">
            <a-upload
              :maxCount="1"
              accept="image/*"
              list-type="picture-card"
              :file-list="logoFileList"
              :before-upload="beforeUpload"
              :customRequest="(options) => customUploadRequest(options, 'logoUrl')"
              @change="(info) => handleUploadChange(info, 'logoUrl')"
              @preview="handlePreview"
              style="display: block;"
            >
              <div v-if="logoFileList.length === 0">
                <PlusOutlined />
                <div style="margin-top: 8px">上传LOGO</div>
              </div>
            </a-upload>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="轮播图片" name="carouselImages" style="margin-bottom: 0;">
            <a-upload
              :maxCount="5"
              accept="image/*"
              list-type="picture-card"
              :file-list="carouselFileList"
              :before-upload="beforeUpload"
              :customRequest="(options) => customUploadRequest(options, 'carouselImages')"
              @change="(info) => handleUploadChange(info, 'carouselImages')"
              @preview="handlePreview"
              style="display: block;"
            >
              <div v-if="carouselFileList.length < 5">
                <PlusOutlined />
                <div style="margin-top: 8px">上传轮播</div>
              </div>
            </a-upload>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="营业执照" name="businessLicenseUrl" style="margin-bottom: 0;">
            <a-upload
              :maxCount="1"
              accept="image/*"
              list-type="picture-card"
              :file-list="licenseFileList"
              :before-upload="beforeUpload"
              :customRequest="(options) => customUploadRequest(options, 'businessLicenseUrl')"
              @change="(info) => handleUploadChange(info, 'businessLicenseUrl')"
              @preview="handlePreview"
              style="display: block;"
            >
              <div v-if="licenseFileList.length === 0">
                <PlusOutlined />
                <div style="margin-top: 8px">上传执照</div>
              </div>
            </a-upload>
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 地址信息 -->
      <a-divider orientation="left">
        <span style="font-weight: 600; color: #1890ff;">地址信息</span>
      </a-divider>

      <a-row :gutter="16">
        <a-col :span="6">
          <a-form-item label="省份" name="province">
            <a-input v-model:value="form.province" placeholder="请输入省份" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="城市" name="city">
            <a-input v-model:value="form.city" placeholder="请输入城市" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="区县" name="district">
            <a-input v-model:value="form.district" placeholder="请输入区县" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="详细地址" name="address">
        <a-input v-model:value="form.address" placeholder="请输入详细地址" />
      </a-form-item>


      <!-- 联系方式 -->
      <a-divider orientation="left">
        <span style="font-weight: 600; color: #1890ff;">联系方式</span>
      </a-divider>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="联系电话" name="phone">
            <a-input v-model:value="form.phone" placeholder="请输入联系电话" />
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
          <a-form-item label="联系人" name="contactPerson">
            <a-input v-model:value="form.contactPerson" placeholder="请输入联系人" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="联系人电话" name="contactPhone">
            <a-input v-model:value="form.contactPhone" placeholder="请输入联系人电话" />
          </a-form-item>
        </a-col>
      </a-row>


      <!-- 详细描述 -->
      <a-divider orientation="left">
        <span style="font-weight: 600; color: #1890ff;">详细描述</span>
      </a-divider>

      <a-form-item label="俱乐部详情" name="description">
        <a-textarea v-model:value="form.description" placeholder="请输入俱乐部详情介绍" :rows="4" />
      </a-form-item>

      <a-form-item label="荣誉信息" name="honorInfo">
        <a-textarea v-model:value="form.honorInfo" placeholder="请输入俱乐部荣誉信息" :rows="3" />
      </a-form-item>

      <a-form-item label="约课需知" name="bookingNotice">
        <a-textarea v-model:value="form.bookingNotice" placeholder="请输入约课注意事项和规则" :rows="3" />
      </a-form-item>
    </a-form>
    
    <!-- 图片预览模态框 -->
    <a-modal :footer="null" v-model:open="previewVisible" @cancel="handlePreviewCancel">
      <img :src="previewUrl" alt="预览图片" style="width: 100%" />
    </a-modal>
  </a-modal>
</template>

<script setup>
import { reactive, ref, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { clubApi } from '/@/api/business/club/club-api';
import { fileApi } from '/@/api/support/file-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { SmartLoading } from '/@/components/framework/smart-loading';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
import dayjs from 'dayjs';

const emits = defineEmits(['refresh']);

const visible = ref(false);
const confirmLoading = ref(false);
const formRef = ref();

// 图片文件列表
const logoFileList = ref([]);
const carouselFileList = ref([]);
const licenseFileList = ref([]);

// 预览相关
const previewVisible = ref(false);
const previewUrl = ref('');

const formDefault = {
  clubId: null,
  clubName: '',
  clubCode: '',
  logoUrl: '',
  carouselImages: '',
  pcBannerUrl: '',
  businessStartTime: null,
  businessEndTime: null,
  address: '',
  phone: '',
  description: '',
  honorInfo: '',
  bookingNotice: '',
  province: '',
  city: '',
  district: '',
  businessLicenseUrl: '',
  contactPerson: '',
  contactPhone: '',
  email: '',
};

const form = reactive({ ...formDefault });

const rules = {
  clubName: [{ required: true, message: '请输入俱乐部名称', trigger: 'blur' }],
  // clubCode: [{ required: true, message: '请输入俱乐部编码', trigger: 'blur' }],
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
  address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
};

// 监听表单URL变化，更新文件列表
watch(() => form.logoUrl, (newVal) => {
  if (newVal && logoFileList.value.length === 0) {
    logoFileList.value = [{
      uid: 'logo-1',
      name: 'logo.jpg',
      status: 'done',
      url: newVal,
    }];
  } else if (!newVal) {
    logoFileList.value = [];
  }
});

watch(() => form.carouselImages, (newVal) => {
  if (newVal && carouselFileList.value.length === 0) {
    try {
      let imageUrls;
      // 兼容单个URL字符串和JSON数组格式
      if (newVal.startsWith('[') && newVal.endsWith(']')) {
        imageUrls = JSON.parse(newVal);
      } else {
        imageUrls = [newVal]; // 将单个URL转换为数组
      }
      carouselFileList.value = imageUrls.map((url, index) => ({
        uid: `carousel-${index}`,
        name: `carousel-${index}.jpg`,
        status: 'done',
        url: url,
      }));
    } catch (e) {
      // 如果解析失败，当作单个URL处理
      carouselFileList.value = [{
        uid: 'carousel-0',
        name: 'carousel-0.jpg',
        status: 'done',
        url: newVal,
      }];
    }
  } else if (!newVal) {
    carouselFileList.value = [];
  }
});

watch(() => form.businessLicenseUrl, (newVal) => {
  if (newVal && licenseFileList.value.length === 0) {
    licenseFileList.value = [{
      uid: 'license-1',
      name: 'license.jpg',
      status: 'done',
      url: newVal,
    }];
  } else if (!newVal) {
    licenseFileList.value = [];
  }
});

// 上传前验证
function beforeUpload(file) {
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    message.error('只能上传图片文件！');
    return false;
  }
  
  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    message.error('图片大小不能超过5MB！');
    return false;
  }
  
  return true;
}

// 自定义上传
async function customUploadRequest(options, fieldName) {
  SmartLoading.show();
  try {
    const formData = new FormData();
    formData.append('file', options.file);
    
    const res = await fileApi.uploadFile(formData, FILE_FOLDER_TYPE_ENUM.COMMON.value);
    const fileInfo = res.data;
    
    // 更新表单字段
    if (fieldName === 'carouselImages') {
      // 轮播图片特殊处理
      let currentUrls = [];
      if (form.carouselImages) {
        try {
          // 兼容单个URL字符串和JSON数组格式
          if (form.carouselImages.startsWith('[') && form.carouselImages.endsWith(']')) {
            currentUrls = JSON.parse(form.carouselImages);
          } else {
            currentUrls = [form.carouselImages];
          }
        } catch (e) {
          currentUrls = [form.carouselImages];
        }
      }
      currentUrls.push(fileInfo.fileUrl);
      form.carouselImages = JSON.stringify(currentUrls);
    } else {
      form[fieldName] = fileInfo.fileUrl;
    }
    
    // 更新文件列表
    const fileItem = {
      uid: options.file.uid,
      name: fileInfo.fileName,
      status: 'done',
      url: fileInfo.fileUrl,
      response: fileInfo,
    };
    
    // 轮播图片需要特殊处理文件列表
    if (fieldName === 'carouselImages') {
      carouselFileList.value.push(fileItem);
    }
    
    options.onSuccess(fileItem, options.file);
    
  } catch (error) {
    smartSentry.captureError(error);
    options.onError(error);
  } finally {
    SmartLoading.hide();
  }
}

// 上传状态变化
function handleUploadChange(info, fieldName) {
  const { file, fileList } = info;
  
  if (file.status === 'removed') {
    if (fieldName === 'carouselImages') {
      // 轮播图片特殊处理
      const remainingUrls = carouselFileList.value
        .filter(item => item.uid !== file.uid)
        .map(item => item.url);
      form.carouselImages = remainingUrls.length > 0 ? JSON.stringify(remainingUrls) : '';
      carouselFileList.value = carouselFileList.value.filter(item => item.uid !== file.uid);
    } else {
      form[fieldName] = '';
      
      // 清空对应的文件列表
      if (fieldName === 'logoUrl') {
        logoFileList.value = [];
      } else if (fieldName === 'businessLicenseUrl') {
        licenseFileList.value = [];
      }
    }
  }
}

// 预览图片
function handlePreview(file) {
  previewUrl.value = file.url || file.preview;
  previewVisible.value = true;
}

// 关闭预览
function handlePreviewCancel() {
  previewVisible.value = false;
}

function show(clubId) {
  visible.value = true;
  if (clubId) {
    getDetail(clubId);
  } else {
    Object.assign(form, formDefault);
    // 清空文件列表
    logoFileList.value = [];
    carouselFileList.value = [];
    licenseFileList.value = [];
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
    
    // 设置图片文件列表 - 会通过watch自动更新
    
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
  // 清空文件列表
  logoFileList.value = [];
  carouselFileList.value = [];
  licenseFileList.value = [];
}

defineExpose({
  show,
});
</script>