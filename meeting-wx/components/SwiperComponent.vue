<template>
	<view 
		class="swiper-wrap" 
		v-if="list && list.length > 0" 
		:style="{'--padding': padding / 2 + 'rpx', height: height + 'rpx'}"
	>
		<swiper 
			class="swiper-container" 
			:previous-margin="previousMargin+'rpx'" 
			:nextMargin="nextMargin+'rpx'" 
			:circular="circular" 
			:indicator-dots="false" 
			:autoplay="autoplay" 
			:interval="interval" 
			:duration="duration"
			@change="changeSwiper"
		>
			<swiper-item 
				class="swiper-item" 
				v-for="(item, index) in list" 
				:key="index"
				@click="swiperItemClick(item)"
			>
				<view class="swiper-element">
					<view class="inner-swiper-element">
						<image class="swiper-img" :src="item.img" mode="widthFix" />
						<view class="swiper-element-title">
							<text>{{item.title}}</text>
						</view>
						
						<view class="swiper-element-contnet">
							<text>{{item.content}}</text>
						</view>
					</view>
				</view>
				
			</swiper-item>
		</swiper>
	</view>
</template>
<script>
	export default {
		props: {
			// 上一张swiper露出多少rpx
			previousMargin: {
				type: Number,
				default: 40
			},
			// 下一张swiper露出多少rpx
			nextMargin: {
				type: Number,
				default: 40
			},
			// 二swiper中间间距
			padding: {
				type: Number,
				default: 20
			},
			// 是否无缝滚动
			circular: {
				type: Boolean,
				default: true
			},
			// banner数据
			// img/title
			list: {
				type: Array,
				default() {
					return null;
				}
			},
			// swiper数据
			indicatorType: {
				type: String,
				validator: (value) => {
					return ['circle', 'square', 'progress', 'number'].indexOf(value) !== -1;
				},
				default: 'circle'
			},
			duration: {
				type: Number,
				default: 1000
			},
			interval: {
				type: Number,
				default: 3000
			},
			// 是否自动播放
			autoplay: {
				type: Boolean,
				default: true
			},
			height: {
				type: Number,
				default: 260
			}
		},
		data() {
			return {
				currentIndex: 0
			}
		},
		methods: {
			changeSwiper(e) {
				this.currentIndex = e.detail.current;
				this.$emit('change', e);
			},
			swiperItemClick(item) {
				this.$emit('click', item);
			}
		}
		
	}
</script>
<style lang="scss" scoped>
	.swiper-wrap{
		width: 100%;
		position: relative;
	}
.swiper-container{
	width: 100%;
	height: 100%;
	.swiper-item{
		// width: 90%!important;
		padding:0 var(--padding);
		box-sizing: border-box;
		// width: 100%;
		position: relative;
	}
	.swiper-img{
		width: 30px;
		height: 30px;
	}
	.swiper-element {
		width: 100%;
		height: 100%;
		padding: 40px 30px;
	}
	.inner-swiper-element {
		background-color: white;
		width: 100%;
		height: 100%;
		padding: 20px;
		border-radius: 20px;
	}
	.swiper-title{
		font-size: 30rpx;
		line-height: 1.5em;
		padding: 0 10rpx;
		box-sizing: border-box;
		background-color: #ccc;
		color: #fff;
		position: absolute;
		bottom: 0;
		left: var(--padding);
		right: var(--padding);
		white-space: nowrap;
		text-overflow: ellipsis;
		overflow: hidden;
	}
	
	.swiper-element-title{
		margin-top: 12px;
		font-weight: 600;
		font-size: 15px;
	}
	.swiper-element-contnet {
		margin-top: 12px;
		font-weight: 300;
		font-size: 14px;
		color: #333;
		text-align: justify;
	}
}
.indicator-wrap{
	// width: 100%;
	height: 45rpx;
	position: absolute;
	bottom: 0;
	left: var(--padding);
	right: var(--padding);
	display: flex;
	flex-direction: row;
	justify-content: center;
	align-items: center;
	&.circle{
		.indicator-item{
			width: 20rpx;
			height: 20rpx;
			border-radius: 50%;
		}
	}
	&.square{
		.indicator-item{
			width: 30rpx;
			height: 15rpx;
		}
	}
	.indicator-item{
		overflow: hidden;
		margin-right: 10rpx;
		background-color: #999;
		&.active{
			background-color: #fff;
		}
		&:nth-last-of-type(1){
			margin-right: 0;
		}
	}
	.indicator-bar{
		width: 400rpx;
		height: 20rpx;
		border-radius: 10rpx;
		overflow: hidden;
		background-color: rgba(255,255,255,.2);
	}
	.indicator-bar-inner{
		width: 100%;
		height: 100%;
		border-radius: 10rpx;
		transition: transform .4s;
		overflow: hidden;
		transform-origin: left center;
		background-color: rgba(255,255,255,1);
	}
	.indicator-number{
		font-size: 32rpx;
		line-height: 45rpx;
		color: #fff;
	}
}
</style>
