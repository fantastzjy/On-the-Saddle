<!--
  * 日历视图组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="calendar-view">
    <a-spin :spinning="loading">
      <!-- 日历头部控制栏 -->
      <div class="calendar-header">
        <div class="calendar-nav">
          <a-button-group>
            <a-button @click="goToPrevious">
              <template #icon>
                <LeftOutlined />
              </template>
            </a-button>
            <a-button @click="goToToday">今天</a-button>
            <a-button @click="goToNext">
              <template #icon>
                <RightOutlined />
              </template>
            </a-button>
          </a-button-group>
          
          <h3 class="calendar-title">{{ currentDateTitle }}</h3>
        </div>

        <div class="calendar-controls">
          <a-radio-group v-model:value="currentViewType" @change="onViewTypeChange">
            <a-radio-button 
              v-for="item in Object.values(SCHEDULE_VIEW_TYPE_ENUM)" 
              :key="item.value"
              :value="item.value"
            >
              {{ item.desc }}
            </a-radio-button>
          </a-radio-group>
        </div>
      </div>

      <!-- 日历内容区 -->
      <div class="calendar-content">
        <!-- 日视图 -->
        <div v-if="currentViewType === SCHEDULE_VIEW_TYPE_ENUM.DAY.value" class="day-view">
          <div class="time-grid">
            <div class="time-header">
              <div class="time-label">时间</div>
              <div class="date-header">
                {{ formatDate(currentDate, 'MM月DD日 dddd') }}
              </div>
            </div>
            
            <div class="time-slots">
              <div 
                v-for="timeSlot in dayTimeSlots" 
                :key="timeSlot.key"
                class="time-slot"
                @click="onTimeSlotClick(timeSlot)"
              >
                <div class="time-label">{{ timeSlot.time }}</div>
                <div class="slot-content">
                  <div 
                    v-for="schedule in getSchedulesInTimeSlot(timeSlot)"
                    :key="schedule.scheduleId"
                    class="schedule-item"
                    :class="getScheduleItemClass(schedule)"
                    @click.stop="onScheduleClick(schedule)"
                    @mousedown="onScheduleMouseDown(schedule, $event)"
                  >
                    <div class="schedule-content">
                      <div class="schedule-title">{{ schedule.memberName }}</div>
                      <div class="schedule-info">
                        {{ schedule.coachName }} | {{ getDuration(schedule) }}分钟
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 周视图 -->
        <div v-else-if="currentViewType === SCHEDULE_VIEW_TYPE_ENUM.WEEK.value" class="week-view">
          <div class="week-grid">
            <div class="week-header">
              <div class="time-label">时间</div>
              <div 
                v-for="day in weekDays" 
                :key="day.key"
                class="day-header"
                :class="{ 'today': day.isToday }"
              >
                <div class="day-name">{{ day.dayName }}</div>
                <div class="day-date">{{ day.date }}</div>
              </div>
            </div>
            
            <div class="week-body">
              <div 
                v-for="timeSlot in weekTimeSlots" 
                :key="timeSlot.key"
                class="time-row"
              >
                <div class="time-label">{{ timeSlot.time }}</div>
                <div 
                  v-for="day in weekDays" 
                  :key="`${timeSlot.key}-${day.key}`"
                  class="day-cell"
                  @click="onTimeSlotClick({ ...timeSlot, date: day.date })"
                >
                  <div 
                    v-for="schedule in getSchedulesInDayTimeSlot(day.date, timeSlot)"
                    :key="schedule.scheduleId"
                    class="schedule-item"
                    :class="getScheduleItemClass(schedule)"
                    @click.stop="onScheduleClick(schedule)"
                    @mousedown="onScheduleMouseDown(schedule, $event)"
                  >
                    <div class="schedule-content">
                      <div class="schedule-title">{{ schedule.memberName }}</div>
                      <div class="schedule-info">{{ schedule.coachName }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 月视图 -->
        <div v-else-if="currentViewType === SCHEDULE_VIEW_TYPE_ENUM.MONTH.value" class="month-view">
          <div class="month-grid">
            <div class="month-header">
              <div 
                v-for="dayName in monthDayNames" 
                :key="dayName"
                class="day-name"
              >
                {{ dayName }}
              </div>
            </div>
            
            <div class="month-body">
              <div 
                v-for="week in monthWeeks" 
                :key="week.key"
                class="month-week"
              >
                <div 
                  v-for="day in week.days" 
                  :key="day.key"
                  class="month-day"
                  :class="{ 
                    'other-month': day.isOtherMonth,
                    'today': day.isToday,
                    'has-schedules': day.schedules.length > 0
                  }"
                  @click="onDayClick(day)"
                >
                  <div class="day-number">{{ day.dayNumber }}</div>
                  <div class="day-schedules">
                    <div 
                      v-for="(schedule, index) in day.schedules.slice(0, 3)" 
                      :key="schedule.scheduleId"
                      class="schedule-item small"
                      :class="getScheduleItemClass(schedule)"
                      @click.stop="onScheduleClick(schedule)"
                    >
                      {{ schedule.memberName }}
                    </div>
                    <div 
                      v-if="day.schedules.length > 3" 
                      class="schedule-more"
                    >
                      +{{ day.schedules.length - 3 }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { LeftOutlined, RightOutlined } from '@ant-design/icons-vue';
import { 
  SCHEDULE_VIEW_TYPE_ENUM,
  LESSON_STATUS_ENUM,
  TIME_SLOT_CONFIG,
  SCHEDULE_DEFAULT_CONFIG
} from '/@/constants/business/schedule/schedule-const';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import weekday from 'dayjs/plugin/weekday';
import isoWeek from 'dayjs/plugin/isoWeek';

dayjs.extend(weekday);
dayjs.extend(isoWeek);
dayjs.locale('zh-cn');

// ======================== Props & Emits ========================
const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  schedules: {
    type: Array,
    default: () => []
  },
  viewType: {
    type: String,
    default: SCHEDULE_VIEW_TYPE_ENUM.WEEK.value
  }
});

const emit = defineEmits([
  'view-change', 
  'date-change', 
  'schedule-click', 
  'schedule-drag', 
  'time-slot-click'
]);

// ======================== 响应式数据 ========================
const currentDate = ref(dayjs());
const currentViewType = ref(props.viewType);
const isDragging = ref(false);
const dragStartPosition = ref(null);

// ======================== 计算属性 ========================
const currentDateTitle = computed(() => {
  switch (currentViewType.value) {
    case SCHEDULE_VIEW_TYPE_ENUM.DAY.value:
      return currentDate.value.format('YYYY年MM月DD日');
    case SCHEDULE_VIEW_TYPE_ENUM.WEEK.value:
      const weekStart = currentDate.value.startOf('isoWeek');
      const weekEnd = currentDate.value.endOf('isoWeek');
      return `${weekStart.format('YYYY年MM月DD日')} - ${weekEnd.format('MM月DD日')}`;
    case SCHEDULE_VIEW_TYPE_ENUM.MONTH.value:
      return currentDate.value.format('YYYY年MM月');
    default:
      return '';
  }
});

// 日视图时间段
const dayTimeSlots = computed(() => {
  const slots = [];
  const startHour = parseInt(TIME_SLOT_CONFIG.BUSINESS_HOURS.START.split(':')[0]);
  const endHour = parseInt(TIME_SLOT_CONFIG.BUSINESS_HOURS.END.split(':')[0]);
  
  for (let hour = startHour; hour <= endHour; hour++) {
    for (let minute = 0; minute < 60; minute += TIME_SLOT_CONFIG.INTERVAL) {
      const time = `${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
      slots.push({
        key: `${currentDate.value.format('YYYY-MM-DD')}-${time}`,
        time,
        date: currentDate.value.format('YYYY-MM-DD'),
        hour,
        minute
      });
    }
  }
  
  return slots;
});

// 周视图天数
const weekDays = computed(() => {
  const days = [];
  const weekStart = currentDate.value.startOf('isoWeek');
  
  for (let i = 0; i < 7; i++) {
    const day = weekStart.add(i, 'day');
    days.push({
      key: day.format('YYYY-MM-DD'),
      date: day.format('YYYY-MM-DD'),
      dayName: day.format('ddd'),
      dayNumber: day.format('DD'),
      isToday: day.isSame(dayjs(), 'day')
    });
  }
  
  return days;
});

// 周视图时间段
const weekTimeSlots = computed(() => {
  const slots = [];
  const startHour = parseInt(TIME_SLOT_CONFIG.BUSINESS_HOURS.START.split(':')[0]);
  const endHour = parseInt(TIME_SLOT_CONFIG.BUSINESS_HOURS.END.split(':')[0]);
  
  for (let hour = startHour; hour <= endHour; hour++) {
    for (let minute = 0; minute < 60; minute += TIME_SLOT_CONFIG.INTERVAL) {
      const time = `${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
      slots.push({
        key: time,
        time,
        hour,
        minute
      });
    }
  }
  
  return slots;
});

// 月视图天名称
const monthDayNames = computed(() => {
  return ['一', '二', '三', '四', '五', '六', '日'];
});

// 月视图周数据
const monthWeeks = computed(() => {
  const weeks = [];
  const monthStart = currentDate.value.startOf('month');
  const monthEnd = currentDate.value.endOf('month');
  const calendarStart = monthStart.startOf('isoWeek');
  const calendarEnd = monthEnd.endOf('isoWeek');
  
  let currentWeekStart = calendarStart;
  let weekIndex = 0;
  
  while (currentWeekStart.isBefore(calendarEnd) || currentWeekStart.isSame(calendarEnd)) {
    const week = {
      key: `week-${weekIndex}`,
      days: []
    };
    
    for (let i = 0; i < 7; i++) {
      const day = currentWeekStart.add(i, 'day');
      const daySchedules = props.schedules.filter(schedule => {
        return dayjs(schedule.startTime).isSame(day, 'day');
      });
      
      week.days.push({
        key: day.format('YYYY-MM-DD'),
        date: day.format('YYYY-MM-DD'),
        dayNumber: day.format('D'),
        isOtherMonth: !day.isSame(currentDate.value, 'month'),
        isToday: day.isSame(dayjs(), 'day'),
        schedules: daySchedules
      });
    }
    
    weeks.push(week);
    currentWeekStart = currentWeekStart.add(1, 'week');
    weekIndex++;
  }
  
  return weeks;
});

// ======================== 监听器 ========================
watch(() => props.viewType, (newType) => {
  currentViewType.value = newType;
});

watch(currentViewType, (newType) => {
  emit('view-change', newType);
});

watch(currentDate, (newDate) => {
  emit('date-change', newDate.toDate());
});

// ======================== 方法 ========================
function getDuration(schedule) {
  if (schedule.duration) {
    return schedule.duration; // 优先使用后端返回的值
  }
  
  if (schedule.startTime && schedule.endTime) {
    // 前端计算 duration
    const start = dayjs(schedule.startTime);
    const end = dayjs(schedule.endTime);
    return end.diff(start, 'minute');
  }
  
  return 60; // 默认60分钟
}

function onViewTypeChange() {
  emit('view-change', currentViewType.value);
}

function goToPrevious() {
  switch (currentViewType.value) {
    case SCHEDULE_VIEW_TYPE_ENUM.DAY.value:
      currentDate.value = currentDate.value.subtract(1, 'day');
      break;
    case SCHEDULE_VIEW_TYPE_ENUM.WEEK.value:
      currentDate.value = currentDate.value.subtract(1, 'week');
      break;
    case SCHEDULE_VIEW_TYPE_ENUM.MONTH.value:
      currentDate.value = currentDate.value.subtract(1, 'month');
      break;
  }
}

function goToNext() {
  switch (currentViewType.value) {
    case SCHEDULE_VIEW_TYPE_ENUM.DAY.value:
      currentDate.value = currentDate.value.add(1, 'day');
      break;
    case SCHEDULE_VIEW_TYPE_ENUM.WEEK.value:
      currentDate.value = currentDate.value.add(1, 'week');
      break;
    case SCHEDULE_VIEW_TYPE_ENUM.MONTH.value:
      currentDate.value = currentDate.value.add(1, 'month');
      break;
  }
}

function goToToday() {
  currentDate.value = dayjs();
}

function onTimeSlotClick(timeSlot) {
  emit('time-slot-click', timeSlot);
}

function onScheduleClick(schedule) {
  emit('schedule-click', schedule.scheduleId);
}

function onDayClick(day) {
  if (currentViewType.value === SCHEDULE_VIEW_TYPE_ENUM.MONTH.value) {
    currentDate.value = dayjs(day.date);
    currentViewType.value = SCHEDULE_VIEW_TYPE_ENUM.DAY.value;
  }
}

function onScheduleMouseDown(schedule, event) {
  if (event.button !== 0) return; // 只处理左键
  
  isDragging.value = true;
  dragStartPosition.value = {
    scheduleId: schedule.scheduleId,
    startX: event.clientX,
    startY: event.clientY,
    originalStartTime: schedule.startTime
  };
  
  document.addEventListener('mousemove', onDocumentMouseMove);
  document.addEventListener('mouseup', onDocumentMouseUp);
  event.preventDefault();
}

function onDocumentMouseMove(event) {
  if (!isDragging.value) return;
  
  // 这里可以添加拖拽过程中的视觉反馈
  // 比如显示拖拽阴影、计算新的时间位置等
}

function onDocumentMouseUp(event) {
  if (!isDragging.value) return;
  
  // 计算拖拽后的新时间
  const dragData = calculateNewScheduleTime(event);
  if (dragData) {
    emit('schedule-drag', dragData);
  }
  
  // 清理拖拽状态
  isDragging.value = false;
  dragStartPosition.value = null;
  document.removeEventListener('mousemove', onDocumentMouseMove);
  document.removeEventListener('mouseup', onDocumentMouseUp);
}

function calculateNewScheduleTime(event) {
  // 简化的拖拽计算逻辑
  // 实际应用中需要根据鼠标位置计算新的时间段
  const deltaX = event.clientX - dragStartPosition.value.startX;
  const deltaY = event.clientY - dragStartPosition.value.startY;
  
  // 如果移动距离太小，认为是点击而不是拖拽
  if (Math.abs(deltaX) < 5 && Math.abs(deltaY) < 5) {
    return null;
  }
  
  // 根据移动距离计算新的时间
  const timeSlotHeight = 40; // 假设每个时间段高度40px
  const slotsToMove = Math.round(deltaY / timeSlotHeight);
  
  if (slotsToMove === 0) return null;
  
  const originalTime = dayjs(dragStartPosition.value.originalStartTime);
  const newStartTime = originalTime.add(slotsToMove * TIME_SLOT_CONFIG.INTERVAL, 'minute');
  
  return {
    scheduleId: dragStartPosition.value.scheduleId,
    newStartTime: newStartTime.format('YYYY-MM-DD HH:mm:ss'),
    newEndTime: newStartTime.add(60, 'minute').format('YYYY-MM-DD HH:mm:ss') // 假设课程1小时
  };
}

function getSchedulesInTimeSlot(timeSlot) {
  return props.schedules.filter(schedule => {
    const scheduleTime = dayjs(schedule.startTime);
    const slotTime = dayjs(`${timeSlot.date} ${timeSlot.time}`);
    
    return scheduleTime.isSame(slotTime, 'minute') || 
           (scheduleTime.isBefore(slotTime) && 
            scheduleTime.add(getDuration(schedule), 'minute').isAfter(slotTime));
  });
}

function getSchedulesInDayTimeSlot(date, timeSlot) {
  return props.schedules.filter(schedule => {
    const scheduleTime = dayjs(schedule.startTime);
    const slotTime = dayjs(`${date} ${timeSlot.time}`);
    
    return scheduleTime.isSame(slotTime, 'day') &&
           (scheduleTime.isSame(slotTime, 'minute') || 
            (scheduleTime.isBefore(slotTime) && 
             scheduleTime.add(getDuration(schedule), 'minute').isAfter(slotTime)));
  });
}

function getScheduleItemClass(schedule) {
  const baseClass = 'schedule-item';
  const statusClass = `status-${schedule.lessonStatus}`;
  const productTypeClass = `type-${schedule.productType}`;
  
  return [baseClass, statusClass, productTypeClass];
}

function formatDate(date, format) {
  return date.format(format);
}
</script>

<style scoped>
.calendar-view {
  width: 100%;
  height: 100%;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.calendar-nav {
  display: flex;
  align-items: center;
  gap: 16px;
}

.calendar-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.calendar-content {
  margin-top: 16px;
}

/* 日视图样式 */
.day-view .time-grid {
  border: 1px solid #f0f0f0;
}

.day-view .time-header {
  display: grid;
  grid-template-columns: 80px 1fr;
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;
}

.day-view .time-label {
  padding: 8px;
  text-align: center;
  font-weight: 500;
  border-right: 1px solid #f0f0f0;
}

.day-view .date-header {
  padding: 8px;
  text-align: center;
  font-weight: 500;
}

.day-view .time-slots {
  display: grid;
  grid-template-columns: 80px 1fr;
}

.day-view .time-slot {
  display: contents;
}

.day-view .time-slot .time-label {
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;
  font-size: 12px;
}

.day-view .slot-content {
  padding: 4px;
  border-bottom: 1px solid #f0f0f0;
  border-right: 1px solid #f0f0f0;
  min-height: 40px;
  position: relative;
  cursor: pointer;
}

.day-view .slot-content:hover {
  background: #f5f5f5;
}

/* 周视图样式 */
.week-view .week-grid {
  border: 1px solid #f0f0f0;
}

.week-view .week-header {
  display: grid;
  grid-template-columns: 80px repeat(7, 1fr);
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;
}

.week-view .day-header {
  padding: 8px 4px;
  text-align: center;
  border-right: 1px solid #f0f0f0;
}

.week-view .day-header.today {
  background: #e6f7ff;
  color: #1890ff;
}

.week-view .day-name {
  font-size: 12px;
  font-weight: 500;
}

.week-view .day-date {
  font-size: 14px;
  font-weight: 600;
}

.week-view .week-body {
  display: grid;
  grid-template-columns: 80px repeat(7, 1fr);
}

.week-view .time-row {
  display: contents;
}

.week-view .time-row .time-label {
  padding: 4px 8px;
  text-align: center;
  font-size: 12px;
  border-bottom: 1px solid #f0f0f0;
  border-right: 1px solid #f0f0f0;
  background: #fafafa;
}

.week-view .day-cell {
  padding: 2px;
  border-bottom: 1px solid #f0f0f0;
  border-right: 1px solid #f0f0f0;
  min-height: 30px;
  position: relative;
  cursor: pointer;
}

.week-view .day-cell:hover {
  background: #f5f5f5;
}

/* 月视图样式 */
.month-view .month-grid {
  border: 1px solid #f0f0f0;
}

.month-view .month-header {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  background: #fafafa;
}

.month-view .day-name {
  padding: 8px;
  text-align: center;
  font-weight: 500;
  border-bottom: 1px solid #f0f0f0;
  border-right: 1px solid #f0f0f0;
}

.month-view .month-body {
  display: grid;
  grid-template-rows: repeat(6, 1fr);
}

.month-view .month-week {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
}

.month-view .month-day {
  padding: 4px;
  border-bottom: 1px solid #f0f0f0;
  border-right: 1px solid #f0f0f0;
  min-height: 100px;
  cursor: pointer;
}

.month-view .month-day.other-month {
  background: #fafafa;
  color: #bfbfbf;
}

.month-view .month-day.today {
  background: #e6f7ff;
}

.month-view .month-day:hover {
  background: #f5f5f5;
}

.month-view .day-number {
  font-weight: 500;
  margin-bottom: 4px;
}

.month-view .day-schedules {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

/* 课程项样式 */
.schedule-item {
  background: #1890ff;
  color: white;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s;
}

.schedule-item:hover {
  opacity: 0.8;
  transform: translateY(-1px);
}

.schedule-item.small {
  padding: 2px 4px;
  font-size: 11px;
}

.schedule-item.status-1 {
  background: #faad14;
}

.schedule-item.status-2 {
  background: #1890ff;
}

.schedule-item.status-3 {
  background: #52c41a;
}

.schedule-item.status-4 {
  background: #ff4d4f;
}

.schedule-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.schedule-title {
  font-weight: 500;
  line-height: 1.2;
}

.schedule-info {
  font-size: 11px;
  opacity: 0.9;
  line-height: 1.2;
}

.schedule-more {
  background: #d9d9d9;
  color: #666;
  border-radius: 4px;
  padding: 2px 4px;
  font-size: 11px;
  text-align: center;
}
</style>