<script>
import { useUserStore } from '@/store/modules/system/user';
export default {
  onLaunch: function () {
    useUserStore().getLoginInfo();
  },
  onShow: function () {
    console.log('App Show');
    this.requestVoicePermission();
  },
  onHide: function () {
    console.log('App Hide');
  },
  methods: {
    requestVoicePermission() {
      // #ifdef MP-WEIXIN
      const that = this;
      wx.getSetting({
        success(settingRes) {
          if (!settingRes.authSetting['scope.record']) {
            console.log('[权限检查] 语音录音权限未授权，主动申请');
            wx.authorize({
              scope: 'scope.record',
              success() {
                console.log('[权限授权] 语音录音权限授权成功');
              },
              fail() {
                console.log('[权限授权] 语音录音权限授权失败，将在使用时再次申请');
              }
            });
          } else {
            console.log('[权限检查] 语音录音权限已授权');
          }
        },
        fail(error) {
          console.error('[权限检查] 获取设置失败:', error);
        }
      });
      // #endif
    }
  }
};
</script>

<style lang="scss">
@import '@/uni_modules/uni-scss/index.scss';

/* 设置基准字体大小为16px */
body {
  font-size: 16px;
}
</style>
