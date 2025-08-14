<template>
  <a-modal
    v-model:open="visible"
    title="添加家庭成员"
    :width="700"
    :footer="null"
    :destroy-on-close="true"
  >
    <a-tabs v-model:activeKey="activeTab" @change="onTabChange">
      <!-- 添加现有会员 -->
      <a-tab-pane key="existing" tab="添加现有会员">
        <div class="existing-member-content">
          <!-- 搜索会员 -->
          <a-form layout="inline" style="margin-bottom: 16px">
            <a-form-item label="搜索方式">
              <a-select v-model:value="searchType" style="width: 120px">
                <a-select-option value="name">姓名</a-select-option>
                <a-select-option value="phone">手机号</a-select-option>
                <a-select-option value="memberNo">会员编号</a-select-option>
              </a-select>
            </a-form-item>
            
            <a-form-item>
              <a-input-search
                v-model:value="searchKeyword"
                :placeholder="getSearchPlaceholder()"
                @search="onSearchMembers"
                enter-button="搜索"
                style="width: 300px"
              />
            </a-form-item>
          </a-form>

          <!-- 搜索结果 -->
          <div v-if="searchResults.length > 0" class="search-results">
            <a-list :data-source="searchResults" size="small">
              <template #renderItem="{ item }">
                <a-list-item>
                  <template #actions>
                    <a-button
                      type="primary"
                      size="small"
                      @click="selectMember(item)"
                      :disabled="selectedMember?.memberId === item.memberId"
                    >
                      {{ selectedMember?.memberId === item.memberId ? '已选择' : '选择' }}
                    </a-button>
                  </template>
                  
                  <a-list-item-meta>
                    <template #title>
                      <span>{{ item.actualName }}</span>
                      <a-tag color="blue" style="margin-left: 8px">{{ item.memberNo }}</a-tag>
                    </template>
                    
                    <template #description>
                      <div>
                        <span>手机号: {{ item.phone || '-' }}</span>
                        <span style="margin-left: 16px">
                          年龄: {{ calculateAge(item.birthDate) }}岁
                        </span>
                        <span style="margin-left: 16px">
                          状态: 
                          <a-tag v-if="item.registrationStatus === 1" color="success">已注册</a-tag>
                          <a-tag v-else color="warning">未激活</a-tag>
                        </span>
                      </div>
                    </template>
                  </a-list-item-meta>
                </a-list-item>
              </template>
            </a-list>
          </div>

          <!-- 未找到结果 -->
          <div v-else-if="hasSearched && searchResults.length === 0" class="no-results">
            <a-empty description="未找到匹配的会员" />
          </div>

          <!-- 选中的会员信息 -->
          <div v-if="selectedMember" class="selected-member">
            <a-divider>选中的会员</a-divider>
            <a-descriptions :column="2" size="small" bordered>
              <a-descriptions-item label="姓名">{{ selectedMember.actualName }}</a-descriptions-item>
              <a-descriptions-item label="会员编号">{{ selectedMember.memberNo }}</a-descriptions-item>
              <a-descriptions-item label="手机号">{{ selectedMember.phone || '-' }}</a-descriptions-item>
              <a-descriptions-item label="年龄">{{ calculateAge(selectedMember.birthDate) }}岁</a-descriptions-item>
            </a-descriptions>
            
            <!-- 关系信息表单 -->
            <a-form
              ref="relationFormRef"
              :model="relationForm"
              :rules="relationRules"
              style="margin-top: 16px"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 16 }"
            >
              <a-form-item label="关系备注" name="remark">
                <a-input
                  v-model:value="relationForm.remark"
                  placeholder="请输入与家庭成员的关系备注"
                />
              </a-form-item>
              
              <a-form-item label="是否监护人" name="isGuardian">
                <a-switch
                  v-model:checked="relationForm.isGuardian"
                  checked-children="是"
                  un-checked-children="否"
                />
              </a-form-item>
            </a-form>

            <div class="form-actions">
              <a-space>
                <a-button @click="onCancel">取消</a-button>
                <a-button type="primary" @click="onSubmitExisting" :loading="confirmLoading">
                  确认添加
                </a-button>
              </a-space>
            </div>
          </div>
        </div>
      </a-tab-pane>

      <!-- 创建新会员 -->
      <a-tab-pane key="create" tab="创建新会员">
        <div class="create-member-content">
          <a-alert
            message="功能说明"
            description="创建新会员将同时为其创建账号并加入当前家庭组"
            type="info"
            show-icon
            style="margin-bottom: 16px"
          />
          
          <a-form
            ref="createFormRef"
            :model="createForm"
            :rules="createRules"
            :label-col="{ span: 6 }"
            :wrapper-col="{ span: 16 }"
          >
            <!-- 基本信息 -->
            <a-divider orientation="left">基本信息</a-divider>
            
            <a-form-item label="真实姓名" name="actualName">
              <a-input v-model:value="createForm.actualName" placeholder="请输入真实姓名" />
            </a-form-item>
            
            <a-form-item label="性别" name="gender">
              <a-radio-group v-model:value="createForm.gender">
                <a-radio :value="1">男</a-radio>
                <a-radio :value="2">女</a-radio>
              </a-radio-group>
            </a-form-item>
            
            <a-form-item label="出生日期" name="birthDate">
              <a-date-picker
                v-model:value="createForm.birthDate"
                style="width: 100%"
                placeholder="请选择出生日期"
              />
            </a-form-item>
            
            <a-form-item label="手机号" name="phone">
              <a-input v-model:value="createForm.phone" placeholder="请输入手机号（可选）" />
            </a-form-item>

            <!-- 家庭关系 -->
            <a-divider orientation="left">家庭关系</a-divider>
            
            <a-form-item label="关系备注" name="remark">
              <a-input v-model:value="createForm.remark" placeholder="请输入关系备注" />
            </a-form-item>
            
            <a-form-item label="是否监护人" name="isGuardian">
              <a-switch
                v-model:checked="createForm.isGuardian"
                checked-children="是"
                un-checked-children="否"
              />
            </a-form-item>

            <div class="form-actions">
              <a-space>
                <a-button @click="onCancel">取消</a-button>
                <a-button type="primary" @click="onSubmitCreate" :loading="confirmLoading">
                  确认创建
                </a-button>
              </a-space>
            </div>
          </a-form>
        </div>
      </a-tab-pane>
    </a-tabs>
  </a-modal>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['success'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const activeTab = ref('existing')
const relationFormRef = ref()
const createFormRef = ref()

const familyGroupInfo = ref({
  familyGroupId: null,
  clubId: null
})

const searchType = ref('name')
const searchKeyword = ref('')
const searchResults = ref([])
const hasSearched = ref(false)
const selectedMember = ref(null)

const relationForm = reactive({
  remark: '',
  isGuardian: false
})

const createForm = reactive({
  actualName: '',
  gender: 1,
  birthDate: undefined,
  phone: '',
  remark: '',
  isGuardian: false
})

// ----------------------- 表单验证规则 -----------------------
const relationRules = {
  remark: [
    { required: true, message: '请输入关系备注', trigger: 'blur' }
  ]
}

const createRules = {
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
  remark: [
    { required: true, message: '请输入关系备注', trigger: 'blur' }
  ]
}

// ----------------------- 计算属性 -----------------------
function getSearchPlaceholder() {
  const placeholders = {
    name: '请输入成员姓名',
    phone: '请输入手机号',
    memberNo: '请输入会员编号'
  }
  return placeholders[searchType.value] || '请输入搜索关键字'
}

function calculateAge(birthDate) {
  if (!birthDate) return 0
  return dayjs().diff(dayjs(birthDate), 'year')
}

// ----------------------- 方法 -----------------------
async function onSearchMembers() {
  if (!searchKeyword.value.trim()) {
    message.warning('请输入搜索关键字')
    return
  }

  try {
    const params = {
      [searchType.value]: searchKeyword.value.trim(),
      clubId: familyGroupInfo.value.clubId,
      pageNum: 1,
      pageSize: 10
    }
    
    const res = await memberApi.pageQuery(params)
    if (res.code === 0 && res.ok) {
      searchResults.value = res.data.list || []
      hasSearched.value = true
      
      if (searchResults.value.length === 0) {
        message.info('未找到匹配的会员')
      }
    } else {
      message.error('搜索失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('搜索失败')
  }
}

function selectMember(member) {
  selectedMember.value = member
  relationForm.remark = ''
  relationForm.isGuardian = false
}

function onTabChange(key) {
  activeTab.value = key
  
  // 切换标签时清空数据
  if (key === 'existing') {
    searchResults.value = []
    hasSearched.value = false
    selectedMember.value = null
    searchKeyword.value = ''
  } else if (key === 'create') {
    Object.assign(createForm, {
      actualName: '',
      gender: 1,
      birthDate: undefined,
      phone: '',
      remark: '',
      isGuardian: false
    })
  }
}

async function onSubmitExisting() {
  if (!selectedMember.value) {
    message.warning('请先选择要添加的会员')
    return
  }

  try {
    await relationFormRef.value.validate()
    
    confirmLoading.value = true
    
    const submitData = {
      familyGroupId: familyGroupInfo.value.familyGroupId,
      memberId: selectedMember.value.memberId,
      remark: relationForm.remark,
      isGuardian: relationForm.isGuardian ? 1 : 0,
      joinDate: dayjs().format('YYYY-MM-DD')
    }
    
    const res = await memberApi.joinFamilyGroup(submitData)
    
    if (res.code === 0 && res.ok) {
      message.success('添加会员成功')
      emits('success')
      onCancel()
    } else {
      message.error('添加失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) return
    smartSentry.captureError(e)
    message.error('添加失败')
  } finally {
    confirmLoading.value = false
  }
}

async function onSubmitCreate() {
  try {
    await createFormRef.value.validate()
    
    confirmLoading.value = true
    
    const submitData = {
      familyGroupId: familyGroupInfo.value.familyGroupId,
      clubId: familyGroupInfo.value.clubId,
      actualName: createForm.actualName,
      gender: createForm.gender,
      birthDate: dayjs(createForm.birthDate).format('YYYY-MM-DD'),
      phone: createForm.phone,
      remark: createForm.remark,
      isGuardian: createForm.isGuardian ? 1 : 0
    }
    
    const res = await memberApi.addFamilyMember(submitData)
    
    if (res.code === 0 && res.ok) {
      message.success('创建会员成功')
      emits('success')
      onCancel()
    } else {
      message.error('创建失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) return
    smartSentry.captureError(e)
    message.error('创建失败')
  } finally {
    confirmLoading.value = false
  }
}

function showModal(familyInfo) {
  familyGroupInfo.value = familyInfo
  activeTab.value = 'existing'
  visible.value = true
  
  // 清空数据
  searchResults.value = []
  hasSearched.value = false
  selectedMember.value = null
  searchKeyword.value = ''
  relationForm.remark = ''
  relationForm.isGuardian = false
}

function onCancel() {
  visible.value = false
  
  // 重置表单
  setTimeout(() => {
    relationFormRef.value?.resetFields()
    createFormRef.value?.resetFields()
  }, 100)
}

// ----------------------- 暴露方法 -----------------------
defineExpose({
  showModal
})
</script>

<style scoped lang="less">
.existing-member-content,
.create-member-content {
  .search-results {
    max-height: 300px;
    overflow-y: auto;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    margin-bottom: 16px;
  }

  .no-results {
    text-align: center;
    padding: 40px 0;
  }

  .selected-member {
    padding: 16px;
    background: #f5f5f5;
    border-radius: 6px;
    margin-top: 16px;
  }

  .form-actions {
    text-align: right;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
}
</style>