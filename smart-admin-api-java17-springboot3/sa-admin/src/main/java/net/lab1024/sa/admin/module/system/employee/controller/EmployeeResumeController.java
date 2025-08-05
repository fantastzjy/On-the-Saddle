package net.lab1024.sa.admin.module.system.employee.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.system.employee.service.EmployeeResumeService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.file.constant.FileFolderTypeEnum;
import net.lab1024.sa.base.module.support.file.domain.vo.FileDownloadVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileVO;
import net.lab1024.sa.base.module.support.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 员工简历管理
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_EMPLOYEE)
public class EmployeeResumeController {

    @Autowired
    private EmployeeResumeService employeeResumeService;

    @Autowired
    private FileService fileService;

    @GetMapping("/employee/{employeeId}/resume")
    @Operation(summary = "获取员工简历文件列表")
    @SaCheckPermission("system:employee:resume:query")
    public ResponseDTO<List<FileVO>> getResumeList(@PathVariable Long employeeId) {
        return employeeResumeService.getResumeList(employeeId);
    }

    @PostMapping("/employee/{employeeId}/resume/upload")
    @Operation(summary = "上传员工简历")
    @SaCheckPermission("system:employee:resume:upload")
    public ResponseDTO<FileUploadVO> uploadResume(
            @PathVariable Long employeeId,
            @RequestParam("file") MultipartFile file) {
        
        // 获取当前用户信息
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        
        // 调用文件上传服务
        ResponseDTO<FileUploadVO> uploadResult = fileService.fileUpload(file, 
            FileFolderTypeEnum.EMPLOYEE_RESUME.getValue(), requestUser);
        
        if (uploadResult.getOk()) {
            // 更新员工表中的简历信息
            employeeResumeService.updateLatestResume(employeeId, uploadResult.getData().getFileKey());
        }
        
        return uploadResult;
    }

    @PostMapping("/employee/{employeeId}/resume/{fileKey}/set-latest")
    @Operation(summary = "设置为最新简历")
    @SaCheckPermission("system:employee:resume:edit")
    public ResponseDTO<String> setLatestResume(
            @PathVariable Long employeeId,
            @PathVariable String fileKey) {
        return employeeResumeService.updateLatestResume(employeeId, fileKey);
    }

    @GetMapping("/employee/{employeeId}/resume/{fileKey}/delete")
    @Operation(summary = "删除员工简历")
    @SaCheckPermission("system:employee:resume:delete")
    public ResponseDTO<String> deleteResume(
            @PathVariable Long employeeId,
            @PathVariable String fileKey) {
        
        // 直接通过DAO删除文件记录（物理删除需要另外处理）
        try {
            // 如果删除的是当前最新简历，清空员工表中的简历信息
            employeeResumeService.clearResumeIfMatch(employeeId, fileKey);
            
            // 删除文件记录
            employeeResumeService.deleteResumeFile(fileKey);
            
            return ResponseDTO.ok("删除成功");
        } catch (Exception e) {
            return ResponseDTO.userErrorParam("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/employee/resume/{fileKey}/download")
    @Operation(summary = "下载员工简历")
    @SaCheckPermission("system:employee:resume:download")
    public void downloadResume(@PathVariable String fileKey, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 调用文件下载服务
        ResponseDTO<FileDownloadVO> downloadResult = fileService.getDownloadFile(fileKey, request.getHeader("User-Agent"));
        if (downloadResult.getOk()) {
            FileDownloadVO fileDownloadVO = downloadResult.getData();            
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileDownloadVO.getMetadata().getFileName());
            response.getOutputStream().write(fileDownloadVO.getData());
        }
    }
}