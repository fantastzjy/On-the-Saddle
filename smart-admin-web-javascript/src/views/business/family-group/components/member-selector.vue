<template>
  <a-select
    v-model:value="selectedMemberId"
    :placeholder="placeholder"
    :disabled="disabled"
    allow-clear
    show-search
    :filter-option="false"
    @search="onSearch"
    @change="onChange"
    :loading="loading"
    :not-found-content="loading ? undefined : notFoundContent"
  >
    <a-select-option
      v-for="member in memberList"
      :key="member.memberId"
      :value="member.memberId"
    >
      <div>
        <div>{{ member.actualName }}</div>
        <div style="font-size: 12px; color: #999;">
          {{ formatMemberInfo(member) }}
        </div>
      </div>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { message } from 'ant-design-vue'
import { memberApi } from '/@/api/business/member-api'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'

// ----------------------- Props -----------------------
const props = defineProps({
  modelValue: {
    type: [Number, String],
    default: null
  },
  mode: {
    type: String,
    default: 'create', // 'create' | 'edit'
    validator: value => ['create', 'edit'].includes(value)
  },
  clubId: {
    type: Number,
    default: null
  },
  familyGroupId: {
    type: Number,
    default: null
  },
  placeholder: {
    type: String,
    default: '请选择会员'
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

// ----------------------- Emits -----------------------
const emits = defineEmits(['update:modelValue', 'change'])

// ----------------------- 响应式数据 -----------------------
const loading = ref(false)
const memberList = ref([])
const searchTimer = ref(null)

const selectedMemberId = computed({
  get: () => props.modelValue,
  set: (value) => emits('update:modelValue', value)
})

const notFoundContent = computed(() => {
  if (props.mode === 'create' && !props.clubId) {
    return '请先选择俱乐部'
  }
  return '输入关键字搜索会员'
})

// ----------------------- 方法 -----------------------
function formatMemberInfo(member) {
  const info = []
  if (member.phone) info.push(`手机:${member.phone}`)
  if (member.memberNo) info.push(`编号:${member.memberNo}`)
  if (member.riderCertNo) info.push(`骑手证:${member.riderCertNo}`)
  return info.join(' | ') || '暂无信息'
}

async function loadMembersByMode(keyword = '') {
  if (props.mode === 'create') {
    // 创建模式：搜索全局会员
    if (!props.clubId) {
      return { success: false, message: '请先选择俱乐部' }
    }
    
    try {
      const res = await memberApi.search({
        keyword: keyword,
        clubId: props.clubId,
        pageNum: 1,
        pageSize: 20
      })
      
      if (res.code === 0 && res.ok) {
        return { success: true, data: res.data.list || [] }
      } else {
        return { success: false, message: res.msg || '搜索失败' }
      }
    } catch (e) {
      return { success: false, message: '搜索失败，请稍后重试' }
    }
  } else {
    // 编辑模式：加载家庭成员
    if (!props.familyGroupId) {
      return { success: false, message: '缺少家庭组ID' }
    }
    
    try {
      const res = await adminFamilyGroupApi.getDetail(props.familyGroupId)
      
      if (res.code === 0 && res.ok && res.data) {
        let members = res.data.members || []
        
        // 如果有关键字，进行本地过滤
        if (keyword.trim()) {
          members = members.filter(member => 
            member.actualName?.includes(keyword) ||
            member.phone?.includes(keyword) ||
            member.memberNo?.includes(keyword) ||
            member.riderCertNo?.includes(keyword)
          )
        }
        
        return { success: true, data: members }
      } else {
        return { success: false, message: res.msg || '获取家庭成员失败' }
      }
    } catch (e) {
      return { success: false, message: '获取家庭成员失败，请稍后重试' }
    }
  }
}

async function onSearch(searchText) {
  // 清除之前的定时器
  if (searchTimer.value) {
    clearTimeout(searchTimer.value)
  }
  
  // 如果搜索文本为空，保持当前列表不变
  if (!searchText || searchText.trim().length < 1) {
    return
  }
  
  // 防抖延迟搜索
  searchTimer.value = setTimeout(async () => {
    await performSearch(searchText.trim())
  }, 300)
}

async function performSearch(keyword) {
  loading.value = true
  
  try {
    const result = await loadMembersByMode(keyword)
    
    if (result.success) {
      memberList.value = result.data
      console.log(`${props.mode}模式搜索到${memberList.value.length}个会员:`, memberList.value)
    } else {
      console.warn('搜索失败:', result.message)
      if (result.message) {
        message.warning(result.message)
      }
    }
  } finally {
    loading.value = false
  }
}

function onChange(value) {
  if (value) {
    const selectedMember = memberList.value.find(m => m.memberId === value)
    if (selectedMember) {
      console.log(`${props.mode}模式选择会员：`, selectedMember.actualName, selectedMember.phone)
    }
  }
  emits('change', value)
}

// 初始化加载（编辑模式时）
async function initLoad() {
  if (props.mode === 'edit' && props.familyGroupId) {
    loading.value = true
    const result = await loadMembersByMode()
    if (result.success) {
      memberList.value = result.data
    }
    loading.value = false
  }
}

// ----------------------- 监听 -----------------------
watch(() => props.familyGroupId, () => {
  if (props.mode === 'edit') {
    initLoad()
  }
}, { immediate: true })

watch(() => props.clubId, () => {
  if (props.mode === 'create') {
    // 俱乐部变化时清空会员列表
    memberList.value = []
  }
})

// ----------------------- 暴露方法 -----------------------
defineExpose({
  clearMembers: () => {
    memberList.value = []
  },
  loadMembers: initLoad
})
</script>