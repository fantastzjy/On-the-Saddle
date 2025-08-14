<template>
  <a-modal
    v-model:open="visible"
    title="加入家庭组"
    :width="700"
    :confirm-loading="confirmLoading"
    @ok="onSubmit"
    @cancel="onCancel"
    :destroy-on-close="true"
  >
    <div class="join-family-content">
      <!-- 会员信息 -->
      <a-card size="small" title="会员信息" class="member-info-card">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="姓名">{{ memberInfo.actualName }}</a-descriptions-item>
          <a-descriptions-item label="会员编号">{{ memberInfo.memberNo }}</a-descriptions-item>
          <a-descriptions-item label="手机号">{{ memberInfo.phone }}</a-descriptions-item>
          <a-descriptions-item label="年龄">{{ calculateAge(memberInfo.birthDate) }}岁</a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 搜索家庭组 -->
      <a-card size="small" title="搜索家庭组" class="search-card">
        <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
          <a-form-item label="搜索方式">
            <a-radio-group v-model:value="searchType" @change="onSearchTypeChange">
              <a-radio value="familyName">按家庭名称搜索</a-radio>
              <a-radio value="memberName">按成员姓名搜索</a-radio>
              <a-radio value="memberPhone">按成员手机号搜索</a-radio>
            </a-radio-group>
          </a-form-item>
          
          <a-form-item :label="getSearchLabel()">
            <a-input-search
              v-model:value="searchKeyword"
              :placeholder="getSearchPlaceholder()"
              @search="onSearch"
              enter-button="搜索"
            />
          </a-form-item>
        </a-form>
      </a-card>

      <!-- 搜索结果 -->
      <a-card size="small" v-if="searchResults.length > 0" title="搜索结果" class="results-card">
        <a-list :data-source="searchResults" size="small">
          <template #renderItem="{ item }">
            <a-list-item>
              <template #actions>
                <a-button
                  type="primary"
                  size="small"
                  @click="selectFamily(item)"
                  :disabled="selectedFamily?.familyGroupId === item.familyGroupId"
                >
                  {{ selectedFamily?.familyGroupId === item.familyGroupId ? '已选择' : '选择此家庭' }}
                </a-button>
              </template>
              
              <a-list-item-meta>
                <template #title>
                  <span class="family-name">{{ item.familyName }}</span>
                  <a-tag color="blue" style="margin-left: 8px">{{ item.memberCount }}人</a-tag>
                </template>
                
                <template #description>
                  <div class="family-info">
                    <div>主要联系人: {{ item.mainContactName }}</div>
                    <div>创建时间: {{ dayjs(item.createTime).format('YYYY-MM-DD') }}</div>
                    <div v-if="item.description">描述: {{ item.description }}</div>
                  </div>
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-card>

      <!-- 未找到结果 -->
      <a-card size="small" v-else-if="hasSearched && searchResults.length === 0" class="no-results-card">
        <a-empty description="未找到匹配的家庭组">
          <template #image>
            <a-icon type="search" style="font-size: 48px; color: #d9d9d9" />
          </template>
        </a-empty>
      </a-card>

      <!-- 选中的家庭组信息 -->
      <a-card size="small" v-if="selectedFamily" title="选中的家庭组" class="selected-family-card">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="家庭名称">{{ selectedFamily.familyName }}</a-descriptions-item>
          <a-descriptions-item label="主要联系人">{{ selectedFamily.mainContactName }}</a-descriptions-item>
          <a-descriptions-item label="成员数量">{{ selectedFamily.memberCount }}人</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ dayjs(selectedFamily.createTime).format('YYYY-MM-DD') }}</a-descriptions-item>
          <a-descriptions-item label="家庭描述" :span="2">
            {{ selectedFamily.description || '暂无描述' }}
          </a-descriptions-item>
        </a-descriptions>
        
        <!-- 关系和备注 -->
        <a-divider />
        <a-form ref="relationFormRef" :model="relationForm" :rules="relationRules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
          <a-form-item label="关系备注" name="remark">
            <a-input
              v-model:value="relationForm.remark"
              placeholder="请输入与家庭成员的关系备注，如：儿子、女儿、父亲、母亲等"
            />
          </a-form-item>
        </a-form>
      </a-card>

      <!-- 提示信息 -->
      <a-alert
        v-if="selectedFamily"
        message="加入提醒"
        description="加入家庭组后，该会员将与家庭组内其他成员共享家庭信息，请确认要加入此家庭组。"
        type="info"
        show-icon
        class="alert-info"
      />
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { memberApi } from '/@/api/business/member-api'
import { smartSentry } from '/@/lib/smart-sentry'

// ----------------------- 事件 -----------------------
const emits = defineEmits(['success'])

// ----------------------- 响应式数据 -----------------------
const visible = ref(false)
const confirmLoading = ref(false)
const memberInfo = ref({})
const searchType = ref('familyName')
const searchKeyword = ref('')
const searchResults = ref([])
const hasSearched = ref(false)
const selectedFamily = ref(null)
const relationFormRef = ref()

const relationForm = reactive({
  remark: ''
})

const relationRules = {
  remark: [
    { required: true, message: '请输入关系备注', trigger: 'blur' },
    { max: 50, message: '备注长度不能超过50个字符', trigger: 'blur' }
  ]
}

// ----------------------- 工具函数 -----------------------
function calculateAge(birthDate) {
  if (!birthDate) return 0
  return dayjs().diff(dayjs(birthDate), 'year')
}

function getSearchLabel() {
  const labels = {
    familyName: '家庭名称',
    memberName: '成员姓名',
    memberPhone: '成员手机号'
  }
  return labels[searchType.value] || '搜索关键字'
}

function getSearchPlaceholder() {
  const placeholders = {
    familyName: '请输入家庭名称关键字',
    memberName: '请输入成员姓名',
    memberPhone: '请输入成员手机号'
  }
  return placeholders[searchType.value] || '请输入搜索关键字'
}

// ----------------------- 搜索相关 -----------------------
function onSearchTypeChange() {
  searchKeyword.value = ''
  searchResults.value = []
  hasSearched.value = false
  selectedFamily.value = null
}

async function onSearch() {
  if (!searchKeyword.value.trim()) {
    message.warning('请输入搜索关键字')
    return
  }

  try {
    const params = {
      searchType: searchType.value,
      keyword: searchKeyword.value.trim(),
      clubId: memberInfo.value.clubId
    }
    
    const res = await memberApi.searchFamilyGroups(params)
    if (res.code === 0 && res.ok) {
      searchResults.value = res.data || []
      hasSearched.value = true
      
      if (searchResults.value.length === 0) {
        message.info('未找到匹配的家庭组')
      }
    } else {
      message.error('搜索失败：' + res.msg)
    }
  } catch (e) {
    smartSentry.captureError(e)
    message.error('搜索失败')
  }
}

function selectFamily(family) {
  selectedFamily.value = family
  relationForm.remark = ''
}

// ----------------------- 弹窗操作 -----------------------
function showModal(member) {
  memberInfo.value = { ...member }
  searchType.value = 'familyName'
  searchKeyword.value = ''
  searchResults.value = []
  hasSearched.value = false
  selectedFamily.value = null
  relationForm.remark = ''
  visible.value = true
}

function onCancel() {
  visible.value = false
}

async function onSubmit() {
  if (!selectedFamily.value) {
    message.warning('请先选择要加入的家庭组')
    return
  }

  try {
    await relationFormRef.value.validate()
    
    confirmLoading.value = true
    
    const submitData = {
      familyGroupId: selectedFamily.value.familyGroupId,
      memberId: memberInfo.value.memberId,
      remark: relationForm.remark,
      isGuardian: 0,
      joinDate: dayjs().format('YYYY-MM-DD')
    }
    
    const res = await memberApi.joinFamilyGroup(submitData)
    
    if (res.code === 0 && res.ok) {
      message.success('加入家庭组成功')
      emits('success')
      onCancel()
    } else {
      message.error('加入失败：' + res.msg)
    }
  } catch (e) {
    if (e?.errorFields) {
      return
    }
    smartSentry.captureError(e)
    message.error('加入失败')
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
.join-family-content {
  .member-info-card,
  .search-card,
  .results-card,
  .no-results-card,
  .selected-family-card {
    margin-bottom: 16px;
  }
  
  .alert-info {
    margin-top: 16px;
  }
}

.family-name {
  font-weight: 500;
  color: #262626;
}

.family-info {
  color: #8c8c8c;
  
  div {
    margin-bottom: 4px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}

.selected-family-card {
  border: 1px solid #1890ff;
  
  :deep(.ant-card-head) {
    background-color: #f0f8ff;
  }
}
</style>