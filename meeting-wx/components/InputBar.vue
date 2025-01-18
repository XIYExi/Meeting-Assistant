<template>
	<view class="input-bar">
		<view class="text-btn">
			<view>
				<image class="talk" src="/static/logo.png" mode="aspectFill"></image>
			</view>
			<view class="input-wrapper">
				<input
					class="input-box"
					v-model="inputValue"
					placeholder="请输入内容"
					type="text"
					@keypress.enter="sendMsg"
				/>
			</view>
			<view class="send-btn" @click="sendMsg">
				<text class="btn-text">发送</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		modelValue: {
			type: String,
			default: '',
		},
	},
	data() {
		return {
			inputValue: this.modelValue,
		};
	},
	watch: {
		modelValue(val) {
			this.inputValue = val;
		},
		inputValue(val) {
			this.$emit('update:modelValue', val);
		},
	},
	methods: {
		sendMsg() {
			if (this.inputValue.trim() === '') return;
			this.$emit('send-msg', this.inputValue);
			this.inputValue = '';
		},
	},
};
</script>

<style>
/* InputBar 样式 */
.input-bar {
	width: 100%;
	background-color: white;
	position: fixed;
	bottom: 0;
}
.text-btn {
	display: flex;
	align-items: center;
	height: 6vh;
	width: 100%;
	background-color: #fff;
	border-top: 1px solid #ccc;
}
.talk {
	width: 7vw;
	height: 7vw;
	margin-right: 2vw;
	margin-left: 10px;
	border-radius: 50%;
}
.input-wrapper {
	flex: 1;
}
.input-box {
	height: 4.5vh;
	line-height: 4.5vh;
	padding: 0 3vw;
	border: 1px solid #ccc;
	border-radius: 10px;
}
.send-btn {
	margin-left: 2vw;
	margin-right: 10px;
	height: 4vh;
	width: 14vw;
	border: solid;
	border-radius: 7px;
	border-color: #4285f4;
	display: flex;
	justify-content: center;
	align-items: center;
}
.btn-text {
	font-size: 4vw;
	color: #4285f4;
}
</style>
