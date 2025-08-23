<template>
  <div class="horse-basic-info">
    <a-row :gutter="24">
      <a-col :span="12">
        <a-descriptions title="基本信息" :column="1" bordered>
          <a-descriptions-item label="马匹编号">{{ horseDetail.horseCode }}</a-descriptions-item>
          <a-descriptions-item label="马名">{{ horseDetail.horseName }}</a-descriptions-item>
          <a-descriptions-item label="芯片号">{{ horseDetail.chipNo }}</a-descriptions-item>
          <a-descriptions-item label="护照号">{{ horseDetail.passportNo || '-' }}</a-descriptions-item>
          <a-descriptions-item label="归属">
            <span v-if="horseDetail.horseType === 1">{{ horseDetail.clubName }}</span>
            <span v-else-if="horseDetail.horseType === 2">{{ horseDetail.ownerName || '-' }}</span>
            <span v-else-if="horseDetail.horseType === 3">{{ horseDetail.responsibleCoachName || '-' }}</span>
            <span v-else>-</span>
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
      
      <a-col :span="12">
        <a-descriptions title="身体特征" :column="1" bordered>
          <a-descriptions-item label="血统">{{ horseDetail.breed }}</a-descriptions-item>
          <a-descriptions-item label="性别">
            <span v-if="horseDetail.gender === 1">公马</span>
            <span v-else-if="horseDetail.gender === 2">母马</span>
            <span v-else-if="horseDetail.gender === 3">骟马</span>
          </a-descriptions-item>
          <a-descriptions-item label="毛色">{{ horseDetail.color || '-' }}</a-descriptions-item>
          <a-descriptions-item label="出生日期">
            {{ horseDetail.birthDate ? dayjs(horseDetail.birthDate).format('YYYY-MM-DD') : '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="身高">{{ horseDetail.height ? `${horseDetail.height}cm` : '-' }}</a-descriptions-item>
          <a-descriptions-item label="体重">{{ horseDetail.weight ? `${horseDetail.weight}kg` : '-' }}</a-descriptions-item>
        </a-descriptions>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 24px;">
      <a-col :span="12">
        <a-descriptions title="管理信息" :column="1" bordered>
          <a-descriptions-item label="马主">{{ horseDetail.ownerName || '-' }}</a-descriptions-item>
          <a-descriptions-item label="责任教练">{{ horseDetail.responsibleCoachName || '-' }}</a-descriptions-item>
          <a-descriptions-item label="责任马工">{{ horseDetail.responsibleGroomName || '-' }}</a-descriptions-item>
          <a-descriptions-item label="健康状态">
            <a-tag v-if="horseDetail.healthStatus === 1" color="green">健康</a-tag>
            <a-tag v-else-if="horseDetail.healthStatus === 2" color="orange">观察</a-tag>
            <a-tag v-else-if="horseDetail.healthStatus === 3" color="red">治疗</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="工作状态">
            <a-tag v-if="horseDetail.workStatus === 1" color="green">可用</a-tag>
            <a-tag v-else-if="horseDetail.workStatus === 2" color="orange">休息</a-tag>
            <a-tag v-else-if="horseDetail.workStatus === 3" color="red">治疗</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ horseDetail.createTime ? dayjs(horseDetail.createTime).format('YYYY-MM-DD HH:mm:ss') : '-' }}
          </a-descriptions-item>
        </a-descriptions>
      </a-col>

      <a-col :span="12">
        <a-descriptions title="健康信息" :column="1" bordered>
          <a-descriptions-item label="健康状况">
            <a-tag v-if="horseDetail.healthStatus === 1" color="green">健康</a-tag>
            <a-tag v-else-if="horseDetail.healthStatus === 2" color="orange">观察</a-tag>
            <a-tag v-else-if="horseDetail.healthStatus === 3" color="red">治疗</a-tag>
            <span v-else>-</span>
          </a-descriptions-item>
          <a-descriptions-item label="备注">
            <div style="white-space: pre-wrap;">{{ horseDetail.remark || '-' }}</div>
          </a-descriptions-item>
        </a-descriptions>

        <!-- 马主马寄养信息 -->
        <a-descriptions v-if="horseDetail.horseType === 2" title="寄养信息" :column="1" bordered style="margin-top: 16px;">
          <a-descriptions-item label="寄养开始时间">
            {{ horseDetail.boardingStartDate ? dayjs(horseDetail.boardingStartDate).format('YYYY-MM-DD HH:mm:ss') : '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="寄养结束时间">
            {{ horseDetail.boardingEndDate ? dayjs(horseDetail.boardingEndDate).format('YYYY-MM-DD HH:mm:ss') : '-' }}
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';
import dayjs from 'dayjs';

defineProps({
  horseDetail: {
    type: Object,
    default: () => ({}),
  },
});
</script>

<style scoped>
.horse-basic-info {
  padding: 16px 0;
}

:deep(.ant-descriptions-title) {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 16px;
}

:deep(.ant-descriptions-item-label) {
  width: 120px;
  background-color: #fafafa;
}
</style>