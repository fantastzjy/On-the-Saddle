<!--
  * 教练月视图组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-25
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="coach-monthly-view">
    <a-spin :spinning="loading">
      <!-- 月历部分 -->
      <div class="monthly-calendar">
        <div class="calendar-header">
          <h3>{{ currentMonth }}</h3>
        </div>
        
        <!-- 周标题 -->
        <div class="week-header">
          <div v-for="day in weekNames" :key="day" class="day-header">{{ day }}</div>
        </div>
        
        <!-- 日历网格 -->
        <div class="calendar-grid">
          <div v-for="day in calendarDays" :key="day.date" 
               class="calendar-day" 
               :class="{ 
                 'other-month': day.isOtherMonth,
                 'today': day.isToday,
                 'has-lessons': day.totalLessons > 0
               }"
               @click="onDayClick(day)">
            <div class="day-number">{{ day.dayOfMonth }}</div>
            <div class="lesson-indicator" v-if="day.totalLessons > 0">
              <div class="lesson-count">{{ day.totalLessons }}课</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 教练统计部分 -->
      <div class="coach-statistics">
        <h4>教练工作量统计</h4>
        <div class="stats-list">
          <div v-for="coach in coachStats" :key="coach.coachId" class="coach-stat-item">
            <div class="coach-info">
              <div class="coach-name">{{ coach.coachName }}</div>
              <div class="lesson-total">{{ coach.totalLessons }}课时</div>
            </div>
            <div class="progress-section">
              <div class="progress-bar">
                <div class="progress-fill" 
                     :style="{ width: `${coach.percentage}%` }"></div>
              </div>
              <div class="percentage">{{ coach.percentage }}%</div>
            </div>
            <div class="daily-breakdown">
              <div v-for="day in coach.dailyStats" 
                   :key="day.date" 
                   class="daily-stat"
                   :class="getDayStatClass(day.lessonCount)"
                   :title="`${day.date}: ${day.lessonCount}课`">
              </div>
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
  year: {
    type: Number,
    default: 0
  },
  month: {
    type: Number,
    default: 0
  },
  calendarDays: {
    type: Array,
    default: () => []
  },
  coachStats: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits([
  'day-click'
]);

// ======================== 计算属性 ========================
const currentMonth = computed(() => {
  if (!props.year || !props.month) {
    return dayjs().format('YYYY年MM月');
  }
  return `${props.year}年${String(props.month).padStart(2, '0')}月`;
});

const weekNames = computed(() => {
  return ['日', '一', '二', '三', '四', '五', '六'];
});

// ======================== 方法 ========================
function onDayClick(day) {
  emit('day-click', day);
}

function getDayStatClass(lessonCount) {
  if (lessonCount === 0) return 'stat-empty';
  if (lessonCount <= 2) return 'stat-light';
  if (lessonCount <= 4) return 'stat-medium';
  return 'stat-heavy';
}
</script>

<style scoped>
.coach-monthly-view {
  width: 100%;
}

/* 月历部分 */
.monthly-calendar {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  margin-bottom: 24px;
}

.calendar-header {
  padding: 16px;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
}

.calendar-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #262626;
}

.week-header {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  background: #fafafa;
}

.day-header {
  padding: 12px 8px;
  text-align: center;
  font-weight: 500;
  color: #595959;
  border-right: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.day-header:last-child {
  border-right: none;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
}

.calendar-day {
  aspect-ratio: 1;
  padding: 8px;
  border-right: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
  display: flex;
  flex-direction: column;
}

.calendar-day:last-child {
  border-right: none;
}

.calendar-day:hover {
  background: #f5f5f5;
}

.calendar-day.other-month {
  background: #fafafa;
  color: #bfbfbf;
}

.calendar-day.today {
  background: #e6f7ff;
  border-color: #1890ff;
}

.calendar-day.has-lessons {
  background: #f6ffed;
}

.day-number {
  font-weight: 500;
  margin-bottom: 4px;
  color: inherit;
}

.lesson-indicator {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lesson-count {
  background: #52c41a;
  color: white;
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 10px;
  font-weight: 500;
}

/* 教练统计部分 */
.coach-statistics {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  padding: 20px;
}

.coach-statistics h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: #262626;
}

.stats-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.coach-stat-item {
  display: grid;
  grid-template-columns: 120px 1fr 200px;
  align-items: center;
  gap: 16px;
  padding: 12px;
  background: #fafafa;
  border-radius: 6px;
}

.coach-info {
  text-align: center;
}

.coach-name {
  font-weight: 600;
  font-size: 14px;
  color: #262626;
  margin-bottom: 4px;
}

.lesson-total {
  font-size: 12px;
  color: #595959;
}

.progress-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #1890ff, #52c41a);
  transition: width 0.3s ease;
}

.percentage {
  font-weight: 600;
  font-size: 12px;
  color: #1890ff;
  min-width: 40px;
  text-align: right;
}

.daily-breakdown {
  display: grid;
  grid-template-columns: repeat(31, 1fr);
  gap: 1px;
}

.daily-stat {
  width: 6px;
  height: 6px;
  border-radius: 1px;
  transition: all 0.2s;
}

.daily-stat:hover {
  transform: scale(1.5);
}

.stat-empty {
  background: #f0f0f0;
}

.stat-light {
  background: #bae7ff;
}

.stat-medium {
  background: #69c0ff;
}

.stat-heavy {
  background: #1890ff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .coach-stat-item {
    grid-template-columns: 1fr;
    gap: 12px;
    text-align: center;
  }
  
  .daily-breakdown {
    justify-content: center;
  }
  
  .progress-section {
    justify-content: center;
  }
}
</style>