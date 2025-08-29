<template>
  <div class="product-order-form">
    
    <!-- 商品选择 -->
    <a-form-item label="选择商品" name="selectedProduct">
      <ProductSelector 
        v-model:value="selectedProduct" 
        :product-type="orderType"
        :member-id="memberId"
        @change="onProductChange"
        placeholder="请选择商品"
      />
    </a-form-item>
    
    <!-- 课程子类型（仅课程订单显示） -->
    <a-form-item 
      label="课程类型" 
      name="orderSubType" 
      v-if="orderType === 1 && selectedProduct"
    >
      <a-select 
        v-model:value="modelValue.orderSubType" 
        placeholder="请选择课程类型"
      >
        <a-select-option value="single_class">单人课</a-select-option>
        <a-select-option value="multi_class">小组课</a-select-option>
      </a-select>
    </a-form-item>
    
    <!-- 教练选择 -->
    <a-form-item label="选择教练" name="coachId" v-if="selectedProduct">
      <CoachSelector 
        v-model:value="currentOrderItem.coachId" 
        @change="onCoachChange"
        placeholder="请选择教练"
      />
    </a-form-item>
    
    <!-- 数量选择 -->
    <a-form-item label="数量" name="quantity" v-if="selectedProduct">
      <a-input-number 
        v-model:value="currentOrderItem.quantity" 
        :min="1" 
        @change="calculatePrice"
      />
    </a-form-item>
    
    <!-- 期望上课时间 -->
    <a-form-item label="期望时间" name="preferredTime" v-if="selectedProduct && orderType === 1">
      <a-date-picker 
        v-model:value="currentOrderItem.preferredTime"
        show-time
        format="YYYY-MM-DD HH:mm"
        placeholder="请选择期望上课时间"
        style="width: 100%"
      />
    </a-form-item>
    
    <!-- 价格显示 -->
    <a-form-item label="单价" v-if="selectedProduct">
      <a-input 
        :value="`¥${currentOrderItem.unitPrice || 0}`"
        readonly
        style="width: 150px"
      />
    </a-form-item>
    
    <!-- 总价显示 -->
    <a-form-item label="小计" v-if="selectedProduct">
      <a-input 
        :value="`¥${(currentOrderItem.unitPrice || 0) * (currentOrderItem.quantity || 1)}`"
        readonly
        style="width: 150px"
        class="total-price-input"
      />
    </a-form-item>
    
  </div>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue';
import ProductSelector from '/@/components/business/ProductSelector.vue';
import CoachSelector from '/@/components/business/CoachSelector.vue';

const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  },
  orderType: {
    type: Number,
    required: true
  },
  memberId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:modelValue']);

const selectedProduct = ref(null);

// 当前订单明细项
const currentOrderItem = reactive({
  productId: null,
  productName: '',
  productType: props.orderType,
  quantity: 1,
  unitPrice: 0,
  coachId: null,
  preferredTime: null,
  itemConfig: null
});

// 商品变更处理
const onProductChange = (product) => {
  if (product) {
    currentOrderItem.productId = product.productId;
    currentOrderItem.productName = product.productName;
    currentOrderItem.productType = product.productType;
    currentOrderItem.unitPrice = product.basePrice || 0;
    
    // 更新到父组件
    updateOrderItems();
  }
};

// 教练变更处理
const onCoachChange = (coachId, coachInfo) => {
  currentOrderItem.coachId = coachId;
  
  // 重新计算价格（如果需要根据教练调整价格）
  calculatePrice();
  
  updateOrderItems();
};

// 价格计算
const calculatePrice = () => {
  // 这里可以根据商品类型、教练、数量等计算价格
  // 暂时使用基础价格
  updateOrderItems();
};

// 更新订单明细到父组件
const updateOrderItems = () => {
  const orderItems = currentOrderItem.productId ? [{ ...currentOrderItem }] : [];
  
  emit('update:modelValue', {
    ...props.modelValue,
    orderItems: orderItems
  });
};

// 监听数量变化
watch(() => currentOrderItem.quantity, calculatePrice);

// 监听期望时间变化
watch(() => currentOrderItem.preferredTime, updateOrderItems);
</script>

<style scoped>
.product-order-form {
  background: #f6ffed;
  padding: 16px;
  border-radius: 6px;
  border-left: 4px solid #52c41a;
}

:deep(.total-price-input .ant-input) {
  font-weight: bold;
  color: #f5222d;
}
</style>