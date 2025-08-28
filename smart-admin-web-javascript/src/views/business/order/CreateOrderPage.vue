<template>
  <div class="create-order-page">
    <a-card 
      :title="embeddedMode ? null : '创建订单'" 
      class="order-card"
      :bordered="!embeddedMode"
    >
      
      <a-form 
        :model="orderForm" 
        :rules="formRules" 
        ref="orderFormRef"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        
        <!-- 会员选择 -->
        <a-form-item label="选择会员" name="memberId">
          <MemberSelector 
            v-model:value="orderForm.memberId" 
            @change="onMemberChange" 
            placeholder="请选择会员"
          />
        </a-form-item>
        
        <!-- 订单类型选择 -->
        <a-form-item label="订单类型" name="orderType">
          <a-radio-group 
            v-model:value="orderForm.orderType" 
            @change="onOrderTypeChange"
            button-style="solid"
          >
            <a-radio-button :value="1">课程订单</a-radio-button>
            <a-radio-button :value="2">课时包订单</a-radio-button>
            <a-radio-button :value="3">活动订单</a-radio-button>
            <a-radio-button :value="4">补差费订单</a-radio-button>
            <a-radio-button :value="5">饲养费订单</a-radio-button>
          </a-radio-group>
        </a-form-item>
        
        <!-- 动态表单区域 -->
        <div v-if="orderForm.orderType">
          
          <!-- 有商品订单 (1,2,3) -->
          <div v-if="hasProduct">
            <ProductOrderForm 
              v-model="orderForm" 
              :order-type="orderForm.orderType" 
              :member-id="orderForm.memberId"
            />
          </div>
          
          <!-- 补差费订单 (4) -->
          <div v-else-if="orderForm.orderType == 4">
            <ExtraFeeOrderForm 
              v-model="orderForm" 
              :member-id="orderForm.memberId"
            />
          </div>
          
          <!-- 饲养费订单 (5) -->
          <div v-else-if="orderForm.orderType == 5">
            <BoardingFeeOrderForm 
              v-model="orderForm"
            />
          </div>
                               
        </div>
        
        <!-- 支付方式选择 -->
        <a-form-item 
          label="支付方式" 
          name="paymentMethod" 
          v-if="orderForm.orderType"
        >
          <a-radio-group v-model:value="orderForm.paymentMethod">
            <a-radio value="wechat">微信支付</a-radio>
            <a-radio value="cash">现金支付</a-radio>
            <a-radio value="transfer">转账</a-radio>
            <a-radio value="card">刷卡</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <!-- 备注信息 -->
        <a-form-item label="备注信息" name="remark" v-if="orderForm.orderType">
          <a-textarea 
            v-model:value="orderForm.remark" 
            :rows="3" 
            placeholder="请输入订单备注信息（可选）"
            :max-length="500"
            show-count
          />
        </a-form-item>
        
        <!-- 操作按钮 -->
        <a-form-item 
          :wrapper-col="{ offset: 4, span: 20 }" 
          v-if="orderForm.orderType"
          style="margin-top: 24px;"
        >
          <a-space size="large">
            <a-button @click="handleCancel" size="large" v-if="embeddedMode">
              取消
            </a-button>
            <a-button @click="resetForm" size="large" v-if="!embeddedMode">
              重置表单
            </a-button>
            <a-button 
              type="primary" 
              @click="submitOrder" 
              :loading="submitting"
              size="large"
            >
              {{ embeddedMode ? '创建订单' : '创建订单' }}
            </a-button>
          </a-space>
        </a-form-item>
        
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { orderApi } from '/@/api/business/order/order-api';
import MemberSelector from '/@/components/business/MemberSelector.vue';
import ProductOrderForm from './components/ProductOrderForm.vue';
import ExtraFeeOrderForm from './components/ExtraFeeOrderForm.vue';
import BoardingFeeOrderForm from './components/BoardingFeeOrderForm.vue';

// Props 定义
const props = defineProps({
  embeddedMode: {
    type: Boolean,
    default: false
  },
  defaultOrderType: {
    type: Number,
    default: null
  },
  contextData: {
    type: Object,
    default: () => ({})
  }
});

// Events 定义
const emit = defineEmits(['success', 'cancel', 'close']);

const router = useRouter();
const orderFormRef = ref();
const submitting = ref(false);

// 表单数据
const orderForm = reactive({
  clubId: 1, // TODO: 从当前登录用户获取
  memberId: null,
  orderType: null,
  orderSubType: null,
  isDirectPrice: null,
  serviceAmount: null,
  quantity: 1,
  targetHorseId: null,
  relatedBookingId: null,
  orderItems: [],
  paymentMethod: 'cash',
  remark: ''
});

// 表单验证规则
const formRules = {
  memberId: [
    { required: true, message: '请选择会员', trigger: 'change' }
  ],
  orderType: [
    { required: true, message: '请选择订单类型', trigger: 'change' }
  ],
  paymentMethod: [
    { required: true, message: '请选择支付方式', trigger: 'change' }
  ]
};

// 是否有商品的订单类型
const hasProduct = computed(() => {
  return [1, 2, 3].includes(orderForm.orderType);
});

// 会员变更处理
const onMemberChange = (memberId) => {
  console.log('会员变更:', memberId);
  // 可以在这里加载会员相关信息
};

// 订单类型变更处理
const onOrderTypeChange = () => {
  // 清空子类型相关数据
  orderForm.orderSubType = null;
  orderForm.serviceAmount = null;
  orderForm.targetHorseId = null;
  orderForm.relatedBookingId = null;
  orderForm.orderItems = [];
  
  // 设置是否直接定价
  orderForm.isDirectPrice = hasProduct.value ? 0 : 1;
  
  console.log('订单类型变更:', orderForm.orderType, '是否直接定价:', orderForm.isDirectPrice);
};

// 重置表单
const resetForm = () => {
  orderFormRef.value?.resetFields();
  Object.assign(orderForm, {
    clubId: 1,
    memberId: null,
    orderType: props.defaultOrderType || null,
    orderSubType: null,
    isDirectPrice: null,
    serviceAmount: null,
    quantity: 1,
    targetHorseId: null,
    relatedBookingId: null,
    orderItems: [],
    paymentMethod: 'cash',
    remark: ''
  });
};

// 取消处理
const handleCancel = () => {
  emit('cancel');
};

// 初始化
onMounted(() => {
  if (props.defaultOrderType) {
    orderForm.orderType = props.defaultOrderType;
  }
});

// 提交订单
const submitOrder = async () => {
  try {
    await orderFormRef.value?.validate();
    
    submitting.value = true;
    
    // 准备提交数据
    const submitData = { ...orderForm };
    
    // 如果是商品订单且没有订单明细，提示错误
    if (hasProduct.value && (!submitData.orderItems || submitData.orderItems.length === 0)) {
      message.error('请添加商品明细');
      return;
    }
    
    // 如果是直接定价订单且没有服务金额，提示错误
    if (!hasProduct.value && (!submitData.serviceAmount || submitData.serviceAmount <= 0)) {
      message.error('请输入服务金额');
      return;
    }
    
    console.log('提交订单数据:', submitData);
    
    const result = await orderApi.createOrder(submitData);
    
    message.success('订单创建成功！');
    
    if (props.embeddedMode) {
      // 嵌入模式：发送成功事件
      emit('success', {
        orderId: result.data,
        orderNo: result.orderNo,
        orderType: submitData.orderType,
        ...submitData
      });
    } else {
      // 独立页面模式：跳转到订单详情页
      router.push(`/order/detail/${result.data}`);
    }
    
  } catch (error) {
    console.error('创建订单失败:', error);
    message.error(error.message || '创建订单失败，请重试');
  } finally {
    submitting.value = false;
  }
};
</script>

<style scoped>
.create-order-page {
  padding: 20px;
}

.order-card {
  max-width: 1000px;
  margin: 0 auto;
}

.order-card :deep(.ant-card-body) {
  padding: 24px 32px;
}

:deep(.ant-radio-button-wrapper) {
  height: 40px;
  line-height: 38px;
}

:deep(.ant-form-item) {
  margin-bottom: 24px;
}

:deep(.ant-form-item-label) {
  font-weight: 500;
}
</style>