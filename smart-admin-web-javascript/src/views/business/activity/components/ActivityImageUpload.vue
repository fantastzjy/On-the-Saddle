<!--
  * 活动图片上传组件 - 横向排列
  *
  * @Author: 1024创新实验室
  * @Date: 2025-08-28
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="activity-image-upload">
    <div class="image-list" ref="imageListRef">
      <div 
        v-for="(image, index) in imageList" 
        :key="index"
        class="image-item"
        :draggable="imageList.length > 1"
        @dragstart="handleDragStart($event, index)"
        @dragover.prevent
        @drop="handleDrop($event, index)"
      >
        <div class="image-preview">
          <img :src="image" :alt="`活动图片${index + 1}`" />
          <div class="image-actions">
            <a-button 
              type="text" 
              size="small" 
              @click="previewImage(image)"
              :icon="h(EyeOutlined)"
            />
            <a-button 
              type="text" 
              size="small" 
              danger
              @click="removeImage(index)"
              :icon="h(DeleteOutlined)"
            />
          </div>
        </div>
        <div class="drag-handle" v-if="imageList.length > 1">
          <DragOutlined />
        </div>
      </div>

      <!-- 上传按钮 -->
      <div 
        v-if="imageList.length < maxCount" 
        class="upload-button"
        @click="triggerUpload"
      >
        <PlusOutlined />
        <div class="upload-text">上传图片</div>
      </div>
    </div>

    <!-- 隐藏的文件上传input -->
    <input
      ref="fileInputRef"
      type="file"
      accept="image/jpeg,image/jpg,image/png,image/gif"
      multiple
      style="display: none"
      @change="handleFileSelect"
    />

    <!-- 图片预览Modal -->
    <a-modal
      v-model:visible="previewVisible"
      :footer="null"
      centered
      width="80%"
      max-width="800px"
    >
      <img
        :src="previewUrl"
        style="width: 100%; height: auto; max-height: 70vh; object-fit: contain"
      />
    </a-modal>
  </div>
</template>

<script setup>
import { ref, computed, h } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined, EyeOutlined, DeleteOutlined, DragOutlined } from '@ant-design/icons-vue';

// ======================== Props & Emits ========================
const props = defineProps({
  value: {
    type: Array,
    default: () => []
  },
  maxCount: {
    type: Number,
    default: 9
  }
});

const emits = defineEmits(['update:value']);

// ======================== 响应式数据 ========================
const fileInputRef = ref();
const imageListRef = ref();
const previewVisible = ref(false);
const previewUrl = ref('');
let dragIndex = -1;

const imageList = computed({
  get: () => props.value || [],
  set: (value) => emits('update:value', value)
});

// ======================== 文件上传 ========================
function triggerUpload() {
  fileInputRef.value?.click();
}

async function handleFileSelect(event) {
  const files = Array.from(event.target.files);
  if (!files.length) return;

  // 检查数量限制
  if (imageList.value.length + files.length > props.maxCount) {
    message.warning(`最多只能上传${props.maxCount}张图片`);
    return;
  }

  // 验证文件
  const validFiles = [];
  for (const file of files) {
    if (validateFile(file)) {
      validFiles.push(file);
    }
  }

  if (!validFiles.length) return;

  // 上传文件
  try {
    const uploadPromises = validFiles.map(file => uploadFile(file));
    const urls = await Promise.all(uploadPromises);
    
    const newImageList = [...imageList.value, ...urls.filter(url => url)];
    imageList.value = newImageList;
    
    message.success(`成功上传${urls.filter(url => url).length}张图片`);
  } catch (error) {
    message.error('图片上传失败');
    console.error('图片上传失败:', error);
  }

  // 清空文件选择
  event.target.value = '';
}

function validateFile(file) {
  // 检查文件类型
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
  if (!allowedTypes.includes(file.type)) {
    message.error(`文件 ${file.name} 格式不支持，请选择 JPEG/PNG/GIF 格式的图片`);
    return false;
  }

  // 检查文件大小 (2MB)
  const maxSize = 2 * 1024 * 1024;
  if (file.size > maxSize) {
    message.error(`文件 ${file.name} 大小超过2MB，请选择较小的图片`);
    return false;
  }

  return true;
}

async function uploadFile(file) {
  // 这里应该调用实际的文件上传API
  // 暂时使用本地预览URL
  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      resolve(e.target.result);
    };
    reader.onerror = () => {
      resolve(null);
    };
    reader.readAsDataURL(file);
  });

  // 实际项目中应该这样调用：
  // const formData = new FormData();
  // formData.append('file', file);
  // const response = await fileApi.uploadImage(formData);
  // return response.ok ? response.data.url : null;
}

// ======================== 图片操作 ========================
function removeImage(index) {
  const newImageList = [...imageList.value];
  newImageList.splice(index, 1);
  imageList.value = newImageList;
}

function previewImage(url) {
  previewUrl.value = url;
  previewVisible.value = true;
}

// ======================== 拖拽排序 ========================
function handleDragStart(event, index) {
  dragIndex = index;
  event.dataTransfer.effectAllowed = 'move';
}

function handleDrop(event, dropIndex) {
  event.preventDefault();
  
  if (dragIndex === -1 || dragIndex === dropIndex) return;

  const newImageList = [...imageList.value];
  const draggedItem = newImageList.splice(dragIndex, 1)[0];
  newImageList.splice(dropIndex, 0, draggedItem);
  
  imageList.value = newImageList;
  dragIndex = -1;
}
</script>

<style scoped>
.activity-image-upload {
  width: 100%;
}

.image-list {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 12px;
  align-items: flex-start;
}

.image-item {
  position: relative;
  width: 120px;
  height: 120px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  overflow: hidden;
  cursor: move;
  transition: all 0.3s;
}

.image-item:hover {
  border-color: #1890ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
}

.image-preview {
  position: relative;
  width: 100%;
  height: 100%;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-item:hover .image-actions {
  opacity: 1;
}

.image-actions .ant-btn {
  color: white;
  border: none;
  background: transparent;
}

.image-actions .ant-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.drag-handle {
  position: absolute;
  top: 4px;
  right: 4px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 2px 4px;
  border-radius: 3px;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-item:hover .drag-handle {
  opacity: 1;
}

.upload-button {
  width: 120px;
  height: 120px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  color: #666;
}

.upload-button:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.upload-text {
  margin-top: 8px;
  font-size: 12px;
}

/* 拖拽时的样式 */
.image-item.dragging {
  opacity: 0.5;
  transform: rotate(5deg);
}

.image-item.drop-target {
  border-color: #1890ff;
  background-color: rgba(24, 144, 255, 0.1);
}
</style>