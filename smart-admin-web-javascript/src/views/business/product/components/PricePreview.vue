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
        <!-- 单人课显示价格构成 -->
        <template v-if="isSingleClass">
          <a-col :span="8">
            <a-statistic
              title="教练费"
              :value="props.dynamicConfig.coachFee || 0"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#666' }"
            />
          </a-col>
          <a-col :span="8">
            <a-statistic
              title="马匹费"
              :value="props.dynamicConfig.horseFee || 0"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#666' }"
            />
          </a-col>
          <a-col :span="8">
            <a-statistic
              title="总价"
              :value="calculateBasePrice()"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#1890ff', fontWeight: 'bold' }"
            />
          </a-col>
        </template>
        
        <!-- 多人课显示价格区间 -->
        <template v-else-if="isMultiClass">
          <a-col :span="12">
            <a-statistic
              title="基础价格"
              :value="calculateBasePrice()"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#666' }"
            />
          </a-col>
          <a-col :span="12">
            <a-statistic
              title="价格区间"
              :value="getPriceRange()"
              prefix="¥"
              :value-style="{ color: '#52c41a', fontSize: '16px', fontWeight: 'bold' }"
            />
          </a-col>
        </template>
        
        <!-- 体验课类型显示基础单价 -->
        <template v-else-if="isExperienceClass">
          <a-col :span="24">
            <a-statistic
              title="基础单价"
              :value="getProductPrice()"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#1890ff', fontWeight: 'bold' }"
            />
          </a-col>
        </template>

        <!-- 其他类型商品 -->
        <template v-else>
          <a-col :span="24">
            <a-statistic
              title="商品价格"
              :value="getProductPrice()"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#1890ff', fontWeight: 'bold' }"
            />
          </a-col>
        </template>
      </a-row>

      <a-divider />

      <!-- 详细价格计算 -->
      <div v-if="showPriceDetails">
        <h4>{{ getDetailsTitle() }}</h4>
        <template v-if="isSingleClass">
          <!-- 单人课显示价格构成明细 -->
          <a-descriptions size="small" :column="1" bordered>
            <a-descriptions-item label="教练费">
              <span style="color: #666;">¥{{ Number(props.dynamicConfig.coachFee || 0).toFixed(2) }}</span>
            </a-descriptions-item>
            <a-descriptions-item label="马匹费">
              <span style="color: #666;">¥{{ Number(props.dynamicConfig.horseFee || 0).toFixed(2) }}</span>
            </a-descriptions-item>
            <a-descriptions-item label="总价">
              <span style="color: #1890ff; font-weight: bold;">¥{{ calculateBasePrice().toFixed(2) }}</span>
            </a-descriptions-item>
          </a-descriptions>
        </template>
        
        <template v-else-if="isMultiClass">
          <!-- 多人课显示价格区间明细 -->
          <a-descriptions size="small" :column="1" bordered>
            <a-descriptions-item label="基础价格">
              <span style="color: #666;">教练费(¥{{ Number(props.dynamicConfig.coachFee || 0).toFixed(2) }}) + 马匹费(¥{{ Number(props.dynamicConfig.horseFee || 0).toFixed(2) }}) = ¥{{ calculateBasePrice().toFixed(2) }}</span>
            </a-descriptions-item>
            <a-descriptions-item label="多人课价格配置">
              <div v-if="getMultiPriceDetails().length > 0">
                <div v-for="detail in getMultiPriceDetails()" :key="detail.people" style="margin-bottom: 4px;">
                  {{ detail.people }}人：<span style="color: #1890ff;">¥{{ detail.price }}/人</span>
                </div>
              </div>
              <span v-else style="color: #999;">未配置多人课价格</span>
            </a-descriptions-item>
            <a-descriptions-item label="价格区间">
              <span style="color: #52c41a; font-weight: bold;">{{ getPriceRange() }}</span>
            </a-descriptions-item>
          </a-descriptions>
        </template>
        
        <template v-else-if="isExperienceClass">
          <!-- 体验课显示基础价格明细 -->
          <a-descriptions size="small" :column="1" bordered>
            <a-descriptions-item label="基础单价">
              <span style="color: #1890ff; font-weight: bold;">¥{{ Number(props.dynamicConfig.price || 0).toFixed(2) }}</span>
            </a-descriptions-item>
            <a-descriptions-item label="课时时长">
              <span style="color: #666;">{{ props.dynamicConfig.durationMinutes || 0 }}分钟 / {{ props.dynamicConfig.durationPeriods || 0 }}鞍时</span>
            </a-descriptions-item>
            <a-descriptions-item label="最大人数">
              <span style="color: #666;">{{ props.dynamicConfig.maxStudents || 0 }}人</span>
            </a-descriptions-item>
          </a-descriptions>
        </template>
        
        <template v-else>
          <!-- 其他类型商品显示原有明细 -->
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
        </template>
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
const isSingleClass = computed(() => {
  return props.productData.productType === 1 && props.dynamicConfig.classType === 1;
});

const isMultiClass = computed(() => {
  return props.productData.productType === 1 && props.dynamicConfig.classType === 2;
});

const isExperienceClass = computed(() => {
  return props.productData.productType === 4;
});

const showPriceDetails = computed(() => {
  return props.productData.productType && (
    (props.productData.productType === 1 && props.dynamicConfig.coachFee && props.dynamicConfig.horseFee) ||
    (props.productData.productType === 2 && props.dynamicConfig.price) ||
    (props.productData.productType === 3 && props.dynamicConfig.price) ||
    (props.productData.productType === 4 && props.dynamicConfig.price) ||
    (priceDetails.value && priceDetails.value.length > 0)
  );
});

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

// ======================== 价格计算方法 ========================
function calculateBasePrice() {
  const coachFee = Number(props.dynamicConfig.coachFee || 0);
  const horseFee = Number(props.dynamicConfig.horseFee || 0);
  return coachFee + horseFee;
}

function getPriceRange() {
  if (!isMultiClass.value) return '';
  
  const multiPriceDetails = getMultiPriceDetails();
  if (multiPriceDetails.length === 0) {
    const basePrice = calculateBasePrice();
    return `¥${basePrice.toFixed(2)}/人`;
  }
  
  const prices = multiPriceDetails.map(detail => Number(detail.price));
  const minPrice = Math.min(...prices);
  const maxPrice = Math.max(...prices);
  
  if (minPrice === maxPrice) {
    return `¥${minPrice.toFixed(2)}/人`;
  }
  
  return `¥${minPrice.toFixed(2)} - ¥${maxPrice.toFixed(2)}/人`;
}

function getMultiPriceDetails() {
  if (!props.dynamicConfig.multiPriceConfig) return [];
  
  try {
    let config = props.dynamicConfig.multiPriceConfig;
    if (typeof config === 'string') {
      config = JSON.parse(config);
    }
    
    const details = [];
    if (config && config.coaches && Array.isArray(config.coaches)) {
      // 取第一个教练的价格配置作为示例
      const firstCoach = config.coaches[0];
      if (firstCoach && firstCoach.prices) {
        Object.entries(firstCoach.prices).forEach(([people, price]) => {
          details.push({
            people: Number(people),
            price: Number(price).toFixed(2)
          });
        });
      }
    }
    
    return details.sort((a, b) => a.people - b.people);
  } catch (e) {
    console.warn('解析多人课价格配置失败:', e);
    return [];
  }
}

function getProductPrice() {
  if (props.productData.productType === 2) {
    // 课时包价格
    return Number(props.dynamicConfig.price || 0);
  } else if (props.productData.productType === 3) {
    // 活动价格
    return Number(props.dynamicConfig.price || 0);
  } else if (props.productData.productType === 4) {
    // 体验课价格
    return Number(props.dynamicConfig.price || 0);
  }
  return 0;
}

function getDetailsTitle() {
  if (isSingleClass.value) {
    return '价格构成';
  } else if (isMultiClass.value) {
    return '价格区间详情';
  } else if (isExperienceClass.value) {
    return '体验课定价';
  }
  return '价格明细';
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