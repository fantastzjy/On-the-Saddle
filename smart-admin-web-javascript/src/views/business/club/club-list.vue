<!--
  * 俱乐部列表
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-01-08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-form class="smart-query-form" v-privilege="'club:club:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="俱乐部名称/联系人/电话" />
      </a-form-item>

      <a-form-item label="省份" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.province" placeholder="省份" />
      </a-form-item>

      <a-form-item label="城市" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.city" placeholder="城市" />
      </a-form-item>

      <a-form-item label="状态" class="smart-query-form-item">
        <a-select style="width: 120px" v-model:value="queryForm.isValid" placeholder="状态" allowClear>
          <a-select-option :value="true">有效</a-select-option>
          <a-select-option :value="false">无效</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" :presets="defaultTimeRanges" @change="dateChange" />
        </a-space>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary" @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="add()" v-privilege="'club:club:add'" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          新建俱乐部
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.CLUB.CLUB" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-table
      :scroll="{ x: 1500 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="clubId"
      :pagination="false"
      :loading="tableLoading"
      bordered
    >
      <template #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'isValid'">
          <a-tag :color="text ? 'green' : 'red'">
            {{ text ? '有效' : '无效' }}
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'clubName'">
          <a-button type="link" @click="detail(record.clubId)" :disabled="!$privilege('club:club:detail')">
            {{ record.clubName }}
          </a-button>
        </template>
        <template v-if="column.dataIndex === 'logoUrl'">
          <img v-if="text" :src="text" style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px;" />
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'businessTime'">
          <span v-if="record.businessStartTime && record.businessEndTime">
            {{ record.businessStartTime }} - {{ record.businessEndTime }}
          </span>
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="detail(record.clubId)" size="small" v-privilege="'club:club:detail'" type="link">详情</a-button>
            <a-button @click="update(record.clubId)" size="small" v-privilege="'club:club:update'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.clubId)" size="small" danger v-privilege="'club:club:delete'" type="link">删除</a-button>
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
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="ajaxQuery"
        @showSizeChange="ajaxQuery"
        :showTotal="(total) => `共${total}条`"
      />
    </div>
  </a-card>

  <!-- 俱乐部新增/编辑弹窗 -->
  <ClubFormModal ref="clubFormModal" @refresh="ajaxQuery" />
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { clubApi } from '/@/api/business/club/club-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
import { smartSentry } from '/@/lib/smart-sentry';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import TableOperator from '/@/components/support/table-operator/index.vue';
import ClubFormModal from './components/club-form-modal.vue';

// ----------------------- 查询 -----------------------

const queryFormState = {
  keywords: null,
  province: null,
  city: null,
  district: null,
  isValid: null,
  startDate: null,
  endDate: null,
  pageNum: 1,
  pageSize: 10,
  isDelete: false,
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const searchDate = ref();

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  searchDate.value = [];
  ajaxQuery();
}

function onSearch() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

function dateChange(dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let params = {
      ...queryForm,
    };
    let res = await clubApi.pageQuery(params);
    tableData.value = res.data.list;
    total.value = res.data.total;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    tableLoading.value = false;
  }
}

// ----------------------- 表格 -----------------------

const columns = ref([
  {
    title: '俱乐部名称',
    dataIndex: 'clubName',
    width: 180,
    fixed: 'left',
  },
  {
    title: '俱乐部编码',
    dataIndex: 'clubCode',
    width: 140,
  },
  {
    title: 'LOGO',
    dataIndex: 'logoUrl',
    width: 80,
  },
  {
    title: '营业时间',
    dataIndex: 'businessTime',
    width: 180,
  },
  {
    title: '地址',
    dataIndex: 'address',
    width: 200,
    ellipsis: true,
  },
  {
    title: '电话',
    dataIndex: 'phone',
    width: 120,
  },
  {
    title: '省份',
    dataIndex: 'province',
    width: 100,
  },
  {
    title: '城市',
    dataIndex: 'city',
    width: 100,
  },
  {
    title: '联系人',
    dataIndex: 'contactPerson',
    width: 100,
  },
  {
    title: '联系电话',
    dataIndex: 'contactPhone',
    width: 120,
  },
  {
    title: '到期时间',
    dataIndex: 'expireDate',
    width: 150,
  },
  {
    title: '状态',
    dataIndex: 'isValid',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150,
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 150,
  },
]);

// ----------------------- 新增/编辑/删除 -----------------------

const clubFormModal = ref();

function add() {
  clubFormModal.value.show();
}

function update(clubId) {
  clubFormModal.value.show(clubId);
}

function detail(clubId) {
  window.open(`#/club/club/club-detail?clubId=${clubId}`, '_blank');
}

async function confirmDelete(clubId) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除该俱乐部吗？',
    okText: '删除',
    okType: 'danger',
    onOk() {
      deleteClub(clubId);
    },
  });
}

async function deleteClub(clubId) {
  try {
    await clubApi.delete(clubId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    smartSentry.captureError(e);
  }
}

onMounted(() => {
  ajaxQuery();
});
</script>