<!--
  * 预约详情页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="booking-detail-container">
    <a-card :loading="loading" style="margin-bottom: 16px;">
      <template #title>
        <div style="display: flex; align-items: center; justify-content: space-between;">
          <div>
            <ArrowLeftOutlined @click="goBack" style="margin-right: 8px; cursor: pointer;" />
            预约详情
          </div>
          <div>
            <a-button 
              type="primary" 
              ghost 
              @click="editBooking" 
              v-if="bookingDetail.bookingStatus <= 2"
              v-privilege="'business:booking:update'"
              style="margin-right: 8px;"
            >
              <EditOutlined />
              编辑
            </a-button>
            <a-button 
              @click="confirmBooking" 
              v-if="bookingDetail.bookingStatus === 1"
              v-privilege="'business:booking:confirm'"
              style="margin-right: 8px;"
            >
              <CheckOutlined />
              确认预约
            </a-button>
            <a-button 
              danger 
              @click="cancelBooking" 
              v-if="bookingDetail.bookingStatus <= 2"
              v-privilege="'business:booking:cancel'"
            >
              <CloseOutlined />
              取消预约
            </a-button>
          </div>
        </div>
      </template>

      <!-- 预约基础信息 -->
      <a-row :gutter="24">
        <a-col :span="12">
          <a-descriptions title="预约信息" :column="1" bordered>
            <a-descriptions-item label="预约单号">
              {{ bookingDetail.bookingNo }}
            </a-descriptions-item>
            <a-descriptions-item label="预约状态">
              <a-tag :color="getStatusColor(bookingDetail.bookingStatus)">
                {{ bookingDetail.bookingStatusName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="课程类型">
              <a-tag :color="getLessonTypeColor(bookingDetail.lessonType)">
                {{ bookingDetail.lessonTypeName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="预约费用">
              <span style="font-size: 18px; font-weight: bold; color: #ff4d4f;">
                ¥{{ bookingDetail.totalFee }}
              </span>
            </a-descriptions-item>
            <a-descriptions-item label="预付款">
              ¥{{ bookingDetail.deposit || 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="付费方式">
              {{ getPaymentMethodName(bookingDetail.paymentMethod) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="12">
          <a-descriptions title="时间安排" :column="1" bordered>
            <a-descriptions-item label="预约日期">
              {{ formatDate(bookingDetail.bookingDate) }}
            </a-descriptions-item>
            <a-descriptions-item label="开始时间">
              {{ bookingDetail.startTime }}
            </a-descriptions-item>
            <a-descriptions-item label="结束时间">
              {{ bookingDetail.endTime }}
            </a-descriptions-item>
            <a-descriptions-item label="课程时长">
              {{ bookingDetail.duration }} 分钟
            </a-descriptions-item>
            <a-descriptions-item label="创建时间">
              {{ formatDateTime(bookingDetail.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="确认时间">
              {{ formatDateTime(bookingDetail.confirmTime) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>

      <!-- 参与人员信息 -->
      <a-divider />
      <a-row :gutter="24">
        <a-col :span="8">
          <a-descriptions title="会员信息" :column="1" bordered>
            <a-descriptions-item label="会员姓名">
              <a-avatar size="small" :src="bookingDetail.memberInfo?.avatar" style="margin-right: 8px;" />
              {{ bookingDetail.memberInfo?.name }}
            </a-descriptions-item>
            <a-descriptions-item label="联系电话">
              {{ bookingDetail.memberInfo?.phone }}
            </a-descriptions-item>
            <a-descriptions-item label="会员等级">
              <a-tag :color="getMemberLevelColor(bookingDetail.memberInfo?.memberLevel)">
                {{ bookingDetail.memberInfo?.memberLevelName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="骑乘经验">
              {{ bookingDetail.memberInfo?.ridingExperience }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="8">
          <a-descriptions title="教练信息" :column="1" bordered>
            <a-descriptions-item label="教练姓名">
              <a-avatar size="small" :src="bookingDetail.coachInfo?.avatar" style="margin-right: 8px;" />
              {{ bookingDetail.coachInfo?.name }}
            </a-descriptions-item>
            <a-descriptions-item label="联系电话">
              {{ bookingDetail.coachInfo?.phone }}
            </a-descriptions-item>
            <a-descriptions-item label="专业特长">
              {{ bookingDetail.coachInfo?.specialties }}
            </a-descriptions-item>
            <a-descriptions-item label="等级">
              {{ bookingDetail.coachInfo?.level }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="8">
          <a-descriptions title="马匹信息" :column="1" bordered>
            <a-descriptions-item label="马匹名称">
              {{ bookingDetail.horseInfo?.name }}
            </a-descriptions-item>
            <a-descriptions-item label="品种">
              {{ bookingDetail.horseInfo?.breed }}
            </a-descriptions-item>
            <a-descriptions-item label="年龄">
              {{ bookingDetail.horseInfo?.age }} 岁
            </a-descriptions-item>
            <a-descriptions-item label="性格特点">
              {{ bookingDetail.horseInfo?.temperament }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>

      <!-- 联系信息 -->
      <a-divider />
      <a-descriptions title="联系信息" :column="2" bordered>
        <a-descriptions-item label="联系人">
          {{ bookingDetail.contactName }}
        </a-descriptions-item>
        <a-descriptions-item label="联系电话">
          {{ bookingDetail.contactPhone }}
        </a-descriptions-item>
      </a-descriptions>

      <!-- 预约备注 -->
      <a-divider />
      <div>
        <h4 style="margin-bottom: 16px;">
          <FileTextOutlined style="margin-right: 8px;" />
          预约备注
        </h4>
        <div style="
          background: #fafafa; 
          padding: 16px; 
          border-radius: 6px;
          min-height: 100px;
          white-space: pre-wrap;
        ">
          {{ bookingDetail.bookingNotes || '暂无备注' }}
        </div>
      </div>

      <!-- 特殊要求 -->
      <div v-if="bookingDetail.specialRequests">
        <a-divider />
        <div>
          <h4 style="margin-bottom: 16px;">
            <ExclamationCircleOutlined style="margin-right: 8px;" />
            特殊要求
          </h4>
          <div style="
            background: #fff7e6; 
            padding: 16px; 
            border-radius: 6px;
            border-left: 4px solid #faad14;
            white-space: pre-wrap;
          ">
            {{ bookingDetail.specialRequests }}
          </div>
        </div>
      </div>

      <!-- 预约记录 -->
      <div v-if="bookingDetail.bookingStatus >= 4">
        <a-divider />
        <div>
          <h4 style="margin-bottom: 16px;">
            <HistoryOutlined style="margin-right: 8px;" />
            预约记录
          </h4>
          <a-descriptions :column="2" bordered v-if="bookingDetail.bookingRecord">
            <a-descriptions-item label="实际开始时间">
              {{ bookingDetail.bookingRecord.actualStartTime }}
            </a-descriptions-item>
            <a-descriptions-item label="实际结束时间">
              {{ bookingDetail.bookingRecord.actualEndTime }}
            </a-descriptions-item>
            <a-descriptions-item label="服务评价" :span="2">
              {{ bookingDetail.bookingRecord.serviceEvaluation }}
            </a-descriptions-item>
            <a-descriptions-item label="客户反馈" :span="2">
              {{ bookingDetail.bookingRecord.customerFeedback }}
            </a-descriptions-item>
            <a-descriptions-item label="处理备注" :span="2">
              {{ bookingDetail.bookingRecord.processingNotes }}
            </a-descriptions-item>
          </a-descriptions>
          <div v-else style="text-align: center; color: #999; padding: 40px;">
            暂无预约记录
          </div>
        </div>
      </div>

      <!-- 操作记录 -->
      <a-divider />
      <a-descriptions title="操作记录" :column="2" bordered>
        <a-descriptions-item label="创建人">
          {{ bookingDetail.createBy }}
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ formatDateTime(bookingDetail.createTime) }}
        </a-descriptions-item>
        <a-descriptions-item label="更新人">
          {{ bookingDetail.updateBy }}
        </a-descriptions-item>
        <a-descriptions-item label="更新时间">
          {{ formatDateTime(bookingDetail.updateTime) }}
        </a-descriptions-item>
      </a-descriptions>
    </a-card>

    <!-- 快速操作 -->
    <a-card title="快速操作" v-if="bookingDetail.bookingStatus === 2">
      <a-space>
        <a-button 
          type="primary" 
          @click="checkinBooking"
          v-privilege="'business:booking:checkin'"
        >
          <LoginOutlined />
          核销预约
        </a-button>
        <a-button 
          @click="rescheduleBooking"
          v-privilege="'business:booking:reschedule'"
        >
          <SwapOutlined />
          改期
        </a-button>
        <a-button 
          @click="addBookingRecord"
          v-privilege="'business:booking:record'"
        >
          <FileAddOutlined />
          添加记录
        </a-button>
      </a-space>
    </a-card>

    <a-card title="快速操作" v-else-if="bookingDetail.bookingStatus === 3">
      <a-space>
        <a-button 
          type="primary" 
          @click="completeBooking"
          v-privilege="'business:booking:complete'"
        >
          <CheckCircleOutlined />
          完成预约
        </a-button>
        <a-button 
          @click="addBookingRecord"
          v-privilege="'business:booking:record'"
        >
          <FileAddOutlined />
          添加记录
        </a-button>
      </a-space>
    </a-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { 
  ArrowLeftOutlined, 
  EditOutlined, 
  CheckOutlined,
  CloseOutlined,
  FileTextOutlined,
  ExclamationCircleOutlined,
  HistoryOutlined,
  LoginOutlined,
  SwapOutlined,
  FileAddOutlined,
  CheckCircleOutlined
} from '@ant-design/icons-vue';
import { bookingApi } from '/@/api/business/booking/booking-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();

// 响应式数据
const loading = ref(false);
const bookingDetail = ref({});

// 生命周期
onMounted(() => {
  loadBookingDetail();
});

// 方法
const loadBookingDetail = async () => {
  try {
    loading.value = true;
    const bookingId = route.params.id;
    const res = await bookingApi.getBookingDetail(bookingId);
    if (res.ok) {
      bookingDetail.value = res.data;
    } else {
      message.error(res.msg || '获取预约详情失败');
    }
  } catch (error) {
    message.error('获取预约详情失败');
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.back();
};

const editBooking = () => {
  router.push(`/booking/edit/${route.params.id}`);
};

const confirmBooking = async () => {
  try {
    const res = await bookingApi.confirmBooking(bookingDetail.value.bookingId);
    if (res.ok) {
      message.success('预约确认成功');
      loadBookingDetail();
    } else {
      message.error(res.msg || '确认失败');
    }
  } catch (error) {
    message.error('确认失败');
    smartSentry.captureError(error);
  }
};

const cancelBooking = () => {
  Modal.confirm({
    title: '确认取消',
    content: `确定要取消预约"${bookingDetail.value.bookingNo}"吗？`,
    okText: '确认取消',
    cancelText: '取消',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await bookingApi.cancelBooking(bookingDetail.value.bookingId);
        if (res.ok) {
          message.success('预约已取消');
          loadBookingDetail();
        } else {
          message.error(res.msg || '取消失败');
        }
      } catch (error) {
        message.error('取消失败');
        smartSentry.captureError(error);
      }
    }
  });
};

const checkinBooking = async () => {
  try {
    const res = await bookingApi.checkinBooking(bookingDetail.value.bookingId);
    if (res.ok) {
      message.success('核销成功');
      loadBookingDetail();
    } else {
      message.error(res.msg || '核销失败');
    }
  } catch (error) {
    message.error('核销失败');
    smartSentry.captureError(error);
  }
};

const completeBooking = async () => {
  try {
    const res = await bookingApi.completeBooking(bookingDetail.value.bookingId);
    if (res.ok) {
      message.success('预约已完成');
      loadBookingDetail();
    } else {
      message.error(res.msg || '操作失败');
    }
  } catch (error) {
    message.error('操作失败');
    smartSentry.captureError(error);
  }
};

const rescheduleBooking = () => {
  message.info('改期功能待实现');
};

const addBookingRecord = () => {
  message.info('添加记录功能待实现');
};

const getStatusColor = (status) => {
  const colors = {
    1: 'default',    // 待确认
    2: 'processing', // 已确认
    3: 'warning',    // 进行中
    4: 'success',    // 已完成
    5: 'error',      // 已取消
    6: 'red'         // 未到场
  };
  return colors[status] || 'default';
};

const getLessonTypeColor = (type) => {
  const colors = {
    1: 'blue',    // 私教课
    2: 'green',   // 小组课
    3: 'orange'   // 体验课
  };
  return colors[type] || 'default';
};

const getMemberLevelColor = (level) => {
  const colors = {
    1: '#cd7f32',  // 铜牌
    2: '#c0c0c0',  // 银牌
    3: '#ffd700',  // 金牌
    4: '#b9f2ff'   // 钻石
  };
  return colors[level] || 'default';
};

const getPaymentMethodName = (method) => {
  const methods = {
    1: '现金支付',
    2: '银行转账',
    3: '在线支付',
    4: '会员卡扣费'
  };
  return methods[method] || '未知';
};

const formatDate = (date) => {
  if (!date) return '-';
  return dayjs(date).format('YYYY-MM-DD dddd');
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm:ss');
};
</script>

<style scoped>
.booking-detail-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.ant-descriptions-title {
  font-weight: 600;
  font-size: 16px;
}

.ant-descriptions-item-label {
  font-weight: 500;
  background-color: #fafafa;
}

.ant-divider {
  margin: 24px 0;
}
</style>