<template>
  <div class="member-detail-wrapper">
    <!-- 页面头部 -->
    <a-page-header
      :title="`${memberInfo.actualName} 的详细信息`"
      @back="$router.go(-1)"
    >
      <template #extra>
        <a-button
          v-privilege="'club:member:update'"
          type="primary"
          @click="showEditModal"
        >
          <EditOutlined />
          编辑
        </a-button>
        <a-button
          v-privilege="'club:member:family'"
          @click="showFamilyModal"
        >
          <TeamOutlined />
          家庭管理
        </a-button>
        <a-button
          v-if="memberInfo.isMembership"
          v-privilege="'club:member:membership'"
          @click="showMembershipModal"
        >
          <CrownOutlined />
          会籍管理
        </a-button>
      </template>
    </a-page-header>

    <!-- 基本信息卡片 -->
    <a-row :gutter="24">
      <a-col :span="18">
        <a-card title="基本信息" class="info-card">
          <a-row :gutter="24">
            <a-col :span="4">
              <div class="avatar-section">
                <a-avatar :src="memberInfo.avatarUrl" :size="80">
                  {{ memberInfo.actualName?.charAt(0) }}
                </a-avatar>
              </div>
            </a-col>
            <a-col :span="20">
              <a-descriptions :column="3" size="middle">
                <a-descriptions-item label="姓名">{{ memberInfo.actualName }}</a-descriptions-item>
                <a-descriptions-item label="会员编号">{{ memberInfo.memberNo }}</a-descriptions-item>
                <a-descriptions-item label="性别">{{ getGenderText(memberInfo.gender) }}</a-descriptions-item>
                
                <a-descriptions-item label="出生日期">
                  {{ memberInfo.birthDate ? dayjs(memberInfo.birthDate).format('YYYY-MM-DD') : '-' }}
                </a-descriptions-item>
                <a-descriptions-item label="年龄">{{ calculateAge(memberInfo.birthDate) }}岁</a-descriptions-item>
                <a-descriptions-item label="手机号">{{ memberInfo.phone }}</a-descriptions-item>
                
                <a-descriptions-item label="邮箱">{{ memberInfo.email || '-' }}</a-descriptions-item>
                <a-descriptions-item label="身份证号">{{ maskIdCard(memberInfo.idCardNo) }}</a-descriptions-item>
                <a-descriptions-item label="骑手证号">{{ memberInfo.riderCertNo || '-' }}</a-descriptions-item>
              </a-descriptions>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      
      <a-col :span="6">
        <a-card title="状态信息" class="status-card">
          <div class="status-item">
            <div class="status-label">注册状态</div>
            <div class="status-value">
              <a-tag :color="getRegistrationStatusColor(memberInfo.registrationStatus)">
                {{ getRegistrationStatusText(memberInfo.registrationStatus) }}
              </a-tag>
            </div>
          </div>
          
          <div class="status-item">
            <div class="status-label">创建方式</div>
            <div class="status-value">
              <a-tag :color="getCreatedByColor(memberInfo.createdByGuardian)">
                {{ getCreatedByText(memberInfo.createdByGuardian) }}
              </a-tag>
            </div>
          </div>
          
          <div class="status-item">
            <div class="status-label">账号状态</div>
            <div class="status-value">
              <a-tag :color="getDisabledFlagColor(memberInfo.disabledFlag)">
                {{ getDisabledFlagText(memberInfo.disabledFlag) }}
              </a-tag>
            </div>
          </div>
          
          <div class="status-item" v-if="memberInfo.lastLoginTime">
            <div class="status-label">最后登录</div>
            <div class="status-value">
              {{ dayjs(memberInfo.lastLoginTime).format('YYYY-MM-DD HH:mm') }}
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 会籍信息（仅会籍会员显示） -->
    <a-card v-if="memberInfo.isMembership" title="会籍信息" class="membership-card">
      <a-descriptions :column="4" size="middle">
        <a-descriptions-item label="会籍状态">
          <a-tag color="gold">会籍会员</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="到期时间">
          <span :style="getMembershipExpireStyle(memberInfo.membershipExpireDate)">
            {{ memberInfo.membershipExpireDate ? dayjs(memberInfo.membershipExpireDate).format('YYYY-MM-DD') : '-' }}
          </span>
        </a-descriptions-item>
        <a-descriptions-item label="剩余天数">
          <span :style="getMembershipExpireStyle(memberInfo.membershipExpireDate)">
            {{ getMembershipRemainingDays(memberInfo.membershipExpireDate) }}
          </span>
        </a-descriptions-item>
        <a-descriptions-item label="续费操作">
          <a-button
            v-privilege="'club:member:membership'"
            type="link"
            @click="showMembershipModal"
            size="small"
          >
            续费管理
          </a-button>
        </a-descriptions-item>
      </a-descriptions>
    </a-card>


    <!-- 详细信息标签页 -->
    <a-card class="detail-tabs-card">
      <a-tabs v-model:activeKey="activeTabKey">
        <!-- 家庭信息 -->
        <a-tab-pane key="family" tab="家庭信息">
          <div v-if="familyInfo">
            <a-descriptions :column="2" size="middle" title="家庭组信息">
              <a-descriptions-item label="家庭名称">{{ familyInfo.familyName }}</a-descriptions-item>
              <a-descriptions-item label="主要联系人">{{ familyInfo.mainContactName }}</a-descriptions-item>
              <a-descriptions-item label="家庭描述" :span="2">
                {{ familyInfo.description || '暂无描述' }}
              </a-descriptions-item>
            </a-descriptions>
            
            <a-divider />
            
            <div class="family-members-section">
              <h4>家庭成员 ({{ familyMembers.length }}人)</h4>
              <a-table
                :data-source="familyMembers"
                :columns="familyMemberColumns"
                :pagination="false"
                size="small"
                row-key="memberId"
              >
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
                
                <template #age="{ record }">
                  {{ calculateAge(record.birthDate) }}岁
                </template>
                
                <template #registrationStatus="{ record }">
                  <a-tag :color="getRegistrationStatusColor(record.registrationStatus)">
                    {{ getRegistrationStatusText(record.registrationStatus) }}
                  </a-tag>
                </template>
                
                <template #isGuardian="{ record }">
                  <a-tag v-if="record.isGuardian" color="gold">监护人</a-tag>
                  <span v-else>-</span>
                </template>
              </a-table>
            </div>
          </div>
          <a-empty v-else description="该会员尚未加入任何家庭组" />
        </a-tab-pane>

        <!-- 订单记录 -->
        <a-tab-pane key="orders" tab="订单记录">
          <div class="orders-section">
            <a-table
              :data-source="orderList"
              :columns="orderColumns"
              :loading="orderLoading"
              :pagination="orderPagination"
              size="small"
              @change="onOrderTableChange"
            >
              <template #orderStatus="{ record }">
                <a-tag :color="getOrderStatusColor(record.orderStatus)">
                  {{ getOrderStatusText(record.orderStatus) }}
                </a-tag>
              </template>
              
              <template #totalAmount="{ record }">
                ¥{{ record.totalAmount }}
              </template>
              
              <template #orderDate="{ record }">
                {{ dayjs(record.orderDate).format('YYYY-MM-DD HH:mm') }}
              </template>
            </a-table>
          </div>
        </a-tab-pane>

        <!-- 课时包信息 -->
        <a-tab-pane key="packages" tab="课时包">
          <div class="packages-section">
            <a-table
              :data-source="packageList"
              :columns="packageColumns"
              :loading="packageLoading"
              size="small"
              :pagination="false"
            >
              <template #remainingLessons="{ record }">
                <span :style="{ color: record.remainingLessons <= 5 ? '#ff4d4f' : '#52c41a' }">
                  {{ record.remainingLessons }} / {{ record.totalLessons }}
                </span>
              </template>
              
              <template #expireDate="{ record }">
                <span :style="getPackageExpireStyle(record.expireDate)">
                  {{ dayjs(record.expireDate).format('YYYY-MM-DD') }}
                </span>
              </template>
            </a-table>
          </div>
        </a-tab-pane>

        <!-- 扩展信息 -->
        <a-tab-pane key="profile" tab="扩展信息">
          <div class="profile-section">
            <pre v-if="memberInfo.profileData">{{ JSON.stringify(JSON.parse(memberInfo.profileData), null, 2) }}</pre>
            <a-empty v-else description="暂无扩展信息" />
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-card>

    <!-- 会员表单弹窗 -->
    <MemberFormModal ref="memberFormModalRef" @reload="loadMemberDetail" />
    
    <!-- 家庭管理弹窗 -->
    <FamilyManageModal ref="familyManageModalRef" @reload="loadFamilyInfo" />
    
    <!-- 会籍续费弹窗 -->
    <MembershipRenewModal ref="membershipRenewModalRef" @reload="loadMemberDetail" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Modal, message } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  EditOutlined,
  TeamOutlined,
  CrownOutlined
} from '@ant-design/icons-vue'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'
import MemberFormModal from './components/member-form-modal.vue'
import FamilyManageModal from './components/family-manage-modal.vue'
import MembershipRenewModal from './components/membership-renew-modal.vue'
import {
  REGISTRATION_STATUS,
  REGISTRATION_STATUS_TEXT,
  REGISTRATION_STATUS_COLOR,
  MEMBERSHIP_STATUS_TEXT,
  MEMBERSHIP_STATUS_COLOR,
  GENDER_TEXT,
  CREATED_BY_TEXT,
  CREATED_BY_COLOR,
  DISABLED_FLAG_TEXT,
  DISABLED_FLAG_COLOR
} from './constants/member-constants'

// ----------------------- 路由 -----------------------
const route = useRoute()
const router = useRouter()

// ----------------------- 组件引用 -----------------------
const memberFormModalRef = ref()
const familyManageModalRef = ref()
const membershipRenewModalRef = ref()

// ----------------------- 响应式数据 -----------------------
const memberInfo = ref({})
const familyInfo = ref(null)
const familyMembers = ref([])
const activeTabKey = ref('family')

// 订单相关
const orderList = ref([])
const orderLoading = ref(false)
const orderPagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// 课时包相关
const packageList = ref([])
const packageLoading = ref(false)

// ----------------------- 表格列配置 -----------------------
const familyMemberColumns = [
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
    customRender: ({ text }) => getGenderText(text)
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
  }
]

const orderColumns = [
  {
    title: '订单号',
    dataIndex: 'orderNo',
    width: 120
  },
  {
    title: '商品名称',
    dataIndex: 'goodsName',
    width: 150
  },
  {
    title: '订单金额',
    dataIndex: 'totalAmount',
    width: 100,
    customRender: { name: 'totalAmount' }
  },
  {
    title: '订单状态',
    dataIndex: 'orderStatus',
    width: 100,
    customRender: { name: 'orderStatus' }
  },
  {
    title: '下单时间',
    dataIndex: 'orderDate',
    width: 150,
    customRender: { name: 'orderDate' }
  }
]

const packageColumns = [
  {
    title: '课时包名称',
    dataIndex: 'packageName',
    width: 200
  },
  {
    title: '剩余/总课时',
    dataIndex: 'remainingLessons',
    width: 120,
    customRender: { name: 'remainingLessons' }
  },
  {
    title: '购买日期',
    dataIndex: 'purchaseDate',
    width: 120,
    customRender: ({ text }) => dayjs(text).format('YYYY-MM-DD')
  },
  {
    title: '到期时间',
    dataIndex: 'expireDate',
    width: 120,
    customRender: { name: 'expireDate' }
  }
]

// ----------------------- 页面函数 -----------------------
onMounted(() => {
  loadMemberDetail()
})

async function loadMemberDetail() {
  const memberId = route.params.memberId
  if (!memberId) {
    message.error('会员ID不能为空')
    router.go(-1)
    return
  }
  
  try {
    const res = await memberApi.detail(memberId)
    if (res.code === 0 && res.ok) {
      memberInfo.value = res.data || {}
      await Promise.all([
        loadFamilyInfo(),
        loadOrderList(),
        loadPackageList()
      ])
    } else {
      message.error('获取会员信息失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('获取会员信息失败')
  }
}

async function loadFamilyInfo() {
  if (!memberInfo.value.memberId) return
  
  try {
    const res = await memberApi.getFamilyInfo(memberInfo.value.memberId)
    if (res.code === 0 && res.ok && res.data) {
      familyInfo.value = res.data.familyGroup
      familyMembers.value = res.data.members || []
    } else {
      familyInfo.value = null
      familyMembers.value = []
    }
  } catch (e) {
    console.warn('获取家庭信息失败：', e)
  }
}

async function loadOrderList() {
  if (!memberInfo.value.memberId) return
  
  orderLoading.value = true
  try {
    // 这里应该调用订单API
    // const res = await orderApi.pageQuery({
    //   customerId: memberInfo.value.memberId,
    //   pageNum: orderPagination.current,
    //   pageSize: orderPagination.pageSize
    // })
    // 模拟数据
    orderList.value = []
    orderPagination.total = 0
  } catch (e) {
    console.warn('获取订单列表失败：', e)
  } finally {
    orderLoading.value = false
  }
}

async function loadPackageList() {
  if (!memberInfo.value.memberId) return
  
  packageLoading.value = true
  try {
    // 这里应该调用课时包API
    // const res = await packageApi.queryList(memberInfo.value.memberId)
    // 模拟数据
    packageList.value = []
  } catch (e) {
    console.warn('获取课时包列表失败：', e)
  } finally {
    packageLoading.value = false
  }
}

// ----------------------- 操作函数 -----------------------
function showEditModal() {
  memberFormModalRef.value.showModal(memberInfo.value)
}

function showFamilyModal() {
  familyManageModalRef.value.showModal(memberInfo.value)
}

function showMembershipModal() {
  membershipRenewModalRef.value.showModal(memberInfo.value)
}


function onOrderTableChange(pagination) {
  orderPagination.current = pagination.current
  orderPagination.pageSize = pagination.pageSize
  loadOrderList()
}

// ----------------------- 工具函数 -----------------------
function calculateAge(birthDate) {
  if (!birthDate) return 0
  return dayjs().diff(dayjs(birthDate), 'year')
}

function maskIdCard(idCard) {
  if (!idCard || idCard.length < 6) return idCard || '-'
  return idCard.replace(/^(.{3}).*(.{4})$/, '$1****$2')
}

function getMembershipRemainingDays(expireDate) {
  if (!expireDate) return '-'
  const diff = dayjs(expireDate).diff(dayjs(), 'day')
  if (diff < 0) return '已过期'
  return `${diff}天`
}

function getMembershipExpireStyle(expireDate) {
  if (!expireDate) return {}
  
  const diff = dayjs(expireDate).diff(dayjs(), 'day')
  if (diff < 0) {
    return { color: '#ff4d4f' } // 已过期 - 红色
  } else if (diff <= 30) {
    return { color: '#fa8c16' } // 30天内到期 - 橙色
  }
  return {}
}

function getPackageExpireStyle(expireDate) {
  const diff = dayjs(expireDate).diff(dayjs(), 'day')
  if (diff < 0) {
    return { color: '#ff4d4f' }
  } else if (diff <= 30) {
    return { color: '#fa8c16' }
  }
  return {}
}

// 状态文本和颜色获取函数
function getRegistrationStatusText(status) {
  return REGISTRATION_STATUS_TEXT[status] || '未知'
}

function getRegistrationStatusColor(status) {
  return REGISTRATION_STATUS_COLOR[status] || 'default'
}

function getMembershipStatusText(status) {
  return MEMBERSHIP_STATUS_TEXT[status] || '未知'
}

function getMembershipStatusColor(status) {
  return MEMBERSHIP_STATUS_COLOR[status] || 'default'
}

function getGenderText(gender) {
  return GENDER_TEXT[gender] || '未知'
}

function getCreatedByText(createdByGuardian) {
  return CREATED_BY_TEXT[createdByGuardian] || '未知'
}

function getCreatedByColor(createdByGuardian) {
  return CREATED_BY_COLOR[createdByGuardian] || 'default'
}

function getDisabledFlagText(disabledFlag) {
  return DISABLED_FLAG_TEXT[disabledFlag] || '未知'
}

function getDisabledFlagColor(disabledFlag) {
  return DISABLED_FLAG_COLOR[disabledFlag] || 'default'
}

function getOrderStatusText(status) {
  const statusMap = {
    0: '待支付',
    1: '已支付',
    2: '已确认',
    3: '已完成',
    4: '已取消',
    5: '已退款'
  }
  return statusMap[status] || '未知'
}

function getOrderStatusColor(status) {
  const colorMap = {
    0: 'orange',
    1: 'blue',
    2: 'cyan',
    3: 'green',
    4: 'red',
    5: 'purple'
  }
  return colorMap[status] || 'default'
}
</script>

<style scoped lang="less">
.member-detail-wrapper {
  padding: 10px;
}

.info-card {
  margin-bottom: 24px;
  
  .avatar-section {
    text-align: center;
    
    .member-status {
      margin-top: 12px;
      
      .ant-tag {
        display: block;
        margin: 4px 0;
      }
    }
  }
}

.status-card {
  height: fit-content;
  
  .status-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .status-label {
      font-weight: 500;
      color: #666;
    }
    
    .status-value {
      text-align: right;
    }
  }
}

.membership-card {
  margin-bottom: 24px;
}


.detail-tabs-card {
  .family-members-section {
    h4 {
      margin-bottom: 16px;
      color: #262626;
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
  
  .orders-section,
  .packages-section {
    min-height: 200px;
  }
  
  .profile-section {
    pre {
      background: #f5f5f5;
      padding: 16px;
      border-radius: 6px;
      overflow-x: auto;
    }
  }
}
</style>