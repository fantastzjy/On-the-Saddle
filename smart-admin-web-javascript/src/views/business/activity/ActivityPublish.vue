<!--
  * 活动发布页面
  *
  * @Author: 1024创新实验室
  * @Date: 2025-08-28
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="activity-publish">
    <a-card>
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
        @finish="onFinish"
      >
        <!-- 活动基本信息 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="活动名称" name="activityName">
              <a-input 
                v-model:value="form.activityName" 
                placeholder="请输入活动名称（最多5个字）"
                maxlength="5" 
              />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="活动价格" name="price">
              <a-input-number
                v-model:value="form.price"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="请输入活动价格"
                addon-before="￥"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 时间和地点 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="活动时间" name="activityTimeRange">
              <a-range-picker
                v-model:value="form.activityTimeRange"
                :show-time="{ 
                  format: 'HH', 
                  hideDisabledOptions: true,
                  showMinute: false,
                  showSecond: false
                }"
                format="YYYY-MM-DD HH时"
                value-format="YYYY-MM-DD HH:00:00"
                :placeholder="['开始时间', '结束时间']"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="活动地点" name="activityLocation">
              <a-input 
                v-model:value="form.activityLocation" 
                placeholder="请输入活动地点"
                maxlength="200" 
              />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 参与设置 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="最大人数" name="maxParticipants">
              <a-input-number
                v-model:value="form.maxParticipants"
                :min="1"
                :max="9999"
                style="width: 100%"
                placeholder="请输入最大参与人数"
                addon-after="人"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 活动详情 -->
        <a-form-item label="活动详情" name="activityDetails" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
          <a-textarea 
            v-model:value="form.activityDetails"
            placeholder="请输入活动详情全文介绍"
            :rows="4"
            maxlength="2000"
          />
        </a-form-item>

        <!-- 活动规则 -->
        <a-form-item label="活动规则" name="activityRule" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
          <a-textarea
            v-model:value="form.activityRule"
            placeholder="可明确活动特殊规则，例如是否在规定时间范围内退款等"
            :auto-size="{ minRows: 6 }"
            maxlength="1000"
          />
        </a-form-item>

        <!-- 活动图片 -->
        <a-form-item label="活动图片" name="detailImages" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
          <ActivityImageUpload 
            v-model:value="form.detailImages"
            :max-count="9"
          />
          <div style="margin-top: 4px; color: #666; font-size: 12px;">
            最多上传9张图片，支持拖拽排序，单张图片不超过2MB
          </div>
        </a-form-item>

        <!-- 操作按钮 -->
        <a-form-item :wrapper-col="{ span: 24 }" style="text-align: center; margin-top: 32px;">
          <a-space size="large">
            <a-button type="primary" html-type="submit" :loading="submitting">
              {{ isEdit ? '确定' : '发布活动' }}
            </a-button>
            <a-button @click="goBack">
              取消
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { activityApi } from '/@/api/business/activity/activity-api';
import ActivityImageUpload from './components/ActivityImageUpload.vue';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();

// ======================== 响应式数据 ========================
const formRef = ref();
const submitting = ref(false);

// 判断是否为编辑模式
const isEdit = computed(() => !!route.params.activityId);

const form = reactive({
  activityName: '',
  activityDetails: '',
  activityTimeRange: [],
  activityLocation: '',
  price: 0,
  maxParticipants: 1,
  activityRule: '',
  detailImages: []
});

// 表单验证规则
const rules = {
  activityName: [
    { required: true, message: '请输入活动名称', trigger: 'blur' },
    { max: 5, message: '活动名称最多5个字符', trigger: 'blur' }
  ],
  activityDetails: [
    { required: true, message: '请输入活动详情', trigger: 'blur' },
    { max: 2000, message: '活动详情不能超过2000个字符', trigger: 'blur' }
  ],
  activityTimeRange: [
    { required: true, message: '请选择活动时间', trigger: 'change' }
  ],
  activityLocation: [
    { required: true, message: '请输入活动地点', trigger: 'blur' },
    { max: 200, message: '活动地点不能超过200个字符', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入活动价格', trigger: 'blur' }
  ],
  maxParticipants: [
    { required: true, message: '请输入最大参与人数', trigger: 'blur' }
  ],
  activityRule: [
    { required: true, message: '请输入活动规则', trigger: 'blur' },
    { max: 1000, message: '活动规则不能超过1000个字符', trigger: 'blur' }
  ]
};

// ======================== 初始化 ========================
onMounted(() => {
  if (isEdit.value) {
    loadActivityData();
  }
});

// ======================== 数据处理 ========================
async function loadActivityData() {
  try {
    const response = await activityApi.queryActivityDetail(route.params.activityId);
    if (response.ok && response.data) {
      const data = response.data;
      
      // 填充表单数据（注意：不再填充商品名称和编码）
      Object.assign(form, {
        activityName: data.activityName || '',
        activityDetails: data.activityDetails || '',
        activityLocation: data.activityLocation || '',
        price: data.price || 0,
        maxParticipants: data.maxParticipants || 1,
        activityRule: data.activityRule || '',
        detailImages: data.detailImages ? JSON.parse(data.detailImages) : []
      });

      // 处理活动时间范围
      if (data.startTime && data.endTime) {
        form.activityTimeRange = [
          dayjs(data.startTime),
          dayjs(data.endTime)
        ];
      }
      
    } else {
      message.error('加载活动信息失败');
      goBack();
    }
  } catch (error) {
    message.error('加载活动信息失败');
    console.error('加载活动信息失败:', error);
    goBack();
  }
}

// ======================== 表单提交 ========================
async function onFinish() {
  await submitForm();
}

async function submitForm() {
  try {
    submitting.value = true;
    
    // 表单验证
    await formRef.value.validate();
    
    // 准备提交数据
    const submitData = {
      ...form,
      // 处理活动时间
      startTime: form.activityTimeRange?.[0],
      endTime: form.activityTimeRange?.[1],
      activityTimeRange: undefined,
      // 处理详情图片JSON
      detailImages: JSON.stringify(form.detailImages || [])
    };

    let response;
    if (isEdit.value) {
      // 编辑模式
      submitData.productId = route.params.activityId;
      response = await activityApi.updateActivity(submitData);
    } else {
      // 新增模式
      response = await activityApi.addActivity(submitData);
    }

    if (response.ok) {
      const action = isEdit.value ? '更新' : '发布';
      message.success(`活动${action}成功`);
      goBack();
    } else {
      message.error(response.msg || '操作失败');
    }
  } catch (error) {
    if (error.errorFields) {
      message.error('请检查表单填写');
    } else {
      message.error('操作失败');
      console.error('提交活动失败:', error);
    }
  } finally {
    submitting.value = false;
  }
}

// ======================== 页面操作 ========================
function goBack() {
  router.push('/activity/list');
}
</script>

<style scoped>
.activity-publish {
  padding: 16px;
  display: flex;
  justify-content: center;
  min-height: calc(100vh - 64px);
}

.activity-publish .ant-card {
  width: 100%;
  max-width: 1200px;
}

.ant-form {
  max-width: 1000px;
  margin: 0 auto;
}

:deep(.ant-form-item-label) {
  font-weight: 500;
}

:deep(.ant-card-head-title) {
  color: #1890ff;
  font-weight: 600;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .activity-publish {
    padding: 8px;
  }
  
  .ant-form {
    max-width: 100%;
  }
}
</style>