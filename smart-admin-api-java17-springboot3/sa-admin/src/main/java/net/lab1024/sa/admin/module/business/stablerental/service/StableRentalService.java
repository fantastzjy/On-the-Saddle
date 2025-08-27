package net.lab1024.sa.admin.module.business.stablerental.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.stablerental.constant.RentalStatusEnum;
import net.lab1024.sa.admin.module.business.stablerental.dao.StableRentalDao;
import net.lab1024.sa.admin.module.business.stablerental.domain.entity.StableRentalEntity;
import net.lab1024.sa.admin.module.business.stablerental.domain.form.StableRentalCreateForm;
import net.lab1024.sa.admin.module.business.stablerental.domain.form.StableRentalQueryForm;
import net.lab1024.sa.admin.module.business.stablerental.domain.form.StableRentalUpdateForm;
import net.lab1024.sa.admin.module.business.stablerental.domain.vo.StableRentalDetailVO;
import net.lab1024.sa.admin.module.business.stablerental.domain.vo.StableRentalListVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.sa.base.module.support.serialnumber.service.SerialNumberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 马房租赁Service
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class StableRentalService {

    @Autowired
    private StableRentalDao stableRentalDao;

    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 分页查询马房租赁列表
     */
    public PageResult<StableRentalListVO> queryPage(StableRentalQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<StableRentalListVO> result = stableRentalDao.queryPage(page, queryForm);
        
        // 设置租赁状态描述
        result.getRecords().forEach(item -> {
            RentalStatusEnum statusEnum = RentalStatusEnum.getByValue(item.getRentalStatus());
            if (statusEnum != null) {
                item.setRentalStatusDesc(statusEnum.getDesc());
            }
        });
        
        return SmartPageUtil.convert2PageResult(page, result.getRecords());
    }

    /**
     * 根据ID查询马房租赁详情
     */
    public StableRentalDetailVO getDetailById(Long rentalId) {
        StableRentalDetailVO detail = stableRentalDao.getDetailById(rentalId);
        if (detail != null) {
            RentalStatusEnum statusEnum = RentalStatusEnum.getByValue(detail.getRentalStatus());
            if (statusEnum != null) {
                detail.setRentalStatusDesc(statusEnum.getDesc());
            }
        }
        return detail;
    }

    /**
     * 创建马房租赁记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> create(StableRentalCreateForm createForm) {
        // 验证时间范围
        if (createForm.getRentalStartTime().isAfter(createForm.getRentalEndTime())) {
            return ResponseDTO.userErrorParam("租赁开始时间不能晚于结束时间");
        }

        StableRentalEntity entity = SmartBeanUtil.copy(createForm, StableRentalEntity.class);
        
        // 生成租赁单号
        String rentalNo = serialNumberService.generate(SerialNumberIdEnum.STABLE_RENTAL);
        entity.setRentalNo(rentalNo);
        entity.setRentalStatus(RentalStatusEnum.ACTIVE.getValue());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsValid(1);
        entity.setIsDelete(0);
        
        stableRentalDao.insert(entity);
        
        return ResponseDTO.ok();
    }

    /**
     * 更新马房租赁记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(StableRentalUpdateForm updateForm) {
        // 验证记录是否存在
        StableRentalEntity existEntity = stableRentalDao.selectById(updateForm.getRentalId());
        if (existEntity == null || existEntity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("租赁记录不存在");
        }

        // 验证时间范围
        if (updateForm.getRentalStartTime().isAfter(updateForm.getRentalEndTime())) {
            return ResponseDTO.userErrorParam("租赁开始时间不能晚于结束时间");
        }

        StableRentalEntity entity = SmartBeanUtil.copy(updateForm, StableRentalEntity.class);
        entity.setUpdateTime(LocalDateTime.now());
        
        stableRentalDao.updateById(entity);
        
        return ResponseDTO.ok();
    }

    /**
     * 删除马房租赁记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long rentalId) {
        StableRentalEntity entity = stableRentalDao.selectById(rentalId);
        if (entity == null || entity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("租赁记录不存在");
        }
        
        entity.setIsDelete(1);
        entity.setUpdateTime(LocalDateTime.now());
        stableRentalDao.updateById(entity);
        
        return ResponseDTO.ok();
    }

    /**
     * 更新租赁状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(Long rentalId, Integer status) {
        StableRentalEntity entity = stableRentalDao.selectById(rentalId);
        if (entity == null || entity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("租赁记录不存在");
        }
        
        RentalStatusEnum statusEnum = RentalStatusEnum.getByValue(status);
        if (statusEnum == null) {
            return ResponseDTO.userErrorParam("租赁状态无效");
        }
        
        entity.setRentalStatus(status);
        entity.setUpdateTime(LocalDateTime.now());
        stableRentalDao.updateById(entity);
        
        return ResponseDTO.ok();
    }
}