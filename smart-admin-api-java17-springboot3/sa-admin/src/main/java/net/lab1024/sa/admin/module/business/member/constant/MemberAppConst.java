package net.lab1024.sa.admin.module.business.member.constant;

/**
 * 会员小程序常量
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
public class MemberAppConst {

    /**
     * Token前缀
     */
    public static final String MEMBER_TOKEN_PREFIX = "mem_";  // todo

    /**
     * 会员登录设备类型
     */
    public static final String MEMBER_LOGIN_DEVICE = "miniapp";

    /**
     * 会员状态
     */
    public static class MemberStatus {
        /** 未激活 */
        public static final Integer UNACTIVATED = 0;
        /** 已注册 */
        public static final Integer REGISTERED = 1;
    }

    /**
     * 会籍状态
     */
    public static class MembershipStatus {
        /** 正常 */
        public static final Integer NORMAL = 1;
        /** 即将到期 */
        public static final Integer EXPIRING_SOON = 2;
        /** 已过期 */
        public static final Integer EXPIRED = 3;
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
