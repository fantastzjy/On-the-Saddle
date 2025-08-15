<template>
  <a-modal
    v-model:open="visible"
    title="新建家庭组"
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

      <a-form-item label="所属俱乐部" name="clubId">
        <a-select
          v-model:value="form.clubId"
          placeholder="请选择俱乐部"
          allow-clear
          show-search
          :filter-option="filterClubOption"
          @change="onClubChange"
        >
          <a-select-option
            v-for="club in clubList"
            :key="club.clubId"
            :value="club.clubId"
          >
            {{ club.clubName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="主要联系人" name="mainContactId">
        <MemberSelector
          v-model:value="form.mainContactId"
          mode="create"
          :club-id="form.clubId"
          placeholder="请选择主要联系人（输入姓名/手机号/会员编号搜索）"
          @change="onMainContactChange"
        />
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
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'
import { clubApi } from '/@/api/business/club/club-api'
import { smartSentry } from '/@/lib/smart-sentry'
import MemberSelector from './member-selector.vue'
import { getCreateValidationRules, formatCreateFormData } from '../utils/validation-rules.js'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['success'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const formRef = ref()
const clubList = ref([])

const form = reactive({
  familyName: '',
  clubId: null,
  mainContactId: null, // 统一改名：guardianMemberId → mainContactId
  description: ''
})

const rules = getCreateValidationRules()

// ----------------------- 生命周期 -----------------------
onMounted(() => {
  loadClubList()
})

// ----------------------- 方法 -----------------------
async function loadClubList() {
  try {
    const res = await clubApi.queryList()
    if (res.code === 0 && res.ok) {
      clubList.value = res.data || []
    }
  } catch (e) {
    console.warn('获取俱乐部列表失败：', e)
  }
}

function onMainContactChange(mainContactId) {
  console.log('选择主要联系人ID：', mainContactId)
}

function onClubChange(clubId) {
  // 清除之前的主要联系人选择
  form.mainContactId = null
  
  if (clubId) {
    const selectedClub = clubList.value.find(c => c.clubId === clubId)
    if (selectedClub) {
      console.log('选择俱乐部：', selectedClub.clubName)
    }
  }
}

function filterClubOption(input, option) {
  return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0
}

function showModal() {
  Object.assign(form, {
    familyName: '',
    clubId: null,
    mainContactId: null,
    description: ''
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
    const createData = formatCreateFormData(form)
    
    const res = await adminFamilyGroupApi.create(createData)
    
    if (res.code === 0 && res.ok) {
      message.success(res.msg || '家庭组创建成功')
      emits('success')
      onCancel()
    } else {
      message.error('创建失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) return
    smartSentry.captureError(e)
    message.error('创建失败')
  } finally {
    confirmLoading.value = false
  }
}

// ----------------------- 暴露方法 -----------------------
defineExpose({
  showModal
})
</script>