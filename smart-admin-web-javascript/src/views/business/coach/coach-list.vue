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
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="教练编号" />
      </a-form-item>

      <a-form-item label="所属俱乐部" class="smart-query-form-item">
        <a-select style="width: 200px" v-model:value="queryForm.clubId" placeholder="选择俱乐部" allowClear>
          <a-select-option v-for="club in clubList" :key="club.clubId" :value="club.clubId">
            {{ club.clubName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="教练等级" class="smart-query-form-item">
        <a-select style="width: 120px" v-model:value="queryForm.coachLevel" placeholder="教练等级" allowClear>
          <a-select-option v-for="level in coachLevels" :key="level" :value="level">
            {{ level }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="场地障碍" class="smart-query-form-item">
        <a-select style="width: 120px" v-model:value="queryForm.riderLevelShowJumping" placeholder="场地障碍等级" allowClear>
          <a-select-option v-for="level in riderLevels" :key="level" :value="level">
            {{ level }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="有效状态" class="smart-query-form-item">
        <a-select style="width: 120px" v-model:value="queryForm.isValid" placeholder="状态" allowClear>
          <a-select-option :value="1">有效</a-select-option>
          <a-select-option :value="0">无效</a-select-option>
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
        <template v-if="column.dataIndex === 'isValid'">
          <a-tag :color="text === 1 ? 'green' : 'red'">
            {{ text === 1 ? '有效' : '无效' }}
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'userName'">
          <a-button type="link" @click="detail(record.coachId)" :disabled="!$privilege('club:coach:detail')">
            {{ record.userName }}
          </a-button>
        </template>
        <template v-if="column.dataIndex === 'avatarUrl'">
          <img v-if="text" :src="text" style="width: 40px; height: 40px; object-fit: cover; border-radius: 50%;" />
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="detail(record.coachId)" size="small" v-privilege="'club:coach:detail'" type="link">详情</a-button>
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
  clubId: null,
  userId: null,
  coachLevel: null,
  riderLevelShowJumping: null,
  riderLevelDressage: null,
  riderLevelEventing: null,
  startDate: null,
  endDate: null,
  pageNum: 1,
  pageSize: 10,
  isDelete: 0,
  isValid: null,
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const searchDate = ref();
const clubList = ref([]);

// 等级选项
const riderLevels = ['初三', '初二', '初一', '中三', '中二', '中一', '国三', '国二', '国一', '健将级'];
const coachLevels = ['一星', '二星', '三星', '四星', '五星'];

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
    title: '用户姓名',
    dataIndex: 'userName',
    width: 120,
    fixed: 'left',
  },
  {
    title: '教练编号',
    dataIndex: 'coachNo',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    width: 80,
  },
  {
    title: '所属俱乐部',
    dataIndex: 'clubName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '入行时间',
    dataIndex: 'entryDate',
    width: 150,
  },
  {
    title: '专长领域',
    dataIndex: 'specialties',
    width: 150,
    ellipsis: true,
  },
  {
    title: '骑手证号',
    dataIndex: 'riderCertNo',
    width: 120,
  },
  {
    title: '场地障碍等级',
    dataIndex: 'riderLevelShowJumping',
    width: 120,
  },
  {
    title: '盛装舞步等级',
    dataIndex: 'riderLevelDressage',
    width: 120,
  },
  {
    title: '三项赛等级',
    dataIndex: 'riderLevelEventing',
    width: 120,
  },
  {
    title: '教练证号',
    dataIndex: 'coachCertNo',
    width: 120,
  },
  {
    title: '教练等级',
    dataIndex: 'coachLevel',
    width: 100,
  },
  {
    title: '排序',
    dataIndex: 'sortOrder',
    width: 80,
  },
  {
    title: '有效状态',
    dataIndex: 'isValid',
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
    width: 150,
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