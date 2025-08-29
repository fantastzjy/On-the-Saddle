package net.lab1024.sa.admin.module.business.product.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 价格计算引擎服务
 * 支持多种定价策略：基础定价、阶梯定价、动态定价、优惠券等
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class PriceCalculationService {

    @Resource
    private ProductDao productDao;

    /**
     * 计算商品价格
     *
     * @param productId 商品ID
     * @param quantity 数量
     * @param coachId 教练ID（单人课/多人课）
     * @param participantCount 参与人数（多人课）
     * @param memberLevel 会员等级
     * @param couponCode 优惠券代码
     * @return 价格计算结果
     */
    public ResponseDTO<Map<String, Object>> calculatePrice(Long productId, Integer quantity,
                                                          Long coachId, Integer participantCount,
                                                          Integer memberLevel, String couponCode) {
        try {
            ProductEntity product = productDao.selectById(productId);
            if (product == null) {
                return ResponseDTO.userErrorParam("商品不存在");
            }

            Map<String, Object> priceResult = new HashMap<>();
            BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal originalPrice = BigDecimal.ZERO;
            BigDecimal discountAmount = BigDecimal.ZERO;

            // 根据商品类型计算价格
            switch (product.getProductType()) {
                case 1: // 课程
                    Map<String, BigDecimal> coursePrice = calculateCoursePrice(productId, quantity, coachId, participantCount);
                    originalPrice = coursePrice.get("originalPrice");
                    totalPrice = coursePrice.get("totalPrice");
                    break;
                case 2: // 课时包
                    Map<String, BigDecimal> packagePrice = calculatePackagePrice(productId, quantity);
                    originalPrice = packagePrice.get("originalPrice");
                    totalPrice = packagePrice.get("totalPrice");
                    break;
                case 3: // 活动
                    Map<String, BigDecimal> activityPrice = calculateActivityPrice(productId, quantity);
                    originalPrice = activityPrice.get("originalPrice");
                    totalPrice = activityPrice.get("totalPrice");
                    break;
                default:
                    return ResponseDTO.userErrorParam("不支持的商品类型");
            }

            // 应用会员折扣
            if (memberLevel != null && memberLevel > 0) {
                BigDecimal memberDiscount = applyMemberDiscount(totalPrice, memberLevel);
                discountAmount = discountAmount.add(memberDiscount);
                totalPrice = totalPrice.subtract(memberDiscount);
            }

            // 应用优惠券
            if (couponCode != null && !couponCode.trim().isEmpty()) {
                BigDecimal couponDiscount = applyCouponDiscount(totalPrice, couponCode, productId);
                discountAmount = discountAmount.add(couponDiscount);
                totalPrice = totalPrice.subtract(couponDiscount);
            }

            // 应用动态定价策略
            BigDecimal dynamicAdjustment = applyDynamicPricing(productId, LocalDateTime.now());
            totalPrice = totalPrice.add(dynamicAdjustment);

            // 确保价格不为负数
            if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
                totalPrice = BigDecimal.ZERO;
            }

            // 价格四舍五入到两位小数
            totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);

            priceResult.put("productId", productId);
            priceResult.put("quantity", quantity);
            priceResult.put("originalPrice", originalPrice);
            priceResult.put("discountAmount", discountAmount);
            priceResult.put("dynamicAdjustment", dynamicAdjustment);
            priceResult.put("finalPrice", totalPrice);
            priceResult.put("priceBreakdown", generatePriceBreakdown(originalPrice, discountAmount, dynamicAdjustment));

            log.info("价格计算完成，商品ID：{}, 原价：{}, 折扣：{}, 最终价格：{}",
                    productId, originalPrice, discountAmount, totalPrice);

            return ResponseDTO.ok(priceResult);

        } catch (Exception e) {
            log.error("价格计算失败", e);
            return ResponseDTO.userErrorParam("价格计算失败：" + e.getMessage());
        }
    }

    /**
     * 批量价格计算
     *
     * @param items 商品明细列表
     * @param memberLevel 会员等级
     * @param couponCode 优惠券代码
     * @return 批量价格计算结果
     */
    public ResponseDTO<Map<String, Object>> calculateBatchPrice(List<Map<String, Object>> items,
                                                               Integer memberLevel, String couponCode) {
        try {
            BigDecimal totalOriginalPrice = BigDecimal.ZERO;
            BigDecimal totalDiscountAmount = BigDecimal.ZERO;
            BigDecimal totalFinalPrice = BigDecimal.ZERO;

            Map<String, Object> batchResult = new HashMap<>();
            Map<String, Map<String, Object>> itemResults = new HashMap<>();

            for (Map<String, Object> item : items) {
                Long productId = Long.valueOf(item.get("productId").toString());
                Integer quantity = Integer.valueOf(item.get("quantity").toString());
                Long coachId = item.containsKey("coachId") ? Long.valueOf(item.get("coachId").toString()) : null;
                Integer participantCount = item.containsKey("participantCount") ?
                    Integer.valueOf(item.get("participantCount").toString()) : null;

                ResponseDTO<Map<String, Object>> itemPrice = calculatePrice(
                    productId, quantity, coachId, participantCount, memberLevel, null); // 单品不应用优惠券

                if (itemPrice.getOk()) {
                    Map<String, Object> priceInfo = itemPrice.getData();
                    itemResults.put(productId.toString(), priceInfo);

                    totalOriginalPrice = totalOriginalPrice.add((BigDecimal) priceInfo.get("originalPrice"));
                    totalDiscountAmount = totalDiscountAmount.add((BigDecimal) priceInfo.get("discountAmount"));
                    totalFinalPrice = totalFinalPrice.add((BigDecimal) priceInfo.get("finalPrice"));
                }
            }

            // 应用整单优惠券
            BigDecimal orderCouponDiscount = BigDecimal.ZERO;
            if (couponCode != null && !couponCode.trim().isEmpty()) {
                orderCouponDiscount = applyCouponDiscount(totalFinalPrice, couponCode, null);
                totalDiscountAmount = totalDiscountAmount.add(orderCouponDiscount);
                totalFinalPrice = totalFinalPrice.subtract(orderCouponDiscount);
            }

            batchResult.put("itemResults", itemResults);
            batchResult.put("totalOriginalPrice", totalOriginalPrice);
            batchResult.put("totalDiscountAmount", totalDiscountAmount);
            batchResult.put("orderCouponDiscount", orderCouponDiscount);
            batchResult.put("totalFinalPrice", totalFinalPrice.setScale(2, RoundingMode.HALF_UP));
            batchResult.put("itemCount", items.size());

            return ResponseDTO.ok(batchResult);

        } catch (Exception e) {
            log.error("批量价格计算失败", e);
            return ResponseDTO.userErrorParam("批量价格计算失败：" + e.getMessage());
        }
    }

    /**
     * 获取价格预估
     * 快速计算价格范围，用于前端展示
     *
     * @param productId 商品ID
     * @return 价格预估信息
     */
    public ResponseDTO<Map<String, Object>> getPriceEstimate(Long productId) {
        try {
            ProductEntity product = productDao.selectById(productId);
            if (product == null) {
                return ResponseDTO.userErrorParam("商品不存在");
            }

            Map<String, Object> estimate = new HashMap<>();

            switch (product.getProductType()) {
                case 1: // 课程
                    estimate = getCourseEstimate(productId);
                    break;
                case 2: // 课时包
                    estimate = getPackageEstimate(productId);
                    break;
                case 3: // 活动
                    estimate = getActivityEstimate(productId);
                    break;
            }

            return ResponseDTO.ok(estimate);

        } catch (Exception e) {
            log.error("获取价格预估失败", e);
            return ResponseDTO.userErrorParam("获取价格预估失败");
        }
    }

    /**
     * 计算课程价格
     * 严格按照数据库设计：base_price = coach_fee + horse_fee (计算字段)
     */
    private Map<String, BigDecimal> calculateCoursePrice(Long productId, Integer quantity,
                                                        Long coachId, Integer participantCount) {
        Map<String, BigDecimal> result = new HashMap<>();

        // 获取课程配置（从dynamicConfig或数据库）
        Map<String, Object> courseConfig = getCourseConfig(productId);
        BigDecimal coachFee = (BigDecimal) courseConfig.get("coachFee");
        BigDecimal horseFee = (BigDecimal) courseConfig.get("horseFee");
        Integer classType = (Integer) courseConfig.get("classType");

        // 严格按照数据库设计计算 base_price = coach_fee + horse_fee
        BigDecimal basePrice = coachFee.add(horseFee);
        BigDecimal unitPrice = basePrice;

        if (classType == 2 && participantCount != null && participantCount > 1) {
            // 小组课价格计算 - 使用 multi_price_config JSON配置
            String multiPriceConfig = (String) courseConfig.get("multiPriceConfig");
            unitPrice = calculateMultiPersonPriceFromConfig(multiPriceConfig, coachId, participantCount, basePrice);
        }

        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        result.put("originalPrice", totalPrice);
        result.put("totalPrice", totalPrice);
        result.put("basePrice", basePrice); // 严格对应数据库计算字段
        return result;
    }

    /**
     * 计算课时包价格
     */
    private Map<String, BigDecimal> calculatePackagePrice(Long productId, Integer quantity) {
        Map<String, BigDecimal> result = new HashMap<>();

        // 获取课时包配置
        Map<String, Object> packageConfig = getPackageConfig(productId);
        BigDecimal packagePrice = (BigDecimal) packageConfig.get("price");

        // 应用数量折扣
        BigDecimal totalPrice = packagePrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountedPrice = applyQuantityDiscount(totalPrice, quantity);

        result.put("originalPrice", totalPrice);
        result.put("totalPrice", discountedPrice);
        return result;
    }

    /**
     * 计算活动价格
     */
    private Map<String, BigDecimal> calculateActivityPrice(Long productId, Integer quantity) {
        Map<String, BigDecimal> result = new HashMap<>();

        // 获取活动配置
        Map<String, Object> activityConfig = getActivityConfig(productId);
        BigDecimal activityPrice = (BigDecimal) activityConfig.get("price");

        BigDecimal totalPrice = activityPrice.multiply(BigDecimal.valueOf(quantity));

        result.put("originalPrice", totalPrice);
        result.put("totalPrice", totalPrice);
        return result;
    }

    /**
     * 根据小组课价格配置计算价格
     * 严格按照数据库设计：multi_price_config JSON格式
     * 格式：{"coaches":[{"coach_id":1,"prices":{"2":150.00,"3":200.00,"4":240.00}}]}
     */
    private BigDecimal calculateMultiPersonPriceFromConfig(String multiPriceConfig, Long coachId,
                                                          Integer participantCount, BigDecimal basePrice) {
        try {
            if (multiPriceConfig == null || multiPriceConfig.trim().isEmpty()) {
                // 如果没有配置，按人头平摊基础价格
                return basePrice.multiply(BigDecimal.valueOf(0.8)); // 多人折扣
            }

            // 解析JSON配置（这里简化处理，实际应使用JSON库）
            // 根据参与人数返回对应价格
            switch (participantCount) {
                case 2:
                    return BigDecimal.valueOf(150.00);
                case 3:
                    return BigDecimal.valueOf(200.00);
                case 4:
                    return BigDecimal.valueOf(240.00);
                case 5:
                    return BigDecimal.valueOf(280.00);
                default:
                    return basePrice.multiply(BigDecimal.valueOf(0.7));
            }
        } catch (Exception e) {
            log.warn("解析多人课价格配置失败，使用默认折扣", e);
            return basePrice.multiply(BigDecimal.valueOf(0.8));
        }
    }

    /**
     * 计算小组课价格（旧版本兼容方法）
     */
    private BigDecimal calculateMultiPersonPrice(Long productId, Long coachId, Integer participantCount) {
        // 获取小组课价格配置
        Map<String, Object> multiPriceConfig = getMultiPersonPriceConfig(productId, coachId);

        if (multiPriceConfig.containsKey(participantCount.toString())) {
            return (BigDecimal) multiPriceConfig.get(participantCount.toString());
        }

        // 如果没有具体配置，按人头平摊
        BigDecimal basePrice = (BigDecimal) multiPriceConfig.get("basePrice");
        BigDecimal discountRate = BigDecimal.valueOf(0.8); // 多人折扣

        return basePrice.multiply(discountRate);
    }

    /**
     * 应用数量折扣
     */
    private BigDecimal applyQuantityDiscount(BigDecimal totalPrice, Integer quantity) {
        if (quantity >= 10) {
            return totalPrice.multiply(BigDecimal.valueOf(0.85)); // 10个以上85折
        } else if (quantity >= 5) {
            return totalPrice.multiply(BigDecimal.valueOf(0.9)); // 5个以上9折
        }
        return totalPrice;
    }

    /**
     * 应用会员折扣
     */
    private BigDecimal applyMemberDiscount(BigDecimal price, Integer memberLevel) {
        BigDecimal discountRate = BigDecimal.ZERO;

        switch (memberLevel) {
            case 1: // 铜牌会员
                discountRate = BigDecimal.valueOf(0.05);
                break;
            case 2: // 银牌会员
                discountRate = BigDecimal.valueOf(0.08);
                break;
            case 3: // 金牌会员
                discountRate = BigDecimal.valueOf(0.12);
                break;
            case 4: // 钻石会员
                discountRate = BigDecimal.valueOf(0.15);
                break;
        }

        return price.multiply(discountRate);
    }

    /**
     * 应用优惠券折扣
     */
    private BigDecimal applyCouponDiscount(BigDecimal price, String couponCode, Long productId) {
        // 简化实现：根据优惠券代码应用不同折扣
        // 实际应该查询优惠券表获取具体规则

        if ("WELCOME10".equals(couponCode)) {
            return price.multiply(BigDecimal.valueOf(0.1)); // 10%折扣
        } else if ("SUMMER20".equals(couponCode)) {
            return price.multiply(BigDecimal.valueOf(0.2)); // 20%折扣
        } else if ("VIP50".equals(couponCode)) {
            BigDecimal discount = BigDecimal.valueOf(50); // 固定50元折扣
            return discount.min(price); // 不超过商品价格
        }

        return BigDecimal.ZERO;
    }

    /**
     * 应用动态定价策略
     */
    private BigDecimal applyDynamicPricing(Long productId, LocalDateTime bookingTime) {
        // 简化实现：根据时间段调整价格
        int hour = bookingTime.getHour();

        if (hour >= 18 && hour <= 21) {
            // 晚间黄金时段：+10%
            return getCurrentPrice(productId).multiply(BigDecimal.valueOf(0.1));
        } else if (hour >= 9 && hour <= 11) {
            // 上午优质时段：+5%
            return getCurrentPrice(productId).multiply(BigDecimal.valueOf(0.05));
        }

        return BigDecimal.ZERO;
    }

    /**
     * 生成价格明细
     */
    private Map<String, Object> generatePriceBreakdown(BigDecimal originalPrice,
                                                      BigDecimal discountAmount,
                                                      BigDecimal dynamicAdjustment) {
        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("originalPrice", originalPrice);
        breakdown.put("memberDiscount", discountAmount);
        breakdown.put("couponDiscount", BigDecimal.ZERO);
        breakdown.put("dynamicAdjustment", dynamicAdjustment);
        breakdown.put("finalPrice", originalPrice.subtract(discountAmount).add(dynamicAdjustment));
        return breakdown;
    }

    /**
     * 获取课程价格预估
     */
    private Map<String, Object> getCourseEstimate(Long productId) {
        Map<String, Object> estimate = new HashMap<>();
        Map<String, Object> courseConfig = getCourseConfig(productId);

        BigDecimal basePrice = ((BigDecimal) courseConfig.get("coachFee"))
            .add((BigDecimal) courseConfig.get("horseFee"));

        estimate.put("minPrice", basePrice.multiply(BigDecimal.valueOf(0.8))); // 最低8折
        estimate.put("maxPrice", basePrice.multiply(BigDecimal.valueOf(1.1))); // 最高涨价10%
        estimate.put("basePrice", basePrice);
        estimate.put("priceUnit", "元/节");

        return estimate;
    }

    /**
     * 获取课时包价格预估
     */
    private Map<String, Object> getPackageEstimate(Long productId) {
        Map<String, Object> estimate = new HashMap<>();
        Map<String, Object> packageConfig = getPackageConfig(productId);

        BigDecimal packagePrice = (BigDecimal) packageConfig.get("price");

        estimate.put("minPrice", packagePrice.multiply(BigDecimal.valueOf(0.85))); // 量大优惠
        estimate.put("maxPrice", packagePrice);
        estimate.put("basePrice", packagePrice);
        estimate.put("priceUnit", "元/包");

        return estimate;
    }

    /**
     * 获取活动价格预估
     */
    private Map<String, Object> getActivityEstimate(Long productId) {
        Map<String, Object> estimate = new HashMap<>();
        Map<String, Object> activityConfig = getActivityConfig(productId);

        BigDecimal activityPrice = (BigDecimal) activityConfig.get("price");

        estimate.put("minPrice", activityPrice.multiply(BigDecimal.valueOf(0.9))); // 早鸟优惠
        estimate.put("maxPrice", activityPrice);
        estimate.put("basePrice", activityPrice);
        estimate.put("priceUnit", "元/人");

        return estimate;
    }

    // 以下为辅助方法，实际应该从数据库获取配置

    /**
     * 获取课程配置 - 严格按照数据库表 m_product_course 字段
     */
    private Map<String, Object> getCourseConfig(Long productId) {
        Map<String, Object> config = new HashMap<>();
        // 模拟数据库查询结果，实际应从数据库获取
        config.put("classType", 1); // 1-单人课 2-多人课
        config.put("durationMinutes", 60);
        config.put("durationPeriods", 1.0);
        config.put("maxStudents", 1);
        config.put("coachFee", BigDecimal.valueOf(200));
        config.put("horseFee", BigDecimal.valueOf(100));
        // base_price = coach_fee + horse_fee (数据库计算字段，不需要手动设置)
        config.put("multiPriceConfig", null); // 多人课才有此配置
        return config;
    }

    private Map<String, Object> getPackageConfig(Long productId) {
        Map<String, Object> config = new HashMap<>();
        config.put("price", BigDecimal.valueOf(2000));
        config.put("totalSessions", 10);
        return config;
    }

    private Map<String, Object> getActivityConfig(Long productId) {
        Map<String, Object> config = new HashMap<>();
        config.put("price", BigDecimal.valueOf(150));
        return config;
    }

    private Map<String, Object> getMultiPersonPriceConfig(Long productId, Long coachId) {
        Map<String, Object> config = new HashMap<>();
        config.put("basePrice", BigDecimal.valueOf(300));
        config.put("2", BigDecimal.valueOf(180)); // 2人课每人180
        config.put("3", BigDecimal.valueOf(120)); // 3人课每人120
        config.put("4", BigDecimal.valueOf(100)); // 4人课每人100
        return config;
    }

    private BigDecimal getCurrentPrice(Long productId) {
        return BigDecimal.valueOf(300); // 简化实现
    }

    /**
     * 重载方法：使用Map参数计算价格 - 严格按照数据库字段处理
     * 用于Controller调用，支持dynamicConfig传入
     */
    public ResponseDTO<Map<String, Object>> calculatePrice(Map<String, Object> priceParams) {
        try {
            // 从参数中获取商品类型
            Integer productType = Integer.valueOf(priceParams.get("productType").toString());

            // 从dynamicConfig中获取数据库字段
            @SuppressWarnings("unchecked")
            Map<String, Object> dynamicConfig = (Map<String, Object>) priceParams.get("dynamicConfig");

            if (dynamicConfig == null) {
                dynamicConfig = new HashMap<>();
            }

            Map<String, Object> priceResult = new HashMap<>();
            BigDecimal basePrice = BigDecimal.ZERO;
            BigDecimal finalPrice = BigDecimal.ZERO;

            if (productType == 1) {
                // 课程价格计算 - 严格按照数据库字段
                BigDecimal coachFee = extractBigDecimal(dynamicConfig, "coachFee", BigDecimal.valueOf(200));
                BigDecimal horseFee = extractBigDecimal(dynamicConfig, "horseFee", BigDecimal.valueOf(100));

                // 数据库计算字段：base_price = coach_fee + horse_fee
                basePrice = coachFee.add(horseFee);
                finalPrice = basePrice;

                Integer classType = extractInteger(dynamicConfig, "classType", 1);
                if (classType == 2) {
                    // 小组课处理
                    String multiPriceConfig = (String) dynamicConfig.get("multiPriceConfig");
                    Integer participantCount = extractInteger(priceParams, "participantCount", 2);
                    finalPrice = calculateMultiPersonPriceFromConfig(multiPriceConfig, null, participantCount, basePrice);
                }

                // 构建价格明细
                List<Map<String, Object>> priceDetails = new ArrayList<>();
                priceDetails.add(Map.of("label", "教练费", "value", coachFee, "important", false));
                priceDetails.add(Map.of("label", "马匹费", "value", horseFee, "important", false));
                priceDetails.add(Map.of("label", "基础单价", "value", basePrice, "important", true));

                priceResult.put("priceDetails", priceDetails);

            } else if (productType == 2) {
                // 课时包价格
                finalPrice = extractBigDecimal(dynamicConfig, "price", BigDecimal.valueOf(2000));
                basePrice = finalPrice;

            } else if (productType == 3) {
                // 活动价格
                finalPrice = extractBigDecimal(dynamicConfig, "price", BigDecimal.valueOf(200));
                basePrice = finalPrice;
            }

            priceResult.put("basePrice", basePrice);
            priceResult.put("finalPrice", finalPrice);
            priceResult.put("estimatedPrice", finalPrice);
            priceResult.put("minPrice", finalPrice.multiply(BigDecimal.valueOf(0.9)));
            priceResult.put("maxPrice", finalPrice.multiply(BigDecimal.valueOf(1.1)));

            return ResponseDTO.ok(priceResult);

        } catch (Exception e) {
            log.error("价格计算参数解析失败", e);
            return ResponseDTO.userErrorParam("价格计算参数格式错误");
        }
    }

    /**
     * 安全提取BigDecimal值
     */
    private BigDecimal extractBigDecimal(Map<String, Object> map, String key, BigDecimal defaultValue) {
        Object value = map.get(key);
        if (value == null) return defaultValue;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全提取Integer值
     */
    private Integer extractInteger(Map<String, Object> map, String key, Integer defaultValue) {
        Object value = map.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 获取价格明细
     * 用于Controller调用
     */
    public ResponseDTO<Map<String, Object>> getPriceDetail(Map<String, Object> priceParams) {
        // 复用价格计算逻辑，返回更详细的价格分解信息
        ResponseDTO<Map<String, Object>> priceResult = calculatePrice(priceParams);

        if (!priceResult.getOk()) {
            return priceResult;
        }

        Map<String, Object> priceData = priceResult.getData();
        Map<String, Object> detailResult = new HashMap<>(priceData);

        // 添加更多明细信息
        detailResult.put("calculationTime", LocalDateTime.now());
        detailResult.put("priceBreakdown", buildPriceBreakdown(priceData));
        detailResult.put("discountDetails", buildDiscountDetails(priceData));

        return ResponseDTO.ok(detailResult);
    }

    /**
     * 构建价格分解明细
     */
    private Map<String, Object> buildPriceBreakdown(Map<String, Object> priceData) {
        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("baseAmount", priceData.get("originalPrice"));
        breakdown.put("memberDiscount", priceData.get("memberDiscount"));
        breakdown.put("couponDiscount", priceData.get("couponDiscount"));
        breakdown.put("finalAmount", priceData.get("finalPrice"));
        return breakdown;
    }

    /**
     * 构建折扣详情
     */
    private Map<String, Object> buildDiscountDetails(Map<String, Object> priceData) {
        Map<String, Object> details = new HashMap<>();
        details.put("totalSaved", priceData.get("discountAmount"));
        details.put("memberLevelDiscount", priceData.get("memberDiscount"));
        details.put("appliedCoupon", priceData.get("couponDiscount"));
        return details;
    }
}
