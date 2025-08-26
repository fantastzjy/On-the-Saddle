<template>
  <view class="page-container">
    <!-- 头部标题 -->

    <view class="header">
      <uni-icons @click="goBack" type="left" color="#fff" size="22" />
      <text>档期管理</text>
      <text />
    </view>
    <!-- 提示文字 -->
    <view class="tip">
      <text>请点击选择你不上课的日期</text>
    </view>

    <!-- 日历卡片 -->
    <view class="calendar-card">
      <!-- 日历头部 -->
      <view class="calendar-header">
        <text class="month-year">{{ currentYear }}年{{ currentMonth }}月</text>
        <view class="arrow-container">
          <image src="/static/images/index/left-arrow.png" class="arrow" @click="prevMonth" />
          <image src="/static/images/index/right-arrow.png" class="arrow" @click="nextMonth" />
        </view>
      </view>

      <!-- 星期 -->
      <view class="week-days">
        <text v-for="day in weekDays" :key="day" class="week-day">{{ day }}</text>
      </view>

      <!-- 日期网格 -->
      <view class="days-grid">
        <text v-for="(day, index) in days" :key="index" :class="['day', { 'selected': selectedDays.includes(day) }]"
          @click="toggleDay(day)">
          {{ day }}
        </text>
      </view>

      <!-- 确定按钮 -->
      <view class="confirm-btn-container">
        <text class="confirm-btn" @click="confirmSelection">确定</text>
      </view>
    </view>

    <!-- 课程类型区域 -->
    <view class="course-type-section">
      <view class="section-header">
        <text class="section-title">课程类型</text>
        <text class="add-btn" @click="addCourseType">新增</text>
      </view>

      <!-- 课程类型卡片 -->
      <view class="course-card" v-for="(course, index) in courseTypes" :key="index">
        <view class="card-row">
          <text class="label">人数</text>
          <text class="value">{{ course.peopleCount }}</text>
        </view>
        <view class="card-row">
          <text class="label">课时</text>
          <text class="value">{{ course.classHours }}</text>
        </view>
        <view class="card-row">
          <text class="label">上课时间</text>
          <text class="value">{{ course.classTime }}</text>
        </view>
        <view class="card-row">
          <text class="label">教练费</text>
          <text class="value">{{ course.coachFee }}</text>
        </view>
        <view class="card-row">
          <text class="label">课时费</text>
          <text class="value">{{ course.classFee }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentYear: 2025,
      currentMonth: 4,
      weekDays: ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su'],
      selectedDays: [],
      courseTypes: [
        {
          peopleCount: '2',
          classHours: '3',
          classTime: '09:00-10:00',
          coachFee: 'W000',
          classFee: 'W000'
        }
      ]
    };
  },
  computed: {
    days() {
      // 生成当前月的天数数组
      const daysInMonth = new Date(this.currentYear, this.currentMonth, 0).getDate();
      const daysArray = [];

      // 添加空白占位符（根据当月第一天是星期几）
      const firstDay = new Date(this.currentYear, this.currentMonth - 1, 1).getDay();
      // 调整周一为第一天
      const adjustedFirstDay = firstDay === 0 ? 6 : firstDay - 1;

      for (let i = 0; i < adjustedFirstDay; i++) {
        daysArray.push('');
      }

      // 添加日期
      for (let i = 1; i <= daysInMonth; i++) {
        daysArray.push(i);
      }

      return daysArray;
    }
  },
  methods: {
    prevMonth() {
      if (this.currentMonth === 1) {
        this.currentMonth = 12;
        this.currentYear--;
      } else {
        this.currentMonth--;
      }
    },
    nextMonth() {
      if (this.currentMonth === 12) {
        this.currentMonth = 1;
        this.currentYear++;
      } else {
        this.currentMonth++;
      }
    },
    toggleDay(day) {
      if (!day) return;

      const index = this.selectedDays.indexOf(day);
      if (index > -1) {
        this.selectedDays.splice(index, 1);
      } else {
        this.selectedDays.push(day);
      }
    },
    confirmSelection() {
      console.log('选中的日期:', this.selectedDays);
      uni.showToast({
        title: '日期已确认',
        icon: 'success'
      });
    },
    addCourseType() {
      this.courseTypes.push({
        peopleCount: '2',
        classHours: '3',
        classTime: '09:00-10:00',
        coachFee: 'W000',
        classFee: 'W000'
      });
    }
  }
};
</script>

<style>
.page-container {
  padding: 30rpx;
  background: linear-gradient(to bottom, #0E0E0E 20%, #0e0e0e, #8b8b8b, #d7d7d7, #e7e7e7);
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 36rpx;
  font-weight: 700;
  margin: 100rpx 0 30rpx 0;
  /* text-align: center; */
  color: #fff;
}

.title {
  font-weight: 700;
  font-size: 36rpx;
  color: #333333;
}

.tip {
  margin-bottom: 30rpx;

}

.tip text {
  font-weight: 700;
  font-size: 36rpx;
  color: #fff;
}

.calendar-card {
  height: 650rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 17rpx;
  padding: 40rpx 30rpx 20rpx 30rpx;
  margin-bottom: 40rpx;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.month-year {
  font-size: 32rpx;
  font-weight: bold;
}

.arrow-container {
  display: flex;
  gap: 20rpx;
}

.arrow {
  width: 40rpx;
  height: 40rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.week-days {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20rpx;
}

.week-day {
  font-size: 28rpx;
  color: #666;
}

.days-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
}

.day {
  width: 70rpx;
  height: 70rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  font-size: 28rpx;
}

.day.selected {
  background: #EDF2F7;
}

.confirm-btn-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 30rpx;
}

.confirm-btn {
  width: 124rpx;
  height: 60rpx;
  border-radius: 8rpx;
  border: 1rpx solid #B7975E;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 28rpx;
  color: #B7975E;
}

.course-type-section {
  margin-top: 40rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-weight: 700;
  font-size: 36rpx;
  color: #333333;
}

.add-btn {
  font-size: 28rpx;
  color: #333333;
}

.course-card {
  height: 528rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.card-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.label {
  font-size: 28rpx;
  color: #666666;
  line-height: 40rpx;
}

.value {
  height: 60rpx;
  background: #F5F5F5;
  border-radius: 8rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #333333;
  min-width: 200rpx;
  text-align: right;
}
</style>