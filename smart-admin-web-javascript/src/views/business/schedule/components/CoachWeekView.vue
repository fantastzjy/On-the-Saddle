<!--
  * 教练周视图组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-25
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="coach-weekly-grid">
    <a-spin :spinning="loading">
      <!-- 日期表头 -->
      <div class="week-header">
        <div class="coach-header">教练</div>
        <div v-for="day in weekDays" :key="day.date" class="day-column">
          <div class="day-name">{{ day.name }}</div>
          <div class="day-date">{{ day.date }}</div>
          <div class="time-periods">
            <div class="period-mini morning">上午</div>
            <div class="period-mini afternoon">下午</div>
            <div class="period-mini evening">晚上</div>
          </div>
        </div>
      </div>

      <!-- 教练行 -->
      <div class="coach-rows">
        <div v-for="coach in coachData" :key="coach.coachId" class="coach-row">
          <div class="coach-info">
            <div class="coach-name">{{ coach.coachName }}</div>
          </div>
          
          <div v-for="day in coach.dailyStats" :key="day.date" class="day-slots">
            <div class="period-slot morning" 
                 :class="{ 'has-lessons': day.morningCount > 0 }"
                 @click="onSlotClick(coach.coachId, day.date, 'morning')">
              <span class="lesson-count" v-if="day.morningCount > 0">{{ day.morningCount }}</span>
            </div>
            <div class="period-slot afternoon"
                 :class="{ 'has-lessons': day.afternoonCount > 0 }"
                 @click="onSlotClick(coach.coachId, day.date, 'afternoon')">
              <span class="lesson-count" v-if="day.afternoonCount > 0">{{ day.afternoonCount }}</span>
            </div>
            <div class="period-slot evening"
                 :class="{ 'has-lessons': day.eveningCount > 0 }"
                 @click="onSlotClick(coach.coachId, day.date, 'evening')">
              <span class="lesson-count" v-if="day.eveningCount > 0">{{ day.eveningCount }}</span>
            </div>
          </div>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';

dayjs.locale('zh-cn');

// ======================== Props & Emits ========================
const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  coachData: {
    type: Array,
    default: () => []
  },
  weekStartDate: {
    type: String,
    default: ''
  },
  weekEndDate: {
    type: String,
    default: ''
  }
});

const emit = defineEmits([
  'slot-click'
]);

// ======================== 计算属性 ========================
const weekDays = computed(() => {
  if (!props.weekStartDate) {
    return [];
  }
  
  const days = [];
  const startDate = dayjs(props.weekStartDate);
  
  for (let i = 0; i < 7; i++) {
    const day = startDate.add(i, 'day');
    days.push({
      date: day.format('YYYY-MM-DD'),
      name: day.format('dddd'),
      date: day.format('MM/DD')
    });
  }
  
  return days;
});

// ======================== 方法 ========================
function onSlotClick(coachId, date, period) {
  emit('slot-click', { coachId, date, period });
}
</script>

<style scoped>
.coach-weekly-grid {
  width: 100%;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  overflow-x: auto;
}

/* 周表头 */
.week-header {
  display: grid;
  grid-template-columns: 120px repeat(7, 1fr);
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
  min-width: 800px;
}

.coach-header {
  padding: 12px 8px;
  text-align: center;
  font-weight: 600;
  border-right: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.day-column {
  padding: 8px;
  text-align: center;
  border-right: 1px solid #f0f0f0;
}

.day-name {
  font-size: 12px;
  font-weight: 500;
  color: #595959;
  margin-bottom: 4px;
}

.day-date {
  font-size: 14px;
  font-weight: 600;
  color: #262626;
  margin-bottom: 8px;
}

.time-periods {
  display: flex;
  justify-content: space-between;
  gap: 2px;
}

.period-mini {
  flex: 1;
  font-size: 10px;
  padding: 2px;
  border-radius: 2px;
  text-align: center;
  color: #8c8c8c;
}

.period-mini.morning {
  background: #fff7e6;
  color: #fa8c16;
}

.period-mini.afternoon {
  background: #e6f7ff;
  color: #1890ff;
}

.period-mini.evening {
  background: #f6ffed;
  color: #52c41a;
}

/* 教练行 */
.coach-rows {
  min-width: 800px;
}

.coach-row {
  display: grid;
  grid-template-columns: 120px repeat(7, 1fr);
  border-bottom: 1px solid #f0f0f0;
}

.coach-row:hover {
  background: #f8f9fa;
}

.coach-info {
  padding: 16px 8px;
  border-right: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coach-name {
  font-weight: 600;
  font-size: 14px;
  color: #262626;
  text-align: center;
}

.day-slots {
  display: flex;
  flex-direction: column;
  border-right: 1px solid #f0f0f0;
}

.period-slot {
  flex: 1;
  min-height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.period-slot:not(:last-child) {
  border-bottom: 1px solid #f5f5f5;
}

.period-slot:hover {
  background: #f0f9ff;
}

.period-slot.has-lessons {
  background: #e6f7ff;
}

.period-slot.morning.has-lessons {
  background: #fff7e6;
}

.period-slot.afternoon.has-lessons {
  background: #e6f7ff;
}

.period-slot.evening.has-lessons {
  background: #f6ffed;
}

.lesson-count {
  font-weight: 600;
  font-size: 16px;
  color: #1890ff;
}

.period-slot.morning .lesson-count {
  color: #fa8c16;
}

.period-slot.afternoon .lesson-count {
  color: #1890ff;
}

.period-slot.evening .lesson-count {
  color: #52c41a;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .week-header,
  .coach-row {
    grid-template-columns: 100px repeat(7, 1fr);
  }
  
  .coach-header {
    padding: 8px 4px;
  }
  
  .coach-info {
    padding: 12px 4px;
  }
  
  .day-column {
    padding: 6px 4px;
  }
}
</style>