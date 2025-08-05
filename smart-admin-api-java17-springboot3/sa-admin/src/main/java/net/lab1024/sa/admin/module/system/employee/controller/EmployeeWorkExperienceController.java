package net.lab1024.sa.admin.module.system.employee.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeWorkExperienceAddForm;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeWorkExperienceUpdateForm;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeWorkExperienceVO;
import net.lab1024.sa.admin.module.system.employee.service.EmployeeWorkExperienceService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工工作经历管理
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_EMPLOYEE)
public class EmployeeWorkExperienceController {

    @Autowired
    private EmployeeWorkExperienceService employeeWorkExperienceService;

    @GetMapping("/employee/{employeeId}/work-experience")
    @Operation(summary = "获取员工工作经历列表")
    @SaCheckPermission("system:employee:query")
    public ResponseDTO<List<EmployeeWorkExperienceVO>> queryWorkExperience(@PathVariable Long employeeId) {
        return employeeWorkExperienceService.queryByEmployeeId(employeeId);
    }

    @PostMapping("/employee/work-experience")
    @Operation(summary = "新增员工工作经历")
    @SaCheckPermission("system:employee:workExperience:add")
    public ResponseDTO<String> addWorkExperience(@Valid @RequestBody EmployeeWorkExperienceAddForm addForm) {
        return employeeWorkExperienceService.add(addForm);
    }

    @PostMapping("/employee/work-experience/update")
    @Operation(summary = "更新员工工作经历")
    @SaCheckPermission("system:employee:workExperience:edit")
    public ResponseDTO<String> updateWorkExperience(@Valid @RequestBody EmployeeWorkExperienceUpdateForm updateForm) {
        return employeeWorkExperienceService.update(updateForm);
    }

    @GetMapping("/employee/work-experience/delete/{workExperienceId}")
    @Operation(summary = "删除员工工作经历")
    @SaCheckPermission("system:employee:workExperience:delete")
    public ResponseDTO<String> deleteWorkExperience(@PathVariable Long workExperienceId) {
        return employeeWorkExperienceService.delete(workExperienceId);
    }
}