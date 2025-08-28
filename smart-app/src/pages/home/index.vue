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
              <view v-for="(time, index) in filteredTimes" :key="index" class="time-item" :class="{
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
      courseTypes: [
        { courseCode: 'EXPERIENCE_COURSE', courseName: '体验课', basePrice: 150 },
        { courseCode: 'BASIC_COURSE', courseName: '基础课', basePrice: 200 },
        { courseCode: 'INTERMEDIATE_COURSE', courseName: '进阶课', basePrice: 300 },
        { courseCode: 'ADVANCED_COURSE', courseName: '高级课', basePrice: 400 },
        { courseCode: 'MASTER_COURSE', courseName: '大师课', basePrice: 500 },
        { courseCode: 'THEORY_COURSE', courseName: '理论课', basePrice: 100 }
      ],
      selectedCourseType: null,
      availableDates: [],
      availableTimes: [],
      selectedDate: 0,
      selectedTime: null,
      voiceGuidanceActive: false, // 语音引导激活状态
      voiceFlowType: '', // 语音流程类型
      voicePresetData: {}, // 语音预设数据
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
      if (!this.availableDates || this.availableDates.length === 0 || this.selectedDate >= this.availableDates.length) {
        return [];
      }
      
      const selectedDateId = this.availableDates[this.selectedDate].id;
      const filtered = this.availableTimes.filter(time => time.dateId === selectedDateId);
      
      console.log('🏠 [时间过滤] 选中日期:', this.availableDates[this.selectedDate], '过滤后时间段:', filtered.length);
      
      return filtered;
    }
  },
  async onLoad(options) {
    console.log('🏠 [首页加载] 页面参数:', options);
    
    // 初始化时间数据
    this.initializeDateTimeData();
    
    await this.getCoachList1()
    await this.getClubInfo1()
    await this.getCourseList1()
    
    // 处理语音约课跳转
    if (options.from === 'voice_booking' && options.guidance) {
      try {
        const guidance = JSON.parse(decodeURIComponent(options.guidance));
        console.log('🏠 [首页加载] 语音引导数据:', guidance);
        this.handleVoiceBookingGuidance(guidance);
      } catch (e) {
        console.error('🏠 [首页加载] 解析语音引导数据失败:', e);
        wx.showModal({
          title: 'AI约课助手',
          content: '语音引导数据异常，请重新进行语音约课',
          confirmText: '重新约课',
          cancelText: '取消',
          success: (res) => {
            if (res.confirm) {
              // 清理异常参数，正常进入首页
              wx.reLaunch({
                url: '/pages/home/index'
              });
            }
          }
        });
      }
    }
  },
  onReady() {
    console.log('🏠 [首页就绪] 页面已就绪，设置事件监听');
    
    // 监听语音组件的弹窗事件
    uni.$on('voice-show-course-popup', (options) => {
      console.log('🏠 [事件监听] 收到显示课程弹窗事件:', options);
      this.handleVoiceCoursePopup(options);
    });
    
    uni.$on('voice-show-time-popup', (options) => {
      console.log('🏠 [事件监听] 收到显示时间弹窗事件:', options);
      this.handleVoiceTimePopup(options);
    });
  },
  onUnload() {
    console.log('🏠 [首页卸载] 移除事件监听');
    
    // 移除事件监听
    uni.$off('voice-show-course-popup');
    uni.$off('voice-show-time-popup');
  },
  methods: {
    // 初始化日期时间数据
    initializeDateTimeData() {
      console.log('🏠 [时间初始化] 开始初始化日期时间数据');
      
      const today = new Date();
      const dates = [];
      
      // 生成未来7天的日期
      for (let i = 0; i < 7; i++) {
        const date = new Date(today);
        date.setDate(today.getDate() + i);
        
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const dateStr = `${month}-${day}`;
        const dateId = `${month}${day}`;
        
        let dayStr = '';
        if (i === 0) {
          dayStr = '今天';
        } else if (i === 1) {
          dayStr = '明天';
        } else if (i === 2) {
          dayStr = '后天';
        } else {
          const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
          dayStr = weekdays[date.getDay()];
        }
        
        dates.push({
          date: dateStr,
          day: dayStr,
          id: dateId,
          fullDate: date
        });
      }
      
      this.availableDates = dates;
      
      // 生成固定的硬编码时间段
      this.generateFixedTimeSlots();
      
      console.log('🏠 [时间初始化] ✅ 日期时间数据初始化完成:', {
        dates: this.availableDates.length,
        times: this.availableTimes.length
      });
    },
    
    // 生成固定的硬编码时间段
    generateFixedTimeSlots() {
      const fixedTimeSlots = [
        { range: '09:00-10:00', hour: 9 },
        { range: '10:00-11:00', hour: 10 },
        { range: '11:00-12:00', hour: 11 },
        { range: '14:00-15:00', hour: 14 },
        { range: '15:00-16:00', hour: 15 },
        { range: '16:00-17:00', hour: 16 },
        { range: '17:00-18:00', hour: 17 },
      ];
      
      const today = new Date();
      const currentHour = today.getHours();
      const currentDateId = this.availableDates[0]?.id; // 今天的日期ID
      
      this.availableTimes = [];
      
      // 为每个日期生成相同的时间段
      this.availableDates.forEach((dateInfo, dateIndex) => {
        fixedTimeSlots.forEach((slot, slotIndex) => {
          const timeId = `${dateInfo.id}${String(slot.hour).padStart(2, '0')}00`;
          
          // 如果是今天，过滤掉已过的时间
          let disabled = false;
          if (dateIndex === 0 && slot.hour <= currentHour) {
            disabled = true;
          }
          
          // 随机设置一些时间段为已约满状态（演示用）
          if (!disabled && Math.random() < 0.15) {
            disabled = true;
          }
          
          this.availableTimes.push({
            id: timeId,
            range: slot.range,
            disabled: disabled,
            dateId: dateInfo.id,
            hour: slot.hour
          });
        });
      });
      
      console.log('🏠 [时间初始化] 生成固定时间段:', this.availableTimes.length);
    },
    getCourseList1() {
      console.log('🏠 [API调用] 开始获取课程列表');
      getCourseList({
        "clubCode": "DEMO_CLUB_001"
      }).then(res => {
        console.log('🏠 [API调用] 课程列表响应:', res);
        if (res.code === 0 && res.data && Array.isArray(res.data)) {
          this.courseTypes = res.data;
          console.log('🏠 [API调用] ✅ 课程数据已更新:', this.courseTypes);
        } else {
          console.warn('🏠 [API调用] ⚠️ 课程数据获取失败，使用默认数据');
          // 保持默认的课程数据结构，但转换为对象格式
          this.courseTypes = [
            { courseCode: 'EXPERIENCE_COURSE', courseName: '体验课', basePrice: 150 },
            { courseCode: 'BASIC_COURSE', courseName: '基础课', basePrice: 200 },
            { courseCode: 'INTERMEDIATE_COURSE', courseName: '进阶课', basePrice: 300 },
            { courseCode: 'ADVANCED_COURSE', courseName: '高级课', basePrice: 400 },
            { courseCode: 'MASTER_COURSE', courseName: '大师课', basePrice: 500 },
            { courseCode: 'THEORY_COURSE', courseName: '理论课', basePrice: 100 }
          ];
        }
      }).catch(err => {
        console.error('🏠 [API调用] ❌ 课程列表获取异常:', err);
        // 使用兜底数据
        this.courseTypes = [
          { courseCode: 'EXPERIENCE_COURSE', courseName: '体验课', basePrice: 150 },
          { courseCode: 'BASIC_COURSE', courseName: '基础课', basePrice: 200 },
          { courseCode: 'INTERMEDIATE_COURSE', courseName: '进阶课', basePrice: 300 },
          { courseCode: 'ADVANCED_COURSE', courseName: '高级课', basePrice: 400 },
          { courseCode: 'MASTER_COURSE', courseName: '大师课', basePrice: 500 },
          { courseCode: 'THEORY_COURSE', courseName: '理论课', basePrice: 100 }
        ];
      });
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
    showCoursePopup(coachFee, coachNo) {
      console.log('🏠 [课程选择] 显示课程弹窗, 教练:', coachNo, '费用:', coachFee);
      
      this.$refs.coursePopup.open()
      this.coachFee = coachFee
      this.coachNo = coachNo
      this.orderCreateForm.coachNo = coachNo
      
      // 如果是语音流程且有预设课程，自动选择并跳转
      if (this.voiceGuidanceActive && this.voiceFlowType) {
        console.log('🎯 [语音引导] 教练选择完成，流程类型:', this.voiceFlowType);
        
        if (this.voiceFlowType === 'coach-course') {
          // 需要继续选择课程，正常显示课程弹窗
          console.log('🎯 [语音引导] 继续选择课程');
        } else if (this.voiceFlowType === 'coach-time') {
          // 有预设课程，自动选择并跳转到时间选择
          if (this.selectedCourseType !== null) {
            console.log('🎯 [语音引导] 自动选择预设课程，跳转时间选择');
            setTimeout(() => {
              this.autoSelectCourseAndShowTime();
            }, 300);
          }
        } else if (this.voiceFlowType === 'coach-only') {
          // 课程和时间都已预设，直接确认
          if (this.selectedCourseType !== null && this.voicePresetData.times) {
            console.log('🎯 [语音引导] 课程和时间已预设，准备确认订单');
            setTimeout(() => {
              this.autoCompleteBooking();
            }, 300);
          }
        }
      }
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
      console.log('🏠 [时间选择] 显示时间选择弹窗');
      
      // 检查是否已选择课程类型
      if (this.selectedCourseType === null) {
        uni.showToast({
          title: '请先选择课程类型',
          icon: 'none'
        });
        return;
      }
      
      // 🔧 关键修复：检查是否已有时间参数，如果有则跳过时间选择
      if (this.voiceGuidanceActive && (this.voicePresetData.times || this.selectedTimes.length > 0)) {
        console.log('✅ [流程跳过] 检测到已有时间参数，跳过时间选择，直接确认订单');
        this.closeCoursePopup();
        
        // 直接进入订单确认流程
        setTimeout(() => {
          this.confirmBooking();
        }, 300);
        return;
      }
      
      this.closeCoursePopup()
      this.$refs.timePopup.open()
      
      // 如果是语音流程，检查是否需要自动处理
      if (this.voiceGuidanceActive && this.voiceFlowType) {
        console.log('🎯 [语音引导] 时间选择弹窗已打开，流程类型:', this.voiceFlowType);
        
        // 如果有预设时间，提示用户
        if (this.voicePresetData.times) {
          console.log('🎯 [语音引导] 发现预设时间数据:', this.voicePresetData.times);
          uni.showToast({
            title: '已为您预设时间，请确认',
            icon: 'none',
            duration: 2000
          });
        }
      }
    },
    closeTimePopup() {
      this.$refs.timePopup.close()

    },
    selectDate(index) {
      console.log('🏠 [日期选择] 选择日期索引:', index, '日期信息:', this.availableDates[index]);
      this.selectedDate = index;
      
      // 清空已选择的时间（因为换了日期，时间需要重新选择）
      this.selectedTimes = [];
      console.log('🏠 [日期选择] 已清空选中的时间段，等待重新选择');
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
      console.log('🏠 [预约确认] 开始确认预约', this.selectedTime);

      if (this.selectedTimes === null || this.selectedTimes.length === 0) {
        uni.showToast({
          title: '请选择预约时间',
          icon: 'none'
        })
        return
      }
      
      // **修复：确保 times 数组和 timeSlots 数组正确初始化**
      if (!this.orderCreateForm.times || this.orderCreateForm.times.length === 0) {
        this.orderCreateForm.times = [{
          date: '',
          timeSlots: []
        }];
      }
      
      // 确保 timeSlots 数组存在
      if (!this.orderCreateForm.times[0].timeSlots) {
        this.orderCreateForm.times[0].timeSlots = [];
      }
      
      // 清空之前的时间数据
      this.orderCreateForm.times[0].timeSlots = [];
      this.orderCreateForm.times[0].date = this.selectedTimes[0].date;
      
      const timp = this.selectedTimes
      timp.forEach(item => {
        if (this.orderCreateForm.times[0].timeSlots) {  // 额外安全检查
          this.orderCreateForm.times[0].timeSlots.push(item.range);
        }
      })
      
      // 准备传递的参数
      this.orderCreateForm.coachNo = this.coachNo
      this.orderCreateForm.courseCode = this.courseCode
      this.orderCreateForm.coachFee = this.coachFee
      this.orderCreateForm.baseFee = this.basePrice
      this.orderCreateForm.totalAmount = (this.basePrice + this.coachFee) * this.selectedTimes.length
      
      // 添加教练名称和课程名称到订单数据 - 🔧 优先使用已设置的教练姓名
      const coachInfo = this.coachList.find(c => c.coachNo === this.coachNo) || {};
      const selectedCourse = this.courseTypes[this.selectedCourseType];
      // 使用已解析的教练姓名，如果没有则查找数据库信息
      this.orderCreateForm.coachName = this.orderCreateForm.coachName || 
                                       coachInfo.actualName || 
                                       coachInfo.userName || 
                                       `教练${this.coachNo}`;
      this.orderCreateForm.courseName = selectedCourse?.courseName || '课程';
      
      // 🔧 确保使用正确的俱乐部ID
      this.orderCreateForm.clubCode = 'DEMO_CLUB_001'; // 使用正确的俱乐部编码

      console.log('🏠 [预约确认] 预约参数:', this.orderCreateForm);
      
      // 构建确认信息显示 - 🔧 使用正确的教练姓名
      const timeInfo = this.selectedTimes.map(t => `${t.date} ${t.range}`).join('、');
      
      const confirmMessage = `确认预约信息：\n教练：${this.orderCreateForm.coachName}\n课程：${this.orderCreateForm.courseName}\n时间：${timeInfo}\n费用：¥${this.orderCreateForm.totalAmount}`;
      
      uni.showModal({
        title: '确认预约',
        content: confirmMessage,
        confirmText: '确认预约',
        cancelText: '再看看',
        success: (res) => {
          if (res.confirm) {
            // 跳转到订单详情页
            uni.navigateTo({ 
              url: `/pages/order-detail/order-detail?data=${encodeURIComponent(JSON.stringify(this.orderCreateForm))}` 
            });
            
            uni.showToast({
              title: '预约成功',
              icon: 'success'
            });
            
            this.closeTimePopup();
            
            // 重置语音引导状态
            if (this.voiceGuidanceActive) {
              this.resetVoiceGuidance();
            }
          }
        }
      });
    },
    
    // =================================== 语音约课相关方法 ===================================
    
    // 处理语音约课引导
    handleVoiceBookingGuidance(guidance) {
      console.log('🎯 [语音引导] 处理语音约课引导:', guidance);
      
      this.voiceGuidanceActive = true;
      this.voiceFlowType = guidance.flowType;
      
      // 应用预设数据
      if (guidance.preset) {
        console.log('🎯 [语音引导] 应用预设数据:', guidance.preset);
        Object.assign(this.orderCreateForm, guidance.preset);
        this.voicePresetData = guidance.preset;
        
        // 如果有预设的教练，查找对应教练信息
        if (guidance.preset.coachNo) {
          this.resolveCoachInfo(guidance.preset.coachNo);
        }
        
        // 如果有预设的课程，自动选中
        if (guidance.preset.courseCode) {
          const courseIndex = this.courseTypes.findIndex(course => 
            course.courseCode === guidance.preset.courseCode
          );
          if (courseIndex !== -1) {
            this.selectedCourseType = courseIndex;
            this.basePrice = this.courseTypes[courseIndex].basePrice;
            this.courseCode = this.courseTypes[courseIndex].courseCode;
            console.log('🎯 [语音引导] 自动选中课程:', this.courseTypes[courseIndex]);
          }
        }
        
        // 如果有预设的时间，转换格式
        if (guidance.preset.times) {
          this.convertVoiceTimesToSelectedTimes(guidance.preset.times);
        }
      }
      
      // 根据流程类型执行后续操作
      this.executeVoiceFlow(guidance);
    },
    
    // 解析教练信息
    resolveCoachInfo(coachNo, coachNameFromAI = null) {
      console.log('🎯 [语音引导] 解析教练信息:', coachNo, '来自AI的姓名:', coachNameFromAI);
      
      // 现在AI返回的是C002、C003等格式，直接使用
      let actualCoachNo = coachNo;
      
      // 如果是旧格式COACH_，转换为新格式
      if (coachNo && coachNo.startsWith('COACH_')) {
        const number = coachNo.replace('COACH_', '').padStart(3, '0');
        actualCoachNo = `C${number}`;
      }
      
      console.log('🎯 [语音引导] 实际教练编号:', actualCoachNo);
      
      // 在教练列表中查找对应教练
      const coach = this.coachList.find(c => 
        c.coachNo === actualCoachNo || 
        c.coachNo === coachNo
      );
      
      if (coach) {
        console.log('🎯 [语音引导] ✅ 找到教练信息:', coach);
        this.coachNo = coach.coachNo;
        this.coachFee = coach.coachFee || 100; // 默认费用
        this.orderCreateForm.coachNo = this.coachNo;
        this.orderCreateForm.coachFee = this.coachFee;
        // 🔧 优先使用AI识别的真实姓名，否则使用数据库中的姓名
        this.orderCreateForm.coachName = coachNameFromAI || coach.actualName || coach.userName || `教练${actualCoachNo}`;
      } else {
        console.warn('🎯 [语音引导] ⚠️ 未找到教练信息，使用默认值');
        // 使用默认值
        this.coachNo = actualCoachNo;
        this.coachFee = 100; // 默认教练费
        this.orderCreateForm.coachNo = this.coachNo;
        this.orderCreateForm.coachFee = this.coachFee;
        // 🔧 优先使用AI识别的真实姓名
        this.orderCreateForm.coachName = coachNameFromAI || `教练${actualCoachNo}`;
      }
      
      console.log('🎯 [语音引导] 教练信息设置完成:', {
        coachNo: this.coachNo,
        coachFee: this.coachFee,
        coachName: this.orderCreateForm.coachName
      });
    },
    
    // 转换语音时间格式为选中时间格式
    convertVoiceTimesToSelectedTimes(voiceTimes) {
      console.log('🎯 [语音引导] 转换时间格式:', voiceTimes);
      
      if (!voiceTimes || !Array.isArray(voiceTimes)) {
        return;
      }
      
      this.selectedTimes = voiceTimes.map(timeSlot => ({
        date: timeSlot.date,
        range: timeSlot.range,
        day: timeSlot.day,
        id: timeSlot.id
      }));
      
      console.log('🎯 [语音引导] 时间转换完成:', this.selectedTimes);
    },
    
    // 执行语音流程
    executeVoiceFlow(guidance) {
      console.log('🎯 [语音引导] 执行语音流程:', guidance.flowType);
      
      // 延迟执行，确保页面渲染完成
      this.$nextTick(() => {
        switch (guidance.flowType) {
          case 'complete':
          case 'coach-course':
          case 'coach-time': 
          case 'coach-only':
            this.highlightCoachSelection();
            break;
          case 'course-time':
          case 'course-only':
            // 这种情况会通过事件触发课程弹窗
            break;
          case 'time-only':
            // 这种情况会通过事件触发时间弹窗
            break;
        }
      });
    },
    
    // 高亮教练选择区域
    highlightCoachSelection() {
      console.log('🎯 [语音引导] 高亮教练选择区域');
      
      // 滚动到教练列表区域
      if (this.coachList && this.coachList.length > 0) {
        uni.pageScrollTo({
          selector: '.coach-list',
          duration: 300
        });
      }
      
      // 显示引导提示
      uni.showToast({
        title: '🎯 请选择教练开始约课',
        icon: 'none',
        duration: 3000
      });
      
      // 如果有预设的教练信息，额外提示
      if (this.voicePresetData && (this.voicePresetData.courseCode || this.voicePresetData.times)) {
        setTimeout(() => {
          let presetInfo = [];
          if (this.voicePresetData.courseCode) {
            presetInfo.push('课程类型');
          }
          if (this.voicePresetData.times) {
            presetInfo.push('上课时间');
          }
          
          if (presetInfo.length > 0) {
            uni.showToast({
              title: `已预设: ${presetInfo.join('、')}`,
              icon: 'none',
              duration: 2000
            });
          }
        }, 1500);
      }
    },
    
    // 处理语音触发的课程弹窗
    handleVoiceCoursePopup(options) {
      console.log('🎯 [语音引导] 处理课程弹窗:', options);
      
      this.voiceGuidanceActive = true;
      this.voiceFlowType = options.flowType;
      
      // 预设教练信息 - 🔧 传递AI识别的教练姓名
      if (options.coachNo) {
        this.resolveCoachInfo(options.coachNo, options.coachName); // 传递AI识别的教练姓名
      }
      
      // 预设时间信息
      if (options.times) {
        this.orderCreateForm.times = options.times;
        this.voicePresetData.times = options.times;
        this.convertVoiceTimesToSelectedTimes(options.times);
      }
      
      // 🔧 关键修复：检查是否已有时间参数，如果有则设置标记
      const hasTimeParam = options.times && options.times.length > 0;
      if (hasTimeParam) {
        console.log('✅ [流程检测] 检测到已有时间参数，将跳过时间选择');
      }
      
      // 延迟显示课程弹窗，确保页面已加载
      setTimeout(() => {
        this.showCoursePopupForVoice();
      }, 500);
    },
    
    // 处理语音触发的时间弹窗
    handleVoiceTimePopup(options) {
      console.log('🎯 [语音引导] 处理时间弹窗:', options);
      
      this.voiceGuidanceActive = true;
      this.voiceFlowType = options.flowType;
      
      // 预设教练信息
      if (options.coachNo) {
        this.resolveCoachInfo(options.coachNo); // 使用统一的教练信息解析方法
      }
      
      // 预设课程信息
      if (options.courseCode) {
        this.courseCode = options.courseCode;
        this.orderCreateForm.courseCode = options.courseCode;
        
        // 找到对应课程并设置价格
        const course = this.courseTypes.find(c => c.courseCode === options.courseCode);
        if (course) {
          this.basePrice = course.basePrice;
          this.orderCreateForm.baseFee = course.basePrice;
          console.log('🎯 [语音引导] 找到课程信息:', course);
        }
      }
      
      // 延迟显示时间弹窗，确保页面已加载
      setTimeout(() => {
        this.showTimePopup();
        console.log('🎯 [语音引导] 时间弹窗已显示');
      }, 300);
    },
    
    // 处理语音触发的时间弹窗
    handleVoiceTimePopup(options) {
      console.log('🎯 [语音引导] 处理时间弹窗:', options);
      
      this.voiceGuidanceActive = true;
      this.voiceFlowType = options.flowType;
      
      // 预设教练和课程信息
      if (options.coachNo) {
        this.coachNo = options.coachNo;
        this.orderCreateForm.coachNo = options.coachNo;
      }
      
      if (options.courseCode) {
        this.courseCode = options.courseCode;
        this.orderCreateForm.courseCode = options.courseCode;
        
        // 找到对应课程并设置价格
        const course = this.courseTypes.find(c => c.courseCode === options.courseCode);
        if (course) {
          this.basePrice = course.basePrice;
          this.selectedCourseType = this.courseTypes.indexOf(course);
        }
      }
      
      // 延迟显示时间弹窗，确保页面已加载
      setTimeout(() => {
        this.showTimePopupForVoice();
      }, 500);
    },
    
    // 为语音流程显示课程弹窗
    showCoursePopupForVoice() {
      console.log('🎯 [语音引导] 显示课程选择弹窗');
      this.$refs.coursePopup.open();
    },
    
    // 为语音流程显示时间弹窗
    showTimePopupForVoice() {
      console.log('🎯 [语音引导] 显示时间选择弹窗');
      this.$refs.timePopup.open();
    },
    
    // =================================== 原有方法增强 ===================================
    
    // 语音流程：自动选择课程并显示时间选择
    autoSelectCourseAndShowTime() {
      console.log('🎯 [语音引导] 执行自动选择课程并显示时间选择');
      
      if (this.selectedCourseType !== null) {
        const selectedCourse = this.courseTypes[this.selectedCourseType];
        console.log('🎯 [语音引导] 自动选择的课程:', selectedCourse);
        
        // 关闭课程弹窗，显示时间弹窗
        this.closeCoursePopup();
        
        setTimeout(() => {
          this.showTimePopup();
        }, 200);
      }
    },
    
    // 语音流程：自动完成预订
    autoCompleteBooking() {
      console.log('🎯 [语音引导] 执行自动完成预订');
      
      // 关闭当前弹窗
      this.closeCoursePopup();
      
      // 应用预设的时间数据
      if (this.voicePresetData.times) {
        this.selectedTimes = Array.isArray(this.voicePresetData.times) 
          ? this.voicePresetData.times 
          : [this.voicePresetData.times];
        
        console.log('🎯 [语音引导] 应用预设时间:', this.selectedTimes);
      }
      
      // 准备订单数据
      setTimeout(() => {
        this.prepareVoiceOrder();
      }, 300);
    },
    
    // 准备语音订单
    prepareVoiceOrder() {
      console.log('🎯 [语音引导] 准备语音订单');
      
      // 确保所有必要数据都已设置
      if (!this.coachNo || !this.courseCode || this.selectedTimes.length === 0) {
        console.warn('🎯 [语音引导] 订单数据不完整:', {
          coachNo: this.coachNo,
          courseCode: this.courseCode,
          selectedTimes: this.selectedTimes
        });
        
        uni.showModal({
          title: '信息不完整',
          content: '预订信息不完整，请手动完成选择',
          showCancel: false,
          confirmText: '重新选择'
        });
        return;
      }
      
      // 构建订单数据
      this.orderCreateForm.times[0].date = this.selectedTimes[0].date || this.availableDates[0].date;
      this.orderCreateForm.times[0].timeSlots = this.selectedTimes.map(t => t.range || t);
      this.orderCreateForm.coachNo = this.coachNo;
      this.orderCreateForm.courseCode = this.courseCode;
      this.orderCreateForm.coachFee = this.coachFee || 0;
      this.orderCreateForm.baseFee = this.basePrice || 0;
      this.orderCreateForm.totalAmount = (this.orderCreateForm.baseFee + this.orderCreateForm.coachFee) * this.selectedTimes.length;
      
      console.log('🎯 [语音引导] 完整订单数据:', this.orderCreateForm);
      
      // 构建详细的确认信息
      const coachInfo = this.coachList.find(c => c.coachNo === this.coachNo) || {};
      const selectedCourse = this.courseTypes.find(c => c.courseCode === this.courseCode) || {};
      const timeInfo = this.selectedTimes.map(t => `${t.date} ${t.range}`).join('、');
      
      const confirmContent = `AI为您智能匹配的约课信息：\n\n👨‍🏫 教练：${coachInfo.actualName || '专业教练'}\n📚 课程：${selectedCourse.courseName || '马术课程'}\n⏰ 时间：${timeInfo}\n💰 费用：¥${this.orderCreateForm.totalAmount}\n\n是否确认预约？`;
      
      // 跳转到订单确认页面
      uni.showModal({
        title: '🎯 AI约课助手',
        content: confirmContent,
        confirmText: '确认预约',
        cancelText: '修改信息',
        success: (res) => {
          if (res.confirm) {
            this.navigateToOrderDetail();
          } else {
            // 用户选择修改，重置到首页重新选择
            this.resetVoiceGuidance();
            uni.showToast({
              title: '可以重新进行语音约课',
              icon: 'none'
            });
          }
        }
      });
    },
    
    // 跳转到订单详情页
    navigateToOrderDetail() {
      console.log('🎯 [语音引导] 跳转到订单确认页');
      
      // 显示跳转提示
      uni.showLoading({
        title: '正在跳转...',
        mask: true
      });
      
      setTimeout(() => {
        uni.hideLoading();
        
        uni.navigateTo({ 
          url: `/pages/order-detail/order-detail?data=${encodeURIComponent(JSON.stringify(this.orderCreateForm))}` 
        });
        
        // 显示成功提示
        setTimeout(() => {
          uni.showToast({
            title: '🎯 AI约课完成！',
            icon: 'success',
            duration: 2000
          });
        }, 500);
        
        // 重置语音引导状态
        this.resetVoiceGuidance();
      }, 800);
    },
    
    // 重置语音引导状态
    resetVoiceGuidance() {
      console.log('🎯 [语音引导] 重置语音引导状态');
      
      this.voiceGuidanceActive = false;
      this.voiceFlowType = '';
      this.voicePresetData = {};
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