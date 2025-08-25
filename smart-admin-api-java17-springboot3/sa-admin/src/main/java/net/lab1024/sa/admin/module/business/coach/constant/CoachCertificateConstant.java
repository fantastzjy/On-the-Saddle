package net.lab1024.sa.admin.module.business.coach.constant;

/**
 * 教练和骑手证书管理常量（扁平化结构版）
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
public class CoachCertificateConstant {

    // ===================== 教练星级常量 =====================
    public static final int COACH_LEVEL_NONE = 0;           // 无证书
    public static final int COACH_LEVEL_ONE_STAR = 1;       // 一星
    public static final int COACH_LEVEL_TWO_STAR = 2;       // 二星
    public static final int COACH_LEVEL_THREE_STAR = 3;     // 三星
    public static final int COACH_LEVEL_FOUR_STAR = 4;      // 四星
    public static final int COACH_LEVEL_FIVE_STAR = 5;      // 五星

    // ===================== 骑手等级常量 =====================
    public static final int RIDER_LEVEL_NONE = 0;           // 无证书
    public static final int RIDER_LEVEL_CHU_SAN = 1;        // 初三
    public static final int RIDER_LEVEL_CHU_ER = 2;         // 初二
    public static final int RIDER_LEVEL_CHU_YI = 3;         // 初一
    public static final int RIDER_LEVEL_ZHONG_SAN = 4;      // 中三
    public static final int RIDER_LEVEL_ZHONG_ER = 5;       // 中二
    public static final int RIDER_LEVEL_ZHONG_YI = 6;       // 中一
    public static final int RIDER_LEVEL_GUO_SAN = 7;        // 国三
    public static final int RIDER_LEVEL_GUO_ER = 8;         // 国二
    public static final int RIDER_LEVEL_GUO_YI = 9;         // 国一
    public static final int RIDER_LEVEL_JIAN_JIANG = 10;    // 健将级

    // ===================== 证书类别常量 =====================
    public static final String CERT_TYPE_COACH_STAR = "教练星级";
    public static final String CERT_TYPE_COACH_SHOW_JUMPING = "教练场地障碍";
    public static final String CERT_TYPE_COACH_DRESSAGE = "教练盛装舞步";
    public static final String CERT_TYPE_COACH_EVENTING = "教练三项赛";
    public static final String CERT_TYPE_RIDER_SHOW_JUMPING = "骑手场地障碍";
    public static final String CERT_TYPE_RIDER_DRESSAGE = "骑手盛装舞步";
    public static final String CERT_TYPE_RIDER_EVENTING = "骑手三项赛";

    /**
     * 获取教练星级描述
     * @param level 星级编号
     * @return 星级描述
     */
    public static String getCoachStarLevelText(Integer level) {
        if (level == null || level == COACH_LEVEL_NONE) return "无证书";
        switch (level) {
            case COACH_LEVEL_ONE_STAR: return "一星";
            case COACH_LEVEL_TWO_STAR: return "二星";
            case COACH_LEVEL_THREE_STAR: return "三星";
            case COACH_LEVEL_FOUR_STAR: return "四星";
            case COACH_LEVEL_FIVE_STAR: return "五星";
            default: return "未知星级";
        }
    }

    /**
     * 获取骑手等级描述
     * @param level 等级编号
     * @return 等级描述
     */
    public static String getRiderLevelText(Integer level) {
        if (level == null || level == RIDER_LEVEL_NONE) return "无证书";
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
            default: return "未知等级";
        }
    }

    /**
     * 获取教练证书类别颜色（用于前端显示）
     * @param certType 证书类型
     * @return 颜色标识
     */
    public static String getCoachCertColor(String certType) {
        switch (certType) {
            case CERT_TYPE_COACH_STAR: return "gold";
            case CERT_TYPE_COACH_SHOW_JUMPING: return "blue";
            case CERT_TYPE_COACH_DRESSAGE: return "green";
            case CERT_TYPE_COACH_EVENTING: return "orange";
            default: return "default";
        }
    }

    /**
     * 获取骑手证书类别颜色（用于前端显示）
     * @param certType 证书类型
     * @return 颜色标识
     */
    public static String getRiderCertColor(String certType) {
        switch (certType) {
            case CERT_TYPE_RIDER_SHOW_JUMPING: return "blue";
            case CERT_TYPE_RIDER_DRESSAGE: return "green";
            case CERT_TYPE_RIDER_EVENTING: return "orange";
            default: return "default";
        }
    }

    /**
     * 验证教练星级是否有效
     * @param level 星级编号
     * @return 是否有效
     */
    public static boolean isValidCoachStarLevel(Integer level) {
        return level != null && level >= COACH_LEVEL_NONE && level <= COACH_LEVEL_FIVE_STAR;
    }

    /**
     * 验证骑手等级是否有效
     * @param level 等级编号
     * @return 是否有效
     */
    public static boolean isValidRiderLevel(Integer level) {
        return level != null && level >= RIDER_LEVEL_NONE && level <= RIDER_LEVEL_JIAN_JIANG;
    }

    /**
     * 获取骑手等级选项列表（用于前端下拉框）
     * @return 等级文本数组
     */
    public static String[] getRiderLevelOptions() {
        return new String[]{
            "无证书", "初三", "初二", "初一", "中三", "中二", "中一", "国三", "国二", "国一", "健将级"
        };
    }

    /**
     * 获取教练星级选项列表（用于前端下拉框）
     * @return 星级文本数组
     */
    public static String[] getCoachStarLevelOptions() {
        return new String[]{
            "无证书", "一星", "二星", "三星", "四星", "五星"
        };
    }
}
