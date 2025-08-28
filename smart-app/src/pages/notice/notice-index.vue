<template>
  <view class="container">
    <!-- 标题 -->
    <!-- <view class="header">
      <text class="title">设置</text>
    </view> -->

    <!-- 绑定手机号模块 -->
    <view class="section">
      <view class="card" @click="handleBindPhone">
        <text class="card-text">绑定手机号</text>
        <uni-icons type="right" size="20" />
      </view>
    </view>

    <!-- 账号管理模块 -->
    <view class="section">
      <view class="card" @click="handleCancelAccount">
        <text class="card-text">注销账号</text>
        <uni-icons type="right" size="20" />
      </view>
      <view class="card" @click="handleCheckUpdate">
        <text class="card-text">检查更新</text>
        <view style="display: flex;">
          <view class="version">V1.0</view>
          <uni-icons type="right" size="20" />
        </view>
      </view>
    </view>

    <!-- 用户协议模块 -->
    <view class="section">
      <view class="card" @click="handleUserAgreement">
        <text class="card-text">用户协议及隐私政策</text>
        <uni-icons type="right" size="20" />
      </view>
      <view class="card logout" @click="handleLogout">
        <text class="card-text">退出登录</text>
        <uni-icons type="right" size="20" />
      </view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    handleBindPhone() {
      uni.showToast({ title: '点击绑定手机号', icon: 'none' });
    },
    handleCancelAccount() {
      uni.showToast({ title: '点击注销账号', icon: 'none' });
    },
    handleCheckUpdate() {
      uni.showToast({ title: '当前已是最新版本', icon: 'none' });
    },
    handleUserAgreement() {
      uni.navigateTo({ url: '/pages/agreement/index' });
    },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '退出成功', icon: 'success' });
            uni.navigateTo({ url: '/pages/login/login' });
          }
        }
      });
    }
  }
}
</script>

<style>
.container {
  background-color: #f6f6f6;
  min-height: 100vh;
  padding: 32rpx;
}

.header {
  margin-bottom: 48rpx;
}

.title {
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
}

.section {
  margin-bottom: 40rpx;
}

.section-title {
  display: block;
  font-size: 32rpx;
  color: #999;
  margin-bottom: 24rpx;
  padding-left: 16rpx;
}

.card {
  width: 640rpx;
  height: 100rpx;
  background: #FFFFFF;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32rpx;
  margin-bottom: 24rpx;
}

.card-text {
  font-size: 28rpx;
  color: #666666;
  line-height: 40rpx;
}

.arrow {
  width: 32rpx;
  height: 32rpx;
}

.version {
  font-size: 28rpx;
  color: #999999;
  margin-right: 15rpx;
  padding-left: 32rpx;
}

.logout {
  margin-top: 40rpx;
}
</style>