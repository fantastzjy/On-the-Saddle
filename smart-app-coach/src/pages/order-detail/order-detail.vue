<template>
  <view class="container">
    <!-- 订单确认标题 -->
    <view class="section-title">
      <uni-icons @click="goBack" type="left" color="#fff" size="22" />
      <text>订单确认</text>
      <text />
    </view>
    <!-- 教练信息卡片 -->
    <view class="coach-card">
      <view class="coach-avatar-container">
        教练信息
      </view>
      <view class="coach-info">
        <image class="coach-avatar" src="https://q1.itc.cn/q_70/images03/20250802/e70c0520fd51496586d2ff380531c368.jpeg"
          mode="aspectFill" />
        <text class="coach-name">王教练</text>
        <image class="coach-sex" src="/static/images/index/sex.png" />
      </view>
    </view>

    <!-- 服务日期部分 -->
    <view class="info-card">
      <view class="info-item">
        <text class="info-label">服务信息</text>
      </view>
      <view class="info-item1">
        <text class="info-label">场地障碍和救援</text>
        <text class="info-value1">
          <text style="font-size: 24rpx;">¥</text>
          000</text>
      </view>

      <view class="info-item">
        <view class="info-label">服务日期</view>
        <view>
          <text class="info-value">2025-07-25 星期五</text>
          <uni-icons type="right" color="#333" size="13" />
        </view>
      </view>
      <view class="info-item">
        <text class="info-label">服务地点</text>
        <view>
          <text class="info-value">上海XX俱乐部</text>
          <uni-icons type="right" color="#333" size="13" />
        </view>
      </view>
      <view class="info-item">
        <text class="info-label">服务时间</text>
        <view>
          <text class="info-value">08:00~09:00</text>
          <uni-icons type="right" color="#333" size="13" />
        </view>
      </view>
    </view>

    <!-- 分隔线 -->
    <!-- <view class="divider" /> -->

    <!-- 总计部分 -->
    <view class="total-card">
      <view style="display: flex; justify-content: space-between; align-items: center;">
        <text class="total-label">总计</text>
        <text class="total-amount">
          <text style="font-size: 24rpx;">¥</text>
          000</text>
      </view>
      <view class="divider" />
      <view style="float: right;">
        <text class="payment-title">实付：
          <text class="total-amount1">
            <text class="total-amount2">¥</text>
            4000</text></text>
      </view>
    </view>

    <!-- 分隔线 -->
    <!-- <view class="divider" /> -->

    <!-- 支付部分 -->
    <view class="payment-card">
      <view class="tips-section">
        <view>
          <image style="width: 26rpx; height: 26rpx;margin-right: 14rpx;" src="/static//images/index/提示.png"
            mode="scaleToFill" />
          <text class="tips-title">温馨提示</text>
        </view>
        <text class="tips-content">您下单后，教练需提前为您锁定教学时间、锁定马匹： </text>
        <text class="tips-item"> · 请您在课程预约后准时上课</text>
        <text class="tips-item"> · 若超时未能准时上课，或将导致课程时间和马匹会发生变动</text>
        <text class="tips-item"> · 若课程预约时间超过24小时，未能上课核销，款项将自动全额退还</text>
      </view>
    </view>

    <!-- 底部支付按钮 -->
    <view class="footer">
      <view class="actual-payment">
        <text style="font-size: 24rpx;color: #24262b;">实付：</text>
        <text class="amount1"><text class="amount">¥</text>000</text>
      </view>
      <view>
        <button class="submit-btn" @click="submitOrder">立即支付</button>
      </view>
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
    submitOrder() {
      // 提交订单逻辑
      uni.showToast({
        title: '订单提交成功',
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
  background: linear-gradient(to bottom, #000000, #d3d3d3, #fff);
  min-height: 100vh;
}

/* 教练卡片样式 */
.coach-card {
  height: 196rpx;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

.coach-avatar-container {
  color: #333;
  font-size: 30rpx;
  display: block;
  font-weight: 500;
  font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 20;
  font-style: normal;
  margin-bottom: 20rpx;
}

.coach-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
}

.coach-info {
  display: flex;
  align-items: center;

}

.coach-name {
  font-size: 32rpx;
  font-weight: 500;
  display: block;
  margin-bottom: 8rpx;
  margin-left: 14rpx;
  margin-right: 18rpx;
}

.coach-sex {
  width: 22rpx;
  height: 22rpx;
}

.coach-title {
  font-size: 26rpx;
  color: #666;
  display: block;
}

/* 卡片通用样式 */
.info-card,
.total-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

/* 标题样式 */
.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 36rpx;
  font-weight: 700;
  margin: 100rpx 0 30rpx 0;
  /* text-align: center; */
  color: #fff;
}

.section-subtitle {
  font-size: 32rpx;
  font-weight: bold;
  margin: 0 0 20rpx 0;
  color: #333;
}

/* 信息项样式 */
.info-item {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
}

.info-item1 {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0 30rpx 0;
  border-bottom: 2rpx dashed #eee;
  margin-bottom: 30rpx;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  height: 42rpx;
  font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 20;
  font-weight: 500;
  font-size: 30rpx;
  color: #333333;
  line-height: 42rpx;
  text-align: left;
  font-style: normal;
  text-transform: none;
}

.info-value {
  font-size: 24rpx;
  color: #333;
  font-weight: 600;
  margin-right: 10rpx;
}

.info-value1 {
  font-size: 40rpx;
  color: #333;
  font-weight: 600;
}

/* 分隔线 */
.divider {
  height: 5rpx;
  border-bottom: 2rpx dashed #eee;
  margin: 30rpx 0;
}

/* 总计部分 */
.total-card {
  height: 170rpx;
  padding: 30rpx 20rpx;
}

.total-label {
  font-size: 32rpx;
  font-weight: bold;
}

.total-amount {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
}

.total-amount1 {
  font-size: 32rpx;
  font-weight: bold;
  color: #ee100f;
}

.total-amount2 {
  font-size: 24rpx;
  font-weight: bold;
  color: #ee100f;
}

/* 支付部分 */
.payment-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  display: block;
  color: #333;
}

.tips-section {
  margin-top: 20rpx;
}

.tips-title {
  font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 20;
  font-weight: 600;
  font-size: 28rpx;
  color: #666666;
  line-height: 40rpx;
  text-align: left;
  font-style: normal;
  text-transform: none;
  margin-bottom: 10rpx;
}

.tips-content {
  font-size: 24rpx;
  color: #666;
  display: block;
  margin-bottom: 10rpx;
}

.tips-item {
  font-size: 24rpx;
  color: #666;
  display: block;
  margin-bottom: 5rpx;
}

/* 底部支付按钮 */
.footer {
  margin-top: 120rpx;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* padding: 0 20rpx; */
}



.amount {
  font-size: 32rpx;
  color: #ee100f;
  font-weight: bold;
  margin-left: 10rpx;
}

.amount1 {
  font-size: 48rpx;
  color: #ee100f;
  font-weight: bold;
}

.submit-btn {
  width: 268rpx;
  height: 96rpx;
  background-color: #b7975e;
  color: #fff;
  border-radius: 16rpx;
  line-height: 96rpx;
  font-size: 36rpx;
  font-weight: 600;
  margin-right: 20rpx;
}
</style>