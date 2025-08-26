<template>
  <view class="login-container" :style="{ background: bgGradient }">
    <!-- Logo -->
    <view class="logo-box">
      <text class="app-name">{{ appName }}</text>
      <image class="logo" :src="logoUrl" mode="aspectFit" />
    </view>

    <!-- 手机号登录表单 -->
    <view class="login-form">
      <view class="login-text">请输入手机号</view>
      <view class="input-group">
        <input class="input" type="number" v-model="phone" placeholder="请输入手机号" maxlength="11" />
      </view>
      <view class="login-text">请输入验证码</view>
      <view class="input-group">
        <input class="input" type="number" v-model="smsCode" placeholder="请输入验证码" maxlength="6" />
        <button class="sms-btn" :disabled="countdown > 0" @click="getSmsCode">
          {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
        </button>
      </view>

      <button class="login-btn phone-login-btn" @click="onPhoneLogin">
        立即登录
      </button>

      <view class="back-to-wechat" @click="showPhoneLogin">
        <text>微信授权登录</text>
      </view>
    </view>

    <!-- 微信登录选项 -->
    <!-- <view class="login-options" v-else> -->
    <!-- 微信登录按钮 -->
    <!-- <button class="login-btn wechat-btn" open-type="getUserInfo" @getuserinfo="onWechatLogin"
        @click="handleWechatAuth">
        <image src="/static/images/wechat-icon.png" class="btn-icon" v-if="showWechatIcon" />
        微信一键登录
      </button> -->

    <!-- 手机号登录入口 -->
    <!-- <view class="phone-btn" @click="showPhoneLogin = true">
        手机号验证登录
      </view> -->
    <!-- </view> -->

    <!-- 用户协议 -->
    <view class="agreement">
      <label class="checkbox">
        <checkbox-group @change="toggleAgreement">
          <checkbox :checked="agreed" color="#07C160" />
        </checkbox-group>
        <text>我已阅读并同意</text>
      </label>
      <text class="link" @click="navToAgreement('user')">《用户注册服务协议》</text>
      <text>与</text>
      <text class="link" @click="navToAgreement('privacy')">《隐私政策》</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 应用配置
      bgGradient: 'linear-gradient(to bottom, #040404,#555555,#ececec, #ffffff)',
      logoUrl: '/static/logo.png',
      appName: '上海XXX俱乐部',
      showWechatIcon: true,

      // 用户协议状态
      agreed: false,

      // 手机号登录相关
      // showPhoneLogin: false,
      phone: '',
      smsCode: '',
      countdown: 0,
      countdownTimer: null,

      // 微信登录相关
      wechatAuthLoading: false
    }
  },
  onLoad() {
    this.getLoginConfig()
    // 检查是否已登录
    this.checkLoginStatus()
  },
  onUnload() {
    // 清除倒计时
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer)
    }
  },
  methods: {
    // 检查登录状态
    checkLoginStatus() {
      const token = uni.getStorageSync('token')
      if (token) {
        uni.switchTab({ url: '/pages/home/index' })
      }
    },
    showPhoneLogin() {
      uni.navigateTo({
        url: '/pages/login/login'
      })
    },
    // 获取应用配置
    async getLoginConfig() {
      try {
        // 这里替换为您的实际API
        const res = await uni.request({
          url: 'https://your-api.com/login/config',
          method: 'GET'
        })

        const data = res[1].data.data
        this.bgGradient = `linear-gradient(to bottom, ${data.colorStart}, ${data.colorEnd})`
        this.logoUrl = data.logoUrl || this.logoUrl
        this.appName = data.appName || this.appName
        this.showWechatIcon = data.showWechatIcon !== false

      } catch (err) {
        console.error('获取登录配置失败:', err)
      }
    },

    // 获取短信验证码
    async getSmsCode() {
      if (!this.phone) {
        return uni.showToast({ title: '请输入手机号', icon: 'none' })
      }

      if (!/^1[3-9]\d{9}$/.test(this.phone)) {
        return uni.showToast({ title: '手机号格式不正确', icon: 'none' })
      }

      uni.showLoading({ title: '发送中...' })

      try {
        // 替换为您的短信接口
        const res = await uni.request({
          url: 'https://your-api.com/sms/send',
          method: 'POST',
          data: {
            phone: this.phone,
            type: 'login'
          }
        })

        uni.hideLoading()

        if (res[1].data.code === 200) {
          uni.showToast({ title: '验证码已发送', icon: 'none' })
          this.startCountdown()
        } else {
          uni.showToast({ title: res[1].data.message || '发送失败', icon: 'none' })
        }
      } catch (err) {
        uni.hideLoading()
        uni.showToast({ title: '发送失败', icon: 'none' })
      }
    },

    // 开始倒计时
    startCountdown() {
      this.countdown = 60
      this.countdownTimer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          clearInterval(this.countdownTimer)
        }
      }, 1000)
    },

    // 手机号登录
    async onPhoneLogin() {
      if (!this.agreed) {
        return uni.showToast({ title: '请先同意用户协议', icon: 'none' })
      }

      if (!this.phone) {
        return uni.showToast({ title: '请输入手机号', icon: 'none' })
      }

      if (!this.smsCode) {
        return uni.showToast({ title: '请输入验证码', icon: 'none' })
      }

      if (!/^\d{6}$/.test(this.smsCode)) {
        return uni.showToast({ title: '验证码格式不正确', icon: 'none' })
      }

      uni.showLoading({ title: '登录中...' })

      try {
        // 替换为您的登录接口
        const res = await uni.request({
          url: 'https://your-api.com/login/phone',
          method: 'POST',
          data: {
            phone: this.phone,
            smsCode: this.smsCode
          }
        })

        uni.hideLoading()

        if (res[1].data.code === 200) {
          // 登录成功处理
          this.handleLoginSuccess(res[1].data)
        } else {
          uni.showToast({ title: res[1].data.message || '登录失败', icon: 'none' })
        }
      } catch (err) {
        uni.hideLoading()
        uni.showToast({ title: '登录失败', icon: 'none' })
      }
    },

    // 处理微信授权
    handleWechatAuth() {
      if (!this.agreed) {
        uni.showToast({ title: '请先同意用户协议', icon: 'none' })
        return false
      }
      return true
    },

    // 微信登录
    async onWechatLogin(e) {
      if (!this.handleWechatAuth()) return

      if (this.wechatAuthLoading) return
      this.wechatAuthLoading = true

      uni.showLoading({ title: '登录中...', mask: true })

      try {
        // 1. 获取微信code
        const loginRes = await new Promise((resolve, reject) => {
          uni.login({
            provider: 'weixin',
            success: resolve,
            fail: reject
          })
        })

        // 2. 获取用户信息
        const userInfo = e.detail.userInfo

        // 3. 调用后端登录接口
        const res = await uni.request({
          url: 'https://your-api.com/login/wechat',
          method: 'POST',
          data: {
            code: loginRes.code,
            encryptedData: e.detail.encryptedData,
            iv: e.detail.iv,
            userInfo: {
              nickName: userInfo.nickName,
              avatarUrl: userInfo.avatarUrl,
              gender: userInfo.gender
            }
          }
        })

        if (res[1].data.code === 200) {
          // 登录成功处理
          this.handleLoginSuccess(res[1].data)
        } else {
          uni.showToast({ title: res[1].data.message || '登录失败', icon: 'none' })
        }
      } catch (err) {
        console.error('微信登录失败:', err)
        uni.showToast({ title: '登录失败', icon: 'none' })
      } finally {
        this.wechatAuthLoading = false
        uni.hideLoading()
      }
    },

    // 登录成功处理
    handleLoginSuccess(data) {
      // 存储token和用户信息
      uni.setStorageSync('token', data.token)
      if (data.userInfo) {
        uni.setStorageSync('userInfo', data.userInfo)
      }

      // 跳转到首页
      uni.switchTab({
        url: '/pages/home/index',
        success: () => {
          // 登录成功提示
          uni.showToast({ title: '登录成功', icon: 'success' })
        },
        fail: (err) => {
          console.error('跳转失败:', err)
        }
      })
    },

    // 切换协议状态
    toggleAgreement(e) {
      this.agreed = e.detail.value.length > 0
    },

    // 查看协议
    navToAgreement(type) {
      const url = type === 'user'
        ? '/pages/agreement/user'
        : '/pages/agreement/privacy'
      uni.navigateTo({ url })
    }
  }
}
</script>

<style lang="scss">
.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 24rpx;
  box-sizing: border-box;
  transition: background 0.5s;

  .logo-box {
    margin-top: 300rpx;
    display: flex;
    flex-direction: column;
    align-items: center;

    .logo {
      width: 124rpx;
      height: 124rpx;
      margin-top: 160rpx;
    }

    .app-name {
      margin-top: 30rpx;
      font-size: 60rpx;
      color: #d5bc84;
      font-weight: bold;
      font-family: 'Alibaba PuHuiTi 2.0-65 Me';
    }
  }

  .login-form {
    width: 100%;
    margin-top: 92rpx;

    .login-text {
      font-family: Source Han Sans, Source Han Sans;
      font-weight: 400;
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.8);
      ;
      margin-bottom: 18rpx;
      margin-left: 5rpx;
      text-align: left;
      font-style: normal;
      text-transform: none;
    }

    .input-group {
      position: relative;
      width: 700rpx;
      height: 96rpx;
      background: rgba(255, 255, 255, 0.33);
      border-radius: 16rpx;
      margin-bottom: 40rpx;
      // border-bottom: 1rpx solid #eee;
      display: flex;
      align-items: center;

      .input {
        flex: 1;
        height: 96rpx;
        font-size: 32rpx;
        padding: 0 20rpx;
      }

      .sms-btn {
        background: transparent;
        color: #B1925A;
        // background-color: #b10e0e;
        width: 173rpx;
        height: 70rpx;
        border-radius: 8rpx;
        border: 2rpx solid #B1925A;
        font-size: 24rpx;
        // border: none;
        padding: 0 10rpx;
        // font-style: normal;
        // text-transform: none;

        &::after {
          border: none;
        }

        &[disabled] {
          color: #999;
        }
      }
    }

    .phone-login-btn {
      width: 100%;
      height: 96rpx;
      line-height: 96rpx;
      border-radius: 48rpx;
      background: linear-gradient(to right, #b7975e, #d4b97d);
      color: #fff;
      font-size: 36rpx;
      margin-top: 60rpx;
      border: none;
      box-shadow: 0 10rpx 20rpx rgba(183, 151, 94, 0.2);

      &:active {
        opacity: 0.9;
      }
    }

    .back-to-wechat {
      text-align: center;
      margin-top: 40rpx;
      color: #999;
      font-size: 28rpx;

      text {
        padding: 10rpx 20rpx;
      }
    }
  }

  .login-options {
    width: 100%;
    margin-top: 120rpx;
    display: flex;
    flex-direction: column;
    align-items: center;

    .login-btn {
      width: 100%;
      height: 96rpx;
      line-height: 96rpx;
      border-radius: 48rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 36rpx;
      color: #fff;
      margin-bottom: 40rpx;
      border: none;
      box-shadow: 0 10rpx 20rpx rgba(0, 0, 0, 0.1);

      &.wechat-btn {
        background: linear-gradient(to right, #09bb07, #2ad61a);
      }

      .btn-icon {
        width: 44rpx;
        height: 44rpx;
        margin-right: 15rpx;
      }
    }

    .phone-btn {
      color: #b7975e;
      font-size: 28rpx;
      padding: 20rpx 40rpx;

      &:active {
        opacity: 0.8;
      }
    }
  }

  .agreement {
    position: absolute;
    bottom: 80rpx;
    left: 0;
    right: 0;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    justify-content: center;
    color: #999;
    font-size: 24rpx;
    padding: 0 60rpx;

    .checkbox {
      display: flex;
      align-items: center;
      margin-right: 5rpx;
    }

    .link {
      color: #b7975e;
      margin: 0 5rpx;
    }
  }
}
</style>