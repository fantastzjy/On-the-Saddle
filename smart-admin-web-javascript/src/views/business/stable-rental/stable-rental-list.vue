<template>
  <div class="stable-rental-list">
    <!-- 查询条件 -->
    <a-card size="small" :bordered="false" :body-style="{ paddingBottom: '0' }">
      <a-form ref="queryForm" :model="queryParam" layout="horizontal" class="smart-query-form">
        <a-row :gutter="24">
          <a-col :span="5">
            <a-form-item label="出租人" name="lessorId">
              <EmployeeSelector v-model:value="queryParam.lessorId" placeholder="请选择出租人" />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="租赁人" name="lesseeId">
              <CoachSelector v-model:value="queryParam.lesseeId" placeholder="请选择租赁人" />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="俱乐部" name="targetClubId">
              <ClubSelector v-model:value="queryParam.targetClubId" placeholder="请选择俱乐部" />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="租赁状态" name="rentalStatus">
              <a-select v-model:value="queryParam.rentalStatus" placeholder="请选择租赁状态" allowClear>
                <a-select-option :value="1">生效中</a-select-option>
                <a-select-option :value="2">已过期</a-select-option>
                <a-select-option :value="3">已取消</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item class="smart-query-form-button">
              <a-button type="primary" @click="ajaxQuery">
                <template #icon>
                  <SearchOutlined />
                </template>
                查询
              </a-button>
              <a-button @click="resetQuery">重置</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <!-- 表格操作栏 -->
    <a-card size="small" :bordered="false" :body-style="{ padding: '0 20px 20px 20px' }">
      <div style="display: flex; justify-content: space-between; align-items: center;" class="smart-margin-bottom10">
        <div>
          <a-button type="primary" @click="showAddModal" v-privilege="'business:stable-rental:add'">
            <template #icon>
              <PlusOutlined />
            </template>
            新建
          </a-button>
        </div>
        <TableOperator 
          v-model="columns" 
          :tableId="TABLE_ID_CONST.BUSINESS.STABLE_RENTAL.STABLE_RENTAL" 
          :refresh="ajaxQuery"
        />
      </div>

      <!-- 数据表格 -->
      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="rentalId"
        :pagination="false"
        :loading="tableLoading"
        :scroll="{ x: 1400 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'rentalStatus'">
            <a-tag v-if="record.rentalStatus === 1" color="green">{{ record.rentalStatusDesc }}</a-tag>
            <a-tag v-else-if="record.rentalStatus === 2" color="red">{{ record.rentalStatusDesc }}</a-tag>
            <a-tag v-else color="orange">{{ record.rentalStatusDesc }}</a-tag>
          </template>

          <template v-else-if="column.dataIndex === 'lesseeName'">
            <a-button 
              type="link" 
              size="small" 
              @click="goToCoachDetail(record.lesseeId)"
              v-if="record.lesseeId"
            >
              {{ record.lesseeName }}
            </a-button>
            <span v-else>{{ record.lesseeName }}</span>
          </template>

          <template v-else-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" @click="showDetailModal(record.rentalId)" size="small">详情</a-button>
              <a-button 
                type="link" 
                @click="showUpdateModal(record)" 
                size="small"
                v-privilege="'business:stable-rental:edit'"
              >
                编辑
              </a-button>
              <a-button 
                type="link" 
                danger 
                @click="onDelete(record.rentalId)" 
                size="small"
              >
                删除
              </a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryParam.pageSize"
          v-model:current="queryParam.pageNum"
          v-model:pageSize="queryParam.pageSize"
          :total="total"
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <StableRentalFormModal ref="formModalRef" @refresh="ajaxQuery" />

    <!-- 详情弹窗 -->
    <StableRentalDetailModal ref="detailModalRef" />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { Modal } from 'ant-design-vue';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { stableRentalApi } from '/@/api/business/stable-rental-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { TABLE_ID_CONST } from '/@/constants/index.js';
import { smartSentry } from '/@/lib/smart-sentry';
import TableOperator from '/@/components/support/table-operator/index.vue';
import StableRentalFormModal from './components/stable-rental-form-modal.vue';
import StableRentalDetailModal from './components/stable-rental-detail-modal.vue';
import { EmployeeSelector, CoachSelector, ClubSelector } from '/@/components/business/selector/index.js';

// 查询参数
const queryParam = reactive({
  lessorId: null,
  lesseeId: null,
  targetClubId: null,
  rentalStatus: null,
  pageNum: 1,
  pageSize: 10,
});

// 表格数据
const tableData = ref([]);
const tableLoading = ref(false);
const total = ref(0);

// 表格列配置
const columns = [
  {
    title: '俱乐部',
    dataIndex: 'clubName',
    width: 120,
    align: 'center',
  },
  {
    title: '出租人',
    dataIndex: 'lessorName',
    width: 100,
    align: 'center',
  },
  {
    title: '租赁人',
    dataIndex: 'lesseeName',
    width: 100,
    align: 'center',
  },
  {
    title: '租赁开始时间',
    dataIndex: 'rentalStartTime',
    width: 120,
    align: 'center',
    customRender: ({ text }) => {
      return text ? text.split(' ')[0] : '';
    }
  },
  {
    title: '租赁结束时间', 
    dataIndex: 'rentalEndTime',
    width: 120,
    align: 'center',
    customRender: ({ text }) => {
      return text ? text.split(' ')[0] : '';
    }
  },
  {
    title: '租赁金额',
    dataIndex: 'rentalAmount',
    width: 100,
    align: 'center',
  },
  {
    title: '状态',
    dataIndex: 'rentalStatus',
    width: 80,
    align: 'center',
  },
  {
    title: '更新人',
    dataIndex: 'updateBy',
    width: 100,
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150,
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    fixed: 'right',
    width: 150,
    align: 'center',
  },
];

// 弹窗引用
const formModalRef = ref();
const detailModalRef = ref();

// 查询数据
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    const result = await stableRentalApi.queryPage(queryParam);
    const { list, total: totalCount } = result.data;
    tableData.value = list;
    total.value = totalCount;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    tableLoading.value = false;
  }
}

// 重置查询
function resetQuery() {
  Object.assign(queryParam, {
    lessorId: null,
    lesseeId: null,
    targetClubId: null,
    rentalStatus: null,
    pageNum: 1,
    pageSize: 10,
  });
  ajaxQuery();
}

// 新增
function showAddModal() {
  formModalRef.value.showModal();
}

// 编辑
function showUpdateModal(record) {
  formModalRef.value.showModal(record);
}

// 详情
function showDetailModal(rentalId) {
  detailModalRef.value.showModal(rentalId);
}

// 删除
function onDelete(rentalId) {
  console.log('删除按钮被点击，rentalId:', rentalId);
  Modal.confirm({
    title: '提示',
    content: '确定要删除该租赁记录吗？',
    okText: '删除',
    okType: 'danger',
    cancelText: '取消',
    onOk: async () => {
      try {
        console.log('确认删除，调用API');
        const response = await stableRentalApi.delete(rentalId);
        console.log('删除API响应:', response);
        ajaxQuery();
        console.log('刷新列表完成');
      } catch (e) {
        console.error('删除失败:', e);
        smartSentry.captureError(e);
      }
    },
  });
}

// 跳转到教练详情页面
function goToCoachDetail(coachId) {
  if (coachId) {
    window.open(`#/club/coach/coach-detail?coachId=${coachId}`, '_blank');
  }
}

onMounted(() => {
  ajaxQuery();
});
</script>

<style scoped>
.stable-rental-list {
  margin: 10px;
}
</style>