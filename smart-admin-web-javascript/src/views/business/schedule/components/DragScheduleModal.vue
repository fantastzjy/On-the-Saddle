<!--
  * 拖拽排课模态框组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-modal
    :visible="visible"
    title="拖拽排课"
    width="1200px"
    :maskClosable="false"
    @cancel="onCancel"
    @ok="onOk"
  >
    <template #footer>
      <a-button @click="onCancel">关闭</a-button>
      <a-button type="primary" @click="onApplyChanges" :loading="applying">应用变更</a-button>
    </template>

    <div class="drag-schedule-container">
      <!-- 操作说明 -->
      <a-alert
        message="拖拽排课说明"
        description="鼠标拖拽课程卡片可以调整时间，支持跨天拖拽。红色表示时间冲突，橙色表示资源紧张。"
        type="info"
        show-icon
        closable
        class="mb-16"
      />

      <!-- 筛选控件 -->
      <div class="drag-filters">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-select
              v-model:value="filterForm.coachId"
              placeholder="选择教练"
              allowClear
              style="width: 100%"
              @change="loadScheduleData"
            >
              <a-select-option 
                v-for="coach in coachList" 
                :key="coach.coachId" 
                :value="coach.coachId"
              >
                {{ coach.coachName }}
              </a-select-option>
            </a-select>
          </a-col>
          <a-col :span="6">
            <a-date-picker
              v-model:value="filterForm.startDate"
              placeholder="开始日期"
              style="width: 100%"
              @change="loadScheduleData"
            />
          </a-col>
          <a-col :span="6">
            <a-date-picker
              v-model:value="filterForm.endDate"
              placeholder="结束日期"
              style="width: 100%"
              @change="loadScheduleData"
            />
          </a-col>
          <a-col :span="6">
            <a-button type="primary" @click="loadScheduleData" :loading="loading">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
          </a-col>
        </a-row>
      </div>

      <!-- 拖拽区域 -->
      <a-spin :spinning="loading">
        <div class="drag-area">
          <div class="time-grid-container">
            <!-- 时间轴 -->
            <div class="time-axis">
              <div class="time-header">时间</div>
              <div 
                v-for="timeSlot in timeSlots" 
                :key="timeSlot.key"
                class="time-slot-label"
              >
                {{ timeSlot.time }}
              </div>
            </div>

            <!-- 日期列 -->
            <div 
              v-for="day in dateRange" 
              :key="day.key"
              class="day-column"
            >
              <!-- 日期头 -->
              <div class="day-header" :class="{ 'today': day.isToday }">
                <div class="day-name">{{ day.dayName }}</div>
                <div class="day-date">{{ day.date }}</div>
              </div>

              <!-- 时间槽 -->
              <div 
                v-for="timeSlot in timeSlots" 
                :key="`${day.key}-${timeSlot.key}`"
                class="time-slot"
                :class="getTimeSlotClass(day, timeSlot)"
                @dragover="onDragOver"
                @drop="onDrop($event, day, timeSlot)"
                @click="onTimeSlotClick(day, timeSlot)"
              >
                <!-- 课程卡片 -->
                <div 
                  v-for="schedule in getSchedulesInSlot(day.date, timeSlot)"
                  :key="schedule.scheduleId"
                  class="schedule-card"
                  :class="getScheduleCardClass(schedule)"
                  :draggable="true"
                  @dragstart="onDragStart($event, schedule)"
                  @dragend="onDragEnd"
                >
                  <div class="schedule-content">
                    <div class="schedule-title">{{ schedule.memberName }}</div>
                    <div class="schedule-info">
                      <div>{{ schedule.coachName }}</div>
                      <div>{{ schedule.duration }}分钟</div>
                    </div>
                    <div class="schedule-time">
                      {{ formatTime(schedule.startTime) }}
                    </div>
                  </div>
                  
                  <!-- 冲突指示器 -->
                  <div v-if="schedule.hasConflict" class="conflict-indicator">
                    <ExclamationCircleOutlined />
                  </div>
                </div>

                <!-- 时间槽提示 -->
                <div v-if="!getSchedulesInSlot(day.date, timeSlot).length" class="slot-hint">
                  点击添加课程
                </div>
              </div>
            </div>
          </div>
        </div>
      </a-spin>

      <!-- 变更记录 -->
      <div v-if="pendingChanges.length > 0" class="changes-panel">
        <h4>待应用变更 ({{ pendingChanges.length }})</h4>
        <a-list size="small" :dataSource="pendingChanges">
          <template #renderItem="{ item, index }">
            <a-list-item>
              <template #actions>
                <a-button 
                  size="small" 
                  type="link" 
                  @click="removePendingChange(index)"
                >
                  撤销
                </a-button>
              </template>
              <a-list-item-meta>
                <template #title>
                  <span>{{ item.memberName }} - {{ item.coachName }}</span>
                </template>
                <template #description>
                  <span>
                    {{ formatTime(item.originalStartTime) }} → {{ formatTime(item.newStartTime) }}
                  </span>
                  <a-tag 
                    v-if="item.hasConflict" 
                    color="red" 
                    size="small" 
                    class="ml-8"
                  >
                    冲突
                  </a-tag>
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { SearchOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { scheduleApi } from '/@/api/business/schedule/schedule-api';
import { TIME_SLOT_CONFIG, SCHEDULE_DEFAULT_CONFIG } from '/@/constants/business/schedule/schedule-const';
import dayjs from 'dayjs';

// ======================== Props & Emits ========================
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:visible', 'ok']);

// ======================== 响应式数据 ========================
const loading = ref(false);
const applying = ref(false);
const coachList = ref([]);
const scheduleData = ref([]);
const pendingChanges = ref([]);
const draggedSchedule = ref(null);

const filterForm = reactive({
  coachId: null,
  startDate: dayjs(),
  endDate: dayjs().add(6, 'day')
});

// ======================== 计算属性 ========================
const visible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
});

// 时间段配置
const timeSlots = computed(() => {
  const slots = [];
  const startHour = parseInt(TIME_SLOT_CONFIG.BUSINESS_HOURS.START.split(':')[0]);
  const endHour = parseInt(TIME_SLOT_CONFIG.BUSINESS_HOURS.END.split(':')[0]);
  
  for (let hour = startHour; hour <= endHour; hour++) {
    for (let minute = 0; minute < 60; minute += SCHEDULE_DEFAULT_CONFIG.DRAG.SNAP_DURATION) {
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

// 日期范围
const dateRange = computed(() => {
  if (!filterForm.startDate || !filterForm.endDate) return [];
  
  const days = [];
  let current = dayjs(filterForm.startDate);
  const end = dayjs(filterForm.endDate);
  
  while (current.isBefore(end) || current.isSame(end, 'day')) {
    days.push({
      key: current.format('YYYY-MM-DD'),
      date: current.format('YYYY-MM-DD'),
      dayName: current.format('ddd'),
      dayNumber: current.format('DD'),
      isToday: current.isSame(dayjs(), 'day')
    });
    current = current.add(1, 'day');
  }
  
  return days;
});

// ======================== 监听器 ========================
watch(() => props.visible, (newVal) => {
  if (newVal) {
    resetData();
    loadCoachList();
    loadScheduleData();
  }
});

// ======================== 方法 ========================
function resetData() {
  scheduleData.value = [];
  pendingChanges.value = [];
  draggedSchedule.value = null;
  
  filterForm.startDate = dayjs();
  filterForm.endDate = dayjs().add(6, 'day');
  filterForm.coachId = null;
}

async function loadCoachList() {
  try {
    const response = await scheduleApi.getCoachList();
    if (response.ok) {
      coachList.value = response.data || [];
    }
  } catch (error) {
    console.error('加载教练列表失败:', error);
  }
}

async function loadScheduleData() {
  if (!filterForm.startDate || !filterForm.endDate) return;
  
  try {
    loading.value = true;
    
    const params = {
      coachId: filterForm.coachId,
      startDate: filterForm.startDate.format('YYYY-MM-DD'),
      endDate: filterForm.endDate.format('YYYY-MM-DD')
    };
    
    const response = await scheduleApi.queryScheduleList(params);
    if (response.ok) {
      scheduleData.value = response.data.records || [];
      // 检测冲突
      await detectConflicts();
    } else {
      message.error(response.msg || '加载数据失败');
    }
  } catch (error) {
    message.error('加载课表数据失败');
    console.error('加载课表数据失败:', error);
  } finally {
    loading.value = false;
  }
}

async function detectConflicts() {
  // 为每个课程检测冲突
  for (const schedule of scheduleData.value) {
    try {
      const response = await scheduleApi.checkConflict({
        scheduleId: schedule.scheduleId,
        startTime: schedule.startTime,
        endTime: schedule.endTime,
        coachId: schedule.coachId,
        horseId: schedule.horseId
      });
      
      if (response.ok) {
        schedule.hasConflict = response.data.hasConflict;
        schedule.conflicts = response.data.conflicts || [];
      }
    } catch (error) {
      console.error('冲突检测失败:', error);
    }
  }
}

function getSchedulesInSlot(date, timeSlot) {
  return scheduleData.value.filter(schedule => {
    const scheduleTime = dayjs(schedule.startTime);
    const slotTime = dayjs(`${date} ${timeSlot.time}`);
    
    return scheduleTime.isSame(slotTime, 'day') &&
           scheduleTime.hour() === timeSlot.hour &&
           scheduleTime.minute() === timeSlot.minute;
  });
}

function getTimeSlotClass(day, timeSlot) {
  const classes = ['time-slot'];
  
  // 检查是否有冲突的课程
  const schedules = getSchedulesInSlot(day.date, timeSlot);
  if (schedules.some(s => s.hasConflict)) {
    classes.push('has-conflict');
  }
  
  // 检查是否是当前时间
  const slotTime = dayjs(`${day.date} ${timeSlot.time}`);
  if (slotTime.isSame(dayjs(), 'hour')) {
    classes.push('current-time');
  }
  
  return classes;
}

function getScheduleCardClass(schedule) {
  const classes = ['schedule-card'];
  
  if (schedule.hasConflict) {
    classes.push('conflict');
  }
  
  classes.push(`status-${schedule.lessonStatus}`);
  
  return classes;
}

function onDragStart(event, schedule) {
  draggedSchedule.value = schedule;
  event.dataTransfer.effectAllowed = 'move';
  event.dataTransfer.setData('text/plain', '');
  
  // 添加拖拽样式
  event.target.classList.add('dragging');
}

function onDragEnd(event) {
  event.target.classList.remove('dragging');
  draggedSchedule.value = null;
}

function onDragOver(event) {
  event.preventDefault();
  event.dataTransfer.dropEffect = 'move';
}

function onDrop(event, day, timeSlot) {
  event.preventDefault();
  
  if (!draggedSchedule.value) return;
  
  const newStartTime = dayjs(`${day.date} ${timeSlot.time}`);
  const originalStartTime = dayjs(draggedSchedule.value.startTime);
  
  // 如果没有变化，不做处理
  if (newStartTime.isSame(originalStartTime, 'minute')) return;
  
  // 计算新的结束时间
  const duration = draggedSchedule.value.duration;
  const newEndTime = newStartTime.add(duration, 'minute');
  
  // 添加到待处理变更
  const changeIndex = pendingChanges.value.findIndex(
    c => c.scheduleId === draggedSchedule.value.scheduleId
  );
  
  const changeData = {
    scheduleId: draggedSchedule.value.scheduleId,
    memberName: draggedSchedule.value.memberName,
    coachName: draggedSchedule.value.coachName,
    originalStartTime: draggedSchedule.value.startTime,
    newStartTime: newStartTime.format('YYYY-MM-DD HH:mm:ss'),
    newEndTime: newEndTime.format('YYYY-MM-DD HH:mm:ss'),
    hasConflict: false // 需要重新检测
  };
  
  if (changeIndex >= 0) {
    pendingChanges.value[changeIndex] = changeData;
  } else {
    pendingChanges.value.push(changeData);
  }
  
  // 更新本地数据
  const scheduleIndex = scheduleData.value.findIndex(
    s => s.scheduleId === draggedSchedule.value.scheduleId
  );
  if (scheduleIndex >= 0) {
    scheduleData.value[scheduleIndex].startTime = changeData.newStartTime;
    scheduleData.value[scheduleIndex].endTime = changeData.newEndTime;
  }
  
  // 重新检测冲突
  detectConflictForChange(changeData);
}

async function detectConflictForChange(changeData) {
  try {
    const response = await scheduleApi.checkConflict({
      scheduleId: changeData.scheduleId,
      startTime: changeData.newStartTime,
      endTime: changeData.newEndTime,
      coachId: draggedSchedule.value.coachId,
      horseId: draggedSchedule.value.horseId
    });
    
    if (response.ok) {
      changeData.hasConflict = response.data.hasConflict;
    }
  } catch (error) {
    console.error('冲突检测失败:', error);
  }
}

function onTimeSlotClick(day, timeSlot) {
  // 点击空白时间段创建新课程
  const datetime = `${day.date} ${timeSlot.time}`;
  // 这里可以触发创建课程的逻辑
  console.log('创建新课程:', datetime);
}

function removePendingChange(index) {
  const change = pendingChanges.value[index];
  
  // 恢复原始数据
  const scheduleIndex = scheduleData.value.findIndex(
    s => s.scheduleId === change.scheduleId
  );
  if (scheduleIndex >= 0) {
    scheduleData.value[scheduleIndex].startTime = change.originalStartTime;
    // 重新计算结束时间
    const duration = scheduleData.value[scheduleIndex].duration;
    const endTime = dayjs(change.originalStartTime).add(duration, 'minute');
    scheduleData.value[scheduleIndex].endTime = endTime.format('YYYY-MM-DD HH:mm:ss');
  }
  
  pendingChanges.value.splice(index, 1);
}

async function onApplyChanges() {
  if (pendingChanges.value.length === 0) {
    message.warning('没有待应用的变更');
    return;
  }
  
  try {
    applying.value = true;
    
    // 批量应用变更
    const response = await scheduleApi.batchUpdateScheduleTime(
      pendingChanges.value.map(change => ({
        scheduleId: change.scheduleId,
        startTime: change.newStartTime,
        endTime: change.newEndTime
      }))
    );
    
    if (response.ok) {
      message.success(`成功应用 ${pendingChanges.value.length} 个变更`);
      pendingChanges.value = [];
      emit('ok');
    } else {
      message.error(response.msg || '应用变更失败');
    }
  } catch (error) {
    message.error('应用变更失败');
    console.error('应用变更失败:', error);
  } finally {
    applying.value = false;
  }
}

function onCancel() {
  visible.value = false;
}

function onOk() {
  if (pendingChanges.value.length > 0) {
    onApplyChanges();
  } else {
    visible.value = false;
  }
}

function formatTime(dateTime) {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('MM-DD HH:mm');
}
</script>

<style scoped>
.drag-schedule-container {
  max-height: 600px;
  overflow: auto;
}

.mb-16 {
  margin-bottom: 16px;
}

.ml-8 {
  margin-left: 8px;
}

.drag-filters {
  margin-bottom: 16px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
}

.drag-area {
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  overflow: auto;
  max-height: 400px;
}

.time-grid-container {
  display: flex;
  min-width: 800px;
}

.time-axis {
  width: 80px;
  flex-shrink: 0;
  border-right: 1px solid #f0f0f0;
}

.time-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.time-slot-label {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;
}

.day-column {
  flex: 1;
  min-width: 120px;
  border-right: 1px solid #f0f0f0;
}

.day-header {
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.day-header.today {
  background: #e6f7ff;
  color: #1890ff;
}

.day-name {
  font-size: 12px;
  margin-bottom: 2px;
}

.day-date {
  font-size: 14px;
  font-weight: 600;
}

.time-slot {
  height: 60px;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
  transition: all 0.2s;
  cursor: pointer;
}

.time-slot:hover {
  background: #f5f5f5;
}

.time-slot.has-conflict {
  background: #fff2f0;
}

.time-slot.current-time {
  background: #e6f7ff;
}

.slot-hint {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #bfbfbf;
  font-size: 11px;
  pointer-events: none;
  opacity: 0;
  transition: opacity 0.2s;
}

.time-slot:hover .slot-hint {
  opacity: 1;
}

.schedule-card {
  position: absolute;
  top: 2px;
  left: 2px;
  right: 2px;
  bottom: 2px;
  background: #1890ff;
  color: white;
  border-radius: 4px;
  padding: 4px 6px;
  cursor: move;
  transition: all 0.2s;
  user-select: none;
}

.schedule-card:hover {
  transform: scale(1.02);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.schedule-card.dragging {
  opacity: 0.5;
  transform: rotate(5deg);
}

.schedule-card.conflict {
  background: #ff4d4f;
}

.schedule-card.status-1 {
  background: #faad14;
}

.schedule-card.status-2 {
  background: #1890ff;
}

.schedule-card.status-3 {
  background: #52c41a;
}

.schedule-card.status-4 {
  background: #ff4d4f;
}

.schedule-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  font-size: 11px;
}

.schedule-title {
  font-weight: 600;
  margin-bottom: 2px;
  line-height: 1.2;
}

.schedule-info {
  flex: 1;
  font-size: 10px;
  opacity: 0.9;
  line-height: 1.1;
}

.schedule-time {
  font-size: 10px;
  opacity: 0.8;
  margin-top: auto;
}

.conflict-indicator {
  position: absolute;
  top: -2px;
  right: -2px;
  width: 16px;
  height: 16px;
  background: #ff4d4f;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  color: white;
}

.changes-panel {
  margin-top: 16px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
  max-height: 200px;
  overflow: auto;
}

.changes-panel h4 {
  margin: 0 0 12px 0;
  color: #1890ff;
}
</style>