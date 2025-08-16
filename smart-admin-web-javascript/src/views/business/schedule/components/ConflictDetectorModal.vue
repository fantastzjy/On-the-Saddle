<!--
  * 冲突检测模态框组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-modal
    :visible="visible"
    title="冲突检测"
    width="800px"
    :maskClosable="false"
    @cancel="onCancel"
    @ok="onOk"
  >
    <template #footer>
      <a-button @click="onCancel">关闭</a-button>
      <a-button 
        type="primary" 
        @click="onRecheck" 
        :loading="checking"
      >
        重新检测
      </a-button>
      <a-button 
        v-if="hasConflicts && recommendedSlots.length > 0"
        type="dashed" 
        @click="showRecommendations"
      >
        查看推荐时段
      </a-button>
    </template>

    <div v-if="schedule" class="conflict-detector">
      <!-- 课程信息 -->
      <a-card size="small" class="schedule-info-card">
        <a-row>
          <a-col :span="18">
            <h4>课单信息</h4>
            <a-descriptions size="small" :column="2">
              <a-descriptions-item label="课单号">{{ schedule.scheduleNo }}</a-descriptions-item>
              <a-descriptions-item label="会员">{{ schedule.memberName }}</a-descriptions-item>
              <a-descriptions-item label="教练">{{ schedule.coachName }}</a-descriptions-item>
              <a-descriptions-item label="马匹">{{ schedule.horseName || '-' }}</a-descriptions-item>
              <a-descriptions-item label="上课时间">
                {{ formatDateTime(schedule.startTime) }}
              </a-descriptions-item>
              <a-descriptions-item label="课程时长">{{ schedule.duration }}分钟</a-descriptions-item>
            </a-descriptions>
          </a-col>
          <a-col :span="6" class="text-right">
            <a-tag :color="getLessonStatusColor(schedule.lessonStatus)">
              {{ getLessonStatusDesc(schedule.lessonStatus) }}
            </a-tag>
          </a-col>
        </a-row>
      </a-card>

      <!-- 检测结果 -->
      <a-card size="small" title="检测结果" class="mt-16">
        <a-spin :spinning="checking">
          <div v-if="!checking">
            <!-- 无冲突 -->
            <a-result
              v-if="!hasConflicts"
              status="success"
              title="未发现冲突"
              sub-title="当前课程时间安排合理，没有检测到资源冲突。"
            >
              <template #icon>
                <CheckCircleOutlined style="color: #52c41a;" />
              </template>
            </a-result>

            <!-- 有冲突 -->
            <div v-else>
              <a-alert
                message="检测到冲突"
                :description="`发现 ${conflictList.length} 个冲突，请及时处理。`"
                type="error"
                show-icon
                class="mb-16"
              />

              <!-- 冲突列表 -->
              <a-list 
                :dataSource="conflictList" 
                class="conflict-list"
              >
                <template #renderItem="{ item }">
                  <a-list-item>
                    <a-list-item-meta>
                      <template #avatar>
                        <a-avatar 
                          :style="{ backgroundColor: getConflictTypeColor(item.type) }"
                        >
                          <component :is="getConflictTypeIcon(item.type)" />
                        </a-avatar>
                      </template>
                      <template #title>
                        <span>{{ getConflictTypeDesc(item.type) }}</span>
                        <a-tag :color="getConflictSeverityColor(item.severity)" size="small" class="ml-8">
                          {{ getConflictSeverityDesc(item.severity) }}
                        </a-tag>
                      </template>
                      <template #description>
                        <div>{{ item.description }}</div>
                        <div class="conflict-details">
                          <span>时间: {{ formatDateTime(item.conflictTime) }}</span>
                          <span v-if="item.resourceName" class="ml-16">
                            资源: {{ item.resourceName }}
                          </span>
                        </div>
                      </template>
                    </a-list-item-meta>
                    <template #actions>
                      <a-button 
                        v-if="item.canResolve"
                        size="small" 
                        type="link"
                        @click="resolveConflict(item)"
                      >
                        解决
                      </a-button>
                      <a-button 
                        size="small" 
                        type="link"
                        @click="viewConflictDetail(item)"
                      >
                        详情
                      </a-button>
                    </template>
                  </a-list-item>
                </template>
              </a-list>
            </div>
          </div>
        </a-spin>
      </a-card>

      <!-- 推荐时段 -->
      <a-card 
        v-if="recommendedSlots.length > 0" 
        size="small" 
        title="推荐时段" 
        class="mt-16"
      >
        <a-row :gutter="16">
          <a-col 
            :span="8" 
            v-for="slot in recommendedSlots.slice(0, 6)" 
            :key="slot.key"
          >
            <a-card 
              size="small" 
              :hoverable="true"
              class="recommended-slot"
              @click="selectRecommendedSlot(slot)"
            >
              <div class="slot-time">
                {{ formatDateTime(slot.startTime) }}
              </div>
              <div class="slot-score">
                <a-progress
                  :percent="slot.score"
                  :strokeColor="getScoreColor(slot.score)"
                  size="small"
                  :showInfo="false"
                />
                <span class="score-text">匹配度: {{ slot.score }}%</span>
              </div>
              <div class="slot-reason">
                {{ slot.reason }}
              </div>
            </a-card>
          </a-col>
        </a-row>
      </a-card>

      <!-- 资源状态 -->
      <a-card size="small" title="资源状态" class="mt-16" v-if="resourceStatus">
        <a-row :gutter="16">
          <a-col :span="12">
            <h5>教练状态</h5>
            <a-list size="small" :dataSource="resourceStatus.coaches">
              <template #renderItem="{ item }">
                <a-list-item>
                  <span>{{ item.coachName }}</span>
                  <template #actions>
                    <a-tag :color="getAvailabilityColor(item.status)">
                      {{ getAvailabilityDesc(item.status) }}
                    </a-tag>
                  </template>
                </a-list-item>
              </template>
            </a-list>
          </a-col>
          <a-col :span="12">
            <h5>马匹状态</h5>
            <a-list size="small" :dataSource="resourceStatus.horses">
              <template #renderItem="{ item }">
                <a-list-item>
                  <span>{{ item.horseName }}</span>
                  <template #actions>
                    <a-tag :color="getAvailabilityColor(item.status)">
                      {{ getAvailabilityDesc(item.status) }}
                    </a-tag>
                  </template>
                </a-list-item>
              </template>
            </a-list>
          </a-col>
        </a-row>
      </a-card>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { 
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  ClockCircleOutlined,
  UserOutlined,
  TeamOutlined
} from '@ant-design/icons-vue';
import { scheduleApi } from '/@/api/business/schedule/schedule-api';
import { 
  LESSON_STATUS_ENUM,
  CONFLICT_TYPE_ENUM,
  COACH_AVAILABILITY_STATUS_ENUM,
  HORSE_AVAILABILITY_STATUS_ENUM
} from '/@/constants/business/schedule/schedule-const';
import dayjs from 'dayjs';

// ======================== Props & Emits ========================
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  schedule: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['update:visible', 'ok']);

// ======================== 响应式数据 ========================
const checking = ref(false);
const conflictList = ref([]);
const recommendedSlots = ref([]);
const resourceStatus = ref(null);

// ======================== 计算属性 ========================
const visible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
});

const hasConflicts = computed(() => {
  return conflictList.value.length > 0;
});

// ======================== 监听器 ========================
watch(() => props.visible, (newVal) => {
  if (newVal && props.schedule) {
    checkConflicts();
    loadResourceStatus();
  }
});

watch(() => props.schedule, (newSchedule) => {
  if (newSchedule && props.visible) {
    checkConflicts();
    loadResourceStatus();
  }
});

// ======================== 方法 ========================
async function checkConflicts() {
  if (!props.schedule) return;

  try {
    checking.value = true;
    conflictList.value = [];
    recommendedSlots.value = [];

    const response = await scheduleApi.checkConflict({
      scheduleId: props.schedule.scheduleId,
      startTime: props.schedule.startTime,
      endTime: props.schedule.endTime || calculateEndTime(),
      coachId: props.schedule.coachId,
      horseId: props.schedule.horseId,
      memberId: props.schedule.memberId
    });

    if (response.ok) {
      const result = response.data;
      conflictList.value = result.conflicts || [];
      
      // 如果有冲突，获取推荐时段
      if (conflictList.value.length > 0) {
        await loadRecommendedSlots();
      }
    } else {
      message.error(response.msg || '冲突检测失败');
    }
  } catch (error) {
    message.error('冲突检测失败');
    console.error('冲突检测失败:', error);
  } finally {
    checking.value = false;
  }
}

async function loadRecommendedSlots() {
  try {
    const response = await scheduleApi.getRecommendedTimeSlots({
      scheduleId: props.schedule.scheduleId,
      duration: props.schedule.duration,
      coachId: props.schedule.coachId,
      horseId: props.schedule.horseId,
      memberId: props.schedule.memberId,
      preferredDate: dayjs(props.schedule.startTime).format('YYYY-MM-DD'),
      preferredTime: dayjs(props.schedule.startTime).format('HH:mm')
    });

    if (response.ok) {
      recommendedSlots.value = response.data || [];
    }
  } catch (error) {
    console.error('获取推荐时段失败:', error);
  }
}

async function loadResourceStatus() {
  if (!props.schedule) return;

  try {
    const response = await scheduleApi.getResourceStatus({
      startTime: props.schedule.startTime,
      endTime: props.schedule.endTime || calculateEndTime()
    });

    if (response.ok) {
      resourceStatus.value = response.data;
    }
  } catch (error) {
    console.error('获取资源状态失败:', error);
  }
}

function calculateEndTime() {
  if (!props.schedule.startTime || !props.schedule.duration) return null;
  return dayjs(props.schedule.startTime).add(props.schedule.duration, 'minute').format('YYYY-MM-DD HH:mm:ss');
}

async function resolveConflict(conflict) {
  Modal.confirm({
    title: '解决冲突',
    content: `确定要自动解决冲突：${conflict.description}？`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const response = await scheduleApi.resolveConflict({
          scheduleId: props.schedule.scheduleId,
          conflictId: conflict.conflictId,
          resolutionType: conflict.suggestedResolution
        });

        if (response.ok) {
          message.success('冲突已解决');
          checkConflicts(); // 重新检测
        } else {
          message.error(response.msg || '解决冲突失败');
        }
      } catch (error) {
        message.error('解决冲突失败');
        console.error('解决冲突失败:', error);
      }
    }
  });
}

function viewConflictDetail(conflict) {
  Modal.info({
    title: '冲突详情',
    content: h => h('div', [
      h('p', `冲突类型: ${getConflictTypeDesc(conflict.type)}`),
      h('p', `严重程度: ${getConflictSeverityDesc(conflict.severity)}`),
      h('p', `冲突时间: ${formatDateTime(conflict.conflictTime)}`),
      h('p', `详细描述: ${conflict.description}`),
      conflict.suggestedResolution && h('p', `建议解决方案: ${conflict.suggestedResolution}`)
    ]),
    width: 500
  });
}

async function selectRecommendedSlot(slot) {
  Modal.confirm({
    title: '调整课程时间',
    content: `确定要将课程时间调整为 ${formatDateTime(slot.startTime)} 吗？`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const response = await scheduleApi.updateScheduleTime({
          scheduleId: props.schedule.scheduleId,
          startTime: slot.startTime,
          endTime: slot.endTime
        });

        if (response.ok) {
          message.success('课程时间已调整');
          emit('ok');
        } else {
          message.error(response.msg || '调整时间失败');
        }
      } catch (error) {
        message.error('调整时间失败');
        console.error('调整时间失败:', error);
      }
    }
  });
}

function showRecommendations() {
  // 滚动到推荐时段区域
  const element = document.querySelector('.recommended-slot');
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' });
  }
}

function onRecheck() {
  checkConflicts();
  loadResourceStatus();
}

function onCancel() {
  visible.value = false;
}

function onOk() {
  visible.value = false;
  emit('ok');
}

// ======================== 辅助方法 ========================
function formatDateTime(dateTime) {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm');
}

function getLessonStatusDesc(value) {
  return Object.values(LESSON_STATUS_ENUM).find(item => item.value === value)?.desc || '-';
}

function getLessonStatusColor(value) {
  return Object.values(LESSON_STATUS_ENUM).find(item => item.value === value)?.color || 'default';
}

function getConflictTypeDesc(type) {
  return Object.values(CONFLICT_TYPE_ENUM).find(item => item.value === type)?.desc || type;
}

function getConflictTypeColor(type) {
  return Object.values(CONFLICT_TYPE_ENUM).find(item => item.value === type)?.color || '#ff4d4f';
}

function getConflictTypeIcon(type) {
  const iconMap = {
    [CONFLICT_TYPE_ENUM.COACH_CONFLICT.value]: UserOutlined,
    [CONFLICT_TYPE_ENUM.HORSE_CONFLICT.value]: TeamOutlined,
    [CONFLICT_TYPE_ENUM.RESOURCE_CONFLICT.value]: ExclamationCircleOutlined,
    [CONFLICT_TYPE_ENUM.TIME_CONFLICT.value]: ClockCircleOutlined
  };
  return iconMap[type] || ExclamationCircleOutlined;
}

function getConflictSeverityDesc(severity) {
  const severityMap = {
    'high': '严重',
    'medium': '中等',
    'low': '轻微'
  };
  return severityMap[severity] || severity;
}

function getConflictSeverityColor(severity) {
  const colorMap = {
    'high': 'red',
    'medium': 'orange',
    'low': 'blue'
  };
  return colorMap[severity] || 'default';
}

function getAvailabilityDesc(status) {
  const coachStatus = Object.values(COACH_AVAILABILITY_STATUS_ENUM).find(item => item.value === status);
  if (coachStatus) return coachStatus.desc;
  
  const horseStatus = Object.values(HORSE_AVAILABILITY_STATUS_ENUM).find(item => item.value === status);
  if (horseStatus) return horseStatus.desc;
  
  return '-';
}

function getAvailabilityColor(status) {
  const coachStatus = Object.values(COACH_AVAILABILITY_STATUS_ENUM).find(item => item.value === status);
  if (coachStatus) return coachStatus.color;
  
  const horseStatus = Object.values(HORSE_AVAILABILITY_STATUS_ENUM).find(item => item.value === status);
  if (horseStatus) return horseStatus.color;
  
  return 'default';
}

function getScoreColor(score) {
  if (score >= 80) return '#52c41a';
  if (score >= 60) return '#faad14';
  return '#ff4d4f';
}
</script>

<style scoped>
.conflict-detector {
  max-height: 600px;
  overflow: auto;
}

.schedule-info-card {
  background: #fafafa;
}

.mt-16 {
  margin-top: 16px;
}

.mb-16 {
  margin-bottom: 16px;
}

.ml-8 {
  margin-left: 8px;
}

.ml-16 {
  margin-left: 16px;
}

.text-right {
  text-align: right;
}

.conflict-list {
  max-height: 300px;
  overflow: auto;
}

.conflict-details {
  margin-top: 4px;
  font-size: 12px;
  color: #666;
}

.recommended-slot {
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.recommended-slot:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.slot-time {
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 8px;
}

.slot-score {
  margin-bottom: 8px;
}

.score-text {
  font-size: 12px;
  color: #666;
  margin-left: 8px;
}

.slot-reason {
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}

h5 {
  margin: 0 0 8px 0;
  font-size: 14px;
  font-weight: 600;
}

:deep(.ant-result-title) {
  font-size: 16px;
}

:deep(.ant-result-subtitle) {
  font-size: 14px;
}

:deep(.ant-list-item-meta-title) {
  margin-bottom: 4px;
}

:deep(.ant-list-item-meta-description) {
  font-size: 12px;
}

:deep(.ant-progress-line) {
  margin-bottom: 0;
}
</style>