<template>
  <view class="feedback-container">
    <!-- 顶部提示 -->
    <view class="feedback-header">
      <text class="header-text">请留下您对系统使用中的问题和建议，这对我们至关重要！</text>
    </view>

    <!-- 聊天内容区域 -->
    <view class="chat-container">
      <block v-for="(item, index) in messages" :key="index">
        <!-- 左侧消息（用户） -->
        <view class="message-left" v-if="item.type === 'left'">
          <image class="avatar" :src="item.avatar" />
          <view class="bubble-left">{{ item.content }}</view>
        </view>

        <!-- 右侧消息（系统） -->
        <view class="message-right" v-else>
          <view class="bubble-right">{{ item.content }}</view>
          <image class="avatar" :src="item.avatar" />
        </view>
      </block>
    </view>

    <!-- 输入框区域 -->
    <view class="input-area">
      <input class="input-box" v-model="inputText" placeholder="请输入反馈内容..." />
      <button class="send-btn" @click="sendMessage">发送</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      inputText: '',
      messages: [
        {
          type: 'left',
          content: '这个系统用起来很方便，但是希望能增加更多功能',
          avatar: 'https://wx2.sinaimg.cn/mw690/9516662fly1h7jo3fneubj20j60j6n1o.jpg'
        },
        {
          type: 'right',
          content: '感谢您的反馈，我们会持续优化系统功能',
          avatar: 'https://ww3.sinaimg.cn/mw690/006upAuggy1htykgkoalvj30sg0sgjwo.jpg'
        },
        {
          type: 'left',
          content: '特别是数据导出功能，希望能支持更多格式',
          avatar: 'https://wx2.sinaimg.cn/mw690/9516662fly1h7jo3fneubj20j60j6n1o.jpg'
        },
        {
          type: 'right',
          content: '您的建议已记录，下个版本会考虑加入',
          avatar: 'https://ww3.sinaimg.cn/mw690/006upAuggy1htykgkoalvj30sg0sgjwo.jpg'
        },
        {
          type: 'left',
          content: '好的',
          avatar: 'https://wx2.sinaimg.cn/mw690/9516662fly1h7jo3fneubj20j60j6n1o.jpg'
        },
      ]
    }
  },
  methods: {
    sendMessage() {
      if (this.inputText.trim() === '') return;

      // 添加用户消息
      this.messages.push({
        type: 'left',
        content: this.inputText,
        avatar: '/static/user-avatar.png'
      });

      this.inputText = '';

      // 模拟系统回复
      setTimeout(() => {
        this.messages.push({
          type: 'right',
          content: '感谢您的反馈，我们会尽快处理',
          avatar: '/static/system-avatar.png'
        });

        // 滚动到底部
        this.scrollToBottom();
      }, 800);
    },
    scrollToBottom() {
      // 实际项目中这里需要实现滚动到底部逻辑
      console.log('滚动到底部');
    }
  }
}
</script>

<style>
.feedback-container {
  background-color: #f6f6f6;
  min-height: 100vh;
  padding: 20rpx;
  padding-bottom: 120rpx;
}

.feedback-header {
  text-align: center;
  margin: 30rpx 0;
}

.header-text {
  font-size: 24rpx;
  color: #999999;
}

.chat-container {
  margin-bottom: 20rpx;
}

.message-left,
.message-right {
  margin: 40rpx 0;
  display: flex;
  align-items: flex-start;
}

.message-left {
  justify-content: flex-start;
}

.message-right {
  justify-content: flex-end;
}

.avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 24rpx;
  margin-top: -20rpx;
}

.message-left .avatar {
  margin-right: 20rpx;
}

.message-right .avatar {
  margin-left: 20rpx;
}

.bubble-left {
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(0, 0, 0, 0.05);
  border-radius: 0rpx 20rpx 20rpx 20rpx;
  font-size: 30rpx;
  color: #333;
  padding: 20rpx;
  max-width: 480rpx;
}

.bubble-right {
  background: #B7975E;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(0, 0, 0, 0.05);
  border-radius: 20rpx 0rpx 20rpx 20rpx;
  font-size: 30rpx;
  color: #FFFFFF;
  padding: 20rpx;
  max-width: 480rpx;
}

.input-area {
  position: fixed;
  bottom: 40rpx;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx;
  display: flex;
  align-items: center;
  border-top: 1rpx solid #eee;
}

.input-box {
  flex: 1;
  background: #f6f6f6;
  border-radius: 40rpx;
  padding: 20rpx 30rpx;
  margin-right: 20rpx;
  font-size: 28rpx;
}

.send-btn {
  background: #B7975E;
  color: #fff;
  border-radius: 40rpx;
  padding: 0 40rpx;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 28rpx;
}
</style>