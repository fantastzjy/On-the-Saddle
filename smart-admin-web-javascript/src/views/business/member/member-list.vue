<template>
  <div class="member-list-wrapper">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" ref="queryFormRef" :model="queryForm">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input
            style="width: 300px"
            v-model:value="queryForm.keywords"
            placeholder="姓名/手机号/骑手证号"
            allow-clear
          />
        </a-form-item>
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch">
              <SearchOutlined />
              查询
            </a-button>
            <a-button @click="onResetQuery">
              <ReloadOutlined />
              清空
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <!-- 表格操作工具栏 -->
    <a-card size="small">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button
            v-privilege="'club:member:add'"
            type="primary"
            size="small"
            @click="showCreateModal"
          >
            <PlusOutlined />
            新建
          </a-button>
        </div>
        <div class="smart-table-setting-block">
          <TableOperator v-model="columns" :table-id="TABLE_ID_CONST.BUSINESS.CLUB.MEMBER" :refresh="onSearch" />
        </div>
      </a-row>

      <!-- 数据表格 -->
      <a-table
        ref="tableRef"
        :columns="columns"
        :data-source="tableData"
        :loading="tableLoading"
        :pagination="false"
        :scroll="{ x: 1800 }"
        row-key="memberId"
        size="small"
        bordered
      >
        <!-- 会员姓名 -->
        <template #actualName="{ record }">
          <a 
            @click="showDetail(record.memberId)" 
            style="color: #1890ff; cursor: pointer; text-decoration: none;"
            @mouseenter="(e) => e.target.style.textDecoration = 'underline'"
            @mouseleave="(e) => e.target.style.textDecoration = 'none'"
          >
            {{ record.actualName }}
          </a>
        </template>

        <!-- 家庭组 -->
        <template #familyName="{ record }">
          <a 
            v-if="record.familyName && record.familyGroupId"
            @click="goToFamilyDetail(record.familyGroupId)" 
            style="color: #1890ff; cursor: pointer; text-decoration: none;"
            @mouseenter="(e) => e.target.style.textDecoration = 'underline'"
            @mouseleave="(e) => e.target.style.textDecoration = 'none'"
          >
            {{ record.familyName }}
          </a>
          <span v-else style="color: #ccc;">-</span>
        </template>

        <!-- 性别 -->
        <template #gender="{ record }">
          {{ GENDER_TEXT[record.gender] }}
        </template>

        <!-- 手机号 -->
        <template #phone="{ record }">
          <div class="sensitive-field phone-field">
            <span>{{ getDisplayPhone(record.phone, record.memberId) }}</span>
            <a-button 
              type="text" 
              size="small" 
              @click="togglePhoneVisibility(record.memberId)"
              class="visibility-btn"
            >
              <EyeOutlined v-if="!phoneVisibilityMap[record.memberId]" />
              <EyeInvisibleOutlined v-else />
            </a-button>
          </div>
        </template>

        <!-- 年龄 -->
        <template #age="{ record }">
          {{ calculateAge(record.birthDate) }}
        </template>

        <!-- 注册状态 -->
        <template #registrationStatus="{ record }">
          <a-tag :color="REGISTRATION_STATUS_COLOR[record.registrationStatus]">
            {{ REGISTRATION_STATUS_TEXT[record.registrationStatus] }}
          </a-tag>
        </template>

        <!-- 会籍状态 -->
        <template #isMembership="{ record }">
          <a-tag :color="MEMBERSHIP_STATUS_COLOR[record.isMembership]">
            {{ MEMBERSHIP_STATUS_TEXT[record.isMembership] }}
          </a-tag>
        </template>

        <!-- 会籍到期时间 -->
        <template #membershipExpireDate="{ record }">
          <span v-if="record.isMembership && record.membershipExpireDate">
            <span :style="getMembershipExpireStyle(record.membershipExpireDate)">
              {{ dayjs(record.membershipExpireDate).format('YYYY-MM-DD') }}
            </span>
          </span>
          <span v-else>-</span>
        </template>

        <!-- 身份证号 -->
        <template #idCardNo="{ record }">
          <div class="sensitive-field idcard-field">
            <span>{{ getDisplayIdCard(record.idCardNo, record.memberId) }}</span>
            <a-button 
              v-if="record.idCardNo && record.idCardNo.trim()"
              type="text" 
              size="small" 
              @click="toggleIdCardVisibility(record.memberId)"
              class="visibility-btn"
            >
              <EyeOutlined v-if="!idCardVisibilityMap[record.memberId]" />
              <EyeInvisibleOutlined v-else />
            </a-button>
          </div>
        </template>

        <!-- 创建方式 -->
        <template #createdByGuardian="{ record }">
          <a-tag :color="CREATED_BY_COLOR[record.createdByGuardian]">
            {{ CREATED_BY_TEXT[record.createdByGuardian] }}
          </a-tag>
        </template>

        <!-- 默认教练 -->
        <template #defaultCoach="{ record }">
          <span v-if="record.defaultCoachName">
            {{ record.defaultCoachName }}
          </span>
          <span v-else style="color: #ccc;">未设置</span>
        </template>

        <!-- 课程级别 -->
        <template #defaultCourseLevel="{ record }">
          <span v-if="record.defaultCourseLevel">
            {{ getCourseLevelText(record.defaultCourseLevel) }}
          </span>
          <span v-else style="color: #ccc;">未设置</span>
        </template>

        <!-- 状态 -->
        <template #disabledFlag="{ record }">
          <a-tag :color="DISABLED_FLAG_COLOR[record.disabledFlag]">
            {{ DISABLED_FLAG_TEXT[record.disabledFlag] }}
          </a-tag>
        </template>

        <!-- 创建时间 -->
        <template #createTime="{ record }">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm') }}
        </template>

        <!-- 更新时间 -->
        <template #updateTime="{ record }">
          {{ record.updateTime ? dayjs(record.updateTime).format('YYYY-MM-DD HH:mm') : '-' }}
        </template>

        <!-- 操作列 -->
        <template #action="{ record }">
          <div class="smart-table-operate">
            <a-button
              v-privilege="'club:member:detail'"
              @click="showDetail(record.memberId)"
              size="small"
              type="link"
            >
              详情
            </a-button>
            <a-button
              v-privilege="'club:member:update'"
              @click="showEditModal(record)"
              size="small"
              type="link"
            >
              编辑
            </a-button>
            <a-button
              v-privilege="'club:member:family'"
              @click="handleFamilyNavigation(record)"
              size="small"
              type="link"
            >
              家庭
            </a-button>
            <a-button
              v-privilege="'club:member:update-status'"
              @click="onUpdateStatus(record)"
              size="small"
              type="link"
              :style="{ color: record.disabledFlag ? '#52c41a' : '#ff4d4f' }"
            >
              {{ record.disabledFlag ? '启用' : '禁用' }}
            </a-button>
            <a-button
              v-privilege="'club:member:delete'"
              @click="onDelete(record)"
              size="small"
              type="link"
              danger
            >
              删除
            </a-button>
          </div>
        </template>
      </a-table>

      <!-- 分页 -->
      <div class="smart-query-table-page">
        <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          show-size-changer
          show-quick-jumper
          :show-total="(total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条`"
          :page-size-options="['10', '20', '50', '100']"
          @change="onSearch"
          @showSizeChange="onSearch"
        />
      </div>
    </a-card>

    <!-- 会员表单弹窗 -->
    <MemberFormModal
      ref="memberFormModalRef"
      @reload="onSearch"
    />

  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { Modal, message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import {
  SearchOutlined,
  ReloadOutlined,
  PlusOutlined,
  ExportOutlined,
  EyeOutlined,
  EyeInvisibleOutlined
} from '@ant-design/icons-vue'
import { memberApi } from '/@/api/business/member-api'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'
import { smartSentry } from '/@/lib/smart-sentry'
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const'
import TableOperator from '/@/components/support/table-operator/index.vue'
import DictLabel from '/@/components/support/dict-label/index.vue'
import MemberFormModal from './components/member-form-modal.vue'
import {
  MEMBERSHIP_STATUS_TEXT,
  MEMBERSHIP_STATUS_COLOR,
  GENDER_TEXT,
  DISABLED_FLAG,
  TABLE_COLUMNS,
  REGISTRATION_STATUS_TEXT,
  REGISTRATION_STATUS_COLOR,
  CREATED_BY_TEXT,
  CREATED_BY_COLOR,
  DISABLED_FLAG_TEXT,
  DISABLED_FLAG_COLOR,
  COURSE_LEVEL_DICT_CODE,
  COURSE_LEVEL_REVERSE_MAP
} from './constants/member-constants'

// ----------------------- 组件引用 -----------------------

const router = useRouter()
const tableRef = ref()
const queryFormRef = ref()
const memberFormModalRef = ref()

// ----------------------- 表格和查询相关 -----------------------

const queryForm = reactive({
  keywords: '',
  pageNum: 1,
  pageSize: 10
})

const tableLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const columns = ref(TABLE_COLUMNS)
const exportLoading = ref(false)

// 脱敏相关状态
const phoneVisibilityMap = ref({})
const idCardVisibilityMap = ref({})

// ----------------------- 页面函数 -----------------------

onMounted(() => {
  onSearch()
})

async function onSearch() {
  tableLoading.value = true
  try {
    let params = { ...queryForm }

    const res = await memberApi.pageQuery(params)
    if (res.code === 0 && res.ok) {
      tableData.value = res.data.list || []
      total.value = res.data.total || 0
    } else {
      message.error('查询失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
  } finally {
    tableLoading.value = false
  }
}

function onResetQuery() {
  queryFormRef.value?.resetFields()
  Object.assign(queryForm, {
    keywords: '',
    pageNum: 1,
    pageSize: 10
  })
  onSearch()
}

function showCreateModal() {
  memberFormModalRef.value.showModal()  // 不传参数，明确为新建模式
}

function showEditModal(record) {
  memberFormModalRef.value.showModal(record)  // 传递记录，明确为编辑模式
}

function showModal(record) {
  // 保留原函数以备其他地方调用，但推荐使用上面两个明确的函数
  memberFormModalRef.value.showModal(record)
}

function showDetail(memberId) {
  router.push({ name: 'MemberDetail', params: { memberId } })
}

function goToFamilyDetail(familyGroupId) {
  router.push(`/family-group/detail/${familyGroupId}`)
}

async function handleFamilyNavigation(record) {
  try {
    const res = await adminFamilyGroupApi.getMemberFamily(record.memberId)
    if (res.code === 0 && res.ok && res.data) {
      // 有家庭，跳转到家庭详情
      router.push(`/family-group/detail/${res.data.familyGroupId}`)
    } else {
      // 无家庭，跳转到家庭管理列表页面
      router.push('/family-group/list')
    }
  } catch (e) {
    smartSentry.captureError(e)
    // 发生错误时也跳转到家庭管理页面
    router.push('/family-group/list')
  }
}



async function onDelete(record) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除该会员吗？',
    okText: '删除',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await memberApi.delete(record.memberId)
        if (res.code === 0 && res.ok) {
          message.success(res.msg || '删除成功')
          onSearch()
        } else {
          message.error('删除失败：' + res.msg)
        }
      } catch (e) {
        smartSentry.captureError(e)
        message.error('删除失败')
      }
    }
  })
}


async function onUpdateStatus(record) {
  const newStatus = record.disabledFlag === DISABLED_FLAG.ENABLED ? DISABLED_FLAG.DISABLED : DISABLED_FLAG.ENABLED
  const statusText = newStatus === DISABLED_FLAG.ENABLED ? '启用' : '禁用'

  Modal.confirm({
    title: '提示',
    content: `确定要${statusText}该会员吗？`,
    onOk: async () => {
      try {
        const res = await memberApi.updateStatus(record.memberId, newStatus)
        if (res.code === 0 && res.ok) {
          message.success(res.msg || `${statusText}成功`)
          onSearch()
        } else {
          message.error(`${statusText}失败：` + res.msg)
        }
      } catch (e) {
        smartSentry.captureError(e)
        message.error(`${statusText}失败`)
      }
    }
  })
}


async function onExport() {
  exportLoading.value = true
  try {
    let params = { ...queryForm }

    const res = await memberApi.exportMembers(params)

    // 创建下载链接
    const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
    const link = document.createElement('a')
    link.href = window.URL.createObjectURL(blob)
    link.download = `会员数据_${dayjs().format('YYYY-MM-DD_HH-mm-ss')}.xlsx`
    link.click()
    window.URL.revokeObjectURL(link.href)

    message.success('导出成功')
  } catch (e) {
    smartSentry.captureError(e)
    message.error('导出失败')
  } finally {
    exportLoading.value = false
  }
}

// ----------------------- 工具函数 -----------------------

// 脱敏功能相关
function togglePhoneVisibility(memberId) {
  phoneVisibilityMap.value[memberId] = !phoneVisibilityMap.value[memberId]
}

function toggleIdCardVisibility(memberId) {
  idCardVisibilityMap.value[memberId] = !idCardVisibilityMap.value[memberId]
}

function getDisplayPhone(phone, memberId) {
  if (!phone) return '-'
  if (phoneVisibilityMap.value[memberId]) {
    return phone
  }
  return maskPhone(phone)
}

function getDisplayIdCard(idCard, memberId) {
  if (!idCard) return '-'
  if (idCardVisibilityMap.value[memberId]) {
    return idCard
  }
  return maskIdCard(idCard)
}

function maskPhone(phone) {
  if (!phone || phone.length < 7) return phone
  // 保持原始长度，避免脱敏后长度变短
  return phone.replace(/^(.{3}).*(.{4})$/, '$1****$2')
}

function calculateAge(birthDate) {
  if (!birthDate) return '-'
  return dayjs().diff(dayjs(birthDate), 'year')
}

function maskIdCard(idCard) {
  if (!idCard || idCard.length < 6) return idCard
  // 保持原始长度，使用更多星号
  if (idCard.length === 18) {
    return idCard.replace(/^(.{3}).*(.{4})$/, '$1***********$2')
  }
  return idCard.replace(/^(.{3}).*(.{3})$/, '$1****$2')
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

// 课程级别转换函数
function getCourseLevelText(level) {
  if (!level) return ''
  return COURSE_LEVEL_REVERSE_MAP[level] || level
}
</script>

<style scoped lang="less">
.member-list-wrapper {
  padding: 10px;
}

.smart-table-btn-block {
  padding-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .smart-table-operate-block {
    flex: 1;

    .ant-btn {
      margin-right: 10px;
    }
  }

  .smart-table-setting-block {
    flex: none;
  }
}

.smart-query-table-page {
  margin-top: 15px;
  text-align: right;
}

.smart-table-operate {
  .ant-btn {
    padding: 0 4px;
  }
}

.sensitive-field {
  display: flex;
  align-items: center;
  gap: 4px;
  
  .visibility-btn {
    padding: 0;
    min-width: 20px;
    height: 20px;
    color: #666;
    flex-shrink: 0; /* 防止按钮被压缩 */
    
    &:hover {
      color: #1890ff;
    }
  }
  
  /* 脱敏文本容器 */
  span {
    flex: 1;
    min-width: 0; /* 允许文本被截断 */
  }
  
  /* 手机号字段 */
  &.phone-field {
    min-width: 120px;
  }
  
  /* 身份证号字段 - 需要更宽的显示空间 */
  &.idcard-field {
    min-width: 170px;
  }
}
</style>
