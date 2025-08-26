<!--
  * 课程列表页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-form class="smart-query-form" v-privilege="'business:product:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
          style="width: 200px"
          v-model:value="queryForm.keywords"
          placeholder="课程名称/课程编码"
        />
      </a-form-item>

      <a-form-item label="课程类型" class="smart-query-form-item">
        <a-select
          style="width: 150px"
          v-model:value="queryForm.productType"
          placeholder="请选择"
          allowClear
        >
          <a-select-option
            v-for="item in Object.values(PRODUCT_TYPE_ENUM)"
            :key="item.value"
            :value="item.value"
          >
            {{ item.desc }}
          </a-select-option>
        </a-select>
      </a-form-item>


      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-range-picker
          style="width: 240px"
          v-model:value="queryForm.createTimeRange"
          :ranges="defaultTimeRanges"
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="add()" v-privilege="'business:product:add'" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          新增
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.PRODUCT.PRODUCT" :refresh="ajaxQuery" />
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
        <template v-if="column.dataIndex === 'productName'">
          <a-button type="link" @click="detail(record.productId)" :disabled="!$privilege('business:product:detail')">
            {{ record.productName }}
          </a-button>
        </template>

        <template v-if="column.dataIndex === 'productType'">
          <a-tag :color="getProductTypeColor(text)">
            {{ getProductTypeDesc(text) }}
          </a-tag>
        </template>

        <template v-if="column.dataIndex === 'images'">
          <!-- 图片字段已移除 -->
        </template>


        <template v-if="column.dataIndex === 'basePrice'">
          <span v-if="text !== null && text !== undefined">
            ¥{{ Number(text).toFixed(2) }}
          </span>
          <span v-else>-</span>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate" style="display: flex; justify-content: center; align-items: center;">
            <a-button
              @click="detail(record.productId)"
              v-privilege="'business:product:detail'"
              size="small"
              type="link"
            >
              详情
            </a-button>
            <a-button
              @click="edit(record)"
              v-privilege="'business:product:update'"
              size="small"
              type="link"
            >
              编辑
            </a-button>
            <a-button
              @click="remove(record.productId)"
              v-privilege="'business:product:delete'"
              size="small"
              type="link"
              danger
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

  <!-- 图片预览 -->
  <!-- <FilePreviewModal v-model:visible="previewVisible" :url="previewUrl" /> -->
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import {
  SearchOutlined,
  ReloadOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';
import { useRouter } from 'vue-router';
import { productApi } from '/@/api/business/product/product-api';
import {
  PRODUCT_TYPE_ENUM,
  PRODUCT_TABLE_COLUMNS,
  PRODUCT_SEARCH_FORM
} from '/@/constants/business/product/product-const';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
import TableOperator from '/@/components/support/table-operator/index.vue';
import dayjs from 'dayjs';

const router = useRouter();

// ======================== 时间范围配置 ========================
const defaultTimeRanges = {
  今天: [dayjs().startOf('day'), dayjs().endOf('day')],
  昨天: [dayjs().subtract(1, 'day').startOf('day'), dayjs().subtract(1, 'day').endOf('day')],
  本周: [dayjs().startOf('week'), dayjs().endOf('week')],
  本月: [dayjs().startOf('month'), dayjs().endOf('month')],
  上月: [dayjs().subtract(1, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month')]
};

// ======================== 响应式数据 ========================
const queryForm = reactive({ ...PRODUCT_SEARCH_FORM, pageNum: 1, pageSize: 10 });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const columns = ref([...PRODUCT_TABLE_COLUMNS]);

// ======================== 初始化 ========================
onMounted(() => {
  ajaxQuery();
});

// ======================== 查询相关 ========================
function onSearch() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

function resetQuery() {
  Object.assign(queryForm, { ...PRODUCT_SEARCH_FORM, pageNum: 1, pageSize: queryForm.pageSize });
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    const params = {
      ...queryForm,
      createStartTime: queryForm.createTimeRange?.[0]?.format('YYYY-MM-DD HH:mm:ss'),
      createEndTime: queryForm.createTimeRange?.[1]?.format('YYYY-MM-DD HH:mm:ss'),
      createTimeRange: undefined
    };

    console.log('查询参数:', params);

    const response = await productApi.queryProductList(params);

    console.log('完整响应:', response);

    if (response.ok) {
      console.log('API返回数据:', response.data);

      // 兼容不同的数据结构
      const records = response.data.records || response.data.list || response.data || [];
      const totalCount = response.data.total || response.data.totalCount || 0;

      tableData.value = records;
      total.value = totalCount;

      console.log('解析后的表格数据:', tableData.value);
      console.log('表格数据长度:', tableData.value.length);
      if (tableData.value.length > 0) {
        console.log('第一条数据:', tableData.value[0]);
        console.log('第一条数据的字段:', Object.keys(tableData.value[0]));
        console.log('rowKey字段值:', tableData.value[0].productId);
        console.log('课程名称字段值:', tableData.value[0].productName);
      }
      console.log('总数:', total.value);
    } else {
      console.error('API返回错误:', response);
      message.error(response.msg || '查询失败');
    }
  } catch (error) {
    console.error('查询课程列表失败详细错误:', error);
    message.error('查询课程列表失败');
  } finally {
    tableLoading.value = false;
  }
}

// ======================== 页面操作 ========================
function add() {
  router.push('/product/add');
}

function edit(record) {
  router.push(`/product/edit/${record.productId}`);
}

function detail(productId) {
  router.push(`/product/detail/${productId}`);
}

async function remove(productId) {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除这个课程吗？删除后不可恢复。',
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const response = await productApi.deleteProduct(productId);
        if (response.ok) {
          message.success('删除成功');
          ajaxQuery();
        } else {
          message.error(response.msg || '删除失败');
        }
      } catch (error) {
        message.error('删除课程失败');
        console.error('删除课程失败:', error);
      }
    }
  });
}



// ======================== 辅助方法 ========================
function getProductTypeDesc(value) {
  return Object.values(PRODUCT_TYPE_ENUM).find(item => item.value === value)?.desc || '-';
}

function getProductTypeColor(value) {
  const colorMap = {
    [PRODUCT_TYPE_ENUM.COURSE.value]: 'blue',
    [PRODUCT_TYPE_ENUM.PACKAGE.value]: 'green',
    [PRODUCT_TYPE_ENUM.ACTIVITY.value]: 'purple',
    [PRODUCT_TYPE_ENUM.EXPERIENCE.value]: 'orange'
  };
  return colorMap[value] || 'default';
}

</script>

<style scoped>
.smart-table-operate {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.smart-table-operate .ant-btn {
  padding: 0;
  height: auto;
  line-height: 1.2;
}
</style>
