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
            {{ plan.planType }}
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
        <div class="upload-container">
          <a-upload
            :file-list="fileList"
            list-type="picture-card"
            :custom-request="customUploadRequest"
            :before-upload="beforeUpload"
            @change="handleUploadChange"
            @preview="handlePreview"
            :max-count="5"
            accept="image/*"
          >
            <div v-if="fileList.length < 5">
              <plus-outlined />
              <div style="margin-top: 8px">上传图片</div>
            </div>
          </a-upload>
          
          <!-- 图片预览模态框 -->
          <a-modal v-model:open="previewVisible" :footer="null" :width="800">
            <img :src="previewImage" style="width: 100%" />
          </a-modal>
        </div>
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
import { ref, reactive, onMounted, watch } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { horseHealthRecordApi, horseHealthPlanApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { fileApi } from '/@/api/support/file-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
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

// 图片上传相关
const fileList = ref([]);
const previewVisible = ref(false);
const previewImage = ref('');

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
    fileList.value = [];
  } else {
    Object.assign(formState.form, {
      ...rowData,
      recordDate: rowData.recordDate ? dayjs(rowData.recordDate) : undefined,
      nextDate: rowData.nextDate ? dayjs(rowData.nextDate) : undefined,
    });
    
    // 加载现有图片到文件列表
    loadExistingImages(rowData.imgUrl);
  }
}

function loadExistingImages(imgUrls) {
  fileList.value = [];
  if (imgUrls) {
    const urls = imgUrls.split(',').filter(url => url.trim());
    fileList.value = urls.map((url, index) => ({
      uid: `existing_${index}`,
      name: `image_${index + 1}`,
      status: 'done',
      url: url.trim(),
      thumbUrl: url.trim(),
    }));
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

    // 确保使用最新的图片URL
    updateFormImageUrls();
    params.imgUrl = formState.form.imgUrl;
    
    console.log('>>> Submitting with imgUrl:', params.imgUrl);

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
  fileList.value = [];
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
    console.log('>>> Starting upload for file:', options.file.name);
    
    // 创建临时文件对象并添加到列表
    const tempFile = {
      uid: options.file.uid,
      name: options.file.name,
      status: 'uploading',
      percent: 0,
      originFileObj: options.file
    };
    
    fileList.value.push(tempFile);
    console.log('>>> Temp file added, fileList length:', fileList.value.length);
    
    const formData = new FormData();
    formData.append('file', options.file);
    
    const res = await fileApi.uploadFile(formData, FILE_FOLDER_TYPE_ENUM.COMMON.value);
    const fileData = res.data;
    
    console.log('>>> Upload API Response:', fileData);
    
    // 找到临时文件并替换为完成状态
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
      
      console.log('>>> File updated at index', fileIndex, 'with URL:', fileData.fileUrl);
    }
    
    // 更新表单imgUrl字段
    updateFormImageUrls();
    
    options.onSuccess(fileData, options.file);
    message.success('图片上传成功');
    
  } catch (error) {
    console.error('Upload error:', error);
    
    // 移除失败的文件
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
  console.log('>>> updateFormImageUrls - fileList length:', fileList.value.length);
  
  const urls = [];
  fileList.value.forEach((file, index) => {
    console.log(`>>> updateFormImageUrls File ${index}:`, {
      status: file.status,
      url: file.url,
      hasUrl: !!file.url
    });
    
    if (file.status === 'done' && file.url) {
      urls.push(file.url);
    }
  });
  
  formState.form.imgUrl = urls.join(',');
  console.log('>>> updateFormImageUrls result:', formState.form.imgUrl);
}

function handleUploadChange(info) {
  const { file, fileList: currentFileList } = info;
  
  if (file.status === 'removed') {
    console.log('>>> File removed:', file.name);
    // 从我们的fileList中移除
    const index = fileList.value.findIndex(item => item.uid === file.uid);
    if (index > -1) {
      fileList.value.splice(index, 1);
      updateFormImageUrls();
    }
  }
}

function handlePreview(file) {
  previewImage.value = file.url || file.thumbUrl;
  previewVisible.value = true;
}

function handleRemove(file) {
  console.log('>>> Removing file with URL:', file.url);
  setTimeout(() => {
    updateFormImageUrls();
  }, 50);
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

<style scoped>
.upload-container {
  width: 100%;
}

.upload-container :deep(.ant-upload-select) {
  width: 100px !important;
  height: 100px !important;
}

.upload-container :deep(.ant-upload-list-picture-card-container) {
  width: 100px;
  height: 100px;
}
</style>