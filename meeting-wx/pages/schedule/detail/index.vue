<template>
  <view class="container">
    <!-- 模糊海报封面 -->
    <view class="poster">
      <image :src="event.url" alt="Event Poster" class="blurred-poster" />
    </view>

    <!-- 参会人员头像、人数和分享按钮 -->
    <view class="info-container">
      <view class="avatars">
        <image src="/static/images/banner/banner07.jpg" alt="Avatar 1" class="avatar" />
        <image src="/static/images/banner/banner07.jpg" alt="Avatar 2" class="avatar" />
        <image src="/static/images/banner/banner07.jpg" alt="Avatar 3" class="avatar" />
      </view>
      <view class="viewers-count">1.2K 人在观看</view>
      <button class="purple-button">分享</button>
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
          <text class="meta-msg">开始时间 {{ event.beginTime }}</text>
        </view>
        <view class="event-time">
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
            <text>{{ event.location }}</text>
          </view>
        </view>
      </view>

      <view class="map-icon">
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
            <text>{{ meetingTypeConstants[event.type] }}</text>
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
            <text>{{ types[eventStatus] }}</text>
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
            <text>{{ parts }} 人</text>
          </view>
        </view>
      </view>  
    </view>

    <view class="divider">
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
    <view class="selector-container">
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
  background-color: #f5f5f5;
  height: 100vh; /* 固定高度 */
  overflow-y: auto; /* 允许滚动 */
}
.meta-msg {
  font-weight: bold;
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
  color: orange;
}

.poster {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 250px; /* 固定高度 */
  overflow: hidden;
}
.flex-wrapper {
  display: flex;
  width: 50%;
  align-items: center;
}

.blurred-poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: blur(5px);
}

.info-container {
  position: relative;
  top: 200px; /* 与海报重叠 */
  background-color: #fff;
  padding: 15px;
  border-radius: 50px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  display: flex;
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

.purple-button {
	font-size: 15px;
  margin-left: auto;
  background-color: #8a2be2;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 30px;
  cursor: pointer;
  height:40px;
  display: flex;               /* 设置为 flexbox */
  align-items: center;         /* 竖直居中 */
  justify-content: center; 
}

.event-name {
  font-size: 24px;
  font-weight: bold;
  margin-top: 240px; /* 固定位置 */
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
  background-color: #e6e6fa;
  border-radius: 10px;
  margin-right: 10px;
}

.icon {
  width: 20px;
  height: 20px;
}

.details-text {
  font-size: 14px;
  color: #666;
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
import {getMeetingDetail, getSimpleMeetingPartUsers} from '@/api/meeting/meeting';
import {meetingTypeConstants} from '@/utils/constant';
import {calculateTimeDifference} from '@/utils/time';

export default {
  components: {
    Recommend,
    Agenda,
    Sum,
  },
  onLoad(option) {
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

      this.times = calculateTimeDifference(resp.data.beginTime);
    });


    getSimpleMeetingPartUsers(option.id).then(resp => {
      // console.log(resp.data)
      this.parts = resp.data.parts;
      this.avatars = resp.data.avatars;
    })
  },
  data() {
    return {
      // 会议详细信息
      event: {},
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
      }
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
  }

};
</script>