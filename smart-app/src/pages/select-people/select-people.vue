<template>
  <view class="container">
    <!-- 头像方块 -->
    <view class="avatar-box" @click="chooseAvatar">
      <image class="avatar-icon1" v-if="avatarUrl" :src="avatarUrl" />
      <image class="avatar-icon" v-else src="/static/images/mine/photo.png" />
      <view class="avatar-edit-tip">点击更换头像</view>
    </view>

    <!-- 信息卡片 -->
    <view class="card">
      <view class="info-item">
        <text class="info-label">建档门店</text>
        <input class="info-value input-field" v-model="userInfo.clubName" placeholder="请输入建档门店" />
      </view>

      <view class="info-item">
        <text class="info-label">是否为会籍会员</text>
        <view class="radio-group">
          <label class="radio-label">
            <radio value="1" :checked="userInfo.isMembership == 1" @click="userInfo.isMembership = 1" />
            <text>是</text>
          </label>
          <label class="radio-label">
            <radio value="0" :checked="userInfo.isMembership == 0" @click="userInfo.isMembership = 0" />
            <text>否</text>
          </label>
        </view>
      </view>

      <view class="info-item">
        <text class="info-label">性别</text>
        <view class="radio-group">
          <label class="radio-label">
            <radio value="1" :checked="userInfo.gender == 1" @click="userInfo.gender = 1" />
            <text>男</text>
          </label>
          <label class="radio-label">
            <radio value="0" :checked="userInfo.gender == 0" @click="userInfo.gender = 0" />
            <text>女</text>
          </label>
        </view>
      </view>

      <view class="info-item">
        <text class="info-label">账号</text>
        <input class="info-value input-field" v-model="userInfo.memberNo" placeholder="请输入账号" />
      </view>

      <view class="info-item">
        <text class="info-label">姓名</text>
        <input class="info-value input-field" v-model="userInfo.actualName" placeholder="请输入姓名" />
      </view>

      <view class="info-item">
        <text class="info-label">手机号</text>
        <input class="info-value input-field" v-model="userInfo.phone" placeholder="请输入手机号" type="number" />
      </view>

      <view class="info-item">
        <text class="info-label">生日</text>
        <picker mode="date" :value="userInfo.birthDate" @change="onBirthDateChange">
          <view class="info-value input-field">{{ userInfo.birthDate || '请选择生日' }}</view>
        </picker>
      </view>

      <view class="info-item">
        <text class="info-label">骑手证号</text>
        <input class="info-value input-field" v-model="userInfo.riderCertNo" placeholder="请输入骑手证号" />
      </view>

      <view class="info-item">
        <text class="info-label">身份证号</text>
        <input class="info-value input-field" v-model="userInfo.idCardNo" placeholder="请输入身份证号" />
      </view>

      <view class="info-item last-item">
        <text class="info-label">会籍有效期</text>
        <picker mode="date" :value="userInfo.membershipExpireDate" @change="onExpireDateChange">
          <view class="info-value input-field">{{ userInfo.membershipExpireDate || '请选择有效期' }}</view>
        </picker>
      </view>
    </view>

    <view style="display: flex; justify-content: center;margin-top: 100rpx;">
      <view class="submit-btn" @click="confirmSubmit">确认保存</view>
    </view>
  </view>
</template>

<script>
import { getUserInfo, updateUserInfo, uploadAvatar } from '@/api/user/user';

export default {
  data() {
    return {
      userInfo: {
        clubName: '',
        isMembership: 0,
        gender: 0,
        memberNo: '',
        actualName: '',
        phone: '',
        birthDate: '',
        riderCertNo: '',
        idCardNo: '',
        membershipExpireDate: ''
      },
      avatarUrl: 'https://img0.baidu.com/it/u=986046126,1988872878&fm=253&app=138&f=JPEG?w=500&h=500',
      isEditing: false
    }
  },
  async onLoad() {
    await this.getUser();
  },
  methods: {
    async getUser() {
      try {
        const res = await getUserInfo();
        if (res.code === 0) {
          this.userInfo = { ...this.userInfo, ...res.data };
          // 如果有头像URL，使用服务器返回的头像
          if (res.data.avatarUrl) {
            this.avatarUrl = res.data.avatarUrl;
          }
        }
      } catch (error) {
        console.error('获取用户信息失败', error);
        uni.showToast({
          title: '获取信息失败',
          icon: 'none'
        });
      }
    },

    // 选择头像
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: async (res) => {
          const tempFilePath = res.tempFilePaths[0];
          this.avatarUrl = tempFilePath;

          // 上传头像到服务器
          try {
            const uploadRes = await uploadAvatar(tempFilePath);
            if (uploadRes.code === 0) {
              uni.showToast({
                title: '头像上传成功',
                icon: 'success'
              });
              this.userInfo.avatarUrl = uploadRes.data.avatarUrl;
            }
          } catch (error) {
            console.error('头像上传失败', error);
            uni.showToast({
              title: '头像上传失败',
              icon: 'none'
            });
          }
        }
      });
    },

    // 生日选择变化
    onBirthDateChange(e) {
      this.userInfo.birthDate = e.detail.value;
    },

    // 有效期选择变化
    onExpireDateChange(e) {
      this.userInfo.membershipExpireDate = e.detail.value;
    },

    // 确认提交
    async confirmSubmit() {
      // 表单验证
      if (!this.userInfo.actualName) {
        uni.showToast({
          title: '请输入姓名',
          icon: 'none'
        });
        return;
      }

      if (!this.userInfo.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        });
        return;
      }

      // 手机号格式验证
      const phoneRegex = /^1[3-9]\d{9}$/;
      if (!phoneRegex.test(this.userInfo.phone)) {
        uni.showToast({
          title: '请输入正确的手机号',
          icon: 'none'
        });
        return;
      }

      // 身份证号格式验证（如果填写了）
      if (this.userInfo.idCardNo && !this.validateIdCard(this.userInfo.idCardNo)) {
        uni.showToast({
          title: '请输入正确的身份证号',
          icon: 'none'
        });
        return;
      }

      try {
        // 调用API更新用户信息
        const res = await updateUserInfo(this.userInfo);
        if (res.code === 0) {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          });

          // 延迟返回上一页
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          uni.showToast({
            title: res.msg || '保存失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('保存用户信息失败', error);
        uni.showToast({
          title: '保存失败',
          icon: 'none'
        });
      }
    },

    // 身份证号验证
    validateIdCard(idCard) {
      const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
      return reg.test(idCard);
    }
  }
}
</script>

<style>
.container {
  background-color: #f6f6f6;
  min-height: 100vh;
  padding-top: 60rpx;
  padding-bottom: 40rpx;
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
  position: relative;
  overflow: hidden;
}

.avatar-icon {
  width: 57rpx;
  height: 49rpx;
}

.avatar-icon1 {
  width: 100%;
  height: 100%;
  border-radius: 12rpx;
}

.avatar-edit-tip {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 20rpx;
  text-align: center;
  padding: 8rpx 0;
}

.card {
  background: #FFFFFF;
  border-radius: 20rpx;
  margin: 0 32rpx;
  padding: 0 32rpx;
}

.info-item {
  padding: 30rpx 0;
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
  width: 200rpx;
}

.info-value {
  font-size: 28rpx;
  color: #333;
  flex: 1;
  text-align: right;
}

.input-field {
  border: 1rpx solid #E0E0E0;
  border-radius: 8rpx;
  padding: 16rpx 20rpx;
  text-align: left;
  min-height: 40rpx;
  background: #FAFAFA;
}

.radio-group {
  display: flex;
  align-items: center;
}

.radio-label {
  display: flex;
  align-items: center;
  margin-left: 30rpx;
}

.radio-label text {
  margin-left: 10rpx;
  font-size: 28rpx;
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

.submit-btn:active {
  background: #9e7f4a;
}
</style>