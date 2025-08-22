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
            <a-textarea v-model:value="form.specialties" placeholder="请输入专长领域" :rows="2" />
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

      <!-- 教练等级信息 -->
      <a-divider orientation="left">教练等级信息</a-divider>

      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="星级教练证号码" name="coachCertNo">
            <a-input v-model:value="form.coachCertNo" placeholder="请输入星级教练证号码" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="教练等级" name="coachLevel">
            <a-select v-model:value="form.coachLevel" placeholder="请选择教练等级" allowClear>
              <a-select-option v-for="level in coachLevels" :key="level" :value="level">
                {{ level }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="教练证书图片" name="coachCertImgUrl">
            <Upload
              accept=".jpg,.jpeg,.png,.gif"
              :maxUploadSize="5"
              :multiple="true"
              buttonText="点击上传教练证书"
              :default-file-list="coachCertFileList"
              @change="coachCertChange"
            />
          </a-form-item>
        </a-col>
      </a-row>
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
  specialties: '',
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
};

const form = reactive({ ...formState });

const rules = {
  clubId: [{ required: true, message: '请选择所属俱乐部', trigger: 'change' }],
  userId: [{ required: true, message: '请选择关联用户', trigger: 'change' }],
  coachFee: [{ type: 'number', min: 0, message: '教练费不能小于0', trigger: 'blur' }],
};

// ----------------------- 显示/隐藏 -----------------------

async function show(coachId) {
  visible.value = true;
  resetForm();
  await loadClubList();
  await loadUserList();

  if (coachId) {
    await loadCoachDetail(coachId);
  }
}

function onClose() {
  visible.value = false;
  resetForm();
}

function resetForm() {
  Object.assign(form, formState);
  riderCertFileList.value = [];
  coachCertFileList.value = [];
  nextTick(() => {
    formRef.value?.clearValidate();
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
    let res = await coachApi.detail(coachId);
    Object.assign(form, res.data);

    // 处理日期字段
    if (form.entryDate) {
      form.entryDate = dayjs(form.entryDate);
    }

    // 处理图片字段
    if (form.riderCertImgUrl) {
      try {
        const riderImages = JSON.parse(form.riderCertImgUrl);
        riderCertFileList.value = Array.isArray(riderImages)
          ? riderImages.map((url, index) => ({ fileUrl: url, fileName: `骑手证书${index + 1}.jpg` }))
          : [{ fileUrl: riderImages, fileName: '骑手证书.jpg' }];
      } catch (e) {
        if (form.riderCertImgUrl) {
          riderCertFileList.value = [{ fileUrl: form.riderCertImgUrl, fileName: '骑手证书.jpg' }];
        }
      }
    }

    if (form.coachCertImgUrl) {
      try {
        const coachImages = JSON.parse(form.coachCertImgUrl);
        coachCertFileList.value = Array.isArray(coachImages)
          ? coachImages.map((url, index) => ({ fileUrl: url, fileName: `教练证书${index + 1}.jpg` }))
          : [{ fileUrl: coachImages, fileName: '教练证书.jpg' }];
      } catch (e) {
        if (form.coachCertImgUrl) {
          coachCertFileList.value = [{ fileUrl: form.coachCertImgUrl, fileName: '教练证书.jpg' }];
        }
      }
    }
  } catch (e) {
    smartSentry.captureError(e);
  }
}

// ----------------------- 文件上传处理 -----------------------

function riderCertChange(fileList) {
  if (fileList.length === 0) {
    form.riderCertImgUrl = '';
  } else if (fileList.length === 1) {
    form.riderCertImgUrl = fileList[0].fileUrl;
  } else {
    form.riderCertImgUrl = JSON.stringify(fileList.map(file => file.fileUrl));
  }
}

function coachCertChange(fileList) {
  if (fileList.length === 0) {
    form.coachCertImgUrl = '';
  } else if (fileList.length === 1) {
    form.coachCertImgUrl = fileList[0].fileUrl;
  } else {
    form.coachCertImgUrl = JSON.stringify(fileList.map(file => file.fileUrl));
  }
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
