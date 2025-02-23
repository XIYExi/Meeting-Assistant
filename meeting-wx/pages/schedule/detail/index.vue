<template>
  <view class="container">
    <!-- 模糊海报封面 -->
    <view class="poster">
      <image :src="event.url" alt="Event Poster" class="blurred-poster" />
      <view class="goto-room-bottom" @click="gotoLivingRoom">进入直播间</view>
    </view>

    <!-- 参会人员头像、人数和分享按钮 -->
    <view class="info-container" v-if="eventStatus === 'upcoming'">
      <view class="avatar-wrapper">
        <view class="avatars">
        <image v-for="(avatar, idx) in avatars" :key="idx" :src="avatar" class="avatar" alt="Avatar" />
        </view>
        <view class="viewers-count">等 {{ parts }} 人已经报名</view>
      </view>
     
      <view class="viewer-btn-wrapper">
        <button class="purple-button">分享</button>
        <button class="purple-button other-button-color">预约</button>
      </view>
    </view>
    <view class="info-container" v-else>
      <view class="tool-wrapper">
        <view class="logo-wrapper"><img src="@/static/images/robot.png" class="robot-mini"/></view>
        <view class="tool-text">会议助手</view>
      </view>
      <view class="tool-wrapper">
        <view class="logo-wrapper"><img src="@/static/images/news.png" class="robot-mini"/></view>
        <view class="tool-text">会议新闻</view>
      </view>
      <view class="tool-wrapper">
        <view class="logo-wrapper"><img src="@/static/images/activity.png" class="robot-mini"/></view>
        <view class="tool-text">活动展览</view>
      </view>
      <view class="tool-wrapper">
        <view class="logo-wrapper"><img src="@/static/images/more.png" class="robot-mini"/></view>
        <view class="tool-text">更多信息</view>
      </view>
    </view>

    <!-- 会议名字 -->
    <view class="event-name">
      <text>{{ event.title }}</text>
    </view>

    <!-- 开始时间 -->
    <view class="event-details">
      <view class="icon-container">
        <uni-icons type="calendar" size="20"></uni-icons>
      </view>
      <view class="details-text">
        <view class="event-date">
          <text class="meta-msg">开始时间 <text class="begin-time"><img src="@/static/images/clock.png" class="clock-png" />{{ event.beginTime }}</text></text>
        </view>
        <view class="event-meta">
          <text>会议持续{{ calculateTimeDifferenceInHours(event.beginTime, event.endTime) }}</text>
        </view>
      </view>
    </view>

    <!-- 会议地点 -->
    <view class="event-details location">
      <view class="event-details-location">
        <view class="icon-container">
          <uni-icons type="location" size="20"></uni-icons>
        </view>
        <view class="details-text">
          <view class="event-location">
            <view class="event-date">
              <text class="meta-msg">会议地点</text>
            </view>
            <text class="event-meta">{{ event.location.formattedAddress }}</text>
          </view>
        </view>
      </view>

      <view class="map-icon" @click="handleNavigateGeoMap">
        <uni-icons type="map" size="20"></uni-icons>
        <text class="map-text">导航</text>
      </view>
    </view>

    <!-- 会议类型 会议状态 参与人数 -->
    <view class="event-details last-detail-wrapper">
      <view class="flex-wrapper">
        <view class="icon-container">
          <uni-icons type="map-pin" size="20"></uni-icons>
        </view>
        <view class="details-text">
          <view class="event-location">
            <view class="event-date">
              <text class="meta-msg">会议类型</text>
            </view>
            <text class="event-meta">{{ meetingTypeConstants[event.type] }}</text>
          </view>
        </view>
      </view>

      <view class="flex-wrapper">
        <view class="icon-container">
          <uni-icons type="smallcircle" size="20"></uni-icons>
        </view>
        <view class="details-text">
          <view class="event-location">
            <view class="event-date">
              <text class="meta-msg">会议状态</text>
            </view>
            <text class="event-meta">{{ types[eventStatus] }}</text>
          </view>
        </view>
      </view>

      <view class="flex-wrapper">
        <view class="icon-container">
          <uni-icons type="staff-filled" size="20"></uni-icons>
        </view>
        <view class="details-text">
          <view class="event-location">
            <view class="event-date">
              <text class="meta-msg">参与人数</text>
            </view>
            <text class="event-meta">{{ parts }} 人</text>
          </view>
        </view>
      </view>  
    </view>

    <!--upcoming-->
    <view class="divider" v-if="eventStatus === 'upcoming'">
      <view class="center-wrapper">
        <view>
          <view>
            <text class="views-text">{{ event.views }}</text>
            人关注
          </view>
        </view>
        <view>
          <view v-if="times.length > 0">
            <view>距离会议开始还有</view>
            <uni-countdown :day="times[0]" :hour="times[1]" :minute="times[2]" :second="times[3]" color="#FFFFFF" background-color="#007AFF" />
          </view>
        </view>
      </view>
    </view>
    <!--ongoing-->
    <view class="ongoing-wrapper" v-if="eventStatus === 'ongoing'">
      <view >
        <Sum :number="userInRoom"/>
      </view>
    </view>


    <!-- 会议介绍 -->
    <view class="event-details">
      <view class="icon-container">
        <uni-icons type="paperclip" size="20"></uni-icons>
      </view>
      <view class="details-text">
        <view class="event-location">
          <view class="event-date">
            <text class="meta-msg">会议介绍</text>
          </view>
        </view>
      </view>
    </view>
    <view class="remark-wrapper">
      <text class="remark">{{ event.remark }}</text>
    </view>
    
    <!-- 选择器 -->
    <!-- <view class="selector-container">
      <view class="selector">
        <view
          v-for="option in options"
          :key="option"
          :class="{ active: selectedOption === option }"
          @click="selectOption(option)"
          class="selector-option"
        >
          {{ option }}
        </view>
      </view>
      <view class="content">
        <component :is="selectedComponent" :id="event.id"/>
      </view>
    </view> -->

    <view class="content">
        <Agenda :id="event.id"/>
    </view>

    <view class="content">
        <Recommend :id="event.id"/>
    </view>



    <!-- 底部固定按钮 -->
    <view class="bottom-blur">
      <view class="bottom-bar">
        <view class="bottom-bar-left-wrapper">
          <view class="bottom-bar-left-wrapper-1">
            <uni-icons type="list" size="24"></uni-icons>
            <view class="bottom-bar-left-wrapper-text">更多</view>
          </view>
          <view>
            <uni-icons type="chatbubble" size="24"></uni-icons>
            <view class="bottom-bar-left-wrapper-text">助理</view>
          </view>
          
        </view>
        <view>
          <button v-if="eventStatus==='upcoming'" class="fixed-button">立刻预约</button>
          <button v-if="eventStatus==='ongoing'" class="fixed-button">进入直播</button>
        </view>
      </view>
    </view>
  </view>
</template>


<style scoped>
.container {
  position: relative;
  padding: 20px;
  background-color: white;
  height: 100vh; /* 固定高度 */
  overflow-y: auto; /* 允许滚动 */
}
.meta-msg {
  font-weight: 500;
  font-size: 14px;
  color: #27272A;
}
.begin-time {
  font-weight: 400;
  margin-left: 10px;
  background-color: #E6F1FE;
  padding: 4px 8px;
  border-radius: 12px;;
  color: #006FEE;
  font-weight: 600;
  font-size: 12px;
}
.clock-png {
  width: 12px;
  height: 12px;
  margin-right: 4px;
}

.divider {
  padding: 20px 0;
  border-top: 0.5px rgb(134, 133, 133) solid;
  border-bottom: 0.5px rgb(134, 133, 133) solid;
}

.map-icon {
  margin-right: 20px;
  align-items: center;
  justify-content: center;
  display: flex;
  flex-direction: column;
}
.map-text {
  font-size: 10px;
  font-weight: 300;
}

.remark-wrapper {
  margin: 5px 0;
  padding: 0 10px;
}
.remark {
  text-align: justify;
  font-weight: 300;
  font-size: 13px;
}

.center-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-left: 10px;
  padding-right: 10px;
}
.views-text {
  font-size: 24px;
  font-weight: bold;
  color: #17C964
}

.poster {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 150px; /* 固定高度 */
  overflow: hidden;
}

.goto-room-bottom {
  position: absolute;
  width: 120px;
  height: 50px;
  border: 1px white solid;
  top: 40px;
  left: calc(50% - 60px);
  border-radius: 12px;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
}

.flex-wrapper {
  display: flex;
  width: 50%;
  align-items: center;
}

.blurred-poster {
  width: 100%;
  object-fit: cover;
  filter: blur(5px);
}

.info-container {
  position: relative;
  top: 100px; /* 与海报重叠 */
  background-color: #fff;
  padding: 12px 30px;
  border-radius: 50px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.tool-wrapper {
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: center;
}
.logo-wrapper {
  text-align: center;
  margin-bottom: 5px;
}
.robot-mini {
  width: 32px;
  height: 32px;
}
.tool-text {
  text-align: center;
  font-size: 12px;
  color: #333;
  font-weight: 300;
}



.avatar-wrapper {
  display: flex;
  justify-content:center;
  align-items: center;
}

.avatars {
  display: flex;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: -10px;
  border: 2px solid #fff;
}

.viewers-count {
  margin-left: 10px;
  font-size: 14px;
  color: #666;
}

.viewer-btn-wrapper {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.purple-button {
	font-size: 15px;
  margin-left: auto;
  background-color: #8a2be2;
  color: #fff;
  border: none;
  padding: 8px 12px;
  border-radius: 20px 0 0 20px;
  cursor: pointer;
  height:30px;
  display: flex;   
  align-items: center;
  justify-content: center;
  margin-right: 8px;
}

.other-button-color {
  border-radius: 0 20px 20px 0;
  margin-right: 0px;
}

.event-name {
  font-size: 24px;
  font-weight: bold;
  margin-top: 120px; /* 固定位置 */
  color: #333;
}

.event-details {
  display: flex;
  align-items: center;
  margin-top: 12px;
}
.event-details.location {
  justify-content: space-between;
}
.event-details-location {
  display: flex;
  align-items: center;
}
.last-detail-wrapper {
  margin-bottom: 15px;
}

.icon-container {
  background-color: #E6F1FE;
  box-shadow: rgba(0, 0, 0, 0.15) 1.95px 1.95px 2.6px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  margin-right: 10px;
}

.icon {
  width: 16px;
  height: 16px;
  opacity: 0.8;
}

.details-text {
  font-size: 14px;
  color: #666;
}

.event-meta {
  margin-top: 6px;
  color: #52525B;
  font-size: 12px;
}

.selector-container {
  margin-top: 20px;
}

.selector {
  display: flex;
  gap: 20px;
  border-radius: 50px;
  background-color: #fafafa; /* Light gray background for the whole selector */
  padding: 5px;
}

.selector-option {
  background-color: transparent;
  padding: 8px 0;
  font-size: 14px;
  cursor: pointer;
  color: #666; /* Dark gray for unselected */
  position: relative;
  outline: none; /* Remove the border when clicked */
  flex-grow: 1; /* Spread options evenly */
  text-align: center;
  border-radius: 25px;
  transition: color 0.3s, background-color 0.3s;
}

.selector-option.active {
  background-color: white; /* White background for selected option */
  color: #8a2be2; /* Purple color for selected option */
}

.content {
  margin-top: 20px;
  padding-bottom: 40px;
}

.bottom-blur {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 10px;
  background: linear-gradient(to top, rgba(255, 255, 255, .9), rgba(255, 255, 255, 0));
  backdrop-filter: blur(10px);
  display: flex;
  justify-content: center;
  height: 60px;
  max-height: 60px;
}

.bottom-bar {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 0 20px;
}
.bottom-bar-left-wrapper {
  display:flex;
}
.bottom-bar-left-wrapper-1 {
  margin-right: 20px;
}
.bottom-bar-left-wrapper-text {
  font-size: 12px;
  margin-top: 2px;
}

.fixed-button {
  height: 40px;
  width: 200px;
  display: flex;              
  align-items: center;        
  justify-content: center;
  background-color: #8a2be2;
  color: #fff;
  border: none;
  padding: 12px 24px;
  border-radius: 35px 35px 35px 0px;
  font-size: 16px;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>


<script>
import Recommend from '@/pages/schedule/detail/recommend.vue';
import Agenda from '@/pages/schedule/detail/agenda.vue';
import Sum from '@/pages/schedule/detail/sum.vue';
import {getMeetingDetail, getSimpleMeetingPartUsers,recordMeetingView} from '@/api/meeting/meeting';
import {startLiving, getUsersInRoom} from '@/api/live/index';
import {meetingTypeConstants} from '@/utils/constant';
import {calculateTimeDifference} from '@/utils/time';

export default {
  components: {
    Recommend,
    Agenda,
    Sum,
  },
  onLoad(option) {
     // 记录，当前会议关注度+1
     recordMeetingView(option.id);

    // 获取会议详细信息
    getMeetingDetail(option.id).then(resp => {
      console.log(resp.data);
      this.event = resp.data;
      if(this.event.status === 1) {
        this.eventStatus = 'upcoming';
      }
      else if(this.event.status === 2) {
        this.eventStatus = 'ongoing';
      }
      else {
        this.eventStatus = 'ended';
      }
      //todo 用于开发
      this.eventStatus = 'ongoing';

      this.times = calculateTimeDifference(resp.data.beginTime);
    });
    
    // ongoing下获得当前多少用户报名
    getSimpleMeetingPartUsers(option.id).then(resp => {
      // console.log(resp.data)
      this.parts = resp.data.parts;
      this.avatars = resp.data.avatars;
    })
	
    // 记录当前聊天室有多少用户
    getUsersInRoom(this.roomId, 10001).then(resp => {
      this.userInRoom = resp.data.length;
    });   
  },
  data() {
    return {
      // 会议详细信息
      event: {
        location: {formattedAddress: null}
      },
      // 会议状态 联动 event.type
      eventStatus: 'upcoming', // 可以是 'upcoming', 'ongoing', 'ended'
      // tabs选择
      selectedOption: '大会议程',
      // options
      options: ['大会议程', '相关推荐'],
      // 会议类型映射
      meetingTypeConstants,
      // 报名人数
      parts: 0,
      // 报名人数前三个人的头像
      avatars: [],
      // 会议距离开始时间，分别是[天，时，分，秒]
      times: [],
      // 会议状态 中文，对应eventStatus 用来渲染展示
      types: {
        'upcoming': '尚未开始',
        'ongoing': '进行中',
        'ended': '已经结束'
      },
      // todo 直播间id
      roomId: 200, 
	    userInRoom: 0,
    };
  },
  computed: {
    selectedComponent() {
      switch (this.selectedOption) {
        case '相关推荐':
          return 'Recommend';
        case '大会议程':
          return 'Agenda';
        case '大会纲要':
          return 'Sum';
        default:
          return 'Recommend';
      }
    },
  },
  created() {
    this.updateOptions();
  },
  methods: {
    selectOption(option) {
      this.selectedOption = option;
    },
    updateOptions() {
      if (this.eventStatus === 'ended') {
        this.options.push('大会纲要');
      }
    },
    calculateTimeDifferenceInHours(beginTimeStr, endTimeStr) {
      // 将时间字符串转换为 Date 对象
      const beginTime = new Date(beginTimeStr);
      const endTime = new Date(endTimeStr);
      // 检查是否开始时间晚于结束时间
      if (beginTime > endTime) {
          return "Meeting Time Error！";
      }
      // 计算时间差（单位为毫秒）
      const timeDifference = endTime - beginTime;
      // 将时间差转换为小时（1小时 = 3600000毫秒）
      const hoursDifference = Math.floor(timeDifference / 3600000);
      // 返回结果
      return `约${hoursDifference}小时`;
    },
    gotoLivingRoom() {
      uni.navigateTo({
				url: `/pages/live/index?id=${this.roomId}`
			});
    },
    handleNavigateGeoMap() {
      uni.navigateTo({
				url: `/pages/map/index?id=${this.event.location.id}`
			});
    }
  }

};
</script>