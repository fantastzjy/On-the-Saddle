<template>
  <view class="login-container" :style="{ background: bgGradient }">
    <!-- Logo -->
    <view class="logo-box">
      <text class="app-name">{{ appName }}</text>
      <image class="logo" :src="logoUrl" mode="aspectFit" />

    </view>

    <!-- 登录选项 -->
    <view class="login-options">
      <view style="margin-bottom: 20rpx;display: flex;justify-content: center;">
        <uni-data-checkbox selectedColor="#b7975e" selectedTextColor="#b7975e" v-model="role" :localdata="range"
          @change="change" />
      </view>
      <!-- 微信登录 -->
      <button class="login-btn wechat-btn" open-type="getUserInfo" @getuserinfo="onWechatLogin">
        <!-- <image src="/static/images/wechat-icon.png" class="btn-icon"/> -->
        微信授权登录
      </button>

      <!-- 手机号登录 -->
      <!-- <button>
        <image src="/static/images/phone-icon.png" class="btn-icon"/>
        手机号验证登录
      </button> -->
      <view class="phone-btn" @click="navToPhoneLogin">
        手机验证码登录
      </view>
    </view>

    <!-- 用户协议 -->
    <view class="agreement">
      <label class="checkbox" @click="toggleAgreementManual">
        <!-- 使用自定义圆形复选框 -->
        <view class="custom-checkbox" :class="{ checked: agreed }" />
        <text>我已阅读并同意</text>
      </label>
      <text class="link" @click="navToAgreement('user')">《用户注册服务协议》</text>
      <text>与</text>
      <text class="link" @click="navToAgreement('privacy')">《隐私政策》</text>
    </view>
  </view>
</template>

<script>
import { getLoginUserInfo } from '@/api/system/login';
export default {
  data() {
    return {
      // 从后台获取的数据
      bgGradient: 'linear-gradient( 180deg, #000000 0%, #000000 22%, rgba(0,0,0,0.8) 35%, rgba(0,0,0,0.51) 49%, rgba(188,188,188,0.666) 64%, rgba(255,255,255,0.64) 77%)', // 默认值，会被后台数据覆盖
      logoUrl: '/static/logo.png',
      appName: '上海XXX俱乐部',
      role: "usr",
      range: [{ "value": "usr", "text": "用户" }, { "value": 'cc', "text": "教练" }],
      // 用户协议状态
      agreed: false
    }
  },
  onLoad() {
    // this.getLoginConfig()
  },
  methods: {
    change(e) {
      console.log('e:', e);
      this.role = e.detail.value
    },
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
    // 微信登录 - 优化后的逻辑
    async onWechatLogin(e) {
      // 防止重复点击
      if (this.isLogging) return;

      if (!this.agreed) {
        return uni.showToast({ title: '请先同意用户协议', icon: 'none' })
      }

      this.isLogging = true;
      uni.showLoading({ title: '登录中...', mask: true });

      try {
        // 1. 获取微信登录code
        const loginRes = await this.getWechatCode();

        console.log(loginRes);
        // 2. 获取用户信息
        let userInfo = {};
        try {
          userInfo = e.detail.userInfo;
        } catch (error) {
          console.warn('获取用户信息失败，使用默认信息:', error);
          userInfo = {
            avatarUrl: '/static/images/default-avatar.png',
            nickName: '微信用户',
          };
        }

        // 3. 调用登录API
        const res = await this.callLoginApi(loginRes.code, userInfo);

        const loginResult = res.data
        console.log(res);
        uni.showToast({ title: JSON.stringify(res), icon: 'none' });
        // 4. 处理登录结果
        if (res.code === 0) {
          // 存储token
          uni.setStorageSync('token', loginResult.token);
          uni.setStorageSync('userInfo', loginResult || userInfo);

          // 显示登录成功提示
          uni.showToast({ title: '登录成功', icon: 'success' });

          // 跳转到首页
          setTimeout(() => {
            uni.navigateTo({ url: '/pages/support/change-log/change-log-list' });
          }, 1500);
        } else {
          // 登录失败处理
          this.handleLoginError(loginResult.message || '登录失败');
        }
      } catch (error) {
        console.error('登录异常:', error);
        this.handleLoginError(error.message || '登录异常');
      } finally {
        this.isLogging = false;
        uni.hideLoading();
      }
    },

    // 获取微信登录code
    getWechatCode() {
      return new Promise((resolve, reject) => {
        uni.login({
          provider: 'weixin',
          success: resolve,
          fail: reject
        });
      });
    },

    // 调用登录API
    async callLoginApi(code, userInfo) {
      // const res = await uni.request({
      //   url: 'http://192.168.255.6:61812/app/member/login/wxLogin',
      //   method: 'POST',
      //   data: {
      //     "code": code,
      //   }
      // });
      // console.log(res);
      // return res.data;

      const res = await getLoginUserInfo({
        code: code,
        role: this.role
      });
      return res;
      // try {
      //   // 优先使用API模块
      // if (loginApi && loginApi.login) {
      // const res = await loginApi.login({
      //   "code": code
      // });
      // return res.data;
      //   } else {
      //     // 备用方案：直接请求
      //     const res = await uni.request({
      //       url: 'https://192.168.255.6:61812/app/member/wxLogin',
      //       method: 'POST',
      //       data: {
      //         code: "demoData",
      //         userInfo: userInfo
      //       }
      //     });
      //     return res[1].data;
      //   }
      // } catch (error) {
      //   console.error('API请求失败:', error);
      //   throw new Error('网络请求失败', error);
      // }
    },

    // 处理登录错误
    handleLoginError(message) {
      uni.showToast({
        title: message,
        icon: 'none',
        duration: 3000
      });

      // 如果是token失效等特定错误，可以跳转到特定页面
      if (message.includes('失效') || message.includes('过期')) {
        setTimeout(() => {
          uni.navigateTo({ url: '/pages/login/index' });
        }, 2000);
      }
    },

    // 跳转手机号登录
    navToPhoneLogin() {

      uni.navigateTo({ url: '/pages/phone/phone' })
    },

    // 切换协议状态
    toggleAgreement(e) {
      this.agreed = e.detail.value.length > 0
    },
    toggleAgreementManual() {
      this.agreed = !this.agreed;
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
.agreement {

  .custom-checkbox {
    display: inline-block;
    width: 24rpx;
    /* 调整为更小尺寸 */
    height: 24rpx;
    border: 1px solid #ccc;
    border-radius: 50%;
    /* 圆形 */
    margin-right: 10rpx;
    position: relative;
    vertical-align: middle;
    transition: all 0.3s;

    &.checked {
      background-color: #07C160;
      border-color: #07C160;

      &::after {
        content: "";
        position: absolute;
        left: 7rpx;
        top: 3rpx;
        width: 6rpx;
        height: 12rpx;
        border: solid white;
        border-width: 0 2rpx 2rpx 0;
        transform: rotate(45deg);
      }
    }
  }
}

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