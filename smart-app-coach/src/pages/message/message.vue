<template>
  <view class="container">
    <!-- 顶部标题 -->
    <view class="title">教练认证</view>

    <!-- 身份证上传部分 -->
    <view class="section">
      <view class="section-title">请上传身份证的正反面</view>
      <view style="display: flex; align-items: center;margin-top: 40rpx;margin-bottom: 20rpx;">
        <image style="width: 32rpx; height: 32rpx;margin-right: 20rpx;" src="/static/images/index/提示.png"
          mode="scaleToFill" />
        <view>
          <view class="section-title2">请上传符合要求的照片</view>
          <view class="section-title2">要求为清晰，无变形，近期正面免冠寸照（红、蓝、白底）</view>
        </view>
      </view>
      <view class="upload-cards">
        <view class="upload-card">
          <view class="card-left">
            <view class="card-main-text">头像面</view>
            <view class="card-sub-text">上传身份证头像面</view>
          </view>
          <view class="card-right">
            <image src="/static/images/index/身份证.png" mode="aspectFit" class="upload-image" />
          </view>
        </view>

        <view class="upload-card" style="margin-top: 20rpx;">
          <view class="card-left">
            <view class="card-main-text">国徽面</view>
            <view class="card-sub-text">上传身份证国徽面</view>
          </view>
          <view class="card-right">
            <image src="/static/images/index/国徽.png" mode="aspectFit" class="upload-image" />
          </view>
        </view>
      </view>
    </view>

    <!-- 教练证书上传部分 -->
    <view class="section1">
      <view class="section-title3">请上传教练证书</view>
      <view class="section-subtitle">您上传的认证信息将按照下面的顺序在主页依次展示</view>

      <!-- 标签导航 -->
      <view class="tabs">
        <view class="tab-item" :class="{ active: activeTab === 'coach' }" @click="switchTab('coach')">
          教练资格证
        </view>
        <view class="tab-item" :class="{ active: activeTab === 'rider' }" @click="switchTab('rider')">
          骑手资格证
        </view>
      </view>

      <!-- 教练资格证内容 -->
      <view class="tab-content" v-if="activeTab === 'coach'">
        <view class="cert-card">
          <view class="cert-item">
            <view class="cert-text">场地障碍</view>
            <view class="stars">
              <uni-rate :readonly="true" :value="2" :size="18" color="#ffd200" @change="onChange" />
            </view>
          </view>

          <view class="cert-item">
            <view class="cert-text">盛装舞步</view>
            <view class="stars">
              <uni-rate v-model="dressage" :size="18" color="#ffd200" @change="onChange" />
            </view>
          </view>

          <view class="cert-item">
            <view class="cert-text">三项赛对应级别</view>
            <view class="stars">
              <uni-rate v-model="eventing" :size="18" color="#ffd200" @change="onChange" />
            </view>
          </view>
        </view>
      </view>

      <!-- 骑手资格证内容 -->
      <view class="tab-content" v-if="activeTab === 'rider'">
        <view class="cert-card">
          <view class="cert-item">
            <view class="cert-text">场地障碍</view>
            <view class="level-select" @click="showLevelPicker('obstacle')">
              <text>{{ selectedLevels.obstacle || '请选择' }}</text>
              <uni-icons style="margin-left: 10rpx;" type="right" size="15" color="#424242" />
            </view>
          </view>

          <view class="cert-item">
            <view class="cert-text">盛装舞步</view>
            <view class="level-select" @click="showLevelPicker('dressage')">
              <text>{{ selectedLevels.dressage || '请选择' }}</text>
              <uni-icons style="margin-left: 10rpx;" type="right" size="15" color="#424242" />
            </view>
          </view>

          <view class="cert-item">
            <view class="cert-text">三项赛对应级别</view>
            <view class="level-select" @click="showLevelPicker('eventing')">
              <text>{{ selectedLevels.eventing || '请选择' }}</text>
              <uni-icons style="margin-left: 10rpx;" type="right" size="15" color="#424242" />
            </view>
          </view>
        </view>
      </view>
      <view class="picker-content" v-if="showPicker">
        <view class="level-item" v-for="(level, index) in levels" :key="index"
          :class="{ selected: tempSelectedLevel === level }" @click="confirmLevel(level)">
          {{ level }}
        </view>
      </view>
      <!-- 上传证书按钮 -->
      <view class="upload-cert-btn">
        <image src="/static/images/index/十字刀.png" mode="aspectFit" class="upload-icon" />
        <view class="upload-text">点击上传证书</view>
      </view>
    </view>

    <!-- 教练介绍部分 -->
    <view class="section">
      <view class="section-title4">教练介绍</view>

      <view class="intro-card">
        <textarea v-model="coachIntroduction" class="intro-input" placeholder="教练自我介绍"
          placeholder-class="placeholder-style" maxlength="-1">
        <!-- <view>
          <image src="/static/images/index/编辑.png" mode="scaleToFill" class="edit-icon" />
        </view> -->
        </textarea>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="submit-btn">确认提交</view>

    <!-- 级别选择下拉框 -->
    <!-- <view class="level-picker" v-if="showPicker"> -->
    <!-- <view class="picker-header">
        <text class="picker-cancel" @click="hideLevelPicker">取消</text>
        <text class="picker-title">选择级别</text>
        <text class="picker-confirm" @click="confirmLevel">确定</text>
      </view> -->
    <!-- </view> -->
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
      coachIntroduction: '',
      activeTab: 'coach', // 当前激活的标签
      obstacle: 3, // 场地障碍评分
      dressage: 4, // 盛装舞步评分
      eventing: 2, // 三项赛评分
      coachScores: {
        obstacle: 3, // 场地障碍评分
        dressage: 4, // 盛装舞步评分
        eventing: 2  // 三项赛评分
      },
      selectedLevels: {
        obstacle: '初一',
        dressage: '中一',
        eventing: '初二'
      },
      showPicker: false, // 是否显示级别选择器
      currentField: '', // 当前正在编辑的字段
      tempSelectedLevel: '', // 临时选中的级别
      levels: ['初一', '初二', '初三', '中一', '中二', '中三'] // 可选级别
    };
  },
  methods: {
    // 切换标签
    switchTab(tab) {
      this.activeTab = tab;
    },

    // 显示级别选择器
    showLevelPicker(field) {
      this.currentField = field;
      this.tempSelectedLevel = this.selectedLevels[field] || '';
      this.showPicker = true;
    },

    // 隐藏级别选择器
    hideLevelPicker() {
      this.showPicker = false;
    },

    // 确认选择的级别
    confirmLevel(level) {
      this.tempSelectedLevel = level
      if (this.currentField && this.tempSelectedLevel) {
        this.selectedLevels[this.currentField] = this.tempSelectedLevel;
      }
      this.hideLevelPicker();
    }
  }
};
</script>

<style>
.container {
  background: linear-gradient(to bottom, #000000, #606060, #fefefe, #fdfdfd);
  min-height: 100vh;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30rpx;
  padding: 60px 30rpx 180rpx 30rpx;
}

.edit-icon {
  width: 36rpx;
  height: 36rpx;
}

/* 标题样式 */
.title {
  font-weight: 700;
  font-size: 36rpx;
  color: #F5F6FA;
  text-align: center;
  margin-bottom: 40rpx;
}

/* 部分标题样式 */
.section {
  width: 100%;
  margin-bottom: 20rpx;
}

.section1 {
  width: 100%;
  margin-bottom: 20rpx;
  margin-top: -10rpx;
}

.section-title {
  font-weight: 600;
  font-size: 36rpx;
  color: #FFFFFF;
  text-align: left;
  margin-bottom: 20rpx;
}

.section-title2 {
  font-size: 24rpx;
  color: #FFFFFF;
  text-align: left;
  line-height: 34rpx;
  margin-bottom: 8rpx;
}

.section-title3 {
  font-weight: 600;
  font-size: 36rpx;
  color: #333333;
  text-align: left;
  margin-bottom: 20rpx;
}

.section-title4 {
  font-weight: 600;
  font-size: 36rpx;
  color: #333333;
  text-align: left;
  margin-bottom: 30rpx;
}

.section-subtitle {
  font-size: 24rpx;
  color: #666666;
  line-height: 34rpx;
  margin-bottom: 20rpx;
}

/* 上传卡片样式 */
.upload-card {
  height: 294rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 20rpx 0rpx rgba(255, 255, 255, 0.45);
  border-radius: 12rpx;
  display: flex;
  justify-content: space-between;
  padding: 30rpx;
  box-sizing: border-box;
}

.card-left {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.card-main-text {
  font-weight: 600;
  font-size: 36rpx;
  color: #1A1A1A;
  line-height: 50rpx;
  text-align: left;
  margin-bottom: 20rpx;
}

.card-sub-text {
  font-size: 24rpx;
  color: #BFBFBF;
  line-height: 34rpx;
}

.card-right {
  display: flex;
  align-items: center;
}

.upload-image {
  width: 370rpx;
  height: 240rpx;
}

/* 标签导航样式 */
.tabs {
  height: 80rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 12rpx;
  display: flex;
  margin-bottom: 30rpx;
}

.tab-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #999999;
  line-height: 40rpx;
  border-radius: 14rpx;
}

.tab-item.active {
  width: 348rpx;
  height: 72rpx;
  background: #F6F2EB;
  font-weight: 600;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #B7975E;
}

/* 证书卡片样式 */
.cert-card {
  position: relative;
  height: 332rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 16rpx;
  padding: 60rpx 30rpx;
  box-sizing: border-box;
  margin-bottom: 30rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.cert-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.cert-item:last-child {
  margin-bottom: 0;
}

.cert-text {
  font-size: 28rpx;
  color: #3D3D3D;
}

.stars {
  display: flex;
}

.icon-star {
  font-size: 36rpx;
  margin-left: 10rpx;
}

.level-select {
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #3D3D3D;
  line-height: 40rpx;
}

.icon-arrow-down {
  margin-left: 10rpx;
  font-size: 24rpx;
  color: #999999;
}

/* 上传证书按钮样式 */
.upload-cert-btn {
  height: 162rpx;
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.upload-icon {
  width: 52rpx;
  height: 52rpx;
  margin-bottom: 10rpx;
}

.upload-text {
  font-size: 28rpx;
  color: #999999;
  line-height: 40rpx;
}

.upload-size {
  font-size: 24rpx;
  color: #BFBFBF;
}

/* 教练介绍输入框样式 */
.intro-card {
  height: 222rpx;
  background: rgba(176, 176, 176, 0.12);
  border-radius: 10rpx;
  padding: 20rpx;
  box-sizing: border-box;
}

.intro-input {
  width: 100%;
  height: 100%;
  font-size: 28rpx;
  color: #333333;
}

.placeholder-style {
  margin-left: 24rpx;
  font-size: 24rpx;
  color: #999999;
}

/* 提交按钮样式 */
.submit-btn {
  width: 544rpx;
  height: 96rpx;
  background: #B7975E;
  border-radius: 186rpx;
  font-weight: 600;
  font-size: 32rpx;
  color: #FFFFFF;
  line-height: 96rpx;
  text-align: center;
  margin-top: 40rpx;
}

/* 级别选择器样式 */
.level-picker {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #FFFFFF;
  border-radius: 16rpx 16rpx 0 0;
  z-index: 999;
}

.picker-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 2rpx solid #D8D8D8;
}

.picker-cancel,
.picker-confirm {
  font-size: 28rpx;
  color: #666666;
}

.picker-title {
  font-size: 32rpx;
  color: #333333;
  font-weight: 600;
}

.picker-content {
  position: absolute;
  top: 83%;
  left: 55%;
  transform: translateX(-50%);
  width: 272rpx;
  height: 400rpx;
  padding: 20rpx 30rpx;
  background: #FFFFFF;
  border-radius: 16rpx 16rpx 16rpx 16rpx;
  border: 2rpx solid #D8D8D8;
}

.level-item {
  height: 52rpx;
  line-height: 52rpx;
  font-size: 28rpx;
  color: #3D3D3D;
  padding: 0 20rpx;
  margin-bottom: 20rpx;
  border-radius: 8rpx;
}

.level-item.selected {
  background: #FAF0DE;
  color: #6B4B12;
}
</style>