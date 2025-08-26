<template>
  <view class="container">
    <!-- 左上角logo -->
    <image class="club-logo" src="/static/images/index/鞍境.png" mode="aspectFit" />

    <!-- 俱乐部名称 -->
    <text class="club-name">上海XXX俱乐部</text>

    <!-- 中间语音按钮 -->
    <view class="voice-btn-container" @touchstart="startRipple" @touchend="stopRipple" @touchcancel="stopRipple">
      <image class="voice-icon" src="/static/images/index/圈图.png" mode="aspectFit" />

      <!-- 波纹动画 -->
      <view class="ripple" v-for="(ripple, index) in ripples" :key="index" :style="{
        width: '600rpx',
        height: '600rpx',
        opacity: ripple.opacity,
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%) scale(' + ripple.scale + ')'
      }" />
    </view>

    <!-- 底部进入按钮 -->
    <button class="enter-btn" @click="enterClub">进入俱乐部</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      ripples: [],
      rippleInterval: null,
      rippleCounter: 0
    }
  },
  methods: {
    startRipple() {
      this.ripples = [];
      this.rippleInterval = setInterval(() => {
        this.addRipple();
      }, 300);
    },
    stopRipple() {
      clearInterval(this.rippleInterval);
      setTimeout(() => {
        this.ripples = [];
      }, 1000);
    },
    addRipple() {
      const rippleId = this.rippleCounter++;
      const newRipple = {
        id: rippleId,
        opacity: 0.6,
        scale: 0.1  // 从0.1倍开始放大
      };
      this.ripples.push(newRipple);

      const duration = 1500;
      const steps = 30;
      const stepTime = duration / steps;
      let step = 0;

      const animate = () => {
        if (step > steps) {
          // 动画完成后移除该波纹
          this.ripples = this.ripples.filter(r => r.id !== rippleId);
          return;
        }

        const progress = step / steps;
        this.$nextTick(() => {
          this.ripples = this.ripples.map(r => {
            if (r.id === rippleId) {
              return {
                ...r,
                scale: 0.1 + progress * 0.9, // 从0.1放大到1.0
                opacity: 0.6 * (1 - progress) // 透明度从0.6线性降到0
              };
            }
            return r;
          });
        });

        step++;
        setTimeout(animate, stepTime);
      };

      animate();
    },
    enterClub() {
      uni.switchTab({
        url: '/pages/home/index'
      });
    }
  },
  beforeUnmount() {
    clearInterval(this.rippleInterval);
  }
}
</script>

<style>
/* 所有样式保持不变 */
.container {
  width: 100%;
  height: 100vh;
  background: linear-gradient(to bottom, #ffffff, #000000);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.club-logo {
  width: 120rpx;
  height: 120rpx;
  position: absolute;
  top: 60rpx;
  left: 60rpx;
}

.club-name {
  font-weight: 500;
  font-size: 56rpx;
  color: #D5BC84;
  margin-top: 200rpx;
  margin-bottom: 150rpx;
}

.voice-btn-container {
  width: 600rpx;
  height: 600rpx;
  position: relative;
  margin: 100rpx 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.voice-icon {
  width: 600rpx;
  height: 600rpx;
  z-index: 2;
}

.ripple {
  position: absolute;
  border-radius: 50%;
  background-color: rgba(213, 188, 132, 0.6);
  transform-origin: center;
  z-index: 1;
  width: 600rpx;
  height: 600rpx;
  transition: opacity 0.1s ease-out;
  /* 添加透明度过渡效果 */
}

.enter-btn {
  width: 370rpx;
  height: 96rpx;
  background: #B7975E;
  border-radius: 186rpx;
  font-weight: 600;
  font-size: 36rpx;
  color: #F9EEDA;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 100rpx;
  border: none;
}

.enter-btn::after {
  border: none;
}
</style>