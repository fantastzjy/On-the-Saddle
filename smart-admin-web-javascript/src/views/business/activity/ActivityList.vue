<!--
  * 活动列表页面
  *
  * @Author: 1024创新实验室
  * @Date: 2025-08-28
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="activity-list">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" v-privilege="'business:activity:query'">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input
            style="width: 200px"
            v-model:value="queryForm.keywords"
            placeholder="活动名称"
          />
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
              清空
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <!-- 操作区域和表格 -->
    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <!-- 左侧操作区域暂时为空 -->
        </div>
        <div class="smart-table-setting-block">
          <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.ACTIVITY.ACTIVITY" :refresh="ajaxQuery" />
        </div>
      </a-row>

      <a-table
        :scroll="{ x: 'max-content' }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="productId"
        :pagination="false"
        :loading="tableLoading"
        bordered
      >
        <template #bodyCell="{ column, record, text }">
          <template v-if="column.dataIndex === 'activityName'">
            <a-button type="link" @click="viewDetail(record.productId)" :disabled="!$privilege('business:activity:detail')">
              {{ record.activityName }}
            </a-button>
          </template>

          <template v-if="column.dataIndex === 'activityTime'">
            <div class="activity-time">
              <div>{{ formatDate(record.startTime) }}</div>
              <div>{{ formatDate(record.endTime) }}</div>
            </div>
          </template>

          <template v-if="column.dataIndex === 'maxParticipants'">
            {{ record.maxParticipants }}人
          </template>

          <template v-if="column.dataIndex === 'price'">
            ¥{{ record.price ? parseFloat(record.price).toFixed(2) : '0.00' }}
          </template>

          <template v-if="column.dataIndex === 'createTime'">
            {{ formatDateTime(text) }}
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button v-privilege="'business:activity:detail'" type="link" @click="viewDetail(record.productId)">
                详情
              </a-button>
              <a-button v-privilege="'business:activity:update'" type="link" @click="editActivity(record.productId)">
                编辑
              </a-button>
              <a-button v-privilege="'business:activity:delete'" type="link" danger @click="deleteActivity(record)">
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
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue';
import { activityApi } from '/@/api/business/activity/activity-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
import TableOperator from '/@/components/support/table-operator/index.vue';
import dayjs from 'dayjs';

const router = useRouter();

// ======================== 响应式数据 ========================
const queryForm = reactive({
  keywords: '',
  pageNum: 1,
  pageSize: 10
});

const tableData = ref([]);
const total = ref(0);
const tableLoading = ref(false);

// 表格列配置
const columns = ref([
  {
    title: '活动名称',
    dataIndex: 'activityName',
    width: 200,
    fixed: 'left',
    align: 'center'
  },
  {
    title: '活动时间',
    dataIndex: 'activityTime',
    width: 180,
    align: 'center'
  },
  {
    title: '活动地点',
    dataIndex: 'activityLocation',
    width: 150,
    align: 'center'
  },
  {
    title: '最大人数',
    dataIndex: 'maxParticipants',
    width: 100,
    align: 'center'
  },
  {
    title: '活动价格',
    dataIndex: 'price',
    width: 100,
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150,
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'operate',
    fixed: 'right',
    width: 150,
    align: 'center'
  }
]);

// ======================== 初始化 ========================
onMounted(() => {
  ajaxQuery();
});

// ======================== 查询相关 ========================
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    
    const params = {
      ...queryForm
    };
    
    const response = await activityApi.queryActivityList(params);
    if (response.ok && response.data) {
      tableData.value = response.data.list || [];
      total.value = response.data.total || 0;
    } else {
      message.error(response.msg || '查询失败');
    }
  } catch (error) {
    message.error('查询活动列表失败');
    console.error('查询活动列表失败:', error);
  } finally {
    tableLoading.value = false;
  }
}

function onSearch() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

function resetQuery() {
  Object.assign(queryForm, {
    keywords: '',
    pageNum: 1,
    pageSize: 10
  });
  ajaxQuery();
}

// ======================== 操作相关 ========================
function viewDetail(activityId) {
  router.push(`/activity/detail/${activityId}`);
}

function editActivity(activityId) {
  router.push(`/activity/edit/${activityId}`);
}

async function deleteActivity(record) {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除活动"${record.activityName}"吗？删除后不可恢复。`,
    okText: '确认删除',
    cancelText: '取消',
    okType: 'danger',
    async onOk() {
      try {
        const response = await activityApi.deleteActivity(record.productId);
        if (response.ok) {
          message.success('删除成功');
          ajaxQuery();
        } else {
          message.error(response.msg || '删除失败');
        }
      } catch (error) {
        message.error('删除活动失败');
        console.error('删除活动失败:', error);
      }
    }
  });
}

// ======================== 工具方法 ========================
function formatDate(dateTime) {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('MM-DD HH:mm');
}

function formatDateTime(dateTime) {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm');
}
</script>

<style scoped>
.activity-list {
  padding: 16px;
}

.activity-time {
  font-size: 12px;
  line-height: 1.4;
}

.smart-table-operate {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.smart-table-operate .ant-btn-link {
  padding: 0;
  height: auto;
}
</style>