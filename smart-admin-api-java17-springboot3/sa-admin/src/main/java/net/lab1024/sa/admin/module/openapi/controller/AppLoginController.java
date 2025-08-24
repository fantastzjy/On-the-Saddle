package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.openapi.domain.form.MemberAppLoginForm;
import net.lab1024.sa.admin.module.business.member.domain.vo.MemberAppLoginVO;
import net.lab1024.sa.admin.module.openapi.service.AppLoginService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * 会员小程序API控制器
 */
@RestController
@RequestMapping("/app/member/login")
@Tag(name = "登录", description = "小程序登录相关接口")
public class AppLoginController {

    @Resource
    private AppLoginService appLoginService;

    @NoNeedLogin
    @PostMapping("/wxLogin")
    @Operation(summary = "微信小程序登录", description = "通过微信code进行登录验证")
    public ResponseDTO<MemberAppLoginVO> wxLogin(@RequestBody @Valid MemberAppLoginForm form) {
        return appLoginService.wxLogin(form);
    }

}
