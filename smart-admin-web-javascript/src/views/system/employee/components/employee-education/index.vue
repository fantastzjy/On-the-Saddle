<!--
  * 员工学历管理组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-01-01
  * @Copyright 1024创新实验室
-->
<template>
  <div class="education-management">
    <div class="header">
      <span class="title">学历信息</span>
      <a-button type="primary" @click="showAddModal" size="small">
        <template #icon><PlusOutlined /></template>
        添加学历
      </a-button>
    </div>

    <!-- 学历列表 -->
    <div class="education-list">
      <a-empty v-if="educationList.length === 0" description="暂无学历信息" />
      
      <div v-else class="education-items">
        <div 
          v-for="(item, index) in educationList" 
          :key="item.educationId"
          class="education-item"
        >
          <div class="education-content">
            <div class="education-main">
              <div class="education-level">{{ item.educationLevel }}</div>
              <div class="education-school">{{ item.schoolName }}</div>
              <div class="education-major" v-if="item.major">{{ item.major }}</div>
            </div>
            
            <div class="education-info">
              <div class="education-time">
                {{ formatDate(item.startDate) }} - 
                {{ item.endDate ? formatDate(item.endDate) : '至今' }}
              </div>
              <div class="education-status">
                <a-tag :color="item.isGraduated ? 'green' : 'orange'">
                  {{ item.isGraduated ? '已毕业' : '在读' }}
                </a-tag>
              </div>
            </div>
          </div>
          
          <div class="education-actions">
            <a-button type="link" size="small" @click="showEditModal(item)">编辑</a-button>
            <a-divider type="vertical" />
            <a-popconfirm 
              title="确定要删除这条学历信息吗？" 
              @confirm="deleteEducation(item.educationId)"
            >
              <a-button type="link" size="small" danger>删除</a-button>
            </a-popconfirm>
          </div>
        </div>
      </div>
    </div>

    <!-- 学历表单弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      width="600px"
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
            <a-form-item label="学历层次" name="educationLevel">
              <a-select v-model:value="form.educationLevel" placeholder="请选择学历层次">
                <a-select-option value="小学">小学</a-select-option>
                <a-select-option value="初中">初中</a-select-option>
                <a-select-option value="高中">高中</a-select-option>
                <a-select-option value="中专">中专</a-select-option>
                <a-select-option value="大专">大专</a-select-option>
                <a-select-option value="本科">本科</a-select-option>
                <a-select-option value="硕士">硕士</a-select-option>
                <a-select-option value="博士">博士</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="是否毕业" name="isGraduated">
              <a-radio-group v-model:value="form.isGraduated">
                <a-radio :value="true">已毕业</a-radio>
                <a-radio :value="false">在读</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="学校名称" name="schoolName">
          <a-input v-model:value="form.schoolName" placeholder="请输入学校名称" />
        </a-form-item>

        <a-form-item label="专业" name="major">
          <a-input v-model:value="form.major" placeholder="请输入专业名称" />
        </a-form-item>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="入学时间" name="startDate">
              <a-date-picker 
                v-model:value="form.startDate" 
                placeholder="请选择入学时间"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="毕业时间" name="endDate">
              <a-date-picker 
                v-model:value="form.endDate" 
                placeholder="请选择毕业时间"
                style="width: 100%"
                :disabled="!form.isGraduated"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="备注" name="remark">
          <a-textarea 
            v-model:value="form.remark" 
            placeholder="请输入备注信息"
            :rows="3"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { employeeEducationApi } from '/@/api/system/employee-education-api';
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
const educationList = ref([]);
const modalVisible = ref(false);
const formRef = ref();
const loading = ref(false);

// 表单数据
const form = reactive({
  educationId: null,
  employeeId: null,
  educationLevel: '',
  schoolName: '',
  major: '',
  startDate: null,
  endDate: null,
  isGraduated: true,
  remark: ''
});

// 表单验证规则
const rules = {
  educationLevel: [{ required: true, message: '请选择学历层次' }],
  schoolName: [{ required: true, message: '请输入学校名称' }],
  startDate: [{ required: true, message: '请选择入学时间' }]
};

// 计算属性
const modalTitle = computed(() => {
  return form.educationId ? '编辑学历信息' : '新增学历信息';
});

// 监听是否毕业状态
watch(() => form.isGraduated, (newVal) => {
  if (!newVal) {
    form.endDate = null;
  }
});

// 格式化日期
const formatDate = (date) => {
  return date ? dayjs(date).format('YYYY-MM') : '';
};

// 加载学历列表
const loadEducationList = async () => {
  try {
    loading.value = true;
    const res = await employeeEducationApi.getEducationList(props.employeeId);
    if (res.data) {
      educationList.value = res.data;
      emit('change', res.data);
    }
  } catch (error) {
    message.error('加载学历信息失败');
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
    educationId: null,
    employeeId: null,
    educationLevel: '',
    schoolName: '',
    major: '',
    startDate: null,
    endDate: null,
    isGraduated: true,
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

    if (form.educationId) {
      await employeeEducationApi.updateEducation(formData);
      message.success('更新学历信息成功');
    } else {
      await employeeEducationApi.addEducation(formData);
      message.success('新增学历信息成功');
    }

    modalVisible.value = false;
    loadEducationList();
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

// 删除学历
const deleteEducation = async (educationId) => {
  try {
    await employeeEducationApi.deleteEducation(educationId);
    message.success('删除学历信息成功');
    loadEducationList();
  } catch (error) {
    message.error('删除学历信息失败');
  }
};

// 初始化
const init = () => {
  if (props.employeeId) {
    loadEducationList();
  }
};

// 暴露方法给父组件
defineExpose({
  refresh: loadEducationList
});

// 组件挂载后初始化
init();
</script>

<style scoped>
.education-management {
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

.education-list {
  min-height: 200px;
}

.education-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.education-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  background: #fafafa;
}

.education-content {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.education-main {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.education-level {
  font-size: 16px;
  font-weight: 500;
  color: #1890ff;
}

.education-school {
  font-size: 14px;
  color: #333;
}

.education-major {
  font-size: 12px;
  color: #666;
}

.education-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}

.education-time {
  font-size: 12px;
  color: #666;
}

.education-actions {
  margin-left: 16px;
}
</style>