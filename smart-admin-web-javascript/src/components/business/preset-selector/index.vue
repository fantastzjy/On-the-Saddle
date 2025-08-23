<!--
  * 预设选择器组件
  * 支持单选/多选模式，可配置预设选项
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-23
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-select 
    :value="currentValue"
    @change="handleChange"
    :mode="mode"
    :placeholder="placeholder"
    optionFilterProp="title"
    :allowClear="allowClear"
    :style="{ width: '100%' }"
  >
    <a-select-option 
      v-for="item in options" 
      :key="item.value" 
      :value="item.value"
      :title="item.label"
    >
      {{ item.label }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { computed } from 'vue';
import { PRESET_OPTIONS_MAP } from '/@/constants/business/preset-const';

// ======================= Props =======================
const props = defineProps({
  // v-model绑定值
  value: {
    type: [String, Array],
    default: undefined
  },
  // 预设类型
  presetType: {
    type: String,
    required: true,
    validator: (value) => {
      return Object.keys(PRESET_OPTIONS_MAP).includes(value);
    }
  },
  // 选择模式：'multiple'/'single'/undefined
  mode: {
    type: String,
    default: undefined
  },
  // 占位符文字
  placeholder: {
    type: String,
    default: '请选择'
  },
  // 是否允许清空
  allowClear: {
    type: Boolean,
    default: true
  }
});

// ======================= Emits =======================
const emit = defineEmits(['update:value', 'change']);

// ======================= 计算属性 =======================
// 获取预设选项
const options = computed(() => {
  return PRESET_OPTIONS_MAP[props.presetType] || [];
});

// 处理当前值
const currentValue = computed(() => {
  if (props.mode === 'multiple') {
    // 多选模式：确保返回数组
    if (Array.isArray(props.value)) {
      return props.value;
    }
    if (props.value) {
      // 如果是字符串，按逗号分割
      return props.value.split(',').filter(item => item.trim());
    }
    return [];
  } else {
    // 单选模式：返回字符串
    if (Array.isArray(props.value)) {
      return props.value[0] || undefined;
    }
    return props.value;
  }
});

// ======================= 方法 =======================
function handleChange(value) {
  let emitValue = value;
  
  if (props.mode === 'multiple') {
    // 多选模式：确保是数组
    emitValue = Array.isArray(value) ? value : (value ? [value] : []);
  } else {
    // 单选模式：确保是字符串或undefined
    emitValue = Array.isArray(value) ? value[0] : value;
  }
  
  emit('update:value', emitValue);
  emit('change', emitValue);
}
</script>