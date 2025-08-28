<template>
  <view class="custom-tabbar">
    <view class="tabbar-item" @click="switchTab(0)">
      <image :src="current === 0 ? '/static/images/tabbar/home-icon-h.png' : '/static/images/tabbar/home-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 0 }">首页</text>
    </view>

    <view class="tabbar-item1" @click="showFeaturedPopup">
      <image :src="current === 1 ? '/static/images/tabbar/list-icon-h.png' : '/static/images/tabbar/list-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 1 }">特色活动</text>
    </view>

    <view class="tabbar-center1" @touchstart="showVoicePopup" @touchend="hideVoicePopup">
      <image src="/static/images/tabbar/luyin.png" :style="{
        width: '110rpx',
        height: '110rpx',
        transform: isPressed ? 'scale(1.1)' : 'scale(1)',
        transition: 'transform 0.2s'
      }" class="center-icon" />
    </view>

    <view class="tabbar-item2" @click="switchTab(3)">
      <image
        :src="current === 3 ? '/static/images/tabbar/message-icon-h.png' : '/static/images/tabbar/message-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 3 }">我的小马</text>
    </view>

    <view class="tabbar-item" @click="switchTab(4)">
      <image :src="current === 4 ? '/static/images/tabbar/mine-icon-h.png' : '/static/images/tabbar/mine-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 4 }">我的</text>
    </view>

    <!-- 特色活动弹窗 -->
    <uni-popup class="custom-gradient-mask" ref="featuredPopup" type="center" :mask="true"
      mask-background-color="rgba(0, 0, 0, 0.9)" :mask-click="false">

      <view class="featured-popup">
        <view class="popup-header">
          <text class="popup-title">选择活动</text>
          <text class="popup-subtitle">选择标签，进入专属活动详情</text>
        </view>

        <view class="tags-container">
          <view v-for="(tag, index) in tags" :key="index" class="tag-item1"
            :class="{ 'tag-selected': selectedTag === index }" @click="selectTag(index)">
            {{ tag }}
          </view>
        </view>

        <view class="popup-footer">
          <view class="confirm-btn" @click="confirmSelection">确认</view>
          <view class="cancel-btn" @click="closeFeaturedPopup">取消</view>
        </view>
      </view>
    </uni-popup>

    <!-- 语音提示弹窗 - 增强版本 -->
    <uni-popup ref="voicePopup" type="center" :mask="true" mask-background-color="rgba(0, 0, 0, 0.7)">
      <view class="voice-popup-enhanced">
        <view class="voice-animation">
          <view class="wave-container">
            <view class="wave" v-if="isRecording"></view>
            <view class="wave wave-2" v-if="isRecording"></view>
            <view class="wave wave-3" v-if="isRecording"></view>
          </view>
          <image src="/static/images/tabbar/luyin.png" class="voice-icon" />
        </view>
        <text class="voice-tips">{{ isRecording ? '正在录音...' : 'AI语音助手' }}</text>
        <text class="voice-subtitle">长按录音，松开识别</text>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import { BaseURL } from '@/lib/config.js'

export default {
  data() {
    return {
      isPressed: false, // 控制是否按下状态
      current: 0,
      _lastCurrent: 0, // 用于存储上一个 current 值
      
      // 语音相关数据
      recognitionManager: null,  // 语音识别管理器（插件提供）
      isRecording: false,
      voiceText: '',
      apiBase: BaseURL, // 使用统一配置的API基础地址

      selectedTag: null,
      tags: [
        '自定义活动1',
        '自定义活动2',
        '自定义活动3',
        '自定义活动4',
        '自定义活动5',
        '自定义活动6'
      ]
    }
  },
  created() {
    console.log('🚀 [组件初始化] CustomTabbar 组件开始创建');
    
    // 初始化时根据当前路由设置 current
    this.updateCurrentByRoute();

    // 监听路由变化（适用于页面返回等情况）
    uni.onAppRoute((res) => {
      this.updateCurrentByRoute();
    });
    
    console.log('🚀 [组件初始化] CustomTabbar 组件初始化完成');
    console.log('🚀 [组件初始化] 当前环境检查:', {
      'uni 对象': typeof uni,
      'wx 对象': typeof wx,
      'requirePlugin': typeof requirePlugin
    });
  },
  mounted() {
    console.log('🚀 [组件挂载] CustomTabbar 组件已挂载到DOM');
    console.log('🚀 [组件挂载] 语音按钮引用:', this.$refs.voicePopup ? '✅ 可用' : '❌ 不可用');
  },
  methods: {
    updateCurrentByRoute() {
      const pages = [
        '/pages/home/index',
        '/pages/featured/featured',
        '/pages/microphone/microphone',
        '/pages/message/message',
        '/pages/mine/mine'
      ];
      const currentRoute = getCurrentPages()[0]?.route || '';
      this.current = pages.indexOf('/' + currentRoute);
    },
    switchTab(index) {
      const urls = [
        '/pages/home/index',
        '/pages/featured/featured',
        '/pages/microphone/microphone',
        '/pages/message/message',
        '/pages/mine/mine'
      ];
      uni.switchTab({
        url: urls[index],
        success: () => {
          this.current = index;
        },
        fail: (err) => {
          console.error('切换失败:', err);
        }

      });
    },

    // 特色活动弹窗
    showFeaturedPopup() {
      this._lastCurrent = this.current;
      this.current = 1;
      this.$refs.featuredPopup.open();
    },

    closeFeaturedPopup() {
      this.current = this._lastCurrent;

      this.$refs.featuredPopup.close();
    },

    // 语音弹窗控制 - 修正版本（直接使用插件的录音识别）
    showVoicePopup() {
      console.log('🎯 [语音按钮] 用户长按语音按钮，开始显示语音弹窗');
      console.log('🎯 [语音按钮] 当前环境信息:', {
        platform: wx.getSystemInfoSync().platform,
        version: wx.getSystemInfoSync().version,
        SDKVersion: wx.getSystemInfoSync().SDKVersion
      });
      
      this.isPressed = true;
      this.$refs.voicePopup.open();
      
      // 直接开始语音识别，不需要手动录音管理
      this.startVoiceRecognition();
      console.log('🎯 [语音按钮] 语音弹窗已打开，开始语音识别');
    },

    hideVoicePopup() {
      console.log('🎯 [语音按钮] 用户松开语音按钮，结束语音识别');
      this.isPressed = false;
      
      // 停止语音识别
      this.stopVoiceRecognition();
      
      this.$refs.voicePopup.close();
      console.log('🎯 [语音按钮] 语音弹窗已关闭');
    },
    
    // 开始语音识别（使用插件的录音识别管理器）
    startVoiceRecognition() {
      console.log('🎙️ [语音识别] ===== 开始语音识别 =====');
      
      try {
        // 检查插件是否可用
        if (typeof requirePlugin === 'undefined') {
          console.warn('🎙️ [语音识别] requirePlugin 不可用，使用降级方案');
          this.useFallbackRecognition();
          return;
        }
        
        const plugin = requirePlugin("WechatSI");
        console.log('🎙️ [语音识别] ✅ 成功加载微信同声传译插件:', plugin);
        
        // 获取语音识别管理器
        this.recognitionManager = plugin.getRecordRecognitionManager();
        console.log('🎙️ [语音识别] 语音识别管理器已创建');
        
        // 设置识别事件监听
        this.setupRecognitionListeners();
        
        // 开始识别（插件会自动处理录音）
        console.log('🎙️ [语音识别] 开始语音识别，参数: {duration: 30000, lang: "zh_CN"}');
        this.recognitionManager.start({
          duration: 30000,    // 最长录音时间30秒
          lang: "zh_CN"       // 中文识别
        });
        
        this.isRecording = true;
        
      } catch (error) {
        console.error('🎙️ [语音识别] ❌ 语音识别启动失败:', error);
        this.useFallbackRecognition();
      }
    },
    
    // 停止语音识别
    stopVoiceRecognition() {
      console.log('🎙️ [语音识别] ===== 准备停止语音识别 =====');
      
      if (this.recognitionManager && this.isRecording) {
        console.log('🎙️ [语音识别] 调用识别管理器停止方法');
        this.recognitionManager.stop();
        this.isRecording = false;
        console.log('🎙️ [语音识别] 识别管理器.stop()方法已调用');
      } else {
        console.warn('🎙️ [语音识别] 识别管理器不存在或未在识别中');
      }
    },
    
    // 设置语音识别事件监听器
    setupRecognitionListeners() {
      if (!this.recognitionManager) return;
      
      console.log('🎙️ [语音识别] 设置事件监听器');
      
      // 监听识别开始
      this.recognitionManager.onStart = (res) => {
        console.log('🎙️ [语音识别] ✅ 识别开始成功:', res);
      };
      
      // 监听实时识别结果
      this.recognitionManager.onRecognize = (res) => {
        console.log('🎙️ [语音识别] 📝 实时识别结果:', res.result);
      };
      
      // 监听识别结束（最重要）
      this.recognitionManager.onStop = (res) => {
        console.log('🎙️ [语音识别] ✅ 识别完成！');
        console.log('🎙️ [语音识别] 最终识别结果:', res.result);
        
        if (res.result && res.result.trim()) {
          console.log('🎙️ [语音识别] 识别到有效文本，发送到AI服务');
          this.voiceText = res.result;
          this.sendToAiService(res.result);
        } else {
          console.warn('🎙️ [语音识别] 识别结果为空');
          wx.showToast({
            title: '未识别到语音内容，请重试',
            icon: 'none'
          });
        }
      };
      
      // 监听识别错误
      this.recognitionManager.onError = (res) => {
        console.error('🎙️ [语音识别] ❌ 识别失败:', res);
        wx.showToast({
          title: '语音识别失败，请重试',
          icon: 'error'
        });
      };
    },
    
    // 降级方案：模拟语音识别结果
    useFallbackRecognition() {
      console.log('🎙️ [语音识别] 使用降级方案（模拟识别）');
      console.log('🎙️ [语音识别] 当前环境信息:', {
        platform: wx.getSystemInfoSync().platform,
        version: wx.getSystemInfoSync().version,
        SDKVersion: wx.getSystemInfoSync().SDKVersion
      });
      
      // 模拟识别结果（开发测试用）
      const mockTexts = [
        '约张教练明天下午3点的基础课程',
        '约李教练后天上午10点的马术课',
        '约王教练这周六下午2点的体验课',
        '预约马术课程',
        '我要约课'
      ];
      
      const mockText = mockTexts[Math.floor(Math.random() * mockTexts.length)];
      console.log('🎙️ [语音识别] 模拟识别结果:', mockText);
      
      wx.showModal({
        title: '开发模式',
        content: `语音识别插件不可用\n使用模拟文本：\n${mockText}\n\n生产环境请配置微信同声传译插件`,
        showCancel: false,
        success: () => {
          this.voiceText = mockText;
          this.sendToAiService(mockText);
        }
      });
    },
    
    // 发送到AI服务
    sendToAiService(speechText) {
      console.log('🤖 [AI服务] 发送语音文本到AI服务:', speechText);
      
      // 验证语音输入
      if (!this.validateSpeechInput(speechText)) {
        return;
      }
      
      console.log('🤖 [AI服务] API地址:', `${this.apiBase}/openapi/ai-course/process-voice`);
      console.log('🤖 [AI服务] 请求参数:', {
        memberId: this.getCurrentMemberId(),
        clubId: this.getCurrentClubId(), 
        speechText: speechText
      });
      
      // 显示处理中提示
      wx.showLoading({ title: 'AI正在处理中...' });
      
      wx.request({
        url: `${this.apiBase}/openapi/ai-course/process-voice`,
        method: 'POST',
        header: {
          'Content-Type': 'application/json'
        },
        data: {
          memberId: this.getCurrentMemberId(),
          clubId: this.getCurrentClubId(), 
          speechText: speechText
        },
        success: (res) => {
          wx.hideLoading();
          console.log('🤖 [AI服务] 服务响应原始数据:', res);
          console.log('🤖 [AI服务] 响应状态码:', res.statusCode);
          console.log('🤖 [AI服务] 响应数据:', res.data);
          
          if (res.data.code === 0) {
            console.log('🤖 [AI服务] 处理成功，开始解析响应');
            this.handleAiResponse(res.data.data);
          } else {
            console.error('🤖 [AI服务] 处理失败:', res.data.message);
            wx.showToast({ 
              title: res.data.message || '处理失败', 
              icon: 'error' 
            });
          }
        },
        fail: (err) => {
          wx.hideLoading();
          console.error('🤖 [AI服务] 网络请求失败:', err);
          console.error('🤖 [AI服务] 错误详情:', {
            errMsg: err.errMsg,
            errno: err.errno,
            url: `${this.apiBase}/openapi/ai-course/process-voice`
          });
          wx.showToast({ 
            title: '网络错误，请检查网络连接', 
            icon: 'error' 
          });
        }
      });
    },
    
    // 验证语音输入
    validateSpeechInput(speechText) {
      if (!speechText || speechText.trim().length === 0) {
        wx.showModal({
          title: '语音识别提示',
          content: '未识别到语音内容，请重试',
          showCancel: false
        });
        return false;
      }
      
      // 1. 检查是否包含中文
      const chineseRegex = /[\u4e00-\u9fa5]/;
      if (!chineseRegex.test(speechText)) {
        wx.showModal({
          title: '语音识别提示',
          content: '请使用中文进行约课，检测到您可能使用了其他语言',
          showCancel: false,
          confirmText: '重新录音'
        });
        return false;
      }
      
      // 2. 检查是否包含约课相关关键词
      const bookingKeywords = ['约', '预约', '课程', '教练', '上课', '训练', '学习', '课', '帮我'];
      const hasBookingIntent = bookingKeywords.some(keyword => speechText.includes(keyword));
      
      if (!hasBookingIntent) {
        wx.showModal({
          title: '语音识别提示', 
          content: '未识别到约课意图，请明确说出约课需求，如"约张教练的课程"',
          showCancel: false,
          confirmText: '重新录音'
        });
        return false;
      }
      
      return true;
    },
    
    // 处理AI响应 - 核心跳转逻辑
    handleAiResponse(response) {
      console.log('🤖 [AI响应] 处理AI响应:', response);
      
      // 验证响应数据完整性
      if (!this.validateAiResponse(response)) {
        wx.showModal({
          title: '处理失败',
          content: 'AI处理结果异常，请重新尝试或手动选择约课',
          confirmText: '手动约课',
          cancelText: '重试',
          success: (res) => {
            if (res.confirm) {
              wx.switchTab({ url: '/pages/home/index' });
            }
          }
        });
        return;
      }
      
      if (response.status === 'error') {
        wx.showModal({
          title: '处理失败',
          content: response.errorMessage || '系统处理出现问题，请稍后重试',
          showCancel: false
        });
        return;
      }

      // 根据用户角色显示不同的处理结果
      switch (response.userRole) {
        case '新会员':
          this.handleNewMemberResponse(response);
          break;
        case '老会员':
          this.handleOldMemberResponse(response);
          break;
        case '马主':
          this.handleHorseOwnerResponse(response);
          break;
        default:
          this.handleDefaultResponse(response);
      }
    },
    
    // 验证AI响应数据
    validateAiResponse(response) {
      // 基本字段验证
      if (!response || !response.status) {
        console.error('🤖 [AI响应] 响应数据缺失基本字段');
        return false;
      }
      
      // 如果声称参数完整，验证必需字段
      if (response.parametersComplete) {
        const hasCoach = response.extractedCoach && response.extractedCoach !== null;
        const hasCourse = response.extractedCourse && response.extractedCourse !== null;
        const hasTimes = response.extractedTimes && Array.isArray(response.extractedTimes) && response.extractedTimes.length > 0;
        
        if (!hasCoach || !hasCourse || !hasTimes) {
          console.error('🤖 [AI响应] 参数声称完整但实际缺失:', {
            hasCoach,
            hasCourse, 
            hasTimes,
            extractedCoach: response.extractedCoach,
            extractedCourse: response.extractedCourse,
            extractedTimes: response.extractedTimes
          });
          return false;
        }
      }
      
      return true;
    },
    
    // 新会员响应处理
    handleNewMemberResponse(response) {
      wx.showModal({
        title: '欢迎新会员',
        content: response.aiResponse,
        confirmText: '开始体验',
        cancelText: '稍后再说',
        success: (res) => {
          if (res.confirm) {
            const params = response.pageParams || '';
            const separator = params ? '?' : '';
            wx.navigateTo({ 
              url: `${response.targetPage}${separator}${params}`
            });
          }
        }
      });
    },
    
    // 老会员响应处理
    handleOldMemberResponse(response) {
      if (response.parametersComplete) {
        // **修复：参数完整时直接跳转到付款页面，不显示确认弹窗**
        console.log('🤖 [AI响应] 三参数完整，直接跳转付款页面');
        
        // 显示简短提示然后跳转
        wx.showToast({
          title: '🎯 约课信息完整，正在跳转...',
          icon: 'none',
          duration: 1500
        });
        
        // 延迟跳转，让用户看到提示
        setTimeout(() => {
          this.navigateToOrderDetailWithAiData(response);
        }, 1000);
      } else {
        // 参数不完整，引导补充
        this.showParameterSelection(response);
      }
    },
    
    // 马主响应处理
    handleHorseOwnerResponse(response) {
      wx.showModal({
        title: '马主服务',
        content: response.aiResponse,
        confirmText: '查看服务',
        cancelText: '取消',
        success: (res) => {
          if (res.confirm) {
            const params = response.pageParams || '';
            const separator = params ? '?' : '';
            wx.navigateTo({ 
              url: `${response.targetPage}${separator}${params}`
            });
          }
        }
      });
    },
    
    // 默认响应处理
    handleDefaultResponse(response) {
      if (response.parametersComplete) {
        // **修复：参数完整时直接跳转到付款页面**
        console.log('🤖 [AI响应] 默认用户三参数完整，直接跳转付款页面');
        
        wx.showToast({
          title: '🎯 约课信息完整，正在跳转...',
          icon: 'none',
          duration: 1500
        });
        
        setTimeout(() => {
          this.navigateToOrderDetailWithAiData(response);
        }, 1000);
      } else {
        this.showParameterSelection(response);
      }
    },
    
    // 参数选择引导 - 按用户要求优化
    showParameterSelection(response) {
      const missing = response.missingParameters || [];
      const extracted = {
        coachNo: response.extractedCoach,
        courseCode: response.extractedCourse, 
        times: response.extractedTimes
      };

      console.log('🎯 [参数引导] 参数缺失分析:', { missing, extracted });
      
      // 特殊情况：全部参数缺失（无法识别任何有效信息）
      if (missing.length >= 3 || (!extracted.coachNo && !extracted.courseCode && (!extracted.times || extracted.times.length === 0))) {
        console.log('🎯 [参数引导] 全部参数缺失，显示2秒弹窗后跳转首页');
        
        wx.showToast({
          title: '未识别到有效约课信息\n请手动选择',
          icon: 'none',
          duration: 2000
        });
        
        setTimeout(() => {
          wx.switchTab({ url: '/pages/home/index' });
        }, 2000);
        return;
      }
      
      // 其他情况：根据具体缺失参数进行引导
      this.handlePartialParameters(response, missing, extracted);
    },
    
    // 处理部分参数缺失的情况
    handlePartialParameters(response, missing, extracted) {
      // 情况1: 缺少教练+课程 - 跳转首页选教练
      if (missing.includes('约课教练') && missing.includes('课程类型')) {
        let message = '请先选择教练，选定后会自动进入课程选择';
        if (extracted.times && extracted.times.length > 0) {
          const time = extracted.times[0];
          message = `✅ 已匹配时间：${time.date} ${time.range}\n\n${message}`;
        }
        
        this.navigateToHomeWithGuidance({
          message,
          title: 'AI约课助手',
          highlight: 'coach',
          preset: { times: extracted.times },
          flowType: 'coach-course'
        });
        return;
      }

      // 情况3: 缺少教练+时间 - 跳转首页选教练，预填课程（优化显示已匹配课程）
      if (missing.includes('约课教练') && missing.includes('约课时间')) {
        let coachTimeMessage = '请选择教练后进入时间选择';
        if (response.courseType || extracted.courseCode) {
          const courseDisplay = response.courseType || this.getCourseNameByCode(extracted.courseCode) || '课程';
          coachTimeMessage = `✅ 已匹配：${courseDisplay}\n\n${coachTimeMessage}`;
        }
        
        this.navigateToHomeWithGuidance({
          message: coachTimeMessage,
          title: 'AI约课助手',
          highlight: 'coach',
          preset: { courseCode: extracted.courseCode },
          flowType: 'coach-time'
        });
        return;
      }

      // 情况4: 缺少课程+时间 - 弹起课程选择（优化显示已匹配教练）
      if (missing.includes('课程类型') && missing.includes('约课时间')) {
        let courseTimeMessage = '请选择课程类型后进入时间选择';
        if (response.coachName) {
          courseTimeMessage = `✅ 已匹配：${response.coachName}教练\n\n${courseTimeMessage}`;
        }
        
        this.showCoursePopupWithPreset({
          coachNo: extracted.coachNo,
          flowType: 'course-time',
          message: courseTimeMessage
        });
        return;
      }

      // 情况5: 只缺少教练 - 跳转首页选教练（优化显示已匹配课程和时间）
      if (missing.includes('约课教练')) {
        let onlyCoachMessage = '请选择教练完成约课';
        let matchedInfo = [];
        
        if (response.courseType || extracted.courseCode) {
          const courseDisplay = response.courseType || this.getCourseNameByCode(extracted.courseCode) || '课程';
          matchedInfo.push(courseDisplay);
        }
        if (extracted.times && extracted.times.length > 0) {
          const time = extracted.times[0];
          matchedInfo.push(`${time.date} ${time.range}`);
        }
        
        if (matchedInfo.length > 0) {
          onlyCoachMessage = `✅ 已匹配：${matchedInfo.join(' | ')}\n\n${onlyCoachMessage}`;
        }
        
        this.navigateToHomeWithGuidance({
          message: onlyCoachMessage,
          title: 'AI约课助手',
          highlight: 'coach',
          preset: { 
            courseCode: extracted.courseCode,
            times: extracted.times 
          },
          flowType: 'coach-only'
        });
        return;
      }

      // 情况6: 只缺少课程 - 弹起课程选择（优化显示已匹配信息）
      if (missing.includes('课程类型')) {
        // 构建详细消息，显示已匹配的教练信息
        let courseMessage = `✅ 已匹配：${response.coachName || '教练'}`;
        if (extracted.times && extracted.times.length > 0) {
          const time = extracted.times[0];
          courseMessage += ` | ${time.date} ${time.range}`;
        }
        courseMessage += '\n\n请选择课程类型完成约课 📚';
        
        this.showCoursePopupWithPreset({
          coachNo: extracted.coachNo,
          times: extracted.times,
          flowType: 'course-only',
          message: courseMessage
        });
        return;
      }

      // 情况7: 只缺少时间 - 弹起时间选择（优化显示已匹配信息）
      if (missing.includes('约课时间')) {
        // 构建详细消息，显示已匹配的教练和课程信息
        let timeMessage = '';
        if (response.coachName) {
          timeMessage += `✅ 已匹配：${response.coachName}教练`;
        }
        if (response.courseType || extracted.courseCode) {
          const courseDisplay = response.courseType || this.getCourseNameByCode(extracted.courseCode) || '课程';
          timeMessage += timeMessage ? ` | ${courseDisplay}` : `✅ 已匹配：${courseDisplay}`;
        }
        timeMessage += '\n\n请选择上课时间完成约课 ⏰';
        
        this.showTimePopupWithPreset({
          coachNo: extracted.coachNo,
          courseCode: extracted.courseCode,
          flowType: 'time-only',
          message: timeMessage
        });
        return;
      }
    },
    
    // 新增方法：带引导的首页跳转
    navigateToHomeWithGuidance(options) {
      console.log('🎯 [参数引导] 跳转首页:', options);
      
      wx.showModal({
        title: options.title,
        content: options.message,
        confirmText: '开始选择',
        cancelText: '取消',
        success: (res) => {
          if (res.confirm) {
            const params = {
              from: 'voice_booking',
              guidance: encodeURIComponent(JSON.stringify(options))
            };
            
            console.log('🎯 [参数引导] 跳转参数:', params);
            wx.navigateTo({
              url: `/pages/home/index?${this.buildUrlParams(params)}`
            });
          }
        }
      });
    },

    // 新增方法：预设课程弹窗
    showCoursePopupWithPreset(options) {
      console.log('🎯 [参数引导] 显示课程选择:', options);
      
      wx.showModal({
        title: 'AI约课助手',
        content: options.message,
        confirmText: '选择课程',
        cancelText: '取消',
        success: (res) => {
          if (res.confirm) {
            // 发送事件到首页组件
            uni.$emit('voice-show-course-popup', options);
            
            // 跳转到首页（如果不在首页）
            wx.switchTab({
              url: '/pages/home/index'
            });
          }
        }
      });
    },

    // 新增方法：预设时间弹窗  
    showTimePopupWithPreset(options) {
      console.log('🎯 [参数引导] 显示时间选择:', options);
      
      wx.showModal({
        title: 'AI约课助手',
        content: options.message,
        confirmText: '选择时间',
        cancelText: '取消',
        success: (res) => {
          if (res.confirm) {
            // 发送事件到首页组件
            uni.$emit('voice-show-time-popup', options);
            
            // 跳转到首页（如果不在首页）
            wx.switchTab({
              url: '/pages/home/index'
            });
          }
        }
      });
    },

    // 工具方法：构建URL参数
    buildUrlParams(params) {
      return Object.keys(params)
        .map(key => `${key}=${params[key]}`)
        .join('&');
    },
    
    // 新增方法：从AI数据构建订单数据并跳转
    async navigateToOrderDetailWithAiData(response) {
      console.log('🎯 [AI跳转] 构建订单数据并跳转到订单详情页:', response);
      
      try {
        // 异步获取真实费用数据
        const coachFee = await this.getCoachFeeByCode(response.extractedCoach);
        const baseFee = await this.getCourseFeeByCode(response.extractedCourse);
        const totalAmount = await this.calculateTotalAmount(response);
        
        // 构建完整的订单数据对象
        const orderData = {
          clubCode: 'DEMO_CLUB_001',
          coachNo: this.extractCoachNumber(response.extractedCoach),
          courseCode: response.extractedCourse,
          courseName: await this.getRealCourseName(response.extractedCourse),
          times: response.extractedTimes ? [{ 
            date: response.extractedTimes[0]?.date, 
            timeSlots: response.extractedTimes.map(t => t.range) 
          }] : [],
          coachFee: coachFee,
          baseFee: baseFee,
          totalAmount: totalAmount,
          coachName: response.coachName
        };
        
        console.log('🎯 [AI跳转] 构建的订单数据（含真实费用）:', orderData);
        
        // 跳转到订单确认页面
        wx.navigateTo({
          url: `/pages/order-detail/order-detail?data=${encodeURIComponent(JSON.stringify(orderData))}`
        });
        
      } catch (error) {
        console.error('🎯 [AI跳转] ❌ 构建订单数据失败:', error);
        wx.showModal({
          title: '跳转失败',
          content: '订单数据构建异常，请重新尝试',
          confirmText: '重试',
          cancelText: '手动约课',
          success: (res) => {
            if (res.confirm) {
              // 重试AI处理
              this.handleAiResponse(response);
            } else {
              // 跳转到手动约课页面
              wx.switchTab({ url: '/pages/home/index' });
            }
          }
        });
      }
    },
    
    // 提取教练编号
    extractCoachNumber(extractedCoach) {
      if (!extractedCoach) return '';
      
      // AI现在返回的是C002格式，直接使用
      if (extractedCoach.startsWith('C')) {
        return extractedCoach;
      }
      
      // 如果是旧格式COACH_001，转换为C格式
      if (extractedCoach.startsWith('COACH_')) {
        const number = extractedCoach.replace('COACH_', '').padStart(3, '0');
        return `C${number}`;
      }
      
      // 如果是纯数字，转换为C格式
      if (!isNaN(extractedCoach)) {
        const number = extractedCoach.toString().padStart(3, '0');
        return `C${number}`;
      }
      
      // 直接返回原值
      return extractedCoach;
    },
    
    // 根据教练编码获取费用 - 改为从API获取真实数据
    async getCoachFeeByCode(coachCode) {
      try {
        const { getByCoach } = require('../../api/home/index');
        const actualCoachNo = this.extractCoachNumber(coachCode);
        const res = await getByCoach({ coachNo: actualCoachNo });
        if (res.code === 0 && res.data && res.data.coachFee) {
          return parseFloat(res.data.coachFee) || 100;
        }
      } catch (error) {
        console.error('🎯 [费用获取] 获取教练费用失败:', error);
      }
      
      // 降级到映射表
      const feeMap = {
        'C001': 100, 'C002': 120, 'C003': 150, 'C004': 130, 'C005': 110, 'C006': 140,
        'COACH_001': 100, 'COACH_002': 120, 'COACH_003': 150, 'COACH_004': 130, 'COACH_005': 110
      };
      
      return feeMap[coachCode] || 100; // 默认费用
    },
    
    // 根据课程编码获取费用 - 改为从API获取真实数据
    async getCourseFeeByCode(courseCode) {
      try {
        const { getCourseList } = require('../../api/home/index');
        const res = await getCourseList({ clubCode: 'DEMO_CLUB_001' });
        if (res.code === 0 && res.data && Array.isArray(res.data)) {
          const course = res.data.find(c => c.courseCode === courseCode);
          if (course && course.basePrice) {
            return parseFloat(course.basePrice) || 200;
          }
        }
      } catch (error) {
        console.error('🎯 [费用获取] 获取课程费用失败:', error);
      }
      
      // 降级到映射表
      const feeMap = {
        'BASIC_COURSE': 200,
        'INTERMEDIATE_COURSE': 300,
        'ADVANCED_COURSE': 400,
        'EXPERIENCE_COURSE': 150,
        'THEORY_COURSE': 100
      };
      
      return feeMap[courseCode] || 200; // 默认费用
    },
    
    // 获取真实课程名称
    async getRealCourseName(courseCode) {
      try {
        const { getCourseList } = require('../../api/home/index');
        const res = await getCourseList({ clubCode: 'DEMO_CLUB_001' });
        if (res.code === 0 && res.data && Array.isArray(res.data)) {
          const course = res.data.find(c => c.courseCode === courseCode);
          if (course && course.courseName) {
            return course.courseName;
          }
        }
      } catch (error) {
        console.error('🎯 [课程获取] 获取课程名称失败:', error);
      }
      return '基础课程'; // 默认名称
    },
    
    // 计算总金额 - 异步版本
    async calculateTotalAmount(response) {
      const coachFee = await this.getCoachFeeByCode(response.extractedCoach);
      const baseFee = await this.getCourseFeeByCode(response.extractedCourse);
      const timeSlotsCount = response.extractedTimes ? response.extractedTimes.length : 1;
      
      return (coachFee + baseFee) * timeSlotsCount;
    },
    
    // 获取当前会员ID（需要根据实际登录状态获取）
    getCurrentMemberId() {
      // 从本地存储或全局状态获取当前登录的会员ID
      // 这里返回示例ID，实际应该从登录状态获取
      return wx.getStorageSync('memberId') || 11;
    },
    
    // 获取当前俱乐部ID（需要根据实际情况获取）
    getCurrentClubId() {
      // 从本地存储或配置获取当前俱乐部ID
      // 这里返回示例ID，实际应该从应用配置获取
      return wx.getStorageSync('clubId') || 1;
    },
    
    // 通过课程编码获取课程名称（辅助方法）
    getCourseNameByCode(courseCode) {
      if (!courseCode) return null;
      
      const courseMap = {
        'BASIC_COURSE': '基础课程',
        'INTERMEDIATE_COURSE': '进阶课程', 
        'ADVANCED_COURSE': '高级课程',
        'EXPERIENCE_COURSE': '体验课程',
        'THEORY_COURSE': '理论课程'
      };
      
      return courseMap[courseCode] || courseCode;
    },

    selectTag(index) {
      this.selectedTag = this.selectedTag === index ? null : index;
    },

    confirmSelection() {
      if (this.selectedTag !== null) {
        uni.navigateTo({
          url: `/pages/eventDetails/eventDetails?activity=${this.tags[this.selectedTag]}`
        });
        this.closeFeaturedPopup();
      } else {
        uni.showToast({
          title: '请选择一个活动',
          icon: 'none'
        });
      }
    }
  }
}
</script>

<style>
.custom-gradient-mask .uni-popup__mask {
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.9), rgba(102, 102, 102, 0.9)) !important;
}

/* 语音弹窗样式 - 增强版本 */
.voice-popup-enhanced {
  background: linear-gradient(135deg, #FFFFFF 0%, #F8F9FA 100%);
  padding: 60rpx 50rpx 40rpx;
  border-radius: 20rpx;
  box-shadow: 0 10rpx 40rpx rgba(0, 0, 0, 0.15);
  text-align: center;
  min-width: 400rpx;
}

.voice-animation {
  position: relative;
  width: 120rpx;
  height: 120rpx;
  margin: 0 auto 30rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.wave-container {
  position: absolute;
  width: 100%;
  height: 100%;
}

.wave {
  position: absolute;
  width: 100%;
  height: 100%;
  border: 3rpx solid #B7975E;
  border-radius: 50%;
  animation: wave-animation 2s ease-out infinite;
}

.wave-2 {
  animation-delay: 0.5s;
}

.wave-3 {
  animation-delay: 1s;
}

@keyframes wave-animation {
  0% {
    transform: scale(0.8);
    opacity: 1;
  }
  100% {
    transform: scale(1.8);
    opacity: 0;
  }
}

.voice-icon {
  width: 80rpx;
  height: 80rpx;
  z-index: 1;
}

.voice-tips {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 10rpx;
}

.voice-subtitle {
  display: block;
  font-size: 24rpx;
  color: #999999;
}

.uni-popup__mask {
  background: linear-gradient(to bottom, #000, #666) !important;
  opacity: 0.8 !important;
}

.custom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 90rpx;
  background-color: #FFFFFF;
  display: flex;
  justify-content: space-around;
  align-items: center;
  box-shadow: 0 -2rpx 6rpx rgba(0, 0, 0, 0.1);
  z-index: 999 !important;
  padding-bottom: env(safe-area-inset-bottom);
}

.tabbar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 20rpx;
  color: #999999;
  flex: 1;
}

.tabbar-item1 {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 20rpx;
  color: #999999;
  flex: 1;
  margin-left: -30rpx;


}

.tabbar-item2 {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 20rpx;
  color: #999999;
  flex: 1;
  margin-right: -30rpx;

}

.tabbar-icon {
  width: 48rpx;
  height: 48rpx;
  margin-bottom: 4rpx;
}

.active-text {
  color: #fad27d;
}

.tabbar-center1 {
  position: relative;
  bottom: 2rpx;
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.center-icon {
  width: 110rpx;
  height: 110rpx;
  transition: transform 0.2s;
  margin-bottom: 20rpx;

}

.tabbar-center1:active .center-icon {
  transform: scale(1.1);
}

/* 特色活动弹窗原有样式保持不变 */
.featured-popup {
  width: 628rpx;
  background: linear-gradient(180deg, #FFF2DC 0%, #FFFFFF 22%, #FFFFFF 66%, #FFF5E4 100%);
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(0, 0, 0, 0.05);
  border-radius: 20rpx;
  border: 4rpx solid #FFFFFF;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 40rpx 10rpx 40rpx;

  box-sizing: border-box;
}

.popup-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40rpx;
}

.popup-title {
  font-weight: 600;
  font-size: 48rpx;
  color: #333333;
  margin-bottom: 16rpx;
}

.popup-subtitle {
  font-weight: 600;
  font-size: 32rpx;
  color: #333333;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 40rpx;
}

.tag-item1 {
  width: 260rpx;
  height: 112rpx;
  background: #F6F6F6;
  border-radius: 146rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 20rpx;
  transition: all 0.3s;
}

.tag-selected {
  background: #F9EEDA;
  border-radius: 146rpx;
  border: 2rpx solid #A0762C;
  font-family: 'OPPOSans', 'OPPOSans';
  font-weight: 500;
  font-size: 28rpx;
  color: #8A5800;
}

.popup-footer {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-top: 20rpx;
}

.cancel-btn {
  font-size: 36rpx;
  color: #666666;
  padding: 30rpx 0;
  text-align: center;
  width: 100rpx;
}

.confirm-btn {
  width: 502rpx;
  height: 110rpx;
  background: #B7975E;
  border-radius: 146rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 600;
  font-size: 36rpx;
  color: #FFFFFF;
}
</style>