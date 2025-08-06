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
        <div class="upload-container">
          <a-upload
            :file-list="fileList"
            list-type="picture-card"
            :custom-request="customUploadRequest"
            :before-upload="beforeUpload"
            @change="handleUploadChange"
            @preview="handlePreview"
            :max-count="3"
            accept="image/*"
          >
            <div v-if="fileList.length < 3">
              <plus-outlined />
              <div style="margin-top: 8px">上传图片</div>
            </div>
          </a-upload>
          
          <!-- 图片预览模态框 -->
          <a-modal v-model:open="previewVisible" :footer="null" :width="600">
            <img :src="previewImage" style="width: 100%" />
          </a-modal>
        </div>
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
import { PlusOutlined } from '@ant-design/icons-vue';
import { horseHealthRecordApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { fileApi } from '/@/api/support/file-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
import { getPlanTypeDesc } from '/@/constants/business/horse/health-const';
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
const executorList = ref([]);

// 图片上传相关
const fileList = ref([]);
const previewVisible = ref(false);
const previewImage = ref('');

const formState = reactive({
  visible: false,
  loading: false,
  form: {
    executorId: undefined,
    content: '',
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
  fileList.value = []; // 重置图片列表
}

function resetForm() {
  Object.assign(formState.form, {
    executorId: undefined,
    content: '',
    recordData: '',
  });
}

async function onSubmit() {
  try {
    await formRef.value.validate();
    formState.loading = true;

    // 更新图片URL
    updateFormImageUrls();
    
    const params = {
      horseId: props.horseId,
      planId: planInfo.id,
      planType: planInfo.planType,
      executorId: formState.form.executorId,
      recordDate: dayjs().format('YYYY-MM-DD HH:mm:ss'),
      content: formState.form.content,
      imgUrl: getImageUrls(),
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
  fileList.value = []; // 重置图片列表
  formRef.value?.resetFields();
}

// 图片上传相关方法
function beforeUpload(file) {
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    message.error('只能上传图片文件！');
    return false;
  }
  
  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    message.error('图片大小不能超过 5MB！');
    return false;
  }
  
  return true;
}

async function customUploadRequest(options) {
  try {
    const tempFile = {
      uid: options.file.uid,
      name: options.file.name,
      status: 'uploading',
      percent: 0,
      originFileObj: options.file
    };
    
    fileList.value.push(tempFile);
    
    const formData = new FormData();
    formData.append('file', options.file);
    
    const res = await fileApi.uploadFile(formData, FILE_FOLDER_TYPE_ENUM.COMMON.value);
    const fileData = res.data;
    
    const fileIndex = fileList.value.findIndex(item => item.uid === options.file.uid);
    if (fileIndex > -1) {
      fileList.value[fileIndex] = {
        uid: options.file.uid,
        name: fileData.fileName || options.file.name,
        status: 'done',
        url: fileData.fileUrl,
        thumbUrl: fileData.fileUrl,
        response: fileData,
        originFileObj: options.file
      };
    }
    
    options.onSuccess(fileData, options.file);
    message.success('图片上传成功');
    
  } catch (error) {
    const fileIndex = fileList.value.findIndex(item => item.uid === options.file.uid);
    if (fileIndex > -1) {
      fileList.value.splice(fileIndex, 1);
    }
    
    options.onError(error);
    message.error('图片上传失败');
    smartSentry.captureError(error);
  }
}

function updateFormImageUrls() {
  // 这个方法保持空实现，因为我们用getImageUrls()
}

function getImageUrls() {
  const urls = [];
  fileList.value.forEach((file) => {
    if (file.status === 'done' && file.url) {
      urls.push(file.url);
    }
  });
  return urls.join(',');
}

function handleUploadChange(info) {
  const { file } = info;
  
  if (file.status === 'removed') {
    const index = fileList.value.findIndex(item => item.uid === file.uid);
    if (index > -1) {
      fileList.value.splice(index, 1);
    }
  }
}

function handlePreview(file) {
  previewImage.value = file.url || file.thumbUrl;
  previewVisible.value = true;
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

.upload-container {
  width: 100%;
}

.upload-container :deep(.ant-upload-select) {
  width: 80px !important;
  height: 80px !important;
}

.upload-container :deep(.ant-upload-list-picture-card-container) {
  width: 80px;
  height: 80px;
}
</style>