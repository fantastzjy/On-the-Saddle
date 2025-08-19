<template>
  <div class="family-group-list">
    <!-- 搜索表单 -->
    <div class="search-container">
      <a-card size="small" title="搜索条件">
        <a-form
          ref="searchFormRef"
          :model="searchForm"
          layout="inline"
          class="search-form"
        >
          <a-form-item label="俱乐部">
            <a-select 
              v-model:value="searchForm.clubId" 
              placeholder="请选择俱乐部"
              allow-clear
              style="width: 180px"
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
          
          <a-form-item label="家庭名称">
            <a-input 
              v-model:value="searchForm.familyName" 
              placeholder="请输入家庭名称"
              style="width: 160px"
            />
          </a-form-item>
          
          <a-form-item label="联系人">
            <a-input 
              v-model:value="searchForm.mainContactName" 
              placeholder="请输入联系人姓名"
              style="width: 160px"
            />
          </a-form-item>
          
          <a-form-item label="联系电话">
            <a-input 
              v-model:value="searchForm.mainContactPhone" 
              placeholder="请输入联系电话"
              style="width: 160px"
            />
          </a-form-item>
          
          <a-form-item label="成员数量">
            <a-input-number 
              v-model:value="searchForm.minMemberCount" 
              placeholder="最少"
              :min="0"
              style="width: 80px"
            />
            <span style="margin: 0 8px">-</span>
            <a-input-number 
              v-model:value="searchForm.maxMemberCount" 
              placeholder="最多"
              :min="0"
              style="width: 80px"
            />
          </a-form-item>
          
          <a-form-item label="创建时间">
            <a-range-picker 
              v-model:value="searchForm.createTimeRange" 
              show-time
              format="YYYY-MM-DD"
              style="width: 240px"
            />
          </a-form-item>
          
          <a-form-item label="状态">
            <a-select 
              v-model:value="searchForm.status" 
              placeholder="请选择状态"
              allow-clear
              style="width: 120px"
            >
              <a-select-option :value="0">正常</a-select-option>
              <a-select-option :value="1">已删除</a-select-option>
            </a-select>
          </a-form-item>
          
          <a-form-item>
            <a-space>
              <a-button type="primary" @click="onSearch" :loading="tableLoading">
                <template #icon><SearchOutlined /></template>
                搜索
              </a-button>
              <a-button @click="onReset">
                <template #icon><ReloadOutlined /></template>
                重置
              </a-button>
            </a-space>
          </a-form-item>
        </a-form>
      </a-card>
    </div>

    <!-- 操作栏 -->
    <div class="action-container">
      <a-card size="small">
        <a-space>
          <a-button 
            type="primary" 
            @click="onCreateFamily"
          >
            <template #icon><PlusOutlined /></template>
            新建家庭
          </a-button>
          
          <a-button 
            type="primary" 
            danger
            disabled
            style="opacity: 0.5; cursor: not-allowed;"
          >
            <template #icon><DeleteOutlined /></template>
            批量删除 (0)
          </a-button>
          
          <a-button 
            v-if="searchForm.status === 1"
            type="default"
            disabled
            style="opacity: 0.5; cursor: not-allowed;"
          >
            <template #icon><RedoOutlined /></template>
            批量恢复 (0)
          </a-button>
          
          <a-button @click="onExport" :loading="exportLoading">
            <template #icon><ExportOutlined /></template>
            导出数据
          </a-button>
        </a-space>
      </a-card>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <a-card size="small">
        <a-table
          :columns="tableColumns"
          :data-source="tableData"
          :loading="tableLoading"
          :pagination="pagination"
          :scroll="{ x: 1200 }"
          row-key="familyGroupId"
          @change="onTableChange"
          size="small"
        >
          <!-- 家庭名称 -->
          <template #familyName="{ record }">
            <a 
              @click="onViewDetail(record)" 
              style="color: #1890ff; cursor: pointer; text-decoration: none;"
              @mouseenter="(e) => e.target.style.textDecoration = 'underline'"
              @mouseleave="(e) => e.target.style.textDecoration = 'none'"
            >
              {{ record.familyName }}
            </a>
          </template>

          <!-- 俱乐部名称 -->
          <template #clubName="{ record }">
            <a-tag color="blue">{{ record.clubName || '-' }}</a-tag>
          </template>

          <!-- 主要联系人 -->
          <template #mainContact="{ record }">
            <div v-if="record.mainContactName">
              <div>{{ record.mainContactName }}</div>
              <div style="color: #999; font-size: 12px">{{ record.mainContactPhone || '-' }}</div>
            </div>
            <span v-else>-</span>
          </template>

          <!-- 成员数量 -->
          <template #memberCount="{ record }">
            <a-tag color="green">{{ record.memberCount }}人</a-tag>
          </template>

          <!-- 状态 -->
          <template #status="{ record }">
            <a-tag v-if="record.isDelete === 0" color="success">正常</a-tag>
            <a-tag v-else color="error">已删除</a-tag>
          </template>

          <!-- 操作列 -->
          <template #action="{ record }">
            <a-space size="small">
              <a-button 
                type="link" 
                size="small"
                @click="onViewDetail(record)"
              >
                查看详情
              </a-button>
              
              <a-button 
                v-if="record.isDelete === 0"
                type="link" 
                size="small"
                @click="onEdit(record)"
              >
                编辑
              </a-button>
              
              <a-button 
                v-if="record.isDelete === 0"
                type="link" 
                size="small"
                @click="onManageMembers(record)"
              >
                管理成员
              </a-button>
              
              <a-popconfirm
                v-if="record.isDelete === 0"
                title="确定要删除这个家庭组吗？"
                @confirm="onDelete(record)"
              >
                <a-button 
                  type="link" 
                  size="small" 
                  danger
                >
                  删除
                </a-button>
              </a-popconfirm>
              
              <a-button 
                v-if="record.isDelete === 1"
                type="link" 
                size="small"
                @click="onRestore(record)"
              >
                恢复
              </a-button>
            </a-space>
          </template>
        </a-table>
      </a-card>
    </div>

    <!-- 编辑家庭组弹窗 -->
    <EditFamilyGroupModal
      ref="editFamilyGroupModalRef"
      @success="onSearch"
    />

    <!-- 新建家庭组弹窗 -->
    <CreateFamilyGroupModal
      ref="createFamilyGroupModalRef"
      @success="onSearch"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  SearchOutlined,
  ReloadOutlined,
  DeleteOutlined,
  RedoOutlined,
  ExportOutlined,
  PlusOutlined
} from '@ant-design/icons-vue'
import { adminFamilyGroupApi } from '/@/api/business/admin-family-group-api'
import { clubApi } from '/@/api/business/club/club-api'
import { smartSentry } from '/@/lib/smart-sentry'
import EditFamilyGroupModal from './components/edit-family-group-modal.vue'
import CreateFamilyGroupModal from './components/create-family-group-modal.vue'

const router = useRouter()

// ----------------------- 响应式数据 -----------------------
const tableLoading = ref(false)
const exportLoading = ref(false)
const searchFormRef = ref()
const editFamilyGroupModalRef = ref()
const createFamilyGroupModalRef = ref()
const clubList = ref([])
const tableData = ref([])

const searchForm = reactive({
  clubId: null,
  familyName: '',
  mainContactName: '',
  mainContactPhone: '',
  minMemberCount: null,
  maxMemberCount: null,
  createTimeRange: [],
  status: 0, // 默认显示正常状态
  pageNum: 1,
  pageSize: 10
})

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条数据`
})

// ----------------------- 表格配置 -----------------------
const tableColumns = [
  // {
  //   title: '家庭组ID',
  //   dataIndex: 'familyGroupId',
  //   width: 100,
  //   fixed: 'left'
  // },
  {
    title: '家庭名称',
    dataIndex: 'familyName',
    width: 150,
    ellipsis: true,
    align: 'center',
    slots: { customRender: 'familyName' }
  },
  {
    title: '所属俱乐部',
    dataIndex: 'clubName',
    width: 120,
    align: 'center',
    slots: { customRender: 'clubName' }
  },
  {
    title: '主要联系人',
    key: 'mainContact',
    width: 140,
    align: 'center',
    slots: { customRender: 'mainContact' }
  },
  {
    title: '成员数量',
    dataIndex: 'memberCount',
    width: 80,
    align: 'center',
    slots: { customRender: 'memberCount' }
  },
  {
    title: '家庭描述',
    dataIndex: 'description',
    width: 200,
    ellipsis: true,
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150,
    align: 'center',
    customRender: ({ text }) => dayjs(text).format('YYYY-MM-DD HH:mm')
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    align: 'center',
    slots: { customRender: 'status' }
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    fixed: 'right',
    align: 'center',
    slots: { customRender: 'action' }
  }
]

// ----------------------- 生命周期 -----------------------
onMounted(() => {
  loadClubList()
  onSearch()
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

async function onSearch() {
  tableLoading.value = true
  
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.current,
      pageSize: pagination.pageSize
    }
    
    // 处理时间范围
    if (searchForm.createTimeRange && searchForm.createTimeRange.length === 2) {
      params.createTimeStart = dayjs(searchForm.createTimeRange[0]).format('YYYY-MM-DD 00:00:00')
      params.createTimeEnd = dayjs(searchForm.createTimeRange[1]).format('YYYY-MM-DD 23:59:59')
    }
    delete params.createTimeRange
    
    const res = await adminFamilyGroupApi.pageQuery(params)
    if (res.code === 0 && res.ok) {
      tableData.value = res.data.list || []
      pagination.current = res.data.pageNum
      pagination.pageSize = res.data.pageSize
      pagination.total = res.data.total
    } else {
      message.error('查询失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('查询失败')
  } finally {
    tableLoading.value = false
  }
}

function onReset() {
  Object.assign(searchForm, {
    clubId: null,
    familyName: '',
    mainContactName: '',
    mainContactPhone: '',
    minMemberCount: null,
    maxMemberCount: null,
    createTimeRange: [],
    status: 0
  })
  pagination.current = 1
  onSearch()
}

function onTableChange(pag) {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  onSearch()
}

function onViewDetail(record) {
  router.push(`/family-group/detail/${record.familyGroupId}`)
}

function onCreateFamily() {
  createFamilyGroupModalRef.value.showModal()
}

function onEdit(record) {
  editFamilyGroupModalRef.value.showModal(record)
}

function onManageMembers(record) {
  router.push(`/family-group/detail/${record.familyGroupId}?tab=members`)
}

async function onDelete(record) {
  try {
    const res = await adminFamilyGroupApi.batchDelete([record.familyGroupId])
    if (res.code === 0 && res.ok) {
      message.success('删除成功')
      onSearch()
    } else {
      message.error('删除失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('删除失败')
  }
}

async function onRestore(record) {
  try {
    const res = await adminFamilyGroupApi.batchRestore([record.familyGroupId])
    if (res.code === 0 && res.ok) {
      message.success('恢复成功')
      onSearch()
    } else {
      message.error('恢复失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('恢复失败')
  }
}

function onBatchDelete() {
  message.warning('批量删除功能已禁用')
}

function onBatchRestore() {
  message.warning('批量恢复功能已禁用')
}

async function onExport() {
  exportLoading.value = true
  try {
    // TODO: 实现导出功能
    message.info('导出功能开发中...')
  } catch (e) {
    smartSentry.captureError(e)
    message.error('导出失败')
  } finally {
    exportLoading.value = false
  }
}
</script>

<style scoped lang="less">
.family-group-list {
  .search-container,
  .action-container,
  .table-container {
    margin-bottom: 16px;
  }
  
  .search-form {
    .ant-form-item {
      margin-bottom: 16px;
    }
  }
}
</style>