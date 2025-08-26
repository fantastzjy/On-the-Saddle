<template>
  <view class="login-container" :style="{ background: bgGradient }">
    <!-- Logo -->
    <view class="logo-box">
      <text class="app-name">{{ appName }}</text>
      <image class="logo" :src="logoUrl" mode="aspectFit" />

    </view>

    <!-- 登录选项 -->
    <view class="login-options">
      <!-- 微信登录 -->
      <button class="login-btn wechat-btn" open-type="getUserInfo" @getuserinfo="onWechatLogin">
        <!-- <image src="/static/images/wechat-icon.png" class="btn-icon"/> -->
        微信一键登录
      </button>

      <!-- 手机号登录 -->
      <!-- <button>
        <image src="/static/images/phone-icon.png" class="btn-icon"/>
        手机号验证登录
      </button> -->
      <view class="phone-btn" @click="navToPhoneLogin">
        手机号验证登录
      </view>
    </view>

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
      // 从后台获取的数据
      bgGradient: 'linear-gradient(to bottom, #040404,#555555,#ececec, #ffffff)', // 默认值，会被后台数据覆盖
      logoUrl: '/static/logo.png',
      appName: '上海XXX俱乐部',

      // 用户协议状态
      agreed: false
    }
  },
  onLoad() {
    this.getLoginConfig()
  },
  methods: {
    // 从后台获取登录配置
    async getLoginConfig() {
      try {
        const res = await uni.request({
          url: 'https://your-api.com/login/config',
          method: 'GET'
        })

        const data = res[1].data.data
        this.bgGradient = `linear-gradient(to bottom, ${data.colorStart}, ${data.colorEnd})`
        this.logoUrl = data.logoUrl
        this.appName = data.appName

      } catch (err) {
        console.error('获取登录配置失败:', err)
        uni.showToast({ title: '加载配置失败', icon: 'none' })
      }
    },

    // 微信登录
    onWechatLogin(e) {
      if (!this.agreed) {
        return uni.showToast({ title: '请先同意用户协议', icon: 'none' })
      }

      uni.showLoading({ title: '登录中...' })

      // 获取微信用户信息
      const userInfo = e.detail.userInfo
      uni.navigateTo({ url: '/pages/support/change-log/change-log-list' })
      // 调用后台接口
      // uni.login({
      //   provider: 'weixin',
      //   success: async (loginRes) => {
      //     try {
      //       const res = await uni.request({
      //         url: 'https://your-api.com/login/wechat',
      //         method: 'POST',
      //         data: {
      //           code: loginRes.code,
      //           userInfo: userInfo
      //         }
      //       })

      //       uni.hideLoading()

      //       if (res[1].data.code === 200) {
      //         uni.setStorageSync('token', res[1].data.token)
      //         uni.switchTab({ url: '/pages/home/index' })
      //       } else {
      //         uni.showToast({ title: res[1].data.message, icon: 'none' })
      //       }
      //     } catch (err) {
      //       uni.hideLoading()
      //       uni.showToast({ title: '登录失败', icon: 'none' })
      //     }
      //   },
      //   fail: () => {
      //     uni.hideLoading()
      //     uni.showToast({ title: '微信登录失败', icon: 'none' })
      //   }
      // })
    },

    // 跳转手机号登录
    navToPhoneLogin() {
      if (!this.agreed) {
        return uni.showToast({ title: '请先同意用户协议', icon: 'none' })
      }
      uni.navigateTo({ url: '/pages/phone/phone' })
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
  padding: 0 60rpx;
  box-sizing: border-box;
  transition: background 0.5s;

  .logo-box {
    margin-top: 338rpx;
    display: flex;
    flex-direction: column;
    align-items: center;

    .logo {
      width: 124rpx;
      height: 124rpx;
      margin-top: 240rpx;
    }

    .app-name {
      margin-top: 30rpx;
      font-size: 60rpx;
      color: #d5bc84;
      font-weight: bold;
      font-family: 'Alibaba PuHuiTi 2.0-65 Me';
    }
  }

  .login-options {
    width: 100%;
    margin-top: 364rpx;

    .login-btn {
      width: 414rpx;
      height: 96rpx;
      border-radius: 45rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 40rpx;
      color: #fff;
      margin-bottom: 40rpx;
      border: none;

      .btn-icon {
        width: 40rpx;
        height: 40rpx;
        margin-right: 15rpx;
      }

      &.wechat-btn {
        background: #b7975e;
        font-size: 36rpx;
      }

    }

    .phone-btn {
      text-align: center;
      color: #999;
      font-size: 26rpx;
      // background: #d5d5d5 // border: 1rpx solid rgba(255, 255, 255, 0.5);
    }
  }

  .agreement {
    position: absolute;
    bottom: 100rpx;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    justify-content: center;
    color: #999;
    font-size: 24rpx;

    .checkbox {
      display: flex;
      align-items: center;
      margin-right: 5rpx;
    }

    .link {
      color: #a0762c;
      // text-decoration: underline;
      margin: 0 5rpx;
    }
  }
}
</style>