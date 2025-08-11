<template>
  <a-modal
    v-model:open="visible"
    title="家庭管理"
    :width="900"
    :footer="null"
    :destroy-on-close="true"
  >
    <div class="family-manage-content">
      <!-- 会员信息 -->
      <a-card size="small" title="会员信息" class="member-info-card">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="姓名">{{ memberInfo.actualName }}</a-descriptions-item>
          <a-descriptions-item label="会员编号">{{ memberInfo.memberNo }}</a-descriptions-item>
          <a-descriptions-item label="手机号">{{ memberInfo.phone }}</a-descriptions-item>
          <a-descriptions-item label="注册状态">
            <a-tag :color="getRegistrationStatusColor(memberInfo.registrationStatus)">
              {{ getRegistrationStatusText(memberInfo.registrationStatus) }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 家庭组信息 -->
      <a-card size="small" class="family-group-card">
        <template #title>
          <span>家庭组信息</span>
          <a-button
            v-if="!familyInfo"
            type="link"
            size="small"
            @click="showCreateFamilyModal"
          >
            创建家庭组
          </a-button>
        </template>
        
        <template #extra v-if="familyInfo">
          <a-button type="link" size="small" @click="showEditFamilyModal">
            编辑
          </a-button>
        </template>

        <div v-if="familyInfo">
          <a-descriptions :column="2" size="small">
            <a-descriptions-item label="家庭名称">{{ familyInfo.familyName }}</a-descriptions-item>
            <a-descriptions-item label="主要联系人">{{ familyInfo.mainContactName }}</a-descriptions-item>
            <a-descriptions-item label="家庭描述" :span="2">
              {{ familyInfo.description || '暂无描述' }}
            </a-descriptions-item>
          </a-descriptions>
        </div>
        <div v-else class="no-family">
          <a-empty
            image="simple"
            description="该会员尚未加入任何家庭组"
          >
            <a-button type="primary" @click="showCreateFamilyModal">创建家庭组</a-button>
          </a-empty>
        </div>
      </a-card>

      <!-- 家庭成员列表 -->
      <a-card size="small" v-if="familyInfo">
        <template #title>
          <span>家庭成员 ({{ familyMembers.length }}人)</span>
          <a-button
            type="link"
            size="small"
            @click="showAddMemberModal"
          >
            添加成员
          </a-button>
        </template>

        <a-table
          :data-source="familyMembers"
          :columns="memberColumns"
          :pagination="false"
          size="small"
          row-key="memberId"
        >
          <!-- 成员信息 -->
          <template #memberInfo="{ record }">
            <div class="family-member-info">
              <a-avatar :src="record.avatarUrl" :size="32">
                {{ record.actualName?.charAt(0) }}
              </a-avatar>
              <div class="member-info-text">
                <div class="member-name">{{ record.actualName }}</div>
                <div class="member-no">{{ record.memberNo }}</div>
              </div>
            </div>
          </template>

          <!-- 年龄 -->
          <template #age="{ record }">
            {{ calculateAge(record.birthDate) }}岁
          </template>

          <!-- 注册状态 -->
          <template #registrationStatus="{ record }">
            <a-tag :color="getRegistrationStatusColor(record.registrationStatus)">
              {{ getRegistrationStatusText(record.registrationStatus) }}
            </a-tag>
          </template>

          <!-- 监护人标识 -->
          <template #isGuardian="{ record }">
            <a-tag v-if="record.isGuardian" color="gold">
              监护人
            </a-tag>
            <span v-else>-</span>
          </template>

          <!-- 加入时间 -->
          <template #joinDate="{ record }">
            {{ dayjs(record.joinDate).format('YYYY-MM-DD') }}
          </template>

          <!-- 操作 -->
          <template #action="{ record }">
            <a-space size="small">
              <a-button
                v-if="!record.isGuardian"
                type="link"
                size="small"
                @click="setGuardian(record, true)"
              >
                设为监护人
              </a-button>
              <a-button
                v-else
                type="link"
                size="small"
                @click="setGuardian(record, false)"
              >
                取消监护人
              </a-button>
              <a-button
                type="link"
                size="small"
                danger
                @click="removeMember(record)"
              >
                移除
              </a-button>
            </a-space>
          </template>
        </a-table>
      </a-card>
    </div>

    <!-- 创建/编辑家庭组弹窗 -->
    <a-modal
      v-model:open="familyFormVisible"
      :title="familyFormTitle"
      :confirm-loading="familyFormLoading"
      @ok="submitFamilyForm"
      @cancel="cancelFamilyForm"
    >
      <a-form
        ref="familyFormRef"
        :model="familyForm"
        :rules="familyFormRules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="家庭名称" name="familyName">
          <a-input v-model:value="familyForm.familyName" placeholder="请输入家庭名称" />
        </a-form-item>
        <a-form-item label="家庭描述" name="description">
          <a-textarea
            v-model:value="familyForm.description"
            placeholder="请输入家庭描述（可选）"
            :rows="3"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 添加家庭成员弹窗 -->
    <FamilyMemberAddModal
      ref="familyMemberAddModalRef"
      @success="onMemberAdded"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Modal, message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'
import FamilyMemberAddModal from './family-member-add-modal.vue'
import {
  REGISTRATION_STATUS_TEXT,
  REGISTRATION_STATUS_COLOR
} from '../constants/member-constants'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['reload'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const memberInfo = ref({})
const familyInfo = ref(null)
const familyMembers = ref([])

// 家庭组表单
const familyFormVisible = ref(false)
const familyFormLoading = ref(false)
const familyFormRef = ref()
const isEditFamily = ref(false)
const familyForm = reactive({
  familyGroupId: undefined,
  familyName: '',
  description: ''
})

const familyFormRules = {
  familyName: [
    { required: true, message: '请输入家庭名称', trigger: 'blur' },
    { max: 50, message: '家庭名称长度不能超过50个字符', trigger: 'blur' }
  ]
}

// 子组件引用
const familyMemberAddModalRef = ref()

// ----------------------- 表格列配置 -----------------------
const memberColumns = [
  {
    title: '成员信息',
    dataIndex: 'memberInfo',
    width: 150,
    customRender: { name: 'memberInfo' }
  },
  {
    title: '性别',
    dataIndex: 'gender',
    width: 60,
    customRender: ({ text }) => text === 1 ? '男' : text === 2 ? '女' : '未知'
  },
  {
    title: '年龄',
    dataIndex: 'age',
    width: 60,
    customRender: { name: 'age' }
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    width: 120
  },
  {
    title: '注册状态',
    dataIndex: 'registrationStatus',
    width: 80,
    customRender: { name: 'registrationStatus' }
  },
  {
    title: '监护人',
    dataIndex: 'isGuardian',
    width: 80,
    customRender: { name: 'isGuardian' }
  },
  {
    title: '加入时间',
    dataIndex: 'joinDate',
    width: 100,
    customRender: { name: 'joinDate' }
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 150,
    customRender: { name: 'action' }
  }
]

// ----------------------- 计算属性 -----------------------
const familyFormTitle = computed(() => {
  return isEditFamily.value ? '编辑家庭组' : '创建家庭组'
})

// ----------------------- 工具函数 -----------------------
function calculateAge(birthDate) {
  if (!birthDate) return 0
  return dayjs().diff(dayjs(birthDate), 'year')
}

function getRegistrationStatusText(status) {
  return REGISTRATION_STATUS_TEXT[status] || '未知'
}

function getRegistrationStatusColor(status) {
  return REGISTRATION_STATUS_COLOR[status] || 'default'
}

// ----------------------- API 调用 -----------------------
async function loadFamilyInfo() {
  if (!memberInfo.value.memberId) return
  
  try {
    const res = await memberApi.getFamilyInfo(memberInfo.value.memberId)
    if (res.code === 1 && res.data) {
      familyInfo.value = res.data.familyGroup
      familyMembers.value = res.data.members || []
    } else {
      familyInfo.value = null
      familyMembers.value = []
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('获取家庭信息失败')
  }
}

// ----------------------- 家庭组操作 -----------------------
function showCreateFamilyModal() {
  isEditFamily.value = false
  Object.assign(familyForm, {
    familyGroupId: undefined,
    familyName: `${memberInfo.value.actualName}的家庭`,
    description: ''
  })
  familyFormVisible.value = true
}

function showEditFamilyModal() {
  isEditFamily.value = true
  Object.assign(familyForm, {
    familyGroupId: familyInfo.value.familyGroupId,
    familyName: familyInfo.value.familyName,
    description: familyInfo.value.description || ''
  })
  familyFormVisible.value = true
}

function cancelFamilyForm() {
  familyFormVisible.value = false
  familyFormRef.value?.resetFields()
}

async function submitFamilyForm() {
  try {
    await familyFormRef.value.validate()
    
    familyFormLoading.value = true
    
    const submitData = {
      ...familyForm,
      memberId: memberInfo.value.memberId,
      clubId: memberInfo.value.clubId
    }
    
    let res
    if (isEditFamily.value) {
      res = await memberApi.updateFamily(submitData)
    } else {
      res = await memberApi.createFamily(submitData)
    }
    
    if (res.code === 1) {
      message.success(isEditFamily.value ? '编辑成功' : '创建成功')
      await loadFamilyInfo()
      cancelFamilyForm()
    } else {
      message.error((isEditFamily.value ? '编辑' : '创建') + '失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) return
    smartSentry.captureError(e)
    message.error((isEditFamily.value ? '编辑' : '创建') + '失败')
  } finally {
    familyFormLoading.value = false
  }
}

// ----------------------- 家庭成员操作 -----------------------
function showAddMemberModal() {
  if (!familyInfo.value) {
    message.warning('请先创建家庭组')
    return
  }
  familyMemberAddModalRef.value.showModal({
    familyGroupId: familyInfo.value.familyGroupId,
    clubId: memberInfo.value.clubId
  })
}

async function setGuardian(member, isGuardian) {
  try {
    const res = await memberApi.setGuardian(
      familyInfo.value.familyGroupId,
      member.memberId,
      isGuardian
    )
    
    if (res.code === 1) {
      message.success(isGuardian ? '设置监护人成功' : '取消监护人成功')
      await loadFamilyInfo()
    } else {
      message.error('操作失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('操作失败')
  }
}

function removeMember(member) {
  Modal.confirm({
    title: '移除家庭成员',
    content: `确定要将"${member.actualName}"从家庭组中移除吗？`,
    okText: '移除',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await memberApi.removeFamilyMember(
          familyInfo.value.familyGroupId,
          member.memberId
        )
        
        if (res.code === 1) {
          message.success('移除成功')
          await loadFamilyInfo()
          emits('reload')
        } else {
          message.error('移除失败：' + res.msg)
        }
      } catch (e) {
        smartSentry.captureError(e)
        message.error('移除失败')
      }
    }
  })
}

function onMemberAdded() {
  loadFamilyInfo()
  emits('reload')
}

// ----------------------- 弹窗操作 -----------------------
async function showModal(member) {
  memberInfo.value = { ...member }
  visible.value = true
  await loadFamilyInfo()
}

// ----------------------- 暴露方法 -----------------------
defineExpose({
  showModal
})
</script>

<style scoped lang="less">
.family-manage-content {
  .member-info-card {
    margin-bottom: 16px;
  }
  
  .family-group-card {
    margin-bottom: 16px;
    
    .no-family {
      text-align: center;
      padding: 20px 0;
    }
  }
}

.family-member-info {
  display: flex;
  align-items: center;
  
  .member-info-text {
    margin-left: 8px;
    
    .member-name {
      font-weight: 500;
      font-size: 14px;
      color: #262626;
    }
    
    .member-no {
      font-size: 12px;
      color: #8c8c8c;
    }
  }
}
</style>