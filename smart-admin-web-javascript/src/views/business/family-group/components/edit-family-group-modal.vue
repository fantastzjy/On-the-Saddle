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
        <a-select
          v-model:value="form.mainContactId"
          placeholder="请选择主要联系人"
          allow-clear
          show-search
          :filter-option="false"
          @search="onSearchMembers"
          @change="onContactChange"
        >
          <a-select-option
            v-for="member in memberList"
            :key="member.memberId"
            :value="member.memberId"
          >
            {{ member.actualName }} ({{ member.memberNo }})
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="家庭描述" name="description">
        <a-textarea
          v-model:value="form.description"
          placeholder="请输入家庭描述（可选）"
          :rows="4"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['success'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const formRef = ref()
const memberList = ref([])

const form = reactive({
  familyGroupId: null,
  familyName: '',
  mainContactId: null,
  description: ''
})

const rules = {
  familyName: [
    { required: true, message: '请输入家庭名称', trigger: 'blur' },
    { max: 50, message: '家庭名称长度不能超过50个字符', trigger: 'blur' }
  ]
}

// ----------------------- 方法 -----------------------
async function loadMembers(familyGroupId) {
  try {
    const res = await adminFamilyGroupApi.getDetail(familyGroupId)
    if (res.code === 0 && res.ok && res.data) {
      memberList.value = res.data.members || []
    }
  } catch (e) {
    console.warn('获取家庭成员列表失败：', e)
  }
}

async function onSearchMembers(searchText) {
  // 这里可以实现成员搜索逻辑
  console.log('搜索成员：', searchText)
}

function onContactChange(contactId) {
  console.log('选择联系人：', contactId)
}

function showModal(familyData) {
  Object.assign(form, {
    familyGroupId: familyData.familyGroupId,
    familyName: familyData.familyName,
    mainContactId: familyData.mainContactId,
    description: familyData.description || ''
  })
  
  // 加载家庭成员列表
  if (familyData.familyGroupId) {
    loadMembers(familyData.familyGroupId)
  }
  
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
    
    const res = await adminFamilyGroupApi.update(form)
    
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