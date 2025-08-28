<template>
  <view class="container">
    <view class="notification-list">
      <view class="notification-item" v-for="(item, index) in notifications" :key="index">
        <view class="time">{{ item.time }}</view>
        <view class="card">
          <view class="header">
            <view class="title-wrapper">
              <text class="title">系统通知</text>
              <view v-if="!item.read" class="unread-dot" />
            </view>
            <image @click="deleteNotification(index)" src="/static/images/mine/删除.png" class="delete-icon"
              mode="widthFix" />
          </view>
          <view class="content">
            {{ item.content }}
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      notifications: [
        {
          time: "2021年12月25日 15:57",
          content: "亲爱的朋友您好，为了维护您的权益，我们近期优化了用户协议服务和隐私政策的部分条款，您可前往「设置」查看。",
          read: false
        },
        {
          time: "2021年12月25日 15:57",
          content: "亲爱的朋友您好，为了维护您的权益，我们近期优化了用户协议服务和隐私政策的部分条款，您可前往「设置」查看。",
          read: false
        },
        {
          time: "2021年12月25日 15:57",
          content: "亲爱的朋友您好，为了维护您的权益，我们近期优化了用户协议服务和隐私政策的部分条款，您可前往「设置」查看。",
          read: true
        }
      ]
    }
  },
  methods: {
    deleteNotification(index) {
      this.notifications.splice(index, 1);
      uni.showToast({
        title: '删除成功',
        icon: 'none'
      });
    }
  }
}
</script>

<style>
.container {
  background-color: #f6f6f6;
  min-height: 100vh;
  padding: 24rpx;
}

.notification-item {
  margin-bottom: 24rpx;
}

.time {
  text-align: center;
  font-weight: 500;
  font-size: 24rpx;
  color: #999999;
  margin-top: 40rpx;
  margin-bottom: 30rpx;
}

.card {
  background-color: #ffffff;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-top: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-wrapper {
  display: flex;
  align-items: center;
}

.title {
  font-weight: 500;
  font-size: 32rpx;
  color: #0B1526;
}

.unread-dot {
  width: 12rpx;
  height: 12rpx;
  background-color: #FF5B5B;
  border-radius: 50%;
  margin-left: 12rpx;
}

.delete-icon {
  width: 33rpx;
  height: 32rpx;
}

.content {
  font-size: 28rpx;
  color: #666666;
  margin-top: 16rpx;
  line-height: 42rpx;
}
</style>