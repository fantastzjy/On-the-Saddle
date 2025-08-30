package net.lab1024.sa.admin.module.business.product.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.product.domain.vo.CoachPriceConfigVO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教练价格配置表单项
 * 用于前端提交教练价格配置数据
 *
 * @Author 1024创新实验室
 * @Date 2024-08-30
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "教练价格配置表单项")
public class CoachPriceFormItem {
    
    @NotNull(message = "教练ID不能为空")
    @Schema(description = "教练ID")
    private Long coachId;
    
    @Schema(description = "教练姓名")
    private String coachName;
    
    @Valid
    @Schema(description = "价格配置，Key为人数，Value为价格")
    private Map<Integer, BigDecimal> prices = new HashMap<>();
    
    /**
     * 转换为CoachPriceInfo对象
     */
    public CoachPriceConfigVO.CoachPriceInfo toCoachPriceInfo() {
        CoachPriceConfigVO.CoachPriceInfo info = new CoachPriceConfigVO.CoachPriceInfo();
        info.setCoachId(this.coachId);
        info.setCoachName(this.coachName);
        
        Map<String, BigDecimal> pricesMap = new HashMap<>();
        if (this.prices != null) {
            this.prices.forEach((count, price) -> {
                if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                    pricesMap.put(String.valueOf(count), price);
                }
            });
        }
        info.setPrices(pricesMap);
        
        return info;
    }
    
    /**
     * 从CoachPriceInfo对象创建
     */
    public static CoachPriceFormItem fromCoachPriceInfo(CoachPriceConfigVO.CoachPriceInfo info) {
        CoachPriceFormItem formItem = new CoachPriceFormItem();
        formItem.setCoachId(info.getCoachId());
        formItem.setCoachName(info.getCoachName());
        
        Map<Integer, BigDecimal> prices = new HashMap<>();
        if (info.getPrices() != null) {
            info.getPrices().forEach((countStr, price) -> {
                try {
                    Integer count = Integer.valueOf(countStr);
                    prices.put(count, price);
                } catch (NumberFormatException e) {
                    // 忽略无效的人数配置
                }
            });
        }
        formItem.setPrices(prices);
        
        return formItem;
    }
    
    /**
     * 验证价格配置是否完整
     */
    public boolean isConfigComplete() {
        if (prices == null || prices.isEmpty()) {
            return false;
        }
        
        for (int count = 2; count <= 6; count++) {
            BigDecimal price = prices.get(count);
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
        if (prices == null) {
            return 0;
        }
        
        int count = 0;
        for (int i = 2; i <= 6; i++) {
            BigDecimal price = prices.get(i);
            if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 设置指定人数的价格
     */
    public void setPrice(int participantCount, BigDecimal price) {
        if (prices == null) {
            prices = new HashMap<>();
        }
        
        if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
            prices.put(participantCount, price);
        } else {
            prices.remove(participantCount);
        }
    }
    
    /**
     * 获取指定人数的价格
     */
    public BigDecimal getPrice(int participantCount) {
        return prices != null ? prices.get(participantCount) : null;
    }
    
    /**
     * 批量设置所有人数的价格为相同值
     */
    public void setAllPrices(BigDecimal price) {
        if (prices == null) {
            prices = new HashMap<>();
        }
        
        if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
            for (int count = 2; count <= 6; count++) {
                prices.put(count, price);
            }
        }
    }
    
    /**
     * 使用递减定价模式设置价格
     */
    public void setPricesWithDecrement(BigDecimal startPrice, BigDecimal decrement) {
        if (prices == null) {
            prices = new HashMap<>();
        }
        
        if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) > 0 &&
            decrement != null && decrement.compareTo(BigDecimal.ZERO) >= 0) {
            
            for (int count = 2; count <= 6; count++) {
                BigDecimal price = startPrice.subtract(decrement.multiply(BigDecimal.valueOf(count - 2)));
                // 确保价格不低于10元
                if (price.compareTo(BigDecimal.TEN) >= 0) {
                    prices.put(count, price);
                }
            }
        }
    }
    
    /**
     * 清空所有价格配置
     */
    public void clearPrices() {
        if (prices != null) {
            prices.clear();
        }
    }
    
    /**
     * 复制另一个教练的价格配置
     */
    public void copyPricesFrom(CoachPriceFormItem source) {
        if (source != null && source.getPrices() != null) {
            if (prices == null) {
                prices = new HashMap<>();
            }
            prices.putAll(source.getPrices());
        }
    }
}