<template>
  <view class="container">
    <!-- 顶部标题 -->
    <view class="title">个人中心</view>

    <!-- 用户信息区域 -->
    <view class="user-info">
      <view class="user-left">
        <image class="avatar" src="https://p0.itc.cn/q_70/images03/20230130/f8ad79698b144ccbbcb8aade02e04fc5.jpeg"
          mode="aspectFill" />
      </view>
      <view class="user-center">
        <text class="username">会员1</text>
        <view style="display: flex;align-items: center;" @click="goToEnit">
          <image class="edit-icon2" src="/static/images/mine/编辑.png" mode="aspectFill" />
          <text style="font-size: 28rpx;color: #FBF3E4;margin-left: 20rpx;">编辑信息</text>
        </view>
      </view>
      <view class="user-right">
        <image class="edit-icon1" @click="goToMes" src="/static/images/mine/铃铛.png" mode="aspectFill" />
        <image class="edit-icon" @click="goToConi" src="/static/images/mine/设置.png" mode="aspectFill" />
      </view>
    </view>

    <!-- 功能标签卡片 -->
    <view class="tag-card">
      <view class="tag-item" @click="goToForm(0)">
        <image class="tag-icon" src="/static/images/mine/课程预约.png" mode="aspectFill" />
        <text class="tag-text">课程预约</text>
      </view>
      <view class="tag-item" @click="goToForm(1)">
        <image class="tag-icon" src="/static/images/mine/待完成.png" mode="aspectFill" />
        <text class="tag-text">待完成</text>
      </view>
      <view class="tag-item" @click="goToForm(2)">
        <image class="tag-icon" src="/static/images/mine/已完成.png" mode="aspectFill" />
        <text class="tag-text">已完成</text>
      </view>
      <view class="tag-item" @click="goToForm(3)">
        <image class="tag-icon" src="/static/images/mine/售后.png" mode="aspectFill" />
        <text class="tag-text">退款/售后</text>
      </view>
    </view>

    <!-- 功能列表卡片 -->
    <view class="menu-card">
      <view class="menu-item">
        <image class="menu-icon" src="/static/images/mine/奖杯.png" mode="aspectFill" />
        <text class="menu-text">我的成绩</text>
      </view>

      <view class="menu-item" @click="goToFeed">
        <image class="menu-icon" src="/static/images/mine/我的反馈.png" mode="aspectFill" />
        <text class="menu-text">反馈鞍境</text>
      </view>
      <view class="menu-item">
        <!-- <image class="menu-icon" src="/static/images/mine/客服.png" mode="aspectFill" /> -->
        <text class="menu-text" />
      </view>
      <view class="menu-item">
        <text class="menu-text" />
      </view>
    </view>
  </view>
  <CustomTabbar />
</template>

<script>
import CustomTabbar from '@/components/custom-tabbar/custom-tabbar.vue';
export default {
  components: {
    CustomTabbar
  },
  data() {
    return {
      // 数据可以在这里定义
    }
  },
  methods: {
    goToForm(type) {
      // const type = e.currentTarget.dataset.type;
      console.log('type', type);
      uni.navigateTo({
        url: `/pages/form/form?type=${type}`
      })
    },
    goToMes() {
      uni.navigateTo({ url: '/pages/notice/notice-detail' })
    },
    goToConi() {
      uni.navigateTo({ url: '/pages/notice/notice-index' })
    },
    goToEnit() {
      uni.navigateTo({ url: '/pages/select-people/select-people' })
    },
    goToFeed() {
      uni.navigateTo({ url: '/pages/support/feedback/feedback-form' })
    }
  },
}
</script>

<style>
/* 基础样式 */
page {

  box-sizing: border-box;
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20", sans-serif;
}

.container {
  background: linear-gradient(to bottom, #000000, #0d0d0d, #a5a5a5, #d9d9d9, #fff);
  min-height: 100vh;
  padding: 40rpx 24rpx;
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

/* 标题样式 */
.title {
  margin-top: 80rpx;
  font-weight: 700;
  font-size: 36rpx;
  color: #F5F6FA;
  text-align: center;
  margin-bottom: 40rpx;
}

/* 用户信息区域 */
.user-info {
  display: flex;
  align-items: center;
  padding: 0 20rpx;
  margin-bottom: 40rpx;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid #FFFFFF;
}

.user-center {
  flex: 1;
  margin-left: 30rpx;
}

.username {
  font-weight: 400;
  font-size: 36rpx;
  color: #FBF3E4;
}

.edit-icon {
  width: 30rpx;
  height: 30rpx;
}

.edit-icon2 {
  width: 40rpx;
  height: 40rpx;
}

.edit-icon1 {
  width: 30rpx;
  height: 30rpx;
  margin-right: 28rpx;
}

/* 功能标签卡片 */
.tag-card {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx 20rpx;
  display: flex;
  justify-content: space-between;
  margin-bottom: 30rpx;
}

.tag-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.tag-icon {
  width: 60rpx;
  height: 60rpx;
  margin-bottom: 15rpx;
}

.tag-text {
  font-family: "Roboto Condensed", sans-serif;
  font-weight: 300;
  font-size: 26rpx;
  color: #6B4B12;
}

/* 功能列表卡片 */
.menu-card {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx 20rpx;
  display: flex;
  justify-content: start;
}

.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;

}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  width: 60rpx;
  height: 60rpx;
  margin-right: 20rpx;
}

.menu-text {
  font-weight: 400;
  font-size: 24rpx;
  color: #6B4B12;
}
</style>