<template>
  <view class="container">
    <!-- 顶部标题 -->
    <view class="title">我的小马</view>

    <!-- 小马列表滑动区域 -->
    <swiper class="horse-swiper" :current="currentHorseIndex" @change="onSwiperChange" :circular="false">
      <swiper-item v-for="(horse, index) in horseList" :key="index">
        <view class="swiper-item-content">
          <!-- 马匹名字卡片 -->
          <view class="horse-card">
            <view class="horse-info">
              <image class="horse-image"
                :src="horse.image || 'https://q7.itc.cn/q_70,c_zoom,h_1200,g_face/images01/20250530/7b6fb0c1faf5445bb05591cafdb3ca45.png'"
                mode="aspectFill" />
              <view class="info-right">
                <view class="horse-name">{{ horse.horseName || '马匹名字' }}</view>
                <view class="horse-name2">寄养时间：{{ horse.boardingPeriod || '20xx年x月~20xx年x月' }}</view>
                <view class="horse-name2">生日：{{ horse.birthDate || '2018/06/09' }}</view>
                <view class="tags">
                  <text class="tag" v-for="(item, index) in horse.careStatistics" key="index">{{ item.description
                    }}</text>
                </view>
              </view>
            </view>
          </view>

          <!-- 小马信息卡片 -->
          <view class="info-card">
            <view class="card-title">小马信息</view>
            <view class="divider" />
            <view class="info-row">
              <text class="info-label">芯片号</text>
              <text class="info-value">{{ horse.chipNo || '26526952' }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">血统</text>
              <text class="info-value">{{ horse.bloodline || '汗血宝马' }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">责任教练</text>
              <text class="info-value">{{ horse.responsibleCoach || '教练1' }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">责任马工</text>
              <text class="info-value">{{ horse.responsibleGroom || '马工' }}</text>
            </view>
          </view>

          <!-- 医疗信息卡片 -->
          <view class="medical-card">
            <view class="card-title">医疗信息</view>
            <view class="medical-row" v-for="(item, index) in horse.medicalInfo" key="index">
              <text class="medical-label">下次{{ item.planTypeName }}时间</text>
              <text class="medical-value">{{ horse.nextExecuteTime || '2025-07-25星期五' }}</text>
            </view>
          </view>
        </view>
      </swiper-item>
    </swiper>

    <!-- 分页指示器 -->
    <view class="pagination" v-if="horseList.length > 1">
      <view v-for="(horse, index) in horseList" :key="index" class="pagination-dot"
        :class="{ 'active': currentHorseIndex === index }" />
    </view>

    <!-- 空状态提示 -->
    <view class="empty-state" v-if="horseList.length === 0">
      <image class="empty-image" src="/static/images/empty-horse.png" mode="aspectFit" />
      <text class="empty-text">暂无小马信息</text>
    </view>

    <CustomTabbar />
  </view>
</template>

<script>
import CustomTabbar from '@/components/custom-tabbar/custom-tabbar.vue';
import { getMyHorseList } from '@/api/home/index';

export default {
  components: {
    CustomTabbar
  },
  data() {
    return {
      horseList: [
        {
          birthDate: "2019/05/20",
          boardingPeriod: "2023年01月~2025年12月",
          careStatistics: [
            { planTypeName: "搓牙", completedCount: 1, description: "搓牙已完成1次" },
            { planTypeName: "钉蹄", completedCount: 1, description: "钉蹄已完成1次" }
          ],
          chipNo: "CHN9840001234568",
          horseName: "月光",
          medicalInfo: [
            { planTypeName: "钉蹄", nextExecuteTime: "2024-08-07 07:30:00" },
            { planTypeName: "养护", nextExecuteTime: "2024-08-14 16:00:00" }
          ],
          responsibleCoach: "胡克",
          responsibleGroom: "未知马工",
        }, {
          birthDate: "2017/08/10",
          boardingPeriod: "1970年01月~1970年01月",
          careStatistics: [],
          chipNo: "CHN9840001234569",
          horseName: "星辰",
          medicalInfo: [],
          responsibleCoach: "未知教练",
          responsibleGroom: "未知马工"
        },
      ],
      currentHorseIndex: 0, // 当前显示的小马索引
    }
  },
  async onLoad() {
    await this.getMyHorseList1()
  },
  methods: {
    getMyHorseList1() {
      getMyHorseList().then(res => {
        if (res.code == 0) {
          this.horseList = res.data;
        }
      }).catch(err => {
        console.error('获取小马列表失败', err);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      });
    },
    // swiper滑动事件
    onSwiperChange(e) {
      this.currentHorseIndex = e.detail.current;
    }
  }
}
</script>

<style>
/* 基础样式 */
.container {
  background: linear-gradient(to bottom, #000000, #fff);
  min-height: 100vh;
  padding: 40rpx 0;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30rpx;
  position: relative;
}

/* 标题样式 */
.title {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  margin-top: 70rpx;
  font-weight: 700;
  font-size: 36rpx;
  color: #FFFFFF;
  text-align: center;
  margin-bottom: 20rpx;
  z-index: 10;
}

/* 小马swiper容器 */
.horse-swiper {
  width: 100%;
  height: calc(100vh - 300rpx);
}

.swiper-item-content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  gap: 30rpx;
  padding-bottom: 120rpx;
  box-sizing: border-box;
  overflow-y: auto;
}

/* 马匹卡片样式 */
.horse-card {
  width: 704rpx;
  height: 232rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(255, 255, 255, 0.45);
  border-radius: 12rpx;
  padding: 24rpx;
  box-sizing: border-box;
}

.horse-info {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.horse-image {
  width: 190rpx;
  height: 190rpx;
  border-radius: 8rpx;
}

.info-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 12rpx;
}

.horse-name2 {
  font-size: 24rpx;
  color: #8C8C8C;
}

.horse-name {
  font-size: 36rpx;
  color: #1A1A1A;
  font-weight: bold;
}

.tags {
  display: flex;
  gap: 12rpx;
  margin-top: 10rpx;
  flex-wrap: wrap;
}

.tag {
  background: #F9EEDA;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #A0762C;
  padding: 4rpx 12rpx;
}

/* 小马信息卡片样式 */
.info-card {
  width: 706rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(255, 255, 255, 0.45);
  border-radius: 16rpx;
  padding: 30rpx;
  box-sizing: border-box;
}

.card-title {
  font-weight: 500;
  font-size: 30rpx;
  color: #333333;
  margin-bottom: 30rpx;
}

.divider {
  height: 1rpx;
  background-image: linear-gradient(to right, #ccc 50%, transparent 50%);
  background-size: 20rpx 1rpx;
  background-repeat: repeat-x;
  margin: 35rpx 0;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-label,
.info-value {
  font-size: 28rpx;
  color: #666666;
}

.info-label {
  color: #333;
  font-weight: 500;
}

/* 医疗信息卡片样式 */
.medical-card {
  width: 706rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 16rpx;
  padding: 30rpx;
  box-sizing: border-box;
  margin-bottom: 40rpx;
}

.medical-row {
  display: flex;
  justify-content: space-between;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.medical-label,
.medical-value {
  font-size: 28rpx;
  color: #666666;
}

.medical-label {
  color: #333;
  font-weight: 500;
}

/* 分页指示器 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16rpx;
  margin-top: 20rpx;
  margin-bottom: 40rpx;
  position: absolute;
  bottom: 120rpx;
  left: 0;
  right: 0;
}

.pagination-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background-color: #cccccc;
  transition: all 0.3s ease;
}

.pagination-dot.active {
  width: 32rpx;
  border-radius: 8rpx;
  background-color: #B7975E;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
}

.empty-image {
  width: 300rpx;
  height: 300rpx;
  opacity: 0.5;
  margin-bottom: 40rpx;
}

.empty-text {
  font-size: 32rpx;
  color: #999;
}
</style>