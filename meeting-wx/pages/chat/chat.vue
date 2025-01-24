<template>
	<view class="container">
		
		<view v-if="chatMsg.length === 0" class="empty">
			<view class="empty-wrapper"> 
				<text class="welcome-words">欢迎使用恒脑大模型智能Agent，请开始聊天吧！</text>
				<text class="askme-words">你可以问我：</text>
				<view class="empty-item-wrapper" @click="handleSendMsgInEmpty(emptys[0])">
					<uni-icons type="chat-filled" size="20"></uni-icons>
					<text class="item-words">{{emptys[0]}}</text>
				</view>
				<view class="empty-item-wrapper" @click="handleSendMsgInEmpty(emptys[1])">
					<uni-icons type="calendar" size="20"></uni-icons>
					<text class="item-words">{{emptys[1]}}</text>
				</view>
				<view class="empty-item-wrapper" @click="handleSendMsgInEmpty(emptys[2])">
					<uni-icons type="person-filled" size="20"></uni-icons>
					<text class="item-words">{{emptys[2]}}</text>
				</view>
			</view>
		</view>
		<view v-else>
			<ChatBox :chat-msg="chatMsg" @scroll-to-bottom="scrollToBottom" />
		</view>
		
		<InputBar v-model="value" @send-msg="handleSendMsg" />
	</view>
</template>

<script>
import ChatBox from '@/components/ChatBox';
import InputBar from '@/components/InputBar';
import {executeChatMessage, sendFlux} from '@/api/agent/agent.js';
import {generateUUID} from '@/utils/uuid.js';

export default {
	data() {
		return {
			uid: '',
			value: '',
			showStream: false,
			intCount: 0,
			path: "ws://127.0.0.1:8010/websocket",
			socket: "",
			emptys: [
				'关于西湖论剑安全大会',
				'告诉我会议日程安排',
				'告诉我出席会议的嘉宾'
			],
			chatMsg: [
				// {
				// 	position: 'left',
				// 	msg: "欢迎使用恒脑大模型智能Agent，请开始聊天吧！",
				// 	user: 'AI',
				// 	timestamp: Date.now(),
				// },
			],
		};
	},
    components: {
	    ChatBox, 
	    InputBar, 
    },
	created() {
		// this.getCode();
		// this.getCookie();
		this.uid = generateUUID();
	},
	mounted() {
		// this.initWebSocket();
		this.init();
	},
	destroyed() {
	    // 销毁监听
	    this.socket.onclose = this.close
	},
	methods: {	
		init: function () {
		    if (typeof (WebSocket) === "undefined") {
				alert("您的浏览器不支持socket")
		    } else {
		    // 实例化socket
		    this.socket = new WebSocket(this.path)
		    // 监听socket连接
		    this.socket.onopen = this.open
		    // 监听socket错误信息
		    this.socket.onerror = this.error
		    // 监听socket消息
		    this.socket.onmessage = this.getMessage
		    }
		},
		open: function () {
		    console.log("socket连接成功");
		    this.send(this.uid)
		},
		error: function () {
		    console.log("连接错误")
		},
		getMessage: function (msg) {
		    this.intCount=this.intCount+1;
		    setTimeout(()=>{
				const lastAIMsg = {
					position: 'left',
					msg: msg.data,
					user: 'AI',
					timestamp: Date.now(),
				};
				this.chatMsg[this.chatMsg.length - 1] = lastAIMsg;
				console.log(msg.data, this.chatMsg)
		    },this.intCount*100)
		    // this.AiMsg = msg.data;
			
		    
		},
		// 发送消息给被连接的服务端
		send: function (params) {
		    this.socket.send(params)
		},
		close: function () {
		    console.log("socket已经关闭")
		},
		
		
		sendMsg(msg) {
		      var that = this;
		      
		      this.showStream = true;
		      //that.messages.push({ sender: 'other', content: this.AiMsg });
				
		      var data = {
		        uid: this.uid,
		        text: msg
		      }
			  
		
		      sendFlux(data).then(response => {
		        this.showStream = false;
		        const lastAIMsg = {
		        	position: 'left',
		        	msg: '',
		        	user: 'AI',
		        	timestamp: Date.now(),
		        };
		        this.chatMsg.push(lastAIMsg);
		        that.loading = false;
		      });
		
		    },
		// 发送消息处理
		handleSendMsg(msg) {
			const currentTime = Date.now();
			this.chatMsg.push({
				position: 'right',
				msg,
				user: '用户',
				timestamp: currentTime,
			});
			// 模拟 AI 回复
			/*
			setTimeout(() => {
				this.chatMsg.push({
					position: 'left',
					msg: "这是AI的回复！",
					user: 'AI',
					timestamp: Date.now(),
				});
			}, 1000);
			*/
		   this.sendMsg(msg);
		},
		// 滚动到最新消息
		scrollToBottom() {
			// 可根据需要实现滚动到底部逻辑
		},
		handleSendMsgInEmpty(msg) {
			const currentTime = Date.now();
			this.chatMsg.push({
				position: 'right',
				msg,
				user: '用户',
				timestamp: currentTime,
			});
			executeChatMessage(msg).then(response => {
				console.log('resp', response);
				if(response.code === 200) {
					const {session} = response.data.body.data;
					const messages = session.messages;
					const assistantResp = messages[1];
					const resp = {
						position: 'left',
						msg: assistantResp.content,
						user: 'AI',
						timestamp: Date.now(),
					};
					this.chatMsg.push(resp);
				}
			})
			
		}
	},
};
</script>

<style>
.container {
	display: flex;
	flex-direction: column;
	height: 100vh;
	background-color: #f8f7f9;
	overflow: hidden;
}

.empty {
	width: 100%;
	height: 100%;
	display: flex;
	justify-content: center;
}
.empty-wrapper{
	margin-top: 180px;
	width: 260px;
	height: 200px;
	background-color: white;
	border-radius: 10px;
	padding: 15px 20px;
	display: flex;
	flex-direction: column;
}
.empty-item-wrapper {
	display: flex;
	width: 100%;
	height: 30px;
}
.welcome-words {
	font-size: 14px;
	font-weight: 500;
}
.askme-words {
	margin: 10px 0 15px 0;
	font-weight: 300;
	font-size: 13px;
}
.item-words {
	font-size: 13px;
	font-weight: 300;
	color: gray;
	margin-left: 4px;
}
</style>
