<template>
  <div class="family-group-detail">
    <!-- 头部导航 -->
    <div class="detail-header">
      <a-page-header
        :title="familyDetail.familyName || '家庭组详情'"
        @back="onBack"
      >
        <template #extra>
          <a-space>
            <a-button @click="onEdit">
              <template #icon><EditOutlined /></template>
              编辑家庭组
            </a-button>
            <a-button @click="onRefresh" :loading="loading">
              <template #icon><ReloadOutlined /></template>
              刷新
            </a-button>
          </a-space>
        </template>
      </a-page-header>
    </div>

    <a-spin :spinning="loading">
      <!-- 基本信息卡片 -->
      <a-card title="基本信息" class="info-card">
        <a-descriptions :column="3" bordered size="small">
          <!-- <a-descriptions-item label="家庭组ID">
            {{ familyDetail.familyGroupId }}
          </a-descriptions-item> -->
          <a-descriptions-item label="家庭名称">
            {{ familyDetail.familyName }}
          </a-descriptions-item>
          <a-descriptions-item label="主要联系人">
            {{ familyDetail.mainContactName || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="成员数量">
            <a-tag color="green">{{ familyDetail.memberCount }}人</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="联系电话">
            {{ familyDetail.mainContactPhone || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ dayjs(familyDetail.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </a-descriptions-item>
          <a-descriptions-item label="更新时间">
            {{ dayjs(familyDetail.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
          </a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag v-if="familyDetail.isDelete === 0" color="success">正常</a-tag>
            <a-tag v-else color="error">已删除</a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 家庭成员卡片 -->
      <a-card class="members-card">
        <template #title>
          <span>家庭成员 ({{ familyMembers.length }}人)</span>
        </template>
        <template #extra>
          <a-button type="primary" @click="onAddMember">
            <template #icon><PlusOutlined /></template>
            添加成员
          </a-button>
        </template>

        <a-table
          :columns="memberColumns"
          :data-source="familyMembers"
          :pagination="false"
          row-key="relationId"
          size="small"
        >
          <!-- 成员信息 -->
          <template #memberInfo="{ record }">
            <div 
              class="member-info" 
              @click="onViewMember(record)"
              style="cursor: pointer;"
              @mouseenter="(e) => e.currentTarget.style.backgroundColor = '#f5f5f5'"
              @mouseleave="(e) => e.currentTarget.style.backgroundColor = 'transparent'"
            >
              <a-avatar :src="record.avatarUrl" :size="40">
                {{ record.actualName?.charAt(0) }}
              </a-avatar>
              <div class="member-details">
                <div class="member-name" style="color: #1890ff;">{{ record.actualName }}</div>
              </div>
            </div>
          </template>

          <!-- 性别 -->
          <template #gender="{ record }">
            <a-tag v-if="record.gender === 1" color="blue">男</a-tag>
            <a-tag v-else-if="record.gender === 2" color="pink">女</a-tag>
            <span v-else>未知</span>
          </template>

          <!-- 年龄 -->
          <template #age="{ record }">
            {{ record.age || 0 }}岁
          </template>

          <!-- 注册状态 -->
          <template #registrationStatus="{ record }">
            <a-tag v-if="record.registrationStatus === 1" color="success">已注册</a-tag>
            <a-tag v-else color="warning">未激活</a-tag>
          </template>

          <!-- 监护人标识 -->
          <template #isGuardian="{ record }">
            <a-tag v-if="record.isGuardian === 1" color="gold">
              <template #icon><CrownOutlined /></template>
              监护人
            </a-tag>
            <span v-else>-</span>
          </template>

          <!-- 手机号内联编辑 -->
          <template #phone="{ record }">
            <div v-if="isEditing(record.memberId, 'phone')" class="inline-edit-container">
              <a-input
                v-model:value="editingValue"
                :status="validationStatus"
                placeholder="请输入手机号"
                @input="onPhoneInput"
                @pressEnter="saveEdit"
                style="width: 200px;"
              />
              <div class="edit-actions">
                <a-button 
                  type="primary" 
                  size="small" 
                  :loading="editingLoading"
                  :disabled="!isPhoneValid"
                  @click="saveEdit"
                >
                  ✓
                </a-button>
                <a-button 
                  size="small" 
                  @click="cancelEdit"
                >
                  ✕
                </a-button>
              </div>
              <div v-if="validationMessage" class="validation-message" :class="validationStatus">
                {{ validationMessage }}
              </div>
            </div>
            <span 
              v-else 
              class="editable-cell"
              @click="startEdit(record, 'phone')"
              :title="'点击编辑手机号: ' + (record.phone || '未设置')"
            >
              {{ record.phone || '点击添加' }}
            </span>
          </template>

          <!-- 关系备注内联编辑 -->
          <template #remark="{ record }">
            <div v-if="isEditing(record.memberId, 'remark')" class="inline-edit-container">
              <a-input
                v-model:value="editingValue"
                placeholder="请输入关系备注"
                @pressEnter="saveEdit"
                style="width: 180px;"
              />
              <div class="edit-actions">
                <a-button 
                  type="primary" 
                  size="small" 
                  :loading="editingLoading"
                  @click="saveEdit"
                >
                  ✓
                </a-button>
                <a-button 
                  size="small" 
                  @click="cancelEdit"
                >
                  ✕
                </a-button>
              </div>
            </div>
            <span 
              v-else 
              class="editable-cell"
              @click="startEdit(record, 'remark')"
              :title="'点击编辑关系备注: ' + (record.remark || '暂无备注')"
            >
              {{ record.remark || '点击添加备注' }}
            </span>
          </template>

          <!-- 加入时间 -->
          <template #joinDate="{ record }">
            {{ dayjs(record.joinDate).format('YYYY-MM-DD') }}
          </template>

          <!-- 操作 -->
          <template #action="{ record }">
            <a-space size="small">
              <a-button 
                type="link" 
                size="small"
                @click="onViewMember(record)"
              >
                查看详情
              </a-button>
              
              <a-button 
                v-if="record.isGuardian !== 1"
                type="link" 
                size="small"
                @click="onSetGuardian(record)"
              >
                设为监护人
              </a-button>
              
              <a-popconfirm
                v-if="record.isGuardian !== 1"
                title="确定要移除此成员吗？"
                @confirm="onRemoveMember(record)"
              >
                <a-button 
                  type="link" 
                  size="small" 
                  danger
                >
                  移除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-card>
    </a-spin>

    <!-- 编辑家庭组弹窗 -->
    <EditFamilyGroupModal
      ref="editFamilyGroupModalRef"
      @success="loadFamilyDetail"
    />

    <!-- 添加成员弹窗 -->
    <AddFamilyMemberModal
      ref="addFamilyMemberModalRef"
      @success="loadFamilyDetail"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  EditOutlined,
  ReloadOutlined,
  PlusOutlined,
  CrownOutlined
} from '@ant-design/icons-vue'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'
import EditFamilyGroupModal from './components/edit-family-group-modal.vue'
import AddFamilyMemberModal from './components/add-family-member-modal.vue'

const router = useRouter()
const route = useRoute()

// ----------------------- 响应式数据 -----------------------
const loading = ref(false)
const editFamilyGroupModalRef = ref()
const addFamilyMemberModalRef = ref()

const familyDetail = reactive({
  familyGroupId: null,
  familyName: '',
  clubName: '',
  mainContactName: '',
  mainContactPhone: '',
  description: '',
  memberCount: 0,
  createTime: null,
  updateTime: null,
  isDelete: 0
})

const familyMembers = ref([])

// ----------------------- 内联编辑状态管理 -----------------------
const editingState = reactive({
  memberId: null,
  field: null,
  value: '',
  originalValue: '',
  loading: false,
  validation: {
    status: '',
    message: '',
    isValid: true
  }
})

let phoneValidationTimer = null

// ----------------------- 计算属性 -----------------------
const editingValue = computed({
  get: () => editingState.value,
  set: (value) => editingState.value = value
})

const validationStatus = computed(() => editingState.validation.status)
const validationMessage = computed(() => editingState.validation.message)
const isPhoneValid = computed(() => editingState.validation.isValid)
const editingLoading = computed(() => editingState.loading)

// 动态列宽计算
const getColumnWidth = (field) => {
  if (editingState.field === field) {
    return field === 'phone' ? 280 : 220  // 编辑时扩大列宽
  }
  return field === 'phone' ? 120 : 150    // 默认列宽
}

// ----------------------- 表格配置 -----------------------
const memberColumns = [
  {
    title: '成员信息',
    key: 'memberInfo',
    slots: { customRender: 'memberInfo' }
  },
  {
    title: '性别',
    key: 'gender',
    align: 'center',
    slots: { customRender: 'gender' }
  },
  {
    title: '年龄',
    key: 'age',
    align: 'center',
    slots: { customRender: 'age' }
  },
  {
    title: '手机号',
    key: 'phone',
    width: computed(() => getColumnWidth('phone')),
    align: 'center',
    slots: { customRender: 'phone' }
  },
  {
    title: '注册状态',
    key: 'registrationStatus',
    align: 'center',
    slots: { customRender: 'registrationStatus' }
  },
  {
    title: '角色',
    key: 'isGuardian',
    align: 'center',
    slots: { customRender: 'isGuardian' }
  },
  {
    title: '关系备注',
    key: 'remark',
    width: computed(() => getColumnWidth('remark')),
    ellipsis: true,
    align: 'center',
    slots: { customRender: 'remark' }
  },
  {
    title: '加入时间',
    key: 'joinDate',
    slots: { customRender: 'joinDate' }
  },
  {
    title: '操作',
    key: 'action',
    slots: { customRender: 'action' }
  }
]

// ----------------------- 生命周期 -----------------------
onMounted(() => {
  loadFamilyDetail()
})

// ----------------------- 方法 -----------------------
async function loadFamilyDetail() {
  const familyGroupId = route.params.id
  if (!familyGroupId) {
    message.error('家庭组ID不存在')
    onBack()
    return
  }

  loading.value = true
  try {
    const res = await adminFamilyGroupApi.getDetail(familyGroupId)
    if (res.code === 0 && res.ok && res.data) {
      Object.assign(familyDetail, res.data)
      familyMembers.value = res.data.members || []
    } else {
      message.error('获取家庭组详情失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('获取家庭组详情失败')
  } finally {
    loading.value = false
  }
}

function onBack() {
  router.push('/family-group')
}

function onEdit() {
  editFamilyGroupModalRef.value.showModal(familyDetail)
}

function onRefresh() {
  loadFamilyDetail()
}

function onAddMember() {
  addFamilyMemberModalRef.value.showModal({
    familyGroupId: familyDetail.familyGroupId,
    clubId: familyDetail.clubId
  })
}

function onViewMember(member) {
  router.push(`/member/detail/${member.memberId}`)
}

async function onSetGuardian(member) {
  try {
    const res = await memberApi.setGuardian(
      familyDetail.familyGroupId,
      member.memberId,
      1
    )
    
    if (res.code === 0 && res.ok) {
      message.success('设置监护人成功')
      loadFamilyDetail()
    } else {
      message.error('设置监护人失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('设置监护人失败')
  }
}

function onRemoveMember(member) {
  Modal.confirm({
    title: '移除家庭成员',
    content: `确定要将"${member.actualName}"从家庭组中移除吗？`,
    onOk: async () => {
      try {
        const res = await memberApi.removeFamilyMember(
          familyDetail.familyGroupId,
          member.memberId
        )
        
        if (res.code === 0 && res.ok) {
          message.success('移除成员成功')
          loadFamilyDetail()
        } else {
          message.error('移除成员失败：' + res.msg)
        }
      } catch (e) {
        smartSentry.captureError(e)
        message.error('移除成员失败')
      }
    }
  })
}
// ----------------------- 内联编辑方法 -----------------------

// 判断是否正在编辑
function isEditing(memberId, field) {
  return editingState.memberId === memberId && editingState.field === field
}

// 开始编辑
function startEdit(record, field) {
  // 如果当前有其他单元格在编辑，先取消
  if (editingState.memberId !== null) {
    cancelEdit()
  }
  
  editingState.memberId = record.memberId
  editingState.field = field
  editingState.value = record[field] || ''
  editingState.originalValue = record[field] || ''
  editingState.validation = {
    status: '',
    message: '',
    isValid: true
  }
}

// 手机号输入处理
function onPhoneInput() {
  const phone = editingState.value.trim()
  
  // 清除之前的定时器
  if (phoneValidationTimer) {
    clearTimeout(phoneValidationTimer)
  }
  
  // 基础格式验证
  if (!phone) {
    editingState.validation = {
      status: '',
      message: '',
      isValid: true
    }
    return
  }
  
  if (!/^1[3-9]\d{9}$/.test(phone)) {
    editingState.validation = {
      status: 'error',
      message: '手机号格式不正确',
      isValid: false
    }
    return
  }
  
  // 防抖重复检查
  phoneValidationTimer = setTimeout(() => {
    validatePhoneUnique(phone)
  }, 500)
}

// 验证手机号唯一性
async function validatePhoneUnique(phone) {
  try {
    editingState.validation.status = 'validating'
    editingState.validation.message = '检查中...'
    
    const res = await memberApi.checkPhoneExist(phone, editingState.memberId)
    
    if (res.code === 0 && res.ok) {
      if (res.data) {
        editingState.validation = {
          status: 'error',
          message: '该手机号已存在',
          isValid: false
        }
      } else {
        editingState.validation = {
          status: 'success',
          message: '',
          isValid: true
        }
      }
    } else {
      editingState.validation = {
        status: 'warning',
        message: '验证失败，请重试',
        isValid: false
      }
    }
  } catch (e) {
    editingState.validation = {
      status: 'warning',
      message: '验证失败，请重试',
      isValid: false
    }
  }
}

// 保存编辑
async function saveEdit() {
  const { memberId, field, value, originalValue } = editingState
  
  // 检查是否有变化
  if (value.trim() === originalValue) {
    cancelEdit()
    return
  }
  
  // 手机号的二次验证
  if (field === 'phone' && value.trim()) {
    if (!/^1[3-9]\d{9}$/.test(value.trim())) {
      message.error('手机号格式不正确')
      return
    }
    
    // 二次重复检查
    try {
      const res = await memberApi.checkPhoneExist(value.trim(), memberId)
      if (res.data) {
        message.error('该手机号已存在')
        return
      }
    } catch (e) {
      message.error('验证失败，请重试')
      return
    }
  }
  
  try {
    editingState.loading = true
    
    if (field === 'phone') {
      // 更新手机号
      const res = await memberApi.update({
        memberId: memberId,
        phone: value.trim()
      })
      
      if (res.code === 0 && res.ok) {
        message.success('手机号更新成功')
        
        // 更新本地数据
        const memberIndex = familyMembers.value.findIndex(m => m.memberId === memberId)
        if (memberIndex !== -1) {
          familyMembers.value[memberIndex].phone = value.trim()
        }
        
        cancelEdit()
      } else {
        message.error('更新失败：' + res.msg)
      }
    } else if (field === 'remark') {
      // 更新备注
      const res = await adminFamilyGroupApi.updateMemberRemark(
        familyDetail.familyGroupId,
        memberId,
        value.trim()
      )
      
      if (res.code === 0 && res.ok) {
        message.success('备注更新成功')
        
        // 更新本地数据
        const memberIndex = familyMembers.value.findIndex(m => m.memberId === memberId)
        if (memberIndex !== -1) {
          familyMembers.value[memberIndex].remark = value.trim()
        }
        
        cancelEdit()
      } else {
        message.error('更新失败：' + res.msg)
      }
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('更新失败')
  } finally {
    editingState.loading = false
  }
}

// 取消编辑
function cancelEdit() {
  if (phoneValidationTimer) {
    clearTimeout(phoneValidationTimer)
    phoneValidationTimer = null
  }
  
  editingState.memberId = null
  editingState.field = null
  editingState.value = ''
  editingState.originalValue = ''
  editingState.loading = false
  editingState.validation = {
    status: '',
    message: '',
    isValid: true
  }
}
</script>

<style scoped lang="less">
.family-group-detail {
  .detail-header {
    background: #fff;
    margin-bottom: 16px;
    padding: 0 24px;
    border-radius: 6px;
  }

  .info-card,
  .members-card {
    margin-bottom: 16px;
  }

  .member-info {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 4px 8px;
    border-radius: 4px;
    transition: background-color 0.2s;

    .member-details {
      .member-name {
        font-weight: 500;
        transition: color 0.2s;
      }

      .member-no {
        font-size: 12px;
        color: #8c8c8c;
      }
    }

    &:hover {
      background-color: #f5f5f5;
      
      .member-name {
        text-decoration: underline;
      }
    }
  }

  .inline-edit-container {
    display: flex;
    align-items: center;
    gap: 8px;
    position: relative;

    .edit-actions {
      display: flex;
      gap: 4px;
    }

    .validation-message {
      position: absolute;
      top: 100%;
      left: 0;
      font-size: 12px;
      margin-top: 4px;
      padding: 2px 8px;
      border-radius: 4px;
      white-space: nowrap;
      z-index: 10;
      background: white;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      
      &.error {
        color: #ff4d4f;
        background: #fff2f0;
        border: 1px solid #ffccc7;
      }
      
      &.warning {
        color: #faad14;
        background: #fffbe6;
        border: 1px solid #ffe58f;
      }
      
      &.success {
        color: #52c41a;
        background: #f6ffed;
        border: 1px solid #b7eb8f;
      }
      
      &.validating {
        color: #1890ff;
        background: #f0f9ff;
        border: 1px solid #91d5ff;
      }
    }
  }

  .editable-cell {
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    transition: background-color 0.2s;
    display: inline-block;
    min-width: 60px;
    color: #666;

    &:hover {
      background-color: #f5f5f5;
      color: #1890ff;
    }
  }
}
</style>