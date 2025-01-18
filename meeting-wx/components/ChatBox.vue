<template>
	<view class="chat-box">
		<view v-for="(item, index) in chatMsg" :key="index">
			<Timestamp v-if="showTime(index)" :timestamp="item.timestamp" />
			<MessageItem :item="item" />
		</view>
		<view id="bottom"></view>
	</view>
</template>

<script>
import MessageItem from '@/components/MessageItem.vue';
import Timestamp from '@/components/Timestamp.vue';

export default {
	props: {
		chatMsg: {
			type: Array,
			required: true,
		},
	},
	methods: {
		// 判断是否显示时间
		showTime(index) {
			if (index === 0) return true;
			const currentMsg = this.chatMsg[index];
			const prevMsg = this.chatMsg[index - 1];
			return currentMsg.timestamp - prevMsg.timestamp > 200000;
		},
	},
	components: {
		MessageItem,
		Timestamp,
	},
};
</script>

<style>
.chat-box {
	flex: 1;
	overflow-y: scroll;
	padding: 10px;
	padding-bottom: 6vh;
}
</style>
