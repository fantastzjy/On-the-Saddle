import { request } from '@/lib/request';

/**
 * AI语音约课API
 * 包含调试日志功能
 * 
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */

// 调试模式开关
const DEBUG_MODE = true;

/**
 * 调试日志函数
 */
function debugLog(tag, message, data = null) {
  if (!DEBUG_MODE) return;
  
  const timestamp = new Date().toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  });
  
  console.log(`[${timestamp}] 🤖 [${tag}] ${message}`);
  
  if (data) {
    console.log(`[${timestamp}] 📊 [${tag}] 数据:`, data);
  }
}

/**
 * 处理语音约课请求
 */
export function processVoiceBooking(data) {
  debugLog('语音约课', '开始处理语音约课请求', {
    会员ID: data.memberId,
    俱乐部ID: data.clubId,
    语音文本: data.speechText
  });
  
  const startTime = Date.now();
  
  return request({
    url: '/openapi/ai/course/voice-booking',
    method: 'POST',
    data: data
  }).then(response => {
    const endTime = Date.now();
    const duration = endTime - startTime;
    
    debugLog('语音约课', `请求完成，耗时: ${duration}ms`, {
      请求状态: response.code === 200 ? '成功' : '失败',
      响应码: response.code,
      响应消息: response.message,
      响应数据: response.data
    });
    
    // 详细分析AI响应
    if (response.data) {
      debugLog('AI响应分析', '解析AI约课响应', {
        识别文本: response.data.recognizedText,
        用户角色: response.data.userRole,
        教练信息: {
          原始: response.data.coachName,
          匹配: response.data.extractedCoach
        },
        课程信息: {
          原始: response.data.courseType,
          匹配: response.data.extractedCourse
        },
        时间信息: response.data.appointmentTime,
        参数完整性: response.data.parametersComplete,
        缺失参数: response.data.missingParameters,
        跳转页面: response.data.targetPage,
        页面参数: response.data.pageParams
      });
      
      // 前端交互决策逻辑分析
      if (response.data.parametersComplete) {
        debugLog('前端交互', '✅ 参数完整，准备跳转订单确认页面', {
          目标页面: response.data.targetPage,
          导航指令: response.data.navigationInstruction,
          页面参数: response.data.pageParams
        });
      } else {
        debugLog('前端交互', '⚠️ 参数不完整，需要用户选择', {
          缺失参数: response.data.missingParameters,
          目标页面: response.data.targetPage,
          导航指令: response.data.navigationInstruction
        });
      }
    }
    
    return response;
  }).catch(error => {
    const endTime = Date.now();
    const duration = endTime - startTime;
    
    debugLog('语音约课', `❌ 请求失败，耗时: ${duration}ms`, {
      错误信息: error.message,
      错误详情: error
    });
    
    throw error;
  });
}

/**
 * 获取用户约课习惯
 */
export function getUserBookingHabit(memberId) {
  debugLog('约课习惯', '获取用户约课习惯', { 会员ID: memberId });
  
  return request({
    url: '/openapi/user/booking-habit',
    method: 'GET',
    params: { memberId }
  }).then(response => {
    debugLog('约课习惯', '获取用户约课习惯成功', {
      用户角色: response.data?.userRole,
      默认教练: response.data?.defaultCoachId,
      默认课程级别: response.data?.defaultCourseLevel,
      俱乐部ID: response.data?.clubId
    });
    
    return response;
  }).catch(error => {
    debugLog('约课习惯', '❌ 获取用户约课习惯失败', error);
    throw error;
  });
}

/**
 * 页面跳转调试函数
 */
export function debugPageNavigation(currentPage, targetPage, params = {}) {
  debugLog('页面跳转', `从 ${currentPage} 跳转到 ${targetPage}`, {
    当前页面: currentPage,
    目标页面: targetPage,
    跳转参数: params,
    跳转时间: new Date().toISOString()
  });
}

/**
 * 用户选择调试函数
 */
export function debugUserSelection(selectionType, selectedValue, allOptions = []) {
  debugLog('用户选择', `用户选择了 ${selectionType}`, {
    选择类型: selectionType,
    选择值: selectedValue,
    所有选项: allOptions,
    选择时间: new Date().toISOString()
  });
}

/**
 * 调试模式控制
 */
export function setDebugMode(enabled) {
  DEBUG_MODE = enabled;
  debugLog('调试模式', `调试模式已${enabled ? '开启' : '关闭'}`);
}

export default {
  processVoiceBooking,
  getUserBookingHabit,
  debugPageNavigation,
  debugUserSelection,
  setDebugMode,
  debugLog
};