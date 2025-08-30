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
import { useUserStore } from '@/store/modules/system/user';
import { USER_TOKEN } from '@/constants/local-storage-key-const';
import { CLUB_CONFIG } from '@/lib/config.js';
import { memberApi } from '@/api/member/member';
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
        console.log('登录API响应:', res);
        console.log('登录结果数据:', loginResult);
        
        // 4. 处理登录结果 - 使用双重验证确保成功判断
        if ((res.code === 0 || res.code === 1) && res.ok === true && loginResult) {
          console.log('登录成功，开始处理登录结果');
          await this.handleLoginSuccess(loginResult);
        } else {
          // 登录失败处理
          console.log('登录失败，code:', res.code, 'ok:', res.ok, 'msg:', res.msg);
          this.handleLoginError(res.msg || loginResult?.message || '登录失败');
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
      //   url: 'http://192.168.43.220:61812/app/member/login/wxLogin',
      //   method: 'POST',
      //   data: {
      //     "code": code,
      //   }
      // });
      // console.log(res);
      // return res.data;

      const res = await getLoginUserInfo({
        code: code,
        role: this.role,
        clubCode: CLUB_CONFIG.CLUB_CODE  // 新增俱乐部编码
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
      //       url: 'https://192.168.43.220:61812/app/member/wxLogin',
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
      console.error('登录失败:', message);
      
      const errorMsgs = {
        'invalid_code': '微信授权码已过期，请重试',
        'user_not_exist': '用户不存在，请联系管理员',
        'system_error': '系统错误，请稍后重试',
        '会员不存在，请先注册': '账号未注册，请先注册账号',
        '账号已被禁用': '您的账号已被停用，请联系管理员',
        '账号未激活': '账号未激活，请联系管理员'
      };
      
      const displayMessage = errorMsgs[message] || message || '登录失败';
      
      uni.showModal({
        title: '登录失败',
        content: displayMessage,
        showCancel: false,
        confirmText: '知道了'
      });
    },

    // 登录成功后的处理
    async handleLoginSuccess(loginResult) {
      const { token, role, isFirstLogin } = loginResult;
      
      try {
        // 1. 存储基本登录信息
        console.log('🔐 [登录调试] 开始存储token到:', USER_TOKEN);
        console.log('🔐 [登录调试] 存储的token值:', token);
        uni.setStorageSync(USER_TOKEN, token);
        
        // 验证存储是否成功
        const storedToken = uni.getStorageSync(USER_TOKEN);
        console.log('🔐 [登录调试] 存储后立即读取的token:', storedToken);
        console.log('🔐 [登录调试] 存储验证:', storedToken === token ? '✅ 成功' : '❌ 失败');
        
        // 2. 更新Store基础信息
        useUserStore().setBasicLoginInfo({ token, role, isFirstLogin });
        
        // 3. 获取详细用户信息（如果是会员）
        if (role === 'usr') {
          console.log('会员登录，开始获取详细用户信息');
          try {
            const userInfoRes = await this.getUserDetailInfo();
            console.log('getUserDetailInfo返回结果:', userInfoRes);
            
            // 验证返回数据的完整性
            if (userInfoRes && userInfoRes.data) {
              const detailUserInfo = userInfoRes.data;
              console.log('解析出的用户详细信息:', detailUserInfo);
              
              // 4. 更新Store详细信息
              console.log('开始更新Store用户详细信息');
              useUserStore().setDetailUserInfo(detailUserInfo);
              
              // 存储到本地
              uni.setStorageSync('userInfo', detailUserInfo);
              console.log('用户详细信息存储到本地成功');
              
              console.log('用户详细信息获取和存储完成');
            } else {
              console.warn('用户详细信息数据结构异常:', userInfoRes);
              // 显示警告但不阻止登录
              uni.showToast({
                title: '用户信息获取异常，部分功能可能受限',
                icon: 'none',
                duration: 3000
              });
            }
          } catch (error) {
            console.error('获取用户详细信息失败:', error);
            console.error('错误类型:', typeof error);
            console.error('错误消息:', error.message);
            
            // 显示用户友好的错误提示，但不阻止登录
            uni.showToast({
              title: '获取用户信息失败，部分功能可能受限',
              icon: 'none',
              duration: 3000
            });
          }
        } else {
          console.log('教练登录，跳过获取用户详细信息');
        }
        
        // 5. 显示登录成功提示
        uni.showToast({ title: '登录成功', icon: 'success' });
        
        // 6. 处理跳转
        setTimeout(() => {
          if (isFirstLogin) {
            this.handleFirstLogin();
          } else {
            this.smartRedirect();
          }
        }, 1500);
        
      } catch (error) {
        console.error('登录后处理失败:', error);
        uni.showToast({ title: '登录处理失败', icon: 'none' });
      }
    },

    // 获取用户详细信息
    async getUserDetailInfo() {
      console.log('开始获取用户详细信息');
      try {
        console.log('使用静态导入的memberApi:', memberApi);
        
        // 验证memberApi是否正确导入
        if (!memberApi) {
          throw new Error('memberApi静态导入失败');
        }
        
        if (!memberApi.getUserInfo) {
          throw new Error('memberApi.getUserInfo方法不存在');
        }
        
        console.log('开始调用memberApi.getUserInfo');
        const result = await memberApi.getUserInfo({});
        console.log('用户详情API调用成功:', result);
        
        if (!result) {
          console.warn('用户详情API返回结果为空');
          throw new Error('用户详情数据为空');
        }
        
        if (!result.data) {
          console.warn('用户详情data字段为空:', result);
        }
        
        return result;
      } catch (error) {
        console.error('获取用户详细信息失败:', error);
        console.error('错误详情:', {
          message: error.message,
          stack: error.stack,
          response: error.response
        });
        throw error; // 重新抛出便于上层处理
      }
    },

    // 首次登录处理
    handleFirstLogin() {
      uni.showModal({
        title: '欢迎使用',
        content: '检测到您是首次登录，您可以稍后在个人中心完善个人信息',
        showCancel: false,
        confirmText: '知道了',
        success: () => {
          this.smartRedirect();
        }
      });
    },

    // 智能跳转逻辑 -> 直接跳转首页
    smartRedirect() {
      console.log('🏠 [跳转] 登录成功，强制跳转到首页');
      uni.switchTab({ 
        url: '/pages/home/index',
        success: () => console.log('🏠 [跳转] 跳转首页成功'),
        fail: (error) => {
          console.error('🏠 [跳转] switchTab失败:', error);
          // 兜底方案：使用reLaunch
          console.log('🏠 [跳转] 使用reLaunch兜底方案');
          uni.reLaunch({ 
            url: '/pages/home/index',
            success: () => console.log('🏠 [跳转] reLaunch跳转成功'),
            fail: (err) => console.error('🏠 [跳转] reLaunch也失败:', err)
          });
        }
      });
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