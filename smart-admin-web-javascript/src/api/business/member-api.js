import { postRequest, getRequest } from '/@/lib/axios'

export const memberApi = {
  // 会员管理
  pageQuery: (param) => postRequest('/club/member/page/query', param),
  detail: (memberId) => getRequest(`/club/member/get/${memberId}`),
  create: (param) => postRequest('/club/member/create', param),
  update: (param) => postRequest('/club/member/update', param),
  updateStatus: (memberId, status) => postRequest('/club/member/update-status', {memberId, status}),
  delete: (memberId) => getRequest(`/club/member/delete/${memberId}`),
  
  // 家庭管理
  getFamilyInfo: (memberId) => getRequest(`/club/member/family/${memberId}`),
  searchFamilyGroups: (param) => postRequest('/club/family/search', param),
  createFamily: (param) => postRequest('/club/family/create', param),
  updateFamily: (param) => postRequest('/club/family/update', param),
  addFamilyMember: (param) => postRequest('/club/family/addMember', param),
  joinFamilyGroup: (param) => postRequest('/club/family/joinMember', param),
  removeFamilyMember: (familyGroupId, memberId) => postRequest('/club/family/remove-member', {familyGroupId, memberId}),
  setGuardian: (familyGroupId, memberId, isGuardian) => postRequest('/club/family/set-guardian', {familyGroupId, memberId, isGuardian}),
  
  // 会籍管理
  renewMembership: (param) => postRequest('/club/member/membership/renew', param),
  getMembershipHistory: (memberId) => getRequest(`/club/member/membership/history/${memberId}`),
  
  // 辅助接口
  checkPhoneExist: (phone, excludeId) => getRequest(`/club/member/check-phone/${phone}${excludeId ? '?excludeId=' + excludeId : ''}`),
  generateMemberNo: () => getRequest('/club/member/generate-no'),
  queryList: (clubId) => getRequest(`/club/member/query/list?clubId=${clubId}`),
  
  // 批量操作
  batchUpdateStatus: (memberIds, status) => postRequest('/club/member/batch-update-status', {memberIds, status}),
  exportMembers: (param) => postRequest('/club/member/export', param, {responseType: 'blob'})
}