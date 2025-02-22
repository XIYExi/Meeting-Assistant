<template>
	<view class="container">
		<!-- 搜索框 -->
		<view class="search-box">
		  <view class="search-icon">
			<!-- 这里可以放放大镜的icon -->
			<uni-icons type="search" size="15"></uni-icons>
		  </view>
		  <input type="text" placeholder="搜索学术会议" class="search-input" @input="changeSearchText($event)"/>
		</view>

		<!-- 筛选容器 -->
		<view class="filter-container">
		  <select v-model="selectedDate" class="filter-dropdown">
        <option value="" disabled selected>日期</option>
        <option v-for="date in dates" :key="date" :value="date">{{ date }}</option>
		  </select>
		  <view class="divider"></view>
		  <select v-model="selectedType" class="filter-dropdown">
        <option value="" disabled selected>类型</option>
        <option v-for="type in eventMeetingTypes" :key="type" :value="type">{{ type }}</option>
		  </select>
		  <view class="divider"></view>
		  <select v-model="selectedLocation" class="filter-dropdown">
        <option value="" disabled selected>仅线下</option>
        <option v-for="location in eventTypes" :key="location" :value="location">{{ location }}</option>
		  </select>
		</view>

		<!-- 按钮 -->
    <!--
		<view class="button-container">
		  <button class="purple-button">分享</button>
		  <button class="white-button">参会指南</button>
		</view>
    -->

		<!-- 会议展示容器 -->
		<view class="event-list">
			<view class="event-container" @click="handleToDetail(event.id)" :key="index" v-for="(event, index) in meetings" >
				<view class="event-card">
					<view class="event-poster">
						<!-- 这里可以放会议海报的图片 -->
						<img :src="event.url" class="event-image" loading="lazy"/>
						<view class="image-fixed-tag">{{ event.meetType === 1 ? "线下" : "线上" }}</view>
					</view>
			  
					<view class="event-details">

            <MeetingTimeMetaComponent 
              :beginTime="event.beginTime" 
              :title="event.title"
              :location="event.location"
            />

            <view class="subscribed-button-wrapper">
              <view class="subscribed-button">
                <text class="event-subscribed">{{ event.subscribed ? '已订阅' : '未订阅' }}</text>
              </view>
            </view>
            
					</view>
				</view>
			</view>
		</view>
	</view>
</template>


<script>
import MeetingTimeMetaComponent from '@/components/MeetingTimeMetaComponent';
import { getBeginTimeList } from '@/api/meeting/meeting.js';
export default {
  onLoad(args) {
    this.$store.dispatch("MeetingList");
    // todo 把其他类型的搜索添加到vuex中

    getBeginTimeList().then(resp => {
      this.dates = resp.data;
    })
  },
  components: {
    MeetingTimeMetaComponent,
  },
  computed: {
    meetings() {
      const meetings = this.$store.state.meeting.meetingList;
      const searchText = this.$store.state.meeting.searchText;
      return meetings.filter(item => {
        // todo 添加其他搜索信息
        return item.title.toLowerCase().includes(searchText.toLowerCase());
      });
    },
  },
  data() {
    return {
      selectedDate: '',
      selectedType: '',
      selectedLocation: '',
      dates: ['2024.4.19', '2024.5.10', '2024.6.15'],
      eventMeetingTypes: ['技术会议', '学术会议', '行业峰会' ,'新品发布'],
      eventTypes: ['线上', '线下'],
    };
  },
  methods: {
  	handleToDetail(id){
  		// this.$tab.navigateTo('/pages/schedule/detail/index');
      
      uni.navigateTo({
        url: `/pages/schedule/detail/index?id=${id}`
      });
  	},
    changeSearchText(event) {
      this.$store.commit('CHANGE_MEETING_LIST', event.target.value);
    }
  },
  
};
</script>

<style scoped>
.container {
  padding: 20px;
  background-color: #f5f5f5;
}

.search-box {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 20px;
  padding: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-icon {
  margin-right: 10px;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 12px;
}

.filter-container {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.filter-dropdown {
  border: none;
  outline: none;
  font-size: 12px;
  color: #333;
  background-color: transparent;
  padding: 5px;
  width: 100px; /* 调整下拉框宽度 */
  margin-right: 10px;
  appearance: none; /* 移除默认样式 */
}

.filter-dropdown:disabled {
  color: #999; /* 默认选项的文字颜色 */
}

.divider {
  width: 1px;
  height: 20px;
  background-color: #ddd;
  margin: 0 10px;
}

.button-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.purple-button, .white-button {
  flex: 1;
  padding: 4px 6px;
  font-size: 13px;
  border-radius: 20px;
  cursor: pointer;
  text-align: center;
  margin: 0 5px;
}

.purple-button {
  background-color: #8a2be2;
  color: #fff;
  border: none;
}

.white-button {
  background-color: #fff;
  color: #8a2be2;
  border: 1px solid #8a2be2;
}

.event-list {
  display: flex;
  flex-direction: column;
  gap: 25px; /* 会议容器之间的间距 */
}

.event-container {
  background-color: #fff;
  border-radius: 8px;
  padding-bottom: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.event-card {
  display: flex;
  flex-direction: column;
}

.event-poster {
  height: 150px;
  position: relative;
  background-color: #ddd;
  border-radius: 8px;
  margin-bottom: 10px;
}
.event-image {
  height: 150px;
  width: 100%;
}

.event-details {
  display: flex;
  flex-direction: column;
  padding-top: 5px;
  padding-left: 10px;
  padding-right: 10px;
}

.image-fixed-tag {
  position: absolute;
  top: 8px;
  right: 8px;
  color: #fff;
  padding: 3px 6px;
  background-color: rgba(24, 240, 168, 0.7);
  font-size: 12px;
  border-radius: 5px;
}

.subscribed-button-wrapper {
  margin-top: 5px;
  display: flex;
  justify-content: flex-end;
}
.subscribed-button {
  width: 60px;
  height: 30px;
  background-color: #20222C;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  display: flex;

}
.event-subscribed {
  font-size: 12px;
  color: #fff;
  font-weight: bold;
}
</style>