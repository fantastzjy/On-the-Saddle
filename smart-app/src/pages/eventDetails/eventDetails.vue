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
      <view class="activity-title">{{ activityInfo.activityName }}</view>
      <view class="detail-title">详情介绍：</view>
      <view class="detail-content">
        {{ activityInfo.activityDetail }}
      </view>
      <view class="divider" />
      <view class="info-item">
        <text class="info-label">地点</text>
        <text class="info-value">{{ activityInfo.location }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">时间</text>
        <text class="info-value">{{ activityInfo.startTime }} ~ {{ activityInfo.endTime }}</text>
      </view>
      <view class="info-item no-border">
        <text class="info-label">金额</text>
        <text class="price"><text class="price1">¥</text>{{ activityInfo.price }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">须知</text>
        <text class="info-value">{{ activityInfo.activityName }}</text>
      </view>
      <view>
        <text class="image-title">图文详情</text>
        <!-- <image class="detail-image"
          src="https://img0.baidu.com/it/u=2592623529,533042228&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500"
          mode="widthFix" /> -->
        <swiper class="detail-image1" indicator-dots autoplay interval="3000">
          <swiper-item v-for="(item, index) in activityInfo.detailImages" :key="index">
            <image class="detail-image" :src="item" mode="aspectFill" />
          </swiper-item>
        </swiper>
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
import { getActivityList } from '@/api/home/index';
export default {
  data() {
    return {
      activityInfo: {}
    }
  },
  onLoad(options) {
    console.log('🎪 [活动详情] 页面加载，参数:', options);
    this.getActivityDetail(options);
  },
  methods: {
    async getActivityDetail(options = {}) {
      try {
        console.log('🎪 [活动详情] 获取活动详情，参数:', options);
        
        const res = await getActivityList({});
        console.log('🎪 [活动详情] API响应:', res);

        if (res.code === 0 && res.data && Array.isArray(res.data)) {
          let selectedActivity = null;

          // 优先级1: 使用activityId查找
          if (options.activityId) {
            selectedActivity = res.data.find(item => 
              (item.activityId && item.activityId == options.activityId) ||
              (item.id && item.id == options.activityId)
            );
            console.log('🎪 [活动详情] 根据activityId查找:', options.activityId, '结果:', selectedActivity);
          }

          // 优先级2: 使用activityIndex查找
          if (!selectedActivity && options.activityIndex !== undefined) {
            const index = parseInt(options.activityIndex);
            if (index >= 0 && index < res.data.length) {
              selectedActivity = res.data[index];
              console.log('🎪 [活动详情] 根据activityIndex查找:', index, '结果:', selectedActivity);
            }
          }

          // 优先级3: 使用activity参数模糊匹配
          if (!selectedActivity && options.activity) {
            selectedActivity = res.data.find(item => 
              (item.activityName && item.activityName.includes(options.activity)) ||
              (item.name && item.name.includes(options.activity))
            );
            console.log('🎪 [活动详情] 根据activity名称查找:', options.activity, '结果:', selectedActivity);
          }

          // 兜底方案: 使用第一个活动
          if (!selectedActivity && res.data.length > 0) {
            selectedActivity = res.data[0];
            console.log('🎪 [活动详情] 使用第一个活动作为兜底');
          }

          if (selectedActivity) {
            this.activityInfo = selectedActivity;
            console.log('🎪 [活动详情] ✅ 活动信息设置成功:', this.activityInfo);
          } else {
            console.warn('🎪 [活动详情] ⚠️ 未找到匹配的活动');
            uni.showToast({
              title: '未找到活动信息',
              icon: 'none'
            });
          }
        } else {
          console.error('🎪 [活动详情] ❌ API返回数据格式错误:', res);
          uni.showToast({
            title: '获取活动详情失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('🎪 [活动详情] ❌ 获取活动详情异常:', error);
        uni.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    },
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

.detail-image1 {
  width: 100%;
  height: 450rpx;
  border-radius: 20rpx;
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

.price {
  color: #6b4b12;
  margin-right: 20rpx;
}

.price1 {
  font-size: 20rpx;
}
</style>''