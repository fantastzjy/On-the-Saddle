<!--
  * 价格预览组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="price-preview">
    <a-spin :spinning="calculating" tip="计算价格中...">
      <!-- 基础价格信息 -->
      <a-row :gutter="16">
        <a-col :span="8">
          <a-statistic
            title="基础价格"
            :value="priceData.basePrice || 0"
            :precision="2"
            prefix="¥"
            :value-style="{ color: '#666' }"
          />
        </a-col>
        <a-col :span="8">
          <a-statistic
            title="预估价格"
            :value="priceData.estimatedPrice || 0"
            :precision="2"
            prefix="¥"
            :value-style="{ color: '#1890ff' }"
          />
        </a-col>
        <a-col :span="8">
          <a-statistic
            title="价格区间"
            :value="`${Number(priceData.minPrice || 0).toFixed(2)} - ${Number(priceData.maxPrice || 0).toFixed(2)}`"
            prefix="¥"
            :value-style="{ color: '#52c41a', fontSize: '14px' }"
          />
        </a-col>
      </a-row>

      <a-divider />

      <!-- 详细价格计算 -->
      <div v-if="priceDetails && priceDetails.length > 0">
        <h4>价格构成</h4>
        <a-table
          :dataSource="priceDetails"
          :columns="priceDetailColumns"
          :pagination="false"
          size="small"
          :showHeader="false"
          rowKey="key"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'label'">
              <span :style="{ fontWeight: record.important ? 'bold' : 'normal' }">
                {{ record.label }}
              </span>
            </template>
            <template v-if="column.dataIndex === 'value'">
              <span 
                :style="{ 
                  color: record.important ? '#1890ff' : (record.negative ? '#ff4d4f' : '#666'),
                  fontWeight: record.important ? 'bold' : 'normal'
                }"
              >
                {{ record.negative && record.value > 0 ? '-' : '' }}¥{{ Number(record.value || 0).toFixed(2) }}
              </span>
            </template>
          </template>
        </a-table>
      </div>

      <!-- 价格计算规则说明 -->
      <a-collapse v-if="priceRules && priceRules.length > 0" class="mt-16">
        <a-collapse-panel key="rules" header="价格计算规则">
          <a-descriptions size="small" :column="1" bordered>
            <a-descriptions-item 
              v-for="rule in priceRules" 
              :key="rule.key"
              :label="rule.label"
            >
              {{ rule.description }}
            </a-descriptions-item>
          </a-descriptions>
        </a-collapse-panel>
      </a-collapse>

      <!-- 价格预警提示 -->
      <div v-if="priceWarnings && priceWarnings.length > 0" class="mt-16">
        <a-alert
          v-for="warning in priceWarnings"
          :key="warning.type"
          :message="warning.message"
          :type="warning.level"
          :show-icon="true"
          :closable="false"
          class="mb-8"
        />
      </div>

      <!-- 价格建议 -->
      <div v-if="priceSuggestions && priceSuggestions.length > 0" class="mt-16">
        <h4>价格建议</h4>
        <a-list size="small" :dataSource="priceSuggestions">
          <template #renderItem="{ item }">
            <a-list-item>
              <a-list-item-meta>
                <template #avatar>
                  <component 
                    :is="getIconComponent(item.type)" 
                    :style="{ color: getIconColor(item.type) }" 
                  />
                </template>
                <template #title>
                  <span>{{ item.title }}</span>
                </template>
                <template #description>
                  <span>{{ item.description }}</span>
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </div>

      <!-- 计算失败提示 -->
      <a-alert 
        v-if="errorMessage"
        :message="errorMessage"
        type="error"
        show-icon
        class="mt-16"
      />
    </a-spin>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue';
import { BulbOutlined, ExclamationCircleOutlined, CheckCircleOutlined } from '@ant-design/icons-vue';
import { productApi } from '/@/api/business/product/product-api';

// ======================== Props & Emits ========================
const props = defineProps({
  productData: {
    type: Object,
    required: true
  },
  dynamicConfig: {
    type: Object,
    default: () => ({})
  },
  autoCalculate: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['price-change', 'error']);

// ======================== 响应式数据 ========================
const calculating = ref(false);
const errorMessage = ref('');

const priceData = reactive({
  basePrice: 0,
  estimatedPrice: 0,
  minPrice: 0,
  maxPrice: 0,
  priceUnit: '元'
});

const priceDetails = ref([]);
const priceRules = ref([]);
const priceWarnings = ref([]);
const priceSuggestions = ref([]);

// ======================== 计算属性 ========================
const priceDetailColumns = computed(() => [
  {
    dataIndex: 'label',
    width: '60%'
  },
  {
    dataIndex: 'value',
    width: '40%',
    align: 'right'
  }
]);

// ======================== 监听器 ========================
watch(() => [props.productData, props.dynamicConfig], () => {
  if (props.autoCalculate && props.productData.productType) {
    calculatePrice();
  }
}, { deep: true });

// ======================== 初始化 ========================
onMounted(() => {
  if (props.autoCalculate && props.productData.productType) {
    calculatePrice();
  }
});

// ======================== 方法 ========================
async function calculatePrice() {
  if (!props.productData.productType) return;

  try {
    calculating.value = true;
    errorMessage.value = '';

    // 构建价格计算参数
    const priceParams = {
      productType: props.productData.productType,
      dynamicConfig: props.dynamicConfig,
      quantity: 1
    };

    // 调用价格计算API
    const response = await productApi.calculatePrice(priceParams);

    if (response.ok) {
      const result = response.data;
      Object.assign(priceData, {
        basePrice: result.basePrice || result.originalPrice || 0,
        estimatedPrice: result.finalPrice || result.estimatedPrice || 0,
        minPrice: result.minPrice || result.finalPrice || 0,
        maxPrice: result.maxPrice || result.finalPrice || 0,
        priceUnit: '元'
      });

      // 构建价格明细
      buildPriceDetails(result);
      
      // 获取价格规则
      priceRules.value = result.priceRules || [];
      
      // 获取价格预警
      priceWarnings.value = result.priceWarnings || [];
      
      // 获取价格建议
      priceSuggestions.value = result.priceSuggestions || [];
      
      // 触发价格变化事件
      emit('price-change', {
        basePrice: priceData.basePrice,
        finalPrice: priceData.estimatedPrice,
        priceDetails: priceDetails.value
      });

    } else {
      errorMessage.value = response.msg || '价格计算失败';
      emit('error', errorMessage.value);
    }

  } catch (error) {
    errorMessage.value = '价格计算请求失败';
    emit('error', errorMessage.value);
    console.error('价格计算失败:', error);
  } finally {
    calculating.value = false;
  }
}

function buildPriceDetails(result) {
  const details = [];
  
  if (result.priceDetails && Array.isArray(result.priceDetails)) {
    // 使用API返回的明细
    priceDetails.value = result.priceDetails.map(item => ({
      key: item.label,
      label: item.label,
      value: item.value,
      important: item.important || false,
      negative: item.negative || false
    }));
  } else {
    // 根据数据库设计构建明细 - 严格按照 base_price = coach_fee + horse_fee
    if (props.dynamicConfig.coachFee) {
      details.push({
        key: 'coachFee',
        label: '教练费',
        value: props.dynamicConfig.coachFee,
        important: false
      });
    }
    
    if (props.dynamicConfig.horseFee) {
      details.push({
        key: 'horseFee',
        label: '马匹费',
        value: props.dynamicConfig.horseFee,
        important: false
      });
    }
    
    // 数据库计算字段：base_price = coach_fee + horse_fee
    if (props.dynamicConfig.coachFee && props.dynamicConfig.horseFee) {
      const basePrice = Number(props.dynamicConfig.coachFee) + Number(props.dynamicConfig.horseFee);
      details.push({
        key: 'basePrice',
        label: '基础单价',
        value: basePrice,
        important: true
      });
    }
    
    // 多人课显示人数价格
    if (props.dynamicConfig.classType === 2 && props.dynamicConfig.multiPriceConfig) {
      details.push({
        key: 'multiPrice',
        label: '多人课价格',
        value: priceData.estimatedPrice,
        important: true
      });
    }
    
    priceDetails.value = details;
  }
}

// ======================== 辅助方法 ========================
function getIconComponent(type) {
  const iconMap = {
    suggestion: BulbOutlined,
    warning: ExclamationCircleOutlined,
    success: CheckCircleOutlined
  };
  return iconMap[type] || BulbOutlined;
}

function getIconColor(type) {
  const colorMap = {
    suggestion: '#1890ff',
    warning: '#faad14',
    success: '#52c41a'
  };
  return colorMap[type] || '#666';
}

// ======================== 暴露方法 ========================
defineExpose({
  calculatePrice,
  refresh: calculatePrice
});
</script>

<style scoped>
.price-preview {
  width: 100%;
}

.mt-16 {
  margin-top: 16px;
}

.mb-8 {
  margin-bottom: 8px;
}

:deep(.ant-statistic-title) {
  color: #666;
  font-size: 12px;
}

:deep(.ant-statistic-content) {
  font-size: 18px;
  font-weight: 600;
}

:deep(.ant-collapse-header) {
  font-size: 13px;
  font-weight: 500;
}
</style>