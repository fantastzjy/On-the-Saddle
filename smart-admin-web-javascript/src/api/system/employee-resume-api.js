import { getRequest, postRequest } from '/@/lib/axios';

export const employeeResumeApi = {

  /**
   * 获取员工简历文件列表
   * @param {number} employeeId - 员工ID
   * @returns {Promise}
   */
  getResumeList: (employeeId) => {
    return getRequest(`/employee/${employeeId}/resume`);
  },

  /**
   * 上传员工简历
   * @param {number} employeeId - 员工ID
   * @param {File} file - 简历文件
   * @returns {Promise}
   */
  uploadResume: (employeeId, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return postRequest(`/employee/${employeeId}/resume/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },

  /**
   * 设置为最新简历
   * @param {number} employeeId - 员工ID
   * @param {string} fileKey - 文件Key
   * @returns {Promise}
   */
  setLatestResume: (employeeId, fileKey) => {
    return postRequest(`/employee/${employeeId}/resume/${fileKey}/set-latest`);
  },

  /**
   * 删除员工简历
   * @param {number} employeeId - 员工ID
   * @param {string} fileKey - 文件Key
   * @returns {Promise}
   */
  deleteResume: (employeeId, fileKey) => {
    return getRequest(`/employee/${employeeId}/resume/${fileKey}/delete`);
  },

  /**
   * 下载简历文件
   * @param {string} fileKey - 文件Key
   * @returns {string} - 下载URL
   */
  getDownloadUrl: (fileKey) => {
    return `/employee/resume/${fileKey}/download`;
  }

};