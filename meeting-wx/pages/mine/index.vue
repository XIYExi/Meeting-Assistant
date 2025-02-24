<template>
  <view>
    <!-- 用户信息容器 -->
    <view class="user-container">
      <view class="gap-space"></view>
      <image class="avatar" :src="userInfo.avatar" @click="handleToAvatar"></image>
      <view class="name-container">
        <text class="name">{{ userInfo.name }}</text>
        <button class="daily-points" @click="handleSignIn">{{ signInText }}</button>
      </view>
      <view class="stats">
        <view class="stat-item">
          <text class="stat-label">我的积分</text>
          <text class="stat-value">{{ userInfo.points }}</text>
        </view>
        <view class="divider"></view>
        <view class="stat-item">
          <text class="stat-label">我的订阅</text>
          <text class="stat-value">{{ userInfo.subscriptions }}</text>
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
      <view class="menu-item" @click="handleToDownload">
        <text class="menu-text">海报下载</text>
        <uni-icons type="right" class="menu-icon"></uni-icons>
      </view>
      <view class="divider"></view>
      <view class="menu-item" @click="handleToSubscribe">
        <text class="menu-text">我的订阅</text>
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
    </view>

    <!-- 退出登录按钮 -->
    <button class="logout-button" @click="handleLogout">
      <text class="logout-text">退出登录</text>
      <uni-icons type="arrow-right" size="18" color="white"></uni-icons>
    </button>

  </view>
</template>

<script>
export default {
  mounted() {
    
  },
  data() {
    return {
      userInfo: {
        avatar: "/static/images/profile.jpg",
        name: "David Silbia",
        points: 350,
        subscriptions: 3,
      },
      isSignedIn: false,
      signInText: '每日签到',
    };
  },
  created() {
    this.checkSignInStatus(); // 检查是否已签到
  },
  methods: {
    handleToAvatar() {
      this.$tab.navigateTo('/pages/mine/avatar/index')
    },
    handleToEditInfo() {
      this.$tab.navigateTo('/pages/mine/info/edit')
    },
    handleToDownload() {
      this.$tab.navigateTo('/pages/mine/poster/index')
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
    handleLogout() {
      this.$modal.confirm('确定注销并退出系统吗？').then(() => {
        this.$store.dispatch('LogOut').then(() => {
          this.$tab.reLaunch('/pages/index')
        })
      })
    },
    handleSignIn() {
      if (this.isSignedIn) {
        this.$modal.showToast('您已经签到了！');
      } else {
        this.isSignedIn = true;
        this.signInText = '已签到';
        this.$modal.showToast('签到成功！');
      }
    },
    checkSignInStatus() {
      const today = new Date().toISOString().split('T')[0];
      const savedDate = localStorage.getItem('lastSignInDate');

      if (savedDate === today) {
        this.isSignedIn = true;
        this.signInText = '已签到';
      }
    },

  },
};
</script>


<style>
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
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  height: 200px;
  /* 固定高度 */
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
