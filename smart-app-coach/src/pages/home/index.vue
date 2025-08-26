<template>
  <view class="page-container">
    <!-- 顶部卡片 -->
    <view class="top-card">
      <view class="icon-item" @click="goToConi">
        <image src="/static/images/index/赛事.png" class="icon" />
        <text class="icon-text">赛事</text>
      </view>
      <view class="icon-item" @click="goToDangan">
        <image src="/static/images/index/档期管理.png" class="icon" />
        <text class="icon-text">档期管理</text>
      </view>
      <view class="icon-item" @click="goToConi">
        <image src="/static/images/index/约兽医.png" class="icon" />
        <text class="icon-text">约兽医</text>
      </view>
      <view class="icon-item" @click="goToConi">
        <image src="/static/images/index/找工作.png" class="icon" />
        <text class="icon-text">找工作</text>
      </view>
    </view>


    <!-- 个人信息卡片 -->
    <view class="profile-card">
      <view class="profile-card-content">
        <view class="profile-left">
          <image src="https://img2.baidu.com/it/u=1593837408,1257030293&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800"
            class="avatar" />
        </view>
        <view class="profile-right">
          <text class="name">某某某</text>
          <view class="badges">
            <view class="badge">中国协中一级骑手证</view>
            <view class="badge">3星教练证</view>
          </view>
          <view class="tags">
            <view class="tag">#中级进阶</view>
            <view class="tag">#赛事指导</view>
          </view>
        </view>
      </view>
      <view class="stats">
        <view class="stat-item">
          <text class="stat-value">0<text class="stat-value1">单</text></text>
          <text class="stat-label">本月订单数</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">0<text class="stat-value1">元</text></text>
          <text class="stat-label">本月教学收益</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">0<text class="stat-value1">元</text></text>
          <text class="stat-label">累计教学收益</text>
        </view>
      </view>
    </view>

    <!-- 日历卡片 -->
    <view class="calendar-card">
      <view class="calendar-header">
        <text class="month-year">{{ currentYear }}年 {{ currentMonth }}月</text>
        <!-- <view class="box1">
          <image src="/static/images/index/down-arrow.png" class="down-arrow" />
        </view> -->
        <view class="arrows">
          <view class="box">
            <image src="/static/images/index/left-arrow.png" class="arrow" @click="prevMonth" />
          </view>
          <view class="box">
            <image src="/static/images/index/right-arrow.png" class="arrow" @click="nextMonth" />
          </view>
        </view>
      </view>
      <view class="week-days">
        <text class="week-day">Mo</text>
        <text class="week-day">Tu</text>
        <text class="week-day">We</text>
        <text class="week-day">Th</text>
        <text class="week-day">Fr</text>
        <text class="week-day">Sa</text>
        <text class="week-day">Su</text>
      </view>
      <view class="calendar-grid">
        <view class="day" v-for="(day, index) in calendarDays" :key="index" :class="{ today: day.isToday }">

          <view class="dot red" v-if="day.courses > 0">
            <!-- <view class="dot " /> -->
            <!-- <view class="dot blue" v-if="day.courses >= 2" />
            <view class="dot green" v-if="day.courses >= 3" /> -->
          </view>
          <text class="day-number">{{ day.date }}</text>
        </view>
      </view>
    </view>

    <!-- 课程列表 -->
    <view class="course-list">
      <view class="course-card" v-for="(course, index) in courses" :key="index">
        <view class="course-left">
          <!-- <view class="dot red" /> -->
          <view class="course-info">
            <text class="course-name">场地障碍（进阶）课</text>
            <text class="student">学员：xxx1</text>
            <text class="time">14:00 - 16:00</text>
          </view>
        </view>
        <view class="course-right">
          <view class="btn primary">课程核销</view>
          <view class="btn secondary">取消/退款</view>
        </view>
      </view>
    </view>
  </view>
  <CustomTabbar />
</template>

<script>
import CustomTabbar from '@/components/custom-tabbar/custom-tabbar.vue';
export default {
  components: {
    CustomTabbar
  },
  data() {
    return {
      currentYear: 2025,
      currentMonth: 4,
      calendarDays: [],
      courses: [
        // 课程数据
        {}, {}, {}
      ]
    }
  },
  mounted() {
    this.generateCalendar();
  },
  methods: {
    goToDangan() {
      uni.navigateTo({
        url: '/pages/dangqi/dangqi'
      })
    },
    goToConi() {
      uni.showToast({
        title: '后期上线',
        icon: 'none'
      })
    },
    generateCalendar() {
      // 生成当月日历数据
      const daysInMonth = new Date(this.currentYear, this.currentMonth, 0).getDate();
      const firstDay = new Date(this.currentYear, this.currentMonth - 1, 1).getDay();

      // 调整周一为第一天（0=周日，1=周一，...）
      let startDay = firstDay === 0 ? 6 : firstDay - 1;

      this.calendarDays = [];

      // 填充前面的空白日期
      for (let i = 0; i < startDay; i++) {
        this.calendarDays.push({ date: '', courses: 0 });
      }

      // 填充当月日期
      const today = new Date();
      for (let i = 1; i <= daysInMonth; i++) {
        const isToday = this.currentYear === today.getFullYear() &&
          this.currentMonth === today.getMonth() + 1 &&
          i === today.getDate();

        // 随机生成课程数量（示例数据）
        const courses = Math.floor(Math.random() * 5); // 0-4节课
        this.calendarDays.push({
          date: i,
          courses: courses,
          isToday: isToday
        });
      }
    },
    prevMonth() {
      if (this.currentMonth === 1) {
        this.currentMonth = 12;
        this.currentYear--;
      } else {
        this.currentMonth--;
      }
      this.generateCalendar();
    },
    nextMonth() {
      if (this.currentMonth === 12) {
        this.currentMonth = 1;
        this.currentYear++;
      } else {
        this.currentMonth++;
      }
      this.generateCalendar();
    }
  }
}
</script>

<style>
.page-container {
  min-height: 100vh;
  background: linear-gradient(to bottom, #0E0E0E 20%, #0e0e0e, #8b8b8b, #d7d7d7, #e7e7e7);
  padding: 30rpx;
}

.top-card {
  margin-top: 140rpx;
  height: 172rpx;
  background: rgba(216, 216, 216, 0.16);
  border-radius: 16rpx;
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-bottom: 30rpx;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.icon {
  width: 60rpx;
  height: 60rpx;
  margin-bottom: 10rpx;
}

.icon-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  ;
}

.profile-card {
  height: 330rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.profile-card-content {
  display: flex;
  justify-content: space-between;
  align-items: start;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
}

.profile-right {
  margin-left: 20rpx;
  flex: 1;
}

.name {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 600;
  font-size: 36rpx;
  color: #1A1A1A;
  display: block;
  margin-bottom: 15rpx;
}

.badges {
  display: flex;
  flex-wrap: wrap;
  margin-top: 20rpx;
  margin-bottom: 15rpx;
}

.badge {
  height: 42rpx;
  border-radius: 4rpx;
  border: 2rpx solid #F4C266;
  font-size: 24rpx;
  color: #F4C266;
  padding: 0 10rpx;
  margin-right: 34rpx;
  display: flex;
  align-items: center;
  padding: 0 30rpx;
  justify-content: center;
}

.tags {
  display: flex;
  margin-bottom: 20rpx;
}

.tag {
  height: 42rpx;
  background: linear-gradient(90deg, #ECC889 0%, #AC843C 100%);
  border-radius: 4rpx;
  font-weight: 600;
  font-size: 24rpx;
  color: #FFFFFF;
  padding: 0 40rpx;
  margin-right: 34rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stats {
  display: flex;
  justify-content: space-between;
  padding: 0 40rpx;
  margin-top: 30rpx;
}

.stat-item {
  height: 116rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
}

.stat-value {
  font-weight: 600;
  font-size: 52rpx;
  color: #1A1A1A;
}

.stat-value1 {
  font-size: 24rpx;
  color: #1A1A1A;
}

.stat-label {
  font-weight: 400;
  font-size: 28rpx;
  color: #8C8C8C;
}

.calendar-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.month-year {
  font-weight: 600;
  font-size: 40rpx;
  color: #2D3748;
  display: flex;
  align-items: center;
  margin-left: 30rpx;
}

.down-arrow {
  width: 40rpx;
  height: 40rpx;
}

.box1 {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx dashed #718096;
  margin-left: -150rpx;
}

.box {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx dashed #718096;
  margin-left: 40rpx;
}

.arrows {
  display: flex;
}

.arrow {
  width: 40rpx;
  height: 40rpx;
}

.week-days {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.week-day {
  font-size: 24rpx;
  color: #2D3748;
  flex: 1;
  text-align: center;
}

.calendar-grid {
  display: flex;
  flex-wrap: wrap;
}

.day {
  width: calc(100% / 7);
  height: 60rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
}

.day-number {
  font-size: 28rpx;
  margin-bottom: 5rpx;
}

.day.today .day-number {
  color: #FFFFFF;
  background-color: #B7975E;
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dots {
  position: absolute;
  top: 5rpx;
  right: 5rpx;
  display: flex;
}

.dot {
  width: 18rpx;
  height: 18rpx;
  border-radius: 50%;
  margin-left: 30rpx;
}

.dot.red {
  background-color: #d09b2e;
}

.dot.blue {
  background-color: #0000FF;
}

.dot.green {
  background-color: #00FF00;
}

.course-list {
  margin-bottom: 140rpx;
}

.course-card {
  height: 150rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 8rpx 45rpx 0rpx rgba(138, 129, 124, 0.1007);
  border-radius: 21rpx;
  padding: 30rpx;
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.course-left {
  display: flex;
  align-items: start;
}

.course-left .dot {
  width: 36rpx;
  height: 36rpx;
  border-radius: 50%;
  background-color: #FF0000;
  margin-right: 20rpx;
  margin-top: 10rpx;
}

.course-info {
  display: flex;
  flex-direction: column;
}

.course-name {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 32rpx;
  color: #333333;
  line-height: 47rpx;
  margin-bottom: 10rpx;
}

.student {
  font-weight: 400;
  font-size: 28rpx;
  color: #333333;
  line-height: 47rpx;
  margin-bottom: 10rpx;
}

.time {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 32rpx;
  line-height: 47rpx;
  color: #333333;
}

.course-right {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.btn {
  width: 148rpx;
  height: 60rpx;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.btn.primary {
  border: 1rpx solid #B7975E;
  color: #B7975E;
}

.btn.secondary {
  border: 1rpx solid #999999;
  color: #999999;
}
</style>