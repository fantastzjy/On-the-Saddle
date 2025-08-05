package net.lab1024.sa.admin.module.system.employee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.module.support.file.constant.FileFolderTypeEnum;
import net.lab1024.sa.base.module.support.file.dao.FileDao;
import net.lab1024.sa.base.module.support.file.domain.entity.FileEntity;
import net.lab1024.sa.base.module.support.file.domain.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工简历 Service
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class EmployeeResumeService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 获取员工简历文件列表
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<List<FileVO>> getResumeList(Long employeeId) {
        // 使用LambdaQueryWrapper查询该员工的简历文件
        LambdaQueryWrapper<FileEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileEntity::getFolderType, FileFolderTypeEnum.EMPLOYEE_RESUME.getValue())
                   .eq(FileEntity::getCreatorId, employeeId)
                   .orderByDesc(FileEntity::getCreateTime);
        
        List<FileEntity> fileEntityList = fileDao.selectList(queryWrapper);
        
        // 转换为VO
        List<FileVO> fileList = fileEntityList.stream().map(entity -> {
            FileVO vo = new FileVO();
            vo.setFileId(entity.getFileId());
            vo.setFileName(entity.getFileName());
            vo.setFileSize(entity.getFileSize() != null ? entity.getFileSize().intValue() : null);
            vo.setFileKey(entity.getFileKey());
            vo.setFileType(entity.getFileType());
            vo.setCreateTime(entity.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        
        return ResponseDTO.ok(fileList);
    }

    /**
     * 更新员工最新简历信息
     *
     * @param employeeId
     * @param fileKey
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateLatestResume(Long employeeId, String fileKey) {
        // 检查员工是否存在
        EmployeeEntity employee = employeeDao.selectById(employeeId);
        if (employee == null || employee.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("员工不存在");
        }

        // 更新员工最新简历信息
        EmployeeEntity updateEntity = new EmployeeEntity();
        updateEntity.setEmployeeId(employeeId);
        updateEntity.setResumeFileKey(fileKey);
        updateEntity.setResumeUpdateTime(LocalDateTime.now());
        updateEntity.setUpdateTime(LocalDateTime.now());

        employeeDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 删除简历文件时清空员工表中的简历信息（如果匹配）
     *
     * @param employeeId
     * @param fileKey
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> clearResumeIfMatch(Long employeeId, String fileKey) {
        // 检查员工是否存在
        EmployeeEntity employee = employeeDao.selectById(employeeId);
        if (employee == null || employee.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("员工不存在");
        }

        // 如果删除的文件是当前最新简历，则清空简历信息
        if (fileKey != null && fileKey.equals(employee.getResumeFileKey())) {
            EmployeeEntity updateEntity = new EmployeeEntity();
            updateEntity.setEmployeeId(employeeId);
            updateEntity.setResumeFileKey(null);
            updateEntity.setResumeUpdateTime(null);
            updateEntity.setUpdateTime(LocalDateTime.now());

            employeeDao.updateById(updateEntity);
        }

        return ResponseDTO.ok();
    }

    /**
     * 根据员工ID删除所有简历文件（用于删除员工时）
     *
     * @param employeeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteResumeByEmployeeId(Long employeeId) {
        // 查询该员工的所有简历文件
        LambdaQueryWrapper<FileEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileEntity::getFolderType, FileFolderTypeEnum.EMPLOYEE_RESUME.getValue())
                   .eq(FileEntity::getCreatorId, employeeId);
        
        List<FileEntity> fileEntityList = fileDao.selectList(queryWrapper);
        
        // 删除文件记录
        for (FileEntity fileEntity : fileEntityList) {
            fileDao.deleteById(fileEntity.getFileId());
        }
    }

    /**
     * 删除简历文件
     *
     * @param fileKey
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteResumeFile(String fileKey) {
        // 根据fileKey查找文件记录
        LambdaQueryWrapper<FileEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileEntity::getFileKey, fileKey);
        
        FileEntity fileEntity = fileDao.selectOne(queryWrapper);
        if (fileEntity != null) {
            fileDao.deleteById(fileEntity.getFileId());
        }
    }
}