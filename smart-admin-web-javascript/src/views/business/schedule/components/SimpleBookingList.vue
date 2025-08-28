<!--
 * 简化预约列表组件 - 用于列表视图显示
 *
 * @Author: Claude Code
 * @Date: 2024-08-25
 * @Copyright: 马术俱乐部管理系统
-->
<template>
  <div class="simple-booking-list">
    <a-table
      :dataSource="bookingList"
      :columns="columns"
      :pagination="paginationConfig"
      :loading="loading"
      size="small"
      rowKey="bookingId"
      bordered
      :scroll="{ x: 'max-content' }"
    >
      <template #bodyCell="{ column, record, text }">
        <!-- 价格信息格式化 -->
        <template v-if="column.dataIndex === 'priceInfo'">
          <div class="price-info">
            <div class="price-row">教练费: ¥{{ formatPrice(record.coachFee) }}</div>
            <div class="price-row">马匹费: ¥{{ formatPrice(record.horseFee) }}</div>
            <div class="price-row total">总费用: ¥{{ formatPrice(record.totalFee) }}</div>
          </div>
        </template>

        <!-- 教练 - 内联编辑 -->
        <template v-if="column.dataIndex === 'coachName'">
          <div v-if="isEditing(record.bookingId, 'coach')" class="inline-edit-container">
            <CoachSelector 
              v-model:value="editingValue"
              :auto-load="true"
              :lazy="false"
              style="width: 150px;"
              @change="onEditValueChange"
            />
            <div class="edit-actions">
              <a-button 
                type="primary" 
                size="small" 
                :loading="editingLoading"
                @click="saveEdit()"
              >
                ✓
              </a-button>
              <a-button 
                size="small" 
                @click="cancelEdit()"
              >
                ✕
              </a-button>
            </div>
          </div>
          <span 
            v-else 
            class="editable-cell"
            @click="startEdit(record, 'coach')"
            :title="'点击切换教练：' + text"
          >
            {{ text || '-' }}
          </span>
        </template>

        <!-- 马匹 - 内联编辑 -->
        <template v-if="column.dataIndex === 'horseName'">
          <div v-if="isEditing(record.bookingId, 'horse')" class="inline-edit-container">
            <HorseSelector 
              v-model:value="editingValue"
              :auto-load="true"
              :lazy="false"
              style="width: 150px;"
              @change="onEditValueChange"
            />
            <div class="edit-actions">
              <a-button 
                type="primary" 
                size="small" 
                :loading="editingLoading"
                @click="saveEdit()"
              >
                ✓
              </a-button>
              <a-button 
                size="small" 
                @click="cancelEdit()"
              >
                ✕
              </a-button>
            </div>
          </div>
          <span 
            v-else 
            class="editable-cell"
            @click="startEdit(record, 'horse')"
            :title="'点击切换马匹：' + text"
          >
            {{ text || '-' }}
          </span>
        </template>

        <!-- 预约时间 - 内联编辑 -->
        <template v-if="column.dataIndex === 'startTime'">
          <div v-if="isEditing(record.bookingId, 'time')" class="inline-edit-container">
            <a-range-picker
              v-model:value="editingValue"
              show-time
              format="MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 260px;"
              @change="onEditValueChange"
            />
            <div class="edit-actions">
              <a-button 
                type="primary" 
                size="small" 
                :loading="editingLoading"
                @click="saveEdit()"
              >
                ✓
              </a-button>
              <a-button 
                size="small" 
                @click="cancelEdit()"
              >
                ✕
              </a-button>
            </div>
          </div>
          <span 
            v-else 
            class="editable-cell"
            @click="startEdit(record, 'time')"
            :title="'点击切换时间：' + formatBookingDate(text)"
          >
            {{ formatBookingDate(text) }}
          </span>
        </template>

        <!-- 预约状态 - 内联编辑 -->
        <template v-if="column.dataIndex === 'bookingStatus'">
          <div v-if="isEditing(record.bookingId, 'status')" class="inline-edit-container">
            <a-select
              v-model:value="editingValue"
              style="width: 120px;"
              @change="onEditValueChange"
            >
              <a-select-option 
                v-for="item in Object.values(BOOKING_STATUS_ENUM)" 
                :key="item.value" 
                :value="item.value"
              >
                {{ item.desc }}
              </a-select-option>
            </a-select>
            <div class="edit-actions">
              <a-button 
                type="primary" 
                size="small" 
                :loading="editingLoading"
                @click="saveEdit()"
              >
                ✓
              </a-button>
              <a-button 
                size="small" 
                @click="cancelEdit()"
              >
                ✕
              </a-button>
            </div>
          </div>
          <a-tag 
            v-else 
            :color="getBookingStatusInfo(record.bookingStatus).color"
            class="editable-cell"
            @click="startEdit(record, 'status')"
            :title="'点击切换状态：' + getBookingStatusInfo(record.bookingStatus).desc"
          >
            {{ getBookingStatusInfo(record.bookingStatus).desc }}
          </a-tag>
        </template>

        <!-- 更新人格式化 -->
        <template v-if="column.dataIndex === 'updateBy'">
          {{ formatUpdateBy(text) }}
        </template>

        <!-- 更新时间格式化 -->
        <template v-if="column.dataIndex === 'updateTime'">
          {{ formatUpdateTime(text) }}
        </template>

        <!-- 操作列 -->
        <template v-if="column.dataIndex === 'action'">
          <a-space>
            <a-button 
              type="primary" 
              size="small"
              @click="handleCheckin(record)"
              :disabled="record.bookingStatus !== 2"
            >
              核销
            </a-button>
            <a-button 
              danger
              size="small"
              @click="handleCancel(record)"
              :disabled="record.bookingStatus === 5"
            >
              取消
            </a-button>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';
import { getBookingStatusInfo, BOOKING_STATUS_ENUM } from '/@/constants/business/schedule/schedule-const.js';
import { bookingApi } from '/@/api/business/booking/booking-api.js';
import CoachSelector from '/@/components/business/selector/CoachSelector.vue';
import HorseSelector from '/@/components/business/selector/HorseSelector.vue';

// Props定义
const props = defineProps({
  bookingList: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  total: {
    type: Number,
    default: 0
  },
  pageNum: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number,
    default: 10
  }
});

// Emits定义
const emit = defineEmits(['query', 'page-change']);

// ==================== 内联编辑状态管理 ====================
const editingCell = ref({ bookingId: null, field: null });
const editingValue = ref(null);
const originalValue = ref(null);
const editingLoading = ref(false);

// 表格列定义 - 课程、会员、教练、马匹、价格、预约时间、预约状态、更新人、更新时间
const columns = [
  {
    title: '课程',
    dataIndex: 'courseName',
    ellipsis: true,
    width: 120,
    align: 'center'
  },
  {
    title: '会员',
    dataIndex: 'memberName',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '教练',
    dataIndex: 'coachName',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '马匹',
    dataIndex: 'horseName',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '价格',
    dataIndex: 'priceInfo',
    width: 150,
    align: 'center'
  },
  {
    title: '预约时间',
    dataIndex: 'startTime',
    width: 120,
    sorter: true,
    align: 'center'
  },
  {
    title: '预约状态',
    dataIndex: 'bookingStatus',
    width: 100,
    align: 'center'
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 120,
    align: 'center'
  },
  {
    title: '更新人',
    dataIndex: 'updateBy',
    ellipsis: true,
    width: 100,
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 120,
    align: 'center',
    fixed: 'right'
  }
];

// 分页配置
const paginationConfig = computed(() => ({
  current: props.pageNum,
  pageSize: props.pageSize,
  total: props.total,
  showSizeChanger: true,
  showQuickJumper: true,
  showLessItems: true,
  pageSizeOptions: ['10', '20', '50', '100'],
  showTotal: (total) => `共${total}条`,
  onChange: (page, size) => {
    emit('page-change', { pageNum: page, pageSize: size });
  },
  onShowSizeChange: (current, size) => {
    emit('page-change', { pageNum: 1, pageSize: size });
  }
}));

// ==================== 内联编辑功能 ====================

// 判断是否正在编辑指定单元格
const isEditing = (bookingId, field) => {
  return editingCell.value.bookingId === bookingId && editingCell.value.field === field;
};

// 开始编辑
const startEdit = (record, field) => {
  if (editingCell.value.bookingId !== null) {
    // 如果已经有其他单元格在编辑，先取消
    cancelEdit();
  }
  
  editingCell.value = { bookingId: record.bookingId, field };
  
  // 设置初始编辑值
  switch(field) {
    case 'coach':
      originalValue.value = record.coachId;
      editingValue.value = record.coachId;
      break;
    case 'horse':
      originalValue.value = record.horseId;
      editingValue.value = record.horseId;
      break;
    case 'time':
      originalValue.value = [record.startTime, record.endTime];
      editingValue.value = [record.startTime, record.endTime].map(time => dayjs(time));
      break;
    case 'status':
      originalValue.value = record.bookingStatus;
      editingValue.value = record.bookingStatus;
      break;
  }
};

// 编辑值变化回调
const onEditValueChange = (value) => {
  editingValue.value = value;
};

// 保存编辑
const saveEdit = async () => {
  if (!editingCell.value.bookingId || !editingCell.value.field) {
    return;
  }
  
  const { bookingId, field } = editingCell.value;
  const newValue = editingValue.value;
  
  // 检查值是否有变化
  if (isValueUnchanged(field, newValue)) {
    cancelEdit();
    return;
  }
  
  try {
    editingLoading.value = true;
    
    // 调用对应的API
    await callUpdateAPI(field, bookingId, newValue);
    
    message.success(`${getFieldDisplayName(field)}更新成功`);
    
    // 更新本地数据
    updateLocalData(bookingId, field, newValue);
    
    // 退出编辑状态
    exitEdit();
    
    // 刷新列表数据
    emit('query');
    
  } catch (error) {
    console.error(`更新${getFieldDisplayName(field)}失败:`, error);
    message.error(`更新${getFieldDisplayName(field)}失败: ${error.message || '未知错误'}`);
  } finally {
    editingLoading.value = false;
  }
};

// 取消编辑
const cancelEdit = () => {
  editingValue.value = originalValue.value;
  exitEdit();
};

// 退出编辑状态
const exitEdit = () => {
  editingCell.value = { bookingId: null, field: null };
  editingValue.value = null;
  originalValue.value = null;
  editingLoading.value = false;
};

// 检查值是否未变化
const isValueUnchanged = (field, newValue) => {
  switch(field) {
    case 'coach':
    case 'horse':
    case 'status':
      return newValue === originalValue.value;
    case 'time':
      if (!newValue || newValue.length !== 2) return true;
      const originalTimes = originalValue.value;
      return dayjs(newValue[0]).isSame(dayjs(originalTimes[0])) && 
             dayjs(newValue[1]).isSame(dayjs(originalTimes[1]));
    default:
      return true;
  }
};

// 调用更新API
const callUpdateAPI = async (field, bookingId, newValue) => {
  switch(field) {
    case 'coach':
      return await bookingApi.updateBookingCoach(bookingId, newValue);
    case 'horse':
      return await bookingApi.updateBookingHorse(bookingId, newValue);
    case 'time':
      if (!newValue || newValue.length !== 2) {
        throw new Error('请选择有效的时间范围');
      }
      const startTime = dayjs(newValue[0]).format('YYYY-MM-DD HH:mm:ss');
      const endTime = dayjs(newValue[1]).format('YYYY-MM-DD HH:mm:ss');
      return await bookingApi.updateBookingTime(bookingId, startTime, endTime);
    case 'status':
      return await bookingApi.updateBookingStatus(bookingId, newValue);
    default:
      throw new Error('未知的编辑字段');
  }
};

// 更新本地数据（乐观更新）
const updateLocalData = (bookingId, field, newValue) => {
  // 由于我们这里是展示组件，数据更新由父组件刷新
  // 这里暂时不做本地数据更新，直接刷新列表
};

// 获取字段显示名称
const getFieldDisplayName = (field) => {
  const names = {
    'coach': '教练',
    'horse': '马匹',
    'time': '时间',
    'status': '状态'
  };
  return names[field] || field;
};
const formatBookingDate = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('MM-DD HH:mm') : '-';
};

const formatUpdateTime = (dateTime) => {
  return dateTime ? dayjs(dateTime).format('MM-DD HH:mm') : '-';
};

const formatUpdateBy = (updateBy) => {
  return updateBy === 'system' ? '' : (updateBy || '');
};

const formatPrice = (price) => {
  return price ? Number(price).toFixed(2) : '0.00';
};

// 操作按钮处理函数
const handleCheckin = (record) => {
  console.log('核销预约:', record);
  // TODO: 实现核销逻辑
};

const handleCancel = (record) => {
  console.log('取消预约:', record);
  // TODO: 实现取消逻辑
};
</script>

<style lang="less" scoped>
.simple-booking-list {
  // 自适应表格样式
  :deep(.ant-table) {
    .ant-table-tbody > tr > td {
      padding: 8px 12px;
    }
    
    .ant-table-thead > tr > th {
      padding: 12px;
      background-color: #fafafa;
      font-weight: 500;
    }
    
    // 表格自适应
    .ant-table-container {
      width: 100%;
    }
  }
  
  // 价格信息样式
  .price-info {
    .price-row {
      font-size: 12px;
      line-height: 1.4;
      margin-bottom: 2px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &.total {
        font-weight: 600;
        color: #1890ff;
      }
    }
  }
  
  // 内联编辑样式
  .editable-cell {
    cursor: pointer;
    transition: all 0.2s;
    border-radius: 4px;
    padding: 2px 6px;
    
    &:hover {
      background-color: #f0f8ff;
      border: 1px dashed #1890ff;
    }
  }
  
  .inline-edit-container {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .edit-actions {
      display: flex;
      gap: 4px;
      
      .ant-btn {
        min-width: 24px;
        height: 24px;
        padding: 0;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  }
}
</style>