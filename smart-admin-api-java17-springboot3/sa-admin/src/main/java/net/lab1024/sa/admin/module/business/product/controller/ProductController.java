package net.lab1024.sa.admin.module.business.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductAddForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductQueryForm;
import net.lab1024.sa.admin.module.business.product.domain.form.ProductUpdateForm;
import net.lab1024.sa.admin.module.business.product.domain.form.UpdateProductCoachesForm;
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

import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/api/admin/product")
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
    @GetMapping("/detail/{productId}")
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
    @PostMapping("/batchDelete")
    public ResponseDTO<String> batchDeleteProducts(@RequestBody List<Long> productIds) {
        return productService.batchDeleteProducts(productIds, SmartRequestUtil.getRequestUserId());
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


    // ========================================
    // 动态表单配置功能 (对应PROD_BE_002: 商品类型动态配置接口)
    // ========================================

    @Operation(summary = "获取商品表单配置")
    @GetMapping("/form/config/{productType}")
    public ResponseDTO<Map<String, Object>> getFormConfig(@PathVariable Integer productType) {
        return dynamicFormConfigService.getFormConfig(productType);
    }

    @Operation(summary = "获取商品详细表单配置")
    @GetMapping("/form/config/{productType}/{classType}")
    public ResponseDTO<Map<String, Object>> getDetailedFormConfig(@PathVariable Integer productType, @PathVariable Integer classType) {
        return dynamicFormConfigService.getDetailedFormConfig(productType, classType);
    }

    @Operation(summary = "验证表单数据")
    @PostMapping("/form/validate")
    public ResponseDTO<String> validateFormData(@RequestBody Map<String, Object> params) {
        Integer productType = Integer.valueOf(params.get("productType").toString());
        @SuppressWarnings("unchecked")
        Map<String, Object> formData = (Map<String, Object>) params.get("formData");
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
    // 扩展API方法 - 与前端API保持一致
    // ========================================

    @Operation(summary = "批量价格计算")
    @PostMapping("/price/calculateBatch")
    public ResponseDTO<Map<String, Object>> calculateBatchPrice(@RequestBody Map<String, Object> params) {
        return priceCalculationService.calculatePrice(params);
    }

    @Operation(summary = "获取价格预估")
    @GetMapping("/price/estimate/{productId}")
    public ResponseDTO<Map<String, Object>> getPriceEstimate(@PathVariable Long productId) {
        // 简化实现，返回基础价格信息
        Map<String, Object> estimate = new HashMap<>();
        estimate.put("productId", productId);
        estimate.put("basePrice", 200.0);
        estimate.put("minPrice", 180.0);
        estimate.put("maxPrice", 250.0);
        return ResponseDTO.ok(estimate);
    }

    @Operation(summary = "获取商品类型选项")
    @GetMapping("/options/types")
    public ResponseDTO<List<Map<String, Object>>> getProductTypeOptions() {
        return productService.getProductTypes();
    }

    @Operation(summary = "获取上架商品列表")
    @GetMapping("/online/{clubId}")
    public ResponseDTO<List<Map<String, Object>>> getOnlineProductList(@PathVariable Long clubId) {
        // 简化实现，返回基础数据
        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();
        product.put("productId", 1L);
        product.put("productName", "马术基础课程");
        product.put("price", 200.0);
        products.add(product);
        return ResponseDTO.ok(products);
    }

    @Operation(summary = "根据类型查询商品")
    @GetMapping("/type/{productType}/{clubId}")
    public ResponseDTO<List<Map<String, Object>>> getProductsByType(@PathVariable Integer productType, @PathVariable Long clubId) {
        // 简化实现，返回基础数据
        List<Map<String, Object>> products = new ArrayList<>();
        return ResponseDTO.ok(products);
    }

    @Operation(summary = "获取商品关联教练")
    @GetMapping("/coaches/{productId}")
    public ResponseDTO<List<Map<String, Object>>> getProductCoaches(@PathVariable Long productId) {
        return productService.getProductCoaches(productId);
    }

    @Operation(summary = "更新商品教练关联")
    @PostMapping("/coaches/update")
    public ResponseDTO<String> updateProductCoaches(@Valid @RequestBody UpdateProductCoachesForm form) {
        return productService.updateProductCoaches(form.getProductId(), form.getCoachIds(), form.getOperator());
    }

    @Operation(summary = "商品统计信息")
    @GetMapping("/statistics/{clubId}")
    public ResponseDTO<Map<String, Object>> getProductStatistics(@PathVariable Long clubId) {
        // 简化实现，返回基础统计数据
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalProducts", 10);
        statistics.put("onlineProducts", 8);
        statistics.put("offlineProducts", 2);
        return ResponseDTO.ok(statistics);
    }

    @Operation(summary = "销售统计")
    @PostMapping("/statistics/sales")
    public ResponseDTO<Map<String, Object>> getSalesStatistics(@RequestBody Map<String, Object> params) {
        // 简化实现，返回基础销售数据
        Map<String, Object> sales = new HashMap<>();
        sales.put("totalSales", 1000.0);
        sales.put("orderCount", 50);
        return ResponseDTO.ok(sales);
    }
}
