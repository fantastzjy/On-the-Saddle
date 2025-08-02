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
    :title="`教练详情 - ${coach.coachName || ''}`"
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
            <a-descriptions-item label="教练姓名">
              {{ coach.coachName || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="教练编号">
              {{ coach.coachCode || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="性别">
              {{ coach.gender === 1 ? '男' : coach.gender === 2 ? '女' : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="出生日期">
              {{ coach.birthDate || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="手机号码">
              {{ coach.phone || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="邮箱">
              {{ coach.email || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="紧急联系人">
              {{ coach.emergencyContact || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="紧急联系电话">
              {{ coach.emergencyPhone || '-' }}
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
      <!-- 职业信息 -->
      <a-col :span="12">
        <a-card title="职业信息" size="small">
          <a-descriptions :column="1" bordered size="small">
            <a-descriptions-item label="专业等级">
              {{ coach.professionalLevel || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="从业年限">
              {{ coach.yearsExperience ? `${coach.yearsExperience}年` : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="所属俱乐部">
              {{ coach.clubName || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="入职日期">
              {{ coach.entryDate || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="薪资">
              {{ coach.salary ? `¥${coach.salary}` : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="在职状态">
              <a-tag :color="coach.isActive ? 'green' : 'red'">
                {{ coach.isActive ? '在职' : '离职' }}
              </a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>

      <!-- 其他信息 -->
      <a-col :span="12">
        <a-card title="其他信息" size="small">
          <a-descriptions :column="1" bordered size="small">
            <a-descriptions-item label="创建时间">
              {{ coach.createTime || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="更新时间">
              {{ coach.updateTime || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="创建人">
              {{ coach.createUserName || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="更新人">
              {{ coach.updateUserName || '-' }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px;">
      <!-- 专业特长 -->
      <a-col :span="24">
        <a-card title="专业特长" size="small">
          <div style="white-space: pre-wrap; line-height: 1.6;">
            {{ coach.speciality || '暂无' }}
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px;">
      <!-- 个人简介 -->
      <a-col :span="24">
        <a-card title="个人简介" size="small">
          <div style="white-space: pre-wrap; line-height: 1.6;">
            {{ coach.bio || '暂无' }}
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px;">
      <!-- 备注 -->
      <a-col :span="24">
        <a-card title="备注" size="small">
          <div style="white-space: pre-wrap; line-height: 1.6;">
            {{ coach.remark || '暂无' }}
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

// ----------------------- 生命周期 -----------------------

onMounted(() => {
  loadCoachDetail();
});
</script>