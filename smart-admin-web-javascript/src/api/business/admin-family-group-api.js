import { postRequest, getRequest } from '/@/lib/axios'

export const adminFamilyGroupApi = {
  // 分页查询家庭组列表
  pageQuery: (param) => postRequest('/admin/family-group/page/query', param),
  
  // 获取家庭组详情
  getDetail: (familyGroupId) => getRequest(`/admin/family-group/${familyGroupId}`),
  
  // 更新家庭组信息
  update: (param) => postRequest('/admin/family-group/update', param),
  
  // 批量删除家庭组
  batchDelete: (familyGroupIds) => postRequest('/admin/family-group/batch/delete', familyGroupIds),
  
  // 批量恢复家庭组
  batchRestore: (familyGroupIds) => postRequest('/admin/family-group/batch/restore', familyGroupIds)
}