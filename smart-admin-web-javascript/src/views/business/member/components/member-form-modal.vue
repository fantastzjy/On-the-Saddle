<template>
  <a-modal
    v-model:open="visible"
    :title="modalTitle"
    :width="800"
    :confirm-loading="confirmLoading"
    @ok="onSubmit"
    @cancel="onCancel"
    :destroy-on-close="true"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
    >
      <!-- 基本信息 -->
      <a-divider orientation="left">基本信息</a-divider>
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="真实姓名" name="actualName">
            <a-input v-model:value="form.actualName" placeholder="请输入真实姓名" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="性别" name="gender">
            <a-select v-model:value="form.gender" placeholder="请选择性别">
              <a-select-option
                v-for="option in GENDER_OPTIONS"
                :key="option.value"
                :value="option.value"
              >
                {{ option.label }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="出生日期" name="birthDate">
            <a-date-picker
              v-model:value="form.birthDate"
              style="width: 100%"
              placeholder="请选择出生日期"
              :disabledDate="disabledBirthDate"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="手机号" name="phone">
            <a-input
              v-model:value="form.phone"
              placeholder="请输入手机号"
              :disabled="isEdit && form.registrationStatus === REGISTRATION_STATUS.ACTIVATED"
            />
          </a-form-item>
        </a-col>
      </a-row>
      
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="邮箱" name="email">
            <a-input v-model:value="form.email" placeholder="请输入邮箱" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="头像">
            <Upload
              v-model="form.avatarUrl"
              :max-count="1"
              :max-size="2"
              accept="image/*"
              list-type="picture-card"
              upload-text="上传头像"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 身份信息 -->
      <a-divider orientation="left">身份信息</a-divider>
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="身份证号" name="idCardNo">
            <a-input v-model:value="form.idCardNo" placeholder="请输入身份证号" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="骑手证号" name="riderCertNo">
            <a-input v-model:value="form.riderCertNo" placeholder="请输入骑手证号" />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 账号信息（仅已注册用户显示） -->
      <template v-if="showAccountInfo">
        <a-divider orientation="left">账号信息</a-divider>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="登录账号" name="loginName">
              <a-input
                v-model:value="form.loginName"
                placeholder="请输入登录账号"
                :disabled="isEdit"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item 
              :label="isEdit ? '新密码' : '登录密码'"
              :name="isEdit ? 'newPassword' : 'loginPwd'"
            >
              <a-input-password
                v-model:value="isEdit ? form.newPassword : form.loginPwd"
                :placeholder="isEdit ? '留空则不修改' : '请输入登录密码'"
                autocomplete="new-password"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </template>

      <!-- 会籍信息 -->
      <a-divider orientation="left">会籍信息</a-divider>
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="会籍类型" name="isMembership">
            <a-radio-group v-model:value="form.isMembership">
              <a-radio :value="MEMBERSHIP_STATUS.NORMAL">普通会员</a-radio>
              <a-radio :value="MEMBERSHIP_STATUS.MEMBER">会籍会员</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="form.isMembership === MEMBERSHIP_STATUS.MEMBER">
          <a-form-item label="会籍到期时间" name="membershipExpireDate">
            <a-date-picker
              v-model:value="form.membershipExpireDate"
              style="width: 100%"
              placeholder="请选择会籍到期时间"
              :disabledDate="disabledExpireDate"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 其他设置 -->
      <a-divider orientation="left">其他设置</a-divider>
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="注册类型" name="registrationStatus">
            <a-radio-group v-model:value="form.registrationStatus" :disabled="isEdit">
              <a-radio :value="REGISTRATION_STATUS.ACTIVATED">已注册</a-radio>
              <a-radio :value="REGISTRATION_STATUS.UNACTIVATED">未激活</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="form.registrationStatus === REGISTRATION_STATUS.UNACTIVATED">
          <a-form-item name="createdByGuardian">
            <template #label>
              <span>
                监护人创建
                <a-tooltip title="未激活用户是否由监护人创建">
                  <QuestionCircleOutlined style="margin-left: 4px" />
                </a-tooltip>
              </span>
            </template>
            <a-switch
              v-model:checked="form.createdByGuardian"
              :checked-value="CREATED_BY.GUARDIAN"
              :un-checked-value="CREATED_BY.SELF"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 年龄提示 -->
      <a-alert
        v-if="showMinorAlert"
        message="未成年人提醒"
        :description="`该会员年龄为${calculateAge(form.birthDate)}岁，属于未成年人，建议由监护人管理相关事务。`"
        type="warning"
        show-icon
        style="margin-bottom: 16px"
      />

      <!-- 未激活用户提示 -->
      <a-alert
        v-if="form.registrationStatus === REGISTRATION_STATUS.UNACTIVATED"
        message="未激活用户"
        description="未激活用户无法登录系统，需要通过激活流程完成注册后才能正常使用。"
        type="info"
        show-icon
      />
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { QuestionCircleOutlined } from '@ant-design/icons-vue'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'
import Upload from '/@/components/support/upload/index.vue'
import {
  REGISTRATION_STATUS,
  MEMBERSHIP_STATUS,
  GENDER_OPTIONS,
  CREATED_BY
} from '../constants/member-constants'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['reload'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)

const form = reactive({
  memberId: undefined,
  actualName: '',
  gender: 1,
  birthDate: undefined,
  phone: '',
  email: '',
  avatarUrl: '',
  idCardNo: '',
  riderCertNo: '',
  loginName: '',
  loginPwd: '',
  newPassword: '',
  isMembership: MEMBERSHIP_STATUS.NORMAL,
  membershipExpireDate: undefined,
  registrationStatus: REGISTRATION_STATUS.ACTIVATED,
  createdByGuardian: CREATED_BY.SELF,
  disabledFlag: 0
})

const rules = {
  actualName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { max: 50, message: '姓名长度不能超过50个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  idCardNo: [
    { pattern: /^[1-9]\d{5}(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[0-9Xx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ],
  loginName: [
    { required: true, message: '请输入登录账号', trigger: 'blur' },
    { min: 4, max: 20, message: '登录账号长度为4-20个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '登录账号只能包含字母、数字和下划线', trigger: 'blur' },
    { validator: validateLoginName, trigger: 'blur' }
  ],
  loginPwd: [
    { required: true, message: '请输入登录密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  membershipExpireDate: [
    { required: true, message: '请选择会籍到期时间', trigger: 'change' }
  ]
}

// ----------------------- 计算属性 -----------------------
const modalTitle = computed(() => {
  return isEdit.value ? '编辑会员' : '新建会员'
})

const showAccountInfo = computed(() => {
  return form.registrationStatus === REGISTRATION_STATUS.ACTIVATED
})

const showMinorAlert = computed(() => {
  return form.birthDate && calculateAge(form.birthDate) < 18
})

// ----------------------- 监听器 -----------------------
watch(
  () => form.registrationStatus,
  (newVal) => {
    if (newVal === REGISTRATION_STATUS.UNACTIVATED) {
      // 未激活用户清空账号信息
      form.loginName = ''
      form.loginPwd = ''
      form.newPassword = ''
    } else {
      // 已注册用户设置默认值
      form.createdByGuardian = CREATED_BY.SELF
    }
  }
)

watch(
  () => form.isMembership,
  (newVal) => {
    if (newVal === MEMBERSHIP_STATUS.NORMAL) {
      form.membershipExpireDate = undefined
    }
  }
)

// ----------------------- 表单验证 -----------------------
async function validatePhone(rule, value) {
  if (value && isEdit.value && form.registrationStatus === REGISTRATION_STATUS.ACTIVATED) {
    return Promise.resolve() // 已注册用户编辑时不验证手机号
  }
  
  if (value) {
    try {
      const res = await memberApi.checkPhoneExist(value, form.memberId)
      if (res.data) {
        return Promise.reject('该手机号已存在')
      }
    } catch (e) {
      console.warn('手机号验证失败：', e)
    }
  }
  return Promise.resolve()
}

async function validateLoginName(rule, value) {
  if (value && form.registrationStatus === REGISTRATION_STATUS.ACTIVATED) {
    try {
      const res = await memberApi.checkLoginNameExist(value, form.memberId)
      if (res.data) {
        return Promise.reject('该登录账号已存在')
      }
    } catch (e) {
      console.warn('登录账号验证失败：', e)
    }
  }
  return Promise.resolve()
}

// ----------------------- 工具函数 -----------------------
function calculateAge(birthDate) {
  if (!birthDate) return 0
  return dayjs().diff(dayjs(birthDate), 'year')
}

function disabledBirthDate(current) {
  // 不能选择未来日期
  return current && current > dayjs().endOf('day')
}

function disabledExpireDate(current) {
  // 不能选择过去日期
  return current && current < dayjs().startOf('day')
}

function generateTempLoginName() {
  return `temp_${Date.now().toString().slice(-8)}`
}

async function generateMemberNo() {
  try {
    const res = await memberApi.generateMemberNo()
    if (res.code === 1) {
      return res.data
    }
  } catch (e) {
    console.warn('生成会员编号失败：', e)
  }
  return `M${Date.now()}`
}

// ----------------------- 弹窗操作 -----------------------
async function showModal(record) {
  visible.value = true
  isEdit.value = !!record
  
  if (record) {
    // 编辑模式
    Object.assign(form, {
      memberId: record.memberId,
      actualName: record.actualName,
      gender: record.gender,
      birthDate: record.birthDate ? dayjs(record.birthDate) : undefined,
      phone: record.phone,
      email: record.email,
      avatarUrl: record.avatarUrl,
      idCardNo: record.idCardNo,
      riderCertNo: record.riderCertNo,
      loginName: record.loginName,
      loginPwd: '',
      newPassword: '',
      isMembership: record.isMembership,
      membershipExpireDate: record.membershipExpireDate ? dayjs(record.membershipExpireDate) : undefined,
      registrationStatus: record.registrationStatus,
      createdByGuardian: record.createdByGuardian,
      disabledFlag: record.disabledFlag
    })
  } else {
    // 新建模式
    const memberNo = await generateMemberNo()
    Object.assign(form, {
      memberId: undefined,
      actualName: '',
      gender: 1,
      birthDate: undefined,
      phone: '',
      email: '',
      avatarUrl: '',
      idCardNo: '',
      riderCertNo: '',
      loginName: '',
      loginPwd: '',
      newPassword: '',
      isMembership: MEMBERSHIP_STATUS.NORMAL,
      membershipExpireDate: undefined,
      registrationStatus: REGISTRATION_STATUS.ACTIVATED,
      createdByGuardian: CREATED_BY.SELF,
      disabledFlag: 0,
      memberNo
    })
  }
  
  // 重置表单验证
  setTimeout(() => {
    formRef.value?.clearValidate()
  }, 100)
}

function onCancel() {
  visible.value = false
  formRef.value?.resetFields()
}

async function onSubmit() {
  try {
    await formRef.value.validate()
    
    confirmLoading.value = true
    
    const submitData = { ...form }
    
    // 处理日期格式
    if (submitData.birthDate) {
      submitData.birthDate = dayjs(submitData.birthDate).format('YYYY-MM-DD')
    }
    if (submitData.membershipExpireDate) {
      submitData.membershipExpireDate = dayjs(submitData.membershipExpireDate).format('YYYY-MM-DD')
    }
    
    // 处理未激活用户的特殊逻辑
    if (submitData.registrationStatus === REGISTRATION_STATUS.UNACTIVATED) {
      submitData.loginName = generateTempLoginName()
      submitData.loginPwd = ''
      submitData.disabledFlag = 1 // 未激活用户默认禁用
    }
    
    // 清理不需要的字段
    delete submitData.newPassword
    
    let res
    if (isEdit.value) {
      // 编辑时处理密码
      if (form.newPassword) {
        submitData.loginPwd = form.newPassword
      } else {
        delete submitData.loginPwd
      }
      res = await memberApi.update(submitData)
    } else {
      res = await memberApi.create(submitData)
    }
    
    if (res.code === 1) {
      message.success(isEdit.value ? '编辑成功' : '新建成功')
      emits('reload')
      onCancel()
    } else {
      message.error((isEdit.value ? '编辑' : '新建') + '失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) {
      // 表单验证失败
      return
    }
    smartSentry.captureError(e)
    message.error((isEdit.value ? '编辑' : '新建') + '失败')
  } finally {
    confirmLoading.value = false
  }
}

// ----------------------- 暴露方法 -----------------------
defineExpose({
  showModal
})
</script>

<style scoped lang="less">
:deep(.ant-upload-select-picture-card) {
  width: 80px;
  height: 80px;
}

:deep(.ant-upload-list-picture-card .ant-upload-list-item) {
  width: 80px;
  height: 80px;
}

.ant-alert {
  margin-bottom: 16px;
}
</style>