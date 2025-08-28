# 微信小程序语音约课功能实现说明

## 🎯 功能概述

实现微信小程序语音约课的完整流程：录音 → 语音识别 → 文字结果 → 后端AI处理 → 约课信息返回 → 页面跳转

## 🚀 核心流程

```
微信小程序 → 录音 → 语音识别 → 文字结果 → 后端AI接口 → 约课信息
```

## 📱 前端实现要点

### 1. 录音功能
- 使用微信小程序原生录音API：`wx.getRecorderManager()`
- 支持长按录音，松开结束
- 录音时长限制：60秒
- 音频格式：MP3，采样率16kHz

### 2. 语音识别
- 使用微信语音识别接口：`wx.translateVoice()`
- 传入录音文件路径
- 获取识别后的文字结果

### 3. 数据发送
- 识别完成后，将文字和用户信息发送到后端
- 调用 `/openapi/ai-course/process-voice` 接口

## 🔌 后端接口

### 核心接口：处理语音约课请求
```
POST /openapi/ai-course/process-voice
Content-Type: application/json

请求体：
{
  "memberId": 1,
  "clubId": 1,
  "speechText": "约张教练明天下午3点的基础课程"
}
```

### 响应格式
```json
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
    "targetPage": "/pages/booking/confirm",
    "pageParams": "coach=张教练&course=基础课程&time=2025-01-28 15:00:00",
    "status": "success"
  }
}
```

## 🎨 前端处理逻辑

### 1. 语音识别成功后的处理
```javascript
// 语音识别成功后，调用后端AI接口
wx.request({
  url: 'https://your-domain/openapi/ai-course/process-voice',
  method: 'POST',
  data: {
    memberId: this.data.memberId,
    clubId: this.data.clubId,
    speechText: recognizedText
  },
  success: (res) => {
    if (res.data.code === 0) {
      this.handleAiResponse(res.data.data);
    }
  }
});
```

### 2. AI响应处理
```javascript
handleAiResponse(aiResponse) {
  if (aiResponse.status === 'error') {
    // 显示错误弹窗
    wx.showModal({
      title: '处理失败',
      content: aiResponse.errorMessage,
      showCancel: false
    });
    return;
  }
  
  if (aiResponse.parametersComplete) {
    // 参数完整，跳转到约课确认页面
    wx.navigateTo({
      url: `${aiResponse.targetPage}?${aiResponse.pageParams}`
    });
  } else {
    // 参数不完整，跳转到相应页面补充
    wx.navigateTo({
      url: `${aiResponse.targetPage}?${aiResponse.pageParams}`
    });
  }
}
```

## ⚡ 性能优化特性

### 1. AI响应速度优化
- **提示词优化**: 减少token数量，提高处理速度
- **参数调优**: 
  - `maxTokens: 150` - 限制输出长度
  - `temperature: 0.05` - 降低随机性，提高一致性
  - `topP: 0.95` - 控制输出质量
  - `stream: false` - 关闭流式输出，一次性返回
- **超时控制**: 8秒超时，避免长时间等待

### 2. 智能缓存机制
- 缓存常见约课请求的AI响应
- 30分钟缓存过期时间
- 重复请求响应时间从秒级降低到毫秒级

### 3. 预期性能指标
- **首次请求**: 1-3秒响应时间
- **缓存命中**: 50-100毫秒响应时间
- **AI模型**: Qwen/QwQ-32B

## 📋 页面跳转规则

### 1. 参数完整
- **跳转页面**: `/pages/booking/confirm`
- **携带参数**: 教练名称、课程类型、约课时间

### 2. 缺失教练名称
- **跳转页面**: `/pages/coach/select`
- **携带参数**: 课程类型、约课时间

### 3. 缺失课程类型
- **跳转页面**: `/pages/course/select`
- **携带参数**: 教练名称、约课时间

### 4. 缺失约课时间
- **跳转页面**: `/pages/time/select`
- **携带参数**: 教练名称、课程类型

### 5. 系统错误
- **跳转页面**: `/pages/index/index`
- **携带参数**: 无

## 🔧 错误处理

### 1. 语音识别失败
- 显示错误弹窗
- 提示用户重新录音

### 2. AI处理失败
- 显示错误弹窗
- 显示具体错误信息

### 3. 网络错误
- 显示网络错误提示
- 建议用户检查网络连接

## 📝 注意事项

1. **录音权限**: 确保小程序有录音权限
2. **网络状态**: 检查网络连接状态
3. **用户信息**: 确保memberId和clubId正确
4. **错误处理**: 所有错误都要显示弹窗提示
5. **页面跳转**: 根据AI返回的targetPage和pageParams进行跳转

## 🧪 测试建议

### 1. 基础功能测试
- 测试语音识别功能
- 测试AI处理接口
- 测试页面跳转逻辑

### 2. 性能测试
- 测试AI响应时间
- 测试缓存命中效果
- 测试错误处理

### 3. 用户体验测试
- 测试录音操作流程
- 测试错误提示友好性
- 测试页面跳转准确性
