<template>
  <a-select
    v-model:value="selectedValue"
    :loading="loading"
    :placeholder="placeholder"
    allow-clear
    @change="handleChange"
    :disabled="!memberId"
  >
    <a-select-option
      v-for="booking in bookingList"
      :key="booking.bookingId"
      :value="booking.bookingId"
      :title="getBookingTitle(booking)"
    >
      <div class="booking-option">
        <div class="booking-main">
          {{ booking.productName }} - {{ formatDate(booking.startTime) }}
        </div>
        <div class="booking-info">
          教练: {{ booking.coachName }} | 状态: {{ getBookingStatusText(booking.bookingStatus) }}
        </div>
        <div class="booking-fee">
          应付: ¥{{ booking.totalFee || 0 }} | 已付: ¥{{ booking.paidFee || 0 }}
          <span v-if="getDifferenceFee(booking) > 0" class="need-extra">
            | 可补差: ¥{{ getDifferenceFee(booking) }}
          </span>
        </div>
      </div>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { bookingApi } from '/@/api/business/booking/booking-api';
import dayjs from 'dayjs';

const props = defineProps({
  value: {
    type: [Number, String],
    default: null
  },
  placeholder: {
    type: String,
    default: '请选择关联预约'
  },
  memberId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:value', 'change']);

const selectedValue = ref(props.value);
const loading = ref(false);
const bookingList = ref([]);

// 加载预约列表
const loadBookingList = async () => {
  if (!props.memberId) {
    bookingList.value = [];
    return;
  }
  
  try {
    loading.value = true;
    const params = {
      pageNum: 1,
      pageSize: 50,
      memberId: props.memberId,
      bookingStatus: [1, 2], // 已预约、已核销的预约才能补差
    };
    
    // 模拟API调用，实际需要替换为真实API
    const result = await bookingApi.getBookingList(params);
    bookingList.value = result.data?.list || [];
  } catch (error) {
    console.error('加载预约列表失败:', error);
    // 提供模拟数据用于测试
    bookingList.value = [
      {
        bookingId: 1,
        productName: '单人课程',
        startTime: new Date(),
        coachName: '张教练',
        bookingStatus: 2,
        totalFee: 300,
        paidFee: 250
      }
    ];
  } finally {
    loading.value = false;
  }
};

// 获取预约标题
const getBookingTitle = (booking) => {
  return `${booking.productName} - ${formatDate(booking.startTime)} - ${booking.coachName}`;
};

// 获取预约状态文本
const getBookingStatusText = (status) => {
  const statusMap = {
    1: '已预约',
    2: '已核销', 
    3: '已取消',
    4: '已过期'
  };
  return statusMap[status] || '未知状态';
};

// 计算可补差金额
const getDifferenceFee = (booking) => {
  return Math.max(0, (booking.totalFee || 0) - (booking.paidFee || 0));
};

// 格式化日期
const formatDate = (date) => {
  return dayjs(date).format('MM-DD HH:mm');
};

// 选择变更处理
const handleChange = (value) => {
  const selectedBooking = bookingList.value.find(b => b.bookingId === value);
  emit('update:value', value);
  emit('change', value, selectedBooking);
};

// 监听外部值变化
watch(() => props.value, (newValue) => {
  selectedValue.value = newValue;
});

// 监听会员ID变化
watch(() => props.memberId, () => {
  selectedValue.value = null;
  emit('update:value', null);
  loadBookingList();
});

// 当会员ID存在时立即加载数据
watch(() => props.memberId, (newMemberId) => {
  if (newMemberId) {
    loadBookingList();
  }
}, { immediate: true });
</script>

<style scoped>
.booking-option {
  padding: 6px 0;
}

.booking-main {
  font-weight: 500;
  color: #262626;
  margin-bottom: 2px;
}

.booking-info {
  font-size: 12px;
  color: #8c8c8c;
  margin-bottom: 2px;
}

.booking-fee {
  font-size: 12px;
  color: #595959;
}

.need-extra {
  color: #fa541c;
  font-weight: 500;
}
</style>