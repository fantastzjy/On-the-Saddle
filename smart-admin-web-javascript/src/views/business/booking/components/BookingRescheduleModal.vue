<!--
  * 预约改期弹窗组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-21
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-modal
    :open="visible"
    title="预约改期"
    @ok="handleReschedule"
    @cancel="handleCancel"
    :confirmLoading="loading"
    width="600px"
  >
    <a-form
      ref="formRef"
      :model="rescheduleForm"
      :rules="formRules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
    >
      <a-form-item label="当前时间">
        <span>{{ formatCurrentTime() }}</span>
      </a-form-item>
      
      <a-form-item label="新开始时间" name="newStartTime">
        <a-date-picker
          v-model:value="rescheduleForm.newStartTime"
          show-time
          placeholder="请选择新开始时间"
          :disabled-date="disabledDate"
          format="YYYY-MM-DD HH:mm"
          valueFormat="YYYY-MM-DD HH:mm:ss"
        />
      </a-form-item>
      
      <a-form-item label="新结束时间" name="newEndTime">
        <a-date-picker
          v-model:value="rescheduleForm.newEndTime"
          show-time  
          placeholder="请选择新结束时间"
          :disabled-date="disabledDate"
          format="YYYY-MM-DD HH:mm"
          valueFormat="YYYY-MM-DD HH:mm:ss"
        />
      </a-form-item>
      
      <a-form-item label="改期原因" name="rescheduleReason">
        <a-textarea
          v-model:value="rescheduleForm.rescheduleReason"
          placeholder="请输入改期原因"
          :rows="3"
        />
      </a-form-item>
      
      <!-- 冲突检测结果显示 -->
      <a-alert
        v-if="conflictResult && conflictResult.hasConflict"
        :message="`时间冲突：${conflictResult.conflictMessage}`"
        type="error"
        show-icon
        style="margin-top: 16px;"
      />
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { message } from 'ant-design-vue';
import { bookingApi } from '/@/api/business/booking/booking-api';
import dayjs from 'dayjs';

// Props
const props = defineProps({
  visible: Boolean,
  bookingInfo: Object
});

// 调试信息 - 监听 bookingInfo 的变化
watch(() => props.bookingInfo, (newBookingInfo) => {
  if (newBookingInfo) {
    console.log('BookingInfo received:', newBookingInfo);
    console.log('BookingDate:', newBookingInfo.bookingDate);
    console.log('StartTime:', newBookingInfo.startTime);
    console.log('EndTime:', newBookingInfo.endTime);
  }
}, { immediate: true });

// Emits
const emit = defineEmits(['update:visible', 'success']);

// 表单数据
const rescheduleForm = reactive({
  bookingId: null,
  newStartTime: null,
  newEndTime: null,
  rescheduleReason: ''
});

// 冲突检测结果
const conflictResult = ref(null);
const loading = ref(false);

// 表单验证规则
const formRules = {
  newStartTime: [{ required: true, message: '请选择新开始时间' }],
  newEndTime: [{ required: true, message: '请选择新结束时间' }],
  rescheduleReason: [{ required: true, message: '请输入改期原因' }]
};

// 监听时间变化，自动检测冲突
watch([() => rescheduleForm.newStartTime, () => rescheduleForm.newEndTime], 
  async ([newStart, newEnd]) => {
    if (newStart && newEnd && props.bookingInfo) {
      await checkConflict();
    }
  });

// 冲突检测
const checkConflict = async () => {
  try {
    const res = await bookingApi.checkRescheduleConflict({
      bookingId: props.bookingInfo.bookingId,
      newStartTime: rescheduleForm.newStartTime,
      newEndTime: rescheduleForm.newEndTime
    });
    
    if (res.ok) {
      conflictResult.value = res.data;
    }
  } catch (error) {
    console.error('冲突检测失败', error);
  }
};

// 提交改期
const handleReschedule = async () => {
  try {
    loading.value = true;
    
    // 检查是否有冲突
    if (conflictResult.value?.hasConflict) {
      message.error('存在时间冲突，无法改期');
      return;
    }
    
    const res = await bookingApi.rescheduleBooking({
      ...rescheduleForm,
      bookingId: props.bookingInfo.bookingId
    });
    
    if (res.ok) {
      message.success('改期成功');
      emit('success');
      handleCancel();
    } else {
      message.error(res.msg || '改期失败');
    }
  } catch (error) {
    message.error('改期失败');
  } finally {
    loading.value = false;
  }
};

// 取消改期
const handleCancel = () => {
  Object.assign(rescheduleForm, {
    bookingId: null,
    newStartTime: null,
    newEndTime: null, 
    rescheduleReason: ''
  });
  conflictResult.value = null;
  emit('update:visible', false);
};

// 禁用过去的日期
const disabledDate = (current) => {
  return current && current < dayjs().startOf('day');
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return '';
  
  // 处理不同的时间格式
  let date;
  if (typeof dateTime === 'string') {
    // 处理字符串格式的时间
    date = dayjs(dateTime);
  } else if (dateTime instanceof Date) {
    // 处理 Date 对象
    date = dayjs(dateTime);
  } else {
    // 处理其他格式
    date = dayjs(dateTime);
  }
  
  // 检查是否是有效日期
  if (!date.isValid()) {
    console.warn('Invalid date:', dateTime);
    return '';
  }
  
  return date.format('YYYY-MM-DD HH:mm');
};

// 格式化当前预约时间的方法
const formatCurrentTime = () => {
  if (!props.bookingInfo) return '';
  
  const { bookingDate, startTime, endTime } = props.bookingInfo;
  
  if (!bookingDate) return '';
  
  // 检查 bookingDate 是否已经包含完整的日期时间
  const bookingDateTime = dayjs(bookingDate);
  
  if (bookingDateTime.isValid()) {
    // 如果 bookingDate 已经是完整的日期时间，直接使用
    // 假设 startTime 和 endTime 是同一天的不同时间点
    if (startTime && endTime) {
      // 提取日期部分，然后组合时间
      const date = bookingDateTime.format('YYYY-MM-DD');
      const startDateTime = dayjs(`${date} ${startTime}`);
      const endDateTime = dayjs(`${date} ${endTime}`);
      
      if (startDateTime.isValid() && endDateTime.isValid()) {
        return `${startDateTime.format('YYYY-MM-DD HH:mm')} - ${endDateTime.format('HH:mm')}`;
      }
    }
    
    // 如果无法解析 startTime 和 endTime，就显示原始的 bookingDate
    return bookingDateTime.format('YYYY-MM-DD HH:mm');
  }
  
  return `${bookingDate} ${startTime || ''} - ${endTime || ''}`;
};
</script>

<style scoped>
.ant-form-item {
  margin-bottom: 16px;
}
</style>