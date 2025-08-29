<!--
  * 动态表单渲染器组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="dynamic-form-renderer">
    <a-spin :spinning="loading" tip="加载表单配置中...">
      <div v-if="!formConfig || formConfig.length === 0" class="empty-config">
        <a-empty description="暂无配置项" />
      </div>
      
      <a-form v-else :model="formData" :rules="dynamicRules" ref="formRef" v-bind="formProps">
        <!-- 将字段按行分组 -->
        <template v-for="(rowFields, rowIndex) in groupedFields" :key="rowIndex">
          <a-row :gutter="16">
            <template v-for="field in rowFields" :key="field.key">
              <!-- 分组标题 -->
              <a-col :span="24" v-if="field.type === 'group'">
                <a-divider orientation="left">
                  <span class="group-title">{{ field.label }}</span>
                </a-divider>
              </a-col>
              
              <!-- 表单项 -->
              <a-col :span="getFieldSpan(field)" v-else>
                <a-form-item 
                  :label="field.label"
                  :name="field.key"
                  :required="field.required"
                  :help="field.help"
                  :extra="field.extra"
                >
            <!-- 输入框 -->
            <a-input
              v-if="field.type === FORM_FIELD_TYPE_ENUM.INPUT"
              v-model:value="formData[field.key]"
              :placeholder="field.placeholder"
              :maxlength="field.maxlength"
              :disabled="field.disabled"
              :allowClear="field.allowClear !== false"
              :class="getFieldClass(field)"
            />
            
            <!-- 数字输入框 -->
            <a-input-number
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.NUMBER"
              v-model:value="formData[field.key]"
              :placeholder="field.placeholder"
              :min="field.min"
              :max="field.max"
              :precision="field.precision"
              :step="field.step || 1"
              :disabled="field.disabled"
              :formatter="field.formatter"
              :parser="field.parser"
              :class="getFieldClass(field)"
            />
            
            <!-- 选择器 -->
            <a-select
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.SELECT"
              v-model:value="formData[field.key]"
              :placeholder="field.placeholder"
              :mode="field.mode"
              :disabled="field.disabled"
              :allowClear="field.allowClear !== false"
              :showSearch="field.showSearch"
              :filterOption="field.filterOption"
              :class="getFieldClass(field)"
            >
              <a-select-option 
                v-for="option in field.options" 
                :key="option.value" 
                :value="option.value"
                :disabled="option.disabled"
              >
                {{ option.label }}
              </a-select-option>
            </a-select>
            
            <!-- 多行文本框 -->
            <a-textarea
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.TEXTAREA"
              v-model:value="formData[field.key]"
              :placeholder="field.placeholder"
              :rows="field.rows || 3"
              :maxlength="field.maxlength"
              :disabled="field.disabled"
              :showCount="field.showCount"
            />
            
            <!-- 日期时间选择器 -->
            <a-date-picker
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.DATETIME"
              v-model:value="formData[field.key]"
              :placeholder="field.placeholder"
              :format="field.format || 'YYYY-MM-DD HH:mm'"
              :valueFormat="field.valueFormat || 'YYYY-MM-DD HH:mm:ss'"
              :showTime="field.showTime !== false"
              :disabled="field.disabled"
              :allowClear="field.allowClear !== false"
              :class="getFieldClass(field)"
            />
            
            <!-- 开关 -->
            <a-switch
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.SWITCH"
              v-model:checked="formData[field.key]"
              :checkedChildren="field.checkedChildren"
              :unCheckedChildren="field.unCheckedChildren"
              :disabled="field.disabled"
            />
            
            <!-- 多选框组 -->
            <a-checkbox-group
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.CHECKBOX"
              v-model:value="formData[field.key]"
              :disabled="field.disabled"
            >
              <a-checkbox 
                v-for="option in field.options" 
                :key="option.value" 
                :value="option.value"
                :disabled="option.disabled"
              >
                {{ option.label }}
              </a-checkbox>
            </a-checkbox-group>
            
            <!-- 单选框组 -->
            <a-radio-group
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.RADIO"
              v-model:value="formData[field.key]"
              :disabled="field.disabled"
            >
              <a-radio 
                v-for="option in field.options" 
                :key="option.value" 
                :value="option.value"
                :disabled="option.disabled"
              >
                {{ option.label }}
              </a-radio>
            </a-radio-group>
            
            <!-- 文件上传 -->
            <FileUpload
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.UPLOAD"
              v-model:value="formData[field.key]"
              :max-count="field.maxCount || 1"
              :max-size="field.maxSize || 5"
              :accepted-types="field.acceptedTypes"
              :list-type="field.listType || 'text'"
              :disabled="field.disabled"
            />
            
            <!-- 多人课价格配置 -->
            <MultiClassPriceConfig
              v-else-if="field.type === 'multi-price-config'"
              v-model:value="formData[field.key]"
              :base-price="getBasePrice()"
              :max-students="formData.maxStudents || 5"
              :disabled="field.disabled"
            />
            
            <!-- 活动详情图片上传 -->
            <ActivityDetailImageUpload
              v-else-if="field.type === FORM_FIELD_TYPE_ENUM.ACTIVITY_DETAIL_IMAGES || 
                        (field.type === FORM_FIELD_TYPE_ENUM.UPLOAD && field.key === 'detailImages')"
              v-model:value="formData[field.key]"
              :max-count="field.maxCount || 9"
              :max-size="field.maxSize || 2"
              :accept-types="field.acceptTypes || '.jpg,.jpeg,.png,.gif'"
              :show-tips="field.showTips !== false"
              :disabled="field.disabled"
            />
            
            <!-- 自定义插槽 -->
            <slot 
              v-else-if="field.type === 'slot'" 
              :name="field.key" 
              :field="field" 
              :value="formData[field.key]"
              :onChange="(value) => onFieldChange(field.key, value)"
            />
            
            <!-- 未知类型 -->
            <div v-else class="unknown-field-type">
              <a-alert 
                :message="`未知字段类型: ${field.type}`" 
                type="warning" 
                show-icon 
              />
            </div>
                </a-form-item>
              </a-col>
            </template>
          </a-row>
        </template>
      </a-form>
    </a-spin>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue';
import { FORM_FIELD_TYPE_ENUM } from '/@/constants/business/product/product-const';
import FileUpload from '/@/components/support/file-upload/index.vue';
import MultiClassPriceConfig from './MultiClassPriceConfig.vue';
import ActivityDetailImageUpload from './ActivityDetailImageUpload.vue';

// ======================== Props & Emits ========================
const props = defineProps({
  value: {
    type: Object,
    default: () => ({})
  },
  formConfig: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  formProps: {
    type: Object,
    default: () => ({
      labelCol: { span: 6 },
      wrapperCol: { span: 18 }
    })
  }
});

const emit = defineEmits(['update:value', 'change', 'validate']);

// ======================== 响应式数据 ========================
const formRef = ref();
const formData = reactive({});
const isInitializing = ref(false); // 标记是否正在初始化

// ======================== 计算属性 ========================
// 将字段按行分组（根据span值）
const groupedFields = computed(() => {
  if (!props.formConfig || props.formConfig.length === 0) {
    return [];
  }
  
  const rows = [];
  let currentRow = [];
  let currentRowSpan = 0;
  
  props.formConfig.forEach(field => {
    const fieldSpan = getFieldSpan(field); // 使用getFieldSpan计算实际span值
    
    // 如果当前行加上新字段会超过24，或者是分组类型，则开始新行
    if (currentRowSpan + fieldSpan > 24 || field.type === 'group') {
      if (currentRow.length > 0) {
        rows.push(currentRow);
        currentRow = [];
        currentRowSpan = 0;
      }
    }
    
    currentRow.push(field);
    currentRowSpan += fieldSpan;
    
    // 如果当前行已满或者是分组类型，则结束当前行
    if (currentRowSpan >= 24 || field.type === 'group') {
      rows.push(currentRow);
      currentRow = [];
      currentRowSpan = 0;
    }
  });
  
  // 添加最后一行
  if (currentRow.length > 0) {
    rows.push(currentRow);
  }
  
  return rows;
});

const dynamicRules = computed(() => {
  const rules = {};
  
  if (!props.formConfig) return rules;
  
  props.formConfig.forEach(field => {
    if (field.type === 'group') return;
    
    const fieldRules = [];
    
    // 必填校验
    if (field.required) {
      fieldRules.push({
        required: true,
        message: field.requiredMessage || `请${getFieldActionText(field.type)}${field.label}`,
        trigger: getFieldTrigger(field.type)
      });
    }
    
    // 长度校验
    if (field.minlength || field.maxlength) {
      fieldRules.push({
        min: field.minlength,
        max: field.maxlength,
        message: getLengthMessage(field.label, field.minlength, field.maxlength),
        trigger: 'blur'
      });
    }
    
    // 数值范围校验
    if (field.type === FORM_FIELD_TYPE_ENUM.NUMBER && (field.min !== undefined || field.max !== undefined)) {
      fieldRules.push({
        type: 'number',
        min: field.min,
        max: field.max,
        message: getRangeMessage(field.label, field.min, field.max),
        trigger: 'blur'
      });
    }
    
    // 自定义校验规则
    if (field.validator) {
      fieldRules.push({
        validator: field.validator,
        trigger: getFieldTrigger(field.type)
      });
    }
    
    // 正则校验
    if (field.pattern) {
      fieldRules.push({
        pattern: new RegExp(field.pattern),
        message: field.patternMessage || `${field.label}格式不正确`,
        trigger: 'blur'
      });
    }
    
    if (fieldRules.length > 0) {
      rules[field.key] = fieldRules;
    }
  });
  
  return rules;
});

// ======================== 监听器 ========================
watch(() => props.value, (newVal) => {
  if (newVal && typeof newVal === 'object') {
    Object.assign(formData, newVal);
  }
}, { immediate: true, deep: true });

watch(() => props.formConfig, (newConfig) => {
  if (newConfig && newConfig.length > 0) {
    isInitializing.value = true;
    initFormData();
    // 初始化完成后，延迟验证确保DOM完全更新
    nextTick(() => {
      setTimeout(() => {
        isInitializing.value = false;
        // 配置切换完成后，重新验证表单
        setTimeout(() => {
          checkFormValid();
        }, 50);
      }, 100);
    });
  }
}, { immediate: true });

watch(formData, (newVal) => {
  emit('update:value', { ...newVal });
  emit('change', { ...newVal });
  
  // 只有在非初始化状态下才进行验证
  if (!isInitializing.value) {
    setTimeout(() => {
      checkFormValid();
    }, 100);
  }
}, { deep: true });

// ======================== 方法 ========================
function initFormData() {
  if (!props.formConfig) return;
  
  props.formConfig.forEach(field => {
    if (field.type === 'group') return;
    
    // 如果表单数据中没有这个字段，设置默认值
    if (!(field.key in formData)) {
      formData[field.key] = getFieldDefaultValue(field);
    }
  });
}

function getFieldDefaultValue(field) {
  if (field.defaultValue !== undefined) {
    return field.defaultValue;
  }
  
  switch (field.type) {
    case FORM_FIELD_TYPE_ENUM.INPUT:
    case FORM_FIELD_TYPE_ENUM.TEXTAREA:
      return '';
    case FORM_FIELD_TYPE_ENUM.NUMBER:
      return field.min || 0;
    case FORM_FIELD_TYPE_ENUM.SELECT:
      return field.mode === 'multiple' ? [] : null;
    case FORM_FIELD_TYPE_ENUM.SWITCH:
      return false;
    case FORM_FIELD_TYPE_ENUM.CHECKBOX:
      return [];
    case FORM_FIELD_TYPE_ENUM.RADIO:
      return null;
    case FORM_FIELD_TYPE_ENUM.UPLOAD:
      return [];
    case FORM_FIELD_TYPE_ENUM.DATETIME:
      return null;
    case FORM_FIELD_TYPE_ENUM.ACTIVITY_DETAIL_IMAGES:
      return [];
    default:
      return null;
  }
}

function onFieldChange(key, value) {
  // 兼容自定义插槽的 onChange 回调
  formData[key] = value;
  
  // 触发表单验证
  nextTick(() => {
    validateField(key);
  });
}

function validateField(key) {
  if (formRef.value) {
    formRef.value.validateFields([key]).then(() => {
      checkFormValid();
    }).catch((error) => {
      console.warn(`字段 ${key} 验证失败:`, error.message || error);
      checkFormValid();
    });
  }
}

async function checkFormValid() {
  try {
    if (formRef.value && props.config && props.config.length > 0) {
      await formRef.value.validate();
      emit('validate', true);
    } else {
      emit('validate', false);
    }
  } catch (error) {
    // 配置切换时可能出现验证错误，这是正常现象
    console.warn('表单验证出现问题，可能是配置切换导致的:', error.message || error);
    emit('validate', false);
  }
}

function getFieldActionText(type) {
  const actionMap = {
    [FORM_FIELD_TYPE_ENUM.INPUT]: '输入',
    [FORM_FIELD_TYPE_ENUM.NUMBER]: '输入',
    [FORM_FIELD_TYPE_ENUM.TEXTAREA]: '输入',
    [FORM_FIELD_TYPE_ENUM.SELECT]: '选择',
    [FORM_FIELD_TYPE_ENUM.DATETIME]: '选择',
    [FORM_FIELD_TYPE_ENUM.CHECKBOX]: '选择',
    [FORM_FIELD_TYPE_ENUM.RADIO]: '选择',
    [FORM_FIELD_TYPE_ENUM.UPLOAD]: '上传',
    [FORM_FIELD_TYPE_ENUM.ACTIVITY_DETAIL_IMAGES]: '上传'
  };
  return actionMap[type] || '填写';
}

function getFieldTrigger(type) {
  const triggerMap = {
    [FORM_FIELD_TYPE_ENUM.INPUT]: 'blur',
    [FORM_FIELD_TYPE_ENUM.NUMBER]: 'blur',
    [FORM_FIELD_TYPE_ENUM.TEXTAREA]: 'blur',
    [FORM_FIELD_TYPE_ENUM.SELECT]: 'change',
    [FORM_FIELD_TYPE_ENUM.DATETIME]: 'change',
    [FORM_FIELD_TYPE_ENUM.SWITCH]: 'change',
    [FORM_FIELD_TYPE_ENUM.CHECKBOX]: 'change',
    [FORM_FIELD_TYPE_ENUM.RADIO]: 'change',
    [FORM_FIELD_TYPE_ENUM.UPLOAD]: 'change',
    [FORM_FIELD_TYPE_ENUM.ACTIVITY_DETAIL_IMAGES]: 'change'
  };
  return triggerMap[type] || 'blur';
}

function getLengthMessage(label, min, max) {
  if (min && max) {
    return `${label}长度应在${min}-${max}个字符之间`;
  } else if (min) {
    return `${label}长度不能少于${min}个字符`;
  } else if (max) {
    return `${label}长度不能超过${max}个字符`;
  }
  return '';
}

function getRangeMessage(label, min, max) {
  if (min !== undefined && max !== undefined) {
    return `${label}应在${min}-${max}之间`;
  } else if (min !== undefined) {
    return `${label}不能小于${min}`;
  } else if (max !== undefined) {
    return `${label}不能大于${max}`;
  }
  return '';
}

// 根据字段类型计算合适的栅格span值
function getFieldSpan(field) {
  const spanConfig = {
    [FORM_FIELD_TYPE_ENUM.NUMBER]: 24,       // 数字输入：独占整行
    [FORM_FIELD_TYPE_ENUM.INPUT]: 24,        // 文本输入：独占整行
    [FORM_FIELD_TYPE_ENUM.SELECT]: 24,       // 下拉选择：独占整行
    [FORM_FIELD_TYPE_ENUM.TEXTAREA]: 24,     // 文本域：独占整行
    [FORM_FIELD_TYPE_ENUM.DATETIME]: 24,     // 日期选择：独占整行
    [FORM_FIELD_TYPE_ENUM.SWITCH]: 24,       // 开关：独占整行
    [FORM_FIELD_TYPE_ENUM.CHECKBOX]: 24,     // 多选框：独占整行
    [FORM_FIELD_TYPE_ENUM.RADIO]: 24,        // 单选框：独占整行
    [FORM_FIELD_TYPE_ENUM.UPLOAD]: 24,       // 上传组件：独占整行
    [FORM_FIELD_TYPE_ENUM.ACTIVITY_DETAIL_IMAGES]: 24, // 图片上传：独占整行
    'multi-price-config': 24                 // 价格配置：独占整行
  };
  
  // 如果字段明确指定了span，使用字段的设置，否则使用类型默认值
  return field.span || spanConfig[field.type] || 24;
}

// 根据字段类型返回CSS样式类
function getFieldClass(field) {
  const classConfig = {
    [FORM_FIELD_TYPE_ENUM.NUMBER]: 'number-input',
    [FORM_FIELD_TYPE_ENUM.INPUT]: 'text-input',
    [FORM_FIELD_TYPE_ENUM.SELECT]: 'select-input',
    [FORM_FIELD_TYPE_ENUM.DATETIME]: 'datetime-input'
  };
  
  return classConfig[field.type] || '';
}

// 计算基础价格：教练费 + 马匹费（改为计算属性避免重复调用）
const basePrice = computed(() => {
  const coachFee = Number(formData.coachFee || 0);
  const horseFee = Number(formData.horseFee || 0);
  const calculatedPrice = coachFee + horseFee;
  return {
    coachFee,
    horseFee,
    basePrice: calculatedPrice
  };
});

// 保持向后兼容的getBasePrice函数
function getBasePrice() {
  return basePrice.value.basePrice;
}

// ======================== 暴露方法 ========================
defineExpose({
  validate: () => {
    return formRef.value?.validate();
  },
  validateFields: (fields) => {
    return formRef.value?.validateFields(fields);
  },
  resetFields: () => {
    formRef.value?.resetFields();
    initFormData();
  },
  clearValidate: (fields) => {
    formRef.value?.clearValidate(fields);
  }
});
</script>

<style scoped>
.dynamic-form-renderer {
  width: 100%;
}

.empty-config {
  padding: 40px 0;
  text-align: center;
}

.group-title {
  font-weight: 600;
  color: #1890ff;
}

.unknown-field-type {
  padding: 8px 0;
}

/* 输入框宽度控制样式 */
.number-input {
  width: 100%;
  max-width: 200px; /* 数字输入框适中宽度，移除!important */
}

.text-input {
  width: 100%;
  max-width: 300px; /* 文本输入框适中宽度，移除!important */
}

.select-input {
  width: 100%;
  max-width: 250px; /* 选择器适中宽度，移除!important */
}

.datetime-input {
  width: 100%;
  max-width: 280px; /* 日期时间选择器宽度，移除!important */
}

/* 响应式设计：小屏幕下输入框占用更多宽度 */
@media (max-width: 768px) {
  .number-input,
  .text-input,
  .select-input,
  .datetime-input {
    max-width: none;
    width: 100%;
  }
}

:deep(.ant-form-item-label > label) {
  font-weight: 500;
}

:deep(.ant-divider-horizontal.ant-divider-with-text) {
  margin: 24px 0 16px 0;
}

:deep(.ant-divider-horizontal.ant-divider-with-text::before),
:deep(.ant-divider-horizontal.ant-divider-with-text::after) {
  border-top-color: #d9d9d9;
}

:deep(.ant-form-item) {
  margin-bottom: 16px;
}

:deep(.ant-form-item-explain-error) {
  font-size: 12px;
}

:deep(.ant-checkbox-group) {
  width: 100%;
}

:deep(.ant-radio-group) {
  width: 100%;
}

:deep(.ant-checkbox-group .ant-checkbox-wrapper) {
  margin-bottom: 8px;
  margin-right: 16px;
}

:deep(.ant-radio-group .ant-radio-wrapper) {
  margin-bottom: 8px;
  margin-right: 16px;
}
</style>