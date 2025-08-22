package net.lab1024.sa.admin.util;

import net.lab1024.sa.admin.module.business.member.domain.RequestMember;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;

/**
 * 会员请求工具类
 *
 * @Author Claude Code
 * @Date 2025-01-22
 * @Copyright 马术俱乐部管理系统
 */
public final class MemberRequestUtil {

    /**
     * 获取当前请求的会员信息
     */
    public static RequestMember getRequestMember() {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        if (requestUser instanceof RequestMember) {
            return (RequestMember) requestUser;
        }
        return null;
    }

    /**
     * 获取当前会员ID
     */
    public static Long getRequestMemberId() {
        RequestMember requestMember = getRequestMember();
        return requestMember != null ? requestMember.getMemberId() : null;
    }

    /**
     * 获取当前会员编号
     */
    public static String getRequestMemberNo() {
        RequestMember requestMember = getRequestMember();
        return requestMember != null ? requestMember.getMemberNo() : null;
    }

    /**
     * 获取当前会员所属俱乐部ID
     */
    public static Long getRequestMemberClubId() {
        RequestMember requestMember = getRequestMember();
        return requestMember != null ? requestMember.getClubId() : null;
    }

    /**
     * 检查是否为会员请求
     */
    public static boolean isMemberRequest() {
        return getRequestMember() != null;
    }
}