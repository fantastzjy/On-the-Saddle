<!--
  * 教练新增/编辑弹窗
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

      <!-- 骑手等级信息 -->
      <a-divider orientation="left">骑手等级信息</a-divider>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="骑手证号码" name="riderCertNo">
            <a-input v-model:value="form.riderCertNo" placeholder="请输入骑手证号码" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="场地障碍等级" name="riderLevelShowJumping">
            <a-select v-model:value="form.riderLevelShowJumping" placeholder="请选择场地障碍等级" allowClear>
              <a-select-option v-for="level in riderLevels" :key="level" :value="level">
                {{ level }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="盛装舞步等级" name="riderLevelDressage">
            <a-select v-model:value="form.riderLevelDressage" placeholder="请选择盛装舞步等级" allowClear>
              <a-select-option v-for="level in riderLevels" :key="level" :value="level">
                {{ level }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="三项赛等级" name="riderLevelEventing">
            <a-select v-model:value="form.riderLevelEventing" placeholder="请选择三项赛等级" allowClear>
              <a-select-option v-for="level in riderLevels" :key="level" :value="level">
                {{ level }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="骑手证书图片" name="riderCertImgUrl">
            <Upload
              accept=".jpg,.jpeg,.png,.gif"
              :maxUploadSize="5"
              :multiple="true"
              buttonText="点击上传骑手证书"
              :default-file-list="riderCertFileList"
              @change="riderCertChange"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 教练证书管理区域 -->
      <a-divider orientation="left">教练证书管理</a-divider>
      <div v-for="(cert, index) in form.coachCertificates" :key="`coach-${index}`" class="certificate-item">
        <a-card size="small" style="margin-bottom: 16px;">
          <template #title>
            教练证书 #{{ index + 1 }}
            <a-button @click="removeCoachCert(index)" danger type="text" size="small" style="float: right;">
              删除
            </a-button>
          </template>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item :name="['coachCertificates', index, 'category']" label="证书类别">
                <a-select v-model:value="cert.category" placeholder="选择证书类别">
                  <a-select-option :value="1">场地障碍</a-select-option>
                  <a-select-option :value="2">盛装舞步</a-select-option>
                  <a-select-option :value="3">三项赛</a-select-option>
                  <a-select-option :value="4">教练星级</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item :name="['coachCertificates', index, 'level']" label="等级">
                <a-select v-model:value="cert.level" placeholder="选择等级">
                  <a-select-option :value="1">一星</a-select-option>
                  <a-select-option :value="2">二星</a-select-option>
                  <a-select-option :value="3">三星</a-select-option>
                  <a-select-option :value="4">四星</a-select-option>
                  <a-select-option :value="5">五星</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item :name="['coachCertificates', index, 'images']" label="证书图片">
                <Upload
                  :key="`coach-upload-${index}-${cert.fileList ? cert.fileList.length : 0}`"
                  :multiple="true"
                  :maxCount="3"
                  buttonText="上传证书"
                  :defaultFileList="cert.fileList || []"
                  @change="(files) => coachCertChange(index, files)"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </div>
      <a-button @click="addCoachCert" type="dashed" block style="margin-bottom: 16px;">
        添加教练证书
      </a-button>

      <!-- 骑手证书管理区域 -->
      <a-divider orientation="left">骑手证书管理</a-divider>
      <div v-for="(cert, index) in form.riderCertificates" :key="`rider-${index}`" class="certificate-item">
        <a-card size="small" style="margin-bottom: 16px;">
          <template #title>
            骑手证书 #{{ index + 1 }}
            <a-button @click="removeRiderCert(index)" danger type="text" size="small" style="float: right;">
              删除
            </a-button>
          </template>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item :name="['riderCertificates', index, 'category']" label="类别">
                <a-select v-model:value="cert.category" placeholder="选择类别">
                  <a-select-option :value="1">场地障碍</a-select-option>
                  <a-select-option :value="2">盛装舞步</a-select-option>
                  <a-select-option :value="3">三项赛</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item :name="['riderCertificates', index, 'level']" label="等级">
                <a-select v-model:value="cert.level" placeholder="选择等级">
                  <a-select-option v-for="(level, idx) in riderLevels" :key="idx" :value="idx + 1">
                    {{ level }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item :name="['riderCertificates', index, 'images']" label="证书图片">
                <Upload
                  :key="`rider-upload-${index}-${cert.fileList ? cert.fileList.length : 0}`"
                  :multiple="true"
                  :maxCount="3"
                  buttonText="上传证书"
                  :defaultFileList="cert.fileList || []"
                  @change="(files) => riderCertChange(index, files)"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </div>
      <a-button @click="addRiderCert" type="dashed" block>
        添加骑手证书
      </a-button>
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
import dayjs from 'dayjs';

const emit = defineEmits(['refresh']);

// ----------------------- 表单 -----------------------

const formRef = ref();
const visible = ref(false);
const confirmLoading = ref(false);
const clubList = ref([]);
const userList = ref([]);

// 文件列表
const riderCertFileList = ref([]);
const coachCertFileList = ref([]);
const idCardFrontFileList = ref([]);
const idCardBackFileList = ref([]);

// 骑手等级选项
const riderLevels = ['初三', '初二', '初一', '中三', '中二', '中一', '国三', '国二', '国一', '健将级'];

// 教练等级选项
const coachLevels = ['一星', '二星', '三星', '四星', '五星'];

const formState = {
  coachId: null,
  clubId: null,
  userId: null,
  coachNo: '',
  entryDate: null,
  specialties: [],
  introduction: '',
  riderCertNo: '',
  riderLevelShowJumping: '',
  riderLevelDressage: '',
  riderLevelEventing: '',
  riderCertImgUrl: '',
  coachCertNo: '',
  coachLevel: '',
  coachCertImgUrl: '',
  coachFee: null,
  sortOrder: 0,
  // 新增字段
  idCardFrontImg: '',
  idCardBackImg: '',
  coachCertificates: [],
  riderCertificates: [],
};

const form = reactive({ ...formState });

const rules = {
  clubId: [{ required: true, message: '请选择所属俱乐部', trigger: 'change' }],
  userId: [{ required: true, message: '请选择关联用户', trigger: 'change' }],
  coachFee: [{ type: 'number', min: 0, message: '教练费不能小于0', trigger: 'blur' }],
};

// ----------------------- 显示/隐藏 -----------------------

async function show(coachId) {
  console.log('=== 打开教练编辑弹窗 ===');
  console.log('传入的coachId:', coachId);
  
  visible.value = true;
  resetForm();
  
  console.log('重置后的form:', form);
  console.log('重置后的idCardFrontFileList:', idCardFrontFileList.value);
  console.log('重置后的idCardBackFileList:', idCardBackFileList.value);
  
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
  console.log('=== 重置表单 ===');
  console.log('重置前的form:', form);
  console.log('重置前的idCardFrontFileList:', idCardFrontFileList.value);
  console.log('重置前的idCardBackFileList:', idCardBackFileList.value);
  
  Object.assign(form, formState);
  riderCertFileList.value = [];
  coachCertFileList.value = [];
  idCardFrontFileList.value = [];
  idCardBackFileList.value = [];
  
  console.log('重置后的form:', form);
  console.log('重置后的idCardFrontFileList:', idCardFrontFileList.value);
  console.log('重置后的idCardBackFileList:', idCardBackFileList.value);
  
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
    console.log('=== 加载教练详情 ===');
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
    } else {
      console.log('身份证正面图片为空，跳过处理');
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
    } else {
      console.log('身份证反面图片为空，跳过处理');
    }

    // 处理教练证书数据 - 支持字符串和数组两种格式
    console.log('=== 处理教练证书数据 ===');
    const coachCerts = processCertificateData(form.coachCertificates, '教练证书');
    
    form.coachCertificates = coachCerts.map((cert, index) => {
      console.log(`处理教练证书${index + 1}:`, cert);
      const processedCert = {
        category: cert.category,
        level: cert.level,
        images: cert.images || [],
        fileList: (cert.images || []).map((url, idx) => ({ 
          fileUrl: url,
          fileKey: url.split('/').pop() || `coach-cert-${index}-${idx}`, // 添加fileKey
          fileName: `教练证书${idx + 1}.jpg`,
          uid: `coach-${index}-${idx}`,
          status: 'done',
          url: url // 添加url字段用于预览
        }))
      };
      console.log(`处理后的教练证书${index + 1}:`, processedCert);
      return processedCert;
    });
    console.log('最终的form.coachCertificates:', form.coachCertificates);

    // 处理骑手证书数据 - 支持字符串和数组两种格式
    console.log('=== 处理骑手证书数据 ===');
    const riderCerts = processCertificateData(form.riderCertificates, '骑手证书');
    
    form.riderCertificates = riderCerts.map((cert, index) => {
      console.log(`处理骑手证书${index + 1}:`, cert);
      const processedCert = {
        category: cert.category,
        level: cert.level,
        images: cert.images || [],
        fileList: (cert.images || []).map((url, idx) => ({ 
          fileUrl: url,
          fileKey: url.split('/').pop() || `rider-cert-${index}-${idx}`, // 添加fileKey
          fileName: `骑手证书${idx + 1}.jpg`,
          uid: `rider-${index}-${idx}`,
          status: 'done',
          url: url // 添加url字段用于预览
        }))
      };
      console.log(`处理后的骑手证书${index + 1}:`, processedCert);
      return processedCert;
    });
    console.log('最终的form.riderCertificates:', form.riderCertificates);
    
    console.log('=== 数据加载完成 ===');
    console.log('最终的form对象:', form);
    console.log('最终的idCardFrontFileList:', idCardFrontFileList.value);
    console.log('最终的idCardBackFileList:', idCardBackFileList.value);
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
    
    // 更新文件列表，添加必要的字段
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
  
  console.log('最终form.idCardFrontImg:', form.idCardFrontImg);
  console.log('最终idCardFrontFileList.value:', idCardFrontFileList.value);
}

function idCardBackChange(fileList) {
  console.log('=== 身份证反面图片上传 ===');
  console.log('传入的fileList:', fileList);
  
  if (fileList && fileList.length > 0) {
    const file = fileList[0];
    form.idCardBackImg = file.fileUrl;
    console.log('设置form.idCardBackImg:', form.idCardBackImg);
    
    // 更新文件列表，添加必要的字段
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
  
  console.log('最终form.idCardBackImg:', form.idCardBackImg);
  console.log('最终idCardBackFileList.value:', idCardBackFileList.value);
}

function riderCertChange(index, fileList) {
  console.log('=== 骑手证书图片上传 ===');
  console.log('证书索引:', index);
  console.log('传入的fileList:', fileList);
  console.log('当前form.riderCertificates:', form.riderCertificates);
  console.log('当前证书对象:', form.riderCertificates[index]);
  
  // 确保数组索引存在
  if (!form.riderCertificates[index]) {
    console.error('骑手证书索引不存在:', index);
    return;
  }
  
  // 使用Vue的响应式更新
  const cert = form.riderCertificates[index];
  console.log('更新前的cert:', cert);
  
  cert.images = fileList.map(file => {
    console.log('文件对象:', file);
    return file.fileUrl;
  });
  
  // 添加必要字段的fileList
  cert.fileList = fileList.map((file, idx) => ({
    fileUrl: file.fileUrl,
    fileKey: file.fileKey || file.fileUrl.split('/').pop() || `rider-cert-${index}-${idx}`,
    fileName: file.fileName || `骑手证书${idx + 1}.jpg`,
    uid: file.uid || `rider-${index}-${idx}`,
    status: 'done',
    url: file.fileUrl
  }));
  
  console.log('更新后的cert.images:', cert.images);
  console.log('更新后的cert.fileList:', cert.fileList);
  console.log('更新后的完整证书对象:', cert);
  console.log('更新后的完整form.riderCertificates:', form.riderCertificates);
}

function coachCertChange(index, fileList) {
  console.log('=== 教练证书图片上传 ===');
  console.log('证书索引:', index);
  console.log('传入的fileList:', fileList);
  console.log('当前form.coachCertificates:', form.coachCertificates);
  console.log('当前证书对象:', form.coachCertificates[index]);
  
  // 确保数组索引存在
  if (!form.coachCertificates[index]) {
    console.error('教练证书索引不存在:', index);
    return;
  }
  
  // 使用Vue的响应式更新
  const cert = form.coachCertificates[index];
  console.log('更新前的cert:', cert);
  
  cert.images = fileList.map(file => {
    console.log('文件对象:', file);
    return file.fileUrl;
  });
  
  // 添加必要字段的fileList
  cert.fileList = fileList.map((file, idx) => ({
    fileUrl: file.fileUrl,
    fileKey: file.fileKey || file.fileUrl.split('/').pop() || `coach-cert-${index}-${idx}`,
    fileName: file.fileName || `教练证书${idx + 1}.jpg`,
    uid: file.uid || `coach-${index}-${idx}`,
    status: 'done',
    url: file.fileUrl
  }));
  
  console.log('更新后的cert.images:', cert.images);
  console.log('更新后的cert.fileList:', cert.fileList);
  console.log('更新后的完整证书对象:', cert);
  console.log('更新后的完整form.coachCertificates:', form.coachCertificates);
}

// ----------------------- 证书管理方法 -----------------------

function addCoachCert() {
  form.coachCertificates.push({
    category: null,
    level: null,
    images: [],
    fileList: []
  });
}

function removeCoachCert(index) {
  form.coachCertificates.splice(index, 1);
}

function addRiderCert() {
  form.riderCertificates.push({
    category: null,
    level: null,
    images: [],
    fileList: []
  });
}

function removeRiderCert(index) {
  form.riderCertificates.splice(index, 1);
}

// ----------------------- 数据转换辅助方法 -----------------------

// 安全的JSON解析函数
function safeJsonParse(jsonString, defaultValue = []) {
  console.log('=== safeJsonParse ===');
  console.log('输入的jsonString:', jsonString, typeof jsonString);
  console.log('默认值:', defaultValue);
  
  if (!jsonString || typeof jsonString !== 'string' || !jsonString.trim()) {
    console.log('输入为空或非字符串，返回默认值');
    return defaultValue;
  }
  
  try {
    const parsed = JSON.parse(jsonString);
    console.log('解析成功:', parsed, Array.isArray(parsed));
    const result = Array.isArray(parsed) ? parsed : defaultValue;
    console.log('最终返回:', result);
    return result;
  } catch (e) {
    console.warn('JSON解析失败:', jsonString, e);
    console.log('解析失败，返回默认值:', defaultValue);
    return defaultValue;
  }
}

// 统一的证书数据处理函数 - 支持字符串和数组两种格式
function processCertificateData(data, certType = 'certificate') {
  console.log(`=== processCertificateData (${certType}) ===`);
  console.log('输入数据:', data, typeof data);
  
  let result = [];
  
  if (typeof data === 'string') {
    console.log('数据为字符串格式，使用JSON解析');
    result = safeJsonParse(data, []);
  } else if (Array.isArray(data)) {
    console.log('数据为数组格式，直接使用');
    result = data;
  } else {
    console.log('数据格式不识别，使用空数组');
    result = [];
  }
  
  console.log('处理后的结果:', result);
  return result;
}

// ----------------------- 提交 -----------------------

async function onSubmit() {
  try {
    await formRef.value.validateFields();
    confirmLoading.value = true;

    let formData = { ...form };

    // 处理日期字段
    if (formData.entryDate) {
      formData.entryDate = dayjs(formData.entryDate).format('YYYY-MM-DD HH:mm:ss');
    }

    // 处理专长领域字段：数组转字符串
    if (Array.isArray(formData.specialties)) {
      formData.specialties = formData.specialties.join(',');
    }

    // 处理证书数据转换（统一结构）
    formData.coachCertificates = JSON.stringify(
      formData.coachCertificates.map(cert => ({
        category: cert.category,
        level: cert.level,
        images: cert.images
      }))
    );

    formData.riderCertificates = JSON.stringify(
      formData.riderCertificates.map(cert => ({
        category: cert.category,
        level: cert.level,
        images: cert.images
      }))
    );

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
