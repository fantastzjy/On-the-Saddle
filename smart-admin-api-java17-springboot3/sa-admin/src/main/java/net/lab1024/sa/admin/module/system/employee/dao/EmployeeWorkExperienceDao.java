package net.lab1024.sa.admin.module.system.employee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeWorkExperienceEntity;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeWorkExperienceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工工作经历 Dao
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface EmployeeWorkExperienceDao extends BaseMapper<EmployeeWorkExperienceEntity> {

    /**
     * 根据员工ID查询工作经历列表
     *
     * @param employeeId
     * @return
     */
    List<EmployeeWorkExperienceVO> selectByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据员工ID删除工作经历（逻辑删除）
     *
     * @param employeeId
     */
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);
}