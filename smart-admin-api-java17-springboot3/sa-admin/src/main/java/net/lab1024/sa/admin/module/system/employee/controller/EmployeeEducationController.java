package net.lab1024.sa.admin.module.system.employee.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeEducationAddForm;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeEducationUpdateForm;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeEducationVO;
import net.lab1024.sa.admin.module.system.employee.service.EmployeeEducationService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工学历管理
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_EMPLOYEE)
public class EmployeeEducationController {

    @Autowired
    private EmployeeEducationService employeeEducationService;

    @GetMapping("/employee/{employeeId}/education")
    @Operation(summary = "获取员工学历信息列表")
    @SaCheckPermission("system:employee:query")
    public ResponseDTO<List<EmployeeEducationVO>> queryEducation(@PathVariable Long employeeId) {
        return employeeEducationService.queryByEmployeeId(employeeId);
    }

    @PostMapping("/employee/education")
    @Operation(summary = "新增员工学历信息")
    @SaCheckPermission("system:employee:education:add")
    public ResponseDTO<String> addEducation(@Valid @RequestBody EmployeeEducationAddForm addForm) {
        return employeeEducationService.add(addForm);
    }

    @PostMapping("/employee/education/update")
    @Operation(summary = "更新员工学历信息")
    @SaCheckPermission("system:employee:education:edit")
    public ResponseDTO<String> updateEducation(@Valid @RequestBody EmployeeEducationUpdateForm updateForm) {
        return employeeEducationService.update(updateForm);
    }

    @GetMapping("/employee/education/delete/{educationId}")
    @Operation(summary = "删除员工学历信息")
    @SaCheckPermission("system:employee:education:delete")
    public ResponseDTO<String> deleteEducation(@PathVariable Long educationId) {
        return employeeEducationService.delete(educationId);
    }
}