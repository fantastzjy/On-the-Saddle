<!--
  * 活动详情图片上传组件
  * 支持拖拽排序、图片预览、删除功能
  * 数据结构：字符串数组，仅存储图片URL
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-23
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="activity-detail-image-upload">
    <!-- 图片列表 -->
    <div class="image-list" v-if="imageList.length > 0">
      <draggable
        v-model="imageList"
        :animation="200"
        :disabled="false"
        ghost-class="sortable-ghost"
        chosen-class="sortable-chosen"
        drag-class="sortable-drag"
        @end="handleDragEnd"
        item-key="index"
      >
        <template #item="{ element: imageUrl, index }">
          <div class="image-item" :key="`img-${index}`">
            <!-- 序号 -->
            <div class="image-index">{{ index + 1 }}</div>
            <!-- 图片 -->
            <div class="image-container" @click="handlePreview(imageUrl, index)">
              <img :src="imageUrl" :alt="`详情图片${index + 1}`" />
              <div class="image-overlay">
                <EyeOutlined class="preview-icon" />
              </div>
            </div>
            <!-- 删除按钮 -->
            <div class="image-actions">
              <a-button
                type="text"
                danger
                size="small"
                @click="handleRemove(index)"
                :icon="h(DeleteOutlined)"
              />
            </div>
          </div>
        </template>
      </draggable>
    </div>

    <!-- 上传按钮 -->
    <div class="upload-container" v-if="imageList.length < maxCount">
      <a-upload
        :show-upload-list="false"
        :before-upload="beforeUpload"
        :custom-request="handleUpload"
        :accept="acceptTypes"
        :disabled="uploading"
      >
        <div class="upload-button">
          <LoadingOutlined v-if="uploading" />
          <PlusOutlined v-else />
          <div class="upload-text">
            {{ uploading ? '上传中...' : '添加图片' }}
          </div>
        </div>
      </a-upload>
    </div>

    <!-- 上传提示 -->
    <div class="upload-tips" v-if="showTips">
      <div class="tip-item">最多上传{{ maxCount }}张图片</div>
      <div class="tip-item">单张图片不超过{{ maxSize }}MB</div>
      <div class="tip-item">支持{{ acceptTypes }}格式</div>
      <div class="tip-item">支持拖拽排序，列表顺序就是展示顺序</div>
    </div>

    <!-- 图片预览模态框 -->
    <a-modal
      v-model:open="previewVisible"
      :footer="null"
      :width="null"
      :centered="true"
      title="图片预览"
      :styles="{ body: { padding: '16px' } }"
      :wrap-class-name="'preview-modal-wrapper'"
    >
      <div class="preview-container">
        <div class="preview-image-wrapper">
          <img :src="previewUrl" :alt="previewAlt" class="preview-image" />
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, computed, watch, h } from 'vue';
import { message } from 'ant-design-vue';
import {
  PlusOutlined,
  EyeOutlined,
  DeleteOutlined,
  LoadingOutlined
} from '@ant-design/icons-vue';
import draggable from 'vuedraggable';
import { fileApi } from '/@/api/support/file-api';
import { useUserStore } from '/@/store/modules/system/user';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';

// ======================== Props & Model ========================
const props = defineProps({
  maxCount: {
    type: Number,
    default: 9
  },
  maxSize: {
    type: Number,
    default: 2 // MB
  },
  acceptTypes: {
    type: String,
    default: '.jpg,.jpeg,.png,.gif'
  },
  showTips: {
    type: Boolean,
    default: true
  }
});

// 使用 defineModel 替代手动的 emit 和 watch
const imageList = defineModel('value', {
  type: Array,
  default: () => []
});

// 确保初始化时数据类型正确
if (!Array.isArray(imageList.value)) {
  imageList.value = [];
}

// ======================== 响应式数据 ========================
const uploading = ref(false);
const previewVisible = ref(false);
const previewUrl = ref('');
const previewAlt = ref('');

// ======================== 计算属性 ========================
const acceptTypesArray = computed(() => {
  return props.acceptTypes.split(',').map(type => type.trim().toLowerCase());
});

// ======================== 方法 ========================

/**
 * 上传前验证
 */
function beforeUpload(file) {
  // 检查文件数量
  if (imageList.value.length >= props.maxCount) {
    message.error(`最多只能上传${props.maxCount}张图片`);
    return false;
  }

  // 检查文件类型
  const fileName = file.name.toLowerCase();
  const fileExtension = fileName.substring(fileName.lastIndexOf('.'));
  if (!acceptTypesArray.value.includes(fileExtension)) {
    message.error(`只支持${props.acceptTypes}格式的图片`);
    return false;
  }

  // 检查文件大小
  const isLimitSize = file.size / 1024 / 1024 < props.maxSize;
  if (!isLimitSize) {
    message.error(`图片大小不能超过${props.maxSize}MB`);
    return false;
  }

  return true;
}

/**
 * 自定义上传
 */
async function handleUpload(options) {
  const { file } = options;
  
  if (!beforeUpload(file)) {
    return;
  }

  try {
    uploading.value = true;
    
    const formData = new FormData();
    formData.append('file', file);
    
    const response = await fileApi.uploadFile(formData, FILE_FOLDER_TYPE_ENUM.COMMON.value);
    
    if (response.ok && response.data && response.data.fileUrl) {
      // 只存储图片地址字符串，使用defineModel自动同步到父组件
      const imageUrl = response.data.fileUrl;
      imageList.value.push(imageUrl);
      message.success('图片上传成功');
    } else {
      message.error('图片上传失败');
    }
  } catch (error) {
    console.error('上传图片失败:', error);
    message.error('图片上传失败');
  } finally {
    uploading.value = false;
  }
}

/**
 * 拖拽排序完成
 */
function handleDragEnd() {
  // 拖拽完成后，imageList已经自动更新了顺序
  // 使用 defineModel，数据会自动同步到父组件
  message.success('图片顺序已更新');
}

/**
 * 删除图片
 */
function handleRemove(index) {
  if (index >= 0 && index < imageList.value.length) {
    imageList.value.splice(index, 1);
    message.success('图片已删除');
  }
}

/**
 * 预览图片
 */
function handlePreview(imageUrl, index) {
  previewUrl.value = imageUrl;
  previewAlt.value = `详情图片${index + 1}`;
  previewVisible.value = true;
}

// ======================== 暴露给父组件的方法 ========================
defineExpose({
  clear: () => {
    imageList.value = [];
  }
});
</script>

<style lang="less" scoped>
.activity-detail-image-upload {
  width: 100%;
  overflow: visible;
  
  .image-list {
    display: flex !important;
    flex-direction: row !important;
    flex-wrap: wrap !important;
    gap: 12px;
    margin-bottom: 12px;
    align-items: flex-start !important;
    justify-content: flex-start !important;
    width: 100% !important;
    min-width: 300px;

    @media (max-width: 768px) {
      gap: 8px;
      min-width: 200px;
    }

    // 🔧 强制覆盖vuedraggable可能的样式
    &, 
    & > *,
    & > * > *,
    & .sortable-chosen,
    & .sortable-ghost {
      display: flex !important;
      flex-direction: row !important;
      flex-wrap: wrap !important;
    }

    .image-item {
      position: relative !important;
      width: 104px !important;
      height: 104px !important;
      flex: 0 0 104px !important;
      flex-shrink: 0 !important;
      flex-grow: 0 !important;
      flex-basis: 104px !important;
      border-radius: 8px;
      overflow: hidden;
      border: 1px solid #d9d9d9;
      background: #fafafa;
      cursor: move;
      transition: all 0.3s ease;
      display: inline-block !important;
      vertical-align: top !important;
      float: left !important;

      @media (max-width: 768px) {
        width: 80px !important;
        height: 80px !important;
        flex: 0 0 80px !important;
        flex-basis: 80px !important;
      }

      &:hover {
        border-color: #1890ff;
        box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
        
        .image-overlay {
          opacity: 1;
        }
        
        .image-actions {
          opacity: 1;
        }
      }

      .image-index {
        position: absolute;
        top: 4px;
        left: 4px;
        width: 20px;
        height: 20px;
        background: rgba(0, 0, 0, 0.6);
        color: white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        font-weight: bold;
        z-index: 3;
      }

      .image-container {
        position: relative;
        width: 100%;
        height: 100%;
        cursor: pointer;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .image-overlay {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.4);
          display: flex;
          align-items: center;
          justify-content: center;
          opacity: 0;
          transition: opacity 0.3s;

          .preview-icon {
            color: white;
            font-size: 20px;
          }
        }
      }

      .image-actions {
        position: absolute;
        top: 4px;
        right: 4px;
        opacity: 0;
        transition: opacity 0.3s;
        z-index: 3;
      }
    }
  }

  .upload-container {
    display: inline-block;

    .upload-button {
      width: 104px;
      height: 104px;
      border: 1px dashed #d9d9d9;
      border-radius: 8px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s;
      background: #fafafa;

      &:hover {
        border-color: #1890ff;
        color: #1890ff;
      }

      .anticon {
        font-size: 24px;
        margin-bottom: 8px;
      }

      .upload-text {
        font-size: 12px;
        color: #666;
      }
    }
  }

  .upload-tips {
    margin-top: 12px;
    padding: 8px 12px;
    background: #f6f8fa;
    border-radius: 6px;
    border-left: 3px solid #1890ff;

    .tip-item {
      font-size: 12px;
      color: #666;
      line-height: 1.5;
      margin-bottom: 2px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .preview-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    max-height: 80vh;
    overflow: hidden;
    width: fit-content;
    margin: 0 auto;

    .preview-image-wrapper {
      display: flex;
      justify-content: center;
      align-items: center;
      max-height: 80vh;
      max-width: 90vw;
      width: fit-content;
      overflow: hidden;
      box-sizing: border-box;
    }

    .preview-image {
      max-width: 90vw !important;
      max-height: 80vh !important;
      width: auto !important;
      height: auto !important;
      object-fit: contain !important;
      border-radius: 6px !important;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
      display: block !important;
    }
  }
}

// 拖拽样式
.sortable-ghost {
  opacity: 0.4;
  background: #1890ff !important;
}

.sortable-chosen {
  opacity: 0.8;
  transform: scale(1.02);
}

.sortable-drag {
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: rotate(3deg);
}
</style>

<!-- 🔧 全局强制样式 - 最高优先级 -->
<style lang="less">
/* 全局强制修复活动详情图片横向排列 */
.activity-detail-image-upload .image-list {
  display: flex !important;
  flex-direction: row !important;
  flex-wrap: wrap !important;
  align-items: flex-start !important;
}

.activity-detail-image-upload .image-list .image-item {
  display: inline-block !important;
  float: left !important;
  margin-right: 12px !important;
  margin-bottom: 12px !important;
}

.activity-detail-image-upload .image-list::after {
  content: "";
  display: table;
  clear: both;
}

/* 🔧 预览模态框自适应宽度 */
.preview-modal-wrapper .ant-modal {
  width: fit-content !important;
  max-width: 95vw !important;
  margin: 0 auto !important;
}

.preview-modal-wrapper .ant-modal .ant-modal-content {
  width: fit-content !important;
}

.preview-modal-wrapper .ant-modal .ant-modal-body {
  padding: 16px !important;
  width: fit-content !important;
}

/* 🔧 强制修复预览图片尺寸 - 全局最高优先级 */
.ant-modal .preview-container .preview-image-wrapper .preview-image {
  max-width: 90vw !important;
  max-height: 80vh !important;
  width: auto !important;
  height: auto !important;
  object-fit: contain !important;
  display: block !important;
}

/* 🔧 强制修复预览容器尺寸 */
.ant-modal .preview-container {
  max-height: 80vh !important;
  overflow: hidden !important;
  width: fit-content !important;
  margin: 0 auto !important;
}

.ant-modal .preview-container .preview-image-wrapper {
  max-height: 80vh !important;
  max-width: 90vw !important;
  width: fit-content !important;
  overflow: hidden !important;
  box-sizing: border-box !important;
}
</style>