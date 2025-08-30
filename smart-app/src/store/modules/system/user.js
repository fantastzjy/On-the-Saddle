/*
 * 登录用户
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:55:09
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import _ from 'lodash';
import { defineStore } from 'pinia';
import { USER_TOKEN } from '@/constants/local-storage-key-const';
import { loginApi } from '@/api/system/login-api';
import { smartSentry } from '@/lib/smart-sentry';
import {messageApi} from "@/api/support/message-api";

const defaultUserInfo = {
  // 基础登录信息
  token: '',
  role: '', // usr=会员, cc=教练
  isFirstLogin: false,
  
  // 详细用户信息（从/app/member/info/info获取）
  memberNo: '',
  actualName: '',
  nickname: '',
  phone: '',
  email: '',
  avatar: '',
  avatarUrl: '',
  gender: null,
  birthDate: null,
  clubCode: '',
  clubName: '',
  registrationStatus: 0,
  isMembership: 0,
  membershipStatus: null,
  membershipExpireDate: null,
  idCardNo: '',
  riderCertNo: '',
  createdByGuardian: 0,
  defaultCoachNo: '',
  defaultCoachName: '',
  defaultCourseLevel: '',
  defaultCourseLevelName: '',
  lastLoginTime: null,
  profileData: '',
  
  // 其他系统字段
  needUpdatePwdFlag: false,
  administratorFlag: false,
  lastLoginIp: '',
  lastLoginIpRegion: '',
  lastLoginUserAgent: '',
  unreadMessageCount: 0,
};

export const useUserStore = defineStore({
  id: 'userStore',
  state: () => ({
    ...defaultUserInfo,
  }),
  getters: {
    getToken(state) {
      return uni.getStorageSync(USER_TOKEN);
    },
    isLoggedIn(state) {
      return !!this.getToken;
    },
    isMember(state) {
      return state.role === 'usr';
    },
    isCoach(state) {
      return state.role === 'cc';
    },
  },

  actions: {
    logout() {
      this.token = null;
      this.setUserLoginInfo(defaultUserInfo);
      console.log(333,USER_TOKEN);
      uni.removeStorage(USER_TOKEN);
    },
    clearUserLoginInfo() {
      this.setUserLoginInfo(defaultUserInfo);
      console.log(444,USER_TOKEN);
      uni.removeStorage(USER_TOKEN);
    },
    async getLoginInfo() {
      let token = uni.getStorageSync(USER_TOKEN);
      if(!token){
        return;
      }
      try {
        let res = await loginApi.getLoginInfo();
        console.log('🔐 [Store] 获取会员信息成功:', res);
        
        // 使用会员信息设置Store状态
        if (res && res.data) {
          this.setDetailUserInfo(res.data);
        }
      } catch (error) {
        console.error('🔐 [Store] 获取会员信息失败:', error);
        // 获取失败时清除登录信息
        this.clearUserLoginInfo();
      }
    },
    
    // 查询未读消息数量
    async queryUnreadMessageCount() {
      try {
        let result = await messageApi.queryUnreadCount();
        this.unreadMessageCount = result.data;
      } catch (e) {
        smartSentry.captureError(e);
      }
    },
    
    // 设置基础登录信息
    setBasicLoginInfo(loginData) {
      this.token = loginData.token;
      this.role = loginData.role;
      this.isFirstLogin = loginData.isFirstLogin;
      
      uni.setStorageSync(USER_TOKEN, loginData.token);
    },
    
    // 设置详细用户信息
    setDetailUserInfo(detailData) {
      console.log('Store开始设置用户详细信息:', detailData);
      
      if (!detailData) {
        console.warn('setDetailUserInfo收到空数据');
        return;
      }
      
      // 设置用户基础信息
      this.memberNo = detailData.memberNo;
      this.actualName = detailData.actualName;
      this.nickname = detailData.actualName; // 使用真实姓名作为昵称显示
      this.phone = detailData.phone;
      this.email = detailData.email;
      this.avatar = detailData.avatarUrl;
      this.avatarUrl = detailData.avatarUrl;
      this.gender = detailData.gender;
      this.birthDate = detailData.birthDate;
      this.clubCode = detailData.clubCode;
      this.clubName = detailData.clubName;
      this.registrationStatus = detailData.registrationStatus;
      this.isMembership = detailData.isMembership;
      this.membershipStatus = detailData.membershipStatus;
      this.membershipExpireDate = detailData.membershipExpireDate;
      this.idCardNo = detailData.idCardNo;
      this.riderCertNo = detailData.riderCertNo;
      this.createdByGuardian = detailData.createdByGuardian;
      this.defaultCoachNo = detailData.defaultCoachNo;
      this.defaultCoachName = detailData.defaultCoachName;
      this.defaultCourseLevel = detailData.defaultCourseLevel;
      this.defaultCourseLevelName = detailData.defaultCourseLevelName;
      this.lastLoginTime = detailData.lastLoginTime;
      this.profileData = detailData.profileData;
      
      console.log('Store用户详细信息设置完成，当前store状态:', {
        memberNo: this.memberNo,
        actualName: this.actualName,
        phone: this.phone,
        clubCode: this.clubCode,
        clubName: this.clubName
      });
      
      // 获取未读消息数量
      if(this.token){
        this.queryUnreadMessageCount();
      }
    },

    //设置登录信息（兼容旧方法）
    setUserLoginInfo(data) {
      // 如果是基础登录信息
      if (data.token && data.role) {
        this.setBasicLoginInfo(data);
      }
      
      // 如果是详细用户信息
      if (data.memberNo || data.actualName) {
        this.setDetailUserInfo(data);
      }
      
      // 兼容旧的字段映射
      if (data.token) {
        this.token = data.token;
      }
      if (data.role) {
        this.role = data.role;
      }
      if (data.actualName) {
        this.actualName = data.actualName;
        this.nickname = data.actualName;
      }
      if (data.phone) {
        this.phone = data.phone;
      }
      if (data.avatarUrl) {
        this.avatar = data.avatarUrl;
        this.avatarUrl = data.avatarUrl;
      }
      
      uni.setStorageSync(USER_TOKEN, this.token);

      // 获取用户未读消息
      if(this.token){
        this.queryUnreadMessageCount();
      }
    },

    // 检查登录状态
    async checkLoginStatus() {
      const token = this.getToken;
      if (!token) {
        this.clearUserLoginInfo();
        return false;
      }

      try {
        // 验证token有效性 - 使用会员接口
        await loginApi.getLoginInfo();
        return true;
      } catch (error) {
        console.warn('🔐 [Store] Token验证失败:', error);
        // token失效，清除登录信息
        this.clearUserLoginInfo();
        return false;
      }
    },
    
    // 检查信息是否完善
    isProfileComplete() {
      return this.registrationStatus === 1 && 
             this.phone && this.phone.length > 0 && 
             this.actualName && this.actualName.length > 0;
    },
  },
});
