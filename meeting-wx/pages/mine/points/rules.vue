<template>
  <view class="container">
   <view class="gap-space">
    <view class="header">
      <image class="avatar" src="/static/avatar.png"></image>
      <view class="user-info">
        <text class="username">{{ username }}</text>
        <text class="points">我的积分 {{ points }}</text>
      </view>
      <button class="points-detail" @click="goToDetails">积分明细</button>
	  </view>
    </view>

    <!-- 积分规则标题 -->
    <view class="table-header">
      <text class="title">积分规则</text>
	  <view class="exchange-container">
      <text class="exchange" @click="handleToGoods">兑换积分</text>
	   <uni-icons type="right" size="12"></uni-icons>
	   </view>
    </view>

    <!-- 积分规则表格 -->
    <view class="points-rules">
      <view class="table">
        <view class="row" v-for="(item, index) in rules" :key="index">
          <text class="points-value">{{ item.points }}</text>
          <text class="description">{{ item.desc }}</text>
          <button :class="['action-btn', item.completed ? 'completed' : 'pending']">
            {{ item.completed ? item.doneText : item.actionText }}
          </button>
        </view>
      </view>
    </view>

    <!-- 积分规则说明 -->
    <view class="rules-section">
      <text class="rule-text">
        1. 会员积分可用于参与各种福利活动和兑换商品，具体使用见兑换详情。
      </text>
      <text class="rule-text">
        2. 积分获取方式包括但不限于：注册、签到、观看直播、邀请好友等。
      </text>
      <text class="rule-text">
        3. 积分无现金价值，仅适用于活动兑换，最终解释权归平台所有。
      </text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      username: "用户名",
      points: 500,
      rules: [
        { points: "+100积分", desc: "注册账号即送100积分", completed: true, actionText: "去注册", doneText: "已注册" },
        { points: "+100积分", desc: "签到即送100积分（每日仅限一次）", completed: true, actionText: "去签到", doneText: "已签到" },
        { points: "+1500积分", desc: "完善信息即送1500积分（仅限一次）", completed: false, actionText: "完善信息", doneText: "已完善" },
        { points: "+2000积分", desc: "观看直播5分钟以上 即送2000积分", completed: false, actionText: "观看直播", doneText: "已观看" },
        { points: "+200积分", desc: "微信/短信分享活动即送200积分（每日仅限一次）", completed: false, actionText: "邀请好友", doneText: "已邀请" }
      ]
    };
  },
  methods: {
    goToDetails() {
      this.$tab.navigateTo('/pages/mine/points/details')
    },
	handleToGoods(){
		this.$tab.navigateTo('/pages/mine/points/goods')
	},
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
  align-items:flex-end;
  justify-content: space-between;
  height:170px;
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
  justify-content: flex-start; 
  align-items: flex-start; 
}

.username {
  font-size: 18px;
  font-weight: bold;
}

.points {
  font-size: 14px;
  margin-top: 5px; 
}


.points-detail {
  font-size: 12px;
  height:30px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 5px 4px;
  border-radius: 14px;
  background-color: white;
  color: #8a2be2;
  border: none;
  margin-right:20px;
}


.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 15px 20px;
}

.title {
  font-size: 16px;
  font-weight: bold;
}

.exchange-container{
	display: flex;
	flex-direction:row;
	justify-content: center;
	align-items: center;
}

.exchange {
  font-size: 12px;
  color: gray;
}

/* 积分规则表格 */
.points-rules {
  background-color: white;
  margin: 10px 10px;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.table {
  display: flex;
  flex-direction: column;
  width: 100%;
}

/* 表格行样式 */
.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #ddd;
  padding: 10px 0;
  height: 50px; /* 表格高度自适应 */
}

/* 每列宽度固定 */
.points-value {
  font-size: 14px;
  font-weight: bold;
  width: 80px; /* 固定宽度 */
}

.description {
  flex: 1;
  font-size: 12px;
  color: #666;
  width: 200px; /* 固定宽度 */
}

.action-btn {
  padding: 4px 10px;
  font-size: 10px;
  border-radius: 12px;
  border: none;
  cursor: pointer;
  width: 60px; /* 固定宽度 */
  height:30px;
  margin-left:8px;
}

.pending {
  background-color: #8a2be2;
  color: white;
}

.completed {
  background-color: #ddd;
  color: #333;
}

/* 积分规则说明 */
.rules-section {
  margin: 10px;
  background-color: white;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.rule-text {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
  display: block;
}
</style>
