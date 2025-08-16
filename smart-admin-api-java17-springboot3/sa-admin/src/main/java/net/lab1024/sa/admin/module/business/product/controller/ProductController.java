package net.lab1024.sa.admin.module.business.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductAddForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductQueryForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductUpdateForm;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductDetailVO;
import net.lab1024.sa.admin.module.business.product.domain.vo.ProductListVO;
import net.lab1024.sa.admin.module.business.product.service.DynamicFormConfigService;
import net.lab1024.sa.admin.module.business.product.service.PriceCalculationService;
import net.lab1024.sa.admin.module.business.product.service.ProductService;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 商品管理Controller
 * 对应模块2.1和2.2 - 简化版商品管理
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Tag(name = "商品管理")
@RestController
@RequestMapping("/api/admin/business/product")
public class ProductController extends SupportBaseController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private DynamicFormConfigService dynamicFormConfigService;
    
    @Autowired
    private PriceCalculationService priceCalculationService;

    // ========================================
    // 商品列表功能 (对应PROD_BE_001: 商品基础CRUD接口)
    // ========================================

    @Operation(summary = "商品列表查询")
    @PostMapping("/query")
    public ResponseDTO<PageResult<ProductListVO>> queryProductList(@RequestBody @Valid ProductQueryForm queryForm) {
        return productService.queryProductList(queryForm);
    }

    @Operation(summary = "查看商品详情")
    @GetMapping("/{productId}")
    public ResponseDTO<ProductDetailVO> getProductDetail(@PathVariable Long productId) {
        return productService.getProductDetail(productId);
    }

    @Operation(summary = "新增商品")
    @PostMapping("/add")
    public ResponseDTO<String> addProduct(@RequestBody @Valid ProductAddForm addForm) {
        return productService.addProduct(addForm, SmartRequestUtil.getRequestUserId());
    }

    @Operation(summary = "编辑商品")
    @PostMapping("/update")
    public ResponseDTO<String> updateProduct(@RequestBody @Valid ProductUpdateForm updateForm) {
        return productService.updateProduct(updateForm, SmartRequestUtil.getRequestUserId());
    }

    @Operation(summary = "删除商品")
    @GetMapping("/delete/{productId}")
    public ResponseDTO<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId, SmartRequestUtil.getRequestUserId());
    }

    @Operation(summary = "批量删除商品")
    @PostMapping("/batch/delete")
    public ResponseDTO<String> batchDeleteProducts(@RequestBody List<Long> productIds) {
        return productService.batchDeleteProducts(productIds, SmartRequestUtil.getRequestUserId());
    }

    @Operation(summary = "更新商品状态")
    @PostMapping("/status/update")
    public ResponseDTO<String> updateProductStatus(@RequestParam Long productId, @RequestParam Integer status) {
        return productService.updateProductStatus(productId, status, SmartRequestUtil.getRequestUserId());
    }

    @Operation(summary = "批量更新商品状态")
    @PostMapping("/batch/status")
    public ResponseDTO<String> batchUpdateProductStatus(@RequestBody List<Long> productIds, @RequestParam Integer status) {
        return productService.batchUpdateProductStatus(productIds, status, SmartRequestUtil.getRequestUserId());
    }

    // ========================================
    // 商品搜索功能 (对应PROD_BE_006: 商品搜索筛选接口)
    // ========================================

    @Operation(summary = "商品搜索")
    @PostMapping("/search")
    public ResponseDTO<PageResult<ProductListVO>> searchProducts(@RequestBody @Valid ProductQueryForm queryForm) {
        return productService.searchProducts(queryForm);
    }

    @Operation(summary = "获取商品类型选项")
    @GetMapping("/types")
    public ResponseDTO<List<Map<String, Object>>> getProductTypes() {
        return productService.getProductTypes();
    }

    @Operation(summary = "获取商品状态选项")
    @GetMapping("/status/options")
    public ResponseDTO<List<Map<String, Object>>> getProductStatusOptions() {
        return productService.getProductStatusOptions();
    }

    // ========================================
    // 动态表单配置功能 (对应PROD_BE_002: 商品类型动态配置接口)
    // ========================================

    @Operation(summary = "获取商品表单配置")
    @GetMapping("/form/config")
    public ResponseDTO<Map<String, Object>> getFormConfig(@RequestParam Integer productType) {
        return dynamicFormConfigService.getFormConfig(productType);
    }

    @Operation(summary = "验证表单数据")
    @PostMapping("/form/validate")
    public ResponseDTO<String> validateFormData(@RequestParam Integer productType, @RequestBody Map<String, Object> formData) {
        return dynamicFormConfigService.validateFormData(productType, formData);
    }

    // ========================================
    // 价格计算功能 (对应PROD_BE_003: 商品价格计算服务)
    // ========================================

    @Operation(summary = "计算商品价格")
    @PostMapping("/price/calculate")
    public ResponseDTO<Map<String, Object>> calculatePrice(@RequestBody Map<String, Object> priceParams) {
        return priceCalculationService.calculatePrice(priceParams);
    }

    @Operation(summary = "获取价格明细")
    @PostMapping("/price/detail")
    public ResponseDTO<Map<String, Object>> getPriceDetail(@RequestBody Map<String, Object> priceParams) {
        return priceCalculationService.getPriceDetail(priceParams);
    }

    // ========================================
    // 库存管理功能 (对应PROD_BE_004: 商品库存管理服务)
    // ========================================

    @Operation(summary = "查询商品库存")
    @GetMapping("/stock/{productId}")
    public ResponseDTO<Map<String, Object>> getProductStock(@PathVariable Long productId) {
        return productService.getProductStock(productId);
    }

    @Operation(summary = "更新商品库存")
    @PostMapping("/stock/update")
    public ResponseDTO<String> updateProductStock(@RequestParam Long productId, 
                                                 @RequestParam Integer quantity, 
                                                 @RequestParam String reason) {
        return productService.updateProductStock(productId, quantity, reason, SmartRequestUtil.getRequestUserId());
    }

    // ========================================
    // 图片上传功能 (对应PROD_BE_005: 商品图片上传处理)
    // ========================================

    @Operation(summary = "上传商品图片")
    @PostMapping("/upload/image")
    public ResponseDTO<Map<String, Object>> uploadProductImage(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        return productService.uploadProductImage(file);
    }

    @Operation(summary = "删除商品图片")
    @PostMapping("/delete/image")
    public ResponseDTO<String> deleteProductImage(@RequestParam String imageUrl) {
        return productService.deleteProductImage(imageUrl);
    }
}