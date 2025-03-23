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
		
		<!-- <InputBar v-model="value" @send-msg="handleSendMsg" /> -->
		<InputComponent v-model="value" @send-msg="handleSendMsg" />
	</view>
</template>

<script>
import ChatBox from '@/components/ChatBox';
import InputComponent from '@/components/InputComponent';
import { sendFlux } from '@/api/agent/agent.js';
import {generateUUID} from '@/utils/uuid.js';
import { getImConfig } from '@/api/live/index.js';
import { b64DecodeUnicode } from '@/utils/common.js';

export default {
	onLoad(params) {
		//  原本直播的锅，因为没有room所以和userId统一
		this.roomId = this.$store.state.user.userId;
		this.userId = this.$store.state.user.userId;
		this.avatarName = this.$store.state.user.nickname;
		getImConfig().then(resp => {
			const {data} = resp;
			this.url = data.wsImServerAddress;
			return data.wsImServerAddress;
		}).then(url => {
			// ws://127.0.0.1:8809/{token}/{userId}/{code}/{param}
			// code固定1002 ，用来登录的时候和netty握手进行校验！(1002是agent对话的登录code)
			// param即roomId(agent中和userId保持一致)
			this.connetImServer('ws://' + url + `/1/${this.$store.state.user.userId}/1002/${this.$store.state.user.userId}`);
		});
	},
	data() {
		return {
			uid: '',
			value: '',
			showStream: false,
			intCount: 0,
			path: "ws://192.168.31.76:8010/websocket",
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
	    InputComponent, 
    },
	created() {
		// this.getCode();
		// this.getCookie();
		this.uid = generateUUID();
	},
	methods: {	
		getMessage: function (msg) {    
			// console.log(this.removeStart(msg.data, "^intent"))
			
			const lastAIMsg = {
				intent: "",
				tool: "",
				position: 'left',
				msg: "",
				llm: "",
				user: 'AI',
				timestamp: Date.now(),
			};
			
			// 这个表示表示对话生成结束！
			if (msg === '&^over') {
				this.loading = false;
				this.scrollToBottom();
				return;
			}
			
			// 先判断是不是思考过程
			// 1. 第一个过来的肯定是intent（如果正常运行），这个时候最后一条数据肯定是用户提问，position为right
			// 2. 所以直接修改并且给lastAIMsg的intent赋值就行了
			if (msg.startsWith("&^intent")) {
				lastAIMsg.intent =  this.removeStart(msg, "&^intent");
			}
			
		
			if (this.chatMsg[this.chatMsg.length - 1].position === 'right') {
				// 3. push的时候，肯定是先给intent塞东西！
				this.chatMsg.push(lastAIMsg);
			}
			else {
				// 4. 第二个返回的（如果正常运行）一定是tool总计出来的东西
				if (msg.startsWith("&^tool")) {
					this.chatMsg[this.chatMsg.length - 1].tool = this.removeStart(msg, "&^tool");
				}
				else if (msg.startsWith("&^llm")) {
					// 5. 模型推理结果返回，这个一定是tool返回之后
					this.chatMsg[this.chatMsg.length - 1].llm = this.removeStart(msg, "&^llm");
				}
				else {
					this.chatMsg[this.chatMsg.length - 1].msg = this.removeStart(msg, "&");
				}
			}
			this.scrollToBottom();
		},
		// 发送消息给被连接的服务端
		send: function (params) {
		    this.socket.send(params)
		},
		close: function () {
		    console.log("socket已经关闭")
		},
		removeStart(str, target) {
			if (str.startsWith(target)) {
				return str.slice(target.length);
			}
			return str;
		},
		
		/* 弃用！！！原本通过http发送消息的串口，现在不用这个了 */
		sendMsg(msg) {
		      const that = this;
			  this.loading = true;
		      this.showStream = true;
		      //that.messages.push({ sender: 'other', content: this.AiMsg });
		      const data = {
		        uid: this.uid,
		        text: msg
		      }
			  
		      sendFlux(data).then(response => {
		        this.showStream = false;
		        const lastAIMsg = {
		        	position: 'left',
		        	msg: response.msg,
		        	user: 'AI',
		        	timestamp: Date.now(),
		        };
				if (this.chatMsg[this.chatMsg.length - 1].position === 'right') {
					this.chatMsg.push(lastAIMsg);
				}
				else {
					this.chatMsg[this.chatMsg.length - 1].msg = response.msg;
				}
				console.log('final', this.chatMsg)
		        that.loading = false;
				this.scrollToBottom();
		      });
		    },
		/* 通过ws发送消息 */
		handleMsgV2(msg) {
			const that = this;
			this.loading = true;
			this.showStream = true;
			//that.messages.push({ sender: 'other', content: this.AiMsg });
			const data = {
			  uid: this.uid,
			  text: msg
			}
			
			new Promise((resolve, reject) => {
				let msgBody = {"roomId": 200,"type":1, "content": this.input,  "avatarName": this.avatarName};
				// ImMsgBody 
				let jsonStr = {"userId": this.userId, "appId": 10002, "bizCode":5554, "data":JSON.stringify(msgBody)};
				let bodyStr = JSON.stringify(jsonStr);
				// ImMsg
				let reviewMsg = {"magic": 19231, "code": 1003, "len": bodyStr.length, "body": bodyStr};
				// console.log(JSON.stringify(reviewMsg));	
				try {
				    this.websocketSend(JSON.stringify(reviewMsg));
				    resolve();
				} catch (error) {
				    reject(error);
				}
			}).then(() => {
				// console.log("!!!执行了吗？")
				this.scrollToBottom();
			})
		},
		// 发送消息处理
		handleSendMsg(msg) {
			if (this.loading) {
				this.$modal.msg("请稍等...");
				return;
			}
			const currentTime = Date.now();
			this.chatMsg.push({
				position: 'right',
				msg,
				user: '用户',
				timestamp: currentTime,
			});
		   // this.sendMsg(msg);
		   this.handleMsgV2(msg);
		},
		// 滚动到最新消息
		scrollToBottom() {
			// 可根据需要实现滚动到底部逻辑
			uni.pageScrollTo({
				scrollTop: 2000000,    //滚动到页面的目标位置（单位px）
				duration: 0    //滚动动画的时长，默认300ms，单位 ms
			});
		},
		handleSendMsgInEmpty(msg) {
			this.loading = true;
			const currentTime = Date.now();
			this.chatMsg.push({
				position: 'right',
				msg,
				user: '用户',
				timestamp: currentTime,
			});
			const data = {
			  uid: this.uid,
			  text: msg
			}
			const that = this;
			sendFlux(data).then(response => {
				if(response.code === 200) {
					this.showStream = false;
					const lastAIMsg = {
						position: 'left',
						msg: response.msg,
						user: 'AI',
						timestamp: Date.now(),
					};
					if (this.chatMsg[this.chatMsg.length - 1].position === 'right') {
						this.chatMsg.push(lastAIMsg);
					}
					else {
						this.chatMsg[this.chatMsg.length - 1].msg = response.msg;
					}
					console.log('final', this.chatMsg)
					that.loading = false;
					this.scrollToBottom();
				}
			})
			
		},
		
		/* 全新版本 使用IM实时方案替代原本的ws */
		connetImServer: function (url) {
			let that = this;
			that.websock = new WebSocket(url);
			that.websock.onmessage = that.websocketOnMessage;
			that.websock.onopen = that.websocketOnOpen;
			that.websock.onerror = that.websocketOnError;
			that.websock.onclose = that.websocketClose;
			console.log("连接到ws服务器");
		},
		websocketOnOpen() {
			console.log('初始化建立连接');
		},
		websocketOnError() {
			console.log('出现异常');
		},
		websocketClose(e) {
			console.log('断开连接', e);
		},
		startHeartBeatJob: function() {
			console.log('首次登录成功');
			let that = this;
			//发送一个心跳包给到服务端
			let jsonStr = {"userId": 1, "appId": 10002};
			let bodyStr = JSON.stringify(jsonStr);
			let heartBeatJsonStr = {"magic": 19231, "code": 1004, "len": bodyStr.length, "body": bodyStr};
			setInterval(function () {
				that.websocketSend(JSON.stringify(heartBeatJsonStr));
			}, 3000);
		},
		websocketSend:function (data) {//数据发送
			this.websock.send(data);
		},
		sendAckCode: function(respData) {
			let jsonStr = {"userId": this.userId, "appId": 10002,"msgId":respData.msgId};
			let bodyStr = JSON.stringify(jsonStr);
			let ackMsgStr = {"magic": 19231, "code": 1005, "len": bodyStr.length, "body": bodyStr};
			this.websocketSend(JSON.stringify(ackMsgStr));
		},
		websocketOnMessage(e) {
			// console.log('收到消息', e)
			let wsData = JSON.parse(e.data);
			if(wsData.code == 1001) {
				// 心跳检测
				this.startHeartBeatJob();
			}
			else if(wsData.code == 1003) {
				//console.log('wsData: ', JSON.parse(b64DecodeUnicode(wsData.body)))
				let respData = JSON.parse(b64DecodeUnicode(wsData.body));
				//console.log('decode', respData)
				if(respData.bizCode==5554) {
					let respMsg = JSON.parse(respData.data);
					let sendMsg = {"content": respMsg.content, "avatarName": respMsg.avatarName};
					// console.log('receive msg: ', respMsg)
					// 把消息添加到chatList中，聊天室可以查看历史消息
					this.getMessage(respMsg)
					
				}
				this.sendAckCode(respData);
			}
		},
		
		
	}
};
</script>

<style>
.container {
	display: flex;
	flex-direction: column;
	height: auto;
	min-height: 100vh;
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
