<template>
	<view class="item flex-row" :class="item.position">
		<image
			v-if="item.position === 'left'"
			src="/static/logo.png"
			class="face"
			mode="aspectFit"
		/>
		<image
			v-else
			src="/static/logo.png"
			class="face"
			mode="aspectFit"
		/>
		<view class="content-wrapper">
			<view class="username">{{ item.user }}</view>
			<text class="content flex-row" :class="item.position" user-select="true">
				{{ item.msg }}
			</text>
			<view class="tool-wrapper" v-if="item.position === 'left'">
				<uni-icons type="reload" size="20" class="icon-reload"></uni-icons>
				<uni-icons type="compose" size="20" class="icon-copy"></uni-icons>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		item: {
			type: Object,
			required: true,
		},
	},
};
</script>

<style>
	.face {
		width: 4vh;
		height: 4vh;
		border-radius: 5px;
	}
	
    .item {
		display: flex;
		margin-bottom: 10px;
		align-items: flex-start;
	}
	.item.left {
		flex-direction: row;
	}
	.item.right {
		flex-direction: row-reverse;
	}

	.tool-wrapper {
		display: flex;
		margin-top: 0px;
	}
	.icon-reload {
		margin-right: 5px;
		opacity: 0.6;
	}
	.icon-copy {
		opacity: 0.6;
	}


	.content-wrapper {
	    display: flex;
	    flex-direction: column;
		align-items: flex-start; /* 默认左对齐 */
		margin: 0;
	}
	.username {
	    font-size: 3vw;
	    color: #000;
	    margin-bottom: 0.5vh;
		order: -1;
	}
	.content {
		color: #000;
		font-size: 3.6vw;
		padding: 2.5vw 3vw;
		background-color: #fff;
		word-break: break-all;
		word-wrap: break-word;
		width: auto;
		max-width: 68vw;
		position: relative;
		margin-bottom: 10px;
	}
	.content.left{
		border-radius: 0 12px 12px 12px;
	}
	.content.right{
		border-radius: 12px 0px 12px 12px;
		background-color: #6a5bc2;
	}
	.item.left .content-wrapper {
	    margin-left: 3.5vw;
	}
	.item.right .content-wrapper {
	    margin-right: 3.5vw;
		align-items: flex-end;
	}
	.item.left .content-wrapper .username {
	    margin-left: 1vw;
	}
	.item.right .content-wrapper .username {
	    margin-right: 1vw;
	}
</style>
