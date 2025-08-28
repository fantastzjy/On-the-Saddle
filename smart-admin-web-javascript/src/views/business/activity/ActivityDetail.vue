<!--
  * 活动详情页面
  *
  * @Author: 1024创新实验室
  * @Date: 2025-08-28
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="activity-detail">
    <a-card :loading="loading">
      <template #extra>
        <a-space>
          <a-button @click="goBack">返回</a-button>
          <a-button 
            v-privilege="'business:activity:update'" 
            type="primary" 
            @click="editActivity"
            :disabled="!activityInfo.productId"
          >
            编辑活动
          </a-button>
        </a-space>
      </template>

      <div v-if="activityInfo.productId">
        <!-- 基本信息 -->
        <a-descriptions title="基本信息" :column="2" bordered>
          <a-descriptions-item label="活动名称">
            {{ activityInfo.activityName }}
          </a-descriptions-item>
          <a-descriptions-item label="活动价格">
            ¥{{ activityInfo.price ? parseFloat(activityInfo.price).toFixed(2) : '0.00' }}
          </a-descriptions-item>
        </a-descriptions>

        <a-divider />

        <!-- 活动详情 -->
        <div class="activity-details-section">
          <h3>活动详情</h3>
          <div class="details-content">
            {{ activityInfo.activityDetails || '暂无活动详情' }}
          </div>
        </div>

        <a-divider />

        <!-- 活动时间和地点 -->
        <a-descriptions title="时间地点" :column="2" bordered>
          <a-descriptions-item label="活动时间" :span="2">
            {{ formatDateTime(activityInfo.startTime) }} ~ {{ formatDateTime(activityInfo.endTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="活动地点" :span="2">
            {{ activityInfo.activityLocation || '-' }}
          </a-descriptions-item>
        </a-descriptions>

        <a-divider />

        <!-- 参与设置 -->
        <a-descriptions title="参与设置" :column="2" bordered>
          <a-descriptions-item label="最大参与人数">
            {{ activityInfo.maxParticipants }}人
          </a-descriptions-item>
          <a-descriptions-item label="活动价格">
            ¥{{ activityInfo.price ? parseFloat(activityInfo.price).toFixed(2) : '0.00' }}
          </a-descriptions-item>
        </a-descriptions>

        <a-divider />

        <!-- 活动规则 -->
        <div class="activity-rules-section">
          <h3>活动规则</h3>
          <div class="rules-content">
            {{ activityInfo.activityRule || '暂无活动规则' }}
          </div>
        </div>

        <a-divider />

        <!-- 活动图片 -->
        <div v-if="detailImages.length > 0" class="activity-images-section">
          <h3>活动图片</h3>
          <div class="images-gallery">
            <div 
              v-for="(image, index) in detailImages" 
              :key="index"
              class="image-item"
              @click="previewImage(image, index)"
            >
              <img :src="image" :alt="`活动图片${index + 1}`" />
            </div>
          </div>
        </div>

        <a-divider />

        <!-- 创建信息 -->
        <a-descriptions title="创建信息" :column="2" bordered size="small">
          <a-descriptions-item label="创建人">
            {{ activityInfo.createBy || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ formatDateTime(activityInfo.createTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="更新人">
            {{ activityInfo.updateBy || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="更新时间">
            {{ formatDateTime(activityInfo.updateTime) }}
          </a-descriptions-item>
        </a-descriptions>
      </div>

      <div v-else class="empty-state">
        <a-empty description="活动信息不存在" />
      </div>
    </a-card>

    <!-- 图片预览Modal -->
    <a-modal
      v-model:visible="previewVisible"
      :footer="null"
      centered
      width="80%"
      :title="`活动图片 ${currentImageIndex + 1}/${detailImages.length}`"
    >
      <div class="image-preview-container">
        <img
          :src="previewUrl"
          style="width: 100%; height: auto; max-height: 70vh; object-fit: contain"
        />
        <div class="image-navigation" v-if="detailImages.length > 1">
          <a-button 
            @click="prevImage" 
            :disabled="currentImageIndex === 0"
            :icon="h(LeftOutlined)"
          >
            上一张
          </a-button>
          <span class="image-counter">{{ currentImageIndex + 1 }} / {{ detailImages.length }}</span>
          <a-button 
            @click="nextImage" 
            :disabled="currentImageIndex === detailImages.length - 1"
            :icon="h(RightOutlined)"
          >
            下一张
          </a-button>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, h } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { LeftOutlined, RightOutlined } from '@ant-design/icons-vue';
import { activityApi } from '/@/api/business/activity/activity-api';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();

// ======================== 响应式数据 ========================
const loading = ref(false);
const previewVisible = ref(false);
const previewUrl = ref('');
const currentImageIndex = ref(0);

const activityInfo = reactive({
  productId: null,
  activityName: '',
  activityDetails: '',
  startTime: null,
  endTime: null,
  activityLocation: '',
  price: 0,
  maxParticipants: 0,
  activityRule: '',
  detailImages: '',
  createBy: '',
  createTime: null,
  updateBy: '',
  updateTime: null
});

// 解析详情图片
const detailImages = computed(() => {
  if (!activityInfo.detailImages) return [];
  try {
    const images = JSON.parse(activityInfo.detailImages);
    return Array.isArray(images) ? images : [];
  } catch (error) {
    console.error('解析活动图片失败:', error);
    return [];
  }
});

// ======================== 初始化 ========================
onMounted(() => {
  loadActivityDetail();
});

// ======================== 数据加载 ========================
async function loadActivityDetail() {
  try {
    loading.value = true;
    
    const activityId = route.params.activityId;
    if (!activityId) {
      message.error('活动ID无效');
      goBack();
      return;
    }

    const response = await activityApi.queryActivityDetail(activityId);
    if (response.ok && response.data) {
      Object.assign(activityInfo, response.data);
    } else {
      message.error(response.msg || '加载活动详情失败');
      goBack();
    }
  } catch (error) {
    message.error('加载活动详情失败');
    console.error('加载活动详情失败:', error);
    goBack();
  } finally {
    loading.value = false;
  }
}

// ======================== 图片预览 ========================
function previewImage(url, index) {
  previewUrl.value = url;
  currentImageIndex.value = index;
  previewVisible.value = true;
}

function prevImage() {
  if (currentImageIndex.value > 0) {
    currentImageIndex.value--;
    previewUrl.value = detailImages.value[currentImageIndex.value];
  }
}

function nextImage() {
  if (currentImageIndex.value < detailImages.value.length - 1) {
    currentImageIndex.value++;
    previewUrl.value = detailImages.value[currentImageIndex.value];
  }
}

// ======================== 页面操作 ========================
function goBack() {
  router.push('/activity/list');
}

function editActivity() {
  router.push(`/activity/edit/${activityInfo.productId}`);
}

// ======================== 工具方法 ========================
function formatDateTime(dateTime) {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm');
}
</script>

<style scoped>
.activity-detail {
  padding: 16px;
}

.activity-details-section,
.activity-rules-section {
  margin: 24px 0;
}

.activity-details-section h3,
.activity-rules-section h3,
.activity-images-section h3 {
  margin-bottom: 16px;
  color: #1890ff;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 8px;
}

.details-content,
.rules-content {
  background: #fafafa;
  padding: 16px;
  border-radius: 6px;
  line-height: 1.6;
  white-space: pre-wrap;
  min-height: 100px;
}

.activity-images-section {
  margin: 24px 0;
}

.images-gallery {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 12px;
}

.image-item {
  width: 120px;
  height: 120px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.image-item:hover {
  border-color: #1890ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
  transform: scale(1.02);
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
}

.image-preview-container {
  text-align: center;
}

.image-navigation {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding: 0 20px;
}

.image-counter {
  color: #666;
  font-size: 14px;
}

:deep(.ant-descriptions-title) {
  color: #1890ff;
  font-weight: 600;
}

:deep(.ant-descriptions-item-label) {
  background-color: #fafafa;
  font-weight: 500;
}
</style>