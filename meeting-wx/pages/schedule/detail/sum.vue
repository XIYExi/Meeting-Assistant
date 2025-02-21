<template>
	<view class="sum-container">
		
		<view class="flex-wrapper">
			<view class="agent-wrapper">
				<view class="robot-wrapper">
					<img src="@/static/images/robot.png" class="robot"/>
					<text class="robot-text">会议助理</text>
				</view>
				<view class="robot-meta">已接入恒脑大模型</view>
			</view>

			<view class="combo-wrapper">
				<view class="combo1-wrapper">
					<view class="combo1-left-wrapper">
						<img src="@/static/images/chat.png" class="chat"/>
						<text class="chat-text">讨论室</text>
					</view>
					<view class="combo1-right-wrapper">
						<text class="person">{{ number }}</text>人
					</view>
				</view>
				<view class="combo1-wrapper no-border">
					<view class="combo1-left-wrapper">
						<view class="combo1-left-wrapper">
							<img src="@/static/images/clip.png" class="chat"/>
						</view>
						<view class="combo1-right-wrapper">
							<view class="chat-text">会议纪要</view>
							<view class="chat-text-meta">AI总结、知识分享</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="flex-wrapper bottom-place">
			<view class="bottom-wrapper">
				<img src="@/static/images/ttts.png" class="bottom"/>
				<view>语音功能</view>
			</view>
			<view class="bottom-wrapper">
				<img src="@/static/images/sum.png" class="bottom"/>
				<view>智能总结</view>
			</view>
			<view class="bottom-wrapper">
				<img src="@/static/images/download.png" class="bottom"/>
				<view>附件下载</view>
			</view>
		</view>

	</view>
</template>

<script>
import {getUsersInRoom} from '@/api/live/index.js';
export default {
	props: {
    	number: {type: Number}
  	},
	data() {
		return {

		}
	},
	mounted() {
		
	},
	methods: {
		
	}
}
</script>

<style scoped>
	.bottom-wrapper {
		min-height: 60px;
		height: 60px;
		width: 30%;
		margin: 0 10px;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		border-radius: 12px;
		background-color: #e6f1fe76;
		box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
		font-size: 13px;
		font-weight: 300;
		color: #999;
	}
	.bottom {
		width: 26px;
		height: 26px;
		margin-bottom: 4px;
	}

	.sum-container{
		margin-top: 20px;
		margin-bottom: 30px;
		background-color: #ffffff;
		border-radius: 10px;
	}
	.flex-wrapper {
		display: flex;
		justify-content: space-between;
	}
	.bottom-place {
		margin-top: 6px;
	}
	.agent-wrapper {
		width: 50%;
		height: 120px;
		background-color: #0070D2;
		margin: 10px 5px;
		border-radius: 12px;
		box-shadow: rgba(0, 0, 0, 0.16) 0px 3px 6px, rgba(0, 0, 0, 0.23) 0px 3px 6px;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
	.robot-wrapper {
		display: flex;
		justify-self: center;
		align-items: center;
	}
	.robot {
		width: 50px;
		height: 50px;
		margin-right: 16px;
	}
	.robot-text {
		color: #fafafa;
		font-size: 16px;
		font-weight: 600;
	}
	.robot-meta {
		color: #fafafa;
		font-size: 12px;
		margin-top: 8px;
		font-weight: 200;
		opacity: 0.8;
		margin-left: -20px;
	}
	.combo-wrapper {
		width: 50%;
		height: 120px;
		border: 1px #0070D2 solid;
		margin: 10px 5px;
		border-radius: 12px;
	}
	.combo1-wrapper {
		height: 50%;
		/* 砍掉两边的margin */
		width: calc(100% - 20px);
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: 1px #0070D2 solid;
		margin-left: 10px;
		margin-right: 10px;
	}
	.combo1-left-wrapper {
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.combo1-left-wrapper-down {
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.combo1-right-wrapper {
		font-size: 12px;
		color: #333;
		font-weight: 300;
	}
	.no-border {
		border-bottom: none;
	}

	.chat {
		width: 34px;
		height: 34px;
		margin-right: 10px;
		padding-left: 5px;
	}
	.chat-text {
		color: #3f3f46;
		font-size: 15px;
		font-weight: 500;
	}
	.chat-text-meta {
		color: #999;
		font-size: 12px;
		font-weight: 300;
	}
	.person {
		color: rgb(130, 217, 48);
		font-weight: 600;
		font-size: 20px;
		margin-right: 4px;
	}
</style>