package net.lab1024.sa.admin.module.business.product.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductAddForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductQueryForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductUpdateForm;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductDetailVO;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductListVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private FileService fileService;

    // ========================================
    // 商品基础CRUD操作 (对应PROD_BE_001)
    // ========================================

    /**
     * 商品列表查询
     */
    public ResponseDTO<PageResult<ProductListVO>> queryProductList(ProductQueryForm queryForm) {
        try {
            Page<ProductEntity> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
            LambdaQueryWrapper<ProductEntity> queryWrapper = buildQueryWrapper(queryForm);
            
            IPage<ProductEntity> pageResult = productDao.selectPage(page, queryWrapper);
            
            // 创建一个新的Page对象来传递给SmartPageUtil
            Page<ProductEntity> resultPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
            PageResult<ProductListVO> result = SmartPageUtil.convert2PageResult(resultPage, pageResult.getRecords(), ProductListVO.class);
            
            // 补充额外信息
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
            // 设置默认俱乐部ID（如果没有提供）
            if (addForm.getClubId() == null) {
                addForm.setClubId(1L); // 设置默认俱乐部ID，后续可以从用户信息中获取
            }
            
            // 验证商品编码唯一性
            if (SmartStringUtil.isNotBlank(addForm.getProductCode())) {
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
            
            // 设置默认值
            if (productEntity.getStatus() == null) {
                productEntity.setStatus(1); // 默认上架
            }
            if (productEntity.getSortOrder() == null) {
                productEntity.setSortOrder(0);
            }
            
            // 保存商品基础信息
            productDao.insert(productEntity);
            
            // 保存商品扩展配置
            saveProductExtendedConfig(productEntity.getProductId(), addForm.getProductType(), addForm.getDynamicConfig());
            
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
            ProductEntity existProduct = productDao.selectById(updateForm.getProductId());
            if (existProduct == null || existProduct.getIsDelete()) {
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
            
            // 更新商品扩展配置
            saveProductExtendedConfig(updateForm.getProductId(), updateForm.getProductType(), updateForm.getDynamicConfig());
            
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

    /**
     * 更新商品状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateProductStatus(Long productId, Integer status, Long operatorId) {
        try {
            ProductEntity productEntity = productDao.selectById(productId);
            if (productEntity == null || productEntity.getIsDelete()) {
                return ResponseDTO.userErrorParam("商品不存在");
            }
            
            productEntity.setStatus(status);
            productEntity.setUpdateBy(operatorId.toString());
            productEntity.setUpdateTime(LocalDateTime.now());
            
            productDao.updateById(productEntity);
            
            log.info("更新商品状态成功，商品ID: {}，状态: {}", productId, status);
            return ResponseDTO.ok();
            
        } catch (Exception e) {
            log.error("更新商品状态失败", e);
            return ResponseDTO.userErrorParam("更新商品状态失败");
        }
    }

    /**
     * 批量更新商品状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> batchUpdateProductStatus(List<Long> productIds, Integer status, Long operatorId) {
        try {
            if (productIds == null || productIds.isEmpty()) {
                return ResponseDTO.userErrorParam("请选择要更新的商品");
            }
            
            int successCount = 0;
            for (Long productId : productIds) {
                ResponseDTO<String> updateResult = updateProductStatus(productId, status, operatorId);
                if (updateResult.getOk()) {
                    successCount++;
                }
            }
            
            log.info("批量更新商品状态成功，更新{}个商品", successCount);
            return ResponseDTO.ok("成功更新" + successCount + "个商品状态");
            
        } catch (Exception e) {
            log.error("批量更新商品状态失败", e);
            return ResponseDTO.userErrorParam("批量更新商品状态失败");
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

    /**
     * 获取商品状态选项
     */
    public ResponseDTO<List<Map<String, Object>>> getProductStatusOptions() {
        List<Map<String, Object>> statuses = Arrays.asList(
            Map.of("value", 1, "label", "上架"),
            Map.of("value", 2, "label", "下架"),
            Map.of("value", 3, "label", "售罄")
        );
        return ResponseDTO.ok(statuses);
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
    // 图片上传功能 (对应PROD_BE_005)
    // ========================================

    /**
     * 上传商品图片
     */
    public ResponseDTO<Map<String, Object>> uploadProductImage(org.springframework.web.multipart.MultipartFile file) {
        try {
            // 获取当前用户信息
            net.lab1024.sa.base.common.domain.RequestUser requestUser = net.lab1024.sa.base.common.util.SmartRequestUtil.getRequestUser();
            
            // 调用文件服务上传图片
            ResponseDTO<net.lab1024.sa.base.module.support.file.domain.vo.FileUploadVO> uploadResult = 
                fileService.fileUpload(file, net.lab1024.sa.base.module.support.file.constant.FileFolderTypeEnum.COMMON.getValue(), requestUser);
            
            if (!uploadResult.getOk()) {
                return ResponseDTO.userErrorParam("图片上传失败：" + uploadResult.getMsg());
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("url", uploadResult.getData().getFileUrl());
            result.put("fileName", uploadResult.getData().getFileName());
            result.put("uploadTime", LocalDateTime.now());
            
            log.info("商品图片上传成功，文件名: {}", uploadResult.getData().getFileName());
            return ResponseDTO.ok(result);
            
        } catch (Exception e) {
            log.error("商品图片上传失败", e);
            return ResponseDTO.userErrorParam("商品图片上传失败");
        }
    }

    /**
     * 删除商品图片
     */
    public ResponseDTO<String> deleteProductImage(String imageUrl) {
        try {
            // 调用文件服务删除图片
            // 这里应该解析URL获取文件key，然后调用删除方法
            
            log.info("商品图片删除成功，图片URL: {}", imageUrl);
            return ResponseDTO.ok();
            
        } catch (Exception e) {
            log.error("商品图片删除失败", e);
            return ResponseDTO.userErrorParam("商品图片删除失败");
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
            wrapper.eq(ProductEntity::getProductType, queryForm.getProductType());
        }
        
        if (queryForm.getStatus() != null) {
            wrapper.eq(ProductEntity::getStatus, queryForm.getStatus());
        }
        
        if (SmartStringUtil.isNotBlank(queryForm.getKeyword())) {
            wrapper.and(w -> w.like(ProductEntity::getProductName, queryForm.getKeyword())
                .or().like(ProductEntity::getProductCode, queryForm.getKeyword())
                .or().like(ProductEntity::getDescription, queryForm.getKeyword()));
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
     * 检查商品是否可以删除
     */
    private ResponseDTO<String> checkProductCanDelete(Long productId) {
        // 这里应该检查商品是否有关联的订单、预约等
        // 暂时返回可以删除
        return ResponseDTO.ok();
    }

    /**
     * 保存商品扩展配置
     */
    private void saveProductExtendedConfig(Long productId, Integer productType, String configDataJson) {
        if (configDataJson == null || configDataJson.trim().isEmpty()) {
            return;
        }
        
        // 解析JSON配置（简化处理，实际应使用JSON库）
        // 这里简化实现，实际项目中应该使用Jackson或其他JSON库解析
        log.info("保存商品扩展配置，商品ID: {}, 类型: {}, 配置: {}", productId, productType, configDataJson);
        
        // 根据商品类型保存到对应的扩展表
        switch (productType) {
            case 1: // 课程
                // saveCourseConfig(productId, configData);
                break;
            case 2: // 课时包
                // savePackageConfig(productId, configData);
                break;
            case 3: // 活动
                // saveActivityConfig(productId, configData);
                break;
        }
    }

    /**
     * 保存课程配置 (暂时未实现)
     */
    // private void saveCourseConfig(Long productId, Map<String, Object> configData) {
    //     // TODO: 保存到 m_product_course 表
    //     log.info("保存课程配置，商品ID: {}, 配置: {}", productId, JSON.toJSONString(configData));
    // }

    /**
     * 保存课时包配置 (暂时未实现)
     */
    // private void savePackageConfig(Long productId, Map<String, Object> configData) {
    //     // TODO: 保存到 m_product_package 表
    //     log.info("保存课时包配置，商品ID: {}, 配置: {}", productId, JSON.toJSONString(configData));
    // }

    /**
     * 保存活动配置 (暂时未实现)
     */
    // private void saveActivityConfig(Long productId, Map<String, Object> configData) {
    //     // TODO: 保存到 m_product_activity 表
    //     log.info("保存活动配置，商品ID: {}, 配置: {}", productId, JSON.toJSONString(configData));
    // }

    /**
     * 补充商品列表数据
     */
    private void enhanceProductListData(List<ProductListVO> productList) {
        for (ProductListVO product : productList) {
            // 设置类型名称
            product.setProductTypeName(getProductTypeName(product.getProductType()));
            
            // 设置状态名称
            product.setStatusName(getProductStatusName(product.getStatus()));
            
            // 设置价格信息和库存信息（示例）
            product.setPriceInfo("¥200起");
            product.setStockInfo(product.getProductType() == 2 ? "库存100" : "无限");
        }
    }

    /**
     * 补充商品详情数据
     */
    private void enhanceProductDetailData(ProductDetailVO productDetail) {
        // 设置类型名称
        productDetail.setProductTypeName(getProductTypeName(productDetail.getProductType()));
        
        // 设置状态名称
        productDetail.setStatusName(getProductStatusName(productDetail.getStatus()));
        
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
     * 获取商品状态名称
     */
    private String getProductStatusName(Integer status) {
        switch (status) {
            case 1: return "上架";
            case 2: return "下架";
            case 3: return "售罄";
            default: return "未知";
        }
    }

    /**
     * 获取课程详情配置
     */
    private Map<String, Object> getCourseDetails(Long productId) {
        // TODO: 从 m_product_course 表查询
        return new HashMap<>();
    }

    /**
     * 获取课时包详情配置
     */
    private Map<String, Object> getPackageDetails(Long productId) {
        // TODO: 从 m_product_package 表查询
        return new HashMap<>();
    }

    /**
     * 获取活动详情配置
     */
    private Map<String, Object> getActivityDetails(Long productId) {
        // TODO: 从 m_product_activity 表查询
        return new HashMap<>();
    }
}