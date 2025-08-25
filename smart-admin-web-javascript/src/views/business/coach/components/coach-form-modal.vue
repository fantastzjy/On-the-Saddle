<!--
  * 教练新增/编辑弹窗（扁平化证书结构版）
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-01-08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-modal
    :title="form.coachId ? '编辑教练' : '新建教练'"
    :open="visible"
    @cancel="onClose"
    @ok="onSubmit"
    :width="900"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ style: { width: '100px' } }" :wrapper-col="{ style: { marginLeft: '10px' } }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="所属俱乐部" name="clubId">
            <a-select v-model:value="form.clubId" placeholder="请选择俱乐部" allowClear>
              <a-select-option v-for="club in clubList" :key="club.clubId" :value="club.clubId">
                {{ club.clubName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="关联用户" name="userId">
            <a-select v-model:value="form.userId" placeholder="请选择用户" allowClear showSearch>
              <a-select-option v-for="user in userList" :key="user.employeeId" :value="user.employeeId">
                {{ user.actualName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="教练编号" name="coachNo">
            <a-input v-model:value="form.coachNo" placeholder="请输入教练编号" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="从业时间" name="entryDate">
            <a-date-picker v-model:value="form.entryDate" placeholder="请选择从业时间" style="width: 100%" show-time />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="排序" name="sortOrder">
            <a-input-number v-model:value="form.sortOrder" placeholder="请输入排序" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="教练费" name="coachFee">
            <a-input-number 
              v-model:value="form.coachFee" 
              placeholder="请输入教练费"
              :min="0"
              :precision="2"
              addon-after="元/鞍时"
              style="width: 100%" 
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="专长领域" name="specialties">
            <PresetSelector 
              v-model:value="form.specialties"
              preset-type="specialties"
              mode="multiple"
              placeholder="请选择专长领域"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 新增身份证信息区域 -->
      <a-divider orientation="left">身份证信息</a-divider>
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="身份证正面" name="idCardFrontImg">
            <Upload
              :key="`id-front-${idCardFrontFileList.length}`"
              accept=".jpg,.jpeg,.png,.gif"
              :maxCount="1"
              buttonText="上传身份证正面"
              :defaultFileList="idCardFrontFileList"
              @change="idCardFrontChange"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="身份证反面" name="idCardBackImg">
            <Upload
              :key="`id-back-${idCardBackFileList.length}`"
              accept=".jpg,.jpeg,.png,.gif"
              :maxCount="1"
              buttonText="上传身份证反面"
              :defaultFileList="idCardBackFileList"
              @change="idCardBackChange"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="个人介绍" name="introduction">
            <a-textarea v-model:value="form.introduction" placeholder="请输入个人介绍" :rows="3" />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 证号信息模块 -->
      <a-divider orientation="left">证号信息</a-divider>
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="教练证号" name="coachCertNo">
            <a-input v-model:value="form.coachCertNo" placeholder="请输入教练证号" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="骑手证号" name="riderCertNo">
            <a-input v-model:value="form.riderCertNo" placeholder="请输入骑手证号" />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 教练证书模块 -->
      <a-divider orientation="left">教练证书信息</a-divider>
      
      <!-- 教练星级证书 -->
      <CoachCertificateCard 
        title="教练星级"
        v-model:level="form.coachStarLevel"
        v-model:images="form.coachStarCertImages"
        :maxLevel="5"
      />
      
      <!-- 教练场地障碍证书 -->
      <CoachCertificateCard 
        title="教练场地障碍" 
        v-model:level="form.coachShowJumpingLevel"
        v-model:images="form.coachShowJumpingImages"
        :maxLevel="5"
      />
      
      <!-- 教练盛装舞步证书 -->
      <CoachCertificateCard 
        title="教练盛装舞步"
        v-model:level="form.coachDressageLevel" 
        v-model:images="form.coachDressageImages"
        :maxLevel="5"
      />
      
      <!-- 教练三项赛证书 -->
      <CoachCertificateCard 
        title="教练三项赛"
        v-model:level="form.coachEventingLevel"
        v-model:images="form.coachEventingImages"
        :maxLevel="5"
      />

      <!-- 骑手证书模块 -->
      <a-divider orientation="left">骑手证书信息</a-divider>
      
      <!-- 骑手场地障碍证书 -->
      <RiderCertificateCard 
        title="骑手场地障碍"
        v-model:level="form.riderShowJumpingLevel"
        v-model:images="form.riderShowJumpingImages"
        :maxLevel="10" 
      />
      
      <!-- 骑手盛装舞步证书 -->
      <RiderCertificateCard 
        title="骑手盛装舞步"
        v-model:level="form.riderDressageLevel"
        v-model:images="form.riderDressageImages"
        :maxLevel="10"
      />
      
      <!-- 骑手三项赛证书 -->
      <RiderCertificateCard 
        title="骑手三项赛" 
        v-model:level="form.riderEventingLevel"
        v-model:images="form.riderEventingImages"
        :maxLevel="10"
      />
    </a-form>
  </a-modal>
</template>

<script setup>
import { reactive, ref, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { coachApi } from '/@/api/business/coach/coach-api';
import { clubApi } from '/@/api/business/club/club-api';
import { employeeApi } from '/@/api/system/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';
import Upload from '/@/components/support/file-upload/index.vue';
import PresetSelector from '/@/components/business/preset-selector/index.vue';
import CoachCertificateCard from './CoachCertificateCard.vue';
import RiderCertificateCard from './RiderCertificateCard.vue';
import dayjs from 'dayjs';

const emit = defineEmits(['refresh']);

// ----------------------- 表单 -----------------------

const formRef = ref();
const visible = ref(false);
const confirmLoading = ref(false);
const clubList = ref([]);
const userList = ref([]);

// 文件列表
const idCardFrontFileList = ref([]);
const idCardBackFileList = ref([]);

// 表单数据结构（扁平化证书结构）
const formState = {
  coachId: null,
  clubId: null,
  userId: null,
  coachNo: '',
  entryDate: null,
  specialties: [],
  introduction: '',
  coachFee: null,
  sortOrder: 0,
  // 身份证字段
  idCardFrontImg: '',
  idCardBackImg: '',
  
  // 证号字段
  riderCertNo: '',
  coachCertNo: '',
  
  // 教练证书扁平化字段（4个类别）
  coachStarLevel: 0,
  coachStarCertImages: '',
  coachShowJumpingLevel: 0,
  coachShowJumpingImages: '',
  coachDressageLevel: 0,
  coachDressageImages: '',
  coachEventingLevel: 0, 
  coachEventingImages: '',
  
  // 骑手证书扁平化字段（3个类别）
  riderShowJumpingLevel: 0,
  riderShowJumpingImages: '',
  riderDressageLevel: 0,
  riderDressageImages: '',
  riderEventingLevel: 0,
  riderEventingImages: '',
};

const form = reactive({ ...formState });

const rules = {
  clubId: [{ required: true, message: '请选择所属俱乐部', trigger: 'change' }],
  userId: [{ required: true, message: '请选择关联用户', trigger: 'change' }],
  coachFee: [{ type: 'number', min: 0, message: '教练费不能小于0', trigger: 'blur' }],
};

// ----------------------- 显示/隐藏 -----------------------

async function show(coachId) {
  console.log('=== 打开教练编辑弹窗（扁平化结构版） ===');
  console.log('传入的coachId:', coachId);
  
  visible.value = true;
  resetForm();
  
  console.log('重置后的form:', form);
  
  await loadClubList();
  await loadUserList();

  if (coachId) {
    console.log('编辑模式，加载教练详情');
    await loadCoachDetail(coachId);
  } else {
    console.log('新增模式');
  }
  
  console.log('show函数执行完成');
}

function onClose() {
  visible.value = false;
  resetForm();
}

function resetForm() {
  console.log('=== 重置表单（扁平化结构版） ===');
  
  Object.assign(form, formState);
  idCardFrontFileList.value = [];
  idCardBackFileList.value = [];
  
  console.log('重置后的form:', form);
  
  nextTick(() => {
    formRef.value?.clearValidate();
    console.log('表单验证已清理');
  });
}

// ----------------------- 数据加载 -----------------------

async function loadClubList() {
  try {
    let res = await clubApi.queryList(true);
    clubList.value = res.data;
  } catch (e) {
    smartSentry.captureError(e);
  }
}

async function loadUserList() {
  try {
    let res = await employeeApi.queryAll();
    userList.value = res.data;
  } catch (e) {
    smartSentry.captureError(e);
  }
}

async function loadCoachDetail(coachId) {
  try {
    console.log('=== 加载教练详情（扁平化结构版） ===');
    console.log('教练ID:', coachId);
    
    let res = await coachApi.detail(coachId);
    console.log('API返回的原始数据:', res.data);
    
    Object.assign(form, res.data);
    console.log('赋值后的form对象:', form);

    // 处理日期字段
    if (form.entryDate) {
      form.entryDate = dayjs(form.entryDate);
      console.log('处理后的入行日期:', form.entryDate);
    }

    // 处理专长领域字段：字符串转数组
    console.log('原始form.specialties:', form.specialties, typeof form.specialties);
    if (form.specialties) {
      form.specialties = form.specialties.split(',').filter(item => item.trim());
    } else {
      form.specialties = [];
    }
    console.log('处理后的专长领域:', form.specialties);

    // 处理身份证图片字段
    console.log('=== 处理身份证图片 ===');
    console.log('原始form.idCardFrontImg:', form.idCardFrontImg);
    console.log('原始form.idCardBackImg:', form.idCardBackImg);
    
    if (form.idCardFrontImg && form.idCardFrontImg.trim()) {
      const frontFileList = [{ 
        fileUrl: form.idCardFrontImg, 
        fileKey: form.idCardFrontImg.split('/').pop() || 'id-card-front',
        fileName: '身份证正面.jpg',
        uid: 'id-front',
        status: 'done',
        url: form.idCardFrontImg
      }];
      idCardFrontFileList.value = frontFileList;
      console.log('设置idCardFrontFileList.value:', frontFileList);
    }
    
    if (form.idCardBackImg && form.idCardBackImg.trim()) {
      const backFileList = [{ 
        fileUrl: form.idCardBackImg, 
        fileKey: form.idCardBackImg.split('/').pop() || 'id-card-back',
        fileName: '身份证反面.jpg',
        uid: 'id-back',
        status: 'done',
        url: form.idCardBackImg
      }];
      idCardBackFileList.value = backFileList;
      console.log('设置idCardBackFileList.value:', backFileList);
    }

    console.log('=== 扁平化证书数据已加载完成 ===');
    console.log('教练证书数据: coachStarLevel=', form.coachStarLevel, ', coachStarCertImages=', form.coachStarCertImages);
    console.log('骑手证书数据: riderShowJumpingLevel=', form.riderShowJumpingLevel, ', riderShowJumpingImages=', form.riderShowJumpingImages);
    console.log('最终的form对象:', form);
    
  } catch (e) {
    console.error('加载教练详情失败:', e);
    smartSentry.captureError(e);
  }
}

// ----------------------- 文件上传处理 -----------------------

function idCardFrontChange(fileList) {
  console.log('=== 身份证正面图片上传 ===');
  console.log('传入的fileList:', fileList);
  
  if (fileList && fileList.length > 0) {
    const file = fileList[0];
    form.idCardFrontImg = file.fileUrl;
    console.log('设置form.idCardFrontImg:', form.idCardFrontImg);
    
    const updatedFileList = [{
      fileUrl: file.fileUrl,
      fileKey: file.fileKey || file.fileUrl.split('/').pop() || 'id-card-front',
      fileName: file.fileName || '身份证正面.jpg',
      uid: file.uid || 'id-front',
      status: 'done',
      url: file.fileUrl
    }];
    
    idCardFrontFileList.value = updatedFileList;
    console.log('更新idCardFrontFileList.value:', updatedFileList);
  } else {
    form.idCardFrontImg = '';
    idCardFrontFileList.value = [];
    console.log('清空身份证正面图片');
  }
}

function idCardBackChange(fileList) {
  console.log('=== 身份证反面图片上传 ===');
  console.log('传入的fileList:', fileList);
  
  if (fileList && fileList.length > 0) {
    const file = fileList[0];
    form.idCardBackImg = file.fileUrl;
    console.log('设置form.idCardBackImg:', form.idCardBackImg);
    
    const updatedFileList = [{
      fileUrl: file.fileUrl,
      fileKey: file.fileKey || file.fileUrl.split('/').pop() || 'id-card-back',
      fileName: file.fileName || '身份证反面.jpg',
      uid: file.uid || 'id-back',
      status: 'done',
      url: file.fileUrl
    }];
    
    idCardBackFileList.value = updatedFileList;
    console.log('更新idCardBackFileList.value:', updatedFileList);
  } else {
    form.idCardBackImg = '';
    idCardBackFileList.value = [];
    console.log('清空身份证反面图片');
  }
}

// ----------------------- 提交 -----------------------

async function onSubmit() {
  try {
    await formRef.value.validateFields();
    confirmLoading.value = true;

    let formData = { ...form };
    console.log('=== 提交教练数据（扁平化结构版） ===');
    console.log('提交前的formData:', formData);

    // 处理日期字段
    if (formData.entryDate) {
      formData.entryDate = dayjs(formData.entryDate).format('YYYY-MM-DD HH:mm:ss');
    }

    // 处理专长领域字段：数组转字符串
    if (Array.isArray(formData.specialties)) {
      formData.specialties = formData.specialties.join(',');
    }

    console.log('处理后的提交数据:', formData);

    if (form.coachId) {
      await coachApi.update(formData);
      message.success('更新成功');
    } else {
      await coachApi.create(formData);
      message.success('创建成功');
    }

    onClose();
    emit('refresh');
  } catch (e) {
    console.error('提交失败:', e);
    smartSentry.captureError(e);
  } finally {
    confirmLoading.value = false;
  }
}

// ----------------------- 暴露方法 -----------------------

defineExpose({
  show,
});
</script>

<style scoped>
.certificate-item {
  margin-bottom: 16px;
}
</style>