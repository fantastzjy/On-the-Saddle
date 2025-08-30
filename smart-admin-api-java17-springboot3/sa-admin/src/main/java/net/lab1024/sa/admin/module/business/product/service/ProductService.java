package net.lab1024.sa.admin.module.business.product.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.product.dao.*;
import net.lab1024.sa.admin.module.business.product.domain.entity.*;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductAddForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductQueryForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductUpdateForm;
import net.lab1024.sa.admin.module.business.product.domain.form.CoachPriceFormItem;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductDetailVO;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductListVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.file.service.FileService;
import net.lab1024.sa.base.module.support.file.service.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 商品管理服务
 * 对应模块2.1 - 简化版商品管理后端服务
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductCourseDao productCourseDao;

    @Autowired
    private ProductPackageDao productPackageDao;

    @Autowired
    private ProductActivityDao productActivityDao;

    @Autowired
    private ProductCoachDao productCoachDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private IFileStorageService fileStorageService;

    // ========================================
    // 商品基础CRUD操作 (对应PROD_BE_001)
    // ========================================

    /**
     * 商品列表查询
     */
    public ResponseDTO<PageResult<ProductListVO>> queryProductList(ProductQueryForm queryForm) {
        try {
            Page<ProductEntity> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
            
            // 使用增强版查询，直接获取包含课程和课时包详情的数据
            Page<ProductListVO> pageResult = productDao.queryProductListWithDetails(page, queryForm);

            // 转换为PageResult
            PageResult<ProductListVO> result = SmartPageUtil.convert2PageResult(pageResult, pageResult.getRecords(), ProductListVO.class);

            // 补充额外信息（增强价格显示等）
            enhanceProductListData(result.getList());

            log.info("查询商品列表成功，共{}条记录", result.getTotal());
            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("查询商品列表失败", e);
            return ResponseDTO.userErrorParam("查询商品列表失败");
        }
    }

    /**
     * 获取商品详情
     */
    public ResponseDTO<ProductDetailVO> getProductDetail(Long productId) {
        try {
            ProductEntity productEntity = productDao.selectById(productId);
            if (productEntity == null || productEntity.getIsDelete()) {
                return ResponseDTO.userErrorParam("商品不存在");
            }

            ProductDetailVO detailVO = SmartBeanUtil.copy(productEntity, ProductDetailVO.class);

            // 补充详情信息
            enhanceProductDetailData(detailVO);

            log.info("获取商品详情成功，商品ID: {}", productId);
            return ResponseDTO.ok(detailVO);

        } catch (Exception e) {
            log.error("获取商品详情失败", e);
            return ResponseDTO.userErrorParam("获取商品详情失败");
        }
    }

    /**
     * 新增商品
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> addProduct(ProductAddForm addForm, Long operatorId) {
        try {
            // 验证教练价格配置（小组课）
            validateCoachPriceConfig(addForm);
            
            // 设置默认俱乐部ID（如果没有提供）
            if (addForm.getClubId() == null) {
                addForm.setClubId(1L); // 设置默认俱乐部ID，后续可以从用户信息中获取
            }

            // 验证商品编码唯一性，如果为空则自动生成
            if (SmartStringUtil.isBlank(addForm.getProductCode())) {
                // 自动生成商品编码
                addForm.setProductCode(generateProductCode(addForm.getProductType(), addForm.getClubId()));
            } else {
                // 验证用户提供的商品编码唯一性
                if (checkProductCodeExists(addForm.getProductCode(), addForm.getClubId(), null)) {
                    return ResponseDTO.userErrorParam("商品编码已存在");
                }
            }

            // 构建商品实体
            ProductEntity productEntity = SmartBeanUtil.copy(addForm, ProductEntity.class);
            productEntity.setCreateBy(operatorId.toString());
            productEntity.setCreateTime(LocalDateTime.now());
            productEntity.setUpdateBy(operatorId.toString());
            productEntity.setUpdateTime(LocalDateTime.now());
            productEntity.setIsValid(true);
            productEntity.setIsDelete(false);

            // 保存商品基础信息
            productDao.insert(productEntity);

            // 保存商品扩展配置 - 优先使用表单中的直接字段
            saveProductExtendedConfigFromForm(productEntity.getProductId(), addForm);
            
            // 保存教练课程关联关系（课程类型）
            if (addForm.getProductType() == 1 && addForm.getCoachIds() != null && !addForm.getCoachIds().isEmpty()) {
                saveProductCoachAssociations(productEntity.getProductId(), addForm.getCoachIds(), operatorId.toString());
            }

            log.info("新增商品成功，商品ID: {}", productEntity.getProductId());
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("新增商品失败", e);
            return ResponseDTO.userErrorParam("新增商品失败");
        }
    }

    /**
     * 编辑商品
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateProduct(ProductUpdateForm updateForm, Long operatorId) {
        try {
            // 验证教练价格配置（小组课）
            validateCoachPriceConfig(updateForm);
            
            ProductEntity existProduct = productDao.selectById(updateForm.getProductId());
            if (existProduct == null || existProduct.getIsDelete().equals(1)) {
                return ResponseDTO.userErrorParam("商品不存在");
            }

            // 验证商品编码唯一性
            if (SmartStringUtil.isNotBlank(updateForm.getProductCode())) {
                if (checkProductCodeExists(updateForm.getProductCode(), existProduct.getClubId(), updateForm.getProductId())) {
                    return ResponseDTO.userErrorParam("商品编码已存在");
                }
            }

            // 更新商品基础信息
            ProductEntity productEntity = SmartBeanUtil.copy(updateForm, ProductEntity.class);
            productEntity.setUpdateBy(operatorId.toString());
            productEntity.setUpdateTime(LocalDateTime.now());

            productDao.updateById(productEntity);

            // 更新商品扩展配置 - 优先使用表单中的直接字段
            saveProductExtendedConfigFromForm(updateForm.getProductId(), updateForm);
            
            // 更新教练课程关联关系（课程类型）
            if (updateForm.getProductType() == 1) {
                if (updateForm.getCoachIds() != null && !updateForm.getCoachIds().isEmpty()) {
                    saveProductCoachAssociations(updateForm.getProductId(), updateForm.getCoachIds(), operatorId.toString());
                } else {
                    // 如果没有教练ID，删除所有关联
                    productCoachDao.deleteByProductId(updateForm.getProductId());
                }
            }

            log.info("编辑商品成功，商品ID: {}", updateForm.getProductId());
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("编辑商品失败", e);
            return ResponseDTO.userErrorParam("编辑商品失败");
        }
    }

    /**
     * 删除商品
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteProduct(Long productId, Long operatorId) {
        try {
            ProductEntity productEntity = productDao.selectById(productId);
            if (productEntity == null || productEntity.getIsDelete()) {
                return ResponseDTO.userErrorParam("商品不存在");
            }

            // 检查商品是否可以删除
            ResponseDTO<String> checkResult = checkProductCanDelete(productId);
            if (!checkResult.getOk()) {
                return checkResult;
            }

            // 软删除商品
            productEntity.setIsDelete(true);
            productEntity.setUpdateBy(operatorId.toString());
            productEntity.setUpdateTime(LocalDateTime.now());

            productDao.updateById(productEntity);

            log.info("删除商品成功，商品ID: {}", productId);
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("删除商品失败", e);
            return ResponseDTO.userErrorParam("删除商品失败");
        }
    }

    /**
     * 批量删除商品
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> batchDeleteProducts(List<Long> productIds, Long operatorId) {
        try {
            if (productIds == null || productIds.isEmpty()) {
                return ResponseDTO.userErrorParam("请选择要删除的商品");
            }

            int successCount = 0;
            List<String> failedReasons = new ArrayList<>();

            for (Long productId : productIds) {
                ResponseDTO<String> deleteResult = deleteProduct(productId, operatorId);
                if (deleteResult.getOk()) {
                    successCount++;
                } else {
                    failedReasons.add("商品ID " + productId + ": " + deleteResult.getMsg());
                }
            }

            if (successCount == productIds.size()) {
                log.info("批量删除商品成功，删除{}个商品", successCount);
                return ResponseDTO.ok("成功删除" + successCount + "个商品");
            } else {
                String message = String.format("成功删除%d个商品，失败%d个。失败原因：%s",
                    successCount, productIds.size() - successCount, String.join("; ", failedReasons));
                return ResponseDTO.userErrorParam(message);
            }

        } catch (Exception e) {
            log.error("批量删除商品失败", e);
            return ResponseDTO.userErrorParam("批量删除商品失败");
        }
    }


    // ========================================
    // 商品搜索功能 (对应PROD_BE_006)
    // ========================================

    /**
     * 商品搜索
     */
    public ResponseDTO<PageResult<ProductListVO>> searchProducts(ProductQueryForm queryForm) {
        // 搜索逻辑与queryProductList相同，这里复用
        return queryProductList(queryForm);
    }

    /**
     * 获取商品类型选项
     */
    public ResponseDTO<List<Map<String, Object>>> getProductTypes() {
        List<Map<String, Object>> types = Arrays.asList(
            Map.of("value", 1, "label", "课程"),
            Map.of("value", 2, "label", "课时包"),
            Map.of("value", 3, "label", "活动")
        );
        return ResponseDTO.ok(types);
    }


    // ========================================
    // 库存管理功能 (对应PROD_BE_004)
    // ========================================

    /**
     * 获取商品库存
     */
    public ResponseDTO<Map<String, Object>> getProductStock(Long productId) {
        try {
            // 这里根据商品类型返回不同的库存信息
            ProductEntity product = productDao.selectById(productId);
            if (product == null) {
                return ResponseDTO.userErrorParam("商品不存在");
            }

            Map<String, Object> stockInfo = new HashMap<>();
            stockInfo.put("productId", productId);
            stockInfo.put("productType", product.getProductType());

            if (product.getProductType() == 2) { // 课时包
                // 查询课时包库存
                stockInfo.put("stockQuantity", 100); // 示例数据
                stockInfo.put("availableQuantity", 80);
                stockInfo.put("reservedQuantity", 20);
            } else {
                stockInfo.put("stockQuantity", -1); // 无限库存
                stockInfo.put("availableQuantity", -1);
                stockInfo.put("reservedQuantity", 0);
            }

            return ResponseDTO.ok(stockInfo);

        } catch (Exception e) {
            log.error("获取商品库存失败", e);
            return ResponseDTO.userErrorParam("获取商品库存失败");
        }
    }

    /**
     * 更新商品库存
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateProductStock(Long productId, Integer quantity, String reason, Long operatorId) {
        try {
            ProductEntity product = productDao.selectById(productId);
            if (product == null) {
                return ResponseDTO.userErrorParam("商品不存在");
            }

            // 这里应该根据商品类型更新对应的库存表
            // 课时包商品更新 m_product_package 表的库存
            // 课程和活动商品不需要库存管理

            log.info("更新商品库存成功，商品ID: {}，变更数量: {}，原因: {}", productId, quantity, reason);
            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("更新商品库存失败", e);
            return ResponseDTO.userErrorParam("更新商品库存失败");
        }
    }



    // ========================================
    // 私有辅助方法
    // ========================================

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<ProductEntity> buildQueryWrapper(ProductQueryForm queryForm) {
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ProductEntity::getIsDelete, false);

        // 产品管理只支持课程(1)和课时包(2)，过滤掉活动类型(3)
        wrapper.in(ProductEntity::getProductType, 1, 2);

        if (queryForm.getClubId() != null) {
            wrapper.eq(ProductEntity::getClubId, queryForm.getClubId());
        }

        if (SmartStringUtil.isNotBlank(queryForm.getProductName())) {
            wrapper.like(ProductEntity::getProductName, queryForm.getProductName());
        }

        if (SmartStringUtil.isNotBlank(queryForm.getProductCode())) {
            wrapper.like(ProductEntity::getProductCode, queryForm.getProductCode());
        }

        if (queryForm.getProductType() != null) {
            // 确保只允许查询课程(1)和课时包(2)
            if (queryForm.getProductType() == 1 || queryForm.getProductType() == 2) {
                wrapper.eq(ProductEntity::getProductType, queryForm.getProductType());
            }
        }

        if (SmartStringUtil.isNotBlank(queryForm.getKeyword())) {
            wrapper.and(w -> w.like(ProductEntity::getProductName, queryForm.getKeyword())
                .or().like(ProductEntity::getProductCode, queryForm.getKeyword()));
        }

        wrapper.orderByDesc(ProductEntity::getCreateTime);

        return wrapper;
    }

    /**
     * 检查商品编码是否存在
     */
    private boolean checkProductCodeExists(String productCode, Long clubId, Long excludeProductId) {
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductEntity::getProductCode, productCode)
            .eq(ProductEntity::getClubId, clubId)
            .eq(ProductEntity::getIsDelete, false);

        if (excludeProductId != null) {
            wrapper.ne(ProductEntity::getProductId, excludeProductId);
        }

        return productDao.selectCount(wrapper) > 0;
    }

    /**
     * 自动生成商品编码
     * 格式：{productType}_{clubId}_{timestamp}_{sequence}
     * 示例：COURSE_1_20240816_001
     */
    private String generateProductCode(Integer productType, Long clubId) {
        // 商品类型前缀
        String typePrefix;
        switch (productType) {
            case 1:
                typePrefix = "COURSE";
                break;
            case 2:
                typePrefix = "PACKAGE";
                break;
            case 3:
                typePrefix = "ACTIVITY";
                break;
            default:
                typePrefix = "PRODUCT";
                break;
        }

        // 时间戳（年月日）
        String dateStr = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 尝试生成唯一编码，最多尝试1000次
        for (int sequence = 1; sequence <= 1000; sequence++) {
            String productCode = String.format("%s_%d_%s_%03d", typePrefix, clubId, dateStr, sequence);

            // 检查编码是否已存在
            if (!checkProductCodeExists(productCode, clubId, null)) {
                return productCode;
            }
        }

        // 如果1000次都重复，使用时间戳确保唯一性
        long timestamp = System.currentTimeMillis();
        return String.format("%s_%d_%s_%d", typePrefix, clubId, dateStr, timestamp % 10000);
    }

    /**
     * 检查商品是否可以删除
     */
    private ResponseDTO<String> checkProductCanDelete(Long productId) {
        // 这里应该检查商品是否有关联的订单、预约等
        // 暂时返回可以删除
        return ResponseDTO.ok();
    }

    /**
     * 从表单保存商品扩展配置（适用于新增和更新）
     */
    private void saveProductExtendedConfigFromForm(Long productId, Object formObject) {
        if (formObject instanceof ProductAddForm) {
            ProductAddForm form = (ProductAddForm) formObject;
            saveProductExtendedConfigByType(productId, form.getProductType(), form);
        } else if (formObject instanceof ProductUpdateForm) {
            ProductUpdateForm form = (ProductUpdateForm) formObject;
            saveProductExtendedConfigByType(productId, form.getProductType(), form);
        }
    }

    /**
     * 根据商品类型保存扩展配置
     */
    private void saveProductExtendedConfigByType(Long productId, Integer productType, Object form) {
        try {
            log.info("保存商品扩展配置，商品ID: {}, 类型: {}", productId, productType);

            switch (productType) {
                case 1: // 课程
                    saveCourseConfigFromForm(productId, form);
                    break;
                case 2: // 课时包
                    savePackageConfigFromForm(productId, form);
                    break;
                case 3: // 活动
                    saveActivityConfigFromForm(productId, form);
                    break;
            }
        } catch (Exception e) {
            log.error("保存商品扩展配置失败，商品ID: {}, 类型: {}", productId, productType, e);
            throw new RuntimeException("保存商品扩展配置失败", e);
        }
    }

    /**
     * 从表单保存课程配置（支持教练价格配置）
     */
    private void saveCourseConfigFromForm(Long productId, Object form) {
        try {
            // 先删除已存在的配置
            LambdaQueryWrapper<ProductCourseEntity> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(ProductCourseEntity::getProductId, productId);
            productCourseDao.delete(deleteWrapper);

            // 创建新的课程配置
            ProductCourseEntity courseEntity = new ProductCourseEntity();
            courseEntity.setProductId(productId);

            // 从表单对象提取字段值（只处理课时费）
            if (form instanceof ProductAddForm) {
                ProductAddForm addForm = (ProductAddForm) form;
                courseEntity.setClassType(addForm.getClassType());
                courseEntity.setDurationMinutes(addForm.getDurationMinutes());
                courseEntity.setDurationPeriods(addForm.getDurationPeriods());
                courseEntity.setMaxStudents(addForm.getMaxStudents());
                courseEntity.setSessionFee(addForm.getSessionFee());
                
                // 处理教练价格配置（小组课专用）
                if (addForm.getClassType() != null && addForm.getClassType() == 2) {
                    String coachPriceConfigJson = buildCoachPriceConfigJson(addForm.getCoachPriceList());
                    courseEntity.setMultiPriceConfig(coachPriceConfigJson);
                } else {
                    courseEntity.setMultiPriceConfig(null);
                }
                
            } else if (form instanceof ProductUpdateForm) {
                ProductUpdateForm updateForm = (ProductUpdateForm) form;
                courseEntity.setClassType(updateForm.getClassType());
                courseEntity.setDurationMinutes(updateForm.getDurationMinutes());
                courseEntity.setDurationPeriods(updateForm.getDurationPeriods());
                courseEntity.setMaxStudents(updateForm.getMaxStudents());
                courseEntity.setSessionFee(updateForm.getSessionFee());
                
                // 处理教练价格配置（小组课专用）
                if (updateForm.getClassType() != null && updateForm.getClassType() == 2) {
                    String coachPriceConfigJson = buildCoachPriceConfigJson(updateForm.getCoachPriceList());
                    courseEntity.setMultiPriceConfig(coachPriceConfigJson);
                } else {
                    courseEntity.setMultiPriceConfig(null);
                }
            }

            // 基础价格直接等于课时费
            if (courseEntity.getSessionFee() != null) {
                courseEntity.setBasePrice(courseEntity.getSessionFee());
            }

            courseEntity.setCreateTime(LocalDateTime.now());
            courseEntity.setUpdateTime(LocalDateTime.now());

            // 只有当有有效数据时才保存
            if (courseEntity.getClassType() != null) {
                productCourseDao.insert(courseEntity);
                log.info("保存课程配置成功，商品ID: {}, 课程分类: {}", productId, courseEntity.getClassType());
            }
        } catch (Exception e) {
            log.error("保存课程配置失败，商品ID: {}", productId, e);
            throw e;
        }
    }

    /**
     * 从表单保存课时包配置
     */
    private void savePackageConfigFromForm(Long productId, Object form) {
        try {
            // 先删除已存在的配置
            LambdaQueryWrapper<ProductPackageEntity> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(ProductPackageEntity::getProductId, productId);
            productPackageDao.delete(deleteWrapper);

            // 创建新的课时包配置
            ProductPackageEntity packageEntity = new ProductPackageEntity();
            packageEntity.setProductId(productId);

            // 从表单对象提取字段值
            if (form instanceof ProductAddForm) {
                ProductAddForm addForm = (ProductAddForm) form;
                packageEntity.setDetails(addForm.getDetails());
                packageEntity.setPrice(addForm.getPrice());
                packageEntity.setTotalSessions(addForm.getTotalSessions());
                packageEntity.setValidityDays(addForm.getValidityDays());
                packageEntity.setStockQuantity(addForm.getStockQuantity());
            } else if (form instanceof ProductUpdateForm) {
                ProductUpdateForm updateForm = (ProductUpdateForm) form;
                packageEntity.setDetails(updateForm.getDetails());
                packageEntity.setPrice(updateForm.getPrice());
                packageEntity.setTotalSessions(updateForm.getTotalSessions());
                packageEntity.setValidityDays(updateForm.getValidityDays());
                packageEntity.setStockQuantity(updateForm.getStockQuantity());
            }

            packageEntity.setCreateTime(LocalDateTime.now());
            packageEntity.setUpdateTime(LocalDateTime.now());

            // 只有当有有效数据时才保存
            if (packageEntity.getDetails() != null && !packageEntity.getDetails().trim().isEmpty()) {
                productPackageDao.insert(packageEntity);
                log.info("保存课时包配置成功，商品ID: {}", productId);
            }
        } catch (Exception e) {
            log.error("保存课时包配置失败，商品ID: {}", productId, e);
            throw e;
        }
    }

    /**
     * 从表单保存活动配置
     */
    private void saveActivityConfigFromForm(Long productId, Object form) {
        try {
            // 先删除已存在的配置
            LambdaQueryWrapper<ProductActivityEntity> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(ProductActivityEntity::getProductId, productId);
            productActivityDao.delete(deleteWrapper);

            // 创建新的活动配置
            ProductActivityEntity activityEntity = new ProductActivityEntity();
            activityEntity.setProductId(productId);

            // 从表单对象提取字段值
            if (form instanceof ProductAddForm) {
                ProductAddForm addForm = (ProductAddForm) form;
                activityEntity.setActivityName(addForm.getActivityName());
                activityEntity.setActivityDetails(addForm.getActivityDetails());
                activityEntity.setStartTime(parseDateTime(addForm.getStartTime()));
                activityEntity.setEndTime(parseDateTime(addForm.getEndTime()));
                activityEntity.setActivityLocation(addForm.getActivityLocation());
                activityEntity.setPrice(addForm.getPrice());
                activityEntity.setMaxParticipants(addForm.getMaxParticipants());
                activityEntity.setActivityRule(addForm.getActivityRule());
                activityEntity.setDetailImages(addForm.getDetailImages());
            } else if (form instanceof ProductUpdateForm) {
                ProductUpdateForm updateForm = (ProductUpdateForm) form;
                activityEntity.setActivityName(updateForm.getActivityName());
                activityEntity.setActivityDetails(updateForm.getActivityDetails());
                activityEntity.setStartTime(parseDateTime(updateForm.getStartTime()));
                activityEntity.setEndTime(parseDateTime(updateForm.getEndTime()));
                activityEntity.setActivityLocation(updateForm.getActivityLocation());
                activityEntity.setPrice(updateForm.getPrice());
                activityEntity.setMaxParticipants(updateForm.getMaxParticipants());
                activityEntity.setActivityRule(updateForm.getActivityRule());
                activityEntity.setDetailImages(updateForm.getDetailImages());
            }

            activityEntity.setCreateTime(LocalDateTime.now());
            activityEntity.setUpdateTime(LocalDateTime.now());

            // 只有当有有效数据时才保存
            if (activityEntity.getActivityName() != null && !activityEntity.getActivityName().trim().isEmpty()) {
                productActivityDao.insert(activityEntity);
                log.info("保存活动配置成功，商品ID: {}", productId);
            }
        } catch (Exception e) {
            log.error("保存活动配置失败，商品ID: {}", productId, e);
            throw e;
        }
    }


    /**
     * 解析时间字符串为LocalDateTime
     */
    private LocalDateTime parseDateTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        try {
            // 尝试解析不同的时间格式
            if (timeStr.contains("T")) {
                return LocalDateTime.parse(timeStr);
            } else {
                // 处理其他可能的时间格式
                return LocalDateTime.parse(timeStr.replace(" ", "T"));
            }
        } catch (Exception e) {
            log.warn("解析时间字符串失败: {}", timeStr, e);
            return null;
        }
    }
    private void saveProductExtendedConfig(Long productId, Integer productType, String configDataJson) {
        if (configDataJson == null || configDataJson.trim().isEmpty()) {
            return;
        }

        try {
            // 解析JSON配置
            Map<String, Object> configData = JSON.parseObject(configDataJson, Map.class);
            log.info("保存商品扩展配置，商品ID: {}, 类型: {}, 配置: {}", productId, productType, configDataJson);

            // 根据商品类型保存到对应的扩展表
            switch (productType) {
                case 1: // 课程
                    saveCourseConfig(productId, configData);
                    break;
                case 2: // 课时包
                    savePackageConfig(productId, configData);
                    break;
                case 3: // 活动
                    saveActivityConfig(productId, configData);
                    break;
            }
        } catch (Exception e) {
            log.error("保存商品扩展配置失败，商品ID: {}, 类型: {}", productId, productType, e);
            throw new RuntimeException("保存商品扩展配置失败", e);
        }
    }

    /**
     * 保存课程配置
     */
    private void saveCourseConfig(Long productId, Map<String, Object> configData) {
        try {
            // 先删除已存在的配置
            LambdaQueryWrapper<ProductCourseEntity> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(ProductCourseEntity::getProductId, productId);
            productCourseDao.delete(deleteWrapper);

            // 创建新的课程配置
            ProductCourseEntity courseEntity = new ProductCourseEntity();
            courseEntity.setProductId(productId);
            courseEntity.setClassType(getIntegerFromConfig(configData, "classType"));
            courseEntity.setDurationMinutes(getIntegerFromConfig(configData, "durationMinutes"));
            courseEntity.setDurationPeriods(getBigDecimalFromConfig(configData, "durationPeriods"));
            courseEntity.setMaxStudents(getIntegerFromConfig(configData, "maxStudents"));

            // 处理课时费（只使用课时费）
            BigDecimal sessionFee = getBigDecimalFromConfig(configData, "sessionFee");
            if (sessionFee == null) {
                sessionFee = BigDecimal.valueOf(0); // 默认值
            }
            courseEntity.setSessionFee(sessionFee);
            courseEntity.setBasePrice(sessionFee);

            courseEntity.setMultiPriceConfig(getStringFromConfig(configData, "multiPriceConfig"));

            courseEntity.setCreateTime(LocalDateTime.now());
            courseEntity.setUpdateTime(LocalDateTime.now());

            productCourseDao.insert(courseEntity);
            log.info("保存课程配置成功，商品ID: {}", productId);
        } catch (Exception e) {
            log.error("保存课程配置失败，商品ID: {}", productId, e);
            throw e;
        }
    }

    /**
     * 保存课时包配置
     */
    private void savePackageConfig(Long productId, Map<String, Object> configData) {
        try {
            // 先删除已存在的配置
            LambdaQueryWrapper<ProductPackageEntity> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(ProductPackageEntity::getProductId, productId);
            productPackageDao.delete(deleteWrapper);

            // 创建新的课时包配置
            ProductPackageEntity packageEntity = new ProductPackageEntity();
            packageEntity.setProductId(productId);
            packageEntity.setDetails(getStringFromConfig(configData, "details"));
            packageEntity.setPrice(getBigDecimalFromConfig(configData, "price"));
            packageEntity.setTotalSessions(getIntegerFromConfig(configData, "totalSessions"));
            packageEntity.setValidityDays(getIntegerFromConfig(configData, "validityDays"));
            packageEntity.setStockQuantity(getIntegerFromConfig(configData, "stockQuantity"));
            packageEntity.setCreateTime(LocalDateTime.now());
            packageEntity.setUpdateTime(LocalDateTime.now());

            productPackageDao.insert(packageEntity);
            log.info("保存课时包配置成功，商品ID: {}", productId);
        } catch (Exception e) {
            log.error("保存课时包配置失败，商品ID: {}", productId, e);
            throw e;
        }
    }

    /**
     * 保存活动配置
     */
    private void saveActivityConfig(Long productId, Map<String, Object> configData) {
        try {
            // 先删除已存在的配置
            LambdaQueryWrapper<ProductActivityEntity> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(ProductActivityEntity::getProductId, productId);
            productActivityDao.delete(deleteWrapper);

            // 创建新的活动配置
            ProductActivityEntity activityEntity = new ProductActivityEntity();
            activityEntity.setProductId(productId);
            activityEntity.setActivityName(getStringFromConfig(configData, "activityName"));
            activityEntity.setActivityDetails(getStringFromConfig(configData, "activityDetails"));
            activityEntity.setStartTime(getLocalDateTimeFromConfig(configData, "startTime"));
            activityEntity.setEndTime(getLocalDateTimeFromConfig(configData, "endTime"));
            activityEntity.setActivityLocation(getStringFromConfig(configData, "activityLocation"));
            activityEntity.setPrice(getBigDecimalFromConfig(configData, "activityPrice"));
            activityEntity.setMaxParticipants(getIntegerFromConfig(configData, "maxParticipants"));
            activityEntity.setActivityRule(getStringFromConfig(configData, "activityRule"));
            activityEntity.setDetailImages(getStringFromConfig(configData, "detailImages"));
            activityEntity.setCreateTime(LocalDateTime.now());
            activityEntity.setUpdateTime(LocalDateTime.now());

            productActivityDao.insert(activityEntity);
            log.info("保存活动配置成功，商品ID: {}", productId);
        } catch (Exception e) {
            log.error("保存活动配置失败，商品ID: {}", productId, e);
            throw e;
        }
    }

    // 辅助方法：从配置Map中安全获取各种类型的值
    private String getStringFromConfig(Map<String, Object> configData, String key) {
        Object value = configData.get(key);
        return value != null ? value.toString() : null;
    }

    private Integer getIntegerFromConfig(Map<String, Object> configData, String key) {
        Object value = configData.get(key);
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal getBigDecimalFromConfig(Map<String, Object> configData, String key) {
        Object value = configData.get(key);
        if (value == null) return null;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime getLocalDateTimeFromConfig(Map<String, Object> configData, String key) {
        Object value = configData.get(key);
        if (value == null) return null;
        if (value instanceof LocalDateTime) return (LocalDateTime) value;
        try {
            // 这里可以根据前端传递的时间格式进行解析
            return LocalDateTime.parse(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 补充商品列表数据（增强版，支持课程详情和价格配置）
     */
    private void enhanceProductListData(List<ProductListVO> productList) {
        for (ProductListVO product : productList) {
            // 设置类型名称
            product.setProductTypeName(getProductTypeName(product.getProductType()));

            // 根据商品类型设置详细信息
            if (product.getProductType() == 1) { // 课程
                enhanceCourseData(product);
            } else if (product.getProductType() == 2) { // 课时包
                enhancePackageData(product);
            }

            // 设置兼容的旧字段
            product.setPriceInfo(product.getPriceDisplay());
            product.setStockInfo(product.getCapacityDisplay());
        }
    }

    /**
     * 增强课程数据
     */
    private void enhanceCourseData(ProductListVO product) {
        // 设置课程分类名称
        product.setClassTypeName(getClassTypeName(product.getClassType()));
        
        // 根据课程分类设置价格显示
        if (product.getClassType() != null && product.getClassType() == 2) {
            // 小组课：解析多人价格配置
            parseMultiPriceConfig(product);
        } else {
            // 单人课：简单显示课时费
            if (product.getSessionFee() != null) {
                product.setPriceDisplay("¥" + product.getSessionFee() + "/节");
            }
        }
        
        // 设置时长显示
        if (product.getDurationMinutes() != null && product.getDurationMinutes() > 0) {
            product.setDurationDisplay(product.getDurationMinutes() + "分钟");
        } else if (product.getDurationPeriods() != null) {
            product.setDurationDisplay(product.getDurationPeriods() + "鞍时");
        }
        
        // 设置容量显示
        if (product.getMaxStudents() != null) {
            product.setCapacityDisplay("最多" + product.getMaxStudents() + "人");
        }
    }

    /**
     * 增强课时包数据
     */
    private void enhancePackageData(ProductListVO product) {
        // 设置价格显示
        if (product.getPackagePrice() != null) {
            product.setPriceDisplay("¥" + product.getPackagePrice() + "/包");
        }
        
        // 设置时长显示
        if (product.getTotalSessions() != null) {
            product.setDurationDisplay(product.getTotalSessions() + "课时");
        }
        
        // 设置容量显示（库存）
        if (product.getStockQuantity() != null) {
            product.setCapacityDisplay("库存" + product.getStockQuantity());
        }
    }

    /**
     * 解析小组课多人价格配置
     */
    private void parseMultiPriceConfig(ProductListVO product) {
        if (product.getMultiPriceConfig() == null || product.getMultiPriceConfig().trim().isEmpty()) {
            // 没有多人价格配置，使用基础课时费
            if (product.getSessionFee() != null) {
                product.setPriceDisplay("¥" + product.getSessionFee() + "/节");
            }
            return;
        }
        
        try {
            // 解析JSON配置
            @SuppressWarnings("unchecked")
            Map<String, Object> priceConfigMap = JSON.parseObject(product.getMultiPriceConfig(), Map.class);
            
            List<ProductListVO.PriceConfigItem> priceItems = new ArrayList<>();
            StringBuilder priceDisplay = new StringBuilder();
            
            // 按人数排序处理
            priceConfigMap.entrySet().stream()
                .sorted(Map.Entry.<String, Object>comparingByKey((k1, k2) -> {
                    try {
                        return Integer.compare(Integer.valueOf(k1), Integer.valueOf(k2));
                    } catch (NumberFormatException e) {
                        return k1.compareTo(k2);
                    }
                }))
                .forEach(entry -> {
                    try {
                        Integer count = Integer.valueOf(entry.getKey());
                        BigDecimal price = new BigDecimal(entry.getValue().toString());
                        
                        ProductListVO.PriceConfigItem item = new ProductListVO.PriceConfigItem();
                        item.setParticipantCount(count);
                        item.setPricePerPerson(price);
                        item.setDisplayText(count + "人¥" + price + "/人");
                        priceItems.add(item);
                        
                        if (priceDisplay.length() > 0) {
                            priceDisplay.append("；");
                        }
                        priceDisplay.append(item.getDisplayText());
                    } catch (Exception e) {
                        log.warn("解析价格配置项失败: {}", entry, e);
                    }
                });
            
            product.setPriceConfigs(priceItems);
            product.setMultiPriceDisplay(priceDisplay.toString());
            product.setPriceDisplay("小组课：" + priceDisplay.toString());
            
        } catch (Exception e) {
            log.error("解析小组课价格配置失败，商品ID: {}, 配置: {}", product.getProductId(), product.getMultiPriceConfig(), e);
            // 解析失败，使用基础价格
            if (product.getSessionFee() != null) {
                product.setPriceDisplay("¥" + product.getSessionFee() + "/节");
            }
        }
    }

    /**
     * 获取课程分类名称
     */
    private String getClassTypeName(Integer classType) {
        if (classType == null) return null;
        switch (classType) {
            case 1: return "单人课";
            case 2: return "小组课";
            default: return "未知";
        }
    }

    /**
     * 补充商品详情数据
     */
    private void enhanceProductDetailData(ProductDetailVO productDetail) {
        // 设置类型名称
        productDetail.setProductTypeName(getProductTypeName(productDetail.getProductType()));

        // 根据商品类型查询详细配置
        switch (productDetail.getProductType()) {
            case 1: // 课程
                productDetail.setCourseDetails(getCourseDetails(productDetail.getProductId()));
                break;
            case 2: // 课时包
                productDetail.setPackageDetails(getPackageDetails(productDetail.getProductId()));
                break;
            case 3: // 活动
                productDetail.setActivityDetails(getActivityDetails(productDetail.getProductId()));
                break;
        }
    }

    /**
     * 获取商品类型名称
     */
    private String getProductTypeName(Integer productType) {
        switch (productType) {
            case 1: return "课程";
            case 2: return "课时包";
            case 3: return "活动";
            default: return "未知";
        }
    }

    /**
     * 获取课程详情配置
     */
    private Map<String, Object> getCourseDetails(Long productId) {
        try {
            LambdaQueryWrapper<ProductCourseEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductCourseEntity::getProductId, productId);
            ProductCourseEntity courseEntity = productCourseDao.selectOne(wrapper);

            Map<String, Object> courseDetails = new HashMap<>();
            if (courseEntity != null) {
                courseDetails.put("classType", courseEntity.getClassType());
                courseDetails.put("durationMinutes", courseEntity.getDurationMinutes());
                courseDetails.put("durationPeriods", courseEntity.getDurationPeriods());
                courseDetails.put("maxStudents", courseEntity.getMaxStudents());
                courseDetails.put("sessionFee", courseEntity.getSessionFee());
                courseDetails.put("basePrice", courseEntity.getBasePrice());
                courseDetails.put("multiPriceConfig", courseEntity.getMultiPriceConfig());
            }

            return courseDetails;
        } catch (Exception e) {
            log.error("获取课程详情失败，商品ID: {}", productId, e);
            return new HashMap<>();
        }
    }

    /**
     * 获取课时包详情配置
     */
    private Map<String, Object> getPackageDetails(Long productId) {
        try {
            LambdaQueryWrapper<ProductPackageEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductPackageEntity::getProductId, productId);
            ProductPackageEntity packageEntity = productPackageDao.selectOne(wrapper);

            Map<String, Object> packageDetails = new HashMap<>();
            if (packageEntity != null) {
                packageDetails.put("details", packageEntity.getDetails());
                packageDetails.put("price", packageEntity.getPrice());
                packageDetails.put("totalSessions", packageEntity.getTotalSessions());
                packageDetails.put("validityDays", packageEntity.getValidityDays());
                packageDetails.put("stockQuantity", packageEntity.getStockQuantity());
            }

            return packageDetails;
        } catch (Exception e) {
            log.error("获取课时包详情失败，商品ID: {}", productId, e);
            return new HashMap<>();
        }
    }

    /**
     * 获取活动详情配置
     */
    private Map<String, Object> getActivityDetails(Long productId) {
        try {
            LambdaQueryWrapper<ProductActivityEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductActivityEntity::getProductId, productId);
            ProductActivityEntity activityEntity = productActivityDao.selectOne(wrapper);

            Map<String, Object> activityDetails = new HashMap<>();
            if (activityEntity != null) {
                activityDetails.put("activityName", activityEntity.getActivityName());
                activityDetails.put("activityDetails", activityEntity.getActivityDetails());
                activityDetails.put("startTime", activityEntity.getStartTime());
                activityDetails.put("endTime", activityEntity.getEndTime());
                activityDetails.put("activityLocation", activityEntity.getActivityLocation());
                activityDetails.put("price", activityEntity.getPrice());
                activityDetails.put("maxParticipants", activityEntity.getMaxParticipants());
                activityDetails.put("activityRule", activityEntity.getActivityRule());

                // 🔧 修复：将JSON字符串转换为数组格式
                String detailImagesJson = activityEntity.getDetailImages();
                log.info("活动详情图片原始数据 - 商品ID: {}, detailImages: {}", productId, detailImagesJson);
                if (SmartStringUtil.isNotBlank(detailImagesJson)) {
                    try {
                        List<String> detailImagesList = JSON.parseArray(detailImagesJson, String.class);
                        activityDetails.put("detailImages", detailImagesList);
                        log.info("活动详情图片解析成功 - 商品ID: {}, 图片数量: {}", productId, detailImagesList.size());
                    } catch (Exception e) {
                        log.warn("解析detailImages失败 - 商品ID: {}, 原始数据: {}", productId, detailImagesJson, e);
                        activityDetails.put("detailImages", new ArrayList<>());
                    }
                } else {
                    log.info("活动详情图片为空 - 商品ID: {}", productId);
                    activityDetails.put("detailImages", new ArrayList<>());
                }
            }

            return activityDetails;
        } catch (Exception e) {
            log.error("获取活动详情失败，商品ID: {}", productId, e);
            return new HashMap<>();
        }
    }

    // ========================================
    // 教练课程绑定相关方法 (新增功能)
    // ========================================

    /**
     * 获取产品关联的教练列表
     */
    public ResponseDTO<List<Map<String, Object>>> getProductCoaches(Long productId) {
        try {
            List<Map<String, Object>> coaches = productCoachDao.selectProductCoachDetails(productId);
            return ResponseDTO.ok(coaches);
        } catch (Exception e) {
            log.error("获取产品教练失败，productId: {}", productId, e);
            return ResponseDTO.userErrorParam("获取产品教练失败");
        }
    }

    /**
     * 更新产品教练关联
     */
    @Transactional
    public ResponseDTO<String> updateProductCoaches(Long productId, List<Long> coachIds, String operator) {
        try {
            // 1. 删除原有关联
            productCoachDao.deleteByProductId(productId);

            // 2. 批量插入新关联
            if (coachIds != null && !coachIds.isEmpty()) {
                List<ProductCoachEntity> entities = new ArrayList<>();
                for (int i = 0; i < coachIds.size(); i++) {
                    ProductCoachEntity entity = new ProductCoachEntity();
                    entity.setProductId(productId);
                    entity.setCoachId(coachIds.get(i));
                    entity.setIsDefault(i == 0 ? 1 : 0); // 第一个设为默认教练
                    entity.setCreateBy(operator);
                    entity.setCreateTime(LocalDateTime.now());
                    entities.add(entity);
                }
                productCoachDao.insertBatch(entities);
            }

            return ResponseDTO.ok("更新成功");
        } catch (Exception e) {
            log.error("更新产品教练关联失败，productId: {}, coachIds: {}", productId, coachIds, e);
            return ResponseDTO.userErrorParam("更新失败");
        }
    }

    /**
     * 根据教练ID获取关联课程（供小程序使用）
     */
    public ResponseDTO<List<Map<String, Object>>> getCoachCourses(Long coachId, Long clubId) {
        try {
            List<Map<String, Object>> courses = productCoachDao.selectCoachCourseList(coachId, clubId);
            return ResponseDTO.ok(courses);
        } catch (Exception e) {
            log.error("获取教练课程失败，coachId: {}, clubId: {}", coachId, clubId, e);
            return ResponseDTO.userErrorParam("获取教练课程失败");
        }
    }
    
    // ========================================
    // 新增：教练价格配置相关方法
    // ========================================
    
    /**
     * 构建教练价格配置JSON字符串
     */
    private String buildCoachPriceConfigJson(java.util.List<CoachPriceFormItem> coachPriceList) {
        if (coachPriceList == null || coachPriceList.isEmpty()) {
            return null;
        }
        
        try {
            net.lab1024.sa.admin.module.business.product.domain.vo.CoachPriceConfigVO config = 
                new net.lab1024.sa.admin.module.business.product.domain.vo.CoachPriceConfigVO();
            
            java.util.Map<Long, net.lab1024.sa.admin.module.business.product.domain.vo.CoachPriceConfigVO.CoachPriceInfo> coachPricing = 
                new java.util.HashMap<>();
            
            for (CoachPriceFormItem formItem : coachPriceList) {
                if (formItem.getCoachId() != null && formItem.getPrices() != null && !formItem.getPrices().isEmpty()) {
                    net.lab1024.sa.admin.module.business.product.domain.vo.CoachPriceConfigVO.CoachPriceInfo priceInfo = 
                        formItem.toCoachPriceInfo();
                    coachPricing.put(formItem.getCoachId(), priceInfo);
                }
            }
            
            config.setCoachPricing(coachPricing);
            return config.toJson();
            
        } catch (Exception e) {
            log.error("构建教练价格配置JSON失败", e);
            return null;
        }
    }
    
    /**
     * 验证教练价格配置的完整性
     */
    private void validateCoachPriceConfig(Object form) {
        try {
            Integer classType = null;
            java.util.List<Long> coachIds = null;
            java.util.List<CoachPriceFormItem> coachPriceList = null;
            
            if (form instanceof ProductAddForm) {
                ProductAddForm addForm = (ProductAddForm) form;
                classType = addForm.getClassType();
                coachIds = addForm.getCoachIds();
                coachPriceList = addForm.getCoachPriceList();
            } else if (form instanceof ProductUpdateForm) {
                ProductUpdateForm updateForm = (ProductUpdateForm) form;
                classType = updateForm.getClassType();
                coachIds = updateForm.getCoachIds();
                coachPriceList = updateForm.getCoachPriceList();
            }
            
            // 只有小组课才需要验证教练价格配置
            if (classType == null || classType != 2) {
                return;
            }
            
            // 验证教练ID不为空
            if (coachIds == null || coachIds.isEmpty()) {
                throw new RuntimeException("小组课必须选择授课教练");
            }
            
            // 验证教练价格配置不为空
            if (coachPriceList == null || coachPriceList.isEmpty()) {
                throw new RuntimeException("小组课必须配置教练价格");
            }
            
            // 验证每个选择的教练都有价格配置
            java.util.Set<Long> selectedCoachIds = new java.util.HashSet<>(coachIds);
            java.util.Set<Long> configuredCoachIds = coachPriceList.stream()
                .map(CoachPriceFormItem::getCoachId)
                .collect(java.util.stream.Collectors.toSet());
            
            if (!selectedCoachIds.equals(configuredCoachIds)) {
                throw new RuntimeException("所有选择的教练都必须配置价格");
            }
            
            // 验证每个教练的价格配置完整性
            for (CoachPriceFormItem item : coachPriceList) {
                if (!item.isConfigComplete()) {
                    throw new RuntimeException("教练 " + item.getCoachName() + " 的价格配置不完整，必须配置2-6人的所有价格");
                }
                
                // 验证价格合理性
                for (int count = 2; count <= 6; count++) {
                    BigDecimal price = item.getPrice(count);
                    if (price != null) {
                        if (price.compareTo(BigDecimal.valueOf(10)) < 0) {
                            throw new RuntimeException("教练 " + item.getCoachName() + " 的" + count + "人价格不能低于10元");
                        }
                        if (price.compareTo(BigDecimal.valueOf(10000)) > 0) {
                            throw new RuntimeException("教练 " + item.getCoachName() + " 的" + count + "人价格不能超过10000元");
                        }
                    }
                }
            }
            
            log.info("教练价格配置验证通过，配置了{}个教练的价格", coachPriceList.size());
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("验证教练价格配置失败", e);
            throw new RuntimeException("教练价格配置验证失败");
        }
    }
    
    /**
     * 保存教练课程关联关系
     */
    private void saveProductCoachAssociations(Long productId, java.util.List<Long> coachIds, String operator) {
        try {
            if (coachIds == null || coachIds.isEmpty()) {
                return;
            }
            
            // 先删除原有关联
            productCoachDao.deleteByProductId(productId);
            
            // 批量插入新关联
            java.util.List<ProductCoachEntity> entities = new java.util.ArrayList<>();
            for (int i = 0; i < coachIds.size(); i++) {
                ProductCoachEntity entity = new ProductCoachEntity();
                entity.setProductId(productId);
                entity.setCoachId(coachIds.get(i));
                entity.setIsDefault(i == 0 ? 1 : 0); // 第一个设为默认教练
                entity.setCreateBy(operator);
                entity.setCreateTime(LocalDateTime.now());
                entities.add(entity);
            }
            
            productCoachDao.insertBatch(entities);
            log.info("保存教练课程关联成功，产品ID: {}, 关联教练数: {}", productId, coachIds.size());
            
        } catch (Exception e) {
            log.error("保存教练课程关联失败，产品ID: {}, 教练ID: {}", productId, coachIds, e);
            throw new RuntimeException("保存教练课程关联失败");
        }
    }

}
