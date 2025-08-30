package net.lab1024.sa.admin.module.business.product.domain.vo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.util.SmartStringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 教练价格配置VO
 * 用于处理小组课教练个性化定价
 *
 * @Author 1024创新实验室
 * @Date 2024-08-30
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Slf4j
@Schema(description = "教练价格配置VO")
public class CoachPriceConfigVO {
    
    @Schema(description = "教练价格配置映射，Key为教练ID")
    private Map<Long, CoachPriceInfo> coachPricing = new HashMap<>();
    
    @Data
    @Schema(description = "教练价格信息")
    public static class CoachPriceInfo {
        @Schema(description = "教练ID")
        private Long coachId;
        
        @Schema(description = "教练姓名")
        private String coachName;
        
        @Schema(description = "人数价格映射，Key为人数字符串，Value为课时费")
        private Map<String, BigDecimal> prices = new HashMap<>();
        
        /**
         * 获取指定人数的价格
         */
        public BigDecimal getPrice(int participantCount) {
            return prices.get(String.valueOf(participantCount));
        }
        
        /**
         * 设置指定人数的价格
         */
        public void setPrice(int participantCount, BigDecimal price) {
            prices.put(String.valueOf(participantCount), price);
        }
        
        /**
         * 检查是否有完整的价格配置（2-6人）
         */
        public boolean isConfigComplete() {
            for (int count = 2; count <= 6; count++) {
                BigDecimal price = getPrice(count);
                if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * 获取已配置的人数数量
         */
        public int getConfiguredCount() {
            int count = 0;
            for (int i = 2; i <= 6; i++) {
                BigDecimal price = getPrice(i);
                if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                    count++;
                }
            }
            return count;
        }
    }
    
    /**
     * 从JSON字符串解析配置
     */
    public static CoachPriceConfigVO fromJson(String json) {
        if (SmartStringUtil.isBlank(json)) {
            return new CoachPriceConfigVO();
        }
        
        try {
            JSONObject configJson = JSON.parseObject(json);
            JSONObject coachPricingJson = configJson.getJSONObject("coachPricing");
            
            CoachPriceConfigVO config = new CoachPriceConfigVO();
            Map<Long, CoachPriceInfo> coachPricing = new HashMap<>();
            
            if (coachPricingJson != null) {
                for (String coachIdStr : coachPricingJson.keySet()) {
                    try {
                        JSONObject coachInfoJson = coachPricingJson.getJSONObject(coachIdStr);
                        
                        CoachPriceInfo priceInfo = new CoachPriceInfo();
                        priceInfo.setCoachId(coachInfoJson.getLong("coachId"));
                        priceInfo.setCoachName(coachInfoJson.getString("coachName"));
                        
                        // 解析价格映射
                        JSONObject pricesJson = coachInfoJson.getJSONObject("prices");
                        Map<String, BigDecimal> prices = new HashMap<>();
                        if (pricesJson != null) {
                            for (String participantCount : pricesJson.keySet()) {
                                try {
                                    BigDecimal price = pricesJson.getBigDecimal(participantCount);
                                    if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                                        prices.put(participantCount, price);
                                    }
                                } catch (Exception e) {
                                    log.warn("解析价格失败，跳过: participantCount={}, price={}", 
                                        participantCount, pricesJson.get(participantCount));
                                }
                            }
                        }
                        priceInfo.setPrices(prices);
                        
                        coachPricing.put(priceInfo.getCoachId(), priceInfo);
                    } catch (Exception e) {
                        log.warn("解析教练配置失败，跳过: coachId={}", coachIdStr, e);
                    }
                }
            }
            
            config.setCoachPricing(coachPricing);
            return config;
            
        } catch (Exception e) {
            log.error("解析教练价格配置失败: {}", json, e);
            return new CoachPriceConfigVO();
        }
    }
    
    /**
     * 转换为JSON字符串
     */
    public String toJson() {
        try {
            Map<String, Object> configMap = new HashMap<>();
            
            Map<String, Object> coachPricingMap = new HashMap<>();
            for (Map.Entry<Long, CoachPriceInfo> entry : this.coachPricing.entrySet()) {
                CoachPriceInfo priceInfo = entry.getValue();
                
                Map<String, Object> coachInfoMap = new HashMap<>();
                coachInfoMap.put("coachId", priceInfo.getCoachId());
                coachInfoMap.put("coachName", priceInfo.getCoachName());
                coachInfoMap.put("prices", priceInfo.getPrices());
                
                coachPricingMap.put(String.valueOf(entry.getKey()), coachInfoMap);
            }
            
            configMap.put("coachPricing", coachPricingMap);
            return JSON.toJSONString(configMap);
        } catch (Exception e) {
            log.error("转换教练价格配置为JSON失败", e);
            return "{}";
        }
    }
    
    /**
     * 获取指定教练和人数的价格
     */
    public BigDecimal getPrice(Long coachId, int participantCount) {
        CoachPriceInfo coachInfo = coachPricing.get(coachId);
        if (coachInfo != null) {
            return coachInfo.getPrice(participantCount);
        }
        return null;
    }
    
    /**
     * 检查教练是否有价格配置
     */
    public boolean hasCoachConfig(Long coachId) {
        return coachPricing.containsKey(coachId);
    }
    
    /**
     * 获取教练的所有价格配置
     */
    public Map<Integer, BigDecimal> getCoachAllPrices(Long coachId) {
        CoachPriceInfo coachInfo = coachPricing.get(coachId);
        if (coachInfo != null) {
            Map<Integer, BigDecimal> result = new HashMap<>();
            coachInfo.getPrices().forEach((countStr, price) -> {
                try {
                    result.put(Integer.valueOf(countStr), price);
                } catch (NumberFormatException e) {
                    log.warn("忽略无效的人数配置: {}", countStr);
                }
            });
            return result;
        }
        return new HashMap<>();
    }
    
    /**
     * 添加或更新教练价格配置
     */
    public void setCoachPricing(Long coachId, String coachName, Map<Integer, BigDecimal> prices) {
        CoachPriceInfo priceInfo = new CoachPriceInfo();
        priceInfo.setCoachId(coachId);
        priceInfo.setCoachName(coachName);
        
        Map<String, BigDecimal> pricesMap = new HashMap<>();
        prices.forEach((count, price) -> {
            if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                pricesMap.put(String.valueOf(count), price);
            }
        });
        priceInfo.setPrices(pricesMap);
        
        this.coachPricing.put(coachId, priceInfo);
    }
    
    /**
     * 移除教练配置
     */
    public void removeCoachPricing(Long coachId) {
        this.coachPricing.remove(coachId);
    }
    
    /**
     * 获取配置摘要信息
     */
    public String getConfigSummary() {
        if (coachPricing.isEmpty()) {
            return "未配置教练价格";
        }
        
        int totalCoaches = coachPricing.size();
        int completeCoaches = (int) coachPricing.values().stream()
            .mapToLong(info -> info.isConfigComplete() ? 1 : 0)
            .sum();
        
        return String.format("%d位教练已配置价格（%d位完整）", totalCoaches, completeCoaches);
    }
}