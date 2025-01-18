<template>
	<view class="timestamp">{{ formattedTime }}</view>
</template>

<script>
export default {
	props: {
		timestamp: {
			type: Number,
			required: true,
		},
	},
	computed: {
		formattedTime() {
			const date = new Date(this.timestamp);
			const hours = date.getHours().toString().padStart(2, '0');
			const minutes = date.getMinutes().toString().padStart(2, '0');
			return `${hours}:${minutes}`;
		},
	},
};
</script>

<style>
.timestamp {
	font-size: 3vw;
	color: #aaa;
	text-align: center;
	width: 100%;
	margin: 10px 0;
}
</style>
