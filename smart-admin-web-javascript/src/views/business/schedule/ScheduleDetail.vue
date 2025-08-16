<!--
  * 课程详情页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="schedule-detail-container">
    <a-card :loading="loading" style="margin-bottom: 16px;">
      <template #title>
        <div style="display: flex; align-items: center; justify-content: space-between;">
          <div>
            <ArrowLeftOutlined @click="goBack" style="margin-right: 8px; cursor: pointer;" />
            课程详情
          </div>
          <div>
            <a-button 
              type="primary" 
              ghost 
              @click="editSchedule" 
              v-privilege="'business:schedule:update'"
              style="margin-right: 8px;"
            >
              <EditOutlined />
              编辑
            </a-button>
            <a-button 
              danger 
              @click="deleteSchedule" 
              v-privilege="'business:schedule:delete'"
            >
              <DeleteOutlined />
              删除
            </a-button>
          </div>
        </div>
      </template>

      <!-- 课程基础信息 -->
      <a-row :gutter="24">
        <a-col :span="12">
          <a-descriptions title="课程信息" :column="1" bordered>
            <a-descriptions-item label="课单号">
              {{ scheduleDetail.lessonNo }}
            </a-descriptions-item>
            <a-descriptions-item label="课程类型">
              <a-tag :color="getLessonTypeColor(scheduleDetail.lessonType)">
                {{ scheduleDetail.lessonTypeName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="课程状态">
              <a-tag :color="getStatusColor(scheduleDetail.lessonStatus)">
                {{ scheduleDetail.lessonStatusName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="课程费用">
              <span style="font-size: 18px; font-weight: bold; color: #ff4d4f;">
                ¥{{ scheduleDetail.lessonFee }}
              </span>
            </a-descriptions-item>
            <a-descriptions-item label="付费方式">
              {{ getPaymentMethodName(scheduleDetail.paymentMethod) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="12">
          <a-descriptions title="时间安排" :column="1" bordered>
            <a-descriptions-item label="课程日期">
              {{ formatDate(scheduleDetail.lessonDate) }}
            </a-descriptions-item>
            <a-descriptions-item label="开始时间">
              {{ scheduleDetail.startTime }}
            </a-descriptions-item>
            <a-descriptions-item label="结束时间">
              {{ scheduleDetail.endTime }}
            </a-descriptions-item>
            <a-descriptions-item label="课程时长">
              {{ scheduleDetail.duration }} 分钟
            </a-descriptions-item>
            <a-descriptions-item label="创建时间">
              {{ formatDateTime(scheduleDetail.createTime) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>

      <!-- 参与人员信息 -->
      <a-divider />
      <a-row :gutter="24">
        <a-col :span="8">
          <a-descriptions title="教练信息" :column="1" bordered>
            <a-descriptions-item label="教练姓名">
              <a-avatar size="small" :src="scheduleDetail.coachInfo?.avatar" style="margin-right: 8px;" />
              {{ scheduleDetail.coachInfo?.name }}
            </a-descriptions-item>
            <a-descriptions-item label="联系电话">
              {{ scheduleDetail.coachInfo?.phone }}
            </a-descriptions-item>
            <a-descriptions-item label="专业特长">
              {{ scheduleDetail.coachInfo?.specialties }}
            </a-descriptions-item>
            <a-descriptions-item label="等级">
              {{ scheduleDetail.coachInfo?.level }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="8">
          <a-descriptions title="会员信息" :column="1" bordered>
            <a-descriptions-item label="会员姓名">
              <a-avatar size="small" :src="scheduleDetail.memberInfo?.avatar" style="margin-right: 8px;" />
              {{ scheduleDetail.memberInfo?.name }}
            </a-descriptions-item>
            <a-descriptions-item label="联系电话">
              {{ scheduleDetail.memberInfo?.phone }}
            </a-descriptions-item>
            <a-descriptions-item label="会员等级">
              <a-tag :color="getMemberLevelColor(scheduleDetail.memberInfo?.memberLevel)">
                {{ scheduleDetail.memberInfo?.memberLevelName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="骑乘经验">
              {{ scheduleDetail.memberInfo?.ridingExperience }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="8">
          <a-descriptions title="马匹信息" :column="1" bordered>
            <a-descriptions-item label="马匹名称">
              {{ scheduleDetail.horseInfo?.name }}
            </a-descriptions-item>
            <a-descriptions-item label="品种">
              {{ scheduleDetail.horseInfo?.breed }}
            </a-descriptions-item>
            <a-descriptions-item label="年龄">
              {{ scheduleDetail.horseInfo?.age }} 岁
            </a-descriptions-item>
            <a-descriptions-item label="性格特点">
              {{ scheduleDetail.horseInfo?.temperament }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>

      <!-- 课程内容 -->
      <a-divider />
      <div>
        <h4 style="margin-bottom: 16px;">
          <BookOutlined style="margin-right: 8px;" />
          课程内容
        </h4>
        <div style="
          background: #fafafa; 
          padding: 16px; 
          border-radius: 6px;
          min-height: 100px;
          white-space: pre-wrap;
        ">
          {{ scheduleDetail.lessonContent || '暂无课程内容' }}
        </div>
      </div>

      <!-- 课程目标 -->
      <a-divider />
      <div>
        <h4 style="margin-bottom: 16px;">
          <TargetOutlined style="margin-right: 8px;" />
          课程目标
        </h4>
        <div style="
          background: #fafafa; 
          padding: 16px; 
          border-radius: 6px;
          min-height: 100px;
          white-space: pre-wrap;
        ">
          {{ scheduleDetail.lessonGoal || '暂无课程目标' }}
        </div>
      </div>

      <!-- 注意事项 -->
      <div v-if="scheduleDetail.notes">
        <a-divider />
        <div>
          <h4 style="margin-bottom: 16px;">
            <ExclamationCircleOutlined style="margin-right: 8px;" />
            注意事项
          </h4>
          <div style="
            background: #fff7e6; 
            padding: 16px; 
            border-radius: 6px;
            border-left: 4px solid #faad14;
            white-space: pre-wrap;
          ">
            {{ scheduleDetail.notes }}
          </div>
        </div>
      </div>

      <!-- 课程记录 -->
      <div v-if="scheduleDetail.lessonStatus >= 3">
        <a-divider />
        <div>
          <h4 style="margin-bottom: 16px;">
            <FileTextOutlined style="margin-right: 8px;" />
            课程记录
          </h4>
          <a-descriptions :column="2" bordered v-if="scheduleDetail.lessonRecord">
            <a-descriptions-item label="实际开始时间">
              {{ scheduleDetail.lessonRecord.actualStartTime }}
            </a-descriptions-item>
            <a-descriptions-item label="实际结束时间">
              {{ scheduleDetail.lessonRecord.actualEndTime }}
            </a-descriptions-item>
            <a-descriptions-item label="教学评价" :span="2">
              {{ scheduleDetail.lessonRecord.teachingEvaluation }}
            </a-descriptions-item>
            <a-descriptions-item label="学员表现" :span="2">
              {{ scheduleDetail.lessonRecord.studentPerformance }}
            </a-descriptions-item>
            <a-descriptions-item label="课程反馈" :span="2">
              {{ scheduleDetail.lessonRecord.feedback }}
            </a-descriptions-item>
          </a-descriptions>
          <div v-else style="text-align: center; color: #999; padding: 40px;">
            暂无课程记录
          </div>
        </div>
      </div>

      <!-- 操作记录 -->
      <a-divider />
      <a-descriptions title="操作记录" :column="2" bordered>
        <a-descriptions-item label="创建人">
          {{ scheduleDetail.createBy }}
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ formatDateTime(scheduleDetail.createTime) }}
        </a-descriptions-item>
        <a-descriptions-item label="更新人">
          {{ scheduleDetail.updateBy }}
        </a-descriptions-item>
        <a-descriptions-item label="更新时间">
          {{ formatDateTime(scheduleDetail.updateTime) }}
        </a-descriptions-item>
      </a-descriptions>
    </a-card>

    <!-- 快速操作 -->
    <a-card title="快速操作" v-if="scheduleDetail.lessonStatus === 2">
      <a-space>
        <a-button 
          type="primary" 
          @click="startLesson"
          v-privilege="'business:schedule:start'"
        >
          <PlayCircleOutlined />
          开始上课
        </a-button>
        <a-button 
          @click="cancelLesson"
          v-privilege="'business:schedule:cancel'"
        >
          <CloseCircleOutlined />
          取消课程
        </a-button>
        <a-button 
          @click="rescheduleLesson"
          v-privilege="'business:schedule:reschedule'"
        >
          <SwapOutlined />
          改期
        </a-button>
      </a-space>
    </a-card>

    <a-card title="快速操作" v-else-if="scheduleDetail.lessonStatus === 3">
      <a-space>
        <a-button 
          type="primary" 
          @click="finishLesson"
          v-privilege="'business:schedule:finish'"
        >
          <CheckCircleOutlined />
          结束课程
        </a-button>
        <a-button 
          @click="addLessonRecord"
          v-privilege="'business:schedule:record'"
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
  DeleteOutlined,
  BookOutlined,
  TargetOutlined,
  ExclamationCircleOutlined,
  FileTextOutlined,
  PlayCircleOutlined,
  CloseCircleOutlined,
  SwapOutlined,
  CheckCircleOutlined,
  FileAddOutlined
} from '@ant-design/icons-vue';
import { scheduleApi } from '/@/api/business/schedule/schedule-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();

// 响应式数据
const loading = ref(false);
const scheduleDetail = ref({});

// 生命周期
onMounted(() => {
  loadScheduleDetail();
});

// 方法
const loadScheduleDetail = async () => {
  try {
    loading.value = true;
    const scheduleId = route.params.id;
    const res = await scheduleApi.getScheduleDetail(scheduleId);
    if (res.ok) {
      scheduleDetail.value = res.data;
    } else {
      message.error(res.msg || '获取课程详情失败');
    }
  } catch (error) {
    message.error('获取课程详情失败');
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.back();
};

const editSchedule = () => {
  router.push(`/schedule/edit/${route.params.id}`);
};

const deleteSchedule = () => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除课程"${scheduleDetail.value.lessonNo}"吗？删除后不可恢复。`,
    okText: '确认删除',
    cancelText: '取消',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await scheduleApi.deleteSchedule(scheduleDetail.value.scheduleId);
        if (res.ok) {
          message.success('删除成功');
          router.push('/schedule/manage');
        } else {
          message.error(res.msg || '删除失败');
        }
      } catch (error) {
        message.error('删除失败');
        smartSentry.captureError(error);
      }
    }
  });
};

const startLesson = async () => {
  try {
    const res = await scheduleApi.startLesson(scheduleDetail.value.scheduleId);
    if (res.ok) {
      message.success('课程已开始');
      loadScheduleDetail();
    } else {
      message.error(res.msg || '操作失败');
    }
  } catch (error) {
    message.error('操作失败');
    smartSentry.captureError(error);
  }
};

const finishLesson = async () => {
  try {
    const res = await scheduleApi.finishLesson(scheduleDetail.value.scheduleId);
    if (res.ok) {
      message.success('课程已结束');
      loadScheduleDetail();
    } else {
      message.error(res.msg || '操作失败');
    }
  } catch (error) {
    message.error('操作失败');
    smartSentry.captureError(error);
  }
};

const cancelLesson = () => {
  Modal.confirm({
    title: '确认取消',
    content: '确定要取消这节课程吗？',
    okText: '确认取消',
    cancelText: '取消',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await scheduleApi.cancelLesson(scheduleDetail.value.scheduleId);
        if (res.ok) {
          message.success('课程已取消');
          loadScheduleDetail();
        } else {
          message.error(res.msg || '操作失败');
        }
      } catch (error) {
        message.error('操作失败');
        smartSentry.captureError(error);
      }
    }
  });
};

const rescheduleLesson = () => {
  // 跳转到改期页面或弹出改期模态框
  message.info('改期功能待实现');
};

const addLessonRecord = () => {
  // 跳转到添加记录页面或弹出记录模态框
  message.info('添加记录功能待实现');
};

const getLessonTypeColor = (type) => {
  const colors = {
    1: 'blue',    // 私教课
    2: 'green',   // 小组课
    3: 'orange'   // 体验课
  };
  return colors[type] || 'default';
};

const getStatusColor = (status) => {
  const colors = {
    1: 'default',  // 待确认
    2: 'processing', // 已确认
    3: 'warning',    // 进行中
    4: 'success',    // 已完成
    5: 'error'       // 已取消
  };
  return colors[status] || 'default';
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
    2: '会员卡扣费',
    3: '课时包扣费'
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
.schedule-detail-container {
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