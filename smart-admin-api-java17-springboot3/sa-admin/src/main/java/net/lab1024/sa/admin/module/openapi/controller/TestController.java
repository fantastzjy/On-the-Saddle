package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 用于测试各种功能
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Tag(name = "测试接口")
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Operation(summary = "测试基础连接")
    @GetMapping("/ping")
    @NoNeedLogin
    public ResponseDTO<String> ping() {
        log.info("测试接口被调用");
        return ResponseDTO.ok("pong - 接口正常");
    }

    @Operation(summary = "测试AI服务")
    @GetMapping("/ai-test")
    @NoNeedLogin
    public ResponseDTO<String> testAi() {
        log.info("AI测试接口被调用");
        return ResponseDTO.ok("AI服务测试接口正常");
    }
}
