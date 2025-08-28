package net.lab1024.sa.admin.module.business.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.activity.dao.ActivityDao;
import net.lab1024.sa.admin.module.business.activity.domain.form.ActivityAddForm;
import net.lab1024.sa.admin.module.business.activity.domain.form.ActivityQueryForm;
import net.lab1024.sa.admin.module.business.activity.domain.form.ActivityUpdateForm;
import net.lab1024.sa.admin.module.business.activity.domain.vo.ActivityDetailVO;
import net.lab1024.sa.admin.module.business.activity.domain.vo.ActivityListVO;
import net.lab1024.sa.admin.module.business.product.dao.ProductActivityDao;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductActivityEntity;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 活动管理服务
 *
 * @Author 1024创新实验室
 * @Date 2025-08-28
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class ActivityService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductActivityDao productActivityDao;

    @Autowired
    private ActivityDao activityDao;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 活动列表查询
     */
    public ResponseDTO<PageResult<ActivityListVO>> queryActivityList(ActivityQueryForm queryForm) {
        try {
            Page<ActivityListVO> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
            
            // 使用自定义DAO查询活动列表（联合查询两张表）
            IPage<ActivityListVO> pageResult = activityDao.queryActivityList(page, queryForm);
            
            PageResult<ActivityListVO> result = SmartPageUtil.convert2PageResult(page, pageResult.getRecords());
            result.setTotal(pageResult.getTotal()); // 设置总数

            log.info("查询活动列表成功，共{}条记录", result.getTotal());
            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("查询活动列表失败", e);
            return ResponseDTO.userErrorParam("查询活动列表失败");
        }
    }

    /**
     * 活动详情查询
     */
    public ResponseDTO<ActivityDetailVO> queryActivityDetail(Long productId) {
        try {
            if (productId == null) {
                return ResponseDTO.userErrorParam("活动ID不能为空");
            }

            // 使用自定义DAO查询活动详情（联合查询两张表）
            ActivityDetailVO activityDetail = activityDao.queryActivityDetail(productId);
            
            if (activityDetail == null) {
                return ResponseDTO.userErrorParam("活动不存在");
            }

            log.info("查询活动详情成功，活动ID: {}", productId);
            return ResponseDTO.ok(activityDetail);

        } catch (Exception e) {
            log.error("查询活动详情失败，活动ID: {}", productId, e);
            return ResponseDTO.userErrorParam("查询活动详情失败");
        }
    }

    /**
     * 活动新增
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> addActivity(ActivityAddForm addForm, Long userId) {
        try {
            // 1. 插入主表 m_product (自动处理商品名称和编码)
            ProductEntity productEntity = new ProductEntity();
            productEntity.setClubId(addForm.getClubId());
            productEntity.setProductName(addForm.getActivityName()); // 商品名称 = 活动名称
            productEntity.setProductCode(generateProductCode(null)); // 自动生成编码
            productEntity.setProductType(3); // 活动类型
            productEntity.setSubType("");
            productEntity.setIsValid(true);
            productEntity.setIsDelete(false);
            productEntity.setCreateBy(String.valueOf(userId));
            productEntity.setCreateTime(LocalDateTime.now());
            productEntity.setUpdateBy(String.valueOf(userId));
            productEntity.setUpdateTime(LocalDateTime.now());

            int productInsertResult = productDao.insert(productEntity);
            if (productInsertResult <= 0) {
                return ResponseDTO.userErrorParam("新增活动失败");
            }

            // 2. 插入扩展表 m_product_activity
            ProductActivityEntity activityEntity = new ProductActivityEntity();
            activityEntity.setProductId(productEntity.getProductId());
            activityEntity.setActivityName(addForm.getActivityName());
            activityEntity.setActivityDetails(addForm.getActivityDetails());
            activityEntity.setStartTime(parseDateTime(addForm.getStartTime()));
            activityEntity.setEndTime(parseDateTime(addForm.getEndTime()));
            activityEntity.setActivityLocation(addForm.getActivityLocation());
            activityEntity.setPrice(addForm.getPrice());
            activityEntity.setMaxParticipants(addForm.getMaxParticipants());
            activityEntity.setActivityRule(addForm.getActivityRule());
            activityEntity.setDetailImages(addForm.getDetailImages());
            activityEntity.setCreateBy(String.valueOf(userId));
            activityEntity.setCreateTime(LocalDateTime.now());
            activityEntity.setUpdateBy(String.valueOf(userId));
            activityEntity.setUpdateTime(LocalDateTime.now());

            int activityInsertResult = productActivityDao.insert(activityEntity);
            if (activityInsertResult <= 0) {
                return ResponseDTO.userErrorParam("新增活动扩展信息失败");
            }

            log.info("新增活动成功，活动ID: {}, 活动名称: {}", productEntity.getProductId(), addForm.getActivityName());
            return ResponseDTO.ok("新增活动成功");

        } catch (Exception e) {
            log.error("新增活动失败", e);
            return ResponseDTO.userErrorParam("新增活动失败");
        }
    }

    /**
     * 活动更新
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateActivity(ActivityUpdateForm updateForm, Long userId) {
        try {
            if (updateForm.getProductId() == null) {
                return ResponseDTO.userErrorParam("活动ID不能为空");
            }

            // 1. 更新主表 m_product (自动同步商品名称)
            ProductEntity productEntity = productDao.selectById(updateForm.getProductId());
            if (productEntity == null) {
                return ResponseDTO.userErrorParam("活动不存在");
            }

            productEntity.setProductName(updateForm.getActivityName()); // 商品名称 = 活动名称
            productEntity.setUpdateBy(String.valueOf(userId));
            productEntity.setUpdateTime(LocalDateTime.now());

            int productUpdateResult = productDao.updateById(productEntity);
            if (productUpdateResult <= 0) {
                return ResponseDTO.userErrorParam("更新活动失败");
            }

            // 2. 更新扩展表 m_product_activity
            LambdaQueryWrapper<ProductActivityEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProductActivityEntity::getProductId, updateForm.getProductId());
            ProductActivityEntity activityEntity = productActivityDao.selectOne(queryWrapper);

            if (activityEntity == null) {
                return ResponseDTO.userErrorParam("活动扩展信息不存在");
            }

            activityEntity.setActivityName(updateForm.getActivityName());
            activityEntity.setActivityDetails(updateForm.getActivityDetails());
            activityEntity.setStartTime(parseDateTime(updateForm.getStartTime()));
            activityEntity.setEndTime(parseDateTime(updateForm.getEndTime()));
            activityEntity.setActivityLocation(updateForm.getActivityLocation());
            activityEntity.setPrice(updateForm.getPrice());
            activityEntity.setMaxParticipants(updateForm.getMaxParticipants());
            activityEntity.setActivityRule(updateForm.getActivityRule());
            activityEntity.setDetailImages(updateForm.getDetailImages());
            activityEntity.setUpdateBy(String.valueOf(userId));
            activityEntity.setUpdateTime(LocalDateTime.now());

            int activityUpdateResult = productActivityDao.updateById(activityEntity);
            if (activityUpdateResult <= 0) {
                return ResponseDTO.userErrorParam("更新活动扩展信息失败");
            }

            log.info("更新活动成功，活动ID: {}, 活动名称: {}", updateForm.getProductId(), updateForm.getActivityName());
            return ResponseDTO.ok("更新活动成功");

        } catch (Exception e) {
            log.error("更新活动失败", e);
            return ResponseDTO.userErrorParam("更新活动失败");
        }
    }

    /**
     * 活动删除（软删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteActivity(Long productId, Long userId) {
        try {
            if (productId == null) {
                return ResponseDTO.userErrorParam("活动ID不能为空");
            }

            ProductEntity productEntity = productDao.selectById(productId);
            if (productEntity == null) {
                return ResponseDTO.userErrorParam("活动不存在");
            }

            // 软删除主表记录
            productEntity.setIsDelete(true);
            productEntity.setUpdateBy(String.valueOf(userId));
            productEntity.setUpdateTime(LocalDateTime.now());

            int deleteResult = productDao.updateById(productEntity);
            if (deleteResult <= 0) {
                return ResponseDTO.userErrorParam("删除活动失败");
            }

            log.info("删除活动成功，活动ID: {}", productId);
            return ResponseDTO.ok("删除活动成功");

        } catch (Exception e) {
            log.error("删除活动失败，活动ID: {}", productId, e);
            return ResponseDTO.userErrorParam("删除活动失败");
        }
    }

    // ========================================
    // 私有辅助方法
    // ========================================

    /**
     * 生成商品编码
     */
    private String generateProductCode(String inputCode) {
        if (SmartStringUtil.isNotBlank(inputCode)) {
            return inputCode;
        }
        // 自动生成编码：ACT + 时间戳
        return "ACT" + System.currentTimeMillis();
    }

    /**
     * 解析日期时间字符串
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (SmartStringUtil.isBlank(dateTimeStr)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            log.error("解析时间失败: {}", dateTimeStr, e);
            return null;
        }
    }
}