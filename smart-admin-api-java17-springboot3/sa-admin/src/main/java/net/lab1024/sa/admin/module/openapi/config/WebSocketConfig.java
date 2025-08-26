package net.lab1024.sa.admin.module.openapi.config;

import net.lab1024.sa.admin.module.openapi.handler.SpeechWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置类
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public SpeechWebSocketHandler speechWebSocketHandler() {
        return new SpeechWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册语音识别WebSocket处理器，支持跨域
        registry.addHandler(speechWebSocketHandler(), "/openapi/speech/recognition")
                .setAllowedOrigins("*"); // 生产环境建议限制具体域名 //TODO
    }
}
