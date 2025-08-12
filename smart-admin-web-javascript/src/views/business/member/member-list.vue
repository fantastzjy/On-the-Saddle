<template>
  <div class="member-list-wrapper">
    <!-- 查询表单 -->
    <a-card size="small" title="查询条件" class="smart-query-form-card">
      <a-form
        ref="queryFormRef"
        :model="queryForm"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="关键字">
              <a-input
                v-model:value="queryForm.keywords"
                placeholder="会员编号/姓名/手机号/骑手证号"
                allow-clear
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="注册状态">
              <a-select
                v-model:value="queryForm.registrationStatus"
                placeholder="请选择注册状态"
                allow-clear
                style="width: 100%"
              >
                <a-select-option :value="REGISTRATION_STATUS.UNACTIVATED">未激活</a-select-option>
                <a-select-option :value="REGISTRATION_STATUS.ACTIVATED">已注册</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="会籍状态">
              <a-select
                v-model:value="queryForm.isMembership"
                placeholder="请选择会籍状态"
                allow-clear
                style="width: 100%"
              >
                <a-select-option :value="MEMBERSHIP_STATUS.NORMAL">普通会员</a-select-option>
                <a-select-option :value="MEMBERSHIP_STATUS.MEMBER">会籍会员</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="创建方式">
              <a-select
                v-model:value="queryForm.createdByGuardian"
                placeholder="请选择创建方式"
                allow-clear
                style="width: 100%"
              >
                <a-select-option :value="CREATED_BY.SELF">自主注册</a-select-option>
                <a-select-option :value="CREATED_BY.GUARDIAN">监护人创建</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="状态">
              <a-select
                v-model:value="queryForm.disabledFlag"
                placeholder="请选择状态"
                allow-clear
                style="width: 100%"
              >
                <a-select-option :value="DISABLED_FLAG.ENABLED">启用</a-select-option>
                <a-select-option :value="DISABLED_FLAG.DISABLED">禁用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="创建时间">
              <a-range-picker
                v-model:value="queryForm.createTimeRange"
                style="width: 100%"
                :presets="[
                  { label: '今天', value: [dayjs(), dayjs()] },
                  { label: '昨天', value: [dayjs().subtract(1, 'day'), dayjs().subtract(1, 'day')] },
                  { label: '近7天', value: [dayjs().subtract(6, 'day'), dayjs()] },
                  { label: '近30天', value: [dayjs().subtract(29, 'day'), dayjs()] }
                ]"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="smart-query-form-button">
          <a-col :span="24" style="text-align: right">
            <a-button type="primary" @click="onSearch">
              <SearchOutlined />
              查询
            </a-button>
            <a-button style="margin-left: 10px" @click="onResetQuery">
              <ReloadOutlined />
              重置
            </a-button>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

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
          <a-button
            v-privilege="'club:member:batch'"
            type="danger"
            size="small"
            :disabled="selectedRowKeyList.length == 0"
            @click="onBatchDelete"
          >
            <DeleteOutlined />
            批量删除
          </a-button>
          <a-button
            v-privilege="'club:member:export'"
            size="small"
            @click="onExport"
            :loading="exportLoading"
          >
            <ExportOutlined />
            导出
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
        :row-selection="{
          type: 'checkbox',
          selectedRowKeys: selectedRowKeyList,
          onChange: onSelectChange,
        }"
        size="small"
        bordered
      >
        <!-- 会员信息 -->
        <template #memberInfo="{ record }">
          <div class="member-info">
            <a-avatar :src="record.avatarUrl" :size="40">
              {{ record.actualName?.charAt(0) }}
            </a-avatar>
            <div class="member-info-text">
              <div class="member-name">{{ record.actualName }}</div>
              <div class="member-no">{{ record.memberNo }}</div>
            </div>
          </div>
        </template>

        <!-- 性别 -->
        <template #gender="{ record }">
          {{ GENDER_TEXT[record.gender] }}
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
          {{ maskIdCard(record.idCardNo) }}
        </template>

        <!-- 创建方式 -->
        <template #createdByGuardian="{ record }">
          <a-tag :color="CREATED_BY_COLOR[record.createdByGuardian]">
            {{ CREATED_BY_TEXT[record.createdByGuardian] }}
          </a-tag>
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

        <!-- 操作列 -->
        <template #action="{ record }">
          <div class="smart-table-operate">
            <a-button
              v-privilege="'club:member:detail'"
              @click="showDetail(record.memberId)"
              size="small"
              type="link"
            >
              查看
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
              @click="showFamilyModal(record)"
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

    <!-- 家庭管理弹窗 -->
    <FamilyManageModal
      ref="familyManageModalRef"
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
  DeleteOutlined,
  ExportOutlined
} from '@ant-design/icons-vue'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const'
import TableOperator from '/@/components/support/table-operator/index.vue'
import MemberFormModal from './components/member-form-modal.vue'
import FamilyManageModal from './components/family-manage-modal.vue'
import {
  REGISTRATION_STATUS,
  REGISTRATION_STATUS_TEXT,
  REGISTRATION_STATUS_COLOR,
  MEMBERSHIP_STATUS,
  MEMBERSHIP_STATUS_TEXT,
  MEMBERSHIP_STATUS_COLOR,
  GENDER_TEXT,
  CREATED_BY,
  CREATED_BY_TEXT,
  CREATED_BY_COLOR,
  DISABLED_FLAG,
  DISABLED_FLAG_TEXT,
  DISABLED_FLAG_COLOR,
  TABLE_COLUMNS
} from './constants/member-constants'

// ----------------------- 组件引用 -----------------------

const router = useRouter()
const tableRef = ref()
const queryFormRef = ref()
const memberFormModalRef = ref()
const familyManageModalRef = ref()

// ----------------------- 表格和查询相关 -----------------------

const queryForm = reactive({
  keywords: '',
  registrationStatus: undefined,
  isMembership: undefined,
  createdByGuardian: undefined,
  disabledFlag: undefined,
  createTimeRange: undefined,
  pageNum: 1,
  pageSize: 10
})

const tableLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const selectedRowKeyList = ref([])
const columns = ref(TABLE_COLUMNS)
const exportLoading = ref(false)

// ----------------------- 页面函数 -----------------------

onMounted(() => {
  onSearch()
})

async function onSearch() {
  tableLoading.value = true
  try {
    let params = { ...queryForm }

    // 处理时间范围
    if (queryForm.createTimeRange && queryForm.createTimeRange.length === 2) {
      params.createTimeBegin = dayjs(queryForm.createTimeRange[0]).format('YYYY-MM-DD HH:mm:ss')
      params.createTimeEnd = dayjs(queryForm.createTimeRange[1]).format('YYYY-MM-DD HH:mm:ss')
    }
    delete params.createTimeRange

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
    registrationStatus: undefined,
    isMembership: undefined,
    createdByGuardian: undefined,
    disabledFlag: undefined,
    createTimeRange: undefined,
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

function showFamilyModal(record) {
  familyManageModalRef.value.showModal(record)
}

// ----------------------- 表格操作 -----------------------

function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys
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

async function onBatchDelete() {
  if (selectedRowKeyList.value.length === 0) {
    message.warning('请选择要删除的会员')
    return
  }

  Modal.confirm({
    title: '批量删除',
    content: `确定要删除选中的 ${selectedRowKeyList.value.length} 个会员吗？`,
    okText: '删除',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await memberApi.batchDelete(selectedRowKeyList.value)
        if (res.code === 0 && res.ok) {
          message.success(res.msg || '批量删除成功')
          selectedRowKeyList.value = []
          onSearch()
        } else {
          message.error('批量删除失败：' + res.msg)
        }
      } catch (e) {
        smartSentry.captureError(e)
        message.error('批量删除失败')
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

    // 处理时间范围
    if (queryForm.createTimeRange && queryForm.createTimeRange.length === 2) {
      params.createTimeBegin = dayjs(queryForm.createTimeRange[0]).format('YYYY-MM-DD HH:mm:ss')
      params.createTimeEnd = dayjs(queryForm.createTimeRange[1]).format('YYYY-MM-DD HH:mm:ss')
    }
    delete params.createTimeRange

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

function calculateAge(birthDate) {
  if (!birthDate) return '-'
  return dayjs().diff(dayjs(birthDate), 'year')
}

function maskIdCard(idCard) {
  if (!idCard || idCard.length < 6) return idCard
  return idCard.replace(/^(.{3}).*(.{4})$/, '$1****$2')
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
</script>

<style scoped lang="less">
.member-list-wrapper {
  padding: 10px;
}

.smart-query-form-card {
  margin-bottom: 10px;

  .smart-query-form-button {
    margin-top: 10px;
  }
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

.member-info {
  display: flex;
  align-items: center;

  .member-info-text {
    margin-left: 10px;

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

.smart-table-operate {
  .ant-btn {
    padding: 0 4px;
  }
}
</style>
