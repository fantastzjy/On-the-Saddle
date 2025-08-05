package net.lab1024.sa.admin.module.system.employee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeEducationDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEducationEntity;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeEducationAddForm;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeEducationUpdateForm;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeEducationVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工学历信息 Service
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class EmployeeEducationService {

    @Autowired
    private EmployeeEducationDao employeeEducationDao;

    /**
     * 根据员工ID查询学历信息列表
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<List<EmployeeEducationVO>> queryByEmployeeId(Long employeeId) {
        List<EmployeeEducationVO> educationList = employeeEducationDao.selectByEmployeeId(employeeId);
        return ResponseDTO.ok(educationList);
    }

    /**
     * 新增学历信息
     *
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(EmployeeEducationAddForm addForm) {
        // 转换实体
        EmployeeEducationEntity entity = SmartBeanUtil.copy(addForm, EmployeeEducationEntity.class);
        entity.setDeletedFlag(false);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        
        // 如果没有设置排序，则自动设置
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        
        // 如果没有设置是否毕业，默认为已毕业
        if (entity.getIsGraduated() == null) {
            entity.setIsGraduated(true);
        }

        employeeEducationDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * 更新学历信息
     *
     * @param updateForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(EmployeeEducationUpdateForm updateForm) {
        // 检查记录是否存在
        EmployeeEducationEntity existEntity = employeeEducationDao.selectById(updateForm.getEducationId());
        if (existEntity == null || existEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("学历信息不存在");
        }

        // 转换实体
        EmployeeEducationEntity entity = SmartBeanUtil.copy(updateForm, EmployeeEducationEntity.class);
        entity.setUpdateTime(LocalDateTime.now());

        employeeEducationDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 删除学历信息
     *
     * @param educationId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long educationId) {
        // 检查记录是否存在
        EmployeeEducationEntity existEntity = employeeEducationDao.selectById(educationId);
        if (existEntity == null || existEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("学历信息不存在");
        }

        // 逻辑删除
        EmployeeEducationEntity entity = new EmployeeEducationEntity();
        entity.setEducationId(educationId);
        entity.setDeletedFlag(true);
        entity.setUpdateTime(LocalDateTime.now());

        employeeEducationDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 根据员工ID删除所有学历信息（用于删除员工时）
     *
     * @param employeeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByEmployeeId(Long employeeId) {
        employeeEducationDao.deleteByEmployeeId(employeeId);
    }
}