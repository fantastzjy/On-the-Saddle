<template>
  <a-select
    v-model:value="selectedValue"
    :loading="loading"
    show-search
    :placeholder="placeholder"
    allow-clear
    :filter-option="filterOption"
    @change="handleChange"
  >
    <a-select-option
      v-for="product in productList"
      :key="product.productId"
      :value="product.productId"
      :title="product.productName"
    >
      <div class="product-option">
        <div class="product-name">{{ product.productName }}</div>
        <div class="product-info">
          {{ getProductTypeText(product.productType) }}
          <span v-if="product.basePrice" class="product-price">
            | ¥{{ product.basePrice }}
          </span>
        </div>
      </div>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { productApi } from '/@/api/business/product/product-api';

const props = defineProps({
  value: {
    type: [Number, String],
    default: null
  },
  placeholder: {
    type: String,
    default: '请选择商品'
  },
  productType: {
    type: Number,
    required: true
  },
  memberId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:value', 'change']);

const selectedValue = ref(props.value);
const loading = ref(false);
const productList = ref([]);

// 加载商品列表
const loadProductList = async () => {
  try {
    loading.value = true;
    const params = {
      pageNum: 1,
      pageSize: 100,
      productType: props.productType,
      isValid: 1,
      isDelete: 0
    };
    
    // 模拟API调用，实际需要替换为真实API
    const result = await productApi.queryProductList(params);
    productList.value = result.data?.list || [];
  } catch (error) {
    console.error('加载商品列表失败:', error);
    // 提供模拟数据用于测试
    productList.value = [
      {
        productId: 1,
        productName: '单人基础课程',
        productType: props.productType,
        basePrice: 300
      },
      {
        productId: 2,
        productName: '多人体验课程',
        productType: props.productType,
        basePrice: 200
      }
    ];
  } finally {
    loading.value = false;
  }
};

// 获取商品类型文本
const getProductTypeText = (type) => {
  const typeMap = {
    1: '课程',
    2: '课时包',
    3: '活动',
    4: '体验课',
    5: '理论课'
  };
  return typeMap[type] || '未知类型';
};

// 搜索过滤
const filterOption = (input, option) => {
  const product = productList.value.find(p => p.productId === option.value);
  if (!product) return false;
  
  const searchText = input.toLowerCase();
  return product.productName.toLowerCase().includes(searchText);
};

// 选择变更处理
const handleChange = (value) => {
  const selectedProduct = productList.value.find(p => p.productId === value);
  emit('update:value', value);
  emit('change', selectedProduct);
};

// 监听外部值变化
watch(() => props.value, (newValue) => {
  selectedValue.value = newValue;
});

// 监听商品类型变化
watch(() => props.productType, () => {
  selectedValue.value = null;
  emit('update:value', null);
  loadProductList();
});

// 组件挂载时加载数据
onMounted(() => {
  loadProductList();
});
</script>

<style scoped>
.product-option {
  padding: 4px 0;
}

.product-name {
  font-weight: 500;
  color: #262626;
}

.product-info {
  font-size: 12px;
  color: #8c8c8c;
  margin-top: 2px;
}

.product-price {
  color: #52c41a;
  font-weight: 500;
}
</style>