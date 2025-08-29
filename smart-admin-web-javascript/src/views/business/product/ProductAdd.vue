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
        <a-typography-text type="secondary" style="font-size: 12px;">
          活动管理请前往专门的活动管理页面
        </a-typography-text>
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
        <a-row :gutter="24">
          <a-col :span="16">
            <a-form-item label="课程类型" name="productType" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
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
        <a-row :gutter="24" v-if="formData.productType === 1">
          <a-col :span="16">
            <a-form-item label="课程分类" name="classType" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
              <a-radio-group v-model:value="formData.dynamicConfig.classType">
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
        <a-row :gutter="24">
          <a-col :span="16">
            <a-form-item label="课程名称" name="productName" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
              <a-input v-model:value="formData.productName" placeholder="请输入课程名称" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-card>

      <!-- 动态表单配置 -->
      <a-card size="small" :title="configSectionTitle" class="form-section" v-if="formData.productType">
        <!-- 体验课特殊布局 -->
        <div v-if="isExperienceClass" class="experience-form-layout">
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="课程时长（分钟）" name="dynamicConfig.durationMinutes">
                <a-input-number 
                  v-model:value="formData.dynamicConfig.durationMinutes" 
                  placeholder="请输入课程时长" 
                  :min="30" 
                  :max="300" 
                  style="width: 100%"
                />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="最大人数" name="dynamicConfig.maxStudents">
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
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="教练费" name="dynamicConfig.coachFee">
                <a-input-number 
                  v-model:value="formData.dynamicConfig.coachFee" 
                  placeholder="请输入教练费" 
                  :min="0" 
                  :max="9999" 
                  style="width: 100%"
                  :formatter="value => `¥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                  :parser="value => value.replace(/¥\s?|(,*)/g, '')"
                />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="马匹费用" name="dynamicConfig.horseFee">
                <a-input-number 
                  v-model:value="formData.dynamicConfig.horseFee" 
                  placeholder="请输入马匹费用" 
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

    </a-form>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch, nextTick } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { ArrowLeftOutlined } from '@ant-design/icons-vue';
import { productApi } from '/@/api/business/product/product-api';
import { 
  PRODUCT_MANAGEMENT_TYPE_ENUM, 
  PRODUCT_FORM_RULES, 
  PRODUCT_DEFAULT_CONFIG,
  COURSE_CLASS_TYPE_ENUM 
} from '/@/constants/business/product/product-const';
import DynamicFormRenderer from './components/DynamicFormRenderer.vue';

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

const formData = reactive({
  // 主表字段 m_product
  productId: null,
  productName: '',
  productCode: '',
  productType: null,
  subType: '',
  
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

const formRules = { ...PRODUCT_FORM_RULES };

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

// ======================== 初始化 ========================
onMounted(() => {
  if (isEdit.value) {
    loadProductDetail();
  }
});

// ======================== 监听器 ========================
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
    }
  }
});

// ======================== 方法 ========================
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
      key: 'coachFee',
      label: '教练费',
      type: 'number',
      required: true,
      min: 0,
      max: 9999,
      defaultValue: 150
    },
    {
      key: 'horseFee',
      label: '马匹费用',
      type: 'number',
      required: true,
      min: 0,
      max: 9999,
      defaultValue: 80
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
    
    // 体验课不需要课程分类
    currentConfig.classType = null;
    
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
      // 体验课：检查分钟字段
      return data.durationMinutes && 
             data.maxStudents && 
             data.coachFee !== null && data.coachFee !== undefined &&
             data.horseFee !== null && data.horseFee !== undefined;
    } else {
      // 正常课程：检查鞍时字段
      return data.classType && 
             data.durationPeriods && 
             data.maxStudents && 
             data.coachFee !== null && data.coachFee !== undefined &&
             data.horseFee !== null && data.horseFee !== undefined;
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