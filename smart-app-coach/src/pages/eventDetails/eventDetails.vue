<template>
  <view class="container">
    <view class="section-title">
      <uni-icons @click="goBack" type="left" color="#fff" size="22" />
      <text>活动详情</text>
      <text />
    </view>
    <!-- 详情介绍卡片 -->
    <view class="detail-card">
      <!-- 活动名称 -->
      <view class="activity-title">活动名称</view>
      <view class="detail-title">详情介绍：</view>
      <view class="detail-content">
        俱乐部坐拥百亩生态牧场，草坪如茵，林带环绕，12个国际
        标准马术场地错落分布——从盛装舞步的细腻沙场地，到踩得
        爽的起伏随顿区，再到休闲骑乘的休闲小径，每一处都经过专
        业设计，既保障运动安全，又能让骑手沉浸式感受与马共舞的
        乐趣。马厩区采用全道透式设计，28间宽敞马舍配备恒温系
        统与自动饮水装置，饲养者来自荷兰、德国、西班牙等国的优
        质温血马。纯血马及pony马，从温温柔人的教学马到血亲纯
        正的美赛马，满足从初学者到专业骑手的不同需求。
      </view>
      <view class="divider" />
      <view class="info-item">
        <text class="info-label">地点</text>
        <text class="info-value">自定义地点</text>
      </view>
      <view class="info-item">
        <text class="info-label">时间</text>
        <text class="info-value">自定义时间</text>
      </view>
      <view class="info-item">
        <text class="info-label">须知</text>
        <text class="info-value">需提前预约，购买后90天内有效</text>
      </view>
      <view class="info-item no-border">
        <text class="info-label">保障</text>
        <text class="info-value">随时退 · 过期退</text>
      </view>
      <view>
        <text class="image-title">图文详情</text>
        <image class="detail-image"
          src="https://img0.baidu.com/it/u=2592623529,533042228&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500"
          mode="widthFix" />
      </view>
    </view>

    <!-- 地点信息卡片 -->
    <!-- <view class="info-card">

    </view> -->

    <!-- 图文详情 -->


    <!-- 报名按钮 -->
    <view class="footer">
      <button class="join-btn" @click="joinActivity">报名参加
        <image style="width: 32rpx; height: 26rpx;" src="/static/images/index/箭头.png" mode="scaleToFill" />
      </button>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    goBack() {
      uni.navigateBack({
        delta: 1
      });
    },
    joinActivity() {
      // 报名参加逻辑
      uni.showToast({
        title: '报名成功',
        icon: 'success'
      });
    }
  }
}
</script>

<style>
/* 整体渐变背景 */
.container {
  padding: 20rpx;
  background: linear-gradient(to bottom, #000000, #ffffff);
  min-height: 100vh;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 36rpx;
  font-weight: 700;
  margin: 80rpx 0 30rpx 0;
  /* text-align: center; */
  color: #fff;
}

/* 活动标题 */
.activity-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #333333;
  text-align: center;
  margin: 30rpx 0;
}

/* 卡片通用样式 */
.detail-card,
.info-card,
.image-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

.divider {
  height: 5rpx;
  border-bottom: 2rpx dashed #eee;
  margin-top: 20rpx;
}

/* 详情介绍 */
.detail-title {
  font-size: 28rpx;
  font-weight: 400;
  color: #333333;
  margin-bottom: 20rpx;
}

.detail-content {
  font-size: 24rpx;
  color: #666666;
  line-height: 34rpx;
  text-align: justify;
  font-style: normal;
  text-transform: none;
}

/* 信息卡片 */
.info-item {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx dashed #f0f0f0;
}

.info-item.no-border {
  border-bottom: none;
}

.info-label {
  font-size: 28rpx;
  color: #333333;
  width: 120rpx;
}

.info-value {
  font-size: 28rpx;
  color: #666666;
  flex: 1;
}

/* 图文详情 */
.image-title {
  font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 20;
  font-size: 32rpx;
  color: #3d3d3d;
  font-weight: 500;
  text-align: center;
  display: block;
  margin-bottom: 20rpx;
  font-style: normal;
  text-transform: none;
}

.detail-image {
  width: 100%;
  border-radius: 20rpx;
}

/* 底部报名按钮 */
.footer {
  /* background-color: #fff; */
  margin-top: 36rpx;
  text-align: right;
}

.join-btn {
  background-color: #b7975e;
  color: #fff;
  border-radius: 16rpx;
  width: 292rpx;
  height: 86rpx;
  line-height: 86rpx;
  font-size: 36rpx;
  font-weight: 600;
  margin-left: 420rpx;
}
</style>''