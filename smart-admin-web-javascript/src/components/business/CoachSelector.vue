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
      v-for="coach in coachList"
      :key="coach.coachId"
      :value="coach.coachId"
      :title="`${coach.actualName} - ¥${coach.coachFee}/课时`"
    >
      <div class="coach-option">
        <div class="coach-name">{{ coach.actualName }}</div>
        <div class="coach-info">
          {{ coach.specialties || '专业教练' }}
          <span class="coach-fee">| ¥{{ coach.coachFee }}/课时</span>
        </div>
      </div>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { coachApi } from '/@/api/business/coach/coach-api';

const props = defineProps({
  value: {
    type: [Number, String],
    default: null
  },
  placeholder: {
    type: String,
    default: '请选择教练'
  },
  clubId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:value', 'change']);

const selectedValue = ref(props.value);
const loading = ref(false);
const coachList = ref([]);

// 加载教练列表
const loadCoachList = async () => {
  try {
    loading.value = true;
    const params = {
      pageNum: 1,
      pageSize: 100,
      clubId: props.clubId || 1,
      isValid: 1,
      isDelete: 0,
      disabledFlag: 0
    };
    
    // 模拟API调用，实际需要替换为真实API
    const result = await coachApi.pageQuery(params);
    coachList.value = result.data?.list || [];
  } catch (error) {
    console.error('加载教练列表失败:', error);
    // 提供模拟数据用于测试
    coachList.value = [
      {
        coachId: 1,
        actualName: '张教练',
        specialties: '障碍、盛装舞步',
        coachFee: 200
      },
      {
        coachId: 2,
        actualName: '李教练',
        specialties: '基础骑术、青少年教学',
        coachFee: 180
      }
    ];
  } finally {
    loading.value = false;
  }
};

// 搜索过滤
const filterOption = (input, option) => {
  const coach = coachList.value.find(c => c.coachId === option.value);
  if (!coach) return false;
  
  const searchText = input.toLowerCase();
  return coach.actualName.toLowerCase().includes(searchText) ||
         (coach.specialties && coach.specialties.toLowerCase().includes(searchText));
};

// 选择变更处理
const handleChange = (value) => {
  const selectedCoach = coachList.value.find(c => c.coachId === value);
  emit('update:value', value);
  emit('change', value, selectedCoach);
};

// 监听外部值变化
watch(() => props.value, (newValue) => {
  selectedValue.value = newValue;
});

// 组件挂载时加载数据
onMounted(() => {
  loadCoachList();
});
</script>

<style scoped>
.coach-option {
  padding: 4px 0;
}

.coach-name {
  font-weight: 500;
  color: #262626;
}

.coach-info {
  font-size: 12px;
  color: #8c8c8c;
  margin-top: 2px;
}

.coach-fee {
  color: #52c41a;
  font-weight: 500;
}
</style>