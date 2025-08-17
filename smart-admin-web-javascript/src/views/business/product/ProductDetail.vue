<!--
  * 课程详情页面
  *
  * @Author: 1024创新实验室
  * @Date: 2024-08-16
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="product-detail-container">
    <a-card :loading="loading" style="margin-bottom: 16px;">
      <template #title>
        <div style="display: flex; align-items: center; justify-content: space-between;">
          <div>
            <ArrowLeftOutlined @click="goBack" style="margin-right: 8px; cursor: pointer;" />
            课程详情
          </div>
          <div>
            <a-button 
              type="primary" 
              ghost 
              @click="editProduct" 
              v-privilege="'business:product:update'"
              style="margin-right: 8px;"
            >
              <EditOutlined />
              编辑
            </a-button>
            <a-button 
              danger 
              @click="deleteProduct" 
              v-privilege="'business:product:delete'"
            >
              <DeleteOutlined />
              删除
            </a-button>
          </div>
        </div>
      </template>

      <!-- 基础信息 -->
      <a-row :gutter="24">
        <a-col :span="12">
          <a-descriptions title="基础信息" :column="1" bordered>
            <a-descriptions-item label="课程名称">
              {{ productDetail.productName }}
            </a-descriptions-item>
            <a-descriptions-item label="课程编码">
              {{ productDetail.productCode }}
            </a-descriptions-item>
            <a-descriptions-item label="课程类型">
              <a-tag :color="getProductTypeColor(productDetail.productType)">
                {{ productDetail.productTypeName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="价格">
              <span style="font-size: 18px; font-weight: bold; color: #ff4d4f;">
                ¥{{ getProductPrice() }}
              </span>
            </a-descriptions-item>
            <a-descriptions-item label="状态">
              <a-tag :color="getStatusColor(productDetail.status)">
                {{ productDetail.statusName }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="排序">
              {{ productDetail.sortOrder }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        
        <a-col :span="12">
          <div style="text-align: center;">
            <div style="margin-bottom: 16px;">
              <h4>课程图片</h4>
            </div>
            <a-image
              v-if="productDetail.imageUrl"
              :width="300"
              :height="200"
              :src="productDetail.imageUrl"
              placeholder
              fallback="/images/product-placeholder.png"
              :preview="true"
              style="border-radius: 8px; border: 1px solid #f0f0f0;"
            />
            <div v-else style="
              width: 300px; 
              height: 200px; 
              border: 2px dashed #d9d9d9; 
              display: flex; 
              align-items: center; 
              justify-content: center;
              border-radius: 8px;
              margin: 0 auto;
            ">
              <div style="text-align: center; color: #999;">
                <PictureOutlined style="font-size: 48px; margin-bottom: 8px;" />
                <div>暂无图片</div>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>

      <!-- 商品描述 -->
      <a-divider />
      <div>
        <h4 style="margin-bottom: 16px;">课程描述</h4>
        <div style="
          background: #fafafa; 
          padding: 16px; 
          border-radius: 6px;
          min-height: 100px;
          white-space: pre-wrap;
        ">
          {{ productDetail.description || '暂无描述' }}
        </div>
      </div>

      <!-- 类型特定信息 -->
      <a-divider />
      <div v-if="productDetail.productType === 1">
        <!-- 课程详情 -->
        <h4 style="margin-bottom: 16px;">
          <BookOutlined style="margin-right: 8px;" />
          课程详情
        </h4>
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="课程类型">
            {{ productDetail.courseDetails?.classType === 1 ? '单人课' : '多人课' }}
          </a-descriptions-item>
          <a-descriptions-item label="课程时长">
            {{ productDetail.courseDetails?.durationMinutes || '-' }} 分钟
          </a-descriptions-item>
          <a-descriptions-item label="鞍时数">
            {{ productDetail.courseDetails?.durationPeriods || '-' }} 鞍时
          </a-descriptions-item>
          <a-descriptions-item label="最大学员数">
            {{ productDetail.courseDetails?.maxStudents || '-' }} 人
          </a-descriptions-item>
          <a-descriptions-item label="教练费">
            ¥{{ productDetail.courseDetails?.coachFee || 0 }}
          </a-descriptions-item>
          <a-descriptions-item label="马匹费">
            ¥{{ productDetail.courseDetails?.horseFee || 0 }}
          </a-descriptions-item>
          <a-descriptions-item v-if="productDetail.courseDetails?.classType === 1" label="课程价格" :span="2">
            <span style="font-size: 16px; color: #1890ff; font-weight: bold;">
              ¥{{ (Number(productDetail.courseDetails?.coachFee || 0) + Number(productDetail.courseDetails?.horseFee || 0)).toFixed(2) }}
            </span>
          </a-descriptions-item>
          <a-descriptions-item v-if="productDetail.courseDetails?.classType === 2" label="多人课价格配置" :span="2">
            <div v-if="getMultiPriceDetails().length > 0">
              <div style="margin-bottom: 8px;">
                <span style="color: #666;">基础价格：¥{{ (Number(productDetail.courseDetails?.coachFee || 0) + Number(productDetail.courseDetails?.horseFee || 0)).toFixed(2) }}</span>
              </div>
              <a-row :gutter="16">
                <a-col v-for="detail in getMultiPriceDetails()" :key="detail.people" :span="6">
                  <a-card size="small" style="text-align: center; margin-bottom: 8px;">
                    <div>{{ detail.people }}人课</div>
                    <div style="color: #1890ff; font-weight: bold; font-size: 14px;">
                      ¥{{ detail.price }}/人
                    </div>
                  </a-card>
                </a-col>
              </a-row>
              <div style="margin-top: 8px; color: #52c41a; font-weight: bold;">
                价格区间：{{ getMultiPriceRange() }}
              </div>
            </div>
            <span v-else style="color: #999;">未配置多人课价格</span>
          </a-descriptions-item>
        </a-descriptions>
      </div>

      <div v-else-if="productDetail.productType === 2">
        <!-- 课时包详情 -->
        <h4 style="margin-bottom: 16px;">
          <CreditCardOutlined style="margin-right: 8px;" />
          课时包详情
        </h4>
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="课包详情">
            {{ productDetail.packageDetails?.details || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="课包价格">
            ¥{{ productDetail.packageDetails?.price || 0 }}
          </a-descriptions-item>
          <a-descriptions-item label="总课时数">
            {{ productDetail.packageDetails?.totalSessions || '-' }} 节
          </a-descriptions-item>
          <a-descriptions-item label="有效期">
            {{ productDetail.packageDetails?.validityDays || '-' }} 天
          </a-descriptions-item>
          <a-descriptions-item label="库存数量">
            {{ productDetail.packageDetails?.stockQuantity || '-' }} 个
          </a-descriptions-item>
        </a-descriptions>
      </div>

      <div v-else-if="productDetail.productType === 3">
        <!-- 活动详情 -->
        <h4 style="margin-bottom: 16px;">
          <CalendarOutlined style="margin-right: 8px;" />
          活动详情
        </h4>
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="活动名称">
            {{ productDetail.activityDetails?.activityName || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="活动详情">
            {{ productDetail.activityDetails?.activityDetails || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="开始时间">
            {{ formatDateTime(productDetail.activityDetails?.startTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="结束时间">
            {{ formatDateTime(productDetail.activityDetails?.endTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="活动地点">
            {{ productDetail.activityDetails?.activityLocation || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="活动价格">
            ¥{{ productDetail.activityDetails?.price || 0 }}
          </a-descriptions-item>
          <a-descriptions-item label="最大参与人数">
            {{ productDetail.activityDetails?.maxParticipants || '-' }} 人
          </a-descriptions-item>
          <a-descriptions-item label="退款规则">
            {{ productDetail.activityDetails?.refundRule || '-' }}
          </a-descriptions-item>
        </a-descriptions>
      </div>

      <!-- 操作记录 -->
      <a-divider />
      <a-descriptions title="操作记录" :column="2" bordered>
        <a-descriptions-item label="创建人">
          {{ productDetail.createBy }}
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ formatDateTime(productDetail.createTime) }}
        </a-descriptions-item>
        <a-descriptions-item label="更新人">
          {{ productDetail.updateBy }}
        </a-descriptions-item>
        <a-descriptions-item label="更新时间">
          {{ formatDateTime(productDetail.updateTime) }}
        </a-descriptions-item>
      </a-descriptions>
    </a-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { 
  ArrowLeftOutlined, 
  EditOutlined, 
  DeleteOutlined, 
  BookOutlined,
  CreditCardOutlined,
  CalendarOutlined,
  PictureOutlined
} from '@ant-design/icons-vue';
import { productApi } from '/@/api/business/product/product-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();

// 响应式数据
const loading = ref(false);
const productDetail = ref({});

// 生命周期
onMounted(() => {
  loadProductDetail();
});

// 方法
const loadProductDetail = async () => {
  try {
    loading.value = true;
    const productId = route.params.id;
    const res = await productApi.getProductDetail(productId);
    if (res.ok) {
      productDetail.value = res.data;
    } else {
      message.error(res.msg || '获取课程详情失败');
    }
  } catch (error) {
    message.error('获取课程详情失败');
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.back();
};

const editProduct = () => {
  router.push(`/product/edit/${route.params.id}`);
};

const deleteProduct = () => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除课程"${productDetail.value.productName}"吗？删除后不可恢复。`,
    okText: '确认删除',
    cancelText: '取消',
    okType: 'danger',
    onOk: async () => {
      try {
        const res = await productApi.deleteProduct(productDetail.value.productId);
        if (res.ok) {
          message.success('删除成功');
          router.push('/product/list');
        } else {
          message.error(res.msg || '删除失败');
        }
      } catch (error) {
        message.error('删除失败');
        smartSentry.captureError(error);
      }
    }
  });
};

const getProductTypeColor = (type) => {
  const colors = {
    1: 'blue',    // 课程
    2: 'green',   // 课时包
    3: 'orange'   // 活动
  };
  return colors[type] || 'default';
};

const getStatusColor = (status) => {
  const colors = {
    1: 'success',  // 上架
    2: 'warning',  // 下架
    3: 'error'     // 售罄
  };
  return colors[status] || 'default';
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-';
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm:ss');
};

const getProductPrice = () => {
  const product = productDetail.value;
  if (!product) return 0;
  
  switch (product.productType) {
    case 1: // 课程类型
      return product.courseDetails?.basePrice || 
             (Number(product.courseDetails?.coachFee || 0) + Number(product.courseDetails?.horseFee || 0));
    case 2: // 课时包类型
      return product.packageDetails?.price || 0;
    case 3: // 活动类型
      return product.activityDetails?.price || 0;
    default:
      return 0;
  }
};

const getMultiPriceDetails = () => {
  const product = productDetail.value;
  if (!product?.courseDetails?.multiPriceConfig) return [];
  
  try {
    let config = product.courseDetails.multiPriceConfig;
    if (typeof config === 'string') {
      config = JSON.parse(config);
    }
    
    const details = [];
    if (config && config.coaches && Array.isArray(config.coaches)) {
      // 取第一个教练的价格配置
      const firstCoach = config.coaches[0];
      if (firstCoach && firstCoach.prices) {
        Object.entries(firstCoach.prices).forEach(([people, price]) => {
          details.push({
            people: Number(people),
            price: Number(price).toFixed(2)
          });
        });
      }
    }
    
    // 按人数排序
    return details.sort((a, b) => a.people - b.people);
  } catch (e) {
    console.warn('解析多人课价格配置失败:', e);
    return [];
  }
};

const getMultiPriceRange = () => {
  const multiPriceDetails = getMultiPriceDetails();
  if (multiPriceDetails.length === 0) {
    const basePrice = Number(productDetail.value?.courseDetails?.coachFee || 0) + 
                     Number(productDetail.value?.courseDetails?.horseFee || 0);
    return `¥${basePrice.toFixed(2)}/人`;
  }
  
  const prices = multiPriceDetails.map(detail => Number(detail.price));
  const minPrice = Math.min(...prices);
  const maxPrice = Math.max(...prices);
  
  if (minPrice === maxPrice) {
    return `¥${minPrice.toFixed(2)}/人`;
  }
  
  return `¥${minPrice.toFixed(2)} - ¥${maxPrice.toFixed(2)}/人`;
};
</script>

<style scoped>
.product-detail-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.ant-descriptions-title {
  font-weight: 600;
  font-size: 16px;
}

.ant-descriptions-item-label {
  font-weight: 500;
  background-color: #fafafa;
}

.ant-divider {
  margin: 24px 0;
}

:deep(.ant-image) {
  border-radius: 8px;
}
</style>