<template>
  <view class="login-container">
    <!-- 顶部标题 -->
    <view class="login-header">
      <text class="title">用户登录</text>
    </view>

    <!-- 表单区域 -->
    <view class="form-container">
      <uni-forms ref="form" :model="formData" :rules="rules">
        <!-- 用户名输入 -->
        <uni-forms-item name="username">
          <uni-easyinput 
            v-model="formData.username" 
            placeholder="请输入用户名" 
            prefixIcon="person"
          />
        </uni-forms-item>

        <!-- 密码输入 -->
        <uni-forms-item name="password">
          <uni-easyinput 
            type="password" 
            v-model="formData.password" 
            placeholder="请输入密码" 
            prefixIcon="locked"
          />
        </uni-forms-item>

        <!-- 登录按钮 -->
        <button 
          type="primary" 
          class="login-btn" 
          @click="handleLogin"
          :loading="loading"
        >登录</button>
      </uni-forms>

      <!-- 注册提示 -->
      <view class="register-tip">
        没有账号？<text class="link" @click="navToRegister">立即注册</text>
      </view>
    </view>
  </view>
</template>

<script>
import { USER_TOKEN } from '@/constants/local-storage-key-const';

export default {
  data() {
    return {
      loading: false,
      formData: {
        username: '',
        password: ''
      },
      rules: {
        username: {
          rules: [{
            required: true,
            errorMessage: '请输入用户名'
          }]
        },
        password: {
          rules: [{
            required: true,
            errorMessage: '请输入密码'
          }, {
            minLength: 6,
            maxLength: 16,
            errorMessage: '密码长度在6-16个字符之间'
          }]
        }
      }
    }
  },
  methods: {
    // 登录方法
    async handleLogin() {
      try {
        // 表单验证
        await this.$refs.form.validate()
        
        this.loading = true
        // 模拟登录请求
        const res = await uni.request({
          url: 'https://your-api.com/login',
          method: 'POST',
          data: this.formData
        })
        
        // 登录成功处理
        if (res[1].data.code === 200) {
          uni.showToast({ title: '登录成功', icon: 'success' })
          // 存储token
          uni.setStorageSync(USER_TOKEN, res[1].data.token)
          // 跳转到首页
          uni.switchTab({ url: '/pages/home/index' })
        } else {
          uni.showToast({ title: res[1].data.message, icon: 'none' })
        }
      } catch (err) {
        console.error('登录失败:', err)
      } finally {
        this.loading = false
      }
    },
    
    // 跳转注册页
    navToRegister() {
      uni.navigateTo({ url: '/pages/register/register' })
    }
  }
}
</script>

<style lang="scss">
.login-container {
  padding: 40rpx;
  .login-header {
    margin: 100rpx 0 60rpx;
    .title {
      font-size: 48rpx;
      font-weight: bold;
      color: #333;
    }
  }
  .form-container {
    .login-btn {
      margin-top: 60rpx;
      height: 90rpx;
      line-height: 90rpx;
      border-radius: 45rpx;
    }
    .register-tip {
      margin-top: 30rpx;
      text-align: center;
      color: #999;
      .link {
        color: #2979ff;
        margin-left: 10rpx;
      }
    }
  }
}
</style>