<!--
  * 俱乐部详情
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-01-08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-card size="small" :loading="loading">
    <template #title>
      <a-space>
        <ArrowLeftOutlined @click="goBack" style="cursor: pointer" />
        <span>俱乐部详情</span>
      </a-space>
    </template>

    <template #extra>
      <a-button @click="edit" v-privilege="'mclub:club:update'" type="primary">
        <template #icon>
          <EditOutlined />
        </template>
        编辑
      </a-button>
    </template>

    <a-row :gutter="24" v-if="clubData">
      <a-col :span="24">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="俱乐部名称" :span="1">
            {{ clubData.clubName || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="俱乐部编码" :span="1">
            {{ clubData.clubCode || '-' }}
          </a-descriptions-item>
          
          <a-descriptions-item label="LOGO" :span="1">
            <img v-if="clubData.logoUrl" :src="clubData.logoUrl" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;" />
            <span v-else>-</span>
          </a-descriptions-item>
          <a-descriptions-item label="置顶图片" :span="1">
            <img v-if="clubData.bannerUrl" :src="clubData.bannerUrl" style="width: 100px; height: 60px; object-fit: cover; border-radius: 8px;" />
            <span v-else>-</span>
          </a-descriptions-item>

          <a-descriptions-item label="营业时间" :span="2">
            <span v-if="clubData.businessStartTime && clubData.businessEndTime">
              {{ clubData.businessStartTime }} - {{ clubData.businessEndTime }}
            </span>
            <span v-else>-</span>
          </a-descriptions-item>

          <a-descriptions-item label="详细地址" :span="2">
            {{ clubData.address || '-' }}
          </a-descriptions-item>

          <a-descriptions-item label="电话" :span="1">
            {{ clubData.phone || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="联系电话" :span="1">
            {{ clubData.contactPhone || '-' }}
          </a-descriptions-item>

          <a-descriptions-item label="省份" :span="1">
            {{ clubData.province || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="城市" :span="1">
            {{ clubData.city || '-' }}
          </a-descriptions-item>

          <a-descriptions-item label="区县" :span="1">
            {{ clubData.district || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="联系人" :span="1">
            {{ clubData.contactPerson || '-' }}
          </a-descriptions-item>

          <a-descriptions-item label="邮箱" :span="1">
            {{ clubData.email || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="法人代表" :span="1">
            {{ clubData.legalPerson || '-' }}
          </a-descriptions-item>

          <a-descriptions-item label="经纬度" :span="2">
            <span v-if="clubData.latitude && clubData.longitude">
              纬度: {{ clubData.latitude }}, 经度: {{ clubData.longitude }}
            </span>
            <span v-else>-</span>
          </a-descriptions-item>

          <a-descriptions-item label="到期时间" :span="1">
            {{ clubData.expireDate || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="状态" :span="1">
            <a-tag :color="clubData.isValid ? 'green' : 'red'">
              {{ clubData.isValid ? '有效' : '无效' }}
            </a-tag>
          </a-descriptions-item>

          <a-descriptions-item label="营业执照" :span="2">
            <img v-if="clubData.businessLicenseUrl" :src="clubData.businessLicenseUrl" style="width: 200px; height: auto; border-radius: 8px;" />
            <span v-else>-</span>
          </a-descriptions-item>

          <a-descriptions-item label="俱乐部详情" :span="2">
            <div v-if="clubData.description" style="white-space: pre-wrap;">{{ clubData.description }}</div>
            <span v-else>-</span>
          </a-descriptions-item>

          <a-descriptions-item label="荣誉信息" :span="2">
            <div v-if="clubData.honorInfo" style="white-space: pre-wrap;">{{ clubData.honorInfo }}</div>
            <span v-else>-</span>
          </a-descriptions-item>

          <a-descriptions-item label="约课需知" :span="2">
            <div v-if="clubData.bookingNotice" style="white-space: pre-wrap;">{{ clubData.bookingNotice }}</div>
            <span v-else>-</span>
          </a-descriptions-item>

          <a-descriptions-item label="创建人" :span="1">
            {{ clubData.createBy || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="创建时间" :span="1">
            {{ clubData.createTime || '-' }}
          </a-descriptions-item>

          <a-descriptions-item label="更新人" :span="1">
            {{ clubData.updateBy || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="更新时间" :span="1">
            {{ clubData.updateTime || '-' }}
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
    </a-row>
  </a-card>

  <!-- 俱乐部编辑弹窗 -->
  <ClubFormModal ref="clubFormModal" @refresh="getDetail" />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ArrowLeftOutlined, EditOutlined } from '@ant-design/icons-vue';
import { clubApi } from '/@/api/business/club/club-api';
import { smartSentry } from '/@/lib/smart-sentry';
import ClubFormModal from './components/club-form-modal.vue';

const route = useRoute();
const router = useRouter();

const loading = ref(false);
const clubData = ref(null);
const clubFormModal = ref();

const clubId = route.query.clubId;

function goBack() {
  router.go(-1);
}

function edit() {
  clubFormModal.value.show(clubId);
}

async function getDetail() {
  if (!clubId) {
    return;
  }
  
  try {
    loading.value = true;
    let res = await clubApi.detail(clubId);
    clubData.value = res.data;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  getDetail();
});
</script>