<!--
  * 教练详情页
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-01-08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-page-header
    :ghost="false"
    :title="`教练详情 - ${coach.userName || ''}`"
    @back="() => $router.go(-1)"
  >
    <template #extra>
      <a-button @click="edit" v-privilege="'club:coach:update'" type="primary">
        <template #icon>
          <EditOutlined />
        </template>
        编辑
      </a-button>
    </template>
  </a-page-header>

  <a-card :loading="loading">
    <a-row :gutter="24">
      <!-- 基本信息 -->
      <a-col :span="12">
        <a-card title="基本信息" size="small">
          <a-descriptions :column="1" bordered size="small">
            <a-descriptions-item label="用户姓名">
              {{ coach.userName || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="教练编号">
              {{ coach.coachNo || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="所属俱乐部">
              {{ coach.clubName || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="入行时间">
              {{ coach.entryDate || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="排序">
              {{ coach.sortOrder || 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="有效状态">
              <a-tag :color="coach.isValid === 1 ? 'green' : 'red'">
                {{ coach.isValid === 1 ? '有效' : '无效' }}
              </a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>

      <!-- 头像 -->
      <a-col :span="12">
        <a-card title="头像" size="small">
          <div style="text-align: center;">
            <img 
              v-if="coach.avatarUrl" 
              :src="coach.avatarUrl" 
              style="width: 150px; height: 150px; object-fit: cover; border-radius: 50%;" 
            />
            <a-empty v-else description="暂无头像" :image="Empty.PRESENTED_IMAGE_SIMPLE" />
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px;">
      <!-- 骑手等级信息 -->
      <a-col :span="12">
        <a-card title="骑手等级信息" size="small">
          <a-descriptions :column="1" bordered size="small">
            <a-descriptions-item label="骑手证号码">
              {{ coach.riderCertNo || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="场地障碍等级">
              {{ coach.riderLevelShowJumping || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="盛装舞步等级">
              {{ coach.riderLevelDressage || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="三项赛等级">
              {{ coach.riderLevelEventing || '-' }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>

      <!-- 教练等级信息 -->
      <a-col :span="12">
        <a-card title="教练等级信息" size="small">
          <a-descriptions :column="1" bordered size="small">
            <a-descriptions-item label="星级教练证号码">
              {{ coach.coachCertNo || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="教练等级">
              {{ coach.coachLevel || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="创建时间">
              {{ coach.createTime || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="更新时间">
              {{ coach.updateTime || '-' }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px;">
      <!-- 专长领域 -->
      <a-col :span="24">
        <a-card title="专长领域" size="small">
          <div style="white-space: pre-wrap; line-height: 1.6;">
            {{ coach.specialties || '暂无' }}
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px;">
      <!-- 个人介绍 -->
      <a-col :span="24">
        <a-card title="个人介绍" size="small">
          <div style="white-space: pre-wrap; line-height: 1.6;">
            {{ coach.introduction || '暂无' }}
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 证书图片 -->
    <a-row :gutter="24" style="margin-top: 16px;" v-if="coach.riderCertImgUrl || coach.coachCertImgUrl">
      <a-col :span="24">
        <a-card title="证书图片" size="small">
          <a-row :gutter="16">
            <a-col :span="12" v-if="coach.riderCertImgUrl">
              <h4>骑手证书</h4>
              <div v-if="isValidJSON(coach.riderCertImgUrl)">
                <img 
                  v-for="(img, index) in parseImageJSON(coach.riderCertImgUrl)" 
                  :key="index"
                  :src="img" 
                  style="width: 200px; height: auto; margin: 8px; border-radius: 4px;" 
                />
              </div>
              <div v-else>
                <img 
                  :src="coach.riderCertImgUrl" 
                  style="width: 200px; height: auto; margin: 8px; border-radius: 4px;" 
                />
              </div>
            </a-col>
            <a-col :span="12" v-if="coach.coachCertImgUrl">
              <h4>教练证书</h4>
              <div v-if="isValidJSON(coach.coachCertImgUrl)">
                <img 
                  v-for="(img, index) in parseImageJSON(coach.coachCertImgUrl)" 
                  :key="index"
                  :src="img" 
                  style="width: 200px; height: auto; margin: 8px; border-radius: 4px;" 
                />
              </div>
              <div v-else>
                <img 
                  :src="coach.coachCertImgUrl" 
                  style="width: 200px; height: auto; margin: 8px; border-radius: 4px;" 
                />
              </div>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </a-card>

  <!-- 教练编辑弹窗 -->
  <CoachFormModal ref="coachFormModal" @refresh="loadCoachDetail" />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { Empty } from 'ant-design-vue';
import { EditOutlined } from '@ant-design/icons-vue';
import { coachApi } from '/@/api/business/coach/coach-api';
import { smartSentry } from '/@/lib/smart-sentry';
import CoachFormModal from './components/coach-form-modal.vue';

const route = useRoute();

// ----------------------- 数据 -----------------------

const loading = ref(false);
const coach = ref({});
const coachFormModal = ref();

// ----------------------- 方法 -----------------------

async function loadCoachDetail() {
  try {
    loading.value = true;
    const coachId = route.query.coachId;
    if (!coachId) {
      return;
    }
    const res = await coachApi.detail(coachId);
    coach.value = res.data;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    loading.value = false;
  }
}

function edit() {
  coachFormModal.value.show(coach.value.coachId);
}

// 判断是否为有效的JSON字符串
function isValidJSON(str) {
  if (!str) return false;
  try {
    JSON.parse(str);
    return true;
  } catch (e) {
    return false;
  }
}

// 解析图片JSON字符串
function parseImageJSON(jsonStr) {
  try {
    const parsed = JSON.parse(jsonStr);
    return Array.isArray(parsed) ? parsed : [parsed];
  } catch (e) {
    return [];
  }
}

// ----------------------- 生命周期 -----------------------

onMounted(() => {
  loadCoachDetail();
});
</script>