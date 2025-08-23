<!--
  *  员工 表单 弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-modal
    :title="form.employeeId ? '编辑员工' : '添加员工'"
    :width="800"
    :open="visible"
    @ok="onSubmit"
    @cancel="onClose"
    destroyOnClose
    okText="确定"
    cancelText="取消"
  >
    <a-alert message="超管需要直接在数据库表 t_employee修改哦" type="error" closable />
    <br />
    
    <!-- 选项卡 -->
    <a-tabs v-model:activeKey="activeTab" type="card">
      <!-- 基本信息 -->
      <a-tab-pane key="basic" tab="基本信息">
        <a-form ref="formRef" :model="form" :rules="rules" layout="horizontal" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="姓名" name="actualName">
                <a-input v-model:value.trim="form.actualName" placeholder="请输入姓名" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="性别" name="gender">
                <smart-enum-select style="width: 100%" v-model:value="form.gender" placeholder="请选择性别" enum-name="GENDER_ENUM" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="生日" name="birthDate">
                <a-date-picker v-model:value="form.birthDate" placeholder="请选择生日" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="手机号" name="phone">
                <a-input v-model:value.trim="form.phone" placeholder="请输入手机号" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="登录名" name="loginName">
                <a-input v-model:value.trim="form.loginName" placeholder="请输入登录名" />
                <p class="hint">初始密码默认为：随机</p>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="状态" name="disabledFlag">
                <a-select v-model:value="form.disabledFlag" placeholder="请选择状态">
                  <a-select-option :value="0">启用</a-select-option>
                  <a-select-option :value="1">禁用</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="角色" name="roleIdList">
                <a-select mode="multiple" v-model:value="form.roleIdList" optionFilterProp="title" placeholder="请选择角色">
                  <a-select-option v-for="item in roleList" :key="item.roleId" :title="item.roleName">{{ item.roleName }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="身份证号码" name="idCard" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
                <a-input v-model:value.trim="form.idCard" placeholder="请输入身份证号码" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-tab-pane>
      
      <!-- 学历信息 -->
      <a-tab-pane key="education" tab="学历信息" :disabled="!form.employeeId">
        <EmployeeEducation 
          v-if="form.employeeId && activeTab === 'education'"
          :employee-id="form.employeeId"
          @change="handleEducationChange"
        />
        <a-empty v-else description="请先保存基本信息，然后添加学历信息" />
      </a-tab-pane>
      
      <!-- 工作经历 -->
      <a-tab-pane key="work-experience" tab="工作经历" :disabled="!form.employeeId">
        <EmployeeWorkExperience 
          v-if="form.employeeId && activeTab === 'work-experience'"
          :employee-id="form.employeeId"
          @change="handleWorkExperienceChange"
        />
        <a-empty v-else description="请先保存基本信息，然后添加工作经历" />
      </a-tab-pane>
      
      <!-- 简历上传 tab 已被删除 -->
    </a-tabs>
  </a-modal>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { nextTick, reactive, ref } from 'vue';
  import { employeeApi } from '/@/api/system/employee-api';
  import { roleApi } from '/@/api/system/role-api';
  import DepartmentTreeSelect from '/@/components/system/department-tree-select/index.vue';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import PositionSelect from '/@/components/system/position-select/index.vue';
  import EmployeeEducation from '../employee-education/index.vue';
  import EmployeeWorkExperience from '../employee-work-experience/index.vue';
  // import EmployeeResume from '../employee-resume/index.vue'; // 简历上传组件已删除
  import { GENDER_ENUM } from '/@/constants/common-const';
  import { regular } from '/@/constants/regular-const';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const departmentTreeSelect = ref();
  const activeTab = ref('basic'); // 当前选项卡
  
  // emit
  const emit = defineEmits(['refresh', 'show-account']);

  // ----------------------- 显示/隐藏 ---------------------

  const visible = ref(false); // 是否展示模态框
  // 隐藏
  function onClose() {
    reset();
    visible.value = false;
    activeTab.value = 'basic'; // 重置选项卡
  }
  // 显示
  async function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    visible.value = true;
    nextTick(() => {
      queryAllRole();
    });
  }

  // ----------------------- 表单显示 ---------------------

  const roleList = ref([]); //角色列表
  async function queryAllRole() {
    let res = await roleApi.queryAll();
    roleList.value = res.data;
  }

  const formRef = ref(); // 组件ref
  const formDefault = {
    employeeId: undefined,
    actualName: undefined,
    departmentId: undefined,
    disabledFlag: 0,
    leaveFlag: 0,
    gender: GENDER_ENUM.MAN.value,
    loginName: undefined,
    phone: undefined,
    roleIdList: undefined,
    positionId: undefined,
    birthDate: undefined,
    idCard: undefined,
    resumeFileKey: undefined, // 最新简历文件key
    resumeUpdateTime: undefined, // 简历更新时间
  };

  let form = reactive(_.cloneDeep(formDefault));
  function reset() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
  }

  // ----------------------- 表单提交 ---------------------
  // 表单规则
  const rules = {
    actualName: [
      { required: true, message: '姓名不能为空' },
      { max: 30, message: '姓名不能大于30个字符', trigger: 'blur' },
    ],
    phone: [
      { required: true, message: '手机号不能为空' },
      { pattern: regular.phone, message: '请输入正确的手机号码', trigger: 'blur' },
    ],
    loginName: [
      { required: true, message: '登录账号不能为空' },
      { max: 30, message: '登录账号不能大于30个字符', trigger: 'blur' },
    ],
    gender: [{ required: true, message: '性别不能为空' }],
    departmentId: [{ required: true, message: '部门不能为空' }],
    disabledFlag: [{ required: true, message: '状态不能为空' }],
    leaveFlag: [{ required: true, message: '在职状态不能为空' }],
    idCard: [
      { pattern: regular.isIdentityCard, message: '请输入正确的身份证号码', trigger: 'blur' },
    ],
  };

  // 校验表单
  function validateForm(formRef) {
    return new Promise((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    });
  }

  // 提交数据
  async function onSubmit() {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      message.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    SmartLoading.show();
    if (form.employeeId) {
      await updateEmployee();
    } else {
      await addEmployee();
    }
  }

  async function addEmployee() {
    try {
      let { data } = await employeeApi.addEmployee(form);
      message.success('添加成功');
      emit('show-account', form.loginName, data);
      onClose();
      emit('refresh');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }
  
  async function updateEmployee() {
    try {
      let result = await employeeApi.updateEmployee(form);
      message.success('更新成功');
      onClose();
      emit('refresh');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  // ----------------------- 扩展信息处理 ---------------------
  
  // 处理学历信息变化
  function handleEducationChange(educationList) {
    // 可以在这里处理学历信息变化的逻辑
    console.log('学历信息更新:', educationList);
  }
  
  // 处理工作经历变化
  function handleWorkExperienceChange(workExperienceList) {
    // 可以在这里处理工作经历变化的逻辑
    console.log('工作经历更新:', workExperienceList);
  }
  
  // 处理简历变化 - 已删除
  // function handleResumeChange(resumeList) {
  //   console.log('简历文件更新:', resumeList);
  // }
  
  // 处理最新简历更新 - 已删除  
  // function handleUpdateLatestResume(fileKey) {
  //   form.resumeFileKey = fileKey;
  //   form.resumeUpdateTime = new Date().toISOString();
  //   console.log('最新简历更新:', fileKey);
  // }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showModal,
  });
</script>
<style scoped lang="less">
  .hint {
    margin-top: 5px;
    color: #bfbfbf;
  }
</style>
