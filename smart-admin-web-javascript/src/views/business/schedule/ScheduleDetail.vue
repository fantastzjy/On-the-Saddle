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
              {{ scheduleDetail.scheduleNo }}
            </a-descriptions-item>
            <a-descriptions-item label="商品类型">
              <a-tag :color="getLessonTypeColor(scheduleDetail.productType)">
                {{ scheduleDetail.productType }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="课程状态">
              <a-tag :color="getStatusColor(scheduleDetail.lessonStatus)">
                {{ scheduleDetail.lessonStatusName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="教练费用">
              <span style="font-size: 18px; font-weight: bold; color: #ff4d4f;">
                ¥{{ scheduleDetail.actualCoachFee || 0 }}
              </span>
            </a-descriptions-item>
            <a-descriptions-item label="马匹费用">
              <span style="font-size: 16px; color: #666;">
                ¥{{ scheduleDetail.actualHorseFee || 0 }}
              </span>
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
              <a-avatar size="small" :src="scheduleDetail.coachAvatar" style="margin-right: 8px;" />
              {{ scheduleDetail.coachName }}
            </a-descriptions-item>
            <a-descriptions-item label="联系电话">
              {{ scheduleDetail.coachPhone || '未提供' }}
            </a-descriptions-item>
            <a-descriptions-item label="专业特长">
              {{ scheduleDetail.coachSpecialties }}
            </a-descriptions-item>
            <a-descriptions-item label="等级">
              {{ scheduleDetail.coachLevel }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="8">
          <a-descriptions title="会员信息" :column="1" bordered>
            <a-descriptions-item label="会员姓名">
              {{ scheduleDetail.memberName }}
            </a-descriptions-item>
            <a-descriptions-item label="联系电话">
              {{ scheduleDetail.memberPhone }}
            </a-descriptions-item>
            <a-descriptions-item label="会员等级">
              <a-tag :color="getMemberLevelColor(scheduleDetail.memberLevel)">
                {{ scheduleDetail.memberLevel }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="骑乘经验">
              {{ scheduleDetail.ridingExperience || '未录入' }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="8">
          <a-descriptions title="马匹信息" :column="1" bordered>
            <a-descriptions-item label="马匹名称">
              {{ scheduleDetail.horseName }}
            </a-descriptions-item>
            <a-descriptions-item label="品种">
              {{ scheduleDetail.horseBreed }}
            </a-descriptions-item>
            <a-descriptions-item label="年龄">
              {{ scheduleDetail.horseAge }} 岁
            </a-descriptions-item>
            <a-descriptions-item label="健康状态">
              <a-tag :color="getHealthStatusColor(scheduleDetail.horseHealthStatus)">
                {{ scheduleDetail.horseHealthStatus }}
              </a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
      </a-row>

      <!-- 商品信息 -->
      <a-divider />
      <div>
        <h4 style="margin-bottom: 16px;">
          <BookOutlined style="margin-right: 8px;" />
          商品信息
        </h4>
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="商品名称">
            {{ scheduleDetail.productName }}
          </a-descriptions-item>
          <a-descriptions-item label="商品类型">
            {{ scheduleDetail.productType }}
          </a-descriptions-item>
          <a-descriptions-item label="预约状态">
            <a-tag :color="getBookingStatusColor(scheduleDetail.bookingStatus)">
              {{ scheduleDetail.bookingStatusName }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="课程时长">
            {{ scheduleDetail.duration }} 分钟
          </a-descriptions-item>
        </a-descriptions>
      </div>

      <!-- 会员备注 -->
      <a-divider />
      <div v-if="scheduleDetail.memberRemark">
        <h4 style="margin-bottom: 16px;">
          <AimOutlined style="margin-right: 8px;" />
          会员信息备注
        </h4>
        <div style="
          background: #fafafa; 
          padding: 16px; 
          border-radius: 6px;
          min-height: 60px;
          white-space: pre-wrap;
        ">
          {{ scheduleDetail.memberRemark || '暂无备注信息' }}
        </div>
      </div>

      <!-- 教练介绍 -->
      <div v-if="scheduleDetail.coachIntroduction">
        <a-divider />
        <div>
          <h4 style="margin-bottom: 16px;">
            <ExclamationCircleOutlined style="margin-right: 8px;" />
            教练介绍
          </h4>
          <div style="
            background: #f6ffed; 
            padding: 16px; 
            border-radius: 6px;
            border-left: 4px solid #52c41a;
            white-space: pre-wrap;
          ">
            {{ scheduleDetail.coachIntroduction }}
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
  AimOutlined,
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
    content: `确定要删除课程"${scheduleDetail.value.scheduleNo}"吗？删除后不可恢复。`,
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

const getStatusColor = (status) => {
  const colors = {
    1: 'default',  // 待上课
    2: 'processing', // 进行中
    3: 'success',    // 已完成
    4: 'error'       // 已取消
  };
  return colors[status] || 'default';
};

const getBookingStatusColor = (status) => {
  const colors = {
    1: 'default',   // 待确认
    2: 'processing', // 已确认
    3: 'warning',    // 进行中
    4: 'success',    // 已完成
    5: 'error'       // 已取消
  };
  return colors[status] || 'default';
};

const getHealthStatusColor = (status) => {
  if (status === '健康') return 'success';
  if (status === '轻微不适') return 'warning';
  if (status === '需要休息') return 'orange';
  if (status === '康复中') return 'blue';
  if (status === '停止工作') return 'error';
  return 'default';
};

const getMemberLevelColor = (level) => {
  if (!level) return 'default';
  if (level.toLowerCase().includes('beginner')) return 'blue';
  if (level.toLowerCase().includes('intermediate')) return 'orange';
  if (level.toLowerCase().includes('advanced')) return 'red';
  return 'default';
};

const getLessonTypeColor = (type) => {
  if (!type) return 'default';
  if (type.includes('课程')) return 'blue';
  if (type.includes('课时包')) return 'green';
  if (type.includes('活动')) return 'orange';
  if (type.includes('单人课')) return 'purple';
  if (type.includes('多人课')) return 'cyan';
  return 'default';
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