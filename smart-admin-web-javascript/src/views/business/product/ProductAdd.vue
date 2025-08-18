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
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="课程名称" name="productName">
              <a-input v-model:value="formData.productName" placeholder="请输入课程名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="课程编码" name="productCode">
              <a-input v-model:value="formData.productCode" placeholder="留空自动生成" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="课程类型" name="productType">
              <a-select 
                v-model:value="formData.productType" 
                placeholder="请选择课程类型"
                @change="onProductTypeChange"
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
          </a-col>
        </a-row>

      </a-card>

      <!-- 动态表单配置 -->
      <a-card size="small" title="商品配置" class="form-section" v-if="formData.productType">
        <DynamicFormRenderer
          v-model:value="formData.dynamicConfig"
          :form-config="dynamicFormConfig"
          :loading="configLoading"
          @validate="onDynamicFormValidate"
          @change="onDynamicFormChange"
        />
      </a-card>

      <!-- 价格预览 -->
      <a-card size="small" title="价格预览" class="form-section" v-if="showPricePreview">
        <PricePreview
          :product-data="formData"
          :dynamic-config="formData.dynamicConfig"
          @price-change="onPriceChange"
        />
      </a-card>
    </a-form>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { ArrowLeftOutlined } from '@ant-design/icons-vue';
import { productApi } from '/@/api/business/product/product-api';
import { 
  PRODUCT_TYPE_ENUM, 
  PRODUCT_FORM_RULES, 
  PRODUCT_DEFAULT_CONFIG 
} from '/@/constants/business/product/product-const';
import DynamicFormRenderer from './components/DynamicFormRenderer.vue';
import PricePreview from './components/PricePreview.vue';

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

const showPricePreview = computed(() => {
  return formData.productType && formData.dynamicConfig && Object.keys(formData.dynamicConfig).length > 0;
});

// ======================== 初始化 ========================
onMounted(() => {
  if (isEdit.value) {
    loadProductDetail();
  }
});

// ======================== 监听器 ========================
watch(() => formData.productType, (newType) => {
  if (newType) {
    loadFormConfig(newType);
  }
});

// ======================== 方法 ========================
async function loadProductDetail() {
  try {
    const response = await productApi.getProductDetail(route.params.id);
    if (response.ok) {
      const product = response.data;
      
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
      } else if (product.productType === 3 && product.activityDetails && Object.keys(product.activityDetails).length > 0) {
        // 活动类型：从activityDetails获取数据
        dynamicConfig = {
          activityName: product.activityDetails.activityName,
          activityDetails: product.activityDetails.activityDetails,
          startTime: product.activityDetails.startTime,
          endTime: product.activityDetails.endTime,
          activityLocation: product.activityDetails.activityLocation,
          price: product.activityDetails.price,
          maxParticipants: product.activityDetails.maxParticipants,
          refundRule: product.activityDetails.refundRule,
          detailImages: product.activityDetails.detailImages || []
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

function onDynamicFormChange(newData) {
  // 检查classType是否发生变化
  if (needsDetailedConfig.value && 
      newData.classType && 
      newData.classType !== currentClassType.value &&
      formData.productType === 1) { // 只有课程类型才需要处理
    
    // classType发生变化，需要重新加载详细配置
    loadDetailedFormConfig(formData.productType, newData.classType);
  }
  
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
    return data.classType && 
           data.durationMinutes && 
           data.durationPeriods && 
           data.maxStudents && 
           data.coachFee !== null && data.coachFee !== undefined &&
           data.horseFee !== null && data.horseFee !== undefined;
  } else if (formData.productType === 2) {
    // 课时包类型必填字段检查
    return data.details && 
           data.price !== null && data.price !== undefined &&
           data.totalSessions &&
           data.validityDays &&
           data.stockQuantity !== null && data.stockQuantity !== undefined;
  } else if (formData.productType === 3) {
    // 活动类型必填字段检查
    return data.activityName &&
           data.activityDetails &&
           data.startTime &&
           data.endTime &&
           data.activityLocation &&
           data.price !== null && data.price !== undefined &&
           data.maxParticipants &&
           data.refundRule;
  }
  
  return false;
}

function onProductTypeChange(productType) {
  // 切换商品类型时重置动态配置
  formData.dynamicConfig = {};
  dynamicFormValid.value = false;
}

function onDynamicFormValidate(valid) {
  dynamicFormValid.value = valid;
  console.log('动态表单验证状态:', valid);
}

function onPriceChange(priceData) {
  // 价格变化时可以做一些处理，比如显示提示信息
  console.log('价格变化:', priceData);
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
      }),
      
      // 活动商品字段 (productType=3)
      ...(formData.productType === 3 && {
        activityName: formData.dynamicConfig.activityName,
        activityDetails: formData.dynamicConfig.activityDetails,
        startTime: formData.dynamicConfig.startTime,
        endTime: formData.dynamicConfig.endTime,
        activityLocation: formData.dynamicConfig.activityLocation,
        price: formData.dynamicConfig.price,
        maxParticipants: formData.dynamicConfig.maxParticipants,
        refundRule: formData.dynamicConfig.refundRule,
        detailImages: JSON.stringify(formData.dynamicConfig.detailImages)
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
}

.form-section {
  margin-bottom: 24px;
}

.form-section:last-child {
  margin-bottom: 0;
}
</style>