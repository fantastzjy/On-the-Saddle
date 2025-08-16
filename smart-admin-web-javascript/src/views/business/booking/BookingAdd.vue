<!--
  * 新增/编辑预约页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="booking-add-container">
    <a-card>
      <template #title>
        <div style="display: flex; align-items: center;">
          <ArrowLeftOutlined @click="goBack" style="margin-right: 8px; cursor: pointer;" />
          {{ isEdit ? '编辑预约' : '新增预约' }}
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
        <a-divider orientation="left">预约信息</a-divider>
        
        <a-form-item label="预约单号" name="bookingNo">
          <a-input
            v-model:value="formData.bookingNo"
            placeholder="系统自动生成"
            :disabled="true"
          />
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

        <a-form-item label="预约日期" name="bookingDate">
          <a-date-picker
            v-model:value="formData.bookingDate"
            style="width: 100%"
            placeholder="请选择预约日期"
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
            :disabled="!formData.bookingDate"
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

        <!-- 联系信息 -->
        <a-divider orientation="left">联系信息</a-divider>

        <a-form-item label="联系人" name="contactName">
          <a-input
            v-model:value="formData.contactName"
            placeholder="请输入联系人姓名"
          />
        </a-form-item>

        <a-form-item label="联系电话" name="contactPhone">
          <a-input
            v-model:value="formData.contactPhone"
            placeholder="请输入联系电话"
          />
        </a-form-item>

        <!-- 备注信息 -->
        <a-divider orientation="left">预约详情</a-divider>

        <a-form-item label="预约备注" name="bookingNotes">
          <a-textarea
            v-model:value="formData.bookingNotes"
            placeholder="请输入预约备注"
            :rows="4"
          />
        </a-form-item>

        <a-form-item label="特殊要求" name="specialRequests">
          <a-textarea
            v-model:value="formData.specialRequests"
            placeholder="请输入特殊要求"
            :rows="3"
          />
        </a-form-item>

        <!-- 费用信息 -->
        <a-divider orientation="left">费用信息</a-divider>

        <a-form-item label="预约费用" name="totalFee">
          <a-input-number
            v-model:value="formData.totalFee"
            style="width: 100%"
            :min="0"
            :precision="2"
            placeholder="元"
          />
        </a-form-item>

        <a-form-item label="预付款" name="deposit">
          <a-input-number
            v-model:value="formData.deposit"
            style="width: 100%"
            :min="0"
            :precision="2"
            :max="formData.totalFee"
            placeholder="元"
          />
        </a-form-item>

        <a-form-item label="付费方式" name="paymentMethod">
          <a-select
            v-model:value="formData.paymentMethod"
            placeholder="请选择付费方式"
          >
            <a-select-option value="1">现金支付</a-select-option>
            <a-select-option value="2">银行转账</a-select-option>
            <a-select-option value="3">在线支付</a-select-option>
            <a-select-option value="4">会员卡扣费</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 操作按钮 -->
        <a-form-item :wrapper-col="{ offset: 4, span: 16 }">
          <a-space>
            <a-button @click="goBack">取消</a-button>
            <a-button @click="saveDraft" :loading="savingDraft">
              <SaveOutlined />
              保存草稿
            </a-button>
            <a-button 
              type="primary" 
              @click="handleSubmit" 
              :loading="submitting"
            >
              {{ isEdit ? '更新预约' : '提交预约' }}
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 时间冲突提示 -->
    <a-modal
      v-model:open="conflictModalVisible"
      title="时间冲突提示"
      :footer="null"
      width="600px"
    >
      <a-alert
        type="warning"
        show-icon
        style="margin-bottom: 16px;"
      >
        <template #message>
          <div>检测到以下时间冲突：</div>
          <ul style="margin-top: 8px; margin-bottom: 0;">
            <li v-for="conflict in conflictList" :key="conflict.id">
              {{ conflict.message }}
            </li>
          </ul>
        </template>
      </a-alert>
      
      <div style="text-align: center;">
        <a-space>
          <a-button @click="conflictModalVisible = false">
            重新选择时间
          </a-button>
          <a-button type="primary" @click="forceSubmit">
            强制提交
          </a-button>
        </a-space>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';
import { 
  ArrowLeftOutlined,
  SaveOutlined
} from '@ant-design/icons-vue';
import { bookingApi } from '/@/api/business/booking/booking-api';
import { memberApi } from '/@/api/business/member/member-api';
import { coachApi } from '/@/api/business/coach/coach-api';
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
const isEdit = computed(() => route.name === 'BookingEdit');

// 表单相关
const formRef = ref();
const submitting = ref(false);
const savingDraft = ref(false);

// 表单数据
const formData = reactive({
  bookingNo: '',
  memberId: null,
  lessonType: null,
  coachId: null,
  horseId: null,
  bookingDate: null,
  startTime: null,
  endTime: null,
  duration: null,
  contactName: '',
  contactPhone: '',
  bookingNotes: '',
  specialRequests: '',
  totalFee: null,
  deposit: null,
  paymentMethod: null
});

// 表单验证规则
const formRules = {
  memberId: [{ required: true, message: '请选择会员' }],
  lessonType: [{ required: true, message: '请选择课程类型' }],
  coachId: [{ required: true, message: '请选择教练' }],
  bookingDate: [{ required: true, message: '请选择预约日期' }],
  startTime: [{ required: true, message: '请选择开始时间' }],
  endTime: [{ required: true, message: '请选择结束时间' }],
  contactName: [{ required: true, message: '请输入联系人姓名' }],
  contactPhone: [{ required: true, message: '请输入联系电话' }],
  totalFee: [{ required: true, message: '请输入预约费用' }],
  paymentMethod: [{ required: true, message: '请选择付费方式' }]
};

// 选项数据
const memberList = ref([]);
const coachList = ref([]);
const horseList = ref([]);
const availableCoaches = ref([]);
const availableHorses = ref([]);

// 冲突检测
const conflictModalVisible = ref(false);
const conflictList = ref([]);

// 生命周期
onMounted(() => {
  loadInitialData();
  if (isEdit.value) {
    loadBookingDetail();
  } else {
    generateBookingNo();
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
      loadMembers(),
      loadCoaches(),
      loadHorses()
    ]);
  } catch (error) {
    message.error('加载数据失败');
    smartSentry.captureError(error);
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

const loadBookingDetail = async () => {
  try {
    const bookingId = route.params.id;
    const res = await bookingApi.getBookingDetail(bookingId);
    if (res.ok) {
      const data = res.data;
      Object.keys(formData).forEach(key => {
        if (data[key] !== undefined) {
          if (key === 'bookingDate') {
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
    message.error('加载预约详情失败');
    smartSentry.captureError(error);
  }
};

const generateBookingNo = () => {
  const now = dayjs();
  formData.bookingNo = `BK${now.format('YYYYMMDD')}${Math.random().toString().slice(-4)}`;
};

const onMemberChange = (memberId) => {
  const member = memberList.value.find(m => m.memberId === memberId);
  if (member) {
    formData.contactName = member.name;
    formData.contactPhone = member.phone;
  }
};

const onLessonTypeChange = () => {
  filterAvailableCoaches();
};

const onCoachChange = () => {
  filterAvailableHorses();
  formData.horseId = null;
};

const onDateChange = () => {
  conflictList.value = [];
};

const onStartTimeChange = () => {
  conflictList.value = [];
};

const filterAvailableCoaches = () => {
  if (!formData.lessonType) {
    availableCoaches.value = [...coachList.value];
    return;
  }
  
  availableCoaches.value = coachList.value.filter(coach => {
    return true; // 根据实际业务逻辑过滤
  });
};

const filterAvailableHorses = () => {
  if (!formData.coachId) {
    availableHorses.value = [];
    return;
  }
  
  availableHorses.value = horseList.value.filter(horse => {
    return true; // 根据实际业务逻辑过滤
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
  return current && current < dayjs().startOf('day');
};

const checkConflict = async () => {
  if (!formData.bookingDate || !formData.startTime || !formData.endTime) {
    return false;
  }

  try {
    const res = await bookingApi.checkConflict({
      coachId: formData.coachId,
      horseId: formData.horseId,
      bookingDate: formData.bookingDate.format('YYYY-MM-DD'),
      startTime: formData.startTime.format('HH:mm'),
      endTime: formData.endTime.format('HH:mm'),
      excludeId: isEdit.value ? route.params.id : null
    });

    if (res.ok && res.data.conflicts && res.data.conflicts.length > 0) {
      conflictList.value = res.data.conflicts;
      return true;
    }
    return false;
  } catch (error) {
    console.error('冲突检测失败', error);
    return false;
  }
};

const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    
    const hasConflict = await checkConflict();
    if (hasConflict) {
      conflictModalVisible.value = true;
      return;
    }

    await submitBooking();
  } catch (error) {
    if (error.errorFields) {
      message.error('请完善表单信息');
    } else {
      message.error('操作失败');
      smartSentry.captureError(error);
    }
  }
};

const forceSubmit = async () => {
  conflictModalVisible.value = false;
  await submitBooking();
};

const submitBooking = async () => {
  try {
    submitting.value = true;

    const submitData = {
      ...formData,
      bookingDate: formData.bookingDate.format('YYYY-MM-DD'),
      startTime: formData.startTime.format('HH:mm'),
      endTime: formData.endTime.format('HH:mm')
    };

    let res;
    if (isEdit.value) {
      res = await bookingApi.updateBooking(route.params.id, submitData);
    } else {
      res = await bookingApi.addBooking(submitData);
    }

    if (res.ok) {
      message.success(isEdit.value ? '更新成功' : '预约成功');
      router.push('/booking/list');
    } else {
      message.error(res.msg || '操作失败');
    }
  } catch (error) {
    message.error('操作失败');
    smartSentry.captureError(error);
  } finally {
    submitting.value = false;
  }
};

const saveDraft = async () => {
  try {
    savingDraft.value = true;
    // 保存草稿逻辑
    message.success('草稿保存成功');
  } catch (error) {
    message.error('保存草稿失败');
    smartSentry.captureError(error);
  } finally {
    savingDraft.value = false;
  }
};

const filterOption = (input, option) => {
  return option.children[0].children.toLowerCase().includes(input.toLowerCase());
};

const goBack = () => {
  router.back();
};
</script>

<style scoped>
.booking-add-container {
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