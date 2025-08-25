<!--
  * 通用报告预览模态框组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-12-07
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-modal
    v-model:open="visible"
    title="报告预览"
    width="1400px"
    :footer="null"
    class="report-modal"
    :maskClosable="false"
  >
    <div v-if="loading" class="loading-container">
      <a-spin size="large" tip="正在生成报告..." />
    </div>

    <div v-else-if="reportData" class="report-container">
      <!-- 报告头部信息 -->
      <div class="report-header">
        <h2 class="report-title">{{ reportData.reportTitle }}</h2>
        <p class="report-subtitle" v-if="reportData.reportSubtitle">{{ reportData.reportSubtitle }}</p>
        <p class="report-meta" v-if="reportData.generateTime">
          生成时间：{{ formatTime(reportData.generateTime) }}
        </p>
      </div>

      <!-- 报告摘要卡片 -->
      <div class="report-summary" v-if="reportData.summary && Object.keys(reportData.summary).length > 0">
        <h3 class="section-title">📊 报告摘要</h3>
        <a-row :gutter="16">
          <a-col :span="6" v-for="(value, key) in reportData.summary" :key="key">
            <a-statistic 
              :title="key" 
              :value="value"
              class="summary-card"
            />
          </a-col>
        </a-row>
      </div>

      <!-- 图表展示区域 -->
      <div class="report-charts" v-if="reportData.chartData && Object.keys(reportData.chartData).length > 0">
        <h3 class="section-title">📈 数据图表</h3>
        <ReportCharts :chart-data="reportData.chartData" />
      </div>

      <!-- 报告章节内容 -->
      <div class="report-sections" v-if="reportData.sections && reportData.sections.length > 0">
        <div 
          v-for="section in reportData.sections" 
          :key="section.sectionId"
          class="report-section"
        >
          <h3 class="section-title">{{ section.title }}</h3>
          
          <!-- 章节内容 -->
          <div class="section-content" v-if="section.content" v-html="section.content"></div>
          
          <!-- 章节表格 -->
          <div v-if="section.tables && section.tables.length > 0" class="section-tables">
            <div v-for="table in section.tables" :key="table.tableId" class="table-container">
              <h4 v-if="table.title" class="table-title">{{ table.title }}</h4>
              <a-table 
                :columns="table.columns"
                :dataSource="table.data"
                :pagination="false"
                size="small"
                :scroll="{ x: 'max-content' }"
                bordered
                class="report-table"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="report-actions">
        <a-space>
          <a-button @click="handleRefresh" :loading="refreshing">
            <template #icon><ReloadOutlined /></template>
            刷新数据
          </a-button>
          <a-button type="primary" @click="handleExportPdf" :loading="exporting">
            <template #icon><FilePdfOutlined /></template>
            导出PDF
          </a-button>
          <a-button @click="handleExportExcel" :loading="exporting">
            <template #icon><FileExcelOutlined /></template>
            导出Excel
          </a-button>
          <a-button @click="handleClose">关闭</a-button>
        </a-space>
      </div>
    </div>

    <div v-else class="empty-container">
      <a-empty description="暂无报告数据" />
    </div>
  </a-modal>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { message } from 'ant-design-vue';
import { ReloadOutlined, FilePdfOutlined, FileExcelOutlined } from '@ant-design/icons-vue';
import dayjs from 'dayjs';
import { reportApi } from '/@/api/report';
import ReportCharts from './ReportCharts.vue';

// 定义组件属性
const props = defineProps({
  open: {
    type: Boolean,
    default: false
  },
  reportData: {
    type: Object,
    default: null
  },
  loading: {
    type: Boolean,
    default: false
  },
  reportParams: {
    type: Object,
    default: () => ({})
  }
});

// 定义组件事件
const emit = defineEmits(['update:open', 'refresh']);

// 响应式数据
const visible = computed({
  get: () => props.open,
  set: (value) => emit('update:open', value)
});

const refreshing = ref(false);
const exporting = ref(false);

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-';
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss');
};

// 刷新数据
const handleRefresh = () => {
  refreshing.value = true;
  emit('refresh');
  
  setTimeout(() => {
    refreshing.value = false;
  }, 1000);
};

// 导出PDF
const handleExportPdf = async () => {
  try {
    exporting.value = true;
    
    const exportData = {
      reportType: props.reportParams.reportType,
      params: props.reportParams.params,
      exportFormat: 'pdf',
      fileName: generateFileName('pdf')
    };

    console.log('PDF导出 - 请求数据:', exportData);
    const response = await reportApi.exportToPdf(exportData);
    
    console.log('PDF导出 - 响应类型:', typeof response);
    console.log('PDF导出 - 响应instanceof:', {
      isArrayBuffer: response instanceof ArrayBuffer,
      isBlob: response instanceof Blob,
      isString: typeof response === 'string',
      isObject: response instanceof Object,
      constructor: response?.constructor?.name
    });
    console.log('PDF导出 - 响应内容预览:', 
      typeof response === 'string' ? response.substring(0, 200) : response);
    
    // 检查响应是否存在
    if (!response) {
      throw new Error('服务器未返回任何数据');
    }
    
    // 创建下载链接
    const blob = new Blob([response], { type: 'text/html' });
    console.log('PDF导出 - Blob创建成功:', blob.size, 'bytes');
    
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = exportData.fileName + '.html';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

    message.success('导出成功！文件已下载，请在浏览器中打印为PDF');
    
  } catch (error) {
    console.error('导出PDF失败:', error);
    if (error.message) {
      message.error(`导出PDF失败: ${error.message}`);
    } else {
      message.error('导出PDF失败，请稍后重试');
    }
  } finally {
    exporting.value = false;
  }
};

// 导出Excel
const handleExportExcel = async () => {
  try {
    exporting.value = true;
    
    const exportData = {
      reportType: props.reportParams.reportType,
      params: props.reportParams.params,
      exportFormat: 'excel',
      fileName: generateFileName('excel')
    };

    console.log('Excel导出 - 请求数据:', exportData);
    const response = await reportApi.exportToExcel(exportData);
    
    console.log('Excel导出 - 响应类型:', typeof response);
    console.log('Excel导出 - 响应instanceof:', {
      isArrayBuffer: response instanceof ArrayBuffer,
      isBlob: response instanceof Blob,
      isString: typeof response === 'string',
      isObject: response instanceof Object,
      constructor: response?.constructor?.name
    });
    console.log('Excel导出 - 响应内容预览:', 
      typeof response === 'string' ? response.substring(0, 200) : response);
    
    // 检查响应是否存在
    if (!response) {
      throw new Error('服务器未返回任何数据');
    }
    
    // 创建下载链接
    const blob = new Blob([response], { 
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
    });
    console.log('Excel导出 - Blob创建成功:', blob.size, 'bytes');
    
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = exportData.fileName + '.xlsx';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

    message.success('导出Excel成功！');
    
  } catch (error) {
    console.error('导出Excel失败:', error);
    if (error.message) {
      message.error(`导出Excel失败: ${error.message}`);
    } else {
      message.error('导出Excel失败，请稍后重试');
    }
  } finally {
    exporting.value = false;
  }
};

// 生成文件名
const generateFileName = (type) => {
  const title = props.reportData?.reportTitle || '报告';
  const subtitle = props.reportData?.reportSubtitle || '';
  const timestamp = dayjs().format('YYYYMMDD_HHmmss');
  
  return `${title}_${subtitle}_${timestamp}`;
};

// 关闭模态框
const handleClose = () => {
  visible.value = false;
};
</script>

<style scoped>
.report-modal :deep(.ant-modal-body) {
  padding: 0;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

.report-container {
  max-height: 80vh;
  overflow-y: auto;
  padding: 24px;
}

.report-header {
  text-align: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
}

.report-title {
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
  margin: 0 0 8px 0;
}

.report-subtitle {
  font-size: 16px;
  color: #666;
  margin: 0 0 8px 0;
}

.report-meta {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.report-summary {
  background: #fafafa;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  padding: 20px;
  margin-bottom: 24px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #262626;
  margin-bottom: 16px;
  border-left: 4px solid #1890ff;
  padding-left: 12px;
}

.summary-card {
  background: white;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  padding: 16px;
  text-align: center;
}

.summary-card :deep(.ant-statistic-title) {
  font-size: 12px;
  margin-bottom: 8px;
}

.summary-card :deep(.ant-statistic-content) {
  font-size: 20px;
  font-weight: bold;
  color: #52c41a;
}

.report-sections {
  margin-top: 24px;
}

.report-section {
  margin-bottom: 32px;
}

.section-content {
  margin-bottom: 16px;
  line-height: 1.6;
  color: #595959;
}

.section-content :deep(p) {
  margin-bottom: 8px;
}

.section-tables {
  margin-top: 16px;
}

.table-container {
  margin-bottom: 24px;
}

.table-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #434343;
}

.report-table {
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.report-table :deep(.ant-table-thead > tr > th) {
  background: #fafafa;
  font-weight: 500;
  text-align: center;
}

.report-table :deep(.ant-table-tbody > tr > td) {
  text-align: center;
}

.report-table :deep(.ant-table-tbody > tr:hover > td) {
  background: #e6f7ff;
}

.report-actions {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
  text-align: center;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
</style>