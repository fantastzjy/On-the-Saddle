<template>
  <a-modal
    v-model:open="visible"
    title="添加家庭成员"
    :width="600"
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
      <a-divider orientation="left">成员基本信息</a-divider>
      
      <a-form-item label="真实姓名" name="actualName">
        <a-input v-model:value="form.actualName" placeholder="请输入真实姓名" />
      </a-form-item>
      
      <a-form-item label="性别" name="gender">
        <a-radio-group v-model:value="form.gender">
          <a-radio :value="1">男</a-radio>
          <a-radio :value="2">女</a-radio>
        </a-radio-group>
      </a-form-item>
      
      <a-form-item label="出生日期" name="birthDate">
        <a-date-picker
          v-model:value="form.birthDate"
          style="width: 100%"
          placeholder="请选择出生日期"
          :disabledDate="disabledBirthDate"
        />
      </a-form-item>
      
      <a-form-item label="手机号" name="phone">
        <a-input v-model:value="form.phone" placeholder="请输入手机号（可选）" />
      </a-form-item>
      
      <a-form-item label="身份证号" name="idCardNo">
        <a-input v-model:value="form.idCardNo" placeholder="请输入身份证号（可选）" />
      </a-form-item>
      
      <a-form-item label="骑手证号" name="riderCertNo">
        <a-input v-model:value="form.riderCertNo" placeholder="请输入骑手证号（可选）" />
      </a-form-item>

      <!-- 监护人信息（未成年人必填） -->
      <template v-if="showGuardianInfo">
        <a-divider orientation="left">监护人信息</a-divider>
        
        <a-alert
          message="未成年人提醒"
          :description="`该成员年龄为${calculateAge(form.birthDate)}岁，属于未成年人，请填写监护人信息。`"
          type="warning"
          show-icon
          style="margin-bottom: 16px"
        />
        
        <a-form-item label="监护人姓名" name="guardianName">
          <a-input v-model:value="form.guardianName" placeholder="请输入监护人姓名" />
        </a-form-item>
        
        <a-form-item label="监护人电话" name="guardianPhone">
          <a-input v-model:value="form.guardianPhone" placeholder="请输入监护人电话" />
        </a-form-item>
        
        <a-form-item label="关系" name="guardianRelation">
          <a-select v-model:value="form.guardianRelation" placeholder="请选择与监护人的关系">
            <a-select-option value="父亲">父亲</a-select-option>
            <a-select-option value="母亲">母亲</a-select-option>
            <a-select-option value="爷爷">爷爷</a-select-option>
            <a-select-option value="奶奶">奶奶</a-select-option>
            <a-select-option value="外公">外公</a-select-option>
            <a-select-option value="外婆">外婆</a-select-option>
            <a-select-option value="其他">其他</a-select-option>
          </a-select>
        </a-form-item>
      </template>

      <!-- 骑行信息 -->
      <a-divider orientation="left">骑行相关</a-divider>
      
      <a-form-item label="默认教练" name="defaultCoachId">
        <a-select
          v-model:value="form.defaultCoachId"
          placeholder="请选择默认教练（可选）"
          allow-clear
          show-search
          :filter-option="false"
          @search="onSearchCoach"
          @change="onCoachChange"
        >
          <a-select-option
            v-for="coach in coachList"
            :key="coach.coachId"
            :value="coach.coachId"
          >
            {{ coach.actualName }} - {{ coach.coachNo }}
          </a-select-option>
        </a-select>
      </a-form-item>
      
      <a-form-item label="课程级别偏好" name="courseLevelPreference">
        <a-select v-model:value="form.courseLevelPreference" placeholder="请选择课程级别偏好（可选）">
          <a-select-option value="初级">初级</a-select-option>
          <a-select-option value="中级">中级</a-select-option>
          <a-select-option value="高级">高级</a-select-option>
          <a-select-option value="专业">专业</a-select-option>
        </a-select>
      </a-form-item>
      
      <a-form-item label="备注" name="remark">
        <a-textarea
          v-model:value="form.remark"
          placeholder="请输入备注信息（可选）"
          :rows="3"
        />
      </a-form-item>

      <!-- 提示信息 -->
      <a-alert
        message="注意事项"
        description="添加的家庭成员将创建为未激活状态，成员可以通过手机号激活账号后正常使用系统。"
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
import { memberApi } from '/@/api/business/member-api'
import { coachApi } from '/@/api/business/coach/coach-api'
import { smartSentry } from '/@/lib/smart-sentry'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['success'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const formRef = ref()
const coachList = ref([])

const familyGroupInfo = ref({
  familyGroupId: null,
  clubId: null
})

const form = reactive({
  actualName: '',
  gender: 1,
  birthDate: undefined,
  phone: '',
  idCardNo: '',
  riderCertNo: '',
  guardianName: '',
  guardianPhone: '',
  guardianRelation: '',
  defaultCoachId: undefined,
  courseLevelPreference: '',
  remark: ''
})

const rules = {
  actualName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { max: 50, message: '姓名长度不能超过50个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  birthDate: [
    { required: true, message: '请选择出生日期', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  idCardNo: [
    { pattern: /^[1-9]\d{5}(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[0-9Xx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ],
  guardianName: [
    { required: true, message: '请输入监护人姓名', trigger: 'blur' }
  ],
  guardianPhone: [
    { required: true, message: '请输入监护人电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '监护人电话格式不正确', trigger: 'blur' }
  ],
  guardianRelation: [
    { required: true, message: '请选择与监护人的关系', trigger: 'change' }
  ]
}

// ----------------------- 计算属性 -----------------------
const showGuardianInfo = computed(() => {
  return form.birthDate && calculateAge(form.birthDate) < 18
})

// ----------------------- 监听器 -----------------------
watch(
  () => form.birthDate,
  () => {
    // 当年龄变化时，清空监护人信息（如果已成年）
    if (!showGuardianInfo.value) {
      form.guardianName = ''
      form.guardianPhone = ''
      form.guardianRelation = ''
    }
  }
)

// ----------------------- 表单验证 -----------------------
async function validatePhone(rule, value) {
  if (value) {
    try {
      const res = await memberApi.checkPhoneExist(value)
      if (res.data) {
        return Promise.reject('该手机号已存在')
      }
    } catch (e) {
      console.warn('手机号验证失败：', e)
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

function generateTempLoginName() {
  return `temp_${Date.now().toString().slice(-8)}`
}

// ----------------------- 教练相关 -----------------------
async function loadCoachList() {
  if (!familyGroupInfo.value.clubId) return
  
  try {
    const res = await coachApi.queryList(true, familyGroupInfo.value.clubId)
    if (res.code === 0 && res.ok) {
      coachList.value = res.data || []
    }
  } catch (e) {
    console.warn('获取教练列表失败：', e)
  }
}

function onSearchCoach(searchText) {
  // 这里可以实现教练搜索逻辑
  console.log('搜索教练：', searchText)
}

function onCoachChange(coachId) {
  console.log('选择教练：', coachId)
}

// ----------------------- 弹窗操作 -----------------------
async function showModal({ familyGroupId, clubId }) {
  familyGroupInfo.value = { familyGroupId, clubId }
  
  // 重置表单
  Object.assign(form, {
    actualName: '',
    gender: 1,
    birthDate: undefined,
    phone: '',
    idCardNo: '',
    riderCertNo: '',
    guardianName: '',
    guardianPhone: '',
    guardianRelation: '',
    defaultCoachId: undefined,
    courseLevelPreference: '',
    remark: ''
  })
  
  visible.value = true
  
  // 加载教练列表
  await loadCoachList()
  
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
    
    const submitData = {
      ...form,
      familyGroupId: familyGroupInfo.value.familyGroupId,
      clubId: familyGroupInfo.value.clubId
    }
    
    // 处理日期格式
    if (submitData.birthDate) {
      submitData.birthDate = dayjs(submitData.birthDate).format('YYYY-MM-DD')
    }
    
    // 设置默认值
    submitData.memberNo = `M${Date.now()}` // 临时编号，后端会重新生成
    submitData.loginName = generateTempLoginName()
    submitData.registrationStatus = 0 // 未激活
    submitData.createdByGuardian = 1 // 监护人创建
    submitData.disabledFlag = 1 // 禁用登录
    submitData.isMembership = 0 // 普通会员
    
    // 构建扩展信息
    const extraData = {}
    if (form.guardianName) extraData.guardianName = form.guardianName
    if (form.guardianPhone) extraData.guardianPhone = form.guardianPhone
    if (form.guardianRelation) extraData.guardianRelation = form.guardianRelation
    if (form.defaultCoachId) extraData.defaultCoachId = form.defaultCoachId
    if (form.courseLevelPreference) extraData.courseLevelPreference = form.courseLevelPreference
    
    submitData.profileData = JSON.stringify(extraData)
    
    const res = await memberApi.addFamilyMember(submitData)
    
    if (res.code === 0 && res.ok) {
      message.success(res.msg || '添加家庭成员成功')
      emits('success')
      onCancel()
    } else {
      message.error('添加失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) {
      // 表单验证失败
      return
    }
    smartSentry.captureError(e)
    message.error('添加失败')
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
.ant-alert {
  margin-bottom: 16px;
}
</style>