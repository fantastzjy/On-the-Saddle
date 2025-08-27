<!--
  * 课程卡片组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-25
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div 
    class="schedule-card" 
    :class="getCardClass()"
    @click="onClick"
  >
    <div class="schedule-content" :class="{ 'simplified': simplified }">
      <div class="member-name">{{ schedule.memberName }}</div>
      
      <!-- 简化模式：只显示马匹名 -->
      <template v-if="simplified">
        <div class="horse-name">{{ schedule.horseName || '待分配' }}</div>
      </template>
      
      <!-- 完整模式：显示详细信息 -->
      <template v-else>
        <div class="schedule-details">
          <div class="horse-course">
            {{ schedule.horseName || '待分配' }} | {{ schedule.courseName || '基础课程' }}
          </div>
          <div class="time-info">
            {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
          </div>
        </div>
      </template>
    </div>
    
    <!-- 状态标识 -->
    <div class="status-indicator" :class="getStatusClass()"></div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import dayjs from 'dayjs';

// ======================== Props & Emits ========================
const props = defineProps({
  schedule: {
    type: Object,
    required: true
  },
  simplified: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['click']);

// ======================== 计算属性 ========================
const getCardClass = () => {
  const baseClass = 'schedule-card';
  const statusClass = getStatusClass();
  const typeClass = `type-${props.schedule.productType || 1}`;
  const modeClass = props.simplified ? 'simplified' : '';
  
  return [baseClass, statusClass, typeClass, modeClass].filter(Boolean);
};

const getStatusClass = () => {
  switch (props.schedule.lessonStatus) {
    case 1:
      return 'status-pending'; // 待上课
    case 2:
      return 'status-ongoing'; // 进行中
    case 3:
      return 'status-completed'; // 已完成
    case 4:
      return 'status-cancelled'; // 已取消
    default:
      return 'status-pending';
  }
};

// ======================== 方法 ========================
function formatTime(timeString) {
  if (!timeString) return '';
  return dayjs(timeString).format('HH:mm');
}

function onClick() {
  emit('click', props.schedule.scheduleId);
}
</script>

<style scoped>
.schedule-card {
  width: 100%;
  height: 70px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #d9d9d9;
  padding: 8px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
}

/* 简化模式的高度调整 */
.schedule-card.simplified {
  height: 60px;
  padding: 6px 8px;
}

.schedule-card:hover {
  border-color: #1890ff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.schedule-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* 简化模式的内容布局 */
.schedule-content.simplified {
  justify-content: center;
  gap: 2px;
}

.member-name {
  font-weight: 600;
  font-size: 14px;
  color: #262626;
  line-height: 1.2;
  margin-bottom: 4px;
}

/* 简化模式下的会员名样式 */
.simplified .member-name {
  font-size: 13px;
  margin-bottom: 1px;
}

/* 简化模式下的马匹名样式 */
.horse-name {
  font-size: 11px;
  color: #8c8c8c;
  line-height: 1.2;
}

.schedule-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.horse-course {
  font-size: 12px;
  color: #595959;
  line-height: 1.2;
  margin-bottom: 2px;
}

.time-info {
  font-size: 11px;
  color: #8c8c8c;
  line-height: 1.2;
}

.status-indicator {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
}

/* 状态颜色 */
.status-pending .status-indicator {
  background: #faad14; /* 待上课 - 橙色 */
}

.status-ongoing .status-indicator {
  background: #1890ff; /* 进行中 - 蓝色 */
}

.status-completed .status-indicator {
  background: #52c41a; /* 已完成 - 绿色 */
}

.status-cancelled .status-indicator {
  background: #ff4d4f; /* 已取消 - 红色 */
}

/* 课程类型样式 */
.type-1 {
  border-left: 3px solid #1890ff; /* 单次课程 */
}

.type-2 {
  border-left: 3px solid #722ed1; /* 课时包 */
}

/* 状态对应的卡片背景色 */
.status-pending {
  background: #fffbe6;
  border-color: #faad14;
}

.status-ongoing {
  background: #e6f7ff;
  border-color: #1890ff;
}

.status-completed {
  background: #f6ffed;
  border-color: #52c41a;
}

.status-cancelled {
  background: #fff2f0;
  border-color: #ff4d4f;
  opacity: 0.7;
}

.status-cancelled .member-name,
.status-cancelled .horse-course {
  text-decoration: line-through;
  color: #8c8c8c;
}

/* 紧凑模式 */
.schedule-card.compact {
  height: 50px;
  padding: 6px;
}

.compact .member-name {
  font-size: 12px;
}

.compact .horse-course {
  font-size: 11px;
}

.compact .time-info {
  font-size: 10px;
}
</style>