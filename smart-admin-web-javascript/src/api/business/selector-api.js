/**
 * 选择器API
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/axios'

export const selectorApi = {
  /**
   * 马匹选择器查询
   */
  queryHorseSelector: (params) => {
    return getRequest('/selector/horse', params)
  },

  /**
   * 会员选择器查询
   */
  queryMemberSelector: (params) => {
    return getRequest('/selector/member', params)
  },

  /**
   * 教练选择器查询
   */
  queryCoachSelector: (params) => {
    return getRequest('/selector/coach', params)
  },

  /**
   * 员工选择器查询
   */
  queryEmployeeSelector: (params) => {
    return getRequest('/selector/employee', params)
  },

  /**
   * 俱乐部选择器查询
   */
  queryClubSelector: (params) => {
    return getRequest('/selector/club', params)
  },

  /**
   * 课程选择器查询
   */
  queryCourseSelector: (params) => {
    return getRequest('/selector/course', params)
  }
}