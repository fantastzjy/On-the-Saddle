<template>
  <a-page-header
    :ghost="false"
    :title="`马匹详情 - ${horseDetail.horseName || ''}`"
    @back="goBack"
    @click="() => console.log('page header clicked')"
  >
    <template #extra>
      <a-button v-privilege="'club:horse:update'" type="primary" @click="showEditModal">
        <template #icon>
          <EditOutlined />
        </template>
        编辑
      </a-button>
    </template>
  </a-page-header>

  <a-card :loading="loading">
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="basic" tab="基本信息">
        <HorseBasicInfo :horse-detail="horseDetail" />
      </a-tab-pane>
      <a-tab-pane key="health-plan" tab="健康计划">
        <HorseHealthPlan :horse-id="horseId" />
      </a-tab-pane>
    </a-tabs>
  </a-card>

  <HorseFormModal ref="formModalRef" @reloadList="loadHorseDetail" />
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ArrowLeftOutlined, EditOutlined } from '@ant-design/icons-vue';
import { horseApi } from '/@/api/business/horse/horse-api';
import { smartSentry } from '/@/lib/smart-sentry';
import HorseFormModal from './components/horse-form-modal.vue';
import HorseBasicInfo from './components/horse-basic-info.vue';
import HorseHealthPlan from './components/horse-health-plan.vue';

const route = useRoute();
const router = useRouter();

const horseId = ref(route.query.horseId);
const activeTab = ref(route.query.tab || 'basic');
const loading = ref(false);
const formModalRef = ref();

const horseDetail = reactive({
  horseId: undefined,
  horseCode: '',
  horseName: '',
  clubName: '',
  horseType: 1,
  chipNo: '',
  passportNo: '',
  breed: '',
  gender: 1,
  color: '',
  birthDate: '',
  ownerName: '',
  responsibleCoachName: '',
  healthStatus: 1,
  workStatus: 1,
  createTime: '',
});

async function loadHorseDetail() {
  if (!horseId.value) return;

  try {
    loading.value = true;
    const res = await horseApi.detail(horseId.value);
    if (res.data) {
      Object.assign(horseDetail, res.data);
    }
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
}

function showEditModal() {
  formModalRef.value.showModal(false, horseDetail);
}

function goBack() {
  console.log('goBack function called');
  console.log('router object:', router);
  console.log('history.length:', window.history.length);
  
  try {
    // 检查是否有历史记录可以返回
    if (window.history.length > 1) {
      console.log('Using router.go(-1)');
      router.go(-1);
    } else {
      console.log('No history, redirecting to horse list');
      router.push('/club/horse/horse-list');
    }
    console.log('Navigation executed successfully');
  } catch (error) {
    console.error('Error in navigation:', error);
    // 如果出错，直接跳转到马匹列表页
    router.push('/club/horse/horse-list');
  }
}

onMounted(() => {
  console.log('Horse detail component mounted');
  console.log('router available:', !!router);
  console.log('router methods:', Object.keys(router));
  loadHorseDetail();
});
</script>

<style scoped>
.sa-page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.sa-page-header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.sa-page-header-title h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}

.sa-page-header-subtitle {
  color: #666;
  font-size: 14px;
  margin-top: 4px;
}
</style>
