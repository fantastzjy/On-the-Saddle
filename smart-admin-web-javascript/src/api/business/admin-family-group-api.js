import { postRequest, getRequest } from '/@/lib/axios'

export const adminFamilyGroupApi = {
  // 创建家庭组
  create: (param) => postRequest('/admin/family-group/create', param),
  
  // 分页查询家庭组列表
  pageQuery: (param) => postRequest('/admin/family-group/page/query', param),
  
  // 获取家庭组详情
  getDetail: (familyGroupId) => getRequest(`/admin/family-group/${familyGroupId}`),
  
  // 更新家庭组信息
  update: (param) => postRequest('/admin/family-group/update', param),
  
  // 批量删除家庭组
  batchDelete: (familyGroupIds) => postRequest('/admin/family-group/batch/delete', familyGroupIds),
  
  // 批量恢复家庭组
  batchRestore: (familyGroupIds) => postRequest('/admin/family-group/batch/restore', familyGroupIds),

  // 根据会员ID查询家庭信息
  getMemberFamily: (memberId) => getRequest(`/admin/family-group/member/${memberId}`),

  // 更新家庭成员关系备注
  updateMemberRemark: (familyGroupId, memberId, remark) => postRequest('/admin/family-group/update-member-remark', {
    familyGroupId,
    memberId,
    remark
  })
}