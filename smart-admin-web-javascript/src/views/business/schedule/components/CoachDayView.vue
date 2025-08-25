<!--
  * 教练日视图组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-25
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="coach-daily-grid">
    <a-spin :spinning="loading">
      <!-- 时间表头 -->
      <div class="time-header">
        <div class="coach-header diagonal-header">
          <span class="diagonal-top">时间</span>
          <span class="diagonal-bottom">教练</span>
        </div>
        <!-- 连续时间点 -->
        <div class="hour-slot">09:00</div>
        <div class="hour-slot">10:00</div>
        <div class="hour-slot">11:00</div>
        <div class="hour-slot lunch-break-after">12:00</div>
        <div class="hour-slot">13:00</div>
        <div class="hour-slot">14:00</div>
        <div class="hour-slot">15:00</div>
        <div class="hour-slot">16:00</div>
        <div class="hour-slot">17:00</div>
        <div class="hour-slot dinner-break-after">18:00</div>
        <div class="hour-slot">19:00</div>
        <div class="hour-slot">20:00</div>
      </div>

      <!-- 教练行数据 -->
      <div class="coach-rows">
        <div v-for="coach in coachData" :key="coach.coachId" class="coach-row">
          <!-- 教练信息列 -->
          <div class="coach-info">
            <div class="coach-name">{{ coach.coachName }}</div>
            <div class="coach-status">{{ coach.coachStatus }}</div>
          </div>
          
          <!-- 连续时间格子 -->
          <div v-for="timeSlot in timeSlots" :key="timeSlot" 
               class="hour-cell" 
               :class="{ 
                 'lunch-break-after': timeSlot === '12:00',
                 'dinner-break-after': timeSlot === '18:00'
               }"
               @click="onSlotClick(coach.coachId, getSlotData(coach, timeSlot))">
            <ScheduleCard 
              v-if="getSlotData(coach, timeSlot)?.scheduleId"
              :schedule="getSlotData(coach, timeSlot)"
              @click="onScheduleClick(getSlotData(coach, timeSlot).scheduleId)" />
            <div v-else class="empty-slot">
              <a-button type="dashed" size="small" @click.stop="onCreateSchedule(coach.coachId, timeSlot)">
                +
              </a-button>
            </div>
          </div>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import ScheduleCard from './ScheduleCard.vue';

// 定义完整时间点（包含13点）
const timeSlots = ['09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00'];

// 获取指定时间的课程数据
function getSlotData(coach, timeSlot) {
  const hour = timeSlot.split(':')[0];
  const hourInt = parseInt(hour);
  
  let slots = [];
  if (hourInt >= 9 && hourInt <= 12) {
    slots = coach.morningSlots || [];
  } else if (hourInt >= 13 && hourInt <= 18) {
    slots = coach.afternoonSlots || [];
  } else if (hourInt >= 19 && hourInt <= 20) {
    slots = coach.eveningSlots || [];
  }
  
  return slots.find(slot => slot.hourSlot === timeSlot) || { hourSlot: timeSlot };
}

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
  queryDate: {
    type: String,
    default: ''
  }
});

const emit = defineEmits([
  'schedule-click',
  'slot-click', 
  'create-schedule'
]);

// ======================== 方法 ========================
function onScheduleClick(scheduleId) {
  emit('schedule-click', scheduleId);
}

function onSlotClick(coachId, slot) {
  emit('slot-click', { coachId, slot });
}

function onCreateSchedule(coachId, hourSlot) {
  emit('create-schedule', { coachId, hourSlot });
}
</script>

<style scoped>
.coach-daily-grid {
  width: 100%;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  overflow-x: auto;
}

/* 时间表头 */
.time-header {
  display: grid;
  grid-template-columns: 90px repeat(12, 1fr);
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
  min-width: 1100px;
}

.coach-header {
  padding: 12px 6px;
  text-align: center;
  font-weight: 600;
  border-right: 1px solid #f0f0f0;
  position: relative;
}

/* 对角线表头样式 */
.diagonal-header {
  background: linear-gradient(to top right, transparent 49%, #d9d9d9 49%, #d9d9d9 51%, transparent 51%);
  padding: 8px;
}

.diagonal-top {
  position: absolute;
  top: 8px;
  right: 12px;
  font-size: 12px;
  font-weight: 500;
  color: #666;
}

.diagonal-bottom {
  position: absolute;
  bottom: 8px;
  left: 12px;
  font-size: 12px;
  font-weight: 500;
  color: #666;
}

.hour-slot {
  padding: 8px 4px;
  text-align: center;
  font-size: 13px;
  font-weight: 500;
  border-right: 1px solid #f0f0f0;
  color: #595959;
}

/* 浅蓝色虚线分界线：12点和18点后面 */
.hour-slot.lunch-break-after {
  border-right: 2px dashed #69c0ff;
}

.hour-slot.dinner-break-after {
  border-right: 2px dashed #69c0ff;
}

/* 教练行 */
.coach-rows {
  min-width: 1100px;
}

.coach-row {
  display: grid;
  grid-template-columns: 90px repeat(12, 1fr);
  border-bottom: 1px solid #f0f0f0;
}

.coach-row:hover {
  background: #f8f9fa;
}

.coach-info {
  padding: 12px 6px;
  border-right: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coach-name {
  font-weight: 600;
  font-size: 13px;
  color: #262626;
  line-height: 1.2;
}

.coach-status {
  font-size: 12px;
  color: #52c41a;
  margin-top: 2px;
}

.hour-cell {
  padding: 4px;
  border-right: 1px solid #f0f0f0;
  min-height: 100px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 浅蓝色虚线分界线：12点和18点后面 */
.hour-cell.lunch-break-after {
  border-right: 2px dashed #69c0ff;
}

.hour-cell.dinner-break-after {
  border-right: 2px dashed #69c0ff;
}

.empty-slot {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.empty-slot:hover {
  background: #f0f9ff;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .time-header,
  .coach-row {
    grid-template-columns: 80px repeat(12, minmax(55px, 1fr));
  }
  
  .coach-header {
    padding: 8px 4px;
  }
  
  .coach-info {
    padding: 8px 4px;
  }
  
  .hour-slot {
    padding: 6px 2px;
    font-size: 12px;
  }
}
</style>