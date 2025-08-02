<template>
  <div class="horse-basic-info">
    <a-row :gutter="24">
      <a-col :span="12">
        <a-descriptions title="基本信息" :column="1" bordered>
          <a-descriptions-item label="马匹编号">{{ horseDetail.horseCode }}</a-descriptions-item>
          <a-descriptions-item label="马名">{{ horseDetail.horseName }}</a-descriptions-item>
          <a-descriptions-item label="芯片号">{{ horseDetail.chipNumber }}</a-descriptions-item>
          <a-descriptions-item label="护照号">{{ horseDetail.passportNumber || '-' }}</a-descriptions-item>
          <a-descriptions-item label="俱乐部">{{ horseDetail.clubName }}</a-descriptions-item>
          <a-descriptions-item label="马匹类型">
            <a-tag v-if="horseDetail.horseType === 'club'" color="blue">俱乐部马匹</a-tag>
            <a-tag v-else-if="horseDetail.horseType === 'owner'" color="green">马主寄养</a-tag>
            <a-tag v-else-if="horseDetail.horseType === 'trainer'" color="orange">教练自有</a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
      
      <a-col :span="12">
        <a-descriptions title="身体特征" :column="1" bordered>
          <a-descriptions-item label="品种">{{ horseDetail.breed }}</a-descriptions-item>
          <a-descriptions-item label="性别">
            <span v-if="horseDetail.gender === 'stallion'">种马</span>
            <span v-else-if="horseDetail.gender === 'mare'">母马</span>
            <span v-else-if="horseDetail.gender === 'gelding'">阉马</span>
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
          <a-descriptions-item label="所属教练">{{ horseDetail.coachName || '-' }}</a-descriptions-item>
          <a-descriptions-item label="责任教练">{{ horseDetail.responsibleCoachName || '-' }}</a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag v-if="horseDetail.status === 'normal'" color="green">正常</a-tag>
            <a-tag v-else-if="horseDetail.status === 'injured'" color="red">受伤</a-tag>
            <a-tag v-else-if="horseDetail.status === 'sick'" color="orange">生病</a-tag>
            <a-tag v-else-if="horseDetail.status === 'retired'" color="gray">退役</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ horseDetail.createTime ? dayjs(horseDetail.createTime).format('YYYY-MM-DD HH:mm:ss') : '-' }}
          </a-descriptions-item>
        </a-descriptions>
      </a-col>

      <a-col :span="12">
        <a-descriptions title="健康信息" :column="1" bordered>
          <a-descriptions-item label="健康状况">
            <div style="white-space: pre-wrap;">{{ horseDetail.healthStatus || '-' }}</div>
          </a-descriptions-item>
          <a-descriptions-item label="备注">
            <div style="white-space: pre-wrap;">{{ horseDetail.remark || '-' }}</div>
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