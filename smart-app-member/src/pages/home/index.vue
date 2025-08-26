<template>
  <view class="container">
    <!-- 整合后的俱乐部卡片（包含轮播图） -->
    <view class="box" />
    <view class="cl-container">
      <view class="club-card">
        <!-- 轮播图放在卡片顶部 -->
        <swiper class="club-swiper" indicator-dots autoplay interval="3000">
          <swiper-item v-for="(item, index) in clubInfo.carouselImages" :key="index">
            <image class="swiper-img" :src="item" mode="aspectFill" />
          </swiper-item>
        </swiper>

        <view class="club-content">
          <view class="club-header">
            <text class="club-name">{{ clubInfo.clubName }}</text>
          </view>
          <view class="club-status">
            <text class="open-status">营业时间：{{ clubInfo.businessHours }}</text>
            <view>
              <text class="booking-status">现在时间段可预约</text>
            </view>
          </view>
          <view class="club-info">
            <view style="margin-top: 30rpx;">
              <text class="address">地址：{{ clubInfo.address }}</text>
              <view style="display: flex;align-items: center;margin-top: 10rpx;">
                <image style="width: 24rpx; height: 24rpx;margin-top: -10rpx; margin-right: 8rpx;"
                  src="/static/images/index/map.png" mode="scaleToFill" />
                <text class="distance1">距您3.0公里，约13分钟</text>
              </view>
            </view>
            <view style="margin-top: 30rpx;text-align: center;">
              <image style="width: 50rpx; height: 50rpx;" src="/static/images/index/phone.png" mode="scaleToFill" />
              <view style="margin-top: -20rpx;">
                <text class="distance">{{ clubInfo.phone }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
      <view class="section-title" @click="toggleDropdown">
        <text style="margin-right: 30rpx; color: #fff;">{{ familyMembers }}</text>
        <uni-icons :type="showDropdown ? 'up' : 'down'" size="15" color="#fff" />

        <!-- 下拉框 -->

      </view>
    </view>
    <view class="dropdown-menu" v-if="showDropdown">
      <view class="dropdown-item" v-for="(rider, index) in riders" :key="index"
        :class="{ 'dropdown-item-selected': selectedRider === index }" @click.stop="selectRider(index)">
        {{ rider.name }}
      </view>
    </view>
    <!-- 教练列表 -->
    <view class="coach-list">
      <view class="coach-card" @click="goToDetail" v-for="(coach, index) in coachList" :key="index">
        <view class="coach-header">
          <view class="coach-info-left">
            <image class="coach-avatar" :src="avatarUrl" mode="aspectFill" />
            <view class="coach-name-box">
              <view>
                <text class="coach-name">{{ coach.actualName }}</text>
              </view>
              <view>
                <text class="coach-experience">从业{{ coach.workingYears }}年</text>
              </view>
            </view>
          </view>
        </view>

        <view class="certificate-box">
          <view class="coach-tags">
            <text class=" certificate1" v-for="(cert, i) in coach.riderLevelTags" :key="i">{{ cert }}</text>
          </view>
          <view class="coach-tags">
            <text class="certificate" v-for="(tag, i) in coach.specialtiesList" :key="i">{{ tag }}</text>
          </view>

        </view>

        <view class="coach-footer">
          <text class="capacity" />
          <view style="display: flex;align-items: center;">
            <text class="price"><text class="price1">¥</text>{{ coach.coachFee }} <text class="price1">每人</text>/<text
                class="price1">鞍时</text></text>
            <button @tap.stop="showCoursePopup(coach.coachFee, coach.coachNo)" class="booking-btn">约Ta</button>
          </view>
        </view>

      </view>
    </view> -->
    <!-- 课程类型选择弹窗 -->
    <uni-popup ref="coursePopup" type="bottom" class="custom-popup course-popup">
      <view class="popup-content">
        <view class="popup-header">
          <text class="popup-title">课程类型</text>
          <uni-icons type="closeempty" size="24" color="#999" @click="closeCoursePopup" />
        </view>
        <view class="course-types">
          <view v-for="(type, index) in courseTypes" :key="index" class="course-type-item"
            :class="{ 'selected': selectedCourseType === index }"
            @click="selectCourseType(index, type.basePrice, type.courseCode)">
            {{ type.courseName }}
          </view>
        </view>
        <button class="confirm-btn" @click="showTimePopup">确认</button>
      </view>
    </uni-popup>

    <!-- 时间选择弹窗 -->
    <uni-popup ref="timePopup" type="bottom" class="custom-popup1 time-popup">
      <view class="popup-content">
        <view class="popup-header">
          <text class="popup-title">预约服务时间</text>
          <uni-icons type="closeempty" size="24" color="#999" @click="closeTimePopup" />
        </view>

        <view class="time-selector">
          <!-- 左侧日期列表 -->
          <scroll-view class="date-list" scroll-y>
            <view v-for="(date, index) in availableDates" :key="index" class="date-item"
              :class="{ 'selected': selectedDate === index }" @click="selectDate(index)">
              <text class="date">{{ date.date }}</text>
              <text class="day">({{ date.day }})</text>
            </view>
          </scroll-view>

          <!-- 右侧时间列表 -->
          <scroll-view class="time-list" scroll-y>
            <view class="time-grid">
              <view v-for="(time, index) in availableTimes" :key="index" class="time-item" :class="{
                'selected': isTimeSelected(time.id),
                'disabled': time.disabled
              }" @click="!time.disabled && toggleTimeSelection(time)">
                <text>{{ time.range }}</text>
                <text v-if="time.disabled" class="full-text">已约满</text>
              </view>
            </view>
          </scroll-view>
        </view>

        <!-- 已选时间展示区域 -->

        <button class="confirm-btn" @click="confirmBooking">确认</button>
      </view>
    </uni-popup>
  </view>
  <CustomTabbar />
</template>

<script>
import CustomTabbar from '@/components/custom-tabbar/custom-tabbar.vue';
import { getCoachList, getClubInfo, getCourseList } from '@/api/home/index';
export default {
  components: {
    CustomTabbar
  },
  data() {
    return {
      role: 'usr',
      familyMembers: '家庭成员',
      selectedTimes: [], // 存储选中的时间段
      basePrice: '',
      coachFee: '',
      clubInfo: '',
      courseCode: '',
      coachNo: '',
      avatarUrl: 'https://q2.itc.cn/q_70/images03/20241013/1b82e2a4ebc94eb7978a3794badf091e.jpeg',
      swiperList: [
        'https://qcloud.dpfile.com/pc/MashFIm6RHlAZyEdPbcehKFbTiPwdFybcAEqIYGzwFgWLRVmASbIkNvi-6HLL5Tz.jpg',
        'https://hellorfimg.zcool.cn/provider_image/large/hi2247588925.jpg?x-image-process=image/format,webp',
        'https://qcloud.dpfile.com/pc/MashFIm6RHlAZyEdPbcehKFbTiPwdFybcAEqIYGzwFgWLRVmASbIkNvi-6HLL5Tz.jpg'
      ],
      coachList: [
        {
          name: '教练2',
          avatar: 'https://q2.itc.cn/q_70/images03/20241013/1b82e2a4ebc94eb7978a3794badf091e.jpeg',
          experience: 8,
          tags: ['#中级进阶', '#赛事指导'],
          certificates: ['中一级骑手证', '3星教练证'],
          capacity: 1,
          price: '000',
          bookingTime: '7a'
        },
        {
          name: '教练3',
          avatar: 'https://q6.itc.cn/images01/20250620/66c99a4d9fea4f1fa43cf55ff5499caa.jpeg',
          experience: 5,
          tags: ['#初级教学', '#儿童课程'],
          certificates: ['中二级骑手证', '2星教练证'],
          capacity: 2,
          price: '500',
          bookingTime: '9a'
        },
        {
          name: '教练4',
          avatar: 'https://q6.itc.cn/images01/20250620/66c99a4d9fea4f1fa43cf55ff5499caa.jpeg',
          experience: 10,
          tags: ['#高级训练', '#赛事指导'],
          certificates: ['高级骑手证', '4星教练证'],
          capacity: 1,
          price: '800',
          bookingTime: '5p'
        }
      ],
      showDropdown: false,
      selectedRider: null,
      riders: [
        { name: '骑手名字1', capacity: 1 },
        { name: '骑手名字2', capacity: 1 },
        { name: '骑手名字3', capacity: 1 }
      ],
      courseTypes: ['体验课', '基础课', '进阶课', '高级课', '大师课', '理论课'],
      selectedCourseType: null,
      availableDates: [
        { date: '06-05', day: '今天', id: '0605' },
        { date: '06-06', day: '周五', id: '0606' },
        { date: '06-07', day: '周六', id: '0607' },
        { date: '06-08', day: '周日', id: '0608' },
        { date: '06-09', day: '周一', id: '0609' },
        { date: '06-10', day: '周二', id: '0610' }
      ],
      availableTimes: [
        { id: '06050800', range: '08:00-09:00', disabled: false, dateId: '0605' },
        { id: '06050900', range: '09:00-10:00', disabled: true, dateId: '0605' },
        { id: '06051000', range: '10:00-11:00', disabled: false, dateId: '0605' },
        { id: '06051100', range: '11:00-12:00', disabled: true, dateId: '0605' },
        { id: '06051200', range: '12:00-13:00', disabled: false, dateId: '0605' },
        { id: '06051300', range: '13:00-14:00', disabled: false, dateId: '0605' },
        { id: '06051400', range: '14:00-15:00', disabled: true, dateId: '0605' },
        { id: '06051500', range: '15:00-16:00', disabled: false, dateId: '0605' },
        { id: '06051600', range: '16:00-17:00', disabled: false, dateId: '0605' },
        { id: '06051700', range: '17:00-18:00', disabled: false, dateId: '0605' },
        { id: '06051800', range: '18:00-19:00', disabled: true, dateId: '0605' },
        { id: '06051900', range: '19:00-20:00', disabled: true, dateId: '0605' },
      ],
      selectedDate: 0,
      selectedTime: null,
      orderCreateForm: {
        clubCode: 'DEMO_CLUB_001',
        coachNo: '',
        courseCode: '',
        times: [{ date: '', timeSlots: [] }],
        coachFee: '',
        baseFee: '',
        totalAmount: ''
      }
    }
  },
  computed: {
    // 根据选中日期过滤时间段
    filteredTimes() {
      const selectedDateId = this.availableDates[this.selectedDate].id;
      return this.availableTimes.filter(time => time.dateId === selectedDateId);
    }
  },
  async onLoad() {
    await this.getCoachList1()
    await this.getClubInfo1()
    await this.getCourseList1()
  },
  methods: {
    getCourseList1() {
      getCourseList({
        "clubCode": "DEMO_CLUB_001"
      }).then(res => {
        console.log(res);
        if (res.code === 0) {
          this.courseTypes = res.data
        }
      })
    },
    getClubInfo1() {
      getClubInfo({
        "clubCode": "DEMO_CLUB_001"
      }).then(res => {
        console.log(res);
        if (res.code === 0) {
          this.clubInfo = res.data
        }
      })
    },
    async getCoachList1() {
      const res = await getCoachList({
        "clubCode": "DEMO_CLUB_001"
      })
      console.log(res);
      if (res.code === 0) {
        this.coachList = res.data || []
      }
    },
    toggleDropdown() {
      this.showDropdown = !this.showDropdown;
    },
    selectRider(index) {
      this.selectedRider = index;
      this.showDropdown = false;
      // 这里可以添加选中后的逻辑
    },
    goToDetail() {
      uni.navigateTo({ url: '/pages/support/change-log/change-log-detail' })
    },
    // 选择日期
    selectDate(index) {
      this.selectedDate = index;
    },
    showCoursePopup(coachFee, coachNo) {
      this.$refs.coursePopup.open()
      this.coachFee = coachFee
      this.coachNo = coachNo
    },
    closeCoursePopup() {
      this.$refs.coursePopup.close()
    },
    selectCourseType(index, price, courseCode) {
      this.selectedCourseType = index
      this.basePrice = price
      this.courseCode = courseCode
    },
    showTimePopup() {
      this.closeCoursePopup()
      this.$refs.timePopup.open()
    },
    closeTimePopup() {
      this.$refs.timePopup.close()

    },
    selectDate(index) {
      this.selectedDate = index
      // 这里可以添加根据日期获取不同时间段的逻辑
    },
    // 切换时间选择（多选/取消）
    toggleTimeSelection(time) {
      const index = this.selectedTimes.findIndex(t => t.id === time.id);

      if (index === -1) {
        // 添加选择
        this.selectedTimes.push({
          id: time.id,
          range: time.range,
          date: this.availableDates[this.selectedDate].date,
          day: this.availableDates[this.selectedDate].day
        });
      } else {
        // 取消选择
        this.selectedTimes.splice(index, 1);
      }
    },
    selectTime(index) {
      this.selectedTime = index
    },
    // 移除已选时间
    removeTimeSelection(timeId) {
      this.selectedTimes = this.selectedTimes.filter(t => t.id !== timeId);
    },

    // 检查时间是否已被选择
    isTimeSelected(timeId) {
      return this.selectedTimes.some(t => t.id === timeId);
    },

    // 格式化已选时间显示
    formatSelectedTime(time) {
      return `${time.date} ${time.range}`;
    },
    confirmBooking() {
      console.log(this.selectedTime);

      if (this.selectedTimes === null) {
        uni.showToast({
          title: '请选择预约时间',
          icon: 'none'
        })
        return
      }
      this.orderCreateForm.times[0].date = this.selectedTimes[0].date
      const timp = this.selectedTimes
      timp.forEach(item => {
        this.orderCreateForm.times[0].timeSlots.push(item.range)
      })
      // 准备传递的参数
      this.orderCreateForm.coachNo = this.coachNo
      this.orderCreateForm.courseCode = this.courseCode
      this.orderCreateForm.coachFee = this.coachFee
      this.orderCreateForm.baseFee = this.basePrice
      this.orderCreateForm.totalAmount = (this.basePrice + this.coachFee) * this.selectedTimes.length

      console.log('预约参数:', this.orderCreateForm);
      uni.navigateTo({ url: `/pages/order-detail/order-detail?data=${encodeURIComponent(JSON.stringify(this.orderCreateForm))}` })
      // 这里添加预约确认逻辑
      uni.showToast({
        title: '预约成功',
        icon: 'success'
      })
      this.closeTimePopup()
    }
  }
}
</script>

<style>
.container {
  background: #e7e7e7;
  padding-bottom: 20rpx;
  z-index: -2;
}

.box {
  position: relative;
  top: 0;
  background: linear-gradient(to bottom, #0E0E0E 20%, #0e0e0e, #8b8b8b, #d7d7d7, #e7e7e7);
  height: 1400rpx;
  z-index: 0.9;
}

.cl-container {
  padding: 26rpx;
  position: absolute;
  top: 0;
  z-index: 1;
}

/* 俱乐部卡片样式（包含轮播图） */

.club-card {
  width: 660rpx;
  height: 700rpx;
  margin-top: 54rpx;
  background-color: #fff;
  padding: 22rpx;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1);
}

/* 轮播图样式 */
.club-swiper {
  width: 100%;
  height: 370rpx;
  border-radius: 20rpx;
}

.swiper-img {
  width: 100%;
  height: 100%;
  border-radius: 20rpx;
}

/* 俱乐部内容区域 */
.club-content {
  padding: 20rpx;
}

.club-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
  margin-top: 18rpx;
}

.club-name {
  font-size: 36rpx;
  color: #333;
}


.club-status {
  height: 80rpx;
  border-top: 2rpx dashed #767676;
  border-bottom: 2rpx dashed #767676;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.open-status {
  font-size: 28rpx;
  color: #333;
}

.booking-status {
  margin-right: 30rpx;
  font-size: 22rpx;
  color: rgba(51, 51, 51, 0.6);
}

.club-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.address {
  font-size: 28rpx;
  color: #333;
}

.distance {
  font-size: 20rpx;
  color: rgba(51, 51, 51, 0.6);
  margin-bottom: 18rpx;
  margin-left: 8rpx;
}

.distance1 {
  font-size: 22rpx;
  color: rgba(51, 51, 51, 0.6);
}


/* 教练卡片样式 */
.coach-list {
  margin-top: -495rpx;
  padding: 26rpx;
  overflow: hidden;
  margin-bottom: 60rpx;
  z-index: 100;
  position: relative;
}

.coach-card {
  padding: 20rpx;
  /* border-bottom: 1rpx solid #eee; */
  background-color: #fff;
  margin-bottom: 20rpx;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 20rpx;
}

.coach-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15rpx;
}

.coach-info-left {
  display: flex;
  align-items: center;
}

.coach-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.coach-name-box {
  margin-left: 30rpx;
  width: 500rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.coach-name {
  font-size: 36rpx;
  font-weight: 500;
  color: #1A1A1A;
}

.coach-experience {
  font-size: 24rpx;
  color: #8C8C8C;
}

.coach-tags {
  display: flex;
  overflow-x: auto;
  scrollbar-width: none;
  /* Firefox */
  -ms-overflow-style: none;
  /* IE and Edge */
  padding: 10px 0;
  white-space: nowrap;
  scroll-behavior: smooth;
}

/* 隐藏滚动条 - Chrome, Safari and Opera */
.coach-tags::-webkit-scrollbar {
  display: none;
}

.tag {
  font-size: 22rpx;
  color: #07c160;
  background-color: #e6f7ee;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
  margin-left: 10rpx;
  margin-bottom: 5rpx;
}

.certificate-box {
  margin-top: 20rpx;
}

.certificate1 {
  font-size: 22rpx;
  color: #333;
  background-color: #f6f6f6;
  padding: 10rpx 34rpx;
  border-radius: 52rpx;
  margin-right: 22rpx;
  margin-bottom: 16rpx;
}

.certificate {
  font-size: 22rpx;
  color: #333;
  background-color: #f6f6f6;
  font-weight: 600;
  padding: 10rpx 34rpx;
  border-radius: 52rpx;
  margin-right: 22rpx;
  margin-bottom: 8rpx;
}

.coach-footer {
  background: linear-gradient(to right, #F9EEDA, #fff);
  border-radius: 16rpx;
  border: 1rpx solid #6b4b12;
  width: 590rpx;
  height: 88rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 36rpx;
  margin-top: 25rpx;
  margin-bottom: 25rpx;
}

.capacity,
.price {
  font-size: 28rpx;
}

.price {
  color: #6b4b12;
  margin-right: 20rpx;
}

.price1 {
  font-size: 20rpx;
}

.booking-btn {
  background-color: #b7975e;
  color: #fff;
  border-radius: 88rpx;
  padding: 0 30rpx;
  font-size: 24rpx;
  height: 60rpx;
  line-height: 60rpx;
  margin: 0;
}


/* 新增的下拉框样式 */
.section-title {
  width: 238rpx;
  height: 64rpx;
  line-height: 64rpx;
  text-align: center;
  font-size: 30rpx;
  background: rgba(53, 53, 53, 0.23);
  border-radius: 154rpx;
  margin-top: 25rpx;
  margin-bottom: 25rpx;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  z-index: 9999 !important;
}

.dropdown-menu {
  position: absolute;
  top: 910rpx;
  left: 25rpx;
  width: 232rpx;

  background: #FFFFFF;
  box-shadow: 0rpx 6rpx 16rpx 0rpx rgba(0, 0, 0, 0.16);
  border-radius: 16rpx;
  border: 2rpx solid #D8D8D8;
  z-index: 9999 !important;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 16rpx;
  box-sizing: border-box;
}

.dropdown-item {
  font-size: 28rpx;
  color: #3D3D3D;
  height: 52rpx;
  line-height: 52rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10rpx;
  margin-bottom: 10rpx;
}

.dropdown-item-selected {
  width: 200rpx;
  height: 52rpx;
  background: #FAF0DE;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #6B4B12;
}

.capacity-text {
  font-size: 24rpx;
  color: #999;
}

/* 弹窗样式 */
.custom-popup {
  border-radius: 32rpx 32rpx 0 0;
  z-index: 9999 !important;
}

.custom-popup1 {
  border-radius: 32rpx 0 0 0;
  z-index: 9999 !important;
}

.popup-content {
  padding: 40rpx;
  background-color: #fff;
  border-radius: 32rpx 32rpx 0 0;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40rpx;
}

.popup-title {
  margin-left: 20rpx;
  font-family: 'Alibaba PuHuiTi 2.0', 'Alibaba PuHuiTi 20';
  font-weight: 600;
  font-size: 36rpx;
  color: #333333;
}

.course-popup,
.course-popup1,
.time-popup {
  z-index: 9999 !important;
}

/* 覆盖 uni-popup 组件的默认 z-index */
::v-deep .uni-popup {
  z-index: 9999 !important;
}

::v-deep .uni-popup__wrapper {
  z-index: 10000 !important;
}

::v-deep .uni-popup__mask {
  z-index: 9998 !important;
}

/* 修复课程类型标签布局 - 一行三个 */
.course-types {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 60rpx;
  gap: 20rpx;
  /* 添加间距 */
}

.course-type-item {
  width: 210rpx;
  /* 调整宽度以适应一行三个 */
  height: 84rpx;
  background: #F6F6F6;
  border-radius: 146rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 400;
  font-size: 28rpx;
  color: #666666;
  flex-shrink: 0;
  /* 防止缩小 */
}

.course-type-item.selected {
  background: #F9EEDA;
  border: 2rpx solid #A0762C;
  font-weight: 500;
  color: #8A5800;
}

/* 确保弹窗内容正确显示 */
.popup-content {
  padding: 40rpx 20rpx 20rpx 0;
  background-color: #fff;
  border-radius: 32rpx 32rpx 0 0;
  z-index: 10001 !important;
  position: relative;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40rpx;
}

.popup-title {
  font-family: 'Alibaba PuHuiTi 2.0', 'Alibaba PuHuiTi 20';
  font-weight: 600;
  font-size: 36rpx;
  color: #333333;
}

/* 时间选择器样式调整 */
.time-selector {
  display: flex;
  margin-bottom: 60rpx;
  gap: 20rpx;
  height: 600rpx;
  /* 增加高度 */
}

/* 左侧日期列表 - 修改为靠左无间隔 */
.date-list {
  width: 200rpx;
  height: 100%;
  background: #f6f6f6;
  border-radius: 16rpx;
  padding: 0;
  /* 移除内边距 */
}

.date-item {
  width: 100%;
  /* 宽度100% */
  height: 108rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-family: 'Alibaba PuHuiTi 2.0', 'Alibaba PuHuiTi 20';
  font-weight: 400;
  font-size: 28rpx;
  color: #232220;
  background: #f6f6f6;
  /* 未选中背景色 */
  border-radius: 0;
  /* 移除圆角 */
  margin: 0;
  /* 移除外边距 */
  padding: 0 20rpx;
  /* 只保留左右内边距 */
  box-sizing: border-box;
}

.date-item.selected {
  background: #F9EEDA;
  /* 选中背景色 */
  font-weight: 600;
  color: #8A5800;
}

.date {
  font-size: 32rpx;
  margin-bottom: 4rpx;
}

.day {
  font-size: 24rpx;
}

/* 右侧时间列表 - 修改为两列排列 */
.time-list {
  flex: 1;
  height: 100%;
}

.time-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 20rpx;
  /* 网格间距 */
}

.time-item {
  width: calc(50% - 10rpx);
  /* 两列布局，考虑间距 */
  height: 112rpx;
  border: 2rpx solid #E2E2E2;
  border-radius: 18rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-family: 'OPPOSans', 'OPPOSans';
  font-weight: 400;
  font-size: 30rpx;
  color: #666666;
  background: #FFFFFF;
  /* 默认背景色 */
  box-sizing: border-box;
}

.time-item.selected {
  background: #F9EEDA;
  border: 2rpx solid #A0762C;
  font-weight: 500;
  color: #8A5800;
}

.time-item.disabled {
  background: #F6F6F6;
  color: #999999;
  border-color: #F6F6F6;
}

.full-text {
  font-size: 24rpx;
  color: #999999;
  margin-top: 4rpx;
}

/* 确认按钮样式保持不变 */
.confirm-btn {
  height: 88rpx;
  background: #B7975E;
  border-radius: 146rpx;
  font-family: 'Alibaba PuHuiTi 2.0', 'Alibaba PuHuiTi 20';
  font-weight: 600;
  font-size: 36rpx;
  color: #FFFFFF;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20rpx;
  border: none;
  outline: none;
}

.confirm-btn:active {
  opacity: 0.8;
}

/* 全局弹窗修复 */
uni-popup,
.uni-popup,
.uni-popup__wrapper {
  z-index: 9999 !important;
}
</style>