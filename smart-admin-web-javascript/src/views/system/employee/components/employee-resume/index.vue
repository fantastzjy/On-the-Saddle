<!--
  * 员工简历上传管理组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-01-01
  * @Copyright 1024创新实验室
-->
<template>
  <div class="resume-management">
    <div class="header">
      <span class="title">简历文件</span>
      <a-upload
        :show-upload-list="false"
        :before-upload="beforeUpload"
        :custom-request="handleUpload"
        accept=".pdf,.doc,.docx"
      >
        <a-button type="primary" size="small" :loading="uploading">
          <template #icon><UploadOutlined /></template>
          上传简历
        </a-button>
      </a-upload>
    </div>

    <!-- 简历文件列表 -->
    <div class="resume-list">
      <a-empty v-if="resumeList.length === 0" description="暂无简历文件" />
      
      <div v-else class="resume-items">
        <div 
          v-for="(item, index) in resumeList" 
          :key="item.fileId"
          class="resume-item"
          :class="{ latest: isLatestResume(item.fileKey) }"
        >
          <div class="resume-info">
            <div class="resume-icon">
              <FileTextOutlined v-if="isPdf(item.fileName)" style="color: #ff4d4f;" />
              <FileWordOutlined v-else style="color: #1890ff;" />
            </div>
            
            <div class="resume-details">
              <div class="resume-name">
                {{ item.fileName }}
                <a-tag v-if="isLatestResume(item.fileKey)" color="green" size="small">最新</a-tag>
              </div>
              <div class="resume-meta">
                <span class="file-size">{{ formatFileSize(item.fileSize) }}</span>
                <a-divider type="vertical" />
                <span class="upload-time">{{ formatDate(item.createTime) }}</span>
              </div>
            </div>
          </div>
          
          <div class="resume-actions">
            <a-button 
              type="link" 
              size="small" 
              @click="previewResume(item)"
              :disabled="!isPdf(item.fileName)"
            >
              预览
            </a-button>
            <a-divider type="vertical" />
            <a-button type="link" size="small" @click="downloadResume(item)">
              下载
            </a-button>
            <a-divider type="vertical" />
            <a-dropdown>
              <a-button type="link" size="small">
                更多 <DownOutlined />
              </a-button>
              <template #overlay>
                <a-menu>
                  <a-menu-item 
                    key="set-latest" 
                    @click="setLatestResume(item)"
                    :disabled="isLatestResume(item.fileKey)"
                  >
                    设为最新
                  </a-menu-item>
                  <a-menu-divider />
                  <a-menu-item key="delete" @click="showDeleteConfirm(item)" danger>
                    删除
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
        </div>
      </div>
    </div>

    <!-- 上传进度 -->
    <a-modal
      v-model:open="progressVisible"
      title="上传进度"
      :footer="null"
      :closable="false"
      width="400px"
    >
      <div class="upload-progress">
        <a-progress :percent="uploadProgress" />
        <div class="progress-text">正在上传：{{ uploadingFileName }}</div>
      </div>
    </a-modal>

    <!-- 简历预览 -->
    <a-modal
      v-model:open="previewVisible"
      title="简历预览"
      width="80%"
      :footer="null"
      :destroy-on-close="true"
    >
      <div class="resume-preview">
        <iframe 
          v-if="previewUrl"
          :src="previewUrl"
          width="100%"
          height="600px"
          frameborder="0"
        ></iframe>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { 
  UploadOutlined, 
  FileTextOutlined, 
  FileWordOutlined, 
  DownOutlined 
} from '@ant-design/icons-vue';
import { employeeResumeApi } from '/@/api/system/employee-resume-api';
import dayjs from 'dayjs';

// Props
const props = defineProps({
  employeeId: {
    type: Number,
    required: true
  },
  latestResumeFileKey: {
    type: String,
    default: ''
  }
});

// Emits
const emit = defineEmits(['change', 'update-latest']);

// 响应式数据
const resumeList = ref([]);
const uploading = ref(false);
const progressVisible = ref(false);
const previewVisible = ref(false);
const uploadProgress = ref(0);
const uploadingFileName = ref('');
const previewUrl = ref('');
const loading = ref(false);

// 判断是否为最新简历
const isLatestResume = (fileKey) => {
  return fileKey === props.latestResumeFileKey;
};

// 判断是否为PDF文件
const isPdf = (fileName) => {
  return fileName && fileName.toLowerCase().endsWith('.pdf');
};

// 格式化文件大小
const formatFileSize = (size) => {
  if (!size) return '0 B';
  const units = ['B', 'KB', 'MB', 'GB'];
  let index = 0;
  let fileSize = size;
  
  while (fileSize >= 1024 && index < units.length - 1) {
    fileSize /= 1024;
    index++;
  }
  
  return `${fileSize.toFixed(1)} ${units[index]}`;
};

// 格式化日期
const formatDate = (date) => {
  return date ? dayjs(date).format('YYYY-MM-DD HH:mm') : '';
};

// 加载简历列表
const loadResumeList = async () => {
  try {
    loading.value = true;
    const res = await employeeResumeApi.getResumeList(props.employeeId);
    if (res.data) {
      resumeList.value = res.data;
      emit('change', res.data);
    }
  } catch (error) {
    message.error('加载简历列表失败');
  } finally {
    loading.value = false;
  }
};

// 文件上传前检查
const beforeUpload = (file) => {
  const isValidType = file.type === 'application/pdf' || 
                     file.type === 'application/msword' || 
                     file.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
  
  if (!isValidType) {
    message.error('只能上传 PDF、DOC、DOCX 格式的文件');
    return false;
  }
  
  const isValidSize = file.size / 1024 / 1024 < 10; // 限制10MB
  if (!isValidSize) {
    message.error('文件大小不能超过 10MB');
    return false;
  }
  
  return false; // 阻止默认上传行为
};

// 自定义上传
const handleUpload = async ({ file }) => {
  try {
    uploading.value = true;
    progressVisible.value = true;
    uploadingFileName.value = file.name;
    uploadProgress.value = 0;

    // 模拟上传进度
    const progressTimer = setInterval(() => {
      if (uploadProgress.value < 90) {
        uploadProgress.value += 10;
      }
    }, 200);

    const res = await employeeResumeApi.uploadResume(props.employeeId, file);
    
    clearInterval(progressTimer);
    uploadProgress.value = 100;

    if (res.data) {
      message.success('简历上传成功');
      
      // 更新最新简历信息
      emit('update-latest', res.data.fileKey);
      
      // 刷新列表
      await loadResumeList();
    }
  } catch (error) {
    message.error('简历上传失败');
  } finally {
    uploading.value = false;
    
    // 延迟关闭进度窗口
    setTimeout(() => {
      progressVisible.value = false;
      uploadProgress.value = 0;
      uploadingFileName.value = '';
    }, 1000);
  }
};

// 预览简历
const previewResume = (item) => {
  if (!isPdf(item.fileName)) {
    message.info('暂不支持预览此格式文件');
    return;
  }
  
  // 构建预览URL
  previewUrl.value = `/api/file/getFileUrl?fileKey=${item.fileKey}`;
  previewVisible.value = true;
};

// 下载简历
const downloadResume = (item) => {
  const downloadUrl = employeeResumeApi.getDownloadUrl(item.fileKey);
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.download = item.fileName;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

// 设置为最新简历
const setLatestResume = async (item) => {
  try {
    await employeeResumeApi.setLatestResume(props.employeeId, item.fileKey);
    message.success('设置最新简历成功');
    emit('update-latest', item.fileKey);
  } catch (error) {
    message.error('设置最新简历失败');
  }
};

// 显示删除确认
const showDeleteConfirm = (item) => {
  Modal.confirm({
    title: '确定要删除这个简历文件吗？',
    content: `文件名：${item.fileName}`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => deleteResume(item)
  });
};

// 删除简历
const deleteResume = async (item) => {
  try {
    await employeeResumeApi.deleteResume(props.employeeId, item.fileKey);
    message.success('删除简历成功');
    
    // 如果删除的是最新简历，需要通知父组件更新
    if (isLatestResume(item.fileKey)) {
      emit('update-latest', null);
    }
    
    // 刷新列表
    loadResumeList();
  } catch (error) {
    message.error('删除简历失败');
  }
};

// 初始化
const init = () => {
  if (props.employeeId) {
    loadResumeList();
  }
};

// 暴露方法给父组件
defineExpose({
  refresh: loadResumeList
});

// 组件挂载后初始化
onMounted(() => {
  init();
});
</script>

<style scoped>
.resume-management {
  padding: 16px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.title {
  font-size: 16px;
  font-weight: 500;
}

.resume-list {
  min-height: 200px;
}

.resume-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.resume-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  background: #fafafa;
  transition: all 0.3s;
}

.resume-item:hover {
  border-color: #d9d9d9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.resume-item.latest {
  border-color: #52c41a;
  background: #f6ffed;
}

.resume-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
}

.resume-icon {
  font-size: 24px;
}

.resume-details {
  flex: 1;
}

.resume-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.resume-meta {
  font-size: 12px;
  color: #666;
}

.file-size {
  margin-right: 8px;
}

.upload-time {
  margin-left: 8px;
}

.resume-actions {
  flex-shrink: 0;
}

.upload-progress {
  text-align: center;
}

.progress-text {
  margin-top: 12px;
  color: #666;
  font-size: 14px;
}

.resume-preview {
  width: 100%;
}
</style>