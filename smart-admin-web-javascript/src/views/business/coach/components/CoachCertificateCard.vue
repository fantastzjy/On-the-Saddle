<!--
  * 教练证书卡片组件
  *
  * @Author:    Claude Code
  * @Date:      2025-08-25
  * @Copyright  马术俱乐部管理系统
-->
<template>
  <a-card size="small" :title="title" style="margin-bottom: 16px;">
    <a-row :gutter="16">
      <a-col :span="12">
        <a-form-item label="星级">
          <a-select :value="localLevel" :placeholder="`选择${title}星级`" @change="handleLevelChange">
            <a-select-option :value="0">无证书</a-select-option>
            <a-select-option v-for="i in maxLevel" :key="i" :value="i">
              {{i}}星
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="证书图片">
          <Upload 
            :multiple="true"
            :maxCount="3"
            :buttonText="`上传${title}证书`"
            :defaultFileList="imageFileList"
            @change="handleImageChange"
            :disabled="localLevel === 0"
          />
        </a-form-item>
      </a-col>
    </a-row>
  </a-card>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import Upload from '/@/components/support/file-upload/index.vue';

const props = defineProps({
  title: String,
  level: { type: Number, default: 0 },
  images: { type: String, default: '' }, 
  maxLevel: { type: Number, default: 5 }
});

const emit = defineEmits(['update:level', 'update:images']);

// 图片文件列表
const imageFileList = ref([]);

// 本地星级值（用于双向绑定）
const localLevel = ref(props.level);

// 处理星级变化
const handleLevelChange = (value) => {
  localLevel.value = value;
  emit('update:level', value);
  if (value === 0) {
    // 清空图片
    emit('update:images', '');
    imageFileList.value = [];
  }
};

// 处理图片上传
const handleImageChange = (fileList) => {
  console.log('=== 教练证书图片上传 ===');
  console.log(`${props.title} - 传入的fileList:`, fileList);
  
  const imageUrls = fileList.map(file => file.fileUrl);
  const imageJson = JSON.stringify(imageUrls);
  
  console.log(`${props.title} - 图片URLs:`, imageUrls);
  console.log(`${props.title} - 图片JSON:`, imageJson);
  
  emit('update:images', imageJson);
  imageFileList.value = fileList;
};

// 监听 prop level 变化，同步到本地值
watch(() => props.level, (newLevel) => {
  localLevel.value = newLevel;
}, { immediate: true });

// 监听图片数据变化，初始化文件列表
watch(() => props.images, (newImages) => {
  console.log('=== 教练证书图片数据变化 ===');
  console.log(`${props.title} - 新图片数据:`, newImages);
  
  if (newImages) {
    try {
      const parsed = JSON.parse(newImages);
      const urls = Array.isArray(parsed) ? parsed : [];
      
      imageFileList.value = urls.map((url, index) => ({
        fileUrl: url,
        fileKey: url.split('/').pop() || `${props.title}-${index}`,
        fileName: `${props.title}证书${index + 1}.jpg`,
        uid: `${props.title}-${index}`,
        status: 'done',
        url: url
      }));
      
      console.log(`${props.title} - 解析后的fileList:`, imageFileList.value);
    } catch (e) {
      console.warn(`${props.title} - 图片JSON解析失败:`, newImages, e);
      imageFileList.value = [];
    }
  } else {
    imageFileList.value = [];
  }
}, { immediate: true });
</script>