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
                  <a-icon 
                    :component="getIconComponent(item.type)" 
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

    // 获取价格预估
    const estimateResponse = await productApi.getPriceEstimate(
      props.productData.productId || 0,
      {
        productType: props.productData.productType,
        dynamicConfig: props.dynamicConfig
      }
    );

    if (estimateResponse.ok) {
      const estimate = estimateResponse.data;
      Object.assign(priceData, {
        basePrice: estimate.basePrice || 0,
        estimatedPrice: estimate.estimatedPrice || 0,
        minPrice: estimate.minPrice || 0,
        maxPrice: estimate.maxPrice || 0,
        priceUnit: estimate.priceUnit || '元'
      });

      // 构建价格明细
      buildPriceDetails(estimate);
      
      // 获取价格规则
      priceRules.value = estimate.priceRules || [];
      
      // 获取价格预警
      priceWarnings.value = estimate.warnings || [];
      
      // 获取价格建议
      priceSuggestions.value = estimate.suggestions || [];

      // 触发价格变化事件
      emit('price-change', {
        ...priceData,
        details: priceDetails.value,
        rules: priceRules.value,
        warnings: priceWarnings.value,
        suggestions: priceSuggestions.value
      });
    } else {
      errorMessage.value = estimateResponse.msg || '价格预估失败';
      emit('error', errorMessage.value);
    }
  } catch (error) {
    errorMessage.value = '价格预估失败，请稍后重试';
    console.error('价格预估失败:', error);
    emit('error', errorMessage.value);
  } finally {
    calculating.value = false;
  }
}

function buildPriceDetails(estimate) {
  const details = [];
  
  // 基础价格
  if (estimate.basePrice) {
    details.push({
      key: 'base',
      label: '基础价格',
      value: estimate.basePrice,
      important: false
    });
  }

  // 动态调整项
  if (estimate.adjustments && Array.isArray(estimate.adjustments)) {
    estimate.adjustments.forEach((adj, index) => {
      details.push({
        key: `adj_${index}`,
        label: adj.label || '价格调整',
        value: Math.abs(adj.amount || 0),
        negative: adj.amount < 0,
        important: false
      });
    });
  }

  // 分隔线
  if (details.length > 0) {
    details.push({
      key: 'divider',
      label: '——————————',
      value: '——————',
      important: false
    });
  }

  // 预估总价
  details.push({
    key: 'total',
    label: '预估总价',
    value: estimate.estimatedPrice || 0,
    important: true
  });

  priceDetails.value = details;
}

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
  return colorMap[type] || '#1890ff';
}

// 手动触发价格计算
function triggerCalculate() {
  calculatePrice();
}

// ======================== 暴露方法 ========================
defineExpose({
  calculate: triggerCalculate,
  getPriceData: () => ({ ...priceData }),
  getPriceDetails: () => [...priceDetails.value]
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
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

:deep(.ant-statistic-content) {
  font-weight: 600;
}

:deep(.ant-table-tbody > tr > td) {
  padding: 4px 8px;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.ant-table-tbody > tr:last-child > td) {
  border-bottom: none;
}

:deep(.ant-collapse) {
  background: transparent;
}

:deep(.ant-collapse > .ant-collapse-item > .ant-collapse-header) {
  padding: 8px 0;
  background: transparent;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.ant-collapse-content > .ant-collapse-content-box) {
  padding: 12px 0;
}

:deep(.ant-descriptions-item-label) {
  font-weight: 500;
  background: #fafafa;
}

:deep(.ant-list-item-meta-title) {
  margin-bottom: 2px;
  font-size: 13px;
}

:deep(.ant-list-item-meta-description) {
  font-size: 12px;
  color: #666;
}

:deep(.ant-list-item) {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.ant-list-item:last-child) {
  border-bottom: none;
}

h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #262626;
}
</style>