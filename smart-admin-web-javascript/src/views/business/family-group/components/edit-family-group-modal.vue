<template>
  <a-modal
    v-model:open="visible"
    title="编辑家庭组"
    :width="600"
    :confirm-loading="confirmLoading"
    @ok="onSubmit"
    @cancel="onCancel"
    :destroy-on-close="true"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
    >
      <a-form-item label="家庭名称" name="familyName">
        <a-input v-model:value="form.familyName" placeholder="请输入家庭名称" />
      </a-form-item>

      <a-form-item label="主要联系人" name="mainContactId">
        <MemberSelector
          v-model:value="form.mainContactId"
          mode="edit"
          :family-group-id="form.familyGroupId"
          placeholder="请选择主要联系人"
          @change="onContactChange"
        />
      </a-form-item>

    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'
import { smartSentry } from '/@/lib/smart-sentry'
import MemberSelector from './member-selector.vue'
import { getEditValidationRules, formatEditFormData } from '../utils/validation-rules.js'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['success'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const formRef = ref()

const form = reactive({
  familyGroupId: null,
  familyName: '',
  mainContactId: null
})

const rules = getEditValidationRules()

// ----------------------- 方法 -----------------------
function onContactChange(contactId) {
  console.log('选择联系人ID：', contactId)
}

function showModal(familyData) {
  Object.assign(form, {
    familyGroupId: familyData.familyGroupId,
    familyName: familyData.familyName,
    mainContactId: Array.isArray(familyData.mainContactId) ? 
      familyData.mainContactId[0] : familyData.mainContactId
  })
  
  visible.value = true
  
  // 重置表单验证
  setTimeout(() => {
    formRef.value?.clearValidate()
  }, 100)
}

function onCancel() {
  visible.value = false
  formRef.value?.resetFields()
}

async function onSubmit() {
  try {
    await formRef.value.validate()
    
    confirmLoading.value = true
    
    // 使用统一的数据格式化方法
    const updateData = formatEditFormData(form)
    
    const res = await adminFamilyGroupApi.update(updateData)
    
    if (res.code === 0 && res.ok) {
      message.success(res.msg || '编辑成功')
      emits('success')
      onCancel()
    } else {
      message.error('编辑失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) return
    smartSentry.captureError(e)
    message.error('编辑失败')
  } finally {
    confirmLoading.value = false
  }
}

// ----------------------- 暴露方法 -----------------------
defineExpose({
  showModal
})
</script>