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

      <a-form-item label="监护人" name="guardianMemberId">
        <a-select
          v-model:value="form.guardianMemberId"
          placeholder="请选择监护人（输入姓名或手机号搜索）"
          allow-clear
          show-search
          :filter-option="false"
          @search="onSearchMembers"
          @change="onGuardianChange"
          :loading="memberSearchLoading"
        >
          <a-select-option
            v-for="member in memberList"
            :key="member.memberId"
            :value="member.memberId"
          >
            {{ member.actualName }} ({{ member.phone || member.memberNo }})
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
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'
import { memberApi } from '/@/api/business/member-api'
import { clubApi } from '/@/api/business/club/club-api'
import { smartSentry } from '/@/lib/smart-sentry'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['success'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const memberSearchLoading = ref(false)
const formRef = ref()
const clubList = ref([])
const memberList = ref([])

const form = reactive({
  familyName: '',
  clubId: null,
  guardianMemberId: null,
  description: ''
})

const rules = {
  familyName: [
    { required: true, message: '请输入家庭名称', trigger: 'blur' },
    { max: 50, message: '家庭名称长度不能超过50个字符', trigger: 'blur' }
  ],
  clubId: [
    { required: true, message: '请选择俱乐部', trigger: 'change' }
  ],
  guardianMemberId: [
    { required: true, message: '请选择监护人', trigger: 'change' }
  ],
  description: [
    { max: 200, message: '家庭描述长度不能超过200个字符', trigger: 'blur' }
  ]
}

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

async function onSearchMembers(searchText) {
  if (!searchText || searchText.length < 2) {
    memberList.value = []
    return
  }
  
  memberSearchLoading.value = true
  try {
    const res = await memberApi.search({
      keyword: searchText.trim(),
      pageNum: 1,
      pageSize: 20
    })
    if (res.code === 0 && res.ok) {
      memberList.value = res.data.list || []
    } else {
      memberList.value = []
    }
  } catch (e) {
    console.warn('搜索会员失败：', e)
    memberList.value = []
  } finally {
    memberSearchLoading.value = false
  }
}

function onGuardianChange(guardianId) {
  console.log('选择监护人：', guardianId)
}

function filterClubOption(input, option) {
  return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0
}

function showModal() {
  Object.assign(form, {
    familyName: '',
    clubId: null,
    guardianMemberId: null,
    description: ''
  })
  
  memberList.value = []
  visible.value = true
  
  // 重置表单验证
  setTimeout(() => {
    formRef.value?.clearValidate()
  }, 100)
}

function onCancel() {
  visible.value = false
  formRef.value?.resetFields()
  memberList.value = []
}

async function onSubmit() {
  try {
    await formRef.value.validate()
    
    confirmLoading.value = true
    
    const res = await adminFamilyGroupApi.create(form)
    
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