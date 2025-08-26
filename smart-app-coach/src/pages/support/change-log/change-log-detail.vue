<template>
  <view class="container">
    <!-- 顶部图片区域 -->
    <image class="header-image" src="/static/images/index/背景.png" mode="widthFix" />
    <view class="tu-top">
      <uni-icons @click="goBack" type="left" size="25" />
      <text />
    </view>
    <!-- 白色背景区域 -->
    <view class="white-bg" />

    <!-- 教练信息卡片 -->
    <view class="coach-card">
      <!-- 头像和时间 -->
      <view class="coach-card1">
        <view class="top-section">
          <image class="avatar" src="https://pic.rmb.bdstatic.com/bjh/down/ecbac0b101032a0c2cb9109194e163f0.jpeg" />
          <text class="coach-name">教练1团</text>
          <image style="height: 22rpx;width: 22rpx;" src="/static//images/index/sex.png" mode="scaleToFill" />
        </view>

        <!-- 教练基本信息 -->
        <view class="coach-info">

          <view class="badges">
            <text class="badge">中码协中一级骑手证</text>
            <text class="badge">3星教练证</text>
          </view>
        </view>

        <!-- 标签区域 -->
        <view class="tags-section">
          <view class="tag-round">#中级进阶</view>
          <view class="tag-round">#赛事指导</view>
        </view>
      </view>

      <!-- 教练介绍卡片 -->
      <view class="intro-card">
        <text class="intro-title">教练介绍</text>
        <text class="intro-content">
          从业xx年，教学学员200+，xx比赛xx级别第xx名。
          20xx年，xx名学员通过中马协场地障碍x级
        </text>
      </view>

      <!-- 底部信息 -->
      <view class="bottom-info">
        <text class="students">1鞍时﹣1人</text>
        <view style="display: flex;align-items: center;">
          <text class="price">
            <text class="price1">￥</text>000</text>
          <button class="appoint-btn">约Ta</button>
        </view>
      </view>
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
  }
}
</script>

<style>
.container {
  position: relative;
  min-height: 100vh;
  background-color: #f6f6f6;
}

.header-image {
  width: 100%;
  height: 600rpx;
  /* 根据实际图片比例调整 */
}

.white-bg {
  height: 300rpx;
  background-color: #FFFFFF;
}

.tu-top {
  position: absolute;
  padding-left: 20rpx;
  top: 112rpx;
  display: flex;
  justify-content: space-between;
}

.coach-card {
  position: absolute;
  top: 440rpx;
  left: 30rpx;
  right: 30rpx;
  z-index: 2;
}

.coach-card1 {
  /* width: 706rpx; */
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(255, 255, 255, 0.45);
  border-radius: 16rpx;
  padding: 30rpx 24rpx 60rpx 24rpx;
  z-index: 2;
}

.top-section {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
}

.time {
  font-size: 24rpx;
  color: #999999;
}

.coach-info {
  margin-bottom: 20rpx;
}

.coach-name {
  font-weight: 500;
  font-size: 28rpx;
  color: #333333;
  margin-right: 20rpx;
  margin-left: 20rpx;
}

.badges {
  display: flex;
  margin-top: 15rpx;
}

.badge {
  font-size: 22rpx;
  color: #666;
  margin-right: 15rpx;
  padding: 5rpx 10rpx;
  background-color: #F6F6F6;
  border-radius: 6rpx;
}

.tags-section {
  display: flex;
  margin-bottom: 30rpx;
}

.tag-round {
  background: #F6F6F6;
  border-radius: 182rpx;
  font-size: 24rpx;
  color: #333333;
  line-height: 22rpx;
  padding: 10rpx 20rpx;
  margin-right: 15rpx;
}

.intro-card {
  height: 500rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(255, 255, 255, 0.45);
  border-radius: 16rpx;
  padding: 30rpx;
  margin-top: 20rpx;
}

.intro-title {
  font-weight: 500;
  font-size: 30rpx;
  color: #333333;
  display: block;
  margin-bottom: 20rpx;
}

.intro-content {
  font-weight: 400;
  font-size: 28rpx;
  color: #333333;
  line-height: 40rpx;
}

.bottom-info {
  height: 92rpx;
  background: linear-gradient(to right, #f9eedb, #fefefd);
  border-radius: 16rpx;
  border: 1rpx solid #6b4b12;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30rpx;
  margin-top: 30rpx;
}

.students {
  font-size: 24rpx;
  color: #6b4b12;
}

.price {
  font-size: 28rpx;
  color: #6b4b12;
  margin-right: 20rpx;
}

.price1 {
  font-size: 20rpx;
  color: #6b4b12;
}

.appoint-btn {
  background-color: #6b4b12;
  font-weight: 600;
  font-size: 24rpx;
  color: #FFFFFF;
  border-radius: 40rpx;
  width: 108rpx;
  text-align: center;
  height: 48rpx;
  line-height: 48rpx;
}
</style>