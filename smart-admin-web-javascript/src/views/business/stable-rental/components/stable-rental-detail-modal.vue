<template>
  <div>
    <a-modal
      v-model:open="visible"
      title="马房租赁详情"
      :width="800"
      :footer="null"
      @cancel="onCancel"
    >
      <div v-if="detail" class="detail-content">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="租赁单号">{{ detail.rentalNo }}</a-descriptions-item>
          <a-descriptions-item label="租赁状态">
            <a-tag v-if="detail.rentalStatus === 1" color="green">{{ detail.rentalStatusDesc }}</a-tag>
            <a-tag v-else-if="detail.rentalStatus === 2" color="red">{{ detail.rentalStatusDesc }}</a-tag>
            <a-tag v-else color="orange">{{ detail.rentalStatusDesc }}</a-tag>
          </a-descriptions-item>
          
          <a-descriptions-item label="出租人">{{ detail.lessorName }}</a-descriptions-item>
          <a-descriptions-item label="租赁人">
            <a-button 
              type="link" 
              size="small" 
              @click="goToCoachDetail(detail.lesseeId)"
              v-if="detail.lesseeId"
            >
              {{ detail.lesseeName }}
            </a-button>
            <span v-else>{{ detail.lesseeName }}</span>
          </a-descriptions-item>
          
          <a-descriptions-item label="租赁俱乐部" :span="2">{{ detail.clubName }}</a-descriptions-item>
          
          <a-descriptions-item label="租赁开始时间">{{ detail.rentalStartTime ? detail.rentalStartTime.split(' ')[0] : '-' }}</a-descriptions-item>
          <a-descriptions-item label="租赁结束时间">{{ detail.rentalEndTime ? detail.rentalEndTime.split(' ')[0] : '-' }}</a-descriptions-item>
          
          <a-descriptions-item label="租赁金额" :span="2">
            <span class="amount">¥{{ detail.rentalAmount }}</span>
          </a-descriptions-item>
          
          <a-descriptions-item label="备注" :span="2">
            {{ detail.remark || '-' }}
          </a-descriptions-item>
          
          <a-descriptions-item label="创建人">{{ detail.createBy }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
          
          <a-descriptions-item label="更新人">{{ detail.updateBy || '-' }}</a-descriptions-item>
          <a-descriptions-item label="更新时间">{{ detail.updateTime || '-' }}</a-descriptions-item>
        </a-descriptions>
      </div>
      
      <div v-else class="loading-content">
        <a-spin size="large" />
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { stableRentalApi } from '/@/api/business/stable-rental-api';
import { smartSentry } from '/@/lib/smart-sentry';

// 弹窗状态
const visible = ref(false);
const detail = ref(null);

// 显示弹窗
async function showModal(rentalId) {
  visible.value = true;
  detail.value = null;
  
  try {
    const result = await stableRentalApi.getDetailById(rentalId);
    detail.value = result.data;
  } catch (e) {
    smartSentry.captureError(e);
  }
}

// 取消
function onCancel() {
  visible.value = false;
  detail.value = null;
}

// 跳转到教练详情页面
function goToCoachDetail(coachId) {
  if (coachId) {
    window.open(`#/club/coach/coach-detail?coachId=${coachId}`, '_blank');
  }
}

// 暴露方法
defineExpose({
  showModal,
});
</script>

<style scoped>
.detail-content {
  padding: 20px 0;
}

.loading-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.amount {
  font-size: 16px;
  font-weight: bold;
  color: #ff4d4f;
}
</style>