<template>
	<view>
		<view class="rec_title">会议推荐</view>

		<view class="event-list">
			<view class="event-container" @click="handleToDetail(event.id)" v-for="(event, index) in meetings" :key="index">
				<view class="event-card">
					<view class="event-poster">
						<img :src="event.url" class="event-image" loading="lazy"/>
            			<view class="image-fixed-tag">{{ event.type === 1 ? "线下" : "线上" }}</view>
					</view>
			  
					<view class="event-details">
						<view>
							<text class="begin-time"><img src="@/static/images/clock.png" class="clock-png" />{{ event.beginTime }}</text>
						</view>
						<text class="event-name">{{ event.title }}</text>
						<text class="event-location">{{ event.location.formattedAddress }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	onLoad(args) {
		this.$store.dispatch("MeetingList");
	},
	computed: {
		meetings() {
			const meetings = this.$store.state.meeting.meetingList;
			return meetings;
		}
	},
	data() {
	  return {
	    selectedDate: '',
	    selectedType: '',
	    selectedLocation: '',
	    dates: ['2024.4.19', '2024.5.10', '2024.6.15'],
	    types: ['技术会议', '学术会议', '行业峰会'],
	    locations: ['仅线下', '线上', '线上线下混合'],
	  };
	},
	methods: {
		handleToDetail(id){
			uni.navigateTo({
				url: `/pages/schedule/detail/index?id=${id}`
			});
		}
	},
}
</script>

<style scoped>

	.rec_title {
		font-weight: 600;
		font-size: 24px;
		margin-bottom: 15px;
		padding-left: 5px;
	}

	.event-list {
	  display: flex;
	  flex-direction: column;
	  gap: 15px; /* 会议容器之间的间距 */
	}
	
	.event-image {
		height: 150px;
		width: 100%;
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
		position: relative;
	  height: 150px;
	  background-color: #ddd;
	  border-radius: 10px;
	  margin-bottom: 10px;
	}
	
	.event-details {
	  display: flex;
	  flex-direction: column;
	}
	
	.begin-time {
		font-weight: 400;
		background-color: #E6F1FE;
		padding: 4px 8px;
		border-radius: 12px;;
		color: #006FEE;
		font-weight: 600;
		font-size: 12px;
		width: auto;
	}
	.clock-png {
		width: 12px;
		height: 12px;
		margin-right: 4px;
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
		margin-top: 6px;
		font-size: 16px;
		color: #333;
		margin-bottom: 5px;
		font-weight: 500;
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