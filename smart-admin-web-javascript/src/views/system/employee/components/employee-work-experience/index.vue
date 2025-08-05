<!--
  * 员工工作经历管理组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-01-01
  * @Copyright 1024创新实验室
-->
<template>
  <div class="work-experience-management">
    <div class="header">
      <span class="title">工作经历</span>
      <a-button type="primary" @click="showAddModal" size="small">
        <template #icon><PlusOutlined /></template>
        添加经历
      </a-button>
    </div>

    <!-- 工作经历列表 -->
    <div class="work-experience-list">
      <a-empty v-if="workExperienceList.length === 0" description="暂无工作经历" />
      
      <div v-else class="work-experience-timeline">
        <div 
          v-for="(item, index) in workExperienceList" 
          :key="item.workExperienceId"
          class="timeline-item"
        >
          <div class="timeline-dot" :class="{ current: !item.endDate }"></div>
          <div class="timeline-content">
            <div class="work-header">
              <div class="work-main">
                <div class="work-position">{{ item.position }}</div>
                <div class="work-company">{{ item.companyName }}</div>
                <div class="work-department" v-if="item.department">{{ item.department }}</div>
              </div>
              
              <div class="work-actions">
                <a-button type="link" size="small" @click="showEditModal(item)">编辑</a-button>
                <a-divider type="vertical" />
                <a-popconfirm 
                  title="确定要删除这条工作经历吗？" 
                  @confirm="deleteWorkExperience(item.workExperienceId)"
                >
                  <a-button type="link" size="small" danger>删除</a-button>
                </a-popconfirm>
              </div>
            </div>
            
            <div class="work-info">
              <div class="work-time">
                <ClockCircleOutlined />
                {{ formatDate(item.startDate) }} - 
                {{ item.endDate ? formatDate(item.endDate) : '至今' }}
                <a-tag v-if="!item.endDate" color="green" size="small">当前工作</a-tag>
              </div>
              
              <div class="work-salary" v-if="item.salaryRange">
                <DollarCircleOutlined />
                {{ item.salaryRange }}
              </div>
            </div>
            
            <div class="work-description" v-if="item.jobDescription">
              <div class="description-title">工作描述：</div>
              <div class="description-content">{{ item.jobDescription }}</div>
            </div>
            
            <div class="work-remark" v-if="item.remark">
              <div class="remark-content">{{ item.remark }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 工作经历表单弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      width="700px"
      @ok="handleSubmit"
      @cancel="handleCancel"
    >
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="vertical"
      >
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="公司名称" name="companyName">
              <a-input v-model:value="form.companyName" placeholder="请输入公司名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="职位" name="position">
              <a-input v-model:value="form.position" placeholder="请输入职位" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="部门" name="department">
              <a-input v-model:value="form.department" placeholder="请输入部门" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="薪资范围" name="salaryRange">
              <a-input v-model:value="form.salaryRange" placeholder="如：8K-12K" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="入职时间" name="startDate">
              <a-date-picker 
                v-model:value="form.startDate" 
                placeholder="请选择入职时间"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="离职时间" name="endDate">
              <a-date-picker 
                v-model:value="form.endDate" 
                placeholder="请选择离职时间（为空表示当前工作）"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="工作描述" name="jobDescription">
          <a-textarea 
            v-model:value="form.jobDescription" 
            placeholder="请描述主要工作内容、职责和成就"
            :rows="4"
          />
        </a-form-item>

        <a-form-item label="备注" name="remark">
          <a-textarea 
            v-model:value="form.remark" 
            placeholder="请输入备注信息"
            :rows="2"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined, ClockCircleOutlined, DollarCircleOutlined } from '@ant-design/icons-vue';
import { employeeWorkExperienceApi } from '/@/api/system/employee-work-experience-api';
import dayjs from 'dayjs';

// Props
const props = defineProps({
  employeeId: {
    type: Number,
    required: true
  }
});

// Emits
const emit = defineEmits(['change']);

// 响应式数据
const workExperienceList = ref([]);
const modalVisible = ref(false);
const formRef = ref();
const loading = ref(false);

// 表单数据
const form = reactive({
  workExperienceId: null,
  employeeId: null,
  companyName: '',
  position: '',
  department: '',
  jobDescription: '',
  startDate: null,
  endDate: null,
  salaryRange: '',
  remark: ''
});

// 表单验证规则
const rules = {
  companyName: [{ required: true, message: '请输入公司名称' }],
  position: [{ required: true, message: '请输入职位' }],
  startDate: [{ required: true, message: '请选择入职时间' }]
};

// 计算属性
const modalTitle = computed(() => {
  return form.workExperienceId ? '编辑工作经历' : '新增工作经历';
});

// 格式化日期
const formatDate = (date) => {
  return date ? dayjs(date).format('YYYY-MM') : '';
};

// 加载工作经历列表
const loadWorkExperienceList = async () => {
  try {
    loading.value = true;
    const res = await employeeWorkExperienceApi.getWorkExperienceList(props.employeeId);
    if (res.data) {
      workExperienceList.value = res.data;
      emit('change', res.data);
    }
  } catch (error) {
    message.error('加载工作经历失败');
  } finally {
    loading.value = false;
  }
};

// 显示新增弹窗
const showAddModal = () => {
  resetForm();
  form.employeeId = props.employeeId;
  modalVisible.value = true;
};

// 显示编辑弹窗
const showEditModal = (record) => {
  resetForm();
  Object.assign(form, {
    ...record,
    startDate: record.startDate ? dayjs(record.startDate) : null,
    endDate: record.endDate ? dayjs(record.endDate) : null
  });
  modalVisible.value = true;
};

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    workExperienceId: null,
    employeeId: null,
    companyName: '',
    position: '',
    department: '',
    jobDescription: '',
    startDate: null,
    endDate: null,
    salaryRange: '',
    remark: ''
  });
  formRef.value?.resetFields();
};

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validateFields();
    
    const formData = {
      ...form,
      startDate: form.startDate ? form.startDate.format('YYYY-MM-DD') : null,
      endDate: form.endDate ? form.endDate.format('YYYY-MM-DD') : null
    };

    if (form.workExperienceId) {
      await employeeWorkExperienceApi.updateWorkExperience(formData);
      message.success('更新工作经历成功');
    } else {
      await employeeWorkExperienceApi.addWorkExperience(formData);
      message.success('新增工作经历成功');
    }

    modalVisible.value = false;
    loadWorkExperienceList();
  } catch (error) {
    if (error.errorFields) {
      return;
    }
    message.error(error.message || '操作失败');
  }
};

// 取消表单
const handleCancel = () => {
  modalVisible.value = false;
  resetForm();
};

// 删除工作经历
const deleteWorkExperience = async (workExperienceId) => {
  try {
    await employeeWorkExperienceApi.deleteWorkExperience(workExperienceId);
    message.success('删除工作经历成功');
    loadWorkExperienceList();
  } catch (error) {
    message.error('删除工作经历失败');
  }
};

// 初始化
const init = () => {
  if (props.employeeId) {
    loadWorkExperienceList();
  }
};

// 暴露方法给父组件
defineExpose({
  refresh: loadWorkExperienceList
});

// 组件挂载后初始化
init();
</script>

<style scoped>
.work-experience-management {
  padding: 16px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.title {
  font-size: 16px;
  font-weight: 500;
}

.work-experience-list {
  min-height: 200px;
}

.work-experience-timeline {
  position: relative;
}

.work-experience-timeline::before {
  content: '';
  position: absolute;
  left: 8px;
  top: 8px;
  bottom: 8px;
  width: 2px;
  background: #f0f0f0;
}

.timeline-item {
  position: relative;
  display: flex;
  margin-bottom: 24px;
}

.timeline-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #fff;
  border: 2px solid #d9d9d9;
  margin-right: 16px;
  flex-shrink: 0;
  z-index: 1;
}

.timeline-dot.current {
  border-color: #52c41a;
  background: #52c41a;
}

.timeline-content {
  flex: 1;
  background: #fafafa;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  padding: 16px;
}

.work-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.work-main {
  flex: 1;
}

.work-position {
  font-size: 16px;
  font-weight: 500;
  color: #1890ff;
  margin-bottom: 4px;
}

.work-company {
  font-size: 14px;
  color: #333;
  margin-bottom: 2px;
}

.work-department {
  font-size: 12px;
  color: #666;
}

.work-info {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.work-time,
.work-salary {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #666;
}

.work-description {
  margin-top: 12px;
}

.description-title {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.description-content {
  font-size: 13px;
  color: #333;
  line-height: 1.5;
  background: #fff;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
}

.work-remark {
  margin-top: 8px;
}

.remark-content {
  font-size: 12px;
  color: #999;
  font-style: italic;
}

.work-actions {
  flex-shrink: 0;
}
</style>