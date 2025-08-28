<template>
  <view class="container">
    <!-- 顶部标题 -->
    <!-- <view class="page-title">我的订单</view> -->

    <!-- 标签导航 -->
    <view class="tab-container">
      <view v-for="(tab, index) in tabs" :key="index" class="tab-item" :class="{ active: activeTab === index }"
        @click="switchTab(index)">
        <text class="tab-text">{{ tab.name }}</text>
        <view v-if="activeTab === index" class="tab-line" />
      </view>
    </view>

    <!-- 订单列表 -->
    <view class="order-list">
      <!-- 课程预约内容 -->
      <block v-if="activeTab === 0">
        <view class="order-item" v-for="(item, index) in courseOrders" :key="index">
          <view class="order-header">
            <text class="venue">{{ item.venue }}</text>
            <text class="status">{{ item.status }}</text>
          </view>
          <view class="order-content">
            <image class="course-image"
              src="https://t11.baidu.com/it/u=444097960,250398539&fm=30&app=106&f=JPEG?w=640&h=480&s=D9A49B51665277CC129115C40300F0B0"
              mode="aspectFill" />
            <view class="order-details">

              <view style="display: flex;justify-content: space-between; align-items: center;">
                <view class="course-info">
                  <text class="package" v-if="item.package">{{ item.package }}</text>
                  <text class="course-name">{{ item.courseName }}</text>
                  <text class="time">{{ item.time }}</text>
                  <text class="lesson" v-if="item.remaining">{{ item.remaining }}</text>
                  <text class="quantity">{{ item.quantity }}</text>
                </view>
                <view>
                  <text class="price1" v-if="item.price"><text class="price">¥</text>{{ item.price }}</text>
                </view>
              </view>
              <view class="order-footer">
                <view />
                <view class="action-btn" @click="handleAction(item.actionType)">
                  <text>{{ item.actionText }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </block>

      <!-- 待完成内容 -->
      <block v-if="activeTab === 1">
        <view class="order-item" v-for="(item, index) in pendingOrders" :key="index">
          <view class="order-header">
            <text class="venue">{{ item.venue }}</text>
            <text class="status">{{ item.status }}</text>
          </view>
          <view class="order-content">
            <image class="course-image"
              src="https://t11.baidu.com/it/u=444097960,250398539&fm=30&app=106&f=JPEG?w=640&h=480&s=D9A49B51665277CC129115C40300F0B0"
              mode="aspectFill" />
            <view class="order-details">

              <view style="display: flex;justify-content: space-between; align-items: center;">
                <view class="course-info">
                  <text class="course-name" v-if="item.package">{{ item.package }}</text>
                  <text class="package">{{ item.courseName }}</text>
                  <text class="time">{{ item.time }}</text>
                  <text class="lesson" v-if="item.remaining">{{ item.remaining }}</text>
                  <!-- <text class="quantity">{{ item.quantity }}</text> -->
                </view>
                <view>
                  <text class="price1" v-if="item.price"><text class="price">¥</text>{{ item.price }}</text>
                </view>
              </view>
              <!-- <view class="order-footer">
                <view />
                <view class="action-btn" @click="handleAction(item.actionType)">
                  <text>{{ item.actionText }}</text>
                </view>
              </view> -->
            </view>
          </view>
        </view>
      </block>

      <!-- 已完成内容 -->
      <block v-if="activeTab === 2">
        <view class="order-item" v-for="(item, index) in completedOrders" :key="index">
          <view class="order-header">
            <text class="venue">{{ item.venue }}</text>
            <text class="status">{{ item.status }}</text>
          </view>
          <view class="order-content">
            <image class="course-image"
              src="https://t11.baidu.com/it/u=444097960,250398539&fm=30&app=106&f=JPEG?w=640&h=480&s=D9A49B51665277CC129115C40300F0B0"
              mode="aspectFill" />
            <view class="order-details">
              <view class="course-info">
                <text class="course-name" v-if="item.package">{{ item.package }}</text>
                <text class="package">{{ item.courseName }}</text>
                <text class="time">{{ item.time }}</text>
                <text class="lesson" v-if="item.remaining">{{ item.remaining }}</text>
                <text class="price" v-if="item.price">{{ item.price }}</text>
                <!-- <text class="quantity">{{ item.quantity }}</text> -->
              </view>
              <view class="order-footer">
                <view />
                <view class="action-btn" @click="handleAction(item.actionType)">
                  <text>{{ item.actionText }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </block>

      <!-- 退款/售后内容 -->
      <block v-if="activeTab === 3">
        <view class="order-item" v-for="(item, index) in refundOrders" :key="index">
          <view class="order-header">
            <text class="venue">{{ item.venue }}</text>
            <text class="status">{{ item.status }}</text>
          </view>
          <view class="order-content">
            <image class="course-image"
              src="https://t11.baidu.com/it/u=444097960,250398539&fm=30&app=106&f=JPEG?w=640&h=480&s=D9A49B51665277CC129115C40300F0B0"
              mode="aspectFill" />
            <view class="order-details">

              <view style="display: flex;justify-content: space-between; align-items: center;">
                <view class="course-info">
                  <text class="package" v-if="item.package">{{ item.package }}</text>
                  <text class="course-name">{{ item.courseName }}</text>
                  <text class="time">{{ item.time }}</text>
                  <text class="lesson" v-if="item.remaining">{{ item.remaining }}</text>
                  <text class="quantity">{{ item.quantity }}</text>
                </view>
                <view>
                  <text class="price1" v-if="item.price"><text class="price">¥</text>{{ item.price }}</text>
                </view>
              </view>
              <!-- <view class="order-footer">
                <view />
                <view class="action-btn" @click="handleAction(item.actionType)">
                  <text>{{ item.actionText }}</text>
                </view>
              </view> -->
            </view>
          </view>
        </view>
      </block>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 0,
      tabs: [
        { name: '课程预约' },
        { name: '待完成' },
        { name: '已完成' },
        { name: '退款/售后' }
      ],
      // 示例数据 - 实际项目中应从接口获取
      courseOrders: [
        {
          venue: '上海市XXX马场',
          status: '待上课',
          courseName: '教练1-场地障碍',
          time: '2025/10/06 16:00-18:00',
          price: '000',
          quantity: '数量：1',
          actionText: '取消/退款',
          actionType: 'cancel'
        }
      ],
      pendingOrders: [
        {
          venue: '上海市XXX马场',
          status: '待上课',
          package: '课时包1',
          courseName: '教练1-场地障碍',
          time: '2025/10/06 16:00-18:00',
          remaining: '剩余课时：5节',
          price: '000',
          quantity: '数量：1',
          actionText: '再来一单',
          actionType: 'reorder'
        }
      ],
      completedOrders: [
        {
          venue: '上海市XXX马场',
          status: '待评价',
          package: '课时包1',
          courseName: '教练1-场地障碍',
          time: '2025/10/06 16:00-18:00',
          remaining: '剩余课时：5节',
          quantity: '数量：1',
          actionText: '评价',
          actionType: 'comment'
        }
      ],
      refundOrders: [
        {
          venue: '上海市XXX马场',
          status: '退款成功',
          courseName: '教练1-场地障碍',
          time: '2025/10/06 16:00-18:00',
          price: '000',
          quantity: '数量：1',
          actionText: '查看详情',
          actionType: 'detail'
        }
      ]
    }
  },
  onLoad(options) {
    const type = options.type
    console.log(type);
    if (type) {
      this.activeTab = Number(type)
    }
  },
  methods: {
    switchTab(index) {
      this.activeTab = index
    },
    handleAction(type) {
      // 处理不同按钮点击事件
      switch (type) {
        case 'cancel':
          uni.showModal({
            title: '提示',
            content: '确定要取消此订单吗？'
          })
          break
        case 'reorder':
          // 再来一单逻辑
          break
        case 'comment':
          // 评价逻辑
          break
        case 'detail':
          // 查看详情逻辑
          break
      }
    }
  }
}
</script>

<style>
/* 基础样式 */
page {
  background-color: #F5F5F5;
  padding: 0 24rpx;
  /* box-sizing: border-box; */
}

.container {
  display: flex;
  flex-direction: column;
}

/* 页面标题 */
.page-title {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 500;
  font-size: 40rpx;
  color: #0B1526;
  padding: 30rpx 0;
  text-align: center;
}

/* 标签导航 */
.tab-container {
  display: flex;
  justify-content: space-around;
  margin-bottom: 30rpx;
  background-color: #FFFFFF;
  border-radius: 12rpx;
  padding: 20rpx 0;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.tab-text {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 28rpx;
  color: #666666;
  padding-bottom: 10rpx;
}

.tab-item.active .tab-text {
  font-weight: 500;
  font-size: 32rpx;
  color: #0B1526;
}

.tab-line {
  width: 36rpx;
  height: 6rpx;
  background: #B7975E;
  border-radius: 3rpx;
}

/* 订单列表 */
.order-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.order-item {
  width: 660rpx;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 20rpx;
}

.order-content {
  display: flex;
  gap: 20rpx;
}

.course-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 8rpx;
}

.order-details {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.venue {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 600;
  font-size: 32rpx;
  color: #333333;
}

.status {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 24rpx;
  color: #666666;
}

.course-info {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
  margin-bottom: 20rpx;
}

.package {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 24rpx;
  color: #999999;
}

.course-name {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 28rpx;
  color: #666666;
}

.time,
.lesson {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 24rpx;
  color: #999999;
}

.price {
  font-weight: 600;
  font-size: 28rpx;
  color: #333333;
}

.price1 {
  font-weight: 600;
  font-size: 40rpx;
  color: #333333;
}

.order-footer {
  margin-top: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quantity {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 400;
  font-size: 24rpx;
  color: #999999;
}

.action-btn {
  width: 174rpx;
  height: 56rpx;
  border-radius: 146rpx;
  border: 1rpx solid #A0762C;
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-btn text {
  font-family: "OPPOSans", sans-serif;
  font-weight: 400;
  font-size: 24rpx;
  color: #8A5800;
}
</style>0.
