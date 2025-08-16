<!--
  * 多人课价格配置组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="multi-class-price-config">
    <a-card size="small" title="多人课价格配置">
      <template #extra>
        <a-button type="link" size="small" @click="resetToBasePrice">
          <RedoOutlined />
          重置为基础价格
        </a-button>
      </template>
      
      <!-- 价格配置表格 -->
      <a-table
        :dataSource="priceTableData"
        :columns="priceTableColumns"
        :pagination="false"
        size="small"
        rowKey="people"
        bordered
      >
        <template #bodyCell="{ column, record, index }">
          <!-- 人数列 -->
          <template v-if="column.dataIndex === 'people'">
            <span class="people-label">{{ record.people }}人课</span>
          </template>
          
          <!-- 价格列 -->
          <template v-if="column.dataIndex === 'price'">
            <a-input-number
              v-model:value="record.price"
              :min="0"
              :max="9999"
              :precision="2"
              :step="10"
              placeholder="请输入价格"
              style="width: 100%"
              @change="onPriceChange(record.people, $event)"
            >
              <template #addonBefore>¥</template>
              <template #addonAfter>元/人</template>
            </a-input-number>
          </template>
          
          <!-- 折扣列 -->
          <template v-if="column.dataIndex === 'discount'">
            <span :class="['discount-text', getDiscountClass(record.discount)]">
              {{ record.discount }}
            </span>
          </template>
        </template>
      </a-table>
      
      <!-- 价格说明 -->
      <div class="price-tips">
        <a-alert
          message="价格设置说明"
          type="info"
          show-icon
          class="mb-12"
        >
          <template #description>
            <ul class="tips-list">
              <li>基础价格为单人课价格：¥{{ basePrice.toFixed(2) }}/人</li>
              <li>多人课价格通常会有优惠，建议设置为基础价格的80%-95%</li>
              <li>人数越多，单价越优惠，有助于提高客户满意度</li>
              <li>可点击"重置为基础价格"快速设置初始价格</li>
            </ul>
          </template>
        </a-alert>
        
        <!-- 价格预览 -->
        <div class="price-preview">
          <h4>价格预览</h4>
          <a-row :gutter="16">
            <a-col v-for="item in priceTableData" :key="item.people" :span="6">
              <a-statistic
                :title="`${item.people}人课`"
                :value="item.price || 0"
                :precision="2"
                prefix="¥"
                suffix="/人"
                :value-style="{ 
                  fontSize: '14px',
                  color: item.price ? '#1890ff' : '#999'
                }"
              />
            </a-col>
          </a-row>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue';
import { RedoOutlined } from '@ant-design/icons-vue';

// ======================== Props & Emits ========================
const props = defineProps({
  value: {
    type: [String, Object],
    default: null
  },
  basePrice: {
    type: Number,
    default: 0
  },
  maxStudents: {
    type: Number,
    default: 5
  },
  disabled: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:value', 'change']);

// ======================== 响应式数据 ========================
const priceTableData = reactive([
  { people: 2, price: null },
  { people: 3, price: null },
  { people: 4, price: null },
  { people: 5, price: null }
]);

// ======================== 计算属性 ========================
const priceTableColumns = computed(() => [
  {
    title: '人数',
    dataIndex: 'people',
    width: '25%',
    align: 'center'
  },
  {
    title: '价格/人',
    dataIndex: 'price',
    width: '40%',
    align: 'center'
  },
  {
    title: '相比单人课',
    dataIndex: 'discount',
    width: '35%',
    align: 'center'
  }
]);

// ======================== 监听器 ========================
watch(() => props.value, (newVal) => {
  parseConfigValue(newVal);
}, { immediate: true });

watch(() => props.maxStudents, (newVal) => {
  updatePeopleRange(newVal);
}, { immediate: true });

// 为每个价格项添加计算属性
priceTableData.forEach((item, index) => {
  // 计算折扣
  Object.defineProperty(item, 'discount', {
    get() {
      if (!item.price || !props.basePrice) return '-';
      const discount = ((props.basePrice - item.price) / props.basePrice * 100);
      if (discount > 0) {
        return `优惠${discount.toFixed(1)}%`;
      } else if (discount < 0) {
        return `加价${Math.abs(discount).toFixed(1)}%`;
      } else {
        return '与单人课同价';
      }
    },
    enumerable: true
  });
});

// ======================== 方法 ========================
function parseConfigValue(value) {
  if (!value) {
    resetPrices();
    return;
  }
  
  try {
    let config = value;
    if (typeof value === 'string') {
      config = JSON.parse(value);
    }
    
    if (config && config.coaches && Array.isArray(config.coaches) && config.coaches.length > 0) {
      // 取第一个教练的价格配置
      const firstCoach = config.coaches[0];
      if (firstCoach && firstCoach.prices) {
        priceTableData.forEach(item => {
          const priceForPeople = firstCoach.prices[item.people.toString()];
          item.price = priceForPeople ? Number(priceForPeople) : null;
        });
      }
    }
  } catch (e) {
    console.warn('解析多人课价格配置失败:', e);
    resetPrices();
  }
}

function updatePeopleRange(maxStudents) {
  // 根据最大人数更新可配置的人数范围
  const validRange = Math.min(Math.max(maxStudents, 2), 5);
  
  // 只显示2到maxStudents的配置项
  priceTableData.forEach((item, index) => {
    // 保持数组长度，但可以通过CSS控制显示
    // 这里暂时保持原有4项配置（2-5人）
  });
}

function onPriceChange(people, price) {
  const item = priceTableData.find(item => item.people === people);
  if (item) {
    item.price = price;
  }
  
  emitConfigChange();
}

function resetToBasePrice() {
  priceTableData.forEach(item => {
    // 设置为基础价格的90%-80%（人数越多优惠越大）
    const discountRate = 0.95 - (item.people - 2) * 0.025; // 2人95%，3人92.5%，4人90%，5人87.5%
    item.price = Number((props.basePrice * discountRate).toFixed(2));
  });
  
  emitConfigChange();
}

function resetPrices() {
  priceTableData.forEach(item => {
    item.price = null;
  });
}

function emitConfigChange() {
  // 构建符合数据库格式的配置
  const prices = {};
  priceTableData.forEach(item => {
    if (item.price !== null && item.price !== undefined) {
      prices[item.people.toString()] = item.price;
    }
  });
  
  // 构建完整的配置对象
  const config = {
    coaches: [
      {
        coach_id: 1, // 默认教练ID，实际使用时应该从教练列表获取
        prices: prices
      }
    ]
  };
  
  const configStr = JSON.stringify(config);
  emit('update:value', configStr);
  emit('change', configStr);
}

function getDiscountClass(discount) {
  if (discount === '-') return '';
  if (discount.includes('优惠')) return 'discount-good';
  if (discount.includes('加价')) return 'discount-bad';
  return 'discount-normal';
}

// ======================== 初始化 ========================
onMounted(() => {
  // 如果有基础价格但没有配置，自动设置默认价格
  if (props.basePrice > 0 && !props.value) {
    setTimeout(() => {
      resetToBasePrice();
    }, 100);
  }
});
</script>

<style scoped>
.multi-class-price-config {
  width: 100%;
}

.people-label {
  font-weight: 500;
  color: #333;
}

.price-tips {
  margin-top: 16px;
}

.tips-list {
  margin: 0;
  padding-left: 20px;
}

.tips-list li {
  margin-bottom: 4px;
  color: #666;
  font-size: 12px;
}

.price-preview {
  margin-top: 16px;
  padding: 12px;
  background: #fafafa;
  border-radius: 6px;
}

.price-preview h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #333;
}

.discount-text {
  font-size: 12px;
  font-weight: 500;
}

.discount-good {
  color: #52c41a;
}

.discount-bad {
  color: #ff4d4f;
}

.discount-normal {
  color: #1890ff;
}

.mb-12 {
  margin-bottom: 12px;
}

:deep(.ant-card-head-title) {
  font-size: 14px;
  font-weight: 600;
}

:deep(.ant-statistic-title) {
  font-size: 12px;
  color: #666;
}

:deep(.ant-statistic-content) {
  font-size: 14px !important;
}

:deep(.ant-table-tbody > tr > td) {
  padding: 8px;
}

:deep(.ant-table-thead > tr > th) {
  padding: 8px;
  background: #fafafa;
  font-weight: 500;
}

:deep(.ant-input-number-group-addon) {
  padding: 0 8px;
  font-size: 12px;
}
</style>