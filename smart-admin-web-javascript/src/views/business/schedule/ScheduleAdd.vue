<!--
  * 新增/编辑排课页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="schedule-add-container">
    <a-card>
      <template #title>
        <div style="display: flex; align-items: center;">
          <ArrowLeftOutlined @click="goBack" style="margin-right: 8px; cursor: pointer;" />
          {{ isEdit ? '编辑课程' : '新增排课' }}
        </div>
      </template>

      <a-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
      >
        <!-- 基础信息 -->
        <a-divider orientation="left">基础信息</a-divider>
        
        <a-form-item label="课单号" name="lessonNo">
          <a-input
            v-model:value="formData.lessonNo"
            placeholder="系统自动生成"
            :disabled="true"
          />
        </a-form-item>

        <a-form-item label="课程类型" name="lessonType">
          <a-select
            v-model:value="formData.lessonType"
            placeholder="请选择课程类型"
            @change="onLessonTypeChange"
          >
            <a-select-option 
              v-for="item in Object.values(LESSON_TYPE_ENUM)" 
              :key="item.value" 
              :value="item.value"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="教练" name="coachId">
          <a-select
            v-model:value="formData.coachId"
            placeholder="请选择教练"
            showSearch
            :filterOption="filterOption"
            @change="onCoachChange"
          >
            <a-select-option 
              v-for="coach in availableCoaches" 
              :key="coach.coachId" 
              :value="coach.coachId"
            >
              {{ coach.name }} - {{ coach.specialties }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="会员" name="memberId">
          <a-select
            v-model:value="formData.memberId"
            placeholder="请选择会员"
            showSearch
            :filterOption="filterOption"
            @change="onMemberChange"
          >
            <a-select-option 
              v-for="member in memberList" 
              :key="member.memberId" 
              :value="member.memberId"
            >
              {{ member.name }} - {{ member.phone }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="马匹" name="horseId">
          <a-select
            v-model:value="formData.horseId"
            placeholder="请选择马匹"
            showSearch
            :filterOption="filterOption"
            :disabled="!formData.coachId"
          >
            <a-select-option 
              v-for="horse in availableHorses" 
              :key="horse.horseId" 
              :value="horse.horseId"
            >
              {{ horse.name }} - {{ horse.breed }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <!-- 时间安排 -->
        <a-divider orientation="left">时间安排</a-divider>

        <a-form-item label="课程日期" name="lessonDate">
          <a-date-picker
            v-model:value="formData.lessonDate"
            style="width: 100%"
            placeholder="请选择课程日期"
            :disabledDate="disabledDate"
            @change="onDateChange"
          />
        </a-form-item>

        <a-form-item label="开始时间" name="startTime">
          <a-time-picker
            v-model:value="formData.startTime"
            style="width: 100%"
            placeholder="请选择开始时间"
            format="HH:mm"
            :disabled="!formData.lessonDate"
            @change="onStartTimeChange"
          />
        </a-form-item>

        <a-form-item label="结束时间" name="endTime">
          <a-time-picker
            v-model:value="formData.endTime"
            style="width: 100%"
            placeholder="请选择结束时间"
            format="HH:mm"
            :disabled="!formData.startTime"
          />
        </a-form-item>

        <a-form-item label="课程时长" name="duration">
          <a-input-number
            v-model:value="formData.duration"
            style="width: 100%"
            :min="30"
            :max="180"
            :step="15"
            placeholder="分钟"
            :disabled="true"
          />
          <div style="color: #999; font-size: 12px; margin-top: 4px;">
            系统根据开始和结束时间自动计算
          </div>
        </a-form-item>

        <!-- 课程详情 -->
        <a-divider orientation="left">课程详情</a-divider>

        <a-form-item label="课程内容" name="lessonContent">
          <a-textarea
            v-model:value="formData.lessonContent"
            placeholder="请输入课程内容安排"
            :rows="4"
          />
        </a-form-item>

        <a-form-item label="课程目标" name="lessonGoal">
          <a-textarea
            v-model:value="formData.lessonGoal"
            placeholder="请输入本次课程的目标"
            :rows="3"
          />
        </a-form-item>

        <a-form-item label="注意事项" name="notes">
          <a-textarea
            v-model:value="formData.notes"
            placeholder="请输入注意事项"
            :rows="3"
          />
        </a-form-item>

        <!-- 费用信息 -->
        <a-divider orientation="left">费用信息</a-divider>

        <a-form-item label="课程费用" name="lessonFee">
          <a-input-number
            v-model:value="formData.lessonFee"
            style="width: 100%"
            :min="0"
            :precision="2"
            placeholder="元"
          />
        </a-form-item>

        <a-form-item label="付费方式" name="paymentMethod">
          <a-select
            v-model:value="formData.paymentMethod"
            placeholder="请选择付费方式"
          >
            <a-select-option value="1">现金支付</a-select-option>
            <a-select-option value="2">会员卡扣费</a-select-option>
            <a-select-option value="3">课时包扣费</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 冲突检测结果 -->
        <div v-if="conflictInfo.length > 0">
          <a-divider orientation="left">
            <span style="color: #ff4d4f;">
              <ExclamationCircleOutlined />
              时间冲突检测
            </span>
          </a-divider>
          
          <a-alert
            type="warning"
            show-icon
            style="margin-bottom: 16px;"
          >
            <template #message>
              <div>检测到以下时间冲突，请调整时间安排：</div>
              <ul style="margin-top: 8px; margin-bottom: 0;">
                <li v-for="conflict in conflictInfo" :key="conflict.id">
                  {{ conflict.message }}
                </li>
              </ul>
            </template>
          </a-alert>
        </div>

        <!-- 推荐时段 -->
        <div v-if="recommendedSlots.length > 0">
          <a-divider orientation="left">
            <span style="color: #52c41a;">
              <BulbOutlined />
              推荐时段
            </span>
          </a-divider>
          
          <div style="margin-bottom: 16px;">
            <a-tag
              v-for="slot in recommendedSlots"
              :key="slot.id"
              color="green"
              style="margin-bottom: 8px; cursor: pointer;"
              @click="selectRecommendedSlot(slot)"
            >
              {{ slot.date }} {{ slot.startTime }}-{{ slot.endTime }}
            </a-tag>
          </div>
        </div>

        <!-- 操作按钮 -->
        <a-form-item :wrapper-col="{ offset: 4, span: 16 }">
          <a-space>
            <a-button @click="goBack">取消</a-button>
            <a-button @click="detectConflict" :loading="detectingConflict">
              <SearchOutlined />
              冲突检测
            </a-button>
            <a-button @click="getRecommendations" :loading="gettingRecommendations">
              <BulbOutlined />
              获取推荐时段
            </a-button>
            <a-button 
              type="primary" 
              @click="handleSubmit" 
              :loading="submitting"
              :disabled="conflictInfo.length > 0"
            >
              {{ isEdit ? '更新' : '保存' }}
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';
import { 
  ArrowLeftOutlined,
  ExclamationCircleOutlined,
  BulbOutlined,
  SearchOutlined
} from '@ant-design/icons-vue';
import { scheduleApi } from '/@/api/business/schedule/schedule-api';
import { coachApi } from '/@/api/business/coach/coach-api';
import { memberApi } from '/@/api/business/member/member-api';
import { horseApi } from '/@/api/business/horse/horse-api';
import { smartSentry } from '/@/lib/smart-sentry';

// 常量定义
const LESSON_TYPE_ENUM = {
  PRIVATE: { value: 1, desc: '私教课' },
  GROUP: { value: 2, desc: '小组课' },
  EXPERIENCE: { value: 3, desc: '体验课' }
};

// 路由和状态
const route = useRoute();
const router = useRouter();
const isEdit = computed(() => route.name === 'ScheduleEdit');

// 表单相关
const formRef = ref();
const submitting = ref(false);
const detectingConflict = ref(false);
const gettingRecommendations = ref(false);

// 表单数据
const formData = reactive({
  lessonNo: '',
  lessonType: null,
  coachId: null,
  memberId: null,
  horseId: null,
  lessonDate: null,
  startTime: null,
  endTime: null,
  duration: null,
  lessonContent: '',
  lessonGoal: '',
  notes: '',
  lessonFee: null,
  paymentMethod: null
});

// 表单验证规则
const formRules = {
  lessonType: [{ required: true, message: '请选择课程类型' }],
  coachId: [{ required: true, message: '请选择教练' }],
  memberId: [{ required: true, message: '请选择会员' }],
  lessonDate: [{ required: true, message: '请选择课程日期' }],
  startTime: [{ required: true, message: '请选择开始时间' }],
  endTime: [{ required: true, message: '请选择结束时间' }],
  lessonFee: [{ required: true, message: '请输入课程费用' }],
  paymentMethod: [{ required: true, message: '请选择付费方式' }]
};

// 选项数据
const coachList = ref([]);
const memberList = ref([]);
const horseList = ref([]);
const availableCoaches = ref([]);
const availableHorses = ref([]);

// 冲突检测和推荐
const conflictInfo = ref([]);
const recommendedSlots = ref([]);

// 生命周期
onMounted(() => {
  loadInitialData();
  if (isEdit.value) {
    loadScheduleDetail();
  } else {
    generateLessonNo();
  }
});

// 监听时间变化
watch([() => formData.startTime, () => formData.endTime], () => {
  calculateDuration();
});

// 方法
const loadInitialData = async () => {
  try {
    await Promise.all([
      loadCoaches(),
      loadMembers(),
      loadHorses()
    ]);
  } catch (error) {
    message.error('加载数据失败');
    smartSentry.captureError(error);
  }
};

const loadCoaches = async () => {
  try {
    const res = await coachApi.getCoachList({ pageNum: 1, pageSize: 100 });
    if (res.ok) {
      coachList.value = res.data.list || [];
      availableCoaches.value = [...coachList.value];
    }
  } catch (error) {
    console.error('加载教练列表失败', error);
  }
};

const loadMembers = async () => {
  try {
    const res = await memberApi.getMemberList({ pageNum: 1, pageSize: 100 });
    if (res.ok) {
      memberList.value = res.data.list || [];
    }
  } catch (error) {
    console.error('加载会员列表失败', error);
  }
};

const loadHorses = async () => {
  try {
    const res = await horseApi.getHorseList({ pageNum: 1, pageSize: 100 });
    if (res.ok) {
      horseList.value = res.data.list || [];
    }
  } catch (error) {
    console.error('加载马匹列表失败', error);
  }
};

const loadScheduleDetail = async () => {
  try {
    const scheduleId = route.params.id;
    const res = await scheduleApi.getScheduleDetail(scheduleId);
    if (res.ok) {
      const data = res.data;
      Object.keys(formData).forEach(key => {
        if (data[key] !== undefined) {
          if (key === 'lessonDate') {
            formData[key] = dayjs(data[key]);
          } else if (key === 'startTime' || key === 'endTime') {
            formData[key] = dayjs(data[key], 'HH:mm');
          } else {
            formData[key] = data[key];
          }
        }
      });
    }
  } catch (error) {
    message.error('加载课程详情失败');
    smartSentry.captureError(error);
  }
};

const generateLessonNo = () => {
  const now = dayjs();
  formData.lessonNo = `LS${now.format('YYYYMMDD')}${Math.random().toString().slice(-4)}`;
};

const onLessonTypeChange = () => {
  // 根据课程类型过滤可用教练
  filterAvailableCoaches();
};

const onCoachChange = () => {
  // 根据教练过滤可用马匹
  filterAvailableHorses();
  // 清空之前的马匹选择
  formData.horseId = null;
};

const onMemberChange = () => {
  // 可以根据会员信息预填充一些数据
};

const onDateChange = () => {
  // 清空冲突检测结果
  conflictInfo.value = [];
  recommendedSlots.value = [];
};

const onStartTimeChange = () => {
  // 清空冲突检测结果
  conflictInfo.value = [];
};

const filterAvailableCoaches = () => {
  if (!formData.lessonType) {
    availableCoaches.value = [...coachList.value];
    return;
  }
  
  // 根据课程类型过滤教练
  availableCoaches.value = coachList.value.filter(coach => {
    // 这里应该根据教练的专业能力来过滤
    return true;
  });
};

const filterAvailableHorses = () => {
  if (!formData.coachId) {
    availableHorses.value = [];
    return;
  }
  
  // 根据教练筛选可用马匹
  availableHorses.value = horseList.value.filter(horse => {
    // 这里应该根据教练和马匹的匹配关系来过滤
    return true;
  });
};

const calculateDuration = () => {
  if (formData.startTime && formData.endTime) {
    const start = dayjs(formData.startTime);
    const end = dayjs(formData.endTime);
    if (end.isAfter(start)) {
      formData.duration = end.diff(start, 'minute');
    }
  }
};

const disabledDate = (current) => {
  // 不能选择过去的日期
  return current && current < dayjs().startOf('day');
};

const detectConflict = async () => {
  if (!formData.lessonDate || !formData.startTime || !formData.endTime) {
    message.warning('请先选择课程日期和时间');
    return;
  }

  try {
    detectingConflict.value = true;
    const res = await scheduleApi.detectConflict({
      coachId: formData.coachId,
      horseId: formData.horseId,
      lessonDate: formData.lessonDate.format('YYYY-MM-DD'),
      startTime: formData.startTime.format('HH:mm'),
      endTime: formData.endTime.format('HH:mm'),
      excludeId: isEdit.value ? route.params.id : null
    });

    if (res.ok) {
      conflictInfo.value = res.data.conflicts || [];
      if (conflictInfo.value.length === 0) {
        message.success('时间安排无冲突');
      }
    }
  } catch (error) {
    message.error('冲突检测失败');
    smartSentry.captureError(error);
  } finally {
    detectingConflict.value = false;
  }
};

const getRecommendations = async () => {
  if (!formData.coachId || !formData.lessonDate) {
    message.warning('请先选择教练和课程日期');
    return;
  }

  try {
    gettingRecommendations.value = true;
    const res = await scheduleApi.getRecommendedSlots({
      coachId: formData.coachId,
      lessonDate: formData.lessonDate.format('YYYY-MM-DD'),
      duration: formData.duration || 60
    });

    if (res.ok) {
      recommendedSlots.value = res.data.slots || [];
    }
  } catch (error) {
    message.error('获取推荐时段失败');
    smartSentry.captureError(error);
  } finally {
    gettingRecommendations.value = false;
  }
};

const selectRecommendedSlot = (slot) => {
  formData.startTime = dayjs(slot.startTime, 'HH:mm');
  formData.endTime = dayjs(slot.endTime, 'HH:mm');
  conflictInfo.value = [];
  recommendedSlots.value = [];
  message.success('已选择推荐时段');
};

const filterOption = (input, option) => {
  return option.children[0].children.toLowerCase().includes(input.toLowerCase());
};

const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    
    if (conflictInfo.value.length > 0) {
      message.error('存在时间冲突，请先解决冲突');
      return;
    }

    submitting.value = true;

    const submitData = {
      ...formData,
      lessonDate: formData.lessonDate.format('YYYY-MM-DD'),
      startTime: formData.startTime.format('HH:mm'),
      endTime: formData.endTime.format('HH:mm')
    };

    let res;
    if (isEdit.value) {
      res = await scheduleApi.updateSchedule(route.params.id, submitData);
    } else {
      res = await scheduleApi.addSchedule(submitData);
    }

    if (res.ok) {
      message.success(isEdit.value ? '更新成功' : '保存成功');
      router.push('/schedule/manage');
    } else {
      message.error(res.msg || '操作失败');
    }
  } catch (error) {
    if (error.errorFields) {
      message.error('请完善表单信息');
    } else {
      message.error('操作失败');
      smartSentry.captureError(error);
    }
  } finally {
    submitting.value = false;
  }
};

const goBack = () => {
  router.back();
};
</script>

<style scoped>
.schedule-add-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.ant-divider-horizontal.ant-divider-with-text-left::before {
  width: 5%;
}

.ant-divider-horizontal.ant-divider-with-text-left::after {
  width: 95%;
}

:deep(.ant-form-item-label) {
  font-weight: 500;
}
</style>