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
    <view class="event-name">西湖论剑暨安恒信息年度新品发布日</view>

    <!-- 会议时间和地点 -->
    <view class="event-details">
      <view class="icon-container">
        <image src="path-to-date-icon.png" class="icon" />
      </view>
      <view class="details-text">
        <view class="event-date">14 December, 2021</view>
        <view class="event-time">Tuesday, 4:00PM - 8:00PM</view>
      </view>
    </view>
    <view class="event-details">
      <view class="icon-container">
        <image src="path-to-location-icon.png" class="icon" />
      </view>
      <view class="details-text">
        <view class="event-location">Gala Convention Center, 35 Guild Street London, UK</view>
      </view>
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
        <component :is="selectedComponent" />
      </view>
    </view>


    <!-- 底部固定按钮 -->
    <view class="bottom-blur">
      <button class="fixed-button">进入直播</button>
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

.poster {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 250px; /* 固定高度 */
  overflow: hidden;
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
  margin-top: 15px;
}

.icon-container {
  background-color: #e6e6fa;
  padding: 10px;
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

/* .selector-option.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #8a2be2; 
} */



.content {
  margin-top: 20px;
}

.bottom-blur {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 20px;
  background: linear-gradient(to top, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
  backdrop-filter: blur(10px);
  display: flex;
  justify-content: center;
}

.fixed-button {
  height: 50px;
  display: flex;              
  align-items: center;        
  justify-content: center;
  background-color: #8a2be2;
  color: #fff;
  border: none;
  padding: 12px 24px;
  border-radius: 35px;
  font-size: 16px;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>


<script>
import Recommend from '@/pages/schedule/detail/recommend.vue';
import Agenda from '@/pages/schedule/detail/agenda.vue';
import Sum from '@/pages/schedule/detail/sum.vue';
import {getMeetingDetail} from '@/api/meeting/meeting';

export default {
  components: {
    Recommend,
    Agenda,
    Sum,
  },
  onLoad(option) {
    // console.log(option.id)
    getMeetingDetail(option.id).then(resp => {
      // console.log(resp.data);
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
    })
  },
  data() {
    return {
      event: {},
      eventStatus: 'upcoming', // 可以是 'upcoming', 'ongoing', 'ended'
      selectedOption: '相关推荐',
      options: ['相关推荐', '大会议程'],
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
  },
};
</script>