<template>
  <div class="sa-token-privilege">
    <div class="common-container">
      <div class="sa-page-header">
        <div class="sa-page-header-left">
          <a-button @click="goBack">
            <template #icon>
              <ArrowLeftOutlined />
            </template>
            返回
          </a-button>
          <div class="sa-page-header-title">
            <h3>{{ horseDetail.horseName || '马匹详情' }}</h3>
            <div class="sa-page-header-subtitle">
              编号：{{ horseDetail.horseCode }} | 芯片号：{{ horseDetail.chipNo }}
            </div>
          </div>
        </div>
        <div class="sa-page-header-right">
          <a-button v-privilege="'club:horse:update'" type="primary" @click="showEditModal">
            <template #icon>
              <EditOutlined />
            </template>
            编辑
          </a-button>
        </div>
      </div>

      <a-card :loading="loading">
        <a-tabs v-model:activeKey="activeTab">
          <a-tab-pane key="basic" tab="基本信息">
            <HorseBasicInfo :horse-detail="horseDetail" />
          </a-tab-pane>
          <a-tab-pane key="health-plan" tab="健康计划">
            <HorseHealthPlan :horse-id="horseId" />
          </a-tab-pane>
          <a-tab-pane key="health-record" tab="健康记录">
            <HorseHealthRecord :horse-id="horseId" />
          </a-tab-pane>
        </a-tabs>
      </a-card>
    </div>

    <HorseFormModal ref="formModalRef" @reloadList="loadHorseDetail" />
  </div>
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
import HorseHealthRecord from './components/horse-health-record.vue';

const route = useRoute();
const router = useRouter();

const horseId = ref(route.query.horseId);
const activeTab = ref('basic');
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
  router.go(-1);
}

onMounted(() => {
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