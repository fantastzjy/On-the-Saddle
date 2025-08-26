package net.lab1024.sa.admin.module.openapi.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.openapi.service.SpeechRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 语音识别WebSocket处理器
 *
 * @Author 1024创新实验室
 * @Date 2024-01-01
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Component
public class SpeechWebSocketHandler implements WebSocketHandler {

    @Autowired
    private SpeechRecognitionService speechRecognitionService;

    // 存储每个会话的音频数据缓存
    private final Map<String, ByteArrayOutputStream> audioBuffers = new ConcurrentHashMap<>();
    
    // 存储WebSocket会话
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        log.info("语音识别WebSocket连接建立，会话ID: {}", sessionId);
        
        sessions.put(sessionId, session);
        audioBuffers.put(sessionId, new ByteArrayOutputStream());
        
        // 发送连接成功消息
        sendMessage(session, createResponse("connected", "连接成功", null));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String sessionId = session.getId();
        
        if (message instanceof BinaryMessage) {
            // 处理二进制音频数据
            handleBinaryMessage(session, (BinaryMessage) message);
        } else if (message instanceof TextMessage) {
            // 处理文本消息（控制命令）
            handleTextMessage(session, (TextMessage) message);
        }
    }

    private void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        String sessionId = session.getId();
        byte[] audioChunk = message.getPayload().array();
        
        // 检测音频格式
        String audioFormat = detectAudioFormat(audioChunk);
        log.debug("接收到音频数据块，会话ID: {}, 大小: {} bytes, 格式: {}", 
                 sessionId, audioChunk.length, audioFormat);
        
        // 将音频数据追加到缓冲区
        ByteArrayOutputStream buffer = audioBuffers.get(sessionId);
        if (buffer != null) {
            try {
                buffer.write(audioChunk);
                log.debug("音频数据已追加到缓冲区，当前总大小: {} bytes", buffer.size());
            } catch (IOException e) {
                log.error("写入音频缓冲区失败", e);
                sendMessage(session, createResponse("error", "数据处理失败", null));
            }
        }
    }
    
    /**
     * 检测音频格式
     */
    private String detectAudioFormat(byte[] audioData) {
        if (audioData.length < 4) return "unknown";
        
        // WAV格式检测 (RIFF header)
        if (audioData[0] == 'R' && audioData[1] == 'I' && 
            audioData[2] == 'F' && audioData[3] == 'F') {
            return "wav";
        }
        
        // MP3格式检测 (ID3 tag or frame header)
        if ((audioData[0] == 'I' && audioData[1] == 'D' && audioData[2] == '3') ||
            (audioData[0] == (byte)0xFF && (audioData[1] & 0xE0) == 0xE0)) {
            return "mp3";
        }
        
        // AAC格式检测 (ADTS header)
        if (audioData[0] == (byte)0xFF && (audioData[1] & 0xF0) == 0xF0) {
            return "aac";
        }
        
        // 如果都不匹配，可能是PCM原始数据
        return "pcm/raw";
    }

    private void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();
        String payload = message.getPayload();
        
        log.info("接收到文本消息，会话ID: {}, 内容: {}", sessionId, payload);
        
        try {
            Map<String, Object> command = JSON.parseObject(payload, Map.class);
            String action = (String) command.get("action");
            
            switch (action) {
                case "start_recording":
                    handleStartRecording(session);
                    break;
                case "stop_recording":
                    handleStopRecording(session);
                    break;
                case "clear_buffer":
                    handleClearBuffer(session);
                    break;
                default:
                    sendMessage(session, createResponse("error", "未知命令: " + action, null));
            }
        } catch (Exception e) {
            log.error("处理文本消息失败", e);
            sendMessage(session, createResponse("error", "命令格式错误", null));
        }
    }

    private void handleStartRecording(WebSocketSession session) {
        String sessionId = session.getId();
        log.info("开始录音，会话ID: {}", sessionId);
        
        // 清空缓冲区准备新的录音
        ByteArrayOutputStream buffer = audioBuffers.get(sessionId);
        if (buffer != null) {
            buffer.reset();
        }
        
        sendMessage(session, createResponse("recording_started", "开始录音", null));
    }

    private void handleStopRecording(WebSocketSession session) {
        String sessionId = session.getId();
        log.info("停止录音并开始识别，会话ID: {}", sessionId);
        
        ByteArrayOutputStream buffer = audioBuffers.get(sessionId);
        if (buffer == null || buffer.size() == 0) {
            sendMessage(session, createResponse("error", "没有音频数据", null));
            return;
        }
        
        // 异步处理语音识别
        new Thread(() -> {
            try {
                byte[] audioData = buffer.toByteArray();
                
                // 发送识别中状态
                sendMessage(session, createResponse("recognizing", "正在识别中...", null));
                
                // 调用语音识别服务
                String recognizedText = speechRecognitionService.processAudioData(audioData);
                
                // 发送识别结果
                Map<String, Object> result = Map.of(
                        "text", recognizedText,
                        "confidence", 0.95, // 模拟置信度
                        "audioSize", audioData.length
                );
                sendMessage(session, createResponse("recognition_result", "识别完成", result));
                
                // 清空缓冲区
                buffer.reset();
                
            } catch (Exception e) {
                log.error("语音识别处理失败", e);
                sendMessage(session, createResponse("error", "识别失败: " + e.getMessage(), null));
            }
        }).start();
    }

    private void handleClearBuffer(WebSocketSession session) {
        String sessionId = session.getId();
        log.info("清空音频缓冲区，会话ID: {}", sessionId);
        
        ByteArrayOutputStream buffer = audioBuffers.get(sessionId);
        if (buffer != null) {
            buffer.reset();
        }
        
        sendMessage(session, createResponse("buffer_cleared", "缓冲区已清空", null));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String sessionId = session.getId();
        log.error("WebSocket传输错误，会话ID: {}", sessionId, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String sessionId = session.getId();
        log.info("WebSocket连接关闭，会话ID: {}, 状态: {}", sessionId, closeStatus);
        
        // 清理资源
        sessions.remove(sessionId);
        ByteArrayOutputStream buffer = audioBuffers.remove(sessionId);
        if (buffer != null) {
            try {
                buffer.close();
            } catch (IOException e) {
                log.warn("关闭音频缓冲区失败", e);
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return true; // 支持分片消息
    }

    private void sendMessage(WebSocketSession session, Map<String, Object> response) {
        try {
            if (session.isOpen()) {
                String jsonResponse = JSON.toJSONString(response);
                session.sendMessage(new TextMessage(jsonResponse));
            }
        } catch (IOException e) {
            log.error("发送WebSocket消息失败", e);
        }
    }

    private Map<String, Object> createResponse(String type, String message, Object data) {
        return Map.of(
                "type", type,
                "message", message,
                "timestamp", System.currentTimeMillis(),
                "data", data != null ? data : new Object()
        );
    }
}