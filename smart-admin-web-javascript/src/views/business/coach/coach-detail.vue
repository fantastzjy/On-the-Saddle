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
      <a-col :span="24">
        <a-card title="基本信息" size="small">
          <a-descriptions :column="1" bordered size="small" :labelStyle="{ width: '160px' }">
            <a-descriptions-item label="用户姓名">
              {{ coach.userName || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="教练编号">
              {{ coach.coachNo || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="所属俱乐部">
              {{ coach.clubName || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="从业时间">
              {{ coach.entryDate ? coach.entryDate.substring(0, 10) : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="教练费">
              {{ coach.coachFee ? `${coach.coachFee}元/鞍时` : '-' }}
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

      <!-- 身份证信息 -->
      <a-col :span="24" v-if="coach.idCardFrontImg || coach.idCardBackImg" style="margin-top: 16px;">
        <a-card title="身份证信息" size="small">
          <a-row :gutter="16">
            <a-col :span="12" v-if="coach.idCardFrontImg">
              <h4>身份证正面</h4>
              <a-image
                :src="coach.idCardFrontImg"
                :alt="'身份证正面'"
                style="max-width: 300px; max-height: 200px; border-radius: 6px;"
              />
            </a-col>
            <a-col :span="12" v-if="coach.idCardBackImg">
              <h4>身份证反面</h4>
              <a-image
                :src="coach.idCardBackImg"
                :alt="'身份证反面'"
                style="max-width: 300px; max-height: 200px; border-radius: 6px;"
              />
            </a-col>
          </a-row>
        </a-card>
      </a-col>

    </a-row>

    <a-row :gutter="24" style="margin-top: 16px;">
      <!-- 骑手等级信息 -->
      <a-col :span="12">
        <a-card title="骑手等级信息" size="small">
          <a-descriptions :column="1" bordered size="small" :labelStyle="{ width: '160px' }">
            <a-descriptions-item label="骑手证号码">
              {{ coach.riderCertNo || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="场地障碍等级">
              {{ getRiderLevelText(coach.riderShowJumpingLevel) }}
            </a-descriptions-item>
            <a-descriptions-item label="盛装舞步等级">
              {{ getRiderLevelText(coach.riderDressageLevel) }}
            </a-descriptions-item>
            <a-descriptions-item label="三项赛等级">
              {{ getRiderLevelText(coach.riderEventingLevel) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>

      <!-- 教练等级信息 -->
      <a-col :span="12">
        <a-card title="教练等级信息" size="small">
          <a-descriptions :column="1" bordered size="small" :labelStyle="{ width: '160px' }">
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

    <!-- 教练证书信息 -->
    <a-row :gutter="24" style="margin-top: 16px;" v-if="coach.coachCertificates && coach.coachCertificates.length > 0">
      <a-col :span="24">
        <a-card title="教练证书" size="small">
          <div 
            v-for="(cert, index) in coach.coachCertificates" 
            :key="`coach-${cert.category}-${cert.level}-${index}`" 
            class="cert-group"
          >
            <a-card size="small" style="margin-bottom: 16px;" :bordered="false" class="cert-card">
              <template #title>
                <a-space>
                  <a-tag :color="getCoachCategoryColor(cert.category)">{{ cert.categoryText }}</a-tag>
                  <a-tag v-if="cert.level" color="gold">{{ cert.levelText }}</a-tag>
                </a-space>
              </template>
              <div class="cert-images" v-if="cert.images && cert.images.length > 0">
                <a-image-preview-group>
                  <a-image
                    v-for="(img, index) in cert.images"
                    :key="index"
                    :src="img"
                    :alt="`${cert.categoryText}证书${index + 1}`"
                    style="max-width: 150px; max-height: 100px; margin: 5px; border-radius: 4px;"
                  />
                </a-image-preview-group>
              </div>
              <a-empty v-else description="暂无证书图片" :image="Empty.PRESENTED_IMAGE_SIMPLE" />
            </a-card>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 骑手证书信息 -->
    <a-row :gutter="24" style="margin-top: 16px;" v-if="coach.riderCertificates && coach.riderCertificates.length > 0">
      <a-col :span="24">
        <a-card title="骑手证书" size="small">
          <div 
            v-for="(cert, index) in coach.riderCertificates" 
            :key="`rider-${cert.category}-${cert.level}-${index}`" 
            class="cert-group"
          >
            <a-card size="small" style="margin-bottom: 16px;" :bordered="false" class="cert-card">
              <template #title>
                <a-space>
                  <a-tag :color="getRiderCategoryColor(cert.category)">{{ cert.categoryText }}</a-tag>
                  <a-tag v-if="cert.level" color="blue">{{ cert.levelText }}</a-tag>
                </a-space>
              </template>
              <div class="cert-images" v-if="cert.images && cert.images.length > 0">
                <a-image-preview-group>
                  <a-image
                    v-for="(img, index) in cert.images"
                    :key="index"
                    :src="img"
                    :alt="`${cert.categoryText}证书${index + 1}`"
                    style="max-width: 150px; max-height: 100px; margin: 5px; border-radius: 4px;"
                  />
                </a-image-preview-group>
              </div>
              <a-empty v-else description="暂无证书图片" :image="Empty.PRESENTED_IMAGE_SIMPLE" />
            </a-card>
          </div>
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

// 获取教练证书类别颜色（统一结构）
function getCoachCategoryColor(category) {
  const colorMap = {
    1: 'blue',     // 场地障碍
    2: 'green',    // 盛装舞步
    3: 'orange',   // 三项赛
    4: 'gold'      // 教练星级
  };
  return colorMap[category] || 'default';
}

// 获取骑手证书类别颜色
function getRiderCategoryColor(category) {
  const colorMap = {
    1: 'blue',     // 场地障碍
    2: 'green',    // 盛装舞步
    3: 'orange'    // 三项赛
  };
  return colorMap[category] || 'default';
}

// 获取骑手等级文本
function getRiderLevelText(level) {
  if (!level || level === 0) return '-';
  const levelTexts = ['初三', '初二', '初一', '中三', '中二', '中一', '国三', '国二', '国一', '健将级'];
  return levelTexts[level - 1] || '未知等级';
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

<style scoped>
.cert-group {
  margin-bottom: 16px;
}

.cert-card {
  background-color: #fafafa;
}

.cert-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.cert-no {
  font-weight: 500;
  color: #1890ff;
}
</style>