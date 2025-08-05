package net.lab1024.sa.admin.module.system.employee.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeWorkExperienceDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeWorkExperienceEntity;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeWorkExperienceAddForm;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeWorkExperienceUpdateForm;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeWorkExperienceVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工工作经历 Service
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class EmployeeWorkExperienceService {

    @Autowired
    private EmployeeWorkExperienceDao employeeWorkExperienceDao;

    /**
     * 根据员工ID查询工作经历列表
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<List<EmployeeWorkExperienceVO>> queryByEmployeeId(Long employeeId) {
        List<EmployeeWorkExperienceVO> workExperienceList = employeeWorkExperienceDao.selectByEmployeeId(employeeId);
        return ResponseDTO.ok(workExperienceList);
    }

    /**
     * 新增工作经历
     *
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(EmployeeWorkExperienceAddForm addForm) {
        // 转换实体
        EmployeeWorkExperienceEntity entity = SmartBeanUtil.copy(addForm, EmployeeWorkExperienceEntity.class);
        entity.setDeletedFlag(false);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        
        // 如果没有设置排序，则自动设置
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }

        employeeWorkExperienceDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * 更新工作经历
     *
     * @param updateForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(EmployeeWorkExperienceUpdateForm updateForm) {
        // 检查记录是否存在
        EmployeeWorkExperienceEntity existEntity = employeeWorkExperienceDao.selectById(updateForm.getWorkExperienceId());
        if (existEntity == null || existEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("工作经历不存在");
        }

        // 转换实体
        EmployeeWorkExperienceEntity entity = SmartBeanUtil.copy(updateForm, EmployeeWorkExperienceEntity.class);
        entity.setUpdateTime(LocalDateTime.now());

        employeeWorkExperienceDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 删除工作经历
     *
     * @param workExperienceId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long workExperienceId) {
        // 检查记录是否存在
        EmployeeWorkExperienceEntity existEntity = employeeWorkExperienceDao.selectById(workExperienceId);
        if (existEntity == null || existEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("工作经历不存在");
        }

        // 逻辑删除
        EmployeeWorkExperienceEntity entity = new EmployeeWorkExperienceEntity();
        entity.setWorkExperienceId(workExperienceId);
        entity.setDeletedFlag(true);
        entity.setUpdateTime(LocalDateTime.now());

        employeeWorkExperienceDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 根据员工ID删除所有工作经历（用于删除员工时）
     *
     * @param employeeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByEmployeeId(Long employeeId) {
        employeeWorkExperienceDao.deleteByEmployeeId(employeeId);
    }
}