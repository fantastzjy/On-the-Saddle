package net.lab1024.sa.admin.module.business.coach.constant;

/**
 * 教练和骑手证书管理常量（统一结构版）
 * 
 * @Author Claude Code
 * @Date 2025-08-24
 * @Copyright 马术俱乐部管理系统
 */
public class CoachCertificateConstant {
    
    // ===================== 教练证书类别常量 =====================
    public static final int COACH_CATEGORY_SHOW_JUMPING = 1;  // 场地障碍
    public static final int COACH_CATEGORY_DRESSAGE = 2;      // 盛装舞步
    public static final int COACH_CATEGORY_EVENTING = 3;      // 三项赛
    public static final int COACH_CATEGORY_STAR = 4;          // 教练星级
    
    // ===================== 骑手证书类别常量 =====================
    public static final int RIDER_CATEGORY_SHOW_JUMPING = 1; // 场地障碍
    public static final int RIDER_CATEGORY_DRESSAGE = 2;     // 盛装舞步
    public static final int RIDER_CATEGORY_EVENTING = 3;     // 三项赛
    
    // ===================== 教练证书等级常量（统一用星级）=====================
    public static final int COACH_LEVEL_ONE_STAR = 1;      // 一星
    public static final int COACH_LEVEL_TWO_STAR = 2;      // 二星
    public static final int COACH_LEVEL_THREE_STAR = 3;    // 三星
    public static final int COACH_LEVEL_FOUR_STAR = 4;     // 四星
    public static final int COACH_LEVEL_FIVE_STAR = 5;     // 五星
    
    // ===================== 骑手证书等级常量 =====================
    public static final int RIDER_LEVEL_CHU_SAN = 1;       // 初三
    public static final int RIDER_LEVEL_CHU_ER = 2;        // 初二
    public static final int RIDER_LEVEL_CHU_YI = 3;        // 初一
    public static final int RIDER_LEVEL_ZHONG_SAN = 4;     // 中三
    public static final int RIDER_LEVEL_ZHONG_ER = 5;      // 中二
    public static final int RIDER_LEVEL_ZHONG_YI = 6;      // 中一
    public static final int RIDER_LEVEL_GUO_SAN = 7;       // 国三
    public static final int RIDER_LEVEL_GUO_ER = 8;        // 国二
    public static final int RIDER_LEVEL_GUO_YI = 9;        // 国一
    public static final int RIDER_LEVEL_JIAN_JIANG = 10;   // 健将级
    
    /**
     * 获取教练证书类别描述
     * @param category 类别编号
     * @return 类别描述
     */
    public static String getCoachCategoryText(Integer category) {
        if (category == null) return "";
        switch (category) {
            case COACH_CATEGORY_SHOW_JUMPING: return "场地障碍";
            case COACH_CATEGORY_DRESSAGE: return "盛装舞步";
            case COACH_CATEGORY_EVENTING: return "三项赛";
            case COACH_CATEGORY_STAR: return "教练星级";
            default: return "";
        }
    }
    
    /**
     * 获取骑手证书类别描述
     * @param category 类别编号
     * @return 类别描述
     */
    public static String getRiderCategoryText(Integer category) {
        if (category == null) return "";
        switch (category) {
            case RIDER_CATEGORY_SHOW_JUMPING: return "场地障碍";
            case RIDER_CATEGORY_DRESSAGE: return "盛装舞步";
            case RIDER_CATEGORY_EVENTING: return "三项赛";
            default: return "";
        }
    }
    
    /**
     * 获取教练证书等级描述（统一用星级）
     * @param level 等级编号
     * @return 等级描述
     */
    public static String getCoachLevelText(Integer level) {
        if (level == null) return "";
        switch (level) {
            case COACH_LEVEL_ONE_STAR: return "一星";
            case COACH_LEVEL_TWO_STAR: return "二星";
            case COACH_LEVEL_THREE_STAR: return "三星";
            case COACH_LEVEL_FOUR_STAR: return "四星";
            case COACH_LEVEL_FIVE_STAR: return "五星";
            default: return "";
        }
    }
    
    /**
     * 获取骑手证书等级描述
     * @param level 等级编号
     * @return 等级描述
     */
    public static String getRiderLevelText(Integer level) {
        if (level == null) return "";
        switch (level) {
            case RIDER_LEVEL_CHU_SAN: return "初三";
            case RIDER_LEVEL_CHU_ER: return "初二";
            case RIDER_LEVEL_CHU_YI: return "初一";
            case RIDER_LEVEL_ZHONG_SAN: return "中三";
            case RIDER_LEVEL_ZHONG_ER: return "中二";
            case RIDER_LEVEL_ZHONG_YI: return "中一";
            case RIDER_LEVEL_GUO_SAN: return "国三";
            case RIDER_LEVEL_GUO_ER: return "国二";
            case RIDER_LEVEL_GUO_YI: return "国一";
            case RIDER_LEVEL_JIAN_JIANG: return "健将级";
            default: return "";
        }
    }
    
    /**
     * 获取教练证书类别颜色（用于前端显示）
     * @param category 类别编号
     * @return 颜色标识
     */
    public static String getCoachCategoryColor(Integer category) {
        if (category == null) return "default";
        switch (category) {
            case COACH_CATEGORY_SHOW_JUMPING: return "blue";
            case COACH_CATEGORY_DRESSAGE: return "green";
            case COACH_CATEGORY_EVENTING: return "orange";
            case COACH_CATEGORY_STAR: return "gold";
            default: return "default";
        }
    }
    
    /**
     * 获取骑手证书类别颜色（用于前端显示）
     * @param category 类别编号
     * @return 颜色标识
     */
    public static String getRiderCategoryColor(Integer category) {
        if (category == null) return "default";
        switch (category) {
            case RIDER_CATEGORY_SHOW_JUMPING: return "blue";
            case RIDER_CATEGORY_DRESSAGE: return "green";
            case RIDER_CATEGORY_EVENTING: return "orange";
            default: return "default";
        }
    }
    
    /**
     * 验证教练证书类别是否有效
     * @param category 类别编号
     * @return 是否有效
     */
    public static boolean isValidCoachCategory(Integer category) {
        return category != null && 
               (category == COACH_CATEGORY_SHOW_JUMPING || 
                category == COACH_CATEGORY_DRESSAGE || 
                category == COACH_CATEGORY_EVENTING ||
                category == COACH_CATEGORY_STAR);
    }
    
    /**
     * 验证骑手证书类别是否有效
     * @param category 类别编号
     * @return 是否有效
     */
    public static boolean isValidRiderCategory(Integer category) {
        return category != null && 
               (category == RIDER_CATEGORY_SHOW_JUMPING || 
                category == RIDER_CATEGORY_DRESSAGE || 
                category == RIDER_CATEGORY_EVENTING);
    }
    
    /**
     * 验证教练证书等级是否有效
     * @param level 等级编号
     * @return 是否有效
     */
    public static boolean isValidCoachLevel(Integer level) {
        return level != null && level >= COACH_LEVEL_ONE_STAR && level <= COACH_LEVEL_FIVE_STAR;
    }
    
    /**
     * 验证骑手证书等级是否有效
     * @param level 等级编号
     * @return 是否有效
     */
    public static boolean isValidRiderLevel(Integer level) {
        return level != null && level >= RIDER_LEVEL_CHU_SAN && level <= RIDER_LEVEL_JIAN_JIANG;
    }
}