<template>
	<view>
		<view class="wp-step">
			<view class="item-step-1" v-for="(item,index) in stepList" :key="index">
				<view class="left">
					{{item.time[0]}} - {{ item.time[1] }}
				</view>
				<view class="mid">
					<image class="circ" src="https://tingqishop.oss-cn-shanghai.aliyuncs.com/cdn/images/other/act-2.png" mode="widthFix"></image>
					<view class="line" :class="['line-1']" />
				</view>
				<view class="right">
					<text :style="{'color':item.isTrue?'':'#333333'}">{{item.content}}</text>
                    <view v-if="item.meta" class="people">
                        <view v-for="(person, pIndex) in item.meta" :key="pIndex" class="person">
                            <text class="meta-1">{{ person[0] }}</text>
                            <text class="meta-2">{{ person[1] }}</text>
                        </view>
                    </view>
                   
				</view>
            
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
			};
		},
		props:{
			stepList:{
				type:Array,
				default(){
					return [
						{
                            time: '2020-6-24',
                            AllPrice: '50000000(已结清)',
                            returnMoney: '500000',
                            orderFenMoney:'500000',
                            isTrue: true
                        }
					]
				}
			}
		},
		methods:{
			clickChange(row,index){
				this.$emit('clickEvent',row)
			}
		}
	}
</script>

<style scoped>

.meta-1 {
    font-size: 12px;
}

.meta-2 {
    font-size: 12px;
}


.wp-step {
  width: 100%;
  background: #FFFFFF;
  display: flex;
  flex-direction: column;
}
.wp-step .item-step-1 {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
}
.wp-step .item-step-1 .left {
  flex: 0.6;
  padding-top: 12rpx;
  color: #333333;
  font-size: 24rpx;
}
.wp-step .item-step-1 .mid {
  width: 24rpx;
  padding: 0 30rpx 0 30rpx;
  position: relative;
  height: 170rpx;
}
.wp-step .item-step-1 .mid .circ {
  z-index: 10;
  top: 0rpx;
  position: relative;
  width: 24rpx!important;
  height: 24rpx!important;
  border-radius: 50%;
}
.wp-step .item-step-1 .mid .circl-1 {
  top: 10rpx;
  position: relative;
  width: 24rpx;
  height: 24rpx;
  background: #CCCCCC;
  border-radius: 50%;
}
.wp-step .item-step-1 .mid .circl-1::after {
  position: absolute;
  left: 50%;
  top: 50%;
  margin-top: -7rpx;
  margin-left: -7rpx;
  content: "";
  width: 14rpx;
  height: 14rpx;
  border-radius: 50%;
  background: #FFFFFF;
}
.wp-step .item-step-1 .mid .line {
  position: absolute;
  top: 30rpx;
  left: 50%;
  width: 2rpx;
  background: #FD532D;
}
.wp-step .item-step-1 .mid .line-1 {
  position: absolute;
  top: 30rpx;
  left: 50%;
  width: 2rpx;
  background: #CCCCCC;
}
.wp-step .item-step-1 .right {
  padding-top: 10rpx;
  flex: 1;
  display: flex;
  flex-direction: column;
}
.wp-step .item-step-1 .right text {
  font-size: 24rpx;
  color: #999999;
}
.wp-step .item-step-1 .right text:first-child {
  font-size: 32rpx;
  color: #FD532D;
}
.wp-step .item-step-1 .right text:first-child i {
  font-style: normal;
}
.wp-step .item-step-1 .right text:nth-child(2) {
  padding-top: 10rpx;
}
.wp-step .item-step-1:last-child .mid .line {
  background: #FFFFFF;
}

</style>
