# 微信语音识别 + AI约课 API 使用说明

## 🎯 系统概述

本系统提供基于微信小程序原生语音识别的智能约课服务，通过AI大模型解析用户语音意图，实现智能约课功能。

## 🚀 核心流程

```
微信小程序录音 → 语音识别 → 文字结果 → 后端AI处理 → 约课信息返回
```

## 📱 前端实现要点

### 1. 录音功能
- 使用 `wx.getRecorderManager()` 管理录音
- 支持长按录音，松开结束
- 录音时长限制：60秒
- 音频格式：MP3，采样率16kHz

### 2. 语音识别
- 使用 `wx.translateVoice()` 接口
- 传入录音文件路径
- 获取识别后的文字结果

### 3. 数据发送
- 识别完成后，将文字和用户信息发送到后端
- 调用 `/openapi/ai-course/process-voice` 接口

## 🔌 后端接口列表

### 核心接口

#### 1. 处理语音约课请求
```
POST /openapi/ai-course/process-voice
Content-Type: application/json

请求体：
{
  "memberId": 1,
  "clubId": 1,
  "speechText": "约张教练明天下午3点的基础课程"
}

响应：
{
  "code": 0,
  "data": {
    "recognizedText": "约张教练明天下午3点的基础课程",
    "coachName": "张教练",
    "courseType": "基础课程",
    "appointmentTime": "2025-01-28 15:00:00",
    "parametersComplete": true,
    "missingParameters": [],
    "aiResponse": "好的，我帮您预约张教练明天下午3点的基础课程。",
    "navigationInstruction": "跳转到约课确认页面",
    "status": "success"
  }
}
```

### 测试接口

#### 2. 测试AI服务连接
```
GET /openapi/ai-course/test-connection
```

#### 3. 测试完整约课场景
```
POST /openapi/ai-course/test-complete-booking
```

#### 4. 测试缺失教练名称场景
```
POST /openapi/ai-course/test-missing-coach
```

#### 5. 测试缺失时间场景
```
POST /openapi/ai-course/test-missing-time
```

#### 6. 测试语音识别效果
```
POST /openapi/ai-course/test-voice-recognition
Content-Type: application/json

{
  "text": "约张教练明天下午3点的基础课程"
}
```

#### 7. 批量测试语音识别场景
```
POST /openapi/ai-course/test-voice-scenarios
```

### 状态查询接口

#### 8. 获取微信语音识别服务状态
```
GET /openapi/ai-course/voice-status
```

#### 9. 获取AI处理性能统计
```
GET /openapi/ai-course/performance-stats
```

#### 10. 获取用户约课习惯
```
GET /openapi/ai-course/user-habit/{memberId}
```

### 缓存管理接口

#### 11. 获取AI缓存统计信息
```
GET /openapi/ai-course/cache/stats
```

#### 12. 清理AI缓存
```
POST /openapi/ai-course/cache/clear
```

## ⚡ 性能优化特性

### 1. AI响应速度优化
- **提示词优化**: 减少token数量，提高处理速度
- **参数调优**: 
  - `maxTokens: 200` - 限制输出长度
  - `temperature: 0.1` - 降低随机性，提高一致性
  - `topP: 0.9` - 控制输出质量
  - `stream: false` - 关闭流式输出，一次性返回
- **超时控制**: 10秒超时，避免长时间等待

### 2. 智能缓存机制
- 缓存常见约课请求的AI响应
- 30分钟缓存过期时间
- 重复请求响应时间从秒级降低到毫秒级

### 3. 预期性能指标
- **首次请求**: 2-5秒响应时间
- **缓存命中**: 50-100毫秒响应时间
- **AI模型**: Qwen/QwQ-32B

## 🧪 测试建议

### 1. 基础功能测试
- 测试AI服务连接
- 测试完整约课场景
- 测试各种缺失参数场景

### 2. 性能测试
- 测试缓存命中效果
- 监控AI响应时间
- 验证超时处理

### 3. 集成测试
- 前端语音识别 + 后端AI处理
- 错误处理和降级策略
- 用户体验流程验证

## 📋 注意事项

1. **认证**: 大部分测试接口使用 `@NoNeedLogin` 注解，无需登录
2. **缓存**: AI响应会自动缓存，提高重复请求的响应速度
3. **超时**: AI请求设置10秒超时，超时后会返回错误信息
4. **错误处理**: 系统提供详细的错误信息和日志记录

## 🔧 故障排除

### 常见问题
1. **AI服务连接失败**: 检查网络连接和API配置
2. **响应时间过长**: 检查缓存状态和AI服务性能
3. **识别结果不准确**: 检查语音质量和AI提示词

### 日志查看
- 查看后端日志了解详细错误信息
- 使用性能统计接口监控系统状态
- 检查缓存命中率优化性能

## 📞 技术支持

如有问题，请查看后端日志或联系技术支持团队。
