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
      v-for="horse in horseList"
      :key="horse.horseId"
      :value="horse.horseId"
      :title="`${horse.horseName} - ${horse.horseCode}`"
    >
      <div class="horse-option">
        <div class="horse-name">{{ horse.horseName }}</div>
        <div class="horse-info">
          {{ horse.horseCode }} | {{ getHorseTypeText(horse.horseType) }}
          <span v-if="horse.boardingFee" class="boarding-fee">
            (饲养费: ¥{{ horse.boardingFee }}/月)
          </span>
        </div>
      </div>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { horseApi } from '/@/api/business/horse/horse-api';

const props = defineProps({
  value: {
    type: [Number, String],
    default: null
  },
  placeholder: {
    type: String,
    default: '请选择马匹'
  },
  horseType: {
    type: Number,
    default: null // 1-俱乐部马 2-竞技马房马 3-马主马
  },
  clubId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:value', 'change']);

const selectedValue = ref(props.value);
const loading = ref(false);
const horseList = ref([]);

// 加载马匹列表
const loadHorseList = async () => {
  try {
    loading.value = true;
    const params = {
      pageNum: 1,
      pageSize: 100,
      clubId: props.clubId || 1,
      horseType: props.horseType,
      isValid: 1,
      isDelete: 0
    };
    
    // 模拟API调用，实际需要替换为真实API
    const result = await horseApi.pageQuery(params);
    horseList.value = result.data?.list || [];
  } catch (error) {
    console.error('加载马匹列表失败:', error);
    // 提供模拟数据用于测试
    horseList.value = [
      {
        horseId: 1,
        horseName: '黑骏马',
        horseCode: 'H001',
        horseType: 2,
        boardingFee: 2000
      },
      {
        horseId: 2,
        horseName: '白雪公主',
        horseCode: 'H002',
        horseType: 2,
        boardingFee: 2500
      }
    ];
  } finally {
    loading.value = false;
  }
};

// 获取马匹类型文本
const getHorseTypeText = (type) => {
  const typeMap = {
    1: '俱乐部马',
    2: '竞技马房马',
    3: '马主马'
  };
  return typeMap[type] || '未知类型';
};

// 搜索过滤
const filterOption = (input, option) => {
  const horse = horseList.value.find(h => h.horseId === option.value);
  if (!horse) return false;
  
  const searchText = input.toLowerCase();
  return horse.horseName.toLowerCase().includes(searchText) ||
         horse.horseCode.toLowerCase().includes(searchText);
};

// 选择变更处理
const handleChange = (value) => {
  const selectedHorse = horseList.value.find(h => h.horseId === value);
  emit('update:value', value);
  emit('change', value, selectedHorse);
};

// 监听外部值变化
watch(() => props.value, (newValue) => {
  selectedValue.value = newValue;
});

// 监听horseType变化，重新加载数据
watch(() => props.horseType, () => {
  loadHorseList();
});

// 组件挂载时加载数据
onMounted(() => {
  loadHorseList();
});
</script>

<style scoped>
.horse-option {
  padding: 4px 0;
}

.horse-name {
  font-weight: 500;
  color: #262626;
}

.horse-info {
  font-size: 12px;
  color: #8c8c8c;
  margin-top: 2px;
}

.boarding-fee {
  color: #52c41a;
  font-weight: 500;
}
</style>