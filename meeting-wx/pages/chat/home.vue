<template>
	<view class="chat-container">
		<view class="margin-top-xl">
			<view v-if="true" class="header">
				<view class="left-wrapper"></view>
				<view class="center-wrapper">
					<image src='/static/chat/moxing.png' mode="aspectFill" class="round"></image>
					<view class="agent-title"> AI会务助理 </view>
				</view>
				<view class="right-wrapper"></view>
			</view>
		</view>
		
		<view class="words-wrapper">
			<view class="time-words">{{timeWords}}</view>
			<view class="social-words">{{new SocialWelcomeWords().randomWords(type)}}</view>
		</view>
		
		<view v-if="isEarlierThanToday(lastOne.beginTime)" class="center-meeting-wrapper">
			<view class="now-words">当前没有会议</view>
			<view class="other-words">可以问我提问往期会议，或者聊点其他的~</view>
			<view class="link-words href-wrapper" @click="handleToChat">开始对话-></view>	
		</view>
		<view v-else class="center-meeting-wrapper">
			<view class="now-words">最近的下一场会议是</view>
			<view class="meeting-words">{{lastOne.title}}</view>
			<view class="other-words">您可以向我发起提问~</view>
			<view class="link-words href-wrapper" @click="handleToChat">开始对话-></view>	
		</view>
		
		<view class="bottom-swiper">
			<SwiperComponent
				:list="list"
				:duration="3000"
				:height="600"
			></SwiperComponent>
		</view>
		
	</view>
</template>

<script>
	import {getLastOne} from '@/api/meeting/meeting.js';
	import {SocialWelcomeWords} from "./SocialWelcomeWords";
	import SwiperComponent from '@/components/SwiperComponent.vue';
	export default {
		components: {
			SwiperComponent: SwiperComponent
		},
		mounted() {
			getLastOne().then(resp => {
				//console.log(resp)
				this.lastOne = resp.data;
			})
			
			const date = new Date();
			const hour = date.getHours();
			this.timeWords = '早上好';
			let type = 1;
			if (hour < 11){
				this.timeWords = '早上好';
				this.type = 1;
			}
			else if (hour >= 11 && hour < 14){
				this.timeWords = '中午好';
				this.type = 2;
			}
			else if (hour >= 14 && hour < 19){
				this.timeWords = '下午好';
				this.type = 3;
			}
			else{
				this.timeWords = '晚上好';
				this.type = 4;
			}
		},
		data() {
			return {
				timeWords: '',
				lastOne: {
					beginTime: null
				},
				type: 0,
				list: [
					{
						img: '/static/chat/moxing.png',
						title: '不做api调包侠',
						content: '基于 RAG 架构的多智能体协同助理，结合西湖论剑大会背景开发强业务智能体软件，选型 Langchain4j 手动接入恒脑大模型，接入 Milvus 和 embedding-text-v3，实现数据库操作，智能体接管软件。'
					},
					{
						img: '/static/chat/moxing.png',
						title: '思考展示 信息可靠',
						content: '人工实现恒脑大模型接入Langchain4j，通过语义分析和文本总结解析用户意图，通过工具路由分发到相关业务处理，每一步骤展示调用链路和中间信息，保障生成结果真实可靠'
					},
					{
						img: '/static/chat/moxing.png',
						title: '多功能接入 体验翻倍',
						content: '接入语言录入，文档上传、解析，图片上传解析等功能，结合恒脑大模型、阿里百炼平台等，赋能用户体验更上一层楼。'
					},
					{
						img: '/static/chat/moxing.png',
						title: '链路传输 数据安全',
						content: '业务后端实现sm2，sm4，ase等多种加密算法，实现数据传输安全加密，对发送消息进行xss拦截过滤，对敏感信息进行脱敏，保障数据传输安全可靠'
					},
				],
			}
		},
		methods: {
			SocialWelcomeWords,
			isEarlierThanToday(timeStr) {
			    // 将时间字符串转换为Date对象
			    const time = new Date(timeStr);
			    // 获取当前时间
			    const now = new Date();
			    // 清除当前时间的时分秒，只保留年月日
			    now.setHours(0, 0, 0, 0);
			    // 比较时间
			    return time < now;
			},
			handleToChat() {
				uni.navigateTo({
					url: '/pages/chat/chat'
				})
			}
		}
	}
</script>

<style scoped>
	.chat-container {
		width: 100%;
		height: 100vh;
		position: relative;
	}
	.bottom-swiper {
		position: absolute;
		width: 100%;
		height: 300px;
		bottom: 120px;
	}
	.href-wrapper {
		margin-top: 30px;
	}
	
	.center-meeting-wrapper {
		padding: 15px 20px 10px 20px;
	}
	.meeting-words {
		font-size: 20px;
		font-weight: 600;
		margin-bottom: 10px;
		margin-top: 5px;
	}
	.now-words {
		font-size: 23px;
		font-weight: 500;
	}
	.other-words {
		margin-top: 5px;
		font-size: 20px;
		font-size: 20px;
	}
	.link-words {
		font-size: 18px!important;
		color: #757474;
		font-weight: 300;
		text-decoration: underline;
	}
	
	.words-wrapper {
		padding: 20px;
	}
	.time-words{
		font-size: 32px;
		font-weight: 600;
	}
	.social-words {
		font-size: 20px;
		margin-top: 10px;
	}

	
	.round {
		border-radius: 0;
		background-color: #f4f5f6;
		width:30px;
		height: 30px
	}
	.header {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding: 10px 0;
		margin-top: 40px;
	}
	.agent-title {
		font-weight: 700;
		font-size: 26px;
		line-height: 40px;
		margin-left: 12px;
		color: #757474;
	}
	
	.left-wrapper {
		width: 15%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.center-wrapper {
		width: 70%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.right-wrapper {
		width: 15%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

</style>
