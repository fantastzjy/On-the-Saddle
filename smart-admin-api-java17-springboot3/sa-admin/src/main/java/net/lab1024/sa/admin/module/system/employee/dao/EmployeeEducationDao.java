package net.lab1024.sa.admin.module.system.employee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEducationEntity;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeEducationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工学历信息 Dao
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface EmployeeEducationDao extends BaseMapper<EmployeeEducationEntity> {

    /**
     * 根据员工ID查询学历信息列表
     *
     * @param employeeId
     * @return
     */
    List<EmployeeEducationVO> selectByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据员工ID删除学历信息（逻辑删除）
     *
     * @param employeeId
     */
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);
}