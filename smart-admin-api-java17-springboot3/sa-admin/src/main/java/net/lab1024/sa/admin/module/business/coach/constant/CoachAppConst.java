package net.lab1024.sa.admin.module.business.coach.constant;

/**
 * 教练小程序常量
 *
 * @Author Claude Code
 * @Date 2025-08-25
 * @Copyright 马术俱乐部管理系统
 */
public class CoachAppConst {

    /**
     * Token前缀
     */
    public static final String COACH_TOKEN_PREFIX = "coach_";

    /**
     * 教练登录设备类型
     */
    public static final String COACH_LOGIN_DEVICE = "miniapp";

    /**
     * 教练状态
     */
    public static class CoachStatus {
        /** 在职 */
        public static final Integer ACTIVE = 1;
        /** 休假 */
        public static final Integer ON_LEAVE = 2;
        /** 离职 */
        public static final Integer RESIGNED = 3;
    }

    /**
     * 性别
     */
    public static class Gender {
        /** 未知 */
        public static final Integer UNKNOWN = 0;
        /** 男 */
        public static final Integer MALE = 1;
        /** 女 */
        public static final Integer FEMALE = 2;
    }

    /**
     * 是否标志
     */
    public static class Flag {
        /** 否 */
        public static final Integer NO = 0;
        /** 是 */
        public static final Integer YES = 1;
    }
}