<!--
  * 证书展示组件（用于详情页）
  *
  * @Author:    Claude Code
  * @Date:      2025-08-25
  * @Copyright  马术俱乐部管理系统
-->
<template>
  <div class="certificate-display" v-if="level && level > 0">
    <a-card size="small" style="margin-bottom: 16px;" :bordered="false" class="cert-card">
      <template #title>
        <a-space>
          <a-tag :color="color">{{ title }}</a-tag>
          <a-tag color="default">{{ levelText }}</a-tag>
        </a-space>
      </template>
      <div class="cert-images" v-if="parsedImages.length > 0">
        <a-image-preview-group>
          <a-image
            v-for="(img, index) in parsedImages"
            :key="index"
            :src="img"
            :alt="`${title}证书${index + 1}`"
            style="max-width: 150px; max-height: 100px; margin: 5px; border-radius: 4px;"
          />
        </a-image-preview-group>
      </div>
      <a-empty v-else description="暂无证书图片" :image="Empty.PRESENTED_IMAGE_SIMPLE" />
    </a-card>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { Empty } from 'ant-design-vue';

const props = defineProps({
  title: String,
  level: Number, 
  levelText: String,
  images: String,
  color: { type: String, default: 'blue' }
});

// 解析图片JSON
const parsedImages = computed(() => {
  if (!props.images) return [];
  try {
    const parsed = JSON.parse(props.images);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
});
</script>

<style scoped>
.cert-card {
  background-color: #fafafa;
}

.cert-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
</style>