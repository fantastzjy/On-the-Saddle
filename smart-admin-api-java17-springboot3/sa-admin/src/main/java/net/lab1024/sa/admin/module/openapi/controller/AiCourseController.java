package net.lab1024.sa.admin.module.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.openapi.service.SpeechRecognitionService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 语音识别控制器
 * 提供语音识别相关的API接口
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Tag(name = "语音识别")
@RestController
@RequestMapping("/openapi/speech")
@Slf4j
public class AiCourseController {

    @Autowired
    private SpeechRecognitionService speechRecognitionService;

    @Operation(summary = "获取语音识别服务信息")
    @GetMapping("/info")
    public ResponseDTO<Map<String, Object>> getServiceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("serviceName", "语音识别服务");
        info.put("version", "1.0.0");
        info.put("websocketUrl", "/openapi/speech/recognition");
        info.put("supportedFormats", speechRecognitionService.getSupportedFormats());
        info.put("maxAudioSize", "10MB");
        info.put("description", "通过WebSocket接收微信小程序录音流并转换为文本");

        Map<String, Object> usage = new HashMap<>();
        usage.put("connect", "连接到WebSocket端点: ws://your-domain/openapi/speech/recognition");
        usage.put("commands", Map.of(
                "start_recording", "开始录音",
                "stop_recording", "停止录音并开始识别",
                "clear_buffer", "清空音频缓冲区"
        ));
        usage.put("audioData", "通过BinaryMessage发送音频数据流");
        info.put("usage", usage);

        return ResponseDTO.ok(info);
    }

    @Operation(summary = "获取WebSocket连接状态")
    @GetMapping("/status")
    public ResponseDTO<Map<String, Object>> getConnectionStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "运行中");
        status.put("websocketEndpoint", "/openapi/speech/recognition");
        status.put("timestamp", System.currentTimeMillis());

        return ResponseDTO.ok(status);
    }

    @Operation(summary = "获取使用示例")
    @GetMapping("/example")
    public ResponseDTO<Map<String, Object>> getUsageExample() {
        Map<String, Object> example = new HashMap<>();

        // JavaScript示例代码
        String jsExample = """
            // 微信小程序WebSocket连接示例
            const socket = new WebSocket('ws://your-domain/openapi/speech/recognition');
            
            socket.onopen = function() {
                console.log('WebSocket连接已建立');
                // 发送开始录音命令
                socket.send(JSON.stringify({action: 'start_recording'}));
            };
            
            socket.onmessage = function(event) {
                const response = JSON.parse(event.data);
                console.log('收到响应:', response);
                
                if (response.type === 'recognition_result') {
                    console.log('识别结果:', response.data.text);
                }
            };
            
            // 发送音频数据
            function sendAudioData(audioBuffer) {
                if (socket.readyState === WebSocket.OPEN) {
                    socket.send(audioBuffer); // 发送二进制数据
                }
            }
            
            // 停止录音
            function stopRecording() {
                socket.send(JSON.stringify({action: 'stop_recording'}));
            }
            """;

        example.put("javascript", jsExample);
        example.put("description", "微信小程序中使用WebSocket进行语音识别的示例代码");

        return ResponseDTO.ok(example);
    }
}
