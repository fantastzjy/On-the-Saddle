package net.lab1024.sa.admin.module.openapi.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.openapi.domain.form.MemberAppLoginForm;
import net.lab1024.sa.admin.module.openapi.domain.vo.AppLoginVO;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberAppLoginVO;
import net.lab1024.sa.admin.module.openapi.service.AppLoginService;
import net.lab1024.sa.admin.module.business.coach.domain.RequestCoach;
import net.lab1024.sa.admin.module.business.member.domain.RequestMember;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序统一API控制器
 * 支持会员(usr)和教练(cc)登录
 */
@Slf4j
@RestController
@RequestMapping("/app/login")
@Tag(name = "统一登录", description = "小程序统一登录相关接口")
public class AppLoginController {

    @Resource
    private AppLoginService appLoginService;

    @NoNeedLogin
    @PostMapping("/wxLogin")
    @Operation(summary = "微信小程序统一登录", description = "支持会员(usr)和教练(cc)登录")
    public ResponseDTO<AppLoginVO> wxLogin(@RequestBody @Valid MemberAppLoginForm form) {
        return appLoginService.wxLogin(form);
    }

    @NoNeedLogin
    @GetMapping("/checkRegistration")
    @Operation(summary = "检查用户注册状态")
    public ResponseDTO<Boolean> checkRegistration(@RequestParam String unionId, @RequestParam String role) {
        return appLoginService.checkRegistration(unionId, role);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public ResponseDTO<String> logout() {
        try {
            RequestUser requestUser = SmartRequestUtil.getRequestUser();
            if (requestUser instanceof RequestMember) {
                RequestMember requestMember = (RequestMember) requestUser;
                log.info("会员退出登录, memberId: {}", requestMember.getMemberId());
            } else if (requestUser instanceof RequestCoach) {
                RequestCoach requestCoach = (RequestCoach) requestUser;
                log.info("教练退出登录, coachId: {}", requestCoach.getCoachId());
            }
            
            StpUtil.logout();
            return ResponseDTO.ok("退出登录成功");
        } catch (Exception e) {
            log.error("退出登录失败", e);
            return ResponseDTO.ok("退出登录成功");
        }
    }
}
