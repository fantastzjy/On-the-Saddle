<template>
  <a-select
    v-model:value="selectedValue"
    :loading="loading"
    show-search
    :placeholder="placeholder"
    allow-clear
    :filter-option="filterOption"
    @change="handleChange"
    @search="handleSearch"
  >
    <a-select-option
      v-for="member in memberList"
      :key="member.memberId"
      :value="member.memberId"
      :title="`${member.actualName} - ${member.phone}`"
    >
      <div class="member-option">
        <div class="member-name">{{ member.actualName }}</div>
        <div class="member-info">{{ member.phone }} | {{ member.memberNo }}</div>
      </div>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch } from 'vue';
import { memberApi } from '/@/api/business/member-api';

const props = defineProps({
  value: {
    type: [Number, String],
    default: null
  },
  placeholder: {
    type: String,
    default: '请选择会员'
  },
  clubId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:value', 'change']);

const selectedValue = ref(props.value);
const loading = ref(false);
const memberList = ref([]);
const searchKeyword = ref('');

// 加载会员列表
const loadMemberList = async (keyword = '') => {
  try {
    loading.value = true;
    const params = {
      pageNum: 1,
      pageSize: 50,
      keywords: keyword,
      clubId: props.clubId,
      registrationStatus: 1 // 只显示已注册会员
    };
    
    // 调用真实API
    const result = await memberApi.pageQuery(params);
    memberList.value = result.data?.list || [];
  } catch (error) {
    console.error('加载会员列表失败:', error);
    memberList.value = [];
  } finally {
    loading.value = false;
  }
};

// 搜索过滤
const filterOption = (input, option) => {
  const member = memberList.value.find(m => m.memberId === option.value);
  if (!member) return false;
  
  const searchText = input.toLowerCase();
  return member.actualName.toLowerCase().includes(searchText) ||
         member.phone.includes(searchText) ||
         member.memberNo.toLowerCase().includes(searchText);
};

// 搜索处理
const handleSearch = (keyword) => {
  searchKeyword.value = keyword;
  if (keyword.length >= 2) {
    loadMemberList(keyword);
  }
};

// 选择变更处理
const handleChange = (value) => {
  const selectedMember = memberList.value.find(m => m.memberId === value);
  emit('update:value', value);
  emit('change', value, selectedMember);
};

// 监听外部值变化
watch(() => props.value, (newValue) => {
  selectedValue.value = newValue;
});

// 组件挂载时加载数据
loadMemberList();
</script>

<style scoped>
.member-option {
  padding: 4px 0;
}

.member-name {
  font-weight: 500;
  color: #262626;
}

.member-info {
  font-size: 12px;
  color: #8c8c8c;
  margin-top: 2px;
}
</style>