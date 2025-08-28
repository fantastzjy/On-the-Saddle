/**
 * AI约课前端调试工具类
 * 提供完整的调试日志和错误追踪功能
 * 
 * @Author Claude Code  
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */

class AIBookingDebugger {
  constructor() {
    this.debugMode = true;
    this.logHistory = [];
    this.maxLogHistory = 100;
  }

  /**
   * 核心日志记录方法
   */
  log(level, category, message, data = null, error = null) {
    if (!this.debugMode) return;
    
    const timestamp = new Date().toISOString();
    const logEntry = {
      timestamp,
      level,
      category,
      message,
      data,
      error,
      page: this.getCurrentPage(),
      userAgent: navigator.userAgent
    };

    // 控制台输出
    this._outputToConsole(logEntry);
    
    // 存储到历史记录
    this._storeLogHistory(logEntry);
    
    // 如果是错误，额外处理
    if (level === 'ERROR') {
      this._handleError(logEntry);
    }
  }

  /**
   * 语音约课流程调试
   */
  debugVoiceBookingFlow(stage, data) {
    const stages = {
      'START': '🎙️ 开始语音约课',
      'RECORDING': '📹 正在录音',
      'PROCESSING': '🤖 AI处理中',
      'RESPONSE': '📥 收到AI响应', 
      'NAVIGATION': '🧭 页面导航',
      'COMPLETE': '✅ 约课完成',
      'ERROR': '❌ 约课失败'
    };
    
    this.log('INFO', 'VoiceBooking', stages[stage] || stage, data);
    
    // 特殊阶段的额外处理
    switch(stage) {
      case 'RESPONSE':
        this._analyzeAIResponse(data);
        break;
      case 'NAVIGATION':
        this._trackPageNavigation(data);
        break;
      case 'ERROR':
        this._handleVoiceBookingError(data);
        break;
    }
  }

  /**
   * AI响应分析
   */
  _analyzeAIResponse(response) {
    if (!response) return;

    this.log('DEBUG', 'AIAnalysis', 'AI响应详细分析', {
      参数提取结果: {
        教练: response.coachName || 'N/A',
        课程: response.courseType || 'N/A', 
        时间: response.appointmentTime || 'N/A'
      },
      匹配结果: {
        教练编号: response.extractedCoach || 'N/A',
        课程编号: response.extractedCourse || 'N/A',
        时间段: response.extractedTimes || []
      },
      完整性检查: {
        是否完整: response.parametersComplete,
        缺失参数: response.missingParameters || [],
        用户角色: response.userRole
      },
      前端导航: {
        目标页面: response.targetPage,
        导航指令: response.navigationInstruction,
        页面参数: response.pageParams
      }
    });

    // 参数完整性评估
    const completeness = this._assessParameterCompleteness(response);
    this.log('INFO', 'AIAnalysis', `参数完整性评估: ${completeness}%`, {
      评估详情: this._getCompletenessDetails(response)
    });
  }

  /**
   * 页面导航追踪
   */
  _trackPageNavigation(navData) {
    const currentPage = this.getCurrentPage();
    
    this.log('INFO', 'Navigation', '页面导航事件', {
      从页面: currentPage,
      到页面: navData.targetPage,
      导航类型: navData.navigationType || 'Unknown',
      参数: navData.params || {},
      导航时间: new Date().toISOString()
    });
  }

  /**
   * 用户交互追踪
   */
  debugUserInteraction(action, target, data = {}) {
    this.log('INFO', 'UserInteraction', `用户${action}: ${target}`, {
      动作: action,
      目标: target,
      数据: data,
      页面: this.getCurrentPage(),
      时间: new Date().toISOString()
    });
  }

  /**
   * API请求调试
   */
  debugAPIRequest(url, method, requestData, responseData, duration) {
    this.log('DEBUG', 'API', `${method} ${url}`, {
      请求数据: requestData,
      响应数据: responseData,
      耗时: `${duration}ms`,
      状态: responseData?.code === 200 ? '成功' : '失败'
    });
  }

  /**
   * 错误处理
   */
  _handleError(logEntry) {
    // 可以在这里实现错误上报逻辑
    console.error('🚨 AI约课系统错误:', logEntry);
    
    // 如果是严重错误，可以弹出用户友好的提示
    if (logEntry.error && logEntry.error.critical) {
      uni.showToast({
        title: '系统出现异常，请稍后重试',
        icon: 'error',
        duration: 3000
      });
    }
  }

  /**
   * 语音约课错误处理
   */
  _handleVoiceBookingError(errorData) {
    this.log('ERROR', 'VoiceBookingError', '语音约课失败', {
      错误类型: errorData.type || 'Unknown',
      错误消息: errorData.message,
      错误堆栈: errorData.stack,
      用户输入: errorData.userInput,
      失败阶段: errorData.stage
    });
  }

  /**
   * 获取当前页面
   */
  getCurrentPage() {
    const pages = getCurrentPages();
    return pages.length > 0 ? pages[pages.length - 1].route : 'Unknown';
  }

  /**
   * 参数完整性评估
   */
  _assessParameterCompleteness(response) {
    const requiredParams = ['coachName', 'courseType', 'appointmentTime'];
    const extractedParams = ['extractedCoach', 'extractedCourse'];
    
    let score = 0;
    let total = requiredParams.length;
    
    requiredParams.forEach(param => {
      if (response[param]) score++;
    });
    
    return Math.round((score / total) * 100);
  }

  /**
   * 获取完整性详情
   */
  _getCompletenessDetails(response) {
    return {
      教练信息: {
        原始: response.coachName || null,
        匹配: response.extractedCoach || null,
        状态: response.coachName && response.extractedCoach ? '✅ 完整' : '❌ 缺失'
      },
      课程信息: {
        原始: response.courseType || null,
        匹配: response.extractedCourse || null,
        状态: response.courseType && response.extractedCourse ? '✅ 完整' : '❌ 缺失'
      },
      时间信息: {
        原始: response.appointmentTime || null,
        格式化: response.extractedTimes || [],
        状态: response.appointmentTime ? '✅ 完整' : '❌ 缺失'
      }
    };
  }

  /**
   * 控制台输出
   */
  _outputToConsole(logEntry) {
    const { timestamp, level, category, message, data } = logEntry;
    const timeStr = new Date(timestamp).toLocaleTimeString('zh-CN');
    const emoji = this._getLevelEmoji(level);
    
    console.log(`[${timeStr}] ${emoji} [${category}] ${message}`);
    if (data) {
      console.log(`[${timeStr}] 📊 [${category}] 数据:`, data);
    }
  }

  /**
   * 获取日志级别对应的emoji
   */
  _getLevelEmoji(level) {
    const emojis = {
      'DEBUG': '🔧',
      'INFO': '💡', 
      'WARN': '⚠️',
      'ERROR': '❌'
    };
    return emojis[level] || '📝';
  }

  /**
   * 存储日志历史
   */
  _storeLogHistory(logEntry) {
    this.logHistory.push(logEntry);
    
    // 限制历史记录数量
    if (this.logHistory.length > this.maxLogHistory) {
      this.logHistory.shift();
    }
  }

  /**
   * 导出日志历史
   */
  exportLogs() {
    return JSON.stringify(this.logHistory, null, 2);
  }

  /**
   * 清空日志历史
   */
  clearLogs() {
    this.logHistory = [];
    this.log('INFO', 'System', '日志历史已清空');
  }

  /**
   * 设置调试模式
   */
  setDebugMode(enabled) {
    this.debugMode = enabled;
    this.log('INFO', 'System', `调试模式已${enabled ? '开启' : '关闭'}`);
  }
}

// 创建全局调试器实例
const aiBookingDebugger = new AIBookingDebugger();

// 导出调试器实例和便捷方法
export default aiBookingDebugger;

export const debugVoiceBooking = (stage, data) => aiBookingDebugger.debugVoiceBookingFlow(stage, data);
export const debugUserInteraction = (action, target, data) => aiBookingDebugger.debugUserInteraction(action, target, data);
export const debugAPI = (url, method, req, res, duration) => aiBookingDebugger.debugAPIRequest(url, method, req, res, duration);
export const setDebugMode = (enabled) => aiBookingDebugger.setDebugMode(enabled);
export const exportLogs = () => aiBookingDebugger.exportLogs();
export const clearLogs = () => aiBookingDebugger.clearLogs();