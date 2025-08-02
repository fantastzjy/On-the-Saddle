<!--
  * 教练列表
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-01-08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-form class="smart-query-form" v-privilege="'club:coach:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="教练姓名/编号/手机号" />
      </a-form-item>

      <a-form-item label="性别" class="smart-query-form-item">
        <a-select style="width: 120px" v-model:value="queryForm.gender" placeholder="性别" allowClear>
          <a-select-option :value="1">男</a-select-option>
          <a-select-option :value="2">女</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="专业等级" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.professionalLevel" placeholder="专业等级" />
      </a-form-item>

      <a-form-item label="所属俱乐部" class="smart-query-form-item">
        <a-select style="width: 200px" v-model:value="queryForm.clubId" placeholder="选择俱乐部" allowClear>
          <a-select-option v-for="club in clubList" :key="club.clubId" :value="club.clubId">
            {{ club.clubName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="在职状态" class="smart-query-form-item">
        <a-select style="width: 120px" v-model:value="queryForm.isActive" placeholder="状态" allowClear>
          <a-select-option :value="true">在职</a-select-option>
          <a-select-option :value="false">离职</a-select-option>
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
        <a-button @click="add()" v-privilege="'club:coach:add'" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          新建教练
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.CLUB.COACH" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-table
      :scroll="{ x: 1800 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="coachId"
      :pagination="false"
      :loading="tableLoading"
      bordered
    >
      <template #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'isActive'">
          <a-tag :color="text ? 'green' : 'red'">
            {{ text ? '在职' : '离职' }}
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'coachName'">
          <a-button type="link" @click="detail(record.coachId)" :disabled="!$privilege('club:coach:detail')">
            {{ record.coachName }}
          </a-button>
        </template>
        <template v-if="column.dataIndex === 'avatarUrl'">
          <img v-if="text" :src="text" style="width: 40px; height: 40px; object-fit: cover; border-radius: 50%;" />
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'gender'">
          <span>{{ text === 1 ? '男' : text === 2 ? '女' : '-' }}</span>
        </template>
        <template v-if="column.dataIndex === 'salary'">
          <span v-if="text">¥{{ text }}</span>
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="update(record.coachId)" size="small" v-privilege="'club:coach:update'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.coachId)" size="small" danger v-privilege="'club:coach:delete'" type="link">删除</a-button>
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

  <!-- 教练新增/编辑弹窗 -->
  <CoachFormModal ref="coachFormModal" @refresh="ajaxQuery" />
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { coachApi } from '/@/api/business/coach/coach-api';
import { clubApi } from '/@/api/business/club/club-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
import { smartSentry } from '/@/lib/smart-sentry';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import TableOperator from '/@/components/support/table-operator/index.vue';
import CoachFormModal from './components/coach-form-modal.vue';

// ----------------------- 查询 -----------------------

const queryFormState = {
  keywords: null,
  gender: null,
  professionalLevel: null,
  clubId: null,
  startDate: null,
  endDate: null,
  pageNum: 1,
  pageSize: 10,
  isDelete: false,
  isActive: null,
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const searchDate = ref();
const clubList = ref([]);

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
    let res = await coachApi.pageQuery(params);
    tableData.value = res.data.list;
    total.value = res.data.total;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    tableLoading.value = false;
  }
}

async function loadClubList() {
  try {
    let res = await clubApi.queryList(true);
    clubList.value = res.data;
  } catch (e) {
    smartSentry.captureError(e);
  }
}

// ----------------------- 表格 -----------------------

const columns = ref([
  {
    title: '教练姓名',
    dataIndex: 'coachName',
    width: 120,
    fixed: 'left',
  },
  {
    title: '教练编号',
    dataIndex: 'coachCode',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    width: 80,
  },
  {
    title: '性别',
    dataIndex: 'gender',
    width: 80,
  },
  {
    title: '手机号码',
    dataIndex: 'phone',
    width: 120,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    width: 150,
    ellipsis: true,
  },
  {
    title: '专业等级',
    dataIndex: 'professionalLevel',
    width: 100,
  },
  {
    title: '专业特长',
    dataIndex: 'speciality',
    width: 150,
    ellipsis: true,
  },
  {
    title: '从业年限',
    dataIndex: 'yearsExperience',
    width: 100,
  },
  {
    title: '所属俱乐部',
    dataIndex: 'clubName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '薪资',
    dataIndex: 'salary',
    width: 100,
  },
  {
    title: '入职日期',
    dataIndex: 'entryDate',
    width: 120,
  },
  {
    title: '紧急联系人',
    dataIndex: 'emergencyContact',
    width: 120,
  },
  {
    title: '紧急联系电话',
    dataIndex: 'emergencyPhone',
    width: 130,
  },
  {
    title: '在职状态',
    dataIndex: 'isActive',
    width: 100,
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
    width: 120,
  },
]);

// ----------------------- 新增/编辑/删除 -----------------------

const coachFormModal = ref();

function add() {
  coachFormModal.value.show();
}

function update(coachId) {
  coachFormModal.value.show(coachId);
}

function detail(coachId) {
  window.open(`#/club/coach/coach-detail?coachId=${coachId}`, '_blank');
}

async function confirmDelete(coachId) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除该教练吗？',
    okText: '删除',
    okType: 'danger',
    onOk() {
      deleteCoach(coachId);
    },
  });
}

async function deleteCoach(coachId) {
  try {
    await coachApi.delete(coachId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    smartSentry.captureError(e);
  }
}

onMounted(() => {
  loadClubList();
  ajaxQuery();
});
</script>