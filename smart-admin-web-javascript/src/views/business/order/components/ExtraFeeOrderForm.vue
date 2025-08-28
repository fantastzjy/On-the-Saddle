<template>
  <div class="extra-fee-form">
    
    <!-- 补差类型选择 -->
    <a-form-item label="补差类型" name="orderSubType">
      <a-select 
        v-model:value="modelValue.orderSubType" 
        @change="onSubTypeChange"
        placeholder="请选择补差类型"
      >
        <a-select-option value="extra_coach_fee">教练费补差</a-select-option>
        <a-select-option value="extra_lesson_fee">课时费补差</a-select-option>
        <a-select-option value="extra_horse_fee">马匹费补差</a-select-option>
      </a-select>
    </a-form-item>
    
    <!-- 关联预约选择 -->
    <a-form-item label="关联预约" name="relatedBookingId">
      <BookingSelector 
        v-model:value="modelValue.relatedBookingId" 
        :member-id="memberId"
        @change="onBookingChange"
        placeholder="请选择要补差的预约"
      />
    </a-form-item>
    
    <!-- 补差金额 -->
    <a-form-item label="补差金额" name="serviceAmount">
      <a-input-number 
        v-model:value="modelValue.serviceAmount" 
        :min="0" 
        :precision="2" 
        style="width: 200px"
        placeholder="0.00"
      >
        <template #addonAfter>元</template>
      </a-input-number>
    </a-form-item>
    
    <!-- 补差原因 -->
    <a-form-item label="补差说明">
      <a-textarea 
        v-model:value="modelValue.remark" 
        :rows="3" 
        placeholder="请说明补差原因"
        :max-length="200"
        show-count
      />
    </a-form-item>
    
  </div>
</template>

<script setup>
import { watch } from 'vue';
import BookingSelector from '/@/components/business/BookingSelector.vue';

const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  },
  memberId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:modelValue']);

// 子类型变更处理
const onSubTypeChange = (subType) => {
  console.log('补差类型变更:', subType);
  // 可以根据不同类型设置默认值或验证规则
};

// 预约变更处理
const onBookingChange = (bookingId, bookingInfo) => {
  console.log('关联预约变更:', bookingId, bookingInfo);
  // 可以根据预约信息自动计算补差金额
};

// 监听会员ID变化，重置关联预约
watch(() => props.memberId, () => {
  if (props.modelValue.relatedBookingId) {
    emit('update:modelValue', {
      ...props.modelValue,
      relatedBookingId: null
    });
  }
});
</script>

<style scoped>
.extra-fee-form {
  background: #fafafa;
  padding: 16px;
  border-radius: 6px;
  border-left: 4px solid #1890ff;
}
</style>