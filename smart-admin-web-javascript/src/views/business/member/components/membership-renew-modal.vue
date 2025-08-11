<template>
  <a-modal
    v-model:open="visible"
    title="会籍续费"
    :width="600"
    :confirm-loading="confirmLoading"
    @ok="onSubmit"
    @cancel="onCancel"
    :destroy-on-close="true"
  >
    <div class="membership-renew-content">
      <!-- 当前会籍信息 -->
      <a-card size="small" title="当前会籍信息">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="会员姓名">{{ memberInfo.actualName }}</a-descriptions-item>
          <a-descriptions-item label="会员编号">{{ memberInfo.memberNo }}</a-descriptions-item>
          <a-descriptions-item label="当前状态">
            <a-tag color="gold">会籍会员</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="到期时间">
            <span :style="getExpireStyle(memberInfo.membershipExpireDate)">
              {{ memberInfo.membershipExpireDate ? dayjs(memberInfo.membershipExpireDate).format('YYYY-MM-DD') : '-' }}
            </span>
          </a-descriptions-item>
          <a-descriptions-item label="剩余天数" :span="2">
            <span :style="getExpireStyle(memberInfo.membershipExpireDate)">
              {{ getRemainingDays(memberInfo.membershipExpireDate) }}
            </span>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 续费表单 -->
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        style="margin-top: 16px"
      >
        <a-divider orientation="left">续费设置</a-divider>
        
        <a-form-item label="续费时长" name="renewMonths">
          <a-input-number
            v-model:value="form.renewMonths"
            :min="1"
            :max="60"
            style="width: 120px"
            @change="calculateNewExpireDate"
          />
          <span style="margin-left: 8px">个月</span>
        </a-form-item>
        
        <a-form-item label="续费金额" name="renewAmount">
          <a-input-number
            v-model:value="form.renewAmount"
            :min="0"
            :precision="2"
            style="width: 150px"
            placeholder="请输入续费金额"
          />
          <span style="margin-left: 8px">元</span>
        </a-form-item>
        
        <a-form-item label="新到期时间" name="newExpireDate">
          <a-date-picker
            v-model:value="form.newExpireDate"
            style="width: 100%"
            placeholder="请选择新的到期时间"
            :disabled-date="disabledDate"
          />
        </a-form-item>
        
        <a-form-item label="支付方式" name="paymentMethod">
          <a-select v-model:value="form.paymentMethod" placeholder="请选择支付方式">
            <a-select-option value="cash">现金</a-select-option>
            <a-select-option value="wechat">微信支付</a-select-option>
            <a-select-option value="alipay">支付宝</a-select-option>
            <a-select-option value="bank">银行转账</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="续费备注" name="remark">
          <a-textarea
            v-model:value="form.remark"
            placeholder="请输入续费备注（可选）"
            :rows="3"
          />
        </a-form-item>

        <!-- 续费预览 -->
        <a-alert
          v-if="form.renewMonths && form.newExpireDate"
          :message="`续费预览：会籍将延长${form.renewMonths}个月，新的到期时间为 ${dayjs(form.newExpireDate).format('YYYY年MM月DD日')}`"
          type="info"
          show-icon
          style="margin-bottom: 16px"
        />
      </a-form>

      <!-- 历史记录 -->
      <a-card size="small" title="续费历史" style="margin-top: 16px">
        <a-table
          :data-source="renewHistory"
          :columns="historyColumns"
          :loading="historyLoading"
          :pagination="false"
          size="small"
          :scroll="{ y: 200 }"
        >
          <template #renewAmount="{ record }">
            ¥{{ record.renewAmount }}
          </template>
          
          <template #renewDate="{ record }">
            {{ dayjs(record.renewDate).format('YYYY-MM-DD') }}
          </template>
        </a-table>
      </a-card>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['reload'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const historyLoading = ref(false)
const formRef = ref()

const memberInfo = ref({})
const renewHistory = ref([])

const form = reactive({
  renewMonths: 12,
  renewAmount: 0,
  newExpireDate: undefined,
  paymentMethod: 'cash',
  remark: ''
})

const rules = {
  renewMonths: [
    { required: true, message: '请输入续费时长', trigger: 'change' },
    { type: 'number', min: 1, max: 60, message: '续费时长范围为1-60个月', trigger: 'change' }
  ],
  renewAmount: [
    { required: true, message: '请输入续费金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '续费金额不能小于0', trigger: 'blur' }
  ],
  newExpireDate: [
    { required: true, message: '请选择新的到期时间', trigger: 'change' }
  ],
  paymentMethod: [
    { required: true, message: '请选择支付方式', trigger: 'change' }
  ]
}

// ----------------------- 表格列配置 -----------------------
const historyColumns = [
  {
    title: '续费时间',
    dataIndex: 'renewDate',
    width: 100,
    customRender: { name: 'renewDate' }
  },
  {
    title: '续费时长',
    dataIndex: 'renewMonths',
    width: 80,
    customRender: ({ text }) => `${text}个月`
  },
  {
    title: '续费金额',
    dataIndex: 'renewAmount',
    width: 80,
    customRender: { name: 'renewAmount' }
  },
  {
    title: '支付方式',
    dataIndex: 'paymentMethod',
    width: 80,
    customRender: ({ text }) => getPaymentMethodText(text)
  },
  {
    title: '备注',
    dataIndex: 'remark',
    ellipsis: true
  }
]

// ----------------------- 监听器 -----------------------
watch(
  () => form.renewMonths,
  () => {
    calculateNewExpireDate()
  }
)

// ----------------------- 工具函数 -----------------------
function getRemainingDays(expireDate) {
  if (!expireDate) return '-'
  const diff = dayjs(expireDate).diff(dayjs(), 'day')
  if (diff < 0) return '已过期'
  return `${diff}天`
}

function getExpireStyle(expireDate) {
  if (!expireDate) return {}
  
  const diff = dayjs(expireDate).diff(dayjs(), 'day')
  if (diff < 0) {
    return { color: '#ff4d4f' } // 已过期 - 红色
  } else if (diff <= 30) {
    return { color: '#fa8c16' } // 30天内到期 - 橙色
  }
  return {}
}

function getPaymentMethodText(method) {
  const methodMap = {
    cash: '现金',
    wechat: '微信支付',
    alipay: '支付宝',
    bank: '银行转账'
  }
  return methodMap[method] || method
}

function calculateNewExpireDate() {
  if (!form.renewMonths) {
    form.newExpireDate = undefined
    return
  }
  
  // 计算新的到期时间
  const currentExpire = memberInfo.value.membershipExpireDate || dayjs().format('YYYY-MM-DD')
  const baseDate = dayjs(currentExpire).isAfter(dayjs()) ? dayjs(currentExpire) : dayjs()
  const newExpireDate = baseDate.add(form.renewMonths, 'month')
  form.newExpireDate = newExpireDate
}

function disabledDate(current) {
  // 不能选择过去的日期
  return current && current < dayjs().startOf('day')
}

// ----------------------- API 调用 -----------------------
async function loadRenewHistory() {
  if (!memberInfo.value.memberId) return
  
  historyLoading.value = true
  try {
    const res = await memberApi.getMembershipHistory(memberInfo.value.memberId)
    if (res.code === 1) {
      renewHistory.value = res.data || []
    }
  } catch (e) {
    console.warn('获取续费历史失败：', e)
  } finally {
    historyLoading.value = false
  }
}

// ----------------------- 弹窗操作 -----------------------
async function showModal(member) {
  memberInfo.value = { ...member }
  visible.value = true
  
  // 重置表单
  Object.assign(form, {
    renewMonths: 12,
    renewAmount: 0,
    newExpireDate: undefined,
    paymentMethod: 'cash',
    remark: ''
  })
  
  // 计算新的到期时间
  calculateNewExpireDate()
  
  // 加载续费历史
  await loadRenewHistory()
  
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
      memberId: memberInfo.value.memberId,
      renewMonths: form.renewMonths,
      renewAmount: form.renewAmount,
      newExpireDate: dayjs(form.newExpireDate).format('YYYY-MM-DD'),
      paymentMethod: form.paymentMethod,
      remark: form.remark,
      oldExpireDate: memberInfo.value.membershipExpireDate
    }
    
    const res = await memberApi.renewMembership(submitData)
    
    if (res.code === 1) {
      message.success('会籍续费成功')
      emits('reload')
      onCancel()
    } else {
      message.error('续费失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) {
      // 表单验证失败
      return
    }
    smartSentry.captureError(e)
    message.error('续费失败')
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
.membership-renew-content {
  .ant-card {
    margin-bottom: 0;
  }
  
  .ant-alert {
    margin-bottom: 16px;
  }
}
</style>