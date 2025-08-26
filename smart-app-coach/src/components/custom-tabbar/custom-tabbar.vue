<template>
  <view class="custom-tabbar">
    <view class="tabbar-item" @click="switchTab(0)">
      <image :src="current === 0 ? '/static/images/tabbar/home-icon-h.png' : '/static/images/tabbar/home-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 0 }">首页</text>
    </view>

    <view class="tabbar-item1" @click="switchTab(1)">
      <image :src="current === 1 ? '/static/images/tabbar/list-icon-h.png' : '/static/images/tabbar/list-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 1 }">会员管理</text>
    </view>

    <view class="tabbar-center1" @touchstart="showVoicePopup" @touchend="hideVoicePopup">
      <image src="/static/images/tabbar/luyin.png" :style="{
        width: '110rpx',
        height: '110rpx',
        transform: isPressed ? 'scale(1.1)' : 'scale(1)',
        transition: 'transform 0.2s'
      }" class="center-icon" />
    </view>

    <view class="tabbar-item2" @click="switchTab(3)">
      <image
        :src="current === 3 ? '/static/images/tabbar/message-icon-h.png' : '/static/images/tabbar/message-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 3 }">教练认证</text>
    </view>

    <view class="tabbar-item" @click="switchTab(4)">
      <image :src="current === 4 ? '/static/images/tabbar/mine-icon-h.png' : '/static/images/tabbar/mine-icon.png'"
        class="tabbar-icon" />
      <text :class="{ 'active-text': current === 4 }">我的</text>
    </view>

    <!-- 语音提示弹窗 -->
    <uni-popup ref="voicePopup" type="center">
      <view class="voice-popup">
        <text>正在使用AI语音...</text>
      </view>
    </uni-popup>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isPressed: false, // 控制是否按下状态
      current: 0,
      _lastCurrent: 0, // 用于存储上一个 current 值

      selectedTag: null,
      tags: [
        '自定义活动1',
        '自定义活动2',
        '自定义活动3',
        '自定义活动4',
        '自定义活动5',
        '自定义活动6'
      ]
    }
  },
  created() {
    // 初始化时根据当前路由设置 current
    this.updateCurrentByRoute();

    // 监听路由变化（适用于页面返回等情况）
    uni.onAppRoute((res) => {
      this.updateCurrentByRoute();
    });
  },
  methods: {
    updateCurrentByRoute() {
      const pages = [
        '/pages/home/index',
        '/pages/featured/featured',
        '/pages/microphone/microphone',
        '/pages/message/message',
        '/pages/mine/mine'
      ];
      const currentRoute = getCurrentPages()[0]?.route || '';
      this.current = pages.indexOf('/' + currentRoute);
    },
    switchTab(index) {
      const urls = [
        '/pages/home/index',
        '/pages/management/management',
        '/pages/microphone/microphone',
        '/pages/message/message',
        '/pages/mine/mine'
      ];
      uni.switchTab({
        url: urls[index],
        success: () => {
          this.current = index;
        },
        fail: (err) => {
          console.error('切换失败:', err);
        }

      });
    },

    // 特色活动弹窗
    showFeaturedPopup() {
      this._lastCurrent = this.current;
      this.current = 1;
      this.$refs.featuredPopup.open();
    },

    closeFeaturedPopup() {
      this.current = this._lastCurrent;

      this.$refs.featuredPopup.close();
    },

    // 语音弹窗控制
    showVoicePopup() {
      this.isPressed = true;
      this.$refs.voicePopup.open();
    },

    hideVoicePopup() {
      this.isPressed = false;
      this.$refs.voicePopup.close();
    },

    selectTag(index) {
      this.selectedTag = this.selectedTag === index ? null : index;
    },

    confirmSelection() {
      if (this.selectedTag !== null) {
        uni.navigateTo({
          url: `/pages/eventDetails/eventDetails?activity=${this.tags[this.selectedTag]}`
        });
        this.closeFeaturedPopup();
      } else {
        uni.showToast({
          title: '请选择一个活动',
          icon: 'none'
        });
      }
    }
  }
}
</script>

<style>
.custom-gradient-mask .uni-popup__mask {
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.9), rgba(102, 102, 102, 0.9)) !important;
}

/* 语音弹窗样式 */
.voice-popup {
  background-color: #FFFFFF;
  padding: 30rpx 50rpx;
  border-radius: 10rpx;
  font-size: 28rpx;
  color: #333333;
  box-shadow: 0 0 20rpx rgba(0, 0, 0, 0.1);
}

.uni-popup__mask {
  background: linear-gradient(to bottom, #000, #666) !important;
  opacity: 0.8 !important;
}

.custom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 90rpx;
  background-color: #FFFFFF;
  display: flex;
  justify-content: space-around;
  align-items: center;
  box-shadow: 0 -2rpx 6rpx rgba(0, 0, 0, 0.1);
  z-index: 999 !important;
  padding-bottom: env(safe-area-inset-bottom);
}

.tabbar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 20rpx;
  color: #999999;
  flex: 1;
}

.tabbar-item1 {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 20rpx;
  color: #999999;
  flex: 1;
  margin-left: -30rpx;


}

.tabbar-item2 {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 20rpx;
  color: #999999;
  flex: 1;
  margin-right: -30rpx;

}

.tabbar-icon {
  width: 48rpx;
  height: 48rpx;
  margin-bottom: 4rpx;
}

.active-text {
  color: #fad27d;
}

.tabbar-center1 {
  position: relative;
  bottom: 2rpx;
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.center-icon {
  width: 110rpx;
  height: 110rpx;
  transition: transform 0.2s;
  margin-bottom: 20rpx;

}

.tabbar-center1:active .center-icon {
  transform: scale(1.1);
}

/* 特色活动弹窗原有样式保持不变 */
.featured-popup {
  width: 628rpx;
  background: linear-gradient(180deg, #FFF2DC 0%, #FFFFFF 22%, #FFFFFF 66%, #FFF5E4 100%);
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(0, 0, 0, 0.05);
  border-radius: 20rpx;
  border: 4rpx solid #FFFFFF;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 40rpx 10rpx 40rpx;

  box-sizing: border-box;
}

.popup-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40rpx;
}

.popup-title {
  font-weight: 600;
  font-size: 48rpx;
  color: #333333;
  margin-bottom: 16rpx;
}

.popup-subtitle {
  font-weight: 600;
  font-size: 32rpx;
  color: #333333;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 40rpx;
}

.tag-item1 {
  width: 260rpx;
  height: 112rpx;
  background: #F6F6F6;
  border-radius: 146rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 20rpx;
  transition: all 0.3s;
}

.tag-selected {
  background: #F9EEDA;
  border-radius: 146rpx;
  border: 2rpx solid #A0762C;
  font-family: 'OPPOSans', 'OPPOSans';
  font-weight: 500;
  font-size: 28rpx;
  color: #8A5800;
}

.popup-footer {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-top: 20rpx;
}

.cancel-btn {
  font-size: 36rpx;
  color: #666666;
  padding: 30rpx 0;
  text-align: center;
  width: 100rpx;
}

.confirm-btn {
  width: 502rpx;
  height: 110rpx;
  background: #B7975E;
  border-radius: 146rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 600;
  font-size: 36rpx;
  color: #FFFFFF;
}
</style>