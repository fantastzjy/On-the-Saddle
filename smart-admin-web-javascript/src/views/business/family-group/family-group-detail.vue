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
          <a-descriptions-item label="家庭组ID">
            {{ familyDetail.familyGroupId }}
          </a-descriptions-item>
          <a-descriptions-item label="家庭名称">
            {{ familyDetail.familyName }}
          </a-descriptions-item>
          <a-descriptions-item label="所属俱乐部">
            <a-tag color="blue">{{ familyDetail.clubName }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="主要联系人">
            {{ familyDetail.mainContactName || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="联系电话">
            {{ familyDetail.mainContactPhone || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="成员数量">
            <a-tag color="green">{{ familyDetail.memberCount }}人</a-tag>
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
          <a-descriptions-item label="家庭描述" :span="3">
            {{ familyDetail.description || '暂无描述' }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 家庭成员卡片 -->
      <a-card class="members-card">
        <template #title>
          <span>家庭成员 ({{ familyMembers.length }}人)</span>
        </template>
        <template #extra>
          <a-space>
            <a-button type="primary" @click="onAddMember">
              <template #icon><PlusOutlined /></template>
              添加成员
            </a-button>
            <a-button @click="onBatchImport">
              <template #icon><UploadOutlined /></template>
              批量导入
            </a-button>
          </a-space>
        </template>

        <a-table
          :columns="memberColumns"
          :data-source="familyMembers"
          :pagination="false"
          row-key="relationId"
          size="small"
          :scroll="{ x: 1000 }"
        >
          <!-- 成员信息 -->
          <template #memberInfo="{ record }">
            <div class="member-info">
              <a-avatar :src="record.avatarUrl" :size="40">
                {{ record.actualName?.charAt(0) }}
              </a-avatar>
              <div class="member-details">
                <div class="member-name">{{ record.actualName }}</div>
                <div class="member-no">{{ record.memberNo }}</div>
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
            {{ record.age }}岁
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
  UploadOutlined,
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

// ----------------------- 表格配置 -----------------------
const memberColumns = [
  {
    title: '成员信息',
    key: 'memberInfo',
    width: 200,
    slots: { customRender: 'memberInfo' }
  },
  {
    title: '性别',
    key: 'gender',
    width: 60,
    align: 'center',
    slots: { customRender: 'gender' }
  },
  {
    title: '年龄',
    key: 'age',
    width: 60,
    align: 'center',
    slots: { customRender: 'age' }
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    width: 120
  },
  {
    title: '注册状态',
    key: 'registrationStatus',
    width: 80,
    align: 'center',
    slots: { customRender: 'registrationStatus' }
  },
  {
    title: '角色',
    key: 'isGuardian',
    width: 80,
    align: 'center',
    slots: { customRender: 'isGuardian' }
  },
  {
    title: '关系备注',
    dataIndex: 'remark',
    width: 120,
    ellipsis: true
  },
  {
    title: '加入时间',
    key: 'joinDate',
    width: 100,
    slots: { customRender: 'joinDate' }
  },
  {
    title: '操作',
    key: 'action',
    width: 180,
    fixed: 'right',
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
  router.push('/family-group/list')
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

function onBatchImport() {
  message.info('批量导入功能开发中...')
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

    .member-details {
      .member-name {
        font-weight: 500;
        color: #262626;
      }

      .member-no {
        font-size: 12px;
        color: #8c8c8c;
      }
    }
  }
}
</style>