import { getRequest, postRequest } from '/@/lib/axios';

export const employeeEducationApi = {

  /**
   * 获取员工学历信息列表
   * @param {number} employeeId - 员工ID
   * @returns {Promise}
   */
  getEducationList: (employeeId) => {
    return getRequest(`/employee/${employeeId}/education`);
  },

  /**
   * 新增员工学历信息
   * @param {Object} data - 学历信息
   * @returns {Promise}
   */
  addEducation: (data) => {
    return postRequest('/employee/education', data);
  },

  /**
   * 更新员工学历信息
   * @param {Object} data - 学历信息
   * @returns {Promise}
   */
  updateEducation: (data) => {
    return postRequest('/employee/education/update', data);
  },

  /**
   * 删除员工学历信息
   * @param {number} educationId - 学历ID
   * @returns {Promise}
   */
  deleteEducation: (educationId) => {
    return getRequest(`/employee/education/delete/${educationId}`);
  }

};