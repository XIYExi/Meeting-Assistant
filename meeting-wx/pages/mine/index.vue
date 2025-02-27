<template>
  <view>
    <!-- 用户信息容器 -->
    <view class="user-container">
      <view class="gap-space"></view>
      <img class="avatar" :src="userInfo.avatar" @click="handleToAvatar" />
      <view class="name-container">
        <text class="name">{{ userInfo.nickname }}</text>
        <button class="daily-points" @click="handleSignIn">{{ canDaily ? '每日签到' : '已签到' }}</button>
      </view>
      <view class="stats">
        <view class="stat-item">
          <text class="stat-label">我的积分</text>
          <text class="stat-value">{{ wallet }}</text>
        </view>
        <view class="divider"></view>
        <view class="stat-item">
          <text class="stat-label">我的订阅</text>
          <text class="stat-value">{{ subscriptions }}</text>
        </view>
      </view>
    </view>
    
    <!-- 功能菜单容器 -->
    <view class="menu-container">
      <view class="menu-item" @click="handleToEditInfo">
        <text class="menu-text">个人资料</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>

      <view class="divider"></view>
      <view class="menu-item" @click="handleToPoints">
        <text class="menu-text">我的积分</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item" @click="handleToGoods">
        <text class="menu-text">积分兑换</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="menu-item" @click="handleToRules">
        <text class="menu-text">积分明细</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
    </view>

    <view class="menu-container">
      <view class="menu-item" >
        <text class="menu-text" style="color: red;">我的预约</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item" @click="handleToDownload">
        <text class="menu-text">海报下载</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item" @click="handleToFile">
        <text class="menu-text">文档下载</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item">
        <text class="menu-text" style="color: red;">大会须知</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item">
        <text class="menu-text" style="color: red;">语音工具</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item" @click="handleToAgent">
        <text class="menu-text">会议助理</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
    </view>

    <!-- 退出登录按钮 -->
    <view class="menu-container">
      <view class="menu-item">
        <text class="menu-text" style="color: red;">关于</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item">
        <text class="menu-text" style="color: red;">系统偏好</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item" @click="handleLogout">
        <text class="menu-text">退出登录</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
    </view>

  </view>
</template>

<script>
import { getUserWalletPoint, dailySignIn, submitTaskForPoint } from '@/api/point/index.js';

export default {
  mounted() {
    console.log(this.$store.state.user);
    getUserWalletPoint(this.$store.state.user.userId).then(resp => {
      this.wallet = resp.data.total;
    });

    dailySignIn(this.$store.state.user.userId).then(resp => {
      // console.log('can daily sign in?', resp)
      this.canDaily = resp.data;
    })
  },
  data() {
    return {
      userInfo: this.$store.state.user,
      subscriptions: 4,
      isSignedIn: false,
      signInText: '每日签到',
      // 积分总数
      wallet: 0,
      canDaily: false
    };
  },
  created() {

  },
  methods: {
    // 修改头像
    handleToAvatar() {
      this.$tab.navigateTo('/pages/mine/avatar/index')
    },
    // 跳转到个人中心
    handleToEditInfo() {
      this.$tab.navigateTo('/pages/mine/info/edit')
    },
    handleToDownload() {
      this.$tab.navigateTo('/pages/mine/poster/index')
    },
    handleToFile() {
      this.$tab.navigateTo('/pages/mine/file/index')
    },
    handleToSubscribe() {
      this.$tab.navigateTo('/pages/mine/subscribe/index')
    },
    handleToPoints() {
      this.$tab.navigateTo('/pages/mine/points/rules')
    },
    handleToGoods() {
      this.$tab.navigateTo('/pages/mine/points/goods')
    },
    handleToRules() {
      this.$tab.navigateTo('/pages/mine/points/details')
    },
    handleToAgent() {
      this.$tab.navigateTo('/pages/chat/chat')
    },
    handleLogout() {
      this.$modal.confirm('确定注销并退出系统吗？').then(() => {
        this.$store.dispatch('LogOut').then(() => {
          this.$tab.reLaunch('/pages/index')
        })
      })
    },
    handleSignIn() {
      if (!this.canDaily) {
        this.$modal.showToast('您已经签到了！');
        return;
      } else {
        this.canDaily = false;

        // 提交后端
        submitTaskForPoint(202, this.userInfo.userId);

        this.$modal.showToast('签到成功！');
      }
    },
  },
};
</script>


<style scope>
/* 用户信息容器 */
.user-container {
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 220px;
}

.gap-space {
  width: 100%px;
  height: 50px;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
}

.name-container {
  display: flex;
  align-items: center;
  /* 垂直居中 */
  justify-content: center;
  /* 水平居中 */
  margin-top: 10px;
  position: relative;
  /* 为子元素定位提供参考 */
}

.name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  text-align: center;
}

.daily-points {
  background-color: #8a2be2;
  color: #fff;
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 30px;
  border: none;
  height: 27px;
  width: 65px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  left: calc(50% + 80px);
}

.stats {
  display: flex;
  align-items: center;
  height: 40px;
  /* 固定高度 */
  margin-top: 15px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 20px;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

.stat-value {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.divider {
  width: 1px;
  height: 30px;
  background-color: #eee;
}

/* 功能菜单容器 */
.menu-container {
  background-color: #fff;
  margin-top: 10px;
  margin-bottom: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px 18px;
  height: 40px;
  /* 固定高度 */
}

.menu-text {
  font-size: 13px;
  color: #333;
}

.menu-icon {
  width: 15px;
  height: 15px;
}

.menu-container .divider {
  height: 1px;
  background-color: #eee;
  margin: 0 20px;
}

/* 退出登录按钮 */
.logout-button {
  width: 120px;
  background-color: #8a2be2;
  color: #fff;
  font-size: 16px;
  padding: 15px;
  border: none;
  margin-top: 60px;
  border-radius: 30px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
}

.logout-text {
  color: #fff;
  font-size: 16px;
}
</style>
