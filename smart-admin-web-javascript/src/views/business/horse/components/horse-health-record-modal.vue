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
        <a-select v-model:value="formState.form.planId" placeholder="请选择关联计划" allowClear @change="onPlanChange">
          <a-select-option v-for="plan in planList" :key="plan.id" :value="plan.id">
            {{ getPlanTypeDesc(plan.planType) }} - {{ plan.nextDate ? dayjs(plan.nextDate).format('YYYY-MM-DD') : '无下次日期' }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <!-- 显示选中计划的类型信息 -->
      <a-form-item v-if="selectedPlanType" label="计划类型">
        <a-tag :color="getPlanTypeColor(selectedPlanType)">
          {{ getPlanTypeDesc(selectedPlanType) }}
        </a-tag>
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
import { ref, reactive, onMounted, watch, computed } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { horseHealthRecordApi, horseHealthPlanApi } from '/@/api/business/horse/horse-api';
import { employeeApi } from '/@/api/system/employee-api';
import { fileApi } from '/@/api/support/file-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
import { getPlanTypeDesc, getPlanTypeColor } from '/@/constants/business/horse/health-const';
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
    planType: '',
    recordDate: undefined,
    executorId: undefined,
    content: '',
    imgUrl: '',
    recordData: '',
    nextDate: undefined,
  },
});

const formRules = {
  recordDate: [{ required: true, message: '请选择记录日期' }],
  content: [{ required: true, message: '请输入记录内容' }],
};

// 计算选中计划的类型
const selectedPlanType = computed(() => {
  // 优先使用关联计划的类型
  if (formState.form.planId) {
    const selectedPlan = planList.value.find(plan => plan.id === formState.form.planId);
    if (selectedPlan?.planType) {
      return selectedPlan.planType;
    }
  }

  // 如果没有关联计划，使用记录本身的类型（编辑模式）
  return formState.form.planType || '';
});

// 处理计划选择变化
function onPlanChange(planId) {
  if (planId) {
    const selectedPlan = planList.value.find(plan => plan.id === planId);
    if (selectedPlan) {
      formState.form.planType = selectedPlan.planType;
    }
  } else {
    formState.form.planType = '';
  }
}

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
    planType: '',
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

    updateFormImageUrls();
    params.imgUrl = formState.form.imgUrl;

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

    updateFormImageUrls();

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
  const urls = [];
  fileList.value.forEach((file) => {
    if (file.status === 'done' && file.url) {
      urls.push(file.url);
    }
  });

  formState.form.imgUrl = urls.join(',');
}

function handleUploadChange(info) {
  const { file } = info;

  if (file.status === 'removed') {
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
