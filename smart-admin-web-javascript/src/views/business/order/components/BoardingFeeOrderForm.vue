<template>
  <div class="boarding-fee-form">
    
    <!-- 饲养费子类型（固定为饲养费） -->
    <a-form-item style="display: none;">
      <a-input v-model:value="modelValue.orderSubType" />
    </a-form-item>
    
    <!-- 马匹选择 -->
    <a-form-item label="选择马匹" name="targetHorseId">
      <HorseSelector 
        v-model:value="modelValue.targetHorseId" 
        :horse-type="2"
        @change="onHorseChange"
        placeholder="请选择马匹"
      />
    </a-form-item>
    
    <!-- 饲养费金额 -->
    <a-form-item label="饲养费" name="serviceAmount">
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
    
    <!-- 费用周期 -->
    <a-form-item label="费用周期">
      <a-range-picker 
        v-model:value="feePeriod"
        picker="month"
        format="YYYY-MM"
        placeholder="['开始月份', '结束月份']"
        style="width: 300px"
        @change="onPeriodChange"
      />
    </a-form-item>
    
    <!-- 饲养费说明 -->
    <a-form-item label="费用说明">
      <a-textarea 
        v-model:value="modelValue.remark" 
        :rows="3" 
        placeholder="请输入饲养费相关说明"
        :max-length="200"
        show-count
      />
    </a-form-item>
    
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import HorseSelector from '/@/components/business/HorseSelector.vue';

const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['update:modelValue']);

const feePeriod = ref(null);

// 组件初始化时设置子类型
onMounted(() => {
  emit('update:modelValue', {
    ...props.modelValue,
    orderSubType: 'boarding_fee'
  });
});

// 马匹变更处理
const onHorseChange = (horseId, horseInfo) => {
  console.log('马匹变更:', horseId, horseInfo);
  
  // 如果马匹信息包含饲养费，自动填入
  if (horseInfo && horseInfo.boardingFee) {
    emit('update:modelValue', {
      ...props.modelValue,
      serviceAmount: horseInfo.boardingFee
    });
  }
};

// 费用周期变更处理
const onPeriodChange = (dates) => {
  if (dates && dates.length === 2) {
    const startMonth = dates[0].format('YYYY-MM');
    const endMonth = dates[1].format('YYYY-MM');
    
    // 更新备注信息
    const periodText = `费用周期：${startMonth} 至 ${endMonth}`;
    const currentRemark = props.modelValue.remark || '';
    const newRemark = currentRemark.includes('费用周期：') 
      ? currentRemark.replace(/费用周期：[^\n]*/g, periodText)
      : currentRemark ? `${currentRemark}\n${periodText}` : periodText;
    
    emit('update:modelValue', {
      ...props.modelValue,
      remark: newRemark
    });
  }
};
</script>

<style scoped>
.boarding-fee-form {
  background: #f0f9ff;
  padding: 16px;
  border-radius: 6px;
  border-left: 4px solid #52c41a;
}
</style>