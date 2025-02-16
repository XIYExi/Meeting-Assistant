<template>
	<view>
		<view class="event-list">
			<view class="event-container" @click="handleToDetail" v-for="(event, index) in events" :key="index">
				<view class="event-card">
					<view class="event-poster">
						<!-- 这里可以放会议海报的图片 -->
					</view>
			  
					<view class="event-details">
						<text class="event-date">{{ event.date }}</text>
						<text class="event-time">{{ event.time }}</text>
						<text class="event-name">{{ event.name }}</text>
						<text class="event-location">{{ event.location }}</text>
						<text class="event-subscribed">{{ event.subscribed ? '已订阅' : '未订阅' }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
	
</template>

<script>
	export default {
	data() {
	  return {
	    selectedDate: '',
	    selectedType: '',
	    selectedLocation: '',
	    dates: ['2024.4.19', '2024.5.10', '2024.6.15'],
	    types: ['技术会议', '学术会议', '行业峰会'],
	    locations: ['仅线下', '线上', '线上线下混合'],
	    events: [
	      {
	        date: '2024.4.19',
	        time: 'SAT, APR 24 - 1:30 PM',
	        name: '西安智能电容器测试项目',
	        location: '53 Bush St · San Francisco, CA',
	        subscribed: true
	      },
	      {
	        date: '2024.5.10',
	        time: 'FRI, MAY 10 - 2:00 PM',
	        name: '全球人工智能峰会',
	        location: '线上',
	        subscribed: false
	      },
	      {
	        date: '2024.6.15',
	        time: 'SAT, JUN 15 - 10:00 AM',
	        name: '未来科技论坛',
	        location: '北京国际会议中心',
	        subscribed: true
	      }
	    ]
	  };
	},
	methods: {
		handleToDetail(){
			this.$tab.navigateTo('/pages/schedule/detail/index');
		}
	},
	}
</script>

<style scoped>
	.event-list {
	  display: flex;
	  flex-direction: column;
	  gap: 15px; /* 会议容器之间的间距 */
	}
	
	.event-container {
	  background-color: #fff;
	  border-radius: 10px;
	  padding: 15px;
	  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	}
	
	.event-card {
	  display: flex;
	  flex-direction: column;
	}
	
	.event-poster {
	  height: 150px;
	  background-color: #ddd;
	  border-radius: 10px;
	  margin-bottom: 10px;
	}
	
	.event-details {
	  display: flex;
	  flex-direction: column;
	}
	
	.event-date {
	  font-size: 12px;
	  color: #666;
	  margin-bottom: 5px;
	}
	
	.event-time {
	  font-size: 12px;
	  color: #666;
	  margin-bottom: 5px;
	}
	
	.event-name {
	  font-size: 16px;
	  color: #333;
	  margin-bottom: 5px;
	}
	
	.event-location {
	  font-size: 12px;
	  color: #666;
	  margin-bottom: 5px;
	}
	
	.event-subscribed {
	  font-size: 12px;
	  color: #8a2be2;
	}
</style>