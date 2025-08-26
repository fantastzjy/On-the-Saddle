<template>
  <view class="container">
    <view class="section-title">
      <uni-icons @click="goBack" type="left" color="#fff" size="22" />
      <text style="margin-left: -50rpx;">编辑资料</text>
      <text />
    </view>
    <!-- 头像方块 -->
    <view class="avatar-box">
      <image class="avatar-icon" src="/static/images/mine/photo.png" />
    </view>

    <!-- 标题 -->
    <!-- <text class="title">个人资料</text> -->

    <!-- 信息卡片 -->
    <view class="card">
      <view class="info-item">
        <text class="info-label">姓名</text>
        <text class="info-value">某某某</text>
      </view>
      <view class="info-item">
        <text class="info-label">性别</text>
        <text class="info-value">女</text>
      </view>
      <view class="info-item">
        <text class="info-label">生日</text>
        <text class="info-value">2003/05/06</text>
      </view>
      <view class="info-item">
        <text class="info-label">账号</text>
        <text class="info-value">250614164609735290</text>
      </view>
      <view class="info-item">
        <text class="info-label">手机号</text>
        <text class="info-value">16460973529</text>
      </view>
      <view class="info-item">
        <text class="info-label">身份证号</text>
        <text class="info-value">522256235478965412</text>
      </view>
    </view>

    <view style="display: flex; justify-content: center;margin-top: 100rpx;">
      <view class="submit-btn">确认</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {}
  },
  methods: {
    goBack() {
      uni.navigateBack({
        delta: 1
      });
    },
    submitOrder() {
      // 提交订单逻辑
      uni.showToast({
        title: '提交成功',
        icon: 'success'
      });
    }
  }
}
</script>

<style>
.container {
  background: linear-gradient(to bottom, #000000, #0b0b0b, #fff);
  min-height: 100vh;
  padding-top: 50rpx;
  padding-bottom: 40rpx;
}

.section-title {
  padding-left: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 36rpx;
  font-weight: 700;
  margin: 60rpx 0 70rpx 0;
  /* text-align: center; */
  color: #fff;
}

.avatar-box {
  width: 140rpx;
  height: 140rpx;
  background: #D8D8D8;
  border-radius: 12rpx;
  margin: 0rpx auto 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-icon {
  width: 57rpx;
  height: 49rpx;
}

.title {
  font-size: 36rpx;
  font-weight: 500;
  color: #333;
  text-align: center;
  margin-bottom: 50rpx;
  display: block;
}

.card {
  background: #FFFFFF;
  border-radius: 20rpx;
  margin: 0 32rpx;
  padding: 0 32rpx;
}

.info-item {
  padding: 40rpx 0;
  border-bottom: 1rpx solid #EEEEEE;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.last-item {
  border-bottom: none;
}

.info-label {
  font-size: 28rpx;
  color: #666666;
}

.info-value {
  font-size: 28rpx;
  color: #333;
}

.submit-btn {
  width: 544rpx;
  height: 96rpx;
  background: #B7975E;
  border-radius: 186rpx;
  font-weight: 600;
  font-size: 32rpx;
  color: #FFFFFF;
  line-height: 96rpx;
  text-align: center;
  margin-top: 40rpx;
}

.submit-btn:hover {
  background: #9e7f4a;
}
</style>