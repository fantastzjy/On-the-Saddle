<!--
  * 商品新增/编辑页面
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
        {{ isEdit ? '编辑商品' : '新增商品' }}
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
            <a-form-item label="商品名称" name="productName">
              <a-input v-model:value="formData.productName" placeholder="请输入商品名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="商品编码" name="productCode">
              <a-input v-model:value="formData.productCode" placeholder="留空自动生成" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="商品类型" name="productType">
              <a-select 
                v-model:value="formData.productType" 
                placeholder="请选择商品类型"
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
          <a-col :span="12">
            <a-form-item label="排序" name="sortOrder">
              <a-input-number 
                v-model:value="formData.sortOrder" 
                :min="0" 
                :max="9999"
                placeholder="数值越小越靠前"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="商品描述" name="description">
          <a-textarea 
            v-model:value="formData.description" 
            placeholder="请输入商品描述"
            :rows="3"
            :maxlength="1000"
            show-count
          />
        </a-form-item>

        <a-form-item label="商品图片" name="images">
          <FileUpload
            v-model:value="formData.images"
            :max-count="PRODUCT_DEFAULT_CONFIG.IMAGE.MAX_COUNT"
            :max-size="PRODUCT_DEFAULT_CONFIG.IMAGE.MAX_SIZE"
            :accepted-types="PRODUCT_DEFAULT_CONFIG.IMAGE.ACCEPTED_TYPES"
            list-type="picture-card"
          >
            <div v-if="getImageCount() < PRODUCT_DEFAULT_CONFIG.IMAGE.MAX_COUNT">
              <PlusOutlined />
              <div style="margin-top: 8px;">上传图片</div>
            </div>
          </FileUpload>
          <div class="upload-tip">
            最多上传{{ PRODUCT_DEFAULT_CONFIG.IMAGE.MAX_COUNT }}张图片，每张不超过{{ PRODUCT_DEFAULT_CONFIG.IMAGE.MAX_SIZE }}MB
          </div>
        </a-form-item>
      </a-card>

      <!-- 动态表单配置 -->
      <a-card size="small" title="商品配置" class="form-section" v-if="formData.productType">
        <DynamicFormRenderer
          v-model:value="formData.dynamicConfig"
          :form-config="dynamicFormConfig"
          :loading="configLoading"
          @validate="onDynamicFormValidate"
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
import { message } from 'ant-design-vue';
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { productApi } from '/@/api/business/product/product-api';
import { 
  PRODUCT_TYPE_ENUM, 
  PRODUCT_FORM_RULES, 
  PRODUCT_DEFAULT_CONFIG 
} from '/@/constants/business/product/product-const';
import FileUpload from '/@/components/support/file-upload/index.vue';
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

const formData = reactive({
  productId: null,
  productName: '',
  productCode: '',
  productType: null,
  description: '',
  images: [],
  sortOrder: 0,
  dynamicConfig: {}
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
      Object.assign(formData, {
        productId: product.productId,
        productName: product.productName,
        productCode: product.productCode,
        productType: product.productType,
        description: product.description,
        images: parseImages(product.images),
        sortOrder: product.sortOrder,
        dynamicConfig: product.dynamicConfig || {}
      });
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
      // 重置动态配置
      formData.dynamicConfig = {};
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

function onProductTypeChange(productType) {
  // 切换商品类型时重置动态配置
  formData.dynamicConfig = {};
  dynamicFormValid.value = false;
}

function onDynamicFormValidate(valid) {
  dynamicFormValid.value = valid;
}

function onPriceChange(priceData) {
  // 价格变化时可以做一些处理，比如显示提示信息
  console.log('价格变化:', priceData);
}

async function onSubmit() {
  try {
    // 验证基础表单
    await formRef.value.validate();
    
    // 验证动态表单
    if (formData.productType && !dynamicFormValid.value) {
      message.error('请完善商品配置信息');
      return;
    }

    submitLoading.value = true;

    // 构造提交数据
    const submitData = {
      ...formData,
      images: JSON.stringify(formData.images),
      dynamicConfig: JSON.stringify(formData.dynamicConfig)
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
    productId: null,
    productName: '',
    productCode: '',
    productType: null,
    description: '',
    images: [],
    sortOrder: 0,
    dynamicConfig: {}
  });
  dynamicFormConfig.value = [];
  dynamicFormValid.value = false;
}

function goBack() {
  router.back();
}

// ======================== 辅助方法 ========================
function parseImages(images) {
  if (!images) return [];
  
  try {
    const parsed = JSON.parse(images);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

function getImageCount() {
  return Array.isArray(formData.images) ? formData.images.length : 0;
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

.upload-tip {
  color: #666;
  font-size: 12px;
  margin-top: 8px;
}

:deep(.ant-card-head-title) {
  font-size: 14px;
  font-weight: 600;
}

:deep(.ant-form-item-label > label) {
  font-weight: 500;
}

:deep(.ant-upload-select-picture-card) {
  width: 100px;
  height: 100px;
}

:deep(.ant-upload-list-picture-card .ant-upload-list-item) {
  width: 100px;
  height: 100px;
}
</style>