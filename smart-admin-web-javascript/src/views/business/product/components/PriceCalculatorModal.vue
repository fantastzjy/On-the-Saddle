<!--
  * 价格计算器模态框组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-modal
    :visible="visible"
    title="价格计算器"
    width="800px"
    :maskClosable="false"
    @cancel="onCancel"
    @ok="onOk"
  >
    <template #footer>
      <a-button @click="onCancel">关闭</a-button>
      <a-button type="primary" @click="onCalculate" :loading="calculating">重新计算</a-button>
    </template>

    <div v-if="product">
      <!-- 商品信息展示 -->
      <a-card size="small" class="product-info-card">
        <a-row>
          <a-col :span="16">
            <h4>{{ product.productName }}</h4>
            <p class="product-code">商品编码: {{ product.productCode || '-' }}</p>
            <a-tag :color="getProductTypeColor(product.productType)">
              {{ getProductTypeDesc(product.productType) }}
            </a-tag>
          </a-col>
          <a-col :span="8" class="text-right">
            <img 
              v-if="getFirstImage(product.images)" 
              :src="getFirstImage(product.images)" 
              class="product-image"
            />
          </a-col>
        </a-row>
      </a-card>

      <!-- 计算参数配置 -->
      <a-card size="small" title="计算参数" class="mt-16">
        <a-form :model="calculateForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="数量">
                <a-input-number 
                  v-model:value="calculateForm.quantity" 
                  :min="1" 
                  :max="100"
                  style="width: 100%"
                  @change="onParamChange"
                />
              </a-form-item>
            </a-col>
            
            <a-col :span="12" v-if="product.productType === PRODUCT_TYPE_ENUM.COURSE.value">
              <a-form-item label="参与人数">
                <a-input-number 
                  v-model:value="calculateForm.participantCount" 
                  :min="1" 
                  :max="10"
                  style="width: 100%"
                  placeholder="多人课时设置"
                  @change="onParamChange"
                />
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item label="会员等级">
                <a-select 
                  v-model:value="calculateForm.memberLevel" 
                  placeholder="请选择"
                  allowClear
                  style="width: 100%"
                  @change="onParamChange"
                >
                  <a-select-option 
                    v-for="item in Object.values(MEMBER_LEVEL_ENUM)" 
                    :key="item.value" 
                    :value="item.value"
                  >
                    {{ item.desc }} ({{ Math.round(item.discount * 100) }}%折扣)
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item label="优惠券代码">
                <a-input 
                  v-model:value="calculateForm.couponCode" 
                  placeholder="输入优惠券代码"
                  @change="onParamChange"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>

      <!-- 价格计算结果 -->
      <a-card size="small" title="价格明细" class="mt-16" v-if="priceResult">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-statistic
              title="原价"
              :value="priceResult.originalPrice"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#666' }"
            />
          </a-col>
          <a-col :span="12">
            <a-statistic
              title="优惠金额"
              :value="priceResult.discountAmount"
              :precision="2"
              prefix="-¥"
              :value-style="{ color: '#ff4d4f' }"
            />
          </a-col>
        </a-row>

        <a-divider />

        <a-row :gutter="16">
          <a-col :span="12">
            <a-statistic
              title="动态调整"
              :value="priceResult.dynamicAdjustment || 0"
              :precision="2"
              :prefix="(priceResult.dynamicAdjustment || 0) >= 0 ? '+¥' : '-¥'"
              :value-style="{ color: (priceResult.dynamicAdjustment || 0) >= 0 ? '#52c41a' : '#ff4d4f' }"
            />
          </a-col>
          <a-col :span="12">
            <a-statistic
              title="最终价格"
              :value="priceResult.finalPrice"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#1890ff', fontSize: '24px', fontWeight: 'bold' }"
            />
          </a-col>
        </a-row>

        <!-- 价格明细展开 -->
        <a-collapse v-if="priceResult.priceBreakdown" class="mt-16">
          <a-collapse-panel key="breakdown" header="查看详细计算过程">
            <a-descriptions size="small" :column="2" bordered>
              <a-descriptions-item label="商品原价">
                ¥{{ Number(priceResult.priceBreakdown.originalPrice || 0).toFixed(2) }}
              </a-descriptions-item>
              <a-descriptions-item label="会员折扣">
                -¥{{ Number(priceResult.priceBreakdown.memberDiscount || 0).toFixed(2) }}
              </a-descriptions-item>
              <a-descriptions-item label="优惠券折扣">
                -¥{{ Number(priceResult.priceBreakdown.couponDiscount || 0).toFixed(2) }}
              </a-descriptions-item>
              <a-descriptions-item label="动态调整">
                {{ (priceResult.priceBreakdown.dynamicAdjustment || 0) >= 0 ? '+' : '' }}¥{{ Number(priceResult.priceBreakdown.dynamicAdjustment || 0).toFixed(2) }}
              </a-descriptions-item>
              <a-descriptions-item label="最终价格" :span="2">
                <span style="font-size: 16px; font-weight: bold; color: #1890ff;">
                  ¥{{ Number(priceResult.priceBreakdown.finalPrice || 0).toFixed(2) }}
                </span>
              </a-descriptions-item>
            </a-descriptions>
          </a-collapse-panel>
        </a-collapse>
      </a-card>

      <!-- 价格预估信息 -->
      <a-card size="small" title="价格预估" class="mt-16" v-if="priceEstimate">
        <a-alert 
          :message="`价格区间: ¥${Number(priceEstimate.minPrice || 0).toFixed(2)} - ¥${Number(priceEstimate.maxPrice || 0).toFixed(2)}`"
          :description="`基础价格: ¥${Number(priceEstimate.basePrice || 0).toFixed(2)} ${priceEstimate.priceUnit || ''}`"
          type="info" 
          show-icon 
        />
      </a-card>

      <!-- 计算失败提示 -->
      <a-alert 
        v-if="calculateError"
        :message="calculateError"
        type="error"
        show-icon
        class="mt-16"
      />
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue';
import { message } from 'ant-design-vue';
import { productApi } from '/@/api/business/product/product-api';
import { 
  PRODUCT_TYPE_ENUM, 
  MEMBER_LEVEL_ENUM 
} from '/@/constants/business/product/product-const';

// ======================== Props & Emits ========================
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  product: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['update:visible', 'ok']);

// ======================== 响应式数据 ========================
const calculating = ref(false);
const priceResult = ref(null);
const priceEstimate = ref(null);
const calculateError = ref('');

const calculateForm = reactive({
  quantity: 1,
  participantCount: null,
  memberLevel: null,
  couponCode: ''
});

// ======================== 计算属性 ========================
const visible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
});

// ======================== 监听器 ========================
watch(() => props.visible, (newVal) => {
  if (newVal && props.product) {
    resetForm();
    loadPriceEstimate();
    calculatePrice();
  }
});

watch(() => props.product, (newVal) => {
  if (newVal && props.visible) {
    resetForm();
    loadPriceEstimate();
    calculatePrice();
  }
});

// ======================== 方法 ========================
function resetForm() {
  Object.assign(calculateForm, {
    quantity: 1,
    participantCount: props.product?.productType === PRODUCT_TYPE_ENUM.COURSE.value ? 1 : null,
    memberLevel: null,
    couponCode: ''
  });
  priceResult.value = null;
  priceEstimate.value = null;
  calculateError.value = '';
}

async function loadPriceEstimate() {
  if (!props.product?.productId) return;
  
  try {
    const response = await productApi.getPriceEstimate(props.product.productId);
    if (response.ok) {
      priceEstimate.value = response.data;
    }
  } catch (error) {
    console.error('获取价格预估失败:', error);
  }
}

async function calculatePrice() {
  if (!props.product?.productId) return;
  
  try {
    calculating.value = true;
    calculateError.value = '';
    
    const params = {
      productId: props.product.productId,
      quantity: calculateForm.quantity,
      coachId: null, // 如果需要指定教练，可以添加选择器
      participantCount: calculateForm.participantCount,
      memberLevel: calculateForm.memberLevel,
      couponCode: calculateForm.couponCode || null
    };
    
    const response = await productApi.calculatePrice(params);
    if (response.ok) {
      priceResult.value = response.data;
    } else {
      calculateError.value = response.msg || '价格计算失败';
      priceResult.value = null;
    }
  } catch (error) {
    calculateError.value = '价格计算失败，请稍后重试';
    priceResult.value = null;
    console.error('价格计算失败:', error);
  } finally {
    calculating.value = false;
  }
}

function onParamChange() {
  // 参数变化时自动重新计算
  if (props.visible && props.product) {
    calculatePrice();
  }
}

function onCalculate() {
  calculatePrice();
}

function onCancel() {
  visible.value = false;
}

function onOk() {
  emit('ok');
}

// ======================== 辅助方法 ========================
function getProductTypeDesc(value) {
  return Object.values(PRODUCT_TYPE_ENUM).find(item => item.value === value)?.desc || '-';
}

function getProductTypeColor(value) {
  const colorMap = {
    [PRODUCT_TYPE_ENUM.COURSE.value]: 'blue',
    [PRODUCT_TYPE_ENUM.PACKAGE.value]: 'green',
    [PRODUCT_TYPE_ENUM.ACTIVITY.value]: 'purple'
  };
  return colorMap[value] || 'default';
}

function getFirstImage(images) {
  if (!images) return null;
  
  try {
    const parsed = JSON.parse(images);
    if (Array.isArray(parsed) && parsed.length > 0) {
      return parsed[0];
    }
  } catch {
    // 如果不是JSON，尝试直接作为URL
    if (typeof images === 'string' && images.trim()) {
      return images;
    }
  }
  
  return null;
}
</script>

<style scoped>
.product-info-card {
  background: #fafafa;
  border: 1px solid #d9d9d9;
}

.product-code {
  color: #666;
  margin: 8px 0;
  font-size: 12px;
}

.product-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #d9d9d9;
}

.text-right {
  text-align: right;
}

.mt-16 {
  margin-top: 16px;
}

:deep(.ant-statistic-title) {
  font-size: 12px;
  color: #666;
}

:deep(.ant-statistic-content) {
  font-weight: 600;
}

:deep(.ant-collapse) {
  background: transparent;
}

:deep(.ant-collapse > .ant-collapse-item > .ant-collapse-header) {
  padding: 8px 0;
  background: transparent;
}

:deep(.ant-collapse-content > .ant-collapse-content-box) {
  padding: 12px 0;
}
</style>