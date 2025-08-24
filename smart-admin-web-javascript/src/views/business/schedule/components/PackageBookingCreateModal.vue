<!--
 * 课时包预约创建模态框
 *
 * @Author: Claude Code
 * @Date: 2025-08-23
 * @Copyright: 马术俱乐部管理系统
-->
<template>
  <a-modal
    v-model:visible="modalVisible"
    title="创建课时包预约"
    :width="720"
    :confirm-loading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <div class="package-booking-form">
      <!-- 课时包信息展示 -->
      <a-card size="small" class="package-info" v-if="packageData">
        <template #title>
          <span>课时包信息</span>
        </template>
        <a-row :gutter="16">
          <a-col :span="8">
            <div class="info-item">
              <label>订单号：</label>
              <span>{{ packageData.orderNo }}</span>
            </div>
          </a-col>
          <a-col :span="8">
            <div class="info-item">
              <label>商品名称：</label>
              <span>{{ packageData.productName }}</span>
            </div>
          </a-col>
          <a-col :span="8">
            <div class="info-item">
              <label>购买会员：</label>
              <span>{{ packageData.buyerMemberName }}</span>
            </div>
          </a-col>
          <a-col :span="8">
            <div class="info-item">
              <label>剩余课时：</label>
              <span class="remaining-count">{{ packageData.remainingCount }}</span>
            </div>
          </a-col>
          <a-col :span="8">
            <div class="info-item">
              <label>过期时间：</label>
              <span>{{ formatDateTime(packageData.expireDate) }}</span>
            </div>
          </a-col>
        </a-row>
      </a-card>

      <!-- 预约表单 -->
      <a-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
        class="booking-form"
      >
        <a-form-item label="消费会员" name="consumerMemberId" required>
          <a-select
            v-model:value="formData.consumerMemberId"
            placeholder="选择消费会员"
            showSearch
            :loading="loadingStates.memberList"
            :filterOption="filterMemberOption"
            @change="onConsumerChange"
          >
            <a-select-option 
              v-for="member in memberList" 
              :key="member.memberId" 
              :value="member.memberId"
            >
              {{ member.actualName }} ({{ member.phone }})
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="预约时间" required>
          <a-row :gutter="8">
            <a-col :span="12">
              <a-form-item name="startTime" :label-col="{ span: 0 }" :wrapper-col="{ span: 24 }">
                <a-date-picker
                  v-model:value="formData.startTime"
                  placeholder="开始时间"
                  :show-time="{ format: 'HH:mm' }"
                  format="YYYY-MM-DD HH:mm"
                  :disabled-date="disabledDate"
                  style="width: 100%"
                  @change="onStartTimeChange"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item name="endTime" :label-col="{ span: 0 }" :wrapper-col="{ span: 24 }">
                <a-date-picker
                  v-model:value="formData.endTime"
                  placeholder="结束时间"
                  :show-time="{ format: 'HH:mm' }"
                  format="YYYY-MM-DD HH:mm"
                  :disabled-date="disabledDate"
                  style="width: 100%"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item label="教练" name="coachId" required>
          <a-select
            v-model:value="formData.coachId"
            placeholder="选择教练"
            showSearch
            :loading="loadingStates.coachList"
            :filterOption="filterCoachOption"
            @change="onCoachChange"
          >
            <a-select-option 
              v-for="coach in coachList" 
              :key="coach.coachId" 
              :value="coach.coachId"
            >
              {{ coach.coachName }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="马匹" name="horseId">
          <a-select
            v-model:value="formData.horseId"
            placeholder="选择马匹（可选）"
            allowClear
            showSearch
            :loading="loadingStates.horseList"
            :filterOption="filterHorseOption"
          >
            <a-select-option 
              v-for="horse in horseList" 
              :key="horse.horseId" 
              :value="horse.horseId"
            >
              {{ horse.horseName }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="消费课时数" name="packageConsumeCount" required>
          <a-input-number
            v-model:value="formData.packageConsumeCount"
            :min="1"
            :max="packageData?.remainingCount || 1"
            placeholder="请输入消费课时数"
            style="width: 100%"
          />
        </a-form-item>

        <a-form-item label="教练费" name="actualCoachFee">
          <a-input-number
            v-model:value="formData.actualCoachFee"
            :precision="2"
            :min="0"
            placeholder="实际教练费"
            style="width: 100%"
          >
            <template #addonAfter>元</template>
          </a-input-number>
        </a-form-item>

        <a-form-item label="马匹费" name="actualHorseFee">
          <a-input-number
            v-model:value="formData.actualHorseFee"
            :precision="2"
            :min="0"
            placeholder="实际马匹费"
            style="width: 100%"
          >
            <template #addonAfter>元</template>
          </a-input-number>
        </a-form-item>

        <a-form-item label="备注" name="remark">
          <a-textarea
            v-model:value="formData.remark"
            placeholder="请输入备注信息"
            :rows="3"
          />
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup>
import { reactive, ref, computed, watch, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { memberApi } from '/@/api/business/member-api';
import { scheduleApi, bookingApi } from '/@/api/business/schedule/schedule-api';
import { coachApi } from '/@/api/business/coach/coach-api';
import { horseApi } from '/@/api/business/horse/horse-api';
import dayjs from 'dayjs';

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  packageData: {
    type: Object,
    default: null
  }
});

// Emits
const emit = defineEmits(['update:visible', 'success']);

// 响应式数据
const modalVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
});

const formRef = ref();
const confirmLoading = ref(false);

const formData = reactive({
  consumerMemberId: null,
  coachId: null,
  horseId: null,
  startTime: null,
  endTime: null,
  packageConsumeCount: 1,
  actualCoachFee: null,
  actualHorseFee: null,
  remark: ''
});

const memberList = ref([]);
const coachList = ref([]);
const horseList = ref([]);

const loadingStates = reactive({
  memberList: false,
  coachList: false,
  horseList: false
});

// 表单验证规则
const rules = {
  consumerMemberId: [
    { required: true, message: '请选择消费会员', trigger: 'change' }
  ],
  coachId: [
    { required: true, message: '请选择教练', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  packageConsumeCount: [
    { required: true, message: '请输入消费课时数', trigger: 'change' }
  ]
};

// 方法
const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    
    if (!props.packageData) {
      message.error('课时包信息缺失');
      return;
    }

    confirmLoading.value = true;

    const requestData = {
      orderId: props.packageData.orderId,
      consumerMemberId: formData.consumerMemberId,
      coachId: formData.coachId,
      horseId: formData.horseId || null,
      startTime: formData.startTime?.format('YYYY-MM-DD HH:mm:ss'),
      endTime: formData.endTime?.format('YYYY-MM-DD HH:mm:ss'),
      packageConsumeCount: formData.packageConsumeCount,
      actualCoachFee: formData.actualCoachFee || 0,
      actualHorseFee: formData.actualHorseFee || 0,
      remark: formData.remark
    };

    const response = await bookingApi.createPackageBooking(requestData);
    
    if (response.code === 0) {
      message.success('预约创建成功');
      handleCancel();
      emit('success');
    } else {
      message.error(response.mesg || '预约创建失败');
    }
  } catch (error) {
    console.error('创建预约失败:', error);
    if (error.errorFields) {
      message.error('请检查表单填写');
    }
  } finally {
    confirmLoading.value = false;
  }
};

const handleCancel = () => {
  formRef.value?.resetFields();
  Object.assign(formData, {
    consumerMemberId: null,
    coachId: null,
    horseId: null,
    startTime: null,
    endTime: null,
    packageConsumeCount: 1,
    actualCoachFee: null,
    actualHorseFee: null,
    remark: ''
  });
  modalVisible.value = false;
};

// 数据加载
const loadMemberList = async () => {
  try {
    loadingStates.memberList = true;
    const response = await memberApi.queryMemberList({
      pageNum: 1,
      pageSize: 100,
      keywords: ''
    });
    if (response.code === 0 && response.data) {
      memberList.value = response.data.list || [];
    }
  } catch (error) {
    console.error('加载会员列表失败:', error);
  } finally {
    loadingStates.memberList = false;
  }
};

const loadCoachList = async () => {
  try {
    loadingStates.coachList = true;
    const response = await scheduleApi.getCoachList();
    if (response.code === 0 && response.data) {
      coachList.value = response.data;
    }
  } catch (error) {
    console.error('加载教练列表失败:', error);
  } finally {
    loadingStates.coachList = false;
  }
};

const loadHorseList = async () => {
  try {
    loadingStates.horseList = true;
    const response = await horseApi.queryHorseList({
      pageNum: 1,
      pageSize: 100
    });
    if (response.code === 0 && response.data) {
      horseList.value = response.data.list || [];
    }
  } catch (error) {
    console.error('加载马匹列表失败:', error);
  } finally {
    loadingStates.horseList = false;
  }
};

// 事件处理
const onConsumerChange = (value) => {
  // 可以在此处添加消费会员变更的逻辑
};

const onCoachChange = (value) => {
  // 可以在此处添加教练变更的逻辑
};

const onStartTimeChange = (value) => {
  if (value && !formData.endTime) {
    // 自动设置结束时间为开始时间后1小时
    formData.endTime = value.add(1, 'hour');
  }
};

// 工具函数
const formatDateTime = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('YYYY-MM-DD HH:mm') : '-';
};

const disabledDate = (current) => {
  // 禁用过去的日期
  return current && current < dayjs().startOf('day');
};

const filterMemberOption = (input, option) => {
  return option.children()[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};

const filterCoachOption = (input, option) => {
  return option.children()[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};

const filterHorseOption = (input, option) => {
  return option.children()[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};

// 监听器
watch(() => props.visible, (newVal) => {
  if (newVal) {
    // 模态框打开时加载数据
    loadMemberList();
    loadCoachList();
    loadHorseList();
    
    // 默认选择购买会员作为消费会员
    if (props.packageData?.buyerMemberId) {
      formData.consumerMemberId = props.packageData.buyerMemberId;
    }
  }
});

onMounted(() => {
  // 组件挂载时预加载数据
  loadCoachList();
});
</script>

<style lang="less" scoped>
.package-booking-form {
  .package-info {
    margin-bottom: 20px;
    
    .info-item {
      margin-bottom: 8px;
      
      label {
        color: #666;
        font-size: 12px;
      }
      
      span {
        color: #333;
        font-size: 13px;
      }
      
      .remaining-count {
        color: #1890ff;
        font-weight: 500;
      }
    }
  }
  
  .booking-form {
    :deep(.ant-form-item) {
      margin-bottom: 16px;
    }
  }
}
</style>