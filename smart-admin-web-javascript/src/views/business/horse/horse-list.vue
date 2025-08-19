<template>
  <div class="sa-token-privilege">
    <div class="common-container">
      <a-form class="smart-query-form" v-privilege="'club:horse:query'">
        <a-row class="smart-query-form-row">
          <a-form-item label="马名/编号" class="smart-query-form-item">
            <a-input style="width: 200px" v-model:value="queryForm.keywords" placeholder="请输入马名或编号" />
          </a-form-item>

          <a-form-item label="俱乐部" class="smart-query-form-item">
            <a-select style="width: 180px" v-model:value="queryForm.clubId" placeholder="请选择俱乐部" allowClear>
              <a-select-option v-for="club in clubList" :key="club.clubId" :value="club.clubId">
                {{ club.clubName }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="马匹类型" class="smart-query-form-item">
            <a-select style="width: 120px" v-model:value="queryForm.horseType" placeholder="请选择类型" allowClear>
              <a-select-option :value="1">俱乐部马</a-select-option>
              <a-select-option :value="2">马主马</a-select-option>
              <a-select-option :value="3">教练马</a-select-option>
            </a-select>
          </a-form-item>



          <a-form-item class="smart-query-form-item smart-margin-left10">
            <a-button-group>
              <a-button type="primary" @click="queryData">
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

        <a-row class="smart-query-form-row">
          <a-form-item class="smart-query-form-item">
            <a-button v-privilege="'club:horse:add'" type="primary" @click="showModal(true)">
              <template #icon>
                <PlusOutlined />
              </template>
              新建
            </a-button>
          </a-form-item>
        </a-row>
      </a-form>

      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="horseId"
        :loading="tableLoading"
        :pagination="false"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'horseType'">
            <a-tag v-if="text === 1" color="blue">俱乐部马</a-tag>
            <a-tag v-else-if="text === 2" color="green">马主马</a-tag>
            <a-tag v-else-if="text === 3" color="orange">教练马</a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'gender'">
            <span v-if="text === 1">公马</span>
            <span v-else-if="text === 2">母马</span>
            <span v-else-if="text === 3">骟马</span>
          </template>
          <template v-else-if="column.dataIndex === 'birthDate'">
            {{ text ? dayjs(text).format('YYYY-MM-DD') : '-' }}
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button v-privilege="'club:horse:query'" type="link" @click="goDetail(record.horseId)">详情</a-button>
              <a-button v-privilege="'club:horse:health:plan:query'" type="link" @click="goHealthPlan(record.horseId)">健康计划</a-button>
              <a-button v-privilege="'club:horse:update'" type="link" @click="showModal(false, record)">编辑</a-button>
              <a-button v-privilege="'club:horse:delete'" type="link" danger @click="onDelete(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          @showSizeChange="queryData"
          :showTotal="(total) => `共${total}条`"
        />
      </div>
    </div>

    <HorseFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SearchOutlined, PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue';
import { horseApi } from '/@/api/business/horse/horse-api';
import { clubApi } from '/@/api/business/club/club-api';
import { smartSentry } from '/@/lib/smart-sentry';
import HorseFormModal from './components/horse-form-modal.vue';
import dayjs from 'dayjs';
import { useRouter } from 'vue-router';

const router = useRouter();

const columns = [
  // {
  //   title: 'ID',
  //   dataIndex: 'horseId',
  //   width: 80,
  // },
  {
    title: '马匹编号',
    dataIndex: 'horseCode',
    width: 120,
    align: 'center'
  },
  {
    title: '马名',
    dataIndex: 'horseName',
    width: 150,
    align: 'center'
  },
  {
    title: '芯片号',
    dataIndex: 'chipNo',
    width: 150,
    align: 'center'
  },
  {
    title: '俱乐部',
    dataIndex: 'clubName',
    width: 150,
    align: 'center'
  },
  {
    title: '类型',
    dataIndex: 'horseType',
    width: 120,
    align: 'center'
  },
  {
    title: '品种',
    dataIndex: 'breed',
    width: 120,
    align: 'center'
  },
  {
    title: '性别',
    dataIndex: 'gender',
    width: 80,
    align: 'center'
  },
  {
    title: '出生日期',
    dataIndex: 'birthDate',
    width: 120,
    align: 'center'
  },
  {
    title: '马主',
    dataIndex: 'ownerName',
    width: 100,
    align: 'center'
  },
  {
    title: '责任教练',
    dataIndex: 'responsibleCoachName',
    width: 100,
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 220,
    align: 'center'
  },
];

const queryForm = reactive({
  keywords: '',
  clubId: undefined,
  horseType: undefined,
  pageNum: 1,
  pageSize: 10,
});

const tableData = ref([]);
const tableLoading = ref(false);
const total = ref(0);

const formModalRef = ref();
const clubList = ref([]);

async function queryData() {
  try {
    tableLoading.value = true;
    const params = { ...queryForm };
    const res = await horseApi.pageQuery(params);
    if (res.data) {
      tableData.value = res.data.list || [];
      total.value = res.data.total || 0;
    }
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    tableLoading.value = false;
  }
}

function resetQuery() {
  Object.assign(queryForm, {
    keywords: '',
    clubId: undefined,
    horseType: undefined,
    pageNum: 1,
    pageSize: 10,
  });
  queryData();
}

function showModal(isCreate, rowData = {}) {
  formModalRef.value.showModal(isCreate, rowData);
}

function onDelete(record) {
  Modal.confirm({
    title: '提示',
    content: `确定要删除马匹"${record.horseName}"吗？`,
    okText: '删除',
    okType: 'danger',
    onOk: async () => {
      try {
        await horseApi.delete(record.horseId);
        message.success('删除成功');
        queryData();
      } catch (error) {
        smartSentry.captureError(error);
      }
    },
  });
}

function goDetail(horseId) {
  window.open(`#/club/horse/horse-detail?horseId=${horseId}`, '_blank');
}

function goHealthPlan(horseId) {
  window.open(`#/club/horse/horse-detail?horseId=${horseId}&tab=health-plan`, '_blank');
}

async function queryClubList() {
  try {
    const res = await clubApi.queryList();
    clubList.value = res.data || [];
  } catch (error) {
    smartSentry.captureError(error);
  }
}

onMounted(() => {
  queryClubList();
  queryData();
});
</script>