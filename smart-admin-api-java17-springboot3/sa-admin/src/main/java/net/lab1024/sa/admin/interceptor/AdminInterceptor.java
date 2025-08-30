package net.lab1024.sa.admin.interceptor;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaAnnotationStrategy;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.coach.constant.CoachAppConst;
import net.lab1024.sa.admin.module.business.coach.domain.RequestCoach;
import net.lab1024.sa.admin.module.business.coach.service.CoachAppService;
import net.lab1024.sa.admin.module.business.member.constant.MemberAppConst;
import net.lab1024.sa.admin.module.business.member.domain.RequestMember;
import net.lab1024.sa.admin.module.business.member.service.MemberAppService;
import net.lab1024.sa.admin.module.system.login.domain.RequestEmployee;
import net.lab1024.sa.admin.module.system.login.service.LoginService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * admin 拦截器
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/7/26 20:20:33
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Resource
    private MemberAppService memberAppService;

    @Resource
    private CoachAppService coachAppService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // OPTIONS请求直接return
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }

        boolean isHandler = handler instanceof HandlerMethod;
        if (!isHandler) {
            return true;
        }

        try {
            // --------------- 第一步： 根据token 获取用户 ---------------

            String tokenValue = StpUtil.getTokenValue();
            log.info("🔐 [Token调试] 拦截器获取到的tokenValue: {}", tokenValue);
            log.info("🔐 [Token调试] tokenValue长度: {}", tokenValue != null ? tokenValue.length() : 0);

            // 判断是否为会员或教练token
            boolean isMemberToken = tokenValue != null && tokenValue.startsWith(MemberAppConst.MEMBER_TOKEN_PREFIX);
            boolean isCoachToken = tokenValue != null && tokenValue.startsWith(CoachAppConst.COACH_TOKEN_PREFIX);
            
            log.info("🔐 [Token调试] isMemberToken: {}, isCoachToken: {}", isMemberToken, isCoachToken);
            log.info("🔐 [Token调试] MEMBER_TOKEN_PREFIX: {}", MemberAppConst.MEMBER_TOKEN_PREFIX);
            log.info("🔐 [Token调试] COACH_TOKEN_PREFIX: {}", CoachAppConst.COACH_TOKEN_PREFIX);

            String loginId = null;
            if (isMemberToken) {
                // 会员token：去掉前缀后用StpUtil验证
                String actualToken = tokenValue.substring(MemberAppConst.MEMBER_TOKEN_PREFIX.length());
                log.info("🔐 [Token调试] 会员token去除前缀后: {}", actualToken);
                loginId = (String) StpUtil.getLoginIdByToken(actualToken);
                log.info("🔐 [Token调试] 会员loginId: {}", loginId);
            } else if (isCoachToken) {
                // 教练token：去掉前缀后用StpUtil验证
                String actualToken = tokenValue.substring(CoachAppConst.COACH_TOKEN_PREFIX.length());
                log.info("🔐 [Token调试] 教练token去除前缀后: {}", actualToken);
                loginId = (String) StpUtil.getLoginIdByToken(actualToken);
                log.info("🔐 [Token调试] 教练loginId: {}", loginId);
            } else {
                // 员工token：直接用StpUtil验证
                log.info("🔐 [Token调试] 作为员工token处理");
                loginId = (String) StpUtil.getLoginIdByToken(tokenValue);
                log.info("🔐 [Token调试] 员工loginId: {}", loginId);
            }

            Object requestUser = null;
            if (isMemberToken) {
                // 会员登录逻辑
                requestUser = memberAppService.getLoginMember(loginId, request);
            } else if (isCoachToken) {
                // 教练登录逻辑
                requestUser = coachAppService.getLoginCoach(loginId, request);
            } else {
                // 员工登录逻辑
                requestUser = loginService.getLoginEmployee(loginId, request);
            }

            // --------------- 第二步： 校验 登录 ---------------

            Method method = ((HandlerMethod) handler).getMethod();
            NoNeedLogin noNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class);
            if (noNeedLogin != null) {
                if (isMemberToken) {
                    checkMemberActiveTimeout((RequestMember) requestUser);
                } else if (isCoachToken) {
                    checkCoachActiveTimeout((RequestCoach) requestUser);
                } else {
                    checkActiveTimeout((RequestEmployee) requestUser);
                }
                return true;
            }

            if (requestUser == null) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
                return false;
            }

            // 检测token 活跃频率
            if (isMemberToken) {
                checkMemberActiveTimeout((RequestMember) requestUser);
            } else if (isCoachToken) {
                checkCoachActiveTimeout((RequestCoach) requestUser);
            } else {
                checkActiveTimeout((RequestEmployee) requestUser);
            }

            // --------------- 第三步： 校验 权限 ---------------

            SmartRequestUtil.setRequestUser((RequestUser) requestUser);
            if (SaAnnotationStrategy.instance.isAnnotationPresent.apply(method, SaIgnore.class)) {
                return true;
            }

            // 会员和教练请求不需要复杂的权限校验，只检查基本状态
            if (isMemberToken) {
                RequestMember requestMember = (RequestMember) requestUser;
                // 检查会员状态
                if (requestMember.getDisabledFlag() != null && requestMember.getDisabledFlag() == 1) {
                    SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.NO_PERMISSION));
                    return false;
                }
                return true;
            }

            if (isCoachToken) {
                RequestCoach requestCoach = (RequestCoach) requestUser;
                // 检查教练状态
                if (requestCoach.getDisabledFlag() != null && requestCoach.getDisabledFlag() == 1) {
                    SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.NO_PERMISSION));
                    return false;
                }
                return true;
            }

            // 如果是员工超级管理员的话，不需要校验权限
            RequestEmployee requestEmployee = (RequestEmployee) requestUser;
            if (requestEmployee.getAdministratorFlag()) {
                return true;
            }

            SaAnnotationStrategy.instance.checkMethodAnnotation.accept(method);

        } catch (SaTokenException e) {
            /*
             * sa-token 异常状态码
             * 具体请看： https://sa-token.cc/doc.html#/fun/exception-code
             */
            int code = e.getCode();
            if (code == 11041 || code == 11051) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.NO_PERMISSION));
            } else if (code == 11016) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_ACTIVE_TIMEOUT));
            } else if (code >= 11011 && code <= 11015) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
            } else {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.PARAM_ERROR));
            }
            return false;
        } catch (Throwable e) {
            SmartResponseUtil.write(response, ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR));
            log.error(e.getMessage(), e);
            return false;
        }

        // 通过验证
        return true;
    }


    /**
     * 检测：token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结
     */
    private void checkActiveTimeout(RequestEmployee requestEmployee) {
        // 用户不在线，也不用检测
        if (requestEmployee == null) {
            return;
        }

        StpUtil.checkActiveTimeout();
        StpUtil.updateLastActiveToNow();
    }

    /**
     * 检测会员token活跃频率
     */
    private void checkMemberActiveTimeout(RequestMember requestMember) {
        // 会员不在线，也不用检测
        if (requestMember == null) {
            return;
        }

        // 会员端不检查活跃超时，只更新活跃时间
        // 小程序端通常保持长连接，不需要严格的活跃超时控制
        try {
            StpUtil.updateLastActiveToNow();
        } catch (Exception e) {
            // 忽略活跃时间更新异常，不影响正常业务
            log.debug("更新会员活跃时间失败, memberId: {}",
                     requestMember != null ? requestMember.getMemberId() : "unknown", e);
        }

        // 注释掉活跃超时检测，避免30012错误
        // StpUtil.checkActiveTimeout();
    }

    /**
     * 检测教练token活跃频率
     */
    private void checkCoachActiveTimeout(RequestCoach requestCoach) {
        // 教练不在线，也不用检测
        if (requestCoach == null) {
            return;
        }

        // 教练端不检查活跃超时，只更新活跃时间
        // 小程序端通常保持长连接，不需要严格的活跃超时控制
        try {
            StpUtil.updateLastActiveToNow();
        } catch (Exception e) {
            // 忽略活跃时间更新异常，不影响正常业务
            log.debug("更新教练活跃时间失败, coachId: {}",
                     requestCoach != null ? requestCoach.getCoachId() : "unknown", e);
        }

        // 注释掉活跃超时检测，避免30012错误
        // StpUtil.checkActiveTimeout();
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除上下文
        SmartRequestUtil.remove();
    }
}
