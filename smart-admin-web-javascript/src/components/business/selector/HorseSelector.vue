<template>
  <a-select
    v-model:value="selectedValue"
    :placeholder="placeholder"
    :disabled="disabled"
    :multiple="multiple"
    :allow-clear="allowClear"
    :show-search="showSearch"
    :loading="loading"
    :size="size"
    :filter-option="false"
    @search="onSearch"
    @focus="onFocus"
    @change="onChange"
    @clear="onClear"
  >
    <a-select-option 
      v-for="item in options" 
      :key="item.horseId" 
      :value="item.horseId"
      :label="item.displayLabel"
    >
      {{ item.displayLabel }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { debounce } from 'lodash-es'
import { selectorApi } from '/@/api/business/selector-api'
import { smartSentry } from '/@/lib/smart-sentry'

const props = defineProps({
  // 基础属性
  value: [String, Number, Array],
  placeholder: { type: String, default: '请选择马匹' },
  disabled: { type: Boolean, default: false },
  multiple: { type: Boolean, default: false },
  allowClear: { type: Boolean, default: true },
  
  // 数据加载
  lazy: { type: Boolean, default: true },
  autoLoad: { type: Boolean, default: false },
  
  // 显示配置
  showSearch: { type: Boolean, default: true },
  size: { type: String, default: 'middle' },
  
  // 筛选参数
  horseType: { type: [Number, Array] },
  healthStatus: { type: [Number, Array] },
  workStatus: { type: [Number, Array] },
  
  // 自定义
  customParams: { type: Object, default: () => ({}) },
})

const emit = defineEmits(['update:value', 'change'])

const selectedValue = ref()
const options = ref([])
const loading = ref(false)
const hasLoaded = ref(false)

// 监听value变化
watch(() => props.value, (newVal) => {
  selectedValue.value = newVal
}, { immediate: true })

// 监听selectedValue变化
watch(selectedValue, (newVal) => {
  emit('update:value', newVal)
})

// 自动加载
if (props.autoLoad || !props.lazy) {
  loadOptions()
}

// 搜索防抖
const debouncedSearch = debounce(async (keywords) => {
  await loadOptions({ keywords })
}, 300)

async function loadOptions(searchParams = {}) {
  if (loading.value) return
  
  try {
    loading.value = true
    
    const params = {
      keywords: searchParams.keywords || '',
      horseType: props.horseType,
      healthStatus: props.healthStatus,
      workStatus: props.workStatus,
      pageSize: 50,
      pageNum: 1,
      ...props.customParams
    }
    
    // 移除空值参数
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === undefined || params[key] === '') {
        delete params[key]
      }
    })
    
    const res = await selectorApi.queryHorseSelector(params)
    if (res.code === 0 && res.data) {
      options.value = res.data
      hasLoaded.value = true
    }
  } catch (error) {
    smartSentry.captureError(error)
  } finally {
    loading.value = false
  }
}

function onFocus() {
  if (props.lazy && !hasLoaded.value) {
    loadOptions()
  }
}

function onSearch(keywords) {
  if (props.showSearch) {
    debouncedSearch(keywords)
  }
}

function onChange(value, option) {
  emit('change', value, option)
}

function onClear() {
  emit('change', undefined, null)
}

// 刷新数据
function refresh() {
  hasLoaded.value = false
  loadOptions()
}

defineExpose({
  refresh,
  loadOptions
})
</script>