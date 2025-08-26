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
          <text class="info-value">{{ getDateInfo(form.times[0].date).fullDate }} {{
            getDateInfo(form.times[0].date).weekday }}</text>
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
          <view v-for="(item, index) in form.times[0].timeSlots" key="index">
            <text class="info-value">{{ item }}</text>
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
        <text class="total-amount">
          <text class="total-label">({{ form.coachFee }} + {{ form.baseFee }}) * {{ form.times[0].timeSlots.length
          }}=</text>
          <text style="font-size: 24rpx;">¥</text>
          {{ form.totalAmount }}</text>
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
export default {
  data() {
    return {
      form: {},
      courseType: '',
      coursePrice: 0,
      selectedTimes: [],
      coach: {},
      rider: {}
    }
  },
  onLoad(options) {
    if (options.data) {
      try {
        const bookingData = JSON.parse(decodeURIComponent(options.data));
        console.log('预约数据:', bookingData);
        this.getCoachInfo(bookingData.coachNo)
        // 使用这些数据填充订单详情页
        this.form = bookingData
      } catch (e) {
        console.error('解析预约数据失败:', e);
        uni.showToast({
          title: '数据解析失败',
          icon: 'none'
        });
      }
    }
  },
  methods: {
    // 获取教练详情
    async getCoachInfo(id) {
      try {
        const res = await getByCoach({
          coachNo: id
        });
        if (res.code === 0) {
          this.coach = res.data;
        }
      } catch (e) {
        console.error('获取教练详情失败:', e);
        uni.showToast({
          title: '获取教练详情失败',
          icon: 'none'
        });
      }
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
</style>