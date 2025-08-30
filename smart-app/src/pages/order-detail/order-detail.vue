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
        <image class="coach-avatar" :src="coachInfo.avatar || 'https://q1.itc.cn/q_70/images03/20250802/e70c0520fd51496586d2ff380531c368.jpeg'"
          mode="aspectFill" />
        <text class="coach-name">{{ getDisplayCoachName() }}</text>
        <image class="coach-sex" src="/static/images/index/sex.png" />
      </view>
    </view>

    <!-- 服务日期部分 -->
    <view class="info-card">
      <view class="info-item">
        <text class="info-label">服务信息</text>
      </view>
      
      <!-- 显示选中的家庭成员 -->
      <view class="info-item" v-if="form.selectedMemberName">
        <text class="info-label">服务对象</text>
        <text class="info-value">{{ form.selectedMemberName }}</text>
        <text v-if="form.isGuardianPurchase" class="guardian-tag">监护人</text>
      </view>
      
      <view class="info-item">
        <text class="info-label">场地障碍和救援</text>
        <text class="info-value1">
          <text style="font-size: 24rpx;">¥</text>
          {{ form.baseFee }}</text>
      </view>
      <view class="info-item1">
        <text class="info-label">教练费</text>
        <text class="info-value1">
          <text style="font-size: 24rpx;">¥</text>
          {{ form.coachFee }}</text>
      </view>
      <view class="info-item">
        <view class="info-label">服务日期</view>
        <view>
          <text class="info-value" v-if="form.times && form.times.length > 0 && form.times[0].date">
            {{ getDateInfo(form.times[0].date).fullDate }} {{ getDateInfo(form.times[0].date).weekday }}
          </text>
          <text class="info-value" v-else>请选择服务日期</text>
          <uni-icons type="right" color="#333" size="13" />
        </view>
      </view>
      <view class="info-item">
        <text class="info-label">服务地点</text>
        <view>
          <text class="info-value">{{ getClubName() }}</text>
          <uni-icons type="right" color="#333" size="13" />
        </view>
      </view>
      <view class="info-item">
        <text class="info-label">服务时间</text>
        <view>
          <template v-if="form.times && form.times.length > 0 && form.times[0].timeSlots">
            <view v-for="(item, index) in form.times[0].timeSlots" :key="index">
              <text class="info-value">{{ item }}</text>
              <uni-icons type="right" color="#333" size="13" />
            </view>
          </template>
          <view v-else>
            <text class="info-value">请选择服务时间</text>
            <uni-icons type="right" color="#333" size="13" />
          </view>
        </view>
      </view>
    </view>

    <!-- 分隔线 -->
    <!-- <view class="divider" /> -->

    <!-- 总计部分 -->
    <view class="total-card">
      <view style="display: flex; justify-content: space-between; align-items: center;">
        <text class="total-label">总计 </text>
        <text class="total-amount" v-if="form.times && form.times.length > 0 && form.times[0].timeSlots">
          <text class="total-label">({{ form.coachFee }} + {{ form.baseFee }}) * {{ form.times[0].timeSlots.length }}=</text>
          <text style="font-size: 24rpx;">¥</text>
          {{ form.totalAmount }}
        </text>
        <text class="total-amount" v-else>
          <text style="font-size: 24rpx;">¥</text>
          {{ form.totalAmount || 0 }}
        </text>
      </view>
      <view class="divider" />
      <view style="float: right;">
        <text class="payment-title">实付:<text class="total-amount1"><text class="total-amount2">¥</text>{{
          form.totalAmount }}</text></text>
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
        <text style="font-size: 24rpx;color: #24262b;">实付:</text>
        <text class="amount1"><text class="amount">¥</text>{{ form.totalAmount }}</text>
      </view>
      <view>
        <button class="submit-btn" @click="submitOrder">立即支付</button>
      </view>
    </view>
  </view>
</template>

<script>
import { getByCoach, addOrder } from '@/api/home/index';
import { createWechatPayment, queryPaymentStatus } from '@/api/payment/wechat-pay.js';
export default {
  data() {
    return {
      form: {},
      coachInfo: {},
      courseType: '',
      coursePrice: 0,
      selectedTimes: [],
      coach: {},
      rider: {}
    }
  },
  onError(error) {
    console.error('🔴 [订单详情页] 页面渲染错误:', error);
    wx.showModal({
      title: '页面加载失败',
      content: '订单详情加载出现问题，请返回重试',
      confirmText: '返回首页',
      cancelText: '重试',
      success: (res) => {
        if (res.confirm) {
          wx.switchTab({ url: '/pages/home/index' });
        } else {
          // 重新加载页面
          setTimeout(() => {
            wx.reLaunch({
              url: getCurrentPages()[getCurrentPages().length - 1].route
            });
          }, 100);
        }
      }
    });
  },
  onLoad(options) {
    if (options.data) {
      try {
        const bookingData = JSON.parse(decodeURIComponent(options.data));
        console.log('🎯 [订单详情] 预约数据:', bookingData);
        
        // 处理教练编号映射
        const processedCoachNo = this.processCoachNumber(bookingData.coachNo);
        console.log('🎯 [订单详情] 处理后的教练编号:', processedCoachNo);
        
        this.getCoachInfo(processedCoachNo);
        
        // 使用这些数据填充订单详情页，同时更新教练编号
        this.form = {
          ...bookingData,
          coachNo: processedCoachNo
        };
        
        // 验证和修复费用数据
        this.validateAndFixOrderData();
      } catch (e) {
        console.error('解析预约数据失败:', e);
        wx.showModal({
          title: '数据解析失败',
          content: '订单数据格式异常，是否返回重新约课？',
          confirmText: '重新约课',
          cancelText: '返回首页',
          success: (res) => {
            if (res.confirm) {
              wx.switchTab({ url: '/pages/home/index' });
            } else {
              wx.switchTab({ url: '/pages/home/index' });
            }
          }
        });
      }
    }
  },
  methods: {
    // 验证和修复订单数据
    validateAndFixOrderData() {
      console.log('🎯 [订单验证] 验证订单数据:', this.form);
      
      // 修复费用数据
      if (!this.form.coachFee || this.form.coachFee === 0) {
        console.warn('🎯 [订单验证] 教练费用为空或0，设置默认值');
        this.form.coachFee = 100; // 默认教练费用
      }
      
      if (!this.form.baseFee || this.form.baseFee === 0) {
        console.warn('🎯 [订单验证] 课程费用为空或0，设置默认值');
        this.form.baseFee = 200; // 默认课程费用
      }
      
      // 重新计算总费用
      if (!this.form.totalAmount || this.form.totalAmount === 0) {
        const timeSlotsCount = this.form.times?.[0]?.timeSlots?.length || 1;
        this.form.totalAmount = (this.form.coachFee + this.form.baseFee) * timeSlotsCount;
        console.log('🎯 [订单验证] 重新计算总费用:', this.form.totalAmount);
      }
      
      // 确保教练姓名存在
      if (!this.form.coachName) {
        console.warn('🎯 [订单验证] 教练姓名为空，需要从API获取');
        // getCoachInfo已经在上面调用了，会异步获取教练信息
      }
      
      console.log('🎯 [订单验证] 验证后的订单数据:', this.form);
    },
    // 处理教练编号映射
    processCoachNumber(coachNo) {
      if (!coachNo) return '';
      
      // AI返回的是标准的教练编号（如C002、C003等），直接使用
      // 如果是旧格式COACH_001，需要转换
      if (coachNo.startsWith('COACH_')) {
        // 移除COACH_前缀，转换为C+数字格式
        const number = coachNo.replace('COACH_', '').padStart(3, '0');
        return `C${number}`;
      }
      
      // 如果已经是C开头的格式，直接返回
      if (coachNo.startsWith('C')) {
        return coachNo;
      }
      
      // 如果是纯数字，转换为C+数字格式
      if (!isNaN(coachNo)) {
        const number = coachNo.toString().padStart(3, '0');
        return `C${number}`;
      }
      
      return coachNo;
    },
    
    // 获取教练详情
    async getCoachInfo(id) {
      try {
        const res = await getByCoach({
          coachNo: id
        });
        if (res.code === 0) {
          this.coachInfo = res.data;
        }
      } catch (e) {
        console.error('获取教练详情失败:', e);
        wx.showModal({
          title: '获取教练详情失败',
          content: '网络连接异常，是否重试获取教练信息？',
          confirmText: '重试',
          cancelText: '继续',
          success: (res) => {
            if (res.confirm) {
              this.getCoachInfo(id);
            }
          }
        });
      }
    },
    
    // 获取显示的教练姓名
    getDisplayCoachName() {
      // 优先使用从订单数据传来的教练姓名
      if (this.form && this.form.coachName) {
        return this.form.coachName;
      }
      
      // 其次使用从API获取的教练信息（注意字段名是actualName）
      if (this.coachInfo && this.coachInfo.actualName) {
        return this.coachInfo.actualName;
      }
      
      // 兼容性处理，检查其他可能的字段名
      if (this.coachInfo && this.coachInfo.coachName) {
        return this.coachInfo.coachName;
      }
      
      // 最后使用默认名称
      return '教练';
    },
    
    // 获取完整日期信息的方法（传入日期，返回包含年月日和星期的信息）
    getDateInfo(inputDate = null) {
      let dateObj;

      // 如果没有传入日期，使用当前日期
      if (!inputDate) {
        dateObj = new Date();
        // 清除时间部分，只保留日期
        dateObj.setHours(0, 0, 0, 0);
      } else {
        // 处理不同格式的日期输入
        if (typeof inputDate === 'string') {
          // 字符串格式的日期
          if (inputDate.includes('-')) {
            // 格式如 "2023-06-15" 或 "06-15"
            const parts = inputDate.split('-');
            if (parts.length === 3) {
              // 包含年份 "2023-06-15"
              const year = parseInt(parts[0]);
              const month = parseInt(parts[1]) - 1; // 月份从0开始
              const day = parseInt(parts[2]);
              dateObj = new Date(year, month, day);
            } else if (parts.length === 2) {
              // 不包含年份 "06-15"，使用当前年份
              const currentYear = new Date().getFullYear();
              const month = parseInt(parts[0]) - 1; // 月份从0开始
              const day = parseInt(parts[1]);
              dateObj = new Date(currentYear, month, day);
            }
          } else if (inputDate.includes('/')) {
            // 格式如 "2023/06/15" 或 "06/15"
            const parts = inputDate.split('/');
            if (parts.length === 3) {
              // 包含年份 "2023/06/15"
              const year = parseInt(parts[0]);
              const month = parseInt(parts[1]) - 1; // 月份从0开始
              const day = parseInt(parts[2]);
              dateObj = new Date(year, month, day);
            } else if (parts.length === 2) {
              // 不包含年份 "06/15"，使用当前年份
              const currentYear = new Date().getFullYear();
              const month = parseInt(parts[0]) - 1; // 月份从0开始
              const day = parseInt(parts[1]);
              dateObj = new Date(currentYear, month, day);
            }
          } else if (inputDate.length === 8) {
            // 格式如 "20230615"（包含年份）
            const year = parseInt(inputDate.substring(0, 4));
            const month = parseInt(inputDate.substring(4, 6)) - 1; // 月份从0开始
            const day = parseInt(inputDate.substring(6, 8));
            dateObj = new Date(year, month, day);
          } else if (inputDate.length === 4) {
            // 格式如 "0615"（不包含年份），使用当前年份
            const currentYear = new Date().getFullYear();
            const month = parseInt(inputDate.substring(0, 2)) - 1; // 月份从0开始
            const day = parseInt(inputDate.substring(2, 4));
            dateObj = new Date(currentYear, month, day);
          } else {
            // 尝试直接解析日期字符串
            dateObj = new Date(inputDate);
          }
        } else if (typeof inputDate === 'number') {
          // 时间戳格式
          dateObj = new Date(inputDate);
        } else if (inputDate instanceof Date) {
          // 已经是Date对象
          dateObj = new Date(inputDate.getTime());
        } else {
          // 无效格式
          return {
            error: '无效的日期格式',
            success: false
          };
        }
      }

      // 检查日期是否有效
      if (isNaN(dateObj.getTime())) {
        return {
          error: '无效的日期',
          success: false
        };
      }

      // 获取年月日
      const year = dateObj.getFullYear();
      const month = dateObj.getMonth() + 1; // 月份从0开始，所以加1
      const day = dateObj.getDate();

      // 获取星期几
      const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
      const weekday = weekdays[dateObj.getDay()];

      // 格式化月份和日期，确保两位数
      const formattedMonth = month < 10 ? `0${month}` : month;
      const formattedDay = day < 10 ? `0${day}` : day;

      // 返回完整的日期信息
      return {
        year: year,
        month: month,
        day: day,
        weekday: weekday,
        fullDate: `${year}-${formattedMonth}-${formattedDay}`,
        chineseDate: `${year}年${formattedMonth}月${formattedDay}日`,
        shortDate: `${formattedMonth}-${formattedDay}`,
        dateObject: dateObj,
        success: true
      };
    },
    
    // 获取俱乐部名称
    getClubName() {
      // 根据clubCode获取俱乐部名称
      const clubMap = {
        'DEMO_CLUB_001': 'On The Saddle马术俱乐部',
        'CLUB_002': '上海XX马术俱乐部',
        'CLUB_003': '北京XX马术俱乐部'
      };
      
      const clubCode = this.form?.clubCode || 'DEMO_CLUB_001';
      return clubMap[clubCode] || '马术俱乐部';
    },
    
    goBack() {
      uni.navigateBack({
        delta: 1
      });
    },
    async submitOrder() {
      console.log('🎯 [订单提交] 开始提交订单', this.form);
      
      // 1. 数据验证
      if (!this.validateOrderData()) {
        return;
      }
      
      // 2. 显示加载中
      uni.showLoading({
        title: '创建订单中...',
        mask: true
      });
      
      try {
        // 3. 调用后端创建订单API
        const orderData = this.buildOrderData();
        console.log('🎯 [订单提交] 订单数据:', orderData);
        
        const res = await addOrder(orderData);
        console.log('🎯 [订单提交] 后端响应:', res);
        
        if (res.code === 0) {
          // 4. 订单创建成功，直接发起微信支付
          const orderId = res.data.orderId;  // 现在后端已经返回orderId字段
          const orderNo = res.data.orderNo;
          console.log('🎯 [订单提交] ✅ 订单创建成功，订单ID:', orderId, '订单号:', orderNo);
          
          // 验证必要字段是否存在
          if (!orderId || !orderNo) {
            uni.hideLoading();
            uni.showModal({
              title: '订单创建异常',
              content: '订单数据不完整，请重试',
              showCancel: false,
              confirmText: '确定'
            });
            return;
          }
          
          // 更新加载提示
          uni.showLoading({
            title: '发起支付中...',
            mask: true
          });
          
          // 直接调用微信支付
          await this.initiateWechatPayment({
            orderId: orderId,
            orderNo: orderNo,
            amount: this.form.totalAmount,
            description: `马术课程预约-${this.getDisplayCoachName()}`
          });
          
        } else {
          // 5. 订单创建失败
          uni.hideLoading();
          console.error('🎯 [订单提交] ❌ 订单创建失败:', res.message);
          uni.showModal({
            title: '订单创建失败',
            content: res.message || '创建订单时发生错误，请重试',
            showCancel: false,
            confirmText: '重试'
          });
        }
        
      } catch (error) {
        // 6. 网络错误或其他异常
        uni.hideLoading();
        console.error('🎯 [订单提交] ❌ 订单提交异常:', error);
        uni.showModal({
          title: '网络错误',
          content: '网络连接失败，请检查网络后重试',
          showCancel: true,
          cancelText: '返回',
          confirmText: '重试',
          success: (res) => {
            if (res.confirm) {
              this.submitOrder(); // 重试
            }
          }
        });
      }
    },
    
    // 验证订单数据
    validateOrderData() {
      if (!this.form) {
        uni.showToast({
          title: '订单数据缺失',
          icon: 'error'
        });
        return false;
      }
      
      if (!this.form.coachNo) {
        uni.showToast({
          title: '请选择教练',
          icon: 'error'
        });
        return false;
      }
      
      if (!this.form.courseCode) {
        uni.showToast({
          title: '请选择课程类型',
          icon: 'error'
        });
        return false;
      }
      
      if (!this.form.times || this.form.times.length === 0) {
        uni.showToast({
          title: '请选择上课时间',
          icon: 'error'
        });
        return false;
      }
      
      return true;
    },
    
    // 构建订单数据
    buildOrderData() {
      // 确保times数据结构正确
      const bookingTimes = this.form.times && this.form.times.length > 0 ? 
        this.form.times.map(timeItem => ({
          date: timeItem.date,
          timeSlots: timeItem.timeSlots || []
        })) : [];
      
      console.log('🎯 [订单构建] 构建times数据:', bookingTimes);
      
      // 获取当前登录会员ID
      const currentMemberId = uni.getStorageSync('memberId');
      if (!currentMemberId) {
        throw new Error('用户未登录，无法创建订单');
      }
      
      // 根据订单来源设置source值
      let orderSource = 1; // 默认手动下单
      let remarks = '手动下单订单';
      
      // 判断是否为语音约课（通过form中的source字段或其他标识）
      if (this.form.source === 2 || this.form.remarks === '语音约课订单') {
        orderSource = 2;
        remarks = '语音约课订单';
      }
      
      return {
        clubCode: this.form.clubCode,
        memberId: currentMemberId,
        coachNo: this.form.coachNo,
        courseCode: this.form.courseCode,
        courseName: this.form.courseName,
        times: bookingTimes,
        totalAmount: this.form.totalAmount,
        coachFee: this.form.coachFee,
        baseFee: this.form.baseFee,
        
        // 家庭成员信息
        selectedMemberId: this.form.selectedMemberId,
        selectedMemberNo: this.form.selectedMemberNo,
        selectedMemberName: this.form.selectedMemberName,
        selectedMemberPhone: this.form.selectedMemberPhone,
        isGuardianPurchase: this.form.isGuardianPurchase,
        
        remarks: remarks,
        source: orderSource
      };
    },
    
    // 跳转到支付页面
    navigateToPayment(orderId) {
      console.log('🎯 [支付跳转] 跳转到支付页面，订单ID:', orderId);
      
      // 检查是否存在支付页面
      const paymentPages = [
        '/pages/payment/payment',
        '/pages/pay/pay', 
        '/pages/order/pay',
        '/pages/payment/index'
      ];
      
      // 构建支付页面参数
      const paymentData = {
        orderId: orderId,
        totalAmount: this.form.totalAmount,
        orderType: 'COURSE_BOOKING',
        from: 'voice_booking'
      };
      
      const paymentUrl = `/pages/payment/payment?${this.buildUrlParams(paymentData)}`;
      
      console.log('🎯 [支付跳转] 支付页面URL:', paymentUrl);
      
      // 尝试跳转到支付页面
      uni.navigateTo({
        url: paymentUrl,
        fail: (error) => {
          console.error('🎯 [支付跳转] ❌ 支付页面跳转失败:', error);
          // 降级方案：显示订单成功，引导用户到订单列表
          uni.showModal({
            title: '订单创建成功',
            content: `订单已创建成功！\n订单号：${orderId}\n请到"我的订单"中完成支付`,
            showCancel: true,
            cancelText: '稍后支付',
            confirmText: '查看订单',
            success: (res) => {
              if (res.confirm) {
                // 跳转到订单列表或我的页面
                uni.switchTab({
                  url: '/pages/mine/mine',
                  fail: () => {
                    // 如果没有订单列表页面，返回首页
                    uni.switchTab({ url: '/pages/home/index' });
                  }
                });
              } else {
                // 返回首页
                uni.switchTab({ url: '/pages/home/index' });
              }
            }
          });
        }
      });
    },
    
    // 构建URL参数
    buildUrlParams(params) {
      return Object.keys(params)
        .map(key => `${key}=${encodeURIComponent(params[key])}`)
        .join('&');
    },
    
    /**
     * 发起微信支付
     */
    async initiateWechatPayment(paymentData) {
      try {
        console.log('🎯 [微信支付] 开始创建支付订单:', paymentData);
        
        // 1. 获取用户openid（从缓存或登录接口获取）
        const openid = await this.getUserOpenid();
        if (!openid) {
          uni.hideLoading();
          uni.showModal({
            title: '支付失败',
            content: '获取用户信息失败，请重新登录',
            showCancel: false,
            confirmText: '确定'
          });
          return;
        }

        // 2. 调用后端创建微信支付订单
        const createPaymentRes = await createWechatPayment({
          orderId: paymentData.orderId,
          orderNo: paymentData.orderNo,
          description: paymentData.description,
          amount: paymentData.amount,
          openid: openid,
          userIp: '127.0.0.1', // 小程序环境下可使用默认值
          expireMinutes: 30
        });

        console.log('🎯 [微信支付] 后端创建支付订单响应:', createPaymentRes);

        if (createPaymentRes.code !== 0) {
          uni.hideLoading();
          uni.showModal({
            title: '支付失败',
            content: createPaymentRes.message || '创建支付订单失败',
            showCancel: false,
            confirmText: '确定'
          });
          return;
        }

        // 3. 调用微信小程序支付接口
        const payParams = createPaymentRes.data;
        console.log('🎯 [微信支付] 调用wx.requestPayment:', payParams);
        
        uni.hideLoading();
        
        // 检查是否为Mock模式
        if (this.isMockMode(payParams)) {
          // Mock模式：模拟支付流程
          this.handleMockPayment(payParams);
        } else {
          // 真实模式：调用微信支付
          uni.requestPayment({
            provider: 'wxpay',
            timeStamp: payParams.timeStamp,
            nonceStr: payParams.nonceStr,
            package: payParams.packageValue,
            signType: payParams.signType || 'RSA',
            paySign: payParams.paySign || '', // 前端不需要自己生成签名
            success: (res) => {
              console.log('🎯 [微信支付] ✅ 支付成功:', res);
              this.handlePaymentSuccess(payParams.paymentNo);
            },
            fail: (err) => {
              console.error('🎯 [微信支付] ❌ 支付失败:', err);
              this.handlePaymentFail(err, payParams.paymentNo);
            }
          });
        }

      } catch (error) {
        console.error('🎯 [微信支付] ❌ 发起支付异常:', error);
        uni.hideLoading();
        uni.showModal({
          title: '支付异常',
          content: '发起支付时出现异常，请重试',
          showCancel: true,
          cancelText: '取消',
          confirmText: '重试',
          success: (res) => {
            if (res.confirm) {
              this.initiateWechatPayment(paymentData);
            }
          }
        });
      }
    },

    /**
     * 获取用户openid
     */
    async getUserOpenid() {
      try {
        // 优先从缓存获取
        let openid = uni.getStorageSync('user_openid');
        if (openid) {
          console.log('🎯 [微信支付] 从缓存获取openid:', openid);
          return openid;
        }

        // 从用户信息中获取
        const userInfo = uni.getStorageSync('userInfo');
        if (userInfo && userInfo.openid) {
          console.log('🎯 [微信支付] 从用户信息获取openid:', userInfo.openid);
          return userInfo.openid;
        }

        // 如果没有openid，提示用户重新登录
        console.error('🎯 [微信支付] 未找到用户openid');
        return null;

      } catch (error) {
        console.error('🎯 [微信支付] 获取openid异常:', error);
        return null;
      }
    },

    /**
     * 处理支付成功
     */
    async handlePaymentSuccess(paymentNo) {
      try {
        console.log('🎯 [微信支付] 支付成功，支付单号:', paymentNo);
        
        uni.showLoading({
          title: '支付完成，请稍候...',
          mask: true
        });

        // 延迟查询支付状态（给回调处理时间）
        setTimeout(async () => {
          try {
            // 查询支付状态确认
            const statusRes = await queryPaymentStatus(paymentNo);
            console.log('🎯 [微信支付] 查询支付状态:', statusRes);

            uni.hideLoading();
            
            uni.showModal({
              title: '支付成功',
              content: '课程预约已完成，您可以在"我的订单"中查看详情',
              showCancel: false,
              confirmText: '查看订单',
              success: (res) => {
                if (res.confirm) {
                  // 跳转到订单列表或我的页面
                  uni.switchTab({
                    url: '/pages/mine/mine',
                    fail: () => {
                      uni.switchTab({ url: '/pages/home/index' });
                    }
                  });
                } else {
                  uni.switchTab({ url: '/pages/home/index' });
                }
              }
            });

          } catch (error) {
            console.error('🎯 [微信支付] 查询支付状态失败:', error);
            uni.hideLoading();
            // 即使查询失败，也认为支付成功
            uni.showToast({
              title: '支付成功',
              icon: 'success',
              duration: 2000
            });
            setTimeout(() => {
              uni.switchTab({ url: '/pages/home/index' });
            }, 2000);
          }
        }, 2000);

      } catch (error) {
        console.error('🎯 [微信支付] 处理支付成功异常:', error);
        uni.hideLoading();
        uni.showToast({
          title: '支付成功',
          icon: 'success'
        });
      }
    },

    /**
     * 判断是否为Mock模式
     */
    isMockMode(payParams) {
      // 根据支付单号前缀判断是否为Mock模式
      return payParams.paymentNo && payParams.paymentNo.startsWith('MOCK_PAY_');
    },

    /**
     * 处理Mock支付
     */
    handleMockPayment(payParams) {
      console.log('🎭 [Mock支付] 进入模拟支付流程');
      
      // 显示Mock支付确认弹窗
      uni.showModal({
        title: '🎭 Mock支付确认',
        content: `这是模拟支付环境\n支付单号: ${payParams.paymentNo}\n支付金额: ¥${this.form.totalAmount}\n\n选择模拟支付结果：`,
        cancelText: '支付失败',
        confirmText: '支付成功',
        success: (res) => {
          if (res.confirm) {
            // 模拟支付成功
            console.log('🎭 [Mock支付] 用户选择支付成功');
            uni.showLoading({
              title: 'Mock支付处理中...',
              mask: true
            });
            
            // 模拟1秒延迟后支付成功
            setTimeout(() => {
              uni.hideLoading();
              this.handlePaymentSuccess(payParams.paymentNo);
            }, 1000);
            
          } else {
            // 模拟支付失败
            console.log('🎭 [Mock支付] 用户选择支付失败');
            this.handlePaymentFail({ errMsg: 'mock_cancel' }, payParams.paymentNo);
          }
        }
      });
    },

    /**
     * 处理支付失败
     */
    handlePaymentFail(error, paymentNo) {
      console.error('🎯 [微信支付] 支付失败:', error, '支付单号:', paymentNo);
      
      // 根据错误类型显示不同提示
      let title = '支付失败';
      let content = '支付过程中出现问题，请重试';
      
      if (error.errMsg) {
        if (error.errMsg.includes('cancel')) {
          title = '支付已取消';
          content = '您已取消支付，可稍后在订单中重新支付';
        } else if (error.errMsg.includes('timeout')) {
          title = '支付超时';
          content = '支付超时，请重新发起支付';
        } else if (error.errMsg.includes('network')) {
          title = '网络异常';
          content = '网络连接异常，请检查网络后重试';
        }
      }
      
      uni.showModal({
        title: title,
        content: content,
        showCancel: true,
        cancelText: '稍后再试',
        confirmText: '重新支付',
        success: (res) => {
          if (res.confirm) {
            // 重新发起支付
            this.initiateWechatPayment({
              orderId: this.form.orderId,
              orderNo: this.form.orderNo,
              amount: this.form.totalAmount,
              description: `马术课程预约-${this.getDisplayCoachName()}`
            });
          } else {
            // 返回首页
            uni.switchTab({ url: '/pages/home/index' });
          }
        }
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
  padding: 15rpx 0 20rpx 0;
  border-bottom: 2rpx dashed #eee;
  margin-bottom: 20rpx;
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

/* 监护人标签样式 */
.guardian-tag {
  background-color: #ff9500;
  color: white;
  font-size: 20rpx;
  padding: 4rpx 8rpx;
  border-radius: 8rpx;
  margin-left: 10rpx;
}
</style>