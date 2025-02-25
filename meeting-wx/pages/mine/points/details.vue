<template>
  <view class="container">
    <view class="gap-space">
      <view class="header">
        <img class="avatar" :src="userInfo.avatar" />
        <view class="user-info">
          <text class="username">{{ userInfo.nickname }}</text>
          <text class="points">我的积分 {{ wallet }}</text>
        </view>
      </view>
    </view>

    <view class="activity-list">
      <view v-for="(item, index) in activities" :key="index" class="activity-item">
        <view class="circle">
          <span class="icon">{{ item.title }}</span>
        </view>
        <view class="activity-details">
          <view class="points-add">{{ item.point }}积分</view>
          <view class="time">{{ item.createTime }}</view>
        </view>
        <view class="tick-icon">
          <span >✔️</span>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getUserWalletPoint , getHistoryPointRecords} from '@/api/point/index.js';

export default {
  mounted() {
    getUserWalletPoint(this.$store.state.user.userId).then(resp => {
      this.wallet = resp.data.total;
    });

    getHistoryPointRecords(this.$store.state.user.userId).then(resp => {
      console.log(resp.data)
      this.activities = resp.data;
    });
  },
  data() {
    return {
      userInfo: this.$store.state.user,
      wallet: 0,
      activities: [
        { icon: '签到', points: 100, date: '2025-02-08 14:46:24', completed: true },
        { icon: '签到', points: 100, date: '2025-02-07 12:18:53', completed: true },
        { icon: '注册', points: 100, date: '2025-01-18 20:28:25', completed: true },
        { icon: '分享', points: 100, date: '2025-01-17 20:55:22', completed: true }
      ]
    };
  },
  methods: {
  }
};
</script>

<style scoped>
/* 页面整体背景 */
.container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 0;
}

/* 顶部紫色信息栏 */
.header {
  background-color: #8a2be2;
  color: white;
  padding: 25px 20px;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  height: 170px;
}


/* 头像 */
.avatar {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background-color: #fff;
  margin-right: 15px;
}

/* 用户信息 */
.user-info {
  display: flex;
  flex-direction: column;
  /* 设置为垂直排列 */
  justify-content: flex-start;
  /* 内容从顶部开始排列 */
  align-items: flex-start;
  /* 内容左对齐 */
}

.username {
  font-size: 18px;
  font-weight: bold;
}

.points {
  font-size: 14px;
  margin-top: 5px;
  /* 增加上边距，使 points 与 username 分开 */
}



.activity-list {
  display: flex;
  flex-direction: column;
  margin: 10px;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.circle {
  width: 80px;
  height: 24px;
  /* border-radius: 50%;
  background-color: #f0f0f0; */
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon {
  font-size: 16px;
  font-weight: bold;
}

.activity-details {
  margin-left: 15px;
  flex-grow: 1;
}

.points-add {
  font-size: 16px;
}

.time {
  font-size: 12px;
  color: #888;
}

.tick-icon {
  margin-left: 10px;
}
</style>
