<template>
	<view class="container">

		<view class="background_color_wrapper" />
		<view class="inner_container">
			<view class="ai_chat_wrapper">
				<view class="ai_wrapper">
					<view class="ai_wrapper_left">
						<view class="ai_logo" />
						<text class="ai_title">会务智能助理</text>
					</view>
					<view class="ai_wrapper_right" @click="gotoAgentPage">
						<text>立即使用</text>
					</view>
				</view>
				<view class="ai_tips">
					<text class="ai_tips_text">和智能助理进行对话，获得积分兑换好礼</text>
				</view>
			</view>


			<view class="router">
				<view class="router_top_wrapper">
					<view class="router_elem_wrapper">
						<text class="router_elem">关于大会</text>
					</view>
					<view class="router_elem_wrapper" @click="gotoNewsPage">
						<text class="router_elem">新闻中心 </text>
					</view>
					<view class="router_elem_wrapper" @click="handleNavigateToActivityPage">
						<text class="router_elem">活动板块</text>
					</view>
					<view class="router_elem_wrapper"  @click="handleNavigateToVideoPage">
						<text class="router_elem">参会指南</text>
					</view>
				</view>
			</view>
			<view class="tool">
				<view class="tool_elem_wrapper">
					<text class="tool_elem">语音记录</text>
				</view>
				<view class="tool_elem_wrapper">
					<text class="tool_elem">实时翻译</text>
				</view>
				<view class="tool_elem_wrapper" @click="handleNavigateToRankPage">
					<text class="tool_elem">实时排行</text>
				</view>
			</view>
			<view class="reservation">
				<view class="reservation_name_wrapper">
					<text class="reservation_block_title">我的预约</text>
					<text class="reservation_name">西湖论剑+网络安全大会</text>
				</view>
				<view class="reservation_wrapper">
					<view class="reservation_elem">
						<text class="reservation_meta">会议地点</text>
						<text class="reservation_meta_msg">安畅大厦2F</text>
					</view>
					<view class="reservation_elem">
						<text class="reservation_meta">距离开始</text>
						<text class="reservation_meta_msg">4天</text>
					</view>
					<view class="reservation_elem_last">
						<text class="reservation_meta">行程</text>
						<text class="reservation_meta_msg">查看行程</text>
					</view>
				</view>
			</view>
		</view>

		<view class="second_container">
			<view class="meeting_card_container">
				<text class="meeting_card_container_title">会议推荐</text>
			</view>
			<scroll-view scroll-x="true" class="scroll-view-x" scroll-left="0">
				<view v-for="(elem, index) in meetingList" :key="index" class="meeting_card">
					<view class="meeting_card_image_wrapper">
						<img :src="elem.url" class="meeting_image" />
					</view>
					<view class="meeting_card_msg_wrapper">
						<text class="meeting_card_title">{{ elem.title }}</text>
						<view class="meeting_card_meta_wrapper">
							<view class="meeting_card_meta">
								<view class="meeting_card_meta_icon_wrapper">
									<text class="meeting_card_meta_begintime">{{ elem.beginTime }}</text>
								</view>
								<view class="meeting_card_meta_icon_wrapper">
									<text class="meeting_card_meta_location">{{ elem.location.formattedAddress }}</text>
								</view>
							</view>
							<view class="meeting_card_details_btn">详细</view>
						</view>
					</view>
				</view>
			</scroll-view>


			<view class="meeting_card_container">
				<text class="meeting_card_container_title">最新消息</text>
			</view>
			<scroll-view scroll-x="true" class="scroller_view_guest_x" scroll-left="0">
				<view v-for="guest in guestList" :key="guest.id" class="guest_wrapper">
					<view class="guest_image_wrapper"></view>
					<view class="guest_meta_wrapper">
						<text class="guest_meta_name">{{ guest.name }}</text>
						<text class="guest_meta_title">{{ guest.title }}</text>
					</view>
				</view>
			</scroll-view>



		</view>
	</view>
</template>

<script>
import { getMeetingListOrderByAsc, getGuestListOrderByDate } from '@/api/meeting/meeting.js';
export default {
	onLoad: function () {
		getMeetingListOrderByAsc().then(resp => {
			//console.log(resp.rows);
			if (resp.code === 200) {
				this.meetingList = resp.rows;
			}
		});

		getGuestListOrderByDate().then(resp => {
			//console.log(resp)
			if (resp.code === 200) {
				this.guestList = resp.rows;
			}
		})

	},
	data() {
		return {
			meetingList: [],
			guestList: []
		}
	},
	methods: {
		gotoTestPage() {
			uni.navigateTo({
				url: '/pages/live/index'
			})
		},
		handleNavigateToActivityPage() {
			uni.navigateTo({
				url: '/pages/media/activity_sector'
			})
		},
		handleNavigateToRankPage() {
			uni.navigateTo({
				url: '/pages/rank/index'
			})
		},
		handleNavigateToVideoPage() {
			uni.navigateTo({
				url: '/pages/media/videoList'
			})
		},
		gotoNewsPage() {
			uni.navigateTo({
				url: '/pages/news/newsList'
			})
		},
		gotoAgentPage() {
			uni.navigateTo({
				url: '/pages/chat/chat'
			})
		}
	}
}
</script>

<style scoped>
.container {
	width: 100vw;
	position: relative;
	background-color: #f0f5ed;
	min-height: 100vh;
}

.background_color_wrapper {
	width: 100vw;
	height: 15vh;
	background-color: #ccff22;
	border-bottom-left-radius: 40%;
	border-bottom-right-radius: 40%;
	z-index: 0;
}

.inner_container {
	position: relative;
	margin-left: 10px;
	margin-right: 10px;
	margin-top: -8vh;
	z-index: 100;
}

.ai_chat_wrapper {
	border-radius: 15px;
	background-color: #ffffff;
	width: 100%;
	min-height: 120px;
}

.ai_wrapper {
	padding-top: 15px;
	margin-left: 10px;
	margin-right: 10px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.ai_wrapper_left {
	display: flex;
	align-items: center;
}

.ai_logo {
	width: 40px;
	height: 40px;
	background-color: #fcf3e7;
	border-radius: 50%;
	margin-right: 10px;
}

.ai_title {
	font-size: 15px;
	color: green;
	font-weight: 500;
}

.ai_wrapper_right {
	width: auto;
	height: auto;
	padding: 6px 12px;
	border-radius: 15px;
	background-color: #ccff22;
	color: black;
	font-weight: 500;
}

.ai_tips {
	margin-left: 10px;
	margin-right: 10px;
	margin-top: 15px;
	padding: 8px 10px;
	background-color: antiquewhite;
	border-radius: 5px;
}

.ai_tips_text {
	font-size: 12px;
	font-weight: 300;
}


.router {
	margin-top: 20px;
	margin-left: 5px;
	margin-right: 5px;
}

.router_top_wrapper {
	display: flex;
	flex-direction: row;
	align-items: center;
	justify-content: space-between;
}

.router_elem_wrapper {
	width: 25%;
	margin: 5px 5px;
	height: 120px;
	flex-direction: row;
	display: flex;
	align-items: center;
	align-items: center;
	text-align: center;
	border-radius: 8px;
	background-color: antiquewhite;
}

.router_elem {
	font-size: 15px;
	font-weight: 500;
	width: 100%;
}


.tool {
	display: flex;
	width: 100%;
	margin-left: 5px;
	margin-right: 5px;
}

.tool_elem_wrapper {
	width: 30%;
	margin: 5px 5px;
	height: 60px;
	flex-direction: row;
	display: flex;
	align-items: center;
	align-items: center;
	text-align: center;
	border-radius: 8px;
	background-color: antiquewhite;
}

.tool_elem {
	font-size: 15px;
	font-weight: 500;
	width: 100%;
}


.reservation {
	margin-top: 20px;
	background-color: white;
	border-radius: 12px;
	padding: 5px 10px;
}

.reservation_name_wrapper {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	margin-left: 5px;
	margin-right: 10px;
	margin-top: 10px;
}

.reservation_block_title {
	font-size: 16px;
	font-weight: 600;
}

.reservation_name {
	font-size: 14px;
	font-weight: 500;
}

.reservation_wrapper {
	margin-top: 15px;
	display: flex;
	flex-direction: row;

}

.reservation_elem {
	border-right: 1px solid gray;
	display: flex;
	flex-direction: column;
	width: 33%;
	justify-content: center;
	align-items: center;
	margin-bottom: 10px;
}

.reservation_elem_last {
	display: flex;
	flex-direction: column;
	width: 33%;
	justify-content: center;
	align-items: center;
}

.reservation_meta {
	lor: gray;
}

.reservation_meta_msg {
	font-size: 13px;
	margin-top: 5px;
	font-weight: 500;
}


.second_container {
	position: relative;
	background-color: white;
	padding-left: 20px;
	padding-right: 20px;
	z-index: 100;
	border-top-left-radius: 15px;
	border-top-right-radius: 15px;
}

.meeting_card_container {
	padding-top: 20px;
	margin-top: 20px;
}

.meeting_card_container_title {
	font-size: 18px;
	font-weight: 700;
}

.scroll-view-x {
	width: 100%;
	white-space: nowrap;
}

.meeting_card {
	display: inline-block;
	margin-right: 20px;
	width: 80% !important;
	height: 260px !important;
	max-height: 260px !important;
	background-color: #fff;
	border-bottom-left-radius: 15px;
	border-bottom-right-radius: 15px;
	margin-top: 15px;
	margin-bottom: 15px;
	position: relative;
}

.meeting_card_image_wrapper {
	width: 100%;
	height: 160px;
	/* background-color: #212121;
	  opacity: 0.5; */
}

.meeting_image {
	width: 100%;
	height: 160px;
	border-radius: 5px;
}

.meeting_card_msg_wrapper {
	padding: 10px;
}

.meeting_card_title {
	font-size: 15px;
	font-weight: 700;
}

.meeting_card_meta_wrapper {
	margin-top: 15px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.meeting_card_meta {
	display: flex;
	flex-direction: column;
}

.meeting_card_meta_icon_wrapper {
	display: flex;
}

.meeting_card_meta_begintime {
	font-size: 14px;
	font-weight: 500;
	margin-bottom: 5px;
	color: #212121;
}

.meeting_card_meta_location {
	font-size: 14px;
	color: #212121;
	font-weight: 500;
}

.meeting_card_details_btn {
	width: auto;
	height: auto;
	padding: 5px 12px;
	border-radius: 8px;
	background-color: chartreuse;
}


.scroller_view_guest_x {
	width: 100% !important;
	white-space: nowrap;
}

.guest_wrapper {
	display: inline-block;
	margin-right: 20px;
	width: 40% !important;
	height: 30px !important;
	position: relative;
	margin-top: 15px;
	margin-bottom: 15px;
}

.guest_image_wrapper {
	width: 100% !important;
	height: 160px !important;
	background-color: cadetblue;
	border-radius: 10px;
}

.guest_meta_wrapper {
	width: 100% !important;
	margin-top: 10px;
	justify-content: center;
	align-items: center;
	display: flex;
	flex-direction: column;
}

.guest_meta_name {
	font-size: 12px;
	text-align: center;
	color: black;
}

.guest_meta_title {
	font-size: 12px;
	padding-top: 5px;
	height: 30px;
	position: relative;
	text-align: center;
	white-space: pre-wrap !important;
	overflow: hidden;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
}
</style>
