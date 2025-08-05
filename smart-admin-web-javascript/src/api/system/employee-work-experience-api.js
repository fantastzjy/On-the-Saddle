import { getRequest, postRequest } from '/@/lib/axios';

export const employeeWorkExperienceApi = {

  /**
   * 获取员工工作经历列表
   * @param {number} employeeId - 员工ID
   * @returns {Promise}
   */
  getWorkExperienceList: (employeeId) => {
    return getRequest(`/employee/${employeeId}/work-experience`);
  },

  /**
   * 新增员工工作经历
   * @param {Object} data - 工作经历信息
   * @returns {Promise}
   */
  addWorkExperience: (data) => {
    return postRequest('/employee/work-experience', data);
  },

  /**
   * 更新员工工作经历
   * @param {Object} data - 工作经历信息
   * @returns {Promise}
   */
  updateWorkExperience: (data) => {
    return postRequest('/employee/work-experience/update', data);
  },

  /**
   * 删除员工工作经历
   * @param {number} workExperienceId - 工作经历ID
   * @returns {Promise}
   */
  deleteWorkExperience: (workExperienceId) => {
    return getRequest(`/employee/work-experience/delete/${workExperienceId}`);
  }

};