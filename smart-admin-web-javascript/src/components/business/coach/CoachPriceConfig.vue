<!--
  教练价格配置组件
  用于小组课教练个性化定价配置
  
  @Author: 1024创新实验室
  @Date: 2024-08-30
  @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="coach-price-config">
    <!-- 说明提示 -->
    <a-alert 
      message="小组课教练定价说明" 
      description="每个教练需要单独设置不同人数（2-6人）的课时费，未设置的教练将无法接受小组课预约。"
      type="info" 
      show-icon 
      closable
      style="margin-bottom: 16px;"
    />
    
    <!-- 教练数据获取失败提示 -->
    <a-alert
      v-if="hasDataIssues"
      message="部分教练信息获取失败"
      :description="dataIssueDescription"
      type="warning"
      show-icon
      style="margin-bottom: 16px;"
    />
    
    <!-- 教练未选择或无有效数据时的空状态 -->
    <div v-if="selectedCoaches.length === 0" class="no-coaches">
      <a-empty description="暂无可配置的教练">
        <template #image>
          <UserOutlined style="font-size: 64px; color: #bfbfbf;" />
        </template>
        <template #description>
          <span v-if="coachConfigSummary.total === 0">
            请先在上方选择授课教练
          </span>
          <span v-else>
            所选教练信息获取失败，请刷新页面重试
          </span>
        </template>
      </a-empty>
    </div>
    
    <!-- 教练配置卡片区域 -->
    <div v-else class="coach-cards">
      <a-card 
        v-for="coach in selectedCoaches" 
        :key="coach.coachId"
        size="small"
        class="coach-card"
        :class="{ 'incomplete-config': !isCoachConfigComplete(coach.coachId) }"
      >
        <!-- 卡片标题 -->
        <template #title>
          <div class="card-title">
            <span class="coach-name">{{ coach.coachName || coach.actualName }}</span>
            <span v-if="coach.coachFee" class="base-fee">教练费: ¥{{ coach.coachFee }}</span>
          </div>
        </template>
        
        <!-- 卡片额外信息 -->
        <template #extra>
          <a-tag 
            :color="isCoachConfigComplete(coach.coachId) ? 'success' : 'warning'"
            style="margin: 0;"
          >
            {{ isCoachConfigComplete(coach.coachId) ? '配置完整' : '需要配置' }}
          </a-tag>
        </template>
        
        <!-- 基础价格设置 -->
        <div class="base-price-section">
          <h4 class="section-title">基础价格设置</h4>
          <div class="base-price-row">
            <span class="participant-label">1人(基础)</span>
            <a-input-number
              v-model:value="coachPriceMap[coach.coachId][1]"
              :min="10" 
              :max="10000"
              :step="10"
              :formatter="value => value ? `¥ ${value}` : ''"
              :parser="value => value ? value.replace(/¥\s?|(,*)/g, '') : ''"
              placeholder="请设置基础价格"
              class="price-input"
              size="small"
              @change="onBasePriceChange(coach.coachId, $event)"
            />
            <div class="price-status">
              <CheckCircleOutlined 
                v-if="getCoachPrice(coach.coachId, 1)" 
                style="color: #52c41a;"
              />
              <ExclamationCircleOutlined 
                v-else 
                style="color: #faad14;"
              />
            </div>
          </div>
        </div>
        
        <!-- 已配置人数 -->
        <div class="configured-counts-section">
          <div class="section-header">
            <h4 class="section-title">已配置人数</h4>
            <a-button 
              size="small" 
              type="primary" 
              ghost 
              @click="showAddCountModal(coach.coachId)"
              :disabled="getAvailableCounts(coach.coachId).length === 0"
            >
              <PlusOutlined />
              添加人数
            </a-button>
          </div>
          
          <div class="price-config-table">
            <div class="config-header">
              <span class="participant-header">人数</span>
              <span class="price-header">课时费</span>
              <span class="status-header">状态</span>
              <span class="action-header">操作</span>
            </div>
            
            <div 
              v-for="count in getCoachParticipantCounts(coach.coachId)" 
              :key="count"
              class="config-row"
              :class="{ 'has-value': getCoachPrice(coach.coachId, count) }"
            >
              <span class="participant-label">{{ count }}人</span>
              
              <div class="calculated-price">
                ¥{{ getCalculatedPrice(coach.coachId, count) }}
                <small class="price-hint">自动计算</small>
              </div>
              
              <div class="price-status">
                <CheckCircleOutlined 
                  v-if="getCalculatedPrice(coach.coachId, count) > 0" 
                  style="color: #52c41a;"
                />
                <ExclamationCircleOutlined 
                  v-else 
                  style="color: #faad14;"
                />
              </div>
              
              <a-button 
                size="small" 
                danger 
                type="text"
                @click="removeParticipantCount(coach.coachId, count)"
                :disabled="getCoachParticipantCounts(coach.coachId).length <= 1"
              >
                <DeleteOutlined />
              </a-button>
            </div>
            
            <!-- 空状态 -->
            <div v-if="getCoachParticipantCounts(coach.coachId).length === 0" class="empty-counts">
              <a-empty size="small" description="暂无配置人数">
                <a-button size="small" type="primary" @click="showAddCountModal(coach.coachId)">
                  <PlusOutlined />
                  添加人数配置
                </a-button>
              </a-empty>
            </div>
          </div>
        </div>
        
        <!-- 配置进度 -->
        <div class="config-progress">
          <a-progress 
            :percent="getConfigProgress(coach.coachId)" 
            size="small"
            :stroke-color="getConfigProgress(coach.coachId) === 100 ? '#52c41a' : '#1890ff'"
          />
          <span class="progress-text">
            已配置 {{ getConfiguredCount(coach.coachId) }}/{{ participantCounts.length }} 个价格
          </span>
        </div>
        
        <!-- 快速操作按钮 -->
        <a-divider style="margin: 12px 0 8px 0;" />
        <div class="quick-actions">
          <a-space size="small" wrap>
            <a-button 
              size="small" 
              @click="showQuickSetModal(coach)"
            >
              <ThunderboltOutlined />
              快速设置
            </a-button>
            <a-button 
              size="small" 
              danger 
              @click="clearCoachPrices(coach.coachId)"
              :disabled="getConfiguredCount(coach.coachId) === 0"
            >
              <ClearOutlined />
              清空
            </a-button>
          </a-space>
        </div>
      </a-card>
    </div>
    
    <!-- 批量操作区域 -->
    <a-card 
      v-if="selectedCoaches.length > 0" 
      title="批量操作" 
      size="small" 
      class="batch-operations"
    >
      <a-space wrap>
        <a-button @click="showBatchPriceModal">
          <SettingOutlined />
          批量设置价格
        </a-button>
        <a-button 
          @click="copyFirstCoachToAll" 
          :disabled="selectedCoaches.length < 2 || getConfiguredCount(selectedCoaches[0].coachId) === 0"
        >
          <CopyOutlined />
          复制第一个教练价格
        </a-button>
        <a-button danger @click="clearAllPrices">
          <DeleteOutlined />
          清空所有价格
        </a-button>
        
        <!-- 配置统计 -->
        <div class="batch-stats">
          <a-statistic 
            title="配置完成率" 
            :value="getOverallProgress()" 
            suffix="%" 
            :precision="1"
            style="display: inline-block; margin-left: 16px;"
          />
        </div>
      </a-space>
    </a-card>
    
    <!-- 快速设置弹窗 -->
    <a-modal
      v-model:visible="quickSetModalVisible"
      :title="`快速设置 - ${currentCoach?.coachName || currentCoach?.actualName}`"
      @ok="applyQuickSet"
      width="500px"
    >
      <a-form layout="vertical">
        <a-form-item label="设置方式">
          <a-radio-group v-model:value="quickSetMode">
            <a-radio value="fixed">固定价格</a-radio>
            <a-radio value="descending">递减定价</a-radio>
            <a-radio value="percentage">按比例设置</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <!-- 固定价格模式 -->
        <div v-if="quickSetMode === 'fixed'">
          <a-form-item label="统一价格">
            <a-input-number
              v-model:value="quickSetPrice"
              :min="10" :max="10000"
              :formatter="value => `¥ ${value}`"
              :parser="value => value.replace(/¥\s?|(,*)/g, '')"
              placeholder="请输入统一价格"
              style="width: 100%"
            />
          </a-form-item>
        </div>
        
        <!-- 递减定价模式 -->
        <div v-if="quickSetMode === 'descending'">
          <a-form-item label="起始价格 (2人)">
            <a-input-number
              v-model:value="quickSetStartPrice"
              :min="10" :max="10000"
              :formatter="value => `¥ ${value}`"
              :parser="value => value.replace(/¥\s?|(,*)/g, '')"
              placeholder="请输入起始价格"
              style="width: 100%"
            />
          </a-form-item>
          <a-form-item label="递减金额">
            <a-input-number
              v-model:value="quickSetDecrement"
              :min="1" :max="500"
              :formatter="value => `¥ ${value}`"
              :parser="value => value.replace(/¥\s?|(,*)/g, '')"
              placeholder="每增加1人减少的金额"
              style="width: 100%"
            />
            <div class="decrement-hint">
              <small>例如：起始200元，递减20元 → 2人200元，3人180元，4人160元...</small>
            </div>
          </a-form-item>
        </div>
        
        <!-- 按比例设置模式 -->
        <div v-if="quickSetMode === 'percentage'">
          <a-form-item label="基准价格">
            <a-input-number
              v-model:value="quickSetBasePrice"
              :min="10" :max="10000"
              :formatter="value => `¥ ${value}`"
              :parser="value => value.replace(/¥\s?|(,*)/g, '')"
              placeholder="基准价格"
              style="width: 100%"
            />
          </a-form-item>
          <a-form-item label="人数折扣比例">
            <div class="percentage-config">
              <div v-for="count in participantCounts" :key="count" class="percentage-item">
                <span>{{ count }}人:</span>
                <a-input-number
                  v-model:value="quickSetPercentages[count]"
                  :min="0.1" :max="2.0" :step="0.1"
                  :formatter="value => `${value}倍`"
                  :parser="value => value.replace('倍', '')"
                  size="small"
                />
              </div>
            </div>
          </a-form-item>
        </div>
        
        <!-- 预览效果 -->
        <a-form-item label="价格预览" v-if="showQuickSetPreview">
          <div class="quick-set-preview">
            <a-tag v-for="preview in quickSetPreviewList" :key="preview.count" color="blue">
              {{ preview.count }}人: ¥{{ preview.price }}
            </a-tag>
          </div>
        </a-form-item>
      </a-form>
    </a-modal>
    
    <!-- 批量价格设置弹窗 -->
    <a-modal
      v-model:visible="batchPriceModalVisible"
      title="批量设置价格"
      @ok="applyBatchPrice"
      width="600px"
    >
      <a-form layout="vertical">
        <a-form-item label="选择教练">
          <a-checkbox-group v-model:value="selectedBatchCoachIds">
            <a-row>
              <a-col :span="12" v-for="coach in selectedCoaches" :key="coach.coachId">
                <a-checkbox :value="coach.coachId">
                  {{ coach.coachName || coach.actualName }}
                </a-checkbox>
              </a-col>
            </a-row>
          </a-checkbox-group>
        </a-form-item>
        
        <a-form-item label="批量价格设置">
          <div class="batch-price-table">
            <div class="batch-price-header">
              <span>人数</span>
              <span>价格</span>
              <span>操作</span>
            </div>
            <div 
              v-for="count in participantCounts" 
              :key="count"
              class="batch-price-row"
            >
              <span class="participant-label">{{ count }}人：</span>
              <a-input-number
                v-model:value="batchPrices[count]"
                :min="10" :max="10000"
                :formatter="value => value ? `¥ ${value}` : ''"
                :parser="value => value ? value.replace(/¥\s?|(,*)/g, '') : ''"
                placeholder="请输入价格"
                size="small"
              />
              <a-button 
                size="small" 
                @click="clearBatchPrice(count)"
                :disabled="!batchPrices[count]"
              >
                清空
              </a-button>
            </div>
          </div>
        </a-form-item>
        
        <a-form-item>
          <a-space>
            <a-button size="small" @click="fillBatchPricesSequential">
              按序递减填充
            </a-button>
            <a-button size="small" @click="clearAllBatchPrices">
              清空所有
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue';
import { message, Modal } from 'ant-design-vue';
import {
  UserOutlined,
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  CopyOutlined,
  ThunderboltOutlined,
  ClearOutlined,
  SettingOutlined,
  DeleteOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  selectedCoaches: {
    type: Array,
    default: () => []
  },
  // 新增：配置状态信息
  coachConfigSummary: {
    type: Object,
    default: () => ({
      total: 0,
      valid: 0,
      failed: 0,
      hasIssues: false
    })
  }
});

const emit = defineEmits(['update:modelValue']);

// 响应式数据
const quickSetModalVisible = ref(false);
const batchPriceModalVisible = ref(false);
const currentCoach = ref(null);
const quickSetMode = ref('fixed');
const quickSetPrice = ref(null);
const quickSetStartPrice = ref(null);
const quickSetDecrement = ref(20);
const quickSetBasePrice = ref(null);
const quickSetPercentages = ref({ 2: 1.0, 3: 0.8, 4: 0.7, 5: 0.6, 6: 0.5 });
const selectedBatchCoachIds = ref([]);
const batchPrices = ref({});

// 内部状态管理 - 使用响应式对象管理教练价格
const coachPriceMap = ref({});

// 同步内部状态到父组件
const syncToParent = () => {
  const result = [];
  
  props.selectedCoaches.forEach(coach => {
    const prices = {};
    let hasPrice = false;
    
    props.participantCounts.forEach(count => {
      const price = coachPriceMap.value[coach.coachId]?.[count];
      if (price && price > 0) {
        prices[count] = price;
        hasPrice = true;
      }
    });
    
    if (hasPrice) {
      result.push({
        coachId: coach.coachId,
        coachName: coach.coachName || coach.actualName,
        prices: prices
      });
    }
  });
  
  emit('update:modelValue', result);
};

// 监听选中教练变化，初始化价格配置
watch(() => props.selectedCoaches, (newCoaches) => {
  // 初始化新教练的价格配置
  newCoaches.forEach(coach => {
    if (!coachPriceMap.value[coach.coachId]) {
      coachPriceMap.value[coach.coachId] = {};
      props.participantCounts.forEach(count => {
        coachPriceMap.value[coach.coachId][count] = null;
      });
    }
  });
  
  // 移除不再选中的教练配置
  Object.keys(coachPriceMap.value).forEach(coachIdStr => {
    const coachId = parseInt(coachIdStr);
    if (!newCoaches.find(c => c.coachId === coachId)) {
      delete coachPriceMap.value[coachIdStr];
    }
  });
  
  // 同步到父组件
  syncToParent();
}, { immediate: true, deep: true });

// 监听外部数据变化，同步到内部状态
watch(() => props.modelValue, (newValue) => {
  if (!newValue || newValue.length === 0) {
    // 清空内部状态
    Object.keys(coachPriceMap.value).forEach(coachId => {
      props.participantCounts.forEach(count => {
        coachPriceMap.value[coachId][count] = null;
      });
    });
    return;
  }
  
  // 同步外部数据到内部状态
  newValue.forEach(item => {
    if (!coachPriceMap.value[item.coachId]) {
      coachPriceMap.value[item.coachId] = {};
    }
    
    Object.entries(item.prices || {}).forEach(([count, price]) => {
      coachPriceMap.value[item.coachId][parseInt(count)] = price;
    });
  });
}, { immediate: true, deep: true });

// 计算属性
const showQuickSetPreview = computed(() => {
  return quickSetMode.value && (quickSetPrice.value || quickSetStartPrice.value || quickSetBasePrice.value);
});

// 新增：是否有数据问题
const hasDataIssues = computed(() => {
  return props.coachConfigSummary.hasIssues;
});

// 新增：数据问题描述
const dataIssueDescription = computed(() => {
  const { total, valid, failed } = props.coachConfigSummary;
  return `已选择${total}个教练，${failed}个教练信息获取失败，当前可配置${valid}个教练。`;
});

const quickSetPreviewList = computed(() => {
  const previews = [];
  
  if (quickSetMode.value === 'fixed' && quickSetPrice.value) {
    props.participantCounts.forEach(count => {
      previews.push({ count, price: quickSetPrice.value });
    });
  } else if (quickSetMode.value === 'descending' && quickSetStartPrice.value && quickSetDecrement.value) {
    props.participantCounts.forEach((count, index) => {
      const price = quickSetStartPrice.value - (index * quickSetDecrement.value);
      if (price > 10) {
        previews.push({ count, price });
      }
    });
  } else if (quickSetMode.value === 'percentage' && quickSetBasePrice.value) {
    props.participantCounts.forEach(count => {
      const multiplier = quickSetPercentages.value[count] || 1.0;
      const price = Math.round(quickSetBasePrice.value * multiplier);
      previews.push({ count, price });
    });
  }
  
  return previews;
});

// 方法
const getCoachPrice = (coachId, count) => {
  return coachPriceMap.value[coachId]?.[count] || null;
};

const onPriceChange = (coachId, count, value) => {
  if (!coachPriceMap.value[coachId]) {
    coachPriceMap.value[coachId] = {};
  }
  
  coachPriceMap.value[coachId][count] = value;
  syncToParent();
};

const isCoachConfigComplete = (coachId) => {
  return props.participantCounts.every(count => {
    const price = getCoachPrice(coachId, count);
    return price && price > 0;
  });
};

const getConfiguredCount = (coachId) => {
  return props.participantCounts.filter(count => {
    const price = getCoachPrice(coachId, count);
    return price && price > 0;
  }).length;
};

const getConfigProgress = (coachId) => {
  const configured = getConfiguredCount(coachId);
  const total = props.participantCounts.length;
  return total > 0 ? Math.round((configured / total) * 100) : 0;
};

const getOverallProgress = () => {
  if (props.selectedCoaches.length === 0) return 0;
  
  const totalSlots = props.selectedCoaches.length * props.participantCounts.length;
  let configuredSlots = 0;
  
  props.selectedCoaches.forEach(coach => {
    configuredSlots += getConfiguredCount(coach.coachId);
  });
  
  return totalSlots > 0 ? (configuredSlots / totalSlots) * 100 : 0;
};

const copyBasePrice = (coach) => {
  if (!coach.coachFee || coach.coachFee <= 0) {
    message.warning('该教练未设置教练费');
    return;
  }
  
  props.participantCounts.forEach(count => {
    coachPriceMap.value[coach.coachId][count] = coach.coachFee;
  });
  
  syncToParent();
  message.success(`已将${coach.coachName || coach.actualName}的教练费应用到所有人数`);
};

const showQuickSetModal = (coach) => {
  currentCoach.value = coach;
  quickSetPrice.value = coach.coachFee || 200;
  quickSetStartPrice.value = coach.coachFee || 200;
  quickSetBasePrice.value = coach.coachFee || 200;
  quickSetModalVisible.value = true;
};

const applyQuickSet = () => {
  if (!currentCoach.value) return;
  
  const coachId = currentCoach.value.coachId;
  let applied = false;
  
  if (quickSetMode.value === 'fixed' && quickSetPrice.value) {
    props.participantCounts.forEach(count => {
      coachPriceMap.value[coachId][count] = quickSetPrice.value;
    });
    applied = true;
  } else if (quickSetMode.value === 'descending' && quickSetStartPrice.value && quickSetDecrement.value) {
    props.participantCounts.forEach((count, index) => {
      const price = quickSetStartPrice.value - (index * quickSetDecrement.value);
      if (price >= 10) {
        coachPriceMap.value[coachId][count] = price;
      }
    });
    applied = true;
  } else if (quickSetMode.value === 'percentage' && quickSetBasePrice.value) {
    props.participantCounts.forEach(count => {
      const multiplier = quickSetPercentages.value[count] || 1.0;
      const price = Math.round(quickSetBasePrice.value * multiplier);
      if (price >= 10) {
        coachPriceMap.value[coachId][count] = price;
      }
    });
    applied = true;
  }
  
  if (applied) {
    syncToParent();
    quickSetModalVisible.value = false;
    message.success('快速设置完成');
  } else {
    message.warning('请完整填写配置参数');
  }
};

const clearCoachPrices = (coachId) => {
  Modal.confirm({
    title: '确认清空',
    content: '确定要清空该教练的所有价格配置吗？',
    onOk() {
      props.participantCounts.forEach(count => {
        coachPriceMap.value[coachId][count] = null;
      });
      syncToParent();
      message.success('已清空该教练的价格配置');
    }
  });
};

const showBatchPriceModal = () => {
  selectedBatchCoachIds.value = props.selectedCoaches.map(coach => coach.coachId);
  batchPrices.value = {};
  batchPriceModalVisible.value = true;
};

const applyBatchPrice = () => {
  if (selectedBatchCoachIds.value.length === 0) {
    message.warning('请选择要设置的教练');
    return;
  }
  
  const hasValidPrice = Object.values(batchPrices.value).some(price => price && price > 0);
  if (!hasValidPrice) {
    message.warning('请至少设置一个价格');
    return;
  }
  
  selectedBatchCoachIds.value.forEach(coachId => {
    Object.entries(batchPrices.value).forEach(([count, price]) => {
      if (price && price > 0) {
        coachPriceMap.value[coachId][parseInt(count)] = price;
      }
    });
  });
  
  syncToParent();
  batchPriceModalVisible.value = false;
  message.success(`已批量设置${selectedBatchCoachIds.value.length}个教练的价格`);
};

const clearBatchPrice = (count) => {
  delete batchPrices.value[count];
};

const fillBatchPricesSequential = () => {
  const startPrice = 200;
  const decrement = 20;
  
  props.participantCounts.forEach((count, index) => {
    const price = startPrice - (index * decrement);
    if (price >= 10) {
      batchPrices.value[count] = price;
    }
  });
};

const clearAllBatchPrices = () => {
  batchPrices.value = {};
};

const copyFirstCoachToAll = () => {
  if (props.selectedCoaches.length < 2) return;
  
  const firstCoachId = props.selectedCoaches[0].coachId;
  const firstCoachPrices = coachPriceMap.value[firstCoachId];
  
  if (!firstCoachPrices || Object.values(firstCoachPrices).every(price => !price)) {
    message.warning('第一个教练还没有配置价格');
    return;
  }
  
  Modal.confirm({
    title: '确认复制',
    content: `确定要将"${props.selectedCoaches[0].coachName || props.selectedCoaches[0].actualName}"的价格配置复制到其他教练吗？`,
    onOk() {
      props.selectedCoaches.slice(1).forEach(coach => {
        props.participantCounts.forEach(count => {
          if (firstCoachPrices[count]) {
            coachPriceMap.value[coach.coachId][count] = firstCoachPrices[count];
          }
        });
      });
      
      syncToParent();
      message.success('价格配置复制完成');
    }
  });
};

const clearAllPrices = () => {
  Modal.confirm({
    title: '确认清空',
    content: '确定要清空所有教练的价格配置吗？此操作不可恢复。',
    onOk() {
      props.selectedCoaches.forEach(coach => {
        props.participantCounts.forEach(count => {
          coachPriceMap.value[coach.coachId][count] = null;
        });
      });
      
      syncToParent();
      message.success('已清空所有价格配置');
    }
  });
};
</script>

<style scoped>
.coach-price-config {
  background: #fafafa;
  padding: 16px;
  border-radius: 6px;
}

.no-coaches {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 6px;
  border: 2px dashed #d9d9d9;
}

.coach-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
  margin-bottom: 16px;
}

.coach-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: all 0.3s ease;
}

.coach-card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
}

.coach-card.incomplete-config {
  border: 2px dashed #faad14;
  background: #fffbf0;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.coach-name {
  font-weight: 600;
  color: #262626;
}

.base-fee {
  font-size: 12px;
  color: #666;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 4px;
}

.price-config-table {
  margin: 12px 0;
}

.config-header {
  display: grid;
  grid-template-columns: 60px 100px 1fr 60px;
  gap: 12px;
  padding: 8px 0;
  font-weight: 600;
  color: #666;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 8px;
}

.config-row {
  display: grid;
  grid-template-columns: 60px 100px 1fr 60px;
  gap: 12px;
  align-items: center;
  padding: 6px 0;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.config-row:hover {
  background-color: #f9f9f9;
}

.config-row.has-value {
  background-color: #f6ffed;
}

.participant-label {
  text-align: center;
  font-weight: 500;
  color: #595959;
}

.price-input {
  width: 100%;
}

.price-status {
  text-align: center;
}

.reference-price {
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.single-price-ref {
  font-weight: 500;
  color: #1890ff;
  font-size: 13px;
}

.price-hint {
  color: #999;
  font-size: 10px;
  line-height: 1;
}

.config-progress {
  margin: 12px 0 8px 0;
}

.progress-text {
  font-size: 12px;
  color: #666;
  margin-left: 8px;
}

.quick-actions {
  text-align: left;
}

.batch-operations {
  margin-top: 16px;
}

.batch-stats {
  display: inline-block;
  vertical-align: top;
}

.decrement-hint {
  margin-top: 4px;
  color: #666;
}

.percentage-config {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}

.percentage-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.quick-set-preview {
  padding: 8px;
  background: #f9f9f9;
  border-radius: 4px;
}

.batch-price-table {
  border: 1px solid #f0f0f0;
  border-radius: 4px;
}

.batch-price-header {
  display: grid;
  grid-template-columns: 80px 1fr 80px;
  gap: 12px;
  padding: 12px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
  font-weight: 600;
}

.batch-price-row {
  display: grid;
  grid-template-columns: 80px 1fr 80px;
  gap: 12px;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #f9f9f9;
}

.batch-price-row:last-child {
  border-bottom: none;
}
</style>