<!--
  * 小组课价格配置组件 - 动态配置版本
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="multi-class-price-config">
    <a-card size="small" title="小组课价格配置">
      <template #extra>
        <a-space>
          <a-button type="link" size="small" @click="resetToBasePrice">
            <RedoOutlined />
            重置价格
          </a-button>
        </a-space>
      </template>
      
      <!-- 配置项提示 -->
      <div class="config-tips" v-if="validationMessages.length > 0">
        <a-alert
          v-for="(msg, index) in validationMessages"
          :key="index"
          :message="msg.text"
          :type="msg.type"
          :show-icon="true"
          :closable="false"
          class="mb-8"
        />
      </div>
      
      <!-- 价格配置表格 -->
      <a-table
        :dataSource="priceConfigs"
        :columns="priceTableColumns"
        :pagination="false"
        size="small"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ column, record, index }">
          <!-- 人数列 -->
          <template v-if="column.dataIndex === 'people'">
            <a-select
              v-model:value="record.people"
              style="width: 100%"
              :options="getAvailablePeopleOptions(record.id)"
              @change="onPeopleChange(record.id, $event)"
            />
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
              @change="onPriceChange"
            >
              <template #addonBefore>¥</template>
              <template #addonAfter>元/人</template>
            </a-input-number>
          </template>
          
          <!-- 折扣列 -->
          <template v-if="column.dataIndex === 'discount'">
            <span :class="['discount-text', getDiscountClass(record.discount)]">
              {{ record.discount || '-' }}
            </span>
          </template>
          
          <!-- 操作列 -->
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a-button 
                type="text" 
                size="small" 
                danger
                :disabled="priceConfigs.length <= 1"
                @click="removeConfig(record.id)"
              >
                <DeleteOutlined />
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
      
      <!-- 添加配置按钮 -->
      <div class="add-config-section">
        <a-button 
          type="dashed" 
          block 
          :disabled="!canAddMore"
          @click="addConfig"
          class="mt-12"
        >
          <PlusOutlined />
          添加人数档位
          <span v-if="!canAddMore" class="text-gray">（已达到最大配置数）</span>
        </a-button>
        
        <div class="add-tips" v-if="canAddMore">
          <span class="text-gray">建议添加：</span>
          <a-space wrap>
            <a-tag 
              v-for="people in suggestedPeople" 
              :key="people"
              color="blue"
              style="cursor: pointer"
              @click="quickAddConfig(people)"
            >
              {{ people }}人课
            </a-tag>
          </a-space>
        </div>
      </div>
      
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
              <li>建议人数越多，单价越优惠（递减定价策略）</li>
              <li>配置的人数档位不能超过"最大人数"设置</li>
            </ul>
          </template>
        </a-alert>
        
        <!-- 价格预览 -->
        <div class="price-preview" v-if="priceConfigs.length > 0">
          <h4>价格预览</h4>
          <a-row :gutter="16">
            <a-col 
              v-for="config in sortedConfigs" 
              :key="config.id" 
              :span="Math.min(24 / priceConfigs.length, 8)"
            >
              <a-statistic
                :title="`${config.people}人课`"
                :value="config.price || 0"
                :precision="2"
                prefix="¥"
                suffix="/人"
                :value-style="{ 
                  fontSize: '14px',
                  color: config.price ? '#1890ff' : '#999'
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
import { RedoOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue';

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
let configIdCounter = 1;

// 为每个价格项动态计算折扣
const addDiscountProperty = (item) => {
  if (!item.hasOwnProperty('discount')) {
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
  }
};

// 初始化为2条配置（2人课、3人课）
const priceConfigs = reactive([]);

// 初始化配置
const initializeConfigs = () => {
  const defaultConfigs = [
    { id: `config_${configIdCounter++}`, people: 2, price: null },
    { id: `config_${configIdCounter++}`, people: 3, price: null }
  ];
  
  defaultConfigs.forEach(config => {
    addDiscountProperty(config);
    priceConfigs.push(config);
  });
};

// 立即初始化
initializeConfigs();

// ======================== 计算属性 ========================
const priceTableColumns = computed(() => [
  {
    title: '人数',
    dataIndex: 'people',
    width: '20%',
    align: 'center'
  },
  {
    title: '价格/人',
    dataIndex: 'price',
    width: '35%',
    align: 'center'
  },
  {
    title: '相比单人课',
    dataIndex: 'discount',
    width: '30%',
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: '15%',
    align: 'center'
  }
]);

// 按人数排序的配置
const sortedConfigs = computed(() => {
  return [...priceConfigs].sort((a, b) => a.people - b.people);
});

// 是否可以添加更多配置
const canAddMore = computed(() => {
  return priceConfigs.length < 9 && getAvailablePeopleNumbers().length > 0;
});

// 建议添加的人数
const suggestedPeople = computed(() => {
  const available = getAvailablePeopleNumbers();
  return available.slice(0, 3); // 显示前3个建议
});

// 验证消息
const validationMessages = computed(() => {
  const messages = [];
  
  // 检查最大人数对应
  const maxConfigPeople = Math.max(...priceConfigs.map(c => c.people));
  if (maxConfigPeople > props.maxStudents) {
    messages.push({
      type: 'warning',
      text: `配置的最高人数(${maxConfigPeople}人)超过了最大人数限制(${props.maxStudents}人)`
    });
  }
  
  // 检查价格合理性
  const sorted = sortedConfigs.value.filter(c => c.price > 0);
  if (sorted.length >= 2) {
    let hasIncreasing = false;
    for (let i = 1; i < sorted.length; i++) {
      if (sorted[i].price > sorted[i-1].price) {
        hasIncreasing = true;
        break;
      }
    }
    if (hasIncreasing) {
      messages.push({
        type: 'info',
        text: '建议人数越多，单价越优惠（递减定价策略）'
      });
    }
  }
  
  // 检查人数连续性
  if (sorted.length >= 2) {
    const peoples = sorted.map(c => c.people);
    let hasGap = false;
    for (let i = 1; i < peoples.length; i++) {
      if (peoples[i] - peoples[i-1] > 1) {
        hasGap = true;
        break;
      }
    }
    if (hasGap) {
      messages.push({
        type: 'info',
        text: '建议配置连续的人数档位，便于客户选择'
      });
    }
  }
  
  return messages;
});

// ======================== 监听器 ========================
watch(() => props.value, (newVal) => {
  parseConfigValue(newVal);
}, { immediate: true });

watch(() => props.maxStudents, () => {
  // 当最大人数变化时，自动调整配置
  priceConfigs.forEach(config => {
    if (config.people > props.maxStudents) {
      // 将超出的人数调整为最大人数
      const availableNumbers = getAvailablePeopleNumbers();
      if (availableNumbers.length > 0) {
        config.people = availableNumbers[0];
      }
    }
  });
}, { immediate: true });

// ======================== 方法 ========================
function parseConfigValue(value) {
  if (!value) {
    resetConfigs();
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
        // 清空现有配置
        priceConfigs.splice(0);
        
        // 根据价格配置重建配置项
        Object.entries(firstCoach.prices).forEach(([people, price]) => {
          const newConfig = {
            id: `config_${configIdCounter++}`,
            people: Number(people),
            price: Number(price)
          };
          
          // 添加折扣属性
          addDiscountProperty(newConfig);
          
          priceConfigs.push(newConfig);
        });
        
        // 确保至少有一个配置项
        if (priceConfigs.length === 0) {
          resetConfigs();
        }
      }
    }
  } catch (e) {
    console.warn('解析小组课价格配置失败:', e);
    resetConfigs();
  }
}

function resetConfigs() {
  priceConfigs.splice(0);
  
  const defaultConfigs = [
    { id: `config_${configIdCounter++}`, people: 2, price: null },
    { id: `config_${configIdCounter++}`, people: 3, price: null }
  ];
  
  defaultConfigs.forEach(config => {
    addDiscountProperty(config);
    priceConfigs.push(config);
  });
}

function getAvailablePeopleNumbers() {
  const usedPeople = priceConfigs.map(c => c.people);
  const available = [];
  
  for (let i = 2; i <= Math.min(props.maxStudents, 10); i++) {
    if (!usedPeople.includes(i)) {
      available.push(i);
    }
  }
  
  return available;
}

function getAvailablePeopleOptions(currentConfigId) {
  const currentConfig = priceConfigs.find(c => c.id === currentConfigId);
  const otherUsedPeople = priceConfigs
    .filter(c => c.id !== currentConfigId)
    .map(c => c.people);
  
  const options = [];
  for (let i = 2; i <= Math.min(props.maxStudents, 10); i++) {
    if (!otherUsedPeople.includes(i)) {
      options.push({
        value: i,
        label: `${i}人课`
      });
    }
  }
  
  return options;
}

function addConfig() {
  const availableNumbers = getAvailablePeopleNumbers();
  if (availableNumbers.length === 0) return;
  
  const nextPeople = availableNumbers[0];
  const suggestedPrice = calculateSuggestedPrice(nextPeople);
  
  const newConfig = {
    id: `config_${configIdCounter++}`,
    people: nextPeople,
    price: suggestedPrice
  };
  
  // 添加折扣属性
  addDiscountProperty(newConfig);
  
  priceConfigs.push(newConfig);
  
  emitConfigChange();
}

function quickAddConfig(people) {
  const suggestedPrice = calculateSuggestedPrice(people);
  
  const newConfig = {
    id: `config_${configIdCounter++}`,
    people: people,
    price: suggestedPrice
  };
  
  // 添加折扣属性
  addDiscountProperty(newConfig);
  
  priceConfigs.push(newConfig);
  
  emitConfigChange();
}

function removeConfig(configId) {
  if (priceConfigs.length <= 1) return;
  
  const index = priceConfigs.findIndex(c => c.id === configId);
  if (index > -1) {
    priceConfigs.splice(index, 1);
    emitConfigChange();
  }
}

function onPeopleChange(configId, newPeople) {
  const config = priceConfigs.find(c => c.id === configId);
  if (config) {
    config.people = newPeople;
    // 根据新人数建议价格
    if (!config.price) {
      config.price = calculateSuggestedPrice(newPeople);
    }
    emitConfigChange();
  }
}

function onPriceChange() {
  emitConfigChange();
}

function calculateSuggestedPrice(people) {
  if (!props.basePrice) return null;
  
  // 递减定价策略：人数越多，优惠越大
  // 2人课：95%，3人课：90%，4人课：85%，5人课：80%，以此类推
  const discountRate = Math.max(0.6, 1.0 - (people - 2) * 0.05);
  return Number((props.basePrice * discountRate).toFixed(2));
}

function resetToBasePrice() {
  priceConfigs.forEach(config => {
    config.price = calculateSuggestedPrice(config.people);
  });
  emitConfigChange();
}

function emitConfigChange() {
  // 构建符合数据库格式的配置
  const prices = {};
  priceConfigs.forEach(config => {
    if (config.price !== null && config.price !== undefined) {
      prices[config.people.toString()] = config.price;
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
  if (!discount || discount === '-') return '';
  if (typeof discount === 'string' && discount.includes('优惠')) return 'discount-good';
  if (typeof discount === 'string' && discount.includes('加价')) return 'discount-bad';
  return 'discount-normal';
}

// ======================== 初始化 ========================
onMounted(() => {
  console.log('MultiClassPriceConfig mounted, basePrice:', props.basePrice, 'value:', props.value);
  
  // 如果有基础价格但没有配置，自动设置默认价格
  if (props.basePrice > 0 && !props.value) {
    setTimeout(() => {
      console.log('Auto setting prices...');
      resetToBasePrice();
    }, 100);
  }
});
</script>

<style scoped>
.multi-class-price-config {
  width: 100%;
}

.config-tips {
  margin-bottom: 16px;
}

.mb-8 {
  margin-bottom: 8px;
}

.mt-12 {
  margin-top: 12px;
}

.add-config-section {
  margin-top: 16px;
}

.add-tips {
  margin-top: 8px;
  padding: 8px 12px;
  background: #fafafa;
  border-radius: 4px;
  font-size: 12px;
}

.text-gray {
  color: #999;
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

:deep(.ant-btn-dashed) {
  border-color: #1890ff;
  color: #1890ff;
}

:deep(.ant-btn-dashed:hover) {
  border-color: #40a9ff;
  color: #40a9ff;
}
</style>