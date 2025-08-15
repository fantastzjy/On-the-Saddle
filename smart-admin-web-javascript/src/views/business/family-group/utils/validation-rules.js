/**
 * 家庭组表单验证规则
 * 统一新建和编辑弹窗的验证逻辑
 */

// 通用验证规则
export const commonValidationRules = {
  familyName: [
    { required: true, message: '请输入家庭名称', trigger: 'blur' },
    { max: 50, message: '家庭名称长度不能超过50个字符', trigger: 'blur' }
  ],
  mainContactId: [
    { required: true, message: '请选择主要联系人', trigger: 'change' }
  ],
  description: [
    { max: 200, message: '家庭描述长度不能超过200个字符', trigger: 'blur' }
  ]
}

// 创建时额外的验证规则
export const createAdditionalRules = {
  clubId: [
    { required: true, message: '请选择俱乐部', trigger: 'change' }
  ]
}

// 编辑时额外的验证规则
export const editAdditionalRules = {
  familyGroupId: [
    { required: true, message: '家庭组ID不能为空', trigger: 'change' }
  ]
}

// 获取创建表单的完整验证规则
export function getCreateValidationRules() {
  return {
    ...commonValidationRules,
    ...createAdditionalRules
  }
}

// 获取编辑表单的完整验证规则
export function getEditValidationRules() {
  return {
    ...commonValidationRules,
    ...editAdditionalRules
  }
}

// 会员信息格式化方法
export function formatMemberInfo(member) {
  if (!member) return '暂无信息'
  
  const info = []
  if (member.phone) info.push(`手机:${member.phone}`)
  if (member.memberNo) info.push(`编号:${member.memberNo}`)
  if (member.riderCertNo) info.push(`骑手证:${member.riderCertNo}`)
  
  return info.length > 0 ? info.join(' | ') : '暂无信息'
}

// 表单数据格式化方法
export function formatCreateFormData(formData) {
  return {
    familyName: formData.familyName,
    clubId: formData.clubId,
    guardianMemberId: formData.mainContactId, // 字段名转换
    description: formData.description
  }
}

export function formatEditFormData(formData) {
  return {
    familyGroupId: formData.familyGroupId,
    familyName: formData.familyName,
    mainContactId: formData.mainContactId,
    description: formData.description
  }
}