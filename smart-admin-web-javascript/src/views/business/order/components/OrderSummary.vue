<template>
  <div class="order-summary">
    <a-divider>订单汇总</a-divider>
    
    <div class="summary-content">
      <a-row :gutter="24">
        
        <a-col :span="12">
          <div class="summary-section">
            <h4>订单信息</h4>
            <div class="info-item">
              <span class="label">订单类型：</span>
              <span class="value">{{ orderTypeText }}</span>
            </div>
            <div class="info-item" v-if="orderData.orderSubType">
              <span class="label">服务类型：</span>
              <span class="value">{{ subTypeText }}</span>
            </div>
            <div class="info-item" v-if="orderData.targetHorseId">
              <span class="label">目标马匹：</span>
              <span class="value">{{ horseInfo.horseName || '未选择' }}</span>
            </div>
            <div class="info-item" v-if="orderData.relatedBookingId">
              <span class="label">关联预约：</span>
              <span class="value">预约ID: {{ orderData.relatedBookingId }}</span>
            </div>
          </div>
        </a-col>
        
        <a-col :span="12">
          <div class="summary-section">
            <h4>费用明细</h4>
            
            <!-- 商品订单明细 -->
            <div v-if="hasProduct && orderData.orderItems && orderData.orderItems.length > 0">
              <div v-for="item in orderData.orderItems" :key="item.productId" class="item-detail">
                <div class="info-item">
                  <span class="label">{{ item.productName }}：</span>
                  <span class="value">¥{{ item.unitPrice }} × {{ item.quantity }}</span>
                </div>
              </div>
            </div>
            
            <!-- 直接定价订单明细 -->
            <div v-else-if="!hasProduct && orderData.serviceAmount">
              <div class="info-item">
                <span class="label">服务费用：</span>
                <span class="value">¥{{ orderData.serviceAmount }} × {{ orderData.quantity || 1 }}</span>
              </div>
            </div>
            
            <a-divider style="margin: 12px 0;" />
            
            <div class="info-item total-amount">
              <span class="label">订单总额：</span>
              <span class="value">¥{{ totalAmount }}</span>
            </div>
            
            <div class="info-item">
              <span class="label">支付方式：</span>
              <span class="value">{{ paymentMethodText }}</span>
            </div>
          </div>
        </a-col>
        
      </a-row>
      
      <!-- 备注信息 -->
      <div v-if="orderData.remark" class="remark-section">
        <h4>备注信息</h4>
        <div class="remark-content">{{ orderData.remark }}</div>
      </div>
      
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';

const props = defineProps({
  orderData: {
    type: Object,
    required: true
  }
});

// 获取马匹信息（模拟数据，实际需要从API获取）
const horseInfo = ref({
  horseName: '示例马匹'
});

// 订单类型文本
const orderTypeText = computed(() => {
  const typeMap = {
    1: '课程订单',
    2: '课时包订单', 
    3: '活动订单',
    4: '补差费订单',
    5: '饲养费订单',
    6: '健康服务订单'
  };
  return typeMap[props.orderData.orderType] || '未知类型';
});

// 子类型文本
const subTypeText = computed(() => {
  const subTypeMap = {
    'single_class': '单人课',
    'multi_class': '多人课',
    'extra_coach_fee': '教练费补差',
    'extra_lesson_fee': '课时费补差', 
    'extra_horse_fee': '马匹费补差',
    'boarding_fee': '饲养费',
    'health_shoeing': '钉蹄服务',
    'health_deworming': '驱虫服务',
    'health_dental': '搓牙服务',
    'health_vaccine': '疫苗服务',
    'health_medication': '养护服务'
  };
  return subTypeMap[props.orderData.orderSubType] || '';
});

// 支付方式文本
const paymentMethodText = computed(() => {
  const methodMap = {
    'wechat': '微信支付',
    'cash': '现金支付',
    'transfer': '转账',
    'card': '刷卡'
  };
  return methodMap[props.orderData.paymentMethod] || '';
});

// 是否有商品
const hasProduct = computed(() => {
  return [1, 2, 3].includes(props.orderData.orderType);
});

// 总金额计算
const totalAmount = computed(() => {
  if (hasProduct.value && props.orderData.orderItems) {
    return props.orderData.orderItems.reduce((total, item) => {
      return total + (item.unitPrice || 0) * (item.quantity || 1);
    }, 0);
  } else if (!hasProduct.value && props.orderData.serviceAmount) {
    return (props.orderData.serviceAmount || 0) * (props.orderData.quantity || 1);
  }
  return 0;
});
</script>

<style scoped>
.order-summary {
  margin-top: 24px;
}

.summary-content {
  background: #fafafa;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #d9d9d9;
}

.summary-section {
  margin-bottom: 16px;
}

.summary-section h4 {
  margin-bottom: 12px;
  color: #262626;
  font-size: 14px;
  font-weight: 600;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 13px;
}

.info-item .label {
  color: #595959;
  flex: 0 0 auto;
}

.info-item .value {
  color: #262626;
  font-weight: 500;
}

.total-amount {
  font-size: 16px;
  font-weight: 600;
}

.total-amount .value {
  color: #f5222d;
  font-size: 18px;
}

.item-detail {
  margin-bottom: 4px;
}

.remark-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed #d9d9d9;
}

.remark-content {
  background: #fff;
  padding: 12px;
  border-radius: 4px;
  border: 1px solid #d9d9d9;
  font-size: 13px;
  color: #595959;
  white-space: pre-wrap;
}
</style>