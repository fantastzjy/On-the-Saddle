<!--
  * 课程新增/编辑页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <a-card :bordered="false">
    <template #title>
      <a-space>
        <ArrowLeftOutlined @click="goBack" style="cursor: pointer;" />
        {{ isEdit ? '编辑课程' : '新增课程' }}
      </a-space>
    </template>

    <template #extra>
      <a-space>
        <a-button @click="goBack">取消</a-button>
        <a-button @click="resetForm">重置</a-button>
        <a-button type="primary" @click="onSubmit" :loading="submitLoading">
          {{ isEdit ? '更新' : '创建' }}
        </a-button>
      </a-space>
    </template>

    <a-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 20 }"
      class="product-form"
    >
      <!-- 基础信息 -->
      <a-card size="small" title="基础信息" class="form-section">
        <!-- 第一行：课程类型 + 课程分类 -->
        <a-row :gutter="24" justify="center">
          <a-col :span="18" :md="16" :lg="14" :xl="12">
            <a-form-item label="课程类型" name="productType" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
              <a-radio-group v-model:value="formData.productType" @change="onProductTypeChange">
                <a-radio
                  v-for="item in Object.values(PRODUCT_MANAGEMENT_TYPE_ENUM)"
                  :key="item.value"
                  :value="item.value"
                >
                  {{ item.desc }}
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第二行：课程分类（仅当选择课程时显示） -->
        <a-row :gutter="24" justify="center" v-if="formData.productType === 1">
          <a-col :span="18" :md="16" :lg="14" :xl="12">
            <a-form-item label="课程分类" name="dynamicConfig.classType" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
              <a-radio-group v-model:value="formData.dynamicConfig.classType" @change="onClassTypeChange">
                <a-radio
                  v-for="item in Object.values(COURSE_CLASS_TYPE_ENUM)"
                  :key="item.value"
                  :value="item.value"
                >
                  {{ item.desc }}
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第三行：课程名称 -->
        <a-row :gutter="24" justify="center">
          <a-col :span="18" :md="16" :lg="14" :xl="12">
            <a-form-item label="课程名称" name="productName" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
              <a-input v-model:value="formData.productName" placeholder="请输入课程名称" />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 第四行：授课教练（仅课程类型显示） -->
        <a-row :gutter="24" justify="center" v-if="formData.productType === 1">
          <a-col :span="18" :md="16" :lg="14" :xl="12">
            <a-form-item label="授课教练" name="coachIds" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
              <CoachSelector
                v-model:value="formData.coachIds"
                :custom-params="{ showCoachFee: true }"
                mode="multiple"
                placeholder="请选择可授课的教练（可多选）"
                :auto-load="true"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>

      <!-- 动态表单配置 -->
      <a-card size="small" :title="configSectionTitle" class="form-section" v-if="formData.productType">
        <!-- 体验课特殊布局 -->
        <div v-if="isExperienceClass" class="experience-form-layout">
          <a-row :gutter="24" justify="center">
            <a-col :span="18" :md="16" :lg="14" :xl="12">
              <a-form-item label="课程时长（分钟）" name="dynamicConfig.durationMinutes" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
                <a-input-number
                  v-model:value="formData.dynamicConfig.durationMinutes"
                  placeholder="请输入课程时长"
                  :min="30"
                  :max="300"
                  style="width: 100%"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" justify="center">
            <a-col :span="18" :md="16" :lg="14" :xl="12">
              <a-form-item label="最大人数" name="dynamicConfig.maxStudents" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
                <a-input-number
                  v-model:value="formData.dynamicConfig.maxStudents"
                  placeholder="请输入最大人数"
                  :min="1"
                  :max="10"
                  style="width: 100%"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" justify="center">
            <a-col :span="18" :md="16" :lg="14" :xl="12">
              <a-form-item label="课时费" name="dynamicConfig.sessionFee" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
                <a-input-number
                  v-model:value="formData.dynamicConfig.sessionFee"
                  placeholder="请输入课时费"
                  :min="0"
                  :max="9999"
                  style="width: 100%"
                  :formatter="value => `¥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                  :parser="value => value.replace(/¥\s?|(,*)/g, '')"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </div>

        <!-- 正常课程和课时包布局 -->
        <div v-else class="normal-form-layout">
          <DynamicFormRenderer
            v-model:value="formData.dynamicConfig"
            :form-config="dynamicFormConfig"
            :loading="configLoading"
            :form-props="{ labelCol: { span: 6 }, wrapperCol: { span: 18 } }"
            @validate="onDynamicFormValidate"
            @change="onDynamicFormChange"
          />
        </div>
      </a-card>

      <!-- 小组课教练价格配置 -->
      <a-card 
        v-if="isGroupCourse" 
        size="small" 
        class="form-section coach-price-section"
      >
        <template #title>
          <span style="color: #1890ff; font-weight: 600;">
            👥 小组课教练定价配置
          </span>
          <!-- 配置状态指示器 -->
          <a-tag 
            v-if="coachConfigSummary.hasIssues"
            color="warning" 
            size="small"
            style="margin-left: 8px;"
          >
            {{ coachConfigSummary.valid }}/{{ coachConfigSummary.total }} 教练可配置
          </a-tag>
        </template>
        
        <!-- 加载状态 -->
        <div v-if="coachDetailsLoading" class="loading-state" style="text-align: center; padding: 40px;">
          <a-spin size="large">
            <template #tip>正在获取教练信息...</template>
          </a-spin>
        </div>
        
        <!-- 配置组件 -->
        <CoachPriceConfig
          v-else
          v-model="formData.coachPriceList"
          :selected-coaches="selectedCoachesWithFee"
          :participant-counts="[2, 3, 4, 5, 6]"
          :coach-config-summary="coachConfigSummary"
        />
      </a-card>

    </a-form>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch, nextTick } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { ArrowLeftOutlined } from '@ant-design/icons-vue';
import { productApi } from '/@/api/business/product/product-api';
import { coachApi } from '/@/api/business/coach/coach-api';
import {
  PRODUCT_MANAGEMENT_TYPE_ENUM,
  PRODUCT_FORM_RULES,
  PRODUCT_DEFAULT_CONFIG,
  COURSE_CLASS_TYPE_ENUM
} from '/@/constants/business/product/product-const';
import DynamicFormRenderer from './components/DynamicFormRenderer.vue';
import CoachPriceConfig from '/@/components/business/coach/CoachPriceConfig.vue';
import { CoachSelector } from '/@/components/business/selector';

const router = useRouter();
const route = useRoute();

// ======================== 响应式数据 ========================
const formRef = ref();
const submitLoading = ref(false);
const configLoading = ref(false);
const dynamicFormConfig = ref([]);
const dynamicFormValid = ref(false);
const needsDetailedConfig = ref(false);
const baseFormConfig = ref(null);
const currentClassType = ref(null);

// 教练详情管理
const selectedCoachDetails = ref([]);
const coachDetailsLoading = ref(false);
const failedCoachIds = ref([]);

const formData = reactive({
  // 主表字段 m_product
  productId: null,
  productName: '',
  productCode: '',
  productType: null,
  subType: '',

  // 教练绑定字段
  coachIds: [],
  
  // 教练价格配置（小组课专用）
  coachPriceList: [],

  // 动态配置字段 - 严格按照数据库表结构
  dynamicConfig: {
    // 课程字段 m_product_course (productType=1)
    classType: null,
    durationMinutes: null,
    durationPeriods: null,
    maxStudents: null,
    coachFee: null,
    horseFee: null,
    multiPriceConfig: null,

    // 课时包字段 m_product_package (productType=2)
    details: '',
    price: null,
    totalSessions: null,
    validityDays: null,
    stockQuantity: null,

    // 活动字段 m_product_activity (productType=3)
    activityName: '',
    activityDetails: '',
    startTime: null,
    endTime: null,
    activityLocation: '',
    price: null,
    maxParticipants: null,
    refundRule: '',
    detailImages: []
  }
});

const formRules = { 
  ...PRODUCT_FORM_RULES,
  coachIds: [
    { 
      validator: (rule, value) => {
        if (isGroupCourse.value && (!value || value.length === 0)) {
          return Promise.reject('小组课必须选择授课教练');
        }
        return Promise.resolve();
      },
      trigger: 'change'
    }
  ],
  coachPriceList: [
    {
      validator: (rule, value) => {
        if (isGroupCourse.value && (!value || value.length === 0)) {
          return Promise.reject('小组课必须配置教练价格');
        }
        return Promise.resolve();
      },
      trigger: 'change'
    }
  ]
};

// ======================== 计算属性 ========================
const isEdit = computed(() => {
  return route.params.id && route.params.id !== 'add';
});

const configSectionTitle = computed(() => {
  switch (formData.productType) {
    case 1:
      return '课程配置';
    case 2:
      return '课时包配置';
    default:
      return '商品配置';
  }
});

// 检测是否为体验课
const isExperienceClass = computed(() => {
  return formData.productType === 1 && formData.productName && formData.productName.includes('体验课');
});

// 检测是否为小组课
const isGroupCourse = computed(() => {
  return formData.productType === 1 && formData.dynamicConfig.classType === 2;
});

// 获取选中的教练及其费用信息（严格版本 - 只返回成功获取详情的教练）
const selectedCoachesWithFee = computed(() => {
  // 增强的验证逻辑，添加详细日志
  const validCoaches = selectedCoachDetails.value.filter(coach => {
    const hasValidId = coach.coachId && coach.coachId > 0;
    const hasValidName = coach.coachName && coach.coachName.trim().length > 0;
    const hasValidFee = coach.coachFee !== null && 
                        coach.coachFee !== undefined && 
                        coach.coachFee >= 0;
    
    console.log('教练验证:', {
      coachId: coach.coachId,
      coachName: coach.coachName,
      coachFee: coach.coachFee,
      hasValidId,
      hasValidName,
      hasValidFee,
      valid: hasValidId && hasValidName && hasValidFee
    });
    
    return hasValidId && hasValidName && hasValidFee;
  });
  
  console.log('原始教练数据:', selectedCoachDetails.value);
  console.log('过滤后有效教练数据:', validCoaches);
  
  return validCoaches;
});

// 检查是否有教练配置不完整
const hasIncompleteCoachData = computed(() => {
  const selectedCount = formData.coachIds?.length || 0;
  const validCount = selectedCoachesWithFee.value.length;
  return selectedCount > validCount;
});

// 获取配置状态摘要
const coachConfigSummary = computed(() => {
  const total = formData.coachIds?.length || 0;
  const valid = selectedCoachesWithFee.value.length;
  const failed = failedCoachIds.value.length;
  
  return {
    total,
    valid,
    failed,
    hasIssues: failed > 0
  };
});

// ======================== 初始化 ========================
onMounted(() => {
  if (isEdit.value) {
    loadProductDetail();
  }
});

// ======================== 监听器 ========================

// 监听教练ID变化，加载教练详情
watch(() => formData.coachIds, (newIds, oldIds) => {
  if (JSON.stringify(newIds) !== JSON.stringify(oldIds)) {
    console.log('教练选择变化:', { oldIds, newIds });
    loadCoachDetails(newIds);
  }
}, { immediate: true });

// 移除productType的watch监听器，避免与onProductTypeChange重复调用
// watch(() => formData.productType, (newType) => {
//   if (newType) {
//     loadFormConfig(newType);
//   }
// });

// 监听课程分类变化，简化条件确保首次选择也能触发
watch(() => formData.dynamicConfig.classType, (newClassType, oldClassType) => {
  // 课程类型下，当classType发生变化时重新加载配置（移除过于严格的条件）
  if (formData.productType === 1 && newClassType && newClassType !== oldClassType) {
    console.log('课程分类切换:', { oldClassType, newClassType });
    // 课程类型内部分类切换，重新加载详细配置
    loadDetailedFormConfig(formData.productType, newClassType);
  }
});

// 监听课程名称变化，检测体验课并动态调整时间字段
watch(() => formData.productName, (newName, oldName) => {
  if (formData.productType === 1 && newName !== oldName) {
    // 检测是否为体验课
    const isExperienceClass = newName && newName.includes('体验课');
    const wasExperienceClass = oldName && oldName.includes('体验课');

    // 只有在体验课状态真正发生变化，且不是在配置加载过程中时才重新加载
    if (isExperienceClass !== wasExperienceClass && !configLoading.value) {
      // 体验课状态发生变化，进行数据转换和重新加载配置
      handleTimeFieldConversion(isExperienceClass, wasExperienceClass);
      loadFormConfigWithExperienceDetection();
      // 清除课程分类字段的验证状态，因为体验课会自动设置classType
      nextTick(() => {
        formRef.value?.clearValidate('dynamicConfig.classType');
      });
    }
  }
});

// ======================== 方法 ========================

/**
 * 加载教练详情（严格版本 - 无默认数据）
 */
async function loadCoachDetails(coachIds) {
  if (!coachIds || coachIds.length === 0) {
    selectedCoachDetails.value = [];
    failedCoachIds.value = [];
    return;
  }
  
  try {
    coachDetailsLoading.value = true;
    failedCoachIds.value = [];
    
    console.log('开始获取教练详情:', coachIds);
    
    // 批量查询教练详情
    const detailPromises = coachIds.map(async (coachId) => {
      try {
        const response = await coachApi.detail(coachId);
        if (response.ok && response.data) {
          return {
            success: true,
            coachId,
            data: response.data
          };
        } else {
          return {
            success: false,
            coachId,
            error: response.msg || '获取教练详情失败'
          };
        }
      } catch (error) {
        return {
          success: false,
          coachId,
          error: error.message || '网络请求失败'
        };
      }
    });
    
    const responses = await Promise.all(detailPromises);
    
    console.log('所有API响应:', responses);
    
    // 分离成功和失败的结果
    const successResponses = responses.filter(res => res.success);
    const failedResponses = responses.filter(res => !res.success);
    
    console.log('成功响应数据:', successResponses.map(res => res.data));
    console.log('失败响应:', failedResponses);
    
    // 更新成功的教练详情
    selectedCoachDetails.value = successResponses.map(res => ({
      coachId: res.data.coachId,
      coachName: res.data.userName,          // ✅ 使用正确的字段名
      actualName: res.data.userName,         // ✅ 使用正确的字段名
      coachFee: res.data.coachFee,           // ✅ 已经正确
      avatar: res.data.avatarUrl,            // ✅ 使用正确的字段名
      level: res.data.coachStarLevel,        // ✅ 教练星级
      specialties: res.data.specialties,     // ✅ 已经正确
      coachNo: res.data.coachNo              // ✅ 教练编号（额外信息）
    }));
    
    console.log('映射后的教练详情:', selectedCoachDetails.value);
    
    // 记录失败的教练ID
    failedCoachIds.value = failedResponses.map(res => res.coachId);
    
    // 错误提示
    if (failedResponses.length > 0) {
      const failedNames = failedResponses.map(res => `教练${res.coachId}`).join('、');
      message.warning(`无法获取${failedNames}的详细信息，这些教练将不显示配置选项`);
      console.warn('教练详情获取失败:', failedResponses);
    }
    
    console.log('教练详情加载完成:', {
      成功: selectedCoachDetails.value.length,
      失败: failedCoachIds.value.length,
      详情: selectedCoachDetails.value
    });
    
  } catch (error) {
    console.error('批量获取教练详情失败:', error);
    message.error('获取教练信息失败，请刷新页面重试');
    selectedCoachDetails.value = [];
    failedCoachIds.value = [...coachIds];
  } finally {
    coachDetailsLoading.value = false;
  }
}

async function loadProductDetail() {
  try {
    const response = await productApi.getProductDetail(route.params.id);
    if (response.ok) {
      const product = response.data;

      // 检查是否为活动类型产品
      if (product.productType === 3) {
        Modal.info({
          title: '活动类型产品',
          content: '此产品为活动类型，请前往活动管理页面进行编辑。产品管理页面仅支持课程和课时包的管理。',
          okText: '知道了',
          onOk: () => {
            goBack();
          }
        });
        return;
      }

      // 先设置基础信息（不包括dynamicConfig和productType）
      Object.assign(formData, {
        productId: product.productId,
        productName: product.productName,
        productCode: product.productCode
      });

      // 构建dynamicConfig数据
      let dynamicConfig = {};

      // 根据商品类型获取对应的详情配置
      if (product.productType === 1 && product.courseDetails && Object.keys(product.courseDetails).length > 0) {
        // 课程类型：从courseDetails获取数据
        dynamicConfig = {
          classType: product.courseDetails.classType,
          durationMinutes: product.courseDetails.durationMinutes,
          durationPeriods: product.courseDetails.durationPeriods,
          maxStudents: product.courseDetails.maxStudents,
          coachFee: product.courseDetails.coachFee,
          horseFee: product.courseDetails.horseFee,
          multiPriceConfig: product.courseDetails.multiPriceConfig
        };
      } else if (product.productType === 2 && product.packageDetails && Object.keys(product.packageDetails).length > 0) {
        // 课时包类型：从packageDetails获取数据
        dynamicConfig = {
          details: product.packageDetails.details,
          price: product.packageDetails.price,
          totalSessions: product.packageDetails.totalSessions,
          validityDays: product.packageDetails.validityDays,
          stockQuantity: product.packageDetails.stockQuantity
        };
      } else {
        // 如果没有详情数据，尝试从旧的dynamicConfig字段获取
        try {
          if (product.dynamicConfig) {
            if (typeof product.dynamicConfig === 'string') {
              dynamicConfig = JSON.parse(product.dynamicConfig);
            } else {
              dynamicConfig = product.dynamicConfig;
            }

            // 如果dynamicConfig中有detailImages，也需要解析
            if (dynamicConfig.detailImages && typeof dynamicConfig.detailImages === 'string') {
              try {
                const parsed = JSON.parse(dynamicConfig.detailImages);
                if (Array.isArray(parsed)) {
                  dynamicConfig.detailImages = parsed;
                }
              } catch (error) {
                console.warn('解析dynamicConfig中的detailImages失败:', error);
                dynamicConfig.detailImages = [];
              }
            }
          }
        } catch (e) {
          console.warn('解析dynamicConfig失败:', e);
          dynamicConfig = {};
        }

        // 临时处理：为没有课程配置的现有课程商品提供默认值
        if (product.productType === 1 && Object.keys(dynamicConfig).length === 0) {
          console.log('为课程商品设置默认配置');
          dynamicConfig = {
            classType: 1, // 默认单人课
            durationMinutes: 60, // 默认60分钟
            durationPeriods: 1.0, // 默认1鞍时
            maxStudents: 1, // 默认最大1人
            coachFee: 200, // 默认教练费200
            horseFee: 100, // 默认马匹费100
            multiPriceConfig: null
          };
        }
      }

      console.log('商品详情加载成功:', product);
      console.log('解析的动态配置数据:', dynamicConfig);
      console.log('详情图片数据类型:', typeof dynamicConfig.detailImages);
      console.log('详情图片数据内容:', dynamicConfig.detailImages);

      // 先加载表单配置，再设置数据
      if (product.productType) {
        await loadFormConfig(product.productType);

        // 配置加载完成后，设置productType和dynamicConfig
        formData.productType = product.productType;
        formData.dynamicConfig = dynamicConfig;

        // 如果是课程类型，加载关联的教练数据
        if (product.productType === 1) {
          await loadProductCoaches(product.productId);
        }

        console.log('最终设置的dynamicConfig:', formData.dynamicConfig);
      }

    } else {
      message.error(response.msg || '加载商品详情失败');
      goBack();
    }
  } catch (error) {
    message.error('加载商品详情失败');
    console.error('加载商品详情失败:', error);
    goBack();
  }
}

/**
 * 加载产品关联的教练数据（增强版本）
 */
async function loadProductCoaches(productId) {
  try {
    const response = await productApi.getProductCoaches(productId);
    if (response.ok && response.data) {
      // 提取教练ID数组
      const coachIds = response.data.map(coach => coach.coachId);
      formData.coachIds = coachIds;
      console.log('加载的教练数据:', response.data);
      console.log('设置的教练ID数组:', coachIds);
      
      // 编辑模式下立即加载教练详情
      if (coachIds.length > 0) {
        await loadCoachDetails(coachIds);
        console.log('编辑模式教练详情加载完成');
      }
    }
  } catch (error) {
    console.warn('加载教练数据失败:', error);
    // 不阻塞页面加载，只记录警告
  }
}

async function loadFormConfig(productType) {
  try {
    configLoading.value = true;
    const response = await productApi.getFormConfig(productType);
    if (response.ok) {
      // 从API响应中提取fields数组给DynamicFormRenderer使用
      dynamicFormConfig.value = response.data?.fields || [];

      // 检查是否需要根据classType获取详细配置
      if (response.data?.needsDetailedConfig) {
        needsDetailedConfig.value = true;
        baseFormConfig.value = response.data;
        // 如果已经有classType值，立即加载详细配置
        if (formData.dynamicConfig.classType) {
          await loadDetailedFormConfig(productType, formData.dynamicConfig.classType);
        }
      } else {
        needsDetailedConfig.value = false;
        baseFormConfig.value = null;
      }

      // 只在新增模式下重置动态配置，编辑模式下保持数据
      if (!isEdit.value) {
        formData.dynamicConfig = {};
      }

      console.log('表单配置加载完成:', {
        needsDetailedConfig: needsDetailedConfig.value,
        fields: dynamicFormConfig.value,
        currentDynamicConfig: formData.dynamicConfig
      });
    } else {
      message.error(response.msg || '加载表单配置失败');
    }
  } catch (error) {
    message.error('加载表单配置失败');
    console.error('加载表单配置失败:', error);
  } finally {
    configLoading.value = false;
  }
}

async function loadDetailedFormConfig(productType, classType) {
  try {
    configLoading.value = true;
    const response = await productApi.getDetailedFormConfig(productType, classType);
    if (response.ok) {
      // 从API响应中提取fields数组给DynamicFormRenderer使用
      dynamicFormConfig.value = response.data?.fields || [];
      currentClassType.value = classType;
    } else {
      message.error(response.msg || '加载详细表单配置失败');
    }
  } catch (error) {
    message.error('加载详细表单配置失败');
    console.error('加载详细表单配置失败:', error);
  } finally {
    configLoading.value = false;
  }
}

// 带体验课检测的动态加载表单配置
async function loadFormConfigWithExperienceDetection() {
  try {
    configLoading.value = true;

    // 检测是否为体验课
    const isExperience = isExperienceClass.value;

    console.log('动态加载表单配置:', {
      productName: formData.productName,
      isExperience: isExperience,
      productType: formData.productType,
      classType: formData.dynamicConfig.classType
    });

    if (isExperience) {
      // 体验课：生成以分钟为单位的表单配置
      await loadExperienceClassFormConfig();
    } else {
      // 正常课程：按照正常流程加载
      if (formData.dynamicConfig.classType) {
        await loadDetailedFormConfig(formData.productType, formData.dynamicConfig.classType);
      } else {
        await loadFormConfig(formData.productType);
      }
    }

  } catch (error) {
    message.error('加载动态表单配置失败');
    console.error('加载动态表单配置失败:', error);
  } finally {
    configLoading.value = false;
  }
}

// 加载体验课表单配置
async function loadExperienceClassFormConfig() {
  // 直接在前端生成体验课配置，以分钟为单位
  const experienceFields = [
    {
      key: 'durationMinutes',
      label: '课程时长（分钟）',
      type: 'number',
      required: true,
      min: 30,
      max: 300,
      defaultValue: 60,
      placeholder: '请输入课程时长，单位：分钟'
    },
    {
      key: 'maxStudents',
      label: '最大人数',
      type: 'number',
      required: true,
      min: 1,
      max: 10,
      defaultValue: 1
    },
    {
      key: 'sessionFee',
      label: '课时费',
      type: 'number',
      required: true,
      min: 0,
      max: 9999,
      defaultValue: 300
    }
  ];

  dynamicFormConfig.value = experienceFields;
  needsDetailedConfig.value = false;

  console.log('体验课配置加载完成:', experienceFields);
}

// 处理时间字段转换（鞍时 <-> 分钟）
function handleTimeFieldConversion(isExperienceClass, wasExperienceClass) {
  const currentConfig = formData.dynamicConfig;

  if (isExperienceClass && !wasExperienceClass) {
    // 从正常课程转为体验课：鞍时 -> 分钟
    if (currentConfig.durationPeriods) {
      // 1鞍时 ≈ 60分钟
      const minutes = Math.round(currentConfig.durationPeriods * 60);
      currentConfig.durationMinutes = minutes;
      currentConfig.durationPeriods = null; // 清空鞍时字段

      console.log(`时间字段转换: ${currentConfig.durationPeriods}鞍时 -> ${minutes}分钟`);
    }

    // 体验课保持单人课分类（体验课本质上也是单人课的一种）
    currentConfig.classType = currentConfig.classType || 1;

  } else if (!isExperienceClass && wasExperienceClass) {
    // 从体验课转为正常课程：分钟 -> 鞍时
    if (currentConfig.durationMinutes) {
      // 60分钟 ≈ 1鞍时
      const periods = Math.round((currentConfig.durationMinutes / 60) * 2) / 2; // 四舍五入到0.5的倍数
      currentConfig.durationPeriods = Math.max(0.5, Math.min(5.0, periods)); // 限制在合理范围内
      currentConfig.durationMinutes = null; // 清空分钟字段

      console.log(`时间字段转换: ${currentConfig.durationMinutes}分钟 -> ${currentConfig.durationPeriods}鞍时`);
    }

    // 正常课程需要课程分类，默认为单人课
    currentConfig.classType = currentConfig.classType || 1;
  }
}

function onDynamicFormChange(newData) {
  // 课程分类现在在基础信息中选择，不再从动态表单中监听classType变化
  // 如果需要重新加载配置，应该监听基础信息中的classType变化

  // 手动触发验证检查
  setTimeout(() => {
    // 如果表单数据有值，认为验证通过
    const hasRequiredFields = checkRequiredFields(newData);
    if (hasRequiredFields) {
      dynamicFormValid.value = true;
    }
  }, 100);
}

function checkRequiredFields(data) {
  if (!formData.productType) return false;

  if (formData.productType === 1) {
    // 课程类型必填字段检查
    const isExperience = isExperienceClass.value;

    if (isExperience) {
      // 体验课：检查分钟字段和课时费
      return data.durationMinutes &&
             data.maxStudents &&
             data.sessionFee !== null && data.sessionFee !== undefined;
    } else {
      // 正常课程：检查鞍时字段和课时费
      return data.classType &&
             data.durationPeriods &&
             data.maxStudents &&
             data.sessionFee !== null && data.sessionFee !== undefined;
    }
  } else if (formData.productType === 2) {
    // 课时包类型必填字段检查
    return data.details &&
           data.price !== null && data.price !== undefined &&
           data.totalSessions &&
           data.validityDays &&
           data.stockQuantity !== null && data.stockQuantity !== undefined;
  }

  return false;
}

function onProductTypeChange(e) {
  const productType = e.target.value;

  // 切换商品类型时重置动态配置和状态，确保数据清洁
  formData.dynamicConfig = {};
  dynamicFormValid.value = false;

  // 统一处理不同商品类型的配置加载
  if (productType === 1) {
    // 课程类型：默认选中单人课，然后加载详细配置
    formData.dynamicConfig.classType = 1;
    needsDetailedConfig.value = true; // 确保状态正确，允许watch触发
    nextTick(() => {
      // 确保验证状态重置，避免旧数据验证错误
      dynamicFormValid.value = false;
      // 手动清除课程分类字段的验证状态
      formRef.value?.clearValidate('dynamicConfig.classType');
      loadDetailedFormConfig(productType, 1);
    });
  } else {
    needsDetailedConfig.value = false; // 仅在非课程类型时重置
    if (productType === 2) {
      // 课时包类型：直接加载基础配置，不需要classType
      nextTick(() => {
        // 重置验证状态
        dynamicFormValid.value = false;
        loadFormConfig(productType);
      });
    }
  }
}

function onClassTypeChange(e) {
  // 课程分类切换时手动清除验证状态
  nextTick(() => {
    formRef.value?.clearValidate('dynamicConfig.classType');
  });
}

function onDynamicFormValidate(valid) {
  dynamicFormValid.value = valid;
  console.log('动态表单验证状态:', valid);
}

async function onSubmit() {
  try {
    // 验证基础表单
    await formRef.value.validate();

    console.log('基础表单验证通过');
    console.log('当前商品类型:', formData.productType);
    console.log('动态配置数据:', formData.dynamicConfig);
    console.log('动态表单验证状态:', dynamicFormValid.value);

    // 验证动态表单
    if (formData.productType) {
      // 检查必填字段
      const hasRequiredFields = checkRequiredFields(formData.dynamicConfig);
      console.log('必填字段检查结果:', hasRequiredFields);

      if (!hasRequiredFields) {
        message.error('请完善商品配置信息');
        return;
      }
      
      // 验证小组课教练价格配置（严格版本）
      if (isGroupCourse.value) {
        // 检查是否有可用的教练
        if (selectedCoachesWithFee.value.length === 0) {
          if (coachConfigSummary.value.total > 0) {
            message.error('无法获取教练详细信息，请刷新页面重试或重新选择教练');
          } else {
            message.error('小组课必须选择授课教练');
          }
          return;
        }
        
        // 检查是否有教练信息获取失败
        if (coachConfigSummary.value.hasIssues) {
          const result = await new Promise((resolve) => {
            Modal.confirm({
              title: '部分教练信息缺失',
              content: `${coachConfigSummary.value.failed}个教练的信息获取失败，只有${coachConfigSummary.value.valid}个教练可以进行价格配置。确定要继续吗？`,
              onOk: () => resolve(true),
              onCancel: () => resolve(false)
            });
          });
          
          if (!result) return;
        }
        
        // 验证价格配置完整性
        if (!formData.coachPriceList || formData.coachPriceList.length === 0) {
          message.error('请完成教练价格配置');
          return;
        }
        
        // 验证每个教练都有完整的价格配置
        const incompleteCoaches = formData.coachPriceList.filter(item => {
          if (!item.prices) return true;
          for (let count = 2; count <= 6; count++) {
            if (!item.prices[count] || item.prices[count] <= 0) {
              return true;
            }
          }
          return false;
        });
        
        if (incompleteCoaches.length > 0) {
          message.error('请完善所有教练的价格配置（2-6人）');
          return;
        }
      }
    }

    submitLoading.value = true;

    // 构造提交数据 - 严格按照数据库字段结构
    const submitData = {
      // 主表字段
      ...formData,

      // 根据商品类型构造对应的扩展表数据
      dynamicConfig: JSON.stringify(formData.dynamicConfig),

      // 课程商品字段 (productType=1)
      ...(formData.productType === 1 && {
        classType: formData.dynamicConfig.classType,
        durationMinutes: formData.dynamicConfig.durationMinutes,
        durationPeriods: formData.dynamicConfig.durationPeriods,
        maxStudents: formData.dynamicConfig.maxStudents,
        coachFee: formData.dynamicConfig.coachFee,
        horseFee: formData.dynamicConfig.horseFee,
        multiPriceConfig: formData.dynamicConfig.multiPriceConfig
      }),

      // 课时包商品字段 (productType=2)
      ...(formData.productType === 2 && {
        details: formData.dynamicConfig.details,
        price: formData.dynamicConfig.price,
        totalSessions: formData.dynamicConfig.totalSessions,
        validityDays: formData.dynamicConfig.validityDays,
        stockQuantity: formData.dynamicConfig.stockQuantity
      })

    };

    let response;
    if (isEdit.value) {
      response = await productApi.updateProduct(submitData);
    } else {
      response = await productApi.addProduct(submitData);
    }

    if (response.ok) {
      // 产品保存成功后，处理教练关联
      if (formData.productType === 1 && formData.coachIds && formData.coachIds.length > 0) {
        const productId = isEdit.value ? formData.productId : response.data.productId;
        try {
          const coachResponse = await productApi.updateProductCoaches({
            productId: productId,
            coachIds: formData.coachIds,
            operator: 'current_user' // 这里可以替换为实际的当前用户
          });

          if (!coachResponse.ok) {
            console.warn('保存教练关联失败:', coachResponse.msg);
            message.warning('课程保存成功，但教练关联保存失败');
          }
        } catch (coachError) {
          console.warn('保存教练关联异常:', coachError);
          message.warning('课程保存成功，但教练关联保存失败');
        }
      }

      message.success(isEdit.value ? '更新成功' : '创建成功');
      goBack();
    } else {
      message.error(response.msg || (isEdit.value ? '更新失败' : '创建失败'));
    }
  } catch (error) {
    if (error.errorFields) {
      message.error('请检查表单信息');
    } else {
      message.error(isEdit.value ? '更新商品失败' : '创建商品失败');
      console.error('提交商品失败:', error);
    }
  } finally {
    submitLoading.value = false;
  }
}

function resetForm() {
  formRef.value?.resetFields();
  Object.assign(formData, {
    // 主表字段 m_product
    productId: null,
    productName: '',
    productCode: '',
    productType: null,
    subType: '',

    // 教练绑定字段
    coachIds: [],
    
    // 教练价格配置（小组课专用）
    coachPriceList: [],

    // 动态配置字段 - 严格按照数据库表结构
    dynamicConfig: {
      // 课程字段 m_product_course (productType=1)
      classType: null,
      durationMinutes: null,
      durationPeriods: null,
      maxStudents: null,
      coachFee: null,
      horseFee: null,
      multiPriceConfig: null,

      // 课时包字段 m_product_package (productType=2)
      details: '',
      price: null,
      totalSessions: null,
      validityDays: null,
      stockQuantity: null
    }
  });
  dynamicFormConfig.value = [];
  dynamicFormValid.value = false;
  needsDetailedConfig.value = false;
  baseFormConfig.value = null;
  currentClassType.value = null;
}

function goBack() {
  router.back();
}
</script>

<style scoped>
.product-form {
  max-width: 1000px;
  margin: 0 auto;
}

.form-section {
  margin-bottom: 24px;
}

.form-section:last-child {
  margin-bottom: 0;
}

/* 单选框样式优化 */
.ant-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.ant-radio-wrapper {
  margin-right: 0;
}

/* 体验课表单布局优化 */
.experience-form-layout .ant-input-number {
  width: 100%;
}

/* 正常表单布局优化 */
.normal-form-layout {
  /* 可以添加额外的样式 */
}
</style>
